package me.cekpedia.models;

/**
 * Created by rezadwihendarno on 18/02/2018.
 */

public class Image {
    private int gambar;
    private String nama;

    public Image(int gambar, String nama) {
        this.gambar = gambar;
        this.nama = nama;
    }

    public int getGambar() {
        return gambar;
    }

    public void setGambar(int gambar) {
        this.gambar = gambar;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

}
