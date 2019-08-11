import java.awt.*;
import java.net.URI;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;


public class DBHandler {

    private Connection connection;

    public DBHandler(){
        String url = "jdbc:mysql://localhost:3306/stocks?verifyServerCertificate=false&useSSL=true&serverTimezone=UTC";
//        String url = "jdbc:mysql://localhost:3306/stocks?useSSL=false";
        connection = null;
        try
        {
            // Can be used to check whether database is properly loaded
            connection = DriverManager.getConnection(url, "root", "reddog");
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

    public boolean addStock(String stockTicker, String boughtPrice, String numOfStocks){
        String query = "INSERT INTO userStocks VALUES (?,?,?,?);";
        try (PreparedStatement stat = connection.prepareStatement(query)) {
            stat.setString(1, stockTicker);
            stat.setDouble(2, Double.parseDouble(boughtPrice));
            java.util.Date date = new java.util.Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
            String currentDateTime = format.format(date);
            stat.setString(3, currentDateTime);
            stat.setInt(4, Integer.parseInt(numOfStocks));
            int recordUpdate = stat.executeUpdate();
            System.out.println(recordUpdate + " rows successfully added into database!");
            return true;
        }
        catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    public ArrayList<StockTuple<String, Double, Time, Integer>> getStocks(){
        ArrayList<StockTuple<String, Double, Time, Integer>> stockData = new ArrayList<>();
        String query = "SELECT * from userStocks;";
        try (PreparedStatement stat = connection.prepareStatement(query)) {
            ResultSet resultSet = stat.executeQuery();
            while (resultSet.next()){
                stockData.add(new StockTuple<>(resultSet.getString(1), resultSet.getDouble(2), resultSet.getTime(3), resultSet.getInt(4)));
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return stockData;
    }

    public boolean removeStock(String stockTicker){
        String query = "DELETE FROM userStocks where name = ?;";
        try (PreparedStatement stat = connection.prepareStatement(query)) {
            stat.setString(1, stockTicker);
            int recordUpdate = stat.executeUpdate();
            System.out.println(recordUpdate + " row successfully removed from database!");
            return true;
        }
        catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

}


