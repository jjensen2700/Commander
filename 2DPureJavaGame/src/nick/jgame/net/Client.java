package nick.jgame.net;

import java.net.*;

import nick.jgame.*;
import nick.jgame.entity.owners.EntityPlayerMP;
import nick.jgame.init.Guis;
import nick.jgame.net.packets.*;
import nick.jgame.util.debug.GameLog;

public final class Client extends Thread {

	private DatagramSocket		clientSocket;

	private final Inet4Address	ipAt;

	private final short			port;

	public Client(final Inet4Address ip, final short port) {

		ipAt = ip;
		this.port = port;
		try {
			clientSocket = new DatagramSocket( );
		} catch (Exception e) {
			GameLog.warn(e);
		}
	}

	private void handleLogin(final Packet00Login packet, final Inet4Address address, final short port) {

		GameLog.info("[" + address.getHostAddress( ) + ":" + port + "] " + packet.getUsername( )
				+ " has joined the game...", false);
		EntityPlayerMP player = new EntityPlayerMP(Guis.world, packet.getUsername( ), address, port);
		Guis.world.spawnIn(player);
	}

	private void parsePacket(final byte[ ] data, final Inet4Address address, final int port) {

		final String message = new String(data).trim( );
		final Packet.Type type = Packet.Type.getType(message.substring(0, 2));
		Packet packet = null;
		switch (type) {
			default:
			case INVALID:
				break;
			case LOGIN:
				packet = new Packet00Login(data);
				handleLogin((Packet00Login) packet, address, (short) port);
				break;
			case DISCONNECT:
				packet = new Packet01Disconnect(data);
				System.out.println("[" + address.getHostAddress( ) + ":" + port + "] "
						+ ((Packet01Disconnect) packet).getUsername( ) + " has left the world...");
				Guis.world.removePlayerMP(((Packet01Disconnect) packet).getUsername( ));
				break;
			case MOVE:
				packet = new Packet02Move(data);
				Guis.world.moveEntity((Packet02Move) packet);
		}
	}

	@Override
	public void run( ) {

		while (MainGame.isRunning( )) {

			final byte[ ] data = new byte[Constants.maxPacketLength];
			final DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				clientSocket.receive(packet);
			} catch (Exception e) {
				GameLog.warn(e);
			}
			parsePacket(packet.getData( ), (Inet4Address) packet.getAddress( ), packet.getPort( ));

		}
	}

	public void sendData(final byte[ ] data) {

		final DatagramPacket packet = new DatagramPacket(data, data.length, ipAt, port);
		try {
			clientSocket.send(packet);
		} catch (Exception e) {
			GameLog.warn(e);
		}

	}

}
