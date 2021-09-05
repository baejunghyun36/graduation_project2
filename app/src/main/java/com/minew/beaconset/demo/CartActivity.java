package com.minew.beaconset.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.minew.beaconset.R;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    //public ArrayList<ItemData> Cart_arrayList;
    //public ItemAdapter Cart_mainAdapter;
    //public RecyclerView Cart_recyclerView;
    //public LinearLayoutManager Cart_linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ImageButton btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();    // 이전 페이지로 돌아가기
            }
        });
    }
}