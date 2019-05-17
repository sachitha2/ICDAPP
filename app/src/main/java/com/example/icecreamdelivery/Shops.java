package com.example.icecreamdelivery;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class Shops extends AppCompatActivity {

    private ListView shopsList;

    private String[] Id,ShopId,ShopName,Address,Contact,Root,Date,IdNo,Credit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);

        shopsList = (ListView) findViewById(R.id.shopsList);

        //name = new String[Lname.length()];
        Id = new String[10];
        ShopId = new String[10];
        ShopName = new String[10];
        Address = new String[10];
        Contact = new String[10];
        Root = new String[10];
        Date = new String[10];
        IdNo = new String[10];
        Credit = new String[10];

        for (int i = 0; i < 10; i++){
            Id[i] = "" + i;
            ShopId[i] = "21";
            ShopName[i] = "Shop Name Here";
            Address[i] = "Rajangane para, 5 kanuwa.";
            Contact[i] = "0715559000";
            Root[i] = "6";
            Date[i] = "2019-05-17";
            IdNo[i] = "760382722";
            Credit[i] = "0";
        }

        ListViewAdapter shopsListViewAdapter = new ListViewAdapter(Shops.this, Id, "Shops", ShopId, ShopName, Address, Contact, Root, Date, IdNo, Credit);
        shopsList.setAdapter(shopsListViewAdapter);

    }
}
