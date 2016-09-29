package com.tests.ricardinho.ensayo1_listadocategorias;

/**
 * Created by RICARDINHO on 26/09/2016.
 */


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterAppList extends ArrayAdapter<App> {

    private final Activity context;
    ArrayList<App> listaApps;

    TextView tv_nombreapp;
    ImageView iv_imagen;
    TextView tv_autorapp;


    public AdapterAppList(Activity frg_argument, ArrayList<App> lista) {
        super(frg_argument, R.layout.fila_lista, lista);
        this.context = frg_argument;
        this.listaApps = lista;
    }

    public AdapterAppList(Fragment frg_argument, ArrayList<App> lista) {
        super(frg_argument.getActivity(), R.layout.fila_lista, lista);

        this.context = frg_argument.getActivity();
        this.listaApps = lista;
    }

    public View getView(int position,View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.fila_lista, parent, false);

        this.iv_imagen = (ImageView) rowView.findViewById(R.id.iv_imagenapp);
        this.tv_nombreapp = (TextView) rowView.findViewById(R.id.tv_nombreimagen);
        this.tv_autorapp = (TextView) rowView.findViewById(R.id.tv_autorapp);

        App appActual = listaApps.get(position);

        iv_imagen.setImageBitmap(appActual.imagenapp);
        tv_nombreapp.setText(appActual.nombreimagen);
        tv_autorapp.setText(appActual.autorapp);

        return rowView;
    }
}
