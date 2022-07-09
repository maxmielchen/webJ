package com.velocitypackage.webj.services.ws;

import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

public abstract class WebSocketServiceHandler extends BaseWebSocketHandler
{
    @Override
    public abstract void onOpen(WebSocketConnection connection);
    
    @Override
    public abstract void onClose(WebSocketConnection connection);
    
    @Override
    public abstract void onMessage(WebSocketConnection connection, String msg);
}
