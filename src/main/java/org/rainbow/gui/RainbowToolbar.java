/*
 * Rainbow - A simulator of processes and resources in a multitasking computer.
 * Copyright (C) 2005 - 2008 E-mail: piero.dallepezze@gmail.com
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
 * File: RainbowToolbar.java
 * Package: gui
 * Author: Dalle Pezze Piero
 * Date: 25/11/2014
 * Version: 1.0
 *
 * Modifies
 *  - v.1.0  (25/11/2014): Separation of the Toolbar from the main GUI.
 */
package org.rainbow.gui;

import org.rainbow.gui.language.*;
import org.rainbow.gui.gpfxRepository.IconUtilsRainbow;
import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * It is the Rainbow Toolbar.
 * 
 * @author Dalle Pezze Piero
 * @version 1.0
 */
public class RainbowToolbar {

	// ToolBar buttons
	private JButton btnSummary, btnForwardStep, btnBackwardStep;

	private JButton btnStart, btnEnd, btnSave, btnOpen;

	private JButton btnNew, btnNewRandom, btnModify;

	private JSpinner jSpinner;

	private JPanel toolPanel = null;

	private RainbowMainGUI rainbow = null;

	/** It creates a toolbar associated to a Rainbow GUI */
	public RainbowToolbar(RainbowMainGUI rainbow) {
		if (rainbow != null) {
			this.rainbow = rainbow;
			createToolBar();
		}
	}

	/**
	 * It creates a toolbar associated to a Rainbow GUI
	 * 
	 * @return a toolbar
	 */
	public JPanel setToolbar(RainbowMainGUI rainbow) {
		if (rainbow != null) {
			this.rainbow = rainbow;
			createToolBar();
			return toolPanel;
		}
		return null;
	}

	/** It sets the buttons for the "create simulation task" */
	public void setButtonsCreateSimulationMenu() {
		btnSummary.setEnabled(false);
	}

	/** It sets the buttons for the "move at end task" */
	public void setButtonsMoveAtEnd() {
		btnStart.setEnabled(true);
		btnBackwardStep.setEnabled(true);
	}

	/** It sets the buttons for the "stop simulation task" */
	public void setButtonsStopSimulation() {
		jSpinner.setEnabled(true);
		btnStart.setEnabled(true);
		btnEnd.setEnabled(true);
		btnForwardStep.setEnabled(true);
		btnBackwardStep.setEnabled(true);
	}

	/** It sets the buttons for the "move backward step task" */
	public void setButtonsMoveBackwardStep() {
		btnEnd.setEnabled(true);
		btnForwardStep.setEnabled(true);
	}

	/** It sets the buttons for the "move forward step task" */
	public void setButtonsMoveForwardStep() {
		btnStart.setEnabled(true);
		btnBackwardStep.setEnabled(true);
	}

	/** It sets the buttons for the "start simulation task" */
	public void setButtonsStartSimulation() {
		btnStart.setEnabled(false);
		btnEnd.setEnabled(true);
		btnForwardStep.setEnabled(true);
		btnBackwardStep.setEnabled(false);
	}

	/** It sets the buttons for the "terminate simulation task" */
	public void setButtonsTerminateSimulation() {
		btnStart.setEnabled(true);
		btnEnd.setEnabled(false);
		btnForwardStep.setEnabled(false);
		btnBackwardStep.setEnabled(true);
		jSpinner.setEnabled(true);
	}

	/** It sets the buttons for the "create simulation task" */
	public void setButtonsCreateSimulation() {
		btnSave.setEnabled(true);
		btnModify.setEnabled(true);
		jSpinner.setEnabled(true);
		btnSummary.setEnabled(true);
	}

	/**
	 * It returns the value for the Spinner control
	 * 
	 * @return the spinner control value
	 */
	public int getSpinnerValue() {
		return ((Integer) jSpinner.getValue()).intValue();
	}

	/**
	 * It returns the tool bar.
	 *
	 * @return the tool bar.
	 */
	public JPanel getToolBar() {
		return toolPanel;
	}

	/**
	 * It initialises the tool bar.
	 *
	 * @return the tool bar.
	 */
	private void createToolBar() {

		btnNew = new JButton(IconUtilsRainbow.getGeneralIcon("new"));
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.createConfiguration();
			}
		});
		btnNew.setToolTipText(Language.getOpenConfigurationTT());

		btnNewRandom = new JButton(IconUtilsRainbow.getGeneralIcon("new")); // change
																			// icon
		btnNewRandom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.createRandomConfiguration();
			}
		});
		btnNewRandom.setToolTipText("Random Configuration");

		btnModify = new JButton(IconUtilsRainbow.getGeneralIcon("edit"));
		btnModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.modifyConfiguration();
			}
		});
		btnModify.setToolTipText(Language.getModifyConfigurationTT());

		btnOpen = new JButton(IconUtilsRainbow.getGeneralIcon("open"));
		;
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.openConfigurationFile();
			}
		});
		btnOpen.setToolTipText(Language.getLoadSavedConfigurationTT());

		btnSave = new JButton(IconUtilsRainbow.getGeneralIcon("save"));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.saveConfiguration();
			}
		});
		btnSave.setToolTipText(Language.getSaveConfigurationTT());

		btnSummary = new JButton(IconUtilsRainbow.getGeneralIcon("summary"));
		btnSummary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.startConfigurationSummary();
			}
		});
		btnSummary.setToolTipText(Language.getConfigurationSummary());

		btnBackwardStep = new JButton(
				IconUtilsRainbow.getNavigationIcon("backward"));
		btnBackwardStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.moveBackwardStep();
			}
		});
		btnBackwardStep.setToolTipText(Language.getMoveBackwardStepTT());

		btnForwardStep = new JButton(
				IconUtilsRainbow.getNavigationIcon("forward"));
		btnForwardStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.moveForwardStep();
			}
		});
		btnForwardStep.setToolTipText(Language.getMoveForwardStepTT());

		btnStart = new JButton(IconUtilsRainbow.getNavigationIcon("begin"));
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.moveAtStart();
			}
		});
		btnStart.setToolTipText(Language.getMoveToInitialStateTT());

		btnEnd = new JButton(IconUtilsRainbow.getNavigationIcon("end"));
		btnEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.moveAtEnd();
			}
		});
		btnEnd.setToolTipText(Language.getMoveToFinalStateTT());

		jSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
		jSpinner.setMaximumSize(new Dimension(10, 40));
		jSpinner.setToolTipText(Language.getSetDelaySimulationTT());

		btnForwardStep.setEnabled(false);
		btnBackwardStep.setEnabled(false);
		btnStart.setEnabled(false);
		btnEnd.setEnabled(false);
		btnSave.setEnabled(false);
		btnModify.setEnabled(false);
		jSpinner.setEnabled(false);

		toolPanel = new JPanel();
		toolPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JToolBar toolBarFile = new JToolBar();
		toolBarFile.setRollover(true);
		toolBarFile.add(btnNew);
		toolBarFile.add(btnNewRandom);
		toolBarFile.add(btnModify);
		toolBarFile.add(btnOpen);
		toolBarFile.add(btnSave);
		toolBarFile.add(Box.createHorizontalGlue());
		toolBarFile.putClientProperty(Options.HEADER_STYLE_KEY,
				HeaderStyle.BOTH);

		JToolBar toolBarSimul = new JToolBar();
		toolBarSimul.setRollover(true);
		toolBarSimul.add(btnSummary);
		toolBarSimul.add(btnStart);
		toolBarSimul.add(btnBackwardStep);
		toolBarSimul.add(btnForwardStep);
		toolBarSimul.add(btnEnd);
		toolBarSimul.add(Box.createHorizontalGlue());
		toolBarSimul.putClientProperty(Options.HEADER_STYLE_KEY,
				HeaderStyle.BOTH);

		JToolBar toolBarFunc = new JToolBar();
		toolBarFunc.setRollover(true);
		toolBarFunc.add(jSpinner);
		toolBarFunc.add(Box.createHorizontalGlue());
		toolBarFunc.putClientProperty(Options.HEADER_STYLE_KEY,
				HeaderStyle.BOTH);

		toolPanel.add(toolBarFile);
		toolPanel.add(toolBarSimul);
		toolPanel.add(toolBarFunc);
	}

}
