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
 * File: Simulation.java
 * Package: simulation
 * Author: Stefano Bertolin, Piero Dalle Pezze
 * Date: 28/01/2006
 * Version: 1.2
 *
 * Modifies:
 *  v1.3 (19/11/2014): Removed automatic advancement and inserted manual incremental step.
 *  v1.2 (01/02/2007): English translation. Java6 compatible. (Piero Dalle Pezze)
 *  v1.1 (30/01/2006): Codify.
 *  v1.0 (28/01/2006): Documentation.
 */
package org.rainbow.simulation;

import org.rainbow.gui.*;
import java.util.*;
import org.rainbow.data.*;

/**
 * This is the class that provides to execute a processes and resources
 * simulation. It also provides to compute the most important statistics.
 *
 * @author Stefano Bertolin
 * @author Dalle Pezze Piero
 * @version 1.3
 */
public class Simulation {

	/**
	 * It contains, for each tick, the running process. It is used by the GUI to
	 * draw the schedulation graphic.
	 */
	private LinkedList<SimulatedProcess> executingProcesses;

	/**
	 * The actual time.
	 */
	private State now;

	/**
	 * The GUI reference.
	 */
	private RainbowMainGUI rainbowMainGUI;

	/**
	 * The RainbowViews reference.
	 */
	private RainbowViews rainbowViews;

	/**
	 * The evolution reference. It contains the history of the simulation.
	 */
	private Evolution evolution;

	/**
	 * The incremental step of a tick (in the GUI).
	 */
	private int delay = 1;

	/**
	 * It creates a simulation of processes.
	 *
	 * @param conf
	 *            The user configuration.
	 * @param GUI
	 *            The GUI reference.
	 */
	public Simulation(Configuration conf, RainbowMainGUI GUI) {
		rainbowMainGUI = GUI;
		rainbowViews = rainbowMainGUI.getViews();
		now = null;
		executingProcesses = new LinkedList<SimulatedProcess>();
		evolution = new Evolution(conf);
	}

	/**
	 * It returns the delay between a tick of simulation and its following. (The
	 * incremental step).
	 *
	 * @return delay.
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * It sets the delay between a tick of simulation and its following. (The
	 * incremental step)
	 *
	 * @param delay
	 * 
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}

	/**
	 * It moves the simulation forward of a tick of time.
	 * 
	 * @return True if the next state exists, false otherwise (the simulation
	 *         finished).
	 */
	public boolean forward() {
		boolean carryon = true;
		for (int i = 0; i < delay && carryon; i++) {
			State next = evolution.forward();
			// the simulation is finished.
			if (next == null) {
				rainbowMainGUI.setSimTerminated();
				carryon = false;
			} else {
				executingProcesses.add(next.getRunning());
				rainbowViews.viewRunningProcesses(executingProcesses);
				if (now != next) {
					now = next;
					rainbowViews.updateState(next);
				}
				if (executingProcesses.size() == evolution.getDuration()) {
					rainbowMainGUI.setSimTerminated();
					carryon = false;
				}
			}
		}
		return carryon;
	}

	/**
	 * It moves the simulation backward of a tick of time.
	 * 
	 * @return True the previous state exists, false otherwise (the simulation
	 *         is at start).
	 */
	public boolean backward() {
		boolean carryon = true;
		for (int i = 0; i < delay && carryon; i++) {
			State previous = evolution.backward();
			// There is not a previous
			if (previous == null) {
				executingProcesses.clear();
				now = null;
				carryon = false;
			} else {
				executingProcesses.removeLast();
				rainbowViews.viewRunningProcesses(executingProcesses);
				if (now != previous) {
					now = previous;
					rainbowViews.updateState(previous);
				}
			}
		}
		return carryon;
	}

	/**
	 * It moves the simulation at start.
	 */
	public void start() {
		evolution.start();
		executingProcesses.clear();
		now = null;
		// Reset the rainbowMainGUI
		rainbowMainGUI.setStartUp();
	}

	/**
	 * It moves the simulation at the end.
	 */
	public void end() {
		State j = null, i = evolution.forward();
		while (i != null) {
			executingProcesses.add(i.getRunning());
			j = i;
			i = evolution.forward();
		}
		if (j != null) {
			now = j;
			rainbowViews.updateState(j);
			rainbowViews.viewRunningProcesses(executingProcesses);
		}
		// Signal the rainbowMainGUI that the simulation is finished
		rainbowMainGUI.setSimTerminated();
	}

	/**
	 * It computes the statistics until this point of the simulation.
	 */
	public void statistics() {
		if (executingProcesses.size() == evolution.getDuration()) {
			LinkedList<SimulatedProcess> last = new LinkedList<SimulatedProcess>(
					executingProcesses);
			last.removeLast();
			rainbowViews.viewStatistics(new Statistics(now, last));
		} else if (executingProcesses.size() > 0) {
			rainbowViews
					.viewStatistics(new Statistics(now, executingProcesses));
		}
	}

	/**
	 * It returns the duration of the simulation.
	 *
	 * @return the duration of the simulation expressed in tick of time.
	 */
	public int getDuration() {
		return evolution.getDuration();
	}

}
