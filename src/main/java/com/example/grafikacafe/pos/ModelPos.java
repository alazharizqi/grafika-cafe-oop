package com.example.grafikacafe.pos;

public class ModelPos {

    public String id_pos;
    public String kode_pos;
    public String no_meja;
    public String customer;
    public String date;
    public String total;

    public ModelPos(String id_pos, String kode_pos, String no_meja, String customer, String date, String total) {
        this.id_pos = id_pos;
        this.kode_pos = kode_pos;
        this.no_meja = no_meja;
        this.customer = customer;
        this.date = date;
        this.total = total;
    }

    public String getId_pos() {return id_pos;}
    public  void setId_pos(String id_pos) {this.id_pos = id_pos;}

    public String getKode_pos() {return kode_pos;}
    public  void setKode_pos(String kode_pos) {this.kode_pos = kode_pos;}

    public String getNo_meja() {return no_meja;}
    public  void setNo_meja(String no_meja) {this.no_meja = no_meja;}

    public String getCustomer() {return customer;}
    public  void setCustomer(String customer) {this.customer = customer;}

    public String getDate() {return date;}
    public  void setDate(String date) {this.date = date;}

    public String getTotal() {return total;}
    public  void setTotal(String total) {this.total = total;}

}
