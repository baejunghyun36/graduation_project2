package com.minew.beaconset.demo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.minew.beaconset.ConnectionState;
import com.minew.beaconset.MinewBeaconConnection;
import com.minew.beaconset.MinewBeaconConnectionListener;
import com.minew.beaconset.MinewBeaconSetting;
import com.minew.beaconset.R;

import java.util.ArrayList;
import java.util.List;

public class DetilActivity extends AppCompatActivity {

    private RecyclerView     mRecycle;
    private DetilListAdapter mAdapter;
    private View             mView;
    private EditText         mEt_data;

    private TextView              mBeacon_save;
    private ProgressDialog        mpDialog;
    private MinewBeaconConnection mMinewBeaconConnection;
    private MinewBeaconSetting    mMinewBeaconSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil);

        initView();
        initConnection();
        initData();
        initListener();
        dialogshow();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.detil_toolbar);
        setSupportActionBar(toolbar);

        mRecycle = (RecyclerView) findViewById(R.id.detil_recyeler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycle.setLayoutManager(layoutManager);
        mAdapter = new DetilListAdapter();
        mRecycle.setAdapter(mAdapter);
        mRecycle.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager
                .HORIZONTAL));

        mBeacon_save = (TextView) findViewById(R.id.beacon_save);
    }

    private void initConnection() {
        String mac = getIntent().getStringExtra("mac");
        mMinewBeaconConnection = MinewBeaconConnection.minewBeaconConnections.get(mac);
        ConnectionState state = mMinewBeaconConnection.state;
    }

    private void initData() {
        List<String> listname = new ArrayList<>();

        listname.add(getString(R.string.uuid));
        listname.add(getString(R.string.major));
        listname.add(getString(R.string.minor));
        listname.add(getString(R.string.measured_power));
        listname.add(getString(R.string.transimssion_power));
        listname.add(getString(R.string.broadcasting_interval));
        listname.add(getString(R.string.serial_id));
        listname.add(getString(R.string.ibeacon_name));
        listname.add(getString(R.string.connection_mode));
        listname.add(getString(R.string.restart_password));
        //sysinfo
        listname.add(getString(R.string.manufacturer_name));
        listname.add(getString(R.string.model));
        listname.add(getString(R.string.sys_serial_id));
        listname.add(getString(R.string.hardware_revision));
        listname.add(getString(R.string.firmware_revision));
        listname.add(getString(R.string.software_revision));
        listname.add(getString(R.string.system_id));
        listname.add(getString(R.string.regulatorycertifactiondata));

        mAdapter.setListname(listname);

        mMinewBeaconSetting = mMinewBeaconConnection.setting;
        List<String> listdata = getList();
        mAdapter.setData(listdata);
    }

    @NonNull
    private List<String> getList() {
        List<String> listdata = new ArrayList<>();
        listdata.add(mMinewBeaconSetting.getUuid());
        listdata.add(mMinewBeaconSetting.getMajor() + "");
        listdata.add(mMinewBeaconSetting.getMinor() + "");
        listdata.add(mMinewBeaconSetting.getCalibratedTxPower() + "");
        listdata.add(mMinewBeaconSetting.getTxPower() + "");
        listdata.add(mMinewBeaconSetting.getBroadcastInterval() + "");
        listdata.add(mMinewBeaconSetting.getDeviceId() + "");
        listdata.add(mMinewBeaconSetting.getName());
        listdata.add(mMinewBeaconSetting.getMode() + "");
        listdata.add(mMinewBeaconSetting.getPassword());

        listdata.add("basbea.com");//mMinewBeaconSetting.getManufacture());
        listdata.add(mMinewBeaconSetting.getModel() + "");
        listdata.add(mMinewBeaconSetting.getSN());
        listdata.add(mMinewBeaconSetting.getHardware());
        listdata.add(mMinewBeaconSetting.getFirmware());
        listdata.add(mMinewBeaconSetting.getSoftware());
        listdata.add(mMinewBeaconSetting.getSystemId());
        listdata.add(mMinewBeaconSetting.getCertData());
        return listdata;
    }

    private void initListener() {
        mAdapter.setOnItemClickLitener(new DetilListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, final int position) {
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        mView = View.inflate(DetilActivity.this, R.layout.dialog_edittext, null);
                        mEt_data = (EditText) mView.findViewById(R.id.et_data);
                        String data = mAdapter.getData(position);
                        mEt_data.setText(data);
                        AlertDialog.Builder builder = new AlertDialog.Builder(DetilActivity.this);
                        builder.setTitle("Beacon Set");
                        builder.setView(mView);
                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        String data = mEt_data.getText().toString().trim();
                                        try {
                                            switch (position) {
                                                case 0:
                                                    if (!mMinewBeaconSetting.getUuid().equals(data)) {
                                                        mMinewBeaconSetting.setUuid(data);
                                                    }
                                                    break;
                                                case 1:
                                                    if (mMinewBeaconSetting.getMajor() != Integer.parseInt(data)) {
                                                        mMinewBeaconSetting.setMajor(Integer.parseInt(data));
                                                    }
                                                    break;
                                                case 2:
                                                    if (mMinewBeaconSetting.getMinor() != Integer.parseInt(data)) {
                                                        mMinewBeaconSetting.setMinor(Integer.parseInt(data));
                                                    }
                                                    break;
                                                case 3:
                                                    if (mMinewBeaconSetting.getCalibratedTxPower() != Integer.parseInt(data)) {
                                                        mMinewBeaconSetting.setCalibratedTxPower(Integer.parseInt(data));
                                                    }
                                                    break;
                                                case 4:
                                                    if (mMinewBeaconSetting.getTxPower() != Integer.parseInt(data)) {
                                                        mMinewBeaconSetting.setTxPower(Integer.parseInt(data));
                                                    }
                                                    break;
                                                case 5:
                                                    if (mMinewBeaconSetting.getBroadcastInterval() != Integer.parseInt(data)) {
                                                        mMinewBeaconSetting.setBroadcastInterval(Integer.parseInt(data));
                                                    }
                                                    break;
                                                case 6:
                                                    if (!mMinewBeaconSetting.getDeviceId().equals(data)) {
                                                        mMinewBeaconSetting.setDeviceId(data);
                                                    }
                                                    break;
                                                case 7:
                                                    if (!mMinewBeaconSetting.getName().equals(data)) {
                                                        mMinewBeaconSetting.setName(data);
                                                    }
                                                    break;
                                                case 8:
                                                    if (mMinewBeaconSetting.getMode() != Integer.parseInt(data)) {
                                                        mMinewBeaconSetting.setMode(Integer.parseInt(data));
                                                    }
                                                    break;
                                                case 9:
                                                    if (data.length() > 0) {
                                                        mMinewBeaconSetting.setPassword(data);
                                                    }
                                                    break;

                                            }
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }
                                        List<String> list = getList();
                                        mAdapter.setData(list);
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;

                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        mBeacon_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView = View.inflate(DetilActivity.this, R.layout.dialog_edittext, null);
                mEt_data = (EditText) mView.findViewById(R.id.et_data);
                AlertDialog.Builder builder = new AlertDialog.Builder(DetilActivity.this);
                builder.setTitle("Restart Beacon")
                        .setMessage("Please input restart password");
                builder.setView(mView);
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                String password = mEt_data.getText().toString().trim();
                                if (password.length() <= 0) {
                                    Toast.makeText(getApplicationContext(), "password can not null", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                mMinewBeaconConnection.writeSetting(mEt_data.getText().toString().trim());

                                mpDialog.setMessage(getString(R.string.setting));
                                mpDialog.show();

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        mMinewBeaconConnection.setMinewBeaconConnectionListener(new MinewBeaconConnectionListener() {
            @Override
            public void onChangeState(MinewBeaconConnection connection, ConnectionState state) {
                switch (state) {
                    case BeaconStatus_Disconnect:
                    case BeaconStatus_ConnectFailed:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        });
                        break;
                }
            }

            @Override
            public void onWriteSettings(MinewBeaconConnection connection, final boolean success) {
                mpDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (success) {
                            Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Fail!", Toast.LENGTH_SHORT).show();
                        }
                        finish();

                    }
                });
            }
        });
    }

    protected void dialogshow() {
        mpDialog = new ProgressDialog(DetilActivity.this);
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

    @Override
    protected void onDestroy() {
        mMinewBeaconConnection.disconnect();
        super.onDestroy();
    }

    private String formatUUID(String str) {
        if (str.length() < 32) {
            return str;
        }
        return str.substring(0, 8) + '-' + str.substring(8, 12) + '-'
                + str.substring(12, 16) + '-' + str.substring(16, 20) + '-'
                + str.substring(20, 32);
    }
}
