package com.beinformed.research.labs.dialogprogress.testing;

import java.util.*;
import java.io.BufferedReader;
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

	// string for file location
	public DataReader(String textfile) {
		try {
			dataRdr = new BufferedReader(new FileReader(textfile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method getData() - reads data from buffered readers and makes sure every
	 * observation has the correct form_id
	 */
	public List<Observation> getData() {
		Map<Integer, List<Question>> questions = new HashMap<Integer, List<Question>>();
		Map<Integer, String> form = new HashMap<Integer, String>();
		List<Observation> data = new ArrayList<Observation>();

		/* Read forms and save in map: forms */
		try {
			/*
			 * read data file and makes sure every observation has correct form
			 * associated
			 */
			String line = dataRdr.readLine();
			// skip header
			line = dataRdr.readLine();
			while (line != null) {
				StringTokenizer tokenizer = new StringTokenizer(line, ",\n\t");
				while (tokenizer.hasMoreTokens() && !line.startsWith("#")) {
					int observationId = Integer.parseInt(tokenizer.nextToken());
					String formId = tokenizer.nextToken().trim();

					form.put(observationId, formId);
					if (!questions.containsKey(observationId)) {
						questions.put(observationId, new ArrayList<Question>());
					}
					String questionId = tokenizer.nextToken().trim();
					String rawTime = tokenizer.nextToken().trim();
					if (rawTime.isEmpty())
						rawTime = "0";
					long timestamp = Long.parseLong(rawTime);
					String answer = tokenizer.nextToken().trim();
					questions.get(observationId).add(
							new Question(questionId, answer, timestamp));
				}
				line = dataRdr.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<Question> ob = new ArrayList<Question>();
		for (Integer obId : questions.keySet()) {
			ob = questions.get(obId);
			data.add(new Observation(true, form.get(obId), ob));
		}
		return data;
	}
}
