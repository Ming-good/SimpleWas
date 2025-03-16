package org.mapping.strategy;

import org.servlet.SimpleServlet;

public interface ServletMappingStrategy {
    SimpleServlet getServlet(String path) throws Exception;
}
