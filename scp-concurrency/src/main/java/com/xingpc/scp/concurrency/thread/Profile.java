package com.xingpc.scp.concurrency.thread;

import java.util.concurrent.TimeUnit;

/**
 * @Author: XingPc
 * @Description: ThreadLocal 对象，以ThreadLocal为对象，任意对象为值的结构
 * @Date: 2020/3/12 20:06
 * @Version: 1.0
 */
public class Profile {

    // 第一次get()方法调用时会进行初始化（如果set方法没有调用），每个线程会调用一次
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new ThreadLocal<Long>() {
        @Override
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };
    public static final void begin() {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }
    public static final long end() {
        return System.currentTimeMillis() - TIME_THREADLOCAL.get();
    }
    public static void main(String[] args) throws Exception {
        Profile.begin();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Cost: " + Profile.end() + " mills");
    }

}
