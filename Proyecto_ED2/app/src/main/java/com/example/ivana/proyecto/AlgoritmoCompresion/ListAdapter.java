package com.example.ivana.proyecto.AlgoritmoCompresion;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ivana.proyecto.R;

import java.util.List;

public class ListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Usuarios> Lista;

    public ListAdapter(Context mContext,List<Usuarios>Lista)
    {
        this.Lista = Lista;
        this.mContext = mContext;
    }


    @Override

    public int getCount() {
        return Lista.size();
    }

    @Override
    public Object getItem(int position) {
        return Lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.list_item, null);
        TextView tvNombre = (TextView) v.findViewById(R.id.text1);
        tvNombre.setText(Lista.get(position).getNombre());
        return v    ;
    }

}


