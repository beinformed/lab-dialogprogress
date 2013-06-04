package org.uva.predictions;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


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
				",.\n\t");
				while (st.hasMoreTokens()) {
					String word = st.nextToken();
					System.out.printf("%s \n", word);
				}
				System.out.println("volgende regel");
				str = src.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


