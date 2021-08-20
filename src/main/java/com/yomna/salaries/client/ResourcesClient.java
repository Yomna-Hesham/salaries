package com.yomna.salaries.client;

import org.springframework.web.multipart.MultipartFile;

public interface ResourcesClient {
    void save(MultipartFile file, String path, String filename);
}
