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
 * File: RainbowMainGUI.java
 * Package: gui
 * Author: Dalle Pezze Piero, Sarto Carlo, Fabio Gallonetto 
 * Date: 13/02/2005
 * Version: 2.14
 *
 * Modifies
 *  - v.2.14  (25/11/2014): Separation of the Views, Toolbar and Menubar from the main GUI. 
 *  - v.2.13 (24/11/2014): Simplified the language management. This file does not need to be edited when adding new languages to Rainbow. 
 *  - v.2.12 (19/11/2014): Removed automatic advancement and inserted manual incremental step. 
 *  - v.2.11 (1/04/2008): Removed method exportXML. XML is used now to save and open regulary 
 *                        configuration files. To retro-compatibility importFCS() is kept. Dalle Pezze Piero.
 *  - v.2.10 (12/05/2007): Added setMaximumLayout() and status bar. Dalle Pezze Piero.
 *  - v.2.9 (10/05/2007): III phase test. Dalle Pezze Piero.
 *  - v.2.8 (02/05/2007): English translation of the class. Java6 supported. Dalle Pezze Piero.
 *  - v.2.7 (04/04/2007): Added language menu and reboot functionality. Dalle Pezze Piero.
 *  - v.2.6 (26/03/2006): II phase test. Sarto Carlo.
 *  - v.2.5 (20/03/2006): Added methods to call the ConfigurationSummary to SGPEMv2 logic. Sarto Carlo.
 *  - v.2.4  (14/03/2006): Adaptation methods of context help to SGPEMv2 logic. Dalle Pezze Piero.
 *  - v.2.3  (09/03/2006): Adaptation methods of help to SGPEMv2 logic. Sarto Carlo.
 *  - v.2.2  (08/03/2006): Added methods to show and hide the view of statistics. Added ViewStatistics. Sarto Carlo.
 *  - v.2.1  (26/03/2006): I phase test Sarto Carlo.
 *  - v.2.0  (21/02/2006): Adaptation methods to save and open a configuration, manage the simulation, 
                           show views, menu, toolBar to SGPEMv2 logic. About views: held ViewGraphicResources 
			   and ViewGraphicRunning from SGPEMv1. Partially changed view dispositions. Sarto Carlo.
 *  - v.1.0  (13/02/2005): Design and implementation of the main GUI. Fabio Gallonetto. (SGPEMv1).
 */
package org.rainbow.gui;

import net.infonode.docking.*;
import net.infonode.docking.mouse.DockingWindowActionMouseButtonListener;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.theme.*;
import net.infonode.docking.util.*;
import net.infonode.gui.componentpainter.ComponentPainter;
import net.infonode.util.Direction;

import org.rainbow.data.*;
import org.rainbow.gui.language.*;
import org.rainbow.gui.SaveQuestion;
import org.rainbow.gui.input.*;
import org.rainbow.simulation.*;

import javax.help.*;
import javax.swing.*;

import java.net.URL;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

/*
 * IMPORTANT NOTE: The following warning thorwn at compile time:
 *
 * Note: /<path-inst>/Rainbow/src/gui/RainbowMainGUI.java uses unchecked or
 * unsafe operations. Note: Recompile with -Xlint:unchecked for details.
 *
 * is due to the method createRootWindow().
 */

/**
 * It is the main GUI of Rainbow.
 * 
 * @author Dalle Pezze Piero (Rainbow) 
 * @author Sarto Carlo (SGPEMv2)
 * @author Fabio Gallonetto (SGPEMv1)
 * @version 2.14
 */
public class RainbowMainGUI {
    
    /**
     * The main window in which views, menu and toolBar are added.
     */
    private RootWindow rootWindow;    
    
   
    /** The theme applied to the GUI. */
    private DockingWindowsTheme currentTheme;
    
    /**
     * An object that contains all modifies affixed to all features (buttons,
     * menu, graphics).
     */
    private RootWindowProperties properties;
    
    
    /** The application frame. */
    private JFrame frame;
    
    /** The about window. */
    private About about;
    
    /**
     * It is the state of the GUI. It is true if the advance automatic rifle is
     * active, false otherwise.
     */
    private boolean stateGUI;
    
    
    /** Help features */
    private HelpSet hs;
    
    private HelpBroker hb;
    
    // ------------------------------
    // BUTTONS AND MENU
    // ------------------------------
    

    
    /** It contains the answer of the SaveQuestion. */
    SaveQuestion askConfirmation;
    
    /** The configuration summary panel. */
    private ConfigurationSummary configurationSummary;
    
    
    
    
    
    /** The toolbar for Rainbow */
    private RainbowToolbar rainbowToolbar = null;

    /** The menubar for Rainbow */
    private RainbowMenubar rainbowMenubar = null;
    
    /** The views for Rainbow */
    private RainbowViews rainbowViews = null;
    
    
    

    
    
    
    /** Window to open and save a configuration created. */
    private FileManager fileManager;
    
    /* DATA */
    private Configuration configuration = null;
    
    private boolean configurationSaved = false;
    
    private Simulation simulation = null;
    
    private JLabel statusMessage = null;
    
    // ---------------------------------
    // METHODS TO CONTROL THE SIMULATION
    // ---------------------------------
    
    /** It allows to open a configuration saved on file. */
    public void openConfigurationFile() {
        if (simulation != null && stateGUI)
            SimStop();
        if (configurationSummary != null) {
            configurationSummary.dispose();
            configurationSummary = null;
        }
        Configuration conf = null;
	/* store the last process ID and reset the ID counter.
         * It is necessary because when the XML configuration file is processed
	 * processes are re-built, so it restarts the counter. However, if the file is corrupted
         * or mistaken, the current simulation remains, and the ID counter must be setted correctly
         * with the last process ID.
	 */
	int lastID = Id.getCounterId();
	Id.resetCounter();	
        // it controls if the current configuration is opened.
        if (configuration != null) {
            // it controls if it is just saved.
            if (configurationSaved) {
                // it opens a new configuration.
                conf = fileManager.openFile(frame);
            } else {
                // it ask to the user if he/she want to save the current
                // configuration.
                int confirm = askConfirmation.answer();
                // the user want to save
                if (confirm == 1) {
                    saveConfiguration();
                    conf = fileManager.openFile(frame);
                } else if (confirm == 2)
                    // the user doesn't want to save.
                    conf = fileManager.openFile(frame);
            }
            
        } else {
            // there isn't a current configuration.
            conf = fileManager.openFile(frame);
        }
        // A configuration file was selected.
        if (conf != null) {
            createSimulation(conf, true);
        } else {
	    // restore the last process ID. (conf corrupted or miskaken)
	    Id.setCounterId(lastID);	
	}
    }
    
    /** It invokes the DataInput panel to create a new configuration. */
    public void createConfiguration() {
        if (simulation != null && stateGUI)
            SimStop();
        int confirm = 2;
        if (configurationSummary != null) {
            configurationSummary.dispose();
            configurationSummary = null;
        }
        if ((configuration != null) && (!configurationSaved)) {
            // the current configuration isn't saved.
            confirm = askConfirmation.answer();
        }
        if (confirm != 3) {
            // the user has selected "YES" or "NO" (not "ABORT").
            if (confirm == 1)
                // the user has selected "YES"
                saveConfiguration();
            Configuration newConfiguration = null;
            DataInput input = new DataInput(frame, true);
            newConfiguration = input.getConfiguration();
            input.dispose();
            if (newConfiguration != null) {
                createSimulation(newConfiguration, false);
            }
        }
    }
    
    /** It invokes an invisible DataInput panel to create a new random configuration. */
    public void createRandomConfiguration() {
        if (simulation != null && stateGUI)
            SimStop();
        int confirm = 2;
        if (configurationSummary != null) {
            configurationSummary.dispose();
            configurationSummary = null;
        }
        if ((configuration != null) && (!configurationSaved)) {
            // the current configuration isn't saved.
            confirm = askConfirmation.answer();
        }
        if (confirm != 3) {
            // the user has selected "YES" or "NO" (not "ABORT").
            if (confirm == 1)
                // the user has selected "YES"
                saveConfiguration();
            Configuration newConfiguration = null;
            DataInput input = new DataInput(frame, false);
            newConfiguration = input.getRandomConfiguration();
            input.dispose();
            if (newConfiguration != null) {
                createSimulation(newConfiguration, false);
            }
        }
    }    
     
    /** It invokes the DataInput panel to modify the current configuration. */
    public void modifyConfiguration() {
        if (simulation != null && stateGUI)
            SimStop();
        if (configurationSummary != null) {
            configurationSummary.dispose();
            configurationSummary = null;
        }
        int confirm = 2;
        if (configuration != null) {
            if (!configurationSaved) {
                // the current configuration is not saved.
                confirm = askConfirmation.answer();
            }
            if (confirm != 3) {
                // the user has selected "YES" or "NO" (not "ABORT").
                if (confirm == 1)
                    // the user has selected "YES"
                    saveConfiguration();
                Configuration newConfiguration = null;
                DataInput input = new DataInput(frame, true, configuration);
                newConfiguration = input.getConfiguration();
                input.dispose();
                if (newConfiguration != null
                        && newConfiguration != configuration) {
                    createSimulation(newConfiguration, false);
                }
            }
        }
    }
    
    /**
     * It opens a window to ask if the user want to save the current
     * configuration.
     */
    public void saveConfiguration() {
        if (simulation != null && stateGUI)
            SimStop();
        
        if (fileManager.saveFile(configuration, frame))
            setConfigurationSaved(true);
    }
    
    /**
     * It opens a window to ask if the user want to export the current
     * configuration in html format.
     */
    public void exportHTML() {
        if (simulation != null && stateGUI)
            SimStop();
        fileManager.exportHTML(configuration, frame);
    }
    

    /** It imports an FCS configuration file. (RETRO-COMPATIBILITY)*/
    public void importFCS() {
        if (simulation != null && stateGUI)
            SimStop();
        if (configurationSummary != null) {
            configurationSummary.dispose();
            configurationSummary = null;
        }
        Configuration conf = null;
        // it controls if the current configuration is opened.
        if (configuration != null) {
            // it controls if it is just saved.
            if (configurationSaved) {
                // it opens a new configuration.
                conf = fileManager.importFCS(frame);
            } else {
                // it ask to the user if he/she want to save the current
                // configuration.
                int confirm = askConfirmation.answer();
                // the user want to save
                if (confirm == 1) {
                    saveConfiguration();
                    conf = fileManager.importFCS(frame);
                } else if (confirm == 2)
                    // the user doesn't want to save.
                    conf = fileManager.importFCS(frame);
            }
            
        } else {
            // there isn't a current configuration.
            conf = fileManager.importFCS(frame);
        }
        // A configuration file was selected.
        if (conf != null) {
            createSimulation(conf, true);
        }
    }    

    /** It closes the Rainbow software.*/
    public void exit() {
	if (!configurationSaved && configuration != null) {
	    int answer = askConfirmation.answer();
	    if (answer == 1) {
		saveConfiguration();
		System.exit(-1);
	    }
	}
	System.exit(-1);
    }
    
    
    
    /**
     * It invokes the method simulation.statistics(). This method is used to the
     * ViewStatistic to call all statistics of the simulation.
     */
    public void askStatistics() {
        if (simulation != null) {
            simulation.statistics();
        }
    }
    

    

    

    
    /** It creates the simulation from a configuration. */
    private void createSimulation(Configuration conf, boolean saved) {
        // It sets the new current configuration
        configuration = conf;
        
        // it controls that the simulation is not greater than RainbowConfig.getMaxLengthSimulation() ticks.
        int totalDurationExecution = 0;
        int totalDurationActivation = 0;
        int totalDuration = 0;
        boolean isCorrect = true;
        for (int i = 0; i < conf.getProcesses().size(); i++) {
            totalDurationExecution += conf.getProcesses().get(i)
            .getExecutionTime();
            if (totalDurationExecution > RainbowConfig.getMaxLengthSimulation()) {
                isCorrect = false;
                break;
            }
            totalDurationActivation += conf.getProcesses().get(i)
            .getActivationTime();
            if (totalDurationActivation > RainbowConfig.getMaxLengthSimulation()) {
                isCorrect = false;
                break;
            }
            totalDuration = totalDurationExecution + totalDurationActivation;
            if (totalDuration > RainbowConfig.getMaxLengthSimulation()) {
                isCorrect = false;
                break;
            }
        }
        
        if (isCorrect) {
            // it creates a new simulation
            simulation = new Simulation(configuration, this);
            if (simulation.getDuration() < RainbowConfig.getMaxLengthSimulation()) {
            
                // It sets true if the configuration was opened from file, false
                // otherwise.
                setConfigurationSaved(saved);

                rainbowToolbar.setButtonsCreateSimulation();  
                rainbowMenubar.setMenuItemsCreateSimulation();                     
             
                // It sets the simulation at start.
                setStartUp();
            } else {
                System.out.println(Language.getER003());
                new Error(Language.getER003(), frame);
            }
        } else {
            System.out.println(Language.getER003());
            new Error(Language.getER003(), frame);
            
        }
        
    }
    
    /** It sets the simulation in the terminate state. */
    public void setSimTerminated() {
        stateGUI = false;
        rainbowViews.setViewsTerminateSimulation();
        rainbowToolbar.setButtonsTerminateSimulation();
        rainbowMenubar.setMenuItemsTerminateSimulation();        
       
        askStatistics();
        
    }
    
    /** It sets the simulation at start. */
    public void setStartUp() {
        stateGUI = false;
        rainbowToolbar.setButtonsStartSimulation();
        rainbowMenubar.setMenuItemsStartSimulation();        
        rainbowViews.setViewsStartSimulation();
    }
    
    /**
     * It sets the state of the configuration.
     *
     * @param saved
     *            It is true if the configuration is saved, false otherwise.
     */
    private void setConfigurationSaved(boolean saved) {
        configurationSaved = saved;
    }
    
    // METHODS TO MANAGE THE SIMULATION
    
    /** It advances the simulation forward of a step. */
    public void moveForwardStep() {
	rainbowToolbar.setButtonsMoveForwardStep();
	rainbowMenubar.setMenuItemsMoveForwardStep();
        simulation.setDelay(rainbowToolbar.getSpinnerValue());        
        simulation.forward();
        askStatistics();
    }
    
    /** It advances the simulation backward of a step. */
    public void moveBackwardStep() {
	rainbowToolbar.setButtonsMoveBackwardStep();
	rainbowMenubar.setMenuItemsMoveBackwardStep();
        simulation.setDelay(rainbowToolbar.getSpinnerValue());       
        if(simulation.backward()) {
            askStatistics();
        } else {
            setStartUp();
        }
    }
    
    
    /** It stops the simulation. */
    public void SimStop() {
	rainbowToolbar.setButtonsStopSimulation();
	rainbowMenubar.setMenuItemsStopSimulation();
        askStatistics();
    }
    
    /** It moves the simulation at start. */
    public void moveAtStart() {    
        simulation.start();        
    }
    
    /** It moves the simulation at end. */
    public void moveAtEnd() {
	rainbowToolbar.setButtonsMoveAtEnd();
	rainbowMenubar.setMenuItemsMoveAtEnd();
        simulation.end();
        askStatistics();
    }
    
    
    
    /**
    * It returns the current configuration 
    * @return the Configuration
    */    
    public Configuration getConfiguration() { return configuration; }     
    
    /**
    * It returns the Help Broker
    * @return the help broker
    */
    public HelpBroker getHelpBroker() { return hb; }
    
    /**
    * It returns the HelpSet
    * @return the help set
    */    
    public HelpSet getHelpSet() { return hs; }

    
    /**
    * It returns the About Window
    * @return the About window
    */    
    public About getAbout() { return about; }    
    

    
    /**
    * It returns the Rainbow Views 
    * @return the Views
    */    
    public RainbowViews getViews() { return rainbowViews; }        
    
    
    /**
    * It returns the Rainbow Toolbar 
    * @return the Toolbar window
    */    
    public RainbowToolbar getToolbar() { return rainbowToolbar; }      
   

    /**
    * It returns the Rainbow Menubar 
    * @return the Menubar window
    */    
    public RainbowMenubar getMenubar() { return rainbowMenubar; }          
    
    
    // METHODS TO INIT, REBOOT, SHUTDOWN
    
    /** It boots the Rainbow application. */
    private void bootRainbow() {
        
        /* fields of the super class */
        configuration = null;
        configurationSaved = false;
        simulation = null;
        
        /* fields of this class */
        stateGUI = false;

        
        
        String theme = RainbowConfig.getTheme();   
        if (theme.equals("Default Theme")) currentTheme = new DefaultDockingTheme();
        else if (theme.equals("Look and Feel Theme")) currentTheme = new LookAndFeelDockingTheme();
        else if (theme.equals("Blue Highlight Theme")) currentTheme = new BlueHighlightDockingTheme();
        else if (theme.equals("Slim Flat Theme")) currentTheme = new SlimFlatDockingTheme();
        else if (theme.equals("Gradient Theme")) currentTheme = new GradientDockingTheme();
        else if (theme.equals("Shaped Gradient Theme")) currentTheme = new ShapedGradientDockingTheme();
        else if (theme.equals("Soft Blue Ice Theme")) currentTheme = new SoftBlueIceDockingTheme();
        else if (theme.equals("Classic Theme")) currentTheme = new ClassicDockingTheme();
        else currentTheme = new SoftBlueIceDockingTheme();
        
        
        properties = new RootWindowProperties();


        configurationSummary = null;
        frame = new JFrame(RainbowConfig.getApplicationName());
        
        about = new About(frame, false);
        fileManager = new FileManager(this);
        askConfirmation = new SaveQuestion(frame);
        
        // add a listener to the frame.
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // the "X" button.
                frame
                        .setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
                // If the configuration isn't saved.
                if (!configurationSaved && configuration != null) {
                    int answer = 3;
                    answer = askConfirmation.answer();
                    if (answer == 1) {
                        saveConfiguration();
                        System.exit(-1);
                    }
                }
                System.exit(-1);
            }
        });
      

        // Initialisation of objects for JavaHelp
        String hsName = "/helpset.hs";
        try {
            URL hsURL = this.getClass().getResource(hsName);
            hs = new HelpSet(null, hsURL);
        } catch (Exception ee) {
            System.out.println("HelpSet " + hsName + " not found");
            return;
        }
        hb = hs.createHelpBroker();
        
        // initialises views, rootWindow, toolbar, menubar
        rainbowViews = new RainbowViews(this);
        createRootWindow();        
	rainbowToolbar = new RainbowToolbar(this);
	rainbowMenubar = new RainbowMenubar(this);        
        setDefaultLayout();        
        showFrame();
    }
    
    /** It reboots the Rainbow application. */
    public void rebootRainbow() {
        JFrame oldFrame = frame;
        Configuration currentConf = configuration;
        bootRainbow();
        oldFrame.dispose();
        if(currentConf != null) {
            createSimulation(currentConf, configurationSaved);
        }
    }
    
    
    
    
    
    /** It opens the root window and adds all static views. */
    private void createRootWindow() {   
    
        rootWindow = DockingUtil.createRootWindow(rainbowViews.getViewMap(), true);
        
        properties.getDockingWindowProperties().setUndockEnabled(true);
        properties.getDockingWindowProperties().setDockEnabled(true);
        properties.getDockingWindowProperties().setCloseEnabled(true);
        
        // It enable the title bar.
        RootWindowProperties titleBarStyleProperties = PropertiesUtil
                .createTitleBarStyleRootWindowProperties();
        rootWindow.getRootWindowProperties().addSuperObject(
                titleBarStyleProperties);
        
        // Set gradient theme. The theme properties object is the super object
        // of our properties object, which
        // means our property value settings will override the theme values
        
        properties.addSuperObject(currentTheme.getRootWindowProperties());
        
        // Our properties object is the super object of the root window
        // properties object, so all property values of the
        // theme and in our property object will be used by the root window
        rootWindow.getRootWindowProperties().addSuperObject(properties);
        
        // Enable the bottom window bar
        rootWindow.getWindowBar(Direction.DOWN).setEnabled(true);
        
        // Add a listener which shows dialogs when a window is closing or
        // closed.
        rootWindow.addListener(new DockingWindowAdapter() {
            public void windowAdded(DockingWindow addedToWindow,
                    DockingWindow addedWindow) {
                rainbowViews.updateViews(addedWindow, true);
            }
            
            public void windowRemoved(DockingWindow removedFromWindow,
                    DockingWindow removedWindow) {
                rainbowViews.updateViews(removedWindow, false);
            }
            
        });
        
        // Add a mouse button listener that closes a window when it's clicked
        // with the middle mouse button.
        rootWindow
                .addTabMouseButtonListener(DockingWindowActionMouseButtonListener.MIDDLE_BUTTON_CLOSE_LISTENER);
        
                /*
                 * It doesn't enable the button "close window" of the external tab to
                 * avoid that the user can close more than a window with only an action.
                 * In fact it would invalidate the listener of single views.
                 */
        rootWindow.getRootWindowProperties().getTabWindowProperties()
        .getCloseButtonProperties().setVisible(false);
        
                /*
                 * It improves the look & feel of the drag for each panel by
                 * implementing an alpha filter that sets visible the position of the
                 * panel when it drags a full rectangle semi-trasparent.
                 */
        ComponentPainter alphaBlendPainter = new ComponentPainter() {
            
            /**
             * @see net.infonode.gui.componentpainter
             *      .ComponentPainter#paint(java.awt.Component,
             *      java.awt.Graphics, int, int, int, int)
             */
            public void paint(Component component, Graphics g, int x, int y,
                    int width, int height) {
                Graphics2D g2 = (Graphics2D) g;
                Composite oldComp = g2.getComposite();
                
                // It sets the rectangle semi trasparent.
                AlphaComposite composite = AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, 0.6f);
                g2.setComposite(composite);
                
                // It colours the rectangle of gradient blue.
                GradientPaint background = new GradientPaint(width, 0,
                        new Color(129, 156, 231), 0, height, new Color(74, 115,
                        231));
                g2.setPaint(background);
                g2.fillRect(x, y, width, height);
                g2.setColor(Color.BLACK);
                g2.setComposite(oldComp);
            }
            
            /**
             * @see net.infonode.gui.componentpainter
             *      .ComponentPainter#paint(java.awt.Component,
             *      java.awt.Graphics, int, int, int, int,
             *      net.infonode.util.Direction, boolean, boolean)
             */
            public void paint(Component component, Graphics g, int x, int y,
                    int width, int height, Direction direction,
                    boolean horizontalFlip, boolean verticalFlip) {
                paint(component, g, x, y, width, height);
            }
            
            /**
             * @see net.infonode.gui.componentpainter
             *      .ComponentPainter#isOpaque(java.awt.Component)
             */
            public boolean isOpaque(Component arg0) {
                return false;
            }
            
            /**
             * @see net.infonode.gui.componentpainter
             *      .ComponentPainter#getColor(java.awt.Component)
             */
            public Color getColor(Component arg0) {
                return new Color(129, 156, 231);
            }
        };
        // It applies the filter to the main window.
        rootWindow.getRootWindowProperties()
        .getDragRectangleShapedPanelProperties().setComponentPainter(
                alphaBlendPainter);
        
    }
    
    
    
    
    
    
    // ----------------------------------
    // METHODS OF GRAPHICS
    // ----------------------------------
    
    /**
     * The default constructor of this class.
     */
    public RainbowMainGUI() {
        // It sets the configuration of the Rainbow application.
        RainbowConfig.setRainbowConfig();
        // It sets the user's language.
        Language.setLanguage(RainbowConfig.getLanguage());
        // It boots Rainbow
        bootRainbow();
    }

 
 
    /**
     * Sets the look and feel.
     *
     */
    void setLookAndFeel() {
        // Clear the modified properties values
        properties.getMap().clear(true);    
    }  
 
 

 
    /**
     * Sets the docking windows theme.
     *
     * @param theme the docking windows theme
     */
    public void setTheme(DockingWindowsTheme theme) {
        // Clear the modified properties values
        properties.getMap().clear(true);    
        properties.replaceSuperObject(currentTheme.getRootWindowProperties(),
                theme.getRootWindowProperties());
        currentTheme = theme;
        RainbowConfig.saveTheme(theme.getName());
    }    
    
    

    
    /** It sets the Status Message 
     *@param message The status message 
     */
    public void setStatusMessage(String message) {
       if(statusMessage != null)   statusMessage.setText(message);
    }
    
    
    /** It sets the default layout. */
    public void setDefaultLayout() {
        rootWindow.setWindow(rainbowViews.getDefaultLayout());
        WindowBar windowBar = rootWindow.getWindowBar(Direction.DOWN);
        while (windowBar.getChildWindowCount() > 0)
            windowBar.getChildWindow(0).close();
    }
    
    /** It sets the maximum layout. */
    public void setMaximuxLayout() {
        rootWindow.setWindow(rainbowViews.getMaximuxLayout());        
        WindowBar windowBar = rootWindow.getWindowBar(Direction.DOWN);
        while (windowBar.getChildWindowCount() > 0)
            windowBar.getChildWindow(0).close();
    }      
    
 
    /**
     * It opens a static view.
     *
     * @param viewIdx
     *            The view to open
     */
    void openStaticView(int viewIndx, boolean openingMode) {
	  View view = rainbowViews.getView(viewIndx);
	  if(openingMode)
	    DockingUtil.addWindow(view, rootWindow);
	  else
	    view.close();
    }  
 
 
    
    
    /** It initializes the main frame and sets it visible. */
    private void showFrame() {
        frame.getContentPane().add(rainbowToolbar.getToolBar(), BorderLayout.NORTH);
        frame.getContentPane().add(rootWindow, BorderLayout.CENTER);
        frame.getContentPane().add(createStatusBar(), BorderLayout.SOUTH);
        frame.setJMenuBar(rainbowMenubar.getMenubar());
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        
    }
    
    /** It returns the current Configuration.
    * @return the current configuration
    */
    void startConfigurationSummary() {
	if (configuration != null) {
	    if (configurationSummary != null)
		configurationSummary.dispose();
	    configurationSummary = new ConfigurationSummary(configuration, frame);
	}
    }
	
    
    
        /**
     * Crea la statusBar del programma come insieme di JLabel in un JPanel
     */
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel();
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        statusBar.setBorder(BorderFactory.createEmptyBorder());
        statusMessage = new JLabel(" ");
        statusBar.add(statusMessage);
        return statusBar;
    }
    

    
}
