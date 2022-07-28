package com.cloud.simulation.loadBalancerAlgorithme;

/*
import com.cloud.simulation.AppServer.Request;
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.vms.Vm;
*/
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LoadBalancerV2 {
    /*
    private Calendar calendar;
    private String algo;
    private int nb_vm;
    private int nb_users;
    private int nb_cloudlet;

    // virtual Machine specification
    private final int VM_PES = 4; // number of CPU cores
    private final int HOSTS = 2;
    private final int HOST_PES = 8;
    ///
    private CloudSim simulation;

    private DatacenterBroker broker0;
    private List<Vm> vmList;
    private List<Cloudlet> cloudletList;
    private Datacenter datacenter0;

    public LoadBalancerV2(String algo, int nb_vm, int nb_users, int nb_cloudlet) {
        this.algo = algo;
        this.nb_vm = nb_vm;
        this.nb_users = nb_users;
        this.nb_cloudlet = nb_cloudlet;
        this.calendar = Calendar.getInstance();
    }
    public void initResource(){
        simulation = new CloudSim();
        datacenter0 = createDatacenter();
    }
    public void start(Request request){

    }
    private Datacenter createDatacenter() {
        final List<Host> hostList = new ArrayList<>(HOSTS);
        for(int i = 0; i < HOSTS; i++) {
            Host host = createHost();
            hostList.add(host);
        }
        return new DatacenterSimple(simulation, hostList);
    }
    private Host createHost() {
        final List<Pe> peList = new ArrayList<>(HOST_PES);
        for (int i = 0; i < HOST_PES; i++) {
            peList.add(new PeSimple(1000));
        }

        final long ram = 2048; //in Megabytes
        final long bw = 10000; //in Megabits/s
        final long storage = 1000000; //in Megabytes

        return new HostSimple(ram, bw, storage, peList);
    }
    
     */





}
