package nick.jgame.world.tiles;

import nick.jgame.init.*;
import nick.jgame.world.*;
import nick.jgame.world.util.WorldUtil;

public final class GrassTile extends Tile {

	private static final GrassTile	instance	= new GrassTile( );

	public static final Tile getInstance( ) {

		return instance;
	}

	private int	ticksUnspread	= 0;

	private GrassTile( ) {

		super(Materials.grass, Sprites.grass, true, "grass");
	}

	@Override
	public void update(final World w, final short x, final short y) {

		if (!WorldUtil.isTileInRange(w, x, y, Tiles.water, 4)) {
			w.setTile(Tiles.dirt, x, y);
			return;
		}
		if ((ticksUnspread >= 2) && ((w.getRand( ).nextInt( ) % ticksUnspread) == 0)) {
			if (WorldUtil.isTouchingWater(w, x, y) && WorldUtil.isTouchingTile(w, x, y, Tiles.dirt)) {

				Tile[ ] around = WorldUtil.getTouching(w, x, y);
				int index = 0;

				for (byte i = 0; i < around.length; i++) {

					if (around[i] == Tiles.dirt) {
						break;
					}
					index++;
				}
				switch (index) {
					case 0:
						w.setTile(this, x, (short) (y - 1));
						break;
					case 1:
						w.setTile(this, (short) (x + 1), y);
						break;
					case 2:
						w.setTile(this, x, (short) (y + 1));
						break;
					case 3:
						w.setTile(this, (short) (x - 1), y);
						break;

				}
				ticksUnspread = 0;
			}
		}

		ticksUnspread += 2;
	}

}
