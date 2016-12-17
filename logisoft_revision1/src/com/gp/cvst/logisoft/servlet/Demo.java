package com.gp.cvst.logisoft.servlet;

public class Demo {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
	String result = "true\"";
	System.out.println(result.replace("\"", "\\\""));
    }
}
