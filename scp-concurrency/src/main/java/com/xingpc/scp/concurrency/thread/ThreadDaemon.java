package com.xingpc.scp.concurrency.thread;

import com.xingpc.scp.concurrency.utils.SleepUtils;

/**
 * @Author: XingPc
 * @Description: 守护进程，在构建Daemon线程时，不能依靠finally块中的内容来确保执行关闭或清理资源
 * 的逻辑
 * @Date: 2020/3/11 17:01
 * @Version: 1.0
 */
public class ThreadDaemon {

    public static void main(String[] args) {
        Thread thread = new Thread(new DaemonRunner(), "DaemonRunner");
        thread.setDaemon(true);
        thread.start();
    }

    static class DaemonRunner implements Runnable {
        @Override
        public void run() {
            try {
                SleepUtils.second(10);
            } finally {
                System.out.println("DaemonThread finally run.");
            }
        }
    }

}
