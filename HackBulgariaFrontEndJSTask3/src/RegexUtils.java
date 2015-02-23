
public class RegexUtils {
	public static String SPLIT_QUERY_TOKENS = "\\W+";
	public static String SPLIT_FILE_DATA = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
	public static String INTEGER = "\\d+";
	public static String INTEGER_FLOAT = "(\\s+)?(\\d+)((\\.\\d+)?)(\\s+)?";
	public static String SHOW = "(\\s+)?show(\\s+)?";
	public static String SUM = "(\\s+)?sum\\s+\\w+(\\s+)?";
	public static String FIND = "(\\s+)?find\\s+(\")?(.*)(\")?(\\s+)?";
	public static String SELECT = "((\\s+)?select(\\s+)?)|((\\s+)?select\\s+((\\w+(\\s+)?,(\\s+)?)*)?(\\w+)(\\s+)?((\\s+)limit\\s+\\d+)?(\\s+)?)";
	public static String SIMPLE_SELECT = "(\\s+)?select(\\s+)?";
	public static String FIND_DECIMAL = "(\\s+)?find\\s+(\\d+)((\\.\\d+)?)(\\s+)?";
	public static String FIND_STRING = "(\\s+)?find\\s+(\")((.)*)(\")(\\s+)?";
	public static String EXIT = "(\\s+)?exit(\\s+)?";
}
