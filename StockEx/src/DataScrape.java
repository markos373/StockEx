import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import org.json.JSONArray;
import org.json.JSONObject;


public class DataScrape {

	public static void main(String[] args) throws Exception{
		System.out.println(getCurrentPrice("AAPL"));
		System.out.println(getLastNdays("AAPL", 100));
	}

	public static double getCurrentPrice(String ticker) throws Exception{
		String URL = "https://financialmodelingprep.com/api/v3/stock/real-time-price/"+ticker;
		JSONObject json = null;
		json = new JSONObject(getWebPageSource(URL));
		return (double) json.get("price");
	}

	public static ArrayList<String> getLastNdays(String ticker, int length){
		ArrayList<String> lastNdays = new ArrayList<String>();
		String URL = "https://financialmodelingprep.com/api/v3/historical-price-full/"+ticker+"?timeseries="+length;
		JSONObject json = null;
		JSONArray historicalData = null;
		try {
			json = new JSONObject(getWebPageSource(URL));
			historicalData = json.getJSONArray("historical");
			for(int i = 0; i < historicalData.length(); i++) {
				JSONObject day = historicalData.getJSONObject(i);
				lastNdays.add(day.getString("date"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lastNdays;
	}

	public static ArrayList<Double> getCloseLastNdays(String ticker, int length){
		ArrayList<Double> lastNdays = new ArrayList<Double>();
		String URL = "https://financialmodelingprep.com/api/v3/historical-price-full/"+ticker+"?timeseries="+length;
		JSONObject json = null;
		JSONArray historicalData = null;
		try {
			json = new JSONObject(getWebPageSource(URL));
			historicalData = json.getJSONArray("historical");
			for(int i = 0; i < historicalData.length(); i++) {
				JSONObject day = historicalData.getJSONObject(i);
				lastNdays.add(day.getDouble("close"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lastNdays;
	}

	public static ArrayList<Double> getVolumeLastNdays(String ticker, int length){
		ArrayList<Double> lastNdays = new ArrayList<Double>();
		String URL = "https://financialmodelingprep.com/api/v3/historical-price-full/"+ticker+"?timeseries="+length;
		JSONObject json = null;
		JSONArray historicalData = null;
		try {
			json = new JSONObject(getWebPageSource(URL));
			historicalData = json.getJSONArray("historical");
			for(int i = 0; i < historicalData.length(); i++) {
				JSONObject day = historicalData.getJSONObject(i);
				lastNdays.add(day.getDouble("volume"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lastNdays;
	}

	public static ArrayList<Double> getOpenLastNdays(String ticker, int length){
		ArrayList<Double> lastNdays = new ArrayList<Double>();
		String URL = "https://financialmodelingprep.com/api/v3/historical-price-full/"+ticker+"?timeseries="+length;
		JSONObject json = null;
		JSONArray historicalData = null;
		try {
			json = new JSONObject(getWebPageSource(URL));
			historicalData = json.getJSONArray("historical");
			for(int i = 0; i < historicalData.length(); i++) {
				JSONObject day = historicalData.getJSONObject(i);
				lastNdays.add(day.getDouble("open"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lastNdays;
	}

	public static ArrayList<Double> getHighLastNdays(String ticker, int length){
		ArrayList<Double> lastNdays = new ArrayList<Double>();
		String URL = "https://financialmodelingprep.com/api/v3/historical-price-full/"+ticker+"?timeseries="+length;
		JSONObject json = null;
		JSONArray historicalData = null;
		try {
			json = new JSONObject(getWebPageSource(URL));
			historicalData = json.getJSONArray("historical");
			for(int i = 0; i < historicalData.length(); i++) {
				JSONObject day = historicalData.getJSONObject(i);
				lastNdays.add(day.getDouble("high"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lastNdays;
	}

	public static ArrayList<Double> getLowLastNdays(String ticker, int length){
		ArrayList<Double> lastNdays = new ArrayList<Double>();
		String URL = "https://financialmodelingprep.com/api/v3/historical-price-full/"+ticker+"?timeseries="+length;
		JSONObject json = null;
		JSONArray historicalData = null;
		try {
			json = new JSONObject(getWebPageSource(URL));
			historicalData = json.getJSONArray("historical");
			for(int i = 0; i < historicalData.length(); i++) {
				JSONObject day = historicalData.getJSONObject(i);
				lastNdays.add(day.getDouble("low"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lastNdays;
	}

	private static String getWebPageSource(String sURL) throws IOException {
		URL url = new URL(sURL);
		URLConnection urlCon = url.openConnection();
		BufferedReader in = null;

		if (urlCon.getHeaderField("Content-Encoding") != null
				&& urlCon.getHeaderField("Content-Encoding").equals("gzip")) {
			in = new BufferedReader(new InputStreamReader(new GZIPInputStream(urlCon.getInputStream())));
		} else {
			in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
		}

		String inputLine;
		StringBuilder sb = new StringBuilder();

		while ((inputLine = in.readLine()) != null)
			sb.append(inputLine);
		in.close();

		return sb.toString();
	}
}
