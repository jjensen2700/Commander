package nick.jgame.entity.ai;

import java.util.ArrayList;

import nick.jgame.util.math.Vec2i;
import nick.jgame.world.World;

public final class Path {

	private final boolean			crossWater, terrainCost;

	private final Vec2i				distance;

	private final ArrayList<Node>	nodeList	= new ArrayList<>( );

	private float					pathCost	= 0;

	private final ArrayList<Node>	posNodes	= new ArrayList<>( );

	private final Vec2i				start, end;

	private World					world;

	public Path(final World w, final Vec2i start, final Vec2i end, final boolean canCrossWater,
			final boolean useTerrainCost) {

		this.world = w;
		this.start = start.round( );
		this.end = end.round( );
		this.crossWater = canCrossWater;
		this.terrainCost = useTerrainCost;
		this.distance = start.sub(end);
	}

	private void addNode(final Node toAdd) {

		nodeList.add(toAdd);
	}

	public void calc( ) {

		boolean goUp = false;
		if (distance.getY( ) < 0) {
			goUp = true;
		}

		boolean goLeft = false;
		if (distance.getX( ) < 0) {
			goLeft = true;
		}

		for (int x = (int) start.getX( ); x != end.getX( );) {
			for (int y = (int) start.getY( ); y != end.getY( );) {

				posNodes.add(Node.getNodeFromTile(x, y, world.getTile((short) x, (short) y)));

				if (goUp) {
					y--;
				} else {
					y++;
				}
			}

			if (goLeft) {
				x--;
			} else {
				x++;
			}
		}

		cleanUpNodeList( );

		float slope = distance.getY( ) / distance.getX( );

		for (int x = (int) start.getX( ); x != end.getX( );) {
			for (int y = (int) start.getY( ); y != end.getY( );) {

				this.addNode(this.getSameNode(x, y));

				if (goUp) {
					y -= slope;
				} else {
					y += slope;
				}
			}

			if (goLeft) {
				x--;
			} else {
				x++;
			}
		}
		calcCost( );
	}

	private void calcCost( ) {

		pathCost = 0;
		for (Node part : nodeList) {
			pathCost += part.getCost( );
		}

	}

	private void cleanUpNodeList( ) {

		for (int i = 0; i < posNodes.size( ); i++) {
			Node pos = posNodes.get(i);
			if (pos.isWater( ) != crossWater) {
				posNodes.remove(i);
			}
		}
	}

	public Vec2i getEnd( ) {

		return end;
	}

	public float getPathCost( ) {

		return pathCost;
	}

	private Node getSameNode(final int x, final int y) {

		for (Node node : posNodes) {
			if (node.hasProperties(x, y, crossWater)) { return node; }
		}
		return null;
	}

	public Vec2i getStart( ) {

		return start;
	}

	public boolean isCrossingWater( ) {

		return crossWater;
	}

	public boolean isUsingTerrainCost( ) {

		return terrainCost;
	}

}
