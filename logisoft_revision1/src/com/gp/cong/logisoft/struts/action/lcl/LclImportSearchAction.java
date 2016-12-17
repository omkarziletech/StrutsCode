/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.lcl.model.ImportSearchBean;
import com.gp.cong.logisoft.bc.scheduler.ProcessInfoBC;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclSearchTemplate;
import com.gp.cong.logisoft.domain.lcl.LclUserDefaults;
import com.gp.cong.logisoft.hibernate.dao.lcl.ImportSearchDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSearchTemplateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUserDefaultsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.SearchDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.lcl.LclSearchForm;
import com.gp.cvst.logisoft.struts.form.lcl.SessionLclSearchForm;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class LclImportSearchAction extends DispatchAction {

    private static final String LCL_QUOTE = "lclQuote";
    private static final String LCL_BOOKING = "lclBooking";

//    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchForm lclSearchForm = new LclSearchForm();
        request.setAttribute("lclSearchForm", lclSearchForm);
        return mapping.findForward("gotoSearch");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchForm lclSearchForm = (LclSearchForm) form;
        HttpSession session = request.getSession();
        LclSearchTemplate template = null;
        ImportSearchDAO importSearchDAO = new ImportSearchDAO();
        LclSearchTemplateDAO lclSearchTemplateDAO = new LclSearchTemplateDAO();
        LclSession lclSession = null != request.getSession().getAttribute("lclSession") ? (LclSession) request.getSession().getAttribute("lclSession") : new LclSession();
        List<ImportSearchBean> searchResultList = importSearchDAO.search(lclSearchForm);
        if (CommonUtils.isNotEmpty(lclSearchForm.getSortBy())) {
            template = lclSearchTemplateDAO.findById(lclSearchForm.getSortBy());
        } else {
            template = lclSearchTemplateDAO.getTemplateByName("LCLSEARCH");
        }
        if (!searchResultList.isEmpty() && searchResultList.size() == 1) { // if single file
            request.setAttribute("isSingleFile", "true");
        }
        request.setAttribute("template", template);
        lclSession.setConsolidated("false");
        lclSession.setSearchResult("true");
        SessionLclSearchForm oldLclSearchForm = new SessionLclSearchForm();
        PropertyUtils.copyProperties(oldLclSearchForm, lclSearchForm);
        session.setAttribute("oldLclSearchForm", oldLclSearchForm);
        request.setAttribute("searchResultList", searchResultList);
        request.getSession().setAttribute("lclSession", lclSession);
        return mapping.findForward("searchResult");
    }

    public ActionForward doSortAscDesc(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchForm lclSearchForm = (LclSearchForm) form;
        HttpSession session = request.getSession();
        SessionLclSearchForm oldSearchForm = (SessionLclSearchForm) session.getAttribute("oldLclSearchForm");
        PropertyUtils.copyProperties(lclSearchForm, oldSearchForm);
        lclSearchForm.setSortByValue(request.getParameter("sortByValue"));
        lclSearchForm.setSearchType(request.getParameter("searchType"));
        LclSearchTemplate template = null;
        ImportSearchDAO importSearchDAO = new ImportSearchDAO();
        LclSearchTemplateDAO lclSearchTemplateDAO = new LclSearchTemplateDAO();
        LclSession lclSession = null != session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        List<ImportSearchBean> searchResultList = importSearchDAO.search(lclSearchForm);
        if (CommonUtils.isNotEmpty(lclSearchForm.getSortBy())) {
            template = lclSearchTemplateDAO.findById(lclSearchForm.getSortBy());
        } else {
            template = lclSearchTemplateDAO.getTemplateByName("LCLSEARCH");
        }
        if (!searchResultList.isEmpty() && searchResultList.size() == 1) { // if single file
            request.setAttribute("isSingleFile", "true");
        }
        request.setAttribute("template", template);
        lclSession.setConsolidated("false");
        lclSession.setSearchResult("true");
        session.setAttribute("oldLclSearchForm", oldSearchForm);
        request.setAttribute("searchResultList", searchResultList);
        request.getSession().setAttribute("lclSession", lclSession);
        return mapping.findForward("searchResult");
    }

//DR GoBack
    public ActionForward backToSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchForm lclSearchForm = (LclSearchForm) form;
        ImportSearchDAO importSearchDAO = new ImportSearchDAO();
        LclSearchTemplateDAO lclSearchTemplateDAO = new LclSearchTemplateDAO();
        String moduleName = lclSearchForm.getModuleName();
        HttpSession session = request.getSession();
        SessionLclSearchForm oldSearchForm = (SessionLclSearchForm) session.getAttribute("oldLclSearchForm");
        if(null != oldSearchForm) {
        PropertyUtils.copyProperties(lclSearchForm, oldSearchForm);
        }
        LclSearchTemplate template = null;
        LclSession lclSession = null != request.getSession().getAttribute("lclSession") ? (LclSession) request.getSession().getAttribute("lclSession") : new LclSession();
        if (CommonUtils.isNotEmpty(moduleName)) {
            lclSearchForm.setModuleName(moduleName);
        }
        List<ImportSearchBean> searchResultList = importSearchDAO.search(lclSearchForm);
        if (CommonUtils.isNotEmpty(lclSearchForm.getSortBy())) {
            template = lclSearchTemplateDAO.findById(lclSearchForm.getSortBy());
        } else {
            template = lclSearchTemplateDAO.getTemplateByName("LCLSEARCH");
        }
        request.setAttribute("template", template);
        request.setAttribute("lclSearchForm", lclSearchForm);
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        request.getSession().setAttribute("lclSession", lclSession);
        request.setAttribute("searchResultList", searchResultList);
        User user = (User) request.getSession().getAttribute("loginuser");
        new ProcessInfoBC().releaseLoack("LCL FILE", request.getParameter("fileNumber"), user.getUserId());
        return mapping.findForward("searchResult");
    }

    public ActionForward newQuote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(LCL_QUOTE);
    }

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("filter");
    }

    public ActionForward newBkg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(LCL_BOOKING);
    }

    public ActionForward goBackToSearchList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchForm lclSearchForm = (LclSearchForm) form;
        SearchDAO searchDAO = new SearchDAO();
        LclSearchTemplateDAO lclSearchTemplateDAO = new LclSearchTemplateDAO();
        lclSearchForm = (LclSearchForm) request.getSession().getAttribute("lclSearchForm");
        LclSession lclSession = null != request.getSession().getAttribute("lclSession") ? (LclSession) request.getSession().getAttribute("lclSession") : new LclSession();
     //   lclSearchForm.setFiles(searchDAO.search(lclSearchForm, lclSearchForm.getModuleName()));
        LclSearchTemplate template = null;
        if (CommonUtils.isNotEmpty(lclSearchForm.getSortBy())) {
            template = lclSearchTemplateDAO.findById(lclSearchForm.getSortBy());
        } else {
            template = lclSearchTemplateDAO.getTemplateByName("LCLSEARCH");
        }
        if (CommonUtils.isNotEmpty(lclSearchForm.getSearchFileNo())) {
            lclSession.setSearchFileNo("Y");
        }
        request.setAttribute("template", template);
        request.setAttribute("lclSearchForm", lclSearchForm);
        lclSession.setConsolidated("false");
        lclSession.setSearchResult("true");
        SessionLclSearchForm sessionLclSearchForm = new SessionLclSearchForm();
        PropertyUtils.copyProperties(sessionLclSearchForm, lclSearchForm);
        lclSession.setSearchForm(sessionLclSearchForm);
        request.getSession().setAttribute("lclSession", lclSession);
        return mapping.findForward("searchResult");
    }

    public ActionForward searchHome(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("searchHome");
    }

    public ActionForward newDr(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("newDr");
    }

    public ActionForward displayLclSearchScreen(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchForm lclSearchForm = (LclSearchForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        LclUserDefaultsDAO lclUserDefaultsDAO = new LclUserDefaultsDAO();
        LclUserDefaults lclUserDefaults = lclUserDefaultsDAO.getLclUserDefaultById(loginUser.getUserId());
        if (lclUserDefaults == null) {
            return mapping.findForward("gotoImportMainScreen");
        }
        UnLocation unlocationCurrentLocation = lclUserDefaults.getCurrentLocation();
        UnLocation unlocationFinalDestination = lclUserDefaults.getFinalDestination();
        UnLocation unlocationPortOfDestination = lclUserDefaults.getPortOfDestination();
        UnLocation unlocationPortOfLoading = lclUserDefaults.getPortOfLoading();
        UnLocation unlocationPortOfOrigin = lclUserDefaults.getPortOfOrigin();
        StringBuilder currentLocation = new StringBuilder();
        StringBuilder finalDestination = new StringBuilder();
        StringBuilder portOfDestination = new StringBuilder();
        StringBuilder portOfLoading = new StringBuilder();
        StringBuilder portOfOrigin = new StringBuilder();
        if (null != unlocationCurrentLocation && unlocationCurrentLocation.getUnLocationName() != null) {
            currentLocation.append(unlocationCurrentLocation.getUnLocationName()).append("/").append(unlocationCurrentLocation.getStateId().getCode()).append("(").append(unlocationCurrentLocation.getUnLocationCode()).append(")");
        }
        if (null != unlocationFinalDestination && unlocationFinalDestination.getUnLocationName() != null) {
            finalDestination.append(unlocationFinalDestination.getUnLocationName()).append("/").append(unlocationFinalDestination.getCountryId().getCodedesc()).append("(").append(unlocationFinalDestination.getUnLocationCode()).append(")");
        }
        if (null != unlocationPortOfDestination && unlocationPortOfDestination.getUnLocationName() != null) {
            portOfDestination.append(unlocationPortOfDestination.getUnLocationName()).append("/").append(unlocationPortOfDestination.getCountryId().getCodedesc()).append("(").append(unlocationPortOfDestination.getUnLocationCode()).append(")");
        }
        if (null != unlocationPortOfLoading && unlocationPortOfLoading.getUnLocationName() != null) {
            if (unlocationPortOfLoading.getStateId() != null) {
                portOfLoading.append(unlocationPortOfLoading.getUnLocationName()).append("/").append(unlocationPortOfLoading.getStateId().getCode()).append("(").append(unlocationPortOfLoading.getUnLocationCode()).append(")");
            } else {
                portOfLoading.append(unlocationPortOfLoading.getUnLocationName()).append("/").append(unlocationPortOfLoading.getCountryId().getCodedesc()).append("(").append(unlocationPortOfLoading.getUnLocationCode()).append(")");
            }
        }
        if (null != unlocationPortOfOrigin && unlocationPortOfOrigin.getUnLocationName() != null) {
            portOfOrigin.append(unlocationPortOfOrigin.getUnLocationName()).append("/").append(unlocationPortOfOrigin.getStateId().getCode()).append("(").append(unlocationPortOfOrigin.getUnLocationCode()).append(")");
        }
        String currentLocation1 = currentLocation.toString();
        String finalDestination1 = finalDestination.toString();
        String portOfDestination1 = portOfDestination.toString();
        String portOfLoading1 = portOfLoading.toString();
        String portOfOrigin1 = portOfOrigin.toString();

        if (null != unlocationCurrentLocation && unlocationCurrentLocation.getId() != null) {
            lclSearchForm.setCurrentLocation(currentLocation1);
            lclSearchForm.setCurrentLocName(unlocationCurrentLocation.getUnLocationName());
            lclSearchForm.setCurrentLocCode((unlocationCurrentLocation.getId()).toString());
        }
        if (null != unlocationFinalDestination && unlocationFinalDestination.getId() != null) {
            lclSearchForm.setDestination(finalDestination1);
            lclSearchForm.setDestinationName(unlocationFinalDestination.getUnLocationName());
            lclSearchForm.setDestCountryCode((unlocationFinalDestination.getId()).toString());
        }
        if (null != unlocationPortOfDestination && unlocationPortOfDestination.getId() != null) {
            lclSearchForm.setPod(portOfDestination1);
            lclSearchForm.setPodName(unlocationPortOfDestination.getUnLocationName());
            lclSearchForm.setPodCountryCode((unlocationPortOfDestination.getId()).toString());
        }
        if (null != unlocationPortOfLoading && unlocationPortOfLoading.getId() != null) {
            lclSearchForm.setPol(portOfLoading1);
            lclSearchForm.setPolName(unlocationPortOfLoading.getUnLocationName());
            lclSearchForm.setPolCountryCode((unlocationPortOfLoading.getId()).toString());
        }
        if (null != unlocationPortOfOrigin && unlocationPortOfOrigin.getId() != null) {
            lclSearchForm.setOrigin(portOfOrigin1);
            lclSearchForm.setPortName(unlocationPortOfOrigin.getUnLocationName());
            lclSearchForm.setCountryCode((unlocationPortOfOrigin.getId()).toString());
        }
        request.setAttribute("currentLocation", currentLocation1);
        request.setAttribute("finalDestination", finalDestination1);
        request.setAttribute("portOfDestination", portOfDestination1);
        request.setAttribute("portOfLoading", portOfLoading1);
        request.setAttribute("portOfOrigin", portOfOrigin1);
        return mapping.findForward("gotoImportMainScreen");
    }

    public ActionForward checkLocking(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchForm lclSearchForm = (LclSearchForm) form;
        SearchDAO searchDAO = new SearchDAO();
        searchDAO.checkLockingStatus(lclSearchForm.getFileNumber(), lclSearchForm.getUserId(), response);
        return null;
    }

//Back TO Main Screen
    public ActionForward goBackToMainScreen(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        LclSearchForm lclSearchForm = (LclSearchForm) form;
        User user = (User) session.getAttribute("loginuser");
        if (user.isSearchScreenReset()) {
            session.removeAttribute("oldLclSearchForm");
        }
        LclSession lclSession = null != request.getSession().getAttribute("lclSession") ? (LclSession) request.getSession().getAttribute("lclSession") : new LclSession();
        lclSession.setSearchResult("false");
        request.getSession().setAttribute("lclSession", lclSession);
        SessionLclSearchForm oldLclSearchForm = (SessionLclSearchForm) session.getAttribute("oldLclSearchForm");
        if (null != oldLclSearchForm) {
            PropertyUtils.copyProperties(lclSearchForm, oldLclSearchForm);
            request.setAttribute("lclSearchForm", lclSearchForm);
        }
        return mapping.findForward("gotoImportMainScreen");
    }

    public ActionForward getCompanyName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        String name = null != LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic") ? LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic").toUpperCase() : "";
        out.print(name);
        out.flush();
        out.close();
        return null;
    }
}
