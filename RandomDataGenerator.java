import java.io.IOException;

import org.uva.testing.DataGenerator;


public class RandomDataGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Writing data to data/test2.csv");
		try {
			DataGenerator gen = new DataGenerator("data/test2.csv");
			gen.generate(10000);
			gen.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
