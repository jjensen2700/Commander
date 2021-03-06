package nick.jgame.gui.parts;

import nick.jgame.gfx.Render;
import nick.jgame.gui.GuiRectComp;
import nick.jgame.input.Mouse;
import nick.jgame.util.render.TxtInfo;

public final class Button extends GuiRectComp {

	private String	txt;

	private int		txtColor, backColor;

	public Button(final int x, final int y, final int width, final int height, final int backColor,
			final String text, final int txtColor, final boolean useSmall) {

		super((short) x, (short) y, (short) width, (short) height);

		setTxt(text.trim( ));

		setBackColor(backColor);
		setTxtColor(txtColor);

	}

	public int getBackColor( ) {

		return backColor;
	}

	public final String getTxt( ) {

		return txt;
	}

	public int getTxtColor( ) {

		return txtColor;
	}

	public TxtInfo getTxtInfo(final Render rend) {

		short x = (short) ((getX( ) + (getWidth( ) / 2)) - rend.getLineCenter(txt, Render.bigFont));
		short y = (short) ((getY( ) + (getHeight( ) / 2)) - rend.getLineHeightCenter(txt, Render.bigFont));
		return new TxtInfo(txt, txtColor, x, y, false);
	}

	public final boolean isClicked( ) {

		float mX = Mouse.getxLoc( );
		float mY = Mouse.getyLoc( );

		if (Mouse.getButton( ) != 1) { return false; }

		return (getX( ) <= mX) & (getY( ) <= mY) & ((getY( ) + getHeight( )) > mY) & ((getX( ) + getWidth( )) > mX);
	}

	@Override
	public void render(final Render rend) {

		for (short x = getX( ); x < (getX( ) + getWidth( )); x++) {
			for (short y = getY( ); y < (getY( ) + getHeight( )); y++) {
				rend.setPixel(backColor, x, y);
			}
		}
		rend.renderTxt(getTxtInfo(rend));
	}

	private void setBackColor(final int backColor) {

		this.backColor = backColor;
	}

	private void setTxt(final String txt) {

		this.txt = txt;
	}

	private void setTxtColor(final int txtColor) {

		this.txtColor = txtColor;
	}

}
