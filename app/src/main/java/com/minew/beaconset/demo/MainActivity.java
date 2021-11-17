package com.minew.beaconset.demo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView tv_id, tv_pass;
    private MinewBeaconManager mMinewBeaconManager;
    private BeaconListAdapter  mAdapter;
    UserRssi comp = new UserRssi();
    private       ProgressDialog mpDialog;
    public static MinewBeacon    clickBeacon;
    private static final int REQUEST_ENABLE_BT = 2;
    private RecyclerView recyclerview;
    public Dialog custom_dialog; // 커스텀 다이얼로그

    private DrawerLayout drawerLayout;
    private View drawerView;
    private ImageView btn_move;
    private Button  btn_search;

    public static int search_complete = 0;
    public static String basket[] = new String[40];
    public static int Basket_index = 0;
    private static String TAG = "phpquerytest";
    public static String rest[] = new String[10];
    public static String name[] = new String[40];
    public static int[] item_location_x = new int[40];
    public static int[] item_location_y = new int[40];

    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로 가기 버튼을 누를 때 표시
    private Toast toast;

    public static int[] item_location_x2 = new int[40];
    public static int[] item_location_y2 = new int[40];
    public static int id[] = new int[40];
    private static final String TAG_JSON = "webnautes";
    private static final String TAG_ADDRESS = "rest";
    private static final String TAG_NAME = "name";
    private static final String TAG_id = "id";
    private static final String TAG_X = "x";
    private static final String TAG_Y = "y";
    String mJsonString;
    EditText item_find;
    private Button menu_1;
    private Button menu_2;
    private Button menu_3;
    private Button btn_current, btn_my_page;
    public Button[] items = new Button[12]; // 아이템 버튼 배열
    public TextView[] tvs = new TextView[12];
    public String[] item_names = new String[]{"새우깡", "포테토칩", "꼬깔콘", "바나나킥", "썬칩", "칸쵸", "빈츠", "초코칩"};


    public static ArrayList<ItemData> arrayList;
    public static ItemAdapter itemAdapter;
    public static RecyclerView recyclerView;
    public static LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //tv_id = findViewById(R.id.tv_id);
        //tv_pass = findViewById(R.id.tv_pass);

        Intent intent = getIntent();
        final String userID = intent.getStringExtra("userID");
        String userPass = intent.getStringExtra("userPass");
        final String userName = intent.getStringExtra("userName");

        custom_dialog = new Dialog(MainActivity.this);       // Dialog 초기화
        custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        custom_dialog.setContentView(R.layout.custom_dialog);   //커스텀 다이얼로그 연결

     //   tv_id.setText(userID);
      //  tv_pass.setText(userPass);

        btn_current = findViewById(R.id.btn_current_location);
        btn_my_page = findViewById(R.id.btn_my_page);
        btn_search = findViewById(R.id.btn_search);
        items[0] = findViewById(R.id.BtnNum1);
        items[1] = findViewById(R.id.BtnNum2);
        items[2] = findViewById(R.id.BtnNum3);
        items[3] = findViewById(R.id.BtnNum4);
        items[4] = findViewById(R.id.BtnNum5);
        items[5] = findViewById(R.id.BtnNum6);
        items[6] = findViewById(R.id.BtnNum7);
        items[7] = findViewById(R.id.BtnNum8);

        menu_1 = findViewById(R.id.menu_1);
        menu_2 = findViewById(R.id.menu_2);
        menu_3 = findViewById(R.id.menu_3);



        recyclerView=(RecyclerView)findViewById(R.id.rv);   //여기서 cartList를 불러올 수 없는건가
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList=new ArrayList<>();
        itemAdapter = new ItemAdapter(arrayList);
        recyclerView.setAdapter(itemAdapter);

        item_find = (EditText)findViewById(R.id.item);
        btn_move = findViewById(R.id.btn_move);
        btn_move.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, CurrentLoc.class);
                startActivity(intent);
            }
        });
        // 물품 검색
        btn_search.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                GetData task = new GetData();  // DB UPLOAD
                task.execute("");

            }
        });
        btn_current.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                for(int i  = 0; i < Basket_index; i++){
                    GetData task = new GetData();
                    task.execute(basket[i]);
                }
            }
        });
        btn_my_page.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(MainActivity.this, mypages.class);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });

        menu_1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, Best.class);
                startActivity(intent);
            }
        });
        menu_2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, Meat.class);
                startActivity(intent);
            }
        });
        menu_3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, fruit.class);
                startActivity(intent);
            }
        });



        // item 버튼이랑 팝업 연결
        items[0].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDialog(item_names[0], R.drawable.potato);
            }
        });
        items[1].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDialog(item_names[1], R.drawable.shrimp);
            }
        });
        items[2].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDialog(item_names[2], R.drawable.choco);
            }
        });
        items[3].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDialog(item_names[3], R.drawable.turtle);
            }
        });
        items[4].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDialog(item_names[4], R.drawable.turtle);
            }
        });
        items[5].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDialog(item_names[5], R.drawable.turtle);
            }
        });
        items[6].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDialog(item_names[6], R.drawable.turtle);
            }
        });
        items[7].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDialog(item_names[7], R.drawable.turtle);
            }
        });



        initView();
        initManager();
        checkBluetooth();
        checkLocation();

        dialogshow();
        mMinewBeaconManager.startService();


        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer );


        ImageButton btn_open1 = (ImageButton)findViewById(R.id.btn_open1);
        btn_open1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });


        Button btn_close = (Button)findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener((new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        }));
    }

    // custom_dialog 디자인
    public void showDialog(final String B, final int iv){
        //버튼 글씨랑 이미지뷰 띄울거야..
        String item_name = B;
        TextView tv_item = custom_dialog.findViewById(R.id.item);
        tv_item.setText(item_name);
        ImageView iv_item = custom_dialog.findViewById(R.id.item_image);
        iv_item.setImageResource(iv);
        TextView answerCart = findViewById(R.id.answerCart);

        custom_dialog.show(); // 다이얼로그 띄우기

        // YES 버튼
        custom_dialog.findViewById(R.id.yesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tmp = B;
                basket[Basket_index++] = tmp;


                ItemData itemData = new ItemData(iv,B);
                arrayList.add(itemData);    // 해당 아이템 추가
                itemAdapter.notifyDataSetChanged(); //새로고침

                Toast.makeText(getApplicationContext(),"장바구니에 담았습니다!",
                        Toast.LENGTH_SHORT).show();

                custom_dialog.dismiss(); // 다이얼로그 닫기
            }
        });

        // NO 버튼
        Button noBtn = custom_dialog.findViewById(R.id.noBtn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"취소되었습니다!",
                        Toast.LENGTH_SHORT).show();

                custom_dialog.dismiss(); // 다이얼로그 닫기
            }
        });
    }

    DrawerLayout.DrawerListener listener=new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        }
        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
        }
        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
        }
        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };
        @Override
        public void onBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
                backKeyPressedTime = System.currentTimeMillis();
                toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
            // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
            if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
                finish();
                toast.cancel();
                toast = Toast.makeText(this,"이용해 주셔서 감사합니다.",Toast.LENGTH_LONG);
                toast.show();
            }
        }
    private void initView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mAdapter = new BeaconListAdapter();
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
        //scan listener;
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

        mAdapter.setOnItemClickLitener(new BeaconListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                mpDialog.setMessage(getString(R.string.connecting)
                        + mAdapter.getData(position).getName());
                mpDialog.show();
                mMinewBeaconManager.stopScan();
                MinewBeacon minewBeacon = mAdapter.getData(position);
                MinewBeaconConnection minewBeaconConnection = new MinewBeaconConnection(MainActivity.this, minewBeacon);
                minewBeaconConnection.setMinewBeaconConnectionListener(minewBeaconConnectionListener);
                minewBeaconConnection.connect();
            }
            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }
    MinewBeaconConnectionListener minewBeaconConnectionListener = new MinewBeaconConnectionListener() {
        @Override
        public void onChangeState(MinewBeaconConnection connection, ConnectionState state) {
            switch (state) {
                case BeaconStatus_Connected:
                    mpDialog.dismiss();
                    Intent intent = new Intent(MainActivity.this, DetilActivity.class);
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
        mpDialog = new ProgressDialog(MainActivity.this);
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
    public class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mJsonString = result;
            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject item = jsonArray.getJSONObject(i);
                    String item_name = item.getString(TAG_NAME);
                    String x = item.getString(TAG_X);
                    String y = item.getString(TAG_Y);
                    int Item_id = Integer.parseInt(item.getString(TAG_id));
                    item_location_x[search_complete] = Integer.parseInt(x);
                    item_location_y[search_complete] = Integer.parseInt(y);
                    item_location_x2[Item_id] = Integer.parseInt(x);
                    item_location_y2[Item_id] = Integer.parseInt(y);
                    id[search_complete] = Item_id;
                    name[Item_id] = item_name;
                    search_complete++;

                }
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
            if(search_complete == Basket_index) {
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                startActivity(intent);
            }
        }
        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];
            String searchKeyword2 = params[0];

            String serverURL = home.commonURL;
            String postParameters = "country=" + searchKeyword1 + "&name=" + searchKeyword2;
            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }
}