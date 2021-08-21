package com.yomna.salaries.client;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface ResourcesClient {
    void save(MultipartFile file, String path, String filename);
    File save(String data, String path, String filename);
    File get(String path, String filename);
}
