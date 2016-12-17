package com.logiware.datamigration;

import java.io.File;
import java.util.Comparator;

/**
 *
 * @author Lakshmi Narayanan
 */
public class FileComparator implements Comparator<File> {

    @Override
    public int compare(File f1, File f2) {
        String name1 = f1.getName();
        String name2 = f2.getName();
        return (name1.startsWith("c") ? name1.substring(7) : name1.substring(8)).compareTo((name2.startsWith("c") ? name2.substring(7) : name2.substring(8)));
    }
}
