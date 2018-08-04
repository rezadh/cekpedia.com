package me.cekpedia.models;

/**
 * Created by rezadwihendarno on 13/04/2018.
 */

public class ImageSlider {
    private String mTitle;
    private String mImage;

    public ImageSlider(String mTitle, String mImage) {
        this.mTitle = mTitle;
        this.mImage = mImage;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }
}
