package az.ibrahim.libraryapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "async.executor")
@Getter
@Setter
public class AsyncProperties {

    private int corePoolSize = 2;
    private int maxPoolSize = 5;
    private int queueCapacity = 100;
    private String threadNamePrefix = "library-async-";
}