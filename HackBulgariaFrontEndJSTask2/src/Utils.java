import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
	
	private static final String[] LOG_LEVELS = { "INFO", "WARNING", "PLSCHECKFFS" };
	private static final String DELIMITER = "::";
	
	public static String getFullLog(int level, String message) {
		StringBuilder sb = new StringBuilder();
		sb.append(LOG_LEVELS[level-1]);
		sb.append(DELIMITER);
		sb.append(getDate());
		sb.append(DELIMITER);
		sb.append(message);
		return sb.toString();
	}

	private static String getDate() {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		df.setTimeZone(tz);
		return df.format(new Date());
	}
}