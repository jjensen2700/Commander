package nick.jgame;

import java.net.Inet4Address;

import nick.jgame.net.*;
import nick.jgame.opts.Options;
import nick.jgame.util.debug.GameLog;

public final class NetworkHandler {

	private static Client	client;

	private static Server	server;

	public static Client getClient( ) {

		return client;
	}

	public static Server getServer( ) {

		return server;
	}

	public static void sendPacketToClient(final Packet p) {

		p.writeDataToClient(server);
	}

	public static void sendPacketToServer(final Packet p) {

		p.writeDataToServer(client);
	}

	public static void startClient( ) {

		if ((client != null) && client.isAlive( )) { return; }
		try {
			client = new Client((Inet4Address) Inet4Address.getLocalHost( ), Constants.port);
			client.start( );
		} catch (Exception e) {
			GameLog.warn(e);
		}
	}

	public static void startServer( ) {

		if (!Options.getBoolOption("runserver") || server.isRunning( )) { return; }
		server = new Server(Constants.port);
		server.start( );
	}

}
