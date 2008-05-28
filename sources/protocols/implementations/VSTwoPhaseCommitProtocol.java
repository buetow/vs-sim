/*
 * VS is (c) 2008 by Paul C. Buetow
 * vs@dev.buetow.org
 */
package protocols.implementations;

import java.util.ArrayList;
import java.util.Vector;

import protocols.VSAbstractProtocol;
import core.VSMessage;

/**
 * The Class VSTwoPhaseCommitProtocol.
 */
public class VSTwoPhaseCommitProtocol extends VSAbstractProtocol {
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a one phase commit protocol.
     */
    public VSTwoPhaseCommitProtocol() {
        super(VSAbstractProtocol.HAS_ON_SERVER_START);
        setClassname(getClass().toString());
    }

    /** PIDs of all processes which still have to vote */
    private ArrayList<Integer> votePids;

    /** PIDs of all processes which have to acknowledge that they recv the global vote result */
    private ArrayList<Integer> ackPids;

    /** The gloal vote result */
    private boolean voteResult;

    /* (non-Javadoc)
     * @see events.VSAbstractProtocol#onServerInit()
     */
    public void onServerInit() {
        Vector<Integer> vec = new Vector<Integer>();
        vec.add(2);
        vec.add(3);

        initVector("pids", vec, "PIDs beteilitger Prozesse");
        initLong("timeout", 2500, "Zeit bis erneuerter Anfrage", "ms");
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerReset()
     */
    public void onServerReset() {
        if (votePids != null) {
            voteResult = true;
            votePids.clear();
            votePids.addAll(getVector("pids"));
            ackPids.clear();
            ackPids.addAll(getVector("pids"));
        }
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerStart()
     */
    public void onServerStart() {
        if (votePids == null) {
            voteResult = true;
            votePids = new ArrayList<Integer>();
            votePids.addAll(getVector("pids"));
            ackPids = new ArrayList<Integer>();
            ackPids.addAll(getVector("pids"));
        }

        if (votePids.size() != 0) {
            long timeout = getLong("timeout") + process.getTime();
            scheduleAt(timeout); /* Will run onServerSchedule() at the specified local time */

            VSMessage message = new VSMessage();
            message.setBoolean("wantVote", true);
            sendMessage(message);

        } else if (ackPids.size() != 0) {
            long timeout = getLong("timeout") + process.getTime();
            scheduleAt(timeout); /* Will run onServerSchedule() at the specified local time */

            VSMessage message = new VSMessage();
            message.setBoolean("isVoteResult", true);
            message.setBoolean("voteResult", voteResult);
            sendMessage(message);
        }
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerRecv(core.VSMessage)
     */
    public void onServerRecv(VSMessage recvMessage) {
        if (votePids.size() != 0 && recvMessage.getBoolean("isVote")) {
            Integer pid = recvMessage.getIntegerObj("pid");
            if (votePids.contains(pid))
                votePids.remove(pid);
            else
                return;

            boolean vote = recvMessage.getBoolean("vote");
            logg("Abstimmung von Prozess " + pid + " erhalten! Ergebnis: " + vote);

            if (!vote)
                voteResult = false;

            if (votePids.size() == 0) {
                logg("Abstimmungen von allen beteiligten Prozessen erhalten! Globales Ergebnis: " + voteResult);
                /* Remove the active schedule which has been created in the onServerStart method */
                removeSchedules();
                /* Create a new schedule and send the vote result */
                onServerStart();
            }
        } else if (ackPids.size() != 0 && recvMessage.getBoolean("isAck")) {
            Integer pid = recvMessage.getIntegerObj("pid");
            if (ackPids.contains(pid))
                ackPids.remove(pid);
            else
                return;

            if (ackPids.size() == 0) {
                /* Remove the active schedule which has been created in the onServerStart method */
                removeSchedules();
                logg("Alle Teilnehmer haben die Abstimmung erhalten");
            }
        }
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onServerSchedule()
     */
    public void onServerSchedule() {
        onServerStart();
    }

    /* Server variables */
    private boolean voteSent;
    private boolean myVote;

    /* (non-Javadoc)
     * @see events.VSAbstractProtocol#onClientInit()
     */
    public void onClientInit() {
        initInteger("ackProb", 50, "Festschreibw'keit", 0, 100, "%");
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientReset()
     */
    public void onClientReset() {
        voteSent = false;
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientRecv(core.VSMessage)
     */
    public void onClientRecv(VSMessage recvMessage) {
        if (recvMessage.getBoolean("wantVote")) {
            if (!voteSent) {
                voteSent = true;
                myVote = process.getRandomPercentage() <= getInteger("ackProb");
            }

            VSMessage message = new VSMessage();
            message.setBoolean("isVote", true);
            message.setBoolean("vote", myVote);
            message.setInteger("pid", process.getProcessID());
            sendMessage(message);

            logg("Abstimmung " + myVote + " versendet");

        } else if (recvMessage.getBoolean("isVoteResult")) {
            boolean voteResult = recvMessage.getBoolean("voteResult");
            logg("Globales Abstimmungsergebnis erhalten. Ergebnis: " + voteResult);

            VSMessage message = new VSMessage();
            message.setBoolean("isAck", true);
            message.setInteger("pid", process.getProcessID());
            sendMessage(message);
        }
    }

    /* (non-Javadoc)
     * @see protocols.VSAbstractProtocol#onClientSchedule()
     */
    public void onClientSchedule() {
    }
}