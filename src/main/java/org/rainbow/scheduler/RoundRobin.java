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
 * File: RoundRobin.java
 * Package: scheduler
 * Author: Marin Pier Giorgio, Piero Dalle Pezze
 * Date: 29/01/2006
 * Version: 1.2
 * 
 * Modifies:
 * v1.3 (13/11/2014): Added iterators.  
 * v1.2 (04/02/2007): English translation. Java6 compatible. (Dalle Pezze Piero)
 * v1.1 (30/01/2006): Class codify.
 * v1.0 (29/01/2006): Class documentation.
 */
package org.rainbow.scheduler;

import java.util.*;

import org.rainbow.data.*;
import org.rainbow.simulation.*;

/**
 * This class represents the round robin scheduling algorithm. The ready queue
 * is a circular list and the preemption acts on ready processes every a fixed
 * portion of time (called time slice or quantum).
 * 
 * @author Marin Pier Giorgio
 * @author Dalle Pezze Piero
 * @version 1.3
 */
public class RoundRobin extends TimeSharing implements Interactive {

	/**
	 * To serializable
	 */
	private static final long serialVersionUID = -5015L;

	/**
	 * The ready queue. The behaviour is FIFO (FCFS).
	 */
	protected LinkedList<PCB> readyQueue = new LinkedList<PCB>();

	/**
	 * The referred dispatcher.
	 */
	protected ProcessDispatcherInterface dispatcher = null;

	/**
	 * The reference to the last running process.
	 */
	protected PCB lastRun = null;

	/**
	 * It creates a round robin object with the selected time slice.
	 * 
	 * @param timeSlice
	 *            The value of the time slice.
	 */
	public RoundRobin(int timeSlice) {
		super(timeSlice);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setProcessDispatcherInterface(
			ProcessDispatcherInterface dispatcher) {
		this.dispatcher = dispatcher;
	}

	/**
	 * It inserts the process in tail of the queue.
	 * 
	 * @param ready
	 *            The process just activated.
	 */
	public void insert(PCB ready) {
		readyQueue.add(ready);
	}

	/**
	 * It runs the process in execution at most for a time passed as parameter.
	 * It is possible that the time slice is minor than the time value. In this
	 * case, the policy always considers the minor time. Therefore if the
	 * quantum is elapsed, the policy removes the running process from the cpu
	 * and inserts it on the bottom of the ready queue. This operation is called
	 * context switch.
	 * 
	 * @param time
	 *            The maximum time that the process can run.
	 * 
	 * @return the state of the dispatcher after having executed the process.
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
		int executionTime, remainingTime = getTimeSlice() - getTick();
		if (remainingTime <= time) {
			executionTime = remainingTime;
		} else {
			executionTime = time;
		}
		// increasing of the counter
		setTick(getTick() + executionTime);
		// executing of the process
		s = dispatcher.increaseSchedulerTime(executionTime);
		if (getTick() == getTimeSlice() && !(running.getRemainingTime() == 0)) {
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
	 * Extract the process from the head of the queue.
	 * 
	 * @return the process that must execute.
	 */
	public PCB extract() {
		if (readyQueue.size() > 0) {
			return readyQueue.removeFirst();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return readyQueue.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public ArrayList<SimulatedProcess> getReadyQueue() {
		ArrayList<SimulatedProcess> sp = new ArrayList<SimulatedProcess>(
				readyQueue.size());
		Iterator<PCB> itReadyQueue = readyQueue.iterator();
		while (itReadyQueue.hasNext()) {
			sp.add(itReadyQueue.next().getSimulatedProcess());
		}
		return sp;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "Round Robin";
	}

}
