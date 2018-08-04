package me.cekpedia;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.cekpedia.models.ImageUpload;

import static me.cekpedia.InputLokasiActivity.FB_DATABASE_PATH;

/**
 * Created by rezadwihendarno on 14/03/2018.
 */

public class ImageListAdapter extends ArrayAdapter<ImageUpload> {
    private Activity context;
    private int resource;
    private List<ImageUpload> listImage;
    private String nameSub="";

    public ImageListAdapter(@NonNull Activity context, int resource, @NonNull List<ImageUpload> objects, String nameSub) {
        super(context, 0, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
        this.nameSub = nameSub;
    }
    public ImageListAdapter(@NonNull Activity context, int resource, @NonNull List<ImageUpload> objects) {
        super(context, 0, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView tvName = listItemView.findViewById(R.id.judul);
        TextView tvLokasi = listItemView.findViewById(R.id.lokasi);
        ImageView imageView = listItemView.findViewById(R.id.imgview);
        TextView tvTelp = listItemView.findViewById(R.id.telp);
        TextView tvDeskripsi =listItemView.findViewById(R.id.deskripsi);
        final TextView tvSponsor = listItemView.findViewById(R.id.tvsponsor);

        tvName.setText(listImage.get(position).getName());
        tvLokasi.setText(listImage.get(position).getLokasi());
        tvTelp.setText(listImage.get(position).getNumber());
        tvDeskripsi.setText(listImage.get(position).getDeskripsi());
        Glide.with(context)
                .load(listImage.get(position).getUrl())
                .into(imageView);

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SubMenuActivity.class);
                intent.putExtra("JUDUL", listImage.get(position).getName());
//                intent.putExtra("FAV", listImage.get(position).isFavourite());
                if (!nameSub.equals(""))
                    intent.putExtra("SUB", nameSub);
                else
                    intent.putExtra("SUB", listImage.get(position).getNameSub());
                context.startActivity(intent);
            }
        });
//        DatabaseReference mDatabaseRef;
//        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH).child("cekpediaItem").child(nameSub);
//        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                final Map<String, Object> detailMenu = (Map<String, Object>) dataSnapshot.getValue();
//                String sponsor = detailMenu.get("sponsor").toString();
//                if (sponsor == "true"){
//                    tvSponsor.setVisibility(View.VISIBLE);
//                }else {
//                    tvSponsor.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        return listItemView;

    }
}
