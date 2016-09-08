package com.example.zakiva.santa.Helpers;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.zakiva.santa.R;
import com.google.firebase.database.DatabaseReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import static com.example.zakiva.santa.Helpers.Infra.addTriviaQuestion;

public class Parser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parser);
        //readExcelFile(this, "/storage/emulated/0/Download/trivia.xls");
    }

    public static void readExcelFile(Context context, String filename) {

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly())
        {
            //Log.e(TAG, "Storage not available or read only");
            Log.d("aaa: ", "1");
            return;
        }

        try{
            // Creating Input Stream
            File file = new File(filename);
            FileInputStream myInput = new FileInputStream(file);

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            /** We now need something to iterate through the cells.**/
            Iterator rowIter = mySheet.rowIterator();
            int key = 44;
            while(rowIter.hasNext()){
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();
                int counter = 0;
                String question = "";
                String ca = "";
                String a1 = "";
                String a2 = "";
                String a3 = "";
                String a4 = "";
                while(cellIter.hasNext()){
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    if (counter == 0){
                        question =  myCell.toString();
                    }
                    if (counter == 1){
                        a1 =  myCell.toString();
                    }
                    if (counter == 2){
                        a2 =  myCell.toString();
                    }
                    if (counter == 3){
                        a3 =  myCell.toString();
                    }
                    if (counter == 4){
                        a4 =  myCell.toString();
                    }
                    if (counter == 5){
                        ca =  myCell.toString();
                    }
                    counter++;
                    //Log.d(TAG, "Cell Value: " +  myCell.toString());
                    //Toast.makeText(context, "cell Value: " + myCell.toString(), Toast.LENGTH_SHORT).show();
                }
                if (ca.equals("1.0")){
                    ca = a1;
                }
                if (ca.equals("2.0")){
                    ca = a2;
                }
                if (ca.equals("3.0")){
                    ca = a3;
                }
                if (ca.equals("4.0")){
                    ca = a4;
                }
                addTriviaQuestion(Integer.toString(key), question, ca, a1, a2, a3, a4);
                key++;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("aaa: ", "3");
        }

        return;
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}
