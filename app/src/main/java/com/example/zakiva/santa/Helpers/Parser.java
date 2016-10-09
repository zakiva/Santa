package com.example.zakiva.santa.Helpers;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by max on 09/10/16.
 */
public class Parser {
    public ArrayList<HashMap<String, Object>> readClassesExcelFile(String fileName, String SheetName) {

        HashMap<Integer, String> headers = new HashMap<>();
        ArrayList<HashMap<String, Object>> cellArrayHolder = new ArrayList<>();
        try {
            FileInputStream myInput = new FileInputStream(fileName);

            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            HSSFSheet mySheet = myWorkBook.getSheet(SheetName);

            Iterator<Row> rowIter = mySheet.rowIterator();
            HSSFRow myRow = (HSSFRow) rowIter.next();
            Iterator<Cell> cellIter = myRow.cellIterator();
            HSSFCell myCell;

            while(cellIter.hasNext()) {
                myCell = (HSSFCell) cellIter.next();
                headers.put(myCell.getColumnIndex(), myCell.toString());
            }
            while (rowIter.hasNext()) {
                myRow = (HSSFRow) rowIter.next();
                cellIter = myRow.cellIterator();
                HashMap<String, Object> line = new HashMap<>();
                while (cellIter.hasNext()) {
                    myCell = (HSSFCell) cellIter.next();
                    line.put(headers.get(myCell.getColumnIndex()), formatCell(myCell));
                }
                cellArrayHolder.add(line);
            }
            myWorkBook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cellArrayHolder;
    }

    public static Object formatCell(HSSFCell cell) {

        int type;
        Object result;
        type = cell.getCellType();
              if(type==0) {
                  result = (int) cell.getNumericCellValue();
              }
              else{  result = cell.getStringCellValue();
        }
        if (result.toString().contains(",")) {
            return result = mySplit(result.toString());
        } else {
            return result.toString();
        }
    }

    public static Object mySplit(String str) {
        ArrayList<String> aList = new ArrayList<String>(Arrays.asList(str.split(",")));
        return aList;
    }

}
