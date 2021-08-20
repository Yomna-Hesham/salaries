package com.yomna.salaries.client.demos;

import com.yomna.salaries.client.ResourcesClient;
import com.yomna.salaries.exception.CannotReadFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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
        logger.debug("save() |  path: {}", path);

        if (! new File(path).exists()) {
            if (! new File(path).mkdir()) {
                throw new IOException("Cannot make directory '" + path + "'");
            }
        }

        file.transferTo(new File(path + filename));
    }
}
