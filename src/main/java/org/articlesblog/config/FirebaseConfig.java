package org.articlesblog.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Getter
public class FirebaseConfig {
    private static final String FILE_NAME = "src/main/resources/articles-blog-4e455-01a08500da81.json";

    private Storage storage;

    public FirebaseConfig() throws IOException {
        init();
        log.info("Firebase initialized");
    }

    @PostConstruct
    public void init() throws IOException {
        FileInputStream serviceAccount = new FileInputStream(FILE_NAME);

        storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()
                .getService();
    }
}