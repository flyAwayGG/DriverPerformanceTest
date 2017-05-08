import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;

/**
 * Created by nikita on 08.05.17.
 */
public class Chart {


    private Chart() {
    }

    public static void build(DefaultCategoryDataset dataset) {
        JFreeChart barChart = ChartFactory.createBarChart(  "Title",
                "Browsers", "Time (seconds)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);


        int width = 3000;   /* Width of the image */
        int height = 700;  /* Height of the image */
        File file = new File("LineChart.jpeg");
        try {
            ChartUtilities.saveChartAsJPEG(file, barChart, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
