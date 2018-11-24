package com.example.ivana.proyecto.AlgoritmoCompresion;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ivana.proyecto.R;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter2 extends BaseAdapter {

    private Context mContext;
    private List<Mensajes> Lista;

    public ListAdapter2(Context mContext, ArrayList<Mensajes>Lista)
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
        View v = View.inflate(mContext, R.layout.cuerpomensajes, null);

        TextView tvNombre = (TextView) v.findViewById(R.id.txt_NombreEmisor);
        TextView tvMensaje = (TextView) v.findViewById(R.id.txt_Mensaje);
        TextView tvReceptor = (TextView) v.findViewById(R.id.txt_Receptor);

        tvNombre.setText(Lista.get(position).getEmisor());
        tvMensaje.setText(Lista.get(position).getMensajes());
        tvReceptor.setText(Lista.get(position).getRecepetor());

        return v    ;
    }

}


