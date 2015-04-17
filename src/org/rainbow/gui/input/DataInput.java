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
 * File: DataInput.java
 * Package: gui.input
 * Author: Dalle Pezze Piero
 * Date: 03/05/2007
 * Version: 1.3
 *
 * Modifies:
 *  - v.1.3 (14/11/2014): Added Random configuration. 
 *  - v.1.2 (15/05/2007): Scheduling policies compatible with ICPP.
 *  - v.1.1 (08/03/2007): Control of buttons, combo box and tooltips.
 *  - v.1.0 (05/03/2007): Code and documentation.
 */

package org.rainbow.gui.input;

import org.rainbow.gui.language.*;
import org.rainbow.data.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.rainbow.gui.gpfxRepository.IconUtilsRainbow;
import javax.help.*;
import java.net.*;
import javax.swing.*;

/**
 * This module is used to insert data and create a configuration using a GUI.
 *
 * @author Piero Dalle Pezze
 * @version 1.1
 */
public class DataInput extends JDialog {
    
    private static final long serialVersionUID = 1424887795;
    
    private String defProcName = "P", defResName = "R";
    
    private int processCounter = 0, resourceCounter = 0;
    
    private ArrayList<SimulatedProcess> processes = null;
    
    private Object[][] processData = null;
    
    private ArrayList<Resource> resources = null;
    
    private Object[][] resourceData = null;
    
    private Object[][] accessData = null;
    
    private SimulatedProcess modifiedProcess = null;
    
    private int indexModifiedProcess = -1;
    
    private Resource modifiedResource = null;
    
    private int indexModifiedResource = -1;
    
    private Access modifiedAccess = null;
    
    private int indexModifiedAccess = -1;
    
    private int createdAccesses = 0;
    
    private Configuration conf = null;
    
    private HelpSet hs = null;
    
    private HelpBroker hb = null;
    
    private JFrame parent = null;

    
   
    
    /**
     * Creates new form NewJDialog
     * @param parent The parent window.
     */
    public DataInput(JFrame parent, boolean visible) {
        super(parent, Language.getDataInput(), true);
        this.parent = parent;
        initHelp();
        processes = new ArrayList<SimulatedProcess>(RainbowConfig
                .getMaxProcessesNumber());
        resources = new ArrayList<Resource>(RainbowConfig.getMaxResourcesNumber());
        Id.resetCounter();
        initDataField();
        this.setResizable(false);
        this.setVisible(visible);
    }
    
    /**
     * Creates new form NewJDialog
     * @param parent The parent window.
     * @param configuration The configuration to load.
     */
    public DataInput(JFrame parent, boolean visible, Configuration configuration) {
        super(parent, Language.getDataInput(), true);
        this.parent = parent;
        conf = configuration;
        initHelp();
        Id.setCounterId(configuration.getProcesses().size());
        initDataField();
        setConfiguration();
        this.setResizable(false);
        this.setVisible(visible);
    }
    
    /**
     * It returns the configuration created.
     * @return the configuration created.
     */
    public Configuration getConfiguration() {
        return conf;
    }
      
      
      
    /**
    * Returns a pseudo-random number between min and max, inclusive.
    * The difference between min and max can be at most
    * <code>Integer.MAX_VALUE - 1</code>.
    *
    * @param min Minimum value
    * @param max Maximum value.  Must be greater than min.
    * @return Integer between min and max, inclusive.
    * @see java.util.Random#nextInt(int)
    */
    private int randInt(int min, int max) {

	// NOTE: Usually this should be a field rather than a method
	// variable so that it is not re-seeded every call.
	Random rand = new Random();

	// nextInt is normally exclusive of the top value,
	// so add 1 to make it inclusive
	int randomNum = rand.nextInt((max - min) + 1) + min;

	return randomNum;
    }      
      
    /**
    * Returns a boolean pseudo-random number.
    *
    * @return Integer between min and max, inclusive.
    * @see java.util.Random#nextInt(int)
    */
    private boolean randBoolean() {

	// NOTE: Usually this should be a field rather than a method
	// variable so that it is not re-seeded every call.
	Random rand = new Random();

	// nextInt is normally exclusive of the top value,
	// so add 1 to make it inclusive
	int randomNum = rand.nextInt(2);
	if(randomNum == 0) return false;
	return true;
    }       
      
      
    /**
      * it returns a random configuration within the limits specified in Rainbow configuration file.
      * @return the configuration created.     
      */
    public Configuration getRandomConfiguration() {
	//These two counters are not actually counters, but eventually they count the total number of processes 
	// and resources.
    	resourceCounter = randInt(0, (int)(RainbowConfig.getMaxResourcesNumber()/2));
	processCounter = randInt(1, (int)(RainbowConfig.getMaxProcessesNumber()/2));    	
	int maxNumAccesses = randInt(1, (int)(RainbowConfig.getMaxAccessesNumber()/processCounter) - 1);
        createdAccesses = 0;
	
	// Add a resource CPU. In the future this will have a random number of cores (multiplicity). For now, this is single core.
	//resources.add(new CPU(1));
	
	// create resources (generic resources)
	for(int i = 0; i < resourceCounter; i++) {
	    boolean preemptive = randBoolean();
	    if(preemptive)
	      resources.add(new PreemptiveResource("R" + String.valueOf(i), randInt(1, RainbowConfig.getMaxMultiplicity())));
	    else
	      resources.add(new NoPreemptiveResource("R" + String.valueOf(i), randInt(1, RainbowConfig.getMaxMultiplicity()), randInt(RainbowConfig.getMinCeilingPriority(), RainbowConfig.getMaxCeilingPriority())));   
	}
	// create processes
	for(int i = 0; i < processCounter; i++) {
	    // force them to activate earlier for avoiding waiting too much!
	    int activationTime = randInt(0, (int)(RainbowConfig.getMaxActivationTime() / 10)); 
	    int executionTime = randInt(1, (int)(RainbowConfig.getMaxExecutionTime() / 5));
	    
	    int priority = randInt(RainbowConfig.getMinPriority(), RainbowConfig.getMaxPriority());
	    SimulatedProcess sp = new SimulatedProcess("P" + String.valueOf(i), activationTime, executionTime, priority);
	    processes.add(sp);
	    
	    // add sp access to the CPU, which is always the first resource inserted by invariant
	    //sp.addNewAccessRequest(resources.get(0), 0, executionTime);
	    

	    
	    // create accesses
	    Iterator<Resource> itResources = resources.iterator();
	    int numCreatedAccesses = 0;
	    while(itResources.hasNext() && numCreatedAccesses < maxNumAccesses) {
		boolean link = randBoolean();
		Resource r = itResources.next();
		if(link) {
		    int requestTime = randInt(0, executionTime-1);
		    int duration = randInt(1, executionTime - requestTime); 
		    sp.addNewAccessRequest(r, requestTime, duration);
		    numCreatedAccesses++;
                }
            }
            createdAccesses = createdAccesses + numCreatedAccesses;      
        }
        
        // Skip ICPP Scheduler for now, as needs further testing in the EventTable.
        //boolean icpp = randBoolean();
        boolean icpp = false;
        String[] schedulingPolicies = null;
        if(icpp) schedulingPolicies = RainbowConfig.getSchedulingPoliciesICPP();
        else schedulingPolicies = RainbowConfig.getSchedulingPolicies();
	String[] assignmentPolicies = RainbowConfig.getAssignmentPolicies();	
	String schedulingPolicy = schedulingPolicies[randInt(0, schedulingPolicies.length-1)];
	String assignmentPolicy = assignmentPolicies[randInt(0, assignmentPolicies.length-1)];
        
        // force a shorter quantum for seeing "more context switch".
        int timeSlice = randInt(0, (int)(RainbowConfig.getMaxQuantum() / 5));
               
	conf = new Configuration(assignmentPolicy, schedulingPolicy, processes, resources, timeSlice);
	conf.setICPP(icpp);
 
	return conf;
    }
	

    
    /**
     * It sets the configuration to load.
     */
    private void setConfiguration() {
        
        // processes
        processes = conf.getProcesses();
        processCounter = processes.size();
        processName.setText(defProcName + processCounter);
        SimulatedProcess proc = null;
        for (int i = 0; i < processCounter; i++) {
            proc = processes.get(i);
            processTable.setValueAt(proc.getName(), i, 0);
            processTable.setValueAt(proc.getActivationTime(), i, 1);
            processTable.setValueAt(proc.getExecutionTime(), i, 2);
            processTable.setValueAt(proc.getInitialPriority(), i, 3);
            processCBX.addItem(proc);
        }
        modifyProcess.setEnabled(true);
        removeProcess.setEnabled(true);
        processCBX.setEnabled(true);
        
        // resources
        resources = conf.getResources();
        resourceCounter = resources.size();
        resourceName.setText(defResName + resourceCounter);
        Resource res = null;
        for (int i = 0; i < resourceCounter; i++) {
            res = resources.get(i);
            resourceTable.setValueAt(res.getName(), i, 0);
            resourceTable.setValueAt(res.getMultiplicity(), i, 2);
            if (res instanceof PreemptiveResource) {
                resourceTable.setValueAt(Language.getYes(), i, 1);
                resourceTable.setValueAt("-----", i, 3);
            } else {
                resourceTable.setValueAt(Language.getNo(), i, 1);
                resourceTable.setValueAt(((NoPreemptiveResource) res)
                .getCeilingPriority(), i, 3);
            }
            resourceCBX.addItem(res);
        }
        if (conf.isICPP()) {
            icpp.setSelected(true);
            ceilingPriority.setEnabled(true);
            schedulingPolicy.removeAllItems();
            String[] sp = RainbowConfig.getSchedulingPoliciesICPP();
            for (int i = 0; i < sp.length; i++) {
                schedulingPolicy.addItem(sp[i]);
            }
            schedulingPolicy.setSelectedIndex(0);
        }
        if(resourceCounter > 0) {
            modifyResource.setEnabled(true);
            removeResource.setEnabled(true);
            resourceCBX.setEnabled(true);
            addAccess.setEnabled(true);
        }
        
        // accesses
        ArrayList<Access> accessList = null;
        createdAccesses = 0;
        Access access = null;
        boolean anyAccess = false;
        for (int i = 0; i < processCounter; i++) {
            proc = processes.get(i);
            accessList = proc.getAccessesList();
            for (int j = 0; j < accessList.size(); j++) {
                access = accessList.get(j);
                accessTable.setValueAt(proc, createdAccesses, 0);
                accessTable.setValueAt(access.getResource(), createdAccesses, 1);
                accessTable.setValueAt(access.getRequestTime(), createdAccesses, 2);
                accessTable.setValueAt(access.getDuration(), createdAccesses, 3);
                createdAccesses = createdAccesses + 1;
                anyAccess = true;
            }
            
        }
        if(anyAccess) {
            modifyAccess.setEnabled(true);
            removeAccess.setEnabled(true);
            requestTime.setEnabled(true);
            requestDuration.setEnabled(true);
            
        }
        
        // policies
        schedulingPolicy.setSelectedItem(conf.getSchedulingPolicy().toString());
        assignmentPolicy.setSelectedItem(conf.getAssignmentPolicy().toString());
        quantum.setSelectedItem(conf.getTimeslice());
        
        String policy = schedulingPolicy.getSelectedItem().toString();
        if (policy == "Round Robin"
                || policy == "Priority Round Robin"
                || policy == "Preemptive on Priority Round Robin"
                || policy == "Multilevel Feedback"
                || policy == "Preemptive Multilevel Feedback"
                || policy == "Multilevel Feedback Dynamic Quantum"
                || policy == "Preemptive Multilevel Feedback Dynamic Quantum"
                || policy == "Linux"
                || policy == "UNIX") {
            quantum.setEnabled(true);
        } else {
            quantum.setEnabled(false);
        }
        
        accept.setEnabled(true);
    }
    
    /**
     * It initializes the help.
     */
    private void initHelp() {
        
        // Inizializzo gli oggetti per JavaHelp
        String hsName = "/helpset.hs";
        try {
            URL hsURL = this.getClass().getResource(hsName);
            hs = new HelpSet(null, hsURL);
        } catch (Exception ee) {
            System.out.println("HelpSet " + hsName + " not found");
            return;
        }
        hb = hs.createHelpBroker();
    }
    
    /**
     * It initializes the data fields of the GUI.
     */
    private void initDataField() {
        processData = new Object[RainbowConfig.getMaxProcessesNumber()][4];
        resourceData = new Object[RainbowConfig.getMaxResourcesNumber()][4];
        accessData = new Object[RainbowConfig.getMaxAccessesNumber()][4];
        initComponents();
        for (int i = RainbowConfig.getMinPriority(); i <= RainbowConfig
                .getMaxPriority(); i++) {
            priority.addItem(i);
        }
        for (int i = RainbowConfig.getMinCeilingPriority(); i <= RainbowConfig
                .getMaxCeilingPriority(); i++) {
            ceilingPriority.addItem(i);
        }
        for (int i = 1; i <= RainbowConfig.getMaxMultiplicity(); i++) {
            multiplicity.addItem(i);
        }
        for (int i = 1; i <= RainbowConfig.getMaxQuantum(); i++) {
            quantum.addItem(i);
        }
        String[] sp = RainbowConfig.getSchedulingPolicies();
        for (int i = 0; i < sp.length; i++) {
            schedulingPolicy.addItem(sp[i]);
        }
        String[] ap = RainbowConfig.getAssignmentPolicies();
        for (int i = 0; i < ap.length; i++) {
            assignmentPolicy.addItem(ap[i]);
        }
        priority.setSelectedItem(0);
        ceilingPriority.setSelectedItem(0);
    }
    
    /**
     * It initializes all graphical objects of the GUI.
     */
    private void initComponents() {
        font = new Font("Dialog", 0, 11);
        fontPanel = new Font("Dialog", 1, 11);
        jPanel1 = new JPanel();
        jPanel5 = new JPanel();
        accept = new JButton(IconUtilsRainbow.getGeneralIcon("apply"));
        abort = new JButton(IconUtilsRainbow.getGeneralIcon("cancel"));
        help = new JButton(IconUtilsRainbow.getHelpIcon("help"));
        accept.setToolTipText(Language.getAccept());
        abort.setToolTipText(Language.getAbort());
        help.setToolTipText(Language.getHelp());
        accept.setBorderPainted(false);
        abort.setBorderPainted(false);
        help.setBorderPainted(false);
        tabMainPanel = new JTabbedPane();
        jPanel2 = new JPanel();
        processTableScroll = new JScrollPane();
        processTable = new JTable();
        processTable.setGridColor(java.awt.Color.lightGray);
        processParametersPanel = new JPanel();
        priorityLabel = new JLabel();
        executionTimeLabel = new JLabel();
        activationTimeLabel = new JLabel();
        processNameLabel = new JLabel();
        defaultProcessNameLabel = new JLabel();
        defaultProcessName = new JCheckBox();
        defaultProcessName.setBackground(white);
        defaultProcessName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (defaultProcessName.isSelected()) {
                    processName.setEnabled(false);
                } else {
                    processName.setEnabled(true);
                }
            }
        });
        processName = new JTextField();
        activationTime = new JTextField();
        executionTime = new JTextField();
        priority = new JComboBox();
        priority.setBackground(white);
        jPanel8 = new JPanel();
        addProcess = new JButton(Language.getAdd(),
                IconUtilsRainbow.getGeneralIcon("edit_add"));
        addProcess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!InputControl.isProcessWrong(activationTime.getText(),
                        executionTime.getText(), priority.getSelectedItem()
                        .toString())) {
                    int row = processes.size();
                    processes.add(new SimulatedProcess(processName.getText(),
                            Integer.parseInt(activationTime.getText()), Integer
                            .parseInt(executionTime.getText()), Integer
                            .parseInt(priority.getSelectedItem()
                            .toString())));
                    processTable.setValueAt(processName.getText(), row, 0);
                    processTable.setValueAt(processes.get(row)
                    .getActivationTime(), row, 1);
                    processTable.setValueAt(processes.get(row)
                    .getExecutionTime(), row, 2);
                    processTable.setValueAt(priority.getSelectedItem(), row, 3);
                    processCBX.addItem(processes.get(row));
                    processCounter++;
                    processName.setText(defProcName + processCounter);
                    activationTime.setText("");
                    executionTime.setText("");
                    modifyProcess.setEnabled(true);
                    removeProcess.setEnabled(true);
                    accept.setEnabled(true);
                    
                    if (resources.size() > 0) {
                        addAccess.setEnabled(true);
                        processCBX.setEnabled(true);
                        resourceCBX.setEnabled(true);
                        requestTime.setEnabled(true);
                        requestDuration.setEnabled(true);
                        processLabel.setEnabled(true);
                        resourceLabel.setEnabled(true);
                        requestTimeLabel.setEnabled(true);
                        requestDurationLabel.setEnabled(true);
                    }
                    if (processes.size() == RainbowConfig
                            .getMaxProcessesNumber()) {
                        addProcess.setEnabled(false);
                    }
                }
            }
        });
        modifyProcess = new JButton(Language.getModify(),
                IconUtilsRainbow.getGeneralIcon("reload"));
        modifyProcess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (modifiedProcess == null) {
                    indexModifiedProcess = processTable.getSelectedRow();
                    if (indexModifiedProcess > -1
                            && indexModifiedProcess < processes.size()) {
                        processName.setText(String.valueOf(processTable
                                .getValueAt(indexModifiedProcess, 0)));
                        activationTime.setText(String.valueOf(processTable
                                .getValueAt(indexModifiedProcess, 1)));
                        executionTime.setText(String.valueOf(processTable
                                .getValueAt(indexModifiedProcess, 2)));
                        priority.setSelectedItem(processTable.getValueAt(
                                indexModifiedProcess, 3));
                        modifiedProcess = processes.get(indexModifiedProcess);
                        addProcess.setEnabled(false);
                        removeProcess.setEnabled(false);
                    }
                } else {
                    if (!InputControl.isProcessWrong(activationTime.getText(),
                            executionTime.getText(), priority.getSelectedItem()
                            .toString())) {
                        modifiedProcess.setName(processName.getText());
                        modifiedProcess.setActivationTime(Integer
                                .parseInt(activationTime.getText()));
                        modifiedProcess.setExecutionTime(Integer
                                .parseInt(executionTime.getText()));
                        modifiedProcess
                                .setInitialPriority(Integer.parseInt(priority
                                .getSelectedItem().toString()));
                        processTable.setValueAt(processName.getText(),
                                indexModifiedProcess, 0);
                        processTable.setValueAt(processes.get(
                                indexModifiedProcess).getActivationTime(),
                                indexModifiedProcess, 1);
                        processTable.setValueAt(processes.get(
                                indexModifiedProcess).getExecutionTime(),
                                indexModifiedProcess, 2);
                        processTable.setValueAt(priority.getSelectedItem(),
                                indexModifiedProcess, 3);
                        processCBX.removeItem(processes
                                .get(indexModifiedProcess));
                        
                        modifiedProcess.getAccessesList().clear();
                        for (int j = 0; j < createdAccesses; j++) {
                            if (((SimulatedProcess) accessTable
                                    .getValueAt(j, 0)).equals(modifiedProcess)) {
                                for (int i = j; i < createdAccesses; i++) {
                                    accessTable.setValueAt(accessTable
                                            .getValueAt(i + 1, 0), i, 0);
                                    accessTable.setValueAt(accessTable
                                            .getValueAt(i + 1, 1), i, 1);
                                    accessTable.setValueAt(accessTable
                                            .getValueAt(i + 1, 2), i, 2);
                                    accessTable.setValueAt(accessTable
                                            .getValueAt(i + 1, 3), i, 3);
                                }
                                createdAccesses--;
                            }
                        }
                        if (createdAccesses == 0) {
                            modifyAccess.setEnabled(false);
                            removeAccess.setEnabled(false);
                        }
                        processCBX.insertItemAt(processes
                                .get(indexModifiedProcess),
                                indexModifiedProcess);
                        processName.setText(defProcName + processCounter);
                        activationTime.setText("");
                        executionTime.setText("");
                        addProcess.setEnabled(true);
                        removeProcess.setEnabled(true);
                        modifiedProcess = null;
                        indexModifiedProcess = -1;
                    }
                }
            }
        });
        removeProcess = new JButton(Language.getRemove(),
                IconUtilsRainbow.getGeneralIcon("edit_remove"));
        removeProcess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = processTable.getSelectedRow();
                if (row > -1 && row < processes.size()) {
                    for (int i = row; i < processes.size(); i++) {
                        processTable.setValueAt(processTable.getValueAt(i + 1,
                                0), i, 0);
                        processTable.setValueAt(processTable.getValueAt(i + 1,
                                1), i, 1);
                        processTable.setValueAt(processTable.getValueAt(i + 1,
                                2), i, 2);
                        processTable.setValueAt(processTable.getValueAt(i + 1,
                                3), i, 3);
                        processes.get(i).decreaseId();
                    }
                    SimulatedProcess removedProcess = processes.get(row);
                    Id.decreaseId();
                    processCBX.removeItem(removedProcess);
                    for (int j = 0; j < createdAccesses; j++) {
                        if (((SimulatedProcess) accessTable.getValueAt(j, 0))
                        .equals(removedProcess)) {
                            for (int i = j; i < createdAccesses; i++) {
                                accessTable.setValueAt(accessTable.getValueAt(
                                        i + 1, 0), i, 0);
                                accessTable.setValueAt(accessTable.getValueAt(
                                        i + 1, 1), i, 1);
                                accessTable.setValueAt(accessTable.getValueAt(
                                        i + 1, 2), i, 2);
                                accessTable.setValueAt(accessTable.getValueAt(
                                        i + 1, 3), i, 3);
                            }
                            createdAccesses--;
                            j--;
                        }
                    }
                    processes.remove(row);
                    
                    if (createdAccesses == 0) {
                        modifyAccess.setEnabled(false);
                        removeAccess.setEnabled(false);
                    }
                    if (processes.size() == 0) {
                        modifyProcess.setEnabled(false);
                        removeProcess.setEnabled(false);
                        addAccess.setEnabled(false);
                        processCBX.setEnabled(false);
                        resourceCBX.setEnabled(false);
                        requestTime.setEnabled(false);
                        requestDuration.setEnabled(false);
                        processLabel.setEnabled(false);
                        resourceLabel.setEnabled(false);
                        requestTimeLabel.setEnabled(false);
                        requestDurationLabel.setEnabled(false);
                        accept.setEnabled(false);
                    }
                }
            }
        });
        jPanel3 = new JPanel();
        resourceTableScroll = new JScrollPane();
        resourceTable = new JTable();
        resourceTable.setGridColor(java.awt.Color.lightGray);
        resourceParametersPanel = new JPanel();
        resourceNameLabel = new JLabel();
        preemptiveLabel = new JLabel();
        multiplicityLabel = new JLabel();
        defaultResourceName = new JCheckBox();
        defaultResourceName.setBackground(white);
        defaultResourceName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (defaultResourceName.isSelected()) {
                    resourceName.setEnabled(false);
                } else {
                    resourceName.setEnabled(true);
                }
            }
        });
        resourceName = new JTextField();
        preemptive = new JCheckBox();
        preemptive.setBackground(white);
        multiplicity = new JComboBox();
        multiplicity.setBackground(white);
        ceilingPriority = new JComboBox();
        ceilingPriority.setBackground(white);
        ceilingPriorityLabel = new JLabel();
        defaultResourceNameLabel = new JLabel();
        jPanel10 = new JPanel();
        modifyResource = new JButton(Language.getModify(),
                IconUtilsRainbow.getGeneralIcon("reload"));
        modifyResource.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (modifiedResource == null) {
                    indexModifiedResource = resourceTable.getSelectedRow();
                    if (indexModifiedResource > -1
                            && indexModifiedResource < resources.size()) {
                        resourceName.setText(String.valueOf(resourceTable
                                .getValueAt(indexModifiedResource, 0)));
                        if (resourceTable.getValueAt(indexModifiedResource, 1) == Language
                                .getYes()) {
                            preemptive.setSelected(true);
                        } else {
                            preemptive.setSelected(false);
                        }
                        multiplicity.setSelectedItem(resourceTable.getValueAt(
                                indexModifiedResource, 2));
                        if (resourceTable.getValueAt(indexModifiedResource, 3) == "-----") {
                            ceilingPriority.setSelectedItem(RainbowConfig
                                    .getMinCeilingPriority());
                        } else {
                            ceilingPriority.setSelectedItem(resourceTable
                                    .getValueAt(indexModifiedResource, 3));
                        }
                        modifiedResource = resources.get(indexModifiedResource);
                        addResource.setEnabled(false);
                        removeResource.setEnabled(false);
                    }
                } else {
                    boolean oldWasPreemptive = (modifiedResource instanceof PreemptiveResource);
                    boolean newIsPreemptive = preemptive.isSelected();
                    if (oldWasPreemptive && newIsPreemptive) {
                        modifiedResource.setName(resourceName.getText());
                        modifiedResource.setMultiplicity(Integer
                                .parseInt(multiplicity.getSelectedItem()
                                .toString()));
                    } else {
                        if (!oldWasPreemptive && !newIsPreemptive) {
                            NoPreemptiveResource r = (NoPreemptiveResource) modifiedResource;
                            r.setName(resourceName.getText());
                            r.setMultiplicity(Integer.parseInt(multiplicity
                                    .getSelectedItem().toString()));
                            r.setCeilingPriority(Integer
                                    .parseInt(ceilingPriority.getSelectedItem()
                                    .toString()));
                        } else {
                            resources.remove(indexModifiedResource);
                            if (newIsPreemptive) {
                                resources.add(indexModifiedResource,
                                        new PreemptiveResource(resourceName
                                        .getText(), Integer
                                        .parseInt(multiplicity
                                        .getSelectedItem()
                                        .toString())));
                            } else {
                                resources.add(indexModifiedResource,
                                        new NoPreemptiveResource(resourceName
                                        .getText(), Integer
                                        .parseInt(multiplicity
                                        .getSelectedItem()
                                        .toString()), Integer
                                        .parseInt(ceilingPriority
                                        .getSelectedItem()
                                        .toString())));
                            }
                        }
                    }
                    resourceCBX
                            .removeItem(resources.get(indexModifiedResource));
                    for (int j = 0; j < createdAccesses; j++) {
                        if (((Resource) accessTable.getValueAt(j, 1))
                        .equals(modifiedResource)) {
                            /* it removes the request in the process */
                            ((SimulatedProcess) accessTable.getValueAt(j, 0))
                            .removeRequest(modifiedResource);
			    /*
			     * it removes the process's access to this resource
			     * in the accessTable
			     */
                            for (int i = j; i < createdAccesses; i++) {
                                accessTable.setValueAt(accessTable.getValueAt(
                                        i + 1, 0), i, 0);
                                accessTable.setValueAt(accessTable.getValueAt(
                                        i + 1, 1), i, 1);
                                accessTable.setValueAt(accessTable.getValueAt(
                                        i + 1, 2), i, 2);
                                accessTable.setValueAt(accessTable.getValueAt(
                                        i + 1, 3), i, 3);
                            }
                            createdAccesses--;
                        }
                    }
                    if (createdAccesses == 0) {
                        modifyAccess.setEnabled(false);
                        removeAccess.setEnabled(false);
                    }
                    
                    resourceCBX.insertItemAt(resources
                            .get(indexModifiedResource), indexModifiedResource);
                    resourceTable.setValueAt(resourceName.getText(),
                            indexModifiedResource, 0);
                    resourceTable.setValueAt(multiplicity.getSelectedItem(),
                            indexModifiedResource, 2);
                    if (preemptive.isSelected()) {
                        resourceTable.setValueAt(Language.getYes(),
                                indexModifiedResource, 1);
                        resourceTable.setValueAt("-----",
                                indexModifiedResource, 3);
                    } else {
                        resourceTable.setValueAt(Language.getNo(),
                                indexModifiedResource, 1);
                        resourceTable.setValueAt(ceilingPriority
                                .getSelectedItem(), indexModifiedResource, 3);
                    }
                    
                    resourceName.setText(defResName + resourceCounter);
                    addResource.setEnabled(true);
                    removeResource.setEnabled(true);
                    modifiedResource = null;
                    indexModifiedResource = -1;
                }
            }
        });
        removeResource = new JButton(Language.getRemove(),
                IconUtilsRainbow.getGeneralIcon("edit_remove"));
        removeResource.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = resourceTable.getSelectedRow();
                if (row > -1 && row < resources.size()) {
                    for (int i = row; i < resources.size(); i++) {
                        resourceTable.setValueAt(resourceTable.getValueAt(
                                i + 1, 0), i, 0);
                        resourceTable.setValueAt(resourceTable.getValueAt(
                                i + 1, 1), i, 1);
                        resourceTable.setValueAt(resourceTable.getValueAt(
                                i + 1, 2), i, 2);
                        resourceTable.setValueAt(resourceTable.getValueAt(
                                i + 1, 3), i, 3);
                    }
                    Resource removedResource = resources.get(row);
                    resourceCBX.removeItem(removedResource);              
                    for (int j = 0; j < createdAccesses; j++) {
                        if (((Resource) accessTable.getValueAt(j, 1)) 
                        .equals(removedResource)) {
			    /*
			     * It removes the process's access to this resource
			     * from the access list
			     */
                            ((SimulatedProcess) accessTable.getValueAt(j, 0))
                            .removeRequest(removedResource);
                            /* It removes all accesses to this resource. */
                            for (int i = j; i < createdAccesses; i++) {
                                accessTable.setValueAt(accessTable.getValueAt(
                                        i + 1, 0), i, 0);
                                accessTable.setValueAt(accessTable.getValueAt(
                                        i + 1, 1), i, 1);
                                accessTable.setValueAt(accessTable.getValueAt(
                                        i + 1, 2), i, 2);
                                accessTable.setValueAt(accessTable.getValueAt(
                                        i + 1, 3), i, 3);
                            }
                            createdAccesses--;
                            j--;
                        }
                    }
                    if (createdAccesses == 0) {
                        modifyAccess.setEnabled(false);
                        removeAccess.setEnabled(false);
                    }
                    resources.remove(row);
                    if (resources.size() == 0) {
                        modifyResource.setEnabled(false);
                        removeResource.setEnabled(false);
                        addAccess.setEnabled(false);
                        processCBX.setEnabled(false);
                        resourceCBX.setEnabled(false);
                        requestTime.setEnabled(false);
                        requestDuration.setEnabled(false);
                        processLabel.setEnabled(false);
                        resourceLabel.setEnabled(false);
                        requestTimeLabel.setEnabled(false);
                        requestDurationLabel.setEnabled(false);
                    }
                }
            }
        });
        addResource = new JButton(Language.getAdd(),
                IconUtilsRainbow.getGeneralIcon("edit_add"));
        addResource.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = resources.size();
                resourceTable.setValueAt(resourceName.getText(), row, 0);
                resourceTable
                        .setValueAt(multiplicity.getSelectedItem(), row, 2);
                if (preemptive.isSelected()) {
                    resourceTable.setValueAt(Language.getYes(), row, 1);
                    resourceTable.setValueAt("-----", row, 3);
                    resources.add(new PreemptiveResource(
                            resourceName.getText(), Integer
                            .parseInt(multiplicity.getSelectedItem()
                            .toString())));
                } else {
                    resourceTable.setValueAt(Language.getNo(), row, 1);
                    resourceTable.setValueAt(ceilingPriority.getSelectedItem(),
                            row, 3);
                    resources.add(new NoPreemptiveResource(resourceName
                            .getText(), Integer.parseInt(multiplicity
                            .getSelectedItem().toString()), Integer
                            .parseInt(ceilingPriority.getSelectedItem()
                            .toString())));
                }
                resourceCBX.addItem(resources.get(row));
                resourceCounter++;
                resourceName.setText(defResName + resourceCounter);
                modifyResource.setEnabled(true);
                removeResource.setEnabled(true);
                if (processes.size() > 0) {
                    addAccess.setEnabled(true);
                    processCBX.setEnabled(true);
                    resourceCBX.setEnabled(true);
                    requestTime.setEnabled(true);
                    requestDuration.setEnabled(true);
                    processLabel.setEnabled(true);
                    resourceLabel.setEnabled(true);
                    requestTimeLabel.setEnabled(true);
                    requestDurationLabel.setEnabled(true);
                }
                if (resources.size() == RainbowConfig.getMaxResourcesNumber()) {
                    addResource.setEnabled(false);
                }
            }
        });
        icppLabel = new JLabel();
        icpp = new JCheckBox();
        icpp.setBackground(white);
        icpp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (icpp.isSelected()) {
                    ceilingPriority.setEnabled(true);
                    schedulingPolicy.removeAllItems();
                    String[] sp = RainbowConfig.getSchedulingPoliciesICPP();
                    for (int i = 0; i < sp.length; i++) {
                        schedulingPolicy.addItem(sp[i]);
                    }
                    schedulingPolicy.setSelectedIndex(0);
                } else {
                    ceilingPriority.setEnabled(false);
                    schedulingPolicy.removeAllItems();
                    String[] sp = RainbowConfig.getSchedulingPolicies();
                    for (int i = 0; i < sp.length; i++) {
                        schedulingPolicy.addItem(sp[i]);
                    }
                    schedulingPolicy.setSelectedIndex(0);
                }
            }
        });
        jPanel4 = new JPanel();
        accessTableScroll = new JScrollPane();
        accessTable = new JTable();
        accessTable.setGridColor(java.awt.Color.lightGray);
        jPanel11 = new JPanel();
        removeAccess = new JButton(Language.getRemove(),
                IconUtilsRainbow.getGeneralIcon("edit_remove"));
        modifyAccess = new JButton(Language.getModify(),
                IconUtilsRainbow.getGeneralIcon("reload"));
        addAccess = new JButton(Language.getAdd(),
                IconUtilsRainbow.getGeneralIcon("edit_add"));
        accessParametersPanel = new JPanel();
        requestDurationLabel = new JLabel();
        requestTimeLabel = new JLabel();
        resourceLabel = new JLabel();
        processLabel = new JLabel();
        resourceCBX = new JComboBox();
        resourceCBX.setBackground(white);
        processCBX = new JComboBox();
        processCBX.setBackground(white);
        requestTime = new JTextField();
        requestDuration = new JTextField();
        jPanel6 = new JPanel();
        jPanel13 = new JPanel();
        quantum = new JComboBox();
        quantum.setBackground(white);
        jPanel14 = new JPanel();
        assignmentPolicyLabel = new JLabel();
        assignmentPolicy = new JComboBox();
        assignmentPolicy.setBackground(white);
        schedulingPolicy = new JComboBox();
        schedulingPolicy.setBackground(white);
        
        schedulingPolicyLabel = new JLabel();
        
        /* fonts */
        defaultProcessNameLabel.setFont(font);
        processNameLabel.setFont(font);
        activationTimeLabel.setFont(font);
        executionTimeLabel.setFont(font);
        priorityLabel.setFont(font);
        processName.setFont(font);
        activationTime.setFont(font);
        executionTime.setFont(font);
        priority.setFont(font);
        icppLabel.setFont(font);
        
        defaultResourceNameLabel.setFont(font);
        resourceNameLabel.setFont(font);
        preemptiveLabel.setFont(font);
        multiplicityLabel.setFont(font);
        ceilingPriorityLabel.setFont(font);
        resourceName.setFont(font);
        multiplicity.setFont(font);
        ceilingPriority.setFont(font);
        icppLabel.setFont(font);
        
        processLabel.setFont(font);
        resourceLabel.setFont(font);
        requestTimeLabel.setFont(font);
        requestDurationLabel.setFont(font);
        processCBX.setFont(font);
        resourceCBX.setFont(font);
        requestTime.setFont(font);
        requestDuration.setFont(font);
        
        schedulingPolicyLabel.setFont(font);
        assignmentPolicyLabel.setFont(font);
        schedulingPolicy.setFont(font);
        assignmentPolicy.setFont(font);
        quantum.setFont(font);
        
        processTable.setFont(font);
        resourceTable.setFont(font);
        accessTable.setFont(font);
        
        processTableScroll.setAutoscrolls(true);
        resourceTableScroll.setAutoscrolls(true);
        accessTableScroll.setAutoscrolls(true);
        
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jPanel1.setBackground(new Color(255, 255, 255));
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jPanel5.setBackground(new Color(255, 255, 255));
        accept.setOpaque(false);
        accept.setEnabled(false);
        accept.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                conf = new Configuration();
                conf.setProcesses(processes);
                conf.setResources(resources);
                conf.setTimeslice(Integer.parseInt(quantum.getSelectedItem()
                .toString()));
                conf.setICPP(icpp.isSelected());
                
                conf.setSchedulingPolicy(schedulingPolicy.getSelectedItem().toString());
                conf.setAssignmentPolicy(assignmentPolicy.getSelectedItem().toString());
                
                setVisible(false);
            }
        });
        
        abort.setOpaque(false);
        abort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        
        help.setOpaque(false);
        help.setActionCommand("processes_tab");
        help.addActionListener(new CSH.DisplayHelpFromSource(hb) {
            public void actionPerformed(ActionEvent e) {
                String helpId;
                switch (tabMainPanel.getSelectedIndex()) {
                    case 0:
                        helpId = "processes_tab";
                        break;
                    case 1:
                        helpId = "resources_tab";
                        break;
                    case 2:
                        helpId = "accesses_tab";
                        break;
                    case 3:
                        helpId = "policies_tab";
                        break;
                    default:
                        helpId = "nothing";
                }
                try {
                    hb.setCurrentID(javax.help.Map.ID.create(helpId, hs));
                    hs.setHomeID(helpId);
                } catch (BadIDException ex) {
                    System.out.println("Target " + helpId + " mistaked");
                } catch (InvalidHelpSetContextException ex) {
                }
                super.actionPerformed(e);
            }
        });
        
        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(
                jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel5Layout.createSequentialGroup().addContainerGap().add(
                abort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED, 10,
                Short.MAX_VALUE).add(help,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                93,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED, 212,
                Short.MAX_VALUE).add(accept,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                85,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap()));
        jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel5Layout.createSequentialGroup().addContainerGap().add(
                jPanel5Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.BASELINE).add(
                abort).add(help).add(accept)).addContainerGap(
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)));
        
        tabMainPanel.setBackground(new Color(255, 255, 255));
        jPanel2.setBackground(new Color(204, 255, 204));
        processTableScroll
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        processTableScroll.setAutoscrolls(true);
        processTableScroll.setOpaque(false);
        jPanel2.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        processTable
                .setModel(new javax.swing.table.DefaultTableModel(processData,
                new String[] { Language.getName(),
                Language.getActivationTime(),
                Language.getExecutionTime(),
                Language.getBasePriority() }) {
            private static final long serialVersionUID = 717295845;
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        processTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        processTable.setDragEnabled(true);
        processTableScroll.setViewportView(processTable);
        
        processParametersPanel.setBorder(BorderFactory.createTitledBorder(null,
                Language.getProcessParameters(),
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, fontPanel,
                new Color(0, 0, 0)));
        processParametersPanel.setOpaque(false);
        priorityLabel.setText(Language.getBasePriority());
        
        executionTimeLabel.setText(Language.getExecutionTime());
        activationTimeLabel.setText(Language.getActivationTime());
        executionTimeLabel.setToolTipText("[1 .. "
                + RainbowConfig.getMaxExecutionTime() + "]");
        activationTimeLabel.setToolTipText("[0 .. "
                + RainbowConfig.getMaxActivationTime() + "]");
        executionTime.setToolTipText("[1 .. "
                + RainbowConfig.getMaxExecutionTime() + "]");
        activationTime.setToolTipText("[0 .. "
                + RainbowConfig.getMaxActivationTime() + "]");
        
        processNameLabel.setText(Language.getName());
        
        defaultProcessNameLabel.setText(Language.getDefaultName());
        
        defaultProcessName.setSelected(true);
        defaultProcessName.setBorder(BorderFactory
                .createEmptyBorder(0, 0, 0, 0));
        defaultProcessName.setMargin(new Insets(0, 0, 0, 0));
        
        processName.setText(defProcName + processCounter);
        processName.setEnabled(false);
        
        priority.setModel(new DefaultComboBoxModel(new String[] {}));
        
        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(
                processParametersPanel);
        processParametersPanel.setLayout(jPanel7Layout);
        jPanel7Layout
                .setHorizontalGroup(jPanel7Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel7Layout
                .createSequentialGroup()
                .addContainerGap()
                .add(
                jPanel7Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                activationTimeLabel)
                .add(executionTimeLabel)
                .add(priorityLabel)
                .add(processNameLabel)
                .add(
                defaultProcessNameLabel))
                .add(85, 85, 85)
                .add(
                jPanel7Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel7Layout
                .createSequentialGroup()
                .add(
                defaultProcessName)
                .addContainerGap())
                .add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                jPanel7Layout
                .createSequentialGroup()
                .add(
                jPanel7Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.TRAILING)
                .add(
                org.jdesktop.layout.GroupLayout.LEADING,
                priority,
                0,
                100,
                Short.MAX_VALUE)
                .add(
                org.jdesktop.layout.GroupLayout.LEADING,
                executionTime,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                100,
                Short.MAX_VALUE)
                .add(
                org.jdesktop.layout.GroupLayout.LEADING,
                activationTime,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                100,
                Short.MAX_VALUE)
                .add(
                org.jdesktop.layout.GroupLayout.LEADING,
                processName,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                100,
                Short.MAX_VALUE))
                .add(
                31,
                31,
                31)))));
        jPanel7Layout
                .setVerticalGroup(jPanel7Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel7Layout
                .createSequentialGroup()
                .addContainerGap()
                .add(
                jPanel7Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.BASELINE)
                .add(
                defaultProcessNameLabel)
                .add(defaultProcessName))
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED)
                .add(
                jPanel7Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                processName,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                processNameLabel))
                .add(4, 4, 4)
                .add(
                jPanel7Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.BASELINE)
                .add(
                activationTimeLabel)
                .add(
                activationTime,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED)
                .add(
                jPanel7Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.BASELINE)
                .add(executionTimeLabel)
                .add(
                executionTime,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED)
                .add(
                jPanel7Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.BASELINE)
                .add(priorityLabel)
                .add(
                priority,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE)));
        
        jPanel8.setOpaque(false);
        addProcess.setOpaque(false);
        addProcess.setToolTipText(Language.getAddNewRowTT());
        
        modifyProcess.setOpaque(false);
        modifyProcess.setToolTipText(Language.getModifySelectedRowTT());
        modifyProcess.setEnabled(false);
        
        removeProcess.setOpaque(false);
        removeProcess.setToolTipText(Language.getRemoveSelectedRowTT());
        removeProcess.setEnabled(false);
        
        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(
                jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(jPanel8Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel8Layout.createSequentialGroup().addContainerGap().add(
                jPanel8Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                modifyProcess,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                121, Short.MAX_VALUE).add(removeProcess,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                121, Short.MAX_VALUE).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                addProcess,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                121, Short.MAX_VALUE)).addContainerGap()));
        jPanel8Layout.setVerticalGroup(jPanel8Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel8Layout.createSequentialGroup().addContainerGap().add(
                addProcess).addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED).add(
                modifyProcess).addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED).add(
                removeProcess).addContainerGap(
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)));
        
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(
                jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout
                .setHorizontalGroup(jPanel2Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                jPanel2Layout
                .createSequentialGroup()
                .addContainerGap()
                .add(
                jPanel2Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.TRAILING)
                .add(
                org.jdesktop.layout.GroupLayout.LEADING,
                processTableScroll,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                506,
                Short.MAX_VALUE)
                .add(
                jPanel2Layout
                .createSequentialGroup()
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED)
                .add(
                processParametersPanel,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)
                .add(
                32,
                32,
                32)
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED)
                .add(
                jPanel8,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)))
                .addContainerGap()));
        jPanel2Layout
                .setVerticalGroup(jPanel2Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel2Layout
                .createSequentialGroup()
                .addContainerGap()
                .add(
                jPanel2Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel8,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(
                processParametersPanel,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE))
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED)
                .add(
                processTableScroll,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                249,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap()));
        tabMainPanel.addTab(Language.getProcesses(), jPanel2);
        
        /* RESOURCE */
        jPanel3.setBackground(new Color(204, 255, 204));
        resourceTableScroll
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        resourceTableScroll.setAutoscrolls(true);
        resourceTableScroll.setOpaque(false);
        resourceTable.setModel(new javax.swing.table.DefaultTableModel(
                resourceData, new String[] { Language.getName(),
                Language.getPreemptive(), Language.getMultiplicity(),
                Language.getCeilingPriority() }) {
            private static final long serialVersionUID = 97958372;
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        resourceTable.setDragEnabled(true);
        resourceTableScroll.setViewportView(resourceTable);
        
        resourceParametersPanel.setBorder(BorderFactory.createTitledBorder(
                null, Language.getResourceParameters(),
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, fontPanel,
                new Color(0, 0, 0)));
        resourceParametersPanel.setOpaque(false);
        resourceNameLabel.setText(Language.getName());
        
        preemptiveLabel.setText(Language.getPreemptive());
        
        multiplicityLabel.setText(Language.getMultiplicity());
        
        defaultResourceName.setSelected(true);
        defaultResourceName.setBorder(BorderFactory.createEmptyBorder(0, 0, 0,
                0));
        defaultResourceName.setMargin(new Insets(0, 0, 0, 0));
        
        resourceName.setText(defResName + resourceCounter);
        resourceName.setEnabled(false);
        
        preemptive.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        preemptive.setMargin(new Insets(0, 0, 0, 0));
        
        multiplicity.setModel(new DefaultComboBoxModel(new String[] {}));
        
        ceilingPriority.setModel(new DefaultComboBoxModel(new String[] {}));
        ceilingPriority.setEnabled(false);
        
        ceilingPriorityLabel.setText(Language.getCeilingPriority());
        
        defaultResourceNameLabel.setText(Language.getDefaultName());
        
        org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(
                resourceParametersPanel);
        resourceParametersPanel.setLayout(jPanel9Layout);
        jPanel9Layout
                .setHorizontalGroup(jPanel9Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel9Layout
                .createSequentialGroup()
                .addContainerGap()
                .add(
                jPanel9Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(resourceNameLabel)
                .add(preemptiveLabel)
                .add(multiplicityLabel)
                .add(
                ceilingPriorityLabel)
                .add(
                defaultResourceNameLabel))
                .add(99, 99, 99)
                .add(
                jPanel9Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING,
                false)
                .add(
                resourceName,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                100,
                Short.MAX_VALUE)
                .add(
                defaultResourceName)
                .add(preemptive)
                .add(
                multiplicity,
                0,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)
                .add(
                ceilingPriority,
                0,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE))
                .addContainerGap(34, Short.MAX_VALUE)));
        jPanel9Layout
                .setVerticalGroup(jPanel9Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel9Layout
                .createSequentialGroup()
                .addContainerGap()
                .add(
                jPanel9Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.BASELINE)
                .add(
                defaultResourceName)
                .add(
                defaultResourceNameLabel))
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED)
                .add(
                jPanel9Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.TRAILING)
                .add(resourceNameLabel)
                .add(
                resourceName,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED)
                .add(
                jPanel9Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.BASELINE)
                .add(preemptiveLabel)
                .add(preemptive))
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED)
                .add(
                jPanel9Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.TRAILING)
                .add(multiplicityLabel)
                .add(
                multiplicity,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED)
                .add(
                jPanel9Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.TRAILING)
                .add(
                ceilingPriorityLabel)
                .add(
                ceilingPriority,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap()));
        
        jPanel10.setOpaque(false);
        modifyResource.setOpaque(false);
        modifyResource.setToolTipText(Language.getModifySelectedRowTT());
        modifyResource.setEnabled(false);
        
        removeResource.setOpaque(false);
        removeResource.setToolTipText(Language.getRemoveSelectedRowTT());
        removeResource.setEnabled(false);
        
        addResource.setOpaque(false);
        addResource.setToolTipText(Language.getAddNewRowTT());
        
        org.jdesktop.layout.GroupLayout jPanel10Layout = new org.jdesktop.layout.GroupLayout(
                jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(jPanel10Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel10Layout.createSequentialGroup().addContainerGap().add(
                jPanel10Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                addResource,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                121, Short.MAX_VALUE).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                modifyResource,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                121, Short.MAX_VALUE).add(removeResource,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                121, Short.MAX_VALUE)).addContainerGap()));
        jPanel10Layout.setVerticalGroup(jPanel10Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel10Layout.createSequentialGroup().addContainerGap().add(
                addResource).addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED).add(
                modifyResource).addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED).add(
                removeResource).addContainerGap(
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)));
        
        icppLabel.setText(Language.getICPP());
        icppLabel.setToolTipText(Language.getICPPTT());
        
        icpp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        icpp.setMargin(new Insets(0, 0, 0, 0));
        
        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(
                jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout
                .setHorizontalGroup(jPanel3Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                jPanel3Layout
                .createSequentialGroup()
                .addContainerGap()
                .add(
                jPanel3Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.TRAILING)
                .add(
                org.jdesktop.layout.GroupLayout.LEADING,
                resourceTableScroll,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                506,
                Short.MAX_VALUE)
                .add(
                jPanel3Layout
                .createSequentialGroup()
                .add(
                resourceParametersPanel,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)
                .add(
                jPanel3Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel3Layout
                .createSequentialGroup()
                .add(
                12,
                12,
                12)
                .add(
                jPanel10,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(
                jPanel3Layout
                .createSequentialGroup()
                .add(
                28,
                28,
                28)
                .add(
                icppLabel)
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED)
                .add(
                icpp)))))
                .addContainerGap()));
        jPanel3Layout
                .setVerticalGroup(jPanel3Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel3Layout
                .createSequentialGroup()
                .addContainerGap()
                .add(
                jPanel3Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.TRAILING)
                .add(
                jPanel3Layout
                .createSequentialGroup()
                .add(
                jPanel10,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED,
                20,
                Short.MAX_VALUE)
                .add(
                jPanel3Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.BASELINE)
                .add(
                icppLabel)
                .add(
                icpp))
                .add(
                24,
                24,
                24))
                .add(
                org.jdesktop.layout.GroupLayout.LEADING,
                resourceParametersPanel,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE))
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED)
                .add(
                resourceTableScroll,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                249,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap()));
        tabMainPanel.addTab(Language.getResources(), jPanel3);
        
        /* ACCESS */
        jPanel4.setBackground(new Color(204, 255, 204));
        accessTableScroll
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        accessTableScroll.setAutoscrolls(true);
        accessTableScroll.setOpaque(false);
        accessTable.setModel(new javax.swing.table.DefaultTableModel(
                accessData, new String[] { Language.getProcess(),
                Language.getResource(), Language.getRequestTime(),
                Language.getRequestDuration() }) {
            private static final long serialVersionUID = 275619123;
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        accessTable.setDragEnabled(true);
        accessTableScroll.setViewportView(accessTable);
        
        jPanel11.setOpaque(false);
        removeAccess.setOpaque(false);
        removeAccess.setToolTipText(Language.getRemoveSelectedRowTT());
        removeAccess.setEnabled(false);
        removeAccess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = accessTable.getSelectedRow();
                if (row > -1 && row < createdAccesses) {
                    /* it removes the access from the process's access list. */
                    SimulatedProcess p = (SimulatedProcess) accessTable
                            .getValueAt(row, 0);
                    int reqTime = Integer.parseInt(accessTable.getValueAt(row,
                            2).toString());
                    ArrayList<Access> accessList = p.getAccessesList();
                    boolean removed = false;
                    for (int h = 0; h < accessList.size() && !removed; h++) {
                        if (accessList.get(h).getRequestTime() == reqTime) {
                            accessList.remove(h);
                            removed = true;
                        }
                    }
                    /* it removes the access from the table. */
                    for (int i = row; i < createdAccesses; i++) {
                        accessTable.setValueAt(
                                accessTable.getValueAt(i + 1, 0), i, 0);
                        accessTable.setValueAt(
                                accessTable.getValueAt(i + 1, 1), i, 1);
                        accessTable.setValueAt(
                                accessTable.getValueAt(i + 1, 2), i, 2);
                        accessTable.setValueAt(
                                accessTable.getValueAt(i + 1, 3), i, 3);
                    }
                    createdAccesses--;
                    if (createdAccesses == 0) {
                        modifyAccess.setEnabled(false);
                        removeAccess.setEnabled(false);
                    }
                }
            }
        });
        
        modifyAccess.setOpaque(false);
        modifyAccess.setToolTipText(Language.getModifySelectedRowTT());
        modifyAccess.setEnabled(false);
        modifyAccess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (modifiedAccess == null) {
                    indexModifiedAccess = accessTable.getSelectedRow();
                    if (indexModifiedAccess > -1
                            && indexModifiedAccess < createdAccesses) {
                        processCBX.setSelectedItem(accessTable.getValueAt(
                                indexModifiedAccess, 0));
                        resourceCBX.setSelectedItem(accessTable.getValueAt(
                                indexModifiedAccess, 1));
                        requestTime.setText(String.valueOf(accessTable
                                .getValueAt(indexModifiedAccess, 2)));
                        requestDuration.setText(String.valueOf(accessTable
                                .getValueAt(indexModifiedAccess, 3)));
                        /* it removes the access from the process's access list. */
                        SimulatedProcess p = (SimulatedProcess) accessTable
                                .getValueAt(indexModifiedAccess, 0);
                        int reqTime = Integer.parseInt(accessTable.getValueAt(
                                indexModifiedAccess, 2).toString());
                        ArrayList<Access> accessList = p.getAccessesList();
                        boolean removed = false;
                        for (int h = 0; h < accessList.size() && !removed; h++) {
                            modifiedAccess = accessList.get(h);
                            if (modifiedAccess.getRequestTime() == reqTime) {
                                accessList.remove(h);
                                removed = true;
                            }
                        }
                        addAccess.setEnabled(false);
                        removeAccess.setEnabled(false);
                    }
                } else {
                    /* it tests the sintax and the ammissibility */
                    if (!InputControl.isAccessWrong(String
                            .valueOf(((SimulatedProcess) processCBX
                            .getSelectedItem()).getActivationTime()),
                            String.valueOf(((SimulatedProcess) processCBX
                            .getSelectedItem()).getExecutionTime()),
                            requestTime.getText(), requestDuration.getText())) {
                        if(((SimulatedProcess) processCBX.getSelectedItem())
                        .addNewAccessRequest((Resource) resourceCBX
                                .getSelectedItem(), Integer
                                .parseInt(requestTime.getText()),
                                Integer.parseInt(requestDuration
                                .getText()))) {
                            accessTable.setValueAt(processCBX.getSelectedItem(),
                                    indexModifiedAccess, 0);
                            accessTable.setValueAt(resourceCBX.getSelectedItem(),
                                    indexModifiedAccess, 1);
                            accessTable.setValueAt(requestTime.getText(),
                                    indexModifiedAccess, 2);
                            accessTable.setValueAt(requestDuration.getText(),
                                    indexModifiedAccess, 3);
                            requestTime.setText("");
                            requestDuration.setText("");
                            addAccess.setEnabled(true);
                            removeAccess.setEnabled(true);
                            modifiedAccess = null;
                            indexModifiedProcess = -1;
                        } else {
                            // impossible to insert
                            System.out.println(Language.getER010());
                            new org.rainbow.gui.Error(Language.getER010(), parent);
                        }
                    } else {
                        String errorMessage = "\n";
                        if (InputControl.isRequestTimeWrong(String
                                .valueOf(((SimulatedProcess) processCBX
                                .getSelectedItem()).getExecutionTime()),
                                requestTime.getText())) {
                            errorMessage = Language.getER006() + "\n";
                        }
                        if (InputControl.isRequestDurationWrong(String
                                .valueOf(((SimulatedProcess) processCBX
                                .getSelectedItem()).getExecutionTime()),
                                requestTime.getText(), requestDuration.getText())) {
                            errorMessage = errorMessage + Language.getER009();
                        }
                        errorMessage = errorMessage
                                + "\n"
                                + Language.getActivationTime()
                                + ": "
                                + String.valueOf(((SimulatedProcess) processCBX
                                .getSelectedItem()).getActivationTime());
                        errorMessage = errorMessage
                                + "\n"
                                + Language.getExecutionTime()
                                + ": "
                                + String.valueOf(((SimulatedProcess) processCBX
                                .getSelectedItem()).getExecutionTime());
                        System.out.println(errorMessage);
                        new org.rainbow.gui.Error(errorMessage, parent);
                    }
                }
            }
        });
        
        addAccess.setOpaque(false);
        addAccess.setToolTipText(Language.getAddNewRowTT());
        addAccess.setEnabled(false);
        addAccess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                /* it tests the sintax and the ammissibility */
                if (!InputControl.isAccessWrong(String
                        .valueOf(((SimulatedProcess) processCBX
                        .getSelectedItem()).getActivationTime()),
                        String.valueOf(((SimulatedProcess) processCBX
                        .getSelectedItem()).getExecutionTime()),
                        requestTime.getText(), requestDuration.getText())) {
                    
                    if(((SimulatedProcess) processCBX.getSelectedItem())
                    .addNewAccessRequest((Resource) resourceCBX
                            .getSelectedItem(), Integer
                            .parseInt(requestTime.getText()),
                            Integer.parseInt(requestDuration
                            .getText()))) {
                        // the access is correct
                        accessTable.setValueAt(processCBX.getSelectedItem(),
                                createdAccesses, 0);
                        accessTable.setValueAt(resourceCBX.getSelectedItem(),
                                createdAccesses, 1);
                        accessTable.setValueAt(requestTime.getText(),
                                createdAccesses, 2);
                        accessTable.setValueAt(requestDuration.getText(),
                                createdAccesses, 3);
                        createdAccesses++;
                        requestTime.setText("");
                        requestDuration.setText("");
                        modifyAccess.setEnabled(true);
                        removeAccess.setEnabled(true);
                        if (createdAccesses == RainbowConfig.getMaxAccessesNumber()) {
                            addAccess.setEnabled(false);
                        }
                    } else {
                        // impossible to insert
                        System.out.println(Language.getER010());
                        new org.rainbow.gui.Error(Language.getER010(), parent);
                    }
                } else {
                    String errorMessage = "\n";
                    if (InputControl.isRequestTimeWrong(String
                            .valueOf(((SimulatedProcess) processCBX
                            .getSelectedItem()).getExecutionTime()),
                            requestTime.getText())) {
                        errorMessage = Language.getER006() + "\n";
                    }
                    if (InputControl.isRequestDurationWrong(String
                            .valueOf(((SimulatedProcess) processCBX
                            .getSelectedItem()).getExecutionTime()),
                            requestTime.getText(), requestDuration.getText())) {
                        errorMessage = errorMessage + Language.getER009();
                    }
                    errorMessage = errorMessage
                            + "\n"
                            + Language.getActivationTime()
                            + ": "
                            + String.valueOf(((SimulatedProcess) processCBX
                            .getSelectedItem()).getActivationTime());
                    errorMessage = errorMessage
                            + "\n"
                            + Language.getExecutionTime()
                            + ": "
                            + String.valueOf(((SimulatedProcess) processCBX
                            .getSelectedItem()).getExecutionTime());
                    System.out.println(errorMessage);
                    new org.rainbow.gui.Error(errorMessage, parent);
                }
            }
        });
        
        org.jdesktop.layout.GroupLayout jPanel11Layout = new org.jdesktop.layout.GroupLayout(
                jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout
                .setHorizontalGroup(jPanel11Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel11Layout
                .createSequentialGroup()
                .addContainerGap()
                .add(
                jPanel11Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                jPanel11Layout
                .createSequentialGroup()
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED)
                .add(
                addAccess,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                121,
                Short.MAX_VALUE))
                .add(
                removeAccess,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                121,
                Short.MAX_VALUE)
                .add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                modifyAccess,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                121,
                Short.MAX_VALUE))
                .addContainerGap()));
        jPanel11Layout.setVerticalGroup(jPanel11Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel11Layout.createSequentialGroup().addContainerGap().add(
                addAccess).addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED).add(
                modifyAccess).addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED).add(
                removeAccess).addContainerGap(
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)));
        
        accessParametersPanel.setBorder(BorderFactory.createTitledBorder(null,
                Language.getAccessParameters(),
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, fontPanel,
                new Color(0, 0, 0)));
        accessParametersPanel.setOpaque(false);
        requestDurationLabel.setText(Language.getRequestDuration());
        requestDurationLabel.setFocusable(false);
        
        requestTimeLabel.setText(Language.getRequestTime());
        requestTimeLabel.setFocusable(false);
        
        resourceLabel.setText(Language.getResource());
        
        processLabel.setText(Language.getProcess());
        
        resourceCBX.setEnabled(false);
        
        processCBX.setEnabled(false);
        
        processCBX.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimulatedProcess p = (SimulatedProcess) processCBX
                        .getSelectedItem();
                if(p != null) {
                    requestTime.setToolTipText("[0 .. "
                            + (p.getExecutionTime() - 1) + "]");
                    requestDuration.setToolTipText("[1 .. " + p.getExecutionTime()
                    + "]");
                    requestTimeLabel.setToolTipText("[" + p.getActivationTime()
                    + " .. " + (p.getExecutionTime() - 1) + "]");
                    requestDurationLabel.setToolTipText("[1 .. "
                            + p.getExecutionTime() + "]");
                }
            }
        });
        
        requestTime.setText("");
        requestTime.setEnabled(false);
        requestDuration.setText("");
        requestDuration.setEnabled(false);
        
        org.jdesktop.layout.GroupLayout jPanel12Layout = new org.jdesktop.layout.GroupLayout(
                accessParametersPanel);
        accessParametersPanel.setLayout(jPanel12Layout);
        jPanel12Layout
                .setHorizontalGroup(jPanel12Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel12Layout
                .createSequentialGroup()
                .addContainerGap()
                .add(
                jPanel12Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel12Layout
                .createSequentialGroup()
                .add(
                jPanel12Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                resourceLabel)
                .add(
                requestTimeLabel)
                .add(
                requestDurationLabel))
                .add(
                86,
                86,
                86)
                .add(
                jPanel12Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING,
                false)
                .add(
                requestDuration)
                .add(
                requestTime)
                .add(
                resourceCBX,
                0,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)
                .add(
                processCBX,
                0,
                107,
                Short.MAX_VALUE)))
                .add(processLabel))
                .add(40, 40, 40)));
        jPanel12Layout
                .setVerticalGroup(jPanel12Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                jPanel12Layout
                .createSequentialGroup()
                .addContainerGap()
                .add(
                jPanel12Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.BASELINE)
                .add(processLabel)
                .add(
                processCBX,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED)
                .add(
                jPanel12Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.TRAILING)
                .add(resourceLabel)
                .add(
                resourceCBX,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)
                .add(
                jPanel12Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.BASELINE)
                .add(requestTimeLabel)
                .add(
                requestTime,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(
                jPanel12Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.BASELINE)
                .add(
                requestDurationLabel)
                .add(
                requestDuration,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(28, 28, 28)));
        
        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(
                jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout
                .setHorizontalGroup(jPanel4Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel4Layout
                .createSequentialGroup()
                .addContainerGap()
                .add(
                jPanel4Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.TRAILING)
                .add(
                org.jdesktop.layout.GroupLayout.LEADING,
                accessTableScroll,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                506,
                Short.MAX_VALUE)
                .add(
                jPanel4Layout
                .createSequentialGroup()
                .add(
                accessParametersPanel,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                355,
                Short.MAX_VALUE)
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED)
                .add(
                jPanel11,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap()));
        jPanel4Layout
                .setVerticalGroup(jPanel4Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel4Layout
                .createSequentialGroup()
                .addContainerGap()
                .add(
                jPanel4Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel11,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(
                accessParametersPanel,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE))
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED)
                .add(
                accessTableScroll,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                249,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap()));
        tabMainPanel.addTab(Language.getAccessesToResources(), jPanel4);
        
        /* POLICIES */
        jPanel6.setBackground(new Color(204, 255, 204));
        jPanel13.setBorder(BorderFactory.createTitledBorder(null, Language
                .getQuantum(),
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, fontPanel,
                new Color(0, 0, 0)));
        jPanel13.setToolTipText(Language.getOnlyForTimeSharingPoliciesTT());
        jPanel13.setOpaque(false);
        quantum.setModel(new DefaultComboBoxModel(new String[] {}));
        quantum.setEnabled(false);
        
        org.jdesktop.layout.GroupLayout jPanel13Layout = new org.jdesktop.layout.GroupLayout(
                jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(jPanel13Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel13Layout.createSequentialGroup().addContainerGap().add(
                quantum,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 83,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)));
        jPanel13Layout.setVerticalGroup(jPanel13Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                jPanel13Layout.createSequentialGroup().addContainerGap(
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE).add(quantum,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap()));
        
        jPanel14.setBorder(BorderFactory.createTitledBorder(null, Language
                .getPolicies(),
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, fontPanel,
                new Color(0, 0, 0)));
        jPanel14.setOpaque(false);
        assignmentPolicyLabel.setText(Language.getAssignmentPolicy());
        
        schedulingPolicyLabel.setText(Language.getSchedulingPolicy());
        
        org.jdesktop.layout.GroupLayout jPanel14Layout = new org.jdesktop.layout.GroupLayout(
                jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(jPanel14Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel14Layout.createSequentialGroup().add(
                jPanel14Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel14Layout.createSequentialGroup()
                .addContainerGap().add(
                schedulingPolicyLabel)).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                jPanel14Layout.createSequentialGroup().add(174,
                174, 174).add(schedulingPolicy, 0, 310,
                Short.MAX_VALUE)).add(
                jPanel14Layout.createSequentialGroup()
                .addContainerGap().add(
                assignmentPolicyLabel)).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                jPanel14Layout.createSequentialGroup().add(174,
                174, 174).add(assignmentPolicy, 0, 310,
                Short.MAX_VALUE))).addContainerGap()));
        jPanel14Layout
                .setVerticalGroup(jPanel14Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                jPanel14Layout
                .createSequentialGroup()
                .add(
                jPanel14Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel14Layout
                .createSequentialGroup()
                .addContainerGap()
                .add(
                schedulingPolicyLabel))
                .add(
                jPanel14Layout
                .createSequentialGroup()
                .add(
                35,
                35,
                35)
                .add(
                schedulingPolicy,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)
                .add(assignmentPolicyLabel)
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED)
                .add(
                assignmentPolicy,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(30, 30, 30)));
        
        schedulingPolicy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(schedulingPolicy.getSelectedItem() != null) {
                    String policy = schedulingPolicy.getSelectedItem().toString();
                    if (policy == "Round Robin"
                            || policy == "Priority Round Robin"
                            || policy == "Preemptive on Priority Round Robin"
                            || policy == "Multilevel Feedback"
                            || policy == "Preemptive Multilevel Feedback"
                            || policy == "Multilevel Feedback Dynamic Quantum"
                            || policy == "Linux"
                            || policy == "UNIX"
                            || policy == "Preemptive Multilevel Feedback Dynamic Quantum") {
                        quantum.setEnabled(true);
                    } else {
                        quantum.setEnabled(false);
                    }
                }
            }
        });
        
        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(
                jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout
                .setHorizontalGroup(jPanel6Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel6Layout
                .createSequentialGroup()
                .addContainerGap()
                .add(
                jPanel6Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel14,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)
                .add(
                jPanel6Layout
                .createSequentialGroup()
                .add(
                jPanel13,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap()));
        jPanel6Layout
                .setVerticalGroup(jPanel6Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel6Layout
                .createSequentialGroup()
                .addContainerGap()
                .add(
                jPanel14,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(16, 16, 16)
                .add(
                jPanel6Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel13,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(142, Short.MAX_VALUE)));
        tabMainPanel.addTab(Language.getPolicies(), jPanel6);
        
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(
                jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout
                .setHorizontalGroup(jPanel1Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                jPanel1Layout
                .createSequentialGroup()
                .addContainerGap()
                .add(
                jPanel1Layout
                .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
                .add(
                tabMainPanel,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                535,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(
                jPanel5,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(
                jPanel1Layout.createSequentialGroup().addContainerGap().add(
                tabMainPanel,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 476,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED, 15,
                Short.MAX_VALUE).add(jPanel5,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap()));
        
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
                getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(0, 563,
                Short.MAX_VALUE).add(jPanel1,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING).add(0, 560,
                Short.MAX_VALUE).add(jPanel1,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));
        jPanel5.setOpaque(false);
        jPanel1.setBackground(new Color(205, 205, 255));
        pack();
    }
    
    private Font font;
    
    private Font fontPanel;
    
    private JTabbedPane tabMainPanel;
    
    private JButton addProcess;
    
    private JButton accept;
    
    private JButton abort;
    
    private JButton help;
    
    private JButton removeProcess;
    
    private JButton modifyProcess;
    
    private JButton addResource;
    
    private JButton removeResource;
    
    private JButton modifyResource;
    
    private JButton addAccess;
    
    private JButton removeAccess;
    
    private JButton modifyAccess;
    
    private JCheckBox defaultProcessName;
    
    private JCheckBox defaultResourceName;
    
    private JCheckBox preemptive;
    
    private JCheckBox icpp;
    
    private JComboBox priority;
    
    private JComboBox multiplicity;
    
    private JComboBox processCBX;
    
    private JComboBox resourceCBX;
    
    private JComboBox schedulingPolicy;
    
    private JComboBox quantum;
    
    private JComboBox assignmentPolicy;
    
    private JComboBox ceilingPriority;
    
    private JLabel processNameLabel;
    
    private JLabel processLabel;
    
    private JLabel resourceLabel;
    
    private JLabel requestTimeLabel;
    
    private JLabel requestDurationLabel;
    
    private JLabel schedulingPolicyLabel;
    
    private JLabel assignmentPolicyLabel;
    
    private JLabel icppLabel;
    
    private JLabel ceilingPriorityLabel;
    
    private JLabel defaultResourceNameLabel;
    
    private JLabel executionTimeLabel;
    
    private JLabel activationTimeLabel;
    
    private JLabel priorityLabel;
    
    private JLabel resourceNameLabel;
    
    private JLabel defaultProcessNameLabel;
    
    private JLabel multiplicityLabel;
    
    private JLabel preemptiveLabel;
    
    private JPanel jPanel1;
    
    private JPanel jPanel10;
    
    private JPanel jPanel11;
    
    private JPanel accessParametersPanel;
    
    private JPanel jPanel13;
    
    private JPanel jPanel14;
    
    private JPanel jPanel2;
    
    private JPanel jPanel3;
    
    private JPanel jPanel4;
    
    private JPanel jPanel5;
    
    private JPanel jPanel6;
    
    private JPanel processParametersPanel;
    
    private JPanel jPanel8;
    
    private JPanel resourceParametersPanel;
    
    private JScrollPane resourceTableScroll;
    
    private JScrollPane processTableScroll;
    
    private JScrollPane accessTableScroll;
    
    private JTable resourceTable;
    
    private JTable processTable;
    
    private JTable accessTable;
    
    private JTextField processName;
    
    private JTextField activationTime;
    
    private JTextField executionTime;
    
    private JTextField resourceName;
    
    private JTextField requestTime;
    
    private JTextField requestDuration;
    
    private Color white = new Color(255,255,255);
}
