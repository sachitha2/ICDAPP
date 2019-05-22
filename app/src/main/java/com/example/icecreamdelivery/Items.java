package com.example.icecreamdelivery;

import android.content.ClipData;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class Items extends AppCompatActivity {

    private ListView itemsList;

    SQLiteDatabase sqLiteItems;

    private String[] Id,ItemId,ItemName,BuyingPrice,Amount,Date,PriceRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        itemsList = (ListView) findViewById(R.id.itemsList);

//        sqLiteItems = openOrCreateDatabase("ICD", Items.MODE_PRIVATE,null);
//
//        Cursor c =sqLiteItems.rawQuery("SELECT * FROM item;",null);
//
//        int nRow = c.getCount();
        int nRow = 10;

        Id = new String[nRow];
        ItemId = new String[nRow];
        ItemName = new String[nRow];
        BuyingPrice = new String[nRow];
        Amount = new String[nRow];
        Date = new String[nRow];
        PriceRange = new String[nRow];

        for (int i = 0; i < 10; i++){
            Id[i] = "" + i;
            ItemId[i] = "319";
            ItemName[i] = "Item Name Here";
            BuyingPrice[i] = "210.33";
            Amount[i] = "150";
            Date[i] = "2019-05-17";
            PriceRange[i] = "250.35, 300.60, 285.43";
        }

        ListViewAdapter itemsListViewAdapter = new ListViewAdapter(Items.this, Id, "Items", ItemId, ItemName, BuyingPrice, Amount, Date, PriceRange);
        itemsList.setAdapter(itemsListViewAdapter);

    }
}
