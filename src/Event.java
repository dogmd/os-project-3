public class Event {
	int duration;
	int startTime;
	String type;
	public Event(String type, int duration) {
		this.duration = duration;
		this.type = type;
	}

	public boolean isRunning(int time) {
		return (time - duration + 1) <= startTime;
	}

	public String toString() {
		return ("(" + type + ": " + duration + ")");
	}
}