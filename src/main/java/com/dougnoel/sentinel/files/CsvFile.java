package com.dougnoel.sentinel.files;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.dougnoel.sentinel.exceptions.FileException;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;

public class CsvFile extends TestFile{

    private final CSVFormat csvFormat;
    private final int numHeaderRows;

    /**
     * Create default CSV file from most recently-downloaded path.
     * @throws FileNotFoundException in the case that a file does not exist at that path.
     */
    public CsvFile() throws FileNotFoundException {
        super();
        csvFormat = CSVFormat.DEFAULT;
        numHeaderRows = 1;
    }

    /**
     * Create default CSV file from specified path.
     * @param pathToFile Path path to the CSV file.
     * @throws FileNotFoundException in the case that a file does not exist at that path.
     */
    public CsvFile(Path pathToFile) throws FileNotFoundException {
        super(pathToFile);
        csvFormat = CSVFormat.DEFAULT;
        numHeaderRows = 1;
    }

    /**
     * Create a CSV file with the given file format from the CSV file at the specified path.
     * @param pathToFile Path path to the CSV file.
     * @param format CSVFormat the format of the CSV file. See CSVFormat documentation for more info.
     * @throws FileNotFoundException in the case that a file does not exist at that path.
     */
    public CsvFile(Path pathToFile, CSVFormat format) throws FileNotFoundException {
        super(pathToFile);
        csvFormat = format;
        numHeaderRows = 1;
    }

    /**
     * Create a CSV file configured with the given number of header rows from the CSV file at the most recently downloaded path.
     * @param numberOfHeaderRows int number of header rows in the CSV file.
     * @throws FileNotFoundException in the case that a file does not exist at that path.
     */
    public CsvFile(int numberOfHeaderRows) throws FileNotFoundException {
        super();
        csvFormat = CSVFormat.DEFAULT;
        numHeaderRows = numberOfHeaderRows;
    }

    /**
     * Create a CSV file configured with the given number of header rows from the CSV file at the given path.
     * @param pathToFile Path path to the CSV file.
     * @param numberOfHeaderRows int number of header rows in the CSV file.
     * @throws FileNotFoundException in the case that a file does not exist at that path.
     */
    public CsvFile(Path pathToFile, int numberOfHeaderRows) throws FileNotFoundException {
        super(pathToFile);
        csvFormat = CSVFormat.DEFAULT;
        numHeaderRows = numberOfHeaderRows;
    }

    /**
     * Create a CSV file configured with the given number of header rows and CSVFormat from the CSV file at the given path.
     * @param pathToFile Path path to the CSV file.
     * @param format CSVFormat the format of the CSV file. See CSVFormat documentation for more info.
     * @param numberOfHeaderRows int number of header rows in the CSV file.
     * @throws FileNotFoundException in the case that a file does not exist at that path.
     */
    public CsvFile(Path pathToFile, CSVFormat format, int numberOfHeaderRows) throws FileNotFoundException {
        super(pathToFile);
        csvFormat = format;
        numHeaderRows = numberOfHeaderRows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CsvFile csvFile = (CsvFile) o;
        return numHeaderRows == csvFile.numHeaderRows && Objects.equals(csvFormat, csvFile.csvFormat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), csvFormat, numHeaderRows);
    }

    private CSVParser getParser() throws IOException {
        return CSVParser.parse(toPath(), Charset.defaultCharset(), csvFormat);
    }

    /**
     * Returns the number of rows in the CSV file.
     * @return int the number of rows in the CSV file.
     */
    public int getNumberOfRows(){
        try(var parser = getParser()){
            return parser.getRecords().size();
        }
        catch(IOException ioe){
            log.trace(SentinelStringUtils.format("IOException caught while parsing CSV file {}.", toPath()));
            return -1;
        }
    }

    /**
     * Returns a List&lt;String&gt; consisting of each value in a given row.
     * The row index starts at 1 and factors out the header rows, so that calling this method with rowIndex=1 gets the first row with actual data.
     * @param rowIndex int index of the first row with real data, starting at 1, taking into consideration row headers.
     * @return List&lt;String&gt; consisting of each value in a given row.
     */
    public List<String> getRowData(int rowIndex){
        int actualRowIndex = rowIndex - 1 + numHeaderRows;
        try(var parser = getParser()){
            return parser.getRecords().get(actualRowIndex).toList();
        }
        catch(IOException ioe){
            log.trace(SentinelStringUtils.format("IOException caught while parsing row {}. Taking into consideration {} header row(s)", actualRowIndex, numHeaderRows));
            return Collections.emptyList();
        }
    }

    /**
     * Returns the value at the given column, row in the file. The row index starts at 1 and factors out the header rows, so that calling this method with rowIndex=1 gets the first row with actual data.
     * @param columnIndex int index of the column, starting at 1
     * @param rowIndex int index of the first row with real data, starting at 1, taking into consideration row headers.
     * @return String the data in the specified cell.
     */
    public String getCellData(int columnIndex, int rowIndex) {
        int adjustedColumnIndex = columnIndex - 1;
        return getRowData(rowIndex).get(adjustedColumnIndex);
    }

    /**
     * Returns the value at the given column, row in the file. The row index starts at 1 and factors out the header rows, so that calling this method with rowIndex=1 gets the first row with actual data.
     * @param columnHeader String name of the column
     * @param rowIndex int index of the first row with real data, starting at 1, taking into consideration row headers.
     * @return String the data in the specified cell.
     */
    public String getCellData(String columnHeader, int rowIndex){
        return getCellData(getColumnIndex(columnHeader), rowIndex);
    }

    /**
     * Creates a list of lists, each list being a set of values for each row in the file.
     * @return List&lt;List&lt;String&gt;&gt; All contents of the file.
     */
    public List<List<String>> getAllFileContents() {
        List<List<String>> allFileContents = new ArrayList<>();
        try(var parser = getParser()) {
            parser.getRecords().stream().forEachOrdered(
                    row -> allFileContents.add(row.toList())
            );
            return  allFileContents;
        }
        catch (IOException ioe){
            log.trace(SentinelStringUtils.format("IOException caught while parsing CSV file {}.", toPath()));
            return Collections.emptyList();
        }
    }

    /**
     * Returns a List&lt;String&gt; consisting of the header values in the row right above where the actual data starts. This depends on numHeaderRows, set when creating the file.
     * If numHeaderRows &lt; 1, this method throws. If numHeaderRows &gt; 1, this method returns the last header row above the actual data.
     * @return List&lt;String&gt; consisting of the header values
     */
    public List<String> getHeaders(){
        if(numHeaderRows < 1){
            throw new IndexOutOfBoundsException("This method is undefined for CSV files without header rows.");
        }
        try(var parser = getParser()){
            return parser.getRecords().get(numHeaderRows - 1).toList(); //last header row is treated as the one that the data rows conform to.
        }
        catch(IOException ioe){
            log.trace("IOException caught while parsing column headers.");
            return Collections.emptyList();
        }
    }

    /**
     * Sets the whole file's contents to the passed newFileContents values, where each list of lists is a row.
     * @param newFileContents List&lt;List&lt;String&gt;&gt; All contents of the file to write.
     */
    public void setFileContents(List<List<String>> newFileContents){
        try(var printer = new CSVPrinter(new FileWriter(this), csvFormat)){
            newFileContents.stream().forEachOrdered(row -> {
                try {
                    printer.printRecord(row);
                } catch (IOException e) {
                    log.trace(SentinelStringUtils.format("Failed inserting record into CSV file. Record: {}",
                            String.join(", ", row)));
                }
            });
            printer.close(true);
        } catch (IOException e) {
            throw new FileException(e, this);
        }
    }

    /**
     * Gets the index of the given column name, starting at 1.
     * @param columnHeader String name of the column.
     * @return int the index of the given column name, starting at 1.
     */
    public int getColumnIndex(String columnHeader){
        return getHeaders().indexOf(columnHeader) + 1;
    }

    /**
     * Sets all the cells in the given column to the given value.
     * @param columnHeader String name of the column.
     * @param newValue String value to set each cell to.
     */
    public void setAllCellsInColumn(String columnHeader, String newValue) {
        int columnIndex = getColumnIndex(columnHeader);
        setAllCellsInColumn(columnIndex, newValue);
    }

    /**
     * Sets all the cells in the given column to the given value.
     * @param columnIndex int index of the column, starting at 1.
     * @param newValue String value to set each cell to.
     */
    public void setAllCellsInColumn(int columnIndex, String newValue){
        int adjustedColumnIndex = columnIndex - 1;
        var fileContents = getAllFileContents();
        fileContents.stream().skip(numHeaderRows).forEach(row -> row.set(adjustedColumnIndex, newValue));
        setFileContents(fileContents);
    }

    /**
     * Verifies the cell in the given row and column contains or equals the given text.
     * @param rowIndex int index of the row, starting at 1.
     * @param columnHeader String name of the column.
     * @param textToMatch String text to check in the cell.
     * @param partialMatch boolean if true, text match is 'contains'. if false, text match is 'equals'.
     * @return boolean true if the cell contains/has the given text.
     */
    public String verifyCellDataContains(int rowIndex, String columnHeader, String textToMatch, boolean partialMatch){
        return verifyCellDataContains(rowIndex, getColumnIndex(columnHeader), textToMatch, partialMatch);
    }

    /**
     * Verifies the cell in the given row and column contains or equals the given text.
     * @param rowIndex int index of the row, starting at 1.
     * @param columnIndex int index of the column, starting at 1.
     * @param textToMatch String text to check in the cell.
     * @param partialMatch boolean if true, text match is 'contains'. if false, text match is 'equals'.
     * @return boolean true if the cell contains/has the given text.
     */
    public String verifyCellDataContains(int rowIndex, int columnIndex, String textToMatch, boolean partialMatch){
        var cell = getCellData(columnIndex, rowIndex);

        if(partialMatch){
            return cell.contains(textToMatch) ? null : cell;
        }
        else{
            return StringUtils.equals(cell, textToMatch) ? null : cell;
        }
    }

    /**
     * Returns a ;List&lt;String&gt;, each element being a row's data in the given column.
     * @param columnHeader String name of the column.
     * @return ;List&lt;String&gt; consisting of a column's data.
     */
    public List<String> getAllCellDataForColumn(String columnHeader) {
        return getAllCellDataForColumn(getColumnIndex(columnHeader));
    }

    /**
     * Returns a ;List&lt;String&gt;, each element being a row's data in the given column.
     * @param columnIndex int index of the column, starting at 1.
     * @return ;List&lt;String&gt; consisting of a column's data.
     */
    public List<String> getAllCellDataForColumn(int columnIndex){
        int adjustedColumnIndex = columnIndex - 1;
        List<String> allCellDataForColumn = new ArrayList<>();
        //skip header row(s), then add the column data from each row
        getAllFileContents().stream().skip(numHeaderRows).forEach(row -> allCellDataForColumn.add(row.get(adjustedColumnIndex)));
        return allCellDataForColumn;
    }

    /**
     * Returns true if all cells in the given column match the text value given.
     *
     * @param columnHeader String the name of the column
     * @param textToMatch String the text that should be in every cell
     * @param partialMatch boolean if true, method returns true if each cell contains the textToMatch. if false, method returns true if every cell equals the textToMatch.
     * @return boolean true if the column contains the given text in every cell, false if not
     */
    public boolean verifyAllColumnCellsContain(String columnHeader, String textToMatch, boolean partialMatch){
        return verifyAllColumnCellsContain(getColumnIndex(columnHeader), textToMatch, partialMatch);
    }

    /**
     * Returns true if all cells in the given column match the text value given.
     *
     * @param columnIndex int the index of the column. Starting at 1.
     * @param textToMatch String the text that should be in every cell
     * @param partialMatch boolean if true, method returns true if each cell contains the textToMatch. if false, method returns true if every cell equals the textToMatch.
     * @return boolean true if the column contains the given text in every cell, false if not
     */
    public boolean verifyAllColumnCellsContain(int columnIndex, String textToMatch, boolean partialMatch){
        var allColumnData = getAllCellDataForColumn(columnIndex);

        if(partialMatch){
            return allColumnData.stream().allMatch(cell -> StringUtils.contains(cell, textToMatch));
        }
        else{
            return allColumnData.stream().allMatch(cell -> StringUtils.equals(cell, textToMatch));
        }
    }
}
