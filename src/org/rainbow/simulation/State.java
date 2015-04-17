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
 * File: State.java
 * Package: simulation
 * Author: Stefano Bertolin, Piero Dalle Pezze
 * Date: 28/01/2006
 * Version: 1.2
 * 
 * Modifies:
 *  v1.2 (01/02/2007): English translation. Java6 compatible. (Piero Dalle Pezze)
 *  v1.1 (30/01/2006): Codify.
 *  v1.0 (28/01/2006): Documentation.
 */
package org.rainbow.simulation;

import java.util.*;
import org.rainbow.data.*;

/**
 * This class represents the inner scheduler state. Remember that the simulation
 * is discrete.
 * 
 * @author Bertolin Stefano
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class State {

	/**
	 * The duration of the state.
	 */
	private int duration;

	/**
	 * A flag to notify if there is a deadlock.
	 */
	private boolean deadlock;

	/**
	 * The current attributions of the resources to processes.
	 */
	private ArrayList<ResourceAttribution> attributedResources;

	/**
	 * The running process.
	 */
	private SimulatedProcess running = null;

	/**
	 * A flag to notify if there is a priority inversion.
	 */
	private boolean priorityInversion = false;

	/**
	 * A flag to notify if there is a ceiling priority violation.
	 */
	private boolean ceilingPriority = false;

	/**
	 * The ready queue.
	 */
	private ArrayList<SimulatedProcess> readyQueue;

	/**
	 * The current queues of blocked processes.
	 */
	private ArrayList<ResourceAttribution> blockedQueues;

	/**
	 * The list of terminated processes.
	 */
	private ArrayList<SimulatedProcess> terminatedProcesses;

	/**
	 * It creates an inner scheduler state.
	 * 
	 * @param duration
	 *            The duration of this state.
	 */
	public State(int duration) {
		if (duration > 0)
			this.duration = duration;
		else
			this.duration = 1;
	}

	/**
	 * It returns true there is a deadlock, false otherwise.
	 * 
	 * @return true if there is a deadlock, false otherwise
	 */
	public boolean getDeadlock() {
		return deadlock;
	}

	/**
	 * It returns true if the ceiling priority is violated.
	 * 
	 * @return true if the ceiling priority is violated.
	 */
	public boolean getCeilingPriorityViolation() {
		return ceilingPriority;
	}

	/**
	 * It setts a violation of ceiling priority.
	 */
	public void setCeilingPriorityViolation(boolean ceilingPriorityViolation) {
		this.ceilingPriority = ceilingPriorityViolation;
	}

	/**
	 * It returns true if the ceiling priority is violated.
	 * 
	 * @return true if the ceiling priority is violated.
	 */
	public boolean getPriorityInversion() {
		return priorityInversion;
	}

	/**
	 * It setts true if there is a priority inversion.
	 */
	public void setPriorityInversion(boolean priorityInversion) {
		this.priorityInversion = priorityInversion;
	}

	/**
	 * It setts if there is a deadlock or not.
	 * 
	 * @param deadlock
	 *            true if there is a deadlock, false otherwise.
	 */
	public void setDeadlock(boolean deadlock) {
		this.deadlock = deadlock;
	}

	/**
	 * It returns the current attribution of the resources.
	 * 
	 * @return the current attribution of the resources.
	 */
	public ArrayList<ResourceAttribution> getAttributedResources() {
		return attributedResources;
	}

	/**
	 * It sets the attibuted resorses.
	 * 
	 * @param attributedResources
	 *            Lists of attributions
	 */
	public void setAttributedResources(
			ArrayList<ResourceAttribution> attributedResources) {
		this.attributedResources = attributedResources;
	}

	/**
	 * It returns the running process.
	 * 
	 * @return The running process.
	 */
	public SimulatedProcess getRunning() {
		return running;
	}

	/**
	 * It sets the running process.
	 * 
	 * @param running
	 *            The running process.
	 */
	public void setRunning(SimulatedProcess running) {
		this.running = running;
	}

	/**
	 * It returns the ready queue.
	 * 
	 * @return The ready queue.
	 */
	public ArrayList<SimulatedProcess> getReady() {
		return readyQueue;
	}

	/**
	 * It sets the ready queue.
	 * 
	 * @param readyQueue
	 *            The ready queue.
	 */
	public void setReady(ArrayList<SimulatedProcess> readyQueue) {
		this.readyQueue = readyQueue;
	}

	/**
	 * It returns the list of the terminated processes.
	 * 
	 * @return The list of the terminated processes.
	 */
	public ArrayList<SimulatedProcess> getTerminated() {
		return terminatedProcesses;
	}

	/**
	 * It sets the list of the terminated processes.
	 * 
	 * @param terminatedProcesses
	 *            The terminated processes.
	 */
	public void setTerminated(ArrayList<SimulatedProcess> terminatedProcesses) {
		this.terminatedProcesses = terminatedProcesses;
	}

	/**
	 * It returns a list of queues that contain blocked processes.
	 * 
	 * @return A list of queues that contain blocked processes.
	 */
	public ArrayList<ResourceAttribution> getBlockedQueues() {
		return blockedQueues;
	}

	/**
	 * It sets the queues of the blocked processes.
	 * 
	 * @param blockedQueues
	 *            A list of queues that contain blocked processes.
	 */
	public void setBlocked(ArrayList<ResourceAttribution> blockedQueues) {
		this.blockedQueues = blockedQueues;
	}

	/**
	 * It returns the duration of the state.
	 * 
	 * @return The duration of the state.
	 */
	public int getDuration() {
		return duration;
	}
}
