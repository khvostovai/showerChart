import org.jfree.data.xy.XYSeries;

/**
 * Created by kortez on 25/03/19.
 */
public class MyXYSeries extends XYSeries {
    //number of column at the table excel log file
    private int numberOfColumn;
    private String annotation;
    private String shortName;

    public MyXYSeries(Comparable key, int n) {
        super(key);
        this.numberOfColumn = n;

    }

    public int getNumberOfColumn() {
        return numberOfColumn;
    }

    public String getAnnotation() {
        return annotation;
    }

    public String getShortName() {
        return shortName;
    }

}
