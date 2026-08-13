package az.ibrahim.libraryapi.service.inter;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String store(MultipartFile file, String fileName);

    Resource load(String filePath);

    void delete(String filePath);
}