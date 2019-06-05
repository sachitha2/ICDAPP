package com.example.icecreamdelivery;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class Shops extends AppCompatActivity implements TextWatcher{

    private ListView shopsList;
    EditText search;



    //chatson
    ArrayList<SingleRowForShops> myList;
    ShopsListAdapter shopsListAdapter;
    //chatson

    SQLiteDatabase sqLiteShops;

    private String[] Id,ShopId,ShopName,Address,Contact,Root,IdNo,Credit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);

        shopsList = (ListView) findViewById(R.id.dataList);
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
        myList = new ArrayList<>();
        SingleRowForShops singleRow;
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



            singleRow = new SingleRowForShops(c.getString(1),ShopId[i], c.getString(2),"2","3","4","5");

            myList.add(singleRow);


            i++;

        }

        search.addTextChangedListener(this);


        shopsListAdapter = new ShopsListAdapter(this,myList);

        shopsList.setAdapter(shopsListAdapter);



    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.shopsListAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
