package com.aptech.bruno.tom_cua_ca;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * Created by Bruno on 06/06/2016.
 */
public class custom_gridview_banco extends ArrayAdapter<Integer> {
    Context context;
    int resource;
    Integer[] object;
    Integer[] giaTien={0,100,200,300,400,500,1000};
    ArrayAdapter<Integer>adapter;

    public custom_gridview_banco(Context context, int resource, Integer[] object) {
        super(context, resource, object);

        this.context=context;
        this.resource=resource;
        this.object=object;
        adapter=new ArrayAdapter<Integer>(context,android.R.layout.simple_spinner_item,giaTien);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view=View.inflate(context,resource,null);
        ImageView imgBanco=(ImageView)view.findViewById(R.id.imgBanco);
        Spinner spiner=(Spinner)view.findViewById(R.id.spiner);
        imgBanco.setImageResource(object[position]);
        spiner.setAdapter(adapter);
        // xu ly dat cuoc
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positionspin, long id) {
                MainActivity.gtDatCuoc[position]=giaTien[positionspin];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
}