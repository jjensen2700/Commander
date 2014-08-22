package nick.jgame.world;

import java.util.*;

import nick.jgame.entity.Entity;
import nick.jgame.gfx.*;
import nick.jgame.init.Tiles;

public class Tile implements OffsetRenderable {

	public static final class Material {

		private boolean	solid;

		private float	terrainCost;

		public Material(final boolean solid, final float moveCost) {

			this.solid = solid;
			this.terrainCost = moveCost;
		}

		public float getTerrainCost( ) {

			return terrainCost;
		}

		public final boolean isSolid( ) {

			return solid;
		}
	}

	private static final HashMap<String, Tile>	registered	= new HashMap<>( );

	private static short						regNum		= 0;

	private static final ArrayList<String>		tileNames	= new ArrayList<>( );

	public static final Tile getAt(final int loc) {

		return Tile.getAt(tileNames.get(loc));
	}

	public static final Tile getAt(final String name) {

		return registered.get(name);
	}

	public static final Tile getRandTile(final Random rand) {

		if (tileNames.size( ) == 0) { return Tiles.air; }
		return Tile.getAt(Math.abs(rand.nextInt(regNum)));
	}

	public static final int getTilesInited( ) {

		return regNum;
	}

	protected Sprite		img;

	protected final boolean	needsTick;

	protected Material		tileMat;

	protected final String	tileName;

	public Tile(final Material mat, final Sprite s, final boolean tick, final String name) {

		tileMat = mat;
		img = s;
		needsTick = tick;
		tileName = name;

		register( );
	}

	public final boolean doesTick( ) {

		return needsTick;
	}

	public final Sprite getImg( ) {

		return img;
	}

	public final String getName( ) {

		return tileName;
	}

	public final Material getTileMat( ) {

		return tileMat;
	}

	public boolean isArable( ) {

		return this == Tiles.grass;
	}

	public final boolean isInvisTile( ) {

		return img == null;
	}

	public boolean isVisible(final Render rend, final short x, final short y) {

		return (rend.isVisible(x, y) || rend.isVisible((short) (x + 32), (short) (y + 32)) || rend.isVisible((short) (x + 32), y) || rend.isVisible(x, (short) (y + 32))) && (img != null);
	}

	public void onCollide(final Entity entity, final short x, final short y) {

	}

	private final void register( ) {

		registered.put(tileName, this);
		tileNames.add(tileName);
		regNum++;

	}

	@Override
	public void render(final Render rend, final short xOff, final short yOff) {

		if (this.isVisible(rend, xOff, yOff) && !this.isInvisTile( )) {
			img.render(rend, xOff, yOff);
		}
	}

	@Override
	public String toString( ) {

		return getName( );
	}

	public void update(final World w, final short x, final short y) {

	}

}
