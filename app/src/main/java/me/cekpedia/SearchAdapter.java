package me.cekpedia;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by rezadwihendarno on 04/04/2018.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    ArrayList<String> JudulList;
    ArrayList<String> LokasiList;
    ArrayList<String> NumberList;
    ArrayList<String> GambarList;
    ArrayList<String> nameSubList;

    private String nameSub="";
    class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView gambar;
        TextView judul, lokasi, nomor;

        public SearchViewHolder(View itemView) {
            super(itemView);
            gambar = (ImageView) itemView.findViewById(R.id.imgview);
            judul = (TextView)itemView.findViewById(R.id.judul);
            lokasi = (TextView)itemView.findViewById(R.id.deskripsi);
            nomor = (TextView)itemView.findViewById(R.id.telp);
        }
    }

    public SearchAdapter(Context context, ArrayList<String> judulList, ArrayList<String> lokasiList, ArrayList<String> numberList, ArrayList<String> gambarList) {
        this.context = context;
        JudulList = judulList;
        LokasiList = lokasiList;
        NumberList = numberList;
        GambarList = gambarList;
    }
    public SearchAdapter(Context context, ArrayList<String> judulList, ArrayList<String> lokasiList, ArrayList<String> numberList, ArrayList<String> gambarList, ArrayList<String> nameSubList) {
        this.context = context;
        this.JudulList = judulList;
        this.LokasiList = lokasiList;
        this.NumberList = numberList;
        this.GambarList = gambarList;
        this.nameSubList = nameSubList;
    }

    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SubMenuActivity.class);
                intent.putExtra("JUDUL", JudulList.get(viewType));
                if (!nameSub.equals(""))
                    intent.putExtra("SUB", nameSub);
                else
                    intent.putExtra("SUB", nameSubList.get(viewType));
                context.startActivity(intent);
            }
        });

        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.judul.setText(JudulList.get(position));
        holder.lokasi.setText(LokasiList.get(position));
        holder.nomor.setText(NumberList.get(position));
        Glide.with(context)
                .load(GambarList.get(position))
                .into(holder.gambar);

//        Glide.with(context).load(profilePicList.get(position)).asBitmap().placeholder(R.mipmap.ic_launcher_round).into(holder.profileImage);
    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//    }

    @Override
    public int getItemCount() {
        return JudulList.size();
    }
}
