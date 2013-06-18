package org.uva.testing;

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
    	writer.append("title,xlabel,ylabel,linename,xvalue,yvalue\n");
    	
	    for (TestResult r : result) {
	    	for (Graph g : r.getAllGraphs()) {
	    		g.writeCSV(writer);
	    	}
	    }
	}
	
}
