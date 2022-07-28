package com.cloud.simulation;

import com.cloud.simulation.AppServer.AppServer;
import org.cloudbus.cloudsim.Log;

public class Main {
    public static void main(String[] args) {
        (new AppServer()).start();
        Log.printLine("Load Balancer Server Start At : localhost:9999");
    }
}
