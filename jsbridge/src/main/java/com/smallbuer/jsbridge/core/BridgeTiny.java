package com.smallbuer.jsbridge.core;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BridgeTiny {

    private String TAG = "BridgeTiny";
    private long mUniqueId = 0;
    private IWebView mWebView;
    private Map<String, OnBridgeCallback> mCallbacks = new ArrayMap<>();
    private Map<String, BridgeHandler> mMessageHandlers = new HashMap<>();
    private List<Object> mMessages = new ArrayList<>();

    public BridgeTiny(IWebView webView){

        this.mWebView = webView;
        webView.addJavascriptInterface(new BridgeJavascritInterface(mCallbacks, this,webView),"jsbridge");
        mMessageHandlers.putAll(Bridge.INSTANCE.getMessageHandlers());

    }

    public Map<String, BridgeHandler> getMessageHandlers(){
        return mMessageHandlers;
    }


    public void webViewLoadJs(IWebView view) {

        view.loadUrl(String.format(BridgeUtil.JAVASCRIPT_STR,BridgeUtil.WebviewJavascriptBridge));

        if (mMessages != null) {
            for (Object message : mMessages) {
                dispatchMessage(message);
            }
            mMessages = null;
        }
    }

    /**
     * 分发message 必须在主线程才分发成功
     *
     * @param message Message
     */
    public void dispatchMessage(Object message) {

        String messageJson = new Gson().toJson(message);
        //escape special characters for json string  为json字符串转义特殊字符
        messageJson = messageJson.replaceAll("(\\\\)([^utrn])", "\\\\\\\\$1$2");
        messageJson = messageJson.replaceAll("(?<=[^\\\\])(\")", "\\\\\"");
        messageJson = messageJson.replaceAll("(?<=[^\\\\])(\')", "\\\\\'");
        messageJson = messageJson.replaceAll("%7B", URLEncoder.encode("%7B"));
        messageJson = messageJson.replaceAll("%7D", URLEncoder.encode("%7D"));
        messageJson = messageJson.replaceAll("%22", URLEncoder.encode("%22"));
        String javascriptCommand = String.format(BridgeUtil.JS_HANDLE_MESSAGE_FROM_JAVA, messageJson);

        BridgeLog.d(TAG,"javascriptCommand->"+javascriptCommand);

        // 必须要找主线程才会将数据传递出去 --- 划重点
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&javascriptCommand.length()>=BridgeUtil.URL_MAX_CHARACTER_NUM) {
                mWebView.evaluateJavascript(javascriptCommand,null);
            }else {
                mWebView.loadUrl(javascriptCommand);
            }
        }
    }


    public void sendResponse(Object data, String callbackId) {
        if (!(data instanceof String)){
            return;
        }
        if (!TextUtils.isEmpty(callbackId)) {
            final JSResponse response = new JSResponse();
            response.responseId = callbackId;
            response.responseData = data instanceof String ? (String) data : new Gson().toJson(data);
            if (Thread.currentThread() == Looper.getMainLooper().getThread()){
                dispatchMessage(response);
            }else {
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        dispatchMessage(response);
                    }
                });
            }

        }
    }


    /**
     * 保存message到消息队列
     *
     * @param handlerName      handlerName
     * @param data             data
     * @param responseCallback OnBridgeCallback
     */
    public void callHandler(String handlerName, Object data, OnBridgeCallback responseCallback) {
        if (!(data instanceof String)){
            return;
        }
        JSRequest request = new JSRequest();
        if (data != null) {
            request.data = data instanceof String ? (String) data : new Gson().toJson(data);
        }
        if (responseCallback != null) {
            String callbackId = String.format(BridgeUtil.CALLBACK_ID_FORMAT, (++mUniqueId) + (BridgeUtil.UNDERLINE_STR + SystemClock.currentThreadTimeMillis()));
            mCallbacks.put(callbackId, responseCallback);
            request.callbackId = callbackId;
        }
        if (!TextUtils.isEmpty(handlerName)) {
            request.handlerName = handlerName;
        }
        queueMessage(request);
    }


    /**
     * list<message> != null 添加到消息集合否则分发消息
     *
     * @param message Message
     */
    private void queueMessage(Object message) {
        if (mMessages != null) {
            mMessages.add(message);
        } else {
            dispatchMessage(message);
       }
    }


    /**
     * free memory
     */
    public void freeMemory(){

        if(mCallbacks!=null){
            mCallbacks.clear();
        }

        if(mMessageHandlers!=null){
            mMessageHandlers.clear();
        }

        if(mMessages!=null){
            mMessages.clear();
        }
    }

}
