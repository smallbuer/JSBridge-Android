package com.smallbuer.jsbridge.demo

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    //private val url  = "file:///android_asset/" + "demo.html"

    private val url  = "file:///android_asset/jsbridge/" + "demo.html"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe ({ granted ->
                if (granted) {

                    initX5WebView()

                    initUcWebView()

                    initBridgeWebView()

                } else {

                }
            },{ finish() })

    }



    /**
     * Usage example for UC webview
     */
    private fun initUcWebView() {



    }


    /**
     * Usage example for X5 webview
     */
    private fun initX5WebView() {

        x5Webview.loadUrl(url)

        btnNativeToJsX5.setOnClickListener{

            x5Webview.callHandler("functionInJs", "我是原生传递的参数") { data ->
                Log.i(TAG, "reponse data from js $data"+",Thread is "+Thread.currentThread().name)
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


                Log.i(TAG, "reponse data from js $data"+",Thread is "+Thread.currentThread().name)
            }
        }

    }


}
