package com.beinformed.research.labs.dialogprogress.testing;

import java.io.FileWriter;
import java.io.IOException;

import com.beinformed.research.labs.dialogprogress.testing.Form.ForkType;

public class DataGenerator {
	
	private FileWriter writer;
	private Form form;
	
	public DataGenerator(String path, Form form) throws IOException {
		this.writer = new FileWriter(path);
		this.form = form;
	}

	public void generate(int n) throws IOException {
		for(int i = 0; i < n; i++) {
			writer.write(form.getObservation());
		}
	}

	public void close() throws IOException {
		if(writer != null)
			writer.close();
	}
}



































