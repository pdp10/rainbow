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
 * File: RainbowScheduler.java
 * Package: scheduler
 * Author: Piero Dalle Pezze
 * Date: 29/01/2006
 * Version: 1.4
 *
 * Modifies:
 * v1.6 (13/11/2014): Added iterators.    
 * v1.5 (10/11/2014): Significant semplification and transformed into a specific Scheduler 
		      with a design list of Events. This class extends a generic Scheduler now.
 * v1.4 (04/06/2007): Corrections in terminatePCBCurrent and removePCBCurrent.
                      It tests before the termination then the release of resources.
 * v1.3 (31/01/2007): English translation. Java6 compatible.
 * v1.2 (05/02/2006): Class testing.
 * v1.1 (30/01/2006): Class codify.
 * v1.0 (29/01/2006): Class documentation.
 */
package org.rainbow.scheduler;

import java.util.*;
//import org.rainbow.gui.input.RainbowConfig;
import org.rainbow.data.*;
import org.rainbow.simulation.*;





/**
 * This class implements the mechanisms to realize a discrete simulation of
 * processes in a multitasking computer. It manages and orders all
 * events that can rise during a schedulation and computes the scheduler inner state. 
 * The events space is the follow: </br>
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
public class RainbowScheduler extends Scheduler {
    
    
    /**
     * This structure store times of a SET of possible events. The minor of these
     * is the time that the scheduler can execute a process WITHOUT changing
     * scheduler internal state.
     */
    protected int[] eventTable = new int[4];
    
    /* THE SPACE OF EVENTS */
    /** Time of the next process activation. */
    protected static final int ACTIVE_PROCESS = 0;
    
    /** Time of the next request of resource (of the pcbCurrent). */
    protected static final int REQUEST_RESOURCE = 1;
    
    /** Time of the next release of resource (of the pcbCurrent). */
    protected static final int RELEASE_RESOURCE = 2;
    
    /** Time of the remaining time of execution (of the pcbCorrente). */
    protected static final int TERMINATE_PROCESS = 3;
    
    
    
    
    
    /**
     * It stores the time of the next event.
     */
    protected int nextEventTime = 0;
    
    
    
  
    
 
    
    /** This class implements the interface ProcessDispatcherInterface allowing 
     * a SchedulingPolicy to interact with the Scheduler using only a subset of methods.
     */
    class ProcessDispatcher implements ProcessDispatcherInterface {
            
	    /**
	    * {@inheritDoc} 
	    */
	    public State increaseSchedulerTime(int time) {
		if (time < 1 || time > nextEventTime) {
		    time = 1;
		}
		pcbCurrent.increaseExecutedTime(time);
		State s = computeState(time);
		/* Increase the scheduler time */
		currentTime = currentTime + time;
		/* Decrease the times of the next events. */
		for (int i = 0; i < eventTable.length; i++) {
		    if (eventTable[i] > 0) {
			eventTable[i] = eventTable[i] - time;
		    }
		}
		return s;
	    }

	    
	    /**
	    * {@inheritDoc} 
	    */
	    public PCB getPCBCurrent() {
		return pcbCurrent;
	    }
	    

	    /**
	    * {@inheritDoc} 
	    */
	    public int getCurrentTime() {
		return currentTime;
	    }
	    
	    
	    /**
	    * {@inheritDoc} 
	    */
	    public HashMap<Integer, PCB> getPCBTable() {
		return pcbTable;
	    }
	    
	    
	    /**
	    * {@inheritDoc} 
	    */
	    public void preemptionPCBCurrent() {
		resetProcessEVT();
		pcbCurrent = null;
	    }
	    
	    /**
	    * {@inheritDoc} 
	    */
	    public void removePCBCurrent() {
		if (eventTable[TERMINATE_PROCESS] == 0) {
		    process_termination_event();
		}
		if (eventTable[RELEASE_RESOURCE] == 0) {
		    releaseResource(pcbCurrent);
		}		
		
		resetProcessEVT();
		pcbCurrent = null;
	    }
    
    }    
    
   
    
    
    /**
     * It creates a RainbowScheduler instance.
     *
     * @param configuration
     *            The user configuration.
     */
    public RainbowScheduler(Configuration configuration) {

	super(configuration);   
	
      
        resetProcessEVT();
                /*
                 * Sets the first activation.
                 */
        if (createdProcesses.size() > 0) {
            eventTable[ACTIVE_PROCESS] = createdProcesses.getFirst()
            .getActivationTime();
        } else {
            eventTable[ACTIVE_PROCESS] = -1;
        }
         
        setProcessDispatcherInterface(new ProcessDispatcher());
       
    }

    
    
    
    
    
    
    
    protected void resetProcessEVT() {
        eventTable[REQUEST_RESOURCE] = -1;
        eventTable[RELEASE_RESOURCE] = -1;
        eventTable[TERMINATE_PROCESS] = -1;
    }
    
    
    /**
     * It sets next events of the process extracted from the ready queue.
     *
     */
    protected void setProcessEVT() {
        eventTable[REQUEST_RESOURCE] = pcbCurrent.nextRequestTime();
        eventTable[RELEASE_RESOURCE] = pcbCurrent.firstReleaseTime();
        eventTable[TERMINATE_PROCESS] = pcbCurrent.getRemainingTime();
    }
    
    
    /**
     * It computes the interval between the current time and the time of the
     * next event. So:
     *
     * T(NextEvent) = min( T(Event) ) for each event in EventSpace
     *
     * where T is the time. The minimum function allows an increasing sorting of
     * events. This methods implements such minor function.
     *
     * @return the remaining time to the first next event.
     */
    protected int nextEventTime() {
                /*
                 * Update process events.
                 */
        eventTable[REQUEST_RESOURCE] = pcbCurrent.nextRequestTime();
        eventTable[RELEASE_RESOURCE] = pcbCurrent.firstReleaseTime();
        eventTable[TERMINATE_PROCESS] = pcbCurrent.getRemainingTime();
                /*
                 * eventTable[i] for i=0..2 is -1 if these events are not present. Of
                 * course, eventTable[TERMINATED_PROCESS] contains the maximum.
                 */
        int min = eventTable[TERMINATE_PROCESS];
        for (int i = 0; i < eventTable.length-1; i++) {
            if (eventTable[i] > 0 && eventTable[i] < min) {
                min = eventTable[i];
            }
        }
        return min;
    }    
    
    
    
    /**
     * It actives a new process.
     */
    protected void activate() {
        // createdProcess is not empty
        while (eventTable[ACTIVE_PROCESS] == 0) {
            // ACTIVATION
            SimulatedProcess process = createdProcesses.removeFirst();
            PCB pcbNew = new PCB(process);
            pcbTable.put(process.getId(), pcbNew);
            schedulingPolicy.insert(pcbNew);
            
            // Compute the next activation process time. If there aren't any
            // processes, eventTable[0] = -1;
            if (createdProcesses.size() > 0) {
                eventTable[ACTIVE_PROCESS] = createdProcesses.getFirst()
                .getActivationTime()
                - currentTime;
            } else {
                eventTable[ACTIVE_PROCESS] = -1;
            }
        }
    }
    
    /**
     * It increases of an unit the currentTime
     */
    protected void increaseCurrentTime() {
        currentTime++;
        if (eventTable[ACTIVE_PROCESS] > 0) {
            eventTable[ACTIVE_PROCESS] = eventTable[ACTIVE_PROCESS] - 1;
        }
        // if eventTable[ACTIVE_PROCESS] == 0, then the next time a new process
        // will be activated.
    }
    
    /**
     * It increases of the variable time the currentTime
     *
     * @param time
     *            the time to increase
     */
    protected void increaseCurrentTime(int time) {
        currentTime = currentTime + time;
        if (eventTable[ACTIVE_PROCESS] > 0) {
            eventTable[ACTIVE_PROCESS] = eventTable[ACTIVE_PROCESS] - time;
        }
        // NOTE: eventTable[ACTIVE_PROCESS] will be 0 or -1
    }
    
    
    
    
    
    
    /**
     * Activate a new process if any.
     * @param history
     *		    the history of this simulation
     */
    protected void process_activation_event(LinkedList<State> history) {
	if (schedulingPolicy.size() == 0 && pcbCurrent == null
		&& eventTable[ACTIVE_PROCESS] > 0) {
	    int time = eventTable[ACTIVE_PROCESS];
	    increaseCurrentTime(time);
	    history.add(computeState(time));
	}
	if (eventTable[ACTIVE_PROCESS] == 0) {
	    activate();
	}
     }    
    
    
    /**
     * Extract a PCB (if any) and allocates its resources.
     */    
     protected void process_extraction_event() {
            if (pcbCurrent == null) {
                pcbCurrent = schedulingPolicy.extract();
                LinkedList<Resource> prStack = pcbCurrent.getPreemptiveResources();
                if (prStack != null) {
                    Resource r = null;
                    while(!prStack.isEmpty()) {
                        r = prStack.pop();
                        attributePreemptiveResource(r);
                    }
                }
                // see conditions. pcbCurrent exists.
                setProcessEVT();
            }
            if (eventTable[REQUEST_RESOURCE] == 0) {
                Resource resource = pcbCurrent.getResource();
                if (allocate(resource)) {
                    // the resource is available. Update of the following event.
                    eventTable[REQUEST_RESOURCE] = pcbCurrent.nextRequestTime();
                } else {
                    // the resource in not available. -> process blocked
                    assignment.get(resource).insert(new Request(pcbCurrent));
                    pcbCurrent = null;
                    resetProcessEVT();
                }
            }    
      }
    
    /**
     * It advances the scheduler (execution event).
     * @param history
     *		    the history of this simulation
     */    
     protected void scheduler_advancement_event(LinkedList<State> history) {
            if (pcbCurrent != null) {
                nextEventTime = nextEventTime();
                history.add(schedulingPolicy.execute(nextEventTime));
            } else {
                increaseCurrentTime();
                history.add(computeState(1));
            }
     }
      
    /**
     * It terminates the current PCB if it is ended. Before terminating it, it 
     * releases all its resources, if any.
     */      
     protected void process_termination_event() {
            if (eventTable[TERMINATE_PROCESS] == 0) {
              // It releases all used resources
	      PCB terminated = pcbCurrent;
	      pcbCurrent = null;
	      resetProcessEVT();
	      releaseAllResources(terminated);
	      pcbTable.remove(terminated.getSimulatedProcess().getId());
	      terminatedProcesses.add(terminated.getSimulatedProcess());
            }
     }
      
    /**
     * It releases the resources of the current PCB, if any. 
     */      
     protected void resource_release_event() { 
            if (eventTable[RELEASE_RESOURCE] == 0) {
                eventTable[RELEASE_RESOURCE] = pcbCurrent.firstReleaseTime();
                releaseResource(pcbCurrent);
            }
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
    public LinkedList<State> run() {
        // the history of the simulation.
        LinkedList<State> history = new LinkedList<State>();
        while (schedulingPolicy.size() > 0 || eventTable[ACTIVE_PROCESS] > -1
                || pcbCurrent != null) {
	    process_activation_event(history);
	    process_extraction_event();
	    scheduler_advancement_event(history);
	    process_termination_event();
	    resource_release_event();
        }
        State s = computeState(1);
        if (pcbTable.size() > 0) {
            s.setDeadlock(true);
        }
        history.add(s);
        return history;
    }
    
      

}
