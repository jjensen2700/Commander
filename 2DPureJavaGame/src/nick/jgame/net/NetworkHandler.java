package nick.jgame.net;

import java.net.Inet4Address;

import nick.jgame.Constants;
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

		server.sendDataToAllClients(p.getData( ));
	}

	public static void sendPacketToServer(final Packet p) {

		client.sendData(p.getData( ));
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

		if (!Options.getBoolOption("runserver") || ((server != null) && server.isRunning( ))) { return; }
		server = new Server(Constants.port);
		server.start( );
	}

	public static void stopServer( ) {

		server.stopServer( );
	}

}
