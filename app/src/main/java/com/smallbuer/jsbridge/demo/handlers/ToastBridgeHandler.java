package com.smallbuer.jsbridge.demo.handlers;

import android.content.Context;
import android.widget.Toast;

import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;

public class ToastBridgeHandler extends BridgeHandler {

    @Override
    public void handler(Context context,String data, CallBackFunction function) {

        Toast.makeText(context,"data:"+data,Toast.LENGTH_SHORT).show();

        function.onCallBack("{\"status\":\"0\",\"msg\":\"吐司成功\"}");

    }
}
