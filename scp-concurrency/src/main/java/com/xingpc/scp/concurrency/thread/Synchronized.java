package com.xingpc.scp.concurrency.thread;

/**
 * @Author: XingPc
 * @Description: 同步:valitile synchronized
 * @Date: 2020/3/11 18:45
 * @Version: 1.0
 */
public class Synchronized {

    public static void main(String[] args) {
        // 对Synchronized Class对象进行加锁
        synchronized (Synchronized.class) {
        }

        // 静态同步方法，对Synchronized Class对象进行加锁
        m();
    }
    public static synchronized void m() {
    }

}
