package com.smallbuer.jsbridge.core;


import android.content.Context;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created on 2019/12/10.
 * Author: smallbuer
 * bridge utils
 */
public class BridgeUtil {


    private String TAG = "BridgeUtil";

    public static final int URL_MAX_CHARACTER_NUM = 2097152;
    public static final String JAVA_SCRIPT_MIN = "WebViewJavascriptBridgemin.js";
    public final static String CALLBACK_ID_FORMAT = "JAVA_CB_%s";
    public final static String JS_HANDLE_MESSAGE_FROM_JAVA = "javascript:WebViewJavascriptBridge._handleMessageFromNative('%s');";
    public final static String JAVASCRIPT_STR = "javascript:%s";
    public final static String UNDERLINE_STR = "_";
    //Version greater than or equal to 17
    public final static String WebviewJavascriptBridge =
            "(function(){if(window.WebViewJavascriptBridge){return}var receiveMessageQueue=[];var messageHandlers={};var responseCallbacks={};var uniqueId=1;function init(messageHandler){if(WebViewJavascriptBridge._messageHandler){throw new Error('WebViewJavascriptBridge.init called twice');}WebViewJavascriptBridge._messageHandler=messageHandler;var receivedMessages=receiveMessageQueue;receiveMessageQueue=null;for(var i=0;i<receivedMessages.length;i++){_dispatchMessageFromNative(receivedMessages[i])}}function send(data,responseCallback){_doSend('jsbridge','send',data,responseCallback)}function registerHandler(handlerName,handler){messageHandlers[handlerName]=handler}function callHandler(handlerName,data,responseCallback){_doSend('jsbridge',handlerName,data,responseCallback)}function callHandlerWithModule(moduleName,handlerName,data,responseCallback){_doSend(moduleName,handlerName,data,responseCallback)}function _doSend(moduleName,handlerName,message,responseCallback){var callbackId;if(typeof responseCallback==='string'){callbackId=responseCallback}else if(responseCallback){callbackId='cb_'+(uniqueId++)+'_'+new Date().getTime();responseCallbacks[callbackId]=responseCallback}else{callbackId=''}try{var evalStr1='window.'+moduleName+'.';if(moduleName=='jsbridge'&&handlerName!='response'){evalStr1+='handler'}else{evalStr1+=handlerName}var fn=eval(evalStr1)}catch(e){console.log(e)}if(typeof fn==='function'){var evalStr='window.'+moduleName;var fnwindow=eval(evalStr);var responseData;var lastMessage;\n" +
                    "             if(typeof message === 'string'){\n" +
                    "                 lastMessage = message;\n" +
                    "             }else{\n" +
                    "                 lastMessage = JSON.stringify(message);\n" +
                    "             };if(moduleName=='jsbridge'&&handlerName!='response'){responseData=fn.call(fnwindow,handlerName,lastMessage,callbackId)}else{responseData=fn.call(fnwindow,lastMessage,callbackId)}if(responseData){responseCallback=responseCallbacks[callbackId];if(!responseCallback){return}responseCallback(responseData);delete responseCallbacks[callbackId]}}}function _dispatchMessageFromNative(messageJSON){setTimeout(function(){var message=JSON.parse(messageJSON);var responseCallback;if(message.responseId){responseCallback=responseCallbacks[message.responseId];if(!responseCallback){return}responseCallback(message.responseData);delete responseCallbacks[message.responseId]}else{if(message.callbackId){var callbackResponseId=message.callbackId;responseCallback=function(responseData){_doSend('jsbridge','response',responseData,callbackResponseId)}}var handler=WebViewJavascriptBridge._messageHandler;if(message.handlerName){handler=messageHandlers[message.handlerName]}try{handler(message.data,responseCallback)}catch(exception){if(typeof console!='undefined'){console.log(\"WebViewJavascriptBridge: WARNING: javascript handler threw.\",message,exception)}}}})}function _handleMessageFromNative(messageJSON){if(receiveMessageQueue){receiveMessageQueue.push(messageJSON)}_dispatchMessageFromNative(messageJSON)}var WebViewJavascriptBridge=window.WebViewJavascriptBridge={init:init,send:send,registerHandler:registerHandler,callHandler:callHandler,callHandlerWithModule:callHandlerWithModule,_handleMessageFromNative:_handleMessageFromNative};console.log(\"start jsbridge ...\");var doc=document;var readyEvent=doc.createEvent('Events');readyEvent.initEvent('WebViewJavascriptBridgeReady');readyEvent.bridge=WebViewJavascriptBridge;doc.dispatchEvent(readyEvent);console.log(\"end jsbridge ...\")})();";

    //Version lower than 17 used
    public final static String WebviewJavascriptBridgeMin =
          "(function(){if(window.WebViewJavascriptBridge){return}var receiveMessageQueue=[];var messageHandlers={};var responseCallbacks={};var uniqueId=1;function init(messageHandler){if(WebViewJavascriptBridge._messageHandler){throw new Error('WebViewJavascriptBridge.init called twice');}WebViewJavascriptBridge._messageHandler=messageHandler;var receivedMessages=receiveMessageQueue;receiveMessageQueue=null;for(var i=0;i<receivedMessages.length;i++){_dispatchMessageFromNative(receivedMessages[i])}}function send(data,responseCallback){_doSend('jsbridge','send',data,responseCallback)}function registerHandler(handlerName,handler){messageHandlers[handlerName]=handler}function callHandler(handlerName,data,responseCallback){_doSend('jsbridge',handlerName,data,responseCallback)}function callHandlerWithModule(moduleName,handlerName,data,responseCallback){_doSend(moduleName,handlerName,data,responseCallback)}function _doSend(moduleName,handlerName,message,responseCallback){var callbackId;if(typeof responseCallback==='string'){callbackId=responseCallback}else if(responseCallback){callbackId='cb_'+(uniqueId++)+'_'+new Date().getTime();responseCallbacks[callbackId]=responseCallback}else{callbackId=''};var lastMessage;\n" +
                  "         if(typeof message === 'string'){\n" +
                  "             lastMessage = message;\n" +
                  "         }else{\n" +
                  "             lastMessage = JSON.stringify(message);\n" +
                  "         }if(moduleName=='jsbridge'&&handlerName!='response'){prompt('{\\\"handlerName\\\":'+handlerName+',\\\"data\\\":'+lastMessage+',\\\"callbackId\\\":'+callbackId+'}')}else{prompt('{\\\"data\\\":'+lastMessage+',\\\"callbackId\\\":'+callbackId+'}')}}function _dispatchMessageFromNative(messageJSON){setTimeout(function(){var message=JSON.parse(messageJSON);var responseCallback;if(message.responseId){responseCallback=responseCallbacks[message.responseId];if(!responseCallback){return}responseCallback(message.responseData);delete responseCallbacks[message.responseId]}else{if(message.callbackId){var callbackResponseId=message.callbackId;responseCallback=function(responseData){_doSend('jsbridge','response',responseData,callbackResponseId)}}var msgData=message.data;var handler=WebViewJavascriptBridge._messageHandler;if(message.handlerName){handler=messageHandlers[message.handlerName]}try{handler(msgData,responseCallback)}catch(exception){if(typeof console!='undefined'){console.log(\"WebViewJavascriptBridge: WARNING: javascript handler threw.\",message,exception)}}}})}function _handleMessageFromNative(messageJSON){if(receiveMessageQueue){receiveMessageQueue.push(messageJSON)}_dispatchMessageFromNative(messageJSON)}var WebViewJavascriptBridge=window.WebViewJavascriptBridge={init:init,send:send,registerHandler:registerHandler,callHandler:callHandler,callHandlerWithModule:callHandlerWithModule,_handleMessageFromNative:_handleMessageFromNative};console.log(\"start jsbridge ...\");var doc=document;var readyEvent=doc.createEvent('Events');readyEvent.initEvent('WebViewJavascriptBridgeReady');readyEvent.bridge=WebViewJavascriptBridge;doc.dispatchEvent(readyEvent);console.log(\"end jsbridge ...\")})();";

}
