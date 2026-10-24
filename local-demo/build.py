"""Build an isolated offline Android demo with installed SDK tools, without Gradle/Firebase."""
import os
import pathlib
import shutil
import subprocess
import zipfile

ROOT = pathlib.Path(__file__).resolve().parent
SDK = pathlib.Path(os.environ.get("ANDROID_HOME", pathlib.Path.home() / "Library/Android/sdk"))
JAVA = pathlib.Path(os.environ.get("JAVA_HOME", "/Library/Java/JavaVirtualMachines/jdk-11.0.16.jdk/Contents/Home"))
TOOLS = SDK / "build-tools/30.0.3"
ANDROID = SDK / "platforms/android-30/android.jar"
OUT = ROOT / "build"
OUT.mkdir(exist_ok=True)
for name in ("classes", "gen", "dex"):
    (OUT / name).mkdir(exist_ok=True)
for asset in (ROOT.parent / "app/src/main/res/drawable").glob("ic_*.png"):
    shutil.copy2(asset, ROOT / "res/drawable" / asset.name)
env = dict(os.environ, JAVA_HOME=str(JAVA))
def run(*args):
    subprocess.run([str(a) for a in args], check=True, env=env, cwd=ROOT)
run(TOOLS / "aapt", "package", "-f", "-m", "-M", ROOT / "AndroidManifest.xml", "-S", ROOT / "res", "-I", ANDROID, "-J", OUT / "gen", "-F", OUT / "unsigned.apk")
run(JAVA / "bin/javac", "-source", "8", "-target", "8", "-bootclasspath", str(ANDROID) + os.pathsep + str(TOOLS / "core-lambda-stubs.jar"), "-d", OUT / "classes", *ROOT.glob("src/**/*.java"), *OUT.glob("gen/**/*.java"))
run(TOOLS / "d8", "--min-api", "21", "--lib", ANDROID, "--output", OUT / "dex", *OUT.glob("classes/**/*.class"))
with zipfile.ZipFile(OUT / "unsigned.apk", "a") as apk:
    apk.write(OUT / "dex/classes.dex", "classes.dex")
run(TOOLS / "zipalign", "-f", "4", OUT / "unsigned.apk", OUT / "aligned.apk")
if not (OUT / "demo.keystore").exists():
    run(JAVA / "bin/keytool", "-genkeypair", "-keystore", OUT / "demo.keystore", "-storepass", "android", "-keypass", "android", "-alias", "demo", "-dname", "CN=Local Demo", "-keyalg", "RSA", "-validity", "3650")
run(TOOLS / "apksigner", "sign", "--ks", OUT / "demo.keystore", "--ks-pass", "pass:android", "--out", OUT / "cekpedia-local.apk", OUT / "aligned.apk")
run(TOOLS / "apksigner", "verify", OUT / "cekpedia-local.apk")
print(OUT / "cekpedia-local.apk")
