package com.minew.beaconset.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.minew.beaconset.R;



public class mypages extends AppCompatActivity {

    private Button list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypages);

        TextView client = findViewById(R.id.client_name);
        ImageButton btn_back = findViewById(R.id.btn_back);
        Button btn_parking = findViewById(R.id.parking);
        list = findViewById(R.id.toCartBtn);
        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        client.setText(userName+ " 님 반갑습니다 :)");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();    // 이전 페이지로 돌아가기
            }
        });
        list.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(mypages.this, CartActivity.class);
                startActivity(intent);
            }
        });

        btn_parking.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mypages.this, ParkingLocation.class);
                startActivity(intent);
            }
        });
    }

}