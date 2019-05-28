package com.example.icecreamdelivery;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class Download extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabase;

    private RequestQueue requestQueueForStock, requestQueueForItem, requestQueueForShop, requestQueueForRoute;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        progressDialog = new ProgressDialog(Download.this);
        progressDialog.setTitle("Downloading Data....");
        progressDialog.setMessage("");
        progressDialog.setCancelable(false);
        progressDialog.show();

        requestQueueForStock = Volley.newRequestQueue(Download.this);
        requestQueueForItem = Volley.newRequestQueue(Download.this);
        requestQueueForShop = Volley.newRequestQueue(Download.this);
        requestQueueForRoute = Volley.newRequestQueue(Download.this);

        createDatabaseAndTables();

        jsonParseStockAndPriceRangeTable();
        jsonParseItemTable();
        jsonParseShopTable();
        jsonParseRouteTable();

    }

    public void createDatabaseAndTables(){

        sqLiteDatabase = openOrCreateDatabase("ICD", Download.MODE_PRIVATE,null);

        //Drop Table if Exist
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS stock;");

        // Creating Stock Table
        sqLiteDatabase.execSQL("CREATE TABLE IF  NOT EXISTS stock (" +
                "id int(6) NOT NULL," +
                "itemId int(6) NOT NULL," +
                "amount int(6) NOT NULL," +
                "rAmount int(6) NOT NULL," +
                "status int(1) NOT NULL);");//COMMENT '1=active,0=not active'


        //Drop Table if Exist
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS price_range;");

        // Creating Price Range Table
        sqLiteDatabase.execSQL("CREATE TABLE IF  NOT EXISTS price_range (" +
                "itemId int(6) NOT NULL," +
                "price float(6) NOT NULL );");


        //Drop Table if Exist
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS item;");

        // Creating Item Table
        sqLiteDatabase.execSQL("CREATE TABLE IF  NOT EXISTS item (" +
                "itemId int(6) NOT NULL," +
                "name varchar(50) NOT NULL );");


        //Drop Table if Exist
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS shop;");

        // Creating Shop Table
        sqLiteDatabase.execSQL("CREATE TABLE IF  NOT EXISTS shop (" +
                "id int(6) NOT NULL," +
                "name varchar(50) NOT NULL," +
                "address varchar(150)  ," +
                "tp varchar(10)  ," +
                "rootId int(4) NOT NULL," +
                "nic varchar(12) ," +
                "credit float(10) NOT NULL );");


        //Drop Table if Exist
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS route;");

        // Creating Route Table
        sqLiteDatabase.execSQL("CREATE TABLE IF  NOT EXISTS route (" +
                "id int(6) NOT NULL," +
                "name varchar(50) NOT NULL );");


        //Drop Table if Exist
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS invoice;");

        // Creating Invoice Table
        sqLiteDatabase.execSQL("CREATE TABLE IF  NOT EXISTS invoice (" +
                //"id int(11) PRIMARY KEY AUTOINCREMENT," +
                "id int(11) NOT NULL," +
                "dealId varchar(18) NOT NULL," +
                "itemId int(11) NOT NULL," +
                "amount int(11) NOT NULL," +
                "sPrice float(10) NOT NULL," +
                "shopId int(6) NOT NULL," +
                "stockId int(11) NOT NULL," +
                "date datetime NOT NULL," +
                "s int(1) NOT NULL );");


        //Drop Table if Exist
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS deal;");

        // Creating Deal Table
        sqLiteDatabase.execSQL("CREATE TABLE IF  NOT EXISTS deal (" +
                //"id bigint PRIMARY KEY AUTOINCREMENT," +
                "id varchar(18) NOT NULL," +
                "shopId int(6) NOT NULL," +
                "Total int(10) NOT NULL );");

    }

    //Downloads Stock and Price Range tables
    public void jsonParseStockAndPriceRangeTable(){

        String url = "http://icd.infinisolutionslk.com/JSONGetVehicleStock.php?uName=" + MainActivity.loggedAccount;

        JsonObjectRequest requestDownloadStock = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Read json and assign them to local variables

                            JSONArray Stock = response.getJSONArray("stock");

                            for (int i = 0; i < Stock.length(); i++){

                                JSONObject temp01 = Stock.getJSONObject(i);

                                sqLiteDatabase.execSQL("INSERT INTO stock (id, itemId, amount, rAmount, status) VALUES (" + temp01.getString("id") +
                                        ", "+temp01.getString("itemId")+
                                        ", "+temp01.getString("amount")+
                                        ", "+temp01.getString("amount")+
                                        ", 0);");



                                for(int j = 0; j < temp01.getJSONArray("priceRange").length(); j++){

                                    JSONArray pRangeArray = temp01.getJSONArray("priceRange");

                                    sqLiteDatabase.execSQL("INSERT INTO price_range (itemId, price) VALUES (" + temp01.getString("itemId") +
                                            ", "+pRangeArray.getString(j)+
                                            ");");

                                }

                            }

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

    //Downloads Item table
    public void jsonParseItemTable(){

        String url = "http://icd.infinisolutionslk.com/JSONGetItems.php";

        JsonObjectRequest requestDownloadItem = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Read json and assign them to local variables

                            JSONArray Id = response.getJSONArray("id");
                            JSONArray ItemName = response.getJSONArray("itemName");

                            for (int i = 0; i < Id.length(); i++){

                                sqLiteDatabase.execSQL("INSERT INTO item (itemId, name) VALUES (" + Id.getInt(i) +
                                        ", '"+ItemName.getString(i)+
                                        "');");

                            }

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
        requestQueueForItem.add(requestDownloadItem);

    }

    //Downloads Shop table
    public void jsonParseShopTable(){

        String url = "http://icd.infinisolutionslk.com/jsonGetShops.php";

        JsonObjectRequest requestDownloadShop = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Read json and assign them to local variables

                            JSONArray Shop = response.getJSONArray("shop");


                            for (int k = 0; k < Shop.length(); k++){

                                JSONObject temp02 = Shop.getJSONObject(k);

                                sqLiteDatabase.execSQL("INSERT INTO shop (id, name, address, tp, rootId, nic, credit) VALUES (" + temp02.getString("id") +
                                        ", '"+temp02.getString("name")+
                                        "', '"+temp02.getString("address")+
                                        "', '"+temp02.getString("tpNumber")+
                                        "', "+temp02.getString("rootId")+
                                        ", '"+temp02.getString("idCardN")+
                                        "', "+temp02.getString("credit")+
                                        ");");

                            }

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
        requestQueueForShop.add(requestDownloadShop);

    }

    //Downloads Route table
    public void jsonParseRouteTable(){

        String url = "http://icd.infinisolutionslk.com/JSONGetRoutes.php";

        JsonObjectRequest requestDownloadRoute = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray Id = response.getJSONArray("id");
                            JSONArray RouteName = response.getJSONArray("routeName");


                            for (int l = 0; l < Id.length(); l++){

                                sqLiteDatabase.execSQL("INSERT INTO route (id, name) VALUES (" + Id.getInt(l) +
                                        ", '"+RouteName.getString(l)+
                                        "');");

                            }

                            progressDialog.hide();
                            Toast.makeText(Download.this, "Download Complete", Toast.LENGTH_SHORT).show();

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
        requestQueueForRoute.add(requestDownloadRoute);

    }

}

//
//        String pRange = "";
//        for(int j = 0; j < temp01.getJSONArray("priceRange").length(); j++){
//          JSONArray pRangeArray = temp01.getJSONArray("priceRange");
//          pRange = pRange + pRangeArray.getInt(j);
//        }
//        priceRangeStock[i] = pRange;
