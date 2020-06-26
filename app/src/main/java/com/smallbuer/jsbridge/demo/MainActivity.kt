package com.smallbuer.jsbridge.demo

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

    }

    /**
     * Usage example for bridgeWebview
     */
    private fun initBridgeWebView() {

        Log.i(TAG,"initBridgeWebView...")

        bridgeWebview.loadUrl(url)

        btnNativeToJsBridgeWebView.setOnClickListener{

            bridgeWebview.callHandler("functionInJs", "我是原生传递的参数") { data ->

                Log.i(TAG, "reponse data from js $data"+",Thread is "+Thread.currentThread().name)
                Toast.makeText(this@MainActivity,data,Toast.LENGTH_SHORT).show()
            }
        }

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
