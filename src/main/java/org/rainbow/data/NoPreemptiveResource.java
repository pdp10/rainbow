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
 * File: NoPreemptiveResource.java
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

/**
 * This class represents a non preemptive resource.
 * 
 * @author Piero Dalle Pezze
 * @version 1.2
 */
public class NoPreemptiveResource extends Resource {

	/**
	 * A serial number between 0 and 99.
	 */
	private static final long serialVersionUID = 2L;

	/**
	 * The ceiling priority of the resource.
	 */
	private int ceilingPriority = 0;

	/**
	 * It creates a non preemptive resource instance.
	 * 
	 * @param name
	 *            The name of the resource.
	 * @param multiplicity
	 *            The multiplicity of the resource. It must be grater than 0.
	 * @param ceilingPriority
	 *            The ceiling priority of the resource.
	 */
	public NoPreemptiveResource(String name, int multiplicity,
			int ceilingPriority) {
		super(name, multiplicity);
		this.ceilingPriority = ceilingPriority;
	}

	/**
	 * It returns the ceiling priority of the resource.
	 * 
	 * @return the ceiling priority of the resource.
	 */
	public int getCeilingPriority() {
		return ceilingPriority;
	}

	/**
	 * It sets the ceiling priority of the resource.
	 * 
	 * @param ceilingPriority
	 *            the ceiling priority of the resource.
	 */
	public void setCeilingPriority(int ceilingPriority) {
		this.ceilingPriority = ceilingPriority;
	}

}