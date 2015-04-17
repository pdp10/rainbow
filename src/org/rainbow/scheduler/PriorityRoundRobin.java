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
 * File: TimeSharing.java
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
 * This class implements a round robin scheduling algorithm using multiple
 * queues sorted by priority. Every queue is sorted using the round robin
 * algorithm. Different queues are sorted with priority decreasing. This version
 * is not preemptive.
 * 
 * 
 * @author Marin Pier Giorgio
 * @author Dalle Pezze Piero
 * @version 1.3
 */
public class PriorityRoundRobin extends TimeSharing implements Interactive {

	/**
	 * To serializable
	 */
	private static final long serialVersionUID = -5013L;

	/**
	 * The ready queue. (Multiple queues)
	 */
	protected HashMap<Integer, LinkedList<PCB>> readyQueue;

	/**
	 * The refered dispatcher.
	 */
	protected ProcessDispatcherInterface dispatcher = null;
        
        /**
	 * The reference to the last running process.
	 */
	protected PCB lastRun = null;
        
	/**
	 * The minimum and the maximum priority available.
	 */
	protected int minPriority, maxPriority;

	/**
	 * {@inheritDoc}
	 */
	public void setProcessDispatcherInterface(ProcessDispatcherInterface dispatcher) {
		this.dispatcher = dispatcher;
	}

	/**
	 * It creates a round robin object with the selected time slice and a number
	 * of priority. Note that minPriority must be < than maximum priority.
	 * 
	 * @param timeSlice
	 *            The value of the time slice.
	 * @param minPriority
	 *            The minimum priority.
	 * @param maxPriority
	 *            The maximum priority.
	 */
	public PriorityRoundRobin(int timeSlice, int minPriority, int maxPriority) {
		super(timeSlice);
		this.minPriority = minPriority;
		this.maxPriority = maxPriority;
		readyQueue = new HashMap<Integer, LinkedList<PCB>>(maxPriority
				- minPriority + 1);
		for (int i = minPriority; i <= maxPriority; i++) {
			readyQueue.put(new Integer(i), new LinkedList<PCB>());
		}
	}

	/**
	 * It adds a ready process in the queue of its priority.
	 * 
	 * @param ready
	 *            The ready process to add to the ready queue.
	 */
	public void insert(PCB ready) {
		readyQueue.get(ready.getActivePriority()).add(ready);
	}

	/**
	 * Extract the process from the head of the queue.
	 * 
	 * @return the process that must execute.
	 */
	public PCB extract() {
		int index = minPriority - 1;
		for (int key = maxPriority; key >= minPriority
				&& index == minPriority - 1; key--) {
			if (readyQueue.get(key).size() > 0) {
				index = key;
			}
		}
		if (index != minPriority - 1) {
			return readyQueue.get(index).removeFirst();
		}
		return null;
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
	 * {@inheritDoc}
	 */
	public int size() {
		int size = 0;
		for (int i = minPriority; i <= maxPriority; i++) {
			// sum of the sizes
			size = size + readyQueue.get(i).size();
		}
		return size;
	}

	/**
	 * {@inheritDoc}
	 */
	public ArrayList<SimulatedProcess> getReadyQueue() {
		ArrayList<SimulatedProcess> ready = new ArrayList<SimulatedProcess>(
				size() + readyQueue.size());
		for (int i = maxPriority; i >= minPriority; i--) {
			LinkedList<PCB> tmp = readyQueue.get(i);
			/*
			 * IMPORTANT NOTICE: THIS IS NOT CLEAN! ArrayList is templated with
			 * SimulatedProcess object. So it is impossible to add a particular
			 * message as the following one. Here it is necessary to print the
			 * queues otherwise the user don't understand the behaviour of the
			 * this policy. The processes here created are only used to print
			 * the sentence "Queue with priority i-th", NOT for other cases.
			 */
			ready.add(new SimulatedProcess("Queue with priority " + i, -1, -1, 1));
			Iterator<PCB> itTmp = tmp.iterator();
			while(itTmp.hasNext()) {
				ready.add(itTmp.next().getSimulatedProcess());
			}
		}
		return ready;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "Priority Round Robin";
	}

}
