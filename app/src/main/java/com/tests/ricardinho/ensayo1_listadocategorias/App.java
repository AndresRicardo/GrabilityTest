package com.tests.ricardinho.ensayo1_listadocategorias;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by RICARDINHO on 27/09/2016.
 */

//clase para almacenar toda la informacion de una aplicacion

public class App {

    public String nombreimagen; //String a presentar junto con la imagen
    public String linkimagen;
    public Bitmap imagenapp;
    public String resumenapp;
    public double precioapp;
    public String monedaprecio;
    public String derechosapp;
    public String tittleapp;
    public String linkapp;
    public String idapp;
    public String autorapp; //String a presentar como autor de la app
    public String linkautor;
    public String categoriaapp;
    public String fechalanzamiento;


    public App(JSONObject JSONAPP) { //el JSON que recibe contiene la informacion de una sola app
        try {
            nombreimagen = JSONAPP.getJSONObject("im:name").getString("label"); //obtenemos el nombre de la imagen
            Log.i("RIC", "en el constructor de App. im:name: " + nombreimagen);

            linkimagen = JSONAPP.getJSONArray("im:image").getJSONObject(Integer.parseInt("2")).getString("label"); //obtenemos el link de la imagen
            Log.i("RIC", "en el constructor de App. linkimagen: " + linkimagen);

            imagenapp = getBitmapFromURL(linkimagen); //obtenemos la imagen desde el link de la imagen
            Log.i("RIC", "en el constructor de App. se obtuvo la imagen");

            resumenapp = JSONAPP.getJSONObject("summary").getString("label");
            Log.i("RIC", "en el constructor de App. se obtuvo el resumen");

            precioapp = JSONAPP.getJSONObject("im:price").getJSONObject("attributes").getDouble("amount");
            Log.i("RIC", "en el constructor de App. precioapp: " + precioapp);

            monedaprecio = JSONAPP.getJSONObject("im:price").getJSONObject("attributes").getString("currency");
            Log.i("RIC", "en el constructor de App. monedaprecio: " + monedaprecio);

            derechosapp = JSONAPP.getJSONObject("rights").getString("label"); //obtenemos los derechos de la app
            Log.i("RIC", "en el constructor de App. derechosapp: " + derechosapp);

            tittleapp = JSONAPP.getJSONObject("title").getString("label");
            Log.i("RIC", "en el constructor de App. tittleapp: " + tittleapp);

            linkapp = JSONAPP.getJSONObject("link").getJSONObject("attributes").getString("href");
            Log.i("RIC", "en el constructor de App. linkapp: " +  linkapp);

            idapp = JSONAPP.getJSONObject("id").getJSONObject("attributes").getString("im:id");
            Log.i("RIC", "en el constructor de App. idapp: " + idapp);

            autorapp = JSONAPP.getJSONObject("im:artist").getString("label");
            Log.i("RIC", "en el constructor de App. tv_autorapp: " + autorapp);

            linkautor = JSONAPP.getJSONObject("im:artist").getJSONObject("attributes").getString("href");
            Log.i("RIC", "en el constructor de App. linkautor: " + linkautor);

            categoriaapp = JSONAPP.getJSONObject("category").getJSONObject("attributes").getString("label");
            Log.i("RIC", "en el constructor de App. categoriaapp: " + categoriaapp);

            fechalanzamiento = JSONAPP.getJSONObject("im:releaseDate").getJSONObject("attributes").getString("label");
            Log.i("RIC", "en el constructor de App. fechalanzamiento: " + fechalanzamiento);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("RIC", "ERROR en el constructor de App: " + e.getMessage());
        }
    }

    private Bitmap getBitmapFromURL(String src){
        Bitmap auxBitmap;

        try {
            URL url = new URL(src);
            Log.i("RIC", "en getBitmapFromURL: link de la imagen: " + src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Log.i("RIC", "en getBitmapFromURL: abriendo conexión openConnection()");
//                connection.setDoInput(true);
//                Log.i("RIC", "en getBitmapFromURL: estableciendo si se permiten entradas setDoInput(true)");
            connection.connect();
            Log.i("RIC", "en getBitmapFromURL: conectando");
            InputStream input = connection.getInputStream();
            Log.i("RIC", "en getBitmapFromURL: obteniendo inputStream getInputStream()");
            auxBitmap = BitmapFactory.decodeStream(input);
            Log.i("RIC", "en getBitmapFromURL: decodificando imagen decodeStream(input)");
            return auxBitmap;

        } catch (java.io.IOException e) {
            e.printStackTrace();
            Log.i("RIC", "ERROR en App.getBitmapFromURL: " + e.getMessage());
            return null;
        }
    }
}