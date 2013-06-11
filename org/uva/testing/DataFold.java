package org.uva.testing;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DataFold<T> {
	private Collection<T> test;
	private Collection<T> train;
	
	public static <T> Collection<DataFold<T>> allFromList(List<T> all, int noFolds) {
		Collections.shuffle(all);
		int foldSize = all.size() / noFolds;
		Collection<DataFold<T>> result = new ArrayList<DataFold<T>>();
		
		for(int f = 0; f < noFolds; f++) {
			Collection<T> train = new ArrayList<T>();
			Collection<T> test = new ArrayList<T>();
			int start = f * foldSize;
			int end = (f+1) * foldSize;
			
			for(int i = 0; i < all.size(); i++) {
				if(i >= start && i < end)
					test.add(all.get(i));
				else
					train.add(all.get(i));
			}
			
			result.add(new DataFold<T>(test, train));
		}
		
		return result;
		
	}
	
	public DataFold(Collection<T> testSet, Collection<T> trainingSet) {
		this.test = testSet;
		this.train = trainingSet;
	}
	
	public Collection<T> getTestSet() {
		return test;
	}
	public Collection<T> getTrainingSet() {
		return train;
	}
}
