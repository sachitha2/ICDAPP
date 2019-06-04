package com.example.icecreamdelivery;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelectShop extends AppCompatActivity {

    private Spinner item;
    private Spinner price;
    private TextView txtInvoiceId;
    private Button btnAdd,btnCompleteI;
    private EditText edtQuantity;
    private LinearLayout itemList;

    int itemCount = 0;



    //bill invoice data
    String qty;
    String iPrice;


    //bill invoice data




    long time;

    private String ShopId,ShopName,Address,Contact,Root,SDate,IdNo,Credit;
    private String[] Id, ItemName, Prices;

    JSONObject invoice;

    SQLiteDatabase sqLiteSelectShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_shop);
        sqLiteSelectShop = openOrCreateDatabase("ICD", SelectShop.MODE_PRIVATE,null);
        ShopId= getIntent().getStringExtra("ShopId");
        ShopName= getIntent().getStringExtra("ShopName");

        setTitle(ShopName);

        txtInvoiceId = (TextView) findViewById(R.id.txtInvoiceId);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        edtQuantity = (EditText) findViewById(R.id.edtQuantity);
        itemList = (LinearLayout) findViewById(R.id.itemList);

        btnCompleteI = findViewById(R.id.btnCompleteI);
        time = System.currentTimeMillis();

        SharedPreferences sharedPreferencesSS = getSharedPreferences("loginInfo", Login.MODE_PRIVATE);
        final int vehicleId = sharedPreferencesSS.getInt("vehicleId", -1);
        txtInvoiceId.setText(vehicleId + "-" + time);
        invoice = new JSONObject();
        btnCompleteI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Goto Complete invoice activiy

                ///save data in table


                sqLiteSelectShop.execSQL("INSERT INTO invoice (id, dealId, itemId, amount,sPrice,shopId,stockId,date,s) VALUES (14, '12587', 250, 10,2,2502,25,'2019-12-12',0);");
                sqLiteSelectShop.execSQL("INSERT INTO deal (id, shopId, Total) VALUES ('"+vehicleId+"-"+time+"', "+ShopId+", 250);");
//                sqLiteSelectShop.execSQL("");


                ///save data in table

//                try {
//
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                Intent intent = new Intent(SelectShop.this, CompleteInvoice.class);
//                String message = mMessageEditText.getText().toString();

                intent.putExtra("ShopId", ShopId);
                intent.putExtra("ShopName", ShopName);
                intent.putExtra("invoiceNumber", vehicleId+"-"+time);
                startActivity(intent);
            }
        });





        //-------------- Items and Prices Spinners------------------------------------------------------------------------------------
        item = (Spinner) findViewById(R.id.item);
        price = (Spinner) findViewById(R.id.price);



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

    }

    public void onAddClick(View view) {

//        Log.d("Sachitha","1");

        JSONArray temp = new JSONArray();
        temp.put(item.getSelectedItem()+"");
        temp.put(price.getSelectedItem()+"");
        temp.put(edtQuantity.getText()+"");
        temp.put(Id[item.getSelectedItemPosition()]);

        try {
//            Log.d("Sachitha","2");
            invoice.put(itemCount+"",temp);//Id[item.getSelectedItemPosition()]
            itemCount++;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Sachitha", invoice.toString());

//        Log.d("Sachitha","3");

        //new code
//        Log.d("Sachitha","3");
        LinearLayout linearLayout = new LinearLayout(SelectShop.this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setPadding(0,20,0,20);


        TextView textView1 = new TextView(this);
        textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.4f));
        textView1.setText(item.getSelectedItem()+"");

        TextView textView2 = new TextView(this);
        textView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));
        textView2.setText(price.getSelectedItem()+"");

        TextView textView3 = new TextView(this);
        textView3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));
        textView3.setText(edtQuantity.getText()+"");

        TextView textView4 = new TextView(this);
        textView4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));

        qty = edtQuantity.getText()+"";

        textView4.setText(( Integer.valueOf(qty) * 5 )+"");


        //new code


//        LinearLayout linearLayout = new LinearLayout(SelectShop.this);
//        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//
//        TextView textView1 = new TextView(this);
//        textView1.setText(item.getSelectedItem()+"");
//
//        TextView textView2 = new TextView(this);
//        textView2.setText(price.getSelectedItem()+"");
//
//        TextView textView3 = new TextView(this);
//        textView3.setText(edtQuantity.getText()+"");

//        int tot = Integer.valueOf(price.getSelectedItem()+"") * Integer.parseInt(edtQuantity.getText().toString());

//        TextView textView4 = new TextView(this);
//        textView4.setText(tot+"");
        linearLayout.addView(textView1);
        linearLayout.addView(textView2);
        linearLayout.addView(textView3);
        linearLayout.addView(textView4);


        itemList.addView(linearLayout);

//        Log.d("Sachitha","6");

    }
}

//TODO Add button eke functions tika
//TODO complete invoice button eke functions tika

//        ShopId= getIntent().getStringExtra("ShopId");
//        ShopName= getIntent().getStringExtra("ShopName");
//        Address= getIntent().getStringExtra("Address");
//        Contact= getIntent().getStringExtra("Contact");
//        Root= getIntent().getStringExtra("Root");
//        SDate= getIntent().getStringExtra("SDate");
//        IdNo= getIntent().getStringExtra("IdNo");
//        Credit= getIntent().getStringExtra("Credit");