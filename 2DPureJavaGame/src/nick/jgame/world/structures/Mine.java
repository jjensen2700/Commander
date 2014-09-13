package nick.jgame.world.structures;

import java.util.ArrayList;

import nick.jgame.gfx.Render;
import nick.jgame.init.Materials;
import nick.jgame.world.World;
import nick.jgame.world.structures.interfaces.*;

public final class Mine extends WorldStruct implements IMoneyMaker, IUpkeep {

	public static enum MineQuality {
		EXCELLENT, GOOD, POOR;

		public static byte getMultiplier(final MineQuality mq) {

			switch (mq) {
				case EXCELLENT:
					return 4;
				case GOOD:
					return 2;
				case POOR:
					return 1;
				default:
					return 0;
			}
		}
	}

	public static enum MineQuantity {
		LOW, MODERATE, RICH;

		public static short getTickLife(final MineQuantity mq) {

			switch (mq) {
				case LOW:
					return 50;
				case MODERATE:
					return 100;
				case RICH:
					return 200;
				default:
					return 10;
			}
		}
	}

	public static enum MineType {
		COAL, GOLD, IRON;

		public static byte getWorth(final MineType mt) {

			switch (mt) {
				case COAL:
					return 1;
				case GOLD:
					return 4;
				case IRON:
					return 2;
				default:
					return 0;
			}
		}
	}

	private boolean			isProducing;

	private MineQuality		quality;

	private MineQuantity	quantity;

	private long			ticksLived;

	private MineType		type;

	public Mine(final World home, final short tileX, final short tileY, final String name) {

		super(home, tileX, tileY, name);
	}

	@Override
	public boolean canBePlacedHere(final World w, final short tileX, final short tileY) {

		return w.getTile(tileX, tileY).getTileMat( ) == Materials.stone;
	}

	@Override
	public byte getProducedMoney( ) {

		if (!isProducing) { return 0; }
		return (byte) (MineQuality.getMultiplier(quality) * MineType.getWorth(type));
	}

	@Override
	public ArrayList<String> getSaveTxt( ) {

		ArrayList<String> toRet = super.getSaveTxt( );
		toRet.add("type:" + type.toString( ));
		toRet.add("quality:" + quality.toString( ));
		toRet.add("quantity:" + type.toString( ));
		return toRet;
	}

	@Override
	public float getUpkeep( ) {

		if (isProducing( )) {
			return .95f;
		} else {
			return .67f;
		}
	}

	@Override
	public boolean isProducing( ) {

		return isProducing;
	}

	@Override
	public void render(final Render rend) {

	}

	public void setIsProducing(final boolean b) {

		isProducing = b;

	}

	@Override
	public void update( ) {

		getOwner( ).addMoney(getProducedMoney( ) - getUpkeep( ));

		if (ticksLived > (MineQuantity.getTickLife(quantity) * 1000)) {
			getHome( ).removeStruct(this);
		}
		ticksLived++;
	}
}
