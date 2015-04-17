/*
 * Rainbow - A simulator of processes and resources in a multitasking computer.
 * Copyright (C) 2006. E-mail: piero.dallepezze@gmail.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *
 * File: Scheduler.java
 * Package: scheduler
 * Author: Piero Dalle Pezze
 * Date: 29/01/2006
 * Version: 1.4
 *
 * Modifies:
 * v1.6 (13/11/2014): Added iterators.
 * v1.5 (10/11/2014): Transformed into an abstract class to enable the extension of additional Schedulers
 *                    different from the default RainbowScheduler.
 * v1.4 (04/06/2007): Corrections in terminatePCBCurrent and removePCBCurrent.
                      It tests before the termination then the release of resources.
 * v1.3 (31/01/2007): English translation. Java6 compatible.
 * v1.2 (05/02/2006): Class testing.
 * v1.1 (30/01/2006): Class codify.
 * v1.0 (29/01/2006): Class documentation.
 */
package org.rainbow.scheduler;

import java.util.*;
import org.rainbow.gui.input.RainbowConfig;
import org.rainbow.data.*;
import org.rainbow.simulation.*;

/**
 * This class implements the mechanisms to realize a discrete simulation of
 * processes in a multitasking computer. So it must manages and orders all
 * events that can rise during a schedulation and computes time intervals which
 * don't the scheduler inner state. The events space is the follow: </br>
 * <ol>
 * <li>Process activation.</li>
 * <li>Request of a new resource.</li>
 * <li>Release of a resource.</li>
 * <li>Process termination.</li>
 * </ol>
 *
 * @author Piero Dalle Pezze
 * @version 1.6
 */
public abstract class Scheduler {
    
    /**
     * It mantains for each resource, its blocked processes.
     */
    protected HashMap<Resource, AssignmentPolicy> assignment = null;
    
    /**
     * It mantains for each resource, processes that can access it.
     */
    protected HashMap<Resource, LinkedList<PCB>> currentAttribution = null;
    
    /**
     * The process that is executing at the current time.
     */
    protected PCB pcbCurrent = null;
    
    /**
     * The scheduling algorithm to use.
     */
    protected SchedulingPolicy schedulingPolicy = null;
    
    /**
     * The list of created processes sorted by activation time increasing. Next,
     * they will be activated.
     */
    protected LinkedList<SimulatedProcess> createdProcesses = null;
    
    /**
     * The list of terminated processes.
     */
    protected ArrayList<SimulatedProcess> terminatedProcesses;
    
    /**
     * The list of the available resources when this class is instanced.
     */
    protected ArrayList<Resource> availableResources = null;
    
    /**
     * The process table. Each entry is a pcb, so an activated process.
     */
    protected HashMap<Integer, PCB> pcbTable = null;
    
    /**
     * The current time. This is the representation of the CPU clock.
     */
    protected int currentTime = 0;
    
  
    
    /** 
     * This interface must be implemented in a scheduler extending this scheduler.
     */
    protected ProcessDispatcherInterface dispatcher = null;
    
    


    
    
    /**
     * It creates a Scheduler instance.
     *
     * @param configuration
     *            The user configuration.
     */
    public Scheduler(Configuration configuration) {


        /* SCHEDULING AND ASSIGNMENT POLICES SETTING */    
        schedulingPolicy = RainbowConfig.getSchedulingPolicy(configuration);
        AssignmentPolicy assignmentPolicy = RainbowConfig.getAssignmentPolicy(configuration);
        

        
        /* PROCESSES SETTING */    
        ArrayList<SimulatedProcess> processes = configuration.getProcesses();
        
        if (processes == null) {
            processes = new ArrayList<SimulatedProcess>();
        }
        if (availableResources == null) {
            availableResources = new ArrayList<Resource>(1);
        }
        
        /* sorting by activation time increasing */
        SimulatedProcess p;
        boolean added = false;
        createdProcesses = new LinkedList<SimulatedProcess>();
        Iterator<SimulatedProcess> itProcesses = processes.iterator();
        while(itProcesses.hasNext()) {
            p = itProcesses.next();        
            added = false;
            ListIterator<SimulatedProcess> itCreatedProcesses = createdProcesses.listIterator();
            while(itCreatedProcesses.hasNext() && !added) {
                if (p.getActivationTime() < itCreatedProcesses.next()
                .getActivationTime()) {
                    createdProcesses.add(itCreatedProcesses.previousIndex(), p);
                    added = true;
                }
            }
            if (!added) {
                createdProcesses.addLast(p);
            }
            
        }
        pcbTable = new HashMap<Integer, PCB>(createdProcesses.size());        
        terminatedProcesses = new ArrayList<SimulatedProcess>(createdProcesses
                .size());        
        
        
        
        /* RESOURCES SETTING */
        availableResources = new ArrayList<Resource>(configuration
                .getResources());
        // Setting of the capacity
        assignment = new HashMap<Resource, AssignmentPolicy>(availableResources
                .size());
        currentAttribution = new HashMap<Resource, LinkedList<PCB>>(
                availableResources.size());
        Resource resource = null;
        // Adds resources
        Iterator<Resource> itAvailableResources = availableResources.iterator();
        while(itAvailableResources.hasNext()) {
	    resource = itAvailableResources.next();
            currentAttribution.put(resource, new LinkedList<PCB>());
            if (!(resource instanceof PreemptiveResource)) {
                // Create a new instance of type AssignmentPolicy
                try {
                    assignment.put(resource, assignmentPolicy.getClass()
                    .newInstance());
                } catch (InstantiationException e) {
                    System.out.println("The policy cannot be instanced.");
                } catch (IllegalAccessException e) {
                    System.out.println("IllegalAccessException");
                }
                
            }
        }
        
        
    }
    
    
    
    
    
    
    /**
     * Set the dispatcher allowing a scheduling policy interact with the scheduler. This method 
     * must be called in the constructor of any inherited Scheduler if a dispatcher is re-implemented.
     *
     * @param dispatcher
     *            A dispatcher instance, implemented within an instance of Scheduler.
     */    
    protected void setProcessDispatcherInterface(ProcessDispatcherInterface dispatcher) {      
	this.dispatcher = dispatcher;
        /* Associate the process dispatcher to the scheduling policy. */
        schedulingPolicy.setProcessDispatcherInterface(dispatcher);
    }  
    
    
    
    
    
    /**
     * This is the core of the discrete simulation of processes. An execution
     * terminates when there are only blocked and terminated processes. An
     * iteration provides to extract a ready process and to run it until the
     * inner scheduler state doesn't change. Every process is extracted by using
     * a proper scheduling algorithm. The execution of the extracted process is
     * upper bound by the time of the first next event that occurs in the
     * scheduler. This method is able to notify a deadlock if it rises.
     *
     * @return the history of the simulation.
     */
    public abstract LinkedList<State> run();
    
 
     /**
     * It creates object of type State. It store the inner state of the
     * scheduler.
     *
     * @param duration
     *            the duration of the state
     * @return an object State.
     */
    protected State computeState(int duration) {
        State s = new State(duration);
        /* copy the current process */
        SimulatedProcess cp = null;
        if (pcbCurrent != null) {
            cp = pcbCurrent.getSimulatedProcess();
        }
        s.setRunning(cp);
        s.setReady(schedulingPolicy.getReadyQueue());
        s.setTerminated(new ArrayList<SimulatedProcess>(terminatedProcesses));
        ArrayList<ResourceAttribution> attribution = new ArrayList<ResourceAttribution>();
        ArrayList<ResourceAttribution> blockedQueues = new ArrayList<ResourceAttribution>();
        /* Dichiarati oggetti qui */
        LinkedList<PCB> pcbList = null;
        ArrayList<SimulatedProcess> processQueue = null;
        Resource r = null;
        AssignmentPolicy ap = null;
        if (availableResources != null) {
            /* Copy of currentAttribution */
            Iterator<Resource> itAvailableResources = availableResources.iterator();
            while(itAvailableResources.hasNext()) {
                r = itAvailableResources.next();
                pcbList = currentAttribution.get(r);
                processQueue = new ArrayList<SimulatedProcess>(pcbList.size());
                Iterator<PCB> itPCBList = pcbList.iterator();
                while(itPCBList.hasNext()) { 
                    processQueue.add(itPCBList.next().getSimulatedProcess());                
                }              
                attribution.add(new ResourceAttribution(r, processQueue));
                if (r instanceof NoPreemptiveResource) {
                    /* Copy of assignment. */
                    ap = assignment.get(r);
                    ArrayList<Request> req = ap.getRequestsQueue();
                    ArrayList<SimulatedProcess> sp = new ArrayList<SimulatedProcess>(
                            req.size());                
		    Iterator<Request> itReq = req.iterator();
		    while(itReq.hasNext()) {
                        sp.add(pcbTable.get(itReq.next().getId())
                        .getSimulatedProcess());		    
		    }
                    blockedQueues.add(new ResourceAttribution(r, sp));
                }         
            }           
        }
        s.setAttributedResources(attribution);
        s.setBlocked(blockedQueues);
        return s;
    }
 
 
    
    
    
    /* RESOURCE ALLOCATION */
    /**
     * It returns true if the resource is allocated to the process, false
     * otherwise.
     */
    protected boolean allocate(Resource resource) {
        // ap means attributedProcesses
        Boolean allocated = false;
        LinkedList<PCB> ap = currentAttribution.get(resource);
        if (resource.getMultiplicity() > ap.size()) {
            /* the resource is available. */
            ap.addFirst(pcbCurrent);
            allocated = true;
        } else {
            /* The resource is completely occupated. */
            if (resource instanceof PreemptiveResource) {
                ap.removeFirst();
                ap.addFirst(pcbCurrent);
                allocated = true;
            }
        }
        if (allocated) {
            pcbCurrent.resourceAttribuited();
            /* it allocates its preemptive resources. They can be subtracted.
             * This is implemented as a stack.*/
            LinkedList<Resource> prStack = pcbCurrent.getPreemptiveResources();
            if (prStack != null) {
		while(!prStack.isEmpty()) {
		    attributePreemptiveResource(prStack.pop());
		}
            }
            return true;
        }
        // the resource cannot be assigned to the pcbCurrent.
        // Note: it is a non preemptive resource.
        return false;
    }
    
    
    /**
     * It attibutes to the process in execution all its preemptive resources.
     */
    protected void attributePreemptiveResource(Resource resource) {
        LinkedList<PCB> ap = currentAttribution.get(resource);
        if (ap.contains(pcbCurrent)) {
            /* the resource is already allocated to the process. */
            return;
        }
        if (resource.getMultiplicity() > ap.size()) {
            /* the resource is available. */
            ap.addFirst(pcbCurrent);
        } else {
            ap.removeFirst();
            ap.addFirst(pcbCurrent);
        }
    }
    
            
    /**
     * It releases a resource attributed to the process.
     *
     * @param pcb
     *            The process that have to release the resource.
     */
    protected void releaseResource(PCB pcb) {
        // lp means list processes
        Resource resource = pcb.getReleasedResource();
        LinkedList<PCB> lp = currentAttribution.get(resource);
        lp.remove(pcb);
        if (resource instanceof NoPreemptiveResource) {
            /* SIGNAL */
            // signal a process that is waiting for the resource.
            Request r = assignment.get(resource).extract();
            if (r != null) {
                /* SIGNAL and SCHEDULING */
                schedulingPolicy.insert(pcbTable.get(r.getId()));
            }
        }
    }
    
    
    /**
     * It release all resource used by a running process that has completed its
     * execution.
     *
     * @param terminated
     *            The process that has terminated its execution.
     */
    protected void releaseAllResources(PCB terminated) {
        // ur means used resources. This is implemented as a Stack
        LinkedList<Resource> urStack = terminated.getUsedResources();
        Resource resource = null;
        
        if (urStack != null) {
	    while(!urStack.isEmpty()) {        
                resource = urStack.pop();
                LinkedList<PCB> lp = currentAttribution.get(resource);
                lp.remove(terminated);
                if (resource instanceof NoPreemptiveResource) {
                    /* SIGNAL */
                    // signal a process that is waiting for the resource.
                    Request r = assignment.get(resource).extract();
                    if (r != null) {
                        /* SIGNAL and SCHEDULING */
                        schedulingPolicy.insert(pcbTable.get(r.getId()));
                    }
                }
            }
        }
    }
    

    
    
}
