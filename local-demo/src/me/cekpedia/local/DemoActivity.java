package me.cekpedia.local;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import java.util.Locale;

/** Offline exploration of the original category structure, using bundled artwork. */
public class DemoActivity extends Activity {
    private final String[] categories = {"Masjid", "Wisata", "Penginapan", "Rumah Sakit", "Restoran", "Supermarket", "Sekolah", "Transportasi", "SPBU", "Apotek", "Bidan"};
    private final String[] icons = {"ic_masjid", "ic_wisata", "ic_penginapan", "ic_rumah_sakit", "ic_restoran", "ic_supermarket", "ic_sekolah", "ic_transportasi", "ic_spbu", "ic_apotek", "ic_bidan"};
    private final String[][] places = {
        {"Masjid Contoh", "Masjid", "Kawasan pusat kota"},
        {"Taman Kota Contoh", "Wisata", "Kawasan taman kota"},
        {"Penginapan Contoh", "Penginapan", "Kawasan pusat kota"},
        {"Rumah Sakit Contoh", "Rumah Sakit", "Kawasan layanan kesehatan"},
        {"Restoran Contoh", "Restoran", "Kawasan kuliner"},
        {"Supermarket Contoh", "Supermarket", "Kawasan perbelanjaan"},
        {"Sekolah Contoh", "Sekolah", "Kawasan pendidikan"},
        {"Terminal Contoh", "Transportasi", "Kawasan terminal"},
        {"SPBU Contoh", "SPBU", "Jalan utama"},
        {"Apotek Contoh", "Apotek", "Kawasan layanan kesehatan"},
        {"Bidan Contoh", "Bidan", "Kawasan permukiman"}
    };
    private LinearLayout root, results;
    private String category = "";
    private boolean favoritesOnly;
    private EditText search;
    private int dp(int n) { return (int)(n * getResources().getDisplayMetrics().density); }
    private TextView text(String value, int size) {
        TextView t = new TextView(this); t.setText(value); t.setTextSize(size);
        t.setTextColor(0xff263238); t.setPadding(dp(16), dp(10), dp(16), dp(10)); return t;
    }
    @Override public void onCreate(Bundle state) {
        super.onCreate(state); getWindow().setStatusBarColor(0xff026da8);
        root = new LinearLayout(this); root.setOrientation(1); root.setBackgroundColor(0xffeeeeee);
        setContentView(root);
        TextView header = text("☰    Cekpedia", 24); header.setTextColor(-1); header.setBackgroundColor(0xff0288d1);
        root.addView(header); header.setOnClickListener(v -> menu());
        TextView notice = text("DEMO LOKAL · Data contoh · Tanpa Firebase", 12); root.addView(notice);
        search = new EditText(this); search.setSingleLine(); search.setHint("Apa yang Anda cari?");
        search.setPadding(dp(16), dp(8), dp(16), dp(8)); root.addView(search);
        ScrollView scroll = new ScrollView(this); root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        LinearLayout content = new LinearLayout(this); content.setOrientation(1); scroll.addView(content);
        TextView banner = text("Teman bepergian terbaikmu\nJelajahi tempat dan layanan di sekitar", 20);
        banner.setBackgroundColor(0xffd8effb); banner.setMinHeight(dp(130)); content.addView(banner);
        GridLayout grid = new GridLayout(this); grid.setColumnCount(4); content.addView(grid);
        for (int i = 0; i < categories.length; i++) {
            final int index = i;
            LinearLayout cell = new LinearLayout(this); cell.setOrientation(1); cell.setGravity(17);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = getResources().getDisplayMetrics().widthPixels / 4; lp.height = dp(90); cell.setLayoutParams(lp);
            ImageView image = new ImageView(this);
            int res = getResources().getIdentifier(icons[i], "drawable", getPackageName());
            if (res != 0) image.setImageResource(res);
            cell.addView(image, new LinearLayout.LayoutParams(dp(44), dp(44)));
            TextView label = text(categories[i], 11); label.setPadding(0, dp(5), 0, 0); cell.addView(label);
            cell.setContentDescription(categories[i]); cell.setOnClickListener(v -> { category = categories[index]; favoritesOnly = false; render(); });
            grid.addView(cell);
        }
        results = new LinearLayout(this); results.setOrientation(1); content.addView(results);
        search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int a, int c, int f) {}
            public void onTextChanged(CharSequence s, int a, int b, int c) { render(); }
            public void afterTextChanged(Editable s) {}
        });
        render();
    }
    private void menu() {
        new AlertDialog.Builder(this).setTitle("Cekpedia Lokal")
            .setItems(new String[]{"Semua tempat", "Favorit", "Tentang demo"}, (dialog, which) -> {
                if (which == 2) { new AlertDialog.Builder(this).setMessage("Preview Android terpisah menggunakan aset proyek asli dan data contoh. Login, peta, telepon, serta pengiriman lokasi belum terhubung. Favorit disimpan di perangkat ini.").setPositiveButton("Tutup", null).show(); return; }
                category = ""; favoritesOnly = which == 1; search.setText(""); render();
            }).show();
    }
    private void render() {
        if (results == null) return;
        results.removeAllViews();
        results.addView(text(favoritesOnly ? "Favorit lokal" : category.isEmpty() ? "Semua tempat contoh" : category + " · Hapus filter", 18));
        results.getChildAt(0).setOnClickListener(v -> { category = ""; render(); });
        String query = search.getText().toString().trim().toLowerCase(Locale.ROOT); int count = 0;
        for (String[] p : places) {
            if (!category.isEmpty() && !category.equals(p[1])) continue;
            if (favoritesOnly && !getPreferences(0).getBoolean(p[0], false)) continue;
            if (!(p[0] + " " + p[1] + " " + p[2]).toLowerCase(Locale.ROOT).contains(query)) continue;
            TextView row = text(p[0] + "\n" + p[1] + " · " + p[2] + "\nLihat detail  ›", 15);
            row.setBackgroundColor(-1); results.addView(row);
            row.setOnClickListener(v -> detail(p)); count++;
        }
        if (count == 0) results.addView(text("Belum ada tempat yang cocok.", 15));
    }
    private void detail(String[] p) {
        boolean saved = getPreferences(0).getBoolean(p[0], false);
        new AlertDialog.Builder(this).setTitle(p[0]).setMessage(p[1] + "\n" + p[2] + "\n\nData contoh untuk mencoba tampilan. Peta dan informasi kontak belum tersedia dalam demo offline.")
            .setPositiveButton(saved ? "Hapus favorit" : "Simpan favorit", (d, w) -> {
                getPreferences(0).edit().putBoolean(p[0], !saved).apply(); render();
            }).setNegativeButton("Tutup", null).show();
    }
}
