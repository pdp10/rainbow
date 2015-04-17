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
 * File: RainbowConfig.java
 * Package: gui.input
 * Author: Dalle Pezze Piero
 * Date: 01/03/2007
 * Version: 1.2
 *
 * Modifies:
 *  - v.1.2 (24/11/2014): Simplified the language management. This file does not need to be edited when adding new languages to Rainbow.
 *  - v.1.1 (19/11/2014): Correction: Moved applicationName from Language to here. 
 *  - v.1.0 (01/03/2007): Code and documentation.
 */
package org.rainbow.gui.input;

import org.rainbow.gui.language.*;
import org.rainbow.scheduler.*;
import org.rainbow.data.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import org.xml.sax.SAXException;
import java.util.*;
import java.text.*;

/**
 * The configuration of Rainbow.
 * @author Piero Dalle Pezze
 * @version 1.2
 */
public final class RainbowConfig {
    
    /**
     * The name of the application: Rainbow - A simulator of processes
     * management in a multitasking computer.
     */
    private static String applicationName = "Rainbow";   
    
    
    private static int maxProcessesNumber = 100;
    
    private static int maxResourcesNumber = 100;
    
    private static int maxAccessesNumber = 500;
    
    private static int maxActivationTime = 100;
    
    private static int maxExecutionTime = 100;
    
    private static int minPriority = -50;
    
    private static int maxPriority = 50;
    
    private static int minCeilingPriority = 0;
    
    private static int maxCeilingPriority = 99;
    
    private static int maxMultiplicity = 99;
    
    private static int maxQuantum = 99;
    
    private static int maxLevelInMFpolicies = 10;
    
    private static String language = Language.getLanguages()[0].getName(); //English
    
    private static String theme = "Soft Blue Ice Theme";

    private static String laf = "InfoNode";
    
    // This value can change only here. It is the maximum length of the
    // simulation
    private static int maxLengthSimulation = 15000;
    
    private static String[] schedulingPolicies = new String[] {
        "First In First Out", "Shortest Job First",
        "Shortest Remaining Time First", "Round Robin",
        "Priority Round Robin", "Preemptive on Priority Round Robin",
        //"Linux", "UNIX",
        "Highest Priority First",
        "Preemptive Highest Priority First",
        "Highest Remaining Ratio First", "Multilevel Feedback",
        "Preemptive Multilevel Feedback",
        "Multilevel Feedback Dynamic Quantum",
        "Preemptive Multilevel Feedback Dynamic Quantum" };
    
    private static String[] schedulingPoliciesICPP = new String[] {
        "First In First Out", "Shortest Job First",
        "Priority Round Robin", "Preemptive on Priority Round Robin",
        "Highest Priority First",
        "Preemptive Highest Priority First",
        "Highest Remaining Ratio First" };
    
    
    
    private static String[] assignmentPolicies = new String[] {
        "First In First Out", "Random", "Highest Priority First" };
    
    private static String msg = "";
    
    /**
     * It initializes the configuration of Rainbow by config.xml file. If this
     * file contains not ammissible values, it loads the default configuration.
     */
    public static boolean setRainbowConfig() {
        String xmlFile = "config/config.xml";
        
        boolean errors = false;
        DocumentBuilderFactory factory = null;
        DocumentBuilder builder = null;
        
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);           
            applicationName = doc.getElementsByTagName(
                    "config").item(0).getAttributes().getNamedItem(
                    "application_name").getNodeValue();            
            maxProcessesNumber = Integer.parseInt(doc.getElementsByTagName(
                    "config").item(1).getAttributes().getNamedItem(
                    "max_number_processes").getNodeValue());
            maxResourcesNumber = Integer.parseInt(doc.getElementsByTagName(
                    "config").item(2).getAttributes().getNamedItem(
                    "max_number_resources").getNodeValue());
            maxAccessesNumber = Integer.parseInt(doc.getElementsByTagName(
                    "config").item(3).getAttributes().getNamedItem(
                    "max_number_accesses").getNodeValue());
            maxActivationTime = Integer.parseInt(doc.getElementsByTagName(
                    "config").item(4).getAttributes().getNamedItem(
                    "max_activation_time").getNodeValue());
            maxExecutionTime = Integer.parseInt(doc.getElementsByTagName(
                    "config").item(5).getAttributes().getNamedItem(
                    "max_execution_time").getNodeValue());
            minPriority = Integer.parseInt(doc.getElementsByTagName("config")
            .item(6).getAttributes().getNamedItem("min_priority")
            .getNodeValue());
            maxPriority = Integer.parseInt(doc.getElementsByTagName("config")
            .item(7).getAttributes().getNamedItem("max_priority")
            .getNodeValue());
            minCeilingPriority = Integer.parseInt(doc.getElementsByTagName(
                    "config").item(8).getAttributes().getNamedItem(
                    "min_ceiling_priority").getNodeValue());
            maxCeilingPriority = Integer.parseInt(doc.getElementsByTagName(
                    "config").item(9).getAttributes().getNamedItem(
                    "max_ceiling_priority").getNodeValue());
            maxMultiplicity = Integer.parseInt(doc.getElementsByTagName(
                    "config").item(10).getAttributes().getNamedItem(
                    "max_multiplicity").getNodeValue());
            maxQuantum = Integer.parseInt(doc.getElementsByTagName("config")
            .item(11).getAttributes().getNamedItem("max_quantum")
            .getNodeValue());
            maxLevelInMFpolicies = Integer.parseInt(doc.getElementsByTagName(
                    "config").item(12).getAttributes().getNamedItem(
                    "number_levels_MF_policies").getNodeValue());
            language = doc.getElementsByTagName("config").item(13)
            .getAttributes().getNamedItem("language").getNodeValue();
            theme = doc.getElementsByTagName("config").item(14)
            .getAttributes().getNamedItem("theme").getNodeValue();
            laf = doc.getElementsByTagName("config").item(15)
            .getAttributes().getNamedItem("look_and_feel").getNodeValue();
             
        } catch (NumberFormatException e) {
            System.out
                    .println(xmlFile
                    + " doesn't contains integers values (except for language).");
            setDefaultRainbowConfig();
            errors = true;
            return false;
        } catch (IOException ex) {
            System.out.println("Some I/O errors was found in the file: " + xmlFile);
            setDefaultRainbowConfig();
            errors = true;
            return false;
        } catch (SAXException ex) {
            setDefaultRainbowConfig();
            System.out.println("Parsing error");
            errors = true;
            return false;
        } catch (FactoryConfigurationError ex) {
            System.out.println("Factory error");
            setDefaultRainbowConfig();
            errors = true;
            return false;
        } catch (ParserConfigurationException ex) {
            System.out.println("Parsing error");
            setDefaultRainbowConfig();
            errors = true;
            return false;
        } catch (Exception ex) {
            System.out.println("Corrupted file: " + xmlFile);
            setDefaultRainbowConfig();
            return false;
        }
        
        if (!errors && findErrors()) {
            System.out.println(xmlFile + " contains an error in " + msg
                    + ". See help to configure it correctly.");
            setDefaultRainbowConfig();
            errors = true;
            return false;
        }
        
        if (!errors && !setLanguage()) {
            System.out
                    .println(xmlFile
                    + " contains an error in the language configuration. See help to configure it correctly.");
        }
        if (!errors && !setTheme()) {
            System.out
                    .println(xmlFile
                    + " contains an error in the theme configuration. See help to configure it correctly.");
        }
        if (!errors && !setLookAndFeel()) {
            System.out
                    .println(xmlFile
                    + " contains an error in the look and feel configuration. See help to configure it correctly.");
        }          
        return true;
    }
    
    /**
     * It sets the default configuration of Rainbow.
     */
    private static void setDefaultRainbowConfig() {
	applicationName = "Rainbow";
        maxProcessesNumber = 100;
        maxResourcesNumber = 100;
        maxAccessesNumber = 500;
        maxActivationTime = 100;
        maxExecutionTime = 100;
        minPriority = -50;
        maxPriority = 50;
        minCeilingPriority = 0;
        maxCeilingPriority = 99;
        maxMultiplicity = 99;
        maxQuantum = 99;
        maxLevelInMFpolicies = 10;
        language = Language.getLanguages()[0].getName();
        laf = "InfoNode";
        System.out.println("Setting the default configuration of Rainbow.");
    }
    
    /**
     * It returns true if at least an error in the configuration is found, false otherwise.
     *@return true if at least an error in the configuration is found, false otherwise
     */
    private static boolean findErrors() {
        if (maxProcessesNumber <= 0 || maxProcessesNumber > 10000) {
            msg = "max_number_processes";
            return true;
        }
        if (maxResourcesNumber <= 0 || maxResourcesNumber > 10000) {
            msg = "max_number_resources";
            return true;
        }
        if (maxAccessesNumber <= 0 || maxAccessesNumber > 20000) {
            msg = "max_number_accesses";
            return true;
        }
        if (maxActivationTime <= 0 || maxActivationTime > 14997) {
            msg = "max_activation_time";
            return true;
        }
        if (maxExecutionTime <= 0 || maxExecutionTime > 14998) {
            msg = "max_execution_time";
            return true;
        }
        if (minPriority <= -200 || minPriority > 0) {
            msg = "min_priority";
            return true;
        }
        if (maxPriority <= 0 || maxPriority > 200) {
            msg = "max_priority";
            return true;
        }
        if (minCeilingPriority <= -200 || minCeilingPriority > 0) {
            msg = "min_ceiling_priority";
            return true;
        }
        if (maxCeilingPriority <= 0 || maxCeilingPriority > 200) {
            msg = "max_ceiling_priority";
            return true;
        }
        if (maxMultiplicity <= 0 || maxMultiplicity > 99) {
            msg = "max_multiplicity";
            return true;
        }
        if (maxQuantum <= 0 || maxQuantum > 200) {
            msg = "max_quantum";
            return true;
        }
        if (maxLevelInMFpolicies <= 0 || maxLevelInMFpolicies > 100) {
            msg = "number_levels_MF_policies";
            return true;
        }
        return false;
    }
    
    
    /**
     * It sets the theme of Rainbow.
     *@return true if it can load the theme, false if the theme was not found.
     * In this case it loads the default theme "Soft Blue Ice Theme".
     */
    private static boolean setTheme() {
        if (theme.equals("Default Theme")) return true;
        if (theme.equals("Look and Feel Theme")) return true;
        if (theme.equals("Blue Highlight Theme")) return true;
        if (theme.equals("Slim Flat Theme")) return true;
        if (theme.equals("Gradient Theme")) return true;
        if (theme.equals("Shaped Gradient Theme")) return true;
        if (theme.equals("Soft Blue Ice Theme")) return true;
        if (theme.equals("Classic Theme")) return true;
        theme = "Soft Blue Ice Theme";
        return false;
    }
   
    /**
     * It sets the look and feel of Rainbow.
     *@return true if it can load the look and feel, false if the look and feel was not found.
     * In this case it loads the default look and feel "JGoodies".
     */
    private static boolean setLookAndFeel() {
        if (laf.equals("System")) return true;
        if (laf.equals("InfoNode")) return true;
        if (laf.equals("JGoodies")) return true;
        if (laf.equals("NimRod")) return true;
        laf = "InfoNode";
        return false;
    }
    
    
    /**
     * It sets the language of Rainbow.
     *@return true if it can load the language, false if the language was not found.
     * In this case it loads the default language (GB).
     */
    private static boolean setLanguage() {
        final Language.LanguageItem[] languages = Language.getLanguages();
        boolean found = false;
        for(int i = 0; i < languages.length && !found; i++) {           
            final Language.LanguageItem lang = languages[i];
	    if (language.equals(lang.getName())) {
		found = true;
	    }
        }
        if(!found) {    
            System.out.println("The language " + language
                        + " doesn't exist");        
	    language = Language.getLanguages()[0].getName();
	}
        return found; 
    }
    
    /**
     * It saves in the config.xml file the theme setted.
     * @return true if the theme was saved, false otherwise.
     */
    public static boolean saveTheme(String themeName) {
        theme = themeName;
        return saveConfiguration();
    }
 
    /**
     * It saves in the config.xml file the look and feel setted.
     * @return true if the look and feel was saved, false otherwise.
     */
    public static boolean saveLookAndFeel(String lafName) {
        laf = lafName;
        return saveConfiguration();
    }
    
     /**
     * It saves in the config.xml file the language setted.
     * @return true if the language was saved, false otherwise.
     */
    public static boolean saveLanguage(String newLanguage) {
        final Language.LanguageItem[] languages = Language.getLanguages();
        boolean found = false;
        for(int i = 0; i < languages.length && !found; i++) {           
            final Language.LanguageItem lang = languages[i];
	    if (newLanguage.equals(lang.getName())) {
		language = newLanguage;
		found = true;
	    }
        }
        if(!found) {
	    language = Language.getLanguages()[0].getName(); 
            System.out.println("The language " + newLanguage
                        + " doesn't exist");
        }                
        return saveConfiguration();
    }
    
    
    /**
     * It saves on file the configuration.
     */
    private static boolean saveConfiguration() {
        
        String xmlFile = "config/config.xml";
        boolean saved = true;
        Calendar now = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(
                "E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        
        try {
            // saving
            FileWriter fileout = new FileWriter(xmlFile);
            BufferedWriter filebuf = new BufferedWriter(fileout);
            PrintWriter printout = new PrintWriter(filebuf);
            
            // the xml configuration file of Rainbow
            printout.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            printout.println("");
            printout.println("<!--");
            printout.println("    Document   : config.xml");
            printout.println("    Created on : "
                    + formatter.format(now.getTime()));
            printout.println("    Author     : auto-generated");
            printout.println("    Description:");
            printout
                    .println("        This is the configuration file of Rainbow.");
            printout
                    .println("        You can edit values to change the parameters of Rainbow's configuration.");
            printout.println("-->");
            printout.println("");
            printout.println("<!DOCTYPE words SYSTEM \"config.dtd\">");
            printout.println("<configuration>");
            printout.println("    <config application_name = \""
                    + applicationName + "\"/>");            
            printout.println("    <config max_number_processes = \""
                    + maxProcessesNumber + "\"/>");
            printout.println("    <config max_number_resources = \""
                    + maxResourcesNumber + "\"/>");
            printout.println("    <config max_number_accesses = \""
                    + maxAccessesNumber + "\"/>");
            printout.println("    <config max_activation_time = \""
                    + maxActivationTime + "\"/>");
            printout.println("    <config max_execution_time = \""
                    + maxExecutionTime + "\"/>");
            printout.println("    <config min_priority = \"" + minPriority
                    + "\"/>");
            printout.println("    <config max_priority = \"" + maxPriority
                    + "\"/>");
            printout.println("    <config min_ceiling_priority = \""
                    + minCeilingPriority + "\"/>");
            printout.println("    <config max_ceiling_priority = \""
                    + maxCeilingPriority + "\"/>");
            printout.println("    <config max_multiplicity = \""
                    + maxMultiplicity + "\"/>");
            printout.println("    <config max_quantum = \"" + maxQuantum
                    + "\"/>");
            printout.println("    <config number_levels_MF_policies = \""
                    + maxLevelInMFpolicies + "\"/>");
            printout
                    .println("    <config language = \"" + language + "\"/>");
            printout
                    .println("    <config theme = \"" + theme + "\"/>");
            printout
                    .println("    <config look_and_feel = \"" + laf + "\"/>");             
            printout.println("</configuration>");
            printout.println("");
            printout.println("<!-- end config.xml -->");
            
            printout.close();
            
        } catch (IOException e) {
            System.out
                    .println("Cannot save the new configuration file of Rainbow.");
            saved = false;
        }
        if (saved) {
            System.out.println("A new configuration file was saved in the file  " + xmlFile);
        }
        return saved;
    }
    
    /**
     * It saves on file the configuration of Rainbow.
     * @return true if the configuration was saved, false otherwise.
     */
    public static boolean saveConfiguration(String applicationNameValue, int maxProcessesValue,
            int maxResourcesValue, int maxAccessesValue, int maxATValue,
            int maxETValue, int minPriorityValue, int maxPriorityValue,
            int minCeilingPriorityValue, int maxCeilingPriorityValue,
            int maxMultiplicityValue, int maxQuantumValue,
            int maxLevelInMFpoliciesValue) {
        
        applicationName = applicationNameValue;
        maxProcessesNumber = maxProcessesValue;
        maxResourcesNumber = maxResourcesValue;
        maxAccessesNumber = maxAccessesValue;
        maxActivationTime = maxATValue;
        maxExecutionTime = maxETValue;
        minPriority = minPriorityValue;
        maxPriority = maxPriorityValue;
        minCeilingPriority = minCeilingPriorityValue;
        maxCeilingPriority = maxCeilingPriorityValue;
        maxMultiplicity = maxMultiplicityValue;
        maxQuantum = maxQuantumValue;
        maxLevelInMFpolicies = maxLevelInMFpoliciesValue;
        
        boolean errors = findErrors();
        if (errors) {
            System.out
                    .println("Cannot save the new configuration file of Rainbow.");
            setDefaultRainbowConfig();
            setLanguage();
            setLookAndFeel();
        } else {
            saveConfiguration();
        }
        return errors;
    }
    
    /**
     * It returns the application name.
     *
     * @return the application name.
     */
    public static String getApplicationName() {
        return applicationName;
    }    
    
    /**
     * It returns the all assignment policies' names.
     * @return all assignment policies' names.
     */
    public static String[] getAssignmentPolicies() {
        return assignmentPolicies;
    }
    
    /**
     * It returns the maximum activation time.
     * @return the maximum activation time.
     */
    public static int getMaxActivationTime() {
        return maxActivationTime;
    }
    
    /**
     * It returns the maximum ceiling priority.
     * @return the maximum ceiling priority.
     */
    public static int getMaxCeilingPriority() {
        return maxCeilingPriority;
    }
    
    /**
     * It returns the maximum execution time.
     * @return the maximum execution time.
     */
    public static int getMaxExecutionTime() {
        return maxExecutionTime;
    }
    
    /**
     * It returns the maximum multiplicity.
     * @return the maximum multiplicity.
     */
    public static int getMaxMultiplicity() {
        return maxMultiplicity;
    }
    
    /**
     * It returns the maximum priority.
     * @return the maximum priority.
     */
    public static int getMaxPriority() {
        return maxPriority;
    }
    
    /**
     * It returns the maximum number of processes.
     * @return the maximum number of processes.
     */
    public static int getMaxProcessesNumber() {
        return maxProcessesNumber;
    }
    
    /**
     * It returns the maximum quantum.
     * @return the maximum quantum.
     */
    public static int getMaxQuantum() {
        return maxQuantum;
    }
    
    /**
     * It returns the maximum number of resources.
     * @return the maximum number of resources.
     */
    public static int getMaxResourcesNumber() {
        return maxResourcesNumber;
    }
    
    /**
     * It returns the minimum ceiling priority.
     * @return the minimum ceiling priority.
     */
    public static int getMinCeilingPriority() {
        return minCeilingPriority;
    }
    
    /**
     * It returns the minimum priority.
     * @return the minimum priority.
     */
    public static int getMinPriority() {
        return minPriority;
    }
    
    /**
     * It returns all scheduling policies' names.
     * @return all scheduling policies' names.
     */
    public static String[] getSchedulingPolicies() {
        return schedulingPolicies;
    }
    
    /**
     * It returns all scheduling policies ICPP' names.
     * @return all scheduling policies ICPP' names.
     */
    public static String[] getSchedulingPoliciesICPP() {
        return schedulingPoliciesICPP;
    }
    
    /**
     * It returns the language name.
     * @return the language name.
     */
    public static String getLanguage() {
        return language;
    }
   
    /**
     * It returns the name of the theme.
     * @return the name of the theme.
     */
    public static String getTheme() {
        return theme;
    }
 
    /**
     * It returns the look 'n' feel.
     * @return the look 'n' feel.
     */
    public static String getLookAndFeel() {
        return laf;
    }
    
    /**
     * It returns the maximum number of accesses.
     * @return the maximum number of accesses.
     */
    public static int getMaxAccessesNumber() {
        return maxAccessesNumber;
    }
    
    /**
     * It returns the maximum number of levels in multilevel feedback policies.
     * @return the maximum number of levels in multilevel feedback policies.
     */
    public static int getMaxLevelInMFpolicies() {
        return maxLevelInMFpolicies;
    }
    
    /**
     * It returns the maximum length of the simulation.
     * @return the maximum length of the simulation.
     */
    public static int getMaxLengthSimulation() {
        return maxLengthSimulation;
    }
    
    /**
     * It returns the scheduling policy of the configuratiion.
     *
     * @param configuration The configuration created by the user.
     * @return The scheduling policy specified in the configuration.
     */
    public static SchedulingPolicy getSchedulingPolicy(Configuration configuration) {
        SchedulingPolicy sched = new FIFO();
        
        String pol = configuration.getSchedulingPolicy();
        
        if (pol.equals("First In First Out")) {
            sched = new FIFO();
        }
        if (pol.equals("Shortest Job First")) {
            sched = new SJF();
        }
        if (pol.equals("Shortest Remaining Time First")) {
            sched = new SRTF();
        }
        if (pol.equals("Round Robin")) {
            sched = new RoundRobin(configuration.getTimeslice());
        }
        if (pol.equals("Priority Round Robin")) {
            sched = new PriorityRoundRobin(configuration.getTimeslice(),
                    getMinPriority(), getMaxPriority());
        }
        if (pol.equals("Preemptive on Priority Round Robin")) {
            sched = new PreemptiveOnPriorityRoundRobin(
                    configuration.getTimeslice(), getMinPriority(), getMaxPriority());
        }
        if (pol.equals("Linux")) {
            sched = new LinuxScheduling(configuration.getTimeslice(),
                    getMinPriority(), getMaxPriority());
        }
        if (pol.equals("UNIX")) {
            sched = new UNIXScheduling(configuration.getTimeslice(),
                    getMinPriority(), getMaxPriority());
        }
        if (pol.equals("Highest Priority First")) {
            sched = new HPF(getMinPriority(), getMaxPriority());
        }
        if (pol.equals("Preemptive Highest Priority First")) {
            sched = new PreemptiveHPF(getMinPriority(), getMaxPriority());
        }
        if (pol.equals("Highest Remaining Ratio First")) {
            sched = new HRRN();
        }
        if (pol.equals("Multilevel Feedback")) {
            sched = new MF(configuration.getTimeslice(), getMaxLevelInMFpolicies());
        }
        if (pol.equals("Preemptive Multilevel Feedback")) {
            sched = new PreemptiveMF(configuration.getTimeslice(),
                    getMaxLevelInMFpolicies());
        }
        if (pol.equals("Multilevel Feedback Dynamic Quantum")) {
            sched = new MFDQ(configuration.getTimeslice(), getMaxLevelInMFpolicies());
        }
        if (pol.equals("Preemptive Multilevel Feedback Dynamic Quantum")) {
            sched = new PreemptiveMFDQ(configuration.getTimeslice(),
                    getMaxLevelInMFpolicies());
        }
        return sched;
    }
    
    /**
     * It returns the assignment policy of the configuratiion.
     *
     * @param configuration The configuration created by the user.
     * @return The assignment policy specified in the configuration.
     */
    public static AssignmentPolicy getAssignmentPolicy(Configuration configuration) {
        AssignmentPolicy assign = new FIFOAssign();
        String pol = configuration.getAssignmentPolicy();
        if (pol.equals("First In First Out")) {
            assign = new FIFOAssign();
        }
        if (pol.equals("Random")) {
            assign = new RandomAssign();
        }
        if (pol.equals("Highest Priority First")) {
            assign = new HPFAssign();
        }
        return assign;
    }
    
    
}
