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
 * File: AssignmentPolicy.java
 * Package: scheduler
 * Author: Marin Pier Giorgio, Dalle Pezze Piero
 * Date: 03/02/2006
 * Version: 1.1
 * 
 * Modifies:
 *  - v.1.1 (31/01/2007): English translation. Java6 compatible. (Piero Dalle Pezze)
 *  - v.1.0 (03/02/2006): Codify and documentation.
 */
package org.rainbow.scheduler.assignpolicies;

import java.util.ArrayList;
import java.io.Serializable;

import org.rainbow.data.*;

/**
 * This is the interface of a generic assignment policy.
 * 
 * @author Marin Pier Giorgio
 * @author Dalle Pezze Piero
 * @version 1.1
 */
public interface AssignmentPolicy extends Serializable {

	/**
	 * It returns the blocked queue.
	 * 
	 * @return the blocked queue.
	 */
	public ArrayList<Request> getRequestsQueue();

	/**
	 * It adds the request of a process in the queue.
	 * 
	 * @param request
	 *            the request of the process to add.
	 */
	public void insert(Request request);

	/**
	 * It extract a request of a process from the queue.
	 * 
	 * @return the request extracted.
	 */
	public Request extract();

	/**
	 * It returns the number of elements in the structure.
	 * 
	 * @return the size of the structure.
	 */
	public int size();

}