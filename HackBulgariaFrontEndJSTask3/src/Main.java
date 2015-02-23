import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
	private static String FILE_NOT_FOUND_MESSAGE = "The file with that name is not found!";
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		CsvReader reader = new CsvReader();

		while (true) {
			System.out.println("\nQUERY (input 'exit' to terminate the programm): ");
			String query = input.nextLine();

			if (query.toLowerCase().matches(RegexUtils.EXIT)) {
				System.out.println("Exiting ...");
				break;
			}
			
			try {
				reader.processQuery(query);
			} catch (FileNotFoundException e) {
				System.err.println(FILE_NOT_FOUND_MESSAGE);
			}
		}
	}
}
