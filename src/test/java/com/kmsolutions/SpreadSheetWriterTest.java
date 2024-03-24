package com.kmsolutions;

import com.kmsolutions.spreadsheet.SpreadSheetWriter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpreadSheetWriterTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldWriteToAFile() throws IOException {
        List<List<String>> records = getRecords();

        final String outputFile = System.getProperty("user.dir") + "/src/test/resources/out/out.txt";
        final String expectedFile = System.getProperty("user.dir") + "/src/test/resources/expected.txt";

        new SpreadSheetWriter(
                10,
                3,
                outputFile,
                records
        ).write();

        File output = new File(outputFile);
        File expected = new File(expectedFile);

        assertTrue(output.exists());
        assertTrue(expected.exists());
        assertEquals(
                "File contents are not the same",
                -1L,
                Files.mismatch(output.toPath(), expected.toPath())
        );
        output.delete();
    }

    @Test
    public void shouldFailToWriteToInvalidFile() {
        final String outputFile = System.getProperty("user.dir") + "/src/test/resources/out/";

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Could not write to a file: " + outputFile);

        List<List<String>> records = getRecords();



        new SpreadSheetWriter(
                10,
                3,
                outputFile,
                records
        ).write();
    }

    private static List<List<String>> getRecords() {
        // Records in a form of a list
        List<List<String>> records = new ArrayList<>();
        // Row #1
        List<String> row1 = new ArrayList<>();
        row1.add("Values");
        row1.add("Factor");

        // Row #2
        List<String> row2 = new ArrayList<>();
        row2.add("#hl");
        row2.add("#hl");

        List<String> row3 = new ArrayList<>();
        row3.add("2");
        row3.add("1.5");
        row3.add("3");

        List<String> row4 = new ArrayList<>();
        row4.add("3");
        row4.add("2");
        row4.add("6");

        List<String> row5 = new ArrayList<>();
        row5.add("4.5");
        row5.add("2.5");
        row5.add("11.25");

        List<String> row6 = new ArrayList<>();
        row6.add(null);
        row6.add(null);
        row6.add("#hl");

        List<String> row7 = new ArrayList<>();
        row7.add(null);
        row7.add("Total:");
        row7.add("20.25");

        List<String> row8 = new ArrayList<>();
        row8.add("Sum test:");
        row8.add("4.5");
        row8.add(null);

        List<String> row9 = new ArrayList<>();
        row9.add("Prod test:");
        row9.add("9.0");
        row9.add(null);

        Collections.addAll(records, row1, row2, row3, row4, row5, row6, row7, row8, row9);
        return records;
    }
}