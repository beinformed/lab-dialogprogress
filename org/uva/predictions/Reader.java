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
	
	String textfile, formfile;
	// string for file location
	public Reader (String textfile, String formfile ){
		this.textfile = textfile;
		this.formfile = formfile;
	}
	
	// reads the textfile
	public List<Observation> getData(String textfile, String formfile){
		
		Map<Integer, List<Question>> observations = new HashMap<Integer, List<Question>>();
		List<Observation> data = new ArrayList<Observation>();
		
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
					long timestamp = Long.parseLong(st.nextToken().trim());
					int question_id = Integer.parseInt(st.nextToken().trim());
					String answer = st.nextToken();
					observations.get(observation_id).add( new Question( question_id, answer, timestamp ));	
					
					//System.out.printf("observation: %d form number: %d timestamp: %f question id: %d answer: %s \n", 
					//		observation_id, form_id, timestamp, question_id, answer);
				}
				//System.out.println("volgende vraag");
				str = src.readLine();
			}
		
		int form_id = -1;
		int nrOfQuestions = -1;
		str = formfRdr.readLine();
		while(str != null ){
			StringTokenizer st = new StringTokenizer(str,",\n\t");
				while (st.hasMoreTokens()) {
					form_id	= Integer.parseInt(st.nextToken().trim());
					nrOfQuestions = Integer.parseInt(st.nextToken().trim());
				}
			str = formfRdr.readLine();
		}
		Form form = new Form( form_id, nrOfQuestions );		

		for ( List<Question> ob : observations.values() )
			data.add( new Observation(true, form, ob));

		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return data;
	}
}


