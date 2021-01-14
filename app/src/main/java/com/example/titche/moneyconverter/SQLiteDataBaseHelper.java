package com.example.titche.moneyconverter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDataBaseHelper extends SQLiteOpenHelper {

    private static  final  String DATABASE_NAME="CurrencyLayer.db";
    private static final  String TABLE_NAME="exchange_table";
    private static final  String COL_1="ID";
    private static final String COL_2="DE";
    private static  final  String COL_3="VERS";
    private static  final String COL_4="VALEUR1";
    private static final String COL_5="VALEUR2";
    private static final String COL_6="VERS2";
    private static final String COL_7="DATE";

    public SQLiteDataBaseHelper(Context context){
        super(context,DATABASE_NAME,null,1);
        SQLiteDatabase db = this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, DE TEXT NOT NULL, VERS TEXT NOT NULL, VALEUR1 TEXT NOT NULL, VALEUR2 TEXT NOT NULL, VERS2 TEXT NOT NULL, DATE TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    /*
    *Methode d'insertion des données
     */
    public boolean insertData(String de, String vers, String value1, String value2, String vers2, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,de);
        contentValues.put(COL_3,vers);
        contentValues.put(COL_4,value1);
        contentValues.put(COL_5,value2);
        contentValues.put(COL_6,vers2);
        contentValues.put(COL_7,date);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }
    /*
    * Methode pour parcourir la db
     */
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from "+TABLE_NAME,null);
        return res;
    }
}
