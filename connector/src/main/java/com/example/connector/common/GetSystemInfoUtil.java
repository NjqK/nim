package com.example.connector.common;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

/**
 * @author kuro
 * @version V1.0
 * @date 4/26/20 9:07 AM
 **/
public class GetSystemInfoUtil {

//    public static void main(String[] args) {
//        System.out.println(getOsName());
//        System.out.println(getSystemCpuLoad());
//        System.out.println(getProcessCpuLoad());
//        System.out.println(getTotalMemorySize());
//        System.out.println(getFreePhysicalMemorySize());
//        System.out.println(getUsedMemory());
//        long maxMem = Runtime.getRuntime().maxMemory()/1024/1024;
//        long freeMem = Runtime.getRuntime().freeMemory()/1024/1024;
//        long usedMem = maxMem - freeMem;
//        System.out.println(maxMem+"_"+freeMem+"_"+usedMem);
//    }


    public static long getAvailableMem() {
        long freeMem = Runtime.getRuntime().freeMemory() / 1024 / 1024;
        System.out.println(freeMem);
        return freeMem;
    }

    /**
     * 获取操作系统名称
     *
     * @return
     */
    public static String getOsName() {
        // 操作系统
        String osName = System.getProperty("os.name");
        return osName;
    }

    /**
     * 获取系统cpu负载
     *
     * @return
     */
    public static double getSystemCpuLoad() {
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();
        double SystemCpuLoad = osmxb.getSystemCpuLoad();
        return SystemCpuLoad;
    }

    /**
     * 获取jvm线程负载
     *
     * @return
     */
    public static double getProcessCpuLoad() {
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();
        double ProcessCpuLoad = osmxb.getProcessCpuLoad();
        return ProcessCpuLoad;
    }

    /**
     * 获取总的物理内存
     *
     * @return
     */
    public static long getTotalMemorySize() {
        int kb = 1024;
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();
        // 总的物理内存
        long totalMemorySize = osmxb.getTotalPhysicalMemorySize() / kb;
        return totalMemorySize;
    }

    /**
     * 获取free的物理内存
     *
     * @return
     */
    public static long getFreePhysicalMemorySize() {
        int kb = 1024;
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();
        // 剩余的物理内存
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / kb;
        return freePhysicalMemorySize;
    }

    /**
     * 获取已使用的物理内存
     *
     * @return
     */
    public static long getUsedMemory() {
        int kb = 1024;
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();
        //已使用的物理内存
        long usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / kb;
        return usedMemory;
    }
}