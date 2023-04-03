package com.dougnoel.sentinel.files;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.*;
import java.nio.file.Path;
import java.util.*;


@SuppressWarnings("serial")
public class XlsFile extends TestFile {
    private static final Logger log = LogManager.getLogger(XlsFile.class.getName()); // Create a logger.
    public static final String IOEXCEPTION_CAUGHT_WHILE_PARSING_XLS_FILE = "IOException caught while parsing XLS file {}.";
    private static final int MY_MINIMUM_COLUMN_COUNT = 0;
    private File spreadsheet;
    private Sheet currentSheet;
    private Map<String, Integer> columns;
    public XSSFWorkbook workbook = null;
    public XSSFSheet sheet = null;
    public XSSFRow row = null;
    public XSSFCell cell = null;
    String xlFilePath;
    private int numHeaderRows;
    private List<List<String>> xlsContents;

    public XlsFile(int numberOfHeaderRows) throws IOException {
        super();
        numHeaderRows = numberOfHeaderRows;
    }

    public XlsFile(Path pathToFile, int numberOfHeaderRows) throws IOException {
        super(pathToFile);
        numHeaderRows = numberOfHeaderRows;
        try (InputStream inp = new FileInputStream(pathToFile.toFile())) {
            if (pathToFile.toString().endsWith(".xls")) {
                POIFSFileSystem fs = new POIFSFileSystem(inp);
                HSSFWorkbook wb = new HSSFWorkbook(fs.getRoot(), true);
                HSSFSheet sheet = wb.getSheetAt(0);
                HSSFRow row = sheet.getRow(numHeaderRows);
                HSSFCell cell = row.getCell(0);
                if (cell != null)
                    System.out.println("Data: " + cell);
                else
                    System.out.println("Cell is empty");
                fs.close();
            } else {
                OPCPackage pkg = OPCPackage.open(inp);
                XSSFWorkbook wb = new XSSFWorkbook(pkg);
                {
                    Sheet sheet = wb.getSheetAt(0);
                    Row row = sheet.getRow(numHeaderRows);
                    Cell cell = row.getCell(0);
                    if (cell != null)
                        System.out.println("Data: " + cell);
                    else
                        System.out.println("Cell is empty");
                    pkg.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public XlsFile(File file) throws IOException {
        super();
        spreadsheet = file;
        columns = new HashMap();
    }

    public int getNumberOfDataRows() {
        sheet = workbook.getSheet(xlFilePath);
        int rowCount = sheet.getLastRowNum() + 1;
        return rowCount;
    }

    public int getColumnCount() {
        sheet = workbook.getSheet(xlFilePath);
        row = sheet.getRow(0);
        int colCount = row.getLastCellNum();
        return colCount;
    }
    public String verifyCellDataContains(int rowIndex, String columnHeader, String textToMatch, boolean partialMatch){
        return verifyCellDataContains(rowIndex, getColumnIndex(columnHeader), textToMatch, partialMatch);
    }

    public String verifyCellDataContains(int rowIndex, int columnIndex, String textToMatch, boolean partialMatch) {
        var cell = readCellData(columnIndex, rowIndex);

        if (partialMatch) {
            return StringUtils.contains(String.valueOf(cell), textToMatch) ? null : String.valueOf(cell);
        } else {
            return StringUtils.equals(String.valueOf(cell), textToMatch) ? null : String.valueOf(cell);
        }
    }

    public XSSFCell readCellData(int columnIndex, int rowIndex) {
        int adjustedColumnIndex = columnIndex - 1;
        return readRowData(rowIndex).getCell(adjustedColumnIndex);
    }

    public XSSFRow readRowData(int rowIndex) {
        int actualRowIndex = rowIndex - 1 + numHeaderRows;
        return sheet.getRow(actualRowIndex);
    }

    public int getColumnIndex(String columnHeader){
        return readHeaders().indexOf(columnHeader) + 1;
    }
    public List<String> readHeaders(){
        sheet = workbook.getSheet(xlFilePath);
        if(numHeaderRows < 1){
            throw new IndexOutOfBoundsException("This method is undefined for XLS files without header rows.");
        }
        return (List<String>) sheet.getHeader(); //last header row is treated as the one that the data rows conform to.
    }
    public void switchToSheet(String name) {
        try (var workbooks = WorkbookFactory.create(spreadsheet)) {
            currentSheet = workbooks.getSheet(name);
            currentSheet.getRow(0).forEach(cell -> {
                columns.put(cell.getStringCellValue(), cell.getColumnIndex());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCellData(String column, int row) {
        var dataRow = currentSheet.getRow(row);
        return getCellDataAsString(dataRow.getCell(columns.get(column)));
    }

    private String getCellDataAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                cell.getStringCellValue();
            case NUMERIC:
                String.valueOf((int) cell.getNumericCellValue());
            default: {
            }
        }
        ;
        return null;
    }
}
