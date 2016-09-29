package com.tests.ricardinho.ensayo1_listadocategorias;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FragmentList extends Fragment {

    private ListView lv_apps;
    private AdapterAppList adapter;
    private ProgressBar pb_loading;
    private WebView wb_resumen;
    private TextView tv_offline;

    public static ArrayList<App> listaApps;
    private ArrayList<String> listaCategories;

    private Boolean areAppsDeployed = false; //bandera para saber en cada momento si se esta mostrando Apps o categorias

    private final String st_url = "https://itunes.apple.com/us/rss/topfreeapplications/limit=10/json";
    private Consultor consultor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        lv_apps = (ListView) getView().findViewById(R.id.lv_apps);
        pb_loading = (ProgressBar) getView().findViewById(R.id.pb_loading);
        wb_resumen = (WebView) getView().findViewById(R.id.wb_resumen_main);
        tv_offline = (TextView) getView().findViewById(R.id.tv_offline);
        lv_apps = (ListView)getView().findViewById(R.id.lv_apps);

        wb_resumen.setWebViewClient(new MyBrowser());

        WebSettings webSettings = wb_resumen.getSettings(); //estas dos lineas son para habilitar
        webSettings.setJavaScriptEnabled(true); //javascript en el WebView

//        lv_apps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                if(areAppsDeployed){
//                    desplegarResumen(listaApps.get(position));
//
//                }else{
//
//                }
//
//            }
//        });

        if(isOnlineNet()) {
            desplegarApps();
        } else{
            pb_loading.setVisibility(View.GONE);
            tv_offline.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Sin Internet", Toast.LENGTH_LONG).show();
        }
    }


    /*metodo para desplegar el listado de aplicaciones*/
    private void desplegarApps(){

        consultor.execute(getActivity());
        areAppsDeployed = true;
    }

    /*metodo para desplegar el resumen de una aplicacion*/
    private void desplegarResumen(App app){

        Intent intent = new Intent(getActivity(), ActivityResumen.class);
        intent.putExtra("nombreimagen", app.nombreimagen);
        intent.putExtra("linkapp", app.linkapp);
        startActivity(intent);
    }

    /*clase que sobre-escribe el metodo shouldOverrideUrlLoading para que se carguen
    las paginas web en el WebView y no en el navegador*/
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public Boolean isOnlineNet() { //funcion para determinar si hay acceso a internet

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

//    /*clase que sirve para llenar de forma personalizada el ListView*/
//    private class AppAdapter extends ArrayAdapter<App> {
//
//        private final Activity context;
//        ArrayList<App> listaApps;
//
//        TextView tv_nombreapp;
//        ImageView iv_imagen;
//        TextView tv_autorapp;
//
//
//        public AppAdapter(Fragment frg_argument, ArrayList<App> lista) {
//            super(frg_argument.getActivity(), R.layout.fila_lista, lista);
//
//            this.context = frg_argument.getActivity();
//            this.listaApps = lista;
//        }
//
//        public View getView(int position,View view, ViewGroup parent){
//            LayoutInflater inflater = context.getLayoutInflater();
//            //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View rowView = inflater.inflate(R.layout.fila_lista, parent, false);
//
//            iv_imagen = (ImageView) rowView.findViewById(R.id.iv_imagenapp);
//            tv_nombreapp = (TextView) rowView.findViewById(R.id.tv_nombreimagen);
//            tv_autorapp = (TextView) rowView.findViewById(R.id.tv_autorapp);
//
//            App appActual = listaApps.get(position);
//
//            iv_imagen.setImageBitmap(appActual.imagenapp);
//            tv_nombreapp.setText(appActual.nombreimagen);
//            tv_autorapp.setText(appActual.autorapp);
//
//            return rowView;
//        }
//    }





//    /*clase para realizar las consultas al servidor y llenar la lsita de apps*/
//    private class Consultor extends AsyncTask<String, String, ArrayList<App>> {
//
//        URL local_url;
//        HttpURLConnection local_urlconnection;
//
//        StringBuilder jsonResult;
//
//        @Override
//        protected void onPreExecute() {
//            lv_apps.setVisibility(View.GONE);
//            wb_resumen.setVisibility(View.GONE);
//            tv_offline.setVisibility(View.GONE);
//            pb_loading.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected ArrayList<App> doInBackground(String... urlParams) {
//            Log.i("RIC", "doInBackground()");
//
//            jsonResult = new StringBuilder();
//
//            ArrayList<App> lista_apps = new ArrayList<>(); //inicializamos la lista de Apps
//
//            try {
//                local_url = new URL(urlParams[0]);
//                local_urlconnection = (HttpURLConnection) local_url.openConnection();
//                InputStream in = new BufferedInputStream(local_urlconnection.getInputStream());
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    jsonResult.append(line);
//                }
//
//                JSONObject object = new JSONObject(jsonResult.toString()).getJSONObject("feed"); //Creamos un objeto JSON a partir de la cadena
//                JSONArray json_array = object.optJSONArray("entry"); //cogemos cada uno de los elementos dentro de la etiqueta "entry"
//
//                Log.i("RIC", "cantidad de apps: " + Integer.toString(json_array.length()));
//
//                for (int i = 0; i < json_array.length(); i++) {
//                    Log.i("RIC", "ciclo "+ Integer.toString(i) + " del for");
//                    App aux_app = new App(json_array.getJSONObject(i)); //creamos un objeto App
//                    lista_apps.add(aux_app); //insertamos el objeto App en la lista
//                }
//
//            }catch( Exception e) {
//                e.printStackTrace();
//                Log.i("RIC", "ERROR: " + e.getMessage());
//            }
//            finally {
//                local_urlconnection.disconnect();
//                Log.i("RIC", "longitud lista de Apps: " + Integer.toString(lista_apps.size()));
//            }
//
//            listaApps = lista_apps;
//
//            return lista_apps;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<App> lista_apps) {
//            Log.i("RIC", "onPostExecute()");
//
//            adapter = new AdapterAppList(FragmentList.this, lista_apps);
//            lv_apps.setAdapter(adapter);
//
//            lv_apps.setVisibility(View.VISIBLE);
//            wb_resumen.setVisibility(View.GONE);
//            tv_offline.setVisibility(View.GONE);
//            pb_loading.setVisibility(View.GONE);
//        }
//    }




/////////////////////////////////////FIN/////////////////////////////////////





//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link FragmentList.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link FragmentList#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class FragmentList extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;
//
//
//    public FragmentList() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment FragmentList.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static FragmentList newInstance(String param1, String param2) {
//        FragmentList fragment = new FragmentList();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_list, container, false);
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//}
