package com.smallbuer.jsbridge.core;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2019/12/10.
 * Author: smallbuer
 * BridgeTiny
 */
public class BridgeTiny {

    private String TAG = "BridgeTiny";
    private long mUniqueId = 0;
    private IWebView mWebView;
    private Map<String, OnBridgeCallback> mCallbacks = new HashMap<>();
    private Map<String, BridgeHandler> mMessageHandlers = new HashMap<>();
    private List<Object> mMessages = new ArrayList<>();
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    public Map<String, OnBridgeCallback> getCallBacks(){
        return mCallbacks;
    }

    public BridgeTiny(IWebView webView){

        this.mWebView = webView;

        if (Build.VERSION.SDK_INT >= 17) {
            webView.addJavascriptInterface(new BridgeJavascritInterface(mCallbacks, this, webView), "jsbridge");
        }else {
            //4.2之前 addJavascriptInterface有安全泄漏风险
            webView.removeJavascriptInterface("searchBoxJavaBridge_");
            webView.removeJavascriptInterface("accessibility");
            webView.removeJavascriptInterface("accessibilityTraversal");
        }
        mMessageHandlers.putAll(Bridge.INSTANCE.getMessageHandlers());
    }

    public Map<String, BridgeHandler> getMessageHandlers(){
        return mMessageHandlers;
    }


    public void webViewLoadJs(IWebView view) {
        if(Build.VERSION.SDK_INT >= 17) {
            view.loadUrl(String.format(BridgeUtil.JAVASCRIPT_STR, BridgeUtil.WebviewJavascriptBridge));
        }else{
            view.loadUrl(String.format(BridgeUtil.JAVASCRIPT_STR, BridgeUtil.WebviewJavascriptBridgeMin));
        }
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
            response.responseData = (String) data ;
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
            request.data = (String) data;
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
     *  JsPrompt处理方式
     * @param view webview
     * @param message
     */
   public void onJsPrompt(final IWebView view,final String message){

       if (Build.VERSION.SDK_INT < 17) {
           new Thread(new Runnable(){
               @Override
               public void run() {
                   try {
                       JSONObject jsonObject = new JSONObject(message);
                       final String responseId = jsonObject.getString("callbackId");
                       final String data = jsonObject.getString("data");
                       Boolean hasHanderName = jsonObject.has("handlerName");
                       if(hasHanderName){
                           String HanderName = jsonObject.getString("handlerName");
                           bridgeHandler(view,data,HanderName,responseId);
                       }else {
                           if (!TextUtils.isEmpty(responseId)) {
                               mMainHandler.post(new Runnable() {
                                   @Override
                                   public void run() {
                                       OnBridgeCallback function = getCallBacks().remove(responseId);
                                       if (function != null) {
                                           function.onCallBack(data);
                                       }
                                   }
                               });
                           }
                       }
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }
           }).start();
       }




   }

    private void bridgeHandler(final IWebView view, final String data, final String handlerName, final String callbackId){

        if(TextUtils.isEmpty(handlerName)){
            return;
        }
        //change to main thread
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {

                if(getMessageHandlers().containsKey(handlerName)){
                    BridgeHandler bridgeHandler = getMessageHandlers().get(handlerName);
                    bridgeHandler.handler(view.getContext(),data, new CallBack(callbackId));
                }
            }
        });
    }

    public class CallBack implements CallBackFunction{
        private String callbackId ;
        public CallBack(String callbackId){
            this.callbackId =  callbackId;
        }
        @Override
        public void onCallBack(String data) {
            sendResponse(data,callbackId);
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
