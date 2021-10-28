package com.minew.beaconset.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.minew.beaconset.R;


public class SearchActivity extends AppCompatActivity {
    public static int id[] = home.id;
    private TextView SearchingItem;
    public static int Last_num = home.Basket_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String search_item = intent.getStringExtra("search_item");
        setContentView(R.layout.activity_search);

        SearchingItem =  findViewById(R.id.tv_searching);
        SearchingItem.setText(home.name[0]);

        Button btn_inCart = findViewById(R.id.btn_inCart);
        Button direct_optimal = findViewById(R.id.btn_direct_optimal);

        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();    // 이전 페이지로 돌아가기
            }
        });


        /*
        btn_inCart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });


        direct_optimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, Optimal_Distance.class);
              //  intent.putExtra("OptimalPath", SearchingItem);

                startActivity(intent);
            }
        });
       */
    }
}