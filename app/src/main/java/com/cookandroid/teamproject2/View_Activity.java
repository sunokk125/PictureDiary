package com.cookandroid.teamproject2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class View_Activity extends AppCompatActivity {


    DBHelper helper;
    SQLiteDatabase db;
    TextView date, weather, title, division, content;
    ImageView selectImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("일기 보기");
        actionBar.setDisplayHomeAsUpEnabled(true);

        date = (TextView) findViewById(R.id.date);
        weather = (TextView) findViewById(R.id.weather);
        title = (TextView) findViewById(R.id.title);
        division = (TextView) findViewById(R.id.division);
        content = (TextView) findViewById(R.id.content);
        selectImg = (ImageView) findViewById(R.id.selectImg);

        Intent intent;
        int did = getIntent().getIntExtra("id", 0);

        helper = new DBHelper(this);
        db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select dtitle,ddate,dweather,ddivision,dcontent,dimgUri from diary_db where did=" + did, null);
        cursor.moveToFirst();

        String d_title = cursor.getString(0);//dtitle text,"+"ddate text,"+"dweather text,"+"ddivision text,"+"dcontent text"
        String d_date = cursor.getString(1);
        String d_weather = cursor.getString(2);
        String d_division = cursor.getString(3);
        String d_content = cursor.getString(4);
        String d_imgUri = cursor.getString(5);

        date.setText(d_date);
        title.setText(d_title);
        weather.setText(d_weather);
        division.setText(d_division);
        content.setText(d_content);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap img = BitmapFactory.decodeFile(d_imgUri, options);
        selectImg.setImageBitmap(img);
    }
}

