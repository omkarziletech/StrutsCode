package com.gp.cong.logisoft.bc.admin;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.PrintConfig;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.gp.cong.logisoft.domain.Printer;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.UserPrinterAssociation;
import com.gp.cong.logisoft.hibernate.dao.PrintConfigDAO;
import com.gp.cong.logisoft.hibernate.dao.PrinterDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.struts.form.PrinterForm;

public class PrinterBC {

    public List savePrinterInformation(PrinterForm printerForm, User user) throws Exception {
        List printerAddList = new ArrayList();
        UserPrinterAssociation userPrinterAssoc = new UserPrinterAssociation();
        PrintConfigDAO printConfigDAO = new PrintConfigDAO();
        PrintConfig documentNew = printConfigDAO.findById(printerForm.getDocumentId());
        userPrinterAssoc.setDocumentId(documentNew);
        userPrinterAssoc.setPrinterName(printerForm.getPrinterName());
        userPrinterAssoc.setPrinterTray(CommonUtils.isNotEmpty(printerForm.getPrinterTray())
                ? printerForm.getPrinterTray() : null);
        UserDAO userDAO = new UserDAO();
        if (user.getUserId() != null) {
            user = userDAO.findById(user.getUserId());
        }

        if (user.getAssocitedPrinter() == null) {
            user.setAssocitedPrinter(new HashSet<UserPrinterAssociation>());
        }

        user.getAssocitedPrinter().add(userPrinterAssoc);
        for (Iterator iterator = user.getAssocitedPrinter().iterator(); iterator.hasNext();) {
            UserPrinterAssociation userPrinterAssociation = (UserPrinterAssociation) iterator.next();
            printerAddList.add(userPrinterAssociation.getDocumentId());
        }
        userDAO.getCurrentSession().flush();
        return printerAddList;
    }

    public List deletPrinterInformation(PrinterForm printerForm, User user) throws Exception {
        UserDAO userDAO = new UserDAO();
        if (user.getId() != null) {
            user = userDAO.findById(user.getUserId());
        }
        List printerAddList = new ArrayList();
        Iterator iter = user.getAssocitedPrinter().iterator();
        while (iter.hasNext()) {
            UserPrinterAssociation userAssoc = (UserPrinterAssociation) iter.next();
            PrintConfig document = (PrintConfig) userAssoc.getDocumentId();
            long i = 0;
            long j = 0;
            i = Integer.parseInt(printerForm.getPrinterId());
            j = document.getId();
            if (i == j) {
                user.getAssocitedPrinter().remove(userAssoc);
                break;
            }
        }
        for (Iterator iterator = user.getAssocitedPrinter().iterator(); iterator.hasNext();) {
            UserPrinterAssociation userPrinterAssociation = (UserPrinterAssociation) iterator.next();
            printerAddList.add(userPrinterAssociation.getDocumentId());
        }
        userDAO.getCurrentSession().flush();
        return printerAddList;

    }

    public Printer getPrinterName(String printerType, User user) throws Exception {

        PrinterDAO printerDAO = new PrinterDAO();
        Printer printerObj = printerDAO.findById(Integer.parseInt(printerType));
        return printerObj;

    }

    public List getPrinterList(PrinterForm printerForm, User user) throws Exception {
        List printerAddList = new ArrayList();
        UserDAO UserDAO = new UserDAO();
        if (user.getUserId() != null) {
            user = UserDAO.findById(user.getUserId());
        }
        if (user.getAssocitedPrinter() == null) {
            user.setAssocitedPrinter(new HashSet<UserPrinterAssociation>());
        }
        for (Iterator iterator = user.getAssocitedPrinter().iterator(); iterator.hasNext();) {
            UserPrinterAssociation userPrinterAssociation = (UserPrinterAssociation) iterator.next();
            printerAddList.add(userPrinterAssociation.getDocumentId());
        }

        return printerAddList;

    }
}
