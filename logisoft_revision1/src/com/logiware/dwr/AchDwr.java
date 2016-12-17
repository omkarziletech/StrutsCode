package com.logiware.dwr;

import java.net.InetAddress;

/**
 *
 * @author Lakshminarayanan
 */
public class AchDwr implements java.io.Serializable{
    private static final long serialVersionUID = 5147830572046103489L;

    public String validateHostAddress(String ipAddress)throws Exception{
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            return inetAddress.getHostAddress();
        }
    }