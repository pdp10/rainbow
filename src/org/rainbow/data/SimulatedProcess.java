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
 * File: SimulatedSimulatedProcess.java
 * Package: data
 * Author: Dalle Pezze Piero
 * Date: 03/02/2006
 * Version: 1.2
 *
 * Modifies:
 * v.1.2 (31/01/2007): English translation. Java6 compatible.
 *                      More generalization of the class. Priority management.
 * v.1.1 (06/02/2006): Class codify.
 * v.1.0 (03/02/2006): Class documentation.
 */

package org.rainbow.data;

import java.util.*;
import java.io.Serializable;

/**
 * The generated hierarchy represents simulated processes from a static point of
 * view except the dynamic priority of a process that can be changed.
 *
 * @author Dalle Pezze Piero
 * @version 1.2
 */
public class SimulatedProcess implements Serializable {
    
    /**
     * A serial number between 200 and 299.
     */
    private static final long serialVersionUID = 200L;
    
    /**
     * The unique process identification. It must initialized by calling methods
     * in the Id class.
     */
    private Integer id;
    
    /**
     * It returns the process identification.
     *
     * @return the process identification.
     */
    public Integer getId() {
        return id;
    }
    
    /**
     * It decreases the counter id.
     */
    public final void decreaseId() {
        id--;
    }
    
    /**
     * The accesses list to resources, sorted by request time increasing.
     */
    private ArrayList<Access> accessesList = new ArrayList<Access>();
    
    /**
     * It returns the list of accesses to resources, sorted by execution time
     * increasing.
     *
     * @return returns a list of accesses to resources.
     */
    public ArrayList<Access> getAccessesList() {
        return accessesList;
    }
    
    /**
     * It adds a new access request to the accesses list. Every new insert has
     * to mantain the accesses list sorted by execution time increasing. It
     * returns true if it inserts the new request correctly, false otherwise.
     *
     * @param resource
     *            The resource that the process want to access.
     * @param requestTime
     *            The request time of the resource.
     * @param duration
     *            The duration of the request time.
     * @return true if it inserts the new request correctly, false otherwise.
     */
    public boolean addNewAccessRequest(Resource resource, int requestTime,
            int duration) {
        boolean added = false, error = false;
        Access readAccess = null;
        Access access = new Access(resource, requestTime, duration);

        // It checks releases.
        for (int i = 0; i < accessesList.size() && !error; i++) {
            readAccess = accessesList.get(i);
            // two release at the same time
            if(requestTime + duration == readAccess.getRequestTime() + readAccess.getDuration()) {
                error = true;
                continue;
            }
            // this new resource will be released at the same time of an inserted request.
            if(requestTime + duration == readAccess.getRequestTime()) {
                error = true;
                continue;                
            }
            // a resource will be released at the same time of this new request.          
            if(requestTime == readAccess.getRequestTime() + readAccess.getDuration()) {
                error = true;
                continue;                
            }
        } 
        // It checks requests.
        for (int i = 0; i < accessesList.size() && !added
                && !error; i++) {
            /* it mantains the sorted list. */
            readAccess = accessesList.get(i);
            
            // same resource, common intervals
            if (readAccess.getResource().equals(resource)) {

                // --------------
                //      ------------
                if(requestTime <= readAccess.getRequestTime() &&
                        requestTime + duration >= readAccess.getRequestTime()) {
                    error = true;
                    continue;
                }
                
                //    ----------------
                // -----------
                if(readAccess.getRequestTime() <= requestTime &&
                        readAccess.getRequestTime() + readAccess.getDuration() >= requestTime) {
                    error = true;
                    continue;
                }
                
                //    -----
                // -----------
                if(readAccess.getRequestTime() <= requestTime &&
                        readAccess.getRequestTime() + readAccess.getDuration() >= requestTime + duration) {
                    error = true;
                    continue;
                }
                
                //  --------------
                //     -------
                if(requestTime <= readAccess.getRequestTime() &&
                        requestTime + duration >= readAccess.getRequestTime() + readAccess.getDuration()) {
                    error = true;
                    continue;
                }
                
            }
            
            if (readAccess.getRequestTime() > requestTime) {
                accessesList.add(i, access);
                added = true;
            } else {
                if (readAccess.getRequestTime() == requestTime) {
                    error = true;
                }
            }
        }
        if (error) {
            return false;
        }
        if (added) {
            return true;
        }
        /* insert at the end. */
        return accessesList.add(access);
    }
    
    /**
     * It removes an old access request to the accesses list. It returns true if
     * it removes the old request correctly, false otherwise.
     *
     * @param resource
     *            The resource that the process want to access.
     * @return true if it removes the old request, false otherwise.
     */
    public boolean removeRequest(Resource resource) {
        boolean removed = false;
        for (int i = 0; i < accessesList.size(); i++) {
	    System.out.println("resource " + resource + " - accessesList " + accessesList.get(i).getResource()); 
            if (resource.equals(accessesList.get(i).getResource())) {
                accessesList.remove(i);
                removed = true;
                i--;                
            }
        }
        return removed;
    }
    
    /**
     * The process activation time.
     */
    private int activationTime;
    
    /**
     * It returns the process activation time.
     *
     * @return the process activation time.
     */
    public int getActivationTime() {
        return activationTime;
    }
    
    /**
     * The process name.
     */
    private String name = "";
    
    /**
     * It returns the process name.
     *
     * @return the process name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * The process execution time.
     */
    private int executionTime;
    
    /**
     * It returns the process execution time.
     *
     * @return the process execution time.
     */
    public int getExecutionTime() {
        return executionTime;
    }
    
    /**
     * The process initial priority. If no changing occurs, all processes will
     * have priority equals to 1. This priority is assigned when the process is
     * created and never can change in the future;
     */
    private int initialPriority;
    
    /**
     * It returns the process initial priority.
     *
     * @return The process initial priority.
     */
    public int getInitialPriority() {
        return initialPriority;
    }
    
    /**
     * It creates a process.
     *
     * @param name
     *            The process name
     * @param activationTime
     *            The process activation time
     * @param executionTime
     *            The process execution time
     * @param initialPriority
     *            The process initial priority.
     * @see Id#returnNewId()
     */
    public SimulatedProcess(String name, int activationTime, int executionTime,
            int initialPriority) {
        id = new Integer(Id.returnNewId()); // get the new id (unique)
        this.name = name;
        this.activationTime = activationTime;
        this.executionTime = executionTime;
        this.initialPriority = initialPriority;
    }
    
    /**
     * It returns true if both processes have the same id, false otherwise.
     *
     * @param process
     *            The second process.
     * @return true if they are the same id, false otherwise.
     */
    public boolean equals(Object process) {
        SimulatedProcess proc;
        if (process instanceof SimulatedProcess) {
            proc = (SimulatedProcess) process;
            if (this.id == proc.getId())
                return true;
        }
        return false;
    }
    
    /**
     * It converces the process to a string.
     *
     * @return the process name.
     */
    public String toString() {
        return name;
    }
    
    /**
     * It sets the activation time of the process.
     *
     * @param activationTime
     *            the activation time of the process.
     */
    public void setActivationTime(int activationTime) {
        this.activationTime = activationTime;
    }
    
    /**
     * It sets the execution time of the process.
     *
     * @param executionTime
     *            the execution time of the process.
     */
    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }
    
    /**
     * It sets the initial priority of the process.
     *
     * @param initialPriority
     *            the initial priority of the process.
     */
    public void setInitialPriority(int initialPriority) {
        this.initialPriority = initialPriority;
    }
    
    /**
     * It sets the name of the process.
     *
     * @param name
     *            the name of the process.
     */
    public void setName(String name) {
        this.name = name;
    }
}
