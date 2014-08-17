package nick.jgame.input;

import java.awt.event.*;
import java.util.ArrayList;

public final class Keyboard implements KeyListener {

	private static final boolean[ ]				down	= new boolean[700];

	private static final Keyboard				inst	= new Keyboard( );

	private static final ArrayList<Character>	typed	= new ArrayList<>( );

	public static void dumpKeyBuff(final boolean dumpBools, final boolean dumpChars) {

		if (dumpBools) {
			for (int i = 0; i < down.length; i++) {
				down[i] = false;

			}
		}
		if (dumpChars) {
			for (int i = 0; i < typed.size( ); i++) {
				typed.set(i, ' ');
			}
		}
	}

	public static Keyboard getInst( ) {

		return inst;
	}

	public static boolean isKeyDown(final int key) {

		return down[key];
	}

	public static void update( ) {

		dumpKeyBuff(true, false);
	}

	@Override
	public void keyPressed(final KeyEvent e) {

		down[e.getKeyCode( )] = true;

	}

	@Override
	public void keyReleased(final KeyEvent e) {

		down[e.getKeyCode( )] = false;

	}

	@Override
	public void keyTyped(final KeyEvent e) {

		typed.add(e.getKeyChar( ));
	}

}
