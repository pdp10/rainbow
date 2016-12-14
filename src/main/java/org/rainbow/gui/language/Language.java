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
 * File: Language.java
 * Package: gui
 * Author: Piero Dalle Pezze
 * Date: 29/01/2007
 * Version: 1.2
 *
 * Modifies:
 * v1.2 (24/11/2014): Simplified the language management. Only this file needs to be modified if a new language is added to Rainbow.
 * v1.1 (30/01/2007): Class codify.
 * v1.0 (29/01/2007): Class documentation.
 */
package org.rainbow.gui.language;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import org.rainbow.gui.input.RainbowConfig;
import org.xml.sax.SAXException;

/**
 * This class is used for the internationalization of the application Rainbow.
 *
 *
 * Translations:
 * <ul>
 * <li>English: Piero Dalle Pezze</li>
 * <li>Italian: Piero Dalle Pezze</li>
 * <li>German: https://translate.google.com/</li>
 * <li>French: https://translate.google.com/</li>
 * <li>Spanish: https://translate.google.com/</li>
 * <li>Portuguese: http://www.tranexp.com:2000</li>
 * <li>Dutch: Staas de Jong</li>
 * <li>Norwegian: http://ets.freetranslation.com</li>
 * <li>Swedish: http://internettolken.se</li>
 * <li>Finnish: http://www.tranexp.com:2000</li>
 * <li>Danish: http://babelfish.altavista.com</li>
 * <li>Russian: Oleksandr Evel</li>
 * <li>Ukrainian: Oleksandr Evel</li>
 * <li>Polish: http://www.poltran.com</li>
 * <li>Greek: http://babelfish.altavista.com</li>
 * <li>Chinese_Simp: http://babelfish.altavista.com</li>
 * <li>Chinese_Trad: http://babelfish.altavista.com</li>
 * <li>Japanese: http://babelfish.altavista.com</li>
 * <li>Korean: http://babelfish.altavista.com</li>
 * <li>Arabic: http://www.appliedlanguage.com</li>
 * <li>Indonesia: http://www.inbahasa.com</li>
 * <li>Hungarian: http://www.webforditas.hu</li>
 * <li>Persian: http://www.parstranslator.net</li>
 * <li>Esperanto: https://translate.google.com/</li>
 * </ul>
 *
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public final class Language {

	/**
	 * Name of element in the view
	 */
	private static String process, graphOfRunningProcesses,
			graphOfResourcesAssignment, resource, resources, readyQueue,
			terminated, processes, assignment, blockedProcesses, statistics,
			showStatistics, hideStatistics, statisticsOfTheSimulation,
			throughput, averageWaitingTime, averageResponseTime,
			averageTurnAround, statisticsOfProcesses, waitingTime,
			responseTime, cpuUsage, cpuUsagePercentage, delay, turnAround,
			simulationCompleted, deadlock, deadlockSimulationTerminated,
			simulationAtInitialState, configurationSummary, state,
			toActivateState, executingState, readyState, blockedState,
			terminatedState, noQueue, configurationFile, exportHTML, exportXML;

	/**
	 * Name of element in the menu.
	 */
	private static String file, newConfiguration, openConfiguration, save,
			modifyConfiguration, exit, defaultLayout, simulation, views, help,
			about, maximumLayout, themes, lookAndFeelTT;

	/* Names of tooltips */
	/**
	 * Open the configuration panel to start a new simulation
	 */
	private static String openConfigurationTT;

	/**
	 * Open the configuration panel to modify the current configuration
	 */
	private static String modifyConfigurationTT;

	/**
	 * Load a saved configuration
	 */
	private static String loadSavedConfigurationTT;

	/**
	 * Save the configuration
	 */
	private static String saveConfigurationTT;

	/**
	 * Play the automatic advancement
	 */
	private static String playAutomaticAdvancementTT;

	/**
	 * Stop the automatic advancement
	 */
	private static String stopAutomaticAdvancementTT;

	/**
	 * Move to the initial state
	 */
	private static String moveToInitialStateTT;

	/**
	 * Move backward of a step
	 */
	private static String moveBackwardStepTT;

	/**
	 * Move forward of a step
	 */
	private static String moveForwardStepTT;

	/**
	 * Move to the final state
	 */
	private static String moveToFinalStateTT;

	/**
	 * Set the delay for the automatic advancement of the simulation
	 */
	private static String setDelaySimulationTT;

	/**
	 * Open the help window
	 */
	private static String openHelpWindowTT;

	/* Messages and errors */
	/**
	 * Possible answers
	 */
	private static String yes, no, abort, submit;

	/**
	 * Do you want to save the configuration?
	 */
	private static String saveQ;

	/**
	 * Data configuration
	 */
	private static String schedulingPolicy, timeSlice, processName, name,
			activationTime, executionTime, basePriority, multiplicity,
			preemption, requestTime, assignmentPolicy,
			chooseTheSchedulingPolicy, ceilingPriority;

	/**
	 * Errors messages
	 */
	private static String ER000, ER001, ER002, ER003, ER004, ER005, ER006,
			ER007, ER008, ER009, ER010, error;

	/**
	 * DataInput
	 */
	private static String dataInput, accessesToResources, policies,
			defaultName, add, modify, remove, accept, processParameters,
			resourceParameters, preemptive, ICPP, accessParameters, quantum,
			requestDuration;

	/**
	 * Data input tool-tips
	 */
	private static String addNewRowTT, removeSelectedRowTT,
			modifySelectedRowTT, ICPPTT, onlyForTimeSharingPoliciesTT;

	/**
	 * Languages supported.
	 */
	private static String language, english = "English", italian = "Italian",
			german = "German", french = "French", spanish = "Spanish",
			portuguese = "Portuguese", dutch = "Dutch",
			norwegian = "Norwegian", swedish = "Swedish", finnish = "Finnish",
			danish = "Danish", russian = "Russian", ukrainian = "Ukrainian",
			polish = "Polish", greek = "Greek", chineseSimp = "Chinese (simp)",
			chineseTrad = "Chinese (trad)", japanese = "Japanese",
			korean = "Korean", arabic = "Arabic", hungarian = "Hungarian",
			indonesia = "Indonesia", persian = "Persian",
			esperanto = "Esperanto";

	/** A class representing a language tuple */
	public static final class LanguageItem {
		/** The language name */
		private String name = "English";
		/** The language id used for selecting the corresponding icon image */
		private String iconID = "gb";

		/**
		 * Private Constructor. Only the class Language can create these
		 * instances.
		 */
		private LanguageItem() {
		}

		/** Private Constructor */
		private LanguageItem(String name, String iconID) {
			this.name = name;
			this.iconID = iconID;
		}

		/** It returns the language name */
		public String getName() {
			return name;
		}

		/** It returns the language icon id */
		public String getIconID() {
			return iconID;
		}
	}

	private static final LanguageItem[] languageContainer = {
			new LanguageItem(english, "gb"), new LanguageItem(italian, "it"),
			new LanguageItem(german, "de"), new LanguageItem(french, "fr"),
			new LanguageItem(spanish, "es"),
			new LanguageItem(portuguese, "pt"), new LanguageItem(dutch, "nl"),
			new LanguageItem(norwegian, "no"), new LanguageItem(swedish, "se"),
			new LanguageItem(finnish, "fi"), new LanguageItem(danish, "dk"),
			new LanguageItem(russian, "ru"), new LanguageItem(ukrainian, "ua"),
			new LanguageItem(polish, "pl"), new LanguageItem(greek, "gr"),
			new LanguageItem(japanese, "jp"), new LanguageItem(korean, "kr"),
			new LanguageItem(chineseSimp, "cn"),
			new LanguageItem(chineseTrad, "cn"),
			new LanguageItem(arabic, "sa"), new LanguageItem(hungarian, "hu"),
			new LanguageItem(indonesia, "mc"), new LanguageItem(persian, "ir"),
			new LanguageItem(esperanto, "esp") };

	public static final LanguageItem[] getLanguages() {
		return languageContainer;
	}

	/**
	 * It initializes the chosen language. </br> Available languages are:
	 * <ul>
	 * <li>English</li>
	 * <li>Italian</li>
	 * <li>German</li>
	 * <li>French</li>
	 * <li>Spanish</li>
	 * <li>Portuguese</li>
	 * <li>Dutch</li>
	 * <li>Norwegian</li>
	 * <li>Swedish</li>
	 * <li>Finnish</li>
	 * <li>Danish</li>
	 * <li>Russian</li>
	 * <li>Ukrainian</li>
	 * <li>Polish</li>
	 * <li>Greek</li>
	 * <li>Simplified Chinese</li>
	 * <li>Traditional Chinese</li>
	 * <li>Japanese</li>
	 * <li>Korean</li>
	 * <li>Arabic</li>
	 * <li>Hungarian</li>
	 * <li>Indonesia</li>
	 * <li>Persian</li>
	 * <li>Esperanto</li> </ol>
	 *
	 *
	 * @param newLanguage
	 *            The String corrisponding to the chosen language.
	 */
	public static boolean setLanguage(String newLanguage) {

		String path = "language/", xmlFile = path
				+ languageContainer[0].getName().toLowerCase() + ".xml", xmlLanguageFile = path
				+ "languageNames.xml";
		boolean defaultLanguage = false;

		/* selection of the language (default = english). */
		boolean found = false;
		for (int i = 0; i < languageContainer.length && !found; i++) {
			LanguageItem lang = languageContainer[i];
			if (newLanguage.equals(lang.getName())) {
				xmlFile = path + lang.getName().toLowerCase() + ".xml";
				found = true;
			}
		}
		if (!found) {
			defaultLanguage = true;
			System.out.println(xmlFile + " doesn't exist");
			setEnglishLanguage();
		}

		/* Set the default language */
		if (!defaultLanguage) {
			DocumentBuilderFactory factory = null;
			DocumentBuilder builder = null;

			try {
				factory = DocumentBuilderFactory.newInstance();
				builder = factory.newDocumentBuilder();

				Document doc = builder.parse(xmlFile);

				/* names in the several views */
				assignment = doc.getElementsByTagName("word").item(0)
						.getAttributes().getNamedItem("assignment")
						.getNodeValue();
				averageResponseTime = doc.getElementsByTagName("word").item(1)
						.getAttributes().getNamedItem("averageResponseTime")
						.getNodeValue();
				averageTurnAround = doc.getElementsByTagName("word").item(2)
						.getAttributes().getNamedItem("averageTurnAround")
						.getNodeValue();
				averageWaitingTime = doc.getElementsByTagName("word").item(3)
						.getAttributes().getNamedItem("averageWaitingTime")
						.getNodeValue();
				blockedProcesses = doc.getElementsByTagName("word").item(4)
						.getAttributes().getNamedItem("blockedProcesses")
						.getNodeValue();
				configurationFile = doc.getElementsByTagName("word").item(5)
						.getAttributes().getNamedItem("configurationFile")
						.getNodeValue();
				cpuUsage = doc.getElementsByTagName("word").item(6)
						.getAttributes().getNamedItem("cpuUsage")
						.getNodeValue();
				cpuUsagePercentage = doc.getElementsByTagName("word").item(7)
						.getAttributes().getNamedItem("cpuUsagePercentage")
						.getNodeValue();
				deadlock = doc.getElementsByTagName("word").item(8)
						.getAttributes().getNamedItem("deadlock")
						.getNodeValue();
				deadlockSimulationTerminated = doc.getElementsByTagName("word")
						.item(9).getAttributes()
						.getNamedItem("deadlockSimulationTerminated")
						.getNodeValue();
				delay = doc.getElementsByTagName("word").item(10)
						.getAttributes().getNamedItem("delay").getNodeValue();
				graphOfResourcesAssignment = doc.getElementsByTagName("word")
						.item(11).getAttributes()
						.getNamedItem("graphOfResourcesAssignment")
						.getNodeValue();
				graphOfRunningProcesses = doc.getElementsByTagName("word")
						.item(12).getAttributes()
						.getNamedItem("graphOfRunningProcesses").getNodeValue();
				hideStatistics = doc.getElementsByTagName("word").item(13)
						.getAttributes().getNamedItem("hideStatistics")
						.getNodeValue();
				noQueue = doc.getElementsByTagName("word").item(14)
						.getAttributes().getNamedItem("noQueue").getNodeValue();
				process = doc.getElementsByTagName("word").item(15)
						.getAttributes().getNamedItem("process").getNodeValue();
				processes = doc.getElementsByTagName("word").item(16)
						.getAttributes().getNamedItem("processes")
						.getNodeValue();
				readyQueue = doc.getElementsByTagName("word").item(17)
						.getAttributes().getNamedItem("readyQueue")
						.getNodeValue();
				resource = doc.getElementsByTagName("word").item(18)
						.getAttributes().getNamedItem("resource")
						.getNodeValue();
				resources = doc.getElementsByTagName("word").item(19)
						.getAttributes().getNamedItem("resources")
						.getNodeValue();
				responseTime = doc.getElementsByTagName("word").item(20)
						.getAttributes().getNamedItem("responseTime")
						.getNodeValue();
				showStatistics = doc.getElementsByTagName("word").item(21)
						.getAttributes().getNamedItem("showStatistics")
						.getNodeValue();
				simulationAtInitialState = doc.getElementsByTagName("word")
						.item(22).getAttributes()
						.getNamedItem("simulationAtInitialState")
						.getNodeValue();
				simulationCompleted = doc.getElementsByTagName("word").item(23)
						.getAttributes().getNamedItem("simulationCompleted")
						.getNodeValue();
				state = doc.getElementsByTagName("word").item(24)
						.getAttributes().getNamedItem("state").getNodeValue();
				statistics = doc.getElementsByTagName("word").item(25)
						.getAttributes().getNamedItem("statistics")
						.getNodeValue();
				statisticsOfProcesses = doc.getElementsByTagName("word")
						.item(26).getAttributes()
						.getNamedItem("statisticsOfProcesses").getNodeValue();
				statisticsOfTheSimulation = doc.getElementsByTagName("word")
						.item(27).getAttributes()
						.getNamedItem("statisticsOfTheSimulation")
						.getNodeValue();
				terminated = doc.getElementsByTagName("word").item(28)
						.getAttributes().getNamedItem("terminated")
						.getNodeValue();
				throughput = doc.getElementsByTagName("word").item(29)
						.getAttributes().getNamedItem("throughput")
						.getNodeValue();
				turnAround = doc.getElementsByTagName("word").item(30)
						.getAttributes().getNamedItem("turnAround")
						.getNodeValue();
				waitingTime = doc.getElementsByTagName("word").item(31)
						.getAttributes().getNamedItem("waitingTime")
						.getNodeValue();
				/* states of a process */
				blockedState = doc.getElementsByTagName("word").item(32)
						.getAttributes().getNamedItem("blockedState")
						.getNodeValue();
				executingState = doc.getElementsByTagName("word").item(33)
						.getAttributes().getNamedItem("executingState")
						.getNodeValue();
				readyState = doc.getElementsByTagName("word").item(34)
						.getAttributes().getNamedItem("readyState")
						.getNodeValue();
				terminatedState = doc.getElementsByTagName("word").item(35)
						.getAttributes().getNamedItem("terminatedState")
						.getNodeValue();
				toActivateState = doc.getElementsByTagName("word").item(36)
						.getAttributes().getNamedItem("toActivateState")
						.getNodeValue();
				/* names in the menu */
				about = doc.getElementsByTagName("word").item(37)
						.getAttributes().getNamedItem("about").getNodeValue()
						+ RainbowConfig.getApplicationName();
				configurationSummary = doc.getElementsByTagName("word")
						.item(38).getAttributes()
						.getNamedItem("configurationSummary").getNodeValue();
				defaultLayout = doc.getElementsByTagName("word").item(39)
						.getAttributes().getNamedItem("defaultLayout")
						.getNodeValue();
				exit = doc.getElementsByTagName("word").item(40)
						.getAttributes().getNamedItem("exit").getNodeValue();
				file = doc.getElementsByTagName("word").item(41)
						.getAttributes().getNamedItem("file").getNodeValue();
				help = doc.getElementsByTagName("word").item(42)
						.getAttributes().getNamedItem("help").getNodeValue();
				modifyConfiguration = doc.getElementsByTagName("word").item(43)
						.getAttributes().getNamedItem("modifyConfiguration")
						.getNodeValue();
				newConfiguration = doc.getElementsByTagName("word").item(44)
						.getAttributes().getNamedItem("newConfiguration")
						.getNodeValue();
				openConfiguration = doc.getElementsByTagName("word").item(45)
						.getAttributes().getNamedItem("openConfiguration")
						.getNodeValue();
				save = doc.getElementsByTagName("word").item(46)
						.getAttributes().getNamedItem("save").getNodeValue();
				simulation = doc.getElementsByTagName("word").item(47)
						.getAttributes().getNamedItem("simulation")
						.getNodeValue();
				views = doc.getElementsByTagName("word").item(48)
						.getAttributes().getNamedItem("views").getNodeValue();
				/* names of tooltips */
				loadSavedConfigurationTT = doc.getElementsByTagName("word")
						.item(49).getAttributes()
						.getNamedItem("loadSavedConfigurationTT")
						.getNodeValue();
				modifyConfigurationTT = doc.getElementsByTagName("word")
						.item(50).getAttributes()
						.getNamedItem("modifyConfigurationTT").getNodeValue();
				moveBackwardStepTT = doc.getElementsByTagName("word").item(51)
						.getAttributes().getNamedItem("moveBackwardStepTT")
						.getNodeValue();
				moveForwardStepTT = doc.getElementsByTagName("word").item(52)
						.getAttributes().getNamedItem("moveForwardStepTT")
						.getNodeValue();
				moveToFinalStateTT = doc.getElementsByTagName("word").item(53)
						.getAttributes().getNamedItem("moveToFinalStateTT")
						.getNodeValue();
				moveToInitialStateTT = doc.getElementsByTagName("word")
						.item(54).getAttributes()
						.getNamedItem("moveToInitialStateTT").getNodeValue();
				openConfigurationTT = doc.getElementsByTagName("word").item(55)
						.getAttributes().getNamedItem("openConfigurationTT")
						.getNodeValue();
				openHelpWindowTT = doc.getElementsByTagName("word").item(56)
						.getAttributes().getNamedItem("openHelpWindowTT")
						.getNodeValue();
				playAutomaticAdvancementTT = doc.getElementsByTagName("word")
						.item(57).getAttributes()
						.getNamedItem("playAutomaticAdvancementTT")
						.getNodeValue();
				saveConfigurationTT = doc.getElementsByTagName("word").item(58)
						.getAttributes().getNamedItem("saveConfigurationTT")
						.getNodeValue();
				setDelaySimulationTT = doc.getElementsByTagName("word")
						.item(59).getAttributes()
						.getNamedItem("setDelaySimulationTT").getNodeValue();
				stopAutomaticAdvancementTT = doc.getElementsByTagName("word")
						.item(60).getAttributes()
						.getNamedItem("stopAutomaticAdvancementTT")
						.getNodeValue();
				/* Messages and errors */
				abort = doc.getElementsByTagName("word").item(61)
						.getAttributes().getNamedItem("abort").getNodeValue();
				no = doc.getElementsByTagName("word").item(62).getAttributes()
						.getNamedItem("no").getNodeValue();
				saveQ = doc.getElementsByTagName("word").item(63)
						.getAttributes().getNamedItem("saveQ").getNodeValue();
				submit = doc.getElementsByTagName("word").item(64)
						.getAttributes().getNamedItem("submit").getNodeValue();
				yes = doc.getElementsByTagName("word").item(65).getAttributes()
						.getNamedItem("yes").getNodeValue();
				/* Data input */
				activationTime = doc.getElementsByTagName("word").item(66)
						.getAttributes().getNamedItem("activationTime")
						.getNodeValue();
				assignmentPolicy = doc.getElementsByTagName("word").item(67)
						.getAttributes().getNamedItem("assignmentPolicy")
						.getNodeValue();
				basePriority = doc.getElementsByTagName("word").item(68)
						.getAttributes().getNamedItem("basePriority")
						.getNodeValue();
				ceilingPriority = doc.getElementsByTagName("word").item(69)
						.getAttributes().getNamedItem("ceilingPriority")
						.getNodeValue();
				chooseTheSchedulingPolicy = doc.getElementsByTagName("word")
						.item(70).getAttributes()
						.getNamedItem("chooseTheSchedulingPolicy")
						.getNodeValue();
				executionTime = doc.getElementsByTagName("word").item(71)
						.getAttributes().getNamedItem("executionTime")
						.getNodeValue();
				multiplicity = doc.getElementsByTagName("word").item(72)
						.getAttributes().getNamedItem("multiplicity")
						.getNodeValue();
				name = doc.getElementsByTagName("word").item(73)
						.getAttributes().getNamedItem("name").getNodeValue();
				processName = doc.getElementsByTagName("word").item(74)
						.getAttributes().getNamedItem("processName")
						.getNodeValue();
				preemption = doc.getElementsByTagName("word").item(75)
						.getAttributes().getNamedItem("preemption")
						.getNodeValue();
				requestTime = doc.getElementsByTagName("word").item(76)
						.getAttributes().getNamedItem("requestTime")
						.getNodeValue();
				schedulingPolicy = doc.getElementsByTagName("word").item(77)
						.getAttributes().getNamedItem("schedulingPolicy")
						.getNodeValue();
				timeSlice = doc.getElementsByTagName("word").item(78)
						.getAttributes().getNamedItem("timeSlice")
						.getNodeValue();
				dataInput = doc.getElementsByTagName("word").item(79)
						.getAttributes().getNamedItem("dataInput")
						.getNodeValue();
				accessesToResources = doc.getElementsByTagName("word").item(80)
						.getAttributes().getNamedItem("accessesToResources")
						.getNodeValue();
				policies = doc.getElementsByTagName("word").item(81)
						.getAttributes().getNamedItem("policies")
						.getNodeValue();
				defaultName = doc.getElementsByTagName("word").item(82)
						.getAttributes().getNamedItem("defaultName")
						.getNodeValue();
				add = doc.getElementsByTagName("word").item(83).getAttributes()
						.getNamedItem("add").getNodeValue();
				modify = doc.getElementsByTagName("word").item(84)
						.getAttributes().getNamedItem("modify").getNodeValue();
				remove = doc.getElementsByTagName("word").item(85)
						.getAttributes().getNamedItem("remove").getNodeValue();
				accept = doc.getElementsByTagName("word").item(86)
						.getAttributes().getNamedItem("accept").getNodeValue();
				processParameters = doc.getElementsByTagName("word").item(87)
						.getAttributes().getNamedItem("processParameters")
						.getNodeValue();
				resourceParameters = doc.getElementsByTagName("word").item(88)
						.getAttributes().getNamedItem("resourceParameters")
						.getNodeValue();
				preemptive = doc.getElementsByTagName("word").item(89)
						.getAttributes().getNamedItem("preemptive")
						.getNodeValue();
				ICPP = doc.getElementsByTagName("word").item(90)
						.getAttributes().getNamedItem("ICPP").getNodeValue();
				accessParameters = doc.getElementsByTagName("word").item(91)
						.getAttributes().getNamedItem("accessParameters")
						.getNodeValue();
				quantum = doc.getElementsByTagName("word").item(92)
						.getAttributes().getNamedItem("quantum").getNodeValue();
				requestDuration = doc.getElementsByTagName("word").item(93)
						.getAttributes().getNamedItem("requestDuration")
						.getNodeValue();
				/* Data input tool-tips */
				addNewRowTT = doc.getElementsByTagName("word").item(94)
						.getAttributes().getNamedItem("addNewRowTT")
						.getNodeValue();
				removeSelectedRowTT = doc.getElementsByTagName("word").item(95)
						.getAttributes().getNamedItem("removeSelectedRowTT")
						.getNodeValue();
				modifySelectedRowTT = doc.getElementsByTagName("word").item(96)
						.getAttributes().getNamedItem("modifySelectedRowTT")
						.getNodeValue();
				ICPPTT = doc.getElementsByTagName("word").item(97)
						.getAttributes().getNamedItem("ICPPTT").getNodeValue();
				onlyForTimeSharingPoliciesTT = doc.getElementsByTagName("word")
						.item(98).getAttributes()
						.getNamedItem("onlyForTimeSharingPoliciesTT")
						.getNodeValue();
				ER000 = doc.getElementsByTagName("word").item(99)
						.getAttributes().getNamedItem("ER000").getNodeValue();
				ER001 = doc.getElementsByTagName("word").item(100)
						.getAttributes().getNamedItem("ER001").getNodeValue();
				ER002 = doc.getElementsByTagName("word").item(101)
						.getAttributes().getNamedItem("ER002").getNodeValue();
				ER003 = doc.getElementsByTagName("word").item(102)
						.getAttributes().getNamedItem("ER003").getNodeValue();
				ER004 = doc.getElementsByTagName("word").item(103)
						.getAttributes().getNamedItem("ER004").getNodeValue();
				ER005 = doc.getElementsByTagName("word").item(104)
						.getAttributes().getNamedItem("ER005").getNodeValue();
				ER006 = doc.getElementsByTagName("word").item(105)
						.getAttributes().getNamedItem("ER006").getNodeValue();
				ER007 = doc.getElementsByTagName("word").item(106)
						.getAttributes().getNamedItem("ER007").getNodeValue();
				ER008 = doc.getElementsByTagName("word").item(107)
						.getAttributes().getNamedItem("ER008").getNodeValue();
				ER009 = doc.getElementsByTagName("word").item(108)
						.getAttributes().getNamedItem("ER009").getNodeValue();
				language = doc.getElementsByTagName("word").item(109)
						.getAttributes().getNamedItem("language")
						.getNodeValue();
				maximumLayout = doc.getElementsByTagName("word").item(110)
						.getAttributes().getNamedItem("maximumLayout")
						.getNodeValue();
				ER010 = doc.getElementsByTagName("word").item(111)
						.getAttributes().getNamedItem("ER010").getNodeValue();
				error = doc.getElementsByTagName("word").item(112)
						.getAttributes().getNamedItem("error").getNodeValue();
				exportHTML = doc.getElementsByTagName("word").item(113)
						.getAttributes().getNamedItem("exportHTML")
						.getNodeValue();
				exportXML = doc.getElementsByTagName("word").item(114)
						.getAttributes().getNamedItem("exportXML")
						.getNodeValue();
				themes = doc.getElementsByTagName("word").item(115)
						.getAttributes().getNamedItem("themes").getNodeValue();
				lookAndFeelTT = doc.getElementsByTagName("word").item(116)
						.getAttributes().getNamedItem("lafTT").getNodeValue();

				// NAMES OF languages
				factory = DocumentBuilderFactory.newInstance();
				builder = factory.newDocumentBuilder();
				doc = builder.parse(xmlLanguageFile);

				english = doc.getElementsByTagName("language").item(0)
						.getAttributes().getNamedItem("english").getNodeValue();
				italian = doc.getElementsByTagName("language").item(1)
						.getAttributes().getNamedItem("italian").getNodeValue();
				german = doc.getElementsByTagName("language").item(2)
						.getAttributes().getNamedItem("german").getNodeValue();
				french = doc.getElementsByTagName("language").item(3)
						.getAttributes().getNamedItem("french").getNodeValue();
				spanish = doc.getElementsByTagName("language").item(4)
						.getAttributes().getNamedItem("spanish").getNodeValue();
				portuguese = doc.getElementsByTagName("language").item(5)
						.getAttributes().getNamedItem("portuguese")
						.getNodeValue();
				dutch = doc.getElementsByTagName("language").item(6)
						.getAttributes().getNamedItem("dutch").getNodeValue();
				norwegian = doc.getElementsByTagName("language").item(7)
						.getAttributes().getNamedItem("norwegian")
						.getNodeValue();
				swedish = doc.getElementsByTagName("language").item(8)
						.getAttributes().getNamedItem("swedish").getNodeValue();
				finnish = doc.getElementsByTagName("language").item(9)
						.getAttributes().getNamedItem("finnish").getNodeValue();
				danish = doc.getElementsByTagName("language").item(10)
						.getAttributes().getNamedItem("danish").getNodeValue();
				russian = doc.getElementsByTagName("language").item(11)
						.getAttributes().getNamedItem("russian").getNodeValue();
				ukrainian = doc.getElementsByTagName("language").item(12)
						.getAttributes().getNamedItem("ukrainian")
						.getNodeValue();
				polish = doc.getElementsByTagName("language").item(13)
						.getAttributes().getNamedItem("polish").getNodeValue();
				greek = doc.getElementsByTagName("language").item(14)
						.getAttributes().getNamedItem("greek").getNodeValue();
				japanese = doc.getElementsByTagName("language").item(15)
						.getAttributes().getNamedItem("japanese")
						.getNodeValue();
				korean = doc.getElementsByTagName("language").item(16)
						.getAttributes().getNamedItem("korean").getNodeValue();
				chineseSimp = doc.getElementsByTagName("language").item(17)
						.getAttributes().getNamedItem("chinese_simp")
						.getNodeValue();
				chineseTrad = doc.getElementsByTagName("language").item(18)
						.getAttributes().getNamedItem("chinese_trad")
						.getNodeValue();
				arabic = doc.getElementsByTagName("language").item(19)
						.getAttributes().getNamedItem("arabic").getNodeValue();
				hungarian = doc.getElementsByTagName("language").item(20)
						.getAttributes().getNamedItem("hungarian")
						.getNodeValue();
				indonesia = doc.getElementsByTagName("language").item(21)
						.getAttributes().getNamedItem("indonesia")
						.getNodeValue();
				persian = doc.getElementsByTagName("language").item(22)
						.getAttributes().getNamedItem("persian").getNodeValue();
				esperanto = doc.getElementsByTagName("language").item(23)
						.getAttributes().getNamedItem("esperanto")
						.getNodeValue();

				return true;

			} catch (IOException ex) {
				System.out.println("Some I/O errors was found in the file: "
						+ xmlFile);
				setEnglishLanguage();
			} catch (SAXException ex) {
				setEnglishLanguage();
				System.out.println("Parsing error");
			} catch (FactoryConfigurationError ex) {
				System.out.println("Factory error");
				setEnglishLanguage();
			} catch (ParserConfigurationException ex) {
				System.out.println("Parsing error");
				setEnglishLanguage();
			} catch (Exception ex) {
				System.out.println("Corrupted file: " + xmlFile);
				setEnglishLanguage();
			}

		}
		return false;

	}

	/**
	 * It sets the English language.
	 */
	private static void setEnglishLanguage() {
		/* names in the several views */
		assignment = "Assignment";
		averageResponseTime = "Average response time";
		averageTurnAround = "Average turn around";
		averageWaitingTime = "Average waiting time";
		blockedProcesses = "Blocked processes";
		configurationFile = "Configuration file (*.xml)";
		cpuUsage = "CPU Usage";
		cpuUsagePercentage = "CPU Usage (%)";
		deadlock = "Deadlock";
		deadlockSimulationTerminated = "Deadlock. Simulation terminated";
		delay = "Delay (tenth of seconds)";
		graphOfResourcesAssignment = "Resource assignment graph";
		graphOfRunningProcesses = "Running processes graph";
		hideStatistics = "Hide statistics";
		noQueue = "No queue";
		process = "Process";
		processes = "Processes";
		readyQueue = "Ready queue";
		resource = "Resource";
		resources = "Resources";
		responseTime = "Response time";
		showStatistics = "Show statistics";
		simulationAtInitialState = "Simulation at the initial state";
		simulationCompleted = "Simulation completed";
		state = "State";
		statistics = "Statistics";
		statisticsOfProcesses = "Process statistics";
		statisticsOfTheSimulation = "Simulation statistics";
		terminated = "Terminated";
		throughput = "Troughput";
		turnAround = "Turn around";
		waitingTime = "Waiting time";

		/* states of a process */
		blockedState = "Blocked";
		executingState = "Running";
		readyState = "Ready";
		terminatedState = "Terminated";
		toActivateState = "About to be active";

		/* names in the menu */
		about = "About " + RainbowConfig.getApplicationName();
		configurationSummary = "Configuration summary";
		defaultLayout = "Default layout";
		maximumLayout = "Maximum layout";
		exit = "Exit";
		file = "File";
		help = "Help";
		modifyConfiguration = "Modify";
		newConfiguration = "New";
		openConfiguration = "Open";
		save = "Save";
		simulation = "Simulation";
		views = "Views";
		exportHTML = "Export (html)";
		exportXML = "Export (xml)";
		themes = "Themes";

		/* names of tooltips */
		loadSavedConfigurationTT = "Load a saved configuration";
		modifyConfigurationTT = "Open the configuration panel to modify the current configuration";
		moveBackwardStepTT = "Move a step backward";
		moveForwardStepTT = "Move a step forward";
		moveToFinalStateTT = "Move to the final state";
		moveToInitialStateTT = "Move to the initial state";
		openConfigurationTT = "Open the configuration panel to start a new simulation";
		openHelpWindowTT = "Open the help window";
		playAutomaticAdvancementTT = "Play the automatic advancement";
		saveConfigurationTT = "Save the configuration";
		setDelaySimulationTT = "Set the delay for the automatic advancement of the simulation";
		stopAutomaticAdvancementTT = "Stop the automatic advancement";
		lookAndFeelTT = "You must reboot to apply this look and feel";

		/* Messages and errors */
		abort = "Abort";
		no = "No";
		saveQ = "Do you want to save the configuration?";
		submit = "Submit";
		yes = "Yes";

		/* Data input */
		activationTime = "Activation time";
		assignmentPolicy = "Assignment policy";
		basePriority = "Base priority";
		ceilingPriority = "Ceiling priority";
		chooseTheSchedulingPolicy = "Choose the scheduling policy";
		executionTime = "Execution time";
		multiplicity = "Multiplicity";
		name = "Name";
		processName = "Process name";
		preemption = "Preemption";
		requestTime = "Request time";
		schedulingPolicy = "Scheduling policy";
		timeSlice = "Time slice";
		dataInput = "Data input";
		accessesToResources = "Resource usage";
		policies = "Policies";
		defaultName = "Default name";
		add = "Add";
		modify = "Modify";
		remove = "Remove";
		accept = "Accept";
		processParameters = "Process parameters";
		resourceParameters = "Resource parameters";
		preemptive = "Preemptive";
		ICPP = "ICPP";
		accessParameters = "Resource usage parameters";
		quantum = "Quantum";
		requestDuration = "Request duration";

		/* Data input tool-tips */
		addNewRowTT = "Add new row";
		removeSelectedRowTT = "Remove the selected row";
		modifySelectedRowTT = "Modify the selected row";
		ICPPTT = "Immediate Ceiling Priority Protocol";
		onlyForTimeSharingPoliciesTT = "Only for time sharing scheduling policies";

		/* Errors messages */
		ER000 = "Error000 - Rainbow configuration file not found or corrupted.\nInizialize default configuration";
		ER001 = "Error001 - Opened file corrupted";
		ER002 = "Error002 - File not saved";
		ER003 = "Error003 - Simulation duration greater than 15000 ticks";
		ER004 = "Error004 - Priority out of range";
		ER005 = "Error005 - Execution time not positive";
		ER006 = "Error006 - Resource request time not valid";
		ER007 = "Error007 - Molteplicity not positive";
		ER008 = "Error008 - Ceiling priority out of range";
		ER009 = "Error009 - Access duration not valid";
		ER010 = "Error010 - Incompatible access. See the table";
		error = "Error";

		/* Languages. */
		language = "Language";
		english = "English";
		italian = "Italian";
		german = "German";
		french = "French";
		spanish = "Spanish";
		portuguese = "Portuguese";
		dutch = "Dutch";
		norwegian = "Norwegian";
		swedish = "Swedish";
		finnish = "Finnish";
		danish = "Danish";
		russian = "Russian";
		ukrainian = "Ukrainian";
		polish = "Polish";
		greek = "Greek";
		chineseSimp = "Chinese (simp)";
		chineseTrad = "Chinese (trad)";
		japanese = "Japanese";
		korean = "Korean";
		arabic = "Arabic";
		hungarian = "Hungarian";
		indonesia = "Indonesia";
		persian = "Persian";
		esperanto = "Esperanto";
	}

	/**
	 * It returns the assignment name.
	 *
	 * @return the assignment name.
	 */
	public static String getAssignment() {
		return assignment;
	}

	/**
	 * It returns the averageResponseTime name.
	 *
	 * @return the averageResponseTime name.
	 */
	public static String getAverageResponseTime() {
		return averageResponseTime;
	}

	/**
	 * It returns the averageTurnAround name.
	 *
	 * @return the averageTurnAround name.
	 */
	public static String getAverageTurnAround() {
		return averageTurnAround;
	}

	/**
	 * It returns the averageWaitingTime name.
	 *
	 * @return the averageWaitingTime name.
	 */
	public static String getAverageWaitingTime() {
		return averageWaitingTime;
	}

	/**
	 * It returns the blockedProcesses name.
	 *
	 * @return the blockedProcesses name.
	 */
	public static String getBlockedProcesses() {
		return blockedProcesses;
	}

	/**
	 * It returns the configurationFile name.
	 *
	 * @return the configurationFile name.
	 */
	public static String getConfigurationFile() {
		return configurationFile;
	}

	/**
	 * It returns the cpuUsage name.
	 *
	 * @return the cpuUsage name.
	 */
	public static String getCpuUsage() {
		return cpuUsage;
	}

	/**
	 * It returns the cpuUsagePercentage name.
	 *
	 * @return the cpuUsagePercentage name.
	 */
	public static String getCpuUsagePercentage() {
		return cpuUsagePercentage;
	}

	/**
	 * It returns the deadlock name.
	 *
	 * @return the deadlock name.
	 */
	public static String getDeadlock() {
		return deadlock;
	}

	/**
	 * It returns the deadlockSimulationTerminated name.
	 *
	 * @return the deadlockSimulationTerminated name.
	 */
	public static String getDeadlockSimulationTerminated() {
		return deadlockSimulationTerminated;
	}

	/**
	 * It returns the delay name.
	 *
	 * @return the delay name.
	 */
	public static String getDelay() {
		return delay;
	}

	/**
	 * It returns the graphOfResourcesAssignment name.
	 *
	 * @return the graphOfResourcesAssignment name.
	 */
	public static String getGraphOfResourcesAssignment() {
		return graphOfResourcesAssignment;
	}

	/**
	 * It returns the graphOfRunningProcesses name.
	 *
	 * @return the graphOfRunningProcesses name.
	 */
	public static String getGraphOfRunningProcesses() {
		return graphOfRunningProcesses;
	}

	/**
	 * It returns the hideStatistics name.
	 *
	 * @return the hideStatistics name.
	 */
	public static String getHideStatistics() {
		return hideStatistics;
	}

	/**
	 * It returns the noQueue name.
	 *
	 * @return the noQueue name.
	 */
	public static String getNoQueue() {
		return noQueue;
	}

	/**
	 * It returns the process name.
	 *
	 * @return the process name.
	 */
	public static String getProcess() {
		return process;
	}

	/**
	 * It returns the processes name.
	 *
	 * @return the processes name.
	 */
	public static String getProcesses() {
		return processes;
	}

	/**
	 * It returns the readyQueue name.
	 *
	 * @return the readyQueue name.
	 */
	public static String getReadyQueue() {
		return readyQueue;
	}

	/**
	 * It returns the resource name.
	 *
	 * @return the resource name.
	 */
	public static String getResource() {
		return resource;
	}

	/**
	 * It returns the resources name.
	 *
	 * @return the resources name.
	 */
	public static String getResources() {
		return resources;
	}

	/**
	 * It returns the responseTime name.
	 *
	 * @return the responseTime name.
	 */
	public static String getResponseTime() {
		return responseTime;
	}

	/**
	 * It returns the showStatistics name.
	 *
	 * @return the showStatistics name.
	 */
	public static String getShowStatistics() {
		return showStatistics;
	}

	/**
	 * It returns the simulationAtInitialState name.
	 *
	 * @return the simulationAtInitialState name.
	 */
	public static String getSimulationAtInitialState() {
		return simulationAtInitialState;
	}

	/**
	 * It returns the simulationCompleted name.
	 *
	 * @return the simulationCompleted name.
	 */
	public static String getSimulationCompleted() {
		return simulationCompleted;
	}

	/**
	 * It returns the state name.
	 *
	 * @return the state name.
	 */
	public static String getState() {
		return state;
	}

	/**
	 * It returns the statistics name.
	 *
	 * @return the statistics name.
	 */
	public static String getStatistics() {
		return statistics;
	}

	/**
	 * It returns the statisticsOfProcesses name.
	 *
	 * @return the statisticsOfProcesses name.
	 */
	public static String getStatisticsOfProcesses() {
		return statisticsOfProcesses;
	}

	/**
	 * It returns the statisticsOfTheSimulation name.
	 *
	 * @return the statisticsOfTheSimulation name.
	 */
	public static String getStatisticsOfTheSimulation() {
		return statisticsOfTheSimulation;
	}

	/**
	 * It returns the terminated name.
	 *
	 * @return the terminated name.
	 */
	public static String getTerminated() {
		return terminated;
	}

	/**
	 * It returns the throughput name.
	 *
	 * @return the throughput name.
	 */
	public static String getThroughput() {
		return throughput;
	}

	/**
	 * It returns the turnAround name.
	 *
	 * @return the turnAround name.
	 */
	public static String getTurnAround() {
		return turnAround;
	}

	/**
	 * It returns the waitingTime name.
	 *
	 * @return the waitingTime name.
	 */
	public static String getWaitingTime() {
		return waitingTime;
	}

	/**
	 * It returns the toActivateState name.
	 *
	 * @return the toActivateState name.
	 */
	public static String getToActivateState() {
		return toActivateState;
	}

	/**
	 * It returns the blockedState name.
	 *
	 * @return the blockedState name.
	 */
	public static String getBlockedState() {
		return blockedState;
	}

	/**
	 * It returns the executingState name.
	 *
	 * @return the executingState name.
	 */
	public static String getExecutingState() {
		return executingState;
	}

	/**
	 * It returns the readyState name.
	 *
	 * @return the readyState name.
	 */
	public static String getReadyState() {
		return readyState;
	}

	/**
	 * It returns the terminatedState name.
	 *
	 * @return the terminatedState name.
	 */
	public static String getTerminatedState() {
		return terminatedState;
	}

	/**
	 * It returns the about name.
	 *
	 * @return the about name.
	 */
	public static String getAbout() {
		return about;
	}

	/**
	 * It returns the configurationSummary name.
	 *
	 * @return the configurationSummary name.
	 */
	public static String getConfigurationSummary() {
		return configurationSummary;
	}

	/**
	 * It returns the defaultLayout name.
	 *
	 * @return the defaultLayout name.
	 */
	public static String getDefaultLayout() {
		return defaultLayout;
	}

	/**
	 * It returns the maximumLayout name.
	 *
	 * @return the maximumLayout name.
	 */
	public static String getMaximumLayout() {
		return maximumLayout;
	}

	/**
	 * It returns the exit name.
	 *
	 * @return the exit name.
	 */
	public static String getExit() {
		return exit;
	}

	/**
	 * It returns the help name.
	 *
	 * @return the help name.
	 */
	public static String getHelp() {
		return help;
	}

	/**
	 * It returns the file name.
	 *
	 * @return the file name.
	 */
	public static String getFile() {
		return file;
	}

	/**
	 * It returns the export HTML name.
	 *
	 * @return the export HTML name.
	 */
	public static String getExportHTML() {
		return exportHTML;
	}

	/**
	 * It returns the export XML name.
	 *
	 * @return the export XML name.
	 */
	public static String getExportXML() {
		return exportXML;
	}

	/**
	 * It returns the modifyConfiguration name.
	 *
	 * @return the modifyConfiguration name.
	 */
	public static String getModifyConfiguration() {
		return modifyConfiguration;
	}

	/**
	 * It returns the newConfiguration name.
	 *
	 * @return the newConfiguration name.
	 */
	public static String getNewConfiguration() {
		return newConfiguration;
	}

	/**
	 * It returns the openConfiguration name.
	 *
	 * @return the openConfiguration name.
	 */
	public static String getOpenConfiguration() {
		return openConfiguration;
	}

	/**
	 * It returns the save name.
	 *
	 * @return the save name.
	 */
	public static String getSave() {
		return save;
	}

	/**
	 * It returns the simulation name.
	 *
	 * @return the simulation name.
	 */
	public static String getSimulation() {
		return simulation;
	}

	/**
	 * It returns the views name.
	 *
	 * @return the views name.
	 */
	public static String getViews() {
		return views;
	}

	/**
	 * It returns the themes name.
	 *
	 * @return the themes name.
	 */
	public static String getThemes() {
		return themes;
	}

	/**
	 * It returns the loadSavedConfigurationTT tool-tips name.
	 *
	 * @return the loadSavedConfigurationTT tool-tips name.
	 */
	public static String getLoadSavedConfigurationTT() {
		return loadSavedConfigurationTT;
	}

	/**
	 * It returns the modifyConfigurationTT tool-tips name.
	 *
	 * @return the modifyConfigurationTT tool-tips name.
	 */
	public static String getModifyConfigurationTT() {
		return modifyConfigurationTT;
	}

	/**
	 * It returns the moveBackwardStepTT tool-tips name.
	 *
	 * @return the moveBackwardStepTT tool-tips name.
	 */
	public static String getMoveBackwardStepTT() {
		return moveBackwardStepTT;
	}

	/**
	 * It returns the moveForwardStepTT tool-tips name.
	 *
	 * @return the moveForwardStepTT tool-tips name.
	 */
	public static String getMoveForwardStepTT() {
		return moveForwardStepTT;
	}

	/**
	 * It returns the moveToFinalStateTT tool-tips name.
	 *
	 * @return the moveToFinalStateTT tool-tips name.
	 */
	public static String getMoveToFinalStateTT() {
		return moveToFinalStateTT;
	}

	/**
	 * It returns the moveToInitialStateTT tool-tips name.
	 *
	 * @return the moveToInitialStateTT tool-tips name.
	 */
	public static String getMoveToInitialStateTT() {
		return moveToInitialStateTT;
	}

	/**
	 * It returns the openConfigurationTT tool-tips name.
	 *
	 * @return the openConfigurationTT tool-tips name.
	 */
	public static String getOpenConfigurationTT() {
		return openConfigurationTT;
	}

	/**
	 * It returns the openHelpWindowTT tool-tips name.
	 *
	 * @return the openHelpWindowTT tool-tips name.
	 */
	public static String getOpenHelpWindowTT() {
		return openHelpWindowTT;
	}

	/**
	 * It returns the playAutomaticAdvancementTT tool-tips name.
	 *
	 * @return the playAutomaticAdvancementTT tool-tips name.
	 */
	public static String getPlayAutomaticAdvancementTT() {
		return playAutomaticAdvancementTT;
	}

	/**
	 * It returns the saveConfigurationTT tool-tips name.
	 *
	 * @return the saveConfigurationTT tool-tips name.
	 */
	public static String getSaveConfigurationTT() {
		return saveConfigurationTT;
	}

	/**
	 * It returns the setDelaySimulationTT tool-tips name.
	 *
	 * @return the setDelaySimulationTT tool-tips name.
	 */
	public static String getSetDelaySimulationTT() {
		return setDelaySimulationTT;
	}

	/**
	 * It returns the stopAutomaticAdvancementTT tool-tips name.
	 *
	 * @return the stopAutomaticAdvancementTT tool-tips name.
	 */
	public static String getStopAutomaticAdvancementTT() {
		return stopAutomaticAdvancementTT;
	}

	/**
	 * It returns the abort message name.
	 *
	 * @return the abort message name.
	 */
	public static String getAbort() {
		return abort;
	}

	/**
	 * It returns the no message name.
	 *
	 * @return the no message name.
	 */
	public static String getNo() {
		return no;
	}

	/**
	 * It returns the saveQ message name.
	 *
	 * @return the saveQ message name.
	 */
	public static String getSaveQ() {
		return saveQ;
	}

	/**
	 * It returns the submit message name.
	 *
	 * @return the submit message name.
	 */
	public static String getSubmit() {
		return submit;
	}

	/**
	 * It returns the yes message name.
	 *
	 * @return the yes message name.
	 */
	public static String getYes() {
		return yes;
	}

	/**
	 * It returns the activationTime data configuration name.
	 *
	 * @return the activationTime data configuration name.
	 */
	public static String getActivationTime() {
		return activationTime;
	}

	/**
	 * It returns the assignmentPolicy data configuration name.
	 *
	 * @return the assignmentPolicy data configuration name.
	 */
	public static String getAssignmentPolicy() {
		return assignmentPolicy;
	}

	/**
	 * It returns the basePriority data configuration name.
	 *
	 * @return the basePriority data configuration name.
	 */
	public static String getBasePriority() {
		return basePriority;
	}

	/**
	 * It returns the ceilingPriority data configuration name.
	 *
	 * @return the ceilingPriority data configuration name.
	 */
	public static String getCeilingPriority() {
		return ceilingPriority;
	}

	/**
	 * It returns the chooseTheSchedulingPolicy data configuration name.
	 *
	 * @return the chooseTheSchedulingPolicy data configuration name.
	 */
	public static String getChooseTheSchedulingPolicy() {
		return chooseTheSchedulingPolicy;
	}

	/**
	 * It returns the executionTime data configuration name.
	 *
	 * @return the executionTime data configuration name.
	 */
	public static String getExecutionTime() {
		return executionTime;
	}

	/**
	 * It returns the multiplicity data configuration name.
	 *
	 * @return the multiplicity data configuration name.
	 */
	public static String getMultiplicity() {
		return multiplicity;
	}

	/**
	 * It returns the name data configuration name.
	 *
	 * @return the name data configuration name.
	 */
	public static String getName() {
		return name;
	}

	/**
	 * It returns the processName data configuration name.
	 *
	 * @return the processName data configuration name.
	 */
	public static String processName() {
		return processName;
	}

	/**
	 * It returns the preemption data configuration name.
	 *
	 * @return the preemption data configuration name.
	 */
	public static String getPreemption() {
		return preemption;
	}

	/**
	 * It returns the requestTime data configuration name.
	 *
	 * @return the requestTime data configuration name.
	 */
	public static String getRequestTime() {
		return requestTime;
	}

	/**
	 * It returns the schedulingPolicy data configuration name.
	 *
	 * @return the schedulingPolicy data configuration name.
	 */
	public static String getSchedulingPolicy() {
		return schedulingPolicy;
	}

	/**
	 * It returns the timeSlice data configuration name.
	 *
	 * @return the timeSlice data configuration name.
	 */
	public static String getTimeSlice() {
		return timeSlice;
	}

	/**
	 * It returns the dataInput data input name.
	 *
	 * @return the dataInput data input name.
	 */
	public static String getDataInput() {
		return dataInput;
	}

	/**
	 * It returns the accessesToResources data input name.
	 *
	 * @return the accessesToResources data input name.
	 */
	public static String getAccessesToResources() {
		return accessesToResources;
	}

	/**
	 * It returns the policies data input name.
	 *
	 * @return the policies data input name.
	 */
	public static String getPolicies() {
		return policies;
	}

	/**
	 * It returns the defaultName data input name.
	 *
	 * @return the defaultNamedata input name.
	 */
	public static String getDefaultName() {
		return defaultName;
	}

	/**
	 * It returns the add data input name.
	 *
	 * @return the add data input name.
	 */
	public static String getAdd() {
		return add;
	}

	/**
	 * It returns the modify data input name.
	 *
	 * @return the modify data input name.
	 */
	public static String getModify() {
		return modify;
	}

	/**
	 * It returns the remove data input name.
	 *
	 * @return the remove data input name.
	 */
	public static String getRemove() {
		return remove;
	}

	/**
	 * It returns the accept data input name.
	 *
	 * @return the accept data input name.
	 */
	public static String getAccept() {
		return accept;
	}

	/**
	 * It returns the processParameters data input name.
	 *
	 * @return the processParameters data input name.
	 */
	public static String getProcessParameters() {
		return processParameters;
	}

	/**
	 * It returns the resourceParameters data input name.
	 *
	 * @return the resourceParameters data input name.
	 */
	public static String getResourceParameters() {
		return resourceParameters;
	}

	/**
	 * It returns the preemptive data input name.
	 *
	 * @return the preemptive data input name.
	 */
	public static String getPreemptive() {
		return preemptive;
	}

	/**
	 * It returns the ICPP data input name.
	 *
	 * @return the ICPP data input name.
	 */
	public static String getICPP() {
		return ICPP;
	}

	/**
	 * It returns the accessParameters data input name.
	 *
	 * @return the accessParameters data input name.
	 */
	public static String getAccessParameters() {
		return accessParameters;
	}

	/**
	 * It returns the quantum data input name.
	 *
	 * @return the quantum data input name.
	 */
	public static String getQuantum() {
		return quantum;
	}

	/**
	 * It returns the ER000 error name.
	 *
	 * @return the ER000 error name.
	 */
	public static String getER000() {
		return ER000;
	}

	/**
	 * It returns the ER001 error name.
	 *
	 * @return the ER001 error name.
	 */
	public static String getER001() {
		return ER001;
	}

	/**
	 * It returns the ER002 error name.
	 *
	 * @return the ER002 error name.
	 */
	public static String getER002() {
		return ER002;
	}

	/**
	 * It returns the ER003 error name.
	 *
	 * @return the ER003 error name.
	 */
	public static String getER003() {
		return ER003;
	}

	/**
	 * It returns the ER004 error name.
	 *
	 * @return the ER004 error name.
	 */
	public static String getER004() {
		return ER004;
	}

	/**
	 * It returns the ER005 error name.
	 *
	 * @return the ER005 error name.
	 */
	public static String getER005() {
		return ER005;
	}

	/**
	 * It returns the ER006 error name.
	 *
	 * @return the ER006 error name.
	 */
	public static String getER006() {
		return ER006;
	}

	/**
	 * It returns the ER007 error name.
	 *
	 * @return the ER007 error name.
	 */
	public static String getER007() {
		return ER007;
	}

	/**
	 * It returns the ER008 error name.
	 *
	 * @return the ER008 error name.
	 */
	public static String getER008() {
		return ER008;
	}

	/**
	 * It returns the ER009 error name.
	 *
	 * @return the ER009 error name.
	 */
	public static String getER009() {
		return ER009;
	}

	/**
	 * It returns the ER010 error name.
	 *
	 * @return the ER010 error name.
	 */
	public static String getER010() {
		return ER010;
	}

	/**
	 * It returns the error name.
	 *
	 * @return the error name.
	 */
	public static String getError() {
		return error;
	}

	/**
	 * It returns the language language name.
	 *
	 * @return the language language name.
	 */
	public static String getLanguage() {
		return language;
	}

	/**
	 * It returns the addNewRowTT tool-tips name.
	 *
	 * @return the addNewRowTT tool-tips name.
	 */
	public static String getAddNewRowTT() {
		return addNewRowTT;
	}

	/**
	 * It returns the ICPPTT tool-tips name.
	 *
	 * @return the ICPPTT tool-tips name.
	 */
	public static String getICPPTT() {
		return ICPPTT;
	}

	/**
	 * It returns the modifyRowTT tool-tips name.
	 *
	 * @return the modifyRowTT tool-tips name.
	 */
	public static String getModifySelectedRowTT() {
		return modifySelectedRowTT;
	}

	/**
	 * It returns the onlyForTimeSharingPoliciesTT tool-tips name.
	 *
	 * @return the onlyForTimeSharingPoliciesTT tool-tips name.
	 */
	public static String getOnlyForTimeSharingPoliciesTT() {
		return onlyForTimeSharingPoliciesTT;
	}

	/**
	 * It returns the removeSelectedRowTT tool-tips name.
	 *
	 * @return the removeSelectedRowTT tool-tips name.
	 */
	public static String getRemoveSelectedRowTT() {
		return removeSelectedRowTT;
	}

	/**
	 * It returns the requestDuration name.
	 *
	 * @return the requestDuration name.
	 */
	public static String getRequestDuration() {
		return requestDuration;
	}

	/**
	 * It returns the lookAndFeelTT name.
	 *
	 * @return the lookAndFeelTT name.
	 */
	public static String getLookAndFeelTT() {
		return lookAndFeelTT;
	}

}
