package com.beinformed.research.labs.dialogprogress;

class Bucket {
	public final int lower;
	public final int upper;
	
	public Bucket(int lower, int upper){
		this.lower = lower;
		this.upper = upper;
	}
	public String toString() {
		return "[" + lower + ", " + upper + "]";
	}
}
