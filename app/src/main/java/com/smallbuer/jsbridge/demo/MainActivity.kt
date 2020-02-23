package com.smallbuer.jsbridge.demo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    val url  = "file:///android_asset/" + "demo.html"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //X5自定义webview
        initX5WebView()
        //自带BridgeWebview
        initBridgeWebView()
    }


    /**
     * Usage example for X5 webview
     */
    private fun initX5WebView() {

        x5Webview.loadUrl(url)

        btnNativeToJsX5.setOnClickListener{

            x5Webview.callHandler("functionInJs", "我是原生传递的参数") { data ->
                Log.i(TAG, "reponse data from js $data")
            }
        }

    }

    /**
     * Usage example for bridgeWebview
     */
    private fun initBridgeWebView() {

        bridgeWebview.loadUrl(url)

        btnNativeToJsBridgeWebView.setOnClickListener{

            bridgeWebview.callHandler("functionInJs", "我是原生传递的参数") { data ->
                Log.i(TAG, "reponse data from js $data")
            }
        }

    }


}
