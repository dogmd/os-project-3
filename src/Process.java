import java.util.*;

public class Process {
	int arrivalTime = -1;
	int serviceTime;
	int startTime;
	int finishTime;
	int turnAroundTime;
	List<Event> events = new ArrayList<>();

	public Process(String process) {
		Scanner parser = new Scanner(process);
		startTime = parser.nextInt();
		while (parser.hasNext()) {
			String type = parser.next();
			int time = parser.nextInt();
			events.add(new Event(type, time));
		}
		parser.close();
	}

	public Event peek() {
		return events.isEmpty() ? null : events.get(0);
	}

	public Event pop() {
		return events.isEmpty() ? null : events.remove(0);
	}

	public String toString() {
		return "(" +  startTime + ", " + events.toString() + ")";
	}
}
