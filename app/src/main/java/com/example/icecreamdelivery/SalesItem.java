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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class SalesItem extends AppCompatActivity  implements TextWatcher {


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
    public String BILL = "";
    Date currentTime = Calendar.getInstance().getTime();
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //For bluetooth
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////


    Button btnCompleteI;

    private ListView itemList;
    EditText search;
    TextView total;
    TextView cash;
    private ListView shopsList;

    //chatson
    ArrayList<SingleRowForSalesItems> myList;
    SalesItemListAdapter shopsListAdapter;
    //chatson

    SQLiteDatabase sqLiteShops;

    private String[] Id,ShopId,ShopName,Address,Contact,Root,IdNo,Credit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_item);
        setTitle("Sales - Items");


        final ProgressDialog progressDialog = new ProgressDialog(SalesItem.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("");
        progressDialog.setCancelable(false);

        itemList = findViewById(R.id.salesItemList);
        search = findViewById(R.id.searchSalesItems);
        total = findViewById(R.id.txtTotal);
        cash = findViewById(R.id.txtCashTotal);

        shopsList = itemList;

        sqLiteShops = openOrCreateDatabase("ICD", SalesItem.MODE_PRIVATE,null);


        //--------------------------------------------------------------------------------------
        //get full total start
        //--------------------------------------------------------------------------------------

        Cursor cDeal =sqLiteShops.rawQuery("SELECT SUM(Total),SUM(credit),SUM(cash) FROM deal WHERE (s = 0 OR s = 1) AND date = date('now','localtime');",null);
        cDeal.moveToNext();

        total.setText("Grand Total "+cDeal.getString(0));
        cash.setText("Cash Total "+cDeal.getString(2));
        //--------------------------------------------------------------------------------------
            //get full total end
        //--------------------------------------------------------------------------------------



        Cursor c =sqLiteShops.rawQuery("SELECT * FROM item ;",null);

        int nRow = c.getCount();


        myList = new ArrayList<>();
        SingleRowForSalesItems singleRow;
        int i = 0;


        ///----------------------------------------------------------------------------------------
        ///Making Report
        ///----------------------------------------------------------------------------------------
        BILL =
                         "-----------------------------------------------\n"+
                         "                 Island Dairies                \n"+
                         "-----------------------------------------------\n"
                        +"-----------------------------------------------\n"
                        +"Date : "+currentTime+"\n"
                        +"-----------------------------------------------\n";


        BILL = BILL + String.format("%1$-10s %2$10s %3$13s %4$10s", "Item", "Qty", "", "Total");
        BILL = BILL + "\n";
        BILL = BILL
                + "-----------------------------------------------\n";







        ///----------------------------------------------------------------------------------------
        ///Making Report END
        ///----------------------------------------------------------------------------------------









        while (c.moveToNext()){

            //Load Soled amount
            Cursor CForInvoice =sqLiteShops.rawQuery("SELECT SUM(amount), SUM(amount*sPrice) FROM invoice WHERE itemId = "+c.getString(0)+" AND date = date('now','localtime') AND s = 1 ;",null);

            CForInvoice.moveToNext();


            singleRow = new SingleRowForSalesItems(c.getString(0),c.getString(1),CForInvoice.getString(0),CForInvoice.getString(1));

            myList.add(singleRow);


            if(CForInvoice.getString(0) != null){
                BILL = BILL + "  "+c.getString(1)+" \n";
                BILL = BILL + "\n " + String.format("%1$20s %2$11s %3$10s", CForInvoice.getString(0), "", CForInvoice.getString(1))+"\n";
                Log.d("helloo","NULLLL");
            }




            i++;

        }


        BILL = BILL
                + "\n-----------------------------------------------";
        BILL = BILL + "\n\n";


        BILL = BILL
                + "-----------------------------------------------\n"
                + "  Solution by\n"
                + "  www.infinisolutionslk.com\n" +
                "  077-1466460/071-5591137\n"
                + "-----------------------------------------------\n";
        BILL = BILL + "\n\n ";

        search.addTextChangedListener(this);


        shopsListAdapter = new SalesItemListAdapter(this,myList);

        shopsList.setAdapter(shopsListAdapter);



        btnCompleteI = findViewById(R.id.btnCompleteI);
        btnCompleteI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BTN","btn clicked");
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


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.shopsListAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

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
