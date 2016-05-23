package com.uniquedu.myticwear;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mobvoi.android.common.ConnectionResult;
import com.mobvoi.android.common.api.MobvoiApiClient;
import com.mobvoi.android.common.api.ResultCallback;
import com.mobvoi.android.wearable.DataApi;
import com.mobvoi.android.wearable.DataEventBuffer;
import com.mobvoi.android.wearable.MessageApi;
import com.mobvoi.android.wearable.PutDataMapRequest;
import com.mobvoi.android.wearable.PutDataRequest;
import com.mobvoi.android.wearable.Wearable;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MobvoiApiClient.ConnectionCallbacks, MobvoiApiClient.OnConnectionFailedListener {
    private MobvoiApiClient mMobvoiApiClient;
    @InjectView(R.id.textview_tag)
    TextView textviewTag;
    @InjectView(R.id.button_send)
    Button buttonSend;
    private int count = 0;
    private static final String COUNT_PATH = "/count";
    private static final String IMAGE_PATH = "/image";
    private static final String IMAGE_KEY = "photo";
    private static final String COUNT_KEY = "count";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mMobvoiApiClient = new MobvoiApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mMobvoiApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
        textviewTag.setText("连接成功");
    }

    @Override
    public void onConnectionSuspended(int i) {
        textviewTag.setText("状态未知");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
        textviewTag.setText("连接失败");
    }

    @OnClick(R.id.button_send)
    public void onClick() {
        if (!mMobvoiApiClient.isConnected()) {
            return;
        }
        //点击按钮发送消息
        Wearable.MessageApi.sendMessage(
                mMobvoiApiClient, "nodeId", "path唯一标示", "传输的内容".getBytes()).setResultCallback(
                new ResultCallback<MessageApi.SendMessageResult>() {
                    @Override
                    public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                        if (!sendMessageResult.getStatus().isSuccess()) {
                            Log.e("tag", "Failed to send message with status code: "
                                    + sendMessageResult.getStatus().getStatusCode());
                        }
                    }
                }
        );

    }
}
