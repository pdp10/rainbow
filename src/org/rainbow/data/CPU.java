/*
 * Rainbow - A simulator of processes and resources in a multitasking computer.
 * Copyright (C) 2014. E-mail: piero.dallepezze@gmail.com
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
 * File: CPU.java
 * Package: data
 * Author: Piero Dalle Pezze
 * Date: 27/11/2014
 * Version: 1.0
 * 
 * Modifies:
 * v1.0 (27/11/2014): Class documentation. Class codify. Piero Dalle Pezze 
 */
package org.rainbow.data;

/**
 * This class represents a CPU
 * 
 * @author Piero Dalle Pezze
 * @version 1.0
 */
public class CPU extends PreemptiveResource {

	/**
	 * A serial number between 0 and 99.
	 */
	private static final long serialVersionUID = 2L;

	/**
	 * It creates a CPU
	 * 
	 * @param core
	 *            The number of cores for this CPU. It must be grater than 0.
	 */
	public CPU(int core) {
		super("CPU", core);
	}
}