package com.minew.beaconset.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.minew.beaconset.R;

public class ParkingLocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_location);

        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();    // 집으로 돌아가기ㅛ료ㅠㅗㅠㅠㅛㅕㅠㅛㅠㅕㅑㅗㅠㅑ
            }
        });

    }

}