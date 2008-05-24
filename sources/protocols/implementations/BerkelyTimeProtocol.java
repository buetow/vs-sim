package protocols.implementations;

import protocols.VSProtocol;
import prefs.VSPrefs;
import core.VSMessage;

import java.util.HashMap;

public class BerkelyTimeProtocol extends VSProtocol {
    /* Berkely Server variables */

    /* Integer: Process ID, Long: Local time of the process */
    private HashMap<Integer,Long> processTimes = new HashMap<Integer,Long>();
    /* Integer: Process ID, Long: Time of receiving the response from the process */
    private HashMap<Integer,Long> recvTimes = new HashMap<Integer,Long>();
    /* Integer: Process ID, Long: Calculated process times (using the RTT) */
    private HashMap<Integer,Long> realTimesRTT = new HashMap<Integer,Long>();
    /* Time the request/response has started */
    private long requestTime;

    public BerkelyTimeProtocol() {
        setClassname(getClass().toString());

        /* Those prefs are editable through the VSProtocol VSEditor GUI. t_min and t_max in milliseconds  */
        setInteger("numProcesses", 0);
    }

    protected void onInit() {
    }

    protected void onClientReset() {
        processTimes.clear();
        recvTimes.clear();
        realTimesRTT.clear();
    }

    protected void onClientStart() {
        requestTime = process.getTime();
        VSMessage message = new VSMessage(getClassname());
        message.setBoolean("isRequest", true);
        sendMessage(message);
    }

    protected void onClientRecv(VSMessage recvMessage) {
        /* Ignore all protocol messages which are not a response message, e.g. itself */
        if (!recvMessage.getBoolean("isResponse"))
            return;

        Integer processID = new Integer(recvMessage.getInteger("processID"));
        Long time = new Long(recvMessage.getLong("time"));

        processTimes.put(processID, time);
        recvTimes.put(processID, new Long(process.getTime()));

        /* All processes have comitted the response */
        if (processTimes.size() == getInteger("numProcesses")) {
            long avgTime = calculateAverageTime();
            /* Set the local's process time to the new avg reference time */
            process.setTime(avgTime);
            /* Tell all other processes what to do in order to justify their times */
            sendJustifyRequests(avgTime);
            /* Start "clean" next time */
            onClientReset();
        }
    }

    /**
     * Calculate the new average time
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
        return (long) sum / (1 + getInteger("numProcesses"));
    }

    /**
     * Sends to all clients a value to justify their local clocks
     */
    private void sendJustifyRequests(long avgTime) {
        for (Integer processID : processTimes.keySet()) {
            long realProcessTime = realTimesRTT.get(processID).longValue();
            long diff = avgTime - realProcessTime;
            VSMessage message = new VSMessage(getClassname());
            message.setBoolean("isJustify", true);
            message.setLong("timeDiff", diff);
            message.setInteger("receiverProcessID", processID);
            sendMessage(message);
        }
    }

    protected void onServerReset() {
    }

    protected void onServerRecv(VSMessage recvMessage) {
        if (recvMessage.getBoolean("isRequest")) {
            VSMessage message = new VSMessage(getClassname());
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

    public String toString() {
        return super.toString();
    }
}
