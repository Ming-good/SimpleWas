package org.mapping.strategy;

import org.mapping.ServletFactory;
import org.servlet.SimpleServlet;

public class PackageMappingStrategy implements ServletMappingStrategy{

    @Override
    public SimpleServlet getServlet(String path) {
        return ServletFactory.search(path.replaceAll("/", ""));
    }
}
