package com.example.icecreamdelivery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

public class SalesItem extends AppCompatActivity {
    private ListView itemList;
    EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_item);
        setTitle("Sales - Items");

        itemList = findViewById(R.id.salesItemList);
        search = findViewById(R.id.searchSalesItems);




    }
}
