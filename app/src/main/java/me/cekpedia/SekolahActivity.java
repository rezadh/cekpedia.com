package me.cekpedia;

public class SekolahActivity extends CategoryActivity {
    @Override protected int layoutResource() { return R.layout.activity_sekolah; }
    @Override protected int listResource() { return R.id.listviewsekolah; }
    @Override protected String categoryKey() { return "sekolah"; }
}
