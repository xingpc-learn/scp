package com.xingpc.scp.concurrency.lock;

import org.junit.Test;

/**
 * 
 * @Author: XingPc
 * @Description: 对象锁
 * @Date: 2021年3月3日 下午5:35:57
 * @Version: 1.0
 */
public class ThreadLock {
	
	private static Object lock = new Object();
	
	@Test
	public void testLock() throws InterruptedException {
		Thread thread1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				synchronized (lock) {
					for (int i = 0; i < 50; i++) {
						System.out.println("a" + "----" + i);
					}
				}
			}
		});
		
		Thread thread2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				synchronized (lock) {
					for (int i = 0; i < 50; i++) {
						System.out.println("b" + "----" + i);
					}
				}
			}
		});
		
		thread1.start();
		Thread.sleep(1000L);
		thread2.start();
	}

}
