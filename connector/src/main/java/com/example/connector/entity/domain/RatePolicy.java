package com.example.connector.entity.domain;

/**
 * @author kuro
 * @version V1.0
 * @date 2020-04-27 1:45 PM
 **/
public class RatePolicy implements WeightPolicy {

    private int cpuRate;
    private int memRate;

    public RatePolicy(int cpuRate, int memRate) {
        this.cpuRate = cpuRate;
        this.memRate = memRate;
    }

    public static final RatePolicy DEFAULT = new RatePolicy(0, 1);

    @Override
    public long calculateWeight(ServiceLoad serviceLoad) {
        long cpuPoint = (int) (serviceLoad.getCpuLoad() * cpuRate);
        long freeMemMB = serviceLoad.getFreeMem() / 1024;
        long memPoint = freeMemMB * memRate;
        return cpuPoint + memPoint;
    }
}
