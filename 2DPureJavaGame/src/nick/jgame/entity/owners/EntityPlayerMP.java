package nick.jgame.entity.owners;

import java.net.Inet4Address;

import nick.jgame.world.World;

public final class EntityPlayerMP extends EntityPlayer {

	private Inet4Address	ip;

	private short			port;

	public EntityPlayerMP(final World w, final String name, final Inet4Address ip, final short port) {

		super(w, name);
		this.port = port;
		this.ip = ip;
	}

	public Inet4Address getIp( ) {

		return ip;
	}

	public short getPort( ) {

		return port;
	}

	public void setIp(final Inet4Address newIP) {

		ip = newIP;

	}

	public void setPort(final short newPort) {

		port = newPort;
	}

}
