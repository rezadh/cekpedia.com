package me.cekpedia;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.cekpedia.models.ImageUpload;

/**
 * Created by rezadwihendarno on 14/03/2018.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ImageViewHolder> {
    private Context mContext;
    private List<ImageUpload> mUploads;

    public ListAdapter(Context context, List<ImageUpload> uploads){
        mContext = context;
        mUploads = uploads;
    }


    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        ImageUpload UploadCurrent = mUploads.get(position);
        holder.tvName.setText(UploadCurrent.getName());
        holder.tvLokasi.setText(UploadCurrent.getLokasi());
        Picasso.with(mContext)
                .load(UploadCurrent.getUrl())
                .fit()
                .centerInside()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvLokasi;
        public ImageView imageView;

        public ImageViewHolder(View itemView){
            super(itemView);

            tvName = itemView.findViewById(R.id.judul);
            tvLokasi = itemView.findViewById(R.id.deskripsi);
            imageView = itemView.findViewById(R.id.imgview);
        }
    }
}
