package com.minew.beaconset.demo;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.minew.beaconset.BluetoothState;
import com.minew.beaconset.ConnectionState;
import com.minew.beaconset.MinewBeacon;
import com.minew.beaconset.MinewBeaconConnection;
import com.minew.beaconset.MinewBeaconConnectionListener;
import com.minew.beaconset.MinewBeaconManager;
import com.minew.beaconset.MinewBeaconManagerListener;
import com.minew.beaconset.R;

import pl.polidea.view.ZoomView;

import java.util.Collections;
import java.util.List;
//은윤 4:26 수정
//충헌 4:42
public class SubActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "AnimationActivity";
    private LinearLayout ll1;
    private Button btn1;
    private ImageView btn2;
    private Button btn3;
    private float screenWidth;
    private float screenHeight;

    private float toX = 0, fromY = 0;
    private float nowX=400, nowY = 400;
    public String res = MainActivity.rest2;


    private float x = nowX;
    private float y = nowY;

    int[] pos=new int[4];

    public static TextView tv_sub;
    private TextView dis_result;
    BeaconListAdapter SubActivity = new BeaconListAdapter();

    private TextView tv_id, tv_pass;
    private RecyclerView       mRecycle;
    private MinewBeaconManager mMinewBeaconManager;

    private subBeaconListAdapter  mAdapter;
    UserRssi comp = new UserRssi();
    private       ProgressDialog mpDialog;
    public static MinewBeacon    clickBeacon;
    private static final int REQUEST_ENABLE_BT = 2;

    final float a[] = subBeaconListAdapter.i;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;




    private String str1 = SubActivity.distance;
    private String str2 = SubActivity.location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
    //    String rest = MainActivity.rest2;
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.zoom_item, null, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        final ZoomView zoomView = new ZoomView(this);
        zoomView.addView(v);
        zoomView.setLayoutParams(layoutParams);
        zoomView.setMiniMapEnabled(true); // 좌측 상단 검은색 미니맵 설정
        zoomView.setMaxZoom(4f); // 줌 Max 배율 설정  1f 로 설정하면 줌 안됩니다.
        zoomView.setMiniMapCaption("Mini Map Test"); //미니 맵 내용
        zoomView.setMiniMapCaptionSize(20); // 미니 맵 내용 글씨 크기 설정

        (new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                while (!Thread.interrupted())
                    try
                    {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() // start actions in UI thread
                        {

                            @Override
                            public void run()
                            {

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
                                                         x= nowX;
                                                         y= nowY;
                                                         zoomView.zoomTo(1.8f,x+200,y+100);

                                                    }
                                                });
                                            } catch (InterruptedException e) {
                                                // ooops
                                            }
                                    }
                                })).start();


                            }
                        });
                    }
                    catch (InterruptedException e)
                    {
                        // ooops
                    }
            }
        })).start();

        RelativeLayout container = (RelativeLayout) findViewById(R.id.container1);
        container.addView(zoomView);

        initView();
        initManager();
        checkBluetooth();
        checkLocation();

        dialogshow();

        init();
        mMinewBeaconManager.startService();

        tv1= findViewById(R.id.test1);
        tv2= findViewById(R.id.test2);
        tv3= findViewById(R.id.test3);
        tv4= findViewById(R.id.test4);
        btn2.setX(nowX);  //이미지 초기 값 -> 정 가운데
        btn2.setY(nowY);
        btn2.getLayoutParams().height = 123;
        btn2.getLayoutParams().width = 123;
        (new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                while (!Thread.interrupted())
                    try
                    {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() // start actions in UI thread
                        {

                            @Override
                            public void run()
                            {
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
                                                        tv1.setText("정현이꺼 " + a[0]);
                                                        tv2.setText("은윤이꺼 " + a[1]);
                                                        tv3.setText("충헌이꺼 " + a[2]);
                                                        tv4.setText("교수님꺼 " + a[3]);
                                                        btn2.setX(nowX);  //이미지 초기 값 -> 정 가운데
                                                        btn2.setY(nowY);
                                                        String rest = MainActivity.rest2;
                                                        tv_sub = findViewById(R.id.tv_sub);
                                                        tv_sub.setText(rest);
                                           //             tv_sub = findViewById(R.id.tv_sub);


                                                        //    Intent intent = getIntent();
                                                        //    String str = intent.getStringExtra("item");
                                             //           tv_sub.setText(res);
                                                        nowX -= 2;
                                                        nowY -= 2;


                                                        // 3개 비콘 distance에 따라서 별 이동하도록 nowX와 nowY 설정하기

                                                        // 화면 밖으로 나가는거 막아주기
                                                        if(nowX<0&&nowY<0){
                                                            nowY=0;
                                                            nowX=0;
                                                        }
                                                        else if((nowX==0)&&(nowY>0)){
                                                            nowX=0;
                                                        }
                                                        else if((nowY==0)&&(nowX>0)){
                                                            nowY=0;
                                                        }
                                                        else if(nowX>1000&&nowY>1000){
                                                            nowY=1000;
                                                            nowX=1000;
                                                        }


                                                    }
                                                });
                                            } catch (InterruptedException e) {
                                                // ooops
                                            }
                                    }
                                })).start();


                            }
                        });
                    }
                    catch (InterruptedException e)
                    {
                        // ooops
                    }
            }
        })).start();

     //   tv_sub = findViewById(R.id.tv_sub);


    //    Intent intent = getIntent();
   //     String str = intent.getStringExtra("item");
    //    tv_sub.setText(rest);



    }



    private void init(){



        ll1 = findViewById(R.id.ll1);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);

        btn1.setOnClickListener((View.OnClickListener) this);
        btn2.setOnClickListener((View.OnClickListener) this);
        btn3.setOnClickListener((View.OnClickListener) this);

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        Log.e(TAG, "Width : " + point.x + " , Height : " + point.y);

        screenWidth = point.x;
        screenHeight = point.y;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn1 :

                fromY = screenHeight - ll1.getHeight() - btn2.getHeight();
                Log.e(TAG, "fromX : " + toX + ",  fromY : " +fromY);

                (new Thread(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        while (!Thread.interrupted())
                            try
                            {
                                Thread.sleep(1000);
                                runOnUiThread(new Runnable() // start actions in UI thread
                                {

                                    @Override
                                    public void run()
                                    {
                                        if(a[0]-a[1]<0){
                                            toX=500;
                                        }
                                        else{
                                            toX=0;
                                        }

                                        TranslateAnimation animation = new TranslateAnimation(nowX, toX, 0, 0);

                                        animation.setDuration(6000);
                                        animation.setFillAfter(false);
                                        animation.setFillEnabled(true);
                                        animation.setAnimationListener(new Animation.AnimationListener() {
                                            @Override
                                            public void onAnimationStart(Animation animation) {
                                            }
                                            @Override
                                            public void onAnimationEnd(Animation animation) {
                                                pos[0]= btn2.getLeft();
                                                pos[1]= btn2.getTop() ;
                                                pos[2]= btn2.getRight()+ (int)fromY;;
                                                pos[3]= btn2.getTop();
                                                btn2.layout(pos[0], pos[1], pos[2], pos[3]);
                                                Log.e(TAG, " 1: " + btn2.getLeft() + " , 2 : " + btn2.getTop() + "  , 3 : "  + btn2.getRight() + "  , 4 : " + btn2.getBottom());
                                            }
                                            @Override
                                            public void onAnimationRepeat(Animation animation) {

                                            }
                                        });
                                        btn2.startAnimation(animation);

                                        if(toX==500)nowX+=41;
                                        else nowX-=41;

                                    }
                                });
                            }
                            catch (InterruptedException e)
                            {
                                // ooops
                            }
                    }
                })).start();

                break;


            case R.id.btn3 :

                Log.e(TAG, "fromX : " + toX + ",  fromY : " +fromY);

                TranslateAnimation animation3 = new TranslateAnimation(0,  -toX, 0, -fromY);
                animation3.setDuration(3000);
                animation3.setFillAfter(false);
                animation3.setFillEnabled(true);
                animation3.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        btn2.layout(0, 0, btn2.getWidth(), btn2.getHeight());
                        Log.e(TAG, " 1: " + btn2.getLeft() + " , 2 : " + btn2.getTop() + "  , 3 : "  + btn2.getRight() + "  , 4 : " + btn2.getBottom());
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                btn2.startAnimation(animation3);

                toX = 0;
                fromY = 0;
                break;

            default:
                break;
        }
    }

    private void initView() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
//        setSupportActionBar(toolbar);
        mRecycle = (RecyclerView) findViewById(R.id.sub_recyeler);
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
                MinewBeaconConnection minewBeaconConnection = new MinewBeaconConnection(SubActivity.this, minewBeacon);
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
                    Intent intent = new Intent(SubActivity.this, DetilActivity.class);
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
        mpDialog = new ProgressDialog(SubActivity.this);
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


