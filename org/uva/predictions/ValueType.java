package org.uva.predictions;

public enum ValueType{
	Question (1), 
	Answer (2), 
	TimeStamp (4), 
	Status (8);
	
	private final int value;

	ValueType(int value) {
		this.value = value;
	}
}
