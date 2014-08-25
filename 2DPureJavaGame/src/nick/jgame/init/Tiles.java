package nick.jgame.init;

import nick.jgame.world.Tile;
import nick.jgame.world.tiles.*;

public final class Tiles {

	public static final Tile	air		= AirTile.getInstance( );

	public static final Tile	grass	= GrassTile.getInstance( );

	public static final Tile	stone	= StoneTile.getInstance( );

	public static final Tile	water	= WaterTile.getInstance( );
}
