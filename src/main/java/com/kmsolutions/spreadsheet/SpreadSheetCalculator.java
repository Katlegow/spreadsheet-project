package com.kmsolutions.spreadsheet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Responsible for performing calculations on calculable cells.
 * <p>
 * User: KatlegoM
 * Date: 2024/03/21
 */
public class SpreadSheetCalculator {
    private final Map<String, Float> MEMO = new HashMap<>();

    /**
     * Performs calculations on calculable cells.
     * @param parsedCSV parsed csv contents
     * @return parsed csv contents with calculated values, calculable cells are now replaced by the values
     */
    public ParsedCSV calculate(ParsedCSV parsedCSV) {
        List<List<String>> records = parsedCSV.records();
        Map<String, String> cells = parsedCSV.cells();
        Map<String, Location> calculableCells = parsedCSV.calculableCells();

        calculableCells.forEach((k, v) -> {
            String[] operatorOperands = k.substring(2, k.length() - 1).split("\\s");
            String[] operands = Arrays.stream(operatorOperands, 1, operatorOperands.length).toArray(String[]::new);
            var result = switch (operatorOperands[0]) {
                case "prod" -> prod(cells, operands);
                case "sum" -> sum(cells, operands);
                default -> throw new IllegalArgumentException("Unexpected operator: " + operatorOperands[0]);
            };
            records.get(v.row()).set(v.col(), String.valueOf(result));
        });
        MEMO.clear();
        return new ParsedCSV(
                records,
                cells,
                parsedCSV.calculableCells(),
                parsedCSV.longestColumn(),
                parsedCSV.largestColumn()
        );
    }

    private Float calculate(Map<String, String> cells, String colum) {
        String[] operatorOperands = colum.substring(2, colum.length() - 1).split("\\s");
        String[] operands = Arrays.stream(operatorOperands, 1, operatorOperands.length).toArray(String[]::new);
        return switch (operatorOperands[0]) {
            case "prod" -> prod(cells, operands);
            case "sum" -> sum(cells, operands);
            default -> throw new IllegalArgumentException("Unexpected operator: " + operatorOperands[0]);
        };
    }

    /**
     * Performs multiplication operation on given operands
     *
     * @param cells individual csv cells and their values, e.g, A1 -> 2, B2 1.5 etc.
     * @param operands list of operands cells, e.g, [A1, B1]
     * @return product performed on operands' results.
     */
    private Float prod(Map<String, String> cells, String... operands) {
        float prod = 1;
        for (String operand : operands) {
            String val = cells.get(operand);
            if (val == null) {
                throw new IllegalArgumentException(String.format("Cell %s is empty or is invalid!", operand));
            } else if (MEMO.containsKey(val)) {
                prod *= MEMO.get(val);
            } else if (Utils.isCalculable(val)) {
                Float calculated = calculate(cells, val);
                prod *= calculated;
                MEMO.put(val, calculated);
            } else {
                float parsedVal = Float.parseFloat(val);
                prod *= parsedVal;
                MEMO.put(val, parsedVal);
            }
        }
        return prod;
    }

    /**
     * Performs addition operation on given operands
     *
     * @param cells individual csv cells and their values, e.g, A1 -> 2, B2 1.5 etc.
     * @param operands list of operands cells, e.g, [A1, B1]
     * @return additions performed on operands' results.
     */
    private Float sum(Map<String, String> cells, String... operands) {
        float sum = 0;
        for (String operand : operands) {
            String val = cells.get(operand);
            if (val == null) {
                throw new IllegalArgumentException(String.format("Cell %s is empty or is invalid!", operand));
            } else if (MEMO.containsKey(val)) {
                sum += MEMO.get(val);
            } else if (Utils.isCalculable(val)) {
                Float calculated = calculate(cells, val);
                sum += calculated;
                MEMO.put(val, calculated);
            } else {
                float parsedVal = Float.parseFloat(val);
                sum += parsedVal;
                MEMO.put(val, parsedVal);
            }
        }
        return sum;
    }

    public static class Utils {
        public static final Pattern SUM = Pattern.compile("^#\\(sum\\s.*\\)$");
        public static final Pattern PROD = Pattern.compile("^#\\(prod\\s.*\\)$");

        public static boolean isCalculable(String column) {
            if (column == null) {
                return false;
            }
            return SUM.matcher(column).matches() || PROD.matcher(column).matches();
        }
    }
}
