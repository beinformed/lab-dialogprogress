package com.beinformed.research.labs.dialogprogress.testing;
import java.io.IOException;

import com.beinformed.research.labs.dialogprogress.testing.Form.ForkType;



public class RandomDataGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length < 2) {
			System.out.println("Syntax:\njava RandomDataGenerator <train> <test>\n<train>: output file for train data\n<test>: output file for test data");
			System.exit(1);
		}
		
		int count = 10000;
		
		String testLocation = args[1];
		String trainLocation = args[0];
		Form form = new Form(30, 15, 5, ForkType.Time);
		
		try {
			DataGenerator genTest = new DataGenerator(testLocation, form);
			genTest.generate(count / 10);
			genTest.close();
			
			DataGenerator genTrain = new DataGenerator(trainLocation, form);
			genTrain.generate(count);
			genTrain.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Done.");
	}

}
