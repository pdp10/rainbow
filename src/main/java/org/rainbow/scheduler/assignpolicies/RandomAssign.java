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
 * File: RandomAssign.java
 * Package: scheduler
 * Author: Dalle Pezze Piero
 * Date: 03/02/2006
 * Version: 1.1
 * 
 * Modifies:
 *  - v.1.2 (13/11/2014): Added iterators.   
 *  - v.1.1 (31/01/2007): English translation. Java6 compatible. (Piero Dalle Pezze)
 *  - v.1.0 (03/02/2006): Codify and documentation.
 */
package org.rainbow.scheduler.assignpolicies;

import java.util.*;

import org.rainbow.data.*;

/**
 * This class implements a Random assignment policy.
 * 
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class RandomAssign implements AssignmentPolicy {

	/**
	 * To serializable
	 */
	private static final long serialVersionUID = -5014L;

	/**
	 * The queue of request processes.
	 */
	protected LinkedList<Request> requestQueue;

	/**
	 * The random generator.
	 */
	protected Random random = new Random();

	/**
	 * It creates the random queue.
	 */
	public RandomAssign() {
		requestQueue = new LinkedList<Request>();
	}

	/**
	 * It inserts the request of the process in tail of the queue.
	 * 
	 * @param request
	 *            The request of the process.
	 */
	public void insert(Request request) {
		requestQueue.addLast(request);
	}

	/**
	 * It extracts in a random way a request of resource.
	 * 
	 * @return a process.
	 */
	public Request extract() {
		if (size() > 0) {
			return requestQueue.remove(random.nextInt(size()));
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return requestQueue.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public ArrayList<Request> getRequestsQueue() {
		ArrayList<Request> sp = new ArrayList<Request>(requestQueue.size());
		Iterator<Request> itRequestQueue = requestQueue.iterator();
		while (itRequestQueue.hasNext()) {
			sp.add(itRequestQueue.next());
		}
		return sp;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "Random";
	}
}