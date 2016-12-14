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
 * File: InputControl.java
 * Package: gui.input
 * Author: Dalle Pezze Piero
 * Date: 28/02/2007
 * Version: 1.0
 *
 * Modifies:
 *  - v.1.0 (28/02/2007): Code and documentation.
 */
package org.rainbow.gui.input;

/**
 * This class is used to find errors in the configuration created by the user.
 *
 * @author Piero Dalle Pezze
 * @version 1.0
 */
public class InputControl {

	/**
	 * It returns true if an error is found in process' data.
	 * 
	 * @return true if an error is found in process' data, false otherwise.
	 */
	public static boolean isProcessWrong(String activationTime,
			String executionTime, String priority) {
		boolean wrong = false;
		try {
			int activationTimeInt = Integer.parseInt(activationTime);
			int executionTimeInt = Integer.parseInt(executionTime);
			int priorityInt = Integer.parseInt(priority);
			if (activationTimeInt < 0
					|| activationTimeInt > RainbowConfig.getMaxActivationTime()) {
				wrong = true;
			}
			if (executionTimeInt < 1
					|| executionTimeInt > RainbowConfig.getMaxExecutionTime()) {
				wrong = true;
			}
			if (priorityInt < RainbowConfig.getMinPriority()
					|| priorityInt > RainbowConfig.getMaxPriority()) {
				wrong = true;
			}
		} catch (NumberFormatException e) {
			wrong = true;
		}
		return wrong;
	}

	/**
	 * It returns true if an error is found in preemptive resource's data.
	 * 
	 * @return true if an error is found in preemptive resource's data, false
	 *         otherwise.
	 */
	public static boolean isPreemptiveResourceWrong(String multiplicity) {
		boolean wrong = false;
		try {
			int multiplicityInt = Integer.parseInt(multiplicity);
			if (multiplicityInt < 1
					|| multiplicityInt > RainbowConfig.getMaxMultiplicity()) {
				wrong = true;
			}
		} catch (NumberFormatException e) {
			wrong = true;
		}
		return wrong;
	}

	/**
	 * It returns true if an error is found in non-preemptive resource's data.
	 * 
	 * @return true if an error is found in non-preemptive resource's data,
	 *         false otherwise.
	 */
	public static boolean isNoPreemptiveResourceWrong(String multiplicity,
			String ceilingPriority) {
		if (isPreemptiveResourceWrong(multiplicity)) {
			return true;
		}
		boolean wrong = false;
		try {
			int ceilingPriorityInt = Integer.parseInt(ceilingPriority);
			if (ceilingPriorityInt < RainbowConfig.getMinCeilingPriority()
					|| ceilingPriorityInt > RainbowConfig
							.getMaxCeilingPriority()) {
				wrong = true;
			}
		} catch (NumberFormatException e) {
			wrong = true;
		}
		return wrong;
	}

	/**
	 * It returns true if an error is found in accesses' data.
	 * 
	 * @return true if an error is found in accesses' data, false otherwise.
	 */
	public static boolean isAccessWrong(String activationTime,
			String executionTime, String requestTime, String requestDuration) {
		boolean wrong = false;
		try {
			int activationTimeInt = Integer.parseInt(activationTime);
			int executionTimeInt = Integer.parseInt(executionTime);
			int requestTimeInt = Integer.parseInt(requestTime);
			int requestDurationInt = Integer.parseInt(requestDuration);
			if (activationTimeInt < 0
					|| activationTimeInt > RainbowConfig.getMaxActivationTime()) {
				wrong = true;
			}
			if (executionTimeInt < 1
					|| executionTimeInt > RainbowConfig.getMaxExecutionTime()) {
				wrong = true;
			}
			if (requestTimeInt < 0 || requestTimeInt > executionTimeInt - 1) {
				wrong = true;
			}
			if (requestDurationInt < 1
					|| requestDurationInt > executionTimeInt - requestTimeInt) {
				wrong = true;
			}
		} catch (NumberFormatException e) {
			wrong = true;
		}
		return wrong;
	}

	/**
	 * It returns true if an error is found in the request time.
	 * 
	 * @return true if an error is found in the request time, false otherwise.
	 */
	public static boolean isRequestTimeWrong(String executionTime,
			String requestTime) {
		boolean wrong = false;
		try {
			int executionTimeInt = Integer.parseInt(executionTime);
			int requestTimeInt = Integer.parseInt(requestTime);
			if (executionTimeInt < 1
					|| executionTimeInt > RainbowConfig.getMaxExecutionTime()) {
				wrong = true;
			}
			if (requestTimeInt < 0 || requestTimeInt > executionTimeInt - 1) {
				wrong = true;
			}
		} catch (NumberFormatException e) {
			wrong = true;
		}
		return wrong;
	}

	/**
	 * It returns true if an error is found in the duration of request.
	 * 
	 * @return true if an error is found in the duration of request, false
	 *         otherwise.
	 */
	public static boolean isRequestDurationWrong(String executionTime,
			String requestTime, String requestDuration) {
		boolean wrong = false;
		try {
			int executionTimeInt = Integer.parseInt(executionTime);
			int requestTimeInt = Integer.parseInt(requestTime);
			int requestDurationInt = Integer.parseInt(requestDuration);
			if (executionTimeInt < 1
					|| executionTimeInt > RainbowConfig.getMaxExecutionTime()) {
				wrong = true;
			}
			if (requestTimeInt < 0 || requestTimeInt > executionTimeInt - 1) {
				wrong = true;
			}
			if (requestDurationInt < 1
					|| requestDurationInt > executionTimeInt - requestTimeInt) {
				wrong = true;
			}
		} catch (NumberFormatException e) {
			wrong = true;
		}
		return wrong;
	}

	/**
	 * It returns true if the scheduling policy doesn't exist.
	 * 
	 * @return true if the scheduling policy doesn't exist, false otherwise.
	 */
	public static boolean isSchedulingPolicyWrong(String schedulingPolicy) {
		String[] schedulingPolicies = RainbowConfig.getSchedulingPolicies();
		boolean notExist = true;
		for (int i = 0; i < schedulingPolicies.length && notExist; i++) {
			if (schedulingPolicy.equals(schedulingPolicies[i])) {
				notExist = false;
			}
		}
		return notExist;
	}

	/**
	 * It returns true if the assignment policy doesn't exist.
	 * 
	 * @return true if the assignment policy doesn't exist, false otherwise.
	 */
	public static boolean isAssignmentPolicyWrong(String assignmentPolicy) {
		String[] assignmentPolicies = RainbowConfig.getAssignmentPolicies();
		boolean notExist = true;
		for (int i = 0; i < assignmentPolicies.length && notExist; i++) {
			if (assignmentPolicy.equals(assignmentPolicies[i])) {
				notExist = false;
			}
		}
		return notExist;
	}

	/**
	 * It returns true if the specified quantum is out of range.
	 * 
	 * @return true if the specified quantum is out of range, false otherwise.
	 */
	public static boolean isQuantumWrong(String quantum) {
		boolean wrong = false;
		try {
			int quantumInt = Integer.parseInt(quantum);
			if (quantumInt < 1 || quantumInt > RainbowConfig.getMaxQuantum()) {
				wrong = true;
			}
		} catch (NumberFormatException e) {
			wrong = true;
		}
		return wrong;
	}

}
