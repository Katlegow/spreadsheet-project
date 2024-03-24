package com.kmsolutions.spreadsheet;

/**
 * This class serves as the application container.
 * <p>
 * User: KatlegoM
 * Date: 2024/03/21
 */
public class SpreadSheet {
    private final String inputFileName;
    private final String outputFileName;

    public SpreadSheet(String inputFileName, String outputFileName) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }

    public String run() {
        CSVParser parser = new CSVParser(inputFileName);
        ParsedCSV parsedCSV = parser.parseCSV();
        SpreadSheetCalculator calculator = new SpreadSheetCalculator();
        parsedCSV = calculator.calculate(parsedCSV);
        SpreadSheetWriter writer = new SpreadSheetWriter(
                parsedCSV.longestColumn(),
                parsedCSV.largestColumn(),
                outputFileName,
                parsedCSV.records()
        );
        return writer.write();
    }
}
