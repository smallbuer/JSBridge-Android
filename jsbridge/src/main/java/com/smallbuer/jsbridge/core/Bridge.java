package com.smallbuer.jsbridge.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2019/12/10.
 * @author smallbuer
 * bridge manager
 */
public enum Bridge {

    INSTANCE;

    private String TAG = "Bridge";
    private Boolean DEBUG = false;
    private Map<String, BridgeHandler> mMessageHandlers = new HashMap<>();

    public Boolean getDEBUG() {
        return DEBUG;
    }

    public void openLog(){
        this.DEBUG = true;
    }


    /**
     * global all handlers
     * @return
     */
    public Map<String, BridgeHandler> getMessageHandlers(){
        return mMessageHandlers;
    }

    /**
     * register handler,so that javascript can call it
     *
     * @param handlerName handlerName
     * @param handler     BridgeHandler
     */
    public void registerHandler(String handlerName, BridgeHandler handler) {
        if (handler != null) {
            // add to Map<String, BridgeHandler>
            mMessageHandlers.put(handlerName, handler);
        }
    }


    /**
     * register handler,so that javascript can call it
     *
     * @param handlers handlerName
     */
    public void registerHandler(Map<String, BridgeHandler> handlers) {
        if (handlers != null) {
            mMessageHandlers.putAll(handlers);
        }
    }


    /**
     * unregister handler
     *
     * @param handlerName
     */
    public void unregisterHandler(String handlerName) {
        if (handlerName != null) {
            mMessageHandlers.remove(handlerName);
        }
    }

}
