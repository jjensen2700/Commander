package nick.jgame.input;

import java.util.HashMap;

public final class KeyBinding {

	private static final HashMap<String, KeyBinding>	bindings	= new HashMap<>( );

	public static final boolean isDown(final KeyBinding action) {

		return Keyboard.isKeyDown(action.boundKey);
	}

	private String	action;

	private int		boundKey;

	public KeyBinding(final int key, final String name) {

		boundKey = key;
		action = name;
		bindings.put(action, this);
	}

	public boolean bindingEqual(final KeyBinding binding) {

		return (binding.action == action) & (binding.boundKey == boundKey);
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof KeyBinding) {
			return bindingEqual((KeyBinding) obj);
		} else {
			return false;
		}
	}

	public String getName( ) {

		return action;
	}

}
