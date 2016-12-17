/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.lcl.sailings.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
import com.gp.cong.logisoft.lcl.model.LclBookingPlanBean;
import com.gp.cong.logisoft.lcl.model.LclBookingVoyageBean;
import com.gp.cong.logisoft.struts.action.lcl.LogiwareDispatchAction;
import com.logiware.lcl.sailings.form.SailingsScheduleForm;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Mei
 */
public class SailingsScheduleAction extends LogiwareDispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception, SQLException {
        SailingsScheduleForm sailingsScheduleForm = (SailingsScheduleForm) form;
        return mapping.findForward("success");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception, SQLException {
        SailingsScheduleForm sailingsScheduleForm = (SailingsScheduleForm) form;
        LclBookingPlanDAO lclBookingPlanDAO = new LclBookingPlanDAO();
        UnLocationDAO unlocationdao = new UnLocationDAO();
        LclUtils lclUtils = new LclUtils();
        List<LclBookingVoyageBean> sailingScheduleList = null;
        LclBookingPlanBean lclBookingPlanBean = lclBookingPlanDAO.getRelay(sailingsScheduleForm.getPooId(),
                sailingsScheduleForm.getFdId(), "N");
        if (lclBookingPlanBean != null) {
            if (CommonUtils.isNotEmpty(lclBookingPlanBean.getPol_id())) {
                sailingsScheduleForm.setPolId(lclBookingPlanBean.getPol_id());
                UnLocation unlocation = unlocationdao.findById(sailingsScheduleForm.getPolId());
                sailingsScheduleForm.setPolName(lclUtils.getConcatenatedOriginByUnlocation(unlocation));
            }
            if (CommonUtils.isNotEmpty(lclBookingPlanBean.getPod_id())) {
                sailingsScheduleForm.setPodId(lclBookingPlanBean.getPod_id());
                UnLocation unlocation = unlocationdao.findById(sailingsScheduleForm.getPodId());
                sailingsScheduleForm.setPodName(lclUtils.getConcatenatedOriginByUnlocation(unlocation));
            }
           sailingScheduleList = lclBookingPlanDAO.getSailingsScheduleSearch(sailingsScheduleForm.getPooId(),
                    sailingsScheduleForm.getPolId(), sailingsScheduleForm.getPodId(), sailingsScheduleForm.getFdId(), "V", lclBookingPlanBean);
            sailingsScheduleForm.setSailingScheduleList(sailingScheduleList);
        }
        return mapping.findForward("success");
    }
}
