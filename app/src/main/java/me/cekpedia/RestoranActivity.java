package me.cekpedia;

public class RestoranActivity extends CategoryActivity {
    @Override protected int layoutResource() { return R.layout.activity_restoran; }
    @Override protected int listResource() { return R.id.listviewrestoran; }
    @Override protected String categoryKey() { return "restoran"; }
}
