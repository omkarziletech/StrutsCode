/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclUserDefaults;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclApplyDefaultDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUserDefaultsDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclUserDefaultsForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.acegisecurity.UserDetails;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author HOME
 */
public class LclUserDefaultsAction extends LogiwareDispatchAction {

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        LclUserDefaultsForm lclUserDefaultsForm = (LclUserDefaultsForm) form;

        UserDAO userDAO = new UserDAO();
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        LclUserDefaultsDAO lclUserDefaultsDAO = new LclUserDefaultsDAO();
        LclUserDefaults lclUserDefaults = lclUserDefaultsDAO.getLclUserDefaultById((loginUser.getUserId()));
        if (lclUserDefaults == null) {
            lclUserDefaults = new LclUserDefaults();
        }
        lclUserDefaults.setCurrentLocation(new UnLocationDAO().findById(lclUserDefaultsForm.getCurrentLocationId()));
        lclUserDefaults.setFinalDestination(new UnLocationDAO().findById(lclUserDefaultsForm.getFinalDestinationId()));
        lclUserDefaults.setPortOfDestination(new UnLocationDAO().findById(lclUserDefaultsForm.getPortOfDestinationId()));
        lclUserDefaults.setPortOfLoading(new UnLocationDAO().findById(lclUserDefaultsForm.getPortOfLoadingId()));
        lclUserDefaults.setPortOfOrigin(new UnLocationDAO().findById(lclUserDefaultsForm.getPortOfOriginId()));
        lclUserDefaults.setUserDetails(userDAO.findById(loginUser.getUserId()));
        lclUserDefaultsDAO.saveOrUpdate(lclUserDefaults);

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
        request.setAttribute("currentLocation", currentLocation1);
        request.setAttribute("finalDestination", finalDestination1);
        request.setAttribute("portOfDestination", portOfDestination1);
        request.setAttribute("portOfLoading", portOfLoading1);
        request.setAttribute("portOfOrigin", portOfOrigin1);

        return mapping.findForward("success");
    }

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUserDefaultsForm lclUserDefaultsForm = (LclUserDefaultsForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        if ("Exports".equalsIgnoreCase(lclUserDefaultsForm.getSelectedMenu())) {
            request.setAttribute("applyDefaultNameList", new LclApplyDefaultDetailsDAO().getLclDefaultNameList(loginUser.getUserId()));
        } else {
            LclUserDefaultsDAO lclUserDefaultsDAO = new LclUserDefaultsDAO();
            LclUserDefaults lclUserDefaults = lclUserDefaultsDAO.getLclUserDefaultById(loginUser.getUserId());
            if (lclUserDefaults == null) {
                return mapping.findForward("success");
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
                lclUserDefaultsForm.setCurrentLocationId(unlocationCurrentLocation.getId());
            }
            if (null != unlocationFinalDestination && unlocationFinalDestination.getId() != null) {
                lclUserDefaultsForm.setFinalDestinationId(unlocationFinalDestination.getId());
            }
            if (null != unlocationPortOfDestination && unlocationPortOfDestination.getId() != null) {
                lclUserDefaultsForm.setPortOfDestinationId(unlocationPortOfDestination.getId());
            }
            if (null != unlocationPortOfLoading && unlocationPortOfLoading.getId() != null) {
                lclUserDefaultsForm.setPortOfLoadingId(unlocationPortOfLoading.getId());
            }
            if (null != unlocationPortOfOrigin && unlocationPortOfOrigin.getId() != null) {
                lclUserDefaultsForm.setPortOfOriginId(unlocationPortOfOrigin.getId());
            }

            request.setAttribute("currentLocation", currentLocation1);
            request.setAttribute("finalDestination", finalDestination1);
            request.setAttribute("portOfDestination", portOfDestination1);
            request.setAttribute("portOfLoading", portOfLoading1);
            request.setAttribute("portOfOrigin", portOfOrigin1);
        }

        return mapping.findForward("success");
    }

    public ActionForward clear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUserDefaultsForm lclUserDefaultsForm = (LclUserDefaultsForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");

        LclUserDefaultsDAO lclUserDefaultsDAO = new LclUserDefaultsDAO();
        LclUserDefaults lclUserDefaults = lclUserDefaultsDAO.getLclUserDefaultById(loginUser.getUserId());

        if (lclUserDefaults == null) {
            return mapping.findForward("success");
        }
        UnLocation unlocationCurrentLocation = lclUserDefaults.getCurrentLocation();
        UnLocation unlocationFinalDestination = lclUserDefaults.getFinalDestination();
        UnLocation unlocationPortOfDestination = lclUserDefaults.getPortOfDestination();
        UnLocation unlocationPortOfLoading = lclUserDefaults.getPortOfLoading();
        UnLocation unlocationPortOfOrigin = lclUserDefaults.getPortOfOrigin();

        if (unlocationCurrentLocation == null) {
            request.setAttribute("currentLocation", null);
        }

        if (unlocationFinalDestination == null) {
            request.setAttribute("finalDestination", null);
        }
        if (unlocationPortOfDestination == null) {
            request.setAttribute("portOfDestination", null);
        }

        if (unlocationPortOfLoading == null) {
            request.setAttribute("portOfLoading", null);
        }

        if (unlocationPortOfOrigin == null) {
            request.setAttribute("portOfOrigin", null);
        }

        return mapping.findForward("success");
    }
}
