package com.cookandroid.teamproject2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;

import java.util.Calendar;
import java.util.GregorianCalendar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    Context context;
    PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("푸시 설정");

        final TextView textView = (TextView)findViewById(R.id.text);
        final TimePicker TimePicker = (TimePicker)findViewById(R.id.timePicker);
        final Button btnSaveTime = (Button)findViewById(R.id.btnSaveTime);
        final Switch switchButton = (Switch) findViewById(R.id.switchBtn);

        this.context = this;

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        final Calendar calendar = Calendar.getInstance();

        final Intent intent = new Intent(this.context ,Alarm_Reciver.class );

        SharedPreferences sharedPreferences = getSharedPreferences("daily alarm", MODE_PRIVATE);
        long millis = sharedPreferences.getLong("nextNotifyTime", Calendar.getInstance().getTimeInMillis());
        boolean onOff = sharedPreferences.getBoolean("switch",false);

        switchButton.setChecked(onOff);

        if (onOff){
            TimePicker.setVisibility(View.VISIBLE);
            btnSaveTime.setVisibility(View.VISIBLE);
        }else{
            TimePicker.setVisibility(View.INVISIBLE);
            btnSaveTime.setVisibility(View.INVISIBLE);

        }

        Calendar nextNotifyTime = new GregorianCalendar();
        nextNotifyTime.setTimeInMillis(millis);

        btnSaveTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int hour = TimePicker.getHour();
                int minute = TimePicker.getMinute();

                calendar.set(Calendar.HOUR_OF_DAY,hour);
                calendar.set(Calendar.MINUTE,minute);

                Toast.makeText(SettingActivity.this,"Alarm 예정 " + hour + "시 " + minute + "분",Toast.LENGTH_SHORT).show();

                intent.putExtra("hour",hour);
                intent.putExtra("minute",minute);

                pendingIntent = PendingIntent.getBroadcast(SettingActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );

                alarmManager.set(AlarmManager.RTC_WAKEUP , calendar.getTimeInMillis(),pendingIntent);


                SharedPreferences.Editor editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
                editor.putLong("nextNotifyTime", (long)calendar.getTimeInMillis());
                editor.putBoolean("switch",switchButton.isChecked());
                editor.apply();

                diaryNotification(calendar);
            }
        });

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences.Editor editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){
                    TimePicker.setVisibility(View.VISIBLE);
                    btnSaveTime.setVisibility(View.VISIBLE);
                    editor.putBoolean("switch",switchButton.isChecked());
                    editor.apply();
                }else{
                    TimePicker.setVisibility(View.INVISIBLE);
                    btnSaveTime.setVisibility(View.INVISIBLE);
                    editor.putBoolean("switch",switchButton.isChecked());
                    editor.apply();

                    alarmManager.cancel(pendingIntent);

                }
            }
        });

    }
    void diaryNotification(Calendar calendar)
    {
        Boolean dailyNotify = true; // 무조건 알람을 사용

        Intent alarmIntent = new Intent(this, Alarm_Reciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        // 사용자가 매일 알람을 허용했다면
        if (dailyNotify) {


            if (alarmManager != null) {

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }


        }
    }
}

