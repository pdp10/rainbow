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
 * File: ViewRunningGraph.java
 * Package: gui.view
 * Author: Fabio Gallonetto, Sarto Carlo, Dalle Pezze Piero
 * Date: 13/02/2005
 * Version: 1.2
 * 
 * Modifies
 *  - v.1.2 (03/05/2007): English translation and Java 6 support. Dalle Pezze Piero.
 *  - v.1.1 (05/02/2006): Adaptation methods to SGPEMv2 engine. Sarto Carlo.
 *  - v.1.0 (13/02/2005): Design and implementation of the class. Fabio Gallonetto. (SGPEMv1).
 */
package org.rainbow.gui.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

import org.rainbow.gui.gpfxRepository.GpfxUtilsRainbow;

/**
 * It draws and paints the graph of running processes.
 * 
 * @author Fabio Gallonetto
 * @author Sarto Carlo
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class ViewRunningGraph extends JPanel {

	/** To serialize. */
	private static final long serialVersionUID = -3021217566431305374L;

	/** It implements all methods to draw. */
	private class JPanelGraphic extends JPanel {

		/** To serialize. */
		private static final long serialVersionUID = -8491593576327143055L;

		/** The default constructor. */
		public JPanelGraphic() {
			super();
		}

		/** Current dimension of the horizontal side of the grid. */
		private int xOffset = 10;

		/** Current dimension of the vertical side of the grid. */
		private int yOffset = 10;

		/** Minimum dimension of the horizontal side of the grid. */
		private int gridX = 10;

		/** Minimum dimension of the vertical side of the grid. */
		private int gridY = 10;

		/** A string with the longest process' name. */
		private String currMaxProcString = "o";

		/**
		 * It defines how many chars of the process' name are viewed.
		 */
		private int maxProcStringSize = 10;

		/** It contains processes' names. */
		ArrayList<String> procName = new ArrayList<String>();

		/** ArrayList of running process id. */
		ArrayList<Integer> instants = new ArrayList<Integer>();

		/**
		 * It paints a set of instants on a graph.
		 * 
		 * @param processesID
		 *            An sorted array of id of running processes.
		 */
		public void drawGraph(int[] processID) {
			instants = new ArrayList<Integer>();
			for (int i = 0; i < processID.length; i++) {
				instants.add(new Integer(processID[i]));
			}
			// It updates the graph.
			repaint();
			// It updates the size of the graph.
			resizeControl();
		}

		/**
		 * It initializes the graph. It sets the graph at start.
		 * 
		 * @param procNameRef
		 *            It is a vector containing the names of processes created
		 *            in the configuration.
		 */
		public void initializeGraph(ArrayList<String> procNameRef) {
			instants = new ArrayList<Integer>();
			procName = procNameRef;
			for (int i = 0; i < procName.size(); i++) {
				String name = procName.get(i);
				if (currMaxProcString.length() < name.length())
					currMaxProcString = name;
			}
			// It updates the graph.
			repaint();
			// It updates the size of the graph.
			resizeControl();
		}

		/**
		 * It resizes the dimension of the graph by follow how many instants are
		 * drawn.
		 */
		private void resizeControl() {

			/*
			 * It computes the width of the control.
			 */
			int newWidth = (instants.size() + 4) * gridX + xOffset;

			// it sets the new width. The height remains the same
			setPreferredSize(new Dimension(newWidth, gridY * procName.size()
					+ yOffset));

			// It notifies the changing to the JScrollPane.
			revalidate();

			/*
			 * It moves the scroll at the current time.
			 */
			scrollRectToVisible(new Rectangle(newWidth + 10, 0, newWidth + 10,
					1));
			revalidate();
		}

		/**
		 * It draws all elements on the graph.
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			// the current width
			int currWidth;
			// the constant current height
			int currHeight = getHeight();
			// the old grid
			int oldXGrid;

			/*
			 * It is a safe cast because paint always invokes paintComponent
			 * with a Graphics2D object.
			 */
			Graphics2D g2 = (Graphics2D) g;

			// It actives the antialiasing to improve the quality of the
			// rendering.
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);

			// It sets the background color.
			setBackground(Color.white);

			/*
			 * It sets horizontal offsets of axiss.
			 */
			if (maxProcStringSize > currMaxProcString.length())
				xOffset = (int) (new TextLayout(currMaxProcString,
						this.getFont(), g2.getFontRenderContext()))
						.getAdvance();
			else
				xOffset = (int) (new TextLayout(currMaxProcString.substring(0,
						maxProcStringSize - 2) + "..", this.getFont(),
						g2.getFontRenderContext())).getAdvance();
			xOffset += (int) (new TextLayout("O", this.getFont(),
					g2.getFontRenderContext())).getAdvance();

			/*
			 * the vertical offsets vertical is fixed.
			 */
			yOffset = (int) (1.5f * (new TextLayout("O", this.getFont(),
					g2.getFontRenderContext())).getAscent());

			/*
			 * The height of the grid is fixed.
			 */
			gridY = yOffset;

			/*
			 * It sets the number of chars of the greatest instant.
			 */
			int maxInstantDigits = (new Integer(instants.size())).toString()
					.length();

			oldXGrid = gridX;

			/*
			 * The width of the grid must consider the dinstance between
			 * instants.
			 */
			if (maxInstantDigits < 3)
				gridX = gridY;
			else
				gridX = maxInstantDigits
						* (int) (new TextLayout("O", this.getFont(),
								g2.getFontRenderContext())).getAdvance();

			// It sets the new width computed.
			currWidth = gridX * instants.size() + xOffset + 2;

			int axisLength = currWidth;
			if (currWidth < getWidth())
				axisLength = getWidth();

			/*
			 * It draws the X axis
			 */
			g2.fillRect(xOffset, currHeight - yOffset, axisLength, 2);

			// It draws the Y axis
			g2.fillRect(xOffset, 0, 2, currHeight - yOffset);

			/*
			 * It draws the processes' labels
			 */
			for (int i = procName.size(); i > 0; i--) {

				// The process' name
				String text = procName.get(i - 1);

				if (text.length() > maxProcStringSize)
					text = text.substring(0, maxProcStringSize - 2) + "..";

				TextLayout label = new TextLayout(text, this.getFont(),
						g2.getFontRenderContext());

				int x = (xOffset - (int) label.getAdvance()) / 2;
				int y = currHeight - yOffset - gridY * (i - 1);

				label.draw(g2, x, y - (int) label.getAscent() / 2);

				g.setColor(Color.LIGHT_GRAY);
				g.drawLine(xOffset, y, axisLength, y);
				g.setColor(Color.BLACK);
			}

			g.setColor(Color.LIGHT_GRAY);
			g.drawLine(xOffset, currHeight - yOffset - gridY * procName.size(),
					axisLength, currHeight - yOffset - gridY * procName.size());

			g.setColor(Color.BLACK);
			for (int i = 0; i <= instants.size(); i++) {

				String text = String.valueOf(i);
				TextLayout label = new TextLayout(text, this.getFont(),
						g2.getFontRenderContext());

				int x = xOffset + gridX * i - (int) label.getAdvance() / 2;
				int y = currHeight - yOffset + (int) label.getAscent() * 3 / 2;

				label.draw(g2, x, y);

			}

			// It re-paints the instants
			for (int i = 0; i < instants.size(); i++) {
				int procIdx = instants.get(i).intValue();
				if (procIdx == 0)
					continue;
				int y = 1 + currHeight - yOffset - gridY * procIdx;
				int x = 2 + xOffset + gridX * i;

				/*
				 * It sets a 3d effect.
				 */
				GradientPaint gradient = new GradientPaint(
						x,
						y,
						GpfxUtilsRainbow.colorFactory(procName.size(), procIdx),
						x, y + (gridY / 2), Color.WHITE, true);
				g2.setPaint(gradient);

				g.fillRect(x, y, gridX, gridY - 1);

				g.setColor(Color.BLACK);

				if (i < instants.size() - 1
						&& instants.get(i + 1).intValue() != procIdx)
					g.drawLine(gridX + x, y, gridX + x, this.getHeight()
							- yOffset);
				else if (i == instants.size() - 1)
					g.drawLine(gridX + x, y, gridX + x, this.getHeight()
							- yOffset);

				if (i > 0 && instants.get(i - 1).intValue() != procIdx)
					g.drawLine(x, y, x, this.getHeight() - yOffset);

			}

			if (gridX != oldXGrid)
				resizeControl();
		}
	}

	/** Component JPanelGraphic. It contains the graph. */
	JPanelGraphic x = new JPanelGraphic();

	/** It contains the JPanelGraphic. */
	JScrollPane base = new JScrollPane();

	/**
	 * The state of the simulation.
	 */
	JLabel jLabel = new JLabel();

	/**
	 * It sets the text of the label that tells the state of the simulation.
	 * 
	 * @param text
	 *            the label that tells the state of the simulation.
	 */
	public void setLabelText(String text) {
		jLabel.setText(text);
		jLabel.setForeground(Color.GRAY);
	}

	public void scrollUp() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (base.getVerticalScrollBar() != null) {
					JScrollBar scroller = base.getVerticalScrollBar();
					scroller.setValue(scroller.getMaximum());
				}
			}
		});
	}

	/** It resets the text of the label that tells the state of the simulation. */
	public void resetLabelText() {
		jLabel.setText("");
	}

	/** The constructor of the class. */
	public ViewRunningGraph() {
		super();
		jLabel.setText("");
		jLabel.setPreferredSize(new java.awt.Dimension(31, 15));
		jLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		this.setLayout(new BorderLayout());
		base.setViewportView(x);
		this.add(base, BorderLayout.CENTER);
		this.add(jLabel, BorderLayout.NORTH);

	}

	/**
	 * It initializes the graph. It sets the graph at start of the simulation.
	 * 
	 * @param procNameRef
	 *            A vector of processes' names.
	 */
	public void initialize(ArrayList<String> procNameRef) {
		x.initializeGraph(procNameRef);
		scrollUp();
	}

	/**
	 * It initializes the graph. It sets the graph at start.
	 * 
	 * @param processID
	 *            It is a vector containing the names of processes created in
	 *            the configuration.
	 */
	public void drawGraph(int[] processID) {
		x.drawGraph(processID);
		scrollUp();
	}
}
