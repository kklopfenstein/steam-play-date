package playdate.common.steam.asynch;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SteamAPIExecutorService {
	// executor service that handles API connections
	private static ExecutorService executorService;
	
	private SteamAPIExecutorService() { }
	
	/**
	 * Get instance of the executor service
	 * @return
	 */
	public static ExecutorService getInstance() {
		if(executorService == null) {
			// TODO: this should be configuration somewhere
			int maxThreads = 50;
			executorService =
					new ThreadPoolExecutor(
						maxThreads, // core thread pool size
						maxThreads, // maximum thread pool size
						1, // time to wait before resizing the pool
						TimeUnit.MINUTES,
						new ArrayBlockingQueue<Runnable>(maxThreads, true),
						new ThreadPoolExecutor.CallerRunsPolicy());
		}
		return executorService;
	}
}
