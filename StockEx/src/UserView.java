import org.json.JSONException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.util.ArrayList;

import javax.swing.*;

public class UserView {

    static DBHandler dbHandler = new DBHandler();;
    static DefaultListModel listModel = new DefaultListModel();
    static Portfolio portfolio = new Portfolio();

    private static void generateMenu(JFrame frame){
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("Stocks");
        menubar.add(menu);
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
                            int numOfStocks = (int)portfolio.stocks.get(input).shares;
                            boolean addedin = false;
                            for (int i = 0; i < listModel.size(); i++){
                                if (((String) listModel.get(i)).contains(input)){
                                    addedin = true;
                                    listModel.add(i, ((String) listModel.get(i)).substring(0, ((String) listModel.get(i)).indexOf(':') + 1) + " " + numOfStocks);
                                    if (numOfStocks != Integer.parseInt(numOfStock.getText())){
                                        listModel.remove( i  + 1);
                                    }
                                    break;
                                }
                            }
                            if (!addedin)
                            listModel.addElement("B" + input + " - Quantity: " + numOfStock.getText());

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
                        boolean returnval = portfolio.placeSellOrder(stock.getText(), Double.parseDouble(numOfStock.getText()));
                        int numOfStocks;
                        try {
                            numOfStocks = (int) portfolio.stocks.get(input).shares;
                        }
                        // the user has 0 of this stock and must be removed from UI list
                        catch (NullPointerException ex){
                            numOfStocks = -1;
                        }
                        if (numOfStocks > 0){
                            boolean addedin = false;
                            for (int i = 0; i < listModel.size(); i++){
                                if (((String) listModel.get(i)).contains(input)){
                                    addedin = true;
                                    listModel.add(i, ((String) listModel.get(i)).substring(0, ((String) listModel.get(i)).indexOf(':') + 1) + " " + numOfStocks);
                                    if (numOfStocks != Integer.parseInt(numOfStock.getText())){
                                        listModel.remove( i  + 1);
                                    }
                                    break;
                                }
                            }
                            if (!addedin)
                                listModel.addElement("B" + input + " - Quantity: " + numOfStock.getText());
                        }
                        else{
                            for (int i = 0; i < listModel.size(); i++){
                                if (((String) listModel.get(i)).contains(input)){
                                    listModel.remove(i);
                                    break;
                                }
                            }
                        }

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
        ArrayList<StockTuple<String, Double, Time, Integer>> stockData = dbHandler.getStocks();
        for (StockTuple<String, Double, Time, Integer> tuple: stockData) {
            String entry = tuple.getVal1() + " - Quantity: " + tuple.getVal4();
            try {
                if (tuple.getVal2() < DataScrape.getCurrentPrice(tuple.getVal1())) {
                    entry = "R" + entry;
                }
                else{
                    entry = "G" + entry;
                }

            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            listModel.addElement(entry);
        }
        scrollPane.setViewportView(list);
        list.setFont(new Font("Arial",Font.BOLD,32));
        list.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof String) {
                    setText(((String) value).substring(1));
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
