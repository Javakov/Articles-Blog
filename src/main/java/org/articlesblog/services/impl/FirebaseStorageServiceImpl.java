package org.articlesblog.services.impl;

import com.google.cloud.storage.*;

import lombok.extern.slf4j.Slf4j;
import org.articlesblog.config.FirebaseConfig;
import org.articlesblog.services.FirebaseStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class FirebaseStorageServiceImpl implements FirebaseStorageService {
    @Value("${firebase.storage.bucket-name}")
    private String BUCKET_NAME;

    @Value("${firebase.storage.image-name}")
    private String IMAGE_NAME;
    private final Storage storage;

    public FirebaseStorageServiceImpl() throws IOException {
        FirebaseConfig config = new FirebaseConfig();
        storage = config.getStorage();
    }

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                String fileName = UUID.randomUUID() + "-" + Objects.requireNonNull(file.getOriginalFilename()).replaceAll("\\s+", "");
                Path path = Paths.get(fileName);
                Files.copy(file.getInputStream(), path);

                BufferedImage originalImage = ImageIO.read(path.toFile());

                int width = 660;
                int height = (int) (originalImage.getHeight() * ((double) width / originalImage.getWidth()));

                BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());

                Graphics2D g2d = resizedImage.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                g2d.drawImage(originalImage, 0, 0, width, height, null);
                g2d.dispose();

                ImageIO.write(resizedImage, "jpg", path.toFile());

                BlobId blobId = BlobId.of(BUCKET_NAME, "img/" + fileName);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
                Blob blob = storage.create(blobInfo, Files.readAllBytes(path));
                Files.delete(path);

                log.info("Загружено пользовательское изображение...");
                return "https://firebasestorage.googleapis.com/v0/b/" + blob.getBucket() + "/o/" +
                        encodeURIComponent(blob.getName()) + "?alt=media&token=" + UUID.randomUUID();
            } else {
                log.info("Загружено стандартное изображение...");
                return IMAGE_NAME;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    @Override
    public String updateImage(String id, MultipartFile file) {
        deleteImage(id);
        log.info("Изменяем изображение...");
        return uploadImage(file);
    }

    @Override
    public void deleteImage(String id) {
        if (!Objects.equals(id, IMAGE_NAME)) {
            String fileName = id.substring(id.lastIndexOf("/") + 1, id.indexOf("?"));
            storage.get(BUCKET_NAME, fileName).delete();
            log.info("Изображение удалено.");
        }
    }

    private static String encodeURIComponent(String s) {
        String result;
        result = URLEncoder.encode(s, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20")
                .replaceAll("%21", "!")
                .replaceAll("%27", "'")
                .replaceAll("%28", "(")
                .replaceAll("%29", ")")
                .replaceAll("%7E", "~");
        return result;
    }
}
