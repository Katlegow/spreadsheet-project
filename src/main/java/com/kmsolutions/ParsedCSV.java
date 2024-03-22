package com.kmsolutions;

import java.util.List;
import java.util.Map;

public record ParsedCSV(
        List<List<String>> records,
        Map<String, String> cells,
        Map<String, Location> calculableCells,
        int longestColumn,
        int largestColumn) {
}
