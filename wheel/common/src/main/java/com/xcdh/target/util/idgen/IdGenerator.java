package com.xcdh.target.util.idgen;

public class IdGenerator {

    private long lastTimestamp = -1L;
    private long sequence = 0L;

    public synchronized long nextId() throws Exception {
        if (null == Worker.workerId)
            throw new Exception("workerId not init");

        long timestamp = timeGen();

        if (timestamp < lastTimestamp)
            throw new Exception("Clock moved backwards.  Refusing to generate id for " + (lastTimestamp - timestamp) + " milliseconds");

        if (lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1L & 0x1FF);
            if (this.sequence == 0L)
                timestamp = tilNextMillis(lastTimestamp);
        } else
            this.sequence = 0L;

        lastTimestamp = timestamp;

        long nextId = timestamp - 885120000000L << 14 | this.sequence << 5 | Worker.workerId;

        return nextId;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
}
