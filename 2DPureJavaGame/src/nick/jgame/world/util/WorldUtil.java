package nick.jgame.world.util;

import nick.jgame.init.Tiles;
import nick.jgame.util.math.MathUtil;
import nick.jgame.world.*;
import nick.jgame.world.Tile.Material;
import nick.jgame.world.structures.*;

public final class WorldUtil {

	public static long calcSeed(final World w) {

		return MathUtil.convertToLong(w.getName( )) + (getArea(w));
	}

	public static int getArea(final World w) {

		return w.getTileHeight( ) * w.getTileWidth( );
	}

	public static Mine getClosestMine(final World w, final short x, final short y, final byte maxDist) {

		for (byte xL = 0; xL < maxDist; xL++) {
			for (byte yL = 0; yL < maxDist; yL++) {
				short x1 = (short) (x + xL);
				short y1 = (short) (y + yL);
				short x2 = (short) (x - xL);
				short y2 = (short) (y - yL);

				if (getStructAtLoc(w, x1, y1) instanceof Mine) { return (Mine) getStructAtLoc(w, x1, y1); }
				if (getStructAtLoc(w, x2, y1) instanceof Mine) { return (Mine) getStructAtLoc(w, x2, y1); }
				if (getStructAtLoc(w, x1, y2) instanceof Mine) { return (Mine) getStructAtLoc(w, x1, y2); }
				if (getStructAtLoc(w, x2, y2) instanceof Mine) { return (Mine) getStructAtLoc(w, x2, y2); }
			}
		}
		return null;
	}

	public static WorldStruct getStructAtLoc(final World w, final short x, final short y) {

		for (int i = 0; i < w.getNumOfStructs( ); i++) {
			final WorldStruct ws = w.getStruct(i);
			if ((ws.getxLoc( ) == x) && (ws.getyLoc( ) == y)) { return ws; }
		}
		return null;
	}

	public static Tile[ ] getTouching(final World w, final short x, final short y) {

		Tile[ ] ts = new Tile[4];

		ts[0] = w.getTile(x, (short) (y - 1));
		ts[1] = w.getTile((short) (x + 1), y);
		ts[2] = w.getTile(x, (short) (y + 1));
		ts[3] = w.getTile((short) (x - 1), y);
		return ts;
	}

	public static boolean isTileInRange(final World w, final short x, final short y, final Tile t, final float radius) {

		for (short xL = (short) (x - radius); xL < (x + radius); xL++) {
			for (short yL = (short) (y - radius); yL < (y + radius); yL++) {
				if (w.getTile(xL, yL) == t) { return true; }
			}
		}
		return false;
	}

	public static boolean isTouchingAir(final World w, final short x, final short y) {

		Tile[ ] touching = getTouching(w, x, y);
		for (Tile i : touching) {
			if (i == Tiles.air) { return true; }
		}
		return false;
	}

	public static boolean isTouchingMat(final World w, final short x, final short y, final Material mat) {

		Tile[ ] touching = getTouching(w, x, y);
		for (Tile i : touching) {
			if (mat == i.getTileMat( )) { return true; }
		}
		return false;
	}

	public static boolean isTouchingTile(final World w, final short x, final short y, final Tile toLook) {

		Tile[ ] touching = getTouching(w, x, y);
		for (Tile i : touching) {
			if (i == toLook) { return true; }
		}
		return false;
	}

	public static boolean isTouchingWater(final World w, final short x, final short y) {

		Tile[ ] touching = getTouching(w, x, y);
		for (Tile i : touching) {
			if (i == Tiles.water) { return true; }
		}
		return false;
	}

	public static void parsePerlinToTiles(final World w, final float[ ][ ] perlin,
			final boolean stoneBorder) {

		float waterLvl = PerlinHeights.getSeaLevel( );
		float stoneStart = PerlinHeights.getBareRockStart( );

		for (short x = 0; x < w.getTileWidth( ); x++) {
			for (short y = 0; y < w.getTileHeight( ); y++) {

				float current = perlin[x][y];

				if (current < waterLvl) {
					w.setTile(Tiles.water, x, y);
				} else if ((current > waterLvl) && (current < stoneStart)) {
					w.setTile(Tiles.grass, x, y);
				} else if (current > stoneStart) {
					w.setTile(Tiles.stone, x, y);
				}

				if (stoneBorder) {
					if ((x == 0) || (y == 0)) {
						w.setTile(Tiles.stone, x, y);
					}
					if ((x == (w.getTileWidth( ) - 1)) || (y == (w.getTileHeight( ) - 1))) {
						w.setTile(Tiles.stone, x, y);
					}
				}

			}

		}
	}
}
