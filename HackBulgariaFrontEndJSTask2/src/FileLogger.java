import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileLogger implements MyLogger {
	private PrintWriter writer;
	
	public FileLogger(String fileName) throws FileNotFoundException{
		this.writer = new PrintWriter(fileName);
	}
	
	@Override
	public void log(int level, String message) {
		writer.println(Utils.getFullLog(level, message));
	}
	
	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}
	
	public void close(){
		if(this.writer != null)
			writer.close();
	}

}