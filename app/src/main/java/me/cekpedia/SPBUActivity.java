package me.cekpedia;

public class SPBUActivity extends CategoryActivity {
    @Override protected int layoutResource() { return R.layout.activity_spbu; }
    @Override protected int listResource() { return R.id.listviewspbu; }
    @Override protected String categoryKey() { return "spbu"; }
}
