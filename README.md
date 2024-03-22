# Spreadsheet Project

## Prerequisite

* Java 17
* Maven

## Restrictions

* This solution works for any csv file within the first 25 columns otherwise will reject the file.
* Takes in `absolute path` to both input and output file names as parameters.

## Running the project

`cd` into the project directory.

Run the following command to build the project:

    mvn clean package --define maven.test.skip=true

Invoke the application, passing it `abosulte path` to input and output directory, for example:

    java -cp target/spreadsheet-project-1.0.jar com.kmsolutions.App /abosulte/path/to/input/file/input.csv /abosulte/path/to/output/file/out.txt

The above example will run the project and output the spreadsheet on the console also to output file destination provided.

## Runing tests

    mvn clean test