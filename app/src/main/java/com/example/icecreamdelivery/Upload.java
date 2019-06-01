package com.example.icecreamdelivery;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        requestQueueForStock = Volley.newRequestQueue(Upload.this);
        try {
            jsonParseStockAndPriceRangeTable();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void jsonParseStockAndPriceRangeTable() throws JSONException {
        String data = "sachitha hirushannn";

        JSONObject json = new JSONObject();
        JSONArray invoiceArray = new JSONArray();
        JSONArray invoiceItems = new JSONArray();
        JSONObject invoiceItemData = new JSONObject();

        JSONObject invoiceData = new JSONObject();

        invoiceData.put("invoiceNumber","3-25800");
        invoiceData.put("credit","450");
        invoiceData.put("total","900");
        invoiceData.put("shopId","258");
        invoiceData.put("cash","450");
        invoiceData.put("date","2018-10-10");

        invoiceArray.put(invoiceData);
        invoiceArray.put(invoiceData);
        invoiceArray.put(invoiceData);
        invoiceArray.put(invoiceData);

        ///////////////////////invoice item part

        invoiceItemData.put("qty","25");
        invoiceItemData.put("price","25.00");
        invoiceItemData.put("itemId","47");


        invoiceItems.put(invoiceItemData);
        invoiceItems.put(invoiceItemData);
        invoiceItems.put(invoiceItemData);
        invoiceItems.put(invoiceItemData);

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
