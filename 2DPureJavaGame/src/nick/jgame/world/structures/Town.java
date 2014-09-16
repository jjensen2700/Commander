package nick.jgame.world.structures;

import java.util.ArrayList;

import nick.jgame.gfx.Render;
import nick.jgame.init.*;
import nick.jgame.world.*;
import nick.jgame.world.util.WorldUtil;

public final class Town extends WorldStruct {

	public static enum Types {
		FARMING, MINING, NONE, TRADE;
	}

	private byte			defenseRating;

	private float			happyPercent	= 50;

	private boolean			isPort			= false;

	private short			maxPop			= 5000;

	private short			population		= 1000;

	private final Types[ ]	types			= new Types[ ] { Types.NONE, Types.NONE };

	public Town(final World home, final short tileX, final short tileY, final String name) {

		super(home, tileX, tileY, name);

		calcTypes(home);

	}

	private void addType(final int index, final Types type) {

		if (index >= types.length) { return; }
		if (types[index] != Types.NONE) {
			types[index] = type;
		}
	}

	private void calcTypes(final World w) {

		Tile[ ] surround = WorldUtil.getTouching(w, getxLoc( ), getyLoc( ));
		byte mineWeight = 0;
		byte farmWeight = 0;
		byte tradeWeight = 0;
		for (Tile t : surround) {
			if (t == Tiles.water) {
				isPort = true;
				continue;
			} else if (t == Tiles.grass) {
				farmWeight++;
				continue;
			} else if (t == Tiles.stone) {
				mineWeight++;
				continue;
			} else if (getHome( ).getRand( ).nextBoolean( )) {
				tradeWeight++;
				continue;
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

	public short getHappyCount( ) {

		return (short) ((happyPercent / 100) * getPop( ));
	}

	public int getHealth( ) {

		if (population < (defenseRating * 1000)) { return defenseRating * 100; }
		return (population + (defenseRating * 100)) / 100;
	}

	public short getPop( ) {

		return population;
	}

	@Override
	public ArrayList<String> getSaveTxt( ) {

		ArrayList<String> toRet = super.getSaveTxt( );

		toRet.add("pop:" + population);
		toRet.add("defense:" + defenseRating);
		toRet.add("happy:" + happyPercent);
		toRet.add("}");
		return toRet;
	}

	public Types[ ] getType( ) {

		return types;
	}

	public boolean hasNoTypes( ) {

		return (types[0] == Types.NONE) && (types[1] == Types.NONE);
	}

	public boolean isPort( ) {

		return isPort;
	}

	public boolean isType(final Types type) {

		return (types[0] == type) || (types[1] == type);
	}

	@Override
	public void render(final Render rend) {

	}

	@Override
	public void update( ) {

		if (hasNoTypes( )) {
			calcTypes(getHome( ));
		}

		if (population >= maxPop) {
			population -= getHome( ).getRand( ).nextInt(5);
		} else if ((population <= 0) && (getHealth( ) <= 0)) {
			getHome( ).removeStruct(this);
		}

		if (happyPercent >= 100) {
			happyPercent -= getHome( ).getRand( ).nextInt(5);
		}

		if (isType(Types.MINING)) {
			final Mine m = WorldUtil.getClosestMine(getHome( ), getxLoc( ), getyLoc( ), (byte) 10);
			if (m.getOwner( ) == getOwner( )) {
				m.setIsProducing(true);
			}
		}

		if (isType(Types.TRADE)) {
			getOwner( ).addMoney(getHome( ).getRand( ).nextFloat( ));
		}
	}
}
