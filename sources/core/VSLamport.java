package core;

public class VSLamport implements VSTime {
    private long globalTime;
    private long lamportTime;

    public VSLamport(long globalTime, long lamportTime) {
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
