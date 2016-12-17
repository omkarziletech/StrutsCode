/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.ims.client;

/**
 *
 * @author Owner
 */
public class Encrypt {
    public static String encrypt(String password, String privateKey){
        String result = "";
        char thisChar;
        char keyChar;
        Integer startChar;
        for(int i=0; i < password.length();i++) {
            thisChar = password.charAt(i);
            startChar = (i % privateKey.length()) - 1;
            if(startChar < 0) {
                startChar = privateKey.length() + startChar;
            }
            keyChar = privateKey.charAt(startChar);
            int j = thisChar;
            int k = keyChar;
            result += (j + k) + "|";
        }
        return result;
    }
    
}
