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

    private Button  btn_move, btn_search;

    public static int Basket_index;
    private static String TAG = "phpquerytest";
    public static String rest[] = new String[10];
    public static String name[] = new String[10];
    public static int[] item_location_x = new int[20];
    public static int[] item_location_y = new int[20];
    public static String id[] = new String[10];
    private static final String TAG_JSON = "webnautes";
    private static final String TAG_ADDRESS = "rest";
    private static final String TAG_id = "id";
    private static final String TAG_X = "x";
    private static final String TAG_Y = "y";
    String mJsonString;
    EditText item_find;

    private Button btn_current, btn_my_page;
    public Button[] items = new Button[12]; // 아이템 버튼 배열

    public static ArrayList<ItemData> arrayList;
    public static ItemAdapter itemAdapter;
    public static RecyclerView recyclerView;
    public static LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_id = findViewById(R.id.tv_id);
        tv_pass = findViewById(R.id.tv_pass);

        Intent intent = getIntent();
        final String userID = intent.getStringExtra("userID");
        String userPass = intent.getStringExtra("userPass");
        final String userName = intent.getStringExtra("userName");

        custom_dialog = new Dialog(MainActivity.this);       // Dialog 초기화
        custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        custom_dialog.setContentView(R.layout.custom_dialog);   //커스텀 다이얼로그 연결

        tv_id.setText(userID);
        tv_pass.setText(userPass);

        btn_current = findViewById(R.id.btn_current_location);
        btn_my_page = findViewById(R.id.btn_my_page);
        btn_search = findViewById(R.id.btn_search);
        items[0] = findViewById(R.id.BtnNum1);
        items[1] = findViewById(R.id.BtnNum2);
        items[2] = findViewById(R.id.BtnNum3);
        items[3] = findViewById(R.id.BtnNum4);



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
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                startActivity(intent);
            }
        });
        // 물품 검색
        btn_search.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                if ( item_find.getText().toString().length() != 0 ) { //검색창 Null 아닐때
                    GetData task = new GetData();  // DB UPLOAD
                    task.execute(item_find.getText().toString());
              //      Intent intent = new Intent(MainActivity.this, SearchActivity.class);
          //          startActivity(intent);
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
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                startActivity(intent);
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


        // item 버튼이랑 팝업 연결
        items[0].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){    // 감자
                showDialog(items[0], R.drawable.potato);
            }
        });
        items[1].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){    // 새우
                showDialog(items[1], R.drawable.shrimp);
            }
        });
        items[2].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){    //초코
                showDialog(items[2], R.drawable.choco);
            }
        });
        items[3].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){    // 꼬북북
                showDialog(items[3], R.drawable.turtle);
            }
        });


        recyclerview = findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data = new ArrayList<>();

        ExpandableListAdapter.Item group1 = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "정육/계란");
        group1.invisibleChildren = new ArrayList<>();
        group1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "한우"));
        group1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "수입육"));
        group1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "돼지고기"));
        group1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "닭/오리고기"));
        group1.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "계란"));


        ExpandableListAdapter.Item group2 = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "과일");
        group2.invisibleChildren = new ArrayList<>();
        group2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "사과"));
        group2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "바나나"));
        group2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "포도"));
        group2.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "키위"));

        ExpandableListAdapter.Item group3 = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "유제품/베이커리");
        group3.invisibleChildren = new ArrayList<>();
        group3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "우유"));
        group3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "치즈"));
        group3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "저지방우유"));
        group3.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "요플레"));

        ExpandableListAdapter.Item group4 = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "세제/욕실/청소");
        group4.invisibleChildren = new ArrayList<>();
        group4.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "persil"));
        group4.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "다우니"));
        group4.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "습기제거"));
        group4.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "락스"));

        data.add(group1);
        data.add(group2);
        data.add(group3);
        data.add(group4);


        recyclerview.setAdapter(new ExpandableListAdapter(data));






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
    public void showDialog(final Button B, final int iv){
        //버튼 글씨랑 이미지뷰 띄울거야..
        String item_name = B.getText().toString();
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

         //       GetData task = new GetData();
      //          String tmp = B.getText().toString();
       //         task.execute(tmp);


                ItemData itemData = new ItemData(iv,B.getText().toString()+"");
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
/*
    public void onClickShowAlert(View view, final Button B, final int iv) {
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(MainActivity.this);

        myAlertBuilder.setTitle("장바구니 버튼");
        myAlertBuilder.setMessage(B.getText().toString()+"을(를) 장바구니에 담으시겠어요?");
        //myAlertBuilder.setView(R.drawable.choco).show();

        final Button pushItem = B;
        // Yes Button or No Button
        myAlertBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                GetData task = new GetData();
                String tmp = B.getText().toString();
                task.execute(tmp);
                ItemData itemData = new ItemData(R.drawable.turtle,pushItem.getText().toString()+"");
                arrayList.add(itemData);    // 해당 아이템 추가
                itemAdapter.notifyDataSetChanged(); //새로고침

                Toast.makeText(getApplicationContext(),"장바구니에 담았습니다!",
                        Toast.LENGTH_SHORT).show();
            }
        });
        myAlertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"취소되었습니다!",
                        Toast.LENGTH_SHORT).show();
            }
        });
        myAlertBuilder.show();
    }

 */
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
                    String address = item.getString(TAG_ADDRESS);
                    String x = item.getString(TAG_X);
                    String y = item.getString(TAG_Y);
                    String Item_id = item.getString(TAG_id);
                    rest[Basket_index] = address;
                    item_location_x[Basket_index] = Integer.parseInt(x);
                    item_location_y[Basket_index] = Integer.parseInt(y);
                    id[Basket_index] = Item_id;
                    Basket_index = Basket_index+1;
        //            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
           //         startActivity(intent);
                }
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            Basket_index = Basket_index +1;
            startActivity(intent);
        }
        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];
            String searchKeyword2 = params[0];

            String serverURL = "http://192.168.0.15/load.php";
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