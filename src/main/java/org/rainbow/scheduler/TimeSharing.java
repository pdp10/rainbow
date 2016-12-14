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
 * Author: Piero Dalle Pezze
 * Date: 29/01/2006
 * Version: 1.2
 * 
 * Modifies:
 * v1.2 (31/01/2007): English translation. Java6 compatible.
 * v1.1 (30/01/2006): Class codify.
 * v1.0 (29/01/2006): Class documentation.
 */
package org.rainbow.scheduler;

/**
 * It provides a generic time sharing scheduling algorithm.
 * 
 * @author Marin Pier Giorgio
 * @author Dalle Pezze PIero
 * @version 1.2
 */
public abstract class TimeSharing implements SchedulingPolicy {

	/**
	 * To serializable
	 */
	private static final long serialVersionUID = -5020L;

	/**
	 * It creates a generic time sharing scheduling policy.
	 * 
	 * @param timeSlice
	 *            The time slice of the time sharing policy.
	 */
	public TimeSharing(int timeSlice) {
		this.timeSlice = timeSlice;
	}

	/**
	 * This is a counter of the time executed. It must be <= timeslice.
	 */
	private int tick;

	/**
	 * The time slice (or quantum) of the policy.
	 */
	private int timeSlice = 0;

	/**
	 * It resets the number of tick that the process has executed.
	 */
	public void reset() {
		tick = 0;
	}

	/**
	 * It returns the value of the time slice.
	 * 
	 * @return the value of the time slice.
	 */
	public int getTimeSlice() {
		return timeSlice;
	}

	/**
	 * It sets the value of the time slice.
	 * 
	 * @param timeSlice
	 *            the value of the time slice.
	 */
	public void setTimeSlice(int timeSlice) {
		this.timeSlice = timeSlice;
	}

	/**
	 * It returns the number of tick executed.
	 * 
	 * @return The number of tick executed.
	 */
	public int getTick() {
		return tick;
	}

	/**
	 * It sets the number of tick that the process has executed. This value must
	 * be <= the value of the time slice.
	 * 
	 * @param tick
	 *            The number of tick that the process has executed.
	 */
	public void setTick(int tick) {
		this.tick = tick;
	}

}
