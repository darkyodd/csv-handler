## CSV-handler
 A java-based library that can be utilized to easily work with and manipulate .csv files.
#### Example:
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
arr.sortBy(3, false, CSVArray.DECENDING_ORDER);
```
##### Remove the column with header name `"useless_data"` from the array:
```
arr.removeColumn("useless_data");
```
##### Write the array to file, but with a different `CSVFileFormat` than was used to read the original data:
 ```
 CSVFileFormat format = new CSVFileFormat.Builder()
                .delimiter(CSVFileFormat.SEMICOLON_DELIMITER)
                .outputFileLineEnd(CSVFileFormat.CARRIAGE_RETURN_LINE_END)
                .build();
        CSVWriter writer = new CSVWriter(format);
        writer.write(arr, "newFile.csv", false);
 ```