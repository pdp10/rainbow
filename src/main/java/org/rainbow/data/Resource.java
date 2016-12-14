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
 * File: Resource.java
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

import java.io.Serializable;

/**
 * This class represents a generic resource.
 * 
 * @author Piero Dalle Pezze
 * @version 1.2
 */
public abstract class Resource implements Serializable {

	/**
	 * A serial number between 4 and 99.
	 */
	private static final long serialVersionUID = 5L;

	/**
	 * A counter of instance of Resource type.
	 */
	private static int counter = 0;

	/**
	 * The resource identification.
	 */
	private int id = counter;

	/**
	 * The resource multiplicity. The number of shared access accepted.
	 */
	protected int multiplicity;

	/**
	 * The resource name.
	 */
	private String name;

	/**
	 * It creates a resource instance.
	 * 
	 * @param name
	 *            The resource name.
	 * @param multiplicity
	 *            The resource multiplicity. It must be grater than 0.
	 */
	public Resource(String name, int multiplicity) {
		counter++;
		this.name = name;
		if (multiplicity < 1) {
			this.multiplicity = 1;
		} else {
			this.multiplicity = multiplicity;
		}
	}

	/**
	 * It returns true if both resources have the same id, false otherwise.
	 * 
	 * @param resource
	 *            The second resource.
	 * @return true if they have the same id, false otherwise.
	 */
	public boolean equals(Object resource) {
		if (resource instanceof Resource) {
			Resource res = (Resource) resource;
			return hashCode() == res.hashCode();
		}
		return false;
	}

	/**
	 * It returns the resource multiplicity.
	 * 
	 * @return the resource multiplicity.
	 */
	public int getMultiplicity() {
		return multiplicity;
	}

	/**
	 * It returns the resource name.
	 * 
	 * @return the resource name.
	 */
	public String getName() {

		return name;

	}

	/**
	 * It return the hashcode of the resource.
	 * 
	 * @return the resource identification.
	 */
	public int hashCode() {
		return id;
	}

	/**
	 * It converces the resource to a string.
	 * 
	 * @return the resource name.
	 */
	public String toString() {
		return name;
	}

	/**
	 * It sets the multiplicity of the resource.
	 * 
	 * @param multiplicity
	 *            the multiplicity of the resource.
	 */
	public void setMultiplicity(int multiplicity) {
		this.multiplicity = multiplicity;
	}

	/**
	 * It sets the name of the resource.
	 * 
	 * @param name
	 *            the name of the resource.
	 */
	public void setName(String name) {
		this.name = name;
	}

}