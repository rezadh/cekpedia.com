package me.cekpedia;

public class HotelActivity extends CategoryActivity {
    @Override protected int layoutResource() { return R.layout.activity_hotel; }
    @Override protected int listResource() { return R.id.listviewhotel; }
    @Override protected String categoryKey() { return "hotel"; }
}
