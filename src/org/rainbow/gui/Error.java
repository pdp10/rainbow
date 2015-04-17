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
 * File: Error.java
 * Package: gui
 * Author: Bertolin Stefano, Dalle Pezze Piero
 * Date: 16/03/2006
 * Version: 1.1
 *
 * Modifies
 * - v.1.1 (01/05/2007): Translation and Java6 compatible. Dalle Pezze Piero.
 * - v.1.0 (16/03/2006): Documentation and codify. Bertolin Stefano.
 */

package org.rainbow.gui;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import org.rainbow.gui.language.*;

/**
 * A common window to print error messages.
 *
 * @author Stefano Bertolin
 * @author Piero Dalle Pezze
 * @version 1.1
 */
public class Error extends JDialog {
    
    /**
     * To serialize.
     */
    private static final long serialVersionUID = -4824556051532407453L;
    
    private JPanel jContentPane = null;
    
    private JPanel messagePanel = null;
    
    private JPanel panelOk = null;
    
    private JLabel messageLabel = null;
    
    private ArrayList<JLabel> messageArrayLabel = null;
    
    private JButton buttonOk = null;
    
    /**
     * The message to show.
     */
    private String message;
    
    /**
     * Array of message to show.
     */
    private ArrayList<String> messageArray;
    
    /**
     * It instances an Error window.
     *
     * @param message
     *            The message to print. (A string).
     * @param parent
     *            The parent of this window.
     */
    public Error(String message, Frame parent) {
        super(parent, Language.getError(), true);
        this.message = message;
        this.setSize(514, 100);
        initialize();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }
    
    /**
     * It instances an Error window.
     *
     * @param messageArray
     *            The message to print. (An array of string).
     * @param parent
     *            The parent of this window.
     */
    public Error(ArrayList<String> messageArray, Frame parent) {
        super(parent, Language.getError(), true);
        this.messageArray = messageArray;
        this.messageArrayLabel = new ArrayList<JLabel>(messageArray.size());
        this.setSize(514, 100);
        initialize();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }
    
    /**
     * It instances an Error window.
     *
     * @param message
     *            The message to print. (A string).
     * @param parent
     *            The parent of this window. (A Dialog).
     */
    public Error(String message, Dialog parent) {
        super(parent, "Error!", true);
        this.message = message;
        this.setSize(314, 100);
        initializeArray();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }
    
    /**
     * It initializes la JDialog.
     */
    private void initialize() {
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                dispose();
            }
        });
        
        this.setContentPane(getJContentPane());
    }
    
    /**
     * It initializes the JDialog.
     */
    private void initializeArray() {
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                dispose();
            }
        });
        this.setContentPane(getJContentPaneArray());
    }
    
    /**
     * It returns the main panel of the JDialog.
     *
     * @return It returns the main panel of the JDialog.
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getMessagePanel(), java.awt.BorderLayout.NORTH);
            jContentPane.add(getPanelOk(), java.awt.BorderLayout.SOUTH);
        }
        return jContentPane;
    }
    
    /**
     * It returns the main panel of the JDialog.
     *
     * @return It returns the main panel of the JDialog.
     */
    private JPanel getJContentPaneArray() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getMessagePanelArray(),
                    java.awt.BorderLayout.NORTH);
            jContentPane.add(getPanelOk(), java.awt.BorderLayout.SOUTH);
        }
        return jContentPane;
    }
    
    /**
     * It returns the panel with the message printed.
     *
     * @return It returns the panel with the message printed.
     */
    private JPanel getMessagePanel() {
        if (messagePanel == null) {
            messageLabel = new JLabel();
            messageLabel.setText(message);
            messagePanel = new JPanel();
            messagePanel.add(messageLabel, null);
        }
        return messagePanel;
    }
    
    /**
     * It returns the panel with the message printed.
     *
     * @return It returns the panel with the message printed.
     */
    private JPanel getMessagePanelArray() {
        if (messagePanel == null) {
            messagePanel = new JPanel();
            for (int i = 0; i < messageArray.size(); i++) {
                messageArrayLabel.add(new JLabel());
                messageArrayLabel.get(i).setText(messageArray.get(i));
                messagePanel.add(messageArray.get(i), null);
            }
        }
        return messagePanel;
    }
    
    /**
     * It returns the "ok" panel.
     *
     * @return It returns the "ok" panel.
     */
    private JPanel getPanelOk() {
        if (panelOk == null) {
            panelOk = new JPanel();
            panelOk.add(getButtonOk(), null);
        }
        return panelOk;
    }
    
    /**
     * It returns the "ok" button.
     *
     * @return It returns the "ok" button.
     */
    private JButton getButtonOk() {
        if (buttonOk == null) {
            buttonOk = new JButton("OK");
            buttonOk.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    dispose();
                }
            });
        }
        return buttonOk;
    }
    
}
