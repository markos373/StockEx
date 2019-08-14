import java.sql.Timestamp;

public class Stock implements Comparable<Stock>{
	String ticker;
	double shares;
	double buyPrice;
	Timestamp buyTime;
	
	public Stock(String ticker, double shares, double buyPrice, Timestamp buyTime) {
		this.ticker = ticker;
		this.shares = shares;
		this.buyPrice = buyPrice;
		this.buyTime = buyTime;
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


	public boolean equals(Object o){
		if (this == o){
			return true;
		}
		if(o == null || o.getClass()!= this.getClass())
			return false;
		Stock stock = (Stock) o;
		return (stock.ticker.equals(this.ticker) && stock.shares == this.shares && stock.buyPrice == this.buyPrice && stock.buyTime.equals(this.buyTime) );
	}

	public double calculateValue() {
		return buyPrice*shares;
	}

	public int compareTo(Stock stock) {
		//ascending order
		if (this.buyPrice < stock.buyPrice){
			return -1;
		}
		else if (this.buyPrice > stock.buyPrice){
			return 1;
		}
		else{
			return 0;
		}

		//descending order
		//return compareQuantity - this.quantity;

	}

}
