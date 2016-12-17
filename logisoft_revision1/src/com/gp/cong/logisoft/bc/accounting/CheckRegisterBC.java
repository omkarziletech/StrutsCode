package com.gp.cong.logisoft.bc.accounting;

import com.gp.cong.common.DateUtils;
import java.util.List;

import com.gp.cong.logisoft.ExcelGenerator.ExportCheckRegisterToExcel;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.struts.form.CheckRegisterForm;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.reports.CheckRegisterReportCreator;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class CheckRegisterBC {

    public String exportToExcel(CheckRegisterForm checkRegisterForm) throws Exception {
        Collection<Integer> transactionIds = new ArrayList<Integer>();
        String[] ids = StringUtils.split(StringUtils.removeStart(StringUtils.removeStart(checkRegisterForm.getTransactionIds(), ","), ","), ",");
        for (String id : ids) {
            transactionIds.add(Integer.parseInt(id));
        }
        List<Transaction> checkRegisterDetails = new AccountingTransactionDAO().findByPropertyWithMultipleValues("transactionId", transactionIds);
        StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation")).append("/Documents/AccountPayable/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName.append("CheckRegister.xls");
        new ExportCheckRegisterToExcel().exportToExcel(fileName.toString(), checkRegisterForm, checkRegisterDetails);
        return fileName.toString();
    }

    public String createReport(CheckRegisterForm checkRegisterForm, String contextPath) throws Exception {
        Collection<Integer> transactionIds = new ArrayList<Integer>();
        String[] ids = StringUtils.split(StringUtils.removeStart(StringUtils.removeStart(checkRegisterForm.getTransactionIds(), ","), ","), ",");
        for (String id : ids) {
            transactionIds.add(Integer.parseInt(id));
        }
        List<Transaction> checkRegisterDetails = new AccountingTransactionDAO().findByPropertyWithMultipleValues("transactionId", transactionIds);
        StringBuilder fileName = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
        fileName.append("/Documents/AccountPayable/");
        fileName.append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
        File file = new File(fileName.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName.append("CheckRegister.pdf");
        new CheckRegisterReportCreator().createReport(fileName.toString(), checkRegisterForm, checkRegisterDetails, contextPath);
        return fileName.toString();
    }
}
