package aqa.torria.rangiffler.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    public static String loadResourceAsString(String resourcePath) {
        try {
            ClassPathResource resource = new ClassPathResource(resourcePath);
            return FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource: " + resourcePath, e);
        }
    }

    public static byte[] loadResourceAsByteArray(String resourcePath) {
        try {
            ClassPathResource resource = new ClassPathResource(resourcePath);
            return FileCopyUtils.copyToByteArray(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource: " + resourcePath, e);
        }
    }

}
