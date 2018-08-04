package me.cekpedia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by rezadwihendarno on 19/02/2018.
 */

public class HotelAdapter extends BaseAdapter{
    private int gambar[];
    private String judul[], deskhotel[];
    private Context context;
    private LayoutInflater inflater;

    public HotelAdapter(Context context, int[] gambar, String[] judul, String[] deskhotel) {
        this.context = context;
        this.gambar = gambar;
        this.judul = judul;
        this.deskhotel = deskhotel;

    }

    @Override
    public int getCount() {
        return judul.length;
    }

    @Override
    public Object getItem(int position) {
        return judul[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listItemView = inflater.inflate(R.layout.list_item, null);
        }

        ImageView imghotel = (ImageView) listItemView.findViewById(R.id.imgview);
        TextView judulhotel = (TextView)listItemView.findViewById(R.id.judul);
        TextView deskripsihotel = (TextView) listItemView.findViewById(R.id.deskripsi);

        imghotel.setImageResource(gambar[position]);
        judulhotel.setText(judul[position]);
        deskripsihotel.setText(deskhotel[position]);

        return listItemView;
    }
}
