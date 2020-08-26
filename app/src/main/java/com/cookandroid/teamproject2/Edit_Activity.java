package com.cookandroid.teamproject2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cookandroid.teamproject2.DBHelper;
import com.cookandroid.teamproject2.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Edit_Activity extends AppCompatActivity {
    DBHelper helper;
    SQLiteDatabase db;

    EditText date,title,content;
    RadioGroup division;
    RadioButton btn1,btn2,btn3,btn4;
    Button btnSave,btnDate;
    ImageButton btnCamera,btnAlbum;
    ImageView selectImg;

    String fileUri;

    private Spinner weather;

    private File tempFile;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("편집 하기");
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnSave=(Button)findViewById(R.id.btnSave);
        btnCamera=(ImageButton)findViewById(R.id.btnCamera);
        btnAlbum=(ImageButton)findViewById(R.id.btnAlbum);


        date = (EditText) findViewById(R.id.date);
        title = (EditText)findViewById(R.id.title);
        content = (EditText)findViewById(R.id.content);

        division = (RadioGroup)findViewById(R.id.division);
        btn1=(RadioButton)findViewById(R.id.btn1);
        btn2=(RadioButton)findViewById(R.id.btn2);
        btn3=(RadioButton)findViewById(R.id.btn3);
        btn4=(RadioButton)findViewById(R.id.btn4);

        btnDate=(Button)findViewById(R.id.btnDate);

        selectImg = (ImageView)findViewById(R.id.selectImg);

        weather =(Spinner) findViewById(R.id.weather);
        final String[] selectWeather = new String[1];
        final ArrayList<String> list = new ArrayList<>();
        list.add("맑음");
        list.add("흐림");
        list.add("비");
        list.add("눈");

        ArrayAdapter spinnerAdapter;
        spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list);
        weather.setAdapter(spinnerAdapter);

        Intent intent;
        final int did = getIntent().getIntExtra("id",0);

        helper = new DBHelper(this);
        db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select dtitle,ddate,dweather,ddivision,dcontent,dimgUri from diary_db where did="+did,null);
        cursor.moveToFirst();

        String d_title = cursor.getString(0);//dtitle text,"+"ddate text,"+"dweather text,"+"ddivision text,"+"dcontent text"
        String d_date = cursor.getString(1);
        String d_weather = cursor.getString(2);
        String d_division = cursor.getString(3);
        String d_content = cursor.getString(4);
        String d_imgUri = cursor.getString(5);

        date.setText(d_date);
        title.setText(d_title);
        if(d_weather.equals("맑음")){
            weather.setSelection(0);
        }else if(d_weather.equals("흐림")){
            weather.setSelection(1);
        }else if(d_weather.equals("비")){
            weather.setSelection(2);
        }else if(d_weather.equals("눈")){
            weather.setSelection(3);
        }
        content.setText(d_content);
        if(d_division.equals("일상")){
            btn1.setChecked(true);
        }else if(d_division.equals("육아일기")){
            btn2.setChecked(true);
        }
        else if(d_division.equals("맛집일기")){
            btn3.setChecked(true);
        }
        else if(d_division.equals("기타")){
            btn4.setChecked(true);
        }

        weather.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    selectWeather[0] ="맑음";
                }else if (position==1){
                    selectWeather[0] ="흐림";
                }else if (position==2){
                    selectWeather[0] ="비";
                }else if (position==3){
                    selectWeather[0] ="눈";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap img = BitmapFactory.decodeFile(d_imgUri, options);
        selectImg.setImageBitmap(img);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String d_division = null;
                int id= division.getCheckedRadioButtonId();
                String d_date=date.getText().toString();
                String d_weather=selectWeather[0];
                String d_title=title.getText().toString();
                String d_content=content.getText().toString();
                if (id!=-1) {
                    RadioButton rb = (RadioButton) findViewById(id);
                    System.out.println(rb);
                    d_division = rb.getText().toString();
                }


                if(d_date==null||d_date.equals("")){
                    Toast.makeText(getApplicationContext(),"날짜를 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(d_weather==null||d_weather.equals("")){
                    Toast.makeText(getApplicationContext(),"날씨를 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(d_title==null||d_title.equals("")){
                    Toast.makeText(getApplicationContext(),"제목을 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(d_content==null||d_content.equals("")){
                    Toast.makeText(getApplicationContext(),"내용을 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                helper.update(did,d_title,d_date,d_weather,d_division,d_content,fileUri);
                finish();
            }
        });


        final String now = date.getText().toString();

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DatePick.class);

                intent.putExtra("first",now);

                startActivityForResult(intent,0);
            }
        });

        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Album();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if(tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        tempFile = null;
                    }
                }
            }

            return;
        }
        if(requestCode==0){
            int year = (Integer) data.getExtras().getInt("year");
            int month = (Integer) data.getExtras().getInt("month");
            int day = (Integer) data.getExtras().getInt("date");
            date.setText(year+"-"+month+"-"+day);
        }

        if (requestCode == PICK_FROM_ALBUM) {

            Uri photoUri = data.getData();

            Cursor cursor = null;

            try {

                String[] proj = { MediaStore.Images.Media.DATA };

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));


            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setImage();

        } else if (requestCode == PICK_FROM_CAMERA&& resultCode == Activity.RESULT_OK && data.hasExtra("data")) {

            setImage(data);

        }
    }



    private void Album(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    private void camera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PICK_FROM_CAMERA);

    }



    private void setImage(Intent data){
        ImageView selectImg = findViewById(R.id.selectImg);

        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        if (bitmap != null) {
            String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            File root = Environment.getExternalStorageDirectory();
            File file = new File(root.getAbsolutePath()+"/DCIM/Camera/" + time + ".jpg");

            fileUri=file.getPath();

            try
            {
                file.createNewFile();
                FileOutputStream ostream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                ostream.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            selectImg.setImageBitmap(bitmap);
        }

    }
    private void setImage(){

        ImageView selectImg = findViewById(R.id.selectImg);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap img = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

        fileUri = tempFile.getAbsolutePath();
        selectImg.setImageBitmap(img);

        tempFile = null;
    }

    //[사진 처리 파트 참고 출처]:https://black-jin0427.tistory.com/120
    //[사진 처리 파트 참고 출처]:https://blog.naver.com/whdals0/221408883011

}