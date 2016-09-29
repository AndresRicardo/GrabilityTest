package com.tests.ricardinho.ensayo1_listadocategorias;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ActivityResumen extends AppCompatActivity {

    WebView wb_resumen_resumen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wb_resumen_resumen = (WebView)findViewById(R.id.wb_resumen_resumen);
        wb_resumen_resumen.setWebViewClient(new MyBrowser());

        WebSettings webSettings = wb_resumen_resumen.getSettings(); //estas dos lineas son para habilitar
        webSettings.setJavaScriptEnabled(true); //javascript en el WebView

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        String link = intent.getStringExtra("linkapp");
        setTitle(intent.getStringExtra("nombreimagen")); //para cambair el nombre de la Activity, funciona poniendolo en cualquier lado

        wb_resumen_resumen.loadUrl(link);

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

}
