package nick.jgame.gui;

import java.util.HashMap;

import nick.jgame.Constants;
import nick.jgame.util.debug.GameLog;

public abstract class GuiWithThread extends GuiHolder implements Runnable {

	private static final HashMap<String, Thread>	guiThreads	= new HashMap<String, Thread>( );

	public static boolean isRunning(final String guiName) {

		return guiThreads.get(guiName).isAlive( );
	}

	protected boolean	hasStarted	= false;

	private boolean		running		= false;

	protected GuiWithThread(final String guiName) {

		super(guiName);
		guiThreads.put(guiName, new Thread(this, guiName));
	}

	@Override
	public void close(final String guiName) {

		if (!guiThreads.get(guiName).isAlive( )) { return; }
		running = false;
		try {
			guiThreads.get(guiName).join( );
		} catch (Exception e) {
			GameLog.warn(e);
		}
	}

	@Override
	public boolean hasOwnThread( ) {

		return true;
	}

	public boolean hasStarted( ) {

		return hasStarted;
	}

	@Override
	public void run( ) {

		long lastTime = System.nanoTime( );
		double delta = 0;

		while (running) {

			final long now = System.nanoTime( );
			delta += (now - lastTime) / Constants.nsInTick;
			lastTime = now;

			while (delta >= 1) {
				update( );
				delta--;
			}
		}
	}

	public void start(final String guiToStart) {

		if (guiThreads.get(guiToStart).isAlive( )) { return; }
		running = true;
		guiThreads.get(guiToStart).start( );
	}

}
