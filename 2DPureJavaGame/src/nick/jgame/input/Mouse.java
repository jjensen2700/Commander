package nick.jgame.input;

import java.awt.event.*;

public final class Mouse implements MouseListener, MouseWheelListener, MouseMotionListener {

	private static int			button	= -1;

	private static final Mouse	inst	= new Mouse( );

	private static boolean		mouseOut;

	private static int			xLoc	= -1;

	private static int			yLoc	= -1;

	public static int getButton( ) {

		return button;
	}

	public static final Mouse getInst( ) {

		return inst;
	}

	public static int getxLoc( ) {

		return xLoc;
	}

	public static int getyLoc( ) {

		return yLoc;
	}

	public static final boolean isMouseOut( ) {

		return mouseOut;
	}

	@Override
	public void mouseClicked(final MouseEvent me) {

	}

	@Override
	public void mouseDragged(final MouseEvent me) {

	}

	@Override
	public void mouseEntered(final MouseEvent me) {

		mouseOut = false;
	}

	@Override
	public void mouseExited(final MouseEvent me) {

		mouseOut = true;
	}

	@Override
	public void mouseMoved(final MouseEvent me) {

		xLoc = me.getX( );
		yLoc = me.getY( );

	}

	@Override
	public void mousePressed(final MouseEvent me) {

		button = me.getButton( );
	}

	@Override
	public void mouseReleased(final MouseEvent me) {

		button = -1;
	}

	@Override
	public void mouseWheelMoved(final MouseWheelEvent mwe) {

	}

}
