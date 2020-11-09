package com.smallbuer.jsbridge.demo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.BridgeLog;
import com.smallbuer.jsbridge.core.BridgeTiny;
import com.smallbuer.jsbridge.core.IWebView;
import com.smallbuer.jsbridge.core.OnBridgeCallback;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2019/12/10.
 * @author smallbuer
 * X5Webview extend example
 */
public class X5WebView extends WebView implements IWebView {
	private String TAG = "X5WebView";
	private BridgeTiny bridgeTiny;
	private Map<String, BridgeHandler> mLocalMessageHandlers = new HashMap<>();
	@SuppressLint("SetJavaScriptEnabled")
	public X5WebView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
		this.setWebViewClient(client);
		this.setWebChromeClient(chromeClient);
		initWebViewSettings();
		this.getView().setClickable(true);
		bridgeTiny = new BridgeTiny(this);
	}

	private void initWebViewSettings() {
		WebSettings webSetting = this.getSettings();
		webSetting.setJavaScriptEnabled(true);
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(true);
		// webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(true);
		// webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		// webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

		// this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
		// settings 的设计
	}

	private WebViewClient client = new WebViewClient() {
		/**
		 * prevent system browser from launching when web page loads
		 */
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView webView, String s) {
			super.onPageFinished(webView, s);
			bridgeTiny.webViewLoadJs((IWebView) webView);
		}

	};

	private WebChromeClient chromeClient = new WebChromeClient(){
		@Override
		public boolean onJsPrompt(WebView webView, String url,String message, String defaultValue, JsPromptResult jsPromptResult) {
			BridgeLog.d(TAG,"message->"+message);
			bridgeTiny.onJsPrompt(X5WebView.this,message);
			//don't delete this line
			jsPromptResult.confirm("do");
			return true;
		}
	};





	@Override
	public void destroy() {
		super.destroy();
		bridgeTiny.freeMemory();
	}

	@Override
	public void addHandlerLocal(String handlerName, BridgeHandler bridgeHandler) {
		mLocalMessageHandlers.put(handlerName,bridgeHandler);
	}

	@Override
	public Map<String, BridgeHandler> getLocalMessageHandlers() {
		return mLocalMessageHandlers;
	}

	@Override
	public void evaluateJavascript(String var1,@Nullable Object object) {
		if(object == null){
			super.evaluateJavascript(var1, null);
			return;
		}
		super.evaluateJavascript(var1, (ValueCallback<String>) object);
	}

	@Override
	public void callHandler(String handlerName, Object data, OnBridgeCallback responseCallback) {
		bridgeTiny.callHandler(handlerName,data,responseCallback);
	}

}
