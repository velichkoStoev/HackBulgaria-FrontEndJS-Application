import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpLogger implements MyLogger {		
	
	@Override
	public void log(int level, String message) {		
		new Thread(new NetworkThread(level, message)).start();
	}

	private class NetworkThread implements Runnable{
		
		private static final String USER_AGENT = "Mozilla/5.0";
		private static final String URI = "http://example.com/";
		
		private String message;
		private int level;
		
		public NetworkThread(int level, String message){
			this.level = level;
			this.message = message;
		}
		
		@Override
		public void run() {
			try {
				URL url = new URL(URI);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();

				// request header
				connection.setRequestMethod("POST");
				// connection.setRequestProperty("User-Agent", USER_AGENT);
				// connection.setRequestProperty("Accept-Language", "en-US");

				String urlParameters = "log=" + Utils.getFullLog(this.level, this.message);

				// send request
				connection.setDoOutput(true);
				DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
				outputStream.writeBytes(urlParameters);
				outputStream.flush();
				outputStream.close();

				int responseCode = connection.getResponseCode();
				System.out.println("Sending " + urlParameters);
				System.out.println("Response code: " + responseCode);

				BufferedReader reader = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				String line;

				StringBuilder sb = new StringBuilder();

				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				reader.close();

				// print response
				System.out.println(sb);

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}