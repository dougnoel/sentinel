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

    private int numHeaderRows;
    private int numSheets;
    private int sheetNum;
    private ArrayList<ArrayList<String>> xlsContents;

    /**
     * Create default XLS file from most recently-downloaded path.
     * @throws IOException in the case that a file does not exist at that path.
     * @throws InvalidFormatException in the case that a file does not exist at that path.
     */
    public XlsFile() throws IOException, InvalidFormatException {
        this(0,0, 1);
        loadXlsFile();
    }

    /**
     * Create default XLS file from specified path.
     * @throws IOException in the case that a file does not exist at that path.
     * @throws InvalidFormatException in the case that a file does not exist at that path.
     */
    private void loadXlsFile() throws IOException, InvalidFormatException {
        xlsContents = readAllFileContents();
    }

    /**
     * Create a XLS file with the given file format from the XLS file at the specified path.
     * @param numberOfHeaderRows Number of the header rows from the XLS file.
     * @throws IOException in the case that a file does not exist at that path.
     */
    public XlsFile(int numberOfSheets,int sheetNumber, int numberOfHeaderRows) throws IOException {
        super();
        numSheets = numberOfSheets -1;
        numHeaderRows = numberOfHeaderRows;
        sheetNum = sheetNumber;
    }


    /**
     * Create a XLS file configured with the given number of sheets and a given number of header rows from the XLS file at the most recently downloaded path.
     * @param pathToFile Path to the XLS file.
     * @param numberOfHeaderRows Number of the header rows from the XLS file.
     * @throws IOException in the case that a file does not exist at that path.
     */
    public XlsFile(Path pathToFile,int numberOfSheets, int sheetNumber, int numberOfHeaderRows) throws IOException {
        super(pathToFile);
        numSheets = numberOfSheets;
        sheetNum = sheetNumber -1;
        numHeaderRows = numberOfHeaderRows;
        try (InputStream inp = new FileInputStream(pathToFile.toFile())) {
            if (pathToFile.toString().endsWith(".xls")) {
                POIFSFileSystem fs = new POIFSFileSystem(inp);
                HSSFWorkbook wb = new HSSFWorkbook(fs.getRoot(), true);
                HSSFSheet sheet = wb.getSheetAt(sheetNum);
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
                    Sheet sheet = wb.getSheetAt(sheetNum);
                    Row row = sheet.getRow(numHeaderRows);
                    Cell cell = row.getCell(0);
                    if (cell != null)
                        System.out.println("Data: " + cell);
                    else
                        System.out.println("Cell is empty");
                    pkg.close();
                }
            }
            loadXlsFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @return
     */
    public int getNumberOfTotalRows() {
        return xlsContents.size();
    }

    /**
     *
     * @return
     */
    public int getNumberOfDataRows() {
        return xlsContents.size() - numHeaderRows;
    }

    /**
     *
     * @return Number of sheets in a given spreadsheet
     */
    public int getNumberOfSheets(){
        return sheetNum;
    }

    /**
     *
     * @return
     * @throws IOException in the case that a file does not exist at that path.
     * @throws InvalidFormatException in the case that a file does not exist at that path.
     */
    public int getColumnCount() throws IOException, InvalidFormatException {
        XSSFWorkbook workbook = new XSSFWorkbook(this);
        XSSFSheet sheet = workbook.getSheet(String.valueOf(this));
        XSSFRow row = sheet.getRow(0);
        int colCount = row.getLastCellNum();
        return colCount;
    }

    /**
     *
     * @param rowIndex
     * @param columnHeader
     * @param textToMatch
     * @param partialMatch
     * @return
     * @throws IOException in the case that a file does not exist at that path.
     * @throws InvalidFormatException in the case that a file does not exist at that path.
     */
    public String verifyCellDataContains(int sheetNumber, int rowIndex, String columnHeader, String textToMatch, boolean partialMatch) throws IOException, InvalidFormatException {
        return verifyCellDataContains(sheetNumber, rowIndex, getColumnIndex(columnHeader), textToMatch, partialMatch);
    }

    /**
     *
     * @param rowIndex
     * @param columnIndex
     * @param textToMatch
     * @param partialMatch
     * @return
     * @throws IOException in the case that a file does not exist at that path.
     * @throws InvalidFormatException in the case that a file does not exist at that path.
     */
    public String verifyCellDataContains(int sheetNumber, int rowIndex, int columnIndex, String textToMatch, boolean partialMatch) throws IOException, InvalidFormatException {
        var cell = readCellData(columnIndex, rowIndex, sheetNumber);

        if (partialMatch) {
            return StringUtils.contains(String.valueOf(cell), textToMatch) ? null : String.valueOf(cell);
        } else {
            return StringUtils.equals(String.valueOf(cell), textToMatch) ? null : String.valueOf(cell);
        }
    }

    private String readCellData(int columnIndex, int rowIndex, int sheetNumber) {
        int adjustedColumnIndex = columnIndex - 1;
        return readRowData(rowIndex).get(adjustedColumnIndex);
    }

    /**
     *
     * @param rowIndex
     * @return
     */
    public List<String> readRowData(int rowIndex) {
        int actualRowIndex = rowIndex - 1 + numHeaderRows;
        return xlsContents.get(actualRowIndex);
    }

    /**
     *
     * @param sheetNumber The sheet being used
     * @return the Sheet index value
     */
    public List<String> readSheet(int sheetNumber) {
        return xlsContents.get(sheetNumber);
    }

    /**
     *
     * @param columnIndex
     * @param rowIndex
     * @return
     */
    public String readCellData(int columnIndex, int rowIndex) {
        int adjustedColumnIndex = columnIndex - 1;
        return readRowData(rowIndex).get(adjustedColumnIndex);
    }

    /**
     *
     * @param columnHeader
     * @param rowIndex
     * @return
     * @throws IOException in the case that a file does not exist at that path.
     * @throws InvalidFormatException in the case that a file does not exist at that path.
     */
    public String readCellData(String columnHeader, int rowIndex) throws IOException, InvalidFormatException {
        return readCellData(getColumnIndex(columnHeader), rowIndex);
    }

    /**
     * @param columnHeader
     * @return
     * @throws IOException            in the case that a file does not exist at that path.
     * @throws InvalidFormatException in the case that a file does not exist at that path.
     */
    private int getColumnIndex(String columnHeader) throws IOException, InvalidFormatException {
        return readHeaders().indexOf(columnHeader) + 1;
    }

    /**
     *
     * @return
     * @throws InvalidFormatException in the case that a file does not exist at that path.
     * @throws IOException in the case that a file does not exist at that path.
     */
    private List<String> readHeaders() throws InvalidFormatException, IOException {
        if (numHeaderRows < 1) {
            throw new IndexOutOfBoundsException("This method is undefined for XLS files without header rows.");
        }
        OPCPackage pkg = OPCPackage.open(this);
        XSSFWorkbook wb = new XSSFWorkbook(pkg);
        Sheet sheet = wb.getSheetAt(sheetNum);
        Row row = sheet.getRow(0);
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            XSSFCell cell = (XSSFCell) cellIterator.next();
            System.out.print(cell.getStringCellValue() + " ");
        }
        return xlsContents.get(numHeaderRows - 1); //last header row is treated as the one that the data rows conform to.
    }

    /**
     *
     * @return
     * @throws IOException in the case that a file does not exist at that path.
     * @throws InvalidFormatException in the case that a file does not exist at that path.
     */
    public ArrayList<ArrayList<String>> readAllFileContents() throws IOException, InvalidFormatException {
        OPCPackage pkg = OPCPackage.open(this);
        XSSFWorkbook wb = new XSSFWorkbook(pkg);
        Sheet sheet = wb.getSheetAt(sheetNum);
        ArrayList<ArrayList<String>> allFileContents = new ArrayList<>();
        for (Row row : sheet) {
            ArrayList<String> rowData = new ArrayList<>();
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING:
                        rowData.add(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        rowData.add(String.valueOf(cell.getNumericCellValue()));
                        break;
                    case BOOLEAN:
                        rowData.add(String.valueOf(cell.getBooleanCellValue()));
                        break;
                    default:
                        rowData.add("");
                        pkg.close();
                }
            }
            allFileContents.add(rowData);
        }
        return allFileContents;
    }
    /**
     * Returns true if all cells in the given column match the text value given.
     *
     * @param columnHeader String the name of the column
     * @param textToMatch  String the text that should be in every cell
     * @param partialMatch boolean if true, method returns true if each cell contains the textToMatch. if false, method returns true if every cell equals the textToMatch.
     * @return boolean true if the column contains the given text in every cell, false if not
     */
    public boolean verifyAllColumnCellsContain(int sheetNumber, String columnHeader, String textToMatch, boolean partialMatch) throws IOException, InvalidFormatException {
                sheetNum = sheetNumber -1;
             return verifyAllColumnCellsContain(sheetNum,getColumnIndex(columnHeader), textToMatch, partialMatch);
    }



    /**
     * Returns true if all cells in the given column match the text value given.
     *
     * @param columnIndex int the index of the column. Starting at 1.
     * @param textToMatch String the text that should be in every cell
     * @param partialMatch boolean if true, method returns true if each cell contains the textToMatch. if false, method returns true if every cell equals the textToMatch.
     * @return boolean true if the column contains the given text in every cell, false if not
     */
    public boolean verifyAllColumnCellsContain(int sheetNumber, int columnIndex, String textToMatch, boolean partialMatch){
        var allColumnData = readAllCellDataForColumn(sheetNumber, columnIndex);

        if(partialMatch){
            return allColumnData.stream().allMatch(cell -> StringUtils.contains(cell, textToMatch));
        }
        else{
            return allColumnData.stream().allMatch(cell -> StringUtils.equals(cell, textToMatch));
        }
    }

    /**
     * Returns true if all cells in the given column are empty.
     * @param columnHeader String name of the column.
     * @return boolean true if all cells are empty, false otherwise (any cell has content).
     */
    public boolean verifyAllColumnCellsEmpty(String columnHeader) throws IOException, InvalidFormatException {
        return verifyAllColumnCellsEmpty(String.valueOf(getColumnIndex(columnHeader)));
    }

    /**
     * Returns true if all cells in the given column are empty.
     * @param columnIndex int index of the column, starting at 1.
     * @return boolean true if all cells are empty, false otherwise (any cell has content).
     */
    public boolean verifyAllColumnCellsEmpty(int columnIndex, int sheetNumber){
        var allColumnData = readAllCellDataForColumn(columnIndex, sheetNumber);
        return allColumnData.stream().allMatch(StringUtils::isEmpty);
    }

    /**
     * Returns true if all cells in the given columns are not empty (all cells have content).
     * @param columnHeader String name of the column.
     * @return boolean true if all cells are not empty, false otherwise (any cell does not have content).
     */
    public boolean verifyAllColumnCellsNotEmpty(String columnHeader) throws IOException, InvalidFormatException {
        return verifyAllColumnCellsNotEmpty(String.valueOf(getColumnIndex(columnHeader)));
    }

    /**
     * Returns true if all cells in the given columns are not empty (all cells have content).
     * @param columnIndex int index of the column, starting at 1.
     * @return boolean true if all cells are not empty, false otherwise (any cell does not have content).
     */
    public boolean verifyAllColumnCellsNotEmpty(int columnIndex, int sheetNumber){
        var allColumnData = readAllCellDataForColumn(columnIndex, sheetNumber);
        return allColumnData.stream().noneMatch(StringUtils::isEmpty);
    }

    /**
     * Returns true if the xls file has the given header.
     * If partialMatch == true, then the comparison is a "contains", meaning this method will return true if any column contains the given columnHeader.
     * If partialMatch == false, then this comparison is an "equals", meaning this method will return true only if any column exactly matches the given columnHeader.
     * @param columnHeader String name of the column to check existence of.
     * @param partialMatch boolean true if the comparison should be "contains", false if comparison should be "equals".
     * @return boolean true if the csv file has a column header matching the given columnHeader
     */
    public boolean verifyColumnHeaderEquals(String columnHeader, boolean partialMatch) throws IOException, InvalidFormatException {
        var headers = readHeaders();
        if(partialMatch)
            return headers.stream().anyMatch(actualHeader -> StringUtils.contains(actualHeader, columnHeader));
        else
            return headers.stream().anyMatch(actualHeader -> StringUtils.equals(actualHeader, columnHeader));
    }

    /**
     * Returns true if any cell in the given column match the text value given.
     *
     * @param columnHeader String the name of the column
     * @param textToMatch String the text that should be in a cell
     * @param partialMatch boolean if true, method returns true if any cell contains the textToMatch. if false, method returns true if any cell equals the textToMatch.
     * @return boolean true if the column contains the given text in any cell, false if not
     */
    public boolean verifyAnyColumnCellContains(String columnHeader, String textToMatch, boolean partialMatch) throws IOException, InvalidFormatException {
        return verifyAnyColumnCellContains(String.valueOf(getColumnIndex(columnHeader)), textToMatch, partialMatch);
    }

    /**
     * Returns a ;List&lt;String&gt;, each element being a row's data in the given column.
     * @param columnIndex int index of the column, starting at 1.
     * @return ;List&lt;String&gt; consisting of a column's data.
     */
    public List<String> readAllCellDataForColumn(int columnIndex, int sheetNum){
        int adjustedColumnIndex = columnIndex - 1;
        List<String> allCellDataForColumn = new ArrayList<>();
        //skip header row(s), then add the column data from each row
        xlsContents.stream().skip(numHeaderRows).forEach(row -> allCellDataForColumn.add(row.get(adjustedColumnIndex)));
        return allCellDataForColumn;
    }

    /**
     * Returns true if any cells in the given column match the text value given.
     *
     * @param columnIndex int the index of the column. Starting at 1.
     * @param textToMatch String the text that should be in a cell
     * @param partialMatch boolean if true, method returns true if any cell contains the textToMatch. if false, method returns true if any cell equals the textToMatch.
     * @return boolean true if the column contains the given text in any cell, false if not
     */
    public boolean verifyAnyColumnCellContains(int sheetNumber, int columnIndex, String textToMatch, boolean partialMatch){
        var allColumnData = readAllCellDataForColumn(sheetNum, columnIndex);

        if(partialMatch){
            return allColumnData.stream().anyMatch(cell -> StringUtils.contains(cell, textToMatch));
        }
        else{
            return allColumnData.stream().anyMatch(cell -> StringUtils.equals(cell, textToMatch));
        }
    }

}