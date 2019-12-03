import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CPUScheduler {
	enum Scheduler {
		FCFS, RR, SPN, SRT, HRRN
	}

	int time;
	Scheduler scheduler;
	Map<String, String> schedulerSettings = new HashMap<>();
	List<Process> incoming = new ArrayList<>();
	List<Process> ready = new ArrayList<>();
	List<Process> blocked = new ArrayList<>();
	Process currentProcess;
	Event currentEvent;

	public CPUScheduler(File schedulerFile, File processFile) throws FileNotFoundException {
		Scanner schedulerParser = new Scanner(schedulerFile);
		scheduler = Scheduler.valueOf(schedulerParser.nextLine());
		while (schedulerParser.hasNextLine()) {
			String[] setting = schedulerParser.nextLine().split("=");
			schedulerSettings.put(setting[0], setting[1]);
		}
		schedulerParser.close();

		Scanner processParser = new Scanner(processFile);
		while (processParser.hasNextLine()) {
			incoming.add(new Process(processParser.nextLine()));
		}
		processParser.close();
		incoming.sort((Process p1, Process p2) -> p1.startTime - p2.startTime);
		currentProcess = null;

		System.out.println("USING: " + scheduler);
		System.out.println(schedulerSettings);
		for (Process p : incoming) {
			System.out.println(p);
		}
		System.out.println("\n\n");
	}

	public void simulate() {
		while (!blocked.isEmpty() || !ready.isEmpty() || !incoming.isEmpty()) {
			System.out.println("TIME: " + time);
			// Move from incoming to ready
			if (!incoming.isEmpty() && time >= incoming.get(0).startTime) {
				ready.add(incoming.remove(0));
			}

			// Wait if no process is available, run immediately if only one ready
			if (currentProcess == null) {
				currentEvent = null;
				if (ready.size() == 1) {
					currentProcess = ready.remove(0);
					currentEvent = currentProcess.peek();
					currentEvent.startTime = time;
				}
				if (ready.isEmpty()) {
					time++;
					continue;
				}
			}

			// Move to next event when previous one finishes
			if (!currentEvent.isRunning(time)) {
				currentProcess.pop();
				currentEvent = currentProcess.peek();
				if (currentEvent == null) {
					currentProcess.finishTime = time;
				} else {
					currentEvent.startTime = time;
				}
			}
			if (currentEvent == null) {
				currentProcess = null;
			}

			// Move blocked processes to blocked
			if (currentEvent != null && currentEvent.type.equals("IO")) {
				blocked.add(currentProcess);
				currentProcess = null;
				currentEvent = null;
			}

			// Move unblocked processes to ready
			if (!blocked.isEmpty() && blocked.get(0).peek().isRunning(time)) {
				ready.add(blocked.remove(0));
			}

			System.out.println("\tBEFORE: " + currentProcess);
			switch (scheduler) {
				case FCFS:
					if (currentProcess == null) {
						if (!ready.isEmpty()) {
							currentProcess = ready.remove(0);
							currentEvent = currentProcess.peek();
							if (currentEvent.startTime == 0) {
								currentEvent.startTime = time;
							}
							if (currentProcess.arrivalTime == -1) {
								currentProcess.arrivalTime = time;
							}
						}
					}

					if (currentProcess != null) {
						System.out.println("\tPROCESS: " + currentProcess);
						System.out.println("\tEVENT: " + currentEvent + " - "+ (time - currentEvent.startTime + 1));
					} else {
						System.out.println("NO PROCESS: " + time + " - " + incoming);
					}
				break;

				case RR:

				break;

				case SPN:

				break;

				case SRT:

				break;

				case HRRN:

				break;
			}
			System.out.println("\tAFTER: " + currentProcess);
			time++;
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		CPUScheduler cpu = new CPUScheduler(new File(args[0]), new File(args[1]));
		cpu.simulate();
	}
}
