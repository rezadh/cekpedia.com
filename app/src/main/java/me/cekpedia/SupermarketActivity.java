package me.cekpedia;

public class SupermarketActivity extends CategoryActivity {
    @Override protected int layoutResource() { return R.layout.activity_supermarket; }
    @Override protected int listResource() { return R.id.listviewsupermarket; }
    @Override protected String categoryKey() { return "supermarket"; }
}
