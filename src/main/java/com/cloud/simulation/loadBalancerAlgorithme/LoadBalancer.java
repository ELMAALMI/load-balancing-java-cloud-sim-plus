package com.cloud.simulation.loadBalancerAlgorithme;

import java.util.*;


import com.cloud.simulation.AppServer.Request;
import com.cloud.simulation.loadBalancerAlgorithme.FirstComeFirstServe.FCFSDatacenterBroker;
import com.cloud.simulation.loadBalancerAlgorithme.GeneticAlgorithm.GeneticAlgorithmDatacenterBroker;
import com.cloud.simulation.loadBalancerAlgorithme.Priority.PriorityDatacenterBroker;
import com.cloud.simulation.loadBalancerAlgorithme.RoundRobin.RoundRobinDatacenterBroker;
import com.cloud.simulation.loadBalancerAlgorithme.ShortestJobFirst.ShortestJobFirstDatacenterBroker;
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class LoadBalancer {

    private List<Vm> createVM(int userId, int numberOfVm) {
        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<Vm> list = new LinkedList<>();

        //VM Parameters
        long size = 10000; //image size (MB)
        int ram = 512; //vm memory (MB)
        int mips = 1000;
        long bw = 1000;
        int pesNumber = 4; //number of cpus
        String vmm = "Xen"; //VMM name

        for(int i = 0; i< numberOfVm; i++){
            Vm vm = new Vm(i, userId, mips+(i*10), pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
            list.add(vm);
        }

        return list;
    }

    private List<Cloudlet> createCloudlet(int userId, int numberOfCloudlet){
        // Creates a container to store Cloudlets
        LinkedList<Cloudlet> list = new LinkedList<>();

        //cloudlet parameters
        long length = 1000;
        long fileSize = 300;
        long outputSize = 300;
        int pesNumber = 1;
        UtilizationModel utilizationModel = new UtilizationModelFull();

        for(int i = 0; i < numberOfCloudlet; i++){
            Cloudlet cloudlet = new Cloudlet(
                    i,
                    (length + 2L * i * 10),
                    pesNumber,
                    fileSize,
                    outputSize,
                    utilizationModel,
                    utilizationModel,
                    utilizationModel
            );
            cloudlet.setUserId(userId);
            cloudlet.setVmId(i);
            list.add(cloudlet);
        }

        return list;
    }

    public List<SimResult> start(Request request) {
        Log.printLine();
        try {
            Calendar calendar = Calendar.getInstance();
            //
            int numUsers = request.getUsers();
            int numberOfDatacenters = request.getDs();
            int numberOfVm = request.getVm();
            String algo = request.getAlgo();
            int numberOfCloudlet = request.getCloudlet();


            // Initialize the CloudSim library
            CloudSim.init(numUsers, calendar, false);

            for (int i = 0; i < numberOfDatacenters; i++) {
                createDatacenter("Datacenter_" + i);
            }
            DatacenterBroker broker = null;
                try {
                    switch (algo) {
                        case "RR" :
                            broker = new RoundRobinDatacenterBroker("Broker");
                            break;
                        case "SJF" :
                            broker = new ShortestJobFirstDatacenterBroker("Broker");
                            break;
                        case "FCFS" :
                            broker = new FCFSDatacenterBroker("Broker");
                            break;
                        case "GA" :
                            broker = new GeneticAlgorithmDatacenterBroker("Broker");
                            break;
                        case "P" :
                            broker = new PriorityDatacenterBroker("Broker");
                            break;
                        default:
                            broker = new RoundRobinDatacenterBroker("Broker");
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            int brokerId = broker.getId();

            Log.printLine();
            Log.printLine("Fourth step: Create VMs");
            List<Vm> vmList = createVM(brokerId, numberOfVm);

            Log.printLine();
            Log.printLine("Fifth step: Create Cloudlets");

            List<Cloudlet> cloudletList = createCloudlet(brokerId, numberOfCloudlet);
            Log.printLine("Sending them to broker...");

            broker.submitVmList(vmList);
            broker.submitCloudletList(cloudletList);

            Log.printLine();
            Log.printLine("Sixth step: Starts the simulation");

            CloudSim.startSimulation();

            Log.printLine();
            Log.printLine("Final step: Print results when simulation is over");
            List<Cloudlet> cloudletReceivedList = broker.getCloudletReceivedList();
            List<Vm> vmsCreatedList = broker.getVmsCreatedList();

            CloudSim.stopSimulation();

            printCloudletList(cloudletReceivedList);

            Log.printLine();
            Log.printLine("Simulation Complete");
            return getResult(cloudletReceivedList);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
        return null;
    }

    private Datacenter createDatacenter(String name){

        // Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store one or more
        //    Machines
        List<Host> hostList = new ArrayList<>();

        // 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
        //    create a list to store these PEs before creating
        //    a Machine.
        List<Pe> peList1 = new ArrayList<>();

        int mips = 10000;

        // 3. Create PEs and add these into the list.
        //for a quad-core machine, a list of 4 PEs is required:
        peList1.add(new Pe(0, new PeProvisionerSimple(mips + 500))); // need to store Pe id and MIPS Rating
        peList1.add(new Pe(1, new PeProvisionerSimple(mips + 1000)));
        peList1.add(new Pe(2, new PeProvisionerSimple(mips + 1500)));
        peList1.add(new Pe(3, new PeProvisionerSimple(mips + 700)));

        //Another list, for a dual-core machine
        List<Pe> peList2 = new ArrayList<>();

        peList2.add(new Pe(0, new PeProvisionerSimple(mips + 700)));
        peList2.add(new Pe(1, new PeProvisionerSimple(mips + 900)));

        //4. Create Hosts with its id and list of PEs and add them to the list of machines
        int hostId=0;
        int ram = 1002048; //host memory (MB)
        long storage = 1000000; //host storage
        int bw = 10000;

        hostList.add(
                new Host(
                        hostId,
                        new RamProvisionerSimple(ram),
                        new BwProvisionerSimple(bw),
                        storage,
                        peList1,
                        new VmSchedulerTimeShared(peList1)
                )
        ); // This is our first machine

        hostId++;

        hostList.add(
                new Host(
                        hostId,
                        new RamProvisionerSimple(ram),
                        new BwProvisionerSimple(bw),
                        storage,
                        peList2,
                        new VmSchedulerTimeShared(peList2)
                )
        ); // Second machine

        // 5. Create a DatacenterCharacteristics object that stores the
        //    properties of a data center: architecture, OS, list of
        //    Machines, allocation policy: time- or space-shared, time zone
        //    and its price (G$/Pe time unit).
        String arch = "x64";      // system architecture
        String os = "Linux";          // operating system
        String vmm = "Xen";
        double time_zone = 10.0;         // time zone this resource located
        double cost = 3.0;              // the cost of using processing in this resource
        double costPerMem = 0.05;		// the cost of using memory in this resource
        double costPerStorage = 0.1;	// the cost of using storage in this resource
        double costPerBw = 0.1;			// the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<>();	//we are not adding SAN devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


        // 6. Finally, we need to create a PowerDatacenter object.
        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }
    public List<SimResult> getResult(List<Cloudlet> list){
        List<SimResult> simResults = new ArrayList<>();
        for (Cloudlet cloudlet:list){
            SimResult simResult = new SimResult();
            simResult.setResourceId(cloudlet.getResourceId());
            simResult.setVmId(cloudlet.getVmId());
            simResult.setCpuTime(cloudlet.getActualCPUTime());
            simResult.setStartTime(cloudlet.getExecStartTime());
            simResult.setEndTime(cloudlet.getFinishTime());
            //
            simResults.add(simResult);
        }
        return simResults;
    }

    private void printCloudletList(List<Cloudlet> list) {
        String indent = "    ";
        Log.printLine();
        Log.printLine();
        Log.printLine("========================================== OUTPUT ==========================================");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent + "Datacenter ID" + indent + "VM ID" + indent + " " + "Time" + indent + "Start Time" + indent + "Finish Time");
        double time = 0;


        for (Cloudlet value : list) {
            value.getVmId();
            Log.print(indent + String.format("%02d", value.getCloudletId()) + indent + indent);
            if (value.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");
                time += (  value.getFinishTime() - value.getExecStartTime());

                Log.printLine(indent + indent + String.format("%02d", value.getResourceId()) +
                        indent + indent + indent + String.format("%02d", value.getVmId()) +
                        indent + indent + String.format("%.2f", value.getActualCPUTime()) +
                        indent + indent + String.format("%.2f", value.getExecStartTime()) +
                        indent + indent + indent + String.format("%.2f", value.getFinishTime())
                );
            }
        }
        double avgTime = time/list.toArray().length;
        Log.printLine("Total CPU Time: " + time);
        Log.printLine("Average CPU Time: " + avgTime);

    }

}
