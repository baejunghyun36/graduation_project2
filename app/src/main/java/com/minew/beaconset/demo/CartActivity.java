package com.minew.beaconset.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.minew.beaconset.R;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    public  static ArrayList<ItemData> arrayList2 = MainActivity.arrayList;
    public  ItemAdapter itemAdapter2 = MainActivity.itemAdapter;
    public  RecyclerView recyclerView;
    public  LinearLayoutManager linearLayoutManager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView=(RecyclerView)findViewById(R.id.cartList);
        linearLayoutManager2 = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager2);
        itemAdapter2 = new ItemAdapter(arrayList2);
        recyclerView.setAdapter(itemAdapter2);

        arrayList2 = MainActivity.arrayList;
        MainActivity.arrayList = arrayList2;
        MainActivity.recyclerView =recyclerView;
        MainActivity.itemAdapter.notifyDataSetChanged(); // main의 리사이클뷰와 동기화

        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(MainActivity.recyclerView == recyclerView){
                    onBackPressed();
                }
            }
        });


    }
}