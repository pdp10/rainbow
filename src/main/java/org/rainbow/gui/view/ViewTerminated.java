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
 * File: ViewTerminated.java
 * Package: gui.view
 * Author: Sarto Carlo, Dalle Pezze Piero
 * Date: 02/02/2006
 * Version: 1.2
 *
 * Modifies
 *  - v.1.2 (10/05/2007): English translation and Java 6 support. Dalle Pezze Piero.
 *  - v.1.1 (07/02/2006): Documentation of the class. Sarto Carlo.
 *  - v.1.0 (02/02/2006): Codify of the class. Sarto Carlo.
 */
package org.rainbow.gui.view;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import org.rainbow.data.*;
import org.rainbow.gui.language.*;
import java.awt.*;

/**
 * It is the view of the terminated processes of a simulation.
 * 
 * @author Sarto Carlo
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class ViewTerminated extends JScrollPane {

	/** To serialize */
	private static final long serialVersionUID = 2351798959670046887L;

	/** The table. */
	private JTable terminatedTable = null;

	/** The header of column. */
	private String[] columnNames;

	/** Data inside the table. */
	private Object[][] data = null;

	/** Model of table */
	private class TableModel extends DefaultTableModel {
		private static final long serialVersionUID = -8832859438752082L;

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
	public ViewTerminated() {
		super();

		columnNames = new String[1];
		columnNames[0] = Language.getProcesses();
		if (terminatedTable == null) {
			terminatedTable = new JTable(new TableModel());
		}
		// Columns cannot be moved.
		terminatedTable.getTableHeader().setReorderingAllowed(false);
		// Cells cannot be selected.
		terminatedTable.setCellSelectionEnabled(false);
		// It sets the colour of the grid.
		terminatedTable.setGridColor(java.awt.Color.lightGray);
		JPanel panel = new JPanel();
		panel.setBackground(java.awt.Color.WHITE);
		panel.setLayout(new BorderLayout());
		panel.add(terminatedTable);
		this.setViewportView(panel);
	}

	/**
	 * It updates the terminated view.
	 * 
	 * @param terminated
	 *            The processes terminated list.
	 */
	public void update(ArrayList<SimulatedProcess> terminated) {
		data = new Object[terminated.size()][2];

		for (int i = 0; i < terminated.size(); i++) {
			SimulatedProcess p = terminated.get(i);
			data[i][0] = p.getName();
		}
		terminatedTable = new JTable(new TableModel());
		// Columns cannot be moved.
		terminatedTable.getTableHeader().setReorderingAllowed(false);
		// Cells cannot be selected.
		terminatedTable.setCellSelectionEnabled(false);
		// It sets the colour of the grid.
		terminatedTable.setGridColor(java.awt.Color.lightGray);
		JPanel panel = new JPanel();
		panel.setBackground(java.awt.Color.WHITE);
		panel.setLayout(new BorderLayout());
		panel.add(terminatedTable);
		this.setViewportView(panel);
	}

	/**
	 * It resets the terminated view.
	 */
	public void reset() {
		data = null;
		terminatedTable = new JTable(new TableModel());
		// Columns cannot be moved.
		terminatedTable.getTableHeader().setReorderingAllowed(false);
		// Cells cannot be selected.
		terminatedTable.setCellSelectionEnabled(false);
		// It sets the colour of the grid.
		terminatedTable.setGridColor(java.awt.Color.lightGray);
		JPanel panel = new JPanel();
		panel.setBackground(java.awt.Color.WHITE);
		panel.setLayout(new BorderLayout());
		panel.add(terminatedTable);
		this.setViewportView(panel);
	}

}