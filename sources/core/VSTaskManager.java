/*
 * Copyright (c) 2008 Paul C. Buetow, vs@dev.buetow.org
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * All icons of the icons/ folder are 	under a Creative Commons 
 * Attribution-Noncommercial-Share Alike License a CC-by-nc-sa. 
 * 
 * The icon's homepage is http://code.google.com/p/ultimate-gnome/
 */

package core;

import java.util.*;

import prefs.*;
import utils.*;

/**
 * The class VSTaskManager. The task manager is responsible that all tasks
 * will get fullfilled in the correct order. Please also read the javadoc
 * of the VSTask class. It describes the difference between local and global
 * timed tasks.
 *
 * @author Paul C. Buetow
 */
public class VSTaskManager {
    /** The seriao version uid */
    private static final long serialVersionUID = 1L;

    /** The tasks. */
    private PriorityQueue<VSTask> tasks;

    /** The global tasks. */
    private PriorityQueue<VSTask> globalTasks;

    /** The fullfilled programmed tasks. */
    private LinkedList<VSTask> fullfilledProgrammedTasks;

    /** The Constant PROGRAMMED. */
    public final static boolean PROGRAMMED = true;

    /** The Constant ONLY_ONCE. */
    public final static boolean ONLY_ONCE = false;

    /** The simulator's default prefs. */
    private VSPrefs prefs;

    /**
     * Instantiates a new lang.process.removetask manager.
     *
     * @param prefs the simulator's default prefs
     */
    public VSTaskManager(VSPrefs prefs) {
        this.prefs = prefs;
        this.tasks = new PriorityQueue<VSTask>();
        this.globalTasks = new PriorityQueue<VSTask>();
        this.fullfilledProgrammedTasks = new LinkedList<VSTask>();
    }

    /**
     * Run tasks. This method gets called by the simulation canvas repeatedly.
     * Almost all simulation actions take place in this method.
     *
     * @param step the step
     * @param offset the offset
     * @param lastGlobalTime the last global time
     */
    public synchronized void runTasks(long step, long offset,
                                      long lastGlobalTime) {
        VSTask task = null;
        VSProcess process = null;
        long localTime;
        long offsetTime;
        long taskTime;
        long globalTime;
        final long globalOffsetTime = lastGlobalTime + step;
        boolean redo;

        do {
            redo = false;

            /* Run tasks which have for its schedule the global time */
            while (globalTasks.size() != 0) {
                task = globalTasks.peek();
                process = task.getProcess();
                localTime = process.getTime();
                offsetTime = localTime + step;
                taskTime = task.getTaskTime();
                globalTime = process.getGlobalTime();

                if (globalOffsetTime < taskTime)
                    break;

                globalTasks.poll();
                redo = true;

                if (process.isCrashed() && !task.isVSProcessRecoverEvent()) {
                    if (task.isProgrammed())
                        fullfilledProgrammedTasks.add(task);
                    continue;
                }

                if (globalOffsetTime == taskTime) {
                    process.setGlobalTime(globalOffsetTime);
                    process.setLocalTime(offsetTime);
                    process.timeModified(false);
                    task.run();
                    process.setGlobalTime(globalTime);
                    if (process.isCrashed())
                        process.addClockOffset(step);
                    if (process.timeModified())
                        process.addClockOffset(process.getTime()-offsetTime);
                    process.setLocalTime(localTime);

                } else { /* if (globalOffsetTime > taskTime) */
                    final long diff = globalOffsetTime - taskTime;
                    if (globalOffsetTime - diff < lastGlobalTime)
                        process.setGlobalTime(lastGlobalTime);
                    else
                        process.setGlobalTime(globalOffsetTime - diff);
                    process.setLocalTime(offsetTime - diff);
                    process.timeModified(false);
                    task.run();
                    process.setGlobalTime(globalTime);
                    if (process.isCrashed())
                        process.addClockOffset(step);
                    if (process.timeModified())
                        process.addClockOffset(process.getTime()-
                                               (offsetTime-diff));
                    process.setLocalTime(localTime);
                }

                if (task.isProgrammed())
                    fullfilledProgrammedTasks.add(task);
            }

            /* Run tasks which have for its schedule the local process times */
            while (tasks.size() != 0) {
                task = tasks.peek();
                process = task.getProcess();
                localTime = process.getTime();
                offsetTime = localTime + step;
                taskTime = task.getTaskTime();
                globalTime = process.getGlobalTime();

                if (offsetTime < taskTime)
                    break;

                tasks.poll();
                redo = true;

                if (process.isCrashed() && !task.isVSProcessRecoverEvent()) {
                    if (task.isProgrammed())
                        fullfilledProgrammedTasks.add(task);
                    continue;
                }

                if (offsetTime == taskTime) {
                    process.setGlobalTime(globalOffsetTime);
                    process.setLocalTime(offsetTime);
                    process.timeModified(false);
                    task.run();
                    process.setGlobalTime(globalTime);
                    if (process.timeModified())
                        process.addClockOffset(process.getTime()-offsetTime);
                    process.setLocalTime(localTime);

                } else { /* if (offsetTime > taskTime) */
                    final long diff = offsetTime - taskTime;
                    if (globalOffsetTime - diff < lastGlobalTime)
                        process.setGlobalTime(lastGlobalTime);
                    else
                        process.setGlobalTime(globalOffsetTime - diff);
                    process.setLocalTime(offsetTime - diff);
                    process.timeModified(false);
                    task.run();
                    process.setGlobalTime(globalTime);
                    if (process.timeModified())
                        process.addClockOffset(process.getTime()-
                                               (offsetTime-diff));
                    process.setLocalTime(localTime);
                }

                if (task.isProgrammed())
                    fullfilledProgrammedTasks.add(task);
            }

        } while (redo);
    }

    /**
     * Resets the task manager.
     */
    public synchronized void reset() {
        PriorityQueue<VSTask> tmp = tasks;
        PriorityQueue<VSTask> tmp2 = globalTasks;
        tasks = new PriorityQueue<VSTask>();
        globalTasks = new PriorityQueue<VSTask>();

        while (fullfilledProgrammedTasks.size() != 0) {
            VSTask task = fullfilledProgrammedTasks.removeFirst();
            if (task.isProgrammed())
                insert(task);
        }

        while (tmp.size() != 0) {
            VSTask task = tmp.poll();
            if (task.isProgrammed())
                insert(task);
        }

        while (tmp2.size() != 0) {
            VSTask task = tmp2.poll();
            if (task.isProgrammed())
                insert(task);
        }
    }

    /**
     * Inserts a task. Only for internal usage. Use the add methods instead.
     * This method checks if the task to insert is a global or a local timed
     * task. And it also checks if the task's time is over already.
     *
     * @param task the task to insert
     */
    private void insert(VSTask task) {
        if (task.timeOver())
            fullfilledProgrammedTasks.addLast(task);

        else if (task.isGlobalTimed())
            globalTasks.add(task);

        else
            tasks.add(task);
    }

    /**
     * Adds a task.
     *
     * @param task the task to add
     */
    public void addTask(VSTask task) {
        addTask(task, VSTaskManager.ONLY_ONCE);
    }

    /**
     * Adds a task.
     *
     * @param task the task to add
     * @param isProgrammed true, if the task is programmed
     */
    public synchronized void addTask(VSTask task, boolean isProgrammed) {
        task.isProgrammed(isProgrammed);
        insert(task);
    }

    /**
     * Removes a task.
     *
     * @param task the task to remove
     *
     * @return true, if the task has been removed with success
     */
    public synchronized boolean removeTask(VSTask task) {
        if (fullfilledProgrammedTasks.remove(task))
            return true;

        else if (task.isGlobalTimed() && globalTasks.remove(task))
            return true;

        else if (!task.isGlobalTimed() && tasks.remove(task))
            return true;

        return false;
    }

    /**
     * Removes several tasks.
     *
     * @param tasks the tasks to remove
     */
    public void removeAllTasks(ArrayList<VSTask> tasks) {
        for (VSTask task : tasks)
            removeTask(task);
    }

    /**
     * Removes the tasks of the specified process.
     *
     * @param process the process to remove the tasks of
     */
    public synchronized void removeTasksOf(VSProcess process) {
        ArrayList<VSTask> removeThose = new ArrayList<VSTask>();

        for (VSTask task : fullfilledProgrammedTasks)
            if (task.isProcess(process))
                removeThose.add(task);

        for (VSTask task : removeThose)
            fullfilledProgrammedTasks.remove(task);

        removeThose.clear();

        for (VSTask task : globalTasks)
            if (task.isProcess(process))
                removeThose.add(task);

        for (VSTask task : removeThose)
            globalTasks.remove(task);

        removeThose.clear();

        for (VSTask task : tasks)
            if (task.isProcess(process))
                removeThose.add(task);

        for (VSTask task : removeThose)
            tasks.remove(task);
    }

    /**
     * Gets the local timed tasks.
     *
     * @return the local timed tasks
     */
    public synchronized VSPriorityQueue<VSTask> getLocalTasks() {
        VSPriorityQueue<VSTask> localTasks = new VSPriorityQueue<VSTask>();

        for (VSTask task : fullfilledProgrammedTasks)
            if (!task.isGlobalTimed() && task.isProgrammed())
                localTasks.add(task);

        for (VSTask task : tasks)
            if (task.isProgrammed())
                localTasks.add(task);

        return localTasks;
    }

    /**
     * Gets the global timed tasks.
     *
     * @return the global timed tasks
     */
    public synchronized VSPriorityQueue<VSTask> getGlobalTasks() {
        VSPriorityQueue<VSTask> globalTasks = new VSPriorityQueue<VSTask>();

        for (VSTask task : fullfilledProgrammedTasks)
            if (task.isGlobalTimed() && task.isProgrammed())
                globalTasks.add(task);

        for (VSTask task : globalTasks)
            if (task.isProgrammed())
                globalTasks.add(task);

        return globalTasks;
    }

    /**
     * Gets the local timed tasks of a specific process.
     *
     * @param process the process to get the local timed tasks of
     *
     * @return the local tasks of the specified process
     */
    public synchronized VSPriorityQueue<VSTask> getProcessLocalTasks(
        VSProcess process) {
        VSPriorityQueue<VSTask> processTasks = new VSPriorityQueue<VSTask>();

        for (VSTask task : fullfilledProgrammedTasks)
            if (!task.isGlobalTimed() && task.isProcess(process) &&
                    task.isProgrammed())
                processTasks.add(task);

        for (VSTask task : tasks)
            if (task.isProcess(process) && task.isProgrammed())
                processTasks.add(task);

        return processTasks;
    }

    /**
     * Gets the global timed tasks of a specific process.
     *
     * @param process the process to get the local timed tasks of
     *
     * @return the global timed tasks of the specified process
     */
    public synchronized VSPriorityQueue<VSTask> getProcessGlobalTasks(
        VSProcess process) {
        VSPriorityQueue<VSTask> processTasks = new VSPriorityQueue<VSTask>();

        for (VSTask task : fullfilledProgrammedTasks)
            if (task.isGlobalTimed() && task.isProcess(process) &&
                    task.isProgrammed())
                processTasks.add(task);

        for (VSTask task : globalTasks)
            if (task.isProcess(process) && task.isProgrammed())
                processTasks.add(task);

        return processTasks;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(prefs.getString("lang.task.manager"));
        buffer.append(" (");
        buffer.append(prefs.getString("lang.tasks.fullfilled"));
        buffer.append(": ");

        for (VSTask task : fullfilledProgrammedTasks) {
            buffer.append(task);
            buffer.append("; ");
        }

        buffer.append(prefs.getString("lang.tasks.global"));

        for (VSTask task : globalTasks) {
            buffer.append(task);
            buffer.append("; ");
        }

        buffer.append(prefs.getString("lang.tasks.local"));

        for (VSTask task : tasks) {
            buffer.append(task);
            buffer.append("; ");
        }

        String descr = buffer.toString();

        if (descr.endsWith("; "))
            return descr.substring(0, descr.length()-2) + ")";

        return descr + ")";
    }
}
