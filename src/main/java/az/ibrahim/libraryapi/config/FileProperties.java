package az.ibrahim.libraryapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

import java.util.Map;

@ConfigurationProperties(prefix = "file")
@Getter
@Setter
public class FileProperties {

    private Storage storage = new Storage();
    private Upload upload = new Upload();

    @Getter
    @Setter
    public static class Storage {
        private String path;
    }

    @Getter
    @Setter
    public static class Upload {
        private DataSize maxSize;
        private Map<String, String> allowedTypes;
    }
}