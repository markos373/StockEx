
public class Stock {
	String ticker;
	double shares;
	
	public Stock(String ticker, double shares) {
		this.ticker = ticker;
		this.shares = shares;
	}
	
	public boolean sell(double shares) {
		if(shares > this.shares) {
			return false;
		}
		this.shares -= shares;
		return true;
	}
	public void buy(double shares) {
		this.shares += shares;
	}
	
	public double calculateValue(double price) {
		return price*shares;
	}
	
}
