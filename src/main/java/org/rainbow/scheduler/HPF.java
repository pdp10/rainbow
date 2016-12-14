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
 * File: HPF.java
 * Package: scheduler
 * Author: Dalle Pezze Piero
 * Date: 03/02/2006
 * Version: 1.1
 * 
 * Modifies:
 *  - v.1.2 (13/11/2014): Added iterators.    
 *  - v.1.1 (31/01/2007): English translation. Java6 compatible. (Piero Dalle Pezze)
 *  - v.1.0 (03/02/2006): Codify and documentation.
 */
package org.rainbow.scheduler;

import java.util.*;

import org.rainbow.simulation.*;
import org.rainbow.data.*;

/**
 * This class implements the Highest Priority First scheduling policy. The index
 * of the ArrayList represents the priority. This policy extracts the process
 * with the highest priority (the queue with the highest index). This variant is
 * not preemptive.
 * 
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class HPF implements Interactive {

	/**
	 * To serializable
	 */
	private static final long serialVersionUID = -5002L;

	/**
	 * The ready queue. For each index of the ArrayList there is a FIFO queue.
	 * The index of the ArrayList represents the priority. This policy extracts
	 * the process with the highest priority (the queue with the highest index).
	 */
	protected HashMap<Integer, LinkedList<PCB>> readyQueue;

	/**
	 * The refered dispatcher.
	 */
	protected ProcessDispatcherInterface dispatcher = null;

	/**
	 * The minimum and the maximum priority available.
	 */
	protected int minPriority, maxPriority;

	/**
	 * It creates the Highest Priority First scheduling policy.
	 * 
	 * @param minPriority
	 *            The minimum priority.
	 * @param maxPriority
	 *            The maximum priority.
	 */
	public HPF(int minPriority, int maxPriority) {
		this.minPriority = minPriority;
		this.maxPriority = maxPriority;
		readyQueue = new HashMap<Integer, LinkedList<PCB>>(maxPriority
				- minPriority + 1);
		for (int i = minPriority; i <= maxPriority; i++) {
			readyQueue.put(i, new LinkedList<PCB>());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setProcessDispatcherInterface(
			ProcessDispatcherInterface dispatcher) {
		this.dispatcher = dispatcher;
	}

	/**
	 * {@inheritDoc}
	 */
	public State execute(int time) {
		return dispatcher.increaseSchedulerTime(time);
	}

	/**
	 * It inserts the yield the queue sorted by priority decreasing. So more
	 * high is the priority, more important is the process.
	 * 
	 * @param ready
	 *            The process just activated.
	 */
	public void insert(PCB ready) {
		readyQueue.get(ready.getActivePriority()).addLast(ready);
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
			 * IMPORTANT NOTICE: THIS IS NOT CLEAN! ArrayList ids templated with
			 * SimulatedProcess object. So it is impossible to add a particular
			 * message as the following one. Here it is necessary to print the
			 * queues otherwise the user don't understand the behaviour of the
			 * this policy. The processes here created are only used to print
			 * the sentence "Queue with priority i-th", NOT for other cases.
			 */
			ready.add(new SimulatedProcess(("Queue with priority " + i), -1,
					-1, 1));
			Iterator<PCB> itTmp = tmp.iterator();
			while (itTmp.hasNext()) {
				ready.add(itTmp.next().getSimulatedProcess());
			}
		}
		return ready;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "Highest Priority First";
	}

}
