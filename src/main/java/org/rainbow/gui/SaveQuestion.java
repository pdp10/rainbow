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
 * File: SaveQuestion.java
 * Package: gui
 * Author: Carlo Sarto, Piero Dalle Pezze
 * Date: 31/01/2006
 * Version: 1.02
 *
 * Modifies:
 * v.1.2 (31/01/2007): English translation. Java6 compatible. (Piero Dalle Pezze)
 * v.1.1 (01/02/2006): Class codify.
 * v.1.0 (31/01/2006): Class documentation.
 */
package org.rainbow.gui;

import org.rainbow.gui.language.*;

/**
 * A common window to question "Yes", "No", "Abort".
 *
 * @author Sarto Carlo
 * @author Piero Dalle Pezze
 * @version 1.2
 */
public class SaveQuestion extends javax.swing.JDialog {

	/** Serializable. */
	private static final long serialVersionUID = 1L;

	private int answer = 3;

	private java.awt.Frame frame;

	/** Creates new form SaveQuestion */
	public SaveQuestion(java.awt.Frame parent) {
		super(parent, true);
		frame = parent;
		initComponents();
	}

	/**
	 * It initializes the window.
	 */
	private void initComponents() {
		jPanel1 = new javax.swing.JPanel();
		jLabelQuestion = new javax.swing.JLabel();
		jButtonYes = new javax.swing.JButton();
		jButtonNo = new javax.swing.JButton();
		jButtonAbort = new javax.swing.JButton();

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				answer = 3;
				setVisible(false);
			}
		});

		jLabelQuestion.setText(Language.getSaveQ());

		jButtonYes.setText(Language.getYes());
		jButtonYes.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				answer = 1;
				setVisible(false);
			}
		});

		jButtonNo.setText(Language.getNo());
		jButtonNo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				answer = 2;
				setVisible(false);
			}
		});

		jButtonAbort.setText(Language.getAbort());
		jButtonAbort.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				answer = 3;
				setVisible(false);
			}
		});

		jPanel1.setOpaque(false);

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								jPanel1Layout
										.createSequentialGroup()
										.addGap(20, 20, 20)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(
																jLabelQuestion,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																175,
																Short.MAX_VALUE)
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addComponent(
																				jButtonYes)
																		.addGap(50,
																				50,
																				50)
																		.addComponent(
																				jButtonNo)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																				50,
																				Short.MAX_VALUE)
																		.addComponent(
																				jButtonAbort)))
										.addGap(20, 20, 20)));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								jPanel1Layout
										.createSequentialGroup()
										.addGap(20, 20, 20)
										.addComponent(jLabelQuestion)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												20, Short.MAX_VALUE)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jButtonYes)
														.addComponent(jButtonNo)
														.addComponent(
																jButtonAbort))
										.addGap(20, 20, 20)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jPanel1,
								javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(50, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jPanel1,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE).addContainerGap()));
		pack();
	}

	/**
	 * It sets visible this dialog and return the user's decision.
	 *
	 * @return an integer that is the user's decision:
	 *         <ol>
	 *         <li>1 Ok</li>
	 *         <li>2 No</li>
	 *         <li>3 Abort</li>
	 *         </ol>
	 */
	public int answer() {
		this.setLocationRelativeTo(frame);
		this.setVisible(true);
		return answer;
	}

	private javax.swing.JButton jButtonYes;

	private javax.swing.JButton jButtonNo;

	private javax.swing.JButton jButtonAbort;

	private javax.swing.JLabel jLabelQuestion;

	private javax.swing.JPanel jPanel1;

}