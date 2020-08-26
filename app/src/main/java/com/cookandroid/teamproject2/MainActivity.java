package com.cookandroid.teamproject2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageButton btnEdit;
    ListView listView;
    ListAdapter listAdapter;
    ArrayList<ListItem> arrayList;
    ToggleButton btnToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("사진 일기");

        btnToggle = (ToggleButton)findViewById(R.id.btnToggle);

        final Intent intent2 = new Intent(this, MyService.class);

        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnToggle.isChecked()){
                    startService(intent2);
                }else {
                    stopService(intent2);
                }
                //isChecked()는 toggle on이되면 true값을 반환 반대면 false
            }
        });



        final DBHelper helper = new DBHelper(this);

        btnEdit = (ImageButton)findViewById(R.id.btnEdit);
        listView = (ListView)findViewById(R.id.listView);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),NewEdit_Activity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int did=arrayList.get(position).did;

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("수정,삭제");
                builder.setMessage("선택하십시오.");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper.delete(did);
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(),Edit_Activity.class);
                        intent.putExtra("id", did);
                        startActivity(intent);
                    }
                });
                AlertDialog alert=builder.create();
                alert.show();

                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int did=arrayList.get(position).did;
                        Intent intent=new Intent(getApplicationContext(),View_Activity.class);
                        intent.putExtra("id", did);
                        System.out.println("did"+did);
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //"권한 설정 완료"
            } else {
                //"권한 설정 요청"
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_btn1:
                Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        final DBHelper helper=new DBHelper(this);
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select did,dtitle,ddate from diary_db order by ddate desc",null);

        arrayList = new ArrayList<ListItem>();
        while(cursor.moveToNext()){
            ListItem listItem= new ListItem();

            listItem.did=cursor.getInt(0);
            listItem.dtitle=cursor.getString(1);
            listItem.ddate=cursor.getString(2);

            arrayList.add(listItem);
        }

        listAdapter = new ListAdapter(this,R.layout.listitem, arrayList);
        listView.setAdapter(listAdapter);


    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
        finish();
    }
}
