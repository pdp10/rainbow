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
 * File: SchedulingPolicy.java
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

import java.io.Serializable;
import java.util.*;
import org.rainbow.data.*;
import org.rainbow.simulation.*;

/**
 * This is a generic scheduling policy.
 * 
 * @author Marin Pier Giorgio
 * @author Piero Dalle Pezze
 * @version 1.1
 */
public interface SchedulingPolicy extends Serializable {

	/**
	 * It executes the process in execution for a time passed as parameter.
	 * 
	 * @param time
	 *            the time to execute.
	 * @return It return the state of the scheduler.
	 */
	public State execute(int time);

	/**
	 * It returns the ready queue.
	 * 
	 * @return the ready queue.
	 */
	public ArrayList<SimulatedProcess> getReadyQueue();

	/**
	 * It sets the refered process dispatcher interface of the scheduler.
	 * 
	 * @param dispatcher
	 *            the referenced dispatcher.
	 */
	public void setProcessDispatcherInterface(ProcessDispatcherInterface dispatcher);

	/**
	 * It adds a new PCB in the queue.
	 * 
	 * @param pcb
	 *            the object to add.
	 */
	public void insert(PCB pcb);

	/**
	 * It extract a PCB from the queue.
	 * 
	 * @return the pcb extracted.
	 */
	public PCB extract();

	/**
	 * It returns the number of elements in the structure.
	 * 
	 * @return the size of the structure.
	 */
	public int size();

}
