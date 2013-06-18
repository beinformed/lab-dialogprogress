import java.io.IOException;

import org.uva.testing.DataGenerator;


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
			gen.generate(100);
			gen.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
