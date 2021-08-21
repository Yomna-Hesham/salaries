package com.yomna.salaries.client.demos;

import com.yomna.salaries.client.ResourcesClient;
import com.yomna.salaries.exception.CannotReadFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Component
public class LocalFileSystemResourcesClient implements ResourcesClient {
    private static final Logger logger = LoggerFactory.getLogger(LocalFileSystemResourcesClient.class);

    @Override
    public void save(MultipartFile file, String path, String filename) {
        try {
            _save(file, path, filename);
        } catch (IOException e) {
            logger.error("save() | IOException: {}", e.getMessage());
            throw new CannotReadFileException();
        }
    }

    private void _save(MultipartFile file, String path, String filename) throws IOException {
        logger.info("save() | Start ...");
        logger.debug("save() |  path: {}, filename: {}", path, filename);

        file.transferTo(createFile(path, filename));
    }

    private File createFile(String path, String filename) throws IOException {
        if (! new File(path).exists()) {
            if (! new File(path).mkdir()) {
                throw new IOException("Cannot make directory '" + path + "'");
            }
        }

        return new File(path + filename);
    }

    @Override
    public File save(String data, String path, String filename) {
        try {
            return _save(data, path, filename);
        } catch (IOException e) {
            logger.error("save() | IOException: {}", e.getMessage());
            throw new CannotReadFileException();
        }
    }

    private File _save(String data, String path, String filename) throws IOException {
        logger.info("save() | Start ...");
        logger.debug("save() |  path: {}, filename: {}, data: {}", path, filename, data);

        File file = createFile(path, filename);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(data);
        fileWriter.close();

        return file;
    }

    @Override
    public File get(String path, String filename) {
        return new File(path + filename);
    }
}
