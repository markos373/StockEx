import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import org.json.JSONArray;
import org.json.JSONObject;

/**
*DataScrape handles grabbing data from financialmodelingprep.com.  
*
*/
public class DataScrape {

	public static void main(String[] args) throws Exception{
		System.out.println(getCurrentPrice("AAPL"));
		System.out.println(getLastNdays("AAPL", 100));
	}

	/**
	* Get current price of a ticker
	*@param ticker String Ticker symbol
	*@return double current price of ticker
	*/
	public static double getCurrentPrice(String ticker) throws Exception{
		String URL = "https://financialmodelingprep.com/api/v3/stock/real-time-price/"+ticker;
		JSONObject json = null;
		json = new JSONObject(getWebPageSource(URL));
		return (double) json.get("price");
	}
	/**
	* Get last N days of a stock
	*@param ticker String Ticker symbol
	*@return ArrayList<String> containing the dates of the last N days (excluding weekends)
	*/
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
	/**
	* Get close value for last N days of a stock
	*@param ticker String Ticker symbol
	*@param length Int number of days specified as N
	*@return ArrayList<Double> containing the close values of the last N days (excluding weekends)
	*/
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
	/**
	* Get volume value for last N days of a stock
	*@param ticker String Ticker symbol
	*@param length Int number of days specified as N
	*@return ArrayList<Double> containing the volume values of the last N days (excluding weekends)
	*/
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
	/**
	* Get open value for last N days of a stock
	*@param ticker String Ticker symbol
	*@param length Int number of days specified as N
	*@return ArrayList<Double> containing the open values of the last N days (excluding weekends)
	*/
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
	/**
	* Get high value for last N days of a stock
	*@param ticker String Ticker symbol
	*@param length Int number of days specified as N
	*@return ArrayList<Double> containing the high values of the last N days (excluding weekends)
	*/
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
	/**
	* Get low value for last N days of a stock
	*@param ticker String Ticker symbol
	*@param length Int number of days specified as N
	*@return ArrayList<Double> containing the low values of the last N days (excluding weekends)
	*/
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
	/**
	* Get sURL as a String to parse as jSON
	*@param sURL URL to get page source of
	*@return String page source of sURL
	*/
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
