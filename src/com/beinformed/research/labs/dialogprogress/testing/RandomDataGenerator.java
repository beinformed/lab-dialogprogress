package com.beinformed.research.labs.dialogprogress.testing;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.beinformed.research.labs.dialogprogress.testing.Form.ForkType;



public class RandomDataGenerator {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length < 3) {
			System.out.println("Syntax:\njava RandomDataGenerator <outputdir> <count> <config>\n<outputdir>: Directory to put the output files\n<count>: number of traces\n<config>: location of config file");
			System.exit(1);
		}
		
		String outputDir = cleanDir(args[0]);
		String config = args[2];
		int count = Integer.parseInt(args[1]);
		
		try {
			Map<String, Form> forms = readConfig(config);
			
			for(String filename : forms.keySet()) {
				System.out.println("generating " + filename);
				
				DataGenerator genTest = new DataGenerator(outputDir + filename + "_test.csv", forms.get(filename));
				genTest.generate(count / 10);
				genTest.close();
				
				DataGenerator genTrain = new DataGenerator(outputDir + filename + "_train.csv", forms.get(filename));
				genTrain.generate(count);
				genTrain.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Done.");
	}
	
	private static String cleanDir(String dir) {
		if(!dir.endsWith("/"))
			return dir + "/";
		else if(dir.contains("\\"))
			return cleanDir(dir.replace("\\", "/"));
		else
			return dir;
	}

	private static Map<String, Form> readConfig(String loc) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(loc));
		Map<String, Form> forms = new HashMap<String, Form>();
		
		String line = reader.readLine();
		while(line != null) {
			if(line.startsWith("#")) {
				line = reader.readLine();
				continue;
			}
			
			int qcount, forkPos, qPos;
			String[] parts = line.split("\\|");
			String filenameBase = line.replace('|', '_');
			qcount = Integer.parseInt(parts[0]);
			forkPos = Integer.parseInt(parts[1]);
			qPos = Integer.parseInt(parts[2]);
			forms.put(filenameBase + "_time", new Form(qcount, forkPos, qPos, ForkType.Time));
			forms.put(filenameBase + "_steps", new Form(qcount, forkPos, qPos, ForkType.Steps));
			
			line = reader.readLine();
		}
		
		reader.close();
		return forms;
	}

}
