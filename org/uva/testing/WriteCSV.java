package org.uva.testing;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class WriteCSV {
	Iterable<TestResult> result;
	String predictor;
	Map<Integer, Error> error;
	
	public static void write(Iterable<TestResult> result){
		//Users/highintothesky/Desktop/nieuwtest.csv
		try
		{
		    FileWriter writer = new FileWriter("/Users/highintothesky/Desktop/nieuwtest.csv");

	    	writer.append("Predictor_name,path_length,error,error_unit\n");
	    	
		    for (TestResult r : result) {
		    	String predictor = r.getPredictor().toString();
		    	Map<Integer, Error> error = r.getAverageError();
		    	
		    	for(Integer key : error.keySet()) {
			    	writer.append(predictor);
			    	writer.append(",");
		    		writer.append(key.toString());
		    		writer.append(",");
		    		writer.append(Double.toString(error.get(key).getError()));
		    		writer.append(",");
		    		writer.append(error.get(key).getUnit().toString());
		    		writer.append("\n");
		    	}
		    }

		    writer.flush();
		    writer.close();
		    
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	}
	
	

}
