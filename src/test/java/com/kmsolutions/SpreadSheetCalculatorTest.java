package com.kmsolutions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SpreadSheetCalculatorTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldFailOnUnknownOperator() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Unexpected operator: sub");

        // Contains fields that needs to be calculated and their position in the spreadsheet
        Map<String, Location> calculable = new HashMap<>();
        calculable.put("#(sub A1 B1)", new Location(1, 1));

        ParsedCSV pCsv = new ParsedCSV(
                List.of(List.of("Test")),
                new HashMap<>(),
                calculable,
                4,
                1
        );

        new SpreadSheetCalculator()
                .calculate(pCsv);
    }

    @Test
    public void shouldAddOnValidSumOperatorAndOperands() {
        // Contains fields that needs to be calculated and their position in the spreadsheet
        Map<String, Location> calculable = new HashMap<>();
        calculable.put("#(sum A2 B2)", new Location(0, 1));

        // Records in a form of a list
        List<List<String>> records = new ArrayList<>();
        // Row #1
        List<String> row1 = new ArrayList<>();
        row1.add("Sum:");
        row1.add("#(sum A2 B2)");

        // Row #2
        List<String> row2 = new ArrayList<>();
        row2.add("2");
        row2.add("1.5");

        Collections.addAll(records, row1, row2);

        // Cells
        Map<String, String> cells = new HashMap<>();
        cells.put("A1", "Sum:");
        cells.put("B1", "#(sum A2 B2)");
        cells.put("A2", "2");
        cells.put("B2", "1.5");

        ParsedCSV pCsv = new ParsedCSV(
                records,
                cells,
                calculable,
                4,
                2
        );

        pCsv = new SpreadSheetCalculator()
                .calculate(pCsv);

        assertEquals("Incorrect addition", "3.5", pCsv.records().get(0).get(1));
    }

    @Test
    public void shouldMultiplyOnValidSumOperatorAndOperands() {
        // Contains fields that needs to be calculated and their position in the spreadsheet
        Map<String, Location> calculable = new HashMap<>();
        calculable.put("#(prod A2 B2)", new Location(0, 1));

        // Records in a form of a list
        List<List<String>> records = new ArrayList<>();
        // Row #1
        List<String> row1 = new ArrayList<>();
        row1.add("Prod:");
        row1.add("#(prod A2 B2)");

        // Row #2
        List<String> row2 = new ArrayList<>();
        row2.add("2");
        row2.add("1.5");

        Collections.addAll(records, row1, row2);

        // Cells
        Map<String, String> cells = new HashMap<>();
        cells.put("A1", "Prod:");
        cells.put("B1", "#(prod A2 B2)");
        cells.put("A2", "2");
        cells.put("B2", "1.5");

        ParsedCSV pCsv = new ParsedCSV(
                records,
                cells,
                calculable,
                4,
                2
        );

        pCsv = new SpreadSheetCalculator()
                .calculate(pCsv);

        assertEquals("Incorrect multiplication", "3.0", pCsv.records().get(0).get(1));
    }

    @Test
    public void shouldFailOnMultiplyEmptyCell() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Cell A2 is empty or is invalid!");

        // Contains fields that needs to be calculated and their position in the spreadsheet
        Map<String, Location> calculable = new HashMap<>();
        calculable.put("#(prod A2 B2)", new Location(0, 1));

        // Records in a form of a list
        List<List<String>> records = new ArrayList<>();
        // Row #1
        List<String> row1 = new ArrayList<>();
        row1.add("Prod:");
        row1.add("#(prod A2 B2");

        records.add(row1);

        // Cells
        Map<String, String> cells = new HashMap<>();
        cells.put("A1", "Prod:");
        cells.put("B1", "#(prod A2 B2");
        cells.put("A2", null);
        cells.put("B2", null);

        new SpreadSheetCalculator()
                .calculate(new ParsedCSV(
                        records,
                        cells,
                        calculable,
                        4,
                        2
                ));
    }

    @Test
    public void shouldFailOnAddEmptyCell() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Cell A2 is empty or is invalid!");

        // Contains fields that needs to be calculated and their position in the spreadsheet
        Map<String, Location> calculable = new HashMap<>();
        calculable.put("#(sum A2 B2)", new Location(0, 1));

        // Records in a form of a list
        List<List<String>> records = new ArrayList<>();
        // Row #1
        List<String> row1 = new ArrayList<>();
        row1.add("Sum:");
        row1.add("#(sum A2 B2)");

        records.add(row1);

        // Cells
        Map<String, String> cells = new HashMap<>();
        cells.put("A1", "Sum:");
        cells.put("B1", "#(sum A2 B2)");
        cells.put("A2", null);
        cells.put("B2", null);

        new SpreadSheetCalculator()
                .calculate(new ParsedCSV(
                        records,
                        cells,
                        calculable,
                        4,
                        2
                ));
    }

    @Test
    public void shouldMultiplyAndAdd() {
        // Contains fields that needs to be calculated and their position in the spreadsheet
        Map<String, Location> calculable = new HashMap<>();
        calculable.put("#(prod A2 B2)", new Location(0, 1));
        calculable.put("#(prod A3 B3)", new Location(0, 2));
        calculable.put("#(sum B1 C1)", new Location(3, 1));

        // Records in a form of a list
        List<List<String>> records = new ArrayList<>();
        // Row #1
        List<String> row1 = new ArrayList<>();
        row1.add("Prod:");
        row1.add("#(prod A2 B2)");
        row1.add("#(prod A3 B3)");

        // Row #2
        List<String> row2 = new ArrayList<>();
        row2.add("2");
        row2.add("1.5");

        List<String> row3 = new ArrayList<>();
        row3.add("4.5");
        row3.add("2.5");

        List<String> row4 = new ArrayList<>();
        row4.add("Sum test:");
        row4.add("#(sum B1 C1)");

        Collections.addAll(records, row1, row2, row3, row4);

        // Cells
        Map<String, String> cells = new HashMap<>();
        cells.put("A1", "Prod:");
        cells.put("B1", "#(prod A2 B2)");
        cells.put("C1", "#(prod A3 B3)");
        cells.put("A2", "2");
        cells.put("B2", "1.5");
        cells.put("A3", "4.5");
        cells.put("B3", "2.5");
        cells.put("A4", "Sum test:");
        cells.put("B4", "#(sum B1 C1)");

        ParsedCSV pCsv = new SpreadSheetCalculator()
                .calculate(new ParsedCSV(
                        records,
                        cells,
                        calculable,
                        9,
                        3
                ));

        assertEquals("Incorrect multiplication", "3.0", pCsv.records().get(0).get(1));
        assertEquals("Incorrect multiplication", "11.25", pCsv.records().get(0).get(2));
        assertEquals("Incorrect additions", "14.25", pCsv.records().get(3).get(1));
    }

    @Test
    public void shouldAddAndMultiply() {
        // Contains fields that needs to be calculated and their position in the spreadsheet
        Map<String, Location> calculable = new HashMap<>();
        calculable.put("#(sum A2 B2)", new Location(0, 1));
        calculable.put("#(sum A3 B3)", new Location(0, 2));
        calculable.put("#(prod B1 C1)", new Location(3, 1));

        // Records in a form of a list
        List<List<String>> records = new ArrayList<>();
        // Row #1
        List<String> row1 = new ArrayList<>();
        row1.add("Sum:");
        row1.add("#(sum A2 B2)");
        row1.add("#(sum A3 B3)");

        // Row #2
        List<String> row2 = new ArrayList<>();
        row2.add("2");
        row2.add("1.5");

        List<String> row3 = new ArrayList<>();
        row3.add("4.5");
        row3.add("2.5");

        List<String> row4 = new ArrayList<>();
        row4.add("Prod test:");
        row4.add("#(prod B1 C1)");

        Collections.addAll(records, row1, row2, row3, row4);

        // Cells
        Map<String, String> cells = new HashMap<>();
        cells.put("A1", "Prod:");
        cells.put("B1", "#(sum A2 B2)");
        cells.put("C1", "#(sum A3 B3)");
        cells.put("A2", "2");
        cells.put("B2", "1.5");
        cells.put("A3", "4.5");
        cells.put("B3", "2.5");
        cells.put("A4", "Prod test:");
        cells.put("B4", "#(prod B1 C1)");

        ParsedCSV pCsv = new SpreadSheetCalculator()
                .calculate(new ParsedCSV(
                        records,
                        cells,
                        calculable,
                        9,
                        3
                ));

        assertEquals("Incorrect multiplication", "3.5", pCsv.records().get(0).get(1));
        assertEquals("Incorrect multiplication", "7.0", pCsv.records().get(0).get(2));
        assertEquals("Incorrect additions", "24.5", pCsv.records().get(3).get(1));
    }

    @Test
    public void shouldFailOnAddAndMultiplyWithUnknownOperator() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Unexpected operator: sub");
        // Contains fields that needs to be calculated and their position in the spreadsheet
        Map<String, Location> calculable = new HashMap<>();
        calculable.put("#(sum A2 B2)", new Location(0, 1));
        calculable.put("#(sub A3 B3)", new Location(0, 2));
        calculable.put("#(prod B1 C1)", new Location(3, 1));

        // Records in a form of a list
        List<List<String>> records = new ArrayList<>();
        // Row #1
        List<String> row1 = new ArrayList<>();
        row1.add("Sum:");
        row1.add("#(sum A2 B2)");
        row1.add("#(sum A3 B3)");

        // Row #2
        List<String> row2 = new ArrayList<>();
        row2.add("2");
        row2.add("1.5");

        List<String> row3 = new ArrayList<>();
        row3.add("4.5");
        row3.add("2.5");

        List<String> row4 = new ArrayList<>();
        row4.add("Prod test:");
        row4.add("#(prod B1 C1)");

        Collections.addAll(records, row1, row2, row3, row4);

        // Cells
        Map<String, String> cells = new HashMap<>();
        cells.put("A1", "Prod:");
        cells.put("B1", "#(sum A2 B2)");
        cells.put("C1", "#(sum A3 B3)");
        cells.put("A2", "2");
        cells.put("B2", "1.5");
        cells.put("A3", "4.5");
        cells.put("B3", "2.5");
        cells.put("A4", "Prod test:");
        cells.put("B4", "#(prod B1 C1)");

        new SpreadSheetCalculator()
                .calculate(new ParsedCSV(
                        records,
                        cells,
                        calculable,
                        9,
                        3
                ));
    }

    @Test
    public void shouldReturnFalseOnEmptyColumn() {
        assertFalse(SpreadSheetCalculator.Utils.isCalculable(null));
    }
}