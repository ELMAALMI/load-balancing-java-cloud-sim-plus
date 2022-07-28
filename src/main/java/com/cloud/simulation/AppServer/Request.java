package com.cloud.simulation.AppServer;

public class Request {
    int users;
    int ds;
    String algo;
    int vm;
    int cloudlet;

    public Request() {
    }
    public Request(int users, int ds, String algo, int vm, int cloudlet) {
        this.users = users;
        this.ds = ds;
        this.algo = algo;
        this.vm = vm;
        this.cloudlet = cloudlet;
    }

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public int getDs() {
        return ds;
    }

    public void setDs(int ds) {
        this.ds = ds;
    }

    public String getAlgo() {
        return algo;
    }

    public void setAlgo(String algo) {
        this.algo = algo;
    }

    public int getVm() {
        return vm;
    }

    public void setVm(int vm) {
        this.vm = vm;
    }

    public int getCloudlet() {
        return cloudlet;
    }

    public void setCloudlet(int cloudlet) {
        this.cloudlet = cloudlet;
    }

    @Override
    public String toString() {
        return "{" +
                "'users': '" + users +", 'ds':" + ds +
                ", 'algo':'" + algo + '\'' +
                ", 'vm':" + vm +
                ", 'cloudlet':" + cloudlet +
                '}';
    }
}

