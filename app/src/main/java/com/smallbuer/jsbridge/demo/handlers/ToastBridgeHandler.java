package com.smallbuer.jsbridge.demo.handlers;

import android.content.Context;
import android.widget.Toast;

import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.BridgeLog;
import com.smallbuer.jsbridge.core.CallBackFunction;

public class ToastBridgeHandler extends BridgeHandler {

    private String TAG = "ToastBridgeHandler";

    @Override
    public void handler(Context context,String data, CallBackFunction function) {

        BridgeLog.d(TAG,"data->"+data+",Thread is "+Thread.currentThread().getName());

        Toast.makeText(context,"data:"+data,Toast.LENGTH_SHORT).show();

        function.onCallBack("{\"status\":\"0\",\"msg\":\"toast success\"}");

    }
}
