package com.example.icecreamdelivery;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        sqLiteDatabase = openOrCreateDatabase("ICD", Upload.MODE_PRIVATE,null);

        progressDialog = new ProgressDialog(Upload.this);
        progressDialog.setTitle("Uploading Data....");
        progressDialog.setMessage("");
        progressDialog.setCancelable(false);
        progressDialog.show();


        requestQueueForStock = Volley.newRequestQueue(Upload.this);
        try {
            jsonParseStockAndPriceRangeTable(progressDialog);
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
        JSONObject invoiceItemData = new JSONObject();

        JSONObject invoiceData = new JSONObject();


        Cursor cForDeals =sqLiteDatabase.rawQuery("SELECT * FROM deal ;",null);

        int nRow = cForDeals.getCount();



        int i=0;
        while (cForDeals.moveToNext()){

            invoiceData.put("i",cForDeals.getString(0));//invoice
            invoiceData.put("c",cForDeals.getString(3));//credit
            invoiceData.put("t",cForDeals.getString(2));//total
            invoiceData.put("s",cForDeals.getString(1));//shop id
            invoiceData.put("cash",cForDeals.getString(4));//cash
            invoiceData.put("d","2018-10-10");//date
            invoiceArray.put(invoiceData);

            Cursor cForInvoiceData = sqLiteDatabase.rawQuery("SELECT * FROM invoice ;",null);
                   cForInvoiceData.moveToNext();
                   invoiceItemData.put("qty","25");
                   invoiceItemData.put("p","25.00");
                   invoiceItemData.put("iId","47");
                   invoiceItems.put(invoiceItemData);


            if(i == 50){
                break;
            }
            i++;
        }






        ///////////////////////invoice item part






        json.put("invoice",invoiceArray);
        json.put("invoiceItem",invoiceItems);

        Log.d("JSON","json"+json);

        String url = "http://icd.infinisolutionslk.com/JSONGetTransaction.php?data="+json.toString();
        Log.d("JSON URL",url);
        JsonObjectRequest requestDownloadStock = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Read json and assign them to local variables
                            progressDialog.hide();
                            JSONArray Status = response.getJSONArray("Status");
                            Log.d("Upload ","In uploading"+Status);

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
