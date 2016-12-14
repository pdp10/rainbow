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
 * File: ResourceUse.java
 * Package: data
 * Author: Michele Perin, Piero Dalle Pezze
 * Date: 31/01/2006
 * Version: 1.2
 *
 * Modifies:
 * v1.2 (31/01/2007): English translation. Java6 compatible. (Dalle Pezze Piero)
 * v1.1 (01/02/2006): Class codify.
 * v1.0 (31/01/2006): Class documentation.
 */

package org.rainbow.data;

/**
 * It represents the dynamic context of use of a resource by a process.
 * 
 * @author Michele Perin
 * @author Piero Dalle Pezze
 * @version 1.2
 */
public class UseResource {
	/**
	 * The refered resource
	 */
	private Resource resource;

	/**
	 * The remaining time that process need to use the resource.
	 */
	private int remainingTime;

	/**
	 * It returns the resource.
	 * 
	 * @return the resource.
	 */
	public Resource getResource() {
		return resource;
	}

	/**
	 * It returns the remaining time that the process neet to use the resource
	 * 
	 * @return the remaining time of access to the resource.
	 */
	public int getRemainingTime() {
		return remainingTime;
	}

	/**
	 * It decreases the access remaining time of the value time.
	 * 
	 * @param time
	 *            The time value to decrease in the remaining time.
	 * @return true if the remaining time is equal to 0, false otherwise.
	 */
	public boolean decreaseRemainingTime(int time) {
		if (remainingTime <= time) {
			remainingTime = 0;
			return true;
		}
		remainingTime = remainingTime - time;
		return false;
	}

	/**
	 * It creates an instance that mantains all values about the dynamic use of
	 * a resource by a process.
	 * 
	 * @param resource
	 *            The resource to use.
	 * @param duration
	 *            The duration of the access to the resource.
	 */
	public UseResource(Resource resource, int duration) {
		this.resource = resource;
		this.remainingTime = duration;
	}
}