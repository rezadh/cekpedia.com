package me.cekpedia;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by rezadwihendarno on 18/04/2018.
 */

public class ImageSlideAdapter extends PagerAdapter {
    private ArrayList<Integer> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    HomeFragment homeFragment;

    String[] aMerchantPhoto;
    int merchantSize;

    public ImageSlideAdapter(Context context, ArrayList<Integer> IMAGES,HomeFragment homeFragment) {
        this.context = context;
        this.IMAGES=IMAGES;
        this.homeFragment = homeFragment;
        inflater = LayoutInflater.from(context);
    }

    public ImageSlideAdapter(Context context, String[] aMerchantPhotoAds, int merchantSize, HomeFragment homeFragment) {
        this.context = context;
        this.aMerchantPhoto = aMerchantPhotoAds;
        this.merchantSize = merchantSize;
        this.homeFragment = homeFragment;
//        inflater = LayoutInflater.from(context);
    }

    public ImageSlideAdapter(Context context, String[] aMerchantPhotoAds, int merchantSize) {
        this.context = context;
        this.aMerchantPhoto = aMerchantPhotoAds;
        this.merchantSize = merchantSize;
        this.homeFragment = homeFragment;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return merchantSize;
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = LayoutInflater.from(view.getContext()).inflate(R.layout.sliding_images, view, false);

        assert imageLayout != null;
        final ImageView imageViewSlideEvent = (ImageView) imageLayout.findViewById(R.id.slider);
        final CardView cardViewAds = (CardView) imageLayout.findViewById(R.id.card);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(aMerchantPhoto[position]);
        Glide.with(homeFragment)
                .load(storageRef)
                .into(imageViewSlideEvent);

        //imageViewSlideEvent.setImageResource(IMAGES.get(position));

        view.addView(imageLayout, 0);

        /*imageViewSlideEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/



        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}