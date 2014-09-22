package nick.jgame.world.util;

import java.io.File;
import java.util.ArrayList;

import nick.jgame.*;
import nick.jgame.entity.*;
import nick.jgame.init.Tiles;
import nick.jgame.net.Server;
import nick.jgame.net.packets.Packet03Tile;
import nick.jgame.util.debug.GameLog;
import nick.jgame.util.io.FileUtil;
import nick.jgame.util.math.MathUtil;
import nick.jgame.world.*;
import nick.jgame.world.Tile.Material;
import nick.jgame.world.structures.*;

public final class WorldUtil {

	public static float calcDiagMoveCost(final Material m1, final Material m2) {

		final double diagDist = Math.sqrt(2);
		final float avgCost = (m1.getTerrainCost( ) + m2.getTerrainCost( )) / 2;
		return (float) (avgCost * diagDist);
	}

	public static long calcSeed(final World w) {

		return MathUtil.convertToLong(w.getName( )) + getArea(w);
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

		for (int i = 0; i < w.getStructList( ).size( ); i++) {
			final WorldStruct ws = w.getStructList( ).get(i);
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

	public static void load(final World w) {

		if (w.isGenerated( )) {
			GameLog.warn("World " + w.getName( ) + " is already generated!");
			return;
		}
		if (!w.loadsFromFile( )) {
			GameLog.warn("World " + w.getName( ) + " does not load from a file!");
			return;
		}
		final ArrayList<String> txt = FileUtil.loadTxt(w.getSaveFile( ));
		txt.trimToSize( );
		int loc = 0;
		for (String now : txt) {
			String[ ] comp1 = now.split(":");

			if (comp1[0].equals("seed")) {
				w.setSeed(Long.parseLong(comp1[1]));
				continue;
			}

			// Things that need coordinates
			String[ ] coords = comp1[1].split(",");
			if (comp1[0].equals("size")) {
				byte x = Byte.parseByte(coords[0]);
				byte y = Byte.parseByte(coords[1]);
				w.setChunkArray(x, y);
				continue;
			}
			if (!comp1[0].equals("entities:")) {
				for (short i = 0; i < Tile.getTilesInited( ); i++) {
					if (comp1[0].equals(Tile.getAt(i))) {

						short x = Short.parseShort(coords[0]);
						short y = Short.parseShort(coords[1]);
						w.setTile(Tile.getAt(i), x, y);
					}

				}
			} else {
				parseEntities(w, txt, loc);
			}
			loc++;
		}

		w.setGenerated( );
	}

	private static void parseEntities(final World w, final ArrayList<String> txt, final int loc) {

		ArrayList<String> locTxt = new ArrayList<>( );
		for (int i = loc; i < txt.size( ); i++) {
			locTxt.add(txt.get(i));
		}
		EnumEntityType enumtype = EnumEntityType.INVALID;
		short xParse = 0, yParse = 0;
		String nameParse = null;
		float money = 0;
		byte lvl = 0, lastLvl = -1;
		for (String now : locTxt) {
			if (now.contains("{")) {
				lastLvl = lvl;
				lvl++;
			} else if (now.contains("}")) {
				lastLvl = lvl;
				lvl--;
			}

			if (now.startsWith("new")) {
				String type = now.substring(3).trim( ).toLowerCase( );
				enumtype = EnumEntityType.getType(type);
				continue;
			}
			String[ ] comp = now.split(":");
			if (comp[0].equalsIgnoreCase("loc")) {
				String[ ] coords = comp[1].split(",");
				xParse = Short.parseShort(coords[0]);
				yParse = Short.parseShort(coords[1]);
				continue;
			} else if (comp[0].equalsIgnoreCase("name")) {
				nameParse = comp[1];
				continue;
			} else if (comp[0].equalsIgnoreCase("owner") && (enumtype != EnumEntityType.OWNER)) {
				GameLog.warn("Entity parsing is either broken or was not saved correctly!");
				continue;
			} else if (comp[0].equalsIgnoreCase("money")) {
				money = Float.parseFloat(comp[1]);
				continue;
			}
			if ((lvl == 0) && (lastLvl > 0)) {
				if (enumtype == EnumEntityType.OWNER) {
					w.spawnIn(EntityOwner.parseOwner(w, nameParse, money));
				} else {
					w.spawnIn(Entity.formEntity(w, enumtype, nameParse, xParse, yParse));
				}
				enumtype = EnumEntityType.INVALID;
				xParse = 0;
				yParse = 0;
				nameParse = "";
				money = 0;
			}
		}
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
				} else {
					w.setTile(Tiles.grass, x, y);
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

	public static void save(final World w) {

		final String horzBreak = "----------------------------------------------------";
		final long seed = w.getSeed( );

		if (!w.loadsFromFile( )) {
			w.setSaveLoc(new File(Constants.assetsLoc + "worlds" + File.separator + seed + ".txt"));
		}

		final ArrayList<String> text = new ArrayList<>( );

		text.add("versionWrittenIn:" + GameVersion.getVersion( ));
		text.add("seed:" + seed);
		text.add("sizeInChunks:" + w.getChunkWidth( ) + "," + w.getChunkHeight( ));
		text.add(horzBreak);
		text.add("worldData:{");

		for (short x = 0; x < w.getTileWidth( ); x++) {
			for (short y = 0; y < w.getTileHeight( ); y++) {
				Tile t = w.getTile(x, y);
				text.add(t.getName( ) + ':' + x + ", " + y);
			}
		}
		text.add("}");
		text.add(horzBreak);
		text.add("entities:");

		for (Entity e : w.getEntityList( )) {
			text.add("{");
			text.addAll(e.getSaveTxt( ));
			text.add("}");
		}

		FileUtil.writeTxt(w.getSaveFile( ), FileUtil.formatToSave(text));
	}

	public static void sendWorldData(final World w, final Server s) {

		for (byte xL = 0; xL < w.getTileWidth( ); xL++) {
			for (byte yL = 0; yL < w.getTileHeight( ); yL++) {
				new Packet03Tile(w.getTile(xL, yL), xL, yL).writeDataToClient( );

			}
		}
	}
}
