package nick.jgame.world.structures;

import nick.jgame.gfx.Render;
import nick.jgame.init.*;
import nick.jgame.world.*;
import nick.jgame.world.util.WorldUtil;

public final class Town extends WorldStruct {

	public static enum Types {
		FARMING, MINING, NONE, TRADE;
	}

	private byte			defenseRating	= 1;

	private boolean			isPort			= false;

	private byte			population		= 2;

	private final Types[ ]	types			= new Types[ ] { Types.NONE, Types.NONE };

	public Town(final World w, final short tileX, final short tileY, final String name) {

		super(tileX, tileY, name);

		if (w != null) {
			calcTypes(w);
		}
	}

	private void addType(final int index, final Types type) {

		if (index >= types.length) { return; }
		if (types[index] != Types.NONE) {
			types[index] = type;
		}
	}

	private void calcTypes(final World w) {

		Tile[ ] surround = WorldUtil.getTouching(w, xLoc, yLoc);
		byte mineWeight = 0;
		byte farmWeight = 0;
		byte tradeWeight = 0;
		for (Tile t : surround) {
			if (t == Tiles.water) {
				isPort = true;

			} else if (t == Tiles.grass) {
				farmWeight++;
			} else if (t == Tiles.stone) {
				mineWeight++;
			} else if (t == Tiles.dirt) {
				tradeWeight++;
			}

		}

		if (mineWeight > Math.max(farmWeight, tradeWeight)) {
			addType(0, Types.MINING);
			mineWeight = -10;
		} else if (farmWeight > Math.max(mineWeight, tradeWeight)) {
			addType(0, Types.FARMING);
			farmWeight = -10;
		} else if (tradeWeight > Math.max(mineWeight, farmWeight)) {
			addType(0, Types.TRADE);
			tradeWeight = -10;
		}
		if (mineWeight == -10) {
			if (farmWeight > tradeWeight) {
				addType(1, Types.FARMING);
			} else {
				addType(1, Types.TRADE);
			}
		} else if (farmWeight == -10) {
			if (mineWeight > tradeWeight) {
				addType(1, Types.MINING);
			} else {
				addType(1, Types.TRADE);
			}
		} else if (tradeWeight == -10) {
			if (farmWeight > mineWeight) {
				addType(1, Types.FARMING);
			} else {
				addType(1, Types.MINING);
			}
		}
	}

	@Override
	public boolean canBePlacedHere(final World w, final short tileX, final short tileY) {

		Tile.Material mat = w.getTile(tileX, tileY).getTileMat( );
		return (mat != Materials.liquid) && (mat != Materials.border) && (mat != Materials.air);
	}

	public int getHealth( ) {

		return population + defenseRating;
	}

	public byte getPop( ) {

		return population;
	}

	public Types[ ] getType( ) {

		return types;
	}

	private boolean hasNoTypes( ) {

		return (types[0] == Types.NONE) && (types[1] == Types.NONE);
	}

	public boolean isPort( ) {

		return isPort;
	}

	@Override
	public void render(final Render rend) {

	}

	@Override
	public void update(final World w) {

		if (hasNoTypes( )) {
			calcTypes(w);
		}
		if (population >= 100) {
			population -= w.getRand( ).nextInt(5);
		}

		if ((population <= 0) && (getHealth( ) <= 0)) {
			w.removeStruct(this);
		}
	}
}
