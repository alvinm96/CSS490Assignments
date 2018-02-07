package program2;

import java.util.Iterator;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import program2.WeatherComponent;

/**
 * 
 * @author Alvin Manalastas
 *
 */
public class Program2 {
	public static void main(String[] args) {		
		Scanner scan = new Scanner(System.in);
		WeatherComponent wc = new WeatherComponent();
		String city = "";
		int type = -1;
		
		while (type != 0) {
			System.out.print("End (0) or Five Day Forecast(1) or Current Weather(2): ");
			type = scan.nextInt();
			
			if (type == 1 || type == 2) {
				System.out.print("Enter city: ");
				city = scan.next();
			}
			
			switch (type) {
			case 0:
				System.out.println("Ended program");
				break;
			case 1:
				wc.getFiveDayForecastByCity(city, new WeatherComponent.ResultHandler() {
					public void callback(JSONObject obj) {
						printForecast(obj);
					}
				});
				break;
			case 2:
				wc.getCurrentWeatherByCity(city, new WeatherComponent.ResultHandler() {
					public void callback(JSONObject obj) {
						printWeatherOverview(obj);
					}
				});
				break;
			default:
				System.out.println("Invalid input");
				break;
			}		
		}
		scan.close();
	}
	
	public static void printForecast(JSONObject obj) {
		JSONObject city = (JSONObject) obj.get("city");
		JSONArray forecastArr = (JSONArray) obj.get("list");
		
		Iterator<Object> forecastIter = forecastArr.iterator();

		System.out.println("5-day / 3-hour Forecast for " + city.getString("name"));
		while (forecastIter.hasNext()) {
			JSONObject currentObj = (JSONObject) forecastIter.next();
			String dt = currentObj.getString("dt_txt");
			JSONObject mainObj = (JSONObject) currentObj.get("main");
			
			System.out.println("-------------------------------------------");
			System.out.println("Date: " + dt);
			double temp = mainObj.getDouble("temp");
			double minTemp = mainObj.getDouble("temp_min");
			double maxTemp = mainObj.getDouble("temp_max");
			
			System.out.println(String.format("Temperature: %s°F", temp));
			System.out.println(String.format("Min Temperature: %s°F", minTemp));
			System.out.println(String.format("Max Temperature: %s°F", maxTemp));
			
		}
	}
	
	public static void printWeatherOverview(JSONObject obj) {
		String city = (String) obj.get("name");
		//JSONArray weatherArr = (JSONArray) obj.get("weather");
		//JSONObject weatherObj = weatherArr.getJSONObject(0);
		JSONObject mainObj = (JSONObject) obj.get("main");
		
		double temp = mainObj.getDouble("temp");
				
		String overview = String.format("It is currently %s°F in %s.", temp, city);
		
		System.out.println(overview);
	}
}
