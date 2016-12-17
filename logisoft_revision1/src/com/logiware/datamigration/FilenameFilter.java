package com.logiware.datamigration;

import java.io.File;

/**
 * FilenameFilter - filter used to filter files based on extension
 * @author Lakshmi Narayanan
 */
public class FilenameFilter implements java.io.FilenameFilter {

    private final String extension;

    public FilenameFilter(String extension) {
        this.extension = extension;
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith("." + extension);
    }
}
