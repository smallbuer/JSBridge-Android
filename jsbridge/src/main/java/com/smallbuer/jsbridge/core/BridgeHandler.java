package com.smallbuer.jsbridge.core;

import android.content.Context;

/**
 * Created on 2019/12/10.
 * Author: smallbuer
 * BridgeHandler
 */
public abstract class BridgeHandler {

    public abstract void handler(Context context, String data, CallBackFunction function);

}