package com.example.icecreamdelivery;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AllInvoices extends AppCompatActivity {
    SQLiteDatabase sqliteDB;
    TextView in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_invoices);
        sqliteDB = openOrCreateDatabase("ICD", SelectShop.MODE_PRIVATE,null);

        setTitle("All Invoices");

        Cursor cForInvoiceData =sqliteDB.rawQuery("SELECT * FROM invoice ;",null);

        int nRow = cForInvoiceData.getCount();


        in = findViewById(R.id.textView3);
        in.setText("Hello "+nRow);

    }
}
