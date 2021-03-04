package com.xingpc.scp.concurrency.thread;

import org.junit.Test;

/**
 * 
 * @Author: XingPc
 * @Description: 线程阻塞
 * @Date: 2021年3月3日 下午4:43:02
 * @Version: 1.0
 */
public class ThreadBlock {
	
	@Test
	public void testBlock() throws InterruptedException {
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				method1();
			}
		}, "a");
		
		Thread thread2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				method1();
			}
		}, "b");
		thread1.start();
		Thread.sleep(1000L);
		thread2.start();
		System.out.println(thread1.getName() + "-----" + thread1.getState());
		System.out.println(thread2.getName() + "-----" + thread2.getState());
	}
	
	private synchronized void method1() {
		try {
			Thread.sleep(2000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
