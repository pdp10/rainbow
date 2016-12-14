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
 * File: ViewProcesses.java
 * Package: gui.view
 * Author: Sarto Carlo, Dalle Pezze Piero
 * Date: 03/02/2006
 * Version: 1.2
 * 
 * Modifies
 *  - v.1.2 (03/05/2007): English translation and Java 6 support. Dalle Pezze Piero.
 *  - v.1.1 (06/02/2006): Documentation of the class. Sarto Carlo.
 *  - v.1.0 (03/02/2006): Codify of the class. Sarto Carlo.
 */
package org.rainbow.gui.view;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.table.*;
import org.rainbow.data.*;
import org.rainbow.simulation.*;
import java.util.ArrayList;
import org.rainbow.gui.language.*;
import java.awt.*;

/**
 * It is the view of the processes and their current state during the
 * simulation.
 * 
 * @author Sarto Carlo
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class ViewProcesses extends JScrollPane {

	/** To serialize */
	private static final long serialVersionUID = -2570828911298123783L;

	/** The table. */
	private JTable processesTable = null;

	/** The header of column. */
	private String[] columnNames;

	/** Data inside the table. */
	private Object[][] data = null;

	/** Model of table */
	private class TableModel extends DefaultTableModel {
		private static final long serialVersionUID = -1928583245978792L;

		public TableModel() {
			super(data, columnNames);
		}

		public boolean isCellEditable(int rowIndex, int collIndex) {
			return false;
		}
	}

	/**
	 * It initializes the view to show the ready queue.
	 * 
	 */
	public ViewProcesses() {
		super();

		columnNames = new String[2];
		columnNames[0] = Language.getProcess();
		columnNames[1] = Language.getState();
		data = new Object[1][2];
		data[0][0] = Language.getProcess();
		data[0][1] = Language.getState();

		if (processesTable == null) {
			processesTable = new JTable(new TableModel());
		}
		// Columns cannot be moved.
		processesTable.getTableHeader().setReorderingAllowed(false);
		// Cells cannot be selected.
		processesTable.setCellSelectionEnabled(false);
		// It sets the colour of the grid.
		processesTable.setGridColor(java.awt.Color.lightGray);
		JPanel panel = new JPanel();
		panel.setBackground(java.awt.Color.WHITE);
		panel.setLayout(new BorderLayout());
		panel.add(processesTable);
		this.setViewportView(panel);
	}

	/**
	 * It initializes the processes view when the simulation is created.
	 * 
	 * @param processes
	 *            The vector of processes of the configuration.
	 */
	public void initialize(ArrayList<SimulatedProcess> processes) {
		data = new Object[processes.size() + 1][2];
		data[0][0] = Language.getProcess();
		data[0][1] = Language.getState();
		for (int i = 0; i < processes.size(); i++) {
			SimulatedProcess p = processes.get(i);
			data[i + 1][0] = p.getName();
			// Processes must be still activated
			data[i + 1][1] = Language.getToActivateState();
		}
		processesTable = new JTable(new TableModel());
		// Columns cannot be moved.
		processesTable.getTableHeader().setReorderingAllowed(false);
		// Cells cannot be selected.
		processesTable.setCellSelectionEnabled(false);
		// It sets the colour of the grid.
		processesTable.setGridColor(java.awt.Color.lightGray);
		JPanel panel = new JPanel();
		panel.setBackground(java.awt.Color.WHITE);
		panel.setLayout(new BorderLayout());
		panel.add(processesTable);
		this.setViewportView(panel);
	}

	/**
	 * It returns true if blocked contains process, false otherwise.
	 * 
	 * @param process
	 *            The process that we want to know its state.
	 * 
	 * @param blocked
	 *            The list of processes blocked for each resource.
	 * 
	 */
	private boolean isBlocked(SimulatedProcess process,
			ArrayList<ResourceAttribution> blocked) {
		for (int i = 0; i < blocked.size(); i++) {
			ResourceAttribution rpr = blocked.get(i);
			if (rpr.getProcessList().contains(process))
				return true;
		}
		return false;
	}

	/**
	 * It updates the processes view.
	 * 
	 * @param processes
	 *            The processes terminated list.
	 * @param state
	 *            The current state of the simulation.
	 */
	public void update(ArrayList<SimulatedProcess> processes, State state) {
		data = new Object[processes.size() + 1][2];
		data[0][0] = Language.getProcess();
		data[0][1] = Language.getState();
		// The id of the running process.
		int runningProcessID = -1;
		if (state.getRunning() != null) {
			runningProcessID = state.getRunning().getId();
		}
		for (int i = 0; i < processes.size(); i++) {
			SimulatedProcess p = processes.get(i);
			int processID = p.getId();
			// The process'name.
			data[i + 1][0] = p.getName();
			// The process's actual state.
			if (processID == runningProcessID) {
				data[i + 1][1] = Language.getExecutingState();
			} else if (state.getReady().contains(p)) {
				data[i + 1][1] = Language.getReadyState();
			} else if (state.getTerminated().contains(p)) {
				data[i + 1][1] = Language.getTerminatedState();
			} else if (isBlocked(p, state.getBlockedQueues())) {
				data[i + 1][1] = Language.getBlockedState();
			} else {
				data[i + 1][1] = Language.getToActivateState();
			}
		}

		processesTable = new JTable(new TableModel());
		// Columns cannot be moved.
		processesTable.getTableHeader().setReorderingAllowed(false);
		// Cells cannot be selected.
		processesTable.setCellSelectionEnabled(false);
		// It sets the colour of the grid.
		processesTable.setGridColor(java.awt.Color.lightGray);
		JPanel panel = new JPanel();
		panel.setBackground(java.awt.Color.WHITE);
		panel.setLayout(new BorderLayout());
		panel.add(processesTable);
		this.setViewportView(panel);
	}

	/**
	 * It resets the processes view.
	 */
	public void reset() {
		data = null;
		processesTable = new JTable(new TableModel());
		// Columns cannot be moved.
		processesTable.getTableHeader().setReorderingAllowed(false);
		// Cells cannot be selected.
		processesTable.setCellSelectionEnabled(false);
		// It sets the colour of the grid.
		processesTable.setGridColor(java.awt.Color.lightGray);
		JPanel panel = new JPanel();
		panel.setBackground(java.awt.Color.WHITE);
		panel.setLayout(new BorderLayout());
		panel.add(processesTable);
		this.setViewportView(panel);
	}
}
