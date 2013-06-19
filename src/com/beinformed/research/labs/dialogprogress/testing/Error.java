package com.beinformed.research.labs.dialogprogress.testing;

import com.beinformed.research.labs.dialogprogress.PredictionUnit;

public class Error {
	private int total = 0;
	private int count = 0;
	private PredictionUnit unit;
	
	public Error(PredictionUnit unit) {
				this.unit = unit;
	}
	
	public void add(int error) {
		this.count++;
		this.total += error;
	}
	public void add(Error e) {
		this.total += e.total;
		this.count += e.count;
	}
	
	public double getError() {
		return total / (double)count;
	}
	public PredictionUnit getUnit() {
		return unit;
	}
	
	public String toString() {
		return Double.toString(getError()) + " (" + unit.toString() + ").";
	}
}
