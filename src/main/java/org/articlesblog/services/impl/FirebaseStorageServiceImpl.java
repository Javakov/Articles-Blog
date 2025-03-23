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

import java.net.URLDecoder;
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
            if (file.isEmpty()) {
                return IMAGE_NAME;
            }

            String fileName = generateFileName(file);
            Path filePath = saveFileToTemp(file, fileName);

            BufferedImage resizedImage = resizeImage(filePath);
            saveResizedImage(filePath, resizedImage);

            Blob blob = uploadToFirebase(fileName, filePath);

            Files.delete(filePath);

            return generateFileUrl(blob);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID() + "-" + Objects.requireNonNull(file.getOriginalFilename()).replaceAll("\\s+", "");
    }

    private Path saveFileToTemp(MultipartFile file, String fileName) throws IOException {
        Path path = Paths.get(fileName);
        Files.copy(file.getInputStream(), path);
        return path;
    }

    private BufferedImage resizeImage(Path path) throws IOException {
        BufferedImage originalImage = ImageIO.read(path.toFile());
        int width = 660;
        int height = (int) (originalImage.getHeight() * ((double) width / originalImage.getWidth()));

        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();

        return resizedImage;
    }

    private void saveResizedImage(Path path, BufferedImage resizedImage) throws IOException {
        ImageIO.write(resizedImage, "jpg", path.toFile());
    }

    private Blob uploadToFirebase(String fileName, Path filePath) throws IOException {
        BlobId blobId = BlobId.of(BUCKET_NAME, "img/" + fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
        return storage.create(blobInfo, Files.readAllBytes(filePath));
    }

    private String generateFileUrl(Blob blob) {
        return "https://firebasestorage.googleapis.com/v0/b/" + blob.getBucket() + "/o/" +
                encodeURIComponent(blob.getName()) + "?alt=media&token=" + UUID.randomUUID();
    }

    @Override
    public String updateImage(String id, MultipartFile file) {
        deleteImage(id);
        return uploadImage(file);
    }

    @Override
    public void deleteImage(String id) {
        if (!Objects.equals(id, IMAGE_NAME)) {
            String fileName = id.substring(id.lastIndexOf("/") + 1, id.indexOf("?"));
            fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            Blob blob = storage.get(BUCKET_NAME, fileName);
            if (blob != null) {
                blob.delete();
                log.info("Deleted image: {}", fileName);
            } else {
                log.warn("Image with filename '{}' not found in Firebase Storage, skipping deletion", fileName);
            }
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
