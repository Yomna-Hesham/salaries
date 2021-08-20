package com.yomna.salaries.util;

import com.yomna.salaries.exception.CannotReadFileException;
import com.yomna.salaries.exception.UnsupportedFileTypeException;
import com.yomna.salaries.exception.WrongCsvSchemeException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            successfullyValidateHeaders();
        } catch (IOException e) {
            logger.error("validateHeaders() | Cannot Read File - msg: {}", e.getMessage());
            throw new CannotReadFileException();
        }
    }

    private void successfullyValidateHeaders() throws IOException {
        logger.info("successfullyValidateHeaders() | Start ...");

        BufferedReader fileReader = new BufferedReader(new
                InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
        CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
        CSVRecord fileHeaders = csvParser.getRecords().get(0);

        logger.debug("successfullyValidateHeaders() | Required Headers Size: {}, File Headers Size: {}",
                headers.size(), fileHeaders.size());
        if (headers.size() != fileHeaders.size()) {
            logger.error("successfullyValidateHeaders() | Sizes are not equal");
            throw new WrongCsvSchemeException();
        }

        for (int i = 0; i < headers.size(); i++) {
            logger.debug("successfullyValidateHeaders() | Required Header#{}: {}, File Header#{}: {}",
                    i, headers.get(i), i, fileHeaders.get(i));
            if (!headers.get(i).equals(fileHeaders.get(i))) {
                logger.error("successfullyValidateHeaders() | Headers Mismatch");
                throw new WrongCsvSchemeException();
            }
        }
    }
}
