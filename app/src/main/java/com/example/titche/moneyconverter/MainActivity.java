package com.example.titche.moneyconverter;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private Button ok;
    private Button exit;
    private Button historique;
    private Spinner spinner;
    private EditText dollar;
    private TextView write;
    private double value=0;
    private double currencie=0;
    private String str=" ";
    private String str1="";
    private double st=0;
    private ConnectionDetector cd;
    Context context=this;
    private MediaPlayer mediaPlayer;
    private static final String BASE_DEVISE="EUR";
    SQLiteDataBaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ok = (Button) findViewById(R.id.convert);
        historique=(Button)findViewById(R.id.historique);
        exit = (Button) findViewById(R.id.exit);
        spinner = (Spinner) findViewById(R.id.toAnother);
        dollar = (EditText) findViewById(R.id.fromEuro);
        write = (TextView) findViewById(R.id.ecriture);
        this.mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.my_sound);
        myDb = new SQLiteDataBaseHelper(getApplicationContext());
        cd = new ConnectionDetector(this);

        convertAndAddData();
        viewAll();
        exit();
    }
    public void convertAndAddData() {
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str=dollar.getText().toString();
                if(cd.isConnected()){
                    if(str.equals("")) {
                        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(1000);
                        showMessage("Champs manquants","Veuillez tout remplir svp");
                    }else{
                        value=Double.parseDouble(str);
                        str1=spinner.getSelectedItem().toString();
                        String symbol="";
                        for(int i=0; i<=2; i++){
                            symbol=symbol+str1.charAt(i);
                        }
                        if(symbol.equals("Sel")){
                            showMessage("Devise non valide","Veuillez choisir une devise valide svp");
                        }else{
                            st=Double.parseDouble(send(BASE_DEVISE,symbol));
                            currencie=((st*value)/1);
                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss a");
                            Date date = new Date();
                            String time =format.format(date);
                            String arrondie=(String.format("%.2f",currencie));
                            write.setText(value +" "+BASE_DEVISE+" in "+symbol+" : "+arrondie+"\n"+"("+time+")");
                            boolean inserted = myDb.insertData(BASE_DEVISE,str1,value+"",arrondie,symbol,time);
                            if(inserted==true){
                                Toast.makeText(getApplicationContext(),"Historique enrégistré avec succès",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext()," bad",Toast.LENGTH_SHORT).show();
                            }
                            playSound();
                        }
                    }
                }else{
                    showMessage("Network","Veuillez vous connectez pour continuer svp");
                }
            }
        });
    }
    public void viewAll(){
        historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getAllData();
                if(res.getCount()==0){
                    showMessage("BD Vide ","Nothing found");
                }
                StringBuilder buider = new StringBuilder();
                while(res.moveToNext()){
                    buider.append("FROM "+res.getString(1)+" TO "+res.getString(2)+"\n");
                    buider.append(res.getString(3)+" "+res.getString(1)+" = "+res.getString(4)+" "+res.getString(5)+"\n");
                    buider.append("Date : "+res.getString(6)+"\n");
                    buider.append("---------DEVELOPED BY PAUL TITCHE---------"+"\n\n");
                }
                res.close();
                showMessage("HISTORIQUE ",buider.toString());
            }
        });
    }
    /*
    *quitter l'application
     */
    public void exit(){
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickExit();
            }
        });
    }

    /*
    * Method who receive the API response
     */
     public String send(String a, String b){
        FixerWebService fixerWebService = new FixerWebService(context);
        String result=null;
        try{
            result=fixerWebService.execute(a,b).get();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(ExecutionException e){
           e.printStackTrace();
        }
        return result;
    }
    /*
    *Method who display the alert message
     */
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    /*
    * Function who start to play my music
     */
    public void playSound(){
        mediaPlayer.start();
    }
    /*
    * Just cancel the application
     */
    public void clickExit(){
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
