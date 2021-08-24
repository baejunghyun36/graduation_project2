package com.minew.beaconset.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import com.minew.beaconset.R;


public class Location extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Button b = (Button)findViewById(R.id.button1);
        final ImageView iv = (ImageView)findViewById(R.id.imageView1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면의 제어권자
                                R.anim.translate_anim);   // 에니메이션 설정 파일
                iv.startAnimation(anim);
            }
        });
    } // end of onCreate
} // end of class
