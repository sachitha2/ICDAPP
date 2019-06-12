package com.example.icecreamdelivery;

public class SingleRowForSalesItems {
    String id;
    String name;



    String rAmount;


    public SingleRowForSalesItems(String id, String name,String rAmount) {
        this.id = id;
        this.name = name;
        this.rAmount = rAmount;
    }


    public String getrAmount() {
        return rAmount;
    }

    public void setrAmount(String rAmount) {
        this.rAmount = rAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
