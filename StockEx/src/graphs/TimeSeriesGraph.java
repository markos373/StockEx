import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TimeSeriesGraph extends ApplicationFrame {

  private ArrayList<Double> values;
  private ArrayList<Date> dates;
  private String title;
  private String xaxisTitle;
  private String yaxisTitle;


  /**
   *  Construtor for a Time Series Graph
   *  @param title - the title of the plot
   */
  public TimeSeriesGraph(final String title) {
    super(title);
    this.title = title;
  }


  /**
   *  Provide the values to be plotted
   *  @param values - ArrayList of float values for the y-axis
   *  @param axisTitle - the title of the y-axis
   */
  public void setValues(ArrayList<Double> values, String axisTitle) {
    this.values = new ArrayList<Double>(values);
    this.yaxisTitle = axisTitle;
  }


  /**
   *  Provide the dates to be plotted
   *  @param dates - ArrayList of string values for the x-axis
   *  @param axisTitle - the title of the x-axis
   */
  public void setDates(ArrayList<Date> dates, String axisTitle) {
    this.dates = new ArrayList<Date>(dates);
    this.xaxisTitle = axisTitle;
  }


  /**
   *  Creates the chart and displays it
   */
  public void createChart() {
    sizeCheck();
    TimeSeries series = new TimeSeries(this.yaxisTitle);
    for (int i = 0; i < dates.size(); i++) {
      try {
        series.add(new Day(dates.get(i)), values.get(i));
      } catch(SeriesException e) {
        System.err.println("[ERROR] couldn't add to TimeSeriesGraph...");
        e.printStackTrace();
        System.exit(1);
      }
    }
    final XYDataset dataset = new TimeSeriesCollection(series);
    final JFreeChart chart = createChart(dataset);
    final ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(560, 370));
    chartPanel.setMouseZoomable(true, false);
    setContentPane(chartPanel);
  }


  /**
   *  Checks to see if the number of dates and values matches...
   *  @exception SizeMismatchException - thrown if the sizes differ
   */
  private void sizeCheck() throws IllegalArgumentException {
    if (dates.size() != values.size()) {
      System.err.println("Dates: " + dates.size());
      System.err.println("Values: " + values.size());
      throw new IllegalArgumentException();
    }
  }


  /**
   *  Creates the chart
   *  @param dataset - the set of x-axis and y-axis values
   *  @return a JFreeChart object
   */
  private JFreeChart createChart(final XYDataset dataset) {
    return ChartFactory.createTimeSeriesChart(
      this.title,
      this.xaxisTitle,
      this.yaxisTitle,
      dataset,
      false,
      false,
      false);
  }
}