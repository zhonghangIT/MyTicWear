package com.uniquedu.myticwear;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mobvoi.android.common.ConnectionResult;
import com.mobvoi.android.common.api.MobvoiApiClient;
import com.mobvoi.android.wearable.MessageApi;
import com.mobvoi.android.wearable.MessageEvent;
import com.mobvoi.android.wearable.Wearable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity implements SensorEventListener, MessageApi.MessageListener, MobvoiApiClient.ConnectionCallbacks, MobvoiApiClient.OnConnectionFailedListener {

    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout mContainerView;
    private TextView mTextView;
    private TextView mClockView;
    private MobvoiApiClient mMobvoiApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();
        mSensorManager =
                (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(
                Sensor.TYPE_STEP_COUNTER);//计步器
        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mTextView = (TextView) findViewById(R.id.text);
        mClockView = (TextView) findViewById(R.id.clock);

        mMobvoiApiClient = new MobvoiApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mMobvoiApiClient.connect();
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mTextView.setTextColor(getResources().getColor(android.R.color.white));
            mClockView.setVisibility(View.VISIBLE);
            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
            mTextView.setTextColor(getResources().getColor(android.R.color.black));
            mClockView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Wearable.MessageApi.removeListener(mMobvoiApiClient, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mClockView.setText("连接成功");
        //连接成功添加接收消息的监听器，后边的this是指的MessageListener，对应的方法为onMessageReceived，此处用Activity
        //实现该接口因为onStop时需要移除该监听
        Wearable.MessageApi.addListener(mMobvoiApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mTextView.setText("连接状态" + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mTextView.setText("连接服务失败");
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String path = messageEvent.getPath();
        String nodeId = messageEvent.getSourceNodeId();
        String data = new String(messageEvent.getData());
        //将接收到的消息添加到界面上
        mTextView.setText("nodeID：" + nodeId + " path:" + path + " 内容:" + data);
    }

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //传感器的接口
//        event.sensor.getName()得到传感器的名称
//        Log.d("Test", "Got the heart rate (beats per minute) : " +
//                String.valueOf(event.values[0]));
        mClockView.setText(String.valueOf(event.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//传感器的接口
    }
}
