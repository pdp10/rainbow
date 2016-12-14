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
 * File: MF.java
 * Package: scheduler
 * Author: Piero Dalle Pezze
 * Date: 29/01/2006
 * Version: 1.2
 * 
 * Modifies:
 * v1.3 (13/11/2014): Added iterators.    
 * v1.2 (04/02/2007): English translation. Java6 compatible.
 * v1.1 (30/01/2006): Class codify.
 * v1.0 (29/01/2006): Class documentation.
 */
package org.rainbow.scheduler;

import java.util.*;

import org.rainbow.data.*;
import org.rainbow.simulation.*;

/**
 * This class implements the Feedback Multilevel scheduling policy which is time
 * sharing. A just activated process is added in the first queue (C0) using a
 * round robin scheduling. After it is executed (after the quantum is elapsed),
 * it is put in the second queue (C1), and so on. The process that is extracted,
 * is the first of the queue with the lower index, if it exists.
 * 
 * @author Dalle Pezze Piero
 * @version 1.3
 */
public class MF extends TimeSharing implements Interactive {

	/**
	 * To serializable
	 */
	private static final long serialVersionUID = -5006L;

	/**
	 * The vector containing the queues.
	 */
	protected ArrayList<LinkedList<PCB>> vectorQueues;

	/**
	 * Counter. The key is the process id, the value is the times that the
	 * process was ready.
	 */
	protected TreeMap<Integer, Integer> readyCNT;

	/**
	 * The last running process
	 */
	protected PCB lastRun = null;

	/**
	 * The number of levels.
	 */
	protected int levels = 10;

	/**
	 * The reference to the dispatcher.
	 */
	protected ProcessDispatcherInterface dispatcher = null;

	/**
	 * It creates a multilevel feedback policy.
	 * 
	 * @param timeSlice
	 *            The time slice value.
	 * @param level
	 *            The number of levels of the multilevel feedback policy.
	 */
	public MF(int timeSlice, int level) {
		super(timeSlice);
		this.levels = level;
		vectorQueues = new ArrayList<LinkedList<PCB>>(levels);
		readyCNT = new TreeMap<Integer, Integer>();
		/* It adds the C0 queue. */
		vectorQueues.add(new LinkedList<PCB>());
	}

	/**
	 * {@inheritDoc}
	 */
	public void setProcessDispatcherInterface(
			ProcessDispatcherInterface dispatcher) {
		this.dispatcher = dispatcher;
	}

	/**
	 * It inserts the process in tail of the queue i-th if before it was ready
	 * in the queue (i-1)-th, for i=1..levels
	 * 
	 * @param ready
	 *            The process just activated.
	 */
	public void insert(PCB ready) {
		if (dispatcher.getPCBTable().size() > readyCNT.size()) {
			// It is the first time that the process is ready.
			readyCNT.put(ready.getSimulatedProcess().getId(), new Integer(0));
			vectorQueues.get(0).addLast(ready);
		} else {
			// the process was ready before.
			Integer readyId = ready.getSimulatedProcess().getId();
			int indexQueue = readyCNT.get(readyId).intValue();
			// setting of the index of queue for ready
			if (indexQueue + 1 >= vectorQueues.size()) {
				if (vectorQueues.size() < levels) {
					// I create a new queue
					vectorQueues.add(new LinkedList<PCB>());
					/* Setting of the NEW index of queue for ready. */
					readyCNT.remove(readyId);
					readyCNT.put(readyId, new Integer(indexQueue + 1));
				}
				/* It adds ready in the last queue created. */
				vectorQueues.get(vectorQueues.size() - 1).addLast(ready);
			} else {
				/* Setting of the NEW index of queue for ready. */
				readyCNT.remove(readyId);
				readyCNT.put(readyId, new Integer(indexQueue + 1));
				/*
				 * It adds ready in the queue with the following index
				 * (indexQueue + 1)
				 */
				vectorQueues.get(indexQueue + 1).addLast(ready);
			}
		}
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
		if (running.getRemainingTime() == 0) {
			// remove its id in the readyCNT.
			readyCNT.remove(running.getSimulatedProcess().getId());
		} else if (getTick() == getTimeSlice()) {
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
	 * It returns the first process of the queue with the lower index.
	 * 
	 * @return a process.
	 */
	public PCB extract() {
		PCB extracted = null;
		boolean found = false;
		LinkedList<PCB> l = null;
		Iterator<LinkedList<PCB>> itVectorQueues = vectorQueues.iterator();
		while (itVectorQueues.hasNext() && !found) {
			l = itVectorQueues.next();
			if (l.size() != 0) {
				extracted = l.removeFirst();
				found = true;
			}
		}
		return extracted;
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		int size = 0;
		Iterator<LinkedList<PCB>> itVectorQueues = vectorQueues.iterator();
		while (itVectorQueues.hasNext()) {
			size += itVectorQueues.next().size();
		}
		return size;
	}

	/**
	 * {@inheritDoc}
	 */
	public ArrayList<SimulatedProcess> getReadyQueue() {
		// see later why vectorQueues.size()
		ArrayList<SimulatedProcess> ready = new ArrayList<SimulatedProcess>(
				size() + vectorQueues.size());
		LinkedList<PCB> l = null;
		ListIterator<LinkedList<PCB>> itVectorQueues = vectorQueues
				.listIterator();
		while (itVectorQueues.hasNext()) {
			l = itVectorQueues.next();
			/*
			 * IMPORTANT NOTICE: THIS IS NOT CLEAN! ArrayList ids templated with
			 * SimulatedProcess object. So it is impossible to add a particular
			 * message as the following one. Here it is necessary to print the
			 * queues otherwise the user don't understand the behaviour of the
			 * this policy. The processes here created are only used to print
			 * the sentence "Queue with priority i-th", NOT for other cases.
			 */
			ready.add(new SimulatedProcess("Queue of level "
					+ itVectorQueues.previousIndex(), -1, -1, 1));
			Iterator<PCB> itL = l.iterator();
			while (itL.hasNext()) {
				ready.add(itL.next().getSimulatedProcess());
			}
		}
		return ready;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "Multilevel Feedback";
	}

}
