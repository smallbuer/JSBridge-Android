package com.smallbuer.jsbridge.demo

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smallbuer.jsbridge.core.BridgeHandler
import com.smallbuer.jsbridge.core.BridgeTiny
import com.smallbuer.jsbridge.core.BridgeWebViewClient
import com.smallbuer.jsbridge.core.CallBackFunction
import com.smallbuer.jsbridge.demo.handlers.HandlerName
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() , RadioGroup.OnCheckedChangeListener {

    private val TAG = "MainActivity"
    private val url  = "file:///android_asset/jsbridge/" + "demo.html"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe ({ granted ->
                if (granted) {

                    group1.setOnCheckedChangeListener(this)

                    initX5WebView()

                    initBridgeWebView()

                } else {

                }
            },{ finish() })

    }

    /**
     * Usage example for X5 webview
     */
    private fun initX5WebView() {

        Log.i(TAG,"initX5WebView...")

        uiVisableAndNone(true)

        x5Webview.loadUrl(url)

        btnNativeToJsX5.setOnClickListener{

            x5Webview.callHandler("functionInJs", "我是原生传递的参数") { data ->
                Log.i(TAG, "reponse data from js $data"+",Thread is "+Thread.currentThread().name)
                Toast.makeText(this@MainActivity,data,Toast.LENGTH_SHORT).show()
            }
        }
        //local register bridge
        x5Webview.addHandlerLocal(HandlerName.HANDLER_NAME_TOAST,object: BridgeHandler(){
            override fun handler(context: Context?, data: String?, function: CallBackFunction?) {
                Log.i(TAG, "YY reponse data from js $data"+",Thread is "+Thread.currentThread().name)
                Toast.makeText(this@MainActivity,data,Toast.LENGTH_SHORT).show()
            }
        })


    }

    /**
     * Usage example for bridgeWebview
     */
    private fun initBridgeWebView() {

        Log.i(TAG,"initBridgeWebView...")

        //------------------如果不需要对URL拦截处理业务逻辑,以下对webViewClient的自定义操作可不写--------
        var bridgeTiny = BridgeTiny(bridgeWebview)
        bridgeWebview.webViewClient = object: BridgeWebViewClient(bridgeWebview,bridgeTiny){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                Log.i(TAG, "shouldOverrideUrlLoading url:$url")
                return super.shouldOverrideUrlLoading(view, url)
            }
            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Log.i(TAG, "shouldInterceptRequest url:${request?.url.toString()}")
                }
                return super.shouldInterceptRequest(view, request)
            }
        }
        //-------------------------------------end--------------------------------------------------
        
        bridgeWebview.loadUrl(url)
        btnNativeToJsBridgeWebView.setOnClickListener{
            bridgeWebview.callHandler("functionInJs", "我是原生传递的参数") { data ->
                Log.i(TAG, "reponse data from js $data"+",Thread is "+Thread.currentThread().name)
                Toast.makeText(this@MainActivity,data,Toast.LENGTH_SHORT).show()
            }
        }

        //点击此按钮后,可以通过参考shouldInterceptRequest回调拦截url,根据自己业务拦截指定的URL进行处理
        btnTestShouldIntercept.setOnClickListener {
            bridgeWebview.loadUrl("https://www.baidu.com")
        }

        //local register bridge
        bridgeWebview.addHandlerLocal(HandlerName.HANDLER_NAME_TOAST,object: BridgeHandler(){
            override fun handler(context: Context?, data: String?, function: CallBackFunction?) {
                Log.i(TAG, "YY reponse data from js $data"+",Thread is "+Thread.currentThread().name)
                Toast.makeText(this@MainActivity,data,Toast.LENGTH_SHORT).show()
            }
        })




    }

    /**
     * Usage example for UC webview
     */
    private fun initUcWebView() {

        Log.i(TAG,"initUcWebView...")

        uiVisableAndNone(false)

        ucWebview.loadUrl(url)

        btnNativeToJsUc.setOnClickListener{

            ucWebview.callHandler("functionInJs", "我是原生传递的参数") { data ->
                Log.i(TAG, "reponse data from js $data"+",Thread is "+Thread.currentThread().name)
                Toast.makeText(this@MainActivity,data,Toast.LENGTH_SHORT).show()
            }
        }
        //local register bridge
        ucWebview.addHandlerLocal(HandlerName.HANDLER_NAME_TOAST,object: BridgeHandler(){
            override fun handler(context: Context?, data: String?, function: CallBackFunction?) {
                Log.i(TAG, "YY reponse data from js $data"+",Thread is "+Thread.currentThread().name)
                Toast.makeText(this@MainActivity,data,Toast.LENGTH_SHORT).show()
            }
        })

    }

    /**
     * Set ui visable or none
     */
    private fun uiVisableAndNone(isX5: Boolean){

        if(isX5){
            ucWebview.visibility = View.GONE
            btnNativeToJsUc.visibility = View.GONE

            x5Webview.visibility = View.VISIBLE
            btnNativeToJsX5.visibility = View.VISIBLE
        }else{

            x5Webview.visibility = View.GONE
            btnNativeToJsX5.visibility = View.GONE

            ucWebview.visibility = View.VISIBLE
            btnNativeToJsUc.visibility = View.VISIBLE


        }
    }




    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {

        if(checkedId == R.id.radioX5WebView){
            Log.i(TAG,"onCheckedChanged checkedId radioX5WebView")
            initX5WebView()

        }else if(checkedId == R.id.radioUcWebView){
            Log.i(TAG,"onCheckedChanged checkedId radioUcWebView")
            initUcWebView()
        }
    }


}
