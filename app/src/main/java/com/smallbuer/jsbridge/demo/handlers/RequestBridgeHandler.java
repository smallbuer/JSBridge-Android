package com.smallbuer.jsbridge.demo.handlers;

import android.content.Context;
import android.widget.Toast;

import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.BridgeLog;
import com.smallbuer.jsbridge.core.CallBackFunction;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestBridgeHandler extends BridgeHandler {

    private String TAG = "RequestBridgeHandler";

    @Override
    public void handler(Context context,String data, CallBackFunction function) {

        BridgeLog.d(TAG,"data->"+data+",Thread is "+Thread.currentThread().getName());

        try {
            JSONObject jsonObject = new JSONObject(data);
            BridgeLog.d(TAG,"jsonObject->"+jsonObject.toString()+",Thread is "+Thread.currentThread().getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //a network request .....
        //callback
        Toast.makeText(context,"request data :"+data,Toast.LENGTH_SHORT).show();
        function.onCallBack("{\"status\":\"0\",\"msg\":\"请求成功\"}");

    }
}
