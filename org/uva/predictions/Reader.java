package org.uva.predictions;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



/**
 * 
 * @author Niels Backer
 * Reads a CSV dialog log file, extracts timestamps, question_ids among others.
 * 
 * @param	textfile	the CSV file you want to extract data from
 * @return				
 *
 */

public class Reader {
	// string for file location
	public static void main(String args[]){
		String textfile, formfile;
		textfile = args[0];
		formfile = args[1];
		getData(textfile, formfile);
	}
	
	// reads the textfile
	public static void getData(String textfile, String formfile){
		Map observations = new HashMap<Integer, List<Question>>();
		try {
			BufferedReader src = new BufferedReader(new FileReader(textfile));
			BufferedReader formfRdr = new BufferedReader(new FileReader(formfile));
			String str = src.readLine();
			while(str != null){
				StringTokenizer st = new StringTokenizer(str,
				",\n\t");
				while (st.hasMoreTokens()) {
					int observation_id = Integer.parseInt(st.nextToken());
					if ( ! observations.containsKey( observation_id ) ){
						observations.put( observation_id, new ArrayList<Question>() );
					}
					//int form_id = Integer.parseInt(st.nextToken());
					long timestamp = Long.parseLong(st.nextToken());
					int question_id = Integer.parseInt(st.nextToken());
					String answer = st.nextToken();
					observations.get(observation_id).add( new Question( question_id, answer, timestamp ));	
					
					//System.out.printf("observation: %d form number: %d timestamp: %f question id: %d answer: %s \n", 
					//		observation_id, form_id, timestamp, question_id, answer);
				}
				//System.out.println("volgende vraag");
				str = src.readLine();
			}
		
		int form_id, nrOfQuestions;
		str = formfRdr.readLine();
		while(str != null ){
			StringTokenizer st = new StringTokenizer(str,",\n\t");
				while (st.hasMoreTokens()) {
					form_id	= Integer.parseInt(st.nextToken());
					nrOfQuestions = Integer.parseInt(st.nextToken());
				}
		}
		Form form = new Form( form_id, nrOfQuestions );		

		List<Observation> data = new ArrayList<Observation>();
		for ( ArrayList<Question> ob : observations.values() )
			data.add( new Observation(true, form, ob));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


