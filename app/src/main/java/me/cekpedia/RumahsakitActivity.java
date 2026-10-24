package me.cekpedia;

public class RumahsakitActivity extends CategoryActivity {
    @Override protected int layoutResource() { return R.layout.activity_rumahsakit; }
    @Override protected int listResource() { return R.id.listviewrumahsakit; }
    @Override protected String categoryKey() { return "rumahsakit"; }
}
