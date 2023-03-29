package seedu.dengue.commons.util;

import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import seedu.dengue.commons.core.LogsCenter;
import seedu.dengue.commons.exceptions.DataConversionException;

/**
 * Converts a Java object instance to CSV and vice versa
 */
public class CsvUtil {

    private static final Logger logger = LogsCenter.getLogger(CsvUtil.class);

    /**
     * Returns the List of T from the given csv file or {@code Optional.empty()} object if the file is not found.
     * If any values are missing from the file, it will throw an error, as long as the file is a valid csv file.
     * @param filePath cannot be null.
     * @param classOfObjectToDeserialize Csv file has to correspond to the class of T given.
     * @throws DataConversionException if the file format is not as expected.
     */
    public static <T> Optional<List<T>> readCsvFile(
            Path filePath, Class<T> classOfObjectToDeserialize) throws DataConversionException {
        requireNonNull(filePath);

        if (!Files.exists(filePath)) {
            logger.info("Csv file " + filePath + " not found");
            return Optional.empty();
        }

        List<T> csvFile;

        try {
            csvFile = readFromCsvFile(filePath, classOfObjectToDeserialize);
        } catch (IOException e) {
            logger.warning("Error reading from Csv file " + filePath + ": " + e);
            throw new DataConversionException(e);
        } catch (Exception e) {
            logger.warning(e.getMessage());
            throw new DataConversionException(e);
        }

        return Optional.of(csvFile);
    }

    /**
     * Saves the Csv object to the specified file.
     * Overwrites existing file if it exists, creates a new file if it doesn't.
     * @param data takes in a List of String arrays for the data
     * @param filePath cannot be null
     * @param header takes in the String array for the data header
     * @throws IOException if there was an error during writing to the file
     */
    public static <T> void saveCsvFile(List<String[]> data, Path filePath, String[] header)
            throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        requireNonNull(filePath);
        requireNonNull(data);

        writeToCsvFile(filePath, header, data);
    }

    /**
     * Creates a new CSV reader with the specified input reader and target class.
     * @param r the input reader for the CSV data
     * @param instanceClass the target class for the CSV data
     * @return a CsvToBean object configured to read CSV data from the specified reader
     */
    public static <T> CsvToBean<T> createCsvReader(Reader r, Class<T> instanceClass) {
        CsvToBean csvToBean = new CsvToBeanBuilder(r)
                .withType(instanceClass)
                .withSeparator(',')
                .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .build();
        return csvToBean;
    }

    /**
     * Creates a new CSV writer with the specified output writer and mapping strategy.
     * @param wr the output writer for the CSV data
     * @param ms the mapping strategy for the CSV data
     * @return StatefulBeanToCsv object configured to write CSV data to the specified writer
     */
    public static <T> StatefulBeanToCsv<T> createCsvWriter(Writer wr, ColumnPositionMappingStrategy<T> ms) {
        StatefulBeanToCsv csvWriter = new StatefulBeanToCsvBuilder(wr)
                .withMappingStrategy(ms)
                .withSeparator(',')
                .build();
        return csvWriter;
    }

    /**
     * Assumes Csv file exists
     */
    public static Reader readFile(Path file) throws FileNotFoundException {
        return new BufferedReader(new FileReader(file.toString()));
    }

    /**
     * Assumes Csv file exists
     */
    public static <T> List<T> readFromCsvFile(Path file, Class<T> instanceClass)
            throws IOException, DataConversionException {
        Reader reader = readFile(file);
        CsvToBean<T> csvReader = createCsvReader(reader, instanceClass);
        List<T> parsed = csvReader.parse();
        if (parsed.isEmpty()) {
            throw new DataConversionException(new Exception("Data is empty!"));
        }
        reader.close();
        return parsed;
    }

    /**
     * Writes the specified data to a CSV file at the given path using the provided header row.
     * @param file the path to the CSV file to write to
     * @param header the header row to include in the CSV file
     * @param data the data to write to the CSV file
     * @throws IOException if an I/O error occurs while writing to the CSV file
     * @throws CsvDataTypeMismatchException if the data provided is of the wrong type for a CSV field
     * @throws CsvRequiredFieldEmptyException if a required CSV field is empty
     */
    public static void writeToCsvFile(Path file, String[] header, List<String[]> data)
            throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        FileUtil.createIfMissing(file);
        FileWriter writer = new FileWriter(file.toString());
        char sp = ',';
        char qc = CSVWriter.NO_QUOTE_CHARACTER;
        String le = CSVWriter.DEFAULT_LINE_END;
        char ec = '\\';
        CSVWriter csvWriter = new CSVWriter(writer, sp, qc, ec, le);
        csvWriter.writeNext(header);
        csvWriter.writeAll(data);
        csvWriter.close();
    }
}
