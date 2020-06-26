package com.smallbuer.jsbridge.core;

import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created on 2020/06/10.
 * @author smallbuer
 * BridgeWebview ChromeClient
 */
public class BridgeWebviewChromeClient extends WebChromeClient {

    private String TAG = "BridgeWebviewChromeClient";
    private BridgeTiny bridgeTiny;
    private BridgeWebView bridgeWebView;
    public BridgeWebviewChromeClient(BridgeWebView bridgeWebView,BridgeTiny bridgeTiny) {
        this.bridgeTiny = bridgeTiny;
        this.bridgeWebView = bridgeWebView;
    }

    @Override
    public boolean onJsPrompt(final WebView view, String url, final String message, String defaultValue, JsPromptResult result) {
        BridgeLog.d(TAG,"message->"+message);
        bridgeTiny.onJsPrompt(bridgeWebView,message);
        //don't delete this line
        result.confirm("do");
        return true;
    }
}
