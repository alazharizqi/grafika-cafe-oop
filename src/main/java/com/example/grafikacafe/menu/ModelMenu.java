package com.example.grafikacafe.menu;

public class ModelMenu {

    public String id_menu;
    public String menu;
    public String kategori;
    public String harga;
    public String deskripsi;
    public String status;

    public ModelMenu(String id_menu, String menu, String kategori, String harga, String deskripsi, String status) {
        this.id_menu = id_menu;
        this.menu = menu;
        this.kategori = kategori;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.status = status;
    }

    public String getId_menu() {return id_menu;}
    public  void setId_menu(String id_menu) {this.id_menu = id_menu;}

    public String getMenu() {return menu;}
    public void setMenu(String menu) {this.menu = menu;}

    public String getKategori() {return kategori;}
    public void setKategori(String kategori) {this.kategori = kategori;}

    public String getHarga() {return harga;}
    public void setHarga(String harga) {this.harga = harga;}

    public String getDeskripsi() {return deskripsi;}
    public void setDeskripsi(String deskripsi) {this.deskripsi = deskripsi;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

}
