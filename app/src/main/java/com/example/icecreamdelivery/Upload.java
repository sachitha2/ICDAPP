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
        JSONObject manJson = new JSONObject();
        JSONArray myArr = new JSONArray();
        myArr.put(1);
        manJson.put("name", "emil");
        manJson.put("username", "emil111");
        manJson.put("age", "111");
        json.put("man",manJson);
        myArr.put(manJson);
        json.put("arr",myArr);

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
