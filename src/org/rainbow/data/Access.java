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
 * File: Access.java
 * Package: data
 * Author: Michele Perin, Dalle Pezze Piero
 * Date: 31/01/2006
 * Version: 1.5
 * 
 * Modifies:
 * v.1.5 (31/01/2007): English translation. Java6 compatible. (Piero Dalle Pezze)
 * v.1.4 (11/02/2006): Method accessRequest() and documentation modified.
 * v.1.3 (06/02/2006): Code checking.
 * v.1.2 (02/02/2006): Checking between documentation and code.
 * v.1.1 (01/02/2006): Class codify.
 * v.1.0 (31/01/2006): Documentation.
 */

package org.rainbow.data;

import java.io.*;

/**
 * Instances represents a process request to access to a resource. These are
 * created by the resource to whom the process want to access.
 * 
 * @author Michele Perin
 * @author Dalle Pezze Piero
 * @version 1.5
 */
public class Access implements Serializable {

	/**
	 * A number between 100 and 199.
	 */
	private static final long serialVersionUID = 100L;

	/**
	 * SimulatedProcess request time to access to a resource.
	 */
	private int requestTime;

	/**
	 * It returns the process request time.
	 * 
	 * @return the process request time
	 */
	public int getRequestTime() {
		return requestTime;
	}

	/**
	 * The duration of the process request.
	 */
	private int duration;

	/**
	 * It returns the duration of the process request.
	 * 
	 * @return the duration of the process request.
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * The resource that the process want to access.
	 */
	private Resource resource;

	/**
	 * It returns the resource that the process want to access.
	 * 
	 * @return the resource that the process want to access.
	 */
	public Resource getResource() {
		return resource;
	}

	/**
	 * It creates an instance that collects all informations about the process
	 * request to a resource.
	 * 
	 * @param resource
	 *            The resource that the process want to access.
	 * @param requestTime
	 *            The process request time.
	 * @param duration
	 *            The duration of the process request.
	 */
	public Access(Resource resource, int requestTime, int duration) {
		this.resource = resource;
		this.requestTime = requestTime;
		this.duration = duration;
	}
}
