"""Create a self-contained gallery and ZIP from the verified captures."""
import json, pathlib, html, zipfile, shutil
ROOT=pathlib.Path(__file__).resolve().parents[2]
OUT=ROOT/'artifacts/before'
rows=json.loads((OUT/'screens.json').read_text())
cards='\n'.join(f'<article><a href="{html.escape(r["file"])}" target="_blank"><img loading="lazy" src="{html.escape(r["file"])}" alt="{html.escape(r["title"])}"></a><span>{html.escape(r["kind"])}</span><h2>{html.escape(r["title"])}</h2><p>{html.escape(r["note"])}</p><a href="{html.escape(r["file"])}" download>Unduh PNG asli</a></article>' for r in rows)
(OUT/'index.html').write_text('''<!doctype html><html lang="id"><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Cekpedia · Before</title><style>
*{box-sizing:border-box}body{margin:0;background:#f2f3f5;color:#17212d;font:16px/1.6 system-ui,sans-serif}header{max-width:1200px;margin:auto;padding:48px 24px 24px}h1{font-size:42px;line-height:1.1;margin:12px 0}header p{max-width:850px;color:#506070}.tag{font-size:12px;letter-spacing:2px}main{max-width:1200px;margin:auto;padding:24px;display:grid;grid-template-columns:repeat(auto-fit,minmax(240px,1fr));gap:24px}article{background:white;border:1px solid #dfe4ea;border-radius:12px;overflow:hidden;padding:16px}article img{display:block;width:100%;height:auto;border:1px solid #e0e5eb;border-radius:5px}article span{display:block;font-size:10px;font-weight:700;letter-spacing:1px;color:#536881;margin-top:18px}h2{font-size:18px;margin:4px 0}article p{font-size:13px;color:#506070}a{color:#0965ab;font-size:13px}footer{max-width:1200px;margin:auto;padding:24px;color:#506070;font-size:13px}
</style><header><div class="tag">ARSIP TAMPILAN · 22 OKTOBER 2026</div><h1>Cekpedia — Before</h1><p>Dokumentasi sebelum perbaikan desain. Klik gambar untuk membuka PNG ukuran penuh (1080 × 2340). Layout, ikon, warna, dan aset preview diambil dari APK rilis lama, bukan demo rekonstruksi.</p><p><strong>Bedakan sumber:</strong> gambar 01 adalah screenshot aplikasi asli yang dijalankan. Gambar 02–22 adalah render layout asli melalui penampil offline terpisah. Daftar menggunakan data contoh dan header drawer memakai isi bawaan APK; banner Firebase tidak dimuat dan peta memakai placeholder berlabel. Screenshot ini mendokumentasikan tampilan, bukan bukti bahwa semua fitur backend berfungsi.</p><p>Profil, Share, dan Send di kode asli hanya menampilkan pesan “Fitur masih dalam pengembangan”. Near Me belum memiliki aksi. Gambar profil placeholder disertakan sebagai referensi layout yang belum selesai.</p><p>Ringkasan gambar: <a href="overview-1.png">Lembar 1</a> · <a href="overview-2.png">Lembar 2</a> · <a href="overview-3.png">Lembar 3</a></p></header><main>'''+cards+'''</main><footer>APK sumber: app/release/app-release.apk. Seluruh resource APK sumber diverifikasi identik byte per byte pada penampil. Tidak ada redesign pada aplikasi asli. Detail teknis dan batas dokumentasi ada di README.md dan provenance.json.</footer></html>''')
provenance=ROOT/'tools/before-preview/build/provenance.json'
shutil.copy2(provenance,OUT/'provenance.json')
(OUT/'README.md').write_text('''# Cekpedia — Before

Dokumentasi dibuat 22 Oktober 2026 pada emulator Pixel 5 API 30, PNG 1080 × 2340.
Buka `index.html` untuk galeri; klik gambar untuk ukuran penuh.

## Keaslian dan batas dokumentasi

- `01-login-apk-asli.png`: screenshot runtime APK `cekpedia.apk` dari root proyek.
- `02`–`22`: render resource asli `app/release/app-release.apk` melalui penampil terpisah `me.cekpedia.before`. Bukan screenshot sesi pengguna aplikasi asli, bukan demo lokal rekonstruksi, dan bukan desain baru.
- Resource `res/*` dan `resources.arsc` disalin tanpa perubahan dan diverifikasi byte per byte. SHA-256 sumber tersimpan dalam `provenance.json`.
- Beranda memakai layout utama, fragment home, dan adapter kategori asli. Banner Firebase tidak dimuat.
- Isi daftar dan teks detail adalah fixture untuk dokumentasi; foto adalah aset bawaan APK. Fixture tidak berasal dari database pengguna. Header drawer memakai isi bawaan APK.
- Fragmen peta diganti area abu-abu berlabel offline agar preview tidak bergantung layanan eksternal.
- Form tidak mengirim data, login tidak dijalankan pada penampil, dan penampil tidak mempunyai permission internet.
- Menu Profil, Share, Send pada kode asli hanya menampilkan pesan pengembangan; Near Me tidak memiliki aksi. Tidak ada halaman penuh untuk keempat menu tersebut. Layout fragment profil placeholder tetap disertakan dan diberi label.
- Versi login `cekpedia.apk` dan APK rilis berbeda; keduanya disertakan dengan keterangan sumber.
- Loading/error Firebase, akun asli, hasil pencarian live, keyboard, dan isi peta live tidak termasuk arsip ini.

## Reproduksi

`JAVA_HOME=<Java 11> python3 tools/before-preview/build.py`

Pasang `tools/before-preview/build/cekpedia-before.apk` ke emulator, lalu jalankan
`python3 tools/before-preview/capture.py` dan `python3 tools/before-preview/package.py`.
Build memakai Android platform 30 dan build-tools 30.0.3. Tidak memerlukan root emulator.

Nama layar dan keterangan per gambar ada di `screens.json`.
''')
archive=ROOT/'artifacts/cekpedia-before.zip'
with zipfile.ZipFile(archive,'w',zipfile.ZIP_DEFLATED) as z:
 for p in sorted(OUT.iterdir()):
  if p.is_file():z.write(p,'cekpedia-before/'+p.name)
print(archive)
