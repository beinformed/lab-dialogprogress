package com.beinformed.research.labs.dialogprogress.testing;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.beinformed.research.labs.dialogprogress.Observation;
import com.beinformed.research.labs.dialogprogress.Question;

/**
 * 
 * Reads a CSV dialog log file, extracts timestamps, question_ids among others.
 * 
 * @param textfile
 *            the CSV file you want to extract data from
 * @return
 * 
 */

public class DataReader {

	BufferedReader dataRdr;
	private List<Observation> observations;
	
	private class DataLine {
		public final String form;
		public final String obsid;
		public final long timestamp;
		public final String question;
		public final String answer;
		public final String type;
		
		public DataLine(String form, String obsid, long timestamp,
				String question, String answer, String type) {
			this.form = form;
			this.obsid = obsid;
			this.timestamp = timestamp;
			this.question = question;
			this.answer = answer;
			this.type = type;
		}
	}

	// string for file location
	public DataReader(String textfile) throws FileNotFoundException {
		dataRdr = new BufferedReader(new FileReader(textfile));
	}

	public List<Observation> getObservations() {
		return observations;
	}

	public void close() throws IOException {
		dataRdr.close();
	}
	
	/**
	 * Method getData() - reads data from buffered readers and makes sure every
	 * observation has the correct form_id
	 * @throws IOException 
	 */
	public void read() throws IOException {
		Map<String, List<DataLine>> lines = getLines();
		List<Observation> observations = new ArrayList<Observation>();
		
		for(String obsid : lines.keySet()) {
			String form = null;
			List<Question> questions = new ArrayList<Question>();
			
			for(DataLine line : lines.get(obsid)) {
				form = line.form;
				questions.addAll(parseQuestion(line));
			}
			
			observations.add(new Observation(true, form, questions));
		}
		this.observations = observations;
	}

	private List<Question> parseQuestion(DataLine line) {
		List<Question> result = new ArrayList<Question>();
		String[] encodedQA = line.answer.split("DataAnchor ");
		
		for(String s : encodedQA) {
			String encodedQuestion = s.endsWith(",") ? s.substring(0, s.length() - 1) : s;
			result.add(buildQuestion(encodedQuestion, line.timestamp, line.type));
		}
		
		return result;
	}

	private Question buildQuestion(String encodedQuestion, long timestamp,
			String type) {
		String[] parts = encodedQuestion.split("=");
		if(parts.length < 2)
			return new Question(encodedQuestion, "", timestamp, type);
		
		String question = parts[0];
		String answer = parts[1];
		
		return new Question(cleanQuestion(question), answer, timestamp, type);
	}

	private String cleanQuestion(String question) {
		// Questions look like this: [object[index]|key]
		// We want them to look like this: object|key
		
		// remove outer [ and ]
		String real = question.substring(1, question.length() - 1);
		// remove index
		real = real.replaceAll("\\[.*?\\]", "");
		
		return real;
	}

	private Map<String, List<DataLine>> getLines() throws IOException {
		Map<String, List<DataLine>> lines = new HashMap<String, List<DataLine>>();
		
		String line = dataRdr.readLine();
		while(line != null) {
			DataLine dataLine = readLine(line);
			
			if(!lines.containsKey(dataLine.obsid))
				lines.put(dataLine.obsid, new ArrayList<DataLine>());
			lines.get(dataLine.obsid).add(dataLine);
			line = dataRdr.readLine();
		}
		
		return lines;
	}
	
	private DataLine readLine(String line) {
		String[] parts = splitLine(line);
		
		return new DataLine(parts[0], parts[1], Long.parseLong(parts[2]), parts[3], parts[4], parts[5]);
	}
	
	private String[] splitLine(String line) {
		String[] parts = line.split("'");
		List<String> result = new ArrayList<String>();
		
		for(int i = 0; i < parts.length; i++) {
			if(i % 2 == 0) {				
				String[] split = trimCommas(parts[i]).split(",");
				for(String s : split)
					result.add(s);
			} else {
				result.add(parts[i]);
			}
		}
		return result.toArray(new String[0]);
	}
	
	private String trimCommas(String part) {
		String result = part;
		if(part.startsWith(",")) {
			result = part.substring(1);
			if(result.isEmpty())
				return part;
		}
		return result;
	}
}




























