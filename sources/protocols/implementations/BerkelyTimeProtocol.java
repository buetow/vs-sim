/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package protocols.implementations;

import protocols.VSAbstractProtocol;
import core.VSMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * The Class BerkelyTimeProtocol.
 */
public class BerkelyTimeProtocol extends VSAbstractProtocol {
    private static final long serialVersionUID = 1L;

    /* Berkely Server variables */

    /** Integer: Process ID, Long: Local time of the process */
    private HashMap<Integer,Long> processTimes = new HashMap<Integer,Long>();

    /** Integer: Process ID, Long: Time of receiving the response from the process */
    private HashMap<Integer,Long> recvTimes = new HashMap<Integer,Long>();

    /** Integer: Process ID, Long: Calculated process times (using the RTT) */
    private HashMap<Integer,Long> realTimesRTT = new HashMap<Integer,Long>();

    /** Contains all process IDs of processes which want to justify their time */
    private ArrayList<Integer> peers = new ArrayList<Integer>();

    /** Time the request/response has started */
    private long requestTime;

    /**
     * Instantiates a new berkely time protocol.
     */
    public BerkelyTimeProtocol() {
        setClassname(getClass().toString());

        /* Those prefs are editable through the VSAbstractProtocol VSAbstractEditor GUI. */
        Vector<Integer> vec = new Vector<Integer>();
        vec.add(2);
        vec.add(3);
        initVector("processPIDs", vec, "PIDs beteiliger Prozesse");
    }

    /* (non-Javadoc)
     * @see events.VSAbstractEvent#onInit()
     */
    protected void onInit() {
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientReset()
     */
    protected void onClientReset() {
        processTimes.clear();
        recvTimes.clear();
        realTimesRTT.clear();
        peers.clear();
        peers.addAll(getVector("processPIDs"));
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientStart()
     */
    protected void onClientStart() {
        peers.addAll(getVector("processPIDs"));
        requestTime = process.getTime();
        VSMessage message = new VSMessage();
        message.setBoolean("isRequest", true);
        sendMessage(message);
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientRecv(core.VSMessage)
     */
    protected void onClientRecv(VSMessage recvMessage) {
        /* Ignore all protocol messages which are not a response message, e.g. itself */
        if (!recvMessage.getBoolean("isResponse"))
            return;

        Integer processID = new Integer(recvMessage.getInteger("processID"));

        if (peers.contains(processID))
            peers.remove(processID);
        else
            return; /* Process has been handled already or is not listed */

        Long time = new Long(recvMessage.getLong("time"));

        processTimes.put(processID, time);
        recvTimes.put(processID, new Long(process.getTime()));

        /* All peers have told their times */
        if (peers.size() == 0) {
            long avgTime = calculateAverageTime();
            /* Set the local's process time to the new avg reference time */
            process.setTime(avgTime);
            /* Tell all other processes what to do in order to justify their times */
            sendJustifyRequests(avgTime);
            /* Start "clean" next time */
            onClientReset();
        }
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientSchedule()
     */
    protected void onClientSchedule() {
    }

    /**
     * Calculate the new average time.
     *
     * @return the long
     */
    private long calculateAverageTime() {
        long sum = 0;
        for (Integer processID : processTimes.keySet()) {
            Long localTime = processTimes.get(processID);
            Long recvTime = recvTimes.get(processID);
            long rtt = recvTime.longValue() - requestTime;
            long realProcessTime = localTime + (long) (rtt / 2);
            realTimesRTT.put(processID, new Long(realProcessTime));
            sum += realProcessTime;
        }
        /* Include the time of the local process */
        sum += process.getTime();
        return (long) sum / (getVector("processPIDs").size() + 1);
    }

    /**
     * Sends to all clients a value to justify their local clocks.
     *
     * @param avgTime the avg time
     */
    private void sendJustifyRequests(long avgTime) {
        for (Integer processID : processTimes.keySet()) {
            long realProcessTime = realTimesRTT.get(processID).longValue();
            long diff = avgTime - realProcessTime;
            VSMessage message = new VSMessage();
            message.setBoolean("isJustify", true);
            message.setLong("timeDiff", diff);
            message.setInteger("receiverProcessID", processID);
            sendMessage(message);
        }
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerReset()
     */
    protected void onServerReset() {
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerRecv(core.VSMessage)
     */
    protected void onServerRecv(VSMessage recvMessage) {
        if (recvMessage.getBoolean("isRequest")) {
            VSMessage message = new VSMessage();
            message.setInteger("processID", process.getProcessID());
            message.setLong("time", process.getTime());
            message.setBoolean("isResponse", true);
            sendMessage(message);

        } else if (recvMessage.getBoolean("isJustify")) {
            /* Check if it's "my" justify message */
            if (recvMessage.getInteger("receiverProcessID") != process.getProcessID())
                return;

            long timeDiff = recvMessage.getLong("timeDiff");
            long recvTime = process.getTime();
            long newTime = process.getTime() + timeDiff;
            logg("Neue Zeit: " + newTime);

            process.setTime(newTime);
        }
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerSchedule()
     */
    protected void onServerSchedule() {
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#toString()
     */
    public String toString() {
        return super.toString();
    }
}
