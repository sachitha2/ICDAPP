package com.example.icecreamdelivery;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Shops extends AppCompatActivity {

    private ListView shopsList;
    EditText search;

    ListViewAdapter shopsListViewAdapter;

    SQLiteDatabase sqLiteShops;

    private String[] Id,ShopId,ShopName,Address,Contact,Root,IdNo,Credit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);

        shopsList = (ListView) findViewById(R.id.shopsList);
        search = (EditText) findViewById(R.id.search);

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

        shopsListViewAdapter = new ListViewAdapter(Shops.this, Id, "Shops", ShopId, ShopName, Address, Contact, Root, IdNo, Credit);
        shopsList.setAdapter(shopsListViewAdapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //(Shops.this).shopsListViewAdapter.getFilter().filter(s);
                //TODO Shops list eka filter karanna one

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
