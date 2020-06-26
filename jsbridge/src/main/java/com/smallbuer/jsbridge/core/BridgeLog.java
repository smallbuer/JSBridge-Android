package com.smallbuer.jsbridge.core;

import android.util.Log;

/**
 * Created on 2019/12/10.
 * Author: smallbuer
 * BridgeLog
 */
public class BridgeLog {


    public static void d(String tag,String message){

        if(Bridge.INSTANCE.getDEBUG()){
            Log.d(tag,message);
        }

    }

    public static void i(String tag,String message){

        if(Bridge.INSTANCE.getDEBUG()){
            Log.i(tag,message);
        }

    }

    public static void w(String tag,String message){

        if(Bridge.INSTANCE.getDEBUG()){
            Log.w(tag,message);
        }

    }


    public static void e(String tag,String message){

        if(Bridge.INSTANCE.getDEBUG()){
            Log.e(tag,message);
        }

    }

}
