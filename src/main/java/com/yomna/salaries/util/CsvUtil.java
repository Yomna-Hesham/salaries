package com.yomna.salaries.util;

import com.yomna.salaries.exception.CannotReadFileException;
import com.yomna.salaries.exception.UnsupportedFileTypeException;
import com.yomna.salaries.exception.WrongCsvSchemeException;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public abstract class CsvUtil {
    private static final Logger logger = LoggerFactory.getLogger(CsvUtil.class);

    private  MultipartFile file;
    private final List<String> acceptedFileExtensions;
    protected List<String> headers;

    public CsvUtil() {
        acceptedFileExtensions = Arrays.asList(".csv", ".xls", ".xlsx");
    }

    public void validateFileFormat(MultipartFile file) {
        logger.info("validateFileFormat() | Start ...");
        logger.debug("validateFileFormat() | Required Headers: {}", headers);

        this.file = file;

        validateFileExtension();
        if (headers != null) {
            validateHeaders();
        }
    }

    private void validateFileExtension() {
        logger.info("validateFileExtension() | Start ...");

        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf('.'));

        logger.debug("validateFileExtension() | File Name: {}, Extension: {}", fileName, extension);

        if (!acceptedFileExtensions.contains(extension)) {
            logger.error("validateFileExtension() | Unsupported File Type");
            throw new UnsupportedFileTypeException();
        }
    }

    private void validateHeaders() {
        logger.info("validateHeaders() | Start ...");

        try {
            _validateHeaders();
        } catch (IOException e) {
            logger.error("validateHeaders() | Cannot Read File - msg: {}", e.getMessage());
            throw new CannotReadFileException();
        }
    }

    private void _validateHeaders() throws IOException {
        logger.info("validateHeaders() | Start ...");

        BufferedReader fileReader = new BufferedReader(new
                InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
        CSVRecord fileHeaders = csvParser.getRecords().get(0);

        logger.debug("validateHeaders() | Required Headers Size: {}, File Headers Size: {}",
                headers.size(), fileHeaders.size());
        if (headers.size() != fileHeaders.size()) {
            logger.error("validateHeaders() | Sizes are not equal");
            throw new WrongCsvSchemeException();
        }

        for (int i = 0; i < headers.size(); i++) {
            logger.debug("validateHeaders() | Required Header#{}: {}, File Header#{}: {}",
                    i, headers.get(i), i, fileHeaders.get(i));
            if (!headers.get(i).equals(fileHeaders.get(i))) {
                logger.error("validateHeaders() | Headers Mismatch");
                throw new WrongCsvSchemeException();
            }
        }
    }

    @SneakyThrows
    public List<?> parseFile(File file) {
        List<CSVRecord> records = CSVParser.parse(file, StandardCharsets.UTF_8, CSVFormat.EXCEL.withHeader()).getRecords();
        logger.debug("Records: {}", records);

        return mapRecords(records);
    }

    protected abstract List<?> mapRecords(List<CSVRecord> records);

    @SneakyThrows
    public String formatDataToCsv(List<?> data) {
        StringWriter writer = new StringWriter();

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL.withHeader(headers.toArray(new String[0])));
        csvPrinter.printRecords(mapData(data));
        csvPrinter.flush();

        return writer.toString();
    }

    protected abstract List<List<String>> mapData(List<?> data);
}
