package com.xcdh.target.util.idgen;

import java.util.concurrent.ConcurrentHashMap;

public class TestGenerator {

    public static ConcurrentHashMap<Long, Integer> map = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 1000; i++) {
            new Thread(new TestCla()).start();
        }
    }

    private static class TestCla implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int clusterId = 30;
            String sentinelHost = "192.168.96.48:27002";
            String masterName = "master-7002";

            try {
                IdgenUtil.init("test123", clusterId, sentinelHost, masterName);
                for (int i = 0; i < 1000; i++) {
                    long a = IdgenUtil.generateCombine(91);
                    map.put(a, 1);
                }

                System.out.println(map.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
