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
 * File: ViewStatistics.java
 * Package: gui.view
 * Author: Sarto Carlo, Dalle Pezze Piero
 * Date: 07/02/2006
 * Version: 1.2
 * 
 * Modifies
 *  - v.1.2 (03/05/2007): English translation and Java 6 support. Dalle Pezze Piero.
 *  - v.1.1 (08/02/2006): Documentation of the class. Sarto Carlo.
 *  - v.1.0 (07/02/2006): Codify of the class. Sarto Carlo.
 */
package org.rainbow.gui.view;

import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;

import org.rainbow.simulation.*;
import org.rainbow.gui.language.*;

/**
 * It is used to show all main statistics of the simulation and processes. On
 * details, it shows for each process:
 * <ul>
 * <li>CPU usage</li>
 * <li>CPU usage (%)</li>
 * <li>Turn around time</li>
 * <li>Response time</li>
 * <li>Waiting time</li>
 * </ul>
 * Therefore it shows the following statistics of the simulation: <li>Average
 * waiting time</li> <li>Average response time</li> <li>Average turn around time
 * </li> <li>Throughput</li> </ol>
 * 
 * @author Sarto Carlo
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class ViewStatistics extends JScrollPane {
	JScrollBar scroller = this.getVerticalScrollBar();

	/** To serialize. */
	private static final long serialVersionUID = 1109913847658572644L;

	/** The second panel of the view that contains the statistics. */
	private JPanel p2 = null;

	private JPanel panelStat = null;

	/** The header of column of processes. */
	private String[] columnNamesProcesses;

	/** Data inside the table of processes. */
	private Object[][] dataProcesses = null;

	/** Model of table of processes. */
	private class TableModelProcesses extends DefaultTableModel {
		private static final long serialVersionUID = -993827345978792L;

		public TableModelProcesses() {
			super(dataProcesses, columnNamesProcesses);
		}

		public boolean isCellEditable(int rowIndex, int collIndex) {
			return false;
		}
	}

	/** The header of column of simulation. */
	private String[] columnNamesSimulation;

	/** Data inside the table of simulation. */
	private Object[][] dataSimulation = null;

	/** Model of table of simulation. */
	private class TableModelSimulation extends DefaultTableModel {
		private static final long serialVersionUID = -993827345978792L;

		public TableModelSimulation() {
			super(dataSimulation, columnNamesSimulation);
		}

		public boolean isCellEditable(int rowIndex, int collIndex) {
			return false;
		}
	}

	/**
	 * The constructor of the class.
	 */
	public ViewStatistics() {
		super();
		columnNamesProcesses = new String[6];
		columnNamesProcesses[0] = Language.getName();
		columnNamesProcesses[1] = Language.getResponseTime();
		columnNamesProcesses[2] = Language.getWaitingTime();
		columnNamesProcesses[3] = Language.getTurnAround();
		columnNamesProcesses[4] = Language.getCpuUsage();
		columnNamesProcesses[5] = Language.getCpuUsagePercentage();

		columnNamesSimulation = new String[4];
		columnNamesSimulation[0] = Language.getThroughput();
		columnNamesSimulation[1] = Language.getAverageWaitingTime();
		columnNamesSimulation[2] = Language.getAverageResponseTime();
		columnNamesSimulation[3] = Language.getAverageTurnAround();
		p2 = getP2();
		initialize();
		this.setViewportView(p2);
	}

	public void initialize() {
		// The main panel
		panelStat = new JPanel();
		panelStat.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(227, 223, 214)));
		panelStat.setBackground(java.awt.Color.WHITE);
		panelStat.setLayout(new BorderLayout());

		// The panel with the statistics of the simulation.
		JTextPane jTextPane = new JTextPane();
		jTextPane.setEditable(false);

		// It creates the panel with simulation's statistics
		dataSimulation = new Object[2][4];
		dataSimulation[0][0] = Language.getThroughput();
		dataSimulation[0][1] = Language.getAverageWaitingTime();
		dataSimulation[0][2] = Language.getAverageResponseTime();
		dataSimulation[0][3] = Language.getAverageTurnAround();
		// it gets values.
		dataSimulation[1][0] = "0.0";
		dataSimulation[1][1] = "0.0";
		dataSimulation[1][2] = "0.0";
		dataSimulation[1][3] = "0.0";

		JTable jTableSimulation = new JTable(new TableModelSimulation());
		// Columns cannot be moved.
		jTableSimulation.getTableHeader().setReorderingAllowed(false);
		// It sets the colour of the grid.
		jTableSimulation.setGridColor(java.awt.Color.lightGray);

		JPanel panelStatSimulation = new JPanel();
		panelStatSimulation.setBackground(java.awt.Color.WHITE);
		panelStatSimulation.setLayout(new BorderLayout());
		panelStatSimulation.add(
				new javax.swing.JLabel(Language.getStatisticsOfTheSimulation()
						+ ":\n"), BorderLayout.NORTH);

		panelStatSimulation.add(jTableSimulation, BorderLayout.CENTER);
		panelStatSimulation
				.add(new javax.swing.JLabel(" "), BorderLayout.SOUTH);

		// panelStat.add(new javax.swing.JLabel(" "),BorderLayout.SOUTH);
		panelStat.add(panelStatSimulation, BorderLayout.NORTH);

		// It creates the panel with processes' statistics
		dataProcesses = new Object[1][6];
		dataProcesses[0][0] = Language.getProcesses();
		dataProcesses[0][1] = Language.getWaitingTime();
		dataProcesses[0][2] = Language.getResponseTime();
		dataProcesses[0][3] = Language.getTurnAround();
		dataProcesses[0][4] = Language.getCpuUsage();
		dataProcesses[0][5] = Language.getCpuUsagePercentage();

		JTable jTable = new JTable(new TableModelProcesses());
		// Columns cannot be moved.
		jTable.getTableHeader().setReorderingAllowed(false);
		// It sets the colour of the grid.
		jTable.setGridColor(java.awt.Color.lightGray);

		JPanel panelStatProcesses = new JPanel();
		panelStatProcesses.setBackground(java.awt.Color.WHITE);
		panelStatProcesses.setLayout(new BorderLayout());
		panelStatProcesses.add(
				new javax.swing.JLabel(Language.getStatisticsOfProcesses()
						+ ":\n"), BorderLayout.NORTH);

		panelStatProcesses.add(jTable, BorderLayout.CENTER);
		panelStatProcesses.add(new javax.swing.JLabel(" "), BorderLayout.SOUTH);

		// panelStat.add(new javax.swing.JLabel(" "),BorderLayout.SOUTH);
		panelStat.add(panelStatProcesses, BorderLayout.CENTER);

		p2 = getP2();
		p2.add(panelStat, BorderLayout.CENTER);
		this.setViewportView(p2);
		scrollUp();
	}

	/**
	 * It creates a panel with the statistics.
	 * 
	 * @return javax.swing.JPanel A panel with the statistics.
	 */
	private JPanel getP2() {
		p2 = new JPanel();
		p2.setLayout(new BorderLayout());
		p2.setBackground(this.getBackground());
		return p2;
	}

	/** It moves the scroller up. */
	public void scrollUp() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (getVerticalScrollBar() != null) {
					scroller.setValue(scroller.getMinimum());
				}
			}
		});
	}

	/**
	 * This method generates a JPanel with the statistics.
	 * 
	 */
	public void makeStatistics(Statistics statistics) {
		// The main panel

		panelStat = new JPanel();
		panelStat.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(227, 223, 214)));
		panelStat.setBackground(java.awt.Color.WHITE);
		panelStat.setLayout(new BorderLayout());

		panelStat.setBackground(this.getBackground());
		// The panel with the statistics of the simulation.
		JTextPane jTextPane = new JTextPane();
		jTextPane.setEditable(false);

		// It creates the panel with simulation's statistics
		dataSimulation = new Object[2][4];
		dataSimulation[0][0] = Language.getThroughput();
		dataSimulation[0][1] = Language.getAverageWaitingTime();
		dataSimulation[0][2] = Language.getAverageResponseTime();
		dataSimulation[0][3] = Language.getAverageTurnAround();

		// it gets values.
		dataSimulation[1][0] = String.valueOf((float) statistics
				.getThroughput());
		dataSimulation[1][1] = String.valueOf((float) statistics
				.getWaitingTimeAV());
		dataSimulation[1][2] = String.valueOf((float) statistics
				.getResponseTimeAV());
		dataSimulation[1][3] = String.valueOf((float) statistics
				.getTurnAroundAV());

		JTable jTableSimulation = new JTable(new TableModelSimulation());
		// Columns cannot be moved.
		jTableSimulation.getTableHeader().setReorderingAllowed(false);
		// It sets the colour of the grid.
		jTableSimulation.setGridColor(java.awt.Color.lightGray);

		JPanel panelStatSimulation = new JPanel();
		panelStatSimulation.setBackground(java.awt.Color.WHITE);
		panelStatSimulation.setLayout(new BorderLayout());
		panelStatSimulation.add(
				new javax.swing.JLabel(Language.getStatisticsOfTheSimulation()
						+ ":\n"), BorderLayout.NORTH);

		panelStatSimulation.add(jTableSimulation, BorderLayout.CENTER);
		panelStatSimulation
				.add(new javax.swing.JLabel(" "), BorderLayout.SOUTH);

		// panelStat.add(new javax.swing.JLabel(" "),BorderLayout.SOUTH);
		panelStat.add(panelStatSimulation, BorderLayout.NORTH);

		// It creates the panel with processes' statistics
		dataProcesses = new Object[statistics.getProcessesStatistics().size() + 1][6];
		dataProcesses[0][0] = Language.getProcesses();
		dataProcesses[0][1] = Language.getWaitingTime();
		dataProcesses[0][2] = Language.getResponseTime();
		dataProcesses[0][3] = Language.getTurnAround();
		dataProcesses[0][4] = Language.getCpuUsage();
		dataProcesses[0][5] = Language.getCpuUsagePercentage();

		boolean added = false;
		int index = 1;
		// It adds processes' statistics
		for (int i = 0; i < statistics.getProcessesStatistics().size(); i++) {
			added = false;

			// It sorts statistics by process' id
			for (int j = 0; j < statistics.getProcessesStatistics().size()
					&& !added; j++) {

				if (statistics.getProcessesStatistics().get(j).getProcess()
						.getId() == index) {

					ProcessStatistics stat = statistics
							.getProcessesStatistics().get(j);
					dataProcesses[i + 1][0] = stat.getProcess().getName();
					dataProcesses[i + 1][1] = (String.valueOf(stat
							.getWaitingTime()));
					dataProcesses[i + 1][2] = (((stat.getResponseTime()) != (-1)) ? String
							.valueOf(stat.getResponseTime()) : "---");
					dataProcesses[i + 1][3] = (((stat.getTurnAround()) != (-1)) ? String
							.valueOf(stat.getTurnAround()) : "---");
					dataProcesses[i + 1][4] = (String.valueOf(stat
							.getCPUUsage()));
					dataProcesses[i + 1][5] = (String.valueOf((float) stat
							.getCPUUsagePercent()));
					index++;
					added = true;
				} // end if
			} // end for j
			if (!added) {
				// NOTE: It prints only statistics of activated process.
				index++;
				// It remains on the same cell. It avoids to lay empty.
				i--;
			}

		} // end for i
		JTable jTable = new JTable(new TableModelProcesses());
		// Columns cannot be moved.
		jTable.getTableHeader().setReorderingAllowed(false);
		// It sets the colour of the grid.
		jTable.setGridColor(java.awt.Color.lightGray);

		JPanel panelStatProcesses = new JPanel();
		panelStatProcesses.setBackground(java.awt.Color.WHITE);
		panelStatProcesses.setLayout(new BorderLayout());
		panelStatProcesses.add(
				new javax.swing.JLabel(Language.getStatisticsOfProcesses()
						+ ":\n"), BorderLayout.NORTH);

		panelStatProcesses.add(jTable, BorderLayout.CENTER);
		panelStatProcesses.add(new javax.swing.JLabel(" "), BorderLayout.SOUTH);

		// panelStat.add(new javax.swing.JLabel(" "),BorderLayout.SOUTH);
		panelStat.add(panelStatProcesses, BorderLayout.CENTER);

		p2 = getP2();
		p2.add(panelStat, BorderLayout.CENTER);
		this.setViewportView(p2);
		scrollUp();
	}

}
