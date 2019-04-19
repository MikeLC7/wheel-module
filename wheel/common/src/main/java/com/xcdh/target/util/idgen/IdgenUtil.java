package com.xcdh.target.util.idgen;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IdgenUtil {

    private static IdGenerator idc = null;
    private static Map<Integer, IdGenerator> idcs = new ConcurrentHashMap<>();

    public static void init(String appUniqName, int clusterId, String sentinelHost, String masterName) throws Exception {
        synchronized (IdgenUtil.class) {
            if (null == Worker.workerId)
                Worker.initWorkerId(appUniqName, clusterId, sentinelHost, masterName);

            if (null == idc)
                idc = new IdGenerator();
        }
    }

    public static long generate() throws Exception {
        if (null == Worker.workerId)
            throw new Exception("workerId not init please run IdgenUtil.init before");

        if (null != idc)
            return idc.nextId();

        synchronized (IdgenUtil.class) {
            if (null == idc)
                idc = new IdGenerator();

            return idc.nextId();
        }
    }

    public static long generate(int businessId) throws Exception {
        if (null == Worker.workerId)
            throw new Exception("workerId not init please run IdgenUtil.init before");

        IdGenerator i = idcs.get(businessId);
        if (null != i)
            return i.nextId();

        synchronized (IdgenUtil.class) {
            IdGenerator j = idcs.get(businessId);
            if (null == j) {
                j = new IdGenerator();
                idcs.put(businessId, j);
            }

            return j.nextId();
        }
    }

    /**
     * 生成ID并把业务ID拼在前面
     *
     * @param businessId
     * @return
     */
    public static long generateCombine(int businessId) throws Exception {
        if (null == Worker.workerId)
            throw new Exception("workerId not init please run IdgenUtil.init before");

        if (businessId >= 92 || businessId < 0)
            throw new Exception("businessId can't be greater than 91 or less than 0");

        return businessId * 100000000000000000L + generate(businessId);
    }
}

