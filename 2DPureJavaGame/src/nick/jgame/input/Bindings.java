package nick.jgame.input;

import java.awt.event.KeyEvent;

public final class Bindings {

	public static final KeyBinding	confirm		= new KeyBinding(KeyEvent.VK_ENTER, "confirm");

	public static final KeyBinding	exit		= new KeyBinding(KeyEvent.VK_ESCAPE, "exit");

	private static final Bindings	inst		= new Bindings( );

	public static final KeyBinding	moveDown	= new KeyBinding(KeyEvent.VK_S, "move_down");

	public static final KeyBinding	moveLeft	= new KeyBinding(KeyEvent.VK_A, "move_left");

	public static final KeyBinding	moveRight	= new KeyBinding(KeyEvent.VK_D, "move_right");

	public static final KeyBinding	moveUp		= new KeyBinding(KeyEvent.VK_W, "move_up");

	public static final KeyBinding	panDown		= new KeyBinding(KeyEvent.VK_DOWN, "pan_down");

	public static final KeyBinding	panLeft		= new KeyBinding(KeyEvent.VK_LEFT, "pan_left");

	public static final KeyBinding	panRight	= new KeyBinding(KeyEvent.VK_RIGHT, "pan_right");

	public static final KeyBinding	panUp		= new KeyBinding(KeyEvent.VK_UP, "pan_up");

	public static final KeyBinding	screenshot	= new KeyBinding(KeyEvent.VK_F1, "screenshot");

	public static final KeyBinding	speed		= new KeyBinding(KeyEvent.VK_SHIFT, "speed");

	public static Bindings getInstance( ) {

		return inst;
	}

	public static boolean isShifting( ) {

		return Keyboard.isKeyDown(KeyEvent.VK_SHIFT);
	}

}
