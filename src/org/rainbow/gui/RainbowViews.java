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
 *  - v.1.0  (25/11/2014): Separation of the Views from the main GUI.
 */
package org.rainbow.gui;


import net.infonode.docking.*;
import net.infonode.docking.util.*;
import org.rainbow.data.*;
import org.rainbow.gui.language.*;
import org.rainbow.gui.gpfxRepository.IconUtilsRainbow;
import org.rainbow.gui.view.*;
import org.rainbow.simulation.*;

import javax.help.*;
import javax.swing.*;

import java.util.LinkedList;
import java.util.ArrayList;


/**
 * It is the Rainbow View system.
 * 
 * @author Dalle Pezze Piero
 * @version 1.0
 */
public class RainbowViews {

    /** The contenitor of the static view. */
    private ViewMap viewMap;   
   
    /** Array of static views. */
    private View[] views; 
    
    /** Elements of view menu. */
    private JMenuItem[] viewItems;
    
    /** Buttons of context help features. */
    private JButton[] helpBtn;        
    
    
    
    
    

    private RainbowMainGUI rainbow = null;
    
    /** It creates the views associated to a Rainbow GUI */
    public RainbowViews(RainbowMainGUI rainbow) {
      if(rainbow != null) {
        this.rainbow = rainbow;
        views = new View[7];
        viewMap = new ViewMap();
        viewItems = new JMenuItem[7];
        helpBtn = new JButton[7];        

	createViews();
	
	
	
        /* Contextual help */
        
	final HelpBroker hb = rainbow.getHelpBroker();
	final HelpSet hs = rainbow.getHelpSet();
        for (int i = 0; i < views.length; i++) {
            helpBtn[i].addActionListener(new CSH.DisplayHelpFromSource(hb) {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String helpId = e.getActionCommand();
                    try {
                        hb.setCurrentID(Map.ID.create(helpId, hs));
                        hs.setHomeID(helpId);
                    } catch (BadIDException ex) {
                        System.out.println("Target " + helpId + " not correct");
                    } catch (InvalidHelpSetContextException ex) {
                    }
                    super.actionPerformed(e);
                }
            });
            
        }	
	
	
	
      }
    }

    

    
    
    /**
    * It returns the views.
    *
    * @return the views.
    */
    public View[] getViews() { return views; }
    
    
    
    /** It draws the statistics on the ViewStatistics. */
    public void viewStatistics(Statistics statistics) {
        if ((views[5]).getComponent() instanceof ViewStatistics) {
            ViewStatistics currView = (ViewStatistics) views[5].getComponent();
            currView.makeStatistics(statistics);
        }
    }    

    
    
    /**
     * It updates all views of the GUI.
     */
    public void updateState(State state) {
        if (state.getDeadlock()) {
            // there is a deadlock.
            if ((views[3]).getComponent() instanceof ViewRunningGraph) {
                ViewRunningGraph currView = (ViewRunningGraph) views[3]
                        .getComponent();
                currView.setLabelText(Language.getDeadlock());
            }
            String simTerminatedMessage = Language.getDeadlockSimulationTerminated();
        } else if ((views[3]).getComponent() instanceof ViewRunningGraph) {
            ViewRunningGraph currView = (ViewRunningGraph) views[3]
                    .getComponent();
            currView.resetLabelText();
        }
        // it updates the process state view.
        if ((views[0]).getComponent() instanceof ViewProcesses) {
            ViewProcesses currView = (ViewProcesses) views[0].getComponent();
            currView.update(rainbow.getConfiguration().getProcesses(), state);
        }
        // it updates the resource view.
        if ((views[1]).getComponent() instanceof ViewResources) {
            ViewResources currView = (ViewResources) views[1].getComponent();
            currView.update(rainbow.getConfiguration().getResources(), state);
        }
        // it updates the ready queue view.
        if ((views[2]).getComponent() instanceof ViewReadyQueue) {
            ViewReadyQueue currView = (ViewReadyQueue) views[2].getComponent();
            currView.update(state.getReady());
        }
        // it updates the terminated process view.
        if ((views[6]).getComponent() instanceof ViewTerminated) {
            ViewTerminated currView = (ViewTerminated) views[6].getComponent();
            currView.update(state.getTerminated());
        }
        // it updates the assignment resource view.
        if ((views[4]).getComponent() instanceof ViewResourcesGraph) {
            ViewResourcesGraph currView = (ViewResourcesGraph) views[4]
                    .getComponent();
            currView.update(state);
        }

    }
    
    
    
    
    /** It updates the view of the running processes. */
    public void viewRunningProcesses(LinkedList<SimulatedProcess> runningProcess) {
        // it updates the running process view.
        if ((views[3]).getComponent() instanceof ViewRunningGraph) {
            ViewRunningGraph currView = (ViewRunningGraph) views[3]
                    .getComponent();
            int idRunningProcess[] = new int[runningProcess.size()];
            for (int i = 0; i < runningProcess.size(); i++) {
                if (runningProcess.get(i) != null) {
                    SimulatedProcess p = runningProcess.get(i);
                    idRunningProcess[i] = p.getId();
                }
            }
            currView.drawGraph(idRunningProcess);
        }
    }    
    

    /** It sets the view at simulation termination 
     */
    public void setViewsTerminateSimulation() {
        // It prints the state of the simulation.
        if ((views[3]).getComponent() instanceof ViewRunningGraph) {
            ViewRunningGraph currView = (ViewRunningGraph) views[3]
                    .getComponent();
            String simTerminatedMessage = Language.getSimulationCompleted();
            currView.setLabelText(simTerminatedMessage);
        }    
    }

    /** It sets the view at simulation startup */
    public void setViewsStartSimulation() {
        // It initializes the ready queue view.
        if ((views[2]).getComponent() instanceof ViewReadyQueue) {
            ViewReadyQueue currView = (ViewReadyQueue) views[2].getComponent();
            currView.reset();
        }
        
        // It initializes the process view.
        if ((views[0]).getComponent() instanceof ViewProcesses) {
            ViewProcesses currView = (ViewProcesses) views[0].getComponent();
            currView.initialize(rainbow.getConfiguration().getProcesses());
        }
        
        // It initializes the assignment resource view.
        if ((views[4]).getComponent() instanceof ViewResourcesGraph) {
            ViewResourcesGraph currView = (ViewResourcesGraph) views[4]
                    .getComponent();
            currView.initialize(rainbow.getConfiguration().getResources());
        }
        
        // It initializes the resource view.
        if ((views[1]).getComponent() instanceof ViewResources) {
            ViewResources currView = (ViewResources) views[1].getComponent();
            currView.initialize(rainbow.getConfiguration().getResources());
        }
        
        // It initializes the terminated view.
        if ((views[6]).getComponent() instanceof ViewTerminated) {
            ViewTerminated currView = (ViewTerminated) views[6].getComponent();
            currView.reset();
        }
        
        // It initializes the running process view.
        if ((views[3]).getComponent() instanceof ViewRunningGraph) {
            ArrayList<String> procName = new ArrayList<String>();
            ArrayList<SimulatedProcess> processes = rainbow.getConfiguration().getProcesses();
            for (int i = 0; i < processes.size(); i++) {
                SimulatedProcess p = processes.get(i);
                procName.add(p.getName());
            }
            ViewRunningGraph currView = (ViewRunningGraph) views[3]
                    .getComponent();
            currView.initialize(procName);
            currView.setLabelText(Language.getSimulationAtInitialState());
        }
        
        if ((views[5]).getComponent() instanceof ViewStatistics) {
            ViewStatistics currView = (ViewStatistics) views[5].getComponent();
            currView.initialize();
        }
    }
    
 
 
    /** It returns the number of views for Rainbow.
    * @return the number of views
    */
    int getNumberOfViews() { return views.length; } 
 
 
 
    
    /** It creates the Rainbow views. */
    private void createViews() {
        
        // It creates buttons for the contextual help.
        for (int i = 0; i < views.length; i++) {
            helpBtn[i] = new JButton(IconUtilsRainbow.getHelpIcon("chelp"));
            helpBtn[i].setOpaque(false);
            helpBtn[i].setBorder(null);
            helpBtn[i].setFocusable(true);
        }
        
        // It creates all views and adds them to the viewMap
        views[0] = new View(Language.getProcesses(), IconUtilsRainbow
                .getPanelIcon("table"), new ViewProcesses());
        helpBtn[0].setActionCommand("processes");
        
        views[1] = new View(Language.getResources(), IconUtilsRainbow
                .getPanelIcon("table"), new ViewResources());
        helpBtn[1].setActionCommand("resources");
        
        views[2] = new View(Language.getReadyQueue(), IconUtilsRainbow
                .getPanelIcon("table"), new ViewReadyQueue());
        helpBtn[2].setActionCommand("ready_queue");
        
        views[3] = new View(Language.getGraphOfRunningProcesses(),
                IconUtilsRainbow.getPanelIcon("function"),
                new ViewRunningGraph());
        helpBtn[3].setActionCommand("advancement");
        
        views[4] = new View(Language.getGraphOfResourcesAssignment(),
                IconUtilsRainbow.getPanelIcon("shapegraph"),
                new ViewResourcesGraph());
        helpBtn[4].setActionCommand("assignment_resources");
        
        views[5] = new View(Language.getStatistics(), IconUtilsRainbow
                .getPanelIcon("statistics"), new ViewStatistics());
        helpBtn[5].setActionCommand("statistics");
        
        views[6] = new View(Language.getTerminated(), IconUtilsRainbow
                .getPanelIcon("table"), new ViewTerminated());
        helpBtn[6].setActionCommand("terminated");
        
        java.util.List<JComponent> list;
        // It adds contextual help buttons to each view.
        for (int i = 0; i < views.length; i++) {
                        /*
                         * Here there is the cause of the following message at compile time:
                         *
                         * Note: /<path-inst>/Rainbow/src/gui/RainbowMainGUI.java uses
                         * unchecked or unsafe operations. Note: Recompile with
                         * -Xlint:unchecked for details.
                         *
                         * It is due to the method getCustomTitleBarComponents() that
                         * returns List of JComponent instead of List<JComponent>
                         */
            list = (java.util.List<JComponent>) (views[i]
                    .getCustomTitleBarComponents());
            
            list.add(helpBtn[i]);
            viewMap.addView(i, views[i]);
        }
        
    }

    
    
    
    /**
     * It opens a static view.
     *
     * @param viewIdx
     *            The view to open
     */
    View getView(int viewIndx) {
	if(viewIndx>=0 && viewIndx <= views.length)
	   return views[viewIndx];
	return views[0];
    }    
    
    /** It returns a reference to the object ViewMap
    * @return the ViewMap
    */
    ViewMap getViewMap() { return viewMap; }     
    
    
    /**
     * It updates all views.
     *
     * @param window
     *            the window in which to search for views
     * @param added
     *            if true the window was added
     */
    void updateViews(DockingWindow window, boolean added) {
        if (window instanceof View) {
            for (int i = 0; i < views.length; i++)
                if (views[i] == window && viewItems[i] != null)
                    viewItems[i].setEnabled(!added);
        } else {
            for (int i = 0; i < window.getChildWindowCount(); i++)
                updateViews(window.getChildWindow(i), added);
        }
    }    
   
   
    
    /** It sets the default layout.
    *@return It return a SplitWindow containing the layout. */    
    SplitWindow getDefaultLayout() {
        return new SplitWindow(true, 0.6f, new SplitWindow(
                false, 0.66f, new TabWindow(new DockingWindow[] { views[5],
                views[3] }), views[4]), new SplitWindow(false,
                0.66f, new SplitWindow(false, 0.5f, new SplitWindow(
                true, 0.5f, views[2], views[6]), views[0]),
                views[1]));
    }
    
    /** It sets the maximum layout. 
    *@return It return a TabWindow containing the layout. */
    TabWindow getMaximuxLayout() {
        return new TabWindow(new DockingWindow[] {
            views[0], views[1], views[2], views[6], views[4], views[5],
            views[3]});
    } 
  
    
    
    
    
    
    
}
