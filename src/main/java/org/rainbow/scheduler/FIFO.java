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
 * File: FIFO.java
 * Package: scheduler
 * Author: Marin Pier Giorgio, Dalle Pezze Piero
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
 * This class implements a First In First Out policy.
 * 
 * @author Marin Pier Giorgio
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class FIFO implements Batch {

	/**
	 * To serializable
	 */
	private static final long serialVersionUID = -5000L;

	/**
	 * The ready queue. The behaviour is FIFO.
	 */
	protected LinkedList<PCB> readyQueue;

	/**
	 * The refered dispatcher.
	 */
	protected ProcessDispatcherInterface dispatcher = null;

	/**
	 * It creates the FIFO policy.
	 */
	public FIFO() {
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
	 * It inserts the process in tail of the queue.
	 * 
	 * @param ready
	 *            The process just activated.
	 */
	public void insert(PCB ready) {
		readyQueue.add(ready);
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
		return "First In First Out";
	}

}
