package com.example.icecreamdelivery;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class Shops extends AppCompatActivity {

    private ListView shopsList;

    SQLiteDatabase sqLiteShops;

    private String[] Id,ShopId,ShopName,Address,Contact,Root,IdNo,Credit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);

        shopsList = (ListView) findViewById(R.id.shopsList);

        sqLiteShops = openOrCreateDatabase("ICD", Shops.MODE_PRIVATE,null);

        Cursor c =sqLiteShops.rawQuery("SELECT * FROM shop;",null);

        int nRow = c.getCount();

        Id = new String[nRow];
        ShopId = new String[nRow];
        ShopName = new String[nRow];
        Address = new String[nRow];
        Contact = new String[nRow];
        Root = new String[nRow];
        IdNo = new String[nRow];
        Credit = new String[nRow];

        int i = 0;
        while (c.moveToNext()){

            Id[i] = "" + i;
            ShopId[i] = c.getString(0);
            ShopName[i] = c.getString(1);
            Address[i] = c.getString(2);
            Contact[i] = c.getString(3);
            Root[i] = c.getString(4);
            IdNo[i] = c.getString(5);
            Credit[i] = c.getFloat(6) + "";

            i++;

        }

        //Toast.makeText(Shops.this, ShopName[3], Toast.LENGTH_LONG).show();

        ListViewAdapter shopsListViewAdapter = new ListViewAdapter(Shops.this, Id, "Shops", ShopId, ShopName, Address, Contact, Root, IdNo, Credit);
        shopsList.setAdapter(shopsListViewAdapter);

    }
}
