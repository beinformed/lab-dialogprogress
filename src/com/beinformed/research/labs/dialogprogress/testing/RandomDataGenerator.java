package com.beinformed.research.labs.dialogprogress.testing;
import java.io.IOException;



public class RandomDataGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length < 1) {
			System.out.println("Syntax:\njava RandomDataGenerator <location>\n<location>: output file for the data");
			System.exit(1);
		}
		
		String location = args[0];
		
		System.out.println("Writing data to " + location);
		try {
			DataGenerator gen = new DataGenerator(location);
			gen.generate(50000);
			gen.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
