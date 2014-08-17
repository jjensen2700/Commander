package nick.jgame.api;

public @interface ModInfoHeader {

	public String name( ) default "";

	public String neededGameVersion( ) default "|";

	public String version( ) default "1.0";
}
