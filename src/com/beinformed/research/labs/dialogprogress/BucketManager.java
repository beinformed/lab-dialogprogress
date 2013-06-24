package com.beinformed.research.labs.dialogprogress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BucketManager {
	private Bucket[] buckets;
	private PredictionUnit unit;
	
	public BucketManager(int noBuckets, Iterable<Observation> data, PredictionUnit unit) {
		List<Integer> values = new ArrayList<Integer>();
		
		for(Observation o : data) {
			values.add(o.getLearnValue(unit));
		}
		Collections.sort(values);
		
		int max = values.get(values.size() - 1);
		int bucketSize = max / noBuckets;
		
		Bucket[] buckets = new Bucket[noBuckets];
		int end = max;
		for(int i = 1; i < noBuckets; i++) {
			int start = max - (bucketSize * i);
			buckets[noBuckets - i] = new Bucket(start + 1, end);
			end = start;
		}
		buckets[0] = new Bucket(0, end);
		this.buckets = buckets;
		this.unit = unit;
	}
	
	public Bucket getBucket(Observation obs) {
		int value = obs.getLearnValue(unit);
		
		for(Bucket b : buckets) {
			if(value >= b.lower && value <= b.upper)
				return b;
		}
		
		throw new IllegalArgumentException("Observation doesn't fit any bucket");
	}
	
	public double[] getBucketVector(Observation obs) {
		int value = obs.getLearnValue(unit);
		return getBucketVector(value);
	}
	public double[] getBucketVector(int learnValue) {
		double[] result = new double[buckets.length];
		for(int i = 0; i < buckets.length; i++) {
			if(learnValue >= buckets[i].lower && learnValue <= buckets[i].upper)
				result[i] = 1;
		}
		return result;
	}
	
	public Bucket fromVectorIndex(int index) {
		return buckets[index];
	}
}
