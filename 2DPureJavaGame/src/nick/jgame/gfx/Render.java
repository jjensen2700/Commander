package nick.jgame.gfx;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

import nick.jgame.*;
import nick.jgame.util.render.TxtInfo;

public final class Render {

	public static final int						transColor	= 0x0f00ff;

	private short								height, width;

	private int[ ]								pixels;

	private final CopyOnWriteArrayList<TxtInfo>	txtList		= new CopyOnWriteArrayList<>( );

	private short								xOff		= 0, yOff = 0;

	public Render(final short w, final short h) {

		setSize(w, h);
		pixels = new int[width * height];
	}

	public void dumpBuffs( ) {

		renderColor(0);
	}

	public void dumpQueuedTxt( ) {

		txtList.clear( );
	}

	public short getHeight( ) {

		return height;
	}

	public int getPixel(final int loc) {

		if ((loc < 0) || (loc >= pixels.length)) { return 0; }
		return pixels[loc];
	}

	public short getWidth( ) {

		return width;
	}

	public short getxOff( ) {

		return xOff;
	}

	public short getyOff( ) {

		return yOff;
	}

	public boolean isVisible(final short x, final short y) {

		return !((x < 0) || (y < 0) || (y >= height) || (x >= width));
	}

	public void moveOffsets(final short x, final short y) {

		xOff -= x;
		yOff -= y;

	}

	public void renderColor(final int color) {

		for (int loc = 0; loc < pixels.length; loc++) {
			setPixel(color, loc);
		}
	}

	public void renderQueuedTxt(final Graphics g) {

		for (TxtInfo info : txtList) {
			if ((info == null) || MainGame.getInst( ).isSwitchingGuis( )) { return; }
			g.setFont(Constants.bigFont);
			g.setColor(new Color(info.getColor( ), false));
			if (info.useSmallFont( )) {
				g.setFont(Constants.smallFont);
			}
			g.drawString(info.getTxt( ), info.getX( ), info.getY( ));
		}

	}

	public void renderSprite(final Sprite s, final short xLoc, final short yLoc) {

		int xPix = 0, yPix = 0;
		for (short y = yLoc; y < ((s.getHeight( ) + yLoc)); y++) {

			for (short x = xLoc; x < ((s.getWidth( ) + xLoc)); x++) {

				if (!isVisible(x, y)) {
					continue;
				}
				int toDraw = s.getPixel(xPix, yPix);
				if (toDraw != transColor) {
					setPixel(s.getPixel(xPix, yPix), x, y);
				}

				xPix++;
			}
			xPix = 0;
			yPix++;
		}
	}

	public void renderTxt(final String txt, final int color, final short x, final short y, final boolean useSmall) {

		final TxtInfo info = new TxtInfo(txt, color, x, y, useSmall);
		renderTxt(info);
	}

	public void renderTxt(final TxtInfo info) {

		if (txtList.contains(info)) { return; }
		txtList.add(info);

	}

	public void setHeight(final short h) {

		height = h;
	}

	private void setPixel(final int color, final int loc) {

		if ((loc < 0) || (loc >= pixels.length)) { return; }
		pixels[loc] = color;
	}

	public void setPixel(final int color, final int x, final int y) {

		setPixel(color, x + (y * width));
	}

	private void setSize(final short w, final short h) {

		setHeight(h);
		setWidth(w);

	}

	public void setWidth(final short w) {

		width = w;
	}
}
