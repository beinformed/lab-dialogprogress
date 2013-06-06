package org.uva.testing;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.uva.predictions.Form;
import org.uva.predictions.Observation;
import org.uva.predictions.Question;



/**
 * 
 * Reads a CSV dialog log file, extracts timestamps, question_ids among others.
 * 
 * @param	textfile	the CSV file you want to extract data from
 * @return				
 *
 */

public class Reader {
	
	BufferedReader dataRdr, formfRdr;
	// string for file location
	public Reader (String textfile, String formfile ){
		try {
			dataRdr = new BufferedReader(new FileReader(textfile));
			formfRdr = new BufferedReader(new FileReader(formfile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
 	 * Method getData() - reads data from buffered readers and makes sure every
 	 * observation has the correct form_id
 	 */ 
	public List<Observation> getData( ){
	
			
		Map<Integer, List<Question>> observations = new HashMap<Integer, List<Question>>();
		Map<Integer, Integer>  obIdsHasForm = new HashMap<Integer, Integer>();
		Map<Integer, Form> forms = new HashMap<Integer, Form>();
		List<Observation> data = new ArrayList<Observation>();
		
		int formId = -1;
		int nrOfQuestions = -1;
		
		/* Read forms and save in map: forms */
		try{
			String str = formfRdr.readLine();
			while(str != null ){
				StringTokenizer st = new StringTokenizer(str,",\n\t");
					while (st.hasMoreTokens()) {
						formId	= Integer.parseInt(st.nextToken().trim());
						nrOfQuestions = Integer.parseInt(st.nextToken().trim());
					}
				forms.put(formId, new Form( formId, nrOfQuestions ));	
				str = formfRdr.readLine();
			}
			
			/* read data file and makes sure every observation has correct form associated */
			str = dataRdr.readLine();
			while(str != null){
				StringTokenizer st = new StringTokenizer(str,",\n\t");
				while (st.hasMoreTokens()) {
					int form_id = Integer.parseInt(st.nextToken());
					int observation_id = Integer.parseInt(st.nextToken());
					if( ! obIdsHasForm.containsKey( observation_id ) )
						obIdsHasForm.put(observation_id, form_id);
					if ( ! observations.containsKey( observation_id ) ){
						observations.put( observation_id, new ArrayList<Question>() );
					}
					long timestamp = Long.parseLong(st.nextToken().trim());
					int question_id = Integer.parseInt(st.nextToken().trim());
					String answer = st.nextToken();
					observations.get(observation_id).add( new Question( question_id, answer, timestamp ));	
					}
					str = dataRdr.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
			
			
			
		List<Question> ob = new ArrayList<Question>();
		Form form;
		for ( Integer obId : observations.keySet() ){
			ob = observations.get(obId);
			form = forms.get(obId);
			data.add( new Observation(true, form, ob));
		}
		return data;
	}
}


