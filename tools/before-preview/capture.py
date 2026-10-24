"""Capture all original-layout previews on an already running local emulator."""
import pathlib, subprocess, time, json, shlex
ROOT=pathlib.Path(__file__).resolve().parents[2]
ADB=pathlib.Path.home()/'Library/Android/sdk/platform-tools/adb'
OUT=ROOT/'artifacts/before'
OUT.mkdir(parents=True,exist_ok=True)
SCREENS=[
 ('02-beranda-preview','Beranda','home','Banner Firebase tidak dimuat.'),
 ('03-menu-samping-preview','Menu samping','drawer','Header bawaan APK; data akun tidak dimuat.'),
 ('04-pencarian-preview','Pencarian','search','Keadaan awal tanpa hasil pencarian.'),
]
for i,(key,title) in enumerate([('masjid','Masjid'),('wisata','Wisata'),('hotel','Penginapan'),('rumahsakit','Rumah Sakit'),('restoran','Restoran'),('supermarket','Supermarket'),('sekolah','Sekolah'),('transportasi','Transportasi'),('spbu','SPBU'),('apotek','Apotek'),('bidan','Bidan')],5):
 SCREENS.append((f'{i:02d}-{key}-preview',title,'category:'+key,'Dua data contoh dan foto bawaan APK; bukan data Firebase.'))
SCREENS += [
 ('16-input-lokasi-atas-preview','Input lokasi · atas','input','Form asli. Unggah dan pemilih lokasi tidak dijalankan.'),
 ('17-input-lokasi-bawah-preview','Input lokasi · bawah','input-bottom','Bagian bawah form asli.'),
 ('18-detail-tempat-preview','Detail tempat','detail','Data contoh; peta diganti area abu-abu berlabel offline.'),
 ('19-favorit-preview','Favorit','favorites','Isi daftar contoh; bukan favorit akun asli.'),
 ('20-profil-placeholder-preview','Layout profil belum selesai','profile-placeholder','Layout fragment yang masih placeholder; menu Profil aslinya hanya menampilkan pesan pengembangan.'),
 ('21-login-release-preview','Login · APK rilis','login','Layout dari app/release/app-release.apk; berbeda dengan cekpedia.apk di root.'),
 ('22-apotek-kosong-preview','Apotek · tanpa data','category:apotek','Keadaan layout kosong tanpa overlay loading Firebase.'),
]
def adb(*args,**kw): return subprocess.run([str(ADB),*args],check=True,timeout=30,**kw)
rows=[{'file':'01-login-apk-asli.png','title':'Login · APK asli dijalankan','kind':'APK ASLI','note':'Dijalankan langsung dari cekpedia.apk; belum login Google.'}]
for name,title,screen,note in SCREENS:
 adb('shell','am','force-stop','me.cekpedia.before',stdout=subprocess.DEVNULL)
 args=['shell','am','start','-n','me.cekpedia.before/.PreviewActivity','--es','screen',shlex.quote(screen),'--es','title',shlex.quote(title)]
 if name.startswith('22-'): args+=['--ez','empty','true']
 adb(*args,stdout=subprocess.DEVNULL)
 time.sleep(1.3)
 # UI dump provides evidence that the viewer did not fail or show another app.
 adb('shell','uiautomator','dump','/sdcard/before-ui.xml',stdout=subprocess.DEVNULL)
 ui=subprocess.check_output([str(ADB),'shell','cat','/sdcard/before-ui.xml']).decode()
 if 'Preview gagal:' in ui or 'package="me.cekpedia.before"' not in ui:
  raise RuntimeError('Screen failed: '+name+' '+ui[:800])
 with (OUT/(name+'.png')).open('wb') as f: adb('exec-out','screencap','-p',stdout=f)
 rows.append({'file':name+'.png','title':title,'kind':'PREVIEW LAYOUT ASLI','note':note})
 print('Captured '+name,flush=True)
(OUT/'screens.json').write_text(json.dumps(rows,ensure_ascii=False,indent=2))
print('Complete: '+str(len(rows))+' screenshots',flush=True)
