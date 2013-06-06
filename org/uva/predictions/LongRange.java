package org.uva.predictions;

/**
 * Represents a range of values.
 * 
 *
 */
public class LongRange {
	long lower;
	long upper;
		
	public static LongRange fromConfidence(long value, double confidence) {
		int deviation = (int) ((1-confidence) * value);
		
		return new LongRange(value - deviation, value + deviation);
	}
	
	/**
	 * 
	 * @param lowerBound
	 * The lower bound of the range (inclusive).
	 * @param upperBound
	 * The upper bound of the range (inclusive).
	 */
	public LongRange(long lowerBound, long upperBound) {
		assert lower < upper;
		
		this.lower = lowerBound;
		this.upper = upperBound;
	}
	
	/**
	 * Returns the number of integer values in the range.
	 * @return
	 */
	public long getSize() {
		return upper - lower;
	}
	/**
	 * Returns the lower bound of the range (inclusive).
	 * @return
	 */
	public long getLowerBound() {
		return lower;
	}
	/**
	 * Returns the upper bound of the range (inclusive).
	 * @return
	 */
	public long getUpperBound() {
		return upper;
	}
	
	/**
	 * {@code true} if this range contains the integer, {@code false} otherwise.
	 * @param l
	 * The value to check.
	 * @return
	 */
	public boolean contains(long l) {
		return l >= lower && l <= upper;
	}

	public long getDifference(long l) {
		if(contains(l))
			return 0;
		else if(l < lower)
			return lower - l;
		else
			return l - upper;
	}
}



























