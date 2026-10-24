package me.cekpedia;

public class TransportasiActivity extends CategoryActivity {
    @Override protected int layoutResource() { return R.layout.activity_transportasi; }
    @Override protected int listResource() { return R.id.listviewtransportasi; }
    @Override protected String categoryKey() { return "transportasi"; }
}
