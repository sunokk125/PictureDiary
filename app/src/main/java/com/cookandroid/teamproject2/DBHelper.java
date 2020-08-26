package com.cookandroid.teamproject2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=1;

    public DBHelper(Context context){
        super(context, "db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String diarydb="create table diary_db("+
                "did integer primary key autoincrement,"+
                "dtitle text,"+
                "ddate text,"+
                "dweather text,"+
                "ddivision text,"+//1.일상 2.육아일기 3.맛집일기 4.기타
                "dcontent text,"+
                "dimgUri text"+
                ")";

        db.execSQL(diarydb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if (newVersion == DATABASE_VERSION){
            db.execSQL("drop table diary_db");
            onCreate(db);
        }
    }

    public void insert(String dtitle, String ddate, String dweather, String ddivision, String dcontent,String dimgUri){
        SQLiteDatabase db= getWritableDatabase();
        db.execSQL("insert into diary_db(dtitle,ddate,dweather,ddivision,dcontent,dimgUri) values('"+dtitle+"','"+ddate+"','"+dweather+"','"+ddivision+"','"+dcontent+"','"+dimgUri
                +"');");
        db.close();
    }

    public void update(int did, String dtitle, String ddate, String dweather, String ddivision, String dcontent, String dimgUri){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("update diary_db set dtitle='" + dtitle +"', ddate='"+ddate+"', dweather='"+dweather+"', ddivision='"+ddivision+"',dcontent='"+dcontent+"',dimgUri='"+dimgUri+"' where did='" + did + "';");
        db.close();
    }


    public void delete(int did) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from diary_db where did='" + did + "';");
        db.close();
    }


}
