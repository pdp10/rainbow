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
 * File: ConfigurationSummary.java
 * Package: gui
 * Author: Bertolin Stefano, Dalle Pezze Piero
 * Date: 16/03/2006
 * Version: 1.2
 *
 * Modifies
 * - v.1.2 (14/05/2007): Added all data of the configuration. Dalle Pezze Piero.
 * - v.1.1 (01/05/2007): Translation and Java6 compatible. Dalle Pezze Piero.
 * - v.1.0 (16/03/2006): Documentation and codify. Bertolin Stefano.
 */

package org.rainbow.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.border.LineBorder;
import org.rainbow.gui.language.*;
import org.rainbow.data.*;

/**
 * It is used to summarize the configuration created in a dialog.
 *
 * @author Stefano Bertolin
 * @author Piero Dalle Pezze
 * @version 1.2
 */
public class ConfigurationSummary extends JDialog {

	/**
	 * To serialize.
	 */
	private static final long serialVersionUID = 1L;

	private JPanel processPanel = null;

	private JPanel accessPanel = null;

	private JPanel resourcePanel = null;

	private JLabel processLabel = null;

	private JLabel accessLabel = null;

	private JLabel resourceLabel = null;

	private JScrollPane accessScrollPanel = null;

	private JTable accessTable = null;

	private JScrollPane resourceScrollPanel = null;

	private JTable resourceTable = null;

	private JScrollPane processScrollPanel = null;

	private JTable processTable = null;

	/**
	 * ArrayList of processes.
	 */
	private ArrayList<SimulatedProcess> process;

	/**
	 * ArrayList of resources.
	 */
	private ArrayList<Resource> resource;

	/**
	 * The name of the scheduling policy.
	 */
	private String schedulingPolicy;

	/**
	 * The name of the assignment policy.
	 */
	private String assignmentPolicy;

	/**
	 * The time slice
	 */
	private int timeslice;

	/**
	 * The icpp flag
	 */
	boolean icpp;

	/**
	 * It creates the window.
	 *
	 * @param conf
	 *            The configuration to show.
	 * @param parent
	 *            The window parent.
	 */
	public ConfigurationSummary(Configuration conf, JFrame parent) {
		super(parent, Language.getConfigurationSummary(), false);
		if (conf != null) {
			resource = conf.getResources();
			process = conf.getProcesses();
			schedulingPolicy = conf.getSchedulingPolicy();
			assignmentPolicy = conf.getAssignmentPolicy();
			timeslice = conf.getTimeslice();
			icpp = conf.isICPP();
			initialize();
			setVisible(true);
		}
	}

	/**
	 * It initializes the window.
	 */
	private void initialize() {
		this.setSize(650, 500);
		// this.setContentPane(getJContentPane());
		JPanel mainPanel = new JPanel() {
			private static final long serialVersionUID = -92845723487474L;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2;
				g2 = (Graphics2D) g;
				GradientPaint gradient = new GradientPaint(0, 0, Color.WHITE,
						getSize().width, getSize().height, new Color(181, 229,
								199));
				g2.setPaint(gradient);
				g.fillRect(0, 0, getSize().width, getSize().height);
			}
		};
		if (resource != null && resource.size() != 0) {
			mainPanel.setLayout(new GridLayout(3, 1));
			mainPanel.add(getProcessPanel());
			mainPanel.add(getResourcePanel());
			mainPanel.add(getAccessPanel());
		} else {
			mainPanel.setLayout(new GridLayout(1, 1));
			mainPanel.add(getProcessPanel());
		}

		setContentPane(mainPanel);
		((JPanel) getContentPane())
				.setBorder(new LineBorder(Color.DARK_GRAY, 1));

		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				dispose();
			}
		});
	}

	/**
	 * It initializes the process panel and returns it.
	 *
	 * @return the process panel.
	 */
	private JPanel getProcessPanel() {
		if (processPanel == null) {
			String header = "   " + Language.getProcesses() + " ("
					+ Language.getSchedulingPolicy() + ": " + schedulingPolicy;
			if (timeslice != -1)
				header = header.concat(", " + Language.getTimeSlice() + ": "
						+ timeslice + "):");
			else
				header = header.concat("):");
			processLabel = new JLabel();
			processLabel.setText(header);
			processPanel = new JPanel(new BorderLayout());
			processPanel.add(processLabel, BorderLayout.NORTH);
			processPanel.add(getProcessScrollPanel(), BorderLayout.CENTER);
			processPanel.setOpaque(false);
		}
		return processPanel;
	}

	/**
	 * It initializes the access panel and returns it.
	 *
	 * @return the access panel.
	 */
	private JPanel getAccessPanel() {
		if (accessPanel == null) {
			accessLabel = new JLabel();
			accessLabel.setText("   " + Language.getAssignment() + " ("
					+ Language.getAssignmentPolicy() + ": " + assignmentPolicy
					+ "):");
			accessPanel = new JPanel(new BorderLayout());
			accessPanel.add(accessLabel, BorderLayout.NORTH);
			accessPanel.add(getAccessScrollPanel(), BorderLayout.CENTER);
			accessPanel.setOpaque(false);
		}
		return accessPanel;
	}

	/**
	 * It initializes the resource panel and returns it.
	 *
	 * @return the resource panel.
	 */
	private JPanel getResourcePanel() {
		if (resourcePanel == null) {
			resourceLabel = new JLabel();
			resourceLabel.setText("   " + Language.getResources() + " ("
					+ Language.getICPP() + ": " + icpp + "):");
			resourcePanel = new JPanel(new BorderLayout());
			resourcePanel.add(resourceLabel, BorderLayout.NORTH);
			resourcePanel.add(getResourceScrollPanel(), BorderLayout.CENTER);
			resourcePanel.setOpaque(false);
		}
		return resourcePanel;
	}

	/**
	 * Used to scroll.
	 */
	private JScrollPane getAccessScrollPanel() {
		if (accessScrollPanel == null) {
			accessScrollPanel = new JScrollPane();
			accessScrollPanel.setViewportView(getAccessTable());
		}
		return accessScrollPanel;
	}

	/**
	 * It initializes the access table and returns it.
	 *
	 * @return the access table.
	 */
	private JTable getAccessTable() {
		if (accessTable == null) {
			Vector<String> columns = new Vector<String>();
			Vector<Vector<String>> values = new Vector<Vector<String>>();
			columns.add(Language.getProcess());
			columns.add(Language.getResource());
			columns.add(Language.getRequestTime());
			columns.add(Language.getRequestDuration());
			SimulatedProcess simulatedProcess;
			ArrayList<Access> accessList;
			Access access;
			for (int i = 0; i < process.size(); i++) {
				simulatedProcess = process.get(i);
				accessList = simulatedProcess.getAccessesList();
				if (accessList != null) {
					for (int j = 0; j < accessList.size(); j++) {
						access = accessList.get(j);
						Vector<String> newRow = new Vector<String>();
						newRow.add(simulatedProcess.getName());
						newRow.add(access.getResource().getName());
						newRow.add((new Integer(access.getRequestTime()))
								.toString());
						newRow.add((new Integer(access.getDuration()))
								.toString());
						values.add(newRow);
					}
				}
			}
			accessTable = new JTable(values, columns);
			accessTable.setEnabled(false);
			accessTable.setGridColor(java.awt.Color.lightGray);
		}
		return accessTable;
	}

	/**
	 * Used to scroll.
	 */
	private JScrollPane getResourceScrollPanel() {
		if (resourceScrollPanel == null) {
			resourceScrollPanel = new JScrollPane();
			resourceScrollPanel.setViewportView(getResourceTable());
		}
		return resourceScrollPanel;
	}

	/**
	 * It initializes the resource table and returns it.
	 *
	 * @return the resource table.
	 */
	private JTable getResourceTable() {
		if (resourceTable == null) {
			Vector<String> columns = new Vector<String>();
			Vector<Vector<String>> values = new Vector<Vector<String>>();
			columns.add(Language.getName());
			columns.add(Language.getPreemptive());
			columns.add(Language.getMultiplicity());
			columns.add(Language.getCeilingPriority());
			Resource resourceTemp;
			for (int i = 0; i < resource.size(); i++) {
				resourceTemp = resource.get(i);
				Vector<String> newRow = new Vector<String>();
				newRow.add(resourceTemp.getName());
				if (resourceTemp instanceof PreemptiveResource) {
					newRow.add(Language.getYes());
					newRow.add((new Integer(resourceTemp.getMultiplicity()))
							.toString());
					newRow.add("--");
				} else {
					newRow.add(Language.getNo());
					newRow.add((new Integer(resourceTemp.getMultiplicity()))
							.toString());
					newRow.add((new Integer(
							((NoPreemptiveResource) resourceTemp)
									.getCeilingPriority())).toString());
				}
				values.add(newRow);
			}
			resourceTable = new JTable(values, columns);
			resourceTable.setEnabled(false);
			resourceTable.setGridColor(java.awt.Color.lightGray);
		}
		return resourceTable;
	}

	/**
	 * Used to scroll.
	 */
	private JScrollPane getProcessScrollPanel() {
		if (processScrollPanel == null) {
			processScrollPanel = new JScrollPane();
			processScrollPanel.setViewportView(getProcessTable());
		}
		return processScrollPanel;
	}

	/**
	 * It initializes the process table and returns it.
	 *
	 * @return the process table.
	 */
	private JTable getProcessTable() {
		if (processTable == null) {
			Vector<String> columns = new Vector<String>();
			Vector<Vector<String>> values = new Vector<Vector<String>>();
			columns.add(Language.getName());
			columns.add(Language.getActivationTime());
			columns.add(Language.getExecutionTime());
			columns.add(Language.getBasePriority());
			SimulatedProcess simulatedProcess;
			for (int i = 0; i < process.size(); i++) {
				simulatedProcess = process.get(i);
				Vector<String> newRow = new Vector<String>();
				newRow.add(simulatedProcess.getName());
				newRow.add((new Integer(simulatedProcess.getActivationTime()))
						.toString());
				newRow.add((new Integer(simulatedProcess.getExecutionTime()))
						.toString());
				newRow.add((new Integer(simulatedProcess.getInitialPriority()))
						.toString());
				values.add(newRow);
			}
			processTable = new JTable(values, columns);
			processTable.setEnabled(false);
			processTable.setGridColor(java.awt.Color.lightGray);
		}
		return processTable;
	}

}