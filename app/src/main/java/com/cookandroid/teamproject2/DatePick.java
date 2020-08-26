package com.cookandroid.teamproject2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DatePick extends AppCompatActivity {
    Button btnEnd;
    DatePicker dPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_pick);

        TextView tx1 = (TextView)findViewById(R.id.textView1);
        //현재 날짜를 인텐트에 받아서
        Intent intent = getIntent();
        //변수에 저장
        String first = intent.getExtras().getString("first");
        tx1.setText(first);

        dPicker = (DatePicker) findViewById(R.id.datePicker1);

        btnEnd=(Button)findViewById(R.id.btnEnd);

        //설정완료버튼 클릭시
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이트피커에서 날짜값들을 추출
                int year = dPicker.getYear();
                int month = dPicker.getMonth()+1;
                int date = dPicker.getDayOfMonth();

                //설정완료 버튼 클릭시 결과값을 뉴에딧이나 에딧 엑티비티에 다시 보내줌
                Intent outIntent = new Intent(getApplicationContext(),MainActivity.class);
                outIntent.putExtra("year",year);
                outIntent.putExtra("month",month);
                outIntent.putExtra("date",date);
                setResult(RESULT_OK,outIntent);
                finish();

            }
        });
    }
}
