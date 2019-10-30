package com.example.clientapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;                     // AltBeacon
import org.altbeacon.beacon.BeaconParser;               // AltBeacon
import org.altbeacon.beacon.BeaconTransmitter;          // AltBeacon

import java.util.Arrays;                                // AltBeacon Storage

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        /* 열기 버튼 이벤트 */
        Button openGateBtn = findViewById(R.id.openGateBtn);
        openGateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* AltBeacon 송출 코드 */
                /*  총 28 바이트
                    1 바이트 길이 필드,
                    1 바이트 유형 필드,
                    2 바이트 회사 식별자,
                    24 광고 데이터를 포함 */

                /*  RSSI = -10nlogd + TxPower
                    n은 전파 손실도와 관련된 값, d는 거리. 장애물이 없는 공간에서는 n=2로 생각하고 계산.
                    여기서 얻고싶은 값이 d이기 때문에 이항하여 정리하면,
                    d = 10 ^ ((TxPower - RSSI) / (10 * n))

                    예시)
                    TxPower에 -65를, RSSI에 -59를 넣으면 거리 추정치는 약 2미터
                    10 ^ 0.3 = 1.995262 */
                try {
                    Beacon beacon = new Beacon.Builder()
                            .setId1("2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6") // 16
                            .setId2("1") //2
                            .setId3("2") //2
                            .setManufacturer(0x0118) //2
                            .setRssi(-59)    // 신호세기. 0에서 -127 사이의 부호있는 1 바이트 값
                            .setTxPower(-65) // 송출신호크기.
                            .setDataFields(Arrays.asList(new Long[]{0l})) // 0 ~ 255 Long Type
                            .build();
                    BeaconParser beaconParser = new BeaconParser()
                            .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
                    BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
                    beaconTransmitter.startAdvertising(beacon);
                    Toast.makeText(getApplicationContext(), "AltBeacon started", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "AltBeacon failed", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        /* 닫기 버튼 이벤트 */
        Button backBtn = findViewById(R.id.toMainBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SubActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_resource, menu);

        return true;
    }
}