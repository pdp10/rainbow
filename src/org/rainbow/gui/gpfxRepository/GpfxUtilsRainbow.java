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
 * File: gpfxUtilsRainbow.java
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

import java.awt.*;

/**
 * This class defines functionalities to draw graphics.
 * 
 * @author Sarto Carlo
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class GpfxUtilsRainbow {

	/**
	 * It defines functionality to draw arrows, by using colors and coordinates.
	 * 
	 * @param g2d
	 *            An object of type Graphics2D. It is the graph on which arrows
	 *            are pictured.
	 * @param xCenter
	 *            It is the x coordinate of the first point from the center.
	 * @param yCenter
	 *            It is the y coordinate of the second point from the center.
	 * @param x
	 *            It is the x coordinate of the first point.
	 * @param y
	 *            It is the y coordinate of the second point.
	 * @param stroke
	 *            It is the stroke of the arrow.
	 */
	public static void drawArrow(Graphics2D g2d, int xCenter, int yCenter,
			int x, int y, float stroke) {
		double aDir = Math.atan2(xCenter - x, yCenter - y);
		g2d.drawLine(x, y, xCenter, yCenter);
		g2d.setStroke(new BasicStroke(1f));

		// it makes the arrow head solid even if dash pattern has been specified
		Polygon tmpPoly = new Polygon();
		int i1 = 12 + (int) (stroke * 2);
		int i2 = 6 + (int) stroke;

		// it makes the arrow head the same size regardless of the length length
		// arrow tip
		tmpPoly.addPoint(x, y);
		tmpPoly.addPoint(x + xCor(i1, aDir + .3), y + yCor(i1, aDir + .3));
		tmpPoly.addPoint(x + xCor(i2, aDir), y + yCor(i2, aDir));
		tmpPoly.addPoint(x + xCor(i1, aDir - .3), y + yCor(i1, aDir - .3));
		// arrow tip
		tmpPoly.addPoint(x, y);

		g2d.drawPolygon(tmpPoly);
		// it removes this line to leave arrow head unpainted
		g2d.fillPolygon(tmpPoly);

	}

	private static int yCor(int len, double dir) {
		return (int) (len * Math.cos(dir));
	}

	private static int xCor(int len, double dir) {
		return (int) (len * Math.sin(dir));
	}

	/**
	 * It generates a different colour from index reference and of chromatic
	 * interval "steps".
	 * 
	 * @param steps
	 *            offset chromatic.
	 * @param index
	 *            index of the reference colour.
	 * @return Colour the colour generated.
	 */
	public static Color colorFactory(int steps, int index) {
		/*
		 * It chooses a maximum number of colours, 256*6, dividing per the
		 * number of processes. -> chromatic interval.
		 */
		int step = 1536 / steps;

		// It computes the index of the associated colour to the process
		// specified.
		int procColorIndex = step * (index - 1);

		int red = 0;
		int green = 0;
		int blue = 0;

		int stepTot = procColorIndex / 256;
		int stepModule = (procColorIndex % 256);

		switch (stepTot) {
		case 0:
			red = 255;
			green = stepModule;
			break;
		case 1:
			red = 255 - stepModule;
			green = 255;
			break;
		case 2:
			green = 255;
			blue = stepModule;
			break;
		case 3:
			blue = 255;
			green = 255 - stepModule;
			break;
		case 4:
			blue = 255;
			red = stepModule;
			break;
		case 5:
			red = 255;
			blue = 255 - stepModule;
			break;
		default:
			break;
		}
		return new Color(red, green, blue);
	}
}