"""Build source layouts with the old APK's bundled libraries for offline visual QA.

This is a resource build, not a production build of the Firebase Java application.
Requires the official Apktool jar in /tmp/cekpedia-apktool.jar (or APKTOOL_JAR).
"""
from pathlib import Path
import os, shutil, subprocess, xml.etree.ElementTree as ET

ROOT=Path(__file__).resolve().parents[2]
STAGE=Path('/tmp/cekpedia-after-decoded')
JAVA=Path(os.environ.get('JAVA_HOME','/Library/Java/JavaVirtualMachines/jdk-11.0.16.jdk/Contents/Home'))
JAR=Path(os.environ.get('APKTOOL_JAR','/tmp/cekpedia-apktool.jar'))
OUT=ROOT/'tools/after-preview/build'
OUT.mkdir(parents=True,exist_ok=True)
def run(*args):subprocess.run([str(a) for a in args],check=True)
run(JAVA/'bin/java','-jar',JAR,'d','-f','-s','-p','/tmp/cekpedia-apktool-framework',ROOT/'app/release/app-release.apk','-o',STAGE)
# The old APK contains auto-versioned layout-v17/v21 copies. Remove those
# counterparts so Android renders the new source layouts on every API level.
for source in (ROOT/'app/src/main/res/layout').glob('*.xml'):
 for qualified in (STAGE/'res').glob('layout-*'):
  old=qualified/source.name
  if old.exists():old.unlink()
for folder in (ROOT/'app/src/main/res').iterdir():
 if not folder.is_dir():continue
 target=STAGE/'res'/folder.name;target.mkdir(exist_ok=True)
 for source in folder.iterdir():
  if not source.is_file():continue
  if folder.name=='drawable-v24' and source.name=='ic_launcher_foreground.xml':continue
  filename=source.name[:-4]+'.png' if source.suffix=='.PNG' else source.name
  dest=target/filename
  if folder.name.startswith('values'):
   incoming=ET.parse(source).getroot()
   def key(item):
    kind=item.get('type') if item.tag=='item' else item.tag
    if kind in ('string-array','integer-array'):kind='array'
    return kind,item.get('name')
   keys={key(item) for item in incoming}
   # Apktool distributes definitions across different XML files than source.
   for other in target.glob('*.xml'):
    if other.name=='public.xml':continue
    tree=ET.parse(other); changed=False
    for item in list(tree.getroot()):
     if key(item) in keys:tree.getroot().remove(item);changed=True
    if changed:tree.write(other,encoding='utf-8',xml_declaration=True)
   existing=ET.parse(dest).getroot() if dest.exists() else ET.Element('resources')
   for item in incoming:
    for previous in list(existing):
     if previous.tag==item.tag and previous.get('name')==item.get('name'):existing.remove(previous)
    existing.append(item)
   ET.ElementTree(existing).write(dest,encoding='utf-8',xml_declaration=True)
  else:shutil.copy2(source,dest)
run(JAVA/'bin/java','-jar',JAR,'b',STAGE,'-p','/tmp/cekpedia-apktool-framework','-o',OUT/'redesigned-resources.apk')
print('Built redesigned source resources:',OUT/'redesigned-resources.apk')
