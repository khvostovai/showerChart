import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
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
    private ArrayList<XYPlot> plotArrayList;
    private FileDialog dialog;
    private CombinedDomainXYPlot plots;
    private JPanel eastPanel;
    private JLabel fileName;
    private JCheckBox fullSet;
    private Parser parser;

    //------------------------------------------------------------------------------------------------------------------
    //Constructor
    //------------------------------------------------------------------------------------------------------------------
    private MyApplication(){
        //init
        super();
        plotArrayList = new ArrayList<>();
        parser = null;

        //main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        //add north panel
        dialog = new FileDialog(this, "MS-21 shit", FileDialog.LOAD);
        mainPanel.add(getNorthPanel(), BorderLayout.NORTH);

        //central panel
        //main domain plot
        plots = new CombinedDomainXYPlot();
        //create chart
        JFreeChart chart = new JFreeChart(plots);
        //delete all legend from chart
        chart.removeLegend();
        //create chart panel
        ChartPanel center = new ChartPanel(chart);
        //disable zoom at Y-axis
        center.setRangeZoomable(false);
        //add to main panel
        mainPanel.add(center, BorderLayout.CENTER);

        //create and add east panel
        eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel,BoxLayout.Y_AXIS));
        JScrollPane pane = new JScrollPane(eastPanel);
        //set scroll policy
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //add to main panel
        mainPanel.add(pane, BorderLayout.EAST);

        //add main panel to Frame
        this.add(mainPanel);

        //some settings
        this.pack();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //set frame to center of screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2 - this.getWidth()/2, dim.height/2 - this.getHeight()/2);
    }
    //------------------------------------------------------------------------------------------------------------------
    //main function Application
    //------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        MyApplication myApplication = new MyApplication();
        myApplication.setVisible(true);
    }

    //------------------------------------------------------------------------------------------------------------------
    //create North panel
    //------------------------------------------------------------------------------------------------------------------
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
    //------------------------------------------------------------------------------------------------------------------
    //add check box with series name to East panel
    //------------------------------------------------------------------------------------------------------------------
    private void fillEastPanel(JPanel panel) {
        for (XYPlot plot : plotArrayList) {
            MyCheckBox checkBox = new MyCheckBox(plot);
            checkBox.addActionListener(this);
            checkBox.setToolTipText(((XYSeriesCollection)plot.getDataset()).getSeries(0).getDescription());
            panel.add(checkBox);
        }
        panel.revalidate();
        panel.repaint();
    }

    //------------------------------------------------------------------------------------------------------------------
    //action application for events
    //------------------------------------------------------------------------------------------------------------------
    public void actionPerformed(ActionEvent actionEvent) {
        //event for check Boxes
        if (actionEvent.getSource().getClass() == MyCheckBox.class)
        {
            MyCheckBox source = (MyCheckBox) actionEvent.getSource();
            if (source.isSelected()) {
                MyXYSeries series = (MyXYSeries) ((XYSeriesCollection)source.getPlot().getDataset()).getSeries(0);
                try {
                    parser.updateMyXYSeries(series);
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                }
                plots.add(source.getPlot());
            }
            else
                plots.remove(source.getPlot());
        }
        //event for open Button
        else if(actionEvent.getSource().getClass() == JButton.class) {
            dialog.setVisible(true);
            if (dialog.getFile() != null) {
                try {
                    //create parser
                    parser = new Parser(dialog.getDirectory()+dialog.getFile());
                    //first of all read title of series
                    plotArrayList = seriesToPlots(parser.parseTitle(fullSet.isSelected()));
                    //set file name to label
                    fileName.setText("File: "+dialog.getFile());
                    fillEastPanel(eastPanel);
                    //repaint all
                    this.revalidate();
                    this.repaint();
                } catch (ParseException | IOException e) {
                    JOptionPane.showMessageDialog(null, e.toString());
                }
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    //convert series to plot add plot to PlotList and setting axis
    //------------------------------------------------------------------------------------------------------------------
    private ArrayList<XYPlot> seriesToPlots(ArrayList<MyXYSeries> series) {
        ArrayList<XYPlot> plotsSet = new ArrayList<>();
        //for each series
        for (XYSeries item : series) {
            //create new plot
            XYPlot plot = new XYPlot();
            //set series as Dataset to Plot
            plot.setDataset(new XYSeriesCollection(item));

            //setting axis
            //crete axis with name series
            NumberAxis axis = new NumberAxis((String) item.getKey());
            axis.setAutoRangeIncludesZero(false);
            axis.setDefaultAutoRange(new Range(item.getMinY(), item.getMaxY()));
            plot.setRangeAxis(axis);

            //setting renders
            SamplingXYLineRenderer renderer = new SamplingXYLineRenderer();
            renderer.setSeriesStroke(0,new BasicStroke(2.0f));
            plot.setRenderer(renderer);
            plotsSet.add(plot);

        }
        // setting Domain axis
        if(series.size() > 0) {
            NumberAxis axis = (NumberAxis) plots.getDomainAxis();
            axis.setAutoRangeIncludesZero(false);
            axis.setDefaultAutoRange(new Range(series.get(0).getMinX(), series.get(0).getMaxX()));
        }
        //return List of Plots
        return plotsSet;
    }

}