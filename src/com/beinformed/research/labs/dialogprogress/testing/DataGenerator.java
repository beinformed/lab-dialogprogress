package com.beinformed.research.labs.dialogprogress.testing;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DataGenerator {
	
	private class Form {
		final int totalq;
		final int askedmin;
		final int askedmax;
		private Map<Integer, Integer> qmin = new HashMap<Integer, Integer>();
		private Map<Integer, Integer> qmax = new HashMap<Integer, Integer>();
		
		public Form() {
			this.totalq = rand.nextInt(maxQuestionsPerForm - minQuestionsPerForm) + minQuestionsPerForm;
			this.askedmin = rand.nextInt(totalq / 3);
			this.askedmax = askedmin + rand.nextInt(totalq - askedmin) + 1;
		}
		
		public int duration(int q) {
			if(!qmin.containsKey(q))
				generateQRange(q);
			
			return rand.nextInt(qmax.get(q) - qmin.get(q)) + qmin.get(q);
		}

		private void generateQRange(int q) {
			qmin.put(q, rand.nextInt(300) + 10);
			qmax.put(q, qmin.get(q) + rand.nextInt(qmin.get(q) * 2) + 1);
		}
	}
	
	/*
	 * Form 1: 20 questions
	 * * * * * * * * * * * * *
	 * between 10 and 15 questions are asked. 
	 * 
	 * Form 2: 30 questions
	 * between 10 and 20 questions are asked.
	 */
	private FileWriter writer;
	private Random rand;	
	private Form[] forms;
	private int minQuestionsPerForm = 15;
	private int maxQuestionsPerForm = 50;

	public DataGenerator(String path) throws IOException {
		this.writer = new FileWriter(path);
		writer.write("observation_id, form_id, question_id, timestamp, answer\n");
		this.rand = new Random();
		this.forms = new Form[] { 
				new Form()
			};
	}

	public void generate(int n) throws IOException {	
		for(int i = 0; i < n; i++) {
			int formid = rand.nextInt(forms.length);
			Form form = forms[formid];
			List<Long> questions = new ArrayList<Long>();
			long base = System.currentTimeMillis();
			int noQuestions = form.askedmin + rand.nextInt(form.askedmax - form.askedmin);
			for(int j = 0; j < noQuestions; j++) {
				int q = rand.nextInt(form.totalq);
				int time = form.duration(q);
				base += time;
				questions.add(base);
			}
			write(i, formid, questions);
		}
	}
	
	private void write(int observation, int formid, List<Long> questions) throws IOException {
		String form = Integer.toString(formid);
		String obs = Integer.toString(observation);
		
		for(Long l : questions) {
			writer.write(obs + "," + form + "," + "q," + l.toString() + ",a\n");
		}
	}

	public void close() throws IOException {
		if(writer != null)
			writer.close();
	}
}



































