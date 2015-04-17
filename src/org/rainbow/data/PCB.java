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
 * File: PCB.java
 * Package: data
 * Author: Michele Perin, Piero Dalle Pezze
 * Date: 31/01/2006
 * Version: 1.2
 * 
 * Modifies:
 * v.1.2 (31/01/2007): English translation. Java6 compatible. (Piero Dalle Pezze)
 * v.1.1 (01/02/2006): Class codify.
 * v.1.0 (31/01/2006): Class documentation.
 */

package org.rainbow.data;

import java.util.*;

/**
 * This class represents a process control block (PCB), that stores all dynamic
 * data of a process.
 * 
 * @author Michele Perin
 * @author Piero Dalle Pezze
 * @version 1.2
 */
public class PCB {


	/**
	 * The refered process.
	 */
	private SimulatedProcess process;

	/**
	 * It returns the process associated to this PCB.
	 * 
	 * @return the process associated to this PCB.
	 */
	public SimulatedProcess getSimulatedProcess() {
		return process;
	}

	/**
	 * The actual executed time of the process.
	 */
	private int executedTime = 0;

	/**
	 * It returns the executed time of the process.
	 * 
	 * @return the executed time of the process.
	 */
	public int getExecutedTime() {
		return executedTime;
	}

	/**
	 * The execution remaining time of the process.
	 */
	private int remainingTime;

	/**
	 * It returns the execution remaining time of the process.
	 * 
	 * @return the execution remainig time of the process. If the process has
	 *         finished its execution, it returns 0.
	 */
	public int getRemainingTime() {
		return remainingTime;
	}

	/**
	 * It increases the time executed by the process of a value specified in
	 * time variable.
	 * 
	 * @param time
	 *            the value to increase.
	 */
	public void increaseExecutedTime(int time) {
		executedTime = executedTime + time;
		remainingTime = remainingTime - time;
		/*
		 * It decreases the remaining time of the access to the resources used
		 * by the process.
		 */
		for (int i = 0; i < usedResources.size(); i++) {
			(usedResources.get(i)).decreaseRemainingTime(time);
		}
	}

	/**
	 * It creates an instance of PCB.
	 * 
	 * @param process
	 *            The process associates to this PCB.
	 */
	public PCB(SimulatedProcess process) {
		this.process = process;
		remainingTime = process.getExecutionTime();
		activePriority = process.getInitialPriority();
	}

	/**
	 * The next resource requested.
	 */
	private Resource resource = null;

	/**
	 * It store the access duration to the above resource.
	 */
	private Integer accessDuration = null;

	/**
	 * A list of dynamic uses of resources wanted.
	 */
	private LinkedList<UseResource> usedResources = new LinkedList<UseResource>();

	/**
	 * It returns the list of the resource actually used by the process.
	 * 
	 * @return a stack of resources currently used by the process.
	 */
	public LinkedList<Resource> getUsedResources() {
		LinkedList<Resource> resourcesStack = new LinkedList<Resource>();
		/*
		 * Extract and add only the Resource.
		 */
		for (int i = 0; i < usedResources.size(); i++) {
			resourcesStack.push(usedResources.get(i).getResource());
		}
		return resourcesStack;
	}

	/**
	 * This store the next resource that the process need to access.
	 */
	private int nextRequest = 0;

	/**
	 * It returns the reference to the next resource at a point of the process
	 * execution. If there is not any resource, it returns null.
	 * 
	 * @return the next resource if it exists. null otherwise.
	 */
	public Resource getResource() {
		if (resource != null) {
			return resource;
		}
		/*
		 * I get the next access (nextRequest mantain this value) I control if
		 * now is the time to access to that resource.
		 */
		ArrayList<Access> accessesList = process.getAccessesList();
		if (nextRequest < accessesList.size()
				&& (accessesList.get(nextRequest)).getRequestTime() == executedTime) {
			Access access = accessesList.get(nextRequest);
			resource = access.getResource();
			accessDuration = new Integer(access.getDuration());
			return resource;
		}
		/* No next request */
		return null;
	}

	/**
	 * Signal to the PCB that the resource is given to the process.
	 */
	public void resourceAttribuited() {
		usedResources.add(new UseResource(resource, accessDuration.intValue()));
		resource = null;
		accessDuration = null;
		nextRequest++;
	}

	/**
	 * It returns the resources that is released.
	 * 
	 * @return a stack of resources.
	 */
	public Resource getReleasedResource() {
		Resource r = null;
                boolean found = false;
		// Extract the resource with remainingTime = 0
		for (int i = 0; i < usedResources.size() && !found; i++) {
			if (usedResources.get(i).getRemainingTime() == 0) {
				r = usedResources.remove(i).getResource();
                                found = true;
			}
		}
		return r;
	}

	/**
	 * It returns the stack of the preemptive resources.
	 * 
	 * @return a stack of resources.
	 */
	public LinkedList<Resource> getPreemptiveResources() {
		LinkedList<Resource> preemptiveResourcesStack = new LinkedList<Resource>();
		for (int i = 0; i < usedResources.size(); i++) {
			if (usedResources.get(i).getResource() instanceof PreemptiveResource) {
				preemptiveResourcesStack.push(usedResources.get(i).getResource());
			}
		}
		return preemptiveResourcesStack;
	}

	/**
	 * It returns true if they are associated to processes having the same id,
	 * false otherwise.
	 * 
	 * @param pcb
	 *            the second pcb.
	 * @return true if they are associated to processes having the same id,
	 *         false otherwise.
	 */
	public boolean equals(Object pcb) {
		if (pcb instanceof PCB) {
			PCB pcb1 = (PCB) pcb;
			return process.equals(pcb1.process);
		}
		return false;
	}

	/**
	 * It returns the time of the next access request.
	 * 
	 * @return the time of the next access request. -1 if there are no requests.
	 */
	public int nextRequestTime() {
		ArrayList<Access> accessesList = process.getAccessesList();
		if (nextRequest < accessesList.size()) {
			return accessesList.get(nextRequest).getRequestTime()
					- executedTime;
		}
		return -1;
	}

	/**
	 * It returns the time of the first resource release.
	 * 
	 * @return the time of the first resource release, if exists. -1 otherwise.
	 */
	public int firstReleaseTime() {
		int tr;
		/*
		 * I search the minimum remaining time of access to the used resources.
		 */
		if (usedResources.size() != 0) {
			tr = usedResources.get(0).getRemainingTime();
			// searching
			for (int i = 1; i < this.usedResources.size(); i++) {
				int temp = usedResources.get(i).getRemainingTime();
				if (temp < tr)
					tr = temp;
			}
		} else
			tr = -1; // no attribuited resource
		return tr;
	}

	/**
	 * The active priority of the process.
	 */
	private int activePriority;

	/**
	 * It returns the active priority of the process.
	 * 
	 * @return The active priority of the process.
	 */
	public int getActivePriority() {
		return activePriority;
	}

	/**
	 * It sets a new priority to the process. This is the method to invoke when
	 * you want to change the priority of the process.
	 * 
	 * @param priority
	 *            The new priority of the process.
	 */
	public void setActivePriority(int priority) {
		activePriority = priority;
	}

}
