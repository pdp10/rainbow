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
 * File: Statistics.java
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
 * This class represents all statistics of the simulation.
 * 
 * @author Stefano Bertolin
 * @author Piero Dalle Pezze
 * @version 1.2
 */
public class Statistics {

	/**
	 * The throughput.
	 */
	private double throughput = 0;

	/**
	 * The turn around average.
	 */
	private double turnAroundAV = 0;

	/**
	 * The waiting time average.
	 */
	private double waitingTimeAV = 0;

	/**
	 * The response time average.
	 */
	private double responseTimeAV = 0;

	/**
	 * List of ProcessStatistic.
	 */
	private ArrayList<ProcessStatistics> procStat;

	/**
	 * It creates all the statistics of the simulation.
	 * 
	 * @param now
	 *            The actual time of the simulation.
	 * @param executed
	 *            The list containing, for each tick, the reference to the
	 *            running process.
	 */
	public Statistics(State now, LinkedList<SimulatedProcess> executed) {
		procStat = new ArrayList<ProcessStatistics>();
		SimulatedProcess running = now.getRunning();
		ArrayList<SimulatedProcess> ready = now.getReady();
		ArrayList<ResourceAttribution> blocked = now.getBlockedQueues();
		ArrayList<SimulatedProcess> terminated = now.getTerminated();
		int i;

		// for each process, it computes its statistics.
		if (running != null) {
			procStat.add(new ProcessStatistics(running, executed));
		}

		for (i = 0; i < ready.size(); i++) {
			/*
			 * This test avoids to consider processes created during the
			 * simulation from policies with priority or multilevel. (See the
			 * method getReadyQueue() in HPF.java, MF.java,
			 * PriorityRoundRobin.java .)
			 */
			if (ready.get(i).getActivationTime() != -1) {
				procStat.add(new ProcessStatistics(ready.get(i), executed));
			}
		}

		ResourceAttribution ra;
		ArrayList<SimulatedProcess> blockedQueue;
		for (i = 0; i < blocked.size(); i++) {
			ra = blocked.get(i);
			blockedQueue = ra.getProcessList();
			for (int j = 0; j < blockedQueue.size(); j++)
				procStat.add(new ProcessStatistics(blockedQueue.get(j),
						executed));
		}
		for (i = 0; i < terminated.size(); i++) {
			procStat.add(new ProcessStatistics(terminated.get(i), executed));
		}

		// it computes all general statistics.
		int completed = 0, started = 0, totWaiting = 0, totResponse = 0, totTurnAround = 0;
		ProcessStatistics sp;
		for (i = 0; i < procStat.size(); i++) {
			sp = procStat.get(i);
			totWaiting += sp.getWaitingTime();
			if (sp.getResponseTime() != -1) {
				totResponse += sp.getResponseTime();
				started++;
			}
			if (sp.getTurnAround() != -1) {
				totTurnAround += sp.getTurnAround();
				completed++;
			}
		}
		// only if there are some processes.
		if (procStat.size() != 0)
			waitingTimeAV = (double) totWaiting / procStat.size();
		if (completed != 0)
			turnAroundAV = (double) totTurnAround / completed;
		if (started != 0)
			responseTimeAV = (double) totResponse / started;
		throughput = (double) 100 * completed / executed.size();
	}

	/**
	 * It returns the waiting time average.
	 * 
	 * @return the waiting time average.
	 */
	public double getWaitingTimeAV() {
		return waitingTimeAV;
	}

	/**
	 * It returns the response time average.
	 * 
	 * @return the response time average.
	 */
	public double getResponseTimeAV() {
		return responseTimeAV;
	}

	/**
	 * It returns the turn around average.
	 * 
	 * @return the turn around average.
	 */
	public double getTurnAroundAV() {
		return turnAroundAV;
	}

	/**
	 * It returns the throughput.
	 * 
	 * @return the throughput.
	 */
	public double getThroughput() {
		return throughput;
	}

	/**
	 * It returns a list of the statistics of every process in the simulation.
	 * 
	 * @return a list of statistics of every process.
	 */
	public ArrayList<ProcessStatistics> getProcessesStatistics() {
		return procStat;
	}

}