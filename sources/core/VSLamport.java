package core;

public class VSLamport {
    private long time;
    private long lamportTime;

    public VSLamport(long time, long lamportTime) {
        this.time = time;
        this.lamportTime = lamportTime;
    }

    public long getTime() {
        return time;
    }

    public long getLamportTime() {
        return lamportTime;
    }
}
