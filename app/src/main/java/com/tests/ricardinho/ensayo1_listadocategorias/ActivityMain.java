package com.tests.ricardinho.ensayo1_listadocategorias;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView lv_apps;
    private ProgressBar pb_loading;
    private WebView wb_resumen;
    private TextView tv_offline;

    public static ArrayList<App> listaApps;
    public static ArrayList<App> listaAppsDeployed;
    public static ArrayList<String> listaCategories;

    private Boolean areAppsDeployed = false; //bandera para saber en cada momento si se esta mostrando Apps o categorias

    private Consultor consultor = new Consultor(){};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv_apps = (ListView) findViewById(R.id.lv_apps);
        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        wb_resumen = (WebView) findViewById(R.id.wb_resumen_main);
        tv_offline = (TextView)findViewById(R.id.tv_offline);

        wb_resumen.setWebViewClient(new MyBrowser());

        WebSettings webSettings = wb_resumen.getSettings(); //estas dos lineas son para habilitar
        webSettings.setJavaScriptEnabled(true); //javascript en el WebView

        lv_apps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(areAppsDeployed){ //si se hizo tap sobre una app (ya que la lista de apps esta desplegada)
                    desplegarResumen(listaAppsDeployed.get(position));

                }else{ //si se hizo tap en una categoria(ya que la lista de apps no esta desplegada, lo que significa que esta desplegada la lista de categorias)
                    desplegarAppsDeCategoria(listaCategories.get(position));
                }

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(isOnlineNet()) {
            desplegarApps();
        } else{
            pb_loading.setVisibility(View.GONE);
            tv_offline.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Sin Internet", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_all_apps) {

            desplegarApps();

            return true;
        } else if(id == R.id.action_categories){

            desplegarCategories();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        String tittleItem = item.getTitle().toString();

        if (id == R.id.nav_all_apps) {
            desplegarApps();

        } else if (id == R.id.nav_categories) {
            desplegarCategories();

        } else if (id == R.id.nav_about) {

        } else if(listaCategories.contains(tittleItem)){
            desplegarAppsDeCategoria(tittleItem);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void desplegarApps(){

        lv_apps.setVisibility(View.GONE);
        wb_resumen.setVisibility(View.GONE);
        tv_offline.setVisibility(View.GONE);
        pb_loading.setVisibility(View.VISIBLE);

        consultor = new Consultor();
        consultor.execute(this);
        areAppsDeployed = true;
    }

    private void desplegarAppsDeCategoria(String category){
        Toast.makeText(ActivityMain.this, category, Toast.LENGTH_SHORT).show();
        ArrayList<App> lista_app_por_cat = new ArrayList<>();

        for(int i=0; i<listaApps.size(); i++){
            if(listaApps.get(i).categoriaapp.equalsIgnoreCase(category)){
                lista_app_por_cat.add(listaApps.get(i));
                Log.i("RIC", category);
            }
        }

        listaAppsDeployed = lista_app_por_cat;

        AdapterAppList adapter = new AdapterAppList(ActivityMain.this, listaAppsDeployed);
        ListView local_lv  = (ListView) findViewById(R.id.lv_apps);
        local_lv.setAdapter(adapter);

        areAppsDeployed = true;
    }

    private void desplegarCategories(){
        lv_apps.setVisibility(View.GONE);
        wb_resumen.setVisibility(View.GONE);
        tv_offline.setVisibility(View.GONE);
        pb_loading.setVisibility(View.VISIBLE);

        listaCategories = new ArrayList<String>();

        for(int i = 0; i < listaApps.size(); i++){
            String elemento = listaApps.get(i).categoriaapp;

            if(!listaCategories.contains(elemento)){
                listaCategories.add(elemento);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, listaCategories);

        lv_apps.setAdapter(adapter);

        lv_apps.setVisibility(View.VISIBLE);
        wb_resumen.setVisibility(View.GONE);
        tv_offline.setVisibility(View.GONE);
        pb_loading.setVisibility(View.GONE);

        areAppsDeployed = false;
    }

    private void desplegarResumen(App app){

        Intent intent = new Intent(this, ActivityResumen.class);
        intent.putExtra("nombreimagen", app.nombreimagen);
        intent.putExtra("linkapp", app.linkapp);
        startActivity(intent);
    }

    /*clase que sobre-escribe el metodo shouldOverrideUrlLoading para que se carguen
    las paginas web en el WebView y no en el navegador
    */
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
