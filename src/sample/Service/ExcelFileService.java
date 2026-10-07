package sample.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ExcelFileService {

    private static final String CONFIG_FILE = "student-it.properties";
    private static final String LAST_FILE_KEY = "lastExcelFile";

    private final Path configPath;

    public ExcelFileService() {
        configPath = Path.of(
                System.getProperty("user.home"),
                ".student-it-center",
                CONFIG_FILE
        );
    }

    public void saveCurrentFile(File file) {

        try {
            Files.createDirectories(configPath.getParent());

            Properties properties = new Properties();

            properties.setProperty(
                    LAST_FILE_KEY,
                    file.getAbsolutePath()
            );

            try (var output = Files.newOutputStream(configPath)) {
                properties.store(output, "Student IT Center");
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not save Excel file path.", e
            );
        }
    }

    public File getLastFile() {

        if (!Files.exists(configPath)) {
            return null;
        }

        Properties properties = new Properties();

        try (var input = Files.newInputStream(configPath)) {

            properties.load(input);

            String path =
                    properties.getProperty(LAST_FILE_KEY);

            if (path == null || path.isBlank()) {
                return null;
            }

            File file = new File(path);

            return file.exists() ? file : null;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not read configuration.", e
            );
        }
    }
}