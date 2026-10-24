package me.cekpedia;

public class ApotekActivity extends CategoryActivity {
    @Override protected int layoutResource() { return R.layout.activity_apotek; }
    @Override protected int listResource() { return R.id.listviewapotek; }
    @Override protected String categoryKey() { return "apotek"; }
}
