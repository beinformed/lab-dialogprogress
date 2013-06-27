package com.beinformed.research.labs.dialogprogress;

import java.util.Set;
import java.util.TreeSet;

class PathInfo {

	private int freq;
	private long total;
	private PredictionUnit unit;
	private Set<Path> children;
	private Path path;
	
	public PathInfo(PredictionUnit unit, Path path) { 
		this.unit = unit;
		this.path = path;
		this.children = new TreeSet<Path>();
	}
	public PathInfo(PredictionUnit unit, Path path, Observation obs, Observation complete) {
		this(unit, path);
		add(obs, complete);
	}
	
	public void add(Observation obs, Observation complete) {
		freq += 1;
		total += complete.getLearnValue(unit) - obs.getLearnValue(unit);
	}
	public void addChild(Path path) {
		if(!path.equals(this.path))
			children.add(path);
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
	public Path getParentPath() {
		return path.getParent();
	}
	public Path getPath() {
		return path;
	}

}
