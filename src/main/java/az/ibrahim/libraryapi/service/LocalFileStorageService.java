package az.ibrahim.libraryapi.service;

import az.ibrahim.libraryapi.config.FileProperties;
import az.ibrahim.libraryapi.exception.FileStorageException;
import az.ibrahim.libraryapi.exception.StoredFileNotFoundException;
import az.ibrahim.libraryapi.service.inter.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path storagePath;

    public LocalFileStorageService(FileProperties properties) {

        this.storagePath = Paths.get(properties.getStorage().getPath())
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            throw new FileStorageException("Could not initialize file storage.");
        }
    }

    @Override
    public String store(MultipartFile file, String fileName) {

        try {
            Path targetPath = storagePath
                    .resolve(fileName)
                    .normalize();

            if (!targetPath.startsWith(storagePath)) {
                throw new FileStorageException("Invalid file path.");
            }

            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return targetPath.toString();

        } catch (IOException e) {
            throw new FileStorageException("Could not store file.");
        }
    }

    @Override
    public Resource load(String filePath) {

        try {
            Path path = Paths.get(filePath)
                    .toAbsolutePath()
                    .normalize();

            if (!path.startsWith(storagePath)) {
                throw new FileStorageException("Invalid file path.");
            }

            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new StoredFileNotFoundException("Stored file not found.");
            }

            return resource;

        } catch (MalformedURLException e) {
            throw new FileStorageException("Could not load file.");
        }
    }

    @Override
    public void delete(String filePath) {

        try {
            Path path = Paths.get(filePath)
                    .toAbsolutePath()
                    .normalize();

            if (!path.startsWith(storagePath)) {
                throw new FileStorageException("Invalid file path.");
            }

            Files.deleteIfExists(path);

        } catch (IOException e) {
            throw new FileStorageException("Could not delete file.");
        }
    }

    @Override
    public List<String> listFiles() {

        try (Stream<Path> paths = Files.list(storagePath)) {

            return paths
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .toList();

        } catch (IOException e) {
            throw new FileStorageException("Could not list stored files.");
        }
    }
}