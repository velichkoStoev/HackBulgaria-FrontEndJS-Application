import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CsvReader {
	private static String CSV_FILE = "test2.csv";
	private static String INVALID_QUERY_MESSAGE = "Invalid query! Please check your input!";
	private static String INVALID_FIND_MESSAGE = "Your FIND query is't valid! Please check your input!";
	private static String INVALID_COLUMN_NAME_MESSAGE = "Some of the columns you have entered don't exists! "
			+ "Use SHOW to view the column names, then review your query!";
	private static String NO_COLUMN_FOUND_MESSAGE = "No column found with the given name! Please check your input!";
	private static String INVALID_SCANNER_PARAM = "Scanner param is null!";
	private static String INVALID_SCANNER_STATE_MESSAGE = "Scanner points to an empty file!";
	private Scanner scanner;

	public void processQuery(String query) throws FileNotFoundException{
		processQuery(initScanner(), query);
		closeScanner();
	}
	
	public void processQuery(Scanner scanner, String query)
			throws FileNotFoundException {
		
		if(scanner == null){
//			throw new IllegalArgumentException(INVALID_SCANNER_PARAM);
			System.err.println(INVALID_SCANNER_PARAM);
			return;
		}
		
		if(!scanner.hasNext()){
//			throw new IllegalStateException(INVALID_SCANNER_STATE_MESSAGE);
			System.err.println(INVALID_SCANNER_STATE_MESSAGE);
			return;
		}			
		
		if (isValidQuery(query, RegexUtils.SHOW)) {
			showQuery(scanner);
		} else if (isValidQuery(query, RegexUtils.SELECT)) {
			selectQuery(query, scanner);
		} else if (isValidQuery(query, RegexUtils.FIND)) {
			findQuery(query, scanner);
		} else if (isValidQuery(query, RegexUtils.SUM)) {
			sumQuery(query, scanner);
		} else {
//			throw new IllegalArgumentException(INVALID_QUERY_MESSAGE);
			System.err.println(INVALID_QUERY_MESSAGE);
		}
	}

	private Scanner initScanner() throws FileNotFoundException {
		File file = new File(CSV_FILE);
		if(file.exists() && !file.isDirectory()){
			this.scanner = new Scanner(file);
		}
		
		return scanner;
	}

	private void closeScanner() {
		if (this.scanner != null) {
			scanner.close();
			scanner = null;
		}
	}
	
	private void showQuery(Scanner scanner) throws FileNotFoundException{
			System.out.println(scanner.nextLine());
	}
	
	private void selectQuery(String query, Scanner scanner) throws FileNotFoundException{
		if (query.matches(RegexUtils.SIMPLE_SELECT)) {
			while (scanner.hasNext()) {
				System.out.println(scanner.nextLine());
			}
		} else {
			String line = scanner.nextLine();
			String[] columnNamesFile = line.split(RegexUtils.SPLIT_QUERY_TOKENS);
			String[] queryTokens = getSelectQueryTokens(query);
			
			Set<String> columnNamesQuery = getColumnNamesFromSelectQuery(queryTokens);
			int limit = getLimitOptionFromQuery(queryTokens);

			Map<String, Integer> namesAndIndexes = getColumnIndexes(
					columnNamesQuery, columnNamesFile);
			
			if(columnNamesQuery.size() != namesAndIndexes.size()){
				System.err.println(INVALID_COLUMN_NAME_MESSAGE);
			} else {
				StringBuilder sb = new StringBuilder();

				for (String key : namesAndIndexes.keySet()) {
					sb.append(key + "\t");
				}
				sb.append("\n");

				Collection<Integer> indexes = namesAndIndexes.values();

				int counter = 0;

				while (scanner.hasNext() && counter != limit) {
					line = scanner.nextLine();
					String[] values = line
							.split(RegexUtils.SPLIT_FILE_DATA);

					for (Integer i : indexes) {
						sb.append(values[i] + "\t");
					}
					sb.append("\n");
					counter++;
				}
				System.out.println(sb);
			}
		}
	}
	
	private void findQuery(String query, Scanner scanner) throws FileNotFoundException{
		String findArg;
		if (query.matches(RegexUtils.FIND_DECIMAL)) {
			
			findArg = getFindQueryArgument(query);			
			scanner.nextLine(); // skipping column names

			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				String[] tokens = line.split(RegexUtils.SPLIT_FILE_DATA);
				for (String token : tokens) {
					if (token.matches(RegexUtils.INTEGER_FLOAT)
							&& token.trim().equalsIgnoreCase(findArg)) {
						System.out.println(line);
					}
				}
			}
		} else if (query.matches(RegexUtils.FIND_STRING)) {
			
			findArg = getFindQueryArgument(query);
			
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.toLowerCase().contains(findArg)) {
					System.out.println(line);
				}
			}
		} else {
			System.err.println(INVALID_FIND_MESSAGE);
		}
	}
	
	private void sumQuery(String query, Scanner scanner) throws FileNotFoundException{
		int columnIndex = -1;
		String line = scanner.nextLine();
		String sumArg = getSumQueryArgument(query);
		String[] columnNames = line.split(RegexUtils.SPLIT_FILE_DATA);

		for (int i = 0; i < columnNames.length; i++) {
			if (columnNames[i].trim().equalsIgnoreCase(sumArg)) {
				columnIndex = i;
				break;
			}
		}

		if (columnIndex != -1) {
			int sum = 0;
			String value;
			
			while (scanner.hasNext()) {
				line = scanner.nextLine();
				value = line.split(RegexUtils.SPLIT_FILE_DATA)[columnIndex].trim();
				if (value.matches(RegexUtils.INTEGER)) {
					sum += Integer.parseInt(value);
				}
			}

			System.out.println(sum);
		} else {
			System.err.println(NO_COLUMN_FOUND_MESSAGE);
		}
	}

	private static String[] getSelectQueryTokens(String query) {
		return query.split(RegexUtils.SPLIT_QUERY_TOKENS);
	}

	private static int getLimitOptionFromQuery(String[] queryTokens) {
		if (queryTokens[queryTokens.length - 1].matches(RegexUtils.INTEGER)
				&& queryTokens[queryTokens.length - 2]
						.equalsIgnoreCase("limit")) {
			return Integer.parseInt(queryTokens[queryTokens.length - 1]);
		}

		return -1;
	}

	private static Set<String> getColumnNamesFromSelectQuery(
			String[] queryTokens) {
		LinkedHashSet<String> names = new LinkedHashSet<String>();

		for (int i = 1; i < queryTokens.length; i++) { // i = 1 -> skip the SELECT keyword
			if (queryTokens[i].equalsIgnoreCase("limit")
					&& i != queryTokens.length - 1) {
				if (queryTokens[i + 1].matches(RegexUtils.INTEGER)) {
					break;
				}
			}

			names.add(queryTokens[i]);
		}

		return names;
	}

	private static Map<String, Integer> getColumnIndexes(Set<String> columnNamesQuery, String[] columnNamesFile) {
		Map<String, Integer> namesAndIndexes = new LinkedHashMap<>();

		for (String columnNameQuery : columnNamesQuery) {
			for (int i = 0; i < columnNamesFile.length; i++) {
				if (columnNameQuery.equalsIgnoreCase(columnNamesFile[i])) {
					namesAndIndexes.put(columnNameQuery, i);
					break;
				}
			}
		}

		return namesAndIndexes;
	}

	private static boolean isValidQuery(String query, String regex) {
		return query.toLowerCase().matches(regex);
	}

	private static String getFindQueryArgument(String query) {
		String arg = query.toLowerCase().replace("find", "").trim();

		if (arg.charAt(0) == '"' && arg.charAt(arg.length() - 1) == '"') {
			return arg.substring(1, arg.length() - 1);
		}

		return arg;
	}

	private static String getSumQueryArgument(String query) {
		return query.split("\\s+")[1].toLowerCase();
	}
	
	@Override
	protected void finalize() throws Throwable {
		closeScanner();
		super.finalize();
	}
}
