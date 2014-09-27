package nick.jgame.gfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;

import nick.jgame.util.math.Vec2i;

public final class Sprite implements OffsetRenderable {

	private static final HashMap<String, Sprite>	registered	= new HashMap<>( );

	private boolean									loaded;

	private File									loc;

	private int[ ]									pixels;

	private Vec2i									size		= Vec2i.zero;

	public Sprite(final File picLoc, final String name) {

		loc = picLoc;
		loaded = load( );
		registered.put(name, this);
	}

	private Sprite(final int width, final int height) {

		this.size = new Vec2i(width, height);
		this.pixels = new int[width * height];
	}

	public Sprite(final int width, final int height, final int color, final String name) {

		this(width, height);

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = color;
		}
		loaded = true;

		registered.put(name, this);
	}

	public Sprite(final Sprite parent, final int x, final int y, final int width, final int height, final String name) {

		this(width, height);

		int pxX = 0, pxY = 0;

		for (int xL = x; xL < (width + x); xL++) {
			for (int yL = y; yL < (height + y); yL++) {
				if ((pxX + (pxY * width)) >= pixels.length) {
					continue;
				}
				pixels[pxX + (pxY * width)] = parent.getPixel(xL, yL);
				pxY++;
			}
			pxY = 0;
			pxX++;
		}
		loaded = true;
		registered.put(name, this);
	}

	public int getHeight( ) {

		return size.getY( );
	}

	public int getPixel(final int x, final int y) {

		if (pixels == null) { return Render.transColor; }
		if ((x + (y * getWidth( ))) >= pixels.length) { return Render.transColor; }
		return pixels[x + (y * getWidth( ))];
	}

	public int getWidth( ) {

		return size.getX( );
	}

	public boolean isLoaded( ) {

		return loaded;
	}

	private boolean load( ) {

		if (!loc.exists( )) {
			System.err.println("File " + loc.getAbsolutePath( ) + " does not exist!");
			return false;
		}
		if (!loc.getName( ).endsWith(".png")) {
			System.err.println("File " + loc.getAbsolutePath( ) + " is not a supported format!");
			return false;
		}
		BufferedImage img = null;
		try {
			img = ImageIO.read(loc);
		} catch (Exception e) {
			System.err.println(e.getMessage( ));
			return false;
		}

		if (img != null) {
			size.set(img.getWidth( ), img.getHeight( ));
			pixels = img.getRGB(0, 0, getWidth( ), getHeight( ), pixels, 0, getWidth( ));
			for (int i = 0; i < pixels.length; i++) {
				pixels[i] = pixels[i] & 0x00FFFFFF;
			}
		}
		return true;
	}

	@Override
	public void render(final Render rend, final short xOff, final short yOff) {

		rend.renderSprite(this, xOff, yOff);
	}

}
