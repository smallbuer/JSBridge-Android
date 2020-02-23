package com.smallbuer.jsbridge.demo

import android.app.Application
import android.util.Log
import com.smallbuer.jsbridge.core.Bridge
import com.smallbuer.jsbridge.core.BridgeHandler
import com.smallbuer.jsbridge.demo.handlers.HandlerName
import com.smallbuer.jsbridge.demo.handlers.PhotoBridgeHandler
import com.smallbuer.jsbridge.demo.handlers.RequestBridgeHandler
import com.smallbuer.jsbridge.demo.handlers.ToastBridgeHandler
import com.tencent.smtt.sdk.QbSdk


/**
 * app start
 */
class App :Application(){

    override fun onCreate() {
        super.onCreate()

        initX5()

        initJsBridgeHandler()

    }


    /**
     * init X5
     */
    private fun initX5(){

        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        val cb: QbSdk.PreInitCallback = object : QbSdk.PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is $arg0")
            }

            override fun onCoreInitFinished() {

            }
        }
        //x5 webview init
        QbSdk.initX5Environment(applicationContext, cb)
    }


    /**
     * Registering native functions globally
     */
    private fun initJsBridgeHandler() {

        //style 1
//        Bridge.INSTANCE.registerHandler(HandlerName.HANDLER_NAME_TOAST, ToastBridgeHandler())
//        Bridge.INSTANCE.registerHandler(HandlerName.HANDLER_NAME_PHOTO, PhotoBridgeHandler())
//        Bridge.INSTANCE.registerHandler(HandlerName.HANDLER_NAME_REQUEST, RequestBridgeHandler())

        //or

        //style 2
        var handlerMap = HashMap<String,BridgeHandler>();
        handlerMap[HandlerName.HANDLER_NAME_TOAST] = ToastBridgeHandler()
        handlerMap[HandlerName.HANDLER_NAME_PHOTO] = PhotoBridgeHandler()
        handlerMap[HandlerName.HANDLER_NAME_REQUEST] = RequestBridgeHandler()
        Bridge.INSTANCE.registerHandler(handlerMap)

    }



}