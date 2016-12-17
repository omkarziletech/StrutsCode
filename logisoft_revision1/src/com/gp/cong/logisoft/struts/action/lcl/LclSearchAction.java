/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.lcl.model.ExportSearchBean;
import com.gp.cong.logisoft.bc.scheduler.ProcessInfoBC;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.TemplateOrder;
import com.gp.cong.logisoft.domain.lcl.LclApplyDefaultDetails;
import com.gp.cong.logisoft.domain.lcl.LclConsolidate;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclSearchTemplate;
import com.gp.cong.logisoft.domain.lcl.LclUserDefaults;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclApplyDefaultDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSearchTemplateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUserDefaultsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.RoleDutyDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.SearchDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.lcl.LclSearchForm;
import com.gp.cvst.logisoft.struts.form.lcl.SessionLclSearchForm;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class LclSearchAction extends DispatchAction {

    private static final String LCL_QUOTE = "lclQuote";
    private static final String LCL_BOOKING = "lclBooking";
    private static final String SEARCH_SCREEN = "searchScreen";

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchForm lclSearchForm = new LclSearchForm();
        request.setAttribute("lclSearchForm", lclSearchForm);
        return mapping.findForward("gotoSearch");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchForm lclSearchForm = (LclSearchForm) form;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginuser");
        LclSearchTemplate template = null;
        SearchDAO searchDAO = new SearchDAO();
        LclSearchTemplateDAO lclSearchTemplateDAO = new LclSearchTemplateDAO();
        LclSession lclSession = null != request.getSession().getAttribute("lclSession") ? (LclSession) request.getSession().getAttribute("lclSession") : new LclSession();
        List<ExportSearchBean> fileSearchList = searchDAO.search(lclSearchForm);
        if (CommonUtils.isNotEmpty(lclSearchForm.getSortBy())) {
            template = lclSearchTemplateDAO.findById(lclSearchForm.getSortBy());
            List<TemplateOrder> orderedTemplateList = new LclSearchTemplateDAO()
                    .getTemplateOrderedList(template.getId());
            request.setAttribute("orderedTemplateList", orderedTemplateList);
        } else {
            template = lclSearchTemplateDAO.getTemplateByName("LCLSEARCH");
            request.setAttribute("orderedTemplateList", null);
        }
        if (!fileSearchList.isEmpty() && fileSearchList.size() == 1) { // if single file
            request.setAttribute("isSingleFile", "true");
        }
        request.setAttribute("template", template);
        if (CommonUtils.isNotEmpty(lclSearchForm.getConoslidateFiles())) {
            lclSession.setConsolidated("false");
            request.setAttribute("consolidate", true);
            lclSession.setConsolidated("false");
            request.setAttribute("consIconSearchScreen", false);
        } else {
            lclSession.setConsolidated("false");
        }
        lclSession.setSearchResult("true");
        SessionLclSearchForm oldLclSearchForm = new SessionLclSearchForm();
        PropertyUtils.copyProperties(oldLclSearchForm, lclSearchForm);
        session.setAttribute("oldLclSearchForm", oldLclSearchForm);
        // request.setAttribute("lclSearchForm", lclSearchForm);
        if (null != user.getRole()) {
            request.setAttribute("roleQuickBkg", new RoleDutyDAO().getRoleDetails("warehouse_quickBkg", user.getRole().getRoleId()));
        }
        request.setAttribute("fileSearchList", fileSearchList);
        request.setAttribute("commodity", lclSearchForm.getCommodity());
        request.getSession().setAttribute("lclSession", lclSession);
        return mapping.findForward("searchResult");
    }

    public ActionForward consolidatedFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchForm lclSearchForm = (LclSearchForm) form;
        SearchDAO searchDAO = new SearchDAO();
        LclConsolidateDAO lclConsolidateDAO = new LclConsolidateDAO();
        String fileIdA = request.getParameter("fileNumberA");
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        if (CommonUtils.isNotEmpty(fileIdA)) {
            List<LclConsolidate> consolidateListB = lclConsolidateDAO.findByProperty("lclFileNumberB.id", Long.parseLong(fileIdA));
            List<LclConsolidate> consolidateListA = lclConsolidateDAO.findByProperty("lclFileNumberA.id", Long.parseLong(fileIdA));
            StringBuilder condition = new StringBuilder();
            condition.append(fileIdA).append(",");
            if (consolidateListA != null && consolidateListA.size() > 0) {
                for (LclConsolidate lc : consolidateListA) {
                    condition.append(lc.getLclFileNumberB().getId()).append(",");
                }
            }
            if (consolidateListB != null && consolidateListB.size() > 0) {
                for (LclConsolidate lc : consolidateListB) {
                    condition.append(lc.getLclFileNumberA().getId()).append(",");
                }
            }
            if (condition != null && !condition.toString().trim().equals("")) {
                condition.deleteCharAt(condition.length() - 1);
            }
            //  lclSearchForm.setFiles(searchDAO.getConsolidateResult(condition.toString(), lclSearchForm));
            request.setAttribute("consIconSearchScreen", false);
            LclSession lclSession = null != request.getSession().getAttribute("lclSession") ? (LclSession) request.getSession().getAttribute("lclSession") : new LclSession();
            lclSession.setConsolidated("true");
            lclSession.setConsolidatedFileId(fileIdA);
            request.getSession().setAttribute("lclSession", lclSession);
//            List<FileSearchBean> fileSearchList = searchDAO.getConsolidateResult(condition.toString(), lclSearchForm);
//            request.setAttribute("fileSearchList", fileSearchList);
        }
        request.setAttribute("consolidate", true);
        return mapping.findForward("searchResult");

    }
//DR GoBack

    public ActionForward backToSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchForm lclSearchForm = (LclSearchForm) form;
        SearchDAO searchDAO = new SearchDAO();
        LclSearchTemplateDAO lclSearchTemplateDAO = new LclSearchTemplateDAO();
        String moduleName = lclSearchForm.getModuleName();
        HttpSession session = request.getSession();
        SessionLclSearchForm oldSearchForm = (SessionLclSearchForm) session.getAttribute("oldLclSearchForm");
        PropertyUtils.copyProperties(lclSearchForm, oldSearchForm);
        LclSearchTemplate template = null;
        LclSession lclSession = null != request.getSession().getAttribute("lclSession") ? (LclSession) request.getSession().getAttribute("lclSession") : new LclSession();
        if (CommonUtils.isNotEmpty(moduleName)) {
            lclSearchForm.setModuleName(moduleName);
        }
        lclSearchForm.setIgnoreSearchStatus("ignoreAlert");
        List<ExportSearchBean> searchResult = searchDAO.search(lclSearchForm);
        if (CommonUtils.isNotEmpty(lclSearchForm.getSortBy())) {
            template = lclSearchTemplateDAO.findById(lclSearchForm.getSortBy());
            List<TemplateOrder> orderedTemplateList = new LclSearchTemplateDAO()
                    .getTemplateOrderedList(template.getId());
            request.setAttribute("orderedTemplateList", orderedTemplateList);
        } else {
            template = lclSearchTemplateDAO.getTemplateByName("LCLSEARCH");
            request.setAttribute("orderedTemplateList", null);
        }
        request.setAttribute("template", template);
        request.setAttribute("lclSearchForm", lclSearchForm);
        request.setAttribute("fileNumber", request.getParameter("fileNumber"));
        request.setAttribute("fileNumberId", request.getParameter("fileNumberId"));
        request.getSession().setAttribute("lclSession", lclSession);
        request.setAttribute("fileSearchList", searchResult);
        User user = (User) request.getSession().getAttribute("loginuser");
        if (null != user.getRole()) {
            request.setAttribute("roleQuickBkg", new RoleDutyDAO().getRoleDetails("warehouse_quickBkg", user.getRole().getRoleId()));
        }
        new ProcessInfoBC().releaseLoack("LCL FILE", request.getParameter("fileNumber"), user.getUserId());
        return mapping.findForward("searchResult");
    }

    public ActionForward newQuote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(LCL_QUOTE);
    }

//    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        return mapping.findForward("filter");
//    }
    public ActionForward newBkg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(LCL_BOOKING);
    }

    public ActionForward openQuickQuote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("quickQuote");
    }

    public ActionForward openVoyage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchForm lclSearchForm = (LclSearchForm) form;
        SearchDAO searchDAO = new SearchDAO();
        List pol = Arrays.asList(lclSearchForm.getVoyageorigin().split(","));
        String pod = lclSearchForm.getVoyagedestination().substring(lclSearchForm.getVoyagedestination().indexOf("(") + 1,
                lclSearchForm.getVoyagedestination().indexOf(")"));
        request.setAttribute("voyageUnitList", searchDAO.getBookedVoyageToLoad(pol, pod));
        return mapping.findForward("openVoyage");
    }

    public ActionForward goBackToSearchList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchForm lclSearchForm = (LclSearchForm) form;
        SearchDAO searchDAO = new SearchDAO();
        LclSearchTemplateDAO lclSearchTemplateDAO = new LclSearchTemplateDAO();
        lclSearchForm = (LclSearchForm) request.getSession().getAttribute("lclSearchForm");
        LclSession lclSession = null != request.getSession().getAttribute("lclSession") ? (LclSession) request.getSession().getAttribute("lclSession") : new LclSession();
        // lclSearchForm.setFiles(searchDAO.search(lclSearchForm, lclSearchForm.getModuleName()));
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
        return mapping.findForward("result");
    }

    public ActionForward searchHome(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("searchHome");
    }

    public ActionForward newDr(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("newDr");
    }

    public ActionForward Release(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSession lclSession = (LclSession) request.getSession().getAttribute("lclSession");
        LclSearchForm lclSearchForm = new LclSearchForm();
        if (lclSession != null && lclSession.getSearchForm() != null) {
            SessionLclSearchForm sessionLclSearchForm = (SessionLclSearchForm) lclSession.getSearchForm();
            PropertyUtils.copyProperties(lclSearchForm, sessionLclSearchForm);
        }
        String fileId = request.getParameter("fileId");
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        LclFileNumber lclFileNumber = lclFileNumberDAO.getByProperty("id", new Long(fileId));
        lclFileNumber.setStatus("R");
        lclFileNumberDAO.saveOrUpdate(lclFileNumber);
        LclSearchTemplate template = null;
        if (CommonUtils.isNotEmpty(lclSearchForm.getSortBy())) {
            template = new LclSearchTemplateDAO().findById(lclSearchForm.getSortBy());
        }
        request.setAttribute("template", template);
        request.setAttribute("lclSearchForm", lclSearchForm);
        return mapping.findForward("result");
    }

    public ActionForward applyUserDefaultValues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchForm lclSearchForm = (LclSearchForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        LclUserDefaultsDAO lclUserDefaultsDAO = new LclUserDefaultsDAO();
        LclUserDefaults lclUserDefaults = lclUserDefaultsDAO.getLclUserDefaultById(loginUser.getUserId());
        if (lclUserDefaults == null) {
            return mapping.findForward(SEARCH_SCREEN);
        }
        UnLocation unlocationCurrentLocation = lclUserDefaults.getCurrentLocation();
        UnLocation unlocationFinalDestination = lclUserDefaults.getFinalDestination();
        UnLocation unlocationPortOfDestination = lclUserDefaults.getPortOfDestination();
        UnLocation unlocationPortOfLoading = lclUserDefaults.getPortOfLoading();
        UnLocation unlocationPortOfOrigin = lclUserDefaults.getPortOfOrigin();
        if (null != unlocationCurrentLocation && unlocationCurrentLocation.getId() != null) {
            StringBuilder currentLocation = new StringBuilder();
            currentLocation.append(unlocationCurrentLocation.getUnLocationName());
            currentLocation.append("/").append(unlocationCurrentLocation.getStateId().getCode()).append("(");
            currentLocation.append(unlocationCurrentLocation.getUnLocationCode()).append(")");
            lclSearchForm.setCurrentLocation(currentLocation.toString());
            lclSearchForm.setCurrentLocName(unlocationCurrentLocation.getUnLocationName());
            lclSearchForm.setCurrentLocCode((unlocationCurrentLocation.getId()).toString());
            request.setAttribute("currentLocation", currentLocation.toString());
        }
        if (null != unlocationFinalDestination && unlocationFinalDestination.getId() != null) {
            StringBuilder finalDestination = new StringBuilder();
            finalDestination.append(unlocationFinalDestination.getUnLocationName());
            finalDestination.append("/").append(unlocationFinalDestination.getCountryId().getCodedesc()).append("(");
            finalDestination.append(unlocationFinalDestination.getUnLocationCode()).append(")");
            lclSearchForm.setDestination(finalDestination.toString());
            lclSearchForm.setDestinationName(unlocationFinalDestination.getUnLocationName());
            lclSearchForm.setDestCountryCode((unlocationFinalDestination.getId()).toString());
            finalDestination.append(unlocationFinalDestination.getUnLocationName()).append("/");
            finalDestination.append(unlocationFinalDestination.getCountryId().getCodedesc()).append("(");
            finalDestination.append(unlocationFinalDestination.getUnLocationCode()).append(")");
            request.setAttribute("finalDestination", finalDestination.toString());
        }
        if (null != unlocationPortOfDestination && unlocationPortOfDestination.getId() != null) {
            StringBuilder portOfDestination = new StringBuilder();
            portOfDestination.append(unlocationPortOfDestination.getUnLocationName());
            portOfDestination.append("/").append(unlocationPortOfDestination.getCountryId().getCodedesc()).append("(");
            portOfDestination.append(unlocationPortOfDestination.getUnLocationCode()).append(")");
            lclSearchForm.setPod(portOfDestination.toString());
            lclSearchForm.setPodName(unlocationPortOfDestination.getUnLocationName());
            lclSearchForm.setPodCountryCode((unlocationPortOfDestination.getId()).toString());
            request.setAttribute("portOfDestination", portOfDestination.toString());
        }
        if (null != unlocationPortOfLoading && unlocationPortOfLoading.getId() != null) {
            StringBuilder portOfLoading = new StringBuilder();
            if (unlocationPortOfLoading.getStateId() != null) {
                portOfLoading.append(unlocationPortOfLoading.getUnLocationName());
                portOfLoading.append("/").append(unlocationPortOfLoading.getStateId().getCode()).append("(");
                portOfLoading.append(unlocationPortOfLoading.getUnLocationCode()).append(")");
            } else {
                portOfLoading.append(unlocationPortOfLoading.getUnLocationName());
                portOfLoading.append("/").append(unlocationPortOfLoading.getCountryId().getCodedesc()).append("(");
                portOfLoading.append(unlocationPortOfLoading.getUnLocationCode()).append(")");
            }
            lclSearchForm.setPol(portOfLoading.toString());
            lclSearchForm.setPolName(unlocationPortOfLoading.getUnLocationName());
            lclSearchForm.setPolCountryCode((unlocationPortOfLoading.getId()).toString());
            request.setAttribute("portOfLoading", portOfLoading.toString());
        }
        if (null != unlocationPortOfOrigin && unlocationPortOfOrigin.getId() != null) {
            StringBuilder portOfOrigin = new StringBuilder();
            portOfOrigin.append(unlocationPortOfOrigin.getUnLocationName());
            portOfOrigin.append("/").append(unlocationPortOfOrigin.getStateId().getCode()).append("(");
            portOfOrigin.append(unlocationPortOfOrigin.getUnLocationCode()).append(")");
            lclSearchForm.setOrigin(portOfOrigin.toString());
            lclSearchForm.setPortName(unlocationPortOfOrigin.getUnLocationName());
            lclSearchForm.setCountryCode((unlocationPortOfOrigin.getId()).toString());
            request.setAttribute("portOfOrigin", portOfOrigin.toString());
        }
        String templateId = new UserDAO().getUserColumnValueWithUserId(loginUser.getUserId().toString(), "user_default_template");
        lclSearchForm.setSortBy(CommonUtils.isNotEmpty(templateId) ? Integer.parseInt(templateId) : 0);
        request.setAttribute("applyDefaultNameList", new LclApplyDefaultDetailsDAO().getLclDefaultNameList(loginUser.getUserId()));
        request.setAttribute("user", loginUser);
        return mapping.findForward(SEARCH_SCREEN);
    }

    public ActionForward checkLocking(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchForm lclSearchForm = (LclSearchForm) form;
        SearchDAO searchDAO = new SearchDAO();
        searchDAO.checkLockingStatus(lclSearchForm.getFileNumber(), lclSearchForm.getUserId(), response);
        return null;
    }
//Back TO Main Screen

    public ActionForward goBackScreen(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
        if (null != user.getRole()) {
            request.setAttribute("roleQuickBkg", new RoleDutyDAO().getRoleDetails("warehouse_quickBkg", user.getRole().getRoleId()));
        }
        String templateId = new UserDAO().getUserColumnValueWithUserId(user.getUserId().toString(), "user_default_template");
        lclSearchForm.setSortBy(CommonUtils.isNotEmpty(templateId) ? Integer.parseInt(templateId) : 0);
        request.setAttribute("applyDefaultNameList", new LclApplyDefaultDetailsDAO().getLclDefaultNameList(user.getUserId()));
        request.setAttribute("user", user);
        return mapping.findForward(SEARCH_SCREEN);
    }

    public ActionForward getCompanyName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        String name = null != LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic") ? LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic").toUpperCase() : "";
        out.print(name);
        out.flush();
        out.close();
        return null;
    }

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchForm lclSearchForm = (LclSearchForm) form;
        request.setAttribute("lclSearchForm", lclSearchForm);
        User user = (User) request.getSession().getAttribute("loginuser");
        if (null != user.getRole()) {
            request.setAttribute("roleQuickBkg", new RoleDutyDAO().getRoleDetails("warehouse_quickBkg", user.getRole().getRoleId()));
        }
        String templateId = new UserDAO().getUserColumnValueWithUserId(user.getUserId().toString(), "user_default_template");
        lclSearchForm.setSortBy(CommonUtils.isNotEmpty(templateId) ? Integer.parseInt(templateId) : 0);
        request.setAttribute("applyDefaultNameList", new LclApplyDefaultDetailsDAO().getLclDefaultNameList(user.getUserId()));
        request.setAttribute("user", user);
        return mapping.findForward(SEARCH_SCREEN);
    }

    public ActionForward doSortAscDesc(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchForm lclSearchForm = (LclSearchForm) form;
        HttpSession session = request.getSession();
        SessionLclSearchForm oldSearchForm = (SessionLclSearchForm) session.getAttribute("oldLclSearchForm");
        PropertyUtils.copyProperties(lclSearchForm, oldSearchForm);
        lclSearchForm.setSortByValue(request.getParameter("sortByValue"));
        lclSearchForm.setSearchType(request.getParameter("searchType"));
        LclSearchTemplate template = null;
        SearchDAO searchDAO = new SearchDAO();
        LclSearchTemplateDAO lclSearchTemplateDAO = new LclSearchTemplateDAO();
        LclSession lclSession = null != session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        List<ExportSearchBean> fileSearchList = searchDAO.search(lclSearchForm);
        if (CommonUtils.isNotEmpty(lclSearchForm.getSortBy())) {
            template = lclSearchTemplateDAO.findById(lclSearchForm.getSortBy());
            List<TemplateOrder> orderedTemplateList = new LclSearchTemplateDAO()
                    .getTemplateOrderedList(template.getId());
            request.setAttribute("orderedTemplateList", orderedTemplateList);
        } else {
            template = lclSearchTemplateDAO.getTemplateByName("LCLSEARCH");
            request.setAttribute("orderedTemplateList", null);
        }
        if (!fileSearchList.isEmpty() && fileSearchList.size() == 1) { // if single file
            request.setAttribute("isSingleFile", "true");
        }
        request.setAttribute("template", template); 
        if (CommonUtils.isNotEmpty(lclSearchForm.getConoslidateFiles())) {
            lclSession.setConsolidated("false");
            request.setAttribute("consolidate", true);
            lclSession.setConsolidated("false");
            request.setAttribute("consIconSearchScreen", false);
        } else {
            lclSession.setConsolidated("false");
        }
        lclSession.setSearchResult("true");
        session.setAttribute("oldLclSearchForm", oldSearchForm);
        request.setAttribute("fileSearchList", fileSearchList);
        request.getSession().setAttribute("lclSession", lclSession);
        return mapping.findForward("searchResult");
    }

    public ActionForward applyExportUserDefaultValues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchForm lclSearchForm = (LclSearchForm) form;
        User user = (User) request.getSession().getAttribute("loginuser");
        LclApplyDefaultDetails applyDefaultDetails = null;
        LclUtils lclUtils = new LclUtils();
        applyDefaultDetails = new LclApplyDefaultDetailsDAO().findById(lclSearchForm.getLclDefaultId());
        if (null != applyDefaultDetails) {
            if (null != applyDefaultDetails.getApplyDefaultName()) {
                lclSearchForm.setLclDefaultName(applyDefaultDetails.getApplyDefaultName());
                lclSearchForm.setLclDefaultId(applyDefaultDetails.getId());
            }
            if (null != applyDefaultDetails.getCurrentLocation()) {
                lclSearchForm.setCurrentLocCode(applyDefaultDetails.getCurrentLocation().getId().toString());
                lclSearchForm.setCurrentLocName(applyDefaultDetails.getCurrentLocation().getUnLocationName());
                lclSearchForm.setCurrentLocation(lclUtils.getConcatenatedOriginByUnlocation(applyDefaultDetails.getCurrentLocation()));
            }
            if (null != applyDefaultDetails.getPortOfOrigin()) {
                lclSearchForm.setPortName(applyDefaultDetails.getPortOfOrigin().getUnLocationCode() + ",");
                lclSearchForm.setOrigin(lclUtils.getConcatenatedOriginByUnlocation(applyDefaultDetails.getPortOfOrigin()));
                lclSearchForm.setCountryCode(applyDefaultDetails.getPortOfOrigin().getId().toString());
            }
            if (null != applyDefaultDetails.getPortOfLoading()) {
                lclSearchForm.setPol(lclUtils.getConcatenatedOriginByUnlocation(applyDefaultDetails.getPortOfLoading()));
                lclSearchForm.setPolName(applyDefaultDetails.getPortOfLoading().getUnLocationCode() + ",");
                lclSearchForm.setPolCountryCode(applyDefaultDetails.getPortOfLoading().getId().toString());
            }
            if (null != applyDefaultDetails.getPortOfDestination()) {
                lclSearchForm.setPod(lclUtils.getConcatenatedOriginByUnlocation(applyDefaultDetails.getPortOfDestination()));
                lclSearchForm.setPodName(applyDefaultDetails.getPortOfDestination().getUnLocationName());
                lclSearchForm.setPodCountryCode(applyDefaultDetails.getPortOfDestination().getId().toString());
            }
            if (null != applyDefaultDetails.getFinalDestination()) {
                lclSearchForm.setDestination(lclUtils.getConcatenatedOriginByUnlocation(applyDefaultDetails.getFinalDestination()));
                lclSearchForm.setDestinationName(applyDefaultDetails.getFinalDestination().getUnLocationName());
                lclSearchForm.setDestCountryCode(applyDefaultDetails.getFinalDestination().getId().toString());
            }
        }
        String forwardPage = "searchScreen";
        String templateId = new UserDAO().getUserColumnValueWithUserId(user.getUserId().toString(), "user_default_template");
        lclSearchForm.setSortBy(CommonUtils.isNotEmpty(templateId) ? Integer.parseInt(templateId) : 0);
        request.setAttribute("applyDefaultNameList", new LclApplyDefaultDetailsDAO().getLclDefaultNameList(user.getUserId()));
        if (lclSearchForm.isSearchAndApply()) {
            search(mapping, form, request, response);
            forwardPage = "searchResult";
        }
        if (null != user.getRole()) {
            request.setAttribute("roleQuickBkg", new RoleDutyDAO().getRoleDetails("warehouse_quickBkg", user.getRole().getRoleId()));
        }
        request.setAttribute("lclSearchForm", lclSearchForm);
        request.setAttribute("user", (User) request.getSession().getAttribute("loginuser"));
        return mapping.findForward(forwardPage);
    }

    public ActionForward addMultieCountry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchForm lclSearchForm = (LclSearchForm) form;
        if ("origin".equalsIgnoreCase(lclSearchForm.getButtonValue())) {
            request.setAttribute("field1", "origin");
            request.setAttribute("field2", "portName");
            request.setAttribute("labelName", "Origin");
        } else if ("pol".equalsIgnoreCase(lclSearchForm.getButtonValue())) {
            request.setAttribute("field1", "pol");
            request.setAttribute("field2", "polName");
            request.setAttribute("labelName", "POL");
        }
        request.setAttribute("lclSearchForm", lclSearchForm);
        return mapping.findForward("addMultieCountry");
    }
}
