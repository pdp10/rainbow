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
 * File: Configuration.java
 * Package: data
 * Author: Bertolin Stefano, Dalle Pezze Piero
 * Date: 31/01/2006
 * Version: 1.2
 * 
 * Modifies:
 * v.1.2 (31/01/2007): English translation. Java6 compatible. (Piero Dalle Pezze)
 * v.1.1 (01/02/2006): Class codify.
 * v.1.0 (31/01/2006): Documentation.
 */

package org.rainbow.data;

import java.io.*;
import java.util.*;

/**
 * This class represents an user's configuration.
 * 
 * @author Bertolin Stefano
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class Configuration implements Serializable {

	/**
	 * To serialize
	 */
	private static final long serialVersionUID = -100L;

	/**
	 * The assignment policy of the resources.
	 */
	private String assignmentPolicy;

	/**
	 * The scheduling policy.
	 */
	private String schedulingPolicy;

	/**
	 * The timeslice for scheduling algorithms with time-sharing.
	 */
	private int timeslice = -1;

	/**
	 * A flag to notify if there the immediate ceiling priority protocol.
	 */
	private boolean ICPP = false;

	/**
	 * A list of processes.
	 */
	private ArrayList<SimulatedProcess> processes = new ArrayList<SimulatedProcess>();

	/**
	 * A list of resources.
	 */
	private ArrayList<Resource> resources = new ArrayList<Resource>();

	/**
	 * It creates a configuration.
	 */
	public Configuration() {
	}

	/**
	 * It creates a configuration.
	 * 
	 * @param assignmentPolicy
	 *            The name of the assignment policy of the resources.
	 * @param schedulingPolicy
	 *            The name of the scheduling policy.
	 * @param processes
	 *            The list of processes.
	 * @param resources
	 *            The list of resources.
	 */
	public Configuration(String assignmentPolicy, String schedulingPolicy,
			ArrayList<SimulatedProcess> processes, ArrayList<Resource> resources) {
		this.assignmentPolicy = assignmentPolicy;
		this.schedulingPolicy = schedulingPolicy;
		this.processes = processes;
		this.resources = resources;
	}

	/**
	 * It creates a configuration.
	 * 
	 * @param assignmentPolicy
	 *            The name of the assignment policy of the resources.
	 * @param schedulingPolicy
	 *            The name of the scheduling policy.
	 * @param processes
	 *            The list of processes.
	 * @param resources
	 *            The list of resources.
	 * @param timeslice
	 *            The time slice for time-sharing scheduling policy.
	 */
	public Configuration(String assignmentPolicy, String schedulingPolicy,
			ArrayList<SimulatedProcess> processes,
			ArrayList<Resource> resources, int timeslice) {
		this(assignmentPolicy, schedulingPolicy, processes, resources);
		this.timeslice = timeslice;
	}

	/**
	 * It returns the assignment policy of the resources.
	 * 
	 * @return the assignment policy.
	 */
	public String getAssignmentPolicy() {
		return assignmentPolicy;
	}

	/**
	 * It returns true user wants the immediate ceiling priority protocol.
	 * 
	 * @return true if user wants the immediate ceiling priority protocol.
	 */
	public boolean isICPP() {
		return ICPP;
	}

	/**
	 * It setts true if user wants the immediate ceiling priority protocol.
	 */
	public void setICPP(boolean ICPP) {
		this.ICPP = ICPP;
	}

	/**
	 * It sets the assignment policy of the resources.
	 * 
	 * @param assignmentPolicy
	 *            The name of the assignment policy.
	 */
	public void setAssignmentPolicy(String assignmentPolicy) {
		this.assignmentPolicy = assignmentPolicy;
	}

	/**
	 * It returns the scheduling policy.
	 * 
	 * @return The scheduling policy.
	 */
	public String getSchedulingPolicy() {
		return schedulingPolicy;
	}

	/**
	 * It sets the scheduling policy.
	 * 
	 * @param schedulingPolicy
	 *            The scheduling policy.
	 */
	public void setSchedulingPolicy(String schedulingPolicy) {
		this.schedulingPolicy = schedulingPolicy;
	}

	/**
	 * It returns the timeslice value. (-1) if the scheduling policy is no a
	 * time-sharing policy.
	 * 
	 * @return Timeslice.
	 */
	public int getTimeslice() {
		return timeslice;
	}

	/**
	 * It sets the timeslice value. (Only for time-sharing scheduling policy.
	 * 
	 * @param timeslice
	 *            Timeslice.
	 */
	public void setTimeslice(int timeslice) {
		this.timeslice = timeslice;
	}

	/**
	 * It returns a list of processes.
	 * 
	 * @return A list of processes.
	 */
	public ArrayList<SimulatedProcess> getProcesses() {
		return processes;
	}

	/**
	 * It sets a list of processes.
	 * 
	 * @param processes
	 *            A vector of processes.
	 */
	public void setProcesses(ArrayList<SimulatedProcess> processes) {
		this.processes = processes;
	}

	/**
	 * It returns a list of resources.
	 * 
	 * @return A list of resources.
	 */
	public ArrayList<Resource> getResources() {
		return resources;
	}

	/**
	 * It sets a list of resources.
	 * 
	 * @param resources
	 *            A vector of resources.
	 */
	public void setResources(ArrayList<Resource> resources) {
		this.resources = resources;
	}

}
