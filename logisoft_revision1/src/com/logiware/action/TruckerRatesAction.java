package com.logiware.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.form.TruckerRatesForm;
import com.logiware.hibernate.dao.TruckerRatesDAO;
import com.logiware.hibernate.domain.TruckerRates;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author Lakshmi Narayanan
 */
public class TruckerRatesAction extends DispatchAction {

    private static final String SUCCESS = "success";

    public ActionForward upload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String defaultFilePath = LoadLogisoftProperties.getProperty("reportLocation") + "//";
        String defaultFileName = "TruckersRatesTemplate.csv";
        try {
            TruckerRatesDAO truckerRatesDAO = new TruckerRatesDAO();
            TruckerRatesForm truckerRatesForm = (TruckerRatesForm) form;
            FormFile uploadFile = truckerRatesForm.getUploadFile();
            inputStream = uploadFile.getInputStream();
            outputStream = new FileOutputStream(new File(defaultFilePath, defaultFileName));
            boolean isCopied = IOUtils.copy(inputStream, outputStream) != -1;
            if (isCopied) {
                truckerRatesDAO.truncate();
                int recordCount = truckerRatesDAO.loadCsv(defaultFilePath + defaultFileName);
                truckerRatesDAO.validateTruckerRates();
                request.setAttribute("uploadConfirmation", "" + recordCount + " Records in '" + uploadFile.getFileName() + "' has been Uploaded Successfully !!!");
            }
            File deleteFile = new File(defaultFilePath + defaultFileName);
            if (deleteFile.exists()) {
                deleteFile.deleteOnExit();
            }
            return mapping.findForward(SUCCESS);
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TruckerRatesForm truckerRatesForm = (TruckerRatesForm) form;
        request.getSession().setAttribute("oldTruckerRatesForm", truckerRatesForm);
        new TruckerRatesDAO().search(truckerRatesForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward gotoPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TruckerRatesForm truckerRatesForm = (TruckerRatesForm) form;
        TruckerRatesForm oldTruckerRatesForm = (TruckerRatesForm) request.getSession().getAttribute("oldTruckerRatesForm");
        oldTruckerRatesForm.setSortBy(truckerRatesForm.getSortBy());
        oldTruckerRatesForm.setOrderBy(truckerRatesForm.getOrderBy());
        oldTruckerRatesForm.setSelectedPage(truckerRatesForm.getSelectedPage());
        oldTruckerRatesForm.setLimit(truckerRatesForm.getLimit());
        new TruckerRatesDAO().search(truckerRatesForm);
        request.setAttribute("truckerRatesForm", oldTruckerRatesForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward doSort(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TruckerRatesForm truckerRatesForm = (TruckerRatesForm) form;
        TruckerRatesForm oldTruckerRatesForm = (TruckerRatesForm) request.getSession().getAttribute("oldTruckerRatesForm");
        oldTruckerRatesForm.setSortBy(truckerRatesForm.getSortBy());
        oldTruckerRatesForm.setOrderBy(truckerRatesForm.getOrderBy());
        oldTruckerRatesForm.setSelectedPage(truckerRatesForm.getSelectedPage());
        oldTruckerRatesForm.setLimit(truckerRatesForm.getLimit());
        new TruckerRatesDAO().search(truckerRatesForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TruckerRatesForm truckerRatesForm = (TruckerRatesForm) form;
        TruckerRates truckerRates = truckerRatesForm.getTruckerRates();
        Map<Serializable, Serializable> fields = new HashMap<Serializable, Serializable>();
        if (CommonUtils.isNotEmpty(truckerRates.getTruckerName()) && CommonUtils.isNotEmpty(truckerRates.getTruckerNumber())) {
            fields.put("trucker_name", truckerRates.getTruckerName());
            fields.put("trucker_number", truckerRates.getTruckerNumber());
            fields.put("trucker", truckerRates.getTruckerNumber());
        }
        if (CommonUtils.isNotEmpty(truckerRates.getFromZip())) {
            fields.put("from_zip", truckerRates.getFromZip());
            fields.put("from_city", truckerRates.getFromCity());
            fields.put("from_state", truckerRates.getFromState());
            fields.put("from_zip_code", truckerRates.getFromZip());
        }
        if (CommonUtils.isNotEmpty(truckerRates.getToPort())) {
            fields.put("to_port", truckerRates.getToPort());
            fields.put("to_port_code", truckerRates.getToPortCode());
        }
        new TruckerRatesDAO().update(truckerRates.getId(), fields);
        PrintWriter out = response.getWriter();
        out.print("Trucker Rate is updated successfully");
        out.flush();
        out.close();
        return null;
    }

    public ActionForward goBack(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("truckerRatesForm", new TruckerRatesForm());
        request.getSession().removeAttribute("oldTruckerRatesForm");
        return mapping.findForward(SUCCESS);
    }
}
