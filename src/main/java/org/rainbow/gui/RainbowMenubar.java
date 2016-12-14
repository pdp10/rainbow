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
 * File: RainbowMenubar.java
 * Package: gui
 * Author: Dalle Pezze Piero
 * Date: 25/11/2014
 * Version: 1.0
 *
 * Modifies
 *  - v.1.0  (25/11/2014): Separation of the menubar from the main GUI.
 */
package org.rainbow.gui;

import net.infonode.docking.theme.*;
import org.rainbow.gui.language.*;
import org.rainbow.gui.gpfxRepository.IconUtilsRainbow;
import org.rainbow.gui.input.*;
import javax.help.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * It is the Rainbow Menubar.
 * 
 * @author Dalle Pezze Piero
 * @version 1.0
 */
public class RainbowMenubar {

	/** It contains the event management of the window menu. */
	private ActionListener lnrWindow = null;

	/** Elements of window menu. */
	private JCheckBoxMenuItem[] windowItems;

	// Simulation Menu buttons
	private JMenuItem mniForwardStep, mniStart, mniEnd, mniBackwardStep;

	// File Menu buttons
	private JMenuItem mniNew, mniOpen;

	private JMenuItem mniSave, mniModify, mniExportHTML;

	private JMenuItem mniExit, mniSummary;

	// Window Menu buttons
	private JMenuItem mniOpenProcesses, mniOpenResources;

	private JMenuItem mniOpenReady, mniOpenTerminated;

	private JMenuItem mniOpenStatistics, mniOpenResourcesGraph;

	private JMenuItem mniOpenProcessesGraph;

	private JMenuBar menubar = null;

	private RainbowMainGUI rainbow = null;

	/** It creates a menubar associated to a Rainbow GUI */
	public RainbowMenubar(RainbowMainGUI rainbow) {
		if (rainbow != null) {
			this.rainbow = rainbow;
			lnrWindow = new WindowActionListener();
			createMenubar();
		}
	}

	/**
	 * It creates a menubar associated to a Rainbow GUI
	 * 
	 * @return a toolbar
	 */
	public JMenuBar setMenubar(RainbowMainGUI rainbow) {
		if (rainbow != null) {
			this.rainbow = rainbow;
			lnrWindow = new WindowActionListener();
			createMenubar();
			return menubar;
		}
		return null;
	}

	/** It sets the buttons for the "create simulation task" */
	public void setMenuItemsCreateSimulationMenu() {
		mniSummary.setEnabled(false);
	}

	/** It sets the buttons for the "move at end task" */
	public void setMenuItemsMoveAtEnd() {
		mniStart.setEnabled(true);
		mniBackwardStep.setEnabled(true);
	}

	/** It sets the buttons for the "stop simulation task" */
	public void setMenuItemsStopSimulation() {
		mniStart.setEnabled(true);
		mniEnd.setEnabled(true);
		mniForwardStep.setEnabled(true);
		mniBackwardStep.setEnabled(true);
	}

	/** It sets the buttons for the "move backward step task" */
	public void setMenuItemsMoveBackwardStep() {
		mniEnd.setEnabled(true);
		mniForwardStep.setEnabled(true);
	}

	/** It sets the buttons for the "move forward step task" */
	public void setMenuItemsMoveForwardStep() {
		mniStart.setEnabled(true);
		mniBackwardStep.setEnabled(true);
	}

	/** It sets the buttons for the "start simulation task" */
	public void setMenuItemsStartSimulation() {
		mniStart.setEnabled(false);
		mniEnd.setEnabled(true);
		mniForwardStep.setEnabled(true);
		mniBackwardStep.setEnabled(false);
	}

	/** It sets the buttons for the "terminate simulation task" */
	public void setMenuItemsTerminateSimulation() {
		mniStart.setEnabled(true);
		mniEnd.setEnabled(false);
		mniForwardStep.setEnabled(false);
		mniBackwardStep.setEnabled(true);
	}

	/** It sets the buttons for the "create simulation task" */
	public void setMenuItemsCreateSimulation() {
		mniSave.setEnabled(true);
		mniModify.setEnabled(true);
		mniSummary.setEnabled(true);
		mniExportHTML.setEnabled(true);
	}

	/**
	 * It returns the tool bar.
	 *
	 * @return the tool bar.
	 */
	public JMenuBar getMenubar() {
		return menubar;
	}

	/** It manages events of the window menu. */
	private class WindowActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			Object source = e.getSource();

			if (source instanceof JCheckBoxMenuItem) {
				int selectedView = -1;
				JCheckBoxMenuItem currMenu = (JCheckBoxMenuItem) source;
				boolean openingMode = currMenu.isSelected();
				if (source.equals(mniOpenProcesses)) {
					selectedView = 0;
				} else if (source.equals(mniOpenResources)) {
					selectedView = 1;
				} else if (source.equals(mniOpenReady)) {
					selectedView = 2;
				} else if (source.equals(mniOpenTerminated)) {
					selectedView = 6;
				} else if (source.equals(mniOpenStatistics)) {
					selectedView = 5;
				} else if (source.equals(mniOpenResourcesGraph)) {
					selectedView = 4;
				} else if (source.equals(mniOpenProcessesGraph)) {
					selectedView = 3;
				}
				rainbow.openStaticView(selectedView, openingMode);
			}
		}
	}

	/**
	 * It creates the menubar frame.
	 *
	 * @return the menubar
	 */
	private JMenuBar createMenubar() {
		menubar = new JMenuBar();
		menubar.add(createFileMenu());
		menubar.add(createSimulationMenu());
		menubar.add(createWindowMenu());
		menubar.add(createThemesMenu());
		menubar.add(createLanguageMenu());
		menubar.add(createHelpMenu());
		return menubar;
	}

	/**
	 * It creates the file menu.
	 *
	 * @return the file menu
	 */
	private JMenu createFileMenu() {
		JMenu fileMenu = new JMenu(Language.getFile());

		mniNew = new JMenuItem(Language.getNewConfiguration(),
				IconUtilsRainbow.getGeneralIcon("new_16"));
		mniNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.createConfiguration();
			}
		});
		fileMenu.add(mniNew);

		mniOpen = new JMenuItem(Language.getOpenConfiguration(),
				IconUtilsRainbow.getGeneralIcon("open_16"));
		mniOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.openConfigurationFile();
			}
		});
		fileMenu.add(mniOpen);

		fileMenu.add(new JSeparator());
		mniSave = new JMenuItem(Language.getSave(),
				IconUtilsRainbow.getGeneralIcon("save_16"));
		mniSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.saveConfiguration();
			}
		});
		fileMenu.add(mniSave);
		mniSave.setEnabled(false);

		mniModify = new JMenuItem(Language.getModifyConfiguration(),
				IconUtilsRainbow.getGeneralIcon("edit_16"));
		mniModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.modifyConfiguration();
			}
		});
		fileMenu.add(mniModify);
		mniModify.setEnabled(false);

		fileMenu.add(new JSeparator());

		mniExportHTML = new JMenuItem(Language.getExportHTML(),
				IconUtilsRainbow.getGeneralIcon("html_16"));
		mniExportHTML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.exportHTML();
			}
		});
		fileMenu.add(mniExportHTML);
		mniExportHTML.setEnabled(false);

		fileMenu.add(new JSeparator());
		mniExit = new JMenuItem(Language.getExit(),
				IconUtilsRainbow.getGeneralIcon("exit_16"));
		mniExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.exit();
			}
		});
		fileMenu.add(mniExit);

		mniSave.setEnabled(false);
		mniExportHTML.setEnabled(false);
		return fileMenu;
	}

	/**
	 * It creates the simulation menu.
	 *
	 * @return The simulation menu.
	 */
	private JMenu createSimulationMenu() {
		JMenu simulationMenu = new JMenu(Language.getSimulation());
		mniSummary = new JMenuItem(Language.getConfigurationSummary(),
				IconUtilsRainbow.getGeneralIcon("summary_16"));
		mniSummary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.startConfigurationSummary();
			}
		});
		simulationMenu.add(mniSummary);
		simulationMenu.add(new JSeparator());

		mniStart = new JMenuItem(Language.getMoveToInitialStateTT(),
				IconUtilsRainbow.getNavigationIcon("begin_16"));
		mniStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.moveAtStart();
			}
		});
		simulationMenu.add(mniStart);

		mniBackwardStep = new JMenuItem(Language.getMoveBackwardStepTT(),
				IconUtilsRainbow.getNavigationIcon("backward_16"));
		mniBackwardStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.moveBackwardStep();
			}
		});
		simulationMenu.add(mniBackwardStep);

		mniForwardStep = new JMenuItem(Language.getMoveForwardStepTT(),
				IconUtilsRainbow.getNavigationIcon("forward_16"));
		mniForwardStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.moveForwardStep();
			}
		});
		simulationMenu.add(mniForwardStep);

		mniEnd = new JMenuItem(Language.getMoveToFinalStateTT(),
				IconUtilsRainbow.getNavigationIcon("end_16"));
		mniEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.moveAtEnd();
			}
		});
		simulationMenu.add(mniEnd);

		mniForwardStep.setEnabled(false);
		mniBackwardStep.setEnabled(false);
		mniStart.setEnabled(false);
		mniEnd.setEnabled(false);
		mniSummary.setEnabled(false);
		rainbow.getToolbar().setButtonsCreateSimulationMenu();

		return simulationMenu;
	}

	/**
	 * It creates the window menu.
	 *
	 * @return The window menu.
	 */
	private JMenu createWindowMenu() {
		JMenu windowMenu = new JMenu(Language.getViews());

		windowItems = new JCheckBoxMenuItem[rainbow.getViews()
				.getNumberOfViews()];

		JMenuItem mniDefaultLayout = new JMenuItem(Language.getDefaultLayout());
		mniDefaultLayout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.setDefaultLayout();
				for (int i = 0; i < windowItems.length; i++)
					(windowItems[i]).setState(true);
			}
		});
		windowMenu.add(mniDefaultLayout);

		JMenuItem mniMaximuxLayout = new JMenuItem(Language.getMaximumLayout(),
				IconUtilsRainbow.getGeneralIcon("viewmag_16"));
		mniMaximuxLayout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.setMaximuxLayout();
				for (int i = 0; i < windowItems.length; i++)
					(windowItems[i]).setState(true);
			}
		});
		windowMenu.add(mniMaximuxLayout);

		windowMenu.add(new JSeparator());

		windowItems[0] = new JCheckBoxMenuItem(
				Language.getGraphOfRunningProcesses(),
				IconUtilsRainbow.getPanelIcon("function"), true);
		mniOpenProcessesGraph = windowItems[0];
		mniOpenProcessesGraph.setMnemonic(KeyEvent.VK_O);
		mniOpenProcessesGraph.addActionListener(lnrWindow);
		windowMenu.add(mniOpenProcessesGraph);

		windowItems[1] = new JCheckBoxMenuItem(
				Language.getGraphOfResourcesAssignment(),
				IconUtilsRainbow.getPanelIcon("shapegraph"), true);
		mniOpenResourcesGraph = windowItems[1];
		mniOpenResourcesGraph.setMnemonic(KeyEvent.VK_A);
		mniOpenResourcesGraph.addActionListener(lnrWindow);
		windowMenu.add(mniOpenResourcesGraph);

		windowItems[2] = new JCheckBoxMenuItem(Language.getReadyQueue(),
				IconUtilsRainbow.getPanelIcon("table"), true);
		mniOpenReady = windowItems[2];
		mniOpenReady.setMnemonic(KeyEvent.VK_C);
		mniOpenReady.addActionListener(lnrWindow);
		windowMenu.add(mniOpenReady);

		windowItems[3] = new JCheckBoxMenuItem(Language.getTerminated(),
				IconUtilsRainbow.getPanelIcon("table"), true);
		mniOpenTerminated = windowItems[3];
		mniOpenTerminated.setMnemonic(KeyEvent.VK_T);
		mniOpenTerminated.addActionListener(lnrWindow);
		windowMenu.add(mniOpenTerminated);

		windowItems[4] = new JCheckBoxMenuItem(Language.getProcesses(),
				IconUtilsRainbow.getPanelIcon("table"), true);
		mniOpenProcesses = windowItems[4];
		mniOpenProcesses.setMnemonic(KeyEvent.VK_P);
		mniOpenProcesses.addActionListener(lnrWindow);
		windowMenu.add(mniOpenProcesses);

		windowItems[5] = new JCheckBoxMenuItem(Language.getResources(),
				IconUtilsRainbow.getPanelIcon("table"), true);
		mniOpenResources = windowItems[5];
		mniOpenResources.setMnemonic(KeyEvent.VK_R);
		mniOpenResources.addActionListener(lnrWindow);
		windowMenu.add(mniOpenResources);

		windowItems[6] = new JCheckBoxMenuItem(Language.getStatistics(),
				IconUtilsRainbow.getPanelIcon("statistics"), true);
		mniOpenStatistics = windowItems[6];
		mniOpenStatistics.setMnemonic(KeyEvent.VK_S);
		mniOpenStatistics.addActionListener(lnrWindow);
		windowMenu.add(mniOpenStatistics);

		return windowMenu;
	}

	/**
	 * Creates the menu where the theme can be changed.
	 *
	 * @return the theme menu
	 */
	private JMenu createThemesMenu() {
		JMenu themesMenu = new JMenu(Language.getThemes());

		DockingWindowsTheme[] themes = { new DefaultDockingTheme(),
				new LookAndFeelDockingTheme(), new BlueHighlightDockingTheme(),
				new SlimFlatDockingTheme(), new GradientDockingTheme(),
				new ShapedGradientDockingTheme(),
				new SoftBlueIceDockingTheme(), new ClassicDockingTheme() };

		// WE ONLY USE SYSTEM NOW.
		// the code is left if a new look 'n' feel is added at some point
		// String[] laf = {"System"};
		// ButtonGroup groupLAF = new ButtonGroup();
		// for (int i = 0; i < laf.length; i++) {
		// JRadioButtonMenuItem item = new JRadioButtonMenuItem(laf[i] +
		// " Look And Feel");
		// item.setSelected(laf[i].equals(RainbowConfig.getLookAndFeel()));
		// item.setToolTipText(Language.getLookAndFeelTT());
		// groupLAF.add(item);
		// final String lookAndFeel = laf[i];
		// themesMenu.add(item).addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// rainbow.setLookAndFeel();
		// RainbowConfig.saveLookAndFeel(lookAndFeel);
		// }
		// });
		// }
		// themesMenu.add(new JSeparator());

		ButtonGroup group = new ButtonGroup();

		for (int i = 0; i < themes.length; i++) {
			final DockingWindowsTheme theme = themes[i];

			JRadioButtonMenuItem item = new JRadioButtonMenuItem(
					theme.getName());
			item.setSelected(theme.getName().equals(RainbowConfig.getTheme()));
			group.add(item);

			themesMenu.add(item).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					rainbow.setTheme(theme);
				}
			});
		}

		return themesMenu;
	}

	/**
	 * It creates the language menu.
	 *
	 * @return The language menu.
	 */
	private JMenu createLanguageMenu() {
		JMenu languageMenu = new JMenu(Language.getLanguage());
		final Language.LanguageItem[] languages = Language.getLanguages();
		for (int i = 0; i < languages.length; i++) {
			final Language.LanguageItem lang = languages[i];
			JMenuItem mnLang = new JMenuItem(lang.getName(),
					IconUtilsRainbow.getFlagsIcon(lang.getIconID()));
			languageMenu.add(mnLang);
			mnLang.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					RainbowConfig.saveLanguage(lang.getName());
					Language.setLanguage(lang.getName());
					rainbow.rebootRainbow();
				}
			});
		}
		return languageMenu;
	}

	/**
	 * It creates the help menu.
	 *
	 * @return The help menu
	 */
	private JMenu createHelpMenu() {
		JMenu helpMenu = new JMenu(Language.getHelp());
		JMenuItem mniHelp = new JMenuItem(Language.getHelp(),
				IconUtilsRainbow.getHelpIcon("help_16"));
		final HelpBroker hb = rainbow.getHelpBroker();
		final HelpSet hs = rainbow.getHelpSet();
		mniHelp.addActionListener(new CSH.DisplayHelpFromSource(hb) {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				String helpId = e.getActionCommand();
				try {
					hb.setCurrentID(Map.ID.create(helpId, hs));
					hs.setHomeID(helpId);
				} catch (BadIDException ex) {
					System.out.println("Target " + helpId
							+ " mistaked in createHelpMenu (BadIDException)");
				} catch (InvalidHelpSetContextException ex) {
					System.out
							.println("Target "
									+ helpId
									+ " mistaked in createHelpMenu (InvalidHelpSetContextException)");
				}
				super.actionPerformed(e);
			}
		});
		mniHelp.setActionCommand("index");
		helpMenu.add(mniHelp);

		JMenuItem mniAbout = new JMenuItem(Language.getAbout(),
				IconUtilsRainbow.getHelpIcon("info_16"));
		mniAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rainbow.getAbout().setVisible(true);
			}
		});
		helpMenu.add(mniAbout);

		return helpMenu;
	}

}
