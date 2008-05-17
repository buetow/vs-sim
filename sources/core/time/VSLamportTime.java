package core.time;

public class VSLamportTime implements VSTime {
    private long globalTime;
    private long lamportTime;

    public VSLamportTime(long globalTime, long lamportTime) {
        this.globalTime = globalTime;
        this.lamportTime = lamportTime;
    }

    public long getGlobalTime() {
        return globalTime;
    }

    public long getLamportTime() {
        return lamportTime;
    }

    public String toString() {
        return "(" + lamportTime + ")";
    }
}
