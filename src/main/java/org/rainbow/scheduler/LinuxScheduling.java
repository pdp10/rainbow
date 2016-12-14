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
 * File: LinuxScheduling.java
 * Package: scheduler
 * Author: Dalle Pezze Piero
 * Date: 03/03/2007
 * Version: 1.1
 * 
 * Modifies:
 *  - v.1.0 (03/03/2007): Codify and documentation.
 */
package org.rainbow.scheduler;

/**
 * This class implements the Linux scheduling policy. DA FARE
 * 
 * @author Dalle Pezze Piero
 * @version 1.0
 */
public class LinuxScheduling extends PriorityRoundRobin {

	/**
	 * To serializable
	 */
	private static final long serialVersionUID = -5005L;

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
	public LinuxScheduling(int timeSlice, int minPriority, int maxPriority) {
		super(timeSlice, minPriority, maxPriority);
	}
}