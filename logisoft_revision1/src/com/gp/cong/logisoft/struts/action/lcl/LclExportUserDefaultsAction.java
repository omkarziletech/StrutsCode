/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclApplyDefaultDetails;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclApplyDefaultDetailsDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclUserDefaultsForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author aravindhan.v
 */
public class LclExportUserDefaultsAction extends LogiwareDispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUserDefaultsForm lclUserDefaultsForm = (LclUserDefaultsForm) form;
        User user = (User) request.getSession().getAttribute("loginuser");
        request.setAttribute("applyDefaultNameList", new LclApplyDefaultDetailsDAO().getLclDefaultNameList(user.getUserId()));
        return mapping.findForward("success");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUserDefaultsForm lclUserDefaultsForm = (LclUserDefaultsForm) form;
        User user = (User) request.getSession().getAttribute("loginuser");
        LclApplyDefaultDetails applyDefaultDetails = null;
        if (0 == lclUserDefaultsForm.getApplyDefaultId()) {
            applyDefaultDetails = new LclApplyDefaultDetails();
        } else {
            applyDefaultDetails = new LclApplyDefaultDetailsDAO().findById(lclUserDefaultsForm.getApplyDefaultId());
        }
        UnLocationDAO unlocationdao = new UnLocationDAO();
        applyDefaultDetails.setApplyDefaultName(lclUserDefaultsForm.getLcldefaultName());
        applyDefaultDetails.setUser(user);
        applyDefaultDetails.setCurrentLocation(unlocationdao.findById(lclUserDefaultsForm.getCurrentLocationId()));
        applyDefaultDetails.setPortOfOrigin(unlocationdao.findById(lclUserDefaultsForm.getPortOfOriginId()));
        applyDefaultDetails.setPortOfLoading(unlocationdao.findById(lclUserDefaultsForm.getPortOfLoadingId()));
        applyDefaultDetails.setPortOfDestination(unlocationdao.findById(lclUserDefaultsForm.getPortOfDestinationId()));
        applyDefaultDetails.setFinalDestination(unlocationdao.findById(lclUserDefaultsForm.getFinalDestinationId()));
        new LclApplyDefaultDetailsDAO().saveOrUpdate(applyDefaultDetails);
        request.setAttribute("applyDefaultNameList", new LclApplyDefaultDetailsDAO().getLclDefaultNameList(user.getUserId()));
        return mapping.findForward("success");

    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUserDefaultsForm lclUserDefaultsForm = (LclUserDefaultsForm) form;
        User user = (User) request.getSession().getAttribute("loginuser");
        LclApplyDefaultDetails applyDefaultDetails = null;
        if (0 != lclUserDefaultsForm.getApplyDefaultId()) {
            applyDefaultDetails = new LclApplyDefaultDetailsDAO().findById(lclUserDefaultsForm.getApplyDefaultId());
            new LclApplyDefaultDetailsDAO().delete(applyDefaultDetails);
        }
        request.setAttribute("applyDefaultNameList", new LclApplyDefaultDetailsDAO().getLclDefaultNameList(user.getUserId()));
        return mapping.findForward("success");
    }

    public ActionForward clear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUserDefaultsForm lclUserDefaultsForm = (LclUserDefaultsForm) form;
        User user = (User) request.getSession().getAttribute("loginuser");
        lclUserDefaultsForm.reset(mapping, request);
        request.setAttribute("applyDefaultNameList", new LclApplyDefaultDetailsDAO().getLclDefaultNameList(user.getUserId()));
        return mapping.findForward("success");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUserDefaultsForm lclUserDefaultsForm = (LclUserDefaultsForm) form;
        User user = (User) request.getSession().getAttribute("loginuser");
        request.setAttribute("applyDefaultNameList", new LclApplyDefaultDetailsDAO().getLclDefaultNameList(user.getUserId()));
        return mapping.findForward("success");
    }

    public ActionForward goBack(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUserDefaultsForm lclUserDefaultsForm = (LclUserDefaultsForm) form;
        User user = (User) request.getSession().getAttribute("loginuser");
        request.setAttribute("applyDefaultNameList", new LclApplyDefaultDetailsDAO().getLclDefaultNameList(user.getUserId()));
        return mapping.findForward("success");
    }

    public ActionForward showValue(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUserDefaultsForm lclUserDefaultsForm = (LclUserDefaultsForm) form;
        User user = (User) request.getSession().getAttribute("loginuser");
        LclUtils lclUtils = new LclUtils();
        LclApplyDefaultDetails applyDefaultDetails = new LclApplyDefaultDetailsDAO().findById(lclUserDefaultsForm.getApplyDefaultId());
        if (null != applyDefaultDetails) {
            lclUserDefaultsForm.setLcldefaultName(null != applyDefaultDetails.getApplyDefaultName() ? applyDefaultDetails.getApplyDefaultName() : "");
            if (null != applyDefaultDetails.getCurrentLocation()) {
                lclUserDefaultsForm.setCurrentLocationId(applyDefaultDetails.getCurrentLocation().getId());
                lclUserDefaultsForm.setCurrentLocation(lclUtils.getConcatenatedOriginByUnlocation(applyDefaultDetails.getCurrentLocation()));
            } else {
                lclUserDefaultsForm.setCurrentLocationId(0);
                lclUserDefaultsForm.setCurrentLocation("");
            }
            if (null != applyDefaultDetails.getPortOfOrigin()) {
                lclUserDefaultsForm.setPortOfOriginId(applyDefaultDetails.getPortOfOrigin().getId());
                lclUserDefaultsForm.setPortOfOrigin(lclUtils.getConcatenatedOriginByUnlocation(applyDefaultDetails.getPortOfOrigin()));
            } else {
                lclUserDefaultsForm.setPortOfOriginId(0);
                lclUserDefaultsForm.setPortOfOrigin("");
            }
            if (null != applyDefaultDetails.getPortOfLoading()) {
                lclUserDefaultsForm.setPortOfLoadingId(applyDefaultDetails.getPortOfLoading().getId());
                lclUserDefaultsForm.setPortOfLoading(lclUtils.getConcatenatedOriginByUnlocation(applyDefaultDetails.getPortOfLoading()));
            } else {
                lclUserDefaultsForm.setPortOfLoadingId(0);
                lclUserDefaultsForm.setPortOfLoading("");
            }
            if (null != applyDefaultDetails.getPortOfDestination()) {
                lclUserDefaultsForm.setPortOfDestinationId(applyDefaultDetails.getPortOfDestination().getId());
                lclUserDefaultsForm.setPortOfDestination(lclUtils.getConcatenatedOriginByUnlocation(applyDefaultDetails.getPortOfDestination()));
            } else {
                lclUserDefaultsForm.setPortOfDestinationId(0);
                lclUserDefaultsForm.setPortOfDestination("");
            }
            if (null != applyDefaultDetails.getFinalDestination()) {
                lclUserDefaultsForm.setFinalDestinationId(applyDefaultDetails.getFinalDestination().getId());
                lclUserDefaultsForm.setFinalDestination(lclUtils.getConcatenatedOriginByUnlocation(applyDefaultDetails.getFinalDestination()));
            } else {
                lclUserDefaultsForm.setFinalDestinationId(0);
                lclUserDefaultsForm.setFinalDestination("");
            }
        }
        request.setAttribute("lclUserDefaultsForm", lclUserDefaultsForm);
        request.setAttribute("applyDefaultNameList", new LclApplyDefaultDetailsDAO().getLclDefaultNameList(user.getUserId()));
        return mapping.findForward("success");
    }

    public String concatUnlocationName(Integer unLocaId) throws Exception {
        StringBuilder concatedString = new StringBuilder();
        UnLocation unlocation = new UnLocationDAO().findById(null != unLocaId ? unLocaId : 0);
        if (null != unlocation) {
            concatedString.append(unlocation.getUnLocationName()).append(unlocation.getStateId() != null ? "/" + unlocation.getStateId().getCode() : "");
            concatedString.append("(").append(unlocation.getUnLocationCode()).append(")");
        }
        return concatedString.toString();
    }
}
