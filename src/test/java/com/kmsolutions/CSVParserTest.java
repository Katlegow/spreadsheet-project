package com.kmsolutions;

import com.kmsolutions.exceptions.FileNotFound;
import com.kmsolutions.exceptions.MaximumFileColumnsExceeded;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CSVParserTest {
    private static final String INPUT_FILENAME = System.getProperty("user.dir") + "/src/test/resources/test.csv";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldReturnParsedCSV() {
        CSVParser parser = new CSVParser(INPUT_FILENAME);
        ParsedCSV parsedCSV = parser.parseCSV();
        // Contains individual cells with their values, e.g, A1 -> #(prod A2 B2)
        Map<String, String> cells = parsedCSV.cells();

        assertNotNull("Parsed CSV empty!", parsedCSV);
        assertNotNull(cells);
        // Test longest column length
        assertEquals("Incorrect column width", 10, parsedCSV.longestColumn());
        // Test row with most columns
        assertEquals("Incorrect largest row columns", 4, parsedCSV.largestColumn());

        // Test individual cells
        /*
            ,,,
            Total:,#(sum C6 C7 C8),Sum test:,#(sum A6 B8)
            ,,Prod test:,#(prod A6 A7 B6)
            #hl,#hl,#hl,
            Values,Factor,,
            2,1.5,#(prod A6 B6),
            3,2,#(prod A7 B7),
            4.5,2.5,#(prod A8 B8),
         */
        assertEquals("Cell A5 contains invalid value", "Values", cells.get("A5"));
        assertEquals("Cell B5 contains invalid value", "Factor", cells.get("B5"));
        assertEquals("Cell B6 contains invalid value", "1.5", cells.get("B6"));
        assertEquals("Cell C8 contains invalid value", "#(prod A8 B8)", cells.get("C8"));
    }

    @Test
    public void shouldFailOnFileThatDoesNotExist() {
        expectedException.expect(FileNotFound.class);
        expectedException.expectMessage("File /doesnotexist.csv NOT FOUND!");

        new CSVParser("/doesnotexist.csv").parseCSV();
    }

    @Test
    public void shouldFailOnFileWithColumnsExceeding25() {
        expectedException.expect(MaximumFileColumnsExceeded.class);
        expectedException.expectMessage("Row #1 exceeds maximum allowed columns");

        final String INPUT_FILENAME_25_COLS = System.getProperty("user.dir") + "/src/test/resources/morecolumns.csv";
        new CSVParser(INPUT_FILENAME_25_COLS).parseCSV();
    }
}