package nick.jgame.gui;

public abstract class GuiRectComp extends GuiComp {

	private short	high;

	private short	wide;

	public GuiRectComp(final short x, final short y, final short width, final short height) {

		super(x, y);
		setWidth(width);
		setHeight(height);
	}

	public final short getHeight( ) {

		return high;
	}

	public final short getWidth( ) {

		return wide;
	}

	protected final void setHeight(final short height) {

		high = height;

	}

	protected final void setWidth(final short width) {

		wide = width;

	}

}
