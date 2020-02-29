package com.smallbuer.jsbridge.demo.handlers;

import android.content.Context;

import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.BridgeLog;
import com.smallbuer.jsbridge.core.CallBackFunction;

public class RequestBridgeHandler extends BridgeHandler {

    private String TAG = "RequestBridgeHandler";

    @Override
    public void handler(Context context,String data, CallBackFunction function) {

        BridgeLog.d(TAG,"data->"+data);

        //a network request .....
        //callback
        function.onCallBack("{\"status\":\"0\",\"msg\":\"请求成功\"}");

    }
}
