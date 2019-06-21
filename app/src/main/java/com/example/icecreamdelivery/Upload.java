package com.example.icecreamdelivery;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class Upload extends AppCompatActivity {
    private RequestQueue requestQueueForStock;
    ProgressDialog progressDialog;
    SQLiteDatabase sqLiteDatabase;
    TextView txtUploaded;
    int step = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        sqLiteDatabase = openOrCreateDatabase("ICD", Upload.MODE_PRIVATE,null);


        txtUploaded = findViewById(R.id.txtUploaded);


        progressDialog = new ProgressDialog(Upload.this);
        progressDialog.setTitle("Uploading Data....");
        progressDialog.setMessage("STEP "+step);
        progressDialog.setCancelable(false);



        requestQueueForStock = Volley.newRequestQueue(Upload.this);
        try {
            //looking for status start
                Cursor cForDeals =sqLiteDatabase.rawQuery("SELECT * FROM deal where s = 0  AND date =  date('now','localtime');",null);
                int nRow = cForDeals.getCount();
                Log.d("Data",nRow + "");


//            Cursor CForNR =sqLiteDatabase.rawQuery("SELECT * FROM deal WHERE s = 0 ;",null);

            int CForNRNum = cForDeals.getCount();
            txtUploaded.setText("To upload "+CForNRNum);

            if(CForNRNum != 0){
                jsonParseStockAndPriceRangeTable(progressDialog);
                progressDialog.show();
            }else{
                Log.d("SQL","Nodata to upload");
            }



            //looking for status End

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void jsonParseStockAndPriceRangeTable(final ProgressDialog progressDialog) throws JSONException {
        progressDialog.setMessage("Uploading data");
        String data = "sachitha hirushannn";

        JSONObject json = new JSONObject();
        JSONArray invoiceArray = new JSONArray();
        JSONArray invoiceItems = new JSONArray();






        Cursor cForDeals =sqLiteDatabase.rawQuery("SELECT * FROM deal WHERE s = 0 AND date =  date('now','localtime');",null);

        int nRow = cForDeals.getCount();



        int i=0;
        while (cForDeals.moveToNext()){
            JSONObject invoiceData = new JSONObject();
            invoiceData.put("i",cForDeals.getString(0));//invoice
            invoiceData.put("c",cForDeals.getString(3));//credit
            invoiceData.put("t",cForDeals.getString(2));//total
            invoiceData.put("s",cForDeals.getString(1));//shop id
            invoiceData.put("cash",cForDeals.getString(4));//cash
            invoiceData.put("d","2018-10-10");//date
            Log.d("invoice", "jsonParseStockAndPriceRangeTable: "+invoiceData);
            invoiceArray.put(invoiceData);

            Cursor cForInvoiceData = sqLiteDatabase.rawQuery("SELECT * FROM invoice where dealId = '"+cForDeals.getString(0)+"' ;",null);
                    JSONArray oneInvoice = new JSONArray();

                   while(cForInvoiceData.moveToNext()){
                            Cursor cForStock = sqLiteDatabase.rawQuery("SELECT * FROM stock where itemId = '"+cForInvoiceData.getString(2)+"' ;",null);
                                   cForStock.moveToNext();
                       JSONObject invoiceItemData = new JSONObject();
                       invoiceItemData.put("qty",cForInvoiceData.getString(3));
                       invoiceItemData.put("p",cForInvoiceData.getString(4));
                       invoiceItemData.put("iId",cForInvoiceData.getString(2));
                       invoiceItemData.put("sId",cForStock.getString(0));
                       invoiceItemData.put("lS",cForStock.getString(2));

                       Log.d("invoiceData",cForInvoiceData.getString(3));

                       Log.d("invoiceN", "jsonParseStockAndPriceRangeTable: "+invoiceItemData);
                       oneInvoice.put(invoiceItemData);
//                       invoiceItemData.remove("qty");
//                       invoiceItemData.remove("p");
//                       invoiceItemData.remove("iId");

                   }
                    Log.d("invoiceN", "jsonParseStockAndPriceRangeTable: "+oneInvoice);
                    invoiceItems.put(oneInvoice);


            if(i == 10){
                break;
            }
            i++;
        }






        ///////////////////////invoice item part






        json.put("invoice",invoiceArray);
        json.put("invoiceItem",invoiceItems);

        Log.d("JSON","json"+json);

        String url = "http://icd.infinisolutionslk.com/JSONGetT.php?data="+json.toString();
        Log.d("JSON URL",url);
        JsonObjectRequest requestDownloadStock = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Read json and assign them to local variables
                            progressDialog.hide();
                            JSONArray invoices = response.getJSONArray("invoices");

                            Log.d("Upload ","In uploading"+invoices);
                            Log.d("Upload ","In uploading"+invoices.length());

                            for(int i = 0;i < invoices.length();i++){
                                Log.d("Upload ","In uploading"+invoices.get(i));
                                if(invoices.get(i).equals("NO")){

                                }else{
                                    sqLiteDatabase.execSQL("UPDATE deal SET  s = 1 WHERE id = '"+invoices.get(i)+"';");
                                }

                            }

                            Cursor cForDeals =sqLiteDatabase.rawQuery("SELECT * FROM deal where s = 0  AND date =  date('now','localtime');",null);

                            //check here are there any data to upload
                            ///TODO
                            int CForNRNum = cForDeals.getCount();
                            txtUploaded.setText("To upload "+CForNRNum);

                            if(CForNRNum != 0){
                                step++;
                                progressDialog.setMessage("STEP "+step);
                                jsonParseStockAndPriceRangeTable(progressDialog);
                                progressDialog.show();
                            }else{
                                Log.d("SQL","Nodata to upload");
                            }

                            txtUploaded.setText("To upload "+CForNRNum);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueueForStock.add(requestDownloadStock);

    }
}
