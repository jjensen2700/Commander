package nick.jgame.entity;

public enum EnumEntityType {
	ENTITY, INVALID, OWNER;

	public static EnumEntityType getType(final String type) {

		for (EnumEntityType e : EnumEntityType.values( )) {
			if (e.toString( ).equalsIgnoreCase(type)) { return e; }
		}

		return INVALID;
	}

}
