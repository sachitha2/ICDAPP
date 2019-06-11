package com.example.icecreamdelivery;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class ViewAInvoice extends AppCompatActivity {
    public  String invoiceId;
    private LinearLayout itemList;
    SQLiteDatabase sqLiteDatabase;



    Date currentTime = Calendar.getInstance().getTime();

    public String BILL = "";

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //For bluetooth
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // will show the statuses like bluetooth open, close or data sent
    TextView myLabel;

    // will enable user to enter any text to be printed
    EditText myTextbox;

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //For bluetooth
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////


    public TextView totalTxt;
    public TextView txtCash;
    public TextView txtCredit;
    public Button btnCompleteI;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ainvoice);

        invoiceId = getIntent().getStringExtra("InvoiceId");

        sqLiteDatabase = openOrCreateDatabase("ICD", CompleteInvoice.MODE_PRIVATE,null);

        Cursor cForDeal =sqLiteDatabase.rawQuery("SELECT * FROM deal where id = '"+invoiceId+"' ;",null);
        cForDeal.moveToNext();

        Log.d("SQL",cForDeal.getString(0)+"");


        final ProgressDialog progressDialog = new ProgressDialog(ViewAInvoice.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("");
        progressDialog.setCancelable(true);




        //------------------------------------------------------------------------------------------
        //Creating bill start
        //------------------------------------------------------------------------------------------
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
                        +"Invoice Number : "+invoiceId+"\n"
                        +"Driver         : \n"

                        +"Shop Name      : "+cForDeal.getString(1)+"\n"
                        +"Shop Id        : "+cForDeal.getString(1)+"\n"
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
//        try {
//            JSONObject obj = new JSONObject(json);
//            nItems = obj.length();
//            for(int x = 0;x < obj.length();x++){
//
//                JSONArray tmpJson = obj.getJSONArray(""+x+"");
//                itemName = tmpJson.getString(0);
//                rate = tmpJson.getString(1);
//                qty = tmpJson.getString(2);
////                        total = ();
//                sPrice = Float.parseFloat(rate);
//                total = sPrice * Integer.valueOf(qty);
//                fullTotal += total;
//                BILL = BILL + "  "+itemName+" \n";
//                BILL = BILL + "\n " + String.format("%1$20s %2$11s %3$10s", qty, rate, total+"")+"\n";
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }



        //        BILL = BILL + "  Testing String \n";

        BILL = BILL
                + "\n-----------------------------------------------";
        BILL = BILL + "\n\n";

        BILL = BILL + "  Total Qty       :" + "     " + nItems + "\n";
        BILL = BILL + "  Total Value     :" + "     " + fullTotal + "\n";
        BILL = BILL + "  Previous credit :" + "     " + fullTotal + "\n";
        BILL = BILL + "  Cash            :" + "     " + fullTotal + "\n";
        BILL = BILL + "  Credit Forward  :" + "     " + "00.00" + "\n";

        BILL = BILL
                + "-----------------------------------------------\n"
                + "  Solution by\n"
                + "  www.infinisolutionslk.com\n" +
                "  077-1466460/071-5591137\n"
                + "-----------------------------------------------\n";
        BILL = BILL + "\n\n ";

        //------------------------------------------------------------------------------------------
        //Creating bill end
        //------------------------------------------------------------------------------------------

        //open database
        sqLiteDatabase = openOrCreateDatabase("ICD", CompleteInvoice.MODE_PRIVATE,null);

        itemList = (LinearLayout) findViewById(R.id.itemList);



        setTitle("View Invoice : "+invoiceId);

        //
        totalTxt = findViewById(R.id.txtTotal);
        txtCash = findViewById(R.id.txtCash);
        txtCredit = findViewById(R.id.txtCredit);


        //buton
        btnCompleteI = findViewById(R.id.btnCompleteI);

        //get invoice as json


        //get invoice as json
        ///TODO here

        btnCompleteI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //this is print old bill
                progressDialog.show();

                try {

                    findBT();
                    openBT();


                    sendData(BILL,progressDialog);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

        //Read deal start
        Cursor cDeal =sqLiteDatabase.rawQuery("SELECT * FROM deal where id = '"+invoiceId+"' ;",null);
        cDeal.moveToNext();
        txtCash.setText("Cash : "+cDeal.getString(4));
        txtCredit.setText("Credit :"+cDeal.getString(3));
        //Read deal end



        //get total
        Cursor totalCash =sqLiteDatabase.rawQuery("SELECT SUM(amount*sPrice) FROM invoice where dealId = '"+invoiceId+"' ;",null);
        totalCash.moveToNext();
        totalTxt.setText("Total : "+totalCash.getFloat(0));

        Cursor cForInvoice =sqLiteDatabase.rawQuery("SELECT * FROM invoice where dealId = '"+invoiceId+"' ;",null);

        Log.d("DB",""+cForInvoice);

        //this is view invoice

        while ( cForInvoice.moveToNext()){
            LinearLayout linearLayout = new LinearLayout(ViewAInvoice.this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setPadding(0, 20, 0, 20);


            TextView textView1 = new TextView(this);
            textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.4f));
            textView1.setText(cForInvoice.getString(2)+"");

            TextView textView2 = new TextView(this);
            textView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));
            textView2.setText(cForInvoice.getString(4)+"");

            TextView textView3 = new TextView(this);
            textView3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));
            textView3.setText( cForInvoice.getString(3)+"");

            TextView textView4 = new TextView(this);
            textView4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));


            textView4.setText( (cForInvoice.getInt(3) * cForInvoice.getFloat(4))+"");

            linearLayout.addView(textView1);
            linearLayout.addView(textView2);
            linearLayout.addView(textView3);
            linearLayout.addView(textView4);


            itemList.addView(linearLayout);


        }






        //this is view invoice


    }
    //--------------------------------------------------------------------------------------------------
    //For Bluetooth
//--------------------------------------------------------------------------------------------------
// close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
//        myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
//                myLabel.setText("No bluetooth adapter available");
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals("MTP-3")) {
                        mmDevice = device;
                        break;
                    }
                }
            }

//            myLabel.setText("Bluetooth device found.");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // tries to open a connection to the bluetooth printer device
    void openBT() throws IOException {
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

//            myLabel.setText("Bluetooth Opened");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     * after opening a connection to bluetooth printer device,
     * we have to listen and check if a data were sent to be printed.
     */
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
//                                                myLabel.setText(data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // this will send text data to be printed by the bluetooth printer
    void sendData(String BILL, final ProgressDialog o) throws IOException {
        try {

            // the text typed by the user
            String msg ;
            msg = BILL;
            mmOutputStream.write(msg.getBytes());
            //Added by chata
//            closeBT();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after 3 seconds
                    o.hide();
//                    CompleteInvoice.this.finish();
                    try {
                        closeBT();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 3000);

            // tell the user data were sent

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//--------------------------------------------------------------------------------------------------
    //For bluetooth
//--------------------------------------------------------------------------------------------------
}
