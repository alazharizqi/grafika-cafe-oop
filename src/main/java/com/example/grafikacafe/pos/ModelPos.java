package com.example.grafikacafe.pos;

public class ModelPos {

    public String id_pos;
    public String kode_pos;
    public String no_meja;
    public String customer;
    public String menu;
    public String kategori;
    public String harga;
    public String deskripsi;
    public String status;
    public String qty;
    public String total;
    public String date;

    public ModelPos(String id_pos, String kode_pos, String no_meja, String customer, String menu, String kategori, String harga, String deskripsi, String status, String qty, String total, String date) {
        this.id_pos = id_pos;
        this.kode_pos = kode_pos;
        this.no_meja = no_meja;
        this.customer = customer;
        this.menu = menu;
        this.kategori = kategori;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.status = status;
        this.qty = qty;
        this.total = total;
        this.date = date;
    }

    public String getId_pos() {return id_pos;}
    public  void setId_pos(String id_pos) {this.id_pos = id_pos;}

    public String getKode_pos() {return kode_pos;}
    public  void setKode_pos(String kode_pos) {this.kode_pos = kode_pos;}

    public String getNo_meja() {return no_meja;}
    public  void setNo_meja(String no_meja) {this.no_meja = no_meja;}

    public String getCustomer() {return customer;}
    public  void setCustomer(String customer) {this.customer = customer;}

    public String getMenu() {return menu;}
    public  void setMenu(String menu) {this.menu = menu;}

    public String getKategori() {return kategori;}
    public  void setKategori(String kategori) {this.kategori = kategori;}

    public String getHarga() {return harga;}
    public  void setHarga(String harga) {this.harga = harga;}

    public String getDeskripsi() {return deskripsi;}
    public  void setDeskripsi(String deskripsi) {this.deskripsi = deskripsi;}

    public String getStatus() {return status;}
    public  void setStatus(String status) {this.status = status;}

    public String getQty() {return qty;}
    public void setQty(String qty) {this.qty = qty;}

    public String getTotal() {return total;}
    public  void setTotal(String total) {this.total = total;}

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}

}
