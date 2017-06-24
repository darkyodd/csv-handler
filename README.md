## CSV-Handler
 A java-based library that can be utilized to easily work with and manipulate .csv files. This library can handle any file
 compliant with RFC 4180 (see <a href="https://en.wikipedia.org/wiki/Comma-separated_values" target="_blank">the CSV wikipedia page</a>).
#### Example of use:
##### Create a `CSVReader` to read a .csv file, using the default `CSVFileFormat` object:
```
CSVReader reader = new CSVReader(CSVFileFormat.DEFAULT_FORMAT);
```
##### Create a `CSVArray` to manipulate the contents of the file, by reading a `.csv` file stored in a `java.io.File` instance called `testFile`:
```
CSVArray arr = reader.readCSV(testFile);
```
##### Sort the `CSVRecord`s in the `CSVArray` in decending order by the alphanumeric values in their 3rd column:
```
arr.sortBy(3, false, false);
```
##### Remove the column with header name `"useless_data"` from the array:
```
arr.removeColumn("useless_data");
```
##### Write the array to file, but with a different `CSVFileFormat` than was used to read the original data:
 ```
 CSVFileFormat outputFormat = new CSVFileFormat.Builder()
                .delimiter(CSVFileFormat.SEMICOLON_DELIMITER)
                .outputFileLineEnd(CSVFileFormat.CARRIAGE_RETURN_LINE_END)
                .build();
 CSVWriter writer = new CSVWriter(outputFormat);
 writer.write(arr, "newFile.csv", false);
 ```