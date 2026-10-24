"""Package an offline viewer using rebuilt source resources and bundled legacy classes.

No original activities, providers, login, or network permissions are registered.
The compile-only AppCompat stub is excluded from the packaged dex.
"""
import os, pathlib, subprocess, zipfile, hashlib, json, shutil
ROOT = pathlib.Path(__file__).resolve().parent
REPO = ROOT.parents[1]
SDK = pathlib.Path(os.environ.get('ANDROID_HOME', pathlib.Path.home() / 'Library/Android/sdk'))
JAVA = pathlib.Path(os.environ.get('JAVA_HOME', '/Library/Java/JavaVirtualMachines/jdk-11.0.16.jdk/Contents/Home'))
TOOLS = SDK / 'build-tools/30.0.3'
ANDROID = SDK / 'platforms/android-30/android.jar'
SOURCE = ROOT / 'build/redesigned-resources.apk'
OUT = ROOT / 'build'
for name in ['classes','dex']:
    if (OUT/name).exists(): shutil.rmtree(OUT/name)
    (OUT/name).mkdir(parents=True,exist_ok=True)
env=dict(os.environ,JAVA_HOME=str(JAVA))
def run(*args): subprocess.run([str(a) for a in args],check=True,env=env)
run(TOOLS/'aapt','package','-f','-M',ROOT/'AndroidManifest.xml','-I',ANDROID,'-F',OUT/'manifest.apk')
run(JAVA/'bin/javac','-source','8','-target','8','-bootclasspath',str(ANDROID)+os.pathsep+str(TOOLS/'core-lambda-stubs.jar'),'-d',OUT/'classes',*ROOT.glob('stubs/**/*.java'),*ROOT.glob('src/**/*.java'))
run(TOOLS/'d8','--min-api','21','--classpath',OUT/'classes','--lib',ANDROID,'--output',OUT/'dex',*OUT.glob('classes/me/**/*.class'))
with zipfile.ZipFile(SOURCE) as original, zipfile.ZipFile(OUT/'manifest.apk') as manifest, zipfile.ZipFile(OUT/'unsigned.apk','w',zipfile.ZIP_DEFLATED) as result:
    for entry in original.infolist():
        if entry.filename == 'AndroidManifest.xml' or entry.filename.startswith('META-INF/'): continue
        result.writestr(entry,original.read(entry.filename))
    result.writestr('AndroidManifest.xml',manifest.read('AndroidManifest.xml'))
    result.write(OUT/'dex/classes.dex','classes2.dex')
run(TOOLS/'zipalign','-f','4',OUT/'unsigned.apk',OUT/'aligned.apk')
if not (OUT/'preview.keystore').exists():
    run(JAVA/'bin/keytool','-genkeypair','-keystore',OUT/'preview.keystore','-storepass','android','-keypass','android','-alias','preview','-dname','CN=Offline Layout Preview','-keyalg','RSA','-validity','3650')
run(TOOLS/'apksigner','sign','--ks',OUT/'preview.keystore','--ks-pass','pass:android','--out',OUT/'cekpedia-after.apk',OUT/'aligned.apk')
run(TOOLS/'apksigner','verify',OUT/'cekpedia-after.apk')
with zipfile.ZipFile(SOURCE) as original, zipfile.ZipFile(OUT/'cekpedia-after.apk') as result:
    for entry in original.namelist():
        if entry.startswith('res/') or entry == 'resources.arsc': assert original.read(entry)==result.read(entry), entry
print('Verified: redesigned source resources preserved; preview APK built.')
(OUT/'provenance.json').write_text(json.dumps({'source':str(SOURCE.relative_to(REPO)),'sha256':hashlib.sha256(SOURCE.read_bytes()).hexdigest(),'resource_bytes':'matches source resource build','data':'offline fixtures; redesigned source layouts; original activities not run'},indent=2))
