package nick.jgame;

import java.awt.*;
import java.awt.image.*;

import nick.jgame.gfx.*;
import nick.jgame.gfx.Window;
import nick.jgame.gui.*;
import nick.jgame.init.Guis;
import nick.jgame.input.*;
import nick.jgame.net.NetworkHandler;
import nick.jgame.opts.Options;
import nick.jgame.util.debug.*;
import nick.jgame.util.math.MathUtil;
import nick.jgame.world.util.WorldUtil;

/**
 * The Main Class. Handles game wide variables.
 *
 * @author Nick
 * @category Main Stuff
 */

public final class MainGame extends Canvas implements Runnable {

	// Static variables

	// Instance Variables.
	/**
	 * The Gui that is currently open.
	 */
	private static GuiHolder			guiOpen;

	private static final BufferedImage	img					= new BufferedImage(Constants.windowWidth, Constants.windowHeight, BufferedImage.TYPE_INT_RGB);

	/**
	 * The Main instance.
	 */
	private static final MainGame		inst				= new MainGame( );

	private static short				lastfps				= 0;

	/**
	 * The main thread.
	 */
	private static Thread				mainLine;

	private static final GameProfiler	mainProf			= new GameProfiler("Main");

	private static final int[ ]			pixels				= ((DataBufferInt) img.getRaster( ).getDataBuffer( )).getData( );

	private static final Render			rend				= new Render(Constants.windowWidth, Constants.windowHeight);

	private static final GameProfiler	renderProf			= new GameProfiler("Rendering");

	/**
	 * The boolean that tells if the game is running. Static to prevent multiple instances of this
	 * game running.
	 */
	private static boolean				running				= false;

	private static final long			serialVersionUID	= 1L;

	private static long					startTime;

	private static boolean				switchingGuis		= false;

	private static final GameProfiler	updateProf			= new GameProfiler("Tick");

	// Instance methods
	private static void actualRender(final BufferStrategy bs) {

		final Graphics g = bs.getDrawGraphics( );

		if (g == null) {

			renderProf.stopTiming( );
			return;
		}
		g.fillRect(0, 0, Constants.windowWidth, Constants.windowHeight);
		g.drawImage(img, 0, 0, Constants.windowWidth, Constants.windowHeight, null);

		rend.renderQueuedTxt(g);

		g.dispose( );
		if (bs.contentsLost( )) { return; }
		bs.show( );
	}

	// Static methods (Mostly getters)
	private static void exit(final int code) {

		Window.getInst( ).setVisible(false);

		Window.getInst( ).dispose( );
		System.exit(code);

	}

	public static synchronized GuiHolder getCurrentGui( ) {

		return guiOpen;
	}

	public static short getFPS( ) {

		return lastfps;
	}

	/**
	 * @return An instance of the MainGame class.
	 */
	public static MainGame getInst( ) {

		return inst;
	}

	/**
	 * @return The starting time of the game in milliseconds.
	 */
	public static long getStartTime( ) {

		return startTime;
	}

	/**
	 * Sets the gui and inits it, if it has not been inited.
	 *
	 * @param gui
	 *            The gui to go to.
	 */
	public static synchronized void gotoGui(final GuiHolder gui) {

		if (isSwitchingGuis( )) {
			GameLog.warn("Currently switching guis!");
			return;
		}

		if (getCurrentGui( ) == gui) { return; }

		switchingGuis = true;

		rend.dumpBuffs( );
		rend.dumpQueuedTxt( );

		guiOpen = gui;
		guiOpen.open(guiOpen.getName( ));
		if (!guiOpen.isInit( )) {
			guiOpen.initGui( );
		}

		if (guiOpen instanceof GuiWithThread) {
			GuiWithThread threadGui = (GuiWithThread) guiOpen;
			if (!threadGui.hasStarted( )) {
				threadGui.start(gui.getName( ));
			}
		}

		GameLog.info("Going to the " + gui.getName( ), false);

		switchingGuis = false;
	}

	/**
	 * Static to prevent multiple instances.
	 *
	 * @return Whether the game is running.
	 */
	public static boolean isRunning( ) {

		return running;
	}

	public static boolean isSwitchingGuis( ) {

		return switchingGuis;
	}

	private static void logMS(final float totalTime, final short fps) {

		double recordedTime = 0;
		recordedTime += mainProf.displayAndReset("Main Loop: ", fps);
		recordedTime += renderProf.displayAndReset("Rendering: ", fps);
		recordedTime += updateProf.displayAndReset("Update Call: ", 0);
		GameLog.info("All Unrecorded Time: " + (totalTime - recordedTime) + " ms", true);
		GameLog.info("Total Time: " + (totalTime) + " ms", true);
	}

	public static void main(final String[ ] args) {

		Window.getInst( ).setup( );

		MainGame.start( );
	}

	public static void moveOffsets(final short x, final short y) {

		rend.moveOffsets(x, y);

	}

	private static void render(final short fps) {

		renderProf.startTiming( );
		final BufferStrategy bs = inst.getBufferStrategy( );

		if (bs == null) {

			if (!Options.getBoolOption("quadbuff")) {
				inst.createBufferStrategy(3);
			} else {
				inst.createBufferStrategy(4);
			}
			renderProf.stopTiming( );
			return;
		}

		rend.dumpBuffs( );
		rend.dumpQueuedTxt( );
		if ((guiOpen == null) || switchingGuis) {
			renderProf.stopTiming( );
			return;
		}
		guiOpen.render(rend);

		if (Options.getBoolOption("displayfps")) {

			String txt = "FPS:" + fps;
			rend.renderTxt(txt, 0xffffff, (short) 0, (short) rend.getLineHeight(txt, Render.smallFont), true);
		}

		for (int loc = 0; loc < pixels.length; loc++) {
			pixels[loc] = rend.getPixel(loc);
		}

		actualRender(bs);

		renderProf.stopTiming( );
	}

	/**
	 * The method that starts the game.
	 */
	private static synchronized void start( ) {

		if (running) { return; }
		GameLog.info("Starting Game...", false);

		running = true;
		startTime = System.currentTimeMillis( );

		threadOn( );
		NetworkHandler.startClient( );
		NetworkHandler.startServer( );
		gotoGui(Guis.mainMenu);

		Window.getInst( ).setVisible(true);
	}

	/**
	 * The method that stops the game.
	 *
	 * @param crash
	 *            True if crashed, false if normal stop
	 * @param e
	 *            Pass in null if normal stop, else pass in an exception
	 */
	public static synchronized void stop(final boolean crash, final Exception e) {

		if (!running) { return; }
		GameLog.info("Stopping Game...", false);
		WorldUtil.save(Guis.world);
		if (!crash || (e == null)) {
			exit(0);
		} else if (crash & (e == null)) {
			GameLog.warn("I don't know why I crashed, but I crashed.");
			exit(1);
		} else if (e != null) {
			GameLog.warn(e.getMessage( ));
			exit(1);
		}
	}

	private static void threadOn( ) {

		mainLine = new Thread(inst, "Main");
		mainLine.start( );
		mainLine.setPriority(Thread.NORM_PRIORITY);
	}

	private static void update( ) {

		if ((getCurrentGui( ) == null) || isSwitchingGuis( )) { return; }
		updateProf.startTiming( );

		if (KeyBinding.isDown(Bindings.screenshot)) {
			ScreenShotHandler.takeScreenShot(img);

		}

		if (!guiOpen.hasOwnThread( )) {
			guiOpen.update( );
		}

		ScreenShotHandler.update( );
		updateProf.stopTiming( );
	}

	// The private constructor (Private to stop multiple instances)
	private MainGame( ) {

		GameLog.info("Initalizing Game...", false);
		setPreferredSize(Constants.getSizeOfWindow( ));

		addKeyListener(Keyboard.getInst( ));

		addMouseListener(Mouse.getInst( ));
		addMouseMotionListener(Mouse.getInst( ));
		addMouseWheelListener(Mouse.getInst( ));

	}

	/**
	 * The method that runs the game.
	 */
	@Override
	public void run( ) {

		GameLog.info("Finishing Init...", false);
		// Timing variables
		long loopTime = System.currentTimeMillis( );
		long lastTime = System.nanoTime( );
		float totalTime = 0;
		// Counting variables
		byte updates = 0;
		short fps = 0;

		double delta = 0;

		GameLog.info("Starting To Run...", false);

		while (running) {

			mainProf.startTiming( );

			final long now = System.nanoTime( );
			delta += (now - lastTime) / Constants.nsInTick;
			lastTime = now;

			while (delta >= 1) {
				update( );
				updates++;
				delta--;
			}

			render(lastfps);
			fps++;

			if ((loopTime <= System.currentTimeMillis( ))) {
				mainProf.stopTiming( );

				final byte newPri = MathUtil.getPrefPriority(fps, updates, (byte) mainLine.getPriority( ));

				GameLog.info("FPS: " + fps + " | Main Thread Lvl: " + mainLine.getPriority( ), true);
				// Logging ms.
				if (Options.getBoolOption("bigdebug")) {
					logMS(totalTime, fps);
				}
				// Variable Setting
				lastfps = fps;
				fps = 0;
				updates = 0;
				loopTime += 1000;
				totalTime = 0;
				mainLine.setPriority(newPri);
				mainProf.startTiming( );
			}
			totalTime += (System.nanoTime( ) - now) / Constants.nsInMs;
			mainProf.stopTiming( );
		}

		stop(false, null);
	}

}
