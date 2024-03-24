package com.kmsolutions.spreadsheet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Responsible for writing csv file contents to a text file format.
 * <p>
 * User: KatlegoM
 * Date: 2024/03/21
 */
public class SpreadSheetWriter {
    private final String outputFilename;
    private final List<List<String>> records;
    private final int width;
    private final int largestColumn;

    /**
     * Constructor.
     *
     * @param width columns width.
     * @param largestColumns row with most column's length.
     * @param outputFilename file to write output contents to.
     * @param records list of parsed csv contents.
     */
    public SpreadSheetWriter(
            int width,
            int largestColumns,
            String outputFilename,
            List<List<String>> records) {
        this.outputFilename = outputFilename;
        this.records = records;
        this.width = width;
        this.largestColumn = largestColumns;
    }

    /**
     * Writes to the given output file.
     *
     * @return Formatted file content to a given output file.
     */
    public String write() {
        StringBuilder builder = new StringBuilder();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename))) {
            prepareContents(builder);
            writer.write(builder.toString());
        } catch (IOException e) {
            throw new RuntimeException("Could not write to a file: " + outputFilename, e);
        }
        return builder.toString();
    }

    /**
     * Prepares text file contents as string then just write once instead of multiple writes per column.
     *
     * @param builder formatted contents container.
     */
    private void prepareContents(StringBuilder builder) {
        AtomicInteger rows = new AtomicInteger(1);
        records.forEach((record) -> {
            int col = 0;
            for (String column : record) {
                Alignment alignment = getAlignment(column);

                if (alignment.equals(Alignment.NONE)) {
                    builder.append(" ".repeat(width));
                } else if (column.equals("#hl")) {
                    builder.append("-".repeat(width));
                } else {
                    builder.append(pad(alignment, formatNumber(column)));
                }

                if ((col != (record.size() - 1)) || ((col + 1) < largestColumn)) {
                    builder.append("|");
                }
                col++;
            }
            if (rows.get() != records.size() && !record.isEmpty()) {
                builder.append("\n");
            }
            rows.getAndIncrement();
        });
    }

    /**
     * Formats number columns.
     *
     * @param colum column to format.
     * @return formatted column if is a number other column as is.
     */
    private String formatNumber(String colum) {
        try {
            float v = Float.parseFloat(colum);
            return String.valueOf(v);
        } catch (NumberFormatException e) {
            return colum;
        }
    }

    /**
     * Adds padding to a column based on the given alignment.
     *
     * @param alignment specifies where padding should be added.
     * @param column column to be padded or aligned.
     * @return formatted column.
     */
    private String pad(Alignment alignment, String column) {
        final String padding = " ".repeat(width - column.length());
        return switch (alignment) {
            case LEFT -> String.format("%s%s", column, padding);
            case RIGHT -> String.format("%s%s", padding, column);
            case NONE -> column;
        };
    }

    /**
     * Determines the alignment of the given column.
     *
     * @param column column content to determine alignment for.
     * @return column alignment.
     */
    private Alignment getAlignment(String column) {
        if (column == null) {
            return Alignment.NONE;
        }
        try {
            Float.parseFloat(column);
            return Alignment.RIGHT;
        } catch (NumberFormatException exception) {
            return Alignment.LEFT;
        }
    }

    private enum Alignment {
        LEFT, RIGHT, NONE
    }
}
