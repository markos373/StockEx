import java.awt.*;
import java.net.URI;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class DBHandler {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/stocks?verifyServerCertificate=false&useSSL=true&serverTimezone=UTC";
//        String url = "jdbc:mysql://localhost:3306/stocks?useSSL=false";
        Connection con = null;
        try
        {
            // Can be used to check whether database is properly loaded
            con = DriverManager.getConnection(url, "***", "***");
            String query = "SELECT * FROM userStocks";
            try (PreparedStatement stat = con.prepareStatement(query)) {
                try (ResultSet rs = stat.executeQuery()){
                    while (rs.next()){
                        System.out.printf("Stock Ticker: %s, Price Bought At: %s, Time Bought At: %s, Number of Stocks bought: %s\n",rs.getString(1), rs.getDouble(2), rs.getTime(3), rs.getInt(4));
                    }
                }
            }
            // Uncomment this to add rows to database
//            Scanner in = new Scanner(System.in);
//            System.out.print("Enter a stock name: ");
//            String stockName = in.nextLine();
//            System.out.print("Enter the price the stock was bought at: ");
//            String boughtPrice = in.nextLine();
//            System.out.print("Enter the time the stock was bought at: ");
//            String boughtTime = in.nextLine();
//            System.out.print("Enter the number of stocks that were bought: ");
//            String numOfStocks = in.nextLine();
//            String query = "INSERT INTO userStocks VALUES (?,?,?,?);";
//            try (PreparedStatement stat = con.prepareStatement(query)) {
//                stat.setString(1, stockName);
//                stat.setDouble(2, Double.parseDouble(boughtPrice));
//                java.util.Date date = new java.util.Date();
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
//                String currentDateTime = format.format(date);
//                stat.setString(3, currentDateTime);
//                stat.setInt(4, Integer.parseInt(numOfStocks));
//                int recordUpdate = stat.executeUpdate();
//                System.out.println(recordUpdate + " rows successfully added into database!");
//            }
        }
        catch (SQLException ex) {
            for (Throwable t : ex)
                System.out.println(t.getMessage());
            System.out.println("Opening connection unsuccesful!");
        }
        finally {
            if (con != null) {
                try {
                    con.close();
                }
                catch (SQLException ex) {
                    for (Throwable t : ex)
                        System.out.println(t.getMessage());
                    System.out.println("Closing connection unsuccesful!");
                }
            }
        }
    }

}
