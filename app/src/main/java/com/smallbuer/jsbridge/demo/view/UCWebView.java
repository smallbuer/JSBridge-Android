package com.smallbuer.jsbridge.demo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.ValueCallback;

import androidx.annotation.Nullable;

import com.smallbuer.jsbridge.core.BridgeTiny;
import com.smallbuer.jsbridge.core.IWebView;
import com.smallbuer.jsbridge.core.OnBridgeCallback;
import com.uc.webview.export.WebSettings;
import com.uc.webview.export.WebView;
import com.uc.webview.export.WebViewClient;

public class UCWebView extends WebView implements IWebView {

	private BridgeTiny bridgeTiny;

	@SuppressLint("SetJavaScriptEnabled")
	public UCWebView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
		this.setWebViewClient(client);
		initWebViewSettings();
		bridgeTiny = new BridgeTiny(this);
	}

	private void initWebViewSettings() {
		WebSettings webSetting = this.getSettings();
		webSetting.setJavaScriptEnabled(true);
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
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

	@Override
	public void destroy() {
		super.destroy();
		bridgeTiny.freeMemory();
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
