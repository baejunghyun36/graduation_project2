package com.minew.beaconset.demo;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class home extends AppCompatActivity {

    public static Context context_main;
    public String str;


    private Button btn_move;

    private Button btn_search;
    private ImageView btnnum2;
    private DrawerLayout drawerLayout;
    private View drawerView;
    private Button btn_current, btn_my_page;
    EditText item_find;

    //parking
    private RecyclerView mRecycle;
    private MinewBeaconManager mMinewBeaconManager;
    private subBeaconListAdapter  mAdapter;
    UserRssi comp = new UserRssi();
    private ProgressDialog mpDialog;
    public static MinewBeacon clickBeacon;
    private static final int REQUEST_ENABLE_BT = 2;
    final float a[] = subBeaconListAdapter.i;
    public static int check;
    private TextView parking_info;
    private TextView parking_info2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context_main =this;
        Intent intent = getIntent();
        final String userID = intent.getStringExtra("userID");
        String userPass = intent.getStringExtra("userPass");
        final String userName = intent.getStringExtra("userName");

        btn_move = findViewById(R.id.btn_move);
        btn_search = findViewById(R.id.btn_search);
        item_find = (EditText)findViewById(R.id.item) ;
        btnnum2 = findViewById(R.id.BtnNum2);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer );
        btn_current = findViewById(R.id.btn_current_location);
        btn_my_page = findViewById(R.id.btn_my_page);


        //parking
        parking_info = findViewById(R.id.parking_info);
        parking_info2 = findViewById(R.id.parking_info2);
        initView();
        initManager();
        checkBluetooth();
        checkLocation();
        dialogshow();
        mMinewBeaconManager.startService();

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
        })).start();



        String parking_info3 = "000005";
        countDown(parking_info3);

        btn_move.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(home.this, SubActivity.class);
                startActivity(intent);
            }
        });


        btn_search.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if ( item_find.getText().toString().length() != 0 ) { //검색창 Null 아닐때
                    Intent intent = new Intent(home.this, SearchActivity.class);
                    intent.putExtra("SearchingItem", item_find.getText().toString());
                    startActivity(intent);
                }
                else {    // 검색창 Null일때
                    Toast.makeText(getApplicationContext(),"검색어를 입력해주세요",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_current.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(home.this, SubActivity.class);
                startActivity(intent);
            }
        });
        btn_my_page.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(home.this, mypages.class);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });

        ImageButton btn_open1 = (ImageButton)findViewById(R.id.btn_open1);
        btn_open1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        btnnum2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(home.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    public void countDown(String time) {


        long conversionTime = 0;

        // 1000 단위가 1초
        // 60000 단위가 1분
        // 60000 * 3600 = 1시간
        String getHour = time.substring(0, 2);
        String getMin = time.substring(2, 4);
        String getSecond = time.substring(4, 6);

        // "00"이 아니고, 첫번째 자리가 0 이면 제거
        if (getHour.substring(0, 1) == "0") {
            getHour = getHour.substring(1, 2);
        }

        if (getMin.substring(0, 1) == "0") {
            getMin = getMin.substring(1, 2);
        }

        if (getSecond.substring(0, 1) == "0") {
            getSecond = getSecond.substring(1, 2);
        }



        // 변환시간
        conversionTime = Long.valueOf(getHour) * 1000 * 3600 + Long.valueOf(getMin) * 60 * 1000 + Long.valueOf(getSecond) * 1000;

        // 첫번쨰 인자 : 원하는 시간 (예를들어 30초면 30 x 1000(주기))
        // 두번쨰 인자 : 주기( 1000 = 1초)
        new CountDownTimer(conversionTime, 1000) {

            // 특정 시간마다 뷰 변경
            public void onTick(long millisUntilFinished) {

                // 시간단위
//                String hour = String.valueOf(millisUntilFinished / (60 * 60 * 1000));

                // 분단위
                long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000));

//                String min = String.valueOf(getMin / (60 * 1000)); // 몫

                // 초단위
                String second = String.valueOf((getMin % (60 * 1000)) / 1000); // 나머지

                // 밀리세컨드 단위
                String millis = String.valueOf((getMin % (60 * 1000)) % 1000); // 몫


                // 초가 한자리면 0을 붙인다
                if (second.length() == 1) {
                    second = "0" + second;
                }

                if(a[0]<a[1]&&a[0]<a[2])check =1;
                else if (a[1]<a[0]&&a[1]<a[2])check =2;
                else if(a[2]<a[0]&&a[2]<a[1])check =3;
                if(check==1){  //정현
                    if(a[0]>a[1]||a[0]>a[2]){
                        if(a[1]<a[2])check =2;
                        else check =3;
                        second="10";
                    }
                }
                else if(check==2){ //은윤
                    if(a[1]>a[0]||a[1]>a[2]){
                        if(a[0]<a[2])check =1;
                        else check =3;
                        second="10";
                    }

                }
                else if(check==3){ //충헌
                    if(a[2]>a[0]||a[2]>a[1]){
                        if(a[0]<a[1])check =1;
                        else check =2;
                        second="10";
                    }
                }

                parking_info.setText(second);

            }

            // 제한시간 종료시
            public void onFinish() {
                if(check==1){
//                    parking_info.setText("지하 2층 정현");
                    str= "지하 2층 정현";
                  /*      Intent intent = new Intent(getBaseContext(),ParkingLocation.class);
                        intent.putExtra("loc",loc);
                        startActivity(intent);*/

                }
                else if(check ==2){
//                    parking_info.setText("지하 2층 은윤");
                    /*       String loc = "지하 2층 은윤";*/

                    str= "지하 2층 정현";
                      /*  Intent intent = new Intent(getBaseContext(),ParkingLocation.class);
                        intent.putExtra("loc",loc);
                        startActivity(intent);*/
                }
                else if(check ==3){
//                    parking_info.setText("지하 2층 충헌");
                    str= "지하 2층 정현";
                    /*                       String loc = "지하 2층 충헌";*/
                  /*      Intent intent = new Intent(getBaseContext(),ParkingLocation.class);
                        intent.putExtra("loc",loc);
                        startActivity(intent);*/
                }
                else{
//                      parking_info.setText("뚜벅이");
                    /*     String loc = "뚜벅이";*/
                    str= "지하 2층 정현";
         /*               Intent intent = new Intent(getBaseContext(),ParkingLocation.class);
                        intent.putExtra("loc",loc);
                        startActivity(intent);*/
                }
                check =1;



                //

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
                MinewBeaconConnection minewBeaconConnection = new MinewBeaconConnection(home.this, minewBeacon);
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
                    Intent intent = new Intent(home.this, DetilActivity.class);
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
                                Toast.makeText(getApplicationContext(), "연결이 끊어졌습니다.", Toast.LENGTH_SHORT).show();
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
        mpDialog = new ProgressDialog(home.this);
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




}