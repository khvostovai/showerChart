
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jfree.data.xy.XYSeries;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

class Parser {
    static ArrayList<XYSeries> parse(String fileName) throws ParseException, IOException {
        ArrayList<XYSeries> series = new ArrayList<>();
        FileInputStream in = new FileInputStream(fileName);
        HSSFWorkbook wb = new HSSFWorkbook(in);

        int lastCell = 0;
        int lastRow;

        //get firs sheet
        Sheet sheet = wb.getSheetAt(0);

        // add title series to dataset
        Row seriesTitle = sheet.getRow(2);
        Iterator<Cell> iterator = seriesTitle.cellIterator();
        while (true) {
            Cell cell = iterator.next();
            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                series.add(new XYSeries(cell.getStringCellValue()));
            } else throw new ParseException("Can't read title of Series " + cell.getColumnIndex(), 1);

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
            // if at cell numeric value
            double demain;
            if (row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                demain =  row.getCell(0).getNumericCellValue();
            }
            else if(row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
                String string = row.getCell(0).getStringCellValue().replace(',','.');
                demain = Double.parseDouble(string);
            }
            else throw new ParseException("Unknow format of Cell 0 row " + i, 1);

            // iterate on cells
            for (int j = 0; j < series.size(); j++) {
                Cell cell = row.getCell(j);
                // if at cell numeric value
                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    series.get(j).add(demain, cell.getNumericCellValue());
                }
                else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    String str = cell.getStringCellValue().replace(',','.');
                    if (str.equals("")) series.get(j).add(demain,null);
                    else series.get(j).add(demain, Double.parseDouble(str));
                }
                else series.get(j).add(demain, null);
            }
        }
        return filterSeries(series);
    }

    private static ArrayList<XYSeries> filterSeries(ArrayList<XYSeries> series) {
        ArrayList<XYSeries> tmp = new ArrayList<>();
        for (XYSeries item : series) {
            switch ((String)item.getKey()) {
                case "Time" :
                    tmp.add(item);
                    item.setKey("Время");
                    break;
// output from 1 channel
                case "04.103.020" :
                    tmp.add(item);
                    item.setKey("ГС2 давление");
                    break;
                case "07.031.001" :
                    tmp.add(item);
                    item.setKey("Угол ПОШ 1 канал");
                    break;
                case "07.031.002" :
                    tmp.add(item);
                    item.setKey("лев РР 1 канал");
                    break;
                case "07.031.003" :
                    tmp.add(item);
                    item.setKey("прав РР 1 канал");
                    break;
                case "07.031.004" :
                    tmp.add(item);
                    item.setKey("ПОШ в 0 1 канал");
                    break;
                case "07.031.005" :
                    tmp.add(item);
                    item.setKey("NWS 1 канал");
                    break;
                case "07.031.006" :
                    tmp.add(item);
                    item.setKey("ЭГК 1 канал");
                    break;
                case "07.031.007" :
                    tmp.add(item);
                    item.setKey("КК 1 канал");
                    break;
                case "07.031.008" :
                    tmp.add(item);
                    item.setKey("отказ лев РР 1 канал");
                    break;
                case "07.031.009" :
                    tmp.add(item);
                    item.setKey("отказ прав РР 1 канал");
                    break;
                case "07.031.010" :
                    tmp.add(item);
                    item.setKey("отказ датчика лев РР 1 канал");
                    break;
                case "07.031.011" :
                    tmp.add(item);
                    item.setKey("отказ датчика прав РР 1 канал");
                    break;
                case "07.031.012" :
                    tmp.add(item);
                    item.setKey("отказ СУПК 1 канал");
                    break;
                case "07.031.013" :
                    tmp.add(item);
                    item.setKey("отказ 1 канала 1 канал");
                    break;
                case "07.031.014" :
                    tmp.add(item);
                    item.setKey("отказ 2 канала 1 канал");
                    break;
                case "07.031.015" :
                    tmp.add(item);
                    item.setKey("отказ педалей РН 1 канал");
                    break;
                case "07.031.016" :
                    tmp.add(item);
                    item.setKey("в право 1 канал");
                    break;
                case "07.031.017" :
                    tmp.add(item);
                    item.setKey("в лево 1 канал");
                    break;
                case "07.031.018" :
                    tmp.add(item);
                    item.setKey("удержание в 0 1 канал");
                    break;
                case "07.031.019" :
                    tmp.add(item);
                    item.setKey("уборка шасси 1 канал");
                    break;
                case "07.031.020" :
                    tmp.add(item);
                    item.setKey("NWS 1 канал");
                    break;
                case "07.031.021" :
                    tmp.add(item);
                    item.setKey("педали 1 канал");
                    break;
                case "07.031.022" :
                    tmp.add(item);
                    item.setKey("отказ CAN 1 канал");
                    break;
                case "07.031.023" :
                    tmp.add(item);
                    item.setKey("отказ 1 КСУ 1 канал");
                    break;
                case "07.031.024" :
                    tmp.add(item);
                    item.setKey("отказ 2 КСУ 1 канал");
                    break;
                case "07.031.025" :
                    tmp.add(item);
                    item.setKey("отказ 3 КСУ 1 канал");
                    break;
                case "07.031.026" :
                    tmp.add(item);
                    item.setKey("отказ 4 КСУ 1 канал");
                    break;
                case "07.031.027" :
                    tmp.add(item);
                    item.setKey("отказ 1 СУОСО 1 канал");
                    break;
                case "07.031.028" :
                    tmp.add(item);
                    item.setKey("отказ 2 СУОСО 1 канал");
                    break;
                case "07.031.029" :
                    tmp.add(item);
                    item.setKey("отказ NWS 1 канал");
                    break;
                case "07.031.030" :
                    tmp.add(item);
                    item.setKey("отказ ЭГРП25-100 1 канал");
                    break;
                case "07.031.031" :
                    tmp.add(item);
                    item.setKey("отказ ЭГРП25-700 лев 1 канал");
                    break;
                case "07.031.032" :
                    tmp.add(item);
                    item.setKey("отказ ЭГРП25-700 прав 1 канал");
                case "07.031.033" :
                    tmp.add(item);
                    item.setKey("блок РР по скорости 1 канал");
                    break;
                case "07.031.034" :
                    tmp.add(item);
                    item.setKey("отказ КК 1 канал");
                case "07.031.035" :
                    tmp.add(item);
                    item.setKey("отказ 1 пит 1 канал");
                    break;
                case "07.031.036" :
                    tmp.add(item);
                    item.setKey("отказ 2 пит 1 канал");
                case "07.031.037" :
                    tmp.add(item);
                    item.setKey("отказ 3 пит 1 канал");
                    break;
                case "07.031.038" :
                    tmp.add(item);
                    item.setKey("отказ 4 пит 1 канал");
                case "07.031.039" :
                    tmp.add(item);
                    item.setKey("требуется ТО 1 канал");
                    break;
//output form 2 channel
                case "07.032.001" :
                    tmp.add(item);
                    item.setKey("Угол ПОШ 2 канал");
                    break;
                case "07.032.002" :
                    tmp.add(item);
                    item.setKey("лев РР 2 канал");
                    break;
                case "07.032.003" :
                    tmp.add(item);
                    item.setKey("прав РР 2 канал");
                    break;
                case "07.032.004" :
                    tmp.add(item);
                    item.setKey("ПОШ в 0 2 канал");
                    break;
                case "07.032.005" :
                    tmp.add(item);
                    item.setKey("NWS 2 канал");
                    break;
                case "07.032.006" :
                    tmp.add(item);
                    item.setKey("ЭГК 2 канал");
                    break;
                case "07.032.007" :
                    tmp.add(item);
                    item.setKey("КК 2 канал");
                    break;
                case "07.032.008" :
                    tmp.add(item);
                    item.setKey("отказ лев РР 2 канал");
                    break;
                case "07.032.009" :
                    tmp.add(item);
                    item.setKey("отказ прав РР 2 канал");
                    break;
                case "07.032.010" :
                    tmp.add(item);
                    item.setKey("отказ датчика лев РР 2 канал");
                    break;
                case "07.032.011" :
                    tmp.add(item);
                    item.setKey("отказ датчика прав РР 2 канал");
                    break;
                case "07.032.012" :
                    tmp.add(item);
                    item.setKey("отказ СУПК 2 канал");
                    break;
                case "07.032.013" :
                    tmp.add(item);
                    item.setKey("отказ 1 канала 2 канал");
                    break;
                case "07.032.014" :
                    tmp.add(item);
                    item.setKey("отказ 2 канала 2 канал");
                    break;
                case "07.032.015" :
                    tmp.add(item);
                    item.setKey("отказ педалей РН 2 канал");
                    break;
                case "07.032.016" :
                    tmp.add(item);
                    item.setKey("в право 2 канал");
                    break;
                case "07.032.017" :
                    tmp.add(item);
                    item.setKey("в лево 2 канал");
                    break;
                case "07.032.018" :
                    tmp.add(item);
                    item.setKey("удержание в 0 2 канал");
                    break;
                case "07.032.019" :
                    tmp.add(item);
                    item.setKey("уборка шасси 2 канал");
                    break;
                case "07.032.020" :
                    tmp.add(item);
                    item.setKey("NWS 2 канал");
                    break;
                case "07.032.021" :
                    tmp.add(item);
                    item.setKey("педали 2 канал");
                    break;
                case "07.032.022" :
                    tmp.add(item);
                    item.setKey("отказ CAN 2 канал");
                    break;
                case "07.032.023" :
                    tmp.add(item);
                    item.setKey("отказ 1 КСУ 2 канал");
                    break;
                case "07.032.024" :
                    tmp.add(item);
                    item.setKey("отказ 2 КСУ 2 канал");
                    break;
                case "07.032.025" :
                    tmp.add(item);
                    item.setKey("отказ 3 КСУ 2 канал");
                    break;
                case "07.032.026" :
                    tmp.add(item);
                    item.setKey("отказ 4 КСУ 2 канал");
                    break;
                case "07.032.027" :
                    tmp.add(item);
                    item.setKey("отказ 1 СУОСО 2 канал");
                    break;
                case "07.032.028" :
                    tmp.add(item);
                    item.setKey("отказ 2 СУОСО 2 канал");
                    break;
                case "07.032.029" :
                    tmp.add(item);
                    item.setKey("отказ NWS 2 канал");
                    break;
                case "07.032.030" :
                    tmp.add(item);
                    item.setKey("отказ ЭГРП25-100 2 канал");
                    break;
                case "07.032.031" :
                    tmp.add(item);
                    item.setKey("отказ ЭГРП25-700 лев 2 канал");
                    break;
                case "07.032.032" :
                    tmp.add(item);
                    item.setKey("отказ ЭГРП25-700 прав 2 канал");
                case "07.032.033" :
                    tmp.add(item);
                    item.setKey("блок РР по скорости 2 канал");
                    break;
                case "07.032.034" :
                    tmp.add(item);
                    item.setKey("отказ КК 2 канал");
                case "07.032.035" :
                    tmp.add(item);
                    item.setKey("отказ 1 пит 2 канал");
                    break;
                case "07.032.036" :
                    tmp.add(item);
                    item.setKey("отказ 2 пит 2 канал");
                case "07.032.037" :
                    tmp.add(item);
                    item.setKey("отказ 3 пит 2 канал");
                    break;
                case "07.032.038" :
                    tmp.add(item);
                    item.setKey("отказ 4 пит 2 канал");
                case "07.032.039" :
                    tmp.add(item);
                    item.setKey("требуется ТО 2 канал");
                    break;
// input message from SUOSO



                default:
                    break;
            }
        }
        Collections.sort(tmp, new Comparator<XYSeries>() {
            @Override
            public int compare(XYSeries  s1, XYSeries s2) {
                return ((String) s1.getKey()).compareTo((String) s2.getKey());
            }
        });
        return tmp;
    }

}
