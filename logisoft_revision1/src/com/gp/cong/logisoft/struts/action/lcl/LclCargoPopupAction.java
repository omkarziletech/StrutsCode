package com.gp.cong.logisoft.struts.action.lcl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cvst.logisoft.struts.form.lcl.LclCargoPopupForm;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingDispoDAO;

public class LclCargoPopupAction extends LogiwareDispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclCargoPopupForm lclCargoPopupForm = (LclCargoPopupForm) form;
        LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
        LclBooking lclBooking = lclBookingDAO.getByProperty("lclFileNumber.id", Long.parseLong(lclCargoPopupForm.getFileId()));
        Integer currentLocId = new LclBookingDispoDAO().currentLocationId(Long.parseLong(lclCargoPopupForm.getFileId()));
        //Import Transhipment Logic
        if (currentLocId == null) {
            if (LclCommonConstant.LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(lclBooking.getBookingType())) {
                lclCargoPopupForm.setOriginCityName(getContactCityStateValues(lclBooking.getPortOfDestination()));
                currentLocId = lclBooking.getPortOfDestination().getId();
            } else {
                currentLocId = lclBooking.getPortOfOrigin().getId();
                lclCargoPopupForm.setOriginCityName(getContactCityStateValues(lclBooking.getPortOfOrigin()));
            }
        } else {
            lclCargoPopupForm.setOriginCityName(getContactCityStateValues(new UnLocationDAO().findById(currentLocId)));
        }
        lclCargoPopupForm.setFileNumber(lclBooking.getLclFileNumber().getFileNumber());
        request.setAttribute("lclFileNumber", lclBooking.getLclFileNumber());
        request.setAttribute("currentLocId", currentLocId);
        request.setAttribute("lclCargoPopupForm", lclCargoPopupForm);
        request.setAttribute("bookingImport", lclBooking.getLclFileNumber().getLclBookingImport());
        request.setAttribute("booking", lclBooking);
        return mapping.findForward("success");
    }

    public String getContactCityStateValues(UnLocation unlocation) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (unlocation != null) {
            if (unlocation.getUnLocationName() != null) {
                sb.append(unlocation.getUnLocationName());
            }
            if (null != unlocation.getStateId() && CommonUtils.isNotEmpty(unlocation.getStateId().getCode())) {
                sb.append(",").append(unlocation.getStateId().getCode());
            }
        }
        return sb.toString();

    }
}
