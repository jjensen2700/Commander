package nick.jgame.world.tiles;

import nick.jgame.init.*;
import nick.jgame.world.*;
import nick.jgame.world.util.WorldUtil;

public final class StoneTile extends Tile {

	private static final StoneTile	tile	= new StoneTile( );

	public static StoneTile getInstance( ) {

		return tile;
	}

	private StoneTile( ) {

		super(Materials.stone, Sprites.stone, true, "stone");
	}

	@Override
	public void update(final World w, final short x, final short y) {

		final Tile[ ] around = WorldUtil.getTouching(w, x, y);

		byte touchNonStone = 0;

		for (byte i = 0; i < around.length; i++) {
			if ((around[i] == Tiles.water) || (around[i] == Tiles.grass)) {
				touchNonStone++;
			}
		}
		if (touchNonStone >= getChangeLvl( )) {
			w.setTile(Tiles.grass, x, y);
			return;
		}
	}

}
