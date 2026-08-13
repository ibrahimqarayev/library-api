package az.ibrahim.libraryapi.validator;

import az.ibrahim.libraryapi.config.FileProperties;
import az.ibrahim.libraryapi.exception.FileValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
@RequiredArgsConstructor
public class FileValidator {

    private final FileProperties properties;

    public void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new FileValidationException("File must not be empty.");
        }

        if (file.getSize() > properties.getUpload().getMaxSize().toBytes()) {
            throw new FileValidationException("File size exceeds the maximum allowed size.");
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank()) {
            throw new FileValidationException("File name must not be empty.");
        }

        String extension = getExtension(originalFilename);

        String expectedContentType =
                properties.getUpload()
                        .getAllowedTypes()
                        .get(extension);

        if (expectedContentType == null) {
            throw new FileValidationException("Unsupported file type.");
        }

        if (!expectedContentType.equalsIgnoreCase(file.getContentType())) {

            throw new FileValidationException("Invalid file content type.");
        }

        validateMagicBytes(file, extension);
    }

    public String getExtension(String filename) {

        int lastDotIndex = filename.lastIndexOf('.');

        if (lastDotIndex == -1) {
            return "";
        }

        return filename
                .substring(lastDotIndex)
                .toLowerCase(Locale.ROOT);
    }

    private void validateMagicBytes(
            MultipartFile file,
            String extension) {

        try (InputStream inputStream = file.getInputStream()) {

            if (".pdf".equals(extension)) {
                validatePdf(inputStream);
            } else if (".epub".equals(extension)) {
                validateEpub(file);
            }

        } catch (IOException e) {
            throw new FileValidationException("Could not validate file content.");
        }
    }

    private void validatePdf(InputStream inputStream)
            throws IOException {

        byte[] magicBytes = "%PDF-".getBytes(StandardCharsets.US_ASCII);

        byte[] header = inputStream.readNBytes(magicBytes.length);

        if (!Arrays.equals(header, magicBytes)) {
            throw new FileValidationException(
                    "File content does not match PDF format."
            );
        }
    }

    private void validateEpub(MultipartFile file)
            throws IOException {

        try (ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream())) {

            ZipEntry mimetypeEntry = zipInputStream.getNextEntry();

            if (mimetypeEntry == null || !"mimetype".equals(mimetypeEntry.getName())) {

                throw new FileValidationException("Invalid EPUB file.");
            }

            byte[] content = zipInputStream.readNBytes(20);

            String mimetype = new String(
                    content,
                    StandardCharsets.UTF_8
            );

            if (!"application/epub+zip".equals(mimetype)) {
                throw new FileValidationException("Invalid EPUB file.");
            }
        }
    }
}