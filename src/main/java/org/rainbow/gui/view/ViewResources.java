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
 * File: ViewResources.java
 * Package: gui.view
 * Author: Sarto Carlo, Dalle Pezze Piero
 * Date: 05/02/2006
 * Version: 1.2
 * 
 * Modifies
 *  - v.1.2 (03/05/2007): English translation and Java 6 support. Dalle Pezze Piero.
 *  - v.1.1 (08/02/2006): Documentation of the class. Sarto Carlo.
 *  - v.1.0 (05/02/2006): Codify of the class. Sarto Carlo.
 */
package org.rainbow.gui.view;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import org.rainbow.simulation.*;
import org.rainbow.data.*;
import org.rainbow.gui.language.*;
import java.awt.*;

/**
 * It is the view of the processes and their current state during the
 * simulation. The table shows the following informations:
 * <ol>
 * <li>Resource: The name of the resource.</li>
 * <li>Current attribution: The list of processes which are using the resource.</li>
 * <li>Blocked queue: For not preemptive resource, the queue of blocked
 * processes which are waiting for the resource access.</li>
 * </ol>
 * 
 * @author Sarto Carlo
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class ViewResources extends JScrollPane {

	/** To serialize */
	private static final long serialVersionUID = -2570828911298123783L;

	/** The table. */
	private JTable resourcesTable = null;

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
	 * It initializes the view to show the resources attibutions. .
	 */
	public ViewResources() {
		super();

		columnNames = new String[3];
		columnNames[0] = Language.getResource();
		columnNames[1] = Language.getAssignment();
		columnNames[2] = Language.getBlockedProcesses();
		data = new Object[1][3];
		data[0][0] = Language.getResource();
		data[0][1] = Language.getAssignment();
		data[0][2] = Language.getBlockedProcesses();

		if (resourcesTable == null) {
			resourcesTable = new JTable(new TableModel());
		}
		// Columns cannot be moved.
		resourcesTable.getTableHeader().setReorderingAllowed(false);
		// Cells cannot be selected.
		resourcesTable.setCellSelectionEnabled(false);
		// It sets the colour of the grid.
		resourcesTable.setGridColor(java.awt.Color.lightGray);
		JPanel panel = new JPanel();
		panel.setBackground(java.awt.Color.WHITE);
		panel.setLayout(new BorderLayout());
		panel.add(resourcesTable);
		this.setViewportView(panel);
	}

	/**
	 * It initializes the processes view when the simulation is created.
	 * 
	 * @param resources
	 *            The vector of resources of the configuration.
	 */
	public void initialize(ArrayList<Resource> resources) {
		data = new Object[resources.size() + 1][3];
		data[0][0] = Language.getResource();
		data[0][1] = Language.getAssignment();
		data[0][2] = Language.getBlockedProcesses();
		for (int i = 0; i < resources.size(); i++) {
			Resource r = resources.get(i);
			data[i + 1][0] = r.getName();
			data[i + 1][1] = "";
			if (r instanceof PreemptiveResource) {
				data[i + 1][2] = Language.getNoQueue();
			} else {
				data[i + 1][2] = "";
			}
		}
		resourcesTable = new JTable(new TableModel());
		// Columns cannot be moved.
		resourcesTable.getTableHeader().setReorderingAllowed(false);
		// Cells cannot be selected.
		resourcesTable.setCellSelectionEnabled(false);
		// It sets the colour of the grid.
		resourcesTable.setGridColor(java.awt.Color.lightGray);
		JPanel panel = new JPanel();
		panel.setBackground(java.awt.Color.WHITE);
		panel.setLayout(new BorderLayout());
		panel.add(resourcesTable);
		this.setViewportView(panel);
	}

	/**
	 * It updates the resources view.
	 * 
	 * @param resources
	 *            The resources list.
	 * @param state
	 *            The current state of the simulation.
	 */
	public void update(ArrayList<Resource> resources, State state) {
		data = new Object[resources.size() + 1][3];
		data[0][0] = Language.getResource();
		data[0][1] = Language.getAssignment();
		data[0][2] = Language.getBlockedProcesses();
		ArrayList<ResourceAttribution> attributions = state
				.getAttributedResources();
		ArrayList<ResourceAttribution> blokedQueues = state.getBlockedQueues();

		// For each resource of the configuration, it obtains the current
		// blocked queues and attributions.
		for (int i = 0; i < resources.size(); i++) {
			Resource r = resources.get(i);
			data[i + 1][0] = r.getName();
			String attributionList = new String("");

			// List of attributions.
			for (int k = 0; k < attributions.size(); k++) {
				ResourceAttribution relation = attributions.get(k);
				if (r.equals(relation.getResource())) {
					// it gets processes' names.
					for (int j = 0; j < relation.getProcessList().size(); j++) {
						SimulatedProcess process = relation.getProcessList()
								.get(j);
						attributionList = attributionList + process.getName();
						if (j + 1 < relation.getProcessList().size())
							attributionList = attributionList + ", ";
					}
				}
			}
			data[i + 1][1] = attributionList;

			String blockedList = new String(Language.getNoQueue());
			if (!(r instanceof PreemptiveResource)) {
				blockedList = " ";
				// List of blocked processes.
				for (int k = 0; k < blokedQueues.size(); k++) {
					ResourceAttribution blockedQueue = blokedQueues.get(k);
					if (r.equals(blockedQueue.getResource())) {
						// it gets processes' names.
						for (int j = 0; j < blockedQueue.getProcessList()
								.size(); j++) {
							SimulatedProcess process = blockedQueue
									.getProcessList().get(j);
							blockedList = blockedList + process.getName();
							if (j + 1 < blockedQueue.getProcessList().size())
								blockedList = blockedList + ", ";
						}
					}
				}

			}
			data[i + 1][2] = blockedList;

		}
		resourcesTable = new JTable(new TableModel());
		// Columns cannot be moved.
		resourcesTable.getTableHeader().setReorderingAllowed(false);
		// Cells cannot be selected.
		resourcesTable.setCellSelectionEnabled(false);
		// It sets the colour of the grid.
		resourcesTable.setGridColor(java.awt.Color.lightGray);
		JPanel panel = new JPanel();
		panel.setBackground(java.awt.Color.WHITE);
		panel.setLayout(new BorderLayout());
		panel.add(resourcesTable);
		this.setViewportView(panel);
	}

	/**
	 * It resets the resources view.
	 */
	public void reset() {
		data = null;
		resourcesTable = new JTable(new TableModel());
		// Columns cannot be moved.
		resourcesTable.getTableHeader().setReorderingAllowed(false);
		// Cells cannot be selected.
		resourcesTable.setCellSelectionEnabled(false);
		JPanel panel = new JPanel();
		panel.setBackground(java.awt.Color.WHITE);
		panel.setLayout(new BorderLayout());
		panel.add(resourcesTable);
		this.setViewportView(panel);
	}

}