package com.cloud.simulation;

import java.util.ArrayList;

class WorkerLoads {
    private ArrayList<Integer> workerLoads = new ArrayList<>();

    WorkerLoads(int num_servers) {
        // Initialize loads of all workers to 0.
        for (int i = 0; i < num_servers; i++)
            workerLoads.add(0);
    }

    int getLoad(int index){
        return workerLoads.get(index);
    }

    // Find worker with minimum load.
    synchronized int getMinLoadServer() {
        int minLoad = workerLoads.get(0), min_ind = 0;
        for (int i = 1; i < workerLoads.size(); i++) {
            int thisLoad = workerLoads.get(i);
            if (thisLoad < minLoad) {
                minLoad = thisLoad;
                min_ind = i;
            }
        }
        return min_ind;
    }

    synchronized void incrementLoad(int index){
        workerLoads.set(index, workerLoads.get(index) + 1);
    }

    synchronized void decrementLoad(int index){
        workerLoads.set(index, workerLoads.get(index) - 1);
    }

}
