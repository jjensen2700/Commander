package nick.jgame.entity.ai;

import nick.jgame.init.Materials;
import nick.jgame.world.Tile;

public final class Node {

	public static Node getNodeFromTile(final int x, final int y, final Tile t) {

		return new Node(x, y, t.getTileMat( ).getTerrainCost( ), t.getTileMat( ) == Materials.liquid);
	}

	private float	cost;

	private boolean	water;

	private int		xLoc, yLoc;

	public Node(final int x, final int y, final float terrainCost, final boolean isWater) {

		this.xLoc = x;
		this.yLoc = y;
		this.water = isWater;
		this.cost = terrainCost;
	}

	public float getCost( ) {

		return cost;
	}

	public int getxLoc( ) {

		return xLoc;
	}

	public int getyLoc( ) {

		return yLoc;
	}

	public boolean hasProperties(final int x, final int y, final boolean isWater) {

		return (xLoc == x) && (yLoc == y) && (isWater == water);
	}

	public boolean isWater( ) {

		return water;
	}

}
