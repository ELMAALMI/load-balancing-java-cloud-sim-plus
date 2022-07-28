package com.cloud.simulation.AppServer;

import com.cloud.simulation.loadBalancerAlgorithme.LoadBalancer;
import com.cloud.simulation.loadBalancerAlgorithme.SimResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppController implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            Request rr = getParams(httpExchange.getRequestURI().getQuery());

            // start simulation
            LoadBalancer balancer = new LoadBalancer();
            List<SimResult> simResults = balancer.start(rr);

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(simResults);

            httpExchange.getResponseHeaders().set("Content-type","application/json");
            httpExchange.sendResponseHeaders(200,json.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(json.getBytes());
            os.close();
        }catch (Exception e){
            e.printStackTrace();
            throw new IOException(e);
        }
    }
    private Request getParams(String url){
        Request request = new Request();
        if(url.length() > 0){
            String[] paramsEncoder = url.split("&");
            Map<String,String> params = extractParams(paramsEncoder);
            request.algo = params.get("algo") !=null ?params.get("algo"):"RR";
            request.cloudlet = params.get("cloudlet") != null ? Integer.parseInt(params.get("cloudlet")):10;
            request.ds = params.get("ds") != null ? Integer.parseInt(params.get("ds")):1;
            request.users = params.get("users") != null? Integer.parseInt(params.get("users")):100;
            request.vm = params.get("cloudlet") != null ? Integer.parseInt(params.get("cloudlet")):10;
        }
        return request;
    }
    private Map<String,String> extractParams(String[] params){
        Map<String,String> map = new HashMap<>();
        for (String value:params){
           if(value.contains("=")){
               String[] p = value.split("=");
               map.put(p[0],p[1]);
           }
        }
        return  map;
    }

}


