package com.example.icecreamdelivery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class Items extends AppCompatActivity {

    private ListView itemsList;

    private String[] Id,ItemId,ItemName,BuyingPrice,Amount,Date,PriceRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        itemsList = (ListView) findViewById(R.id.itemsList);

        //name = new String[Lname.length()];
        Id = new String[10];
        ItemId = new String[10];
        ItemName = new String[10];
        BuyingPrice = new String[10];
        Amount = new String[10];
        Date = new String[10];
        PriceRange = new String[10];

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
