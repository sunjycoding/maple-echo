package com.sunjy.maple.util;

import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * @author created by sunjy on 12/1/23
 */
public class FileUtils {

    public static String readJsonFile(String filepath) {
        ClassPathResource classPathResource = new ClassPathResource(filepath);
        try (InputStream inputStream = classPathResource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("读取文件出错", e);
        }
    }

    public static BufferedImage readImageFromResources(String imagePath) {
        ClassPathResource classPathResource = new ClassPathResource(imagePath);
        try (InputStream inputStream = classPathResource.getInputStream()) {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("读取图片文件出错", e);
        }
    }

    public static File readFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("文件不存在: " + filePath);
        }
        return file;
    }

    public static BufferedImage readImage(String imagePath) {
        try {
            return ImageIO.read(readFile(imagePath));
        } catch (IOException e) {
            throw new RuntimeException("读取图片错误", e);
        }
    }

    public static void saveFile(File sourceFile, String destinationPath) {
        File destinationFile = new File(destinationPath);
        try {
            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("无法保存文件", e);
        }
    }
}
