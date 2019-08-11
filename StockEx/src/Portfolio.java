import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Portfolio {
	public HashMap<String, Stock> stocks;
	static DBHandler dbHandler = new DBHandler();;

	public Portfolio() {
		stocks = new HashMap<String, Stock>();
		this.initPortfolio();
	}

	public void initPortfolio(){
		ArrayList<StockTuple<String, Double, Time, Integer>> stockData = dbHandler.getStocks();
		for (StockTuple<String, Double, Time, Integer> stock: stockData){
			stocks.put(stock.getVal1(), new Stock(stock.getVal1(), stock.getVal4()));
		}
	}

	public void placeBuyOrder(String ticker, double shares) {
		Stock stock = stocks.get(ticker);
		if(stock != null) {
			stock.buy(shares);
		}else {
			if (shares > 0){
				stock = new Stock(ticker, shares);
				stocks.put(ticker, stock);
			}
		}
	}
	public boolean placeSellOrder(String ticker, double shares) {
		Stock stock = stocks.get(ticker);
		if(stock != null) {
			boolean success = stock.sell(shares);
			if(!success) {
				System.out.println("You don't own that many shares!");
				return false;
			}
			if (stock.shares == 0){
				stocks.remove(stock.ticker);
			}
		}
		else {
			return false;
		}
		return true;
	}

	public double getPortfolioValue() throws Exception{
		double sum = 0.0;
		for (String key : stocks.keySet()) {
		    Stock stock = stocks.get(key);
		    sum+= stock.calculateValue(DataScrape.getCurrentPrice(key));
		}
		return sum;
	}
}
