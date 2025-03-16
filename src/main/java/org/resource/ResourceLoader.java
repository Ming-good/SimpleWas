package org.resource;

import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceLoader {
    private static final Logger logger = LoggerFactory.getLogger(ResourceLoader.class);
    private final String rootDirectory;

    public ResourceLoader(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public InputStream getResource(String filePath) {
        logger.debug("[WAS] resource location : " + rootDirectory + filePath);
        return getClass().getClassLoader().getResourceAsStream(rootDirectory + filePath);
    }
}
