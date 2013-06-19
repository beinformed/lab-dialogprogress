package com.beinformed.research.labs.dialogprogress;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PathInfo {

	private int freq;
	private long total;
	private PredictionUnit unit;
	private Set<Path> children;
	
	public PathInfo(PredictionUnit unit) { 
		this.unit = unit;
		this.children = new TreeSet<Path>();
	}
	public PathInfo(PredictionUnit unit, Observation obs, Observation complete) {
		this(unit);
		add(obs, complete);
	}
	
	public void add(Observation obs, Observation complete) {
		freq += 1;
		total += complete.getLearnValue(unit) - obs.getLearnValue(unit);
	}
	public void addChild(Path path) {
		this.children.add(path);
	}
	
	public int getFrequency() {
		return freq;
	}
	public int getPrediction() {
		return (int) (total / freq);
	}
	public Iterable<Path> getChildren() {
		return children;
	}

}
