package com.velocitypackage.http;

import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class HttpService
{
    private final HttpServer server;
    
    private final List<HttpContext> httpContexts = new ArrayList<>();
    
    /**
     * Constructor for Webservice
     *
     * @param port with them, you can connect to the HttpService
     * @throws IOException if the port is already in usage
     */
    public HttpService(int port) throws IOException
    {
        try
        {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException ignored)
        {
            throw new IOException("Port is already in use");
        }
    }
    
    /**
     * Adds a new context
     *
     * @param httpContext context self
     */
    public void add(@NotNull HttpContext httpContext)
    {
        httpContexts.add(httpContext);
    }
    
    /**
     * remove specific context
     *
     * @param httpContext context self
     */
    public void remove(@NotNull HttpContext httpContext)
    {
        httpContexts.remove(httpContext);
    }
    
    /**
     * remove context with index
     *
     * @param index position of context
     */
    public void remove(int index)
    {
        httpContexts.remove(index);
    }
    
    /**
     * start the http service in another thread
     */
    public void start()
    {
        new Thread(this::run).start();
    }
    
    /**
     * runnable
     */
    private void run()
    {
        server.createContext("/", exchange ->
        {
            String path = exchange.getRequestURI().getPath();
            boolean isNull = true;
            for (HttpContext httpContext : httpContexts)
            {
                if (httpContext.acceptPath(path))
                {
                    isNull = false;
                    String content = httpContext.content();
                    exchange.sendResponseHeaders(200, content.getBytes().length);
                    OutputStream output = exchange.getResponseBody();
                    output.write(content.getBytes());
                    output.flush();
                    output.close();
                }
            }
            if (! isNull)
            {
                exchange.sendResponseHeaders(405, - 1);
            }
        });
        server.setExecutor(null);
        server.start();
    }
}
