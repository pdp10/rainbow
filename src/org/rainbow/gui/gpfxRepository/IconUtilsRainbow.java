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
 * File: IconUtilsBlueThoth.java
 * Package: gui.gpfxRepository
 * Author: Sarto Carlo, Dalle Pezze Piero
 * Date: 10/02/2006
 * Version: 1.2
 *
 * Modifies:
 *  - v.1.2 (16/04/2007): English translation. Java6 compatible. (Piero Dalle Pezze)
 *  - v.1.1 (16/02/2006): Documentation. Sarto Carlo.
 *  - v.1.0 (10/02/2006): Codify. Sarto Carlo.
 * */

package org.rainbow.gui.gpfxRepository;

import javax.swing.*;
import java.net.URL;

/**
 * This class defined functionalities to manage icons.
 * 
 * @author Sarto Carlo
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class IconUtilsRainbow {

	private static IconUtilsRainbow iconUtils = null;

	/**
	 * It returns an icon by specify a category and a name.
	 * 
	 * @param category
	 *            It can be: general, help, navigation, panel wizard
	 * @param name
	 *            The name of the icon.
	 */
	public ImageIcon getIcon(String category, String name) {

		// Build the URL String
		String imageName = "/rainbow/" + category + "/" + name + ".png";

		// Get a URL pointing to the image
		URL iconURL = this.getClass().getResource(imageName);
		// System.out.println( "Icon URL: " + iconURL +" f " + imageName );
		if (iconURL == null)
			return null;
		// Build and return a new ImageIcon built from this URL
		return new ImageIcon(iconURL);
	}

	/**
	 * It returns an icon of the category "general", by specify the name.
	 * 
	 * @param name
	 *            The name of the icon.
	 */
	public static ImageIcon getGeneralIcon(String name) {
		if (iconUtils == null) {
			iconUtils = new IconUtilsRainbow();
		}

		return iconUtils.getIcon("general", name);
	}

	/**
	 * It returns an icon of the category "panel", by specify the name.
	 * 
	 * @param name
	 *            The name of the icon.
	 */
	public static ImageIcon getPanelIcon(String name) {
		if (iconUtils == null) {
			iconUtils = new IconUtilsRainbow();
		}

		return iconUtils.getIcon("panel", name);
	}

	/**
	 * It returns an icon of the category "navigation", by specify the name.
	 * 
	 * @param name
	 *            The name of the icon.
	 */
	public static ImageIcon getNavigationIcon(String name) {
		if (iconUtils == null) {
			iconUtils = new IconUtilsRainbow();
		}

		return iconUtils.getIcon("navigation", name);
	}

	/**
	 * It returns an icon of the category "help", by specify the name.
	 * 
	 * @param name
	 *            The name of the icon.
	 */
	public static ImageIcon getHelpIcon(String name) {
		if (iconUtils == null) {
			iconUtils = new IconUtilsRainbow();
		}

		return iconUtils.getIcon("help", name);
	}

	/**
	 * It returns an icon of the category "flags", by specify the name.
	 * 
	 * @param name
	 *            The name of the icon.
	 */
	public static ImageIcon getFlagsIcon(String name) {
		if (iconUtils == null) {
			iconUtils = new IconUtilsRainbow();
		}

		return iconUtils.getIcon("flags", name);
	}

}