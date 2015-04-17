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
 * File: ProcessDispatcher.java
 * Package: scheduler
 * Author: Piero Dalle Pezze
 * Date: 10/11/2014
 * Version: 1.2
 *
 * Modifies:
 * v1.2 (10/11/2014): Class testing.
 * v1.1 (10/11/2014): Class codify.
 * v1.0 (10/11/2014): Class documentation.
 */
package org.rainbow.scheduler;

import java.util.*;
import org.rainbow.data.*;
import org.rainbow.simulation.*;

/**
 * This is the interface specifying how a SchedulingPolicy can interact with Scheduler. This interface 
 * must be implemented inside a scheduler as inner class.
 *
 * @author Piero Dalle Pezze
 * @version 1.2
 */
interface ProcessDispatcherInterface {
    
    
    /**
     * It increases the time of the scheduler and descreases the next event
     * time. It must be invoked by the scheduling algorithm, in particular
     * inside the method execute(nextEventTime). The parameter time, must be
     * greater then 0 and less than nextEventTime received. Otherwise it sets
     * the increase value to 1.
     *
     * @param time
     *            the time to increase.
     * @return the inner scheduler state.
     */
    State increaseSchedulerTime(int time);

    
    /**
     * It returns the process in execution. Note that it shares the memory.
     *
     * @return the process in execution if it exist or null otherwise.
     */
    PCB getPCBCurrent();
    
    
    /**
     * It returns the current time.
     *
     * @return the current time.
     */
    int getCurrentTime();    
    
    
    /**
     * It returns the processes table. Note that it shares the structure and the
     * memory.
     *
     * @return the processes table.
     */
    HashMap<Integer, PCB> getPCBTable();
    
      
    /**
     * It removes the pcbCurrent from the CPU. The next time, a new process must
     * be extracted from the ready queue. This method must be called by preemptive
     * scheduling policies inside the method insert() if the there is the preemption
     * of the pcbCurrent.
     */
    void preemptionPCBCurrent();
    
    
    /**
     * It removes the pcbCurrent from the CPU. The next time, a new process must
     * be extracted from the ready queue. It tests also if releasing or removing
     * the current process. This method must be called by time sharing scheduling
     * policies inside the method execute() when the quantum is elapsed.
     */
    void removePCBCurrent();
    

}
