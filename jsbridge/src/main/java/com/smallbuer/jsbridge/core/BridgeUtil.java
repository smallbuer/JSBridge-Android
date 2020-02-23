package com.smallbuer.jsbridge.core;


/**
 *
 */
public class BridgeUtil {


    private String TAG = "BridgeUtil";

    public static final int URL_MAX_CHARACTER_NUM = 2097152;
    public static final String JAVA_SCRIPT = "WebViewJavascriptBridge.js";
    public final static String CALLBACK_ID_FORMAT = "JAVA_CB_%s";
    public final static String JS_HANDLE_MESSAGE_FROM_JAVA = "javascript:WebViewJavascriptBridge._handleMessageFromNative('%s');";
    public final static String JAVASCRIPT_STR = "javascript:%s";
    public final static String UNDERLINE_STR = "_";


}
