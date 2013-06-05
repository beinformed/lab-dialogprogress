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
		String textfile;
		textfile = args[0];
		getData(textfile);
	}
	
	// reads the textfile
	public static void getData(String textfile){
		try {
			BufferedReader src = new BufferedReader(new FileReader(textfile));
			String str = src.readLine();
			while(str != null){
				StringTokenizer st = new StringTokenizer(str,
				",\n\t");
				while (st.hasMoreTokens()) {
					int observation_id = Integer.parseInt(st.nextToken());
					int form_id = Integer.parseInt(st.nextToken());
					Double timestamp = Double.parseDouble(st.nextToken());
					int question_id = Integer.parseInt(st.nextToken());
					String answer = st.nextToken();
					Boolean is_necessary = Boolean.valueOf(st.nextToken());
					
					System.out.printf("observation: %d form number: %d timestamp: %f question id: %d answer: %s \n", 
							observation_id, form_id, timestamp, question_id, answer);
				}
				System.out.println("volgende vraag");
				str = src.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


