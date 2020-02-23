#### JSBridge-Android
A solution for fast interaction between js and native

本项目来源于 lzyzsd的[JsBridge](https://github.com/lzyzsd/JsBridge)，由于作者长时间未修复部分代码丢失问题，所以目前存在的调用丢失问题以及效率问题无法在原项目得到回应，所以重构了目前js文件以及java类；

主要修改如下：

不再使用URL SHEME拦截方式，直接采用webview的addJavaScriptInterface,此方法根据Android源码跟踪，是目前采用webview方案js与原生交互效率最高的一种系统实现；


本项目保持和lzyzsd老用户的兼容性，内部也重构了BridgeWebView的实现，直接作为View使用即可；
同时也扩展了外部webview的扩展方案,例如X5webview的示例，如果使用UC也同理；




