"""Capture redesigned source layouts on the local Android emulator."""
import pathlib, subprocess, time, json, shlex, sys, re, xml.etree.ElementTree as ET
ROOT=pathlib.Path(__file__).resolve().parents[2]
ADB=pathlib.Path.home()/'Library/Android/sdk/platform-tools/adb'
OUT=ROOT/'artifacts/after'
OUT.mkdir(parents=True,exist_ok=True)
SCREENS=[('01-login','Masuk','login'),('02-beranda','Beranda','home'),('03-beranda-bawah','Beranda · kategori','home-bottom'),('04-menu-samping','Menu samping','drawer'),('05-pencarian','Pencarian','search')]
for i,(key,title) in enumerate([('masjid','Masjid'),('wisata','Wisata'),('hotel','Penginapan'),('rumahsakit','Rumah Sakit'),('restoran','Restoran'),('supermarket','Supermarket'),('sekolah','Sekolah'),('transportasi','Transportasi'),('spbu','SPBU'),('apotek','Apotek'),('bidan','Bidan')],6):
 SCREENS.append((f'{i:02d}-{key}',title,'category:'+key))
SCREENS += [('17-input-lokasi-atas','Usulkan tempat · atas','input'),('18-input-lokasi-bawah','Usulkan tempat · bawah','input-bottom'),('19-detail-tempat','Detail tempat','detail'),('20-favorit','Tempat favorit','favorites'),('21-profil','Profil','profile'),('22-kategori-kosong','Kategori · kosong','category:apotek'),('23-hasil-pencarian','Hasil pencarian','search'),('24-pencarian-kosong','Pencarian · tidak cocok','search')]
def adb(*args,**kw):return subprocess.run([str(ADB),*args],check=True,timeout=35,**kw)
rows=[]
selected=set(sys.argv[1:])
for name,title,screen in SCREENS:
 rows.append({"file":name+".png","title":title,"screen":screen,"kind":"AFTER · PREVIEW OFFLINE","note":"Layout dari source Android yang diperbarui; isi tempat adalah data contoh. Peta dan layanan eksternal tidak dijalankan."})
 if selected and name not in selected:continue
 adb('shell','am','force-stop','me.cekpedia.after',stdout=subprocess.DEVNULL)
 args=['shell','am','start','-n','me.cekpedia.after/.PreviewActivity','--es','screen',shlex.quote(screen),'--es','title',shlex.quote(title),'--ez','capture','true']
 if name.startswith('22-'):args+=['--ez','empty','true']
 if name.startswith('23-'):args+=['--es','query','FARMHOUSE']
 if name.startswith('24-'):args+=['--es','query','zzzzz']
 adb(*args,stdout=subprocess.DEVNULL);time.sleep(1.3)
 adb('shell','uiautomator','dump','/sdcard/after-ui.xml',stdout=subprocess.DEVNULL)
 ui=subprocess.check_output([str(ADB),'shell','cat','/sdcard/after-ui.xml']).decode()
 if 'Preview gagal:' in ui or 'package="me.cekpedia.after"' not in ui:raise RuntimeError('Failed: '+name+' '+ui[:800])
 if name.startswith('02-'):
  heading=next(node for node in ET.fromstring(ui).iter('node') if 'Mau ke mana' in node.get('text',''))
  x1,y1,x2,y2=map(int,re.findall(r'\d+',heading.get('bounds')))
  assert y2-y1>=180,'Home headline is clipped by initial scrolling'
 if name.startswith('03-'):
  assert all(label in ui for label in ['Input Lokasi','SPBU','Apotek','Bidan']),'Last category row is clipped'
 if name.startswith('23-'):assert 'Farmhouse Lembang' in ui and 'Trans Studio Bandung' not in ui
 if name.startswith('24-'):assert 'Belum ada tempat yang cocok' in ui
 with (OUT/(name+'.png')).open('wb') as f:adb('exec-out','screencap','-p',stdout=f)
 print('Captured '+name,flush=True)
(OUT/'screens.json').write_text(json.dumps(rows,ensure_ascii=False,indent=2))
print('Complete: '+str(len(rows))+' screenshots',flush=True)
