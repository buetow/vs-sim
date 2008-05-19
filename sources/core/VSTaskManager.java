package core;

import java.util.*;

import protocols.*;
import prefs.*;
import utils.*;

public class VSTaskManager {
    private PriorityQueue<VSTask> tasks;
    private PriorityQueue<VSTask> globalTasks;
    private LinkedList<VSTask> fullfilledProgrammedTasks;
    public final static boolean PROGRAMMED = true;
    public final static boolean ONLY_ONCE = false;
    private VSPrefs prefs;

    public VSTaskManager(VSPrefs prefs) {
        this.prefs = prefs;
        //Comparator<VSTask> comparator = createComparator();
        this.tasks = new PriorityQueue<VSTask>();//100, comparator);
        this.globalTasks = new PriorityQueue<VSTask>();//(100, comparator);
        this.fullfilledProgrammedTasks = new LinkedList<VSTask>();
    }

	/*
    private Comparator<VSTask> createComparator() {
        return new Comparator<VSTask>() {
            public int compare(VSTask a, VSTask b) {
                if (a.getTaskTime() > b.getTaskTime())
                    return 1;

                if (a.getTaskTime() < b.getTaskTime())
                    return -1;

                return 0;
            }
        };
    }
	*/

    public synchronized void runTasks(final long step, final long offset, final long lastGlobalTime) {
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

                if (process.isCrashed() && !task.isProcessRecoverEvent()) {
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

                } else /* if (globalOffsetTime > taskTime) */ {
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
                        process.addClockOffset(process.getTime()-(offsetTime-diff));
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

                if (process.isCrashed() && !task.isProcessRecoverEvent()) {
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

                } else /* if (offsetTime > taskTime) */ {
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
                        process.addClockOffset(process.getTime()-(offsetTime-diff));
                    process.setLocalTime(localTime);
                }

                if (task.isProgrammed())
                    fullfilledProgrammedTasks.add(task);
            }

        } while (redo);
    }

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

    private void insert(VSTask task) {
		if (task.timeOver())  
			fullfilledProgrammedTasks.addLast(task);

		else if (task.isGlobalTimed())
            globalTasks.add(task);

        else
            tasks.add(task);
    }

    public void addTask(VSTask task) {
        addTask(task, VSTaskManager.ONLY_ONCE);
    }

    public synchronized void addTask(VSTask task, boolean isProgrammed) {
        task.isProgrammed(isProgrammed);
        insert(task);
    }

    public synchronized boolean removeTask(VSTask task) {
        if (fullfilledProgrammedTasks.remove(task))
            return true;

        else if (task.isGlobalTimed() && globalTasks.remove(task))
            return true;

        else if (!task.isGlobalTimed() && tasks.remove(task))
            return true;

        return false;
    }

	/*
    public synchronized LinkedList<VSTask> getProtocolTasks(VSProtocol protocol) {
        LinkedList<VSTask> protocolTasks = new LinkedList<VSTask>();

        for (VSTask task : fullfilledProgrammedTasks)
            if (task.isProtocol(protocol))
                protocolTasks.addLast(task);

        for (VSTask task : globalTasks)
            if (task.isProtocol(protocol))
                protocolTasks.addLast(task);

        for (VSTask task : tasks)
            if (task.isProtocol(protocol))
                protocolTasks.addLast(task);

        return protocolTasks;
    }

    public synchronized LinkedList<VSTask> getNonProtocolTasks(VSProtocol protocol) {
        LinkedList<VSTask> nonProtocolTasks = new LinkedList<VSTask>();

        for (VSTask task : fullfilledProgrammedTasks)
            if (!task.isProtocol(protocol))
                nonProtocolTasks.addLast(task);

        for (VSTask task : globalTasks)
            if (!task.isProtocol(protocol))
                nonProtocolTasks.addLast(task);

        for (VSTask task : tasks)
            if (!task.isProtocol(protocol))
                nonProtocolTasks.addLast(task);

        return nonProtocolTasks;
    }

    public synchronized void modifyProtocolTasks(VSProtocol protocol, LinkedList<VSTask> protocolTasks) {
        VSProcess process = protocol.getProcess();
        LinkedList<VSTask> nonProtocolTasks = getNonProtocolTasks(protocol);
        ListIterator<VSTask> iter1 = protocolTasks.listIterator(0);
        ListIterator<VSTask> iter2 = nonProtocolTasks.listIterator(0);
        long localTime = process.getTime();

        fullfilledProgrammedTasks.clear();
        globalTasks.clear();
        tasks.clear();

        for (VSTask task : nonProtocolTasks) {
            if (task.getTaskTime() < localTime)
                fullfilledProgrammedTasks.addLast(task);
            else
                insert(task);
        }

        for (VSTask task : protocolTasks) {
            if (task.getTaskTime() < localTime)
                fullfilledProgrammedTasks.addLast(task);
            else
                insert(task);
        }
    }
*/
    public synchronized VSPriorityQueue<VSTask> getLocalTasks() {
        VSPriorityQueue<VSTask> processTasks = new VSPriorityQueue<VSTask>();

        for (VSTask task : fullfilledProgrammedTasks)
            if (!task.isGlobalTimed() && task.isProgrammed())
                processTasks.add(task);

        for (VSTask task : tasks)
            if (task.isProgrammed())
                processTasks.add(task);

        return processTasks;
    }

    public synchronized VSPriorityQueue<VSTask> getGlobalTasks() {
        VSPriorityQueue<VSTask> processTasks = new VSPriorityQueue<VSTask>();

        for (VSTask task : fullfilledProgrammedTasks)
            if (task.isGlobalTimed() && task.isProgrammed())
                processTasks.add(task);

        for (VSTask task : globalTasks)
            if (task.isProgrammed())
                processTasks.add(task);

        return processTasks;
    }

    public synchronized VSPriorityQueue<VSTask> getProcessLocalTasks(VSProcess process) {
        VSPriorityQueue<VSTask> processTasks = new VSPriorityQueue<VSTask>();

        for (VSTask task : fullfilledProgrammedTasks)
            if (!task.isGlobalTimed() && task.isProcess(process) && task.isProgrammed())
                processTasks.add(task);

        for (VSTask task : tasks)
            if (task.isProcess(process) && task.isProgrammed())
                processTasks.add(task);

        return processTasks;
    }

    public synchronized VSPriorityQueue<VSTask> getProcessGlobalTasks(VSProcess process) {
        VSPriorityQueue<VSTask> processTasks = new VSPriorityQueue<VSTask>();

        for (VSTask task : fullfilledProgrammedTasks)
            if (task.isGlobalTimed() && task.isProcess(process) && task.isProgrammed())
                processTasks.add(task);

        for (VSTask task : globalTasks)
            if (task.isProcess(process) && task.isProgrammed())
                processTasks.add(task);

        return processTasks;
    }

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
