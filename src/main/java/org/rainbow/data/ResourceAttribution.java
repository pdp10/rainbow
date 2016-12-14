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
 * File: ResourceAttribution.java
 * Package: data
 * Author: Piero Dalle Pezze
 * Date: 04/02/2006
 * Version: 1.2
 * 
 * Modifies:
 * v1.2 (31/01/2007): English translation. Java6 compatible. (Dalle Pezze Piero)
 * v1.1 (04/02/2006): Class codify. Piero Dalle Pezze 
 * v1.0 (04/02/2006): Class documentation. Piero Dalle Pezze
 */
package org.rainbow.data;

import java.util.ArrayList;

/**
 * This class represents a generic relation between an object Resource and a
 * list of Process objects that want to access it.
 * 
 * @author Piero Dalle Pezze
 * @version 1.2
 */
public class ResourceAttribution {

	/**
	 * A list of Process objects that want to access the resource.
	 */
	private ArrayList<SimulatedProcess> processList;

	/**
	 * The resource wanted by processes in processList.
	 */
	private Resource resource;

	/**
	 * It creates an instance of this relation.
	 * 
	 * @param resource
	 *            The resource wanted by processes in processList.
	 * @param processList
	 *            A list of process that want to access the resource.
	 */
	public ResourceAttribution(Resource resource,
			ArrayList<SimulatedProcess> processList) {
		this.resource = resource;
		this.processList = processList;
	}

	/**
	 * It returns the list of processes that want to access the resource.
	 * 
	 * @return the list of processes
	 */
	public ArrayList<SimulatedProcess> getProcessList() {
		return processList;
	}

	/**
	 * It returns the resource.
	 * 
	 * @return the resource.
	 */
	public Resource getResource() {
		return resource;
	}

}