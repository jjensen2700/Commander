package nick.jgame.gui;

import nick.jgame.gfx.Renderable;

public abstract class GuiComp implements Renderable {

	private short	x, y;

	public GuiComp(final short x, final short y) {

		this.setX(x);
		this.setY(y);
	}

	public final short getX( ) {

		return x;
	}

	public final short getY( ) {

		return y;
	}

	protected final void setX(final short x) {

		this.x = x;
	}

	protected final void setY(final short y) {

		this.y = y;
	}

}
