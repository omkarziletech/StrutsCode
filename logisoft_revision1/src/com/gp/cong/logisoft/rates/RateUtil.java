/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.rates;

/**
 *
 * @author Administrator
 */
public class RateUtil {

    public static void main(String[] args) {
        String text = "003399, 01/15/2009, 03/15/2009";

        if (text.contains(",")) {
            String[] list = text.split(",");
            for (String s : list) {
            }
        }
    }

    private static String getSplitValue(String text, int index) {
        if (text != null && text.contains(",")) {
            String[] list = text.split(",");
            if (list.length > index) {
                return list[index];
            }
        }
        return null;
    }

    public static String getContract(String text) {
        return getSplitValue(text, 0);
    }

    public static String getStartDate(String text) {
        return getSplitValue(text, 1);
    }

    public static String getEndDate(String text) {
        return getSplitValue(text, 2);
    }

    public static boolean isIntegerValue(String txt)throws Exception {
        boolean flag = false;
        if (txt != null) {
            txt = txt.replace(",", "").trim();
                Integer.parseInt(txt);
                flag = true;
        }
        return flag;
    }
}
