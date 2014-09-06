package nick.jgame.net.packets;

import nick.jgame.net.*;

public final class Packet00Login extends Packet {

	private final String	userName;

	public Packet00Login(final byte[ ] data) {

		userName = readData(data);

	}

	public Packet00Login(final String name) {

		this.userName = name;
	}

	@Override
	public byte[ ] getData( ) {

		return ("00|" + getUsername( )).getBytes( );
	}

	@Override
	public Type getType( ) {

		return Type.LOGIN;
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
