package com.example.icecreamdelivery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SelectShop extends AppCompatActivity {

    private String ShopId,ShopName,Address,Contact,Root,SDate,IdNo,Credit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_shop);

        ShopId= getIntent().getStringExtra("ShopId");
        ShopName= getIntent().getStringExtra("ShopName");
        Address= getIntent().getStringExtra("Address");
        Contact= getIntent().getStringExtra("Contact");
        Root= getIntent().getStringExtra("Root");
        SDate= getIntent().getStringExtra("SDate");
        IdNo= getIntent().getStringExtra("IdNo");
        Credit= getIntent().getStringExtra("Credit");

        setTitle(ShopName);

    }
}
