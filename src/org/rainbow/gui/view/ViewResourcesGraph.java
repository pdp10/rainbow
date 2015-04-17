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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;

import org.rainbow.gui.gpfxRepository.*;
import org.rainbow.simulation.*;
import java.util.ArrayList;
import org.rainbow.data.*;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * It draws and paints the graph of assignment of the resources.
 * 
 * @author Fabio Gallonetto
 * @author Sarto Carlo
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class ViewResourcesGraph extends JScrollPane {

	/** To serialize. */
	private static final long serialVersionUID = 1L;

	private class JPanelGraph extends JPanel {
		/** To serialize. */
		private static final long serialVersionUID = 1494461726230074582L;

		/**
		 * Current dimension of the horizontal side of the rectangle of the
		 * resource.
		 */
		private int resXEdge = 10;

		/**
		 * Current dimension of the vertical side of the rectangle of the
		 * resource.
		 */
		private int resYEdge = 10;

		/**
		 * Current dimension of the horizontal side of the circle of the
		 * process.
		 */
		private int procXEdge = 10;

		/** Current dimension of the vertical side of the circle of the process. */
		private int procYEdge = 10;

		/** It contains the greatest resource's name. */
		private String currMaxResString = "o";

		/** It contains the greatest process' name. */
		private String currMaxProcString = "o";

		/**
		 * The maximum size of the name of a resource.
		 */
		private final int maxResStringSize = 5;

		/**
		 * The maximum size of the name of a process.
		 */
		private final int maxProcStringSize = 5;

		private State state = null;

		/** ArrayList of resources. */
		private ArrayList<Resource> resources = new ArrayList<Resource>();

		/** ArrayList of processes. */
		private ArrayList<SimulatedProcess> processes = new ArrayList<SimulatedProcess>();
		

		/**
		 * White colour for preemptive resource, grey colour for not preemptive
		 * one. Inside the square there are the name and the multiplicity of the
		 * resource.
		 * 
		 * @param g2
		 *            The graph.
		 */
		private void drawResources(Graphics2D g2) {
			// It computes the dimensions
			if (maxResStringSize > currMaxResString.length())
				resXEdge = (int) (new TextLayout(currMaxResString, this
						.getFont(), g2.getFontRenderContext())).getAdvance();
			else
				resXEdge = (int) (new TextLayout(currMaxResString.substring(0,
						maxResStringSize - 2)
						+ "..", this.getFont(), g2.getFontRenderContext()))
						.getAdvance();
			resXEdge += 3 * (int) (new TextLayout("O", this.getFont(), g2
					.getFontRenderContext())).getAdvance();

			int maxYResEdge = 5 * (int) (new TextLayout("O", this.getFont(), g2
					.getFontRenderContext())).getAscent();

			if (resXEdge > maxYResEdge)
				resYEdge = maxYResEdge;
			else
				resYEdge = resXEdge;

			for (int i = 1; i <= resources.size(); i++) {
				int x = 2 + (resXEdge + 10) * (i - 1);
				int y = 2;

				String text = resources.get(i - 1).getName();
				if (text.length() > maxResStringSize)
					text = text.substring(0, maxResStringSize - 2) + "..";
				TextLayout label = new TextLayout(text, this.getFont(), g2
						.getFontRenderContext());

				if (resources.get(i - 1) instanceof PreemptiveResource)
					g2.setColor(Color.WHITE);
				else
					g2.setColor(Color.LIGHT_GRAY);

				g2.fillRect(x, y, resXEdge, resYEdge);

				g2.setColor(Color.BLACK);
				g2.drawRect(x, y, resXEdge, resYEdge);

				label.draw(g2, x + resXEdge / 2 - label.getAdvance() / 2, y
						+ (resYEdge / 9) * 2 + label.getAscent());

				// It writes the multiplicity
				String mult = "M: "
						+ new Integer(resources.get(i - 1).getMultiplicity())
								.toString();
				label = new TextLayout(mult, this.getFont(), g2
						.getFontRenderContext());

				label.draw(g2, x + resXEdge / 2 - label.getAdvance() / 2, y
						+ (resYEdge / 9) * 6 + label.getAscent());

			}
		}

		/**
		 * It draws processes with a yellow background.
		 * 
		 * @param g2
		 *            The graph.
		 */
		private void drawProcesses(Graphics2D g2) {
			// it computes dimensions.
			if (maxProcStringSize > currMaxProcString.length())
				procXEdge = (int) (new TextLayout(currMaxProcString, this
						.getFont(), g2.getFontRenderContext())).getAdvance();
			else
				procXEdge = (int) (new TextLayout(currMaxProcString.substring(
						0, maxProcStringSize - 2)
						+ "..", this.getFont(), g2.getFontRenderContext()))
						.getAdvance();
			procXEdge += 3 * (int) (new TextLayout("O", this.getFont(), g2
					.getFontRenderContext())).getAdvance();

			int maxYProcEdge = 3 * (int) (new TextLayout("O", this.getFont(),
					g2.getFontRenderContext())).getAscent();

			if (procYEdge > maxYProcEdge)
				procYEdge = maxYProcEdge;
			else
				procYEdge = procXEdge;

			// it draws processes.
			for (int i = 1; i <= processes.size(); i++) {
				int x = 2 + (procXEdge + 10) * (i - 1);
				int y = this.getHeight() - 2 - procXEdge;
				SimulatedProcess p = processes.get(i - 1);
				String text = (p.getName());

				if (text.length() > maxProcStringSize)
					text = text.substring(0, maxProcStringSize - 2) + "..";

				TextLayout label = new TextLayout(text, this.getFont(), g2
						.getFontRenderContext());

				// e circle is yellow
				g2.setColor(Color.YELLOW);
				g2.fillOval(x, y, procXEdge, procXEdge);

				g2.setColor(Color.BLACK);
				g2.drawOval(x, y, procXEdge, procXEdge);

				label.draw(g2, x + procXEdge / 2 - label.getAdvance() / 2, y
						+ procXEdge / 2 + label.getAscent() / 2);
			}
		}

		/**
		 * It draws arrows among processes and resources. So it represents
		 * attributions and requests.
		 * 
		 * @param g2
		 *            The graph.
		 */
		private void drawRelations(Graphics2D g2) {
			if (state != null) {
				ArrayList<ResourceAttribution> blocked = state
						.getBlockedQueues();
				// Disegno le frecce delle relazioni di richiesta risorsa
				for (int i = 0; i < blocked.size(); i++) {
					ResourceAttribution blockedList = blocked.get(i);

					if (blockedList != null) {
						String resName;
						String procName;

						for (int j = 0; j < blockedList.getProcessList().size(); j++) {

							resName = blockedList.getResource().getName();
							if (!processes.contains(blockedList
									.getProcessList().get(j)))
								processes.add(blockedList
										.getProcessList().get(j));
							procName = (blockedList.getProcessList().get(j))
									.getName();

							int procPos = -1;
							int resPos = -1;

							for (int y = 0; y < resources.size()
									&& resPos == -1; y++)
								if (resources.get(y).getName().compareTo(
										resName) == 0)
									resPos = y;

							for (int y = 0; y < processes.size()
									&& procPos == -1; y++)
								if (processes.get(y).getName().compareTo(
										procName) == 0)
									procPos = y;
							if (procPos == -1 || resPos == -1)
								continue;

							int x1 = resPos * (resXEdge + 10) + resXEdge / 2
									+ 2;
							int y1 = resYEdge + 5;
							int x2 = procPos * (procXEdge + 10) + procXEdge / 2
									+ 2;
							int y2 = this.getHeight() - 2 - procXEdge - 5;

							g2.setColor(Color.BLACK);
							GpfxUtilsRainbow
									.drawArrow(g2, x2, y2, x1, y1, 1.0f);
						}
					}
				}

				ArrayList<ResourceAttribution> attributions = state
						.getAttributedResources();
				for (int i = 0; i < attributions.size(); i++) {
					ResourceAttribution attribution = attributions.get(i);

					if (attribution != null) {
						String resName;
						String procName;

						for (int j = 0; j < attribution.getProcessList().size(); j++) {

							resName = attribution.getResource().getName();
							if (!processes.contains(attribution
									.getProcessList().get(j)))
								processes.add(attribution
										.getProcessList().get(j));
							procName = (attribution.getProcessList().get(j))
									.getName();

							int procPos = -1;
							int resPos = -1;

							for (int y = 0; y < resources.size()
									&& resPos == -1; y++)
								if (resources.get(y).getName().compareTo(
										resName) == 0)
									resPos = y;

							for (int y = 0; y < processes.size()
									&& procPos == -1; y++)
								if (processes.get(y).getName().compareTo(
										procName) == 0)
									procPos = y;
							if (procPos == -1 || resPos == -1)
								continue;

							int x1 = resPos * (resXEdge + 10) + resXEdge / 2
									+ 2;
							int y1 = resYEdge + 5;
							int x2 = procPos * (procXEdge + 10) + procXEdge / 2
									+ 2;
							int y2 = this.getHeight() - 2 - procXEdge - 5;

							g2.setColor(Color.BLUE);
							GpfxUtilsRainbow
									.drawArrow(g2, x1, y1, x2, y2, 1.0f);
						}
					}
				}
			}
		}

		/**
		 * It updates the view.
		 * 
		 * @param state
		 *            The state to represent.
		 */
		public void update(State state) {
			this.state = state;
			this.processes.clear();
			repaint();
		}

		/**
		 * It initializes the graph. It draws only the resources.
		 * 
		 * @param resources
		 *            The vector of resources created in the configuration.
		 */
		public void initialize(ArrayList<Resource> resources) {
			this.state = null;
			this.processes.clear();
			this.resources = resources;
			repaint();
		}

		/**
		 * It draws the graph.
		 * 
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 * @param g
		 *            The graph.
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBackground(Color.white);

			/*
			 * It is a safe cast because paint always invokes paintComponent
			 * with a Graphics2D object.
			 */
			Graphics2D g2;
			g2 = (Graphics2D) g;

			// It actives the antialiasing to improve the quality of the
			// rendering.
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);

			// It draws all elements.
			drawResources(g2);
			drawRelations(g2);
			drawProcesses(g2);
			// It updates the dimensions.
			resizeControl();
		}

		/** It resize the graphic. */
		private void resizeControl() {

			/*
			 * It computes the sizes.
			 */
			int procSize = processes.size() * (procXEdge + 10);
			int resSize = resources.size() * (resXEdge + 10);
			int newWidth = resSize > procSize ? resSize : procSize;

			int currWidth = (int) getPreferredSize().getWidth();
			if (newWidth == currWidth)
				return;

			setPreferredSize(new Dimension(newWidth, 150));

			revalidate();

		}

	}

	/** The JPanelGraph on which the graph is drawn. */
	JPanelGraph jPanelGraph = new JPanelGraph();

	/**
	 * The default constructor.
	 */
	public ViewResourcesGraph() {
		super();
		this.setViewportView(jPanelGraph);

	}

	/**
	 * It updates the graph.
	 * 
	 * @param state
	 *            The current state.
	 */
	public void update(State state) {
		jPanelGraph.update(state);
	}

	/**
	 * It initialized the graph.
	 * 
	 * @param resources
	 *            The vector of resources created during the configuration.
	 */
	public void initialize(ArrayList<Resource> resources) {
		jPanelGraph.initialize(resources);
	}
}