/*
 * Rainbow - A simulator of processes and resources in a multitasking computer.
 * Copyright (C) 2006 2007 2008 E-mail: piero.dallepezze@gmail.com
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
 * File: FileManager.java
 * Package: gui
 * Author: Sarto Carlo, Dalle Pezze Piero
 * Date: 16/03/2006
 * Version: 1.4
 *
 * Modifies
 * - v.1.4 (1/04/2008): Removed method exportXML. XML is used now to save and open regulary 
 *                      configuration files. To retro-compatibility importFCS() is kept. Dalle Pezze Piero.
 * - v.1.3 (08/02/2008): Method importXML(). It remains the control of accesses. Executed test on
                         XML parser: No error notified. Piero Dalle Pezze.
 * - v.1.2 (22/05/2007): Added methods exportXML() and exportHTML(). Piero Dalle Pezze.
 * - v.1.1 (01/05/2007): English translation. Java6 compatible. (Dalle Pezze Piero).
 * - v.1.0 (16/03/2006): Documentation and codify. Sarto Carlo.
 */
package org.rainbow.gui;

import java.io.*;
import java.util.*;

import org.rainbow.data.*;
import org.rainbow.gui.language.*;
import org.rainbow.gui.input.*;

import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.w3c.dom.*;

/**
 * It realizes the functionalities to:
 * <ol>
 * <li>open a saved configuration of Rainbow</li>
 * <li>save a configuration initialized.</li>
 * <li>import in fcs format. (retro-compatibility)</li>
 * <li>export in html format.</li>
 * </ol>
 *
 * @author Sarto Carlo
 * @author Piero Dalle Pezze
 * @version 1.4
 */
public class FileManager {

	RainbowMainGUI gui = null;

	/**
	 * Default constructor.
	 */
	public FileManager(RainbowMainGUI gui) {
		super();
		this.gui = gui;
	}

	/**
	 * It returns the extension of the file.
	 *
	 * @param file
	 *            The file.
	 * @return The extension of the file.
	 */
	private String getExtension(File file) {
		String ext = null;
		String s = file.getName();
		int i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	/**
	 * It allows to save a configuration in the file system using xml format.
	 *
	 * @param configuration
	 *            The configuration to save.
	 * @param frame
	 *            The window parent.
	 * @return True if the configuration is exported, false otherwise.
	 */
	public boolean saveFile(Configuration configuration, JFrame frame) {
		if (configuration == null)
			return false;

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.addChoosableFileFilter(new FileFilter() {

			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}

				String extension = getExtension(f);
				if (extension != null && extension.equals("xml"))
					return true;
				return false;
			}

			// The description of this filter
			public String getDescription() {
				return new String(Language.getExportXML());

			}
		});

		int returnVal = chooser.showSaveDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File saveFile = chooser.getSelectedFile();

			if (getExtension(saveFile) == null) {
				String newFile = new String(saveFile.getAbsolutePath() + ".xml");
				saveFile = new File(newFile);
			}
			Writer output = null;
			// saving in the FS
			try {
				if (saveFile.exists()) {
					int result = JOptionPane.showConfirmDialog(frame,
							"The name of the file already exists. Overwrite?",
							"Save the configuration",
							JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.CANCEL_OPTION)
						return false;
				}
				gui.setStatusMessage("Saving file.xml ...");
				Calendar calendar = Calendar.getInstance();
				ArrayList<SimulatedProcess> processes = configuration
						.getProcesses();
				ArrayList<Resource> resources = configuration.getResources();

				// declared here only to make visible to finally clause; generic
				// reference
				output = new BufferedWriter(new FileWriter(saveFile));
				;
				// use buffering
				// FileWriter always assumes default encoding is OK!

				output.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
				output.write("\n");
				output.write("<!--\n");
				output.write("  Rainbow - A simulator of processes and resources in a multitasking computer.\n");
				output.write("  Copyright (C) 2006. E-mail: piero.dallepezze@gmail.com\n");
				output.write("\n");
				output.write("  This program is free software; you can redistribute it and/or modify\n");
				output.write("  it under the terms of the GNU General Public License as published by\n");
				output.write("  the Free Software Foundation; either version 2 of the License, or\n");
				output.write("  (at your option) any later version.\n");
				output.write("\n");
				output.write("  This program is distributed in the hope that it will be useful,\n");
				output.write("  but WITHOUT ANY WARRANTY; without even the implied warranty of\n");
				output.write("  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n");
				output.write("  GNU General Public License for more details.\n");
				output.write("\n");
				output.write("  You should have received a copy of the GNU General Public License along\n");
				output.write("  with this program; if not, write to the Free Software Foundation, Inc.,\n");
				output.write("  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.\n");
				output.write("-->\n");
				output.write("\n");
				output.write("<!--\n");
				output.write("  This is a configuration file exported by using the software Rainbow.\n");
				output.write("  Created on: " + calendar.getTime().toString()
						+ "\n");
				output.write("-->\n");
				output.write("\n\n");

				output.write("<configuration>\n");
				output.write("      <schedulingPolicy>"
						+ configuration.getSchedulingPolicy()
						+ "</schedulingPolicy>\n");
				output.write("      <assignmentPolicy>"
						+ configuration.getAssignmentPolicy()
						+ "</assignmentPolicy>\n");
				output.write("      <timeSlice>" + configuration.getTimeslice()
						+ "</timeSlice>\n");
				output.write("      <icpp>" + configuration.isICPP()
						+ "</icpp>\n");
				// insert data about processes
				output.write("  <processes>\n");
				for (int i = 0; i < processes.size(); i++) {
					SimulatedProcess p = processes.get(i);
					output.write("      <process>\n");
					output.write("          <name>" + p.getName() + "</name>\n");
					output.write("          <activationTime>"
							+ (new Integer(p.getActivationTime())).toString()
							+ "</activationTime>\n");
					output.write("          <executionTime>"
							+ (new Integer(p.getExecutionTime())).toString()
							+ "</executionTime>\n");
					output.write("          <basePriority>"
							+ (new Integer(p.getInitialPriority())).toString()
							+ "</basePriority>\n");
					output.write("      </process>\n");
				}
				output.write("  </processes>\n");
				// insert data about resources
				output.write("  <resources>\n");
				for (int i = 0; i < resources.size(); i++) {
					Resource r = resources.get(i);
					boolean b = (r instanceof PreemptiveResource);
					output.write("      <resource>\n");
					output.write("          <name>" + r.getName() + "</name>\n");
					output.write("          <preemptive>"
							+ (new Boolean(b)).toString() + "</preemptive>\n");
					output.write("          <multiplicity>"
							+ (new Integer(r.getMultiplicity())).toString()
							+ "</multiplicity>\n");
					if (b) {
						output.write("          <ceilingPriority>--</ceilingPriority>\n");
					} else {
						NoPreemptiveResource npr = (NoPreemptiveResource) r;
						output.write("          <ceilingPriority>"
								+ (new Integer(npr.getCeilingPriority()))
										.toString() + "</ceilingPriority>\n");
					}
					output.write("      </resource>\n");
				}
				output.write("  </resources>\n");
				// insert data about accesses
				ArrayList<Access> accessList = null;
				Access access = null;
				output.write("  <accesses>\n");
				for (int i = 0; i < processes.size(); i++) {
					SimulatedProcess p = processes.get(i);
					accessList = p.getAccessesList();
					if (accessList != null) {
						for (int j = 0; j < accessList.size(); j++) {
							access = accessList.get(j);
							output.write("      <access>\n");
							output.write("          <processName>"
									+ p.getName() + "</processName>\n");
							output.write("          <resourceName>"
									+ access.getResource().getName()
									+ "</resourceName>\n");
							output.write("          <requestTime>"
									+ (new Integer(access.getRequestTime()))
											.toString() + "</requestTime>\n");
							output.write("          <requestDuration>"
									+ (new Integer(access.getDuration()))
											.toString()
									+ "</requestDuration>\n");
							output.write("      </access>\n");
						}
					}
				}
				output.write("  </accesses>\n");
				output.write("</configuration>\n");
				output.write("\n\n<!-- end file -->\n");
				output.close();
				gui.setStatusMessage("Configuration file exported!");
				return true;
			} catch (IOException e) {
				new org.rainbow.gui.Error("Configuration file not exported!",
						frame);
				gui.setStatusMessage("Configuration file not exported!");
				System.out.println("[NOTICE ER001] [DATE:"
						+ Calendar.getInstance().getTime().toString()
						+ "] [MESSAGE: " + "Impossible to save" + "]");
			}
		}
		return false;
	}

	/**
	 * It allows to import a configuration in the file system using xml format.
	 *
	 * @param frame
	 *            The window parent.
	 * @return The configuration to open.
	 */
	public Configuration openFile(JFrame frame) {

		Configuration conf = null;
		ArrayList<SimulatedProcess> confProcesses = new ArrayList<SimulatedProcess>();
		ArrayList<Resource> confResources = new ArrayList<Resource>();
		String SP = "", AP = "";
		int TS = 1;
		boolean ICPP = false;

		try {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogType(JFileChooser.OPEN_DIALOG);
			chooser.addChoosableFileFilter(new FileFilter() {
				public boolean accept(File f) {
					if (f.isDirectory()) {
						return true;
					}

					String extension = getExtension(f);
					if (extension != null && extension.equals("xml"))
						return true;
					return false;
				}

				// The description of this filter
				public String getDescription() {
					return new String(Language.getConfigurationFile());

				}
			});

			Boolean error = false;
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = null;

			int returnVal = chooser.showOpenDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				doc = docBuilder.parse(chooser.getSelectedFile());
			} else {
				return null;
			}

			// normalize text representation
			doc.getDocumentElement().normalize();
			// System.out.println ("Root element of the doc is " +
			// doc.getDocumentElement().getNodeName());

			// READ GENERAL CONFIGURATION
			// schedulingPolicy
			NodeList schedulingPolicy = doc
					.getElementsByTagName("schedulingPolicy");
			NodeList SPList = ((Element) schedulingPolicy.item(0))
					.getChildNodes();
			if (InputControl.isSchedulingPolicyWrong(((Node) SPList.item(0))
					.getNodeValue())) {
				new org.rainbow.gui.Error("Scheduling policy is not valid!",
						frame);
				gui.setStatusMessage("Scheduling policy is not valid!");
				return null;
			} else {
				SP = (String) ((Node) SPList.item(0)).getNodeValue();
			}
			// System.out.println("SP : " +
			// ((Node)SPList.item(0)).getNodeValue());

			// assignmentPolicy
			NodeList assignmentPolicy = doc
					.getElementsByTagName("assignmentPolicy");
			NodeList APList = ((Element) assignmentPolicy.item(0))
					.getChildNodes();
			if (InputControl.isAssignmentPolicyWrong((String) ((Node) APList
					.item(0)).getNodeValue())) {
				new org.rainbow.gui.Error("Assignment policy is not valid!",
						frame);
				gui.setStatusMessage("Assignment policy is not valid!");
				return null;
			} else {
				AP = (String) ((Node) APList.item(0)).getNodeValue();
			}
			// System.out.println("AP : " +
			// ((Node)APList.item(0)).getNodeValue());

			// timeSlice
			NodeList timeSlice = doc.getElementsByTagName("timeSlice");
			NodeList TSList = ((Element) timeSlice.item(0)).getChildNodes();
			if (InputControl.isQuantumWrong((String) ((Node) TSList.item(0))
					.getNodeValue())) {
				new org.rainbow.gui.Error("Time slice is not valid!", frame);
				gui.setStatusMessage("Time slice is not valid!");
				return null;
			} else {
				TS = Integer.parseInt(((Node) TSList.item(0)).getNodeValue());
			}
			// System.out.println("TS : " +
			// ((Node)TSList.item(0)).getNodeValue());

			// icpp
			NodeList icpp = doc.getElementsByTagName("icpp");
			NodeList ICPPList = ((Element) icpp.item(0)).getChildNodes();
			if (!((Node) ICPPList.item(0)).getNodeValue().equals("true")
					&& !((Node) ICPPList.item(0)).getNodeValue()
							.equals("false")) {
				new org.rainbow.gui.Error("ICPP is not valid!", frame);
				gui.setStatusMessage("ICPP is not valid!");
				return null;
			} else {
				if (((Node) ICPPList.item(0)).getNodeValue().equals("true")) {
					ICPP = true;
				} else {
					ICPP = false;
				}
			}
			// System.out.println("ICPP : " +
			// ((Node)ICPPList.item(0)).getNodeValue());

			// READ PROCESSES
			NodeList listOfProcesses = doc.getElementsByTagName("process");
			// System.out.println("Total no of processes : " +
			// listOfProcesses.getLength());
			for (int s = 0; s < listOfProcesses.getLength() && !error; s++) {
				Node processNode = listOfProcesses.item(s);
				if (processNode.getNodeType() == Node.ELEMENT_NODE) {
					Element processElement = (Element) processNode;
					// NAME
					NodeList nameList = processElement
							.getElementsByTagName("name");
					Element nameElement = (Element) nameList.item(0);
					NodeList textFNList = nameElement.getChildNodes();
					// System.out.println("Name : " +
					// ((Node)textFNList.item(0)).getNodeValue());
					// ACTIVATION TIME
					NodeList activationTimeList = processElement
							.getElementsByTagName("activationTime");
					Element activationTimeElement = (Element) activationTimeList
							.item(0);
					NodeList textLNList = activationTimeElement.getChildNodes();
					// System.out.println("Activation Time : " +
					// ((Node)textLNList.item(0)).getNodeValue());
					// EXECUTION TIME
					NodeList executionTimeList = processElement
							.getElementsByTagName("executionTime");
					Element executionTimeElement = (Element) executionTimeList
							.item(0);
					NodeList executionTimeCNList = executionTimeElement
							.getChildNodes();
					// System.out.println("Execution Time : " +
					// ((Node)executionTimeCNList.item(0)).getNodeValue());
					// BASE PRIORITY
					NodeList basePriorityList = processElement
							.getElementsByTagName("basePriority");
					Element basePriorityElement = (Element) basePriorityList
							.item(0);
					NodeList textbasePriorityList = basePriorityElement
							.getChildNodes();
					// System.out.println("Base Priority : " +
					// ((Node)textbasePriorityList.item(0)).getNodeValue());
					boolean nameUsed = false;
					for (int j = 0; j < confProcesses.size() && !nameUsed; j++) {
						if ((((Node) textFNList.item(0)).getNodeValue())
								.equals(confProcesses.get(j).getName())) {
							nameUsed = true;
						}
					}
					if (nameUsed) {
						new org.rainbow.gui.Error("The process's name "
								+ ((Node) textFNList.item(0)).getNodeValue()
								+ " is already used", frame);
						gui.setStatusMessage("The process's name "
								+ ((Node) textFNList.item(0)).getNodeValue()
								+ " is already used");
						return null;
					}

					if (InputControl
							.isProcessWrong(((Node) textLNList.item(0))
									.getNodeValue(),
									((Node) executionTimeCNList.item(0))
											.getNodeValue(),
									((Node) textbasePriorityList.item(0))
											.getNodeValue())) {
						new org.rainbow.gui.Error("The process "
								+ ((Node) textFNList.item(0)).getNodeValue()
								+ " is not valid!", frame);
						gui.setStatusMessage("The process "
								+ ((Node) textFNList.item(0)).getNodeValue()
								+ " is not valid");
						return null;
					} else {
						confProcesses.add(new SimulatedProcess(
								((Node) textFNList.item(0)).getNodeValue(),
								Integer.parseInt(((Node) textLNList.item(0))
										.getNodeValue()), Integer
										.parseInt(((Node) executionTimeCNList
												.item(0)).getNodeValue()),
								Integer.parseInt(((Node) textbasePriorityList
										.item(0)).getNodeValue())));

					}
				}
			}
			// READ RESOURCE
			NodeList listOfResources = doc.getElementsByTagName("resource");
			// System.out.println("Total no of resources : " +
			// listOfResources.getLength());
			for (int s = 0; s < listOfResources.getLength() && !error; s++) {
				Node resourceNode = listOfResources.item(s);
				if (resourceNode.getNodeType() == Node.ELEMENT_NODE) {
					Element resourceElement = (Element) resourceNode;
					// NAME
					NodeList nameList = resourceElement
							.getElementsByTagName("name");
					Element nameElement = (Element) nameList.item(0);
					NodeList textFNList = nameElement.getChildNodes();
					// System.out.println("Name : " +
					// ((Node)textFNList.item(0)).getNodeValue());

					// PREEMPTIVE
					NodeList preemptiveList = resourceElement
							.getElementsByTagName("preemptive");
					Element preemptiveElement = (Element) preemptiveList
							.item(0);
					NodeList preemptiveCNList = preemptiveElement
							.getChildNodes();
					// System.out.println("Preemptive : " +
					// ((Node)preemptiveCNList.item(0)).getNodeValue());

					// MULTIPLICITY
					NodeList multiplicityList = resourceElement
							.getElementsByTagName("multiplicity");
					Element multiplicityElement = (Element) multiplicityList
							.item(0);
					NodeList multiplicityCNList = multiplicityElement
							.getChildNodes();
					// System.out.println("Multiplicity : " +
					// ((Node)multiplicityCNList.item(0)).getNodeValue());

					// CEILING PRIORITY
					NodeList cpList = resourceElement
							.getElementsByTagName("ceilingPriority");
					Element cpElement = (Element) cpList.item(0);
					NodeList cpCNList = cpElement.getChildNodes();
					// System.out.println("Ceiling Priority : " +
					// ((Node)cpCNList.item(0)).getNodeValue());
					boolean nameUsed = false;
					for (int j = 0; j < confResources.size() && !nameUsed; j++) {
						if ((((Node) textFNList.item(0)).getNodeValue())
								.equals(confResources.get(j).getName())) {
							nameUsed = true;
						}
					}
					if (nameUsed) {
						new org.rainbow.gui.Error("The resource's name "
								+ ((Node) textFNList.item(0)).getNodeValue()
								+ " is already used", frame);
						gui.setStatusMessage("The resource's name "
								+ ((Node) textFNList.item(0)).getNodeValue()
								+ " is already used");
						return null;
					}
					if ((((Node) preemptiveCNList.item(0)).getNodeValue())
							.equals("true")) {
						if (InputControl
								.isPreemptiveResourceWrong(((Node) multiplicityCNList
										.item(0)).getNodeValue())) {
							new org.rainbow.gui.Error(
									"The resource "
											+ ((Node) textFNList.item(0)).getNodeValue()
											+ " is not valid", frame);
							gui.setStatusMessage("The resource "
									+ ((Node) textFNList.item(0))
											.getNodeValue() + " is not valid");
							return null;
						} else {
							confResources.add(new PreemptiveResource(
									((Node) textFNList.item(0)).getNodeValue(),
									Integer.parseInt(((Node) multiplicityCNList
											.item(0)).getNodeValue())));
						}
					} else {
						if ((((Node) preemptiveCNList.item(0)).getNodeValue())
								.equals("false")) {
							if (InputControl.isNoPreemptiveResourceWrong(
									((Node) multiplicityCNList.item(0))
											.getNodeValue(), ((Node) cpCNList
											.item(0)).getNodeValue())) {
								new org.rainbow.gui.Error(
										"The resource "
												+ ((Node) textFNList.item(0)).getNodeValue()
												+ " is not valid", frame);
								gui.setStatusMessage("The resource "
										+ ((Node) textFNList.item(0))
												.getNodeValue()
										+ " is not valid");
								return null;
							} else {
								confResources
										.add(new NoPreemptiveResource(
												((Node) textFNList.item(0))
														.getNodeValue(),
												Integer.parseInt(((Node) multiplicityCNList
														.item(0))
														.getNodeValue()),
												Integer.parseInt(((Node) cpCNList
														.item(0))
														.getNodeValue())));
							}
						} else {
							new org.rainbow.gui.Error(
									"The resource "
											+ ((Node) textFNList.item(0)).getNodeValue()
											+ " is not valid", frame);
							gui.setStatusMessage("The resource "
									+ ((Node) textFNList.item(0))
											.getNodeValue() + " is not valid");
							return null;
						}

					}
				}
			}

			// READ ACCESSES
			NodeList listOfAccesses = doc.getElementsByTagName("access");
			// System.out.println("Total no of accesses : " +
			// listOfAccesses.getLength());
			for (int s = 0; s < listOfAccesses.getLength() && !error; s++) {
				Node accessNode = listOfAccesses.item(s);
				if (accessNode.getNodeType() == Node.ELEMENT_NODE) {
					Element accessElement = (Element) accessNode;
					// PROCESS NAME
					NodeList pList = accessElement
							.getElementsByTagName("processName");
					Element pElement = (Element) pList.item(0);
					NodeList pCNList = pElement.getChildNodes();
					// System.out.println("Process Name : " +
					// ((Node)pCNList.item(0)).getNodeValue());

					// RESOURCE NAME
					NodeList rList = accessElement
							.getElementsByTagName("resourceName");
					Element rElement = (Element) rList.item(0);
					NodeList rCNList = rElement.getChildNodes();
					// System.out.println("Resource Name : " +
					// ((Node)rCNList.item(0)).getNodeValue());

					// REQUEST TIME
					NodeList rtList = accessElement
							.getElementsByTagName("requestTime");
					Element rtElement = (Element) rtList.item(0);
					NodeList rtCNList = rtElement.getChildNodes();
					// System.out.println("Request Time : " +
					// ((Node)rtCNList.item(0)).getNodeValue());

					// REQUEST DURATION
					NodeList rdList = accessElement
							.getElementsByTagName("requestDuration");
					Element rdElement = (Element) rdList.item(0);
					NodeList rdCNList = rdElement.getChildNodes();
					// System.out.println("Request Duration : " +
					// ((Node)rdCNList.item(0)).getNodeValue());

					boolean found = false;
					SimulatedProcess currentProcess = null;
					for (int j = 0; j < confProcesses.size() && !found; j++) {
						if ((((Node) pCNList.item(0)).getNodeValue())
								.equals(confProcesses.get(j).getName())) {
							currentProcess = confProcesses.get(j);
							found = true;
						}
					}
					if (!found) {
						new org.rainbow.gui.Error("The process in the access "
								+ s + " does not exist", frame);
						gui.setStatusMessage("The process in the access " + s
								+ " does not exist");
						return null;
					}
					found = false;
					Resource currentResource = null;
					for (int j = 0; j < confResources.size() && !found; j++) {
						if ((((Node) rCNList.item(0)).getNodeValue())
								.equals(confResources.get(j).getName())) {
							currentResource = confResources.get(j);
							found = true;
						}
					}
					if (!found) {
						new org.rainbow.gui.Error("The resource in the access "
								+ s + " does not exist", frame);
						gui.setStatusMessage("The resource in the access " + s
								+ " does not exist");
						return null;
					}
					if (InputControl.isRequestTimeWrong(
							String.valueOf(currentProcess.getExecutionTime()),
							((Node) rtCNList.item(0)).getNodeValue())) {
						new org.rainbow.gui.Error(
								"The request time of the access " + s
										+ " is not valid", frame);
						gui.setStatusMessage("The request time of the access "
								+ s + " is not valid");
						return null;
					}
					if (InputControl.isRequestDurationWrong(
							String.valueOf(currentProcess.getExecutionTime()),
							((Node) rtCNList.item(0)).getNodeValue(),
							((Node) rdCNList.item(0)).getNodeValue())) {
						new org.rainbow.gui.Error(
								"The request duration of the access " + s
										+ " is not valid", frame);
						gui.setStatusMessage("The request duration of the access "
								+ s + " is not valid");
						return null;
					}
					if (InputControl.isAccessWrong(
							String.valueOf(currentProcess.getActivationTime()),
							String.valueOf(currentProcess.getExecutionTime()),
							((Node) rtCNList.item(0)).getNodeValue(),
							((Node) rdCNList.item(0)).getNodeValue())) {
						new org.rainbow.gui.Error("The access " + s
								+ " is not valid", frame);
						gui.setStatusMessage("The access " + s
								+ " is not valid");
						return null;
					} else {
						currentProcess.addNewAccessRequest(currentResource,
								Integer.parseInt(((Node) rtCNList.item(0))
										.getNodeValue()), Integer
										.parseInt(((Node) rdCNList.item(0))
												.getNodeValue()));
					}
				}
			}

		} catch (SAXParseException err) {
			System.out.println("** Parsing error" + ", line "
					+ err.getLineNumber() + ", uri " + err.getSystemId());
			System.out.println(" " + err.getMessage());
			return null;
		} catch (SAXException e) {
			Exception x = e.getException();
			((x == null) ? e : x).printStackTrace();
			return null;
		} catch (Exception e) {
			new org.rainbow.gui.Error("Configuration file corrupted!", frame);
			gui.setStatusMessage("Configuration file corrupted!");
			System.out.println("[NOTICE ER002] [DATE:"
					+ Calendar.getInstance().getTime().toString()
					+ "] [MESSAGE: " + "The selected file is corrupted" + "]");
			return null;
		}
		conf = new Configuration(AP, SP, confProcesses, confResources);
		conf.setICPP(ICPP);
		conf.setTimeslice(TS);
		gui.setStatusMessage("Configuration file opened!");
		return conf;
	}

	/**
	 * It allows to export a configuration in the file system using a html
	 * format.
	 *
	 * @param configuration
	 *            The configuration to save.
	 * @param frame
	 *            The window parent.
	 * @return True if the configuration is exported, false otherwise.
	 */
	public boolean exportHTML(Configuration configuration, JFrame frame) {
		if (configuration == null)
			return false;

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.addChoosableFileFilter(new FileFilter() {

			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}

				String extension = getExtension(f);
				if (extension != null && extension.equals("html"))
					return true;
				return false;
			}

			// The description of this filter
			public String getDescription() {
				return new String(Language.getExportHTML());

			}
		});

		int returnVal = chooser.showSaveDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File saveFile = chooser.getSelectedFile();

			if (getExtension(saveFile) == null) {
				String newFile = new String(saveFile.getAbsolutePath()
						+ ".html");
				saveFile = new File(newFile);
			}
			Writer output = null;
			// saving in the FS
			try {
				if (saveFile.exists()) {
					int result = JOptionPane.showConfirmDialog(frame,
							"The name of the file already exists. Overwrite?",
							"Save the configuration",
							JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.CANCEL_OPTION)
						return false;
				}
				gui.setStatusMessage("Saving file.html ...");
				Calendar calendar = Calendar.getInstance();
				ArrayList<SimulatedProcess> processes = configuration
						.getProcesses();
				ArrayList<Resource> resources = configuration.getResources();

				// declared here only to make visible to finally clause; generic
				// reference
				output = new BufferedWriter(new FileWriter(saveFile));
				;
				// use buffering
				// FileWriter always assumes default encoding is OK!

				output.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
				output.write("");
				output.write("<!Rainbow - A simulator of processes and resources in a multitasking computer.");
				output.write("Copyright (C) 2006. E-mail: piero.dallepezze@gmail.com");
				output.write("");
				output.write("This program is free software; you can redistribute it and/or modify");
				output.write("it under the terms of the GNU General Public License as published by");
				output.write("the Free Software Foundation; either version 2 of the License, or");
				output.write("(at your option) any later version.");
				output.write("");
				output.write("This program is distributed in the hope that it will be useful,");
				output.write("but WITHOUT ANY WARRANTY; without even the implied warranty of");
				output.write("MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the");
				output.write("GNU General Public License for more details.");
				output.write("");
				output.write("You should have received a copy of the GNU General Public License along");
				output.write("with this program; if not, write to the Free Software Foundation, Inc.,");
				output.write("51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.>");
				output.write("");
				output.write("<HTML>");
				output.write("<HEAD>");
				output.write("<META HTTP-EQUIV=\"CONTENT-TYPE\" CONTENT=\"text/html; charset=utf-8\">");
				output.write("<TITLE>Configuration of a simulation (Rainbow)</TITLE>");
				output.write("<META NAME=\"GENERATOR\" CONTENT=\"Rainbow 3.0\">");
				output.write("</HEAD>");

				// THE BODY
				output.write("<BODY LANG=\"en-US\" BGCOLOR=\"#ccffff\" DIR=\"LTR\">");
				output.write("<P><BR><BR>");
				output.write("</P>");
				output.write("<P><FONT FACE=\"Courier 10 Pitch\"># This is a configuration file ");
				output.write("exported by using the software Rainbow.</FONT></P>");
				output.write("<P><FONT FACE=\"Courier 10 Pitch\"># Created on: "
						+ calendar.getTime().toString() + "</FONT></P>");
				output.write("<P><BR><BR>");
				output.write("</P>");
				output.write("<P><FONT FACE=\"Courier 10 Pitch\"><I><U><FONT SIZE=4><B>"
						+ Language.getPolicies() + ", ");
				output.write(Language.getQuantum() + ", " + Language.getICPP()
						+ "</B></FONT></U></I></FONT></P>");
				output.write("<UL>");
				output.write("<LI><P><FONT FACE=\"Courier 10 Pitch\"><B>"
						+ Language.getSchedulingPolicy() + ":</B> "
						+ configuration.getSchedulingPolicy() + "</FONT>");
				output.write("</P>");
				output.write("<LI><P><FONT FACE=\"Courier 10 Pitch\"><B>"
						+ Language.getAssignmentPolicy() + ":</B> "
						+ configuration.getAssignmentPolicy() + "</FONT>");
				output.write("</P>");
				output.write("<LI><P><FONT FACE=\"Courier 10 Pitch\"><B>"
						+ Language.getQuantum() + ":</B> "
						+ configuration.getTimeslice() + "</FONT>");
				output.write("</P>");
				output.write("<LI><P><FONT FACE=\"Courier 10 Pitch\"><B>"
						+ Language.getICPPTT() + ":</B> "
						+ configuration.isICPP() + "</FONT>");
				output.write("</P>");
				output.write("</UL>");
				output.write("<BR><BR>");
				output.write("<H3><FONT FACE=\"Courier 10 Pitch\"><I><U>"
						+ Language.getProcessParameters() + "</U></I>");
				output.write("</FONT>");
				output.write("</H3>");
				output.write("<CENTER>");
				output.write("<TABLE WIDTH=959 BORDER=1 BORDERCOLOR=\"#666666\" CELLPADDING=4 CELLSPACING=0>");
				output.write("<COL WIDTH=231>");
				output.write("<COL WIDTH=232>");
				output.write("<COL WIDTH=232>");
				output.write("<COL WIDTH=231>");
				output.write("<TR VALIGN=TOP>");
				output.write("<TH WIDTH=231>");
				output.write("<P><FONT FACE=\"Courier 10 Pitch\">"
						+ Language.getName() + "</FONT></P>");
				output.write("</TH>");
				output.write("<TH WIDTH=232>");
				output.write("<P><FONT FACE=\"Courier 10 Pitch\">"
						+ Language.getActivationTime() + "</FONT></P>");
				output.write("</TH>");
				output.write("<TH WIDTH=232>");
				output.write("<P><FONT FACE=\"Courier 10 Pitch\">"
						+ Language.getExecutionTime() + "</FONT></P>");
				output.write("</TH>");
				output.write("<TH WIDTH=231>");
				output.write("<P><FONT FACE=\"Courier 10 Pitch\">"
						+ Language.getBasePriority() + "</FONT></P>");
				output.write("</TH>");
				output.write("</TR>");
				// insert data about processes
				for (int i = 0; i < processes.size(); i++) {
					SimulatedProcess p = processes.get(i);
					output.write("<TR VALIGN=TOP>");
					output.write("<TD WIDTH=231 ALIGN=RIGHT>");
					output.write(p.getName());
					output.write("</TD>");
					output.write("<TD WIDTH=232 ALIGN=RIGHT>");
					output.write((new Integer(p.getActivationTime()))
							.toString());
					output.write("</TD>");
					output.write("<TD WIDTH=232 ALIGN=RIGHT>");
					output.write((new Integer(p.getExecutionTime())).toString());
					output.write("</TD>");
					output.write("<TD WIDTH=231 ALIGN=RIGHT>");
					output.write((new Integer(p.getInitialPriority()))
							.toString());
					output.write("</TD>");
					output.write("</TR>");
				}
				output.write("</TABLE>");
				output.write("</CENTER>");
				output.write("<H3><BR><BR>");
				output.write("</H3>");
				output.write("<H3><FONT FACE=\"Courier 10 Pitch\"><I><U>"
						+ Language.getResourceParameters() + "</U></I>");
				output.write("</FONT>");
				output.write("</H3>");
				output.write("<CENTER>");
				output.write("<TABLE WIDTH=959 BORDER=1 BORDERCOLOR=\"#666666\" CELLPADDING=4 CELLSPACING=0>");
				output.write("<COL WIDTH=231>");
				output.write("<COL WIDTH=232>");
				output.write("<COL WIDTH=232>");
				output.write("<COL WIDTH=231>");
				output.write("<TR VALIGN=TOP>");
				output.write("<TH WIDTH=231>");
				output.write("<P><FONT FACE=\"Courier 10 Pitch\">"
						+ Language.getName() + "</FONT></P>");
				output.write("</TH>");
				output.write("<TH WIDTH=232>");
				output.write("<P><FONT FACE=\"Courier 10 Pitch\">"
						+ Language.getPreemptive() + "</FONT></P>");
				output.write("</TH>");
				output.write("<TH WIDTH=232>");
				output.write("<P><FONT FACE=\"Courier 10 Pitch\">"
						+ Language.getMultiplicity() + "</FONT></P>");
				output.write("</TH>");
				output.write("<TH WIDTH=231>");
				output.write("<P><FONT FACE=\"Courier 10 Pitch\">"
						+ Language.getCeilingPriority() + "</FONT></P>");
				output.write("</TH>");
				output.write("</TR>");
				// insert data about resources
				for (int i = 0; i < resources.size(); i++) {
					Resource r = resources.get(i);
					boolean b = (r instanceof PreemptiveResource);
					output.write("<TR VALIGN=TOP>");
					output.write("<TD WIDTH=231 ALIGN=RIGHT>");
					output.write(r.getName());
					output.write("</TD>");
					output.write("<TD WIDTH=232 ALIGN=RIGHT>");
					output.write((new Boolean(b)).toString());
					output.write("</TD>");
					output.write("<TD WIDTH=232 ALIGN=RIGHT>");
					output.write((new Integer(r.getMultiplicity())).toString());
					output.write("</TD>");
					output.write("<TD WIDTH=231 ALIGN=RIGHT>");
					if (b) {
						output.write("--");
					} else {
						NoPreemptiveResource npr = (NoPreemptiveResource) r;
						output.write((new Integer(npr.getCeilingPriority()))
								.toString());
					}
					output.write("</TD>");
					output.write("</TR>");
				}
				output.write("</TABLE>");
				output.write("</CENTER>");
				output.write("<H3><BR><BR>");
				output.write("</H3>");
				output.write("<H3><FONT FACE=\"Courier 10 Pitch\"><I><U>"
						+ Language.getAccessParameters() + "</U></I>");
				output.write("</FONT>");
				output.write("</H3>");
				output.write("<CENTER>");
				output.write("<TABLE WIDTH=959 BORDER=1 BORDERCOLOR=\"#666666\" CELLPADDING=4 CELLSPACING=0>");
				output.write("<COL WIDTH=231>");
				output.write("<COL WIDTH=232>");
				output.write("<COL WIDTH=232>");
				output.write("<COL WIDTH=231>");
				output.write("<TR VALIGN=TOP>");
				output.write("<TH WIDTH=231>");
				output.write("<P><FONT FACE=\"Courier 10 Pitch\">"
						+ Language.getProcess() + "</FONT></P>");
				output.write("</TH>");
				output.write("<TH WIDTH=232>");
				output.write("<P><FONT FACE=\"Courier 10 Pitch\">"
						+ Language.getResource() + "</FONT></P>");
				output.write("</TH>");
				output.write("<TH WIDTH=232>");
				output.write("<P><FONT FACE=\"Courier 10 Pitch\">"
						+ Language.getRequestTime() + "</FONT></P>");
				output.write("</TH>");
				output.write("<TH WIDTH=231>");
				output.write("<P><FONT FACE=\"Courier 10 Pitch\">"
						+ Language.getRequestDuration() + "</FONT></P>");
				output.write("</TH>");
				output.write("</TR>");
				// insert data about accesses
				ArrayList<Access> accessList = null;
				Access access = null;
				for (int i = 0; i < processes.size(); i++) {
					SimulatedProcess p = processes.get(i);
					accessList = p.getAccessesList();
					if (accessList != null) {
						for (int j = 0; j < accessList.size(); j++) {
							access = accessList.get(j);
							output.write("<TR VALIGN=TOP>");
							output.write("<TD WIDTH=231 ALIGN=RIGHT>");
							output.write(p.getName());
							;
							output.write("</TD>");
							output.write("<TD WIDTH=232 ALIGN=RIGHT>");
							output.write(access.getResource().getName());
							output.write("</TD>");
							output.write("<TD WIDTH=232 ALIGN=RIGHT>");
							output.write((new Integer(access.getRequestTime()))
									.toString());
							output.write("</TD>");
							output.write("<TD WIDTH=231 ALIGN=RIGHT>");
							output.write((new Integer(access.getDuration()))
									.toString());
							output.write("</TD>");
							output.write("</TR>");
						}
					}
				}
				output.write("</TABLE>");
				output.write("</CENTER>");
				output.write("<BR><BR>");

				output.write("</BODY>");
				output.write("</HTML>");
				output.close();
				gui.setStatusMessage("Configuration file exported!");
				return true;
			} catch (IOException e) {
				new org.rainbow.gui.Error("Configuration file not exported!",
						frame);
				gui.setStatusMessage("Configuration file not exported!");
				System.out.println("[NOTICE ER001] [DATE:"
						+ Calendar.getInstance().getTime().toString()
						+ "] [MESSAGE: " + "Impossible to save" + "]");
			}
		}
		return false;
	}

	/**
	 * It allows to open a configuration saved in the file system.
	 * (RETRO-COMPATIBILITY)
	 *
	 * @param frame
	 *            The window parent.
	 * @return The configuration to open.
	 */
	public Configuration importFCS(JFrame frame) {
		ObjectInputStream in = null;
		try {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogType(JFileChooser.OPEN_DIALOG);
			chooser.addChoosableFileFilter(new FileFilter() {
				public boolean accept(File f) {
					if (f.isDirectory()) {
						return true;
					}

					String extension = getExtension(f);
					if (extension != null && extension.equals("fcs"))
						return true;
					return false;
				}

				// The description of this filter
				public String getDescription() {
					return new String(Language.getConfigurationFile());

				}
			});

			int returnVal = chooser.showOpenDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				in = new ObjectInputStream(
						new FileInputStream(chooser.getSelectedFile()));
				Configuration cf = (Configuration) in.readObject();
				gui.setStatusMessage("Configuration file opened!");
				return cf;
			}
		} catch (Exception e) {
			new org.rainbow.gui.Error("Configuration file corrupted!", frame);
			gui.setStatusMessage("Configuration file corrupted!");
			System.out.println("[NOTICE ER002] [DATE:"
					+ Calendar.getInstance().getTime().toString()
					+ "] [MESSAGE: " + "The selected file is corrupted" + "]");
			return null;
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

}
