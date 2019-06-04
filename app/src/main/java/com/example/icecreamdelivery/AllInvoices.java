package com.example.icecreamdelivery;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AllInvoices extends AppCompatActivity implements TextWatcher {
    private ListView shopsList;
    EditText search;

    String shopName;

    //chatson
    ArrayList<SingleRowForAllInvoices> myList;
    AllInvoicesListAdapter shopsListAdapter;
    //chatson

    SQLiteDatabase sqLiteShops;

    private String[] Id,ShopId,ShopName,Address,Contact,Root,IdNo,Credit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_invoices);
        shopsList = (ListView) findViewById(R.id.shopsList);
        search = (EditText) findViewById(R.id.search);

        sqLiteShops = openOrCreateDatabase("ICD", Shops.MODE_PRIVATE,null);

        Cursor c =sqLiteShops.rawQuery("SELECT * FROM deal;",null);

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
        SingleRowForAllInvoices singleRow;
        int i = 0;
        while (c.moveToNext()){

            Id[i] = "" + i;
            ShopId[i] = c.getString(0);
            ShopName[i] = c.getString(1);
            Address[i] = c.getString(2);

            //Looking for shop name
            shopName = "sam shop";
            Cursor shopNameFind =sqLiteShops.rawQuery("SELECT * FROM shop where id = "+c.getString(1)+";",null);

            shopNameFind.moveToNext();
            shopName = shopNameFind.getString(1);

            //Looking for shop name




            singleRow = new SingleRowForAllInvoices(shopName, c.getString(0), c.getString(1),c.getString(2));

            myList.add(singleRow);


            i++;

        }

        search.addTextChangedListener(this);


        shopsListAdapter = new AllInvoicesListAdapter(this,myList);

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
