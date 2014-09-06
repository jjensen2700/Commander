package nick.jgame.net.packets;

import nick.jgame.net.*;

public final class Packet01Disconnect extends Packet {

	private final String	userName;

	public Packet01Disconnect(final byte[ ] data) {

		userName = readData(data);
	}

	public Packet01Disconnect(final String name) {

		this.userName = name;
	}

	@Override
	public byte[ ] getData( ) {

		return ("01|" + getUsername( )).getBytes( );
	}

	@Override
	public Type getType( ) {

		return Type.DISCONNECT;
	}

	public String getUsername( ) {

		return userName;
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
