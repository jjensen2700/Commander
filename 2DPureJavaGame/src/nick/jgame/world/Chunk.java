package nick.jgame.world;

import nick.jgame.gfx.Render;
import nick.jgame.init.Tiles;
import nick.jgame.world.util.ChunkCoords;

public final class Chunk {

	public static final byte	sideLength	= 32;

	private static boolean isOutOfBounds(final short x, final short y) {

		return (x < 0) || (x >= sideLength) || (y < 0) || (y >= sideLength);
	}

	private final ChunkCoords	coords;

	private final Tile[ ][ ]	tiles	= new Tile[32][32];

	public Chunk(final byte chunkX, final byte chunkY) {

		coords = new ChunkCoords(chunkY, chunkY);
	}

	public Tile getTile(final short x, final short y) {

		if (isOutOfBounds(x, y)) { return Tiles.air; }
		if (tiles[x][y] == null) {
			setTile(Tiles.air, x, y);
			return Tiles.air;
		}
		return tiles[x][y];
	}

	/**
	 * Use pixel precision.
	 */
	public boolean isVisible(final Render rend, final short x, final short y) {

		final short dist = 32 * 32;
		if (rend.isVisible(x, y)) { return true; }
		if (rend.isVisible((short) (x + dist), (short) (y + dist))) { return true; }
		if (rend.isVisible((short) (x + dist), y)) { return true; }
		if (rend.isVisible((short) (x + (dist / 2)), (short) (y + (dist / 2)))) { return true; }
		return (rend.isVisible(x, (short) (y + dist)));
	}

	public void setTile(final Tile t, final short x, final short y) {

		if (isOutOfBounds(x, y)) {
			System.err.println(x + "," + y + " is not a valid chunk tile location!");
			return;
		}
		if (tiles[x][y] == Tiles.stone) { return; }
		tiles[x][y] = t;
	}

	public void update(final World w) {

		for (short x = 0; x < sideLength; x++) {
			for (short y = 0; y < sideLength; y++) {

				short tileX = (short) (x + (coords.getX( ) * sideLength));
				short tileY = (short) (y + (coords.getY( ) * sideLength));
				final Tile t = w.getTile(tileX, tileY);

				if (t.doesTick( )) {
					t.update(w, tileX, tileY);

				}

			}
		}
	}
}
