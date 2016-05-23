package com.uniquedu.myticwear;

import android.content.Intent;
import android.os.IBinder;

import com.mobvoi.android.wearable.DataEventBuffer;
import com.mobvoi.android.wearable.MessageEvent;
import com.mobvoi.android.wearable.Node;
import com.mobvoi.android.wearable.WearableListenerService;

/**
 * Created by ZhongHang on 2016/5/23.
 */
public class MyService extends WearableListenerService {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        super.onDataChanged(dataEventBuffer);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
//        messageEvent.getPath() + messageEvent.getSourceNodeId() + messageEvent.getData();
        String path = messageEvent.getPath();
        String nodeId = messageEvent.getSourceNodeId();
        String data = new String(messageEvent.getData());
        //后台服务接收到数据，将数据显示到界面上
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPeerConnected(Node node) {
        super.onPeerConnected(node);
    }

    @Override
    public void onPeerDisconnected(Node node) {
        super.onPeerDisconnected(node);
    }

    public MyService() {
        super();
    }
}
