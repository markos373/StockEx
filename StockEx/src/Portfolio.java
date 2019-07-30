import java.util.HashMap;

public class Portfolio {
	HashMap<String, Stock> stocks;
	public Portfolio() {
		stocks = new HashMap<String, Stock>();
	}
	public void placeBuyOrder(String ticker, double shares) {
		Stock stock = stocks.get(ticker);
		if(stock != null) {
			stock.buy(shares);
		}else {
			stock = new Stock(ticker, shares);
			stocks.put(ticker, stock);
		}
	}
	public boolean placeSellOrder(String ticker, double shares) {
		Stock stock = stocks.get(ticker);
		if(stock != null) {
			boolean success = stock.sell(shares);
			if(!success) {
				System.out.println("You don't own that many shares!");
			}
		}else {
			return false;
		}
		return true;
	}
	
	public double getPortfolioValue() {
		double sum = 0.0;
		for (String key : stocks.keySet()) {
		    Stock stock = stocks.get(key);
		    sum+= stock.calculateValue(DataScrape.getCurrentPrice(key));
		}
		return sum;
	}
}
