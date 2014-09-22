package nick.jgame.util.math;

public final class Vec2i {

	private int	x;

	private int	y;

	public Vec2i(final int x, final int y)
	{

		this.x = x;
		this.y = y;
	}

	public Vec2i abs( )
	{

		return new Vec2i(Math.abs(x), Math.abs(y));
	}

	public Vec2i add(final int r)
	{

		return new Vec2i(x + r, y + r);
	}

	public Vec2i add(final Vec2i r)
	{

		return new Vec2i(x + r.getX( ), y + r.getY( ));
	}

	public float cross(final Vec2i r)
	{

		return (x * r.getY( )) - (y * r.getX( ));
	}

	public Vec2i div(final int r)
	{

		return new Vec2i(x / r, y / r);
	}

	public Vec2i div(final Vec2i r)
	{

		return new Vec2i(x / r.getX( ), y / r.getY( ));
	}

	public float dot(final Vec2i r)
	{

		return (x * r.getX( )) + (y * r.getY( ));
	}

	public boolean equals(final Vec2i r)
	{

		return (x == r.getX( )) && (y == r.getY( ));
	}

	public int getX( )
	{

		return x;
	}

	public int getY( )
	{

		return y;
	}

	public float length( )
	{

		return (float) Math.sqrt((x * x) + (y * y));
	}

	public Vec2i lerp(final Vec2i dest, final int lerpFactor)
	{

		return dest.sub(this).mul(lerpFactor).add(this);
	}

	public float max( )
	{

		return Math.max(x, y);
	}

	public Vec2i mul(final int r)
	{

		return new Vec2i(x * r, y * r);
	}

	public Vec2i mul(final Vec2i r)
	{

		return new Vec2i(x * r.getX( ), y * r.getY( ));
	}

	public Vec2i normalized( )
	{

		int length = (int) length( );

		return new Vec2i(x / length, y / length);
	}

	public Vec2i rotate(final int angle)
	{

		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);

		return new Vec2i((int) ((x * cos) - (y * sin)), (int) ((x * sin) + (y * cos)));
	}

	public Vec2i round( ) {

		return new Vec2i(Math.round(getX( )), Math.round(getY( )));
	}

	public Vec2i set(final int x, final int y) {

		this.x = x;
		this.y = y;
		return this;
	}

	public Vec2i set(final Vec2i r) {

		set(r.getX( ), r.getY( ));
		return this;
	}

	public void setX(final int x)
	{

		this.x = x;
	}

	public void setY(final int y)
	{

		this.y = y;
	}

	public Vec2i sub(final int r)
	{

		return new Vec2i(x - r, y - r);
	}

	public Vec2i sub(final Vec2i r)
	{

		return new Vec2i(x - r.getX( ), y - r.getY( ));
	}

	@Override
	public String toString( )
	{

		return "(" + x + " " + y + ")";
	}

}
