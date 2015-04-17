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
 * File: HPFAssign.java
 * Package: scheduler
 * Author: Dalle Pezze Piero
 * Date: 31/01/2007
 * Version: 1.1
 * 
 * Modifies:
 *  - v.1.1 (13/11/2014): Added iterators.    
 *  - v.1.0 (05/03/2007): Codify and documentation.
 */
package org.rainbow.scheduler;

import java.util.*;
import org.rainbow.data.*;

/**
 * This class implements the Highest Priority First assignment policy. This
 * policy always extracts the request with the highest priority.
 * 
 * @author Dalle Pezze Piero
 * @version 1.1
 */
public class HPFAssign implements AssignmentPolicy {

	/**
	 * To serializable
	 */
	private static final long serialVersionUID = -5003L;

	/**
	 * The queue of requests sorted using priority policy.
	 */
	private LinkedList<Request> requestsQueue;

	/**
	 * It creates the Highest Priority First assignment policy.
	 */
	public HPFAssign() {
		requestsQueue = new LinkedList<Request>();
	}

	/**
	 * It inserts the request of the process in the queue. The order depends on
	 * the priority of the request.
	 * 
	 * @param request
	 *            the request to add.
	 */
	public void insert(Request request) {
		boolean added = false;
		ListIterator<Request> itRequestsQueue = requestsQueue.listIterator();
		while(itRequestsQueue.hasNext() && !added) {
			if (request.getPriority() > itRequestsQueue.next().getPriority()) {
				added = true;
				requestsQueue.add(itRequestsQueue.previousIndex(), request);
			}
		}
		if (!added) {
			requestsQueue.add(request);
		}
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
		return "Highest Priority First";
	}

}
