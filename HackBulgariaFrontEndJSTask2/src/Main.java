import java.io.FileNotFoundException;

public class Main {
	
	private static final String FILE_NAME = "test.txt";
	
	public static void main(String[] args) {
		ConsoleLogger cl = new ConsoleLogger();
		cl.log(1, "Hello world!");

		FileLogger fl;
		try {
			fl = new FileLogger(FILE_NAME);
			fl.log(3, "Hello world!");
			fl.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		HttpLogger hl = new HttpLogger();
		hl.log(1, "Go web!");
	}
}