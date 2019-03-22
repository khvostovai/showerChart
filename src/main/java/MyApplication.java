import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.SamplingXYLineRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class MyApplication extends JFrame implements ActionListener{
    private ArrayList<XYSeries> seriesArrayList;
    private ArrayList<XYPlot> plotArrayList;
    private JFreeChart chart;
    private FileDialog dialog;
    private CombinedDomainXYPlot plots;
    private ChartPanel center;
    private JPanel eastPanel;
    private JLabel fileName;
    private JCheckBox fullSet;

    private MyApplication(){
        //init
        super();
        plotArrayList = new ArrayList<>();
        plots = new CombinedDomainXYPlot();
        chart = new JFreeChart(plots);
        dialog = new FileDialog(this, "MS-21 shit", FileDialog.LOAD);
        center = new ChartPanel(chart);
        eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel,BoxLayout.Y_AXIS));

        fillEastPanel(eastPanel);
        //some settings
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800,600);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
        //Layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(getNorthPanel(), BorderLayout.NORTH);
        mainPanel.add(center, BorderLayout.CENTER);

        JScrollPane pane = new JScrollPane(eastPanel);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainPanel.add(pane, BorderLayout.EAST);

        this.pack();
        this.add(mainPanel);
    }

    private void fillEastPanel(JPanel panel) {
        for (XYPlot plot : plotArrayList) {
            MyCheckBox checkBox = new MyCheckBox(plot);
            checkBox.addActionListener(this);
            panel.add(checkBox);
        }
        panel.revalidate();
        panel.repaint();
    }


    public static void main(String[] args) {
        MyApplication myApplication = new MyApplication();
        myApplication.setVisible(true);
    }

    private CombinedDomainXYPlot getMultiPlot() {
        // ensure seriesArrayList is not null
        if(seriesArrayList == null) return new CombinedDomainXYPlot();

        CombinedDomainXYPlot multiPlot = new CombinedDomainXYPlot();
        // for each series of ArrayList
        for (XYSeries series : seriesArrayList) {
            XYPlot plot = new XYPlot();
            plot.setDataset(new XYSeriesCollection(series));
            NumberAxis axis = new NumberAxis();
            plot.setRangeAxis(new NumberAxis());
            SamplingXYLineRenderer renderer = new SamplingXYLineRenderer();
            renderer.setSeriesStroke(0, new BasicStroke(2.0f));
            plot.setRenderer(renderer);
            multiPlot.add(plot);
        }
        multiPlot.setOrientation(PlotOrientation.VERTICAL);
        return multiPlot;
    }

    private JPanel getNorthPanel() {
        JPanel north = new JPanel();
        north.setLayout(new FlowLayout());
        fileName = new JLabel("Open file: ");
        JButton selectFileButon = new JButton("select file");
        selectFileButon.addActionListener(this);
        fullSet = new JCheckBox("Full set");
        north.add(fileName);
        north.add(selectFileButon);
        north.add(fullSet);
        return north;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource().getClass() == MyCheckBox.class)
        {
            MyCheckBox source = (MyCheckBox) actionEvent.getSource();
            if (source.isSelected())
                plots.add(source.getPlot());
            else
                plots.remove(source.getPlot());
        }
        else if(actionEvent.getSource().getClass() == JButton.class) {
            dialog.setVisible(true);
            if (dialog.getFile() != null) {
                try {
                    plotArrayList = seriesToPlots(Parser.parse(dialog.getDirectory() + dialog.getFile(), fullSet.isSelected()));
                    fileName.setText("File: "+dialog.getFile());
                    fillEastPanel(eastPanel);
                    this.revalidate();
                    this.repaint();
                } catch (ParseException | IOException e) {
                    JOptionPane.showMessageDialog(null, e.toString());
                }
            }
        }
    }

    private ArrayList<XYPlot> seriesToPlots(ArrayList<XYSeries> series) {
        ArrayList<XYPlot> plotsSet = new ArrayList<>();
        for (XYSeries item : series) {
            XYPlot plot = new XYPlot();
            plot.setDataset(new XYSeriesCollection(item));
            NumberAxis axis = new NumberAxis();
            axis.setAutoRangeIncludesZero(false);
            axis.setDefaultAutoRange(new Range(item.getMinY(), item.getMaxY()));
            if (item.getMaxY() - item.getMinY() <= 1.0) {
                axis.setLowerMargin(0.1);
                axis.setUpperMargin(0.1);
            }
            plot.setRangeAxis(axis);
            SamplingXYLineRenderer renderer = new SamplingXYLineRenderer();
            renderer.setSeriesStroke(0,new BasicStroke(2.0f));
            plot.setRenderer(renderer);
            plotsSet.add(plot);
        }
        if(series.size() > 0) {
            NumberAxis axis = (NumberAxis) plots.getDomainAxis();
            axis.setAutoRangeIncludesZero(false);
            axis.setDefaultAutoRange(new Range(series.get(0).getMinX(), series.get(0).getMaxX()));
        }
        return plotsSet;
    }

    //temp
    private ArrayList<XYSeries> setTestSeries() {
        XYSeries x2 = new XYSeries("x^2");
        XYSeries x3 = new XYSeries("x^3");
        for (int i =10 ; i < 21; i++) {
            x2.add(i, i * i);
            x3.add(i, i * i * i);
        }
        ArrayList<XYSeries> arrayList = new ArrayList<>();
        arrayList.add(x2);
        arrayList.add(x3);
        return arrayList;
    }
}