package nick.jgame.world.tiles;

import nick.jgame.init.*;
import nick.jgame.world.*;
import nick.jgame.world.util.WorldUtil;

public final class WaterTile extends Tile {

	private static final WaterTile	tile	= new WaterTile( );

	public static WaterTile getInstance( ) {

		return tile;
	}

	private WaterTile( ) {

		super(Materials.liquid, Sprites.water, true, "water");

	}

	@Override
	public void update(final World w, final short x, final short y) {

		final Tile[ ] around = WorldUtil.getTouching(w, x, y);

		byte touchNonWater = 0;

		for (byte i = 0; i < around.length; i++) {
			if ((around[i] == Tiles.stone) || (around[i] == Tiles.grass)) {
				touchNonWater++;
			}
		}
		if (touchNonWater >= getChangeLvl( )) {
			w.setTile(Tiles.grass, x, y);
			return;
		}
		if (WorldUtil.isTouchingAir(w, x, y)) {

			int index = 0;

			for (byte i = 0; i < around.length; i++) {

				if (around[i] == Tiles.air) {
					break;
				}
				index++;
			}

			switch (index) {
				case 0:
					if (!w.isOutOfBounds(x, (short) (y - 1))) {
						w.setTile(this, x, (short) (y - 1));
					}
					break;
				case 1:
					if (!w.isOutOfBounds((short) (x + 1), y)) {
						w.setTile(this, (short) (x + 1), y);
					}
					break;
				case 2:
					if (!w.isOutOfBounds(x, (short) (y + 1))) {
						w.setTile(this, x, (short) (y + 1));
					}
					break;
				case 3:
					if (!w.isOutOfBounds((short) (x - 1), y)) {
						w.setTile(this, (short) (x - 1), y);
					}
					break;

			}

		}

	}

}
