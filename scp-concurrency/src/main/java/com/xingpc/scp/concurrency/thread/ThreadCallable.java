package com.xingpc.scp.concurrency.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * 
 * @Author: XingPc
 * @Description: 使用Callable接口实现多线程，带返回值
 * @Date: 2021年3月3日 上午10:47:23
 * @Version: 1.0
 */
public class ThreadCallable {
	
	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool();
		Task task = new Task();
//		Future<Integer> result = executor.submit(task);
		FutureTask<Integer> futureTask = new FutureTask<>(task);
		executor.submit(futureTask);
		// get调用会阻塞当前线程，直到获得结果
		// 所以实际使用时使用带有超时时间的get重载方法
		try {
//			System.out.println(result.get());
//			System.out.println(result.isCancelled());
//			System.out.println(result.isDone());
			System.out.println(futureTask.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	static class Task implements Callable<Integer> {

		@Override
		public Integer call() throws Exception {
			Thread.sleep(1000);
			return 2;
		}
		
	}

}
