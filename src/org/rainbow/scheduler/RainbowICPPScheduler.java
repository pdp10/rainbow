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
 * File: RainbowICPPScheduler.java
 * Package: scheduler
 * Author: Piero Dalle Pezze
 * Date: 26/02/2007
 * Version: 1.0
 * 
 * Modifies:
 * v1.1 (13/11/2014): Added iterators.    
 * v1.0 (26/02/2007): Class codify and documentation.
 */
package org.rainbow.scheduler;

import java.util.*;

import org.rainbow.data.*;
import org.rainbow.simulation.*;

/**
 * This class implements the mechanisms to realize a discrete simulation of
 * processes in a multitasking computer using the immediate ceiling priority
 * protocol.
 * 
 * @author Piero Dalle Pezze
 * @version 1.1
 */
public class RainbowICPPScheduler extends RainbowScheduler {

	/**
	 * It is true if the ICPP is violated, false otherwise.
	 */
	protected boolean icppViolated = false;

	/**
	 * It is true if the there is a priority inversion.
	 */
	protected boolean priorityInversion = false;

	
	
	/**
	 * It creates a SchedulerICPP instance.
	 * 
	 * @param configuration
	 *            The user configuration.
	 */
	public RainbowICPPScheduler(Configuration configuration) {
		super(configuration);
	}

	
	
	/**
	 * It returns true if the resource is allocated to the process, false
	 * otherwise.
	 */
	protected boolean allocate(Resource resource) {
		if (!super.allocate(resource)) {
			return false;
		}
		if (resource instanceof NoPreemptiveResource) {
			int executionPriority = pcbCurrent.getActivePriority();
			int ceilingPriority = ((NoPreemptiveResource) resource)
					.getCeilingPriority();
			if (executionPriority <= ceilingPriority) {
				/*
				 * not strict mode " <= ". The user can configure the
				 * possiblility of ceilingPriority of the resource will be < or <=
				 * than processes accessing her.
				 */
				icppViolated = false;
				priorityInversion = true;
				pcbCurrent.setActivePriority(ceilingPriority);
			} else {
				icppViolated = true;
			}
		}
		return true;
	}

	
	
	
	/**
	 * It creates object of type State. It store the inner state of the
	 * scheduler.
	 * 
	 * @param duration
	 *            the duration of the state
	 * @return an object State.
	 */
	protected State computeState(int duration) {
		State s = super.computeState(duration);
		if (icppViolated) {
			s.setCeilingPriorityViolation(true);
		}
		if (priorityInversion) {
			s.setPriorityInversion(priorityInversion);
		}
		priorityInversion = false;
		icppViolated = false;
		return s;
	}

	
	/**
	 * It release a resource attributed to the process.
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
			/* It sets the priority. (monotonic non-increasing) */
			LinkedList<Resource> resourcesStack = pcbCurrent.getUsedResources();
			int maxPriority = pcbCurrent.getSimulatedProcess()
					.getInitialPriority();
			int priorityCeiling = maxPriority;
			
			Iterator<Resource> itResourcesStack = resourcesStack.iterator();
			while(itResourcesStack.hasNext()) {
                            resource = itResourcesStack.next();			
                            if(resource instanceof NoPreemptiveResource) {
				priorityCeiling = ((NoPreemptiveResource) resource)
						.getCeilingPriority();
				if (maxPriority < priorityCeiling) {
					maxPriority = priorityCeiling;
				}
                            }
			}
			pcb.setActivePriority(maxPriority);
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
		    }
	    } 
      }	
	
	
	/**
	* It releases the resources of the current PCB, if any. 
	*/      
	protected void resource_release_event() { 
	      if (eventTable[RELEASE_RESOURCE] == 0) {
		      eventTable[RELEASE_RESOURCE] = pcbCurrent.firstReleaseTime();
		      releaseResource(pcbCurrent);
// 		      process_termination_event();
// 		      if (pcbCurrent != null) {
// 			      PCB lastRun = pcbCurrent;
// 			      pcbCurrent = null;
// 			      resetProcessEVT();
// 			      schedulingPolicy.insert(lastRun);
// 		      }
	      }
	}	
	
}
