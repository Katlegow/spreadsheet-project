package com.kmsolutions;

/**
 * User: KatlegoM
 * Date: 2024/03/21
 */
public class App {
    public static void main(String[] args) {
        SpreadSheet spreadSheet = new SpreadSheet(
                args[0],
                args[1]
        );
        System.out.println(spreadSheet.run());
    }
}
