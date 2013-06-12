package org.uva.testing;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class ResultWriter {
	private FileWriter writer;
	
	public ResultWriter(String path) throws IOException {
		this.writer = new FileWriter(path);
	}
	public void close() throws IOException {
		if(writer != null)
			writer.close();
	}
	
	public void write(Iterable<TestResult> result) throws IOException{
    	writer.append("Predictor_name,unit,path_length,error\n");
    	
	    for (TestResult r : result) {
	    	String predictor = r.getPredictor().getClass().getSimpleName();
	    	Map<Integer, Error> error = r.getAverageError();
	    	
	    	for(Integer key : error.keySet()) {
		    	writer.append(predictor);
		    	writer.append(",");
	    		writer.append(error.get(key).getUnit().toString());
	    		writer.append(",");
	    		writer.append(key.toString());
	    		writer.append(",");
	    		writer.append(Double.toString(error.get(key).getError()));
	    		writer.append("\n");
	    	}
	    }
	}
	
}
