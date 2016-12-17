/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.security;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Vector;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * @description FtpWrapper
 * @author LakshmiNarayanan
 */
public class FtpWrapper extends FTPClient {

    /** A convenience method for connecting and logging in */
    public boolean connectAndLogin(String host, String userName, String password)
            throws IOException, UnknownHostException, FTPConnectionClosedException {
        boolean success = false;
        this.connect(host);
        int reply = getReplyCode();
        if (FTPReply.isPositiveCompletion(reply)) {
            success = this.login(userName, password);
        }
        if (!success) {
            this.disconnect();
        }
        return success;
    }

    public void setPassiveMode(boolean setPassive) {
        if (setPassive) {
            this.enterLocalPassiveMode();
        } else {
            this.enterLocalActiveMode();
        }
    }

    /** Use ASCII or Binary mode for file transfers */
    public void setAsciiFileType(boolean setAscii) throws IOException {
        if (setAscii) {
            this.setFileType(FTP.ASCII_FILE_TYPE);
        } else {
            this.setFileType(FTP.BINARY_FILE_TYPE);
        }
    }

    /** Download a file from the server, and save it to the specified local file */
    public boolean downloadFile(String serverFile, String localFile)
            throws IOException, FTPConnectionClosedException {
        FileOutputStream out = new FileOutputStream(localFile);
        boolean result = this.retrieveFile(serverFile, out);
        out.close();
        return result;
    }

    /** Upload a file to the server */
    public boolean uploadFile(InputStream inputStream, String serverFile)
            throws IOException, FTPConnectionClosedException {
        if (serverFile.contains("/")) {
            this.mkd(serverFile.substring(0, serverFile.lastIndexOf("/")));
        }
        return this.storeFile(serverFile, inputStream);
    }

    /** Get the list of files in the current directory as a Vector of Strings
     * (excludes subdirectories) */
    public Vector listFileNames()
            throws IOException, FTPConnectionClosedException {
        FTPFile[] files = this.listFiles();
        Vector v = new Vector();
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isDirectory()) {
                v.addElement(files[i].getName());
            }
        }
        return v;
    }

    /** Get the list of files in the current directory as a single Strings,
     * delimited by \n (char '10') (excludes subdirectories) */
    public String listFileNamesString()
            throws IOException, FTPConnectionClosedException {
        return this.vectorToString(listFileNames(), "\n");
    }

    /** Get the list of subdirectories in the current directory as a Vector of Strings
     * (excludes files) */
    public Vector listSubdirNames()
            throws IOException, FTPConnectionClosedException {
        FTPFile[] files = this.listFiles();
        Vector v = new Vector();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                v.addElement(files[i].getName());
            }
        }
        return v;
    }

    /** Get the list of subdirectories in the current directory as a single Strings,
     * delimited by \n (char '10') (excludes files) */
    public String listSubdirNamesString()
            throws IOException, FTPConnectionClosedException {
        return this.vectorToString(listSubdirNames(), "\n");
    }

    /** Convert a Vector to a delimited String */
    private String vectorToString(Vector v, String delim) {
        StringBuffer sb = new StringBuffer();
        String s = "";
        for (int i = 0; i < v.size(); i++) {
            sb.append(s).append((String) v.elementAt(i));
            s = delim;
        }
        return sb.toString();
    }
}
