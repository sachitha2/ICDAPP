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

public class SalesItem extends AppCompatActivity  implements TextWatcher {
    private ListView itemList;
    EditText search;
    TextView total;
    TextView cash;
    private ListView shopsList;

    //chatson
    ArrayList<SingleRowForSalesItems> myList;
    SalesItemListAdapter shopsListAdapter;
    //chatson

    SQLiteDatabase sqLiteShops;

    private String[] Id,ShopId,ShopName,Address,Contact,Root,IdNo,Credit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_item);
        setTitle("Sales - Items");

        itemList = findViewById(R.id.salesItemList);
        search = findViewById(R.id.searchSalesItems);
        total = findViewById(R.id.txtTotal);
        cash = findViewById(R.id.txtCashTotal);

        shopsList = itemList;

        sqLiteShops = openOrCreateDatabase("ICD", SalesItem.MODE_PRIVATE,null);


        //--------------------------------------------------------------------------------------
        //get full total start
        //--------------------------------------------------------------------------------------

        Cursor cDeal =sqLiteShops.rawQuery("SELECT SUM(Total),SUM(credit) FROM deal;",null);
        cDeal.moveToNext();

        total.setText("Grand Total "+cDeal.getString(0));
        cash.setText("Cash Total "+cDeal.getString(1));
        //--------------------------------------------------------------------------------------
        //get full total end
        //--------------------------------------------------------------------------------------



        Cursor c =sqLiteShops.rawQuery("SELECT * FROM item ;",null);

        int nRow = c.getCount();


        myList = new ArrayList<>();
        SingleRowForSalesItems singleRow;
        int i = 0;
        while (c.moveToNext()){
            singleRow = new SingleRowForSalesItems(c.getString(0),c.getString(1));

            myList.add(singleRow);


            i++;

        }

        search.addTextChangedListener(this);


        shopsListAdapter = new SalesItemListAdapter(this,myList);

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
