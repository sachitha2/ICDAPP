package com.example.icecreamdelivery;

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
    private TextView total;
    public  float fullTotal;

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
        total = findViewById(R.id.total);
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
                String itemId;
                String amount;

                for(int x = 0;x < invoice.length();x++){
                    try {
                        JSONArray tmpJson = invoice.getJSONArray(""+x+"");
                        sqLiteSelectShop.execSQL("INSERT INTO invoice (id, dealId, itemId, amount,sPrice,shopId,stockId,date,s) VALUES (100, '"+vehicleId+"-"+time+"',"+tmpJson.getString(3)+" , "+tmpJson.getString(2)+","+tmpJson.getString(1)+",2502,25,'2019-12-12',0);");
                        Log.d("json arr", "onClick: "+tmpJson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                sqLiteSelectShop.execSQL("INSERT INTO deal (id, shopId, Total, credit, cash,s,date) VALUES ('"+vehicleId+"-"+time+"', "+ShopId+", "+fullTotal+",25,25,2,'2018-05-05');");
                Log.d("Reading json object", "onClick: "+invoice);
                Log.d("Reading json object L", "onClick: length of json object"+invoice.length());
//                sqLiteSelectShop.execSQL("");


                ///save data in table

                Intent intent = new Intent(SelectShop.this, CompleteInvoice.class);
//                String message = mMessageEditText.getText().toString();

                intent.putExtra("ShopId", ShopId);
                intent.putExtra("ShopName", ShopName);
                intent.putExtra("invoiceNumber", vehicleId+"-"+time);
                intent.putExtra("json",invoice.toString());
                intent.putExtra("itemTotla",fullTotal+"");
                startActivity(intent);
                SelectShop.this.finish();
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
        if(edtQuantity.getText().length() != 0){

        if (price.getSelectedItem() == null) {
            Log.d("Sachitha","Price is null");
        } else {


            JSONArray temp = new JSONArray();
            temp.put(item.getSelectedItem() + "");
            temp.put(price.getSelectedItem() + "");
            temp.put(edtQuantity.getText() + "");
            temp.put(Id[item.getSelectedItemPosition()]);

            try {
                invoice.put(itemCount + "", temp);//Id[item.getSelectedItemPosition()]
                itemCount++;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("Sachitha", invoice.toString());


            //new code
            LinearLayout linearLayout = new LinearLayout(SelectShop.this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setPadding(0, 20, 0, 20);


            TextView textView1 = new TextView(this);
            textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.4f));
            textView1.setText(item.getSelectedItem() + "");

            TextView textView2 = new TextView(this);
            textView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));
            textView2.setText(price.getSelectedItem() + "");

            TextView textView3 = new TextView(this);
            textView3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));
            textView3.setText(edtQuantity.getText() + "");

            TextView textView4 = new TextView(this);
            textView4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));

            qty = edtQuantity.getText() + "";
            edtQuantity.setText("");
            float sPrice = Float.parseFloat(price.getSelectedItem().toString());
            textView4.setText((Integer.valueOf(qty) * sPrice) + "");

            fullTotal += (Integer.valueOf(qty) * sPrice);

            total.setText("Total \n" + fullTotal + "");

            //new code

            linearLayout.addView(textView1);
            linearLayout.addView(textView2);
            linearLayout.addView(textView3);
            linearLayout.addView(textView4);


            itemList.addView(linearLayout);

        }
    }
    }
}

