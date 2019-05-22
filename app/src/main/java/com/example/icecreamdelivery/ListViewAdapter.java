package com.example.icecreamdelivery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewAdapter extends BaseAdapter {

    Context context;
    String[] Id;
    String From;

    String[] ItemId;
    String[] ItemName;
    String[] BuyingPrice;
    String[] Amount;
    String[] Date;
    String[] PriceRange;

    String[] ShopId;
    String[] ShopName;
    String[] Address;
    String[] Contact;
    String[] Root;
    String[] IdNo;
    String[] Credit;

    public ListViewAdapter(Context context, String[] Id, String From, String[] ItemId, String[] ItemName, String[] BuyingPrice, String[] Amount, String[] Date, String[] PriceRange){

        this.context = context;
        this.Id = Id;
        this.From = From;

        this.ItemId = ItemId;
        this.ItemName = ItemName;
        this.BuyingPrice = BuyingPrice;
        this.Amount = Amount;
        this.Date = Date;
        this.PriceRange = PriceRange;

    }

    public ListViewAdapter(Context context, String[] Id, String From, String[] ShopId, String[] ShopName, String[] Address, String[] Contact, String[] Root, String[] IdNo, String[] Credit){

        this.context = context;
        this.Id = Id;
        this.From = From;

        this.ShopId = ShopId;
        this.ShopName = ShopName;
        this.Address = Address;
        this.Contact = Contact;
        this.Root = Root;
        this.IdNo = IdNo;
        this.Credit = Credit;

    }

    @Override
    public int getCount() {
        return Id.length;
    }

    @Override
    public Object getItem(int position) {
        return Id[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater myInflater = LayoutInflater.from(context);
        View btnView = myInflater.inflate(R.layout.items_list_layout,null);

        switch (From){

            case "Items":

                btnView = myInflater.inflate(R.layout.items_list_layout,null);

                TextView itemId = (TextView)btnView.findViewById(R.id.txtItemId);
                TextView itemName = (TextView)btnView.findViewById(R.id.txtItemName);
                TextView buyingPrice = (TextView)btnView.findViewById(R.id.txtBuyingPrice);
                TextView amount = (TextView)btnView.findViewById(R.id.txtAmount);
                TextView date = (TextView)btnView.findViewById(R.id.txtDate);
                TextView priceRange = (TextView)btnView.findViewById(R.id.txtPriceRange);

                itemId.setText(ItemId[position]);
                itemName.setText(ItemName[position]);
                buyingPrice.setText(BuyingPrice[position]);
                amount.setText(Amount[position]);
                date.setText(Date[position]);
                priceRange.setText(PriceRange[position]);

                break;

            case "Shops":

                btnView = myInflater.inflate(R.layout.shops_list_layout,null);

                TextView shopId = (TextView)btnView.findViewById(R.id.txtShopId);
                TextView shopName = (TextView)btnView.findViewById(R.id.txtShopName);
                TextView address = (TextView)btnView.findViewById(R.id.txtAddress);
                TextView contact = (TextView)btnView.findViewById(R.id.txtContact);
                TextView root = (TextView)btnView.findViewById(R.id.txtRoot);
                TextView idNo = (TextView)btnView.findViewById(R.id.txtIdNo);
                TextView credit = (TextView)btnView.findViewById(R.id.txtCredit);

                shopId.setText(ShopId[position]);
                shopName.setText(ShopName[position]);
                address.setText(Address[position]);
                contact.setText(Contact[position]);
                root.setText(Root[position]);
                idNo.setText(IdNo[position]);
                credit.setText(Credit[position]);

                btnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intentSelectShop = new Intent(context, SelectShop.class);
                        intentSelectShop.putExtra("ShopId", ShopId[position]);
                        intentSelectShop.putExtra("ShopName", ShopName[position]);
                        intentSelectShop.putExtra("Address", Address[position]);
                        intentSelectShop.putExtra("Contact", Contact[position]);
                        intentSelectShop.putExtra("Root", Root[position]);
                        intentSelectShop.putExtra("IdNo", IdNo[position]);
                        intentSelectShop.putExtra("Credit", Credit[position]);
                        context.startActivity(intentSelectShop);
                        Toast.makeText(context, ShopName[position] + " was clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
//
            case "ReturnItems":
//
                btnView = myInflater.inflate(R.layout.shops_list_layout,null);

                TextView RshopId = (TextView)btnView.findViewById(R.id.txtShopId);
                TextView RshopName = (TextView)btnView.findViewById(R.id.txtShopName);
                TextView Raddress = (TextView)btnView.findViewById(R.id.txtAddress);
                TextView Rcontact = (TextView)btnView.findViewById(R.id.txtContact);
                TextView Rroot = (TextView)btnView.findViewById(R.id.txtRoot);
                TextView RidNo = (TextView)btnView.findViewById(R.id.txtIdNo);
                TextView Rcredit = (TextView)btnView.findViewById(R.id.txtCredit);

                RshopId.setText(ShopId[position]);
                RshopName.setText(ShopName[position]);
                Raddress.setText(Address[position]);
                Rcontact.setText(Contact[position]);
                Rroot.setText(Root[position]);
                RidNo.setText(IdNo[position]);
                Rcredit.setText(Credit[position]);

                btnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intentAddReturnItems = new Intent(context, AddReturnItems.class);
                        intentAddReturnItems.putExtra("ShopId", ShopId[position]);
                        intentAddReturnItems.putExtra("ShopName", ShopName[position]);
                        intentAddReturnItems.putExtra("Address", Address[position]);
                        intentAddReturnItems.putExtra("Contact", Contact[position]);
                        intentAddReturnItems.putExtra("Root", Root[position]);
                        intentAddReturnItems.putExtra("IdNo", IdNo[position]);
                        intentAddReturnItems.putExtra("Credit", Credit[position]);
                        context.startActivity(intentAddReturnItems);
                        Toast.makeText(context, ShopName[position] + " was clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                break;

        }

        return btnView;

    }

}
