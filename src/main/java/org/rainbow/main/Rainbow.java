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
 * File: Rainbow.java
 * Package: main
 * Author: Dalle Pezze Piero
 * Date: 31/01/2007
 * Version: 1.1
 *
 * Modifies:
 * v.1.1 (30/05/2007): Look 'n' feel improvements.
 * v.1.0 (31/01/2007): Documentation and codify.
 */

package org.rainbow.main;

import org.rainbow.gui.*;
import org.rainbow.gui.input.*;
import javax.swing.*;

/**
 * It starts the Rainbow application.
 *
 * @author Dalle Pezze Piero
 * @version 1.1
 */

public class Rainbow {
	public static void main(String[] args) { // throws Exception {

		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("swing.aatext", "true");

		RainbowConfig.setRainbowConfig();
		String savedLAF = RainbowConfig.getLookAndFeel();

		try {
			if (savedLAF.equals("System")) {
				// Set the system Look and Feel
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} else {
				// DEFAULT
				System.out.println("Look And Feel " + savedLAF
						+ " doesn't exists!\nDefault look and feel: System");
				// Set InfoNode Look and Feel
				RainbowConfig.saveLookAndFeel("System");
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			}
		} catch (Exception e) {
			System.out.println("Look And Feel is not available!");
			System.out.println(e.getStackTrace());
		}

		// Docking windows should be run in the Swing thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new RainbowMainGUI();
			}
		});

	}
}
