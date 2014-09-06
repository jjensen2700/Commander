package nick.jgame.net.packets;

import nick.jgame.net.*;
import nick.jgame.world.Tile;

public final class Packet03Tile extends Packet {

	private Tile	tile;

	private short	xLoc;

	private short	yLoc;

	public Packet03Tile(final byte[ ] data) {

		final String[ ] msg = readData(data).split(",");
		tile = Tile.getAt(msg[0]);
		xLoc = Short.parseShort(msg[1]);
		yLoc = Short.parseShort(msg[2]);
	}

	public Packet03Tile(final Tile t, final short x, final short y) {

		tile = t;
		xLoc = x;
		yLoc = y;
	}

	@Override
	public byte[ ] getData( ) {

		return ("03|" + tile.getName( ) + "," + xLoc + "," + yLoc).getBytes( );
	}

	public final Tile getTile( ) {

		return tile;
	}

	@Override
	public Type getType( ) {

		return Type.TILE;

	}

	public final short getxLoc( ) {

		return xLoc;
	}

	public final short getyLoc( ) {

		return yLoc;
	}

	@Override
	public void writeDataToClient( ) {

		NetworkHandler.sendPacketToClient(this);

	}

	@Override
	public void writeDataToServer( ) {

		NetworkHandler.sendPacketToServer(this);

	}

}
