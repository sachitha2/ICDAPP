package com.example.icecreamdelivery;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CompleteInvoice extends AppCompatActivity implements TextWatcher {
    private RequestQueue requestQueueUpload;
    Button print;
    public  String mac;
    public  String val;
    public EditText editText;
    public EditText getCash;
    public String BILL = "";

    String cash;
    String json;
    String itemTotla;
    String invoiceN;
    String shopId;
    String ShopName;
    String DriverName;
    float preCredit;
    float totalWithCredit = 0;
    Date currentTime = Calendar.getInstance().getTime();
    private static final String TAG = "bluetooth1";

    //TextViews
    TextView txtInvoiceId;
    TextView itemTotal;
    TextView previousCredit;
    TextView totalCredit;
    //TextViews

    ///Font Helpers

    final String SPACES = "         ";//9
    final String uline = "________________________________________";
    final String dline = "----------------------------------------";
    //sachitha hirushan
    ///Font Helpers
    Button btnOn, btnOff;
    Bitmap bitmap;

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module (you must edit this line)
    private static String address;//02:2F:01:1E:CA:40

    SQLiteDatabase sqLiteSelectShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_invoice);
        mac = "02:2F:01:1E:CA:40";
        SharedPreferences sharedPreferencesSS = getSharedPreferences("prefs", Login.MODE_PRIVATE);
        final String MAC = sharedPreferencesSS.getString("MAC", "sam");
        Log.d("MAC",MAC);

        address = MAC;
        sqLiteSelectShop = openOrCreateDatabase("ICD", CompleteInvoice.MODE_PRIVATE,null);


        print = findViewById(R.id.btnPrint);

        getCash = findViewById(R.id.cash);
        shopId = getIntent().getStringExtra("ShopId");
        ShopName = getIntent().getStringExtra("ShopName");
        invoiceN = getIntent().getStringExtra("invoiceNumber");
        json = getIntent().getStringExtra("json");
        itemTotla = getIntent().getStringExtra("itemTotla");


        Log.d("json",json);

        setTitle("Print Bill");
        txtInvoiceId = findViewById(R.id.txtInvoiceId);
        itemTotal = findViewById(R.id.itemTotal);
        previousCredit = findViewById(R.id.previousCredit);
        totalCredit = findViewById(R.id.totalCredit);
        txtInvoiceId.setText(invoiceN);



        //get shop credit
        Cursor cForItems =sqLiteSelectShop.rawQuery("SELECT * FROM shop where id = '"+shopId+"' ;",null);
        cForItems.moveToNext();

        preCredit =Integer.valueOf(cForItems.getString(6));

        //get shop credit

        itemTotal.setText(itemTotla+"");
        previousCredit.setText(preCredit+"");

        totalWithCredit = (preCredit + Float.valueOf(itemTotla));
        totalCredit.setText((totalWithCredit) +"");

        ///get MAC




        getCash.addTextChangedListener(this);



        Toast.makeText(CompleteInvoice.this,"data "+mac,Toast.LENGTH_SHORT).show();

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cash = getCash.getText().toString();

                if(cash.length() == 0){
                        Log.d("Print","cash is empty");
                }else{
                    //updating deal table


                  sqLiteSelectShop.execSQL("UPDATE deal SET credit = '"+totalWithCredit+"',cash = "+cash+" WHERE id = '"+invoiceN+"';");

                    //Update data in deal table

                Log.d("Print Buton","Print Button clicked"+cash);

                //Make bill
                BILL =
                                "-----------------------------------------------\n"+
                                "                 Island Dairies                \n"+
                                "-----------------------------------------------\n"+
                                "  Address                    \n"+
                                "       E.M.S.K Gunarathna              \n" +
                                "       Track 06 angamuwa                \n" +
                                "       Rajanganaya               \n\n" +
                                "  Telephone:               \n" +
                                "       071-5888479,0711012888               \n"
                                +"-----------------------------------------------\n"
                                +"Invoice Number : "+invoiceN+"\n"
                                +"Driver         : "+DriverName+"\n"

                                +"Shop Name      : "+ShopName+"\n"
                                +"Shop Id        : "+shopId+"\n"
                                +"Date : "+currentTime+"\n"
                                +"-----------------------------------------------\n";


                BILL = BILL + String.format("%1$-10s %2$10s %3$13s %4$10s", "Item", "Qty", "Rate", "Total");
                BILL = BILL + "\n";
                BILL = BILL
                        + "-----------------------------------------------\n";
                String itemName,qty,rate ;
                double total;
                float sPrice ;
                int nItems = 0;
                float fullTotal = 0;
                try {
                    JSONObject obj = new JSONObject(json);
                    nItems = obj.length();
                    for(int x = 0;x < obj.length();x++){

                        JSONArray tmpJson = obj.getJSONArray(""+x+"");
                        itemName = tmpJson.getString(0);
                        rate = tmpJson.getString(1);
                        qty = tmpJson.getString(2);
//                        total = ();
                          sPrice = Float.parseFloat(rate);
                          total = sPrice * Integer.valueOf(qty);
                          fullTotal += total;
                        BILL = BILL + "  "+itemName+" \n";
                        BILL = BILL + "\n " + String.format("%1$20s %2$11s %3$10s", qty, rate, total+"")+"\n";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



                //        BILL = BILL + "  Testing String \n";

                BILL = BILL
                        + "\n-----------------------------------------------";
                BILL = BILL + "\n\n";

                BILL = BILL + "  Total Qty       :" + "     " + nItems + "\n";
                BILL = BILL + "  Total Value     :" + "     " + fullTotal + "\n";
                BILL = BILL + "  Previous credit :" + "     " + preCredit + "\n";
                BILL = BILL + "  Cash            :" + "     " + cash + "\n";
                BILL = BILL + "  Credit Forward  :" + "     " + "00.00" + "\n";

                BILL = BILL
                        + "-----------------------------------------------\n"
                        + "  Solution by\n"
                        + "  www.infinisolutionslk.com\n" +
                        "  077-1466460/071-5591137\n"
                        + "-----------------------------------------------\n";
                BILL = BILL + "\n\n ";


                //Make bill

                if(btAdapter == null){
                    Log.d("print bt ","Blutooth device not found");
                }else{

                    sendData(BILL);
                }


            }}
        });


        //Request que for volley
        requestQueueUpload = Volley.newRequestQueue(CompleteInvoice.this);



    }



    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "...onResume - try connect...");

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e1) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e1.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting...");
        try {
            btSocket.connect();
            Log.d(TAG, "...Connection ok...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Create Socket...");

        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
            }
        }

        try     {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
//            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                    //check net availability befor uploading

                    if(1 == 2){

                    }else{
                        //intent to next activity
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, 1);
                    }



            }
        }
    }

    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    private void sendData(String message) {
        byte[] msgBuffer = message.getBytes();

        Log.d(TAG, "...Send data: " + message + "...");

        try {
            outStream.write(msgBuffer);
            //Add sync function to here
            ///TODO
//            Intent intent = new Intent(CompleteInvoice.this, Shops.class);
//            startActivity(intent);


        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
            msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(getCash.getText().length() == 0){
            Log.d("MSG","Empty");
            totalCredit.setText((totalWithCredit)+"");
        }else{
            Log.d("MSG","Not Empty");
            totalCredit.setText((totalWithCredit - Float.valueOf(getCash.getText().toString()))+"");
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    ///Upload part
    public void jsonParseStockAndPriceRangeTable(final ProgressDialog progressDialog) throws JSONException {
        progressDialog.setMessage("Uploading data");
        String data = "sachitha hirushannn";

        JSONObject json = new JSONObject();
        JSONArray invoiceArray = new JSONArray();
        JSONArray invoiceItems = new JSONArray();






        Cursor cForDeals =sqLiteSelectShop.rawQuery("SELECT * FROM deal ;",null);

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

            Cursor cForInvoiceData = sqLiteSelectShop.rawQuery("SELECT * FROM invoice where dealId = '"+cForDeals.getString(0)+"' ;",null);
            JSONArray oneInvoice = new JSONArray();

            while(cForInvoiceData.moveToNext()){

                JSONObject invoiceItemData = new JSONObject();
                invoiceItemData.put("qty",cForInvoiceData.getString(3));
                invoiceItemData.put("p",cForInvoiceData.getString(4));
                invoiceItemData.put("iId",cForInvoiceData.getString(2));

                Log.d("invoiceData",cForInvoiceData.getString(3));

                Log.d("invoiceN", "jsonParseStockAndPriceRangeTable: "+invoiceItemData);
                oneInvoice.put(invoiceItemData);
//                       invoiceItemData.remove("qty");
//                       invoiceItemData.remove("p");
//                       invoiceItemData.remove("iId");

            }
            Log.d("invoiceN", "jsonParseStockAndPriceRangeTable: "+oneInvoice);
            invoiceItems.put(oneInvoice);


            if(i == 50){
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
        requestQueueUpload.add(requestDownloadStock);

    }


}
