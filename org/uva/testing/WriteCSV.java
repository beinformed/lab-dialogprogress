package org.uva.testing;

import java.io.FileWriter;
import java.io.IOException;

public class WriteCSV {
	Iterable<TestResult> result;
	
	public static void write(Iterable<TestResult> result){
		//Users/highintothesky/Desktop/nieuwtest.csv
		try
		{
		    FileWriter writer = new FileWriter("/Users/highintothesky/Desktop/nieuwtest.csv");

		    for (TestResult r : result) {
		    	writer.append(r.toString());
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
