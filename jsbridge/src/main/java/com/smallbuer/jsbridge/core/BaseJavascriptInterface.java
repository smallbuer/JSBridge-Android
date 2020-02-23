package com.smallbuer.jsbridge.core;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import java.util.Map;

public abstract class BaseJavascriptInterface {

    private String TAG = "BaseJavascriptInterface";

    private Map<String, OnBridgeCallback> mCallbacks;

    public BaseJavascriptInterface(Map<String, OnBridgeCallback> callbacks) {
        mCallbacks = callbacks;
    }

    @JavascriptInterface
    public String send(String data, String callbackId) {
        return send(data);
    }

    @JavascriptInterface
    public void response(String data, String responseId) {
        Log.d(TAG, "response->"+data + ", responseId: " + responseId + " " + Thread.currentThread().getName());
        if (!TextUtils.isEmpty(responseId)) {
            OnBridgeCallback function = mCallbacks.remove(responseId);
            if (function != null) {
                function.onCallBack(data);
            }
        }
    }

    public abstract String send(String data);



}
