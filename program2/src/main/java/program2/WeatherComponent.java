package program2;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 
 * @author amana
 *
 */
public class WeatherComponent {
	private final String apiKey = "88559a0fbc4825a50c517237534ef248";
	private final String baseUrl = "http://api.openweathermap.org/data/2.5/";
	
	public void getFiveDayForecastByCity(String city, ResultHandler handler) {
		String weatherUrl = this.baseUrl + "forecast?units=imperial&APPID=" + this.apiKey + "&q=";
		
		try {
			URL url = new URL((weatherUrl + city.replace(" ", "")));
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			
			if (conn.getResponseCode() == 404) {
				System.err.println(String.format("Error getting forecast in %s. Enter a valid city name.", city));
				return;
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String response = "";
			String line;
			while ((line = br.readLine()) != null) {
				response += line;
			}
			
			handler.callback(new JSONObject(response));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getCurrentWeatherByCity(String city, ResultHandler handler) {
		String weatherUrl = this.baseUrl + "weather?units=imperial&APPID=" + this.apiKey + "&q=";
		try {
			URL url = new URL((weatherUrl + city.replace(" ", "")));
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			
			if (conn.getResponseCode() == 404) {
				System.err.println(String.format("Error getting weather in %s. Enter a valid city name.", city));
				return;
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String response = "";
			String line;
			while ((line = br.readLine()) != null) {
				response += line;
			}
			
			handler.callback(new JSONObject(response));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static interface ResultHandler {
		public void callback(JSONObject obj);
	}
}
