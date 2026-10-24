package me.cekpedia;

public class BidanActivity extends CategoryActivity {
    @Override protected int layoutResource() { return R.layout.activity_bidan; }
    @Override protected int listResource() { return R.id.listviewbidan; }
    @Override protected String categoryKey() { return "bidan"; }
}
