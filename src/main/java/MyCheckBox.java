import org.jfree.chart.plot.XYPlot;

import javax.swing.*;

/**
 * Created by khvostovai on 21.03.2019.
 */
public class MyCheckBox extends JCheckBox {
    private XYPlot plot;

    public MyCheckBox(XYPlot plot) {
        super();
        if(plot.getDataset().getSeriesCount() == 1) {
            this.setText((String) plot.getDataset().getSeriesKey(0));
        } else this.setText("unknow");
        this.plot = plot;
    }
    public XYPlot getPlot() {
        return this.plot;
    }
}
