package com.tests.ricardinho.ensayo1_listadocategorias;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.webkit.WebView;
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

/**
 * Created by RICARDINHO on 28/09/2016.
 */

/*clase para realizar las consultas al servidor y llenar la lsita de apps*/
public class Consultor extends AsyncTask<Activity, String, ArrayList<App>> {

    Activity localActivity;

    URL local_url;
    HttpURLConnection local_urlconnection;

    StringBuilder jsonResult;

    ArrayList<App> lista_apps;

    @Override
    protected ArrayList<App> doInBackground(Activity... activities) {
        Log.i("RIC", "doInBackground()");
        localActivity = activities[0];

        jsonResult = new StringBuilder();

        lista_apps = new ArrayList<>(); //inicializamos la lista de Apps

        try {
            local_url = new URL(localActivity.getResources().getString(R.string.serviceURL));
            local_urlconnection = (HttpURLConnection) local_url.openConnection();
            InputStream in = new BufferedInputStream(local_urlconnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                jsonResult.append(line);
            }

            JSONObject object = new JSONObject(jsonResult.toString()).getJSONObject("feed"); //Creamos un objeto JSON a partir de la cadena
            JSONArray json_array = object.optJSONArray("entry"); //cogemos cada uno de los elementos dentro de la etiqueta "entry"

            Log.i("RIC", "cantidad de apps: " + Integer.toString(json_array.length()));

            for (int i = 0; i < json_array.length(); i++) {
                Log.i("RIC", "ciclo "+ Integer.toString(i) + " del for");
                App aux_app = new App(json_array.getJSONObject(i)); //creamos un objeto App
                lista_apps.add(aux_app); //insertamos el objeto App en la lista
            }

        }catch( Exception e) {
            e.printStackTrace();
            Log.i("RIC", "ERROR: " + e.getMessage());
        }
        finally {
            local_urlconnection.disconnect();
            Log.i("RIC", "longitud lista de Apps: " + Integer.toString(lista_apps.size()));
        }

        return lista_apps;
    }

    @Override
    protected void onPostExecute(ArrayList<App> lista_apps) {
        Log.i("RIC", "onPostExecute()");

        AdapterAppList adapter = new AdapterAppList(localActivity, lista_apps);
        ListView local_lv  = (ListView) localActivity.findViewById(R.id.lv_apps);
        local_lv.setAdapter(adapter);

        ActivityMain.listaApps = lista_apps;
        ActivityMain.listaAppsDeployed = lista_apps;

        ListView lv_apps = (ListView) localActivity.findViewById(R.id.lv_apps);
        lv_apps.setVisibility(View.VISIBLE);
        WebView wb_resumen = (WebView) localActivity.findViewById(R.id.wb_resumen_main);
        wb_resumen.setVisibility(View.GONE);
        TextView tv_offline = (TextView) localActivity.findViewById(R.id.tv_offline);
        tv_offline.setVisibility(View.GONE);
        ProgressBar pb_loading = (ProgressBar) localActivity.findViewById(R.id.pb_loading);
        pb_loading.setVisibility(View.GONE);

        ArrayList<String> lista_categories = new ArrayList<>(); //inicializamos la lista de categorias

        NavigationView navigationView = (NavigationView) localActivity.findViewById(R.id.nav_view);
         MenuItem myMoveGroupItem = navigationView.getMenu().findItem(R.id.submenu_categories);
        SubMenu subMenu = myMoveGroupItem.getSubMenu();
        subMenu.clear();

        for(int j = 0; j < lista_apps.size(); j++){
            String cat_aux = lista_apps.get(j).categoriaapp;

            if(!lista_categories.contains(cat_aux)){
                lista_categories.add(cat_aux);

                subMenu.add(cat_aux);
            }

            ActivityMain.listaCategories = lista_categories;
        }
    }
}
