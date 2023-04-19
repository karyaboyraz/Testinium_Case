package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelReader {
    private String fileName;
    private Workbook workbook;

    public ExcelReader(String fileName) throws IOException {
        this.fileName = fileName;
        FileInputStream excelFile = new FileInputStream(fileName);
        workbook = WorkbookFactory.create(excelFile);
        excelFile.close();
    }

    public String getValue(String sheetName, int rowNum, int colNum) {
        Sheet sheet = workbook.getSheet(sheetName);
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            return null;
        }
        Cell cell = row.getCell(colNum);
        if (cell == null) {
            return null;
        }
        return cell.toString();
    }


    public void close() throws IOException {
        workbook.close();
    }
}
