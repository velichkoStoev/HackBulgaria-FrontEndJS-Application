
public class ConsoleLogger implements MyLogger{

	@Override
	public void log(int level, String message) {
		System.out.println(Utils.getFullLog(level, message));
	}

}