/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.lcl.LclConsolidate;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.lcl.bc.ExportBookingUtils;
import com.gp.cong.logisoft.lcl.model.LclConsolidateBean;
import com.gp.cvst.logisoft.struts.form.lcl.LclConsolidateForm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author lakshh
 */
public class LclConsolidateAction extends LogiwareDispatchAction {

    private static String CONSOLIDATE_DESC = "consolidateDesc";

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclConsolidateForm lclConsolidateForm = (LclConsolidateForm) form;
        if (CommonUtils.isNotEmpty(lclConsolidateForm.getFileNumberId())) {
            LclConsolidateDAO consolidateDAO = new LclConsolidateDAO();
            List<LclConsolidateBean> files = consolidateDAO.getAllConsolidateFiles(lclConsolidateForm.getFileNumberId(), lclConsolidateForm);
            lclConsolidateForm.setConsolidateFileList(files);
            request.setAttribute("fileNumber", request.getParameter("fileNumber"));
            request.setAttribute("consolidatedId", request.getParameter("consolidatedId"));
        }
        request.setAttribute("lclConsolidateForm", lclConsolidateForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward saveConsolidate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclConsolidateForm lclConsolidateForm = (LclConsolidateForm) form;
        if (CommonUtils.isNotEmpty(lclConsolidateForm.getConsolidatedFileIds())
                && CommonUtils.isNotEmpty(lclConsolidateForm.getFileNumberId())) {
            LclUtils lclUtils = new LclUtils();
            String file_state = request.getParameter("fileState");
            LclConsolidateDAO lclConsolidateDAO = new LclConsolidateDAO();
            List existsConsildateList = lclConsolidateDAO.getConsolidatesFiles(Long.parseLong(lclConsolidateForm.getFileNumberId().toString()));
            existsConsildateList.add(lclConsolidateForm.getFileNumberId().toString());

            String[] consolidated_Array = lclConsolidateForm.getConsolidatedFileIds().split(",");
            LclConsolidate consolidate = lclConsolidateDAO.getByProperty("lclFileNumberA.id", lclConsolidateForm.getFileNumberId());
            Long parentFileId = consolidate != null && (consolidate.getLclFileNumberA() != consolidate.getLclFileNumberB())
                    ? consolidate.getLclFileNumberB().getId() : lclConsolidateForm.getFileNumberId();

            List<String> childIds = new ArrayList<String>();
            if (CommonUtils.isNotEmpty(consolidated_Array)) {
                for (String childId : consolidated_Array) {
                    lclConsolidateDAO.updateConsolidateFile(Long.parseLong(childId), parentFileId, file_state);
                    childIds.add(childId);
                }
            }
            for (Object file : childIds) {
                List consolidatedFiles = lclConsolidateDAO.getConsolidatesFiles(Long.parseLong(file.toString()));
                String r1 = "Consolidated ->  " + new LclFileNumberDAO().getFileNumberByFileId(file.toString());
                for (Object id : consolidatedFiles) {
                    lclUtils.insertLCLRemarks(Long.parseLong(id.toString()), r1, LclCommonConstant.REMARKS_DR_AUTO_NOTES, getCurrentUser(request));
                }
                if (!parentFileId.equals(lclConsolidateForm.getFileNumberId())) {
                    String r2 = "Consolidated ->  " + new LclFileNumberDAO().getFileNumberByFileId(lclConsolidateForm.getFileNumberId().toString());
                    lclUtils.insertLCLRemarks(Long.parseLong(file.toString()), r2, LclCommonConstant.REMARKS_DR_AUTO_NOTES, getCurrentUser(request));
                    consolidatedFiles = lclConsolidateDAO.getConsolidatesFiles(Long.parseLong(file.toString()));
                    for (Object innerId : consolidatedFiles) {
                        if (!innerId.equals(lclConsolidateForm.getFileNumberId()) && !childIds.contains(innerId.toString())) {
                            String r3 = "Consolidated ->  " + new LclFileNumberDAO().getFileNumberByFileId(innerId.toString());
                            lclUtils.insertLCLRemarks(Long.parseLong(file.toString()), r3, LclCommonConstant.REMARKS_DR_AUTO_NOTES, getCurrentUser(request));
                        }
                    }
                } else {
                    for (Object existsId : existsConsildateList) {
                        String r2 = "Consolidated ->  " + new LclFileNumberDAO().getFileNumberByFileId(existsId.toString());
                        lclUtils.insertLCLRemarks(Long.parseLong(file.toString()), r2, LclCommonConstant.REMARKS_DR_AUTO_NOTES, getCurrentUser(request));
                    }
                }
            }

            String acceptance_Remarks = request.getParameter("acceptance_remarks");
            if (CommonUtils.isNotEmpty(request.getParameter("currentFileId"))) {
                new ExportBookingUtils().updateConsolidationBLCharges(Long.parseLong(request.getParameter("currentFileId")),
                        null != acceptance_Remarks ? acceptance_Remarks : "", getCurrentUser(request), request);
            }
            lclUtils.getFormattedConsolidatedList(request, lclConsolidateForm.getFileNumberId());
        }
        return mapping.findForward(CONSOLIDATE_DESC);
    }

    public ActionForward doSortAscDec(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclConsolidateForm lclConsolidateForm = (LclConsolidateForm) form;
        lclConsolidateForm.setSortByValue(request.getParameter("sortByValue"));
        lclConsolidateForm.setSearchType(request.getParameter("searchType"));
        if (CommonUtils.isNotEmpty(lclConsolidateForm.getFileNumberId())) {
            List<LclConsolidateBean> files = new LclConsolidateDAO().getAllConsolidateFiles(lclConsolidateForm.getFileNumberId(), lclConsolidateForm);
            lclConsolidateForm.setConsolidateFileList(files);
        }
        request.setAttribute("lclConsolidateForm", lclConsolidateForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward openManualConsolidateDR(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclConsolidateForm lclConsolidateForm = (LclConsolidateForm) form;
        if(null != request.getSession() && null != request.getSession().getAttribute("pickedConsolidateDrList")) {
        request.getSession().removeAttribute("pickedConsolidateDrList");
        }
        request.setAttribute("destination",lclConsolidateForm.getPodId());
        request.setAttribute("finalDest", lclConsolidateForm.getFdId());
        request.setAttribute("fileNumberId", lclConsolidateForm.getFileNumberId());
        return mapping.findForward("manualAddPickedConslidated");
    }

//    public ActionForward displayPickedConsolidateDr(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        LclConsolidateForm lclConsolidateForm = (LclConsolidateForm) form;
//        Set<String> sessionPickedConsolidateDrList = new HashSet<>();
//        if (CommonUtils.isNotEmpty(lclConsolidateForm.getPickedConsolidateDr())) {
//            HttpSession session = request.getSession(true);
//            if (session != null) {
//                sessionPickedConsolidateDrList = (Set<String>) (null != request.getSession().getAttribute("pickedConsolidateDrList") ? request.getSession().getAttribute("pickedConsolidateDrList") : new HashSet<>());
//                if (sessionPickedConsolidateDrList.add(lclConsolidateForm.getPickedConsolidateDr())) {
//                    session.setAttribute("pickedConsolidateDrList", sessionPickedConsolidateDrList);
//                } else {
//                    request.setAttribute("duplicateFileNo", lclConsolidateForm.getPickedConsolidateDr());
//                }
//            }
//        }
//        return mapping.findForward("manualAddPickedConslidated");
//    }

//    public ActionForward removePickedConsolidateDr(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        LclConsolidateForm lclConsolidateForm = (LclConsolidateForm) form;
//        Set<String> sessionPickedConsolidateDrList = new HashSet<>();
//        HttpSession session = request.getSession(true);
//        sessionPickedConsolidateDrList = (Set<String>) request.getSession().getAttribute("pickedConsolidateDrList");
//        while (sessionPickedConsolidateDrList.remove(lclConsolidateForm.getPickedConsolidateDr())) {
//            session.setAttribute("pickedConsolidateDrList", sessionPickedConsolidateDrList);
//        }
//        return mapping.findForward("manualAddPickedConslidated");
//    }
}
