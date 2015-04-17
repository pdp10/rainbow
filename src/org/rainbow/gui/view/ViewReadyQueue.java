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
 * File: ViewReadyQueue.java
 * Package: gui.view
 * Author: Sarto Carlo, Dalle Pezze Piero
 * Date: 01/02/2006
 * Version: 1.2
 * 
 * Modifies
 *  - v.1.2 (04/05/2007): English translation and Java 6 support. Dalle Pezze Piero.
 *  - v.1.1 (05/02/2006): Documentation of the class. Sarto Carlo.
 *  - v.1.0 (01/02/2006): Codify of the class. Sarto Carlo.
 */
package org.rainbow.gui.view;

import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.*;
import java.awt.*;

import org.rainbow.gui.language.*;
import org.rainbow.data.*;

/**
 * It is the view of the ready queue of a simulation.
 * 
 * @author Sarto Carlo
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class ViewReadyQueue extends JScrollPane {

	/** To serialize */
	private static final long serialVersionUID = -4441774947632050544L;

	/** The table. */
	private JTable readyTable = null;

	/** The header of column. */
	private String[] columnNames;

	/** Data inside the table. */
	private Object[][] data = null;

	/** Model of table */
	private class TableModel extends DefaultTableModel {
		private static final long serialVersionUID = -8010045440378942082L;

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
	public ViewReadyQueue() {
		super();

		columnNames = new String[1];
		columnNames[0] = Language.getProcesses();

		if (readyTable == null) {
			readyTable = new JTable(new TableModel());
		}
		// Columns cannot be moved.
		readyTable.getTableHeader().setReorderingAllowed(false);
		// Cells cannot be selected.
		readyTable.setCellSelectionEnabled(false);
		// It sets the colour of the grid.
		readyTable.setGridColor(java.awt.Color.lightGray);
                JPanel panel = new JPanel();
                panel.setBackground(java.awt.Color.WHITE);
                panel.setLayout(new BorderLayout());
                panel.add(readyTable);
		this.setViewportView(panel);
	}

	/**
	 * It updates the ready process queue.
	 * 
	 * @param readyQueue
	 *            The ready queue.
	 */
	public void update(ArrayList<SimulatedProcess> readyQueue) {
		data = new Object[readyQueue.size()][2];
		// It inserts the process' name.
		for (int i = 0; i < readyQueue.size(); i++) {
			data[i][0] = readyQueue.get(i).getName();
		}

		readyTable = new JTable(new TableModel());
		// Columns cannot be moved.
		readyTable.getTableHeader().setReorderingAllowed(false);
		// Cells cannot be selected.
		readyTable.setCellSelectionEnabled(false);
		// It sets the colour of the grid.
		readyTable.setGridColor(java.awt.Color.lightGray);
                JPanel panel = new JPanel();
                panel.setBackground(java.awt.Color.WHITE);
                panel.setLayout(new BorderLayout());
                panel.add(readyTable);
		this.setViewportView(panel);
	}

	/**
	 * It resets the ready queue view.
	 */
	public void reset() {
		data = null;
		readyTable = new JTable(new TableModel());
		// Columns cannot be moved.
		readyTable.getTableHeader().setReorderingAllowed(false);
		// Cells cannot be selected.
		readyTable.setCellSelectionEnabled(false);
		// It sets the colour of the grid.
		readyTable.setGridColor(java.awt.Color.lightGray);
                JPanel panel = new JPanel();
                panel.setBackground(java.awt.Color.WHITE);
                panel.setLayout(new BorderLayout());
                panel.add(readyTable);
		this.setViewportView(panel);
	}

}