package com.minew.beaconset.demo;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.minew.beaconset.BluetoothState;
import com.minew.beaconset.ConnectionState;
import com.minew.beaconset.MinewBeacon;
import com.minew.beaconset.MinewBeaconConnection;
import com.minew.beaconset.MinewBeaconConnectionListener;
import com.minew.beaconset.MinewBeaconManager;
import com.minew.beaconset.MinewBeaconManagerListener;
import com.minew.beaconset.R;

import java.util.Collections;
import java.util.List;


public class ParkingLocation extends AppCompatActivity {


    private RecyclerView       mRecycle;
    private MinewBeaconManager mMinewBeaconManager;
    private subBeaconListAdapter  mAdapter;
    UserRssi comp = new UserRssi();
    private       ProgressDialog mpDialog;
    public static MinewBeacon    clickBeacon;
    private static final int REQUEST_ENABLE_BT = 2;
    final float a[] = subBeaconListAdapter.i;
    private int check =0;
    private TextView parking_info;
    private TextView parking_info2;
/*    private Button btn_move;*/
    public static String now_s="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_location);

        parking_info = findViewById(R.id.parking_info);
        String s= ((home)home.context_main).str;
        check=1;
        parking_info.setText(s);

     /*   if(now_s=="1"){
            parking_info = findViewById(R.id.parking_info);
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            String s = bundle.getString("loc");
            now_s = s;
            parking_info.setText(s);
        }

        else{
            parking_info.setText(now_s);
        }
*/

 /*       btn_move = findViewById(R.id.btn1);
        btn_move.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ParkingLocation.this, home.class);
                startActivity(intent);
            }
        });*/


//
//





       /* parking_info = findViewById(R.id.parking_info);
        parking_info2 = findViewById(R.id.parking_info2);*/
    /*    initView();
        initManager();
        checkBluetooth();
        checkLocation();

        dialogshow();*/
       /* mMinewBeaconManager.startService();

        (new Thread(new Runnable() {

            @Override
            public void run() {
                while (!Thread.interrupted())
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() // start actions in UI thread
                        {

                            @Override
                            public void run() {
                                (new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        while (!Thread.interrupted())
                                            try {
                                                Thread.sleep(1000);
                                                runOnUiThread(new Runnable() // start actions in UI thread
                                                {
                                                    @Override
                                                    public void run() {

                                                        parking_info2.setText(a[0] + " " + a[1] + " " + a[2] + " " + a[3]);

                                                    }
                                                });
                                            } catch (InterruptedException e) {
                                                // ooops
                                            }
                                    }
                                })).start();
                            }
                        });
                    } catch (InterruptedException e) {
                        // ooops
                    }
            }
        })).start();*/


       /* String parking_info3 = "000040";
        countDown(parking_info3);*/

        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();    // ????????? ????????????
            }
        });

    }

    /*public void countDown(String time) {
        long conversionTime = 0;

        // 1000 ????????? 1???
        // 60000 ????????? 1???
        // 60000 * 3600 = 1??????
        String getHour = time.substring(0, 2);
        String getMin = time.substring(2, 4);
        String getSecond = time.substring(4, 6);

        // "00"??? ?????????, ????????? ????????? 0 ?????? ??????
        if (getHour.substring(0, 1) == "0") {
            getHour = getHour.substring(1, 2);
        }

        if (getMin.substring(0, 1) == "0") {
            getMin = getMin.substring(1, 2);
        }

        if (getSecond.substring(0, 1) == "0") {
            getSecond = getSecond.substring(1, 2);
        }



        // ????????????
        conversionTime = Long.valueOf(getHour) * 1000 * 3600 + Long.valueOf(getMin) * 60 * 1000 + Long.valueOf(getSecond) * 1000;

        // ????????? ?????? : ????????? ?????? (???????????? 30?????? 30 x 1000(??????))
        // ????????? ?????? : ??????( 1000 = 1???)
        new CountDownTimer(conversionTime, 1000) {

            // ?????? ???????????? ??? ??????
            public void onTick(long millisUntilFinished) {

                // ????????????
//                String hour = String.valueOf(millisUntilFinished / (60 * 60 * 1000));

                // ?????????
                long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000));

//                String min = String.valueOf(getMin / (60 * 1000)); // ???

                // ?????????
                String second = String.valueOf((getMin % (60 * 1000)) / 1000); // ?????????

                // ??????????????? ??????
                String millis = String.valueOf((getMin % (60 * 1000)) % 1000); // ???

         *//*        ????????? ???????????? 0??? ?????????
                if (hour.length() == 1) {
                    hour = "0" + hour;
                }
                 ?????? ???????????? 0??? ?????????
                if (min.length() == 1) {
                    min = "0" + min;
                }
*//*
                // ?????? ???????????? 0??? ?????????
                if (second.length() == 1) {
                    second = "0" + second;
                }

                if(a[0]<a[1]&&a[0]<a[2])check =1;
                else if (a[1]<a[0]&&a[1]<a[2])check =2;
                else if(a[2]<a[0]&&a[2]<a[1])check =3;
                if(check==1){  //??????
                    if(a[0]>a[1]||a[0]>a[2]){
                        if(a[1]<a[2])check =2;
                        else check =3;
                        second="10";
                    }
                }
                else if(check==2){ //??????
                    if(a[1]>a[0]||a[1]>a[2]){
                        if(a[0]<a[2])check =1;
                        else check =3;
                        second="10";
                    }

                }
                else if(check==3){ //??????
                    if(a[2]>a[0]||a[2]>a[1]){
                        if(a[0]<a[1])check =1;
                        else check =2;
                        second="10";
                    }
                }

                parking_info.setText(second);

            }

            // ???????????? ?????????
            public void onFinish() {
                if(check==1){
                    parking_info.setText("?????? 2??? ??????");
                }
                else if(check ==2){
                    parking_info.setText("?????? 2??? ??????");
                }
                else if(check ==3){
                    parking_info.setText("?????? 2??? ??????");
                }
                else{
                    parking_info.setText("?????????");
                }

                // TODO : ???????????? ?????? ???????????? ?????? ???????????? ????????????
            }
        }.start();
    }
    private void initView() {
        mRecycle = (RecyclerView) findViewById(R.id.sub_recyeler2);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycle.setLayoutManager(layoutManager);
        mAdapter = new subBeaconListAdapter();
        mRecycle.setAdapter(mAdapter);
        mRecycle.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager
                .HORIZONTAL));
    }
    private void initManager() {
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
    }
    private void checkLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},101);
        }
    }
    private void checkBluetooth() {
        BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
        switch (bluetoothState) {
            case BluetoothStateNotSupported:
                Toast.makeText(this, "Not Support BLE", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BluetoothStatePowerOff:
                showBLEDialog();
                break;
            case BluetoothStatePowerOn:
                break;
        }
    }
    private void initListener() {
        mMinewBeaconManager.setMinewbeaconManagerListener(new MinewBeaconManagerListener() {
            @Override
            public void onUpdateBluetoothState(BluetoothState state) {
                switch (state) {
                    case BluetoothStatePowerOff:
                        Toast.makeText(getApplicationContext(), "bluetooth off", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothStatePowerOn:
                        Toast.makeText(getApplicationContext(), "bluetooth on", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onRangeBeacons(List<MinewBeacon> beacons) {
                Collections.sort(beacons, comp);
                mAdapter.setData(beacons);
            }
            @Override
            public void onAppearBeacons(List<MinewBeacon> beacons) {
            }
            @Override
            public void onDisappearBeacons(List<MinewBeacon> beacons) {
            }
        });
        mAdapter.setOnItemClickLitener(new subBeaconListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                mpDialog.setMessage(getString(R.string.connecting)
                        + mAdapter.getData(position).getName());
                mpDialog.show();
                mMinewBeaconManager.stopScan();
                //connect to beacon
                MinewBeacon minewBeacon = mAdapter.getData(position);
                MinewBeaconConnection minewBeaconConnection = new MinewBeaconConnection(ParkingLocation.this, minewBeacon);
                minewBeaconConnection.setMinewBeaconConnectionListener(minewBeaconConnectionListener);
                minewBeaconConnection.connect();
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }
    //connect listener;
    MinewBeaconConnectionListener minewBeaconConnectionListener = new MinewBeaconConnectionListener() {
        @Override
        public void onChangeState(MinewBeaconConnection connection, ConnectionState state) {
            switch (state) {
                case BeaconStatus_Connected:
                    mpDialog.dismiss();
                    Intent intent = new Intent(ParkingLocation.this, DetilActivity.class);
                    intent.putExtra("mac", connection.setting.getMacAddress());
                    startActivity(intent);
                    break;
                case BeaconStatus_ConnectFailed:
                case BeaconStatus_Disconnect:
                    if (mpDialog != null) {
                        mpDialog.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
            }
        }
        @Override
        public void onWriteSettings(MinewBeaconConnection connection, boolean success) {
        }
    };

    @Override
    protected void onResume() {
        mMinewBeaconManager.startScan();
        initListener();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMinewBeaconManager.stopScan();
        super.onPause();
    }

    protected void dialogshow() {
        mpDialog = new ProgressDialog(ParkingLocation.this);
        mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mpDialog.setTitle(null);//
        mpDialog.setIcon(null);//
        mpDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
            }
        });
        mpDialog.setCancelable(true);//
        mpDialog.setCanceledOnTouchOutside(false);
    }
    private void showBLEDialog() {
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                mMinewBeaconManager.startScan();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        mMinewBeaconManager.stopService();
        super.onDestroy();
    }
*/
}