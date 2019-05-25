package com.example.icecreamdelivery;

import android.content.ClipData;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SelectShop extends AppCompatActivity {

    private Spinner item;
    private Spinner price;
    private TextView txtInvoiceId;

    long time;

    private String ShopId,ShopName,Address,Contact,Root,SDate,IdNo,Credit;
    private String[] Id, ItemName, Prices;

    SQLiteDatabase sqLiteSelectShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_shop);

        txtInvoiceId = (TextView) findViewById(R.id.txtInvoiceId);
        time = System.currentTimeMillis();
        SharedPreferences sharedPreferencesSS = getSharedPreferences("loginInfo", Login.MODE_PRIVATE);
        int vehicleId = sharedPreferencesSS.getInt("vehicleId", -1);
        txtInvoiceId.setText(vehicleId + "-" + time);

        //-------------- Items and Prices Spinners------------------------------------------------------------------------------------
        item = (Spinner) findViewById(R.id.item);
        price = (Spinner) findViewById(R.id.price);

        sqLiteSelectShop = openOrCreateDatabase("ICD", SelectShop.MODE_PRIVATE,null);

        Cursor cForItems =sqLiteSelectShop.rawQuery("SELECT * FROM item ;",null);

        int nRow = cForItems.getCount();

        Id = new String[nRow];
        ItemName = new String[nRow];

        int i=0;
        while (cForItems.moveToNext()){

            Id[i] = cForItems.getString(0);
            ItemName[i] = cForItems.getString(1);

            i++;
        }

        ArrayAdapter<String> ItemAdaptor = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ItemName);
        item.setAdapter(ItemAdaptor);


        item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                Cursor cForPrices =sqLiteSelectShop.rawQuery("SELECT * FROM price_range WHERE itemId = "+Id[item.getSelectedItemPosition()]+" ;",null);

                int nRowPrices = cForPrices.getCount();

                Prices = new String[nRowPrices];

                int j=0;
                while (cForPrices.moveToNext()){

                    Prices[j] = cForPrices.getFloat(1) + "";

                    j++;
                }

                ArrayAdapter<String> PriceAdaptor = new ArrayAdapter<String>(SelectShop.this, android.R.layout.simple_spinner_dropdown_item, Prices);
                price.setAdapter(PriceAdaptor);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        //-------------- End of Items and Prices Spinners------------------------------------------------------------------------------------

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
