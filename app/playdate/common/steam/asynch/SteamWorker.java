package playdate.common.steam.asynch;

/**
 * 
 * @author kevin
 *
 */
public abstract class SteamWorker extends Thread {

	protected boolean started = false;
	protected boolean running = false;
	
	private static final int MAX_WAIT = 60000;
	
	@Override
	public synchronized void run() {
		started = true;
		running = true;
		personalizedRun();
		notify();
		running = false;
	}
	
	public abstract void personalizedRun();
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public synchronized void isFinished() throws InterruptedException {
		if(running) {
			wait(MAX_WAIT);
		}
	}

}
