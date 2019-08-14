import graphs.CandlestickChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Testing {

  private static String d1 = "31-Dec-1998";
  private static String d2 = "01-Jan-1999";
  private static String d3 = "02-Jan-1999";
  private static String d4 = "03-Jan-1999";
  private static String d5 = "04-Jan-1999";

  private static void configureCandlestick() {

    JFrame.setDefaultLookAndFeelDecorated(true);
    JFrame frame = new JFrame("TESTING XD");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    CandlestickChart chart = new CandlestickChart("SOME-STOCK");
    try {
      chart.addCandel(d1, 100.0, 250.0, 267.0, 90.0, 200);
      chart.addCandel(d2, 20.0, 250.0, 267.0, 90.0, 200);
      chart.addCandel(d3, 200.0, 240.0, 247.0, 160.0, 500);
      chart.addCandel(d4, 123.0, 230.0, 287.0, 140.0, 150);
      chart.addCandel(d5, 140.0, 220.0, 267.0, 90.0, 200);
    } catch(Exception e) {
      e.printStackTrace();
    }

    frame.setContentPane(chart);
    frame.setResizable(false);
    frame.pack();
    frame.setVisible(true);
  }

  public static void main(String[] args) {

    // TimeSeriesGraph test = new TimeSeriesGraph("Testing");
    // ArrayList<Double> values = new ArrayList<Double>();
    // values.add(1.0);
    // values.add(2.0);
    // values.add(5.0);
    // values.add(8.0);
    // values.add(15.0);
    //
    // ArrayList<Date> dates = new ArrayList<Date>();
    // SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
    // try {
    //   dates.add(formatter.parse(d1));
    //   dates.add(formatter.parse(d2));
    //   dates.add(formatter.parse(d3));
    //   dates.add(formatter.parse(d4));
    //   dates.add(formatter.parse(d5));
    // } catch(Exception e) {}
    //
    // test.setValues(values, "Price");
    // test.setDates(dates, "Date");
    // test.createChart();
    // test.pack();
    // RefineryUtilities.positionFrameRandomly(test);
    // test.setVisible(true);

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

  //configureCandlestick();
  }
}
