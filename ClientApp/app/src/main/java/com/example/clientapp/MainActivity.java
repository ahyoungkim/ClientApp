package com.example.clientapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;            // 문자 서비스
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;                    // 간이알림

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M) // SDK 버전 체크
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 권한 체크 요청 */
        checkPermission();

        /* 문자 서비스 */
        final EditText textPhoneNo = findViewById(R.id.editTextPhoneNo);
        final EditText textSMS = findViewById(R.id.editTextSMS);

        Button buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 핸드폰 번호 및 내용 획득
                String phoneNo = textPhoneNo.getText().toString();
                String sms = textSMS.getText().toString();

                try {
                    // 문자 전송
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                    Toast.makeText(getApplicationContext(), "전송 완료!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "SMS failed, please try again later!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    checkPermission();          // 문자 전송 권한 체크
                }
            }
        });

        /* 스캔 버튼 이벤트 */
        Button scanBtn = findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                startActivity(intent);
            }
        });

        /* 종료 버튼 이벤트 */
        Button exitBtn = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 앱 및 프로세스 종료
                finishAffinity();               // 해당 앱의 루트 액티비티를 종료
                System.runFinalization();       // 작업 중인 모든 쓰레드 종료가 완료되면 종료
                System.exit(0);                 // 현재 액티비티 종료
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_resource, menu);

        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.M) // SDK 버전 체크
    private void checkPermission() {

        // 앱 권한을 확인하여 없으면 권한 요청
        requestPermissions(new String[]{
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION
        }, 1000);
    }
}
