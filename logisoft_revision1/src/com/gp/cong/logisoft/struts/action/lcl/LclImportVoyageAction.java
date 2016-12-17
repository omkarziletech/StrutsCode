/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.beans.ImpVoyageSearchBean;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.DispositionDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.ImportVoyageSearchDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.lcl.LclUnitsScheduleForm;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Mei
 */
public class LclImportVoyageAction extends LogiwareDispatchAction {

    private LclSsHeaderDAO lclSsHeaderDAO = new LclSsHeaderDAO();
    private ImportVoyageSearchDAO importVoyageSearchDAO = new ImportVoyageSearchDAO();

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception, SQLException {
        LclUnitsScheduleForm lclUnitsScheduleForm = (LclUnitsScheduleForm) form;
        User user = getCurrentUser(request);
        lclUnitsScheduleForm.setLoginId(user.getId().toString());
        List<ImpVoyageSearchBean> voyageList = importVoyageSearchDAO.getImportVoyageSearch(lclUnitsScheduleForm);
        request.setAttribute("voyageList", voyageList);
        lclUnitsScheduleForm.setLoginName(user.getLoginName());
        request.setAttribute("lclUnitsScheduleForm", lclUnitsScheduleForm);
        HttpSession session = request.getSession();
        session.setAttribute("companyMnemonicCode", null != LoadLogisoftProperties.getProperty("application.fclBl.print.companyName") ? LoadLogisoftProperties.getProperty("application.fclBl.print.companyName").toUpperCase() : "");
        session.setAttribute("applicationEmailCompanyName", null != LoadLogisoftProperties.getProperty("application.email.companyName") ? LoadLogisoftProperties.getProperty("application.email.companyName").toUpperCase() : "");
        return mapping.findForward("voyageSchedule");
    }

    public ActionForward searchVoyageResult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitsScheduleForm lclUnitsScheduleForm = (LclUnitsScheduleForm) form;
        List<ImpVoyageSearchBean> voyageList = importVoyageSearchDAO.getImportVoyageSearch(lclUnitsScheduleForm);
        request.setAttribute("voyageList", voyageList);
        lclUnitsScheduleForm.setColumnName("");
        request.setAttribute("lclUnitsScheduleForm", lclUnitsScheduleForm);
        return mapping.findForward("voyageSchedule");
    }

    public ActionForward addVoyage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception, SQLException {
        LclUnitsScheduleForm lclUnitsScheduleForm = (LclUnitsScheduleForm) form;
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getPortOfOriginId())
                && CommonUtils.isNotEmpty(lclUnitsScheduleForm.getFinalDestinationId())) {
            if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getOrigin())) {
                request.setAttribute("originValue", lclUnitsScheduleForm.getOrigin());
                request.setAttribute("originalOriginName", lclUnitsScheduleForm.getOrigin());
            }
            if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getDestination())) {
                request.setAttribute("originalDestinationName", lclUnitsScheduleForm.getDestination());
                request.setAttribute("destinationValue", lclUnitsScheduleForm.getDestination());
            }
            if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getPortOfOriginId())) {
                request.setAttribute("originalOriginId", lclUnitsScheduleForm.getPortOfOriginId());
                request.setAttribute("originId", lclUnitsScheduleForm.getPortOfOriginId());
            }
            if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getFinalDestinationId())) {
                request.setAttribute("destinationId", lclUnitsScheduleForm.getFinalDestinationId());
                request.setAttribute("originalDestinationId", lclUnitsScheduleForm.getFinalDestinationId());
            }
            if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getLimit())) {
                request.setAttribute("limit", lclUnitsScheduleForm.getLimit());
            }
            request.setAttribute("openPopup", "openPopupOnly");
        }
        return mapping.findForward("addImportVoyage");
    }

    public ActionForward deleteVoyage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitsScheduleForm lclUnitsScheduleForm = (LclUnitsScheduleForm) form;
        if (lclUnitsScheduleForm.getVoyageId() != null && !lclUnitsScheduleForm.getVoyageId().trim().equals("")) {
            LclSsHeader lclssheader = lclSsHeaderDAO.findById(Long.parseLong(lclUnitsScheduleForm.getVoyageId()));
            lclssheader.setStatus("V"); //***** delete operation on Voyage will put Voyag into void Status *
            lclSsHeaderDAO.saveOrUpdate(lclssheader);
            List<ImpVoyageSearchBean> voyageList = importVoyageSearchDAO.getImportVoyageSearch(lclUnitsScheduleForm);
            request.setAttribute("voyageList", voyageList);
        }
        request.setAttribute("lclUnitsScheduleForm", lclUnitsScheduleForm);
        return mapping.findForward("voyageSchedule");
    }

    public ActionForward goBack(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitsScheduleForm lclUnitsScheduleForm = (LclUnitsScheduleForm) form;
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getOrigin())) {
            lclUnitsScheduleForm.setPolCode(lclUnitsScheduleForm.getOrigin().substring(lclUnitsScheduleForm.getOrigin().indexOf("(") + 1, lclUnitsScheduleForm.getOrigin().indexOf(")")));
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getLoginId())) {
            lclUnitsScheduleForm.setLoginName(new UserDAO().getLoginName(Integer.parseInt(lclUnitsScheduleForm.getLoginId())));
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getDispositionId())) {
            lclUnitsScheduleForm.setDispositionCode(new DispositionDAO().findById(Integer.parseInt(lclUnitsScheduleForm.getDispositionId())).getEliteCode());
        }
        List<ImpVoyageSearchBean> voyageList = importVoyageSearchDAO.getImportVoyageSearch(lclUnitsScheduleForm);
        request.setAttribute("voyageList", voyageList);
        request.setAttribute("goBackVoyNo", request.getParameter("voyageNumber"));
        request.setAttribute("lclUnitsScheduleForm", lclUnitsScheduleForm);
        return mapping.findForward("voyageSchedule");
    }

    public ActionForward checkWarehouse(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        String unLocCode = request.getParameter("unLocCode");
        Integer warehouse = warehouseDAO.warehouseNo(unLocCode);
        if (warehouse == 0) {
            out.print("false");
        } else {
            out.print("true");
        }
        return mapping.findForward("checkWarehouse");
    }
}
