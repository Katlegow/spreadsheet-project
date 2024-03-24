package com.kmsolutions;

import com.kmsolutions.exceptions.FileNotFound;
import com.kmsolutions.exceptions.MaximumFileColumnsExceeded;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for parsing given csv file.
 */
public class CSVParser {
    private static final String CSV_DELIMITER = ",";
    private static final int MAX_COLUMNS = 25;
    private final String fileName;

    public CSVParser(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Parses the csv file into list of records, individual cells and other metadata.
     *
     * @return Parsed csv contents.
     */
    public ParsedCSV parseCSV() {
        List<List<String>> records = new ArrayList<>();
        Map<String, String> cells = new HashMap<>();
        Map<String, Location> calculableCells = new HashMap<>();
        int longestColumn = 0;
        int largestColumns = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(this.fileName))) {
            int row = 0;
            String record;
            while ((record = reader.readLine()) != null) {
                int chr = 65;
                String[] splitLine = record.split(CSV_DELIMITER);

                if (splitLine.length > MAX_COLUMNS) {
                    throw new MaximumFileColumnsExceeded(String.format("Row #%d exceeds maximum allowed columns", (row + 1)));
                }

                for (int col = 0; col < splitLine.length; col++) {
                    String column = splitLine[col];
                    cells.put(String.format("%s%d", (char) chr, (row + 1)), column);
                    if (SpreadSheetCalculator.Utils.isCalculable(column)) {
                        calculableCells.put(column, new Location(row, col));
                    }
                    if (column != null && !SpreadSheetCalculator.Utils.isCalculable(column)) {
                        longestColumn = Math.max(longestColumn, column.length());
                    }
                    chr++;
                }
                records.add(new ArrayList<>(List.of(splitLine)));
                largestColumns = Math.max(largestColumns, splitLine.length);
                row++;
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFound(String.format("File %s NOT FOUND!", this.fileName), e);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Could not read file: %s", this.fileName), e);
        }
        return new ParsedCSV(
                records,
                cells,
                calculableCells,
                longestColumn,
                largestColumns
        );
    }
}
