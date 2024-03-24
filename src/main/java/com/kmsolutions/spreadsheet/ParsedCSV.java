package com.kmsolutions.spreadsheet;

import java.util.List;
import java.util.Map;

/**
 * Contains parsed csv contents.
 * <p>
 * @param records csv contents in a list format.
 * @param cells key value pair individual cell's contents, e.g, A1 -> Values, B1 -> Factor etc.
 * @param calculableCells key value pair cells that are calculable and their location in record's list, e.g, #(prod A1 B1) -> (0,1).
 * @param longestColumn column with most character's length.
 * @param largestColumn row with most columns' length.
 * <p>
 * User: KatlegoM
 * Date: 2024/03/21
 */
public record ParsedCSV(
        List<List<String>> records,
        Map<String, String> cells,
        Map<String, Location> calculableCells,
        int longestColumn,
        int largestColumn) {
}
