package com.xingpc.scp.concurrency.thread;

import com.xingpc.scp.concurrency.utils.SleepUtils;

import java.util.concurrent.TimeUnit;

/**
 * @Author: XingPc
 * @Description: 线程中断，如果该线程已经处于终结状态，即使该线程被中断过，在调用该线程对象的isInterrupte()
 * 时依旧会返回false
 * @Date: 2020/3/11 17:26
 * @Version: 1.0
 */
public class ThreadInterrupted {

    public static void main(String[] args) throws Exception {
        // sleepThread不停的尝试睡眠
        Thread sleepThread = new Thread(new SleepRunner(), "SleepThread");
        sleepThread.setDaemon(true);
        // busyThread不停的运行
        Thread busyThread = new Thread(new BusyRunner(), "BusyThread");
        busyThread.setDaemon(true);
        sleepThread.start();
        busyThread.start();
        // 休眠5秒，让sleepThread和busyThread充分运行
        TimeUnit.SECONDS.sleep(5);
        sleepThread.interrupt();
        busyThread.interrupt();
        System.out.println("SleepThread interrupted is " + sleepThread.isInterrupted());
        System.out.println("BusyThread interrupted is " + busyThread.isInterrupted());
        // 防止sleepThread和busyThread立刻退出
        SleepUtils.second(2);
    }

    static class SleepRunner implements Runnable {
        @Override
        public void run() {
            while (true) {
                SleepUtils.second(10);
            }
        }
    }

    static class BusyRunner implements Runnable {
        @Override
        public void run() {
            while (true) {
            }
        }
    }

}
