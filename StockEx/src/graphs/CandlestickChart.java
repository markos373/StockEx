package graphs;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;

import java.util.Date;

import org.jfree.data.time.Day;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

// =============================================================================
// =============================================================================

public class CandlestickChart extends JPanel {

  private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-mm-dd");
  private static final String LABEL_ONE = "Price";
  private static final String LABEL_TWO = "Volume";
  private static final String LABEL_THREE = "Date";
  private static final Color BACKGROUND_COLOR = Color.BLACK;
  private static final Color GRIDLINE_COLOR = Color.WHITE;

  private OHLCSeries ohlcSeries;
  private TimeSeries volumeSeries;

  private double open;
  private double close;
  private double high;
  private double low;
  private long volume;


  /**
   *  Constructor for the candlestick chart
   *  @param title - the title for the chart
   */
  public CandlestickChart(final String title) {
    final JFreeChart candlestickChart = createChart(title);
    final ChartPanel chartPanel = new ChartPanel(candlestickChart);
    chartPanel.setPreferredSize(new Dimension(1000, 500));
    chartPanel.setMouseZoomable(true);
    chartPanel.setMouseWheelEnabled(true);
    add(chartPanel, BorderLayout.CENTER);
  }


  /**
   *  Creates the candlestick chart and the volume subplot
   *  @param title - the title for the chart
   */
  private JFreeChart createChart(final String title) {

    // Create price data set, create price axis, render, and create subplot
    OHLCSeriesCollection candlestickDataset = new OHLCSeriesCollection();
    ohlcSeries = new OHLCSeries(LABEL_ONE);
    candlestickDataset.addSeries(ohlcSeries);

    // Create candlestick axis
    NumberAxis priceAxis = new NumberAxis(LABEL_ONE);
    priceAxis.setAutoRangeIncludesZero(false);

    // Render the candlestick chart
    CandlestickRenderer candlestickRenderer = new CandlestickRenderer(CandlestickRenderer.WIDTHMETHOD_AVERAGE,
                        false, new CustomHighLowItemLabelGenerator(new SimpleDateFormat("yyyy-dd-mm"), new DecimalFormat("0.000")));
    XYPlot candlestickSubplot = new XYPlot(candlestickDataset, null, priceAxis, candlestickRenderer);
    candlestickSubplot.setRangeGridlinePaint(GRIDLINE_COLOR);
    candlestickSubplot.setRangeGridlineStroke(new BasicStroke(1));
    candlestickSubplot.setDomainGridlinesVisible(true);
    candlestickSubplot.setBackgroundPaint(BACKGROUND_COLOR);

    // Create the volume subplot beneath the candlestick
    TimeSeriesCollection volumeDataset = new TimeSeriesCollection();
    volumeSeries = new TimeSeries(LABEL_TWO);
    volumeDataset.addSeries(volumeSeries);

    // Create volume chart axis
    NumberAxis volumeAxis = new NumberAxis(LABEL_TWO);
    volumeAxis.setAutoRangeIncludesZero(false);
    volumeAxis.setNumberFormatOverride(new DecimalFormat("0"));

    // Create the volume chart renderer
    XYBarRenderer dateRenderer = new XYBarRenderer();
    dateRenderer.setShadowVisible(false);
    dateRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("Volume--> Time={1} Size={2}",
                                         new SimpleDateFormat("dd-MMM-yyyy:mm"), new DecimalFormat("0")));

    // Create the volume subplot
    XYPlot volumeSubplot = new XYPlot(volumeDataset, null, volumeAxis, dateRenderer);
    volumeSubplot.setRangeGridlinePaint(GRIDLINE_COLOR);
    volumeSubplot.setRangeGridlineStroke(new BasicStroke(1));
    volumeSubplot.setDomainGridlinesVisible(true);
    volumeSubplot.setBackgroundPaint(BACKGROUND_COLOR);

// =============================================================================
// ======================= CREATE MAIN PLOT WITH SUBPLOTS ======================
// =============================================================================

    // Create charts common date axis and set margins
    DateAxis dateAxis = new DateAxis(LABEL_THREE);
    dateAxis.setDateFormatOverride(new SimpleDateFormat("dd-MMM-yyyy"));
    dateAxis.setLowerMargin(0.02);
    dateAxis.setUpperMargin(0.02);

    // Create the plot
    CombinedDomainXYPlot mainPlot = new CombinedDomainXYPlot(dateAxis);
    mainPlot.setGap(10.0);
    mainPlot.add(candlestickSubplot, 4);
    mainPlot.add(volumeSubplot, 2);
    mainPlot.setOrientation(PlotOrientation.VERTICAL);
    JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, mainPlot, true);
    chart.removeLegend();
    return chart;
  }


  /**
   *  Add a single candle to the chart
   *  @param d - the date for the candle, as string
   *  @param o - open price
   *  @param c - closing price
   *  @param h - high price
   *  @param l - low price
   *  @param v - volume
   *  @exception ParseException for illegal argument values
   */
  public void addCandel(String d, double o, double c, double h, double l, double v)
    throws ParseException {

    Day day = new Day(FORMAT.parse(d));
    ohlcSeries.add(day, o, h, l, c);
    volumeSeries.add(day, v);
  }
}
