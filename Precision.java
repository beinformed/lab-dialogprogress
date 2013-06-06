
public class Precision {
	private final double timePrecision;
	private final double stepsPrecision;
	
	public Precision(double time, double steps) {
		this.timePrecision = time;
		this.stepsPrecision = steps;
	}
	
	public double getTimePrecision() {
		return timePrecision;
	}
	public double getStepsPrecision() {
		return stepsPrecision;
	}
	
	public String toString() {
		return Double.toString(timePrecision) + " (time), " + Double.toString(stepsPrecision) + " (steps)";
	}
}
