package com.beinformed.research.labs.dialogprogress.testing;

import java.io.FileWriter;
import java.io.IOException;

public class DataGenerator {
	
	private FileWriter writer;
	
	public DataGenerator(String path) throws IOException {
		this.writer = new FileWriter(path);
	}

	public void generate(int n) throws IOException {
		Form form = new Form();
		for(int i = 0; i < n; i++) {
			writer.write(form.generateObservation());
		}
	}

	public void close() throws IOException {
		if(writer != null)
			writer.close();
	}
}



































