package com.zyplayer.doc.data.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类
 *
 * @author 离狐千慕
 * @since 2023-10-06
 */
public class ThreadPoolUtil {
	private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 100, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
	
	/**
	 * 获取线程池
	 */
	public static ThreadPoolExecutor getThreadPool() {
		return threadPoolExecutor;
	}
}
