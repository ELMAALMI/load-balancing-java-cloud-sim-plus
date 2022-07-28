package com.cloud.simulation.AppServer;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class AppServer {
    public void start(){
        try {
            int PORT = 9999;
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT),0);
            httpServer.createContext("/load-sim",new AppController());
            httpServer.setExecutor(null);
            httpServer.start();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
