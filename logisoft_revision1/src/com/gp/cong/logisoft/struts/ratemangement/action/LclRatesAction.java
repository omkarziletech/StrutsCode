/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.ratemangement.action;

import com.gp.cong.logisoft.beans.LclRatesInfoBean;
import com.gp.cong.logisoft.beans.LclRatesPrtChgBean;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLRatesDAO;
import com.gp.cong.logisoft.struts.action.lcl.LogiwareDispatchAction;
import com.gp.cong.logisoft.struts.ratemangement.form.LclRatesForm;
import com.gp.cong.struts.LoadLogisoftProperties;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author logiware
 */
public class LclRatesAction extends LogiwareDispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclRatesForm lclRatesForm = (LclRatesForm) form;
        return mapping.findForward("success");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclRatesForm lclRatesForm = (LclRatesForm) form;
        String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        LCLRatesDAO lclRatesDAO = new LCLRatesDAO(databaseSchema);
        User user = (User) request.getSession().getAttribute("loginuser");
        // lclRatesDAO.insertOfrates(lclRatesForm, databaseSchema, user.getLoginName());
        List<LclRatesInfoBean> ratesList = lclRatesDAO.findRates(lclRatesForm.getTrmnum(), lclRatesForm.getEciportcode(), lclRatesForm.getComCode());
        List<LclRatesPrtChgBean> ratesPrtChgList = lclRatesDAO.findPrtChgRates(lclRatesForm.getTrmnum(), lclRatesForm.getEciportcode(), lclRatesForm.getComCode());
        request.setAttribute("ratesList", ratesList);
        request.setAttribute("ratesPrtChgList", ratesPrtChgList);
        request.setAttribute("lclRatesForm", lclRatesForm);
        return mapping.findForward("success");
    }

    public ActionForward saveRates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclRatesForm lclRatesForm = (LclRatesForm) form;
        String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        LCLRatesDAO lclRatesDAO = new LCLRatesDAO(databaseSchema);
        User user = (User) request.getSession().getAttribute("loginuser");
        List<LclRatesInfoBean> ratesList = lclRatesDAO.findRates(lclRatesForm.getTrmnum(), lclRatesForm.getEciportcode(), lclRatesForm.getComCode());
        if (ratesList.isEmpty()) {
            lclRatesDAO.insertOfrates(lclRatesForm, databaseSchema, user.getLoginName());
        }else{
            lclRatesDAO.updateRates(lclRatesForm, databaseSchema, user.getLoginName());
        }
        ratesList = lclRatesDAO.findRates(lclRatesForm.getTrmnum(), lclRatesForm.getEciportcode(), lclRatesForm.getComCode());
        request.setAttribute("ratesList", ratesList);
        request.setAttribute("lclRatesForm", lclRatesForm);
        return mapping.findForward("success");
    }

    public ActionForward savePrtRates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclRatesForm lclRatesForm = (LclRatesForm) form;
        String databaseSchema = LoadLogisoftProperties.getProperty("elite.database.name");
        LCLRatesDAO lclRatesDAO = new LCLRatesDAO(databaseSchema);
        User user = (User) request.getSession().getAttribute("loginuser");
        lclRatesDAO.insertOfratesPrtChg(lclRatesForm, databaseSchema, user.getLoginName());
        List<LclRatesInfoBean> ratesList = lclRatesDAO.findRates(lclRatesForm.getTrmnum(), lclRatesForm.getEciportcode(), lclRatesForm.getComCode());
        List<LclRatesPrtChgBean> ratesPrtChgList = lclRatesDAO.findPrtChgRates(lclRatesForm.getTrmnum(), lclRatesForm.getEciportcode(), lclRatesForm.getComCode());
        request.setAttribute("ratesList", ratesList);
        request.setAttribute("ratesPrtChgList", ratesPrtChgList);
        request.setAttribute("lclRatesForm", lclRatesForm);
        return mapping.findForward("success");
    }
}
