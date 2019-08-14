import org.json.JSONException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserView {

    static DBHandler dbHandler = new DBHandler();;
    static DefaultListModel listModel = new DefaultListModel();
    static DefaultListModel purchaseHistory = new DefaultListModel();
    static Portfolio portfolio = new Portfolio();

    private static void updateSidebar(){
        listModel.clear();
        for(Stock stock1: portfolio.getPortfolio()){
            listModel.addElement(stock1.buyTime + " " + stock1.ticker);
        }
    }

    private static void generateMenu(JFrame frame){
        JMenuBar menubar = new JMenuBar();
        JMenuItem transactionHistory = new JMenuItem("Transaction History");
        transactionHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<String> columnNames = new Vector<String>();
//                for (int column = 1; column <= columnCount; column++) {
//                    columnNames.add(metaData.getColumnName(column));
//                }
                columnNames.add("Date/Time");
                columnNames.add("Action");
                columnNames.add("Stock Ticker");
                columnNames.add("Number of Stocks");
                columnNames.add("Buy Price");
                columnNames.add("Sell Price");
                // data of the table
                Vector<Vector<Object>> data = new Vector<Vector<Object>>();
                ArrayList<TransactionHistoryTuple<Timestamp, String, String, Integer, Double, Double>> stockData = dbHandler.getTransactionHistory();
                for(TransactionHistoryTuple transactionHistoryTuple: stockData){
                    Vector<Object> vector = new Vector<Object>();
                    vector.add(transactionHistoryTuple.val1);
                    if (transactionHistoryTuple.val2.equals("B")){
                        vector.add("Bought");
                    }
                    else{
                        vector.add("Sold");
                    }
                    vector.add(transactionHistoryTuple.val3);
                    vector.add(transactionHistoryTuple.val4);
                    vector.add(transactionHistoryTuple.val5);
                    if ((double)transactionHistoryTuple.val6 == -1.0){
                        vector.add("");
                    }
                    else{
                        vector.add(transactionHistoryTuple.val6);
                    }
                    data.add(vector);
                }

                DefaultTableModel defaultTableModel = new DefaultTableModel(data, columnNames);

                // It creates and displays the table
                JTable table = new JTable(defaultTableModel);
                JFrame jFrame = new JFrame();
                jFrame.add(new JScrollPane(table));
                jFrame.setTitle("Transaction History");
//                jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jFrame.pack();
                jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

                jFrame.setVisible(true);
                // Closes the Connection
//                JOptionPane.showMessageDialog(null, new JScrollPane(table));
//                StringBuilder stringBuilder = new StringBuilder();
//                for(TransactionHistoryTuple transactionHistoryTuple: stockData){
//                    stringBuilder.append(transactionHistoryTuple.val1).append(transactionHistoryTuple.val2).append(transactionHistoryTuple.val3).append(transactionHistoryTuple.val4).append(transactionHistoryTuple.val5).append(transactionHistoryTuple.val6).append("\n");
//                }
//                JOptionPane.showMessageDialog(null,  stringBuilder.toString());
            }
        });
        JMenu menu = new JMenu("Stocks");
        menubar.add(menu);
        menubar.add(transactionHistory);
        JMenuItem addStock = new JMenuItem("Add Stock");
        addStock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField stock = new JTextField();
                JTextField numOfStock = new JTextField();
                Object[] message = {
                        "Stock Ticker:", stock,
                        "Number of Stocks:", numOfStock,
                };
                int response1 = JOptionPane.showConfirmDialog(null, message, "Enter Stock Information", JOptionPane.OK_CANCEL_OPTION);
                if (response1 == JOptionPane.YES_OPTION){
                    try{
                        String input = stock.getText();
                        DataScrape.getCurrentPrice(input);
                        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to add " + input +" stock to your portfolio?", "Confirm Portfolio Modification",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (response == JOptionPane.NO_OPTION) {
                        } else if (response == JOptionPane.YES_OPTION) {
                            portfolio.placeBuyOrder(stock.getText(), Double.parseDouble(numOfStock.getText()));
                            updateSidebar();
                        } else if (response == JOptionPane.CLOSED_OPTION) {
                        }
                    }
                    catch (Exception ex){
                        JOptionPane.showMessageDialog(frame, "Invalid stock ticker!");
                    }
                }

            }
        });
        JMenuItem removeStock = new JMenuItem( "Sell Stock");
        removeStock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] choices = new String[listModel.size()];
                for (int i = 0; i < listModel.size(); i++){
                    choices[i] = ((String) listModel.get(i)).substring(1);
                }

                JTextField stock = new JTextField();
                JTextField numOfStock = new JTextField();
                Object[] message = {
                        "Stock Ticker:", stock,
                        "Number of Stocks:", numOfStock,
                };
                int response1 = JOptionPane.showConfirmDialog(null, message, "Enter Stock Information", JOptionPane.OK_CANCEL_OPTION);
                if (response1 == JOptionPane.OK_OPTION){
                    int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove " + stock.getText() +" from your portfolio?", "Confirm Portfolio Modification",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.NO_OPTION) {
                    } else if (response == JOptionPane.YES_OPTION) {
                        String input = stock.getText();
                        portfolio.placeSellOrder(stock.getText(), Double.parseDouble(numOfStock.getText()));
                        updateSidebar();

                    } else if (response == JOptionPane.CLOSED_OPTION) {
                    }
                }

            }
        });
        menu.add(addStock);
        menu.add(removeStock);
        frame.setJMenuBar(menubar);
    }

    private static void generateSidebar(JPanel pa){

        JScrollPane scrollPane = new JScrollPane();
        JList list = new JList(listModel);
        DefaultListCellRenderer renderer =  (DefaultListCellRenderer)list.getCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        updateSidebar();
//        ArrayList<StockTuple<String, Double, Time, Integer>> stockData = dbHandler.getStocks();
//        for (StockTuple<String, Double, Time, Integer> tuple: stockData) {
//            String entry = tuple.getVal1() + " - Quantity: " + tuple.getVal4();
//            try {
//                if (tuple.getVal2() < DataScrape.getCurrentPrice(tuple.getVal1())) {
//                    entry = "R" + entry;
//                }
//                else{
//                    entry = "G" + entry;
//                }
//
//            }
//            catch (Exception ex){
//                ex.printStackTrace();
//            }
//            listModel.addElement(entry);
//        }
        scrollPane.setViewportView(list);
        list.setFont(new Font("Arial",Font.BOLD,32));
        list.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof String) {
//                    setText(((String) value).substring(1));
                    if (((String) value).charAt(0) == 'R'){
                        setForeground(Color.RED);
                    }
                    else if (((String) value).charAt(0) == 'G'){
                        setForeground(Color.GREEN);
                    }
                }
                return c;
            }

        });
        pa.add(list, BorderLayout.EAST);
    }


    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("StockEx");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        JPanel pa = new JPanel();
        pa.setLayout(new BorderLayout());
        generateMenu(frame);
        generateSidebar(pa);
        frame.add(pa);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }


}
