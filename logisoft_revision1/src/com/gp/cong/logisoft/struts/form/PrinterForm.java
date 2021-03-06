/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.form;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 11-16-2007
 * 
 * XDoclet definition:
 * @struts.form name="printerForm"
 */
public class PrinterForm extends ActionForm {
    /*
     * Generated Methods
     */

    private String printerId;
    private String printerName;
    private Long documentId;
    private String buttonValue;
    private Integer index;
    private List listOfPrinter;
    private String printerTray;

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    /**
     * Method validate
     * @param mapping
     * @param request
     * @return ActionErrors
     */
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Method reset
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        // TODO Auto-generated method stub
        this.documentId = 0l;
        this.printerName = "0";
    }

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public String getPrinterId() {
        return printerId;
    }

    public void setPrinterId(String printerId) {
        this.printerId = printerId;
    }

    public List getListOfPrinter() {
        return listOfPrinter;
    }

    public void setListOfPrinter(List listOfPrinter) {
        this.listOfPrinter = listOfPrinter;
    }

    public String getPrinterTray() {
        return printerTray;
    }

    public void setPrinterTray(String printerTray) {
        this.printerTray = printerTray;
    }
}
