package com.example.icecreamdelivery;

import android.content.ClipData;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class Items extends AppCompatActivity {

    private ListView itemsList;

    SQLiteDatabase sqLiteItems;

    private String[] Id,ItemId,ItemName,Amount,RAmount,PriceRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        itemsList = (ListView) findViewById(R.id.itemsList);

        sqLiteItems = openOrCreateDatabase("ICD", Items.MODE_PRIVATE,null);

        Cursor cForStock =sqLiteItems.rawQuery("SELECT * FROM stock;",null);

        int nRow = cForStock.getCount();

        Id = new String[nRow];
        ItemId = new String[nRow];
        ItemName = new String[nRow];
        Amount = new String[nRow];
        RAmount = new String[nRow];
        PriceRange = new String[nRow];

        int i = 0;
        while (cForStock.moveToNext()){

            Cursor cForItemName =sqLiteItems.rawQuery("SELECT * FROM item WHERE itemId = "+cForStock.getString(1)+";",null);
            cForItemName.moveToFirst();

            Id[i] = "" + i;
            ItemId[i] = cForStock.getString(1);
            ItemName[i] = cForItemName.getString(1);
            Amount[i] = cForStock.getString(2);
            RAmount[i] = cForStock.getString(3);

            Cursor cForPriceRange =sqLiteItems.rawQuery("SELECT * FROM price_range WHERE itemId = "+cForStock.getInt(1)+";",null);

            String pRange = "";
            while (cForPriceRange.moveToNext()){

                pRange = pRange + cForPriceRange.getString(1) + "  ";

            }

            PriceRange[i] = pRange;

            i++;

        }

        ListViewAdapter itemsListViewAdapter = new ListViewAdapter(Items.this, Id, "Items", ItemId, ItemName, Amount, RAmount, PriceRange);
        itemsList.setAdapter(itemsListViewAdapter);

    }
}
