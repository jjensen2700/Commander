package nick.jgame.world.tiles;

import nick.jgame.init.*;
import nick.jgame.world.*;
import nick.jgame.world.util.WorldUtil;

public final class GrassTile extends Tile {

	private static final GrassTile	tile	= new GrassTile( );

	public static GrassTile getInstance( ) {

		return tile;
	}

	private GrassTile( ) {

		super(Materials.grass, Sprites.grass, true, "grass");
	}

	@Override
	public void update(final World w, final short x, final short y) {

		final Tile[ ] around = WorldUtil.getTouching(w, x, y);

		byte touchStone = 0, touchWater = 0;

		for (byte i = 0; i < around.length; i++) {
			if (around[i] == Tiles.stone) {
				touchStone++;
			} else if (around[i] == Tiles.water) {
				touchWater++;
			}
		}
		if (touchStone >= getChangeLvl( )) {
			w.setTile(Tiles.stone, x, y);
			return;
		} else if (touchWater >= getChangeLvl( )) {
			w.setTile(Tiles.water, x, y);
			return;
		}
	}

}
