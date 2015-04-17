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
 * File: ProcessStatistics.java
 * Package: simulation
 * Author: Stefano Bertolin, Piero Dalle Pezze
 * Date: 28/01/2006
 * Version: 1.2
 * 
 * Modifies:
 *  v1.2 (01/02/2007): English translation. Java6 compatible. (Piero Dalle Pezze)
 *  v1.1 (30/01/2006): Codify.
 *  v1.0 (28/01/2006): Documentation.
 */
package org.rainbow.simulation;

import java.util.*;
import org.rainbow.data.*;

/**
 * This class contains all statistics of a process.
 * 
 * @author Stefano Bertolin
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class ProcessStatistics {

	/**
	 * The process reference.
	 */
	private SimulatedProcess process;

	/**
	 * The waiting time.
	 */
	private int waitingTime;

	/**
	 * The cpu usage time.
	 */
	private int cpuUsage = 0;

	/**
	 * The cpu usage (%).
	 */
	private double cpuUsagePercent;

	/**
	 * The response time.
	 */
	private int responseTime = -1;

	/**
	 * Turn around time.
	 */
	private int turnAround = -1;

	/**
	 * It creates a collection of the statistics of a process.
	 * 
	 * @param proc
	 *            The refered process.
	 * @param executed
	 *            The list containing, for each tick, the reference to the
	 *            running process.
	 */
	public ProcessStatistics(SimulatedProcess proc,
			LinkedList<SimulatedProcess> executed) {
		process = proc;
		// count the tick that the process has executed.
		for (int j = 0; j < executed.size(); j++) {
			if (executed.get(j) != null && executed.get(j).equals(process))
				cpuUsage++;
		}
		cpuUsagePercent = (double) 100 * cpuUsage / executed.size();
		// Test if the process has terminated.
		if (cpuUsage == process.getExecutionTime()) {
			int endExecution = executed.lastIndexOf(process) + 1;
			waitingTime = endExecution - process.getActivationTime() - cpuUsage;
			// set the turn around only if the process is terminated
			turnAround = endExecution - process.getActivationTime();
		} else
			waitingTime = executed.size() - process.getActivationTime()
					- cpuUsage;
		if (cpuUsage != 0)
			// set the response time only if the process has already executed at
			// least for a tick.
			responseTime = executed.indexOf(process)
					- process.getActivationTime();
	}

	/**
	 * It returns the process reference.
	 * 
	 * @return the process reference.
	 */
	public SimulatedProcess getProcess() {
		return process;
	}

	/**
	 * It returns the waiting time.
	 * 
	 * @return the waiting time.
	 */
	public int getWaitingTime() {
		return waitingTime;
	}

	/**
	 * It returns the cpu usage time.
	 * 
	 * @return the cpu usage time.
	 */
	public int getCPUUsage() {
		return cpuUsage;
	}

	/**
	 * It returns the cpu usage percentage.
	 * 
	 * @return the cpu usage percentage.
	 */
	public double getCPUUsagePercent() {
		return cpuUsagePercent;
	}

	/**
	 * It returns the response time. If the process is not start yet, it returns
	 * -1.
	 * 
	 * @return the response time.
	 */
	public int getResponseTime() {
		return responseTime;
	}

	/**
	 * It returns the turn around time. If the process is not terminated yet, it
	 * returns -1.
	 * 
	 * @return the turn around time.
	 */
	public int getTurnAround() {
		return turnAround;
	}

}
