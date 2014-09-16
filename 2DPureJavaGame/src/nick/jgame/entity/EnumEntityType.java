package nick.jgame.entity;

public enum EnumEntityType {
	ENTITY, INVALID, OWNER;

	public static EnumEntityType getType(final String type) {

		if (type.equalsIgnoreCase("entity")) {
			return ENTITY;
		} else if (type.equalsIgnoreCase("owner")) { return OWNER; }
		return INVALID;
	}

}
