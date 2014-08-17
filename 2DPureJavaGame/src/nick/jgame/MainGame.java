package nick.jgame;

import java.awt.*;
import java.awt.image.*;

import javax.swing.JFrame;

import nick.jgame.gfx.*;
import nick.jgame.gui.*;
import nick.jgame.init.Guis;
import nick.jgame.input.*;
import nick.jgame.opts.Options;
import nick.jgame.util.debug.*;
import nick.jgame.util.math.MathUtil;

/**
 * The Main Class. Handles game wide variables.
 *
 * @author Nick
 * @category Main Stuff
 */

public final class MainGame extends Canvas implements Runnable {

	// Instance variables
	private static final JFrame		frame				= new JFrame( );

	// Static variables
	/**
	 * The Main instance.
	 */
	private static final MainGame	inst				= new MainGame( );

	/**
	 * The main thread.
	 */
	private static Thread			mainLine;

	/**
	 * The boolean that tells if the game is running. Static to prevent multiple instances of this
	 * game running.
	 */
	private static boolean			running				= false;

	private static final long		serialVersionUID	= 0xffffffL;

	private static long				startTime;

	private static void exit(final int code) {

		frame.setVisible(false);

		frame.dispose( );
		System.exit(code);

	}

	// Static methods (Mostly getters)
	public static GuiHolder getCurrentGui( ) {

		return getInst( ).guiOpen;
	}

	/**
	 * @return An instance of the MainGame class.
	 */
	public static MainGame getInst( ) {

		return inst;
	}

	/**
	 * @return The Render object
	 */
	public static Render getRend( ) {

		return getInst( ).renderer;
	}

	/**
	 * @return The starting time of the game in milliseconds.
	 */
	public static long getStartTime( ) {

		return startTime;
	}

	/**
	 * Static to prevent multiple instances.
	 *
	 * @return Whether the game is running.
	 */
	public static boolean isRunning( ) {

		return running;
	}

	public static void main(final String[ ] args) {

		frame.setTitle(Constants.getDisplayName( ));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(MainGame.getInst( ));
		frame.pack( );
		frame.setLocationRelativeTo(null);

		MainGame.getInst( ).start( );
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

	/**
	 * The Gui that is currently open.
	 */
	private GuiHolder			guiOpen;

	private final BufferedImage	img				= new BufferedImage(Constants.windowWidth, Constants.windowHeight, BufferedImage.TYPE_INT_RGB);

	private final GameProfiler	mainProf		= new GameProfiler("Main");

	private final int[ ]		pixels			= ((DataBufferInt) img.getRaster( ).getDataBuffer( )).getData( );

	private final Render		renderer;

	private final GameProfiler	renderProf		= new GameProfiler("Rendering");

	private boolean				switchingGuis	= false;

	private final GameProfiler	updateProf		= new GameProfiler("Tick");

	// The private constructor (Private to stop multiple instances)
	private MainGame( ) {

		GameLog.info("Initalizing Game...", false);
		setPreferredSize(Constants.getSizeOfWindow( ));
		renderer = new Render(Constants.windowWidth, Constants.windowHeight);

		addKeyListener(Keyboard.getInst( ));

		addMouseListener(Mouse.getInst( ));
		addMouseMotionListener(Mouse.getInst( ));
		addMouseWheelListener(Mouse.getInst( ));

	}

	// Instance methods
	private void actualRender(final BufferStrategy bs, final Graphics g) {

		g.fillRect(0, 0, Constants.windowWidth, Constants.windowHeight);
		g.drawImage(img, 0, 0, Constants.windowWidth, Constants.windowHeight, null);

		renderer.renderQueuedTxt(g);

		g.dispose( );
		if (bs.contentsLost( )) { return; }
		bs.show( );
	}

	/**
	 * Sets the gui and inits it, if it has not been inited.
	 *
	 * @param gui
	 *            The gui to go to.
	 */
	public void gotoGui(final GuiHolder gui) {

		if (isSwitchingGuis( )) {
			GameLog.warn("Currently switching guis!");
			return;
		}

		if (gui == guiOpen) {
			GameLog.warn("Already in the " + gui.getName( ) + "!");

		}
		switchingGuis = true;

		renderer.dumpBuffs( );
		renderer.dumpQueuedTxt( );

		if (!gui.isInit( )) {
			gui.initGui( );
		}
		if (gui instanceof GuiWithThread) {
			GuiWithThread threadGui = (GuiWithThread) gui;
			if (!threadGui.hasStarted( )) {
				threadGui.start(gui.getName( ));
			}
		}

		GameLog.info("Going to the " + gui.getName( ), false);
		guiOpen = gui;

		switchingGuis = false;
	}

	public boolean isSwitchingGuis( ) {

		return switchingGuis;
	}

	private void logMS(final float totalTime, final short fps) {

		double recordedTime = 0;
		recordedTime += mainProf.displayAndReset("Main Loop: ", fps);
		recordedTime += renderProf.displayAndReset("Rendering: ", fps);
		recordedTime += updateProf.displayAndReset("Update Call: ", 0);
		GameLog.info("All Unrecorded Time: " + (totalTime - recordedTime) + " ms", true);
		GameLog.info("Total Time: " + (totalTime) + " ms", true);
	}

	// -Running stuff
	private void render( ) {

		renderProf.startTiming( );
		final BufferStrategy bs = getBufferStrategy( );

		if (bs == null) {

			if (!Options.getBoolOption("quadbuff")) {
				createBufferStrategy(3);
			} else {
				createBufferStrategy(4);
			}
			renderProf.stopTiming( );
			return;
		}

		renderer.dumpBuffs( );
		if (isSwitchingGuis( ) || (guiOpen == null)) {
			renderProf.stopTiming( );
			return;
		}
		guiOpen.render(renderer);
		for (int loc = 0; loc < pixels.length; loc++) {
			pixels[loc] = renderer.getPixel(loc);
		}

		final Graphics g = bs.getDrawGraphics( );
		if (g == null) {
			GameLog.warn("The Graphics objects is null!");
			renderProf.stopTiming( );
			return;
		}
		actualRender(bs, g);

		renderProf.stopTiming( );
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

			render( );
			fps++;

			if ((loopTime <= System.currentTimeMillis( ))) {
				mainProf.stopTiming( );

				final byte newPri = MathUtil.getPrefPriority(fps, updates, (byte) mainLine.getPriority( ));

				GameLog.info("FPS: " + fps + " | Game Ticks: " + updates + " | Main Thread Lvl: " + mainLine.getPriority( ), true);
				// Logging ms.
				if (Options.getBoolOption("bigdebug")) {
					logMS(totalTime, fps);
				}
				// Variable Setting
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

	/**
	 * The method that starts the game.
	 */
	private synchronized void start( ) {

		if (running) { return; }
		GameLog.info("Starting Game...", false);

		running = true;
		startTime = System.currentTimeMillis( );

		threadOn( );

		Bindings.getInstance( ).start( );

		gotoGui(Guis.mainMenu);

		frame.setVisible(true);
	}

	private void threadOn( ) {

		mainLine = new Thread(this, "Main");
		mainLine.start( );
		mainLine.setPriority(Thread.NORM_PRIORITY);
	}

	private void update( ) {

		updateProf.startTiming( );

		if (KeyBinding.isDown(Bindings.screenshot)) {
			ScreenShotHandler.takeScreenShot(img);

		}
		if ((guiOpen == null) || isSwitchingGuis( )) {
			GameLog.warn("A null GUI or switching!");
			updateProf.stopTiming( );
			return;
		}
		if (!guiOpen.hasOwnThread( )) {
			guiOpen.update( );
		}

		ScreenShotHandler.update( );
		updateProf.stopTiming( );
	}

}
