package com.example.icecreamdelivery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class ReturnItems extends AppCompatActivity {

    private ListView returnItemsList;

    private String[] Id,ShopId,ShopName,Address,Contact,Root,IdNo,Credit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_items);

        returnItemsList = (ListView) findViewById(R.id.returnItemsList);

        //name = new String[Lname.length()];
        Id = new String[10];
        ShopId = new String[10];
        ShopName = new String[10];
        Address = new String[10];
        Contact = new String[10];
        Root = new String[10];
        IdNo = new String[10];
        Credit = new String[10];

        for (int i = 0; i < 10; i++){
            Id[i] = "" + i;
            ShopId[i] = "21";
            ShopName[i] = "Shop Name Here";
            Address[i] = "Rajangane para, 5 kanuwa.";
            Contact[i] = "0715559000";
            Root[i] = "6";
            IdNo[i] = "760382722";
            Credit[i] = "0";
        }

        ListViewAdapter returnItemsListViewAdapter = new ListViewAdapter(ReturnItems.this, Id, "ReturnItems", ShopId, ShopName, Address, Contact, Root, IdNo, Credit);
        returnItemsList.setAdapter(returnItemsListViewAdapter);

    }
}
