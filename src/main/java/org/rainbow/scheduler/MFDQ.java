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
 * File: MFDQ.java
 * Package: scheduler
 * Author: Piero Dalle Pezze
 * Date: 29/01/2006
 * Version: 1.2
 * 
 * Modifies:
 * v1.2 (20/02/2007): English translation. Java6 compatible. (Dalle Pezze Piero)
 * v1.1 (30/01/2006): Class codify.
 * v1.0 (29/01/2006): Class documentation.
 */
package org.rainbow.scheduler;

import org.rainbow.data.*;
import org.rainbow.simulation.*;

/**
 * This is an implementation of the Multilevel Feedback with dynamic quantum
 * policy. Each level has a different quantum value. For a process point of
 * view, it has a dynamic quantum each times it is added to the ready queue. On
 * details: <br/>
 * <ul>
 * <li>queue of level 0 : dynamic_time_slice = time_slice</li>
 * <li>queue of level i-th: dynamic_time_slice(i) = 2 * dynamic_time_slice(i-1)</li>
 * </ul>
 * 
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class MFDQ extends MF {

	/**
	 * To serializable
	 */
	private static final long serialVersionUID = -5008L;

	/**
	 * It is the dynamic time slice.
	 */
	protected int dynamicTimeSlice;

	/**
	 * It creates a multilevel feedback with dynamic quantum policy.
	 * 
	 * @param timeSlice
	 *            The time slice value.
	 * @param level
	 *            The number of levels of the multilevel feedback policy.
	 */
	public MFDQ(int timeSlice, int level) {
		super(timeSlice, level);
		dynamicTimeSlice = timeSlice;
	}

	/**
	 * It executes the process for a time which is equal to the dynamic quantum,
	 * instead of the ordinary quantum
	 * 
	 * @param time
	 *            the time at most to execute.
	 * 
	 * @return the state of the process.
	 */
	public State execute(int time) {
		State s;
		PCB running = dispatcher.getPCBCurrent();
		if (!(running.equals(lastRun))) {
			// The last running process is different by this one.
			// resetting of the counter.
			this.reset();
		}
		// It computes the next first event (the minor between time and the
		// preemption)
		int executionTime, remainingTime = getDynamicTimeSlice() - getTick();
		if (remainingTime <= time) {
			executionTime = remainingTime;
		} else {
			executionTime = time;
		}
		// increasing of the counter
		setTick(getTick() + executionTime);
		// executing of the process
		s = dispatcher.increaseSchedulerTime(executionTime);
		if (running.getRemainingTime() == 0) {
			// remove its id in the readyCNT.
			readyCNT.remove(running.getSimulatedProcess().getId());
		} else if (getTick() == getDynamicTimeSlice()) {
			// preemption
			dispatcher.removePCBCurrent();
			insert(running);
			reset();
		} else {
			// remember the last run process
			lastRun = running;
		}
		return s;
	}

	/**
	 * It always extracts the process in the queue with minor index if it
	 * exists.
	 * 
	 * @return the process to run.
	 */
	public PCB extract() {
		PCB extract = super.extract();
		if (extract != null) {
			/* It sets the dynamic quantum for the extracted process. */
			setDynamicTimeSlice(extract);
		}
		return extract;
	}

	/**
	 * It sets the dynamic quantum of the extracted process. On details: <br/>
	 * <ul>
	 * <li>queue of level 0 : dynamic_time_slice = time_slice</li>
	 * <li>queue of level i-th: dynamic_time_slice(i) = 2 *
	 * dynamic_time_slice(i-1)</li>
	 * </ul>
	 * 
	 * @param pcb
	 *            The pcb whom to compute the dynamic quantum.
	 */
	protected void setDynamicTimeSlice(PCB pcb) {
		/* It finds the index of the last ready queue of pcb */
		Integer readyId = pcb.getSimulatedProcess().getId();
		int indexQueue = readyCNT.get(readyId).intValue();
		int i = 1;
		/* It sets the base case */
		dynamicTimeSlice = getTimeSlice();
		while (i <= indexQueue) {
			/* dynamic_time_slice(i) = 2 * dynamic_time_slice(i-1) */
			dynamicTimeSlice = 2 * dynamicTimeSlice;
			i++;
		}
	}

	/**
	 * It returns the dynamic time slice of the extracted process.
	 * 
	 * @return the dynamic time slice of the extracted process.
	 */
	protected int getDynamicTimeSlice() {
		return dynamicTimeSlice;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "Multilevel Feedback Dynamic Quantum";
	}

}
