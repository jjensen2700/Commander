package nick.jgame.events;

public abstract class BasicEvent {

	protected final long	timeOccured;

	public BasicEvent(final long timeHappened) {

		this.timeOccured = timeHappened;
	}

	public final long getTimeOccured( ) {

		return timeOccured;
	}

}
