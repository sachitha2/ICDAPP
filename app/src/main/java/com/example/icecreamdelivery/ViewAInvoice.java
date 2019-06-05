package com.example.icecreamdelivery;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewAInvoice extends AppCompatActivity {
    public  String invoiceId;
    private LinearLayout itemList;
    SQLiteDatabase sqLiteDatabase;

    public TextView totalTxt;
    public TextView txtCash;
    public TextView txtCredit;
    public Button btnCompleteI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ainvoice);
        //open database
        sqLiteDatabase = openOrCreateDatabase("ICD", CompleteInvoice.MODE_PRIVATE,null);

        itemList = (LinearLayout) findViewById(R.id.itemList);


        invoiceId = getIntent().getStringExtra("InvoiceId");
        setTitle("View Invoice : "+invoiceId);

        //
        totalTxt = findViewById(R.id.txtTotal);
        txtCash = findViewById(R.id.txtCash);
        txtCredit = findViewById(R.id.txtCredit);


        //buton
        btnCompleteI = findViewById(R.id.btnCompleteI);

        btnCompleteI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}
