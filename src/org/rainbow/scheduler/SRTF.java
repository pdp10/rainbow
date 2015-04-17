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
 * File: SRTF.java
 * Package: scheduler
 * Author: Marin Pier Giorgio, Dalle Pezze Piero
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
 * This class implemets the Shortest Remaining Time First scheduling policy. It
 * choose the process with the minimum execution time. This variant is
 * preemptive.
 * 
 * @author Marin Pier Giorgio
 * @author Dalle Pezze Piero
 * @version 1.1
 */
public class SRTF extends SJF implements Preemptive {

	/**
	 * To serializable
	 */
	private static final long serialVersionUID = -5017L;

	/**
	 * It creates the SRTF policy.
	 */
	public SRTF() {
		super();
	}

	/**
	 * It returns the process with the minimum remaining time of execution.
	 * 
	 * @param ready
	 *            A process.
	 * @param inExecution
	 *            The process actually in execution.
	 * @return the process with the minimum remaining time of execution.
	 */
	public PCB minor(PCB ready, PCB inExecution) {
		if (ready.getRemainingTime() < inExecution.getRemainingTime()) {
			return ready;
		}
		return inExecution;

	}

	/**
	 * It inserts the new ready process in the ready queue. Before inserting, it
	 * observes if the new process has a remaining time of execution less than
	 * the process actually in execution. In these case, it applies the
	 * preemption of the process in execution and the new process becomes the
	 * process in execution. It inserts the yield the queue sorted by execution
	 * time increasing.
	 * 
	 * @param ready
	 *            the process to insert in the ready queue.
	 */
	public void insert(PCB ready) {
		PCB inExecution = dispatcher.getPCBCurrent();
		if (inExecution != null) {
			// there is a process in execution
			PCB min = minor(ready, inExecution);
			if (ready.equals(min)) {
				// Preemption
				dispatcher.preemptionPCBCurrent();
				readyQueue.addFirst(inExecution);
				readyQueue.addFirst(ready);
			} else {
				super.insert(ready);
			}
		} else {
			super.insert(ready);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "Shortest Remaining Time First";
	}
}