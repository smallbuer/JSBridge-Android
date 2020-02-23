package com.smallbuer.jsbridge.core;

import android.content.Context;

public abstract class BridgeHandler {

    public abstract void handler(Context context, String data, CallBackFunction function);

}