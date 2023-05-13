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
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smallbuer.jsbridge.core.*
import com.smallbuer.jsbridge.demo.handlers.HandlerName
import com.smallbuer.jsbridge.demo.view.X5WebView
import com.tbruyelle.rxpermissions2.RxPermissions


class MainActivity : AppCompatActivity()  {

    private val TAG = "MainActivity"
    private val url  = "file:///android_asset/jsbridge/" + "demo.html"
    private var mX5WebView: X5WebView? = null
    private var mBtnNativeToJsX5: Button? = null
    private var mBridgeWebView: BridgeWebView? = null
    private var mBtnNativeToJsBridgeWebView: Button? = null
    private var mBtnTestShouldIntercept: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mX5WebView = findViewById<X5WebView>(R.id.x5Webview)
        mBtnNativeToJsX5 = findViewById(R.id.btnNativeToJsX5)

        mBridgeWebView = findViewById<BridgeWebView>(R.id.bridgeWebview)
        mBtnNativeToJsBridgeWebView = findViewById(R.id.btnNativeToJsBridgeWebView)

        mBtnTestShouldIntercept = findViewById(R.id.btnTestShouldIntercept)

        initX5WebView()
        initBridgeWebView()

    }

    /**
     * Usage example for X5 webview
     */
    private fun initX5WebView() {

        Log.i(TAG,"initX5WebView...")

        mX5WebView?.loadUrl(url)

        mBtnNativeToJsX5?.setOnClickListener{

            mX5WebView?.callHandler("functionInJs", "我是原生传递的参数") { data ->
                Log.i(TAG, "reponse data from js $data"+",Thread is "+Thread.currentThread().name)
                Toast.makeText(this@MainActivity,data,Toast.LENGTH_SHORT).show()
            }
        }
        //local register bridge
        mX5WebView?.addHandlerLocal(HandlerName.HANDLER_NAME_TOAST,object: BridgeHandler(){
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
        var bridgeTiny = BridgeTiny(mBridgeWebView)
        mBridgeWebView?.webViewClient = object: BridgeWebViewClient(mBridgeWebView,bridgeTiny){
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

        mBridgeWebView?.loadUrl(url)
        mBtnNativeToJsBridgeWebView?.setOnClickListener{
            mBridgeWebView?.callHandler("functionInJs", "我是原生传递的参数") { data ->
                Log.i(TAG, "reponse data from js $data"+",Thread is "+Thread.currentThread().name)
                Toast.makeText(this@MainActivity,data,Toast.LENGTH_SHORT).show()
            }
        }

        //点击此按钮后,可以通过参考shouldInterceptRequest回调拦截url,根据自己业务拦截指定的URL进行处理
        mBtnTestShouldIntercept?.setOnClickListener {
            mBridgeWebView?.loadUrl("https://www.baidu.com")
        }

        //local register bridge
        mBridgeWebView?.addHandlerLocal(HandlerName.HANDLER_NAME_TOAST,object: BridgeHandler(){
            override fun handler(context: Context?, data: String?, function: CallBackFunction?) {
                Log.i(TAG, "YY reponse data from js $data"+",Thread is "+Thread.currentThread().name)
                Toast.makeText(this@MainActivity,data,Toast.LENGTH_SHORT).show()
            }
        })
    }


}
