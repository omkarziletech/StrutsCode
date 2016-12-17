package com.gp.cong.logisoft.edi.gtnexus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.gp.cong.common.CommonConstants;

public class ReadGTnexusXMLFiles {

    private Properties prop = new Properties();

    public List<File> readFiles(String messageType, String osName) throws Exception {
        List<File> fileList = new ArrayList<File>();
        String inputDirectory = "";
        prop.load(getClass().getResourceAsStream(CommonConstants.EDIPROPERTIES));
        if (osName.contains("linux")) {
            if ("997".equals(messageType)) {
                inputDirectory = prop.getProperty("linuxGtnexus997Directory");
            } else {
                inputDirectory = prop.getProperty("linuxGtnexus315Directory");
            }
        } else {
            if ("997".equals(messageType)) {
                inputDirectory = prop.getProperty("gtnexus997Directory");
            } else {
                inputDirectory = prop.getProperty("gtnexus315Directory");
            }
        }
        File homedir = new File(inputDirectory);
        File[] allFiles = homedir.listFiles();
        if (null != allFiles) {
            for (int i = 0; i < allFiles.length; i++) {
                if (allFiles[i].isFile()) {
                    fileList.add(allFiles[i]);
                }
            }
        }
        return fileList;
    }
    //	 Move file (src) to File/directory dest.

    public synchronized void move(File src, File dest) throws Exception {
        copy(src, dest);
        src.delete();
    }
    //Copy file (src) to File/directory dest.

    public synchronized void copy(File src, File dest) throws Exception {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dest);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
