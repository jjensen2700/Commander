package nick.jgame.util.math;

public final class Vec2f {

	private float	x;

	private float	y;

	public Vec2f(final float x, final float y)
	{

		this.x = x;
		this.y = y;
	}

	public Vec2f abs( )
	{

		return new Vec2f(Math.abs(x), Math.abs(y));
	}

	public Vec2f add(final float r)
	{

		return new Vec2f(x + r, y + r);
	}

	public Vec2f add(final Vec2f r)
	{

		return new Vec2f(x + r.getX( ), y + r.getY( ));
	}

	public float cross(final Vec2f r)
	{

		return (x * r.getY( )) - (y * r.getX( ));
	}

	public Vec2f div(final float r)
	{

		return new Vec2f(x / r, y / r);
	}

	public Vec2f div(final Vec2f r)
	{

		return new Vec2f(x / r.getX( ), y / r.getY( ));
	}

	public float dot(final Vec2f r)
	{

		return (x * r.getX( )) + (y * r.getY( ));
	}

	public boolean equals(final Vec2f r)
	{

		return (x == r.getX( )) && (y == r.getY( ));
	}

	public float getX( )
	{

		return x;
	}

	public float getY( )
	{

		return y;
	}

	public float length( )
	{

		return (float) Math.sqrt((x * x) + (y * y));
	}

	public Vec2f lerp(final Vec2f dest, final float lerpFactor)
	{

		return dest.sub(this).mul(lerpFactor).add(this);
	}

	public float max( )
	{

		return Math.max(x, y);
	}

	public Vec2f mul(final float r)
	{

		return new Vec2f(x * r, y * r);
	}

	public Vec2f mul(final Vec2f r)
	{

		return new Vec2f(x * r.getX( ), y * r.getY( ));
	}

	public Vec2f normalized( )
	{

		float length = length( );

		return new Vec2f(x / length, y / length);
	}

	public Vec2f rotate(final float angle)
	{

		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);

		return new Vec2f((float) ((x * cos) - (y * sin)), (float) ((x * sin) + (y * cos)));
	}

	public Vec2f round( ) {

		return new Vec2f(Math.round(getX( )), Math.round(getY( )));
	}

	public Vec2f set(final float x, final float y) {

		this.x = x;
		this.y = y;
		return this;
	}

	public Vec2f set(final Vec2f r) {

		set(r.getX( ), r.getY( ));
		return this;
	}

	public void setX(final float x)
	{

		this.x = x;
	}

	public void setY(final float y)
	{

		this.y = y;
	}

	public Vec2f sub(final float r)
	{

		return new Vec2f(x - r, y - r);
	}

	public Vec2f sub(final Vec2f r)
	{

		return new Vec2f(x - r.getX( ), y - r.getY( ));
	}

	@Override
	public String toString( )
	{

		return "(" + x + " " + y + ")";
	}

}
