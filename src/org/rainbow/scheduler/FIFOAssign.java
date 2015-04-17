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
 * File: FIFOAssign.java
 * Package: scheduler
 * Author: Marin Pier Giorgio, Dalle Pezze Piero
 * Date: 03/02/2006
 * Version: 1.1
 * 
 * Modifies:
 *  - v.1.2 (13/11/2014): Added iterators.    
 *  - v.1.1 (31/01/2007): English translation. Java6 compatible. (Piero Dalle Pezze)
 *  - v.1.0 (03/02/2006): Codify and documentation.
 */
package org.rainbow.scheduler;

import java.util.*;

import org.rainbow.data.*;

/**
 * This class implements a First In First Out assignment policy.
 * 
 * @author Marin Pier Giorgio
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class FIFOAssign implements AssignmentPolicy {

	/**
	 * To serializable
	 */
	private static final long serialVersionUID = -5001L;

	/**
	 * The queue of requests using FIFO policy.
	 */
	private LinkedList<Request> requestsQueue;

	/**
	 * It creates a FCFS queue.
	 */
	public FIFOAssign() {
		requestsQueue = new LinkedList<Request>();
	}

	/**
	 * It inserts the request of the process in tail of the queue.
	 * 
	 * @param request
	 *            the request to add.
	 */
	public void insert(Request request) {
		requestsQueue.add(request);
	}

	/**
	 * It extracts the request from the head of the queue.
	 * 
	 * @return the request.
	 */
	public Request extract() {
		if (requestsQueue.size() > 0) {
			return requestsQueue.removeFirst();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return requestsQueue.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public ArrayList<Request> getRequestsQueue() {
		ArrayList<Request> sp = new ArrayList<Request>(requestsQueue.size());
		Iterator<Request> itRequestsQueue = requestsQueue.iterator();
		while(itRequestsQueue.hasNext()) {
			sp.add(itRequestsQueue.next());		
		}
		return sp;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "First In First Out";
	}

}
