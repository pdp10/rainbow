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
 * File: Request.java
 * Package: data
 * Author: Piero Dalle Pezze
 * Date: 04/02/2007
 * Version: 1.1
 * 
 * Modifies:
 * v1.1 (04/02/2007): Class codify. Piero Dalle Pezze 
 * v1.0 (04/02/2007): Class documentation. Piero Dalle Pezze
 */
package org.rainbow.data;

import java.io.Serializable;

/**
 * This class represents a generic request of a process to a resource.
 * 
 * @author Piero Dalle Pezze
 * @version 1.2
 */
public class Request implements Serializable {

	/**
	 * A serial number between 4 and 99.
	 */
	private static final long serialVersionUID = 4L;

	/**
	 * The identifier of the request. It is the id of the process.
	 */
	private Integer id;

	/**
	 * The priority of the request. It is the active priority of the process.
	 */
	private int priority;

	/**
	 * It creates a request of a process.
	 * 
	 * @param process
	 *            the process that requests the resource.
	 */
	public Request(PCB process) {
		id = process.getSimulatedProcess().getId();
		// The dynamic priority is the current priority of the process
		priority = process.getActivePriority();
	}

	/**
	 * It returns the id of the request.
	 * 
	 * @return the id of the request.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * It returns the priority of the request.
	 * 
	 * @return the priority of the request.
	 */
	public Integer getPriority() {
		return priority;
	}
}