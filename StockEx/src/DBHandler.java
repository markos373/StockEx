import java.awt.*;
import java.net.URI;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;


public class DBHandler {

    private Connection connection;

    public DBHandler(){
        String url = "jdbc:mysql://localhost:3306/stocks?verifyServerCertificate=false&useSSL=true&serverTimezone=EST";
//        String url = "jdbc:mysql://localhost:3306/stocks?useSSL=false";
        connection = null;
        try
        {
            // Can be used to check whether database is properly loaded
            connection = DriverManager.getConnection(url, "root", "sqlworkbench123");
            // Uncomment this to add rows to database
//
        }
        catch (SQLException ex) {
            for (Throwable t : ex)
                System.out.println(t.getMessage());
            System.out.println("Opening connection unsuccesful!");
        }
    }

    public void connectionClose(){
        if (connection != null) {
            try {
                connection.close();
            }
            catch (SQLException ex) {
                for (Throwable t : ex)
                    System.out.println(t.getMessage());
                System.out.println("Closing connection unsuccesful!");
            }
        }
    }

    public boolean addStock(String stockTicker, int numOfStocks, double currentPrice){
        String query = "INSERT INTO userStocks VALUES (?,?,?,?);";
        try (PreparedStatement stat = connection.prepareStatement(query)) {
            stat.setString(2, stockTicker);
            stat.setDouble(3, currentPrice);
            java.util.Date date = new java.util.Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
            String currentDateTime = format.format(date);
            stat.setString(1, currentDateTime);
            stat.setInt(4, numOfStocks);
            int recordUpdate = stat.executeUpdate();
            System.out.println(recordUpdate + " rows successfully added into database!");
            return true;
        }
        catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    public ArrayList<StockTuple<Timestamp, String, Double, Integer>> getStocks(){
        ArrayList<StockTuple<Timestamp, String, Double, Integer>> stockData = new ArrayList<>();
        String query = "SELECT * from userStocks;";
        try (PreparedStatement stat = connection.prepareStatement(query)) {
            ResultSet resultSet = stat.executeQuery();
            while (resultSet.next()){
                Timestamp timestamp = resultSet.getTimestamp(1);
                java.util.Date date = timestamp;
                stockData.add(new StockTuple<>(resultSet.getTimestamp(1), resultSet.getString(2), resultSet.getDouble(3),  resultSet.getInt(4)));
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return stockData;
    }



    public boolean removeStock(Timestamp time){
        String query = "DELETE FROM userStocks where boughtDT = ?;";
        try (PreparedStatement stat = connection.prepareStatement(query)) {
            stat.setString(1, time.toString());
            int recordUpdate = stat.executeUpdate();
            System.out.println(recordUpdate + " row successfully removed from database!");
            return true;
        }
        catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateStock(Double boughtPrice, int numOfStocks){
        String query = "UPDATE userStocks set numStocks = ? where boughtPrice = ?;";
        try (PreparedStatement stat = connection.prepareStatement(query)) {
            stat.setInt(1, numOfStocks);
            stat.setDouble(2, boughtPrice);
            int recordUpdate = stat.executeUpdate();
            System.out.println(recordUpdate + " row successfully removed from database!");
            return true;
        }
        catch(SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateTransactionHistory(String ticker, String action, int numOfStocks, double buyPrice, double sellPrice){
            String query = "INSERT INTO transactionHistory VALUES (?,?,?,?,?,?);";
            try (PreparedStatement stat = connection.prepareStatement(query)) {
                stat.setString(2, action);
                stat.setString(3, ticker);
                java.util.Date date = new java.util.Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
                String currentDateTime = format.format(date);
                stat.setString(1, currentDateTime);
                stat.setInt(4, numOfStocks);
                stat.setDouble(5, buyPrice);
                stat.setDouble(6, sellPrice);
                int recordUpdate = stat.executeUpdate();
                System.out.println(recordUpdate + " rows successfully added into database!");
                return true;
            }
            catch(SQLException ex){
                ex.printStackTrace();
                return false;
            }


    }

    public ArrayList<TransactionHistoryTuple<Timestamp, String, String, Integer, Double, Double>> getTransactionHistory(){
        ArrayList<TransactionHistoryTuple<Timestamp, String, String, Integer, Double, Double>> stockData = new ArrayList<>();
        String query = "SELECT * from transactionHistory;";
        try (PreparedStatement stat = connection.prepareStatement(query)) {
            ResultSet resultSet = stat.executeQuery();
            while (resultSet.next()){
                stockData.add(new TransactionHistoryTuple<>(resultSet.getTimestamp(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4),resultSet.getDouble(5),  resultSet.getDouble(6)));
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return stockData;
    }

}
