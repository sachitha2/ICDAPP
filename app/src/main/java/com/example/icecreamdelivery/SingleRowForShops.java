package com.example.icecreamdelivery;

public class SingleRowForShops {
    String name;
    String age;
    //By Chatson
    String address;
    String Contact;
    String route;
    String idCardN;
    String credit;
    //By Chatson


    public SingleRowForShops(String name,String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public  String getAge(){
        return  age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  void  setAge(String age){
        this.age = age;
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
