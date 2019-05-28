package com.example.icecreamdelivery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShopsListAdapter extends BaseAdapter implements Filterable {

    Context c;
    ArrayList<SingleRowForShops> originalArray,tmpArray;
    ///filter
    CustomFilter cs;
    ///filter



    public  ShopsListAdapter(Context c, ArrayList<SingleRowForShops> originalArray){
        this.c = c;
        this.originalArray = originalArray;
        this.tmpArray = originalArray;
    }


    @Override
    public Object getItem(int position) {
        return originalArray.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.shops_list_layout,null);

        TextView textView =(TextView)row.findViewById(R.id.txtShopName);
        TextView textAge = row.findViewById(R.id.txtShopId);


        textView.setText(originalArray.get(position).getName());
        textAge.setText(originalArray.get(position).getAge());

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentSelectShop = new Intent(c, SelectShop.class);

//                intentSelectShop.putExtra("ShopId", ShopId[position]);
//                intentSelectShop.putExtra("ShopName", ShopName[position]);
//                intentSelectShop.putExtra("Address", Address[position]);
//                intentSelectShop.putExtra("Contact", Contact[position]);
//                intentSelectShop.putExtra("Root", Root[position]);
//                intentSelectShop.putExtra("IdNo", IdNo[position]);
//                intentSelectShop.putExtra("Credit", Credit[position]);



                intentSelectShop.putExtra("ShopId", 100);
                intentSelectShop.putExtra("ShopName", "Sachitha Stores");
                intentSelectShop.putExtra("Address", "No");
                intentSelectShop.putExtra("Contact", "0715591137");
                intentSelectShop.putExtra("Root","10");
                intentSelectShop.putExtra("IdNo", "983142044v");
                intentSelectShop.putExtra("Credit", "300");
                c.startActivity(intentSelectShop);
                Toast.makeText(c, "Shop NAme" + " was clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return row;
    }

    @Override
    public int getCount() {
        return originalArray.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    ///Codes for filter
    @Override
    public Filter getFilter() {

        if(cs == null){
            cs = new CustomFilter();
        }

        return cs;
    }

    class CustomFilter extends  Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();


                ArrayList<SingleRowForShops> filters = new ArrayList<>();

                for (int i = 0; i < tmpArray.size(); i++) {
                    if (tmpArray.get(i).getName().toUpperCase().contains(constraint)) {
                        SingleRowForShops singleRow = new SingleRowForShops(tmpArray.get(i).getName(),tmpArray.get(i).getAge());



                        filters.add(singleRow);




                    }

                    if (tmpArray.get(i).getAge().toUpperCase().contains(constraint)) {
                        SingleRowForShops singleRow = new SingleRowForShops(tmpArray.get(i).getName(),tmpArray.get(i).getAge());



                        filters.add(singleRow);




                    }

                }
                results.count = filters.size();
                results.values = filters;

            }else {
                results.count = tmpArray.size();
                results.values = tmpArray;

            }
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            originalArray  = (ArrayList<SingleRowForShops>)results.values;
            notifyDataSetChanged();
        }
    }
    ///Codes for filter
}
