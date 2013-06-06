package org.uva.predictions;

/**
 * Represents a range of values.
 * 
 *
 */
public class Range {
	int lower;
	int upper;
	
	public static Range fromConfidence(int value, double confidence) {
		int deviation = (int) ((1-confidence) * value);
		
		return new Range(value - deviation, value + deviation);
	}
	
	/**
	 * 
	 * @param lowerBound
	 * The lower bound of the range (inclusive).
	 * @param upperBound
	 * The upper bound of the range (inclusive).
	 */
	public Range(int lowerBound, int upperBound) {
		assert lower < upper;
		
		this.lower = lowerBound;
		this.upper = upperBound;
	}
	
	/**
	 * Returns the number of integer values in the range.
	 * @return
	 */
	public int getSize() {
		return upper - lower;
	}
	/**
	 * Returns the lower bound of the range (inclusive).
	 * @return
	 */
	public int getLowerBound() {
		return lower;
	}
	/**
	 * Returns the upper bound of the range (inclusive).
	 * @return
	 */
	public int getUpperBound() {
		return upper;
	}
	
	/**
	 * {@code true} if this range contains the integer, {@code false} otherwise.
	 * @param a
	 * The value to check.
	 * @return
	 */
	public boolean contains(int a) {
		return a >= lower && a <= upper;
	}
}
