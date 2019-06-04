package com.example.icecreamdelivery;

public class SingleRowForAllInvoices {
    String name;
    String id;

    public SingleRowForAllInvoices(String name, String id, String address, String contact, String route, String idCardN, String credit) {
        this.name = name;
        this.id = id;
        this.address = address;
        this.Contact = contact;
        this.route = route;
        this.idCardN = idCardN;
        this.credit = credit;
    }

    //By Chatson
    String address;
    String Contact;
    String route;
    String idCardN;
    String credit;
    //By Chatson


    public SingleRowForAllInvoices(String name,String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public  String getAge(){
        return  id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  void  setAge(String id){
        this.id = id;
    }


    public String getAddress() {
        return address;
    }

    public String getContact() {
        return Contact;
    }

    public String getRoute() {
        return route;
    }

    public String getIdCardN() {
        return idCardN;
    }

    public String getCredit() {
        return credit;
    }
}
