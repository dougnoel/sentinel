package com.dougnoel.sentinel.files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.EvaluationWorkbook;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ooxml.POIXMLFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.*;
import java.nio.file.Path;
import java.util.*;


@SuppressWarnings("serial")
public class XlsFile extends TestFile{
    private static final Logger log = LogManager.getLogger(XlsFile.class.getName()); // Create a logger.
    public static final String IOEXCEPTION_CAUGHT_WHILE_PARSING_XLS_FILE = "IOException caught while parsing XLS file {}.";

    private WorkbookFactory workbookFactory;
    private List<List<String>> xlsContents;
    private int numHeaderRows;
//    private final int numHeaderRows;

//    /**
//     * Create default XLS file from most recently-downloaded path.
//     * @throws FileNotFoundException in the case that a file does not exist at that path.
//     */
//    public XlsFile() throws FileNotFoundException {
//        FileOutputStream out = new FileOutputStream(pathToFile);
//        numHeaderRows = 0;
//    }

    /**
     * Create default XLS file from specified path.
     * @param pathToFile Path to the XLS file.
     * @throws IOException in the case that a file does not exist at that path.
     */

    public XlsFile(Path pathToFile) throws IOException {
        super(pathToFile);
        InputStream inp = new FileInputStream(pathToFile.toFile());
        Workbook wb = WorkbookFactory.create(inp);
        numHeaderRows = 1;

    }
//
//    /**
//     * Create a XLS Sheet with the given file format from the XLS file at the specified path.
//     *
//     * @param pathToFile     Path to the XLS file.
//     * @param hssfdataFormat XLSFormat the format of the XLS file. See XLSFormat documentation for more info.
//     * @throws FileNotFoundException in the case that a file does not exist at that path.
//     */
//    public XlsFile(Path pathToFile, List<String> hssfdataFormat) throws FileNotFoundException {
//        super(pathToFile);
//        hssfdataFormat = HSSFDataFormat.getBuiltinFormats();
//        numHeaderRows = 1;
//        loadXlsFile();
//    }

    /**
     * Create a XLS file configured with the given number of header rows from the XLS file at the most recently downloaded path.
     * @param numberOfHeaderRows int number of header rows in the XLS file.
     * @throws FileNotFoundException in the case that a file does not exist at that path.
     */
    public XlsFile(int numberOfHeaderRows) throws FileNotFoundException {
        super();
        numHeaderRows = numberOfHeaderRows;
    }

//    /**
//     * Create a XLS file configured with the given number of header rows from the XLS file at the given path.
//     * @param pathToFile Path path to the XLS file.
//     * @param numberOfHeaderRows int number of header rows in the XLS file.
//     * @throws FileNotFoundException in the case that a file does not exist at that path.
//     */
//    public XlsFile(Path pathToFile, int numberOfHeaderRows) throws FileNotFoundException {
//        super(pathToFile);
//        numHeaderRows = numberOfHeaderRows;
//        loadXlsFile();
//    }

    /**
     * Create a XLS file configured with the given number of header rows from the XLS file at the given path.
     *
     * @param pathToFile         Path to the XLS file.
     * @param numHeaderRows int number of header rows in the XLS file.
     * @throws FileNotFoundException in the case that a file does not exist at that path.
     */
    public XlsFile(Path pathToFile, int numHeaderRows) throws IOException {
        super(pathToFile);
        this.numHeaderRows = numHeaderRows;
        try (InputStream inp = new FileInputStream(pathToFile.toFile())) {
            if (pathToFile.toString().endsWith(".xls")) {
                POIFSFileSystem fs = new POIFSFileSystem(inp);
                HSSFWorkbook wb = new HSSFWorkbook(fs.getRoot(), true);
                HSSFSheet sheet = wb.getSheetAt(0);
                HSSFRow row = sheet.getRow(0);
                HSSFCell cell = row.getCell(0);
                if (cell == null)
                    cell = row.createCell(0);
                cell.setCellValue(String.valueOf(CellType.STRING));
                cell.setCellValue("ColumnName");
                fs.close();
            } else {
                OPCPackage pkg = OPCPackage.open(inp);
                XSSFWorkbook wb = new XSSFWorkbook(pkg); {
                Sheet sheet = wb.getSheetAt(0);
                Row row = sheet.getRow(0);
                Cell cell = row.getCell(0);
                if (cell == null)
                    cell = row.createCell(0);
                cell.setCellValue(String.valueOf(CellType.STRING));
                cell.setCellValue("ColumnName");
                pkg.close();
                };
            }

    } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }

//        @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        if (!super.equals(o)) return false;
//        XlsFile xlsFile = (XlsFile) o;
//        return numHeaderRows == xlsFile.numHeaderRows && Objects.equals(csvFormat, csvFile.csvFormat);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(super.hashCode(), csvFormat, numHeaderRows);
//    }
//
//    private XLSParser getParser() throws IOException {
//        return XLSParser.parse(toPath(), Charset.defaultCharset(), csvFormat);
//    }
//
//    private void loadXlsFile(){
//            InputStream inp = new FileInputStream(pathToFile.toFile());
//            HSSFWorkbook wb = new HSSFWorkbook(inp);
//            HSSFSheet sheet=wb.getSheetAt(0);
//            int rowCount=sheet.getLastRowNum()-sheet.getFirstRowNum();
//            for(int i=0;i<=rowCount;i++){
//
//                //get cell count in a row
//                int cellcount=sheet.getRow(i).getLastCellNum();
//
//                //iterate over each cell to print its value
//                System.out.println("Row"+ i+" data is :");
//
//                for(int j=0;j<cellcount;j++){
//                    System.out.print(sheet.getRow(i).getCell(j).getStringCellValue() +",");
//                }
//    }
//
//    /**
//     * Returns the total number of rows in the CSV file, including headers.
//     * @return int the total number of rows in the CSV file, including headers.
//     */
//    public int getNumberOfTotalRows(){
//        return csvContents.size();
//    }
//
//    /**
//     * Returns the number of rows of data in the CSV file. Does not include header rows.
//     * @return int the number of data rows in the CSV file. Does not include header rows.
//     */
//    public int getNumberOfDataRows(){
//        return csvContents.size() - numHeaderRows;
//    }
//
//    /**
//     * Returns a List&lt;String&gt; consisting of each value in a given row.
//     * The row index starts at 1 and factors out the header rows, so that calling this method with rowIndex=1 gets the first row with actual data.
//     * @param rowIndex int index of the first row with real data, starting at 1, taking into consideration row headers.
//     * @return List&lt;String&gt; consisting of each value in a given row.
//     */
//    public List<String> readRowData(int rowIndex){
//        int actualRowIndex = rowIndex - 1 + numHeaderRows;
//        return csvContents.get(actualRowIndex);
//    }
//
//    /**
//     * Returns the value at the given column, row in the file. The row index starts at 1 and factors out the header rows, so that calling this method with rowIndex=1 gets the first row with actual data.
//     * @param columnIndex int index of the column, starting at 1
//     * @param rowIndex int index of the first row with real data, starting at 1, taking into consideration row headers.
//     * @return String the data in the specified cell.
//     */
//    public String readCellData(int columnIndex, int rowIndex) {
//        int adjustedColumnIndex = columnIndex - 1;
//        return readRowData(rowIndex).get(adjustedColumnIndex);
//    }
//
//    /**
//     * Returns the value at the given column, row in the file. The row index starts at 1 and factors out the header rows, so that calling this method with rowIndex=1 gets the first row with actual data.
//     * @param columnHeader String name of the column
//     * @param rowIndex int index of the first row with real data, starting at 1, taking into consideration row headers.
//     * @return String the data in the specified cell.
//     */
//    public String readCellData(String columnHeader, int rowIndex){
//        return readCellData(getColumnIndex(columnHeader), rowIndex);
//    }
//
//    /**
//     * Creates a list of lists, each list being a set of values for each row in the file.
//     * @return List&lt;List&lt;String&gt;&gt; All contents of the file.
//     */
//    public List<List<String>> readAllFileContents() {
//        List<List<String>> allFileContents = new ArrayList<>();
//        try(var parser = getParser()) {
//            parser.getRecords().stream().forEachOrdered(
//                    row -> allFileContents.add(row.toList())
//            );
//            return  allFileContents;
//        }
//        catch (IOException ioe){
//            log.trace(SentinelStringUtils.format(IOEXCEPTION_CAUGHT_WHILE_PARSING_CSV_FILE, toPath()));
//            return Collections.emptyList();
//        }
//    }
//
//    /**
//     * Returns a List&lt;String&gt; consisting of the header values in the row right above where the actual data starts. This depends on numHeaderRows, set when creating the file.
//     * If numHeaderRows &lt; 1, this method throws. If numHeaderRows &gt; 1, this method returns the last header row above the actual data.
//     * @return List&lt;String&gt; consisting of the header values
//     */
//    public List<String> readHeaders(){
//        if(numHeaderRows < 1){
//            throw new IndexOutOfBoundsException("This method is undefined for CSV files without header rows.");
//        }
//        return csvContents.get(numHeaderRows - 1); //last header row is treated as the one that the data rows conform to.
//    }
//
//    /**
//     * Sets the whole file's contents to the passed newFileContents values, where each list of lists is a row.
//     * @param newFileContents List&lt;List&lt;String&gt;&gt; All contents of the file to write.
//     */
//    public void writeFileContents(List<List<String>> newFileContents){
//        try(var printer = new CSVPrinter(new FileWriter(this), csvFormat)){
//            newFileContents.stream().forEachOrdered(row -> {
//                try {
//                    printer.printRecord(row);
//                } catch (IOException e) {
//                    log.trace(SentinelStringUtils.format("Failed inserting record into CSV file. Record: {}",
//                            String.join(", ", row)));
//                }
//            });
//        } catch (IOException e) {
//            throw new FileException(e, this);
//        }
//        csvContents = newFileContents;
//    }
//
//    /**
//     * Gets the index of the given column name, starting at 1.
//     * @param columnHeader String name of the column.
//     * @return int the index of the given column name, starting at 1.
//     */
//    public int getColumnIndex(String columnHeader){
//        return readHeaders().indexOf(columnHeader) + 1;
//    }
//
//    /**
//     * Sets all the cells in the given column to the given value.
//     * @param columnHeader String name of the column.
//     * @param newValue String value to set each cell to.
//     */
//    public void writeAllCellsInColumn(String columnHeader, String newValue) {
//        int columnIndex = getColumnIndex(columnHeader);
//        writeAllCellsInColumn(columnIndex, newValue);
//    }
//
//    /**
//     * Sets all the cells in the given column to the given value.
//     * @param columnIndex int index of the column, starting at 1.
//     * @param newValue String value to set each cell to.
//     */
//    public void writeAllCellsInColumn(int columnIndex, String newValue){
//        int adjustedColumnIndex = columnIndex - 1;
//        csvContents.stream().skip(numHeaderRows).forEach(row -> row.set(adjustedColumnIndex, newValue));
//        writeFileContents(csvContents);
//    }
//
//    /**
//     * Verifies the cell in the given row and column contains or equals the given text.
//     * @param rowIndex int index of the row, starting at 1.
//     * @param columnHeader String name of the column.
//     * @param textToMatch String text to check in the cell.
//     * @param partialMatch boolean if true, text match is 'contains'. if false, text match is 'equals'.
//     * @return boolean true if the cell contains/has the given text.
//     */
//    public String verifyCellDataContains(int rowIndex, String columnHeader, String textToMatch, boolean partialMatch){
//        return verifyCellDataContains(rowIndex, getColumnIndex(columnHeader), textToMatch, partialMatch);
//    }
//
//    /**
//     * Verifies the cell in the given row and column contains or equals the given text.
//     * @param rowIndex int index of the row, starting at 1.
//     * @param columnIndex int index of the column, starting at 1.
//     * @param textToMatch String text to check in the cell.
//     * @param partialMatch boolean if true, text match is 'contains'. if false, text match is 'equals'.
//     * @return boolean true if the cell contains/has the given text.
//     */
//    public String verifyCellDataContains(int rowIndex, int columnIndex, String textToMatch, boolean partialMatch){
//        var cell = readCellData(columnIndex, rowIndex);
//
//        if(partialMatch){
//            return cell.contains(textToMatch) ? null : cell;
//        }
//        else{
//            return StringUtils.equals(cell, textToMatch) ? null : cell;
//        }
//    }
//
//    /**
//     * Returns a ;List&lt;String&gt;, each element being a row's data in the given column.
//     * @param columnHeader String name of the column.
//     * @return ;List&lt;String&gt; consisting of a column's data.
//     */
//    public List<String> readAllCellDataForColumn(String columnHeader) {
//        return readAllCellDataForColumn(getColumnIndex(columnHeader));
//    }
//
//    /**
//     * Returns a ;List&lt;String&gt;, each element being a row's data in the given column.
//     * @param columnIndex int index of the column, starting at 1.
//     * @return ;List&lt;String&gt; consisting of a column's data.
//     */
//    public List<String> readAllCellDataForColumn(int columnIndex){
//        int adjustedColumnIndex = columnIndex - 1;
//        List<String> allCellDataForColumn = new ArrayList<>();
//        //skip header row(s), then add the column data from each row
//        csvContents.stream().skip(numHeaderRows).forEach(row -> allCellDataForColumn.add(row.get(adjustedColumnIndex)));
//        return allCellDataForColumn;
//    }
//
//    /**
//     * Returns true if all cells in the given column match the text value given.
//     *
//     * @param columnHeader String the name of the column
//     * @param textToMatch String the text that should be in every cell
//     * @param partialMatch boolean if true, method returns true if each cell contains the textToMatch. if false, method returns true if every cell equals the textToMatch.
//     * @return boolean true if the column contains the given text in every cell, false if not
//     */
//    public boolean verifyAllColumnCellsContain(String columnHeader, String textToMatch, boolean partialMatch){
//        return verifyAllColumnCellsContain(getColumnIndex(columnHeader), textToMatch, partialMatch);
//    }
//
//    /**
//     * Returns true if all cells in the given column match the text value given.
//     *
//     * @param columnIndex int the index of the column. Starting at 1.
//     * @param textToMatch String the text that should be in every cell
//     * @param partialMatch boolean if true, method returns true if each cell contains the textToMatch. if false, method returns true if every cell equals the textToMatch.
//     * @return boolean true if the column contains the given text in every cell, false if not
//     */
//    public boolean verifyAllColumnCellsContain(int columnIndex, String textToMatch, boolean partialMatch){
//        var allColumnData = readAllCellDataForColumn(columnIndex);
//
//        if(partialMatch){
//            return allColumnData.stream().allMatch(cell -> StringUtils.contains(cell, textToMatch));
//        }
//        else{
//            return allColumnData.stream().allMatch(cell -> StringUtils.equals(cell, textToMatch));
//        }
//    }
//
//    /**
//     * Returns true if all cells in the given column are empty.
//     * @param columnHeader String name of the column.
//     * @return boolean true if all cells are empty, false otherwise (any cell has content).
//     */
//    public boolean verifyAllColumnCellsEmpty(String columnHeader){
//        return verifyAllColumnCellsEmpty(getColumnIndex(columnHeader));
//    }
//
//    /**
//     * Returns true if all cells in the given column are empty.
//     * @param columnIndex int index of the column, starting at 1.
//     * @return boolean true if all cells are empty, false otherwise (any cell has content).
//     */
//    public boolean verifyAllColumnCellsEmpty(int columnIndex){
//        var allColumnData = readAllCellDataForColumn(columnIndex);
//        return allColumnData.stream().allMatch(StringUtils::isEmpty);
//    }
//
//    /**
//     * Returns true if all cells in the given columns are not empty (all cells have content).
//     * @param columnHeader String name of the column.
//     * @return boolean true if all cells are not empty, false otherwise (any cell does not have content).
//     */
//    public boolean verifyAllColumnCellsNotEmpty(String columnHeader){
//        return verifyAllColumnCellsNotEmpty(getColumnIndex(columnHeader));
//    }
//
//    /**
//     * Returns true if all cells in the given columns are not empty (all cells have content).
//     * @param columnIndex int index of the column, starting at 1.
//     * @return boolean true if all cells are not empty, false otherwise (any cell does not have content).
//     */
//    public boolean verifyAllColumnCellsNotEmpty(int columnIndex){
//        var allColumnData = readAllCellDataForColumn(columnIndex);
//        return allColumnData.stream().noneMatch(StringUtils::isEmpty);
//    }
//
//    /**
//     * Returns true if the csv file has the given header.
//     * If partialMatch == true, then the comparison is a "contains", meaning this method will return true if any column contains the given columnHeader.
//     * If partialMatch == false, then this comparison is an "equals", meaning this method will return true only if any column exactly matches the given columnHeader.
//     * @param columnHeader String name of the column to check existence of.
//     * @param partialMatch boolean true if the comparison should be "contains", false if comparison should be "equals".
//     * @return boolean true if the csv file has a column header matching the given columnHeader
//     */
//    public boolean verifyColumnHeaderEquals(String columnHeader, boolean partialMatch){
//        var headers = readHeaders();
//        if(partialMatch)
//            return headers.stream().anyMatch(actualHeader -> StringUtils.contains(actualHeader, columnHeader));
//        else
//            return headers.stream().anyMatch(actualHeader -> StringUtils.equals(actualHeader, columnHeader));
//    }
//
//    /**
//     * Returns true if any cell in the given column match the text value given.
//     *
//     * @param columnHeader String the name of the column
//     * @param textToMatch String the text that should be in a cell
//     * @param partialMatch boolean if true, method returns true if any cell contains the textToMatch. if false, method returns true if any cell equals the textToMatch.
//     * @return boolean true if the column contains the given text in any cell, false if not
//     */
//    public boolean verifyAnyColumnCellContains(String columnHeader, String textToMatch, boolean partialMatch){
//        return verifyAnyColumnCellContains(getColumnIndex(columnHeader), textToMatch, partialMatch);
//    }
//
//    /**
//     * Returns true if any cells in the given column match the text value given.
//     *
//     * @param columnIndex int the index of the column. Starting at 1.
//     * @param textToMatch String the text that should be in a cell
//     * @param partialMatch boolean if true, method returns true if any cell contains the textToMatch. if false, method returns true if any cell equals the textToMatch.
//     * @return boolean true if the column contains the given text in any cell, false if not
//     */
//    public boolean verifyAnyColumnCellContains(int columnIndex, String textToMatch, boolean partialMatch){
//        var allColumnData = readAllCellDataForColumn(columnIndex);
//
//        if(partialMatch){
//            return allColumnData.stream().anyMatch(cell -> StringUtils.contains(cell, textToMatch));
//        }
//        else{
//            return allColumnData.stream().anyMatch(cell -> StringUtils.equals(cell, textToMatch));
//        }
    }

}
