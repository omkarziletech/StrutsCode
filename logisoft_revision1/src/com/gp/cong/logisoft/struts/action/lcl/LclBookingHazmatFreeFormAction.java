/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cvst.logisoft.struts.form.lcl.LclBookingHazmatFreeForm;
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
public class LclBookingHazmatFreeFormAction extends LogiwareDispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        LclBookingHazmatFreeForm freeForm = (LclBookingHazmatFreeForm) form;
        String freeformValue = setHazmatFreeFormInfo(freeForm);
        if (CommonUtils.isNotEmpty(freeformValue)) {
            freeForm.setFreeFormatValue("");
            String[] data = freeformValue.split("\n");
            if (CommonUtils.isNotEmpty(data)) {
                for (int i = 0; i < data.length; i++) {
                    if (i == 0) {
                        freeForm.setLine1(CommonUtils.isNotEmpty(data[0]) ? data[0] : "");
                    } else if (i == 1) {
                        freeForm.setLine2(CommonUtils.isNotEmpty(data[1]) ? data[1] : "");
                    } else if (i == 2) {
                        freeForm.setLine3(CommonUtils.isNotEmpty(data[2]) ? data[2] : "");
                    } else if (i == 3) {
                        freeForm.setLine4(CommonUtils.isNotEmpty(data[3]) ? data[3] : "");
                    } else if (i == 4) {
                        freeForm.setLine5(CommonUtils.isNotEmpty(data[4]) ? data[4] : "");
                    } else if (i == 5) {
                        freeForm.setLine6(CommonUtils.isNotEmpty(data[5]) ? data[5] : "");
                    } else if (i == 6) {
                        freeForm.setLine7(CommonUtils.isNotEmpty(data[6]) ? data[6] : "");
                    }
                }
            }
        } else {
            request.setAttribute("freeFormValue", freeForm.getFreeFormatValue().toUpperCase());
        }
        request.setAttribute("lclBookingHazmatFreeForm", freeForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        LclBookingHazmatFreeForm freeForm = (LclBookingHazmatFreeForm) form;
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession")
                ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        lclSession.setFreeFormatValue(freeForm.getFreeFormatValue());
        if (CommonUtils.isNotEmpty(freeForm.getSelectedSection()) && CommonUtils.isNotEmpty(freeForm.getHazmatId())) {
            if ("LCLQT".equalsIgnoreCase(freeForm.getSelectedSection())) {
                saveHazmatFreeFormInfo(freeForm, "lcl_quote_hazmat");
            } else if ("LCLBKG".equalsIgnoreCase(freeForm.getSelectedSection())) {
                saveHazmatFreeFormInfo(freeForm, "lcl_booking_hazmat");
            } else if ("LCLBL".equalsIgnoreCase(freeForm.getSelectedSection())) {
                saveHazmatFreeFormInfo(freeForm, "lcl_bl_hazmat");
            }
        }
        session.setAttribute("lclSession", lclSession);
        return mapping.findForward(SUCCESS);
    }

    public void saveHazmatFreeFormInfo(LclBookingHazmatFreeForm freeForm, String dbName) throws Exception {
        new LclUtils().updateHazmatFreeFromData(dbName, freeForm.getHazmatId(), freeForm.getFreeFormatValue());
    }

    public String setHazmatFreeFormInfo(LclBookingHazmatFreeForm freeForm) throws Exception {
        String hazmatInfo = "";
        if (CommonUtils.isNotEmpty(freeForm.getSelectedSection()) && CommonUtils.isNotEmpty(freeForm.getHazmatId())) {
            if ("LCLQT".equalsIgnoreCase(freeForm.getSelectedSection())) {
                hazmatInfo = new LclUtils().getHazmatFreeFormData("lcl_quote_hazmat", freeForm.getHazmatId());
            } else if ("LCLBKG".equalsIgnoreCase(freeForm.getSelectedSection())) {
                hazmatInfo = new LclUtils().getHazmatFreeFormData("lcl_booking_hazmat", freeForm.getHazmatId());
            } else if ("LCLBL".equalsIgnoreCase(freeForm.getSelectedSection())) {
                hazmatInfo = new LclUtils().getHazmatFreeFormData("lcl_bl_hazmat", freeForm.getHazmatId());
            }
        }
        return hazmatInfo;
    }
}
