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
 * Date: 29/04/01/2006
 * Version: 1.2
 * 
 * Modifies:
 * v1.2 (04/02/2007): English translation. Java6 compatible. (Dalle Pezze Piero)
 * v1.1 (30/01/2006): Class codify.
 * v1.0 (29/01/2006): Class documentation.
 */
package org.rainbow.scheduler;
import org.rainbow.simulation.*;
import org.rainbow.data.*;

/**
 * This class implements a round robin scheduling policy which is preemptive on
 * the priority of processes.
 * 
 * @author Marin Pier Giorgio
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class PreemptiveOnPriorityRoundRobin extends PriorityRoundRobin
		implements Preemptive {

	/**
	 * To serializable
	 */
	private static final long serialVersionUID = -5012L;

        
        /**
         * It is true if there is the preemption of the pcbCurrent, false otherwise.
         */
        protected boolean preemption = false;
        
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
	public PreemptiveOnPriorityRoundRobin(int timeSlice, int minPriority,
			int maxPriority) {
		super(timeSlice, minPriority, maxPriority);
	}

	/**
	 * It returns the process with the minimum priority.
	 * 
	 * @param ready
	 *            The ready process with the greatest priority.
	 * @param running
	 *            The running process.
	 * @return The process with the minimum priority.
	 */
	public PCB minor(PCB ready, PCB running) {
		int readyPriority = ready.getActivePriority();
		int runningPriority = running.getActivePriority();
		if (runningPriority < readyPriority) {
			return running;
		}
		return ready;
	}

	/**
	 * {@inheritDoc} This variant uses preemption if the priority of the ready
	 * process is bigger than the priority of the running one.
	 * 
	 * @param ready
	 *            The ready process to add to the ready queue.
	 */
	public void insert(PCB ready) {
		PCB running = dispatcher.getPCBCurrent();
		if (running != null) {
			PCB min = minor(ready, running);
			if (running.equals(min)) {
				// preemption
                                preemption = true;
				dispatcher.preemptionPCBCurrent();
				int priority = running.getActivePriority();
				// running becomes ready
				readyQueue.get(priority).addFirst(running);
				// ready has the greatest priority! So I put it ahead of his
				// priority queue.
				priority = ready.getActivePriority();
				readyQueue.get(priority).addFirst(ready);
			} else {
				// no preemption
				super.insert(ready);
			}
		} else {
			// no running process.
			super.insert(ready);
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
		if (getTick() == getTimeSlice() && !(running.getRemainingTime() == 0)) {
			// preemption
			dispatcher.removePCBCurrent();
			if(!preemption) insert(running);
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
		return "Preemptive on Priority Round Robin";
	}

}
