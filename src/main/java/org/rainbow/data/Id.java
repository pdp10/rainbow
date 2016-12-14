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
 * File: Id.java
 * Package: data
 * Author: Michele Perin, Dalle Pezze Piero
 * Date: 16/02/2006
 * Version: 1.1
 *
 * Modifies:
 * v.1.1 (31/01/2007): English translation. Java6 compatible. (Dalle Pezze Piero)
 * v.1.0 (16/02/2006): Class and documentation definition.
 */

package org.rainbow.data;

/**
 * It generates unique process identification (ID).
 * 
 * @author Michele Perin
 * @author Dalle Pezze Piero
 * @version 1.1
 */
public class Id {
	/**
	 * Id type instance counter.
	 */
	private static int counter = 0;

	/**
	 * It returns a new increased id.
	 * 
	 * @return a new unique id.
	 */
	public final static int returnNewId() {
		counter++;
		return counter;
	}

	/**
	 * It sets the counter id.
	 * 
	 * @param id
	 *            the current counter id.
	 */
	public final static void setCounterId(int id) {
		counter = id;
	}

	/**
	 * It gets the last id.
	 * 
	 * @return the last id.
	 */
	public final static int getCounterId() {
		return counter;
	}

	/**
	 * It decreases the counter id.
	 */
	public final static void decreaseId() {
		counter--;
	}

	/**
	 * It resets the counter.
	 */
	public final static void resetCounter() {
		counter = 0;
	}
}
