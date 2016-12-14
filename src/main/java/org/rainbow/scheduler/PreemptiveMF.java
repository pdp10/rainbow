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
 * File: PreemptiveMF.java
 * Package: scheduler
 * Author: Piero Dalle Pezze
 * Date: 29/01/2006
 * Version: 1.4
 *
 * Modifies:
 * v1.4 (04/06/2007): Correct method insert.
 * v1.3 (15/05/2007): Correct method minor.
 * v1.2 (04/02/2007): English translation. Java6 compatible.
 * v1.1 (30/01/2006): Class codify.
 * v1.0 (29/01/2006): Class documentation.
 */
package org.rainbow.scheduler;

import org.rainbow.simulation.*;
import java.util.*;
import org.rainbow.data.*;

/**
 * This version is preemptive on the level of ready queue. A process of a lower
 * index of queue is more important than another one with a greater intex. A
 * running process which was inside a queue i-th will be preemptive by a process
 * of a queue j-th if j < i.
 *
 * @author Dalle Pezze Piero
 * @version 1.4
 */
public class PreemptiveMF extends MF implements Preemptive {

	/**
	 * To serializable
	 */
	private static final long serialVersionUID = -5010L;

	/**
	 * It is true if there is the preemption of the pcbCurrent, false otherwise.
	 */
	protected boolean preemption = false;

	public PreemptiveMF(int timeSlice, int level) {
		super(timeSlice, level);
	}

	/**
	 * It returns the process that is (was) in the queue with the highest index.
	 * This is the process less important.
	 *
	 * @param ready
	 *            The ready process.
	 * @param running
	 *            The running process.
	 * @return The process that is (was) in the queue with the highest index.
	 */
	public PCB minor(PCB ready, PCB running) {
		if (readyCNT.get(running.getSimulatedProcess().getId()).intValue() > readyCNT
				.get(ready.getSimulatedProcess().getId()).intValue()) {
			// preemption
			return running;
		}
		return ready;
	}

	/**
	 * {@inheritDoc} This version is preemptive.
	 */
	public void insert(PCB ready) {
		// IT INITIALIZES INDEXES
		if (readyCNT.get(ready.getSimulatedProcess().getId()) == null) {
			// ready is just actived. This is the first time that it becomes
			// ready.
			// initialize its readyCNT
			readyCNT.put(ready.getSimulatedProcess().getId(), new Integer(0));
		} else {
			// This is at least the second times that ready become ready again.
			// It adds ready in the ready queue of the correct level. (It was
			// setted at start)
			setNextIndex(ready);
		}
		// TEST PREEMPTION
		// Is a process running?
		PCB running = dispatcher.getPCBCurrent();
		if (running != null) {
			// A process is running
			// preemption??
			PCB min = minor(ready, running);
			if (running.equals(min)) {
				// preemption.
				preemption = true;
				dispatcher.preemptionPCBCurrent();
				// the running process now is ready.
				// setting of the index of queue for running
				// It adds running in the ready queue of the correct level.
				setNextIndex(running);
				Integer runningId = running.getSimulatedProcess().getId();
				int indexQueue = readyCNT.get(runningId).intValue();
				vectorQueues.get(indexQueue).addLast(running);
			}
		}
		Integer readyId = ready.getSimulatedProcess().getId();
		int indexQueue = readyCNT.get(readyId).intValue();
		vectorQueues.get(indexQueue).addLast(ready);
	}

	/**
	 * It sets the next index of queue for the ready process. This auxiliar
	 * procedure will be called by insert.
	 *
	 * @pcb The process that will be insert in the ready queue.
	 */
	private void setNextIndex(PCB ready) {
		Integer readyId = ready.getSimulatedProcess().getId();
		int indexQueue = readyCNT.get(readyId).intValue();
		if (indexQueue + 1 >= vectorQueues.size()) {
			// ready will be inserted in the queue with the highest index.
			if (vectorQueues.size() < levels) {
				indexQueue = indexQueue + 1;
				// I create a new queue
				vectorQueues.add(new LinkedList<PCB>());
				/* Setting of the NEW index of queue for ready */
				readyCNT.remove(readyId);
				readyCNT.put(readyId, new Integer(indexQueue));
			} // else do nothing! it keeps the same index of queue
		} else {
			/* Setting of the NEW index of queue for ready */
			indexQueue = indexQueue + 1;
			readyCNT.remove(readyId);
			readyCNT.put(readyId, new Integer(indexQueue));
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
		preemption = false;
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
			if (!preemption)
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
	public String toString() {
		return "Preemptive Multilevel Feedback";
	}

}
