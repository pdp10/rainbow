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
 * File: HRRN.java
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

import org.rainbow.data.*;
import org.rainbow.simulation.*;

/**
 * This class implements the scheduling algorithm HRRN (Highest Response Ratio
 * Next). </br>The response ratio of a process is: </br> response ratio =
 * (exptected execution time + waiting time) / expected execution time. </br>The
 * waiting time and the response ratio are computed for each extraction. It is
 * extracted the process having highest response ratio. This policy is fairness.
 * 
 * @author Piero Dalle Pezze
 * @version 1.2
 */
public class HRRN implements Interactive {

	/**
	 * To serializable
	 */
	private static final long serialVersionUID = -5004L;

	/**
	 * The ready queue. The behaviour is FIFO.
	 */
	protected LinkedList<PCB> readyQueue;

	/**
	 * The refered dispatcher.
	 */
	protected ProcessDispatcherInterface dispatcher = null;

	/**
	 * It creates the HRRN policy.
	 */
	public HRRN() {
		readyQueue = new LinkedList<PCB>();
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
	 * It inserts the process in tail of the queue. Note: it is irrelevant the
	 * insertion position.
	 * 
	 * @param ready
	 *            The process just activated.
	 */
	public void insert(PCB ready) {
		readyQueue.add(ready);
	}

	/**
	 * It is extracted the process having highest response ratio. The response
	 * ratio of a process is defined so: </br> response_ratio =
	 * (expected_execution_time + waiting_time) / expected_execution_time.
	 * 
	 * @return the process having the highest response ratio.
	 */
	public PCB extract() {
		if (readyQueue.size() > 0) {
			/* rr means response ratio */
			float maxRR = 0.0f, rr = 0.0f;
			int j = 0;
			PCB pcb = null;
			/* Search the process having the highest response ratio */
			ListIterator<PCB> itReadyQueue = readyQueue.listIterator();
			while (itReadyQueue.hasNext()) {
				pcb = itReadyQueue.next();
				/* compute the response-ratio of the process i-th. */
				rr = (float) (pcb.getRemainingTime()
						+ dispatcher.getCurrentTime() - pcb
						.getSimulatedProcess().getActivationTime())
						/ pcb.getRemainingTime();
				if (maxRR < rr) {
					maxRR = rr;
					j = itReadyQueue.previousIndex();
				}
			}
			return readyQueue.remove(j);
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
		return "Highest Remaining Ratio Next";
	}

}
