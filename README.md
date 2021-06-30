#### JSBridge-Android
[![Download](https://img.shields.io/badge/Download-1.0.6-brightgreen.svg)](https://search.maven.org/artifact/com.smallbuer/jsbridge/)   [![Release Version](https://img.shields.io/badge/release-1.0.6-red.svg)](https://github.com/smallbuer/JSBridge-Android/releases)   [![Maven Central](https://img.shields.io/badge/Maven%20Central-1.0.6-brightgreen.svg)](https://search.maven.org/artifact/com.smallbuer/jsbridge/1.0.6/aar)  [![Platform](https://img.shields.io/badge/Platform-Android-brightgreen.svg)](https://github.com/smallbuer/JSBridge-Android)

### 简介

本项目来源于 lzyzsd的[JsBridge](https://github.com/lzyzsd/JsBridge)，由于作者长时间未修复部分代码丢失问题，所以目前存在的调用丢失问题以及效率问题无法在原项目得到回应，所以重构了目前js文件以及java类；

主要修改如下：

不再使用URL SHEME拦截方式，直接采用webview的addJavaScriptInterface,此方法根据Android源码跟踪，是目前采用webview方案js与原生交互效率最高的一种系统实现；
对于API小于17的使用者，1.0.3版本将支持版本降低到14，满足一些朋友的需要;
在1.0.6将BridgeWebViewClient设置为Public,方便业务层进行拦截URL处理；

为了安全主要实现方式利用onJsPrompt方法让H5可以和原生进行交互，并且移除了低版本的三个危险漏洞；
```
//4.2之前addJavascriptInterface有安全泄漏风险，进行移除
webView.removeJavascriptInterface("searchBoxJavaBridge_");
webView.removeJavascriptInterface("accessibility");
webView.removeJavascriptInterface("accessibilityTraversal");
```
本项目保持和lzyzsd老用户的兼容性，内部也重构了BridgeWebView的实现，直接作为View使用即可；
同时也扩展了外部webview的扩展方案,例如X5webview,UC内核(官方申请合作使用,此demo供学习使用)的示例；

欢迎提供好的实现意见与PR；

如果只想解决lzyzsd的[JsBridge]项目调用桥调用丢失问题，可以参考files目录下的README,将JS文件替换其项目即可；

jscript文件下的js文件为新版本SDK内部字符串压缩前的原文件，文件带Min的为API<17的低版本使用,不带的为>=17使用和之前保持一致；
JS源文件在java代码中已经做了压缩处理，此文件只为了供大家查看使用，不需要添加到项目中；

### 导入SDK

1.在项目的根目录build.gradle中的repositories 添加:
```

repositories {
        jcenter()
}
```
2.然后在模块的build.gradle(Module) 的 dependencies 添加:
```

dependencies {
      implementation 'com.smallbuer:jsbridge:1.0.6'
}
```

注：内部需要借助于GSON库，使用外部依赖；



### 使用步骤

1.新增原生功能module
> 继承 BridgeHandler并实现handler方法，例如：

```

public class ToastBridgeHandler extends BridgeHandler {

    @Override
    public void handler(Context context,String data, CallBackFunction function) {

        Toast.makeText(context,"data:"+data,Toast.LENGTH_SHORT).show();

        function.onCallBack("{\"status\":\"0\",\"msg\":\"吐司成功\"}");

    }
}
```

2.原生功能注册
>对所有的原生功能进行全局注册，分为两种方式，推荐方式二；这里注册的桥名也是H5调用时传递的方法名称；

方式一：
```
    //style 1
    Bridge.INSTANCE.registerHandler(HandlerName.HANDLER_NAME_TOAST, ToastBridgeHandler())        
    Bridge.INSTANCE.registerHandler(HandlerName.HANDLER_NAME_PHOTO, PhotoBridgeHandler())
    Bridge.INSTANCE.registerHandler(HandlerName.HANDLER_NAME_REQUEST, RequestBridgeHandler())
```
方式二：

```
    //style 2
    var handlerMap = HashMap<String,BridgeHandler>();
    handlerMap[HandlerName.HANDLER_NAME_TOAST] = ToastBridgeHandler()
    handlerMap[HandlerName.HANDLER_NAME_PHOTO] = PhotoBridgeHandler()
    handlerMap[HandlerName.HANDLER_NAME_REQUEST] = RequestBridgeHandler()
    Bridge.INSTANCE.registerHandler(handlerMap)
```

其中HandlerName.HANDLER_NAME_TOAST等常量即为原生功能的名称标示，用于H5调用需要指定，参考4

>对原生功能进行局部注册，方便在Activity和Fragment中进行桥的注册；
```java
//local register bridge
bridgeWebview.addHandlerLocal(HandlerName.HANDLER_NAME_TOAST,object: BridgeHandler(){
    override fun handler(context: Context?, data: String?, function: CallBackFunction?) {
        Toast.makeText(this@MainActivity,data,Toast.LENGTH_SHORT).show()
    }
})
```

拿到页面中的webview对象后，调用addHandlerLocal方法进行注册局部桥，可以用来新增桥和替换全局的桥，便于业务定制；

支持版本1.0.4+；



3.使用方法

>直接使用封装好的BridgeWebview,基于系统自带webview;

（1）xml中导入
```
<com.smallbuer.jsbridge.core.BridgeWebView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/bridgeWebview"
    android:layout_weight="1"
    />
```
（2）代码中使用
```
bridgeWebview.loadUrl(url)

//原生调用H5，functionInJs是H5为原生注册的桥名，参考demo.html
bridgeWebview.callHandler("functionInJs", "我是原生传递的参数") { data ->
                Log.i(TAG, "reponse data from js $data")
            }
```


>外部扩展其他webview,比如X5内核，uc内核等，源码请参考demo中X5WebView实现

（1）新建X5WebView继承X5包内的webview并实现IWebView接口<br>
（2）在X5WebView中新建BridgeTiny并传入X5WebView对应，BridgeTiny作为一个webview的管理类；<br> 
（3）在webview执行onPageFinished时，加载js脚本内容用于执行对象的注册；<br>
（4）在webview执行destroy时清空使用内存；<br>

使用API如下：<br>

（1）xml中导入
```
    <com.smallbuer.jsbridge.demo.view.X5WebView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/x5Webview"
        android:layout_weight="1"
        />
```

（2）代码中使用
```
x5Webview.loadUrl(url)

//原生调用H5，functionInJs是H5为原生注册的桥名，参考demo.html
x5Webview.callHandler("functionInJs", "我是原生传递的参数") { data ->
    Log.i(TAG, "reponse data from js $data")
}
```

4.H5调用

>H5中可以自己封装调用名称，适合Vue.js或者存js调用;
```
window.WebViewJavascriptBridge.callHandler(
                'toast'                     //桥注册的名称ID
                , {'msg': '中文测试'}        //传递给原生的参数
                , function(responseData) {  //异步回调接口
                    console.log('native return->'+responseData);
                }
            );
```

## 混淆

```
-keep class com.smallbuer.jsbridge.core.** { *; }
```

## License

JSBridge-Android is [Apache-2.0 licensed](./LICENSE).