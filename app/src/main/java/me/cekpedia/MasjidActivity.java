package me.cekpedia;

public class MasjidActivity extends CategoryActivity {
    @Override protected int layoutResource() { return R.layout.activity_masjid; }
    @Override protected int listResource() { return R.id.listviewmasjid; }
    @Override protected String categoryKey() { return "masjid"; }
}
