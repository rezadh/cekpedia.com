package me.cekpedia;

/**
 * Created by rezadwihendarno on 19/02/2018.
 */

public class Hotel {
    private int gambar;
    private String NamaHotel, deskripsihotel;

    public Hotel(int gambar, String namaHotel, String deskripsihotel) {
        this.gambar = gambar;
        NamaHotel = namaHotel;
        this.deskripsihotel = deskripsihotel;
    }

    public int getGambar() {
        return gambar;
    }

    public void setGambar(int gambar) {
        this.gambar = gambar;
    }

    public String getNamaHotel() {
        return NamaHotel;
    }

    public void setNamaHotel(String namaHotel) {
        NamaHotel = namaHotel;
    }

    public String getDeskripsihotel() {
        return deskripsihotel;
    }

    public void setDeskripsihotel(String deskripsihotel) {
        this.deskripsihotel = deskripsihotel;
    }
}
