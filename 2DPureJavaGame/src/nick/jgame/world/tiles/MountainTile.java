package nick.jgame.world.tiles;

import nick.jgame.gfx.Render;
import nick.jgame.init.*;
import nick.jgame.world.*;
import nick.jgame.world.util.WorldUtil;

public final class MountainTile extends Tile {

	private static final MountainTile	tile	= new MountainTile( );

	public static MountainTile getInstance( ) {

		return tile;
	}

	private MountainTile( ) {

		super(Materials.stone, Sprites.mountain, true, "stone");
	}

	@Override
	public void render(final Render rend, final short xOff, final short yOff) {

		if (this.isVisible(rend, xOff, yOff) && !this.isInvisTile( )) {
			rend.renderSprite(Sprites.dirtBack, xOff, yOff);
			img.render(rend, xOff, yOff);
		}
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
