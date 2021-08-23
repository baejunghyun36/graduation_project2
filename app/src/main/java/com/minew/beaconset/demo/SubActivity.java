package com.minew.beaconset.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.minew.beaconset.MinewBeacon;
import com.minew.beaconset.R;

import org.w3c.dom.Text;

import java.util.List;

public class SubActivity extends AppCompatActivity {


    private TextView tv_sub;
    private TextView dis_result;

    BeaconListAdapter mainActivity = new BeaconListAdapter();




    private String str1 = mainActivity.distance;
    private String str2 = mainActivity.location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);


        tv_sub = findViewById(R.id.tv_sub);
        dis_result = findViewById(R.id.dis_result);

        Intent intent = getIntent();
        String str = intent.getStringExtra("str");
        tv_sub.setText(str);




        dis_result.setText(str1+" "+str2);


    }


}


