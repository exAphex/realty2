package org.exaphex.realty.processor.chart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.exaphex.realty.model.Transaction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;

import java.awt.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class IncomeExpenseChartProcessor {
    protected static final Logger logger = LogManager.getLogger();

    public static JFreeChart createBuildingChart(List<Transaction> transactions) {
        IntervalXYDataset dataset = createDataset(transactions);
        return createChart(dataset);
    }

    private static IntervalXYDataset createDataset(List<Transaction> transactions) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date now = new Date();
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        beginCalendar.setTime(now);
        beginCalendar.set(Calendar.DAY_OF_MONTH, 1);
        beginCalendar.add(Calendar.MONTH, -12);
        finishCalendar.setTime(now);
        TimeSeries tsIncome = new TimeSeries("Income");
        TimeSeries tsExpenses = new TimeSeries("Expenses");

        while (beginCalendar.before(finishCalendar)) {
            Date monthDate = beginCalendar.getTime();

            List<Transaction> monthTransactions = transactions.stream().filter(t -> {
                try {
                    Date tempTransactionDate = formatter.parse(t.getDate());
                    return (tempTransactionDate.getMonth() == monthDate.getMonth() && tempTransactionDate.getYear() == monthDate.getYear());
                } catch (ParseException e) {
                    logger.error(e);
                }
                return false;
            }).toList();

            Float fIncome = monthTransactions.stream().filter(t -> Transaction.isIncome(t.getType())).map(Transaction::getAmount).reduce(0f, Float::sum);
            Float fExpenses = monthTransactions.stream().filter(t -> Transaction.isExpense(t.getType())).map(t -> t.getAmount() + t.getSecondaryAmount()).reduce(0f, Float::sum);
            tsExpenses.add(new Month(monthDate), fExpenses );
            tsIncome.add(new Month(monthDate), fIncome );
            beginCalendar.add(Calendar.MONTH, 1);
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(tsExpenses);
        dataset.addSeries(tsIncome);
        return dataset;
    }

    private static JFreeChart createChart(IntervalXYDataset dataset) {

        JFreeChart chart = ChartFactory.createXYBarChart(
                "Income/Expenses",  // title
                null,
                true,
                null,   // y-axis label
                dataset);
        XYPlot plot = (XYPlot)chart.getPlot();

        ClusteredXYBarRenderer cb = new ClusteredXYBarRenderer(0.5, true);
        cb.setShadowVisible(false);
        cb.setDrawBarOutline(false);
        cb.setBarPainter(new StandardXYBarPainter());
        cb.setSeriesPaint(0, new Color(229,14,14));
        cb.setSeriesPaint(1, Color.GREEN);
        plot.setRenderer(cb);
        chart.setBackgroundPaint(new Color(30,30,30));
        chart.getTitle().setPaint(new Color(221,221,221));
        chart.getLegend().setBackgroundPaint(new Color(30,30,30));
        chart.getLegend().setItemPaint(new Color(221,221,221));
        chart.getXYPlot().setBackgroundPaint(new Color(30,30,30));
        chart.getXYPlot().getDomainAxis().setLabelPaint(new Color(221,221,221));
        chart.getXYPlot().getDomainAxis().setTickLabelPaint(new Color(221,221,221));
        chart.getXYPlot().getDomainAxis().setTickMarkPaint(new Color(221,221,221));
        chart.getXYPlot().getDomainAxis().setAxisLinePaint(new Color(221,221,221));
        chart.getXYPlot().getRangeAxis().setLabelPaint(new Color(221,221,221));
        chart.getXYPlot().getRangeAxis().setTickLabelPaint(new Color(221,221,221));
        chart.getXYPlot().getRangeAxis().setTickMarkPaint(new Color(221,221,221));
        chart.getXYPlot().getRangeAxis().setAxisLinePaint(new Color(221,221,221));
        return chart;

    }
}
