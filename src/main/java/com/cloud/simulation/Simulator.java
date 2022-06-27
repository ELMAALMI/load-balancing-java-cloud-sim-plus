package com.cloud.simulation;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;

import java.util.ArrayList;
import java.util.List;

public class Simulator {
    private int  HOSTS = 1;
    private int  HOST_PES = 8;
    private int  HOST_MIPS = 1000;
    private int  HOST_RAM = 2048; //in Megabytes
    private long HOST_BW = 10_000; //in Megabits/s
    private long HOST_STORAGE = 1_000_000; //in Megabytes

    private int VMS = 2;
    private int VM_PES = 4;

    private int CLOUDLETS = 4;
    private int CLOUDLET_PES = 2;
    private int CLOUDLET_LENGTH = 10_000;

    private CloudSim simulation;
    private DatacenterBroker broker0;
    private List<Vm> vmList;
    private List<Cloudlet> cloudletList;
    private Datacenter datacenter;


    public Simulator() {
       init();
    }
    public void start(){
        this.simulation.start();
        final List<Cloudlet> finishedCloudlets = broker0.getCloudletFinishedList();
        new CloudletsTableBuilder(finishedCloudlets).build();
    }
    private void init(){
        simulation = new CloudSim();
        datacenter = createDatacenter();
        //Creates a broker that is a software acting on behalf a cloud customer to manage his/her VMs and Cloudlets
        broker0 = new DatacenterBrokerSimple(simulation);
        vmList = createVms();
        cloudletList = createCloudlets();
        broker0.submitVmList(vmList);
        broker0.submitCloudletList(cloudletList);
    }
    private Datacenter createDatacenter() {
        final ArrayList<Host> hostList = new ArrayList<Host>(HOSTS);
        for(int i = 0; i < HOSTS; i++) {
            final Host host = createHost();
            hostList.add(host);
        }
        //Uses a VmAllocationPolicySimple by default to allocate VMs
        return new DatacenterSimple(simulation, hostList);
    }
    private Host createHost() {
        final ArrayList<Pe> peList = new ArrayList<Pe>(HOST_PES);
        //List of Host's CPUs (Processing Elements, PEs)
        for (int i = 0; i < HOST_PES; i++) {
            //Uses a PeProvisionerSimple by default to provision PEs for VMs
            peList.add(new PeSimple(HOST_MIPS));
        }

        /*
        Uses ResourceProvisionerSimple by default for RAM and BW provisioning
        and VmSchedulerSpaceShared for VM scheduling.
        */
        return new HostSimple(HOST_RAM, HOST_BW, HOST_STORAGE, peList);
    }
    private List<Vm> createVms() {
        final ArrayList<Vm> vmList = new ArrayList<Vm>(VMS);
        for (int i = 0; i < VMS; i++) {
            //Uses a CloudletSchedulerTimeShared by default to schedule Cloudlets
            final Vm vm = new VmSimple(HOST_MIPS, VM_PES);
            vm.setRam(512).setBw(1000).setSize(10_000);
            vmList.add(vm);
        }

        return vmList;
    }
    private List<Cloudlet> createCloudlets() {
        final ArrayList<Cloudlet> cloudletList = new ArrayList<Cloudlet>(CLOUDLETS);
        //UtilizationModel defining the Cloudlets use only 50% of any resource all the time
        final UtilizationModelDynamic utilizationModel = new UtilizationModelDynamic(0.5);
        for (int i = 0; i < CLOUDLETS; i++) {
            final Cloudlet cloudlet = new CloudletSimple(CLOUDLET_LENGTH, CLOUDLET_PES, utilizationModel);
            cloudlet.setSizes(1024);
            cloudletList.add(cloudlet);
        }
        return cloudletList;
    }

    public void setHOSTS(int HOSTS) {
        this.HOSTS = HOSTS;
    }

    public void setHOST_PES(int HOST_PES) {
        this.HOST_PES = HOST_PES;
    }

    public void setHOST_MIPS(int HOST_MIPS) {
        this.HOST_MIPS = HOST_MIPS;
    }

    public void setHOST_RAM(int HOST_RAM) {
        this.HOST_RAM = HOST_RAM;
    }

    public void setHOST_BW(long HOST_BW) {
        this.HOST_BW = HOST_BW;
    }

    public void setHOST_STORAGE(long HOST_STORAGE) {
        this.HOST_STORAGE = HOST_STORAGE;
    }

    public void setVMS(int VMS) {
        this.VMS = VMS;
    }

    public void setVM_PES(int VM_PES) {
        this.VM_PES = VM_PES;
    }

    public void setCLOUDLETS(int CLOUDLETS) {
        this.CLOUDLETS = CLOUDLETS;
    }

    public void setCLOUDLET_PES(int CLOUDLET_PES) {
        this.CLOUDLET_PES = CLOUDLET_PES;
    }

    public void setCLOUDLET_LENGTH(int CLOUDLET_LENGTH) {
        this.CLOUDLET_LENGTH = CLOUDLET_LENGTH;
    }
}
