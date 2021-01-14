package com.example.titche.moneyconverter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class FixerWebService extends AsyncTask<String,Void,String>{

    private Context contex;
    /*
    *Constructeur par défaut
     */
    public FixerWebService(Context  context){
        this.contex=context;
    }
    /*
    * Methode de lecture de l'API via les threads
    * param[...] autant de variable
     */
    @Override
    protected String doInBackground(String... param) {
        String a =param[0];
        String b= param[1];
        double rate =0;
        try{

            URL url = new URL("http://data.fixer.io/api/latest?access_key=3f9ce77119fbaacd4b1429317aa838f6");
            //URL url = new URL("http://www.apilayer.net/api/live?access_key=05077ffdccb30418b383e886a2f690db");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Scanner scanner = null;
            //lit les fichiers en utilisant une zone de mémoire tampon-->accélérer la lecture
            scanner = new Scanner(new BufferedInputStream(conn.getInputStream()));

            String rslt ="";

            while (scanner.hasNext()){
                rslt += scanner.nextLine();
            }
            Log.i("Resultat de API : ", rslt);

            rate = new JSONObject(rslt).getJSONObject("rates").getDouble(b);
            scanner.close();

        } catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return rate+"";
    }
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
