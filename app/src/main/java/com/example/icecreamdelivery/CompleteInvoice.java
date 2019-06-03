package com.example.icecreamdelivery;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CompleteInvoice extends AppCompatActivity {
    Button print;
    public  String mac;
    public  String val;
    public EditText editText;
    public EditText getCash;
    public String BILL = "";

    String cash;
    String invoiceN;
    String shopId;
    String ShopName;
    String DriverName;
    Date currentTime = Calendar.getInstance().getTime();
    private static final String TAG = "bluetooth1";



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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_invoice);
        mac = "02:2F:01:1E:CA:40";
        address = mac;
        print = findViewById(R.id.btnPrint);

        getCash = findViewById(R.id.cash);
        shopId = getIntent().getStringExtra("ShopId");
        ShopName = getIntent().getStringExtra("ShopName");
        invoiceN = getIntent().getStringExtra("invoiceNumber");

        setTitle("Print Bill");
        ///get MAC








        Toast.makeText(CompleteInvoice.this,"data "+mac,Toast.LENGTH_SHORT).show();

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cash = getCash.getText().toString();
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
                BILL = BILL + "  Testing String \n";
                BILL = BILL + "\n " + String.format("%1$20s %2$11s %3$10s", "5", "10", "50.00")+"\n";
                //        BILL = BILL + "  Testing String \n";

                BILL = BILL
                        + "\n-----------------------------------------------";
                BILL = BILL + "\n\n";

                BILL = BILL + "  Total Qty       :" + "      " + "85" + "\n";
                BILL = BILL + "  Total Value     :" + "     " + "700.00" + "\n";
                BILL = BILL + "  Previous credit :" + "     " + "700.00" + "\n";
                BILL = BILL + "  Cash            :" + "     " + cash + "\n";
                BILL = BILL + "  Credit Forward  :" + "     " + "700.00" + "\n";

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
                    checkBTState();
                    sendData(BILL);
                }


            }
        });






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
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
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
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 35 in the java code";
            msg = msg +  ".\n\nCheck that the SPP UUID: " + MY_UUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
    }

}
