/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logiware.dwr;

import com.logiware.edi.xml.Inttra300Creator;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Balaji.E
 */
public class Edi300Dwr {
    public String createInttraXml(String fileNumber,String createOrCancel,HttpServletRequest request) throws Exception {
	return new Inttra300Creator().create(fileNumber, request,createOrCancel);
    }
    public String cancelInttraXml(String fileNumber,String createOrCancel, HttpServletRequest request) throws Exception {
	return new Inttra300Creator().create(fileNumber, request,createOrCancel);
    }
}
