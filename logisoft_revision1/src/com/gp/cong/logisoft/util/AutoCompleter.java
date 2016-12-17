/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gp.cong.logisoft.util;

/**
 *
 * @author Administrator
 */
public class AutoCompleter{

    private static final String OPEN_UL = "<UL>";
    private static final String CLOSE_UL = "</UL>";
    private static final String OPEN_LI = "<LI>";
    private static final String CLOSE_LI = "</LI>";
    StringBuffer buffer;

    public AutoCompleter() {
        buffer = new StringBuffer(OPEN_UL);
    }

    public void add(String value){
        buffer.append(OPEN_LI);
        buffer.append(value);
        buffer.append(CLOSE_LI);
    }

    public void put(String value){
        add(value);
    }

    public void close(){
        buffer.append(CLOSE_UL);
    }

    @Override
    public String toString(){
        return buffer.toString();
    }
}
