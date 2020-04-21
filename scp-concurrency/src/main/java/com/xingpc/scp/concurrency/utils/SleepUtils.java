package com.xingpc.scp.concurrency.utils;

import java.util.concurrent.TimeUnit;

/**
 * @Author: XingPc
 * @Description: 休眠时间
 * @Date: 2020/3/11 16:24
 * @Version: 1.0
 */
public class SleepUtils {

    public static final void second(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
        }
    }

}
