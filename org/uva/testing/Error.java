package org.uva.testing;

public class Error {
	private long time = 0;
	private int steps = 0;
	private int total = 0;
	
	public void add(int steps, long time) {
		this.total++;
		this.steps += steps;
		this.time += time;
	}
	public void add(Error e) {
		this.time += e.time;
		this.steps += e.steps;
		this.total += e.total;
	}
	
	public double getTimeError() {
		return time / (double)total;
	}
	public double getStepsError() {
		return steps / (double)total;
	}
	
	public String toString() {
		return Double.toString(getStepsError()) + " (steps), " + Double.toString(getTimeError()) + " (time)";
	}
}
