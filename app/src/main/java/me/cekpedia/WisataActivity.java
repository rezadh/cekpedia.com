package me.cekpedia;

public class WisataActivity extends CategoryActivity {
    @Override protected int layoutResource() { return R.layout.activity_wisata; }
    @Override protected int listResource() { return R.id.listviewwisata; }
    @Override protected String categoryKey() { return "wisata"; }
}
