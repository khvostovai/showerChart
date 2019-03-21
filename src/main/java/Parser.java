
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jfree.data.xy.XYSeries;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by khvostovai on 18.03.2019.
 */
public class Parser {
    public static ArrayList<XYSeries> parse(String fileName) throws ParseException, IOException {
        ArrayList<XYSeries> series = new ArrayList<XYSeries>();
        FileInputStream in = new FileInputStream(fileName);
        HSSFWorkbook wb = new HSSFWorkbook(in);

        int lastCell = 0;
        int lastRow = 0;

        //get firs sheet
        Sheet sheet = wb.getSheetAt(0);

        // add title series to dataset
        Row seriesTitle = sheet.getRow(3);
        Iterator<Cell> iterator = seriesTitle.cellIterator();
        while (true) {
            Cell cell = iterator.next();
            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                series.add(new XYSeries(cell.getStringCellValue()));
            } else throw new ParseException("Can't read title of Series " + cell.getColumnIndex() , 1);

            if (!iterator.hasNext() || cell.getStringCellValue().equals(""))
                break;
            else
                lastCell++;
        }

        // add data to series into data set
        // iterate on rows
        lastRow = sheet.getLastRowNum();
        for (int i = 6; i < lastRow; i++) {
            Row row = sheet.getRow(i);
            double demain = getDoubleValueOfCell(row.getCell(0));
            // iterate on cells
            for (int j = 0; j < lastCell; j++) {
                Cell cell = row.getCell(j);
                series.get(j).add(demain, getDoubleValueOfCell(cell));
            }
        }
        return series;
    }
    private static double getDoubleValueOfCell(Cell cell) throws ParseException {
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return cell.getNumericCellValue();
        }
        else if(cell.getCellType() == Cell.CELL_TYPE_STRING) {
            if (cell.getStringCellValue().equals("")) {
                //TODO make return null if ""
                return Double.parseDouble(null);
            }
            return Double.parseDouble(cell.getStringCellValue().replace(',','.'));
        } else throw new ParseException("Can't read cell, row " +cell.getRowIndex()+ " column " + cell.getColumnIndex(), 1);
    }
}
