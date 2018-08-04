package me.cekpedia.models;

import android.widget.ImageView;

/**
 * Created by rezadwihendarno on 14/03/2018.
 */

public class ImageUpload {
    private String name;
    private String url, nameSub, deskripsi;
    private ImageView imageView;
    private String lokasi, number;
    private Double lang, longi;
    private boolean sponsor;

    public ImageUpload(String name, String lokasi, String number, String url, Double lang, Double longi, String deskripsi, boolean sponsor, String nameSub) {
        this.name = name;
        this.lokasi = lokasi;
        this.number = number;
        this.url = url;
        this.lang = lang;
        this.longi = longi;
        this.deskripsi = deskripsi;
        this.sponsor = sponsor;
        this.nameSub = nameSub;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public boolean isSponsor() {
        return sponsor;
    }

    //    public ImageUpload(String name, String lokasi, String number, String url, boolean Favourite, String nameSub) {
//        this.name = name;
//        this.lokasi = lokasi;
//        this.number = number;
////        this.imageView = imageView;
//        this.url = url;
//        this.favourite = Favourite;
//        this.nameSub = nameSub;
//    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setNameSub(String nameSub) {
        this.nameSub = nameSub;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setLang(Double lang) {
        this.lang = lang;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }

    public void setSponsor(boolean sponsor) {
        this.sponsor = sponsor;
    }

    public ImageUpload (String nameSub){
        this.nameSub = nameSub;
    }

    public String getNameSub() {
        return nameSub;
    }

    public ImageUpload(String name, String lokasi, String url) {
        this.name = name;
        this.lokasi = lokasi;
        this.url = url;

    }

    public Double getLang() {
        return lang;
    }
    public boolean isFavourite() {
        return sponsor;
    }

    public void setFavourite(boolean favourite) {
        this.sponsor = favourite;
    }

    public String getNumber() {
        return number;
    }

    public Double getLongi() {
        return longi;
    }

    public String getLokasi() {
        return lokasi;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
    public ImageUpload(){

    }
}