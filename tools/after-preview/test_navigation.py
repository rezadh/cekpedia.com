"""Check offline preview navigation through actual UI taps, not direct screen launches."""
import pathlib, subprocess, time, re, json, xml.etree.ElementTree as ET
ROOT=pathlib.Path(__file__).resolve().parents[2]
ADB=pathlib.Path.home()/'Library/Android/sdk/platform-tools/adb'
def adb(*args):return subprocess.check_output([str(ADB),*args],timeout=30).decode()
def ui():
 adb('shell','uiautomator','dump','/sdcard/after-nav.xml')
 return ET.fromstring(adb('shell','cat','/sdcard/after-nav.xml'))
def tap_text(value):
 for node in ui().iter('node'):
  if node.get('text')==value:
   x1,y1,x2,y2=map(int,re.findall(r'\d+',node.get('bounds')))
   adb('shell','input','tap',str((x1+x2)//2),str((y1+y2)//2));time.sleep(.5);return
 raise AssertionError('Text not visible: '+value)
def assert_text(value):
 assert any(value in n.get('text','') for n in ui().iter('node')),value
checks=[]
adb('shell','am','force-stop','me.cekpedia.after')
adb('shell','am','start','-n','me.cekpedia.after/.PreviewActivity','--es','screen','home')
time.sleep(.8)
adb('shell','input','swipe','500','1800','500','650','500')
tap_text('Apotek');assert_text('Kimia Farma Buah Batu');checks.append('home category tap → Apotek')
tap_text('Kimia Farma Buah Batu');assert_text('Petunjuk arah');assert_text('Kimia Farma Buah Batu');checks.append('place card → matching detail')
adb('shell','input','keyevent','4');time.sleep(.7);adb('shell','input','keyevent','4');time.sleep(.7)
for node in ui().iter('node'):
 if 'navigation drawer' in node.get('content-desc','').lower():
  x1,y1,x2,y2=map(int,re.findall(r'\d+',node.get('bounds')))
  adb('shell','input','tap',str((x1+x2)//2),str((y1+y2)//2));break
else:raise AssertionError('Drawer affordance not found')
tap_text('Profil');assert_text('Perjalananmu');checks.append('drawer → profile')
tap_text('Lihat tempat favorit');assert_text('Ingin kembali');checks.append('profile → favorite places')
adb('shell','am','force-stop','me.cekpedia.after')
adb('shell','am','start','-n','me.cekpedia.after/.PreviewActivity','--es','screen','home')
(ROOT/'artifacts/after/navigation-check.json').write_text(json.dumps({'status':'passed','checks':checks,'scope':'offline preview; not Firebase end-to-end'},indent=2))
print('PASS: '+', '.join(checks))
