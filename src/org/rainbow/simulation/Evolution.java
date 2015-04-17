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
 * File: Evolution.java
 * Package: simulation
 * Author: Stefano Bertolin, Piero Dalle Pezze
 * Date: 28/01/2006
 * Version: 1.2
 *
 * Modifies:
 *  v1.2 (01/02/2007): English translation. Java6 compatible. (Piero Dalle Pezze)
 *  v1.1 (30/01/2006): Codify.
 *  v1.0 (28/01/2006): Documentation.
 */
package org.rainbow.simulation;

import java.util.*;
import org.rainbow.data.*;
import org.rainbow.scheduler.*;

/**
 * This class store all states of the simulation in an array. It is possible to
 * go forward(), backward(), start().
 *
 * @author Bertolin Stefano
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class Evolution {
    
    /**
     * The actual time.
     */
    private int actualTime = -1;
    
    /**
     * The lenght of the history.
     */
    private int length = 0;
    
    /**
     * The history of the simulation created by the Scheduler.
     */
    private ArrayList<State> history;
    
    /**
     * It creates an instance of Evolution. It creates an instance of Scheduler
     * and invoke the method Scheduler.run();
     *
     * @param configuration
     *            The user configuration.
     */
    public Evolution(Configuration configuration) {
        history = new ArrayList<State>();
        Scheduler scheduler;
              
        if (configuration.isICPP()) {
            scheduler = new RainbowICPPScheduler(configuration);
        } else {
            scheduler = new RainbowScheduler(configuration);
        }
        
        LinkedList<State> compressedHistory = scheduler.run();
        // A state can take more than 1 tick.
        State s;
        int i, j;
        // history can have equal states.
        for (i = 0; i < compressedHistory.size(); i++) {
            s = compressedHistory.get(i);
            for (j = 0; j < s.getDuration(); j++) {
                history.add(s);
            }
        }
        length = history.size();
    }
    
    /**
     * It reset the actual time.
     */
    public synchronized void start() {
        actualTime = -1;
    }
    
    /**
     * It returns the next state.
     *
     * @return the next state.
     */
    public synchronized State forward() {
        if (actualTime < length - 1) {
            actualTime = actualTime + 1;
            return history.get(actualTime);
        }
        return null;
    }
    
    /**
     * It returns the previous state.
     *
     * @return the previous state.
     */
    public synchronized State backward() {
        if (actualTime <= 0) {
            actualTime = -1;
            return null;
        }
        actualTime = actualTime - 1;
        return history.get(actualTime);
    }
    
    
    /**
     * It returns the length of the simulation.
     *
     * @return the length of the simulation.
     */
    public synchronized int getDuration() {
        return length;
    }
}
