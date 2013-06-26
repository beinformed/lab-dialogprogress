package com.beinformed.research.labs.dialogprogress.testing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;


class Form {
	public enum ForkType {
		Steps, Time;
	}
	
	private class RandomString
	{
	  private final char[] buf;

	  public RandomString(int length)
	  {
	    if (length < 1)
	      throw new IllegalArgumentException("length < 1: " + length);
	    buf = new char[length];
	  }

	  public String nextString()
	  {
	    for (int idx = 0; idx < buf.length; ++idx) 
	      buf[idx] = (char) ('a' + rnd.nextInt('z' - 'a'));
	    return new String(buf);
	  }

	}
	
	private class FormQuestion {
		private int max;
		private int min;
		private boolean forked;
		private String question;
		
		public FormQuestion(int min, int max, boolean forked) {
			this.max = max;
			this.min = min;
			this.forked = forked;
			this.question = questionGen.nextString();
		}
		public int duration() {
			return (rnd.nextInt(max-min + 1) + min) * 1000;
		}
		public boolean forked() {
			return forked;
		}
		public String question() {
			return question;
		}

	}

	private List<FormQuestion> questions;
	RandomString questionGen = new RandomString(10);
	private Random rnd = new Random();
	private int size, forkPos, importantQuestionPos;
	private ForkType type;
	private String form;
	
	public Form(int size, int forkPos, int importantQuestionPos, ForkType type) {
		this.size = size;
		this.forkPos = forkPos;
		this.importantQuestionPos = importantQuestionPos;
		this.type = type;
		this.questions = new ArrayList<FormQuestion>();
		this.form = questionGen.nextString();
		generateQuestions();
	}
	
	public String getObservation() {
		boolean fork = rnd.nextInt(100) < 80;
		List<FormQuestion> toAsk = new ArrayList<FormQuestion>();
		
		for(int i = 0; i < size; i++)
			if(questions.get(i).forked() == fork || i <= forkPos || type == ForkType.Time)
				toAsk.add(questions.get(i));
		return getString(fork, toAsk);
	}
	
	private String getString(boolean fork, List<FormQuestion> toAsk) {
		long start = System.currentTimeMillis();
		String obsId = UUID.randomUUID().toString();
		String result = "";
		
		for(int i = 0; i < toAsk.size(); i++) {
			FormQuestion q = toAsk.get(i);
			String tag = i == toAsk.size() - 1 ? "OK" : "DATA_MISSING";
		
			int time = q.duration();
			String question = q.question();
			String answer;
			
			if(i == importantQuestionPos && fork)
				answer = "true";
			else if(i == importantQuestionPos && !fork)
				answer = "false";
			else
				answer = Integer.toString(rnd.nextInt(3));
			
			if(this.type == ForkType.Time && fork)
				time += 30;
			
			String line = this.form + "," + obsId + "," + (start + time) + ",'[DataAnchor [" + 
					question + "]]','DataAnchor [" + question + "]=" + answer + "'," + tag + "\n";
			result += line;
			start += time;
		}
		
		return result;
	}

	private void generateQuestions() {
		if(type == ForkType.Steps)
			generateStepsQuestions();
		else
			generateTimeQuestions();
	}

	private void generateStepsQuestions() {
		int split = ((int) ((size - forkPos) * .80)) + forkPos;
		
		for(int i = 0; i < size; i++) {
			int min = rnd.nextInt(75);
			int max = rnd.nextInt(30) + min;
			questions.add(new FormQuestion(min,  max, i >= split));
		}
	}

	private void generateTimeQuestions() {
		for(int i = 0; i < size; i++) {
			//int min = rnd.nextInt(75);
			//int max = rnd.nextInt(30) + min;
			int min = 5, max = 10;
			questions.add(new FormQuestion(min, max, true));
		}
	}
	
}



























