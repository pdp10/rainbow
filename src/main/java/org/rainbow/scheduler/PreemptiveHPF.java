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
 * File: PreemptiveHPF.java
 * Package: scheduler
 * Author: Dalle Pezze Piero
 * Date: 03/02/2006
 * Version: 1.1
 * 
 * Modifies:
 *  - v.1.1 (31/01/2007): English translation. Java6 compatible. (Piero Dalle Pezze)
 *  - v.1.0 (03/02/2006): Codify and documentation.
 */
package org.rainbow.scheduler;

import org.rainbow.data.*;

/**
 * This class implements the Highest Priority First scheduling policy. The index
 * of the ArrayList represents the priority. This policy extracts the process
 * with the highest priority (the queue with the highest index). This variant is
 * preemptive.
 * 
 * @author Dalle Pezze Piero
 * @version 1.1
 */
public class PreemptiveHPF extends HPF implements Preemptive {

	/**
	 * To serializable
	 */
	private static final long serialVersionUID = -5009L;

	/**
	 * It creates the Highest Priority First scheduling policy with preemption.
	 * 
	 * @param minPriority
	 *            The minimum priority.
	 * @param maxPriority
	 *            The maximum priority.
	 */
	public PreemptiveHPF(int minPriority, int maxPriority) {
		super(minPriority, maxPriority);
	}

	/**
	 * It returns the process with the minimum priority.
	 * 
	 * @param ready
	 *            A process.
	 * @param inExecution
	 *            The process actually in execution.
	 * @return the process with the minimum remaining time of execution.
	 */
	public PCB minor(PCB ready, PCB inExecution) {
		if (inExecution.getActivePriority() < ready.getActivePriority()) {
			return inExecution;
		}
		return ready;
	}

	/**
	 * It inserts the new ready process in the ready queue. Before inserting, it
	 * observes if the process in execution has a priority less than the process
	 * ready. In these case, it applies the preemption of the process in
	 * execution and the new process becomes the process in execution. It
	 * inserts the yield the queue sorted by priority decreasing.
	 * 
	 * @param ready
	 *            the process to insert in the ready queue.
	 */
	public void insert(PCB ready) {
		PCB running = dispatcher.getPCBCurrent();
		if (running != null) {
			/* there is a running process. */
			PCB min = minor(ready, running);
			if (running.equals(min)) {
				/* Preemption */
				dispatcher.preemptionPCBCurrent();
				super.insert(running);
			}
		}
		super.insert(ready);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "Preemptive Highest Priority First";
	}
}
