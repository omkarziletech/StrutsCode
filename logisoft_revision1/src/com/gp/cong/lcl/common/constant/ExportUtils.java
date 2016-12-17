/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.common.constant;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.lcl.model.LclBookingPlanBean;
import com.logiware.common.filter.JspWrapper;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mei
 */
public class ExportUtils {

    public Map setRelayandUpcomingSailings(Map result, Integer pooId, Integer fdId,
            String relay, String screenName, HttpServletRequest request, HttpServletResponse response, String cfcl) throws Exception {

        LclBookingPlanDAO bookingPlanDAO = new LclBookingPlanDAO();
        LclBookingPlanBean bookingPlanBean = bookingPlanDAO.getRelay(pooId, fdId, relay);
        setPolPod(bookingPlanBean, request);
        JspWrapper jspWrapper = new JspWrapper(response);
        request.getRequestDispatcher("/jsps/LCL/ajaxload/polPod.jsp").include(request, jspWrapper);
        result.put("polPod", jspWrapper.getOutput());
        if ("Quote".equalsIgnoreCase(screenName)) {
            request.setAttribute("voyageAction", true);
        }
        request.setAttribute("voyageList", getUpcomingSailings(pooId, fdId, bookingPlanBean, bookingPlanDAO, cfcl));
        jspWrapper = new JspWrapper(response);
        request.getRequestDispatcher("/jsps/LCL/lclVoyage.jsp").include(request, jspWrapper);
        result.put("upcomingSailings", jspWrapper.getOutput());
        return result;
    }

    public Map setRelayandUpcomingSailingsOlder(Map result, Integer pooId, Integer fdId,
            String relay, String prevSailing, String screenName, HttpServletRequest request, HttpServletResponse response, String cfcl) throws Exception {

        LclBookingPlanDAO bookingPlanDAO = new LclBookingPlanDAO();
        LclBookingPlanBean bookingPlanBean = bookingPlanDAO.getRelay(pooId, fdId, relay);
        setPolPod(bookingPlanBean, request);

        if ("Quote".equalsIgnoreCase(screenName)) {
            request.setAttribute("voyageAction", true);
        }
        request.setAttribute("voyageList", getUpcomingSailingsOlder(pooId, fdId, prevSailing, bookingPlanBean, bookingPlanDAO, cfcl));
        JspWrapper jspWrapper = new JspWrapper(response);
        request.getRequestDispatcher("/jsps/LCL/lclVoyage.jsp").include(request, jspWrapper);
        result.put("upcomingSailings", jspWrapper.getOutput());
        return result;
    }

    public void setPolPod(LclBookingPlanBean bookingPlanBean, HttpServletRequest request) throws Exception {
        LclFileNumberDAO fileNumberDAO = new LclFileNumberDAO();
        if (CommonUtils.isNotEmpty(bookingPlanBean.getPol_id())) {
            request.setAttribute("pol", fileNumberDAO.getConcatedUnlocationById(bookingPlanBean.getPol_id()));
            request.setAttribute("portOfLoadingId", bookingPlanBean.getPol_id());
            request.setAttribute("polUnlocationcode", bookingPlanBean.getPol_code());
            request.setAttribute("polCode", bookingPlanBean.getPol_code());
        }
        if (CommonUtils.isNotEmpty(bookingPlanBean.getPod_id())) {
            request.setAttribute("pod", fileNumberDAO.getConcatedUnlocationById(bookingPlanBean.getPod_id()));
            request.setAttribute("portOfDestinationId", bookingPlanBean.getPod_id());
            request.setAttribute("podUnlocationcode", bookingPlanBean.getPod_code());
            request.setAttribute("podCode", bookingPlanBean.getPod_code());
        }
    }

    public List getUpcomingSailings(Integer pooId, Integer fdId, LclBookingPlanBean bookingPlanBean,
            LclBookingPlanDAO bookingPlanDAO, String cfcl) throws Exception {
        return bookingPlanDAO.getUpComingSailingsSchedule(pooId, bookingPlanBean.getPol_id(), bookingPlanBean.getPod_id(),
                fdId, "V", bookingPlanBean, cfcl);
    }

    public List getUpcomingSailingsOlder(Integer pooId, Integer fdId, String prevSailing, LclBookingPlanBean bookingPlanBean,
            LclBookingPlanDAO bookingPlanDAO, String cfcl) throws Exception {
        return bookingPlanDAO.getUpComingSailingsScheduleOlder(pooId, bookingPlanBean.getPol_id(), bookingPlanBean.getPod_id(),
                fdId, "V", bookingPlanBean, prevSailing, cfcl);
    }

    public String[] getTerimal(String fileId, String state) throws Exception {
        try {
            String[] result = new String[3];
            if (state.equalsIgnoreCase("Booking")) {
                LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileId));
                new LCLBookingDAO().getCurrentSession().evict(lclBooking);
                result[0] = lclBooking.getTerminal().getTerminalLocation() + "/" + lclBooking.getTerminal().getTrmnum();
                result[1] = lclBooking.getTerminal().getTrmnum();
                result[2] = lclBooking.getLclFileNumber().getStatus();
            } else if (state.equalsIgnoreCase("BL")) {
                LclBl lclBl = new LCLBlDAO().findById(Long.parseLong(fileId));
                new LCLBlDAO().getCurrentSession().evict(lclBl);
                result[0] = lclBl.getTerminal().getTerminalLocation() + "/" + lclBl.getTerminal().getTrmnum();
                result[1] = lclBl.getTerminal().getTrmnum();
                result[2] = "";
            }
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    public String[] getPortOfDestination(String fileId, HttpServletRequest request) throws Exception {
        String[] result = {"", "", "", "", "", "", "", "", "", "", ""};
        LclBl lclBl = new LCLBlDAO().findById(Long.parseLong(fileId));
        new LCLBlDAO().getCurrentSession().evict(lclBl);
        if (lclBl.getPortOfLoading() != null) {
            result[0] = new LclFileNumberDAO().getConcatedUnlocationById(lclBl.getPortOfDestination().getId());
            result[1] = lclBl.getPortOfDestination().getId().toString();
            result[2] = lclBl.getPortOfDestination().getUnLocationCode();
        }
        if (lclBl.getRelayOverride()) {
            result[3] = "true";
        }
        if (lclBl.getPortOfLoading() != null) {
            result[4] = new LclFileNumberDAO().getConcatedUnlocationById(lclBl.getPortOfLoading().getId());
            result[5] = lclBl.getPortOfLoading().getId().toString();
            result[6] = lclBl.getPortOfLoading().getUnLocationCode();
        }
        if (lclBl.getFinalDestination() != null) {
            result[7] = new LclFileNumberDAO().getConcatedUnlocationById(lclBl.getFinalDestination().getId());
            result[8] = lclBl.getFinalDestination().getId().toString();
            result[9] = lclBl.getFinalDestination().getUnLocationCode();
        }
        result[10] = lclBl.getDeliveryMetro();
        return result;
    }
}
