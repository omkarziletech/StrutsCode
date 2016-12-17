/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclMapForm;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Meiyazhakan
 */
public class LclServiceMapAction extends LogiwareDispatchAction {

    public ActionForward displayDoorOriginCityMap(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclMapForm lclMapForm = (LclMapForm) form;
        List<Object[]> doorList = new PortsDAO().getDoorOriginCity(lclMapForm.getCountryName(), lclMapForm.getZipCode());
        //request.setAttribute("countryName", lclMapForm.getCountryName());
        if (CommonUtils.isNotEmpty(doorList)) {
            for (Object[] city : doorList) {
                request.setAttribute("locationName", city[0].toString());
                //countryName = city[1].toString();
                break;
            }
            request.setAttribute("destinationList", doorList);
        }
        return mapping.findForward("lclServiceMap");
    }

    public ActionForward displayDestinationMap(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclMapForm lclMapForm = (LclMapForm) form;
        List<Object[]> destinationList = new PortsDAO().getCountryListforDestination(lclMapForm.getCountryName());
        if (CommonUtils.isNotEmpty(destinationList)) {
            for (Object[] city : destinationList) {
                request.setAttribute("locationName", city[0].toString());
                //countryName = city[1].toString();
                break;
            }
            request.setAttribute("destinationList", destinationList);
        }
        return mapping.findForward("lclServiceMap");
    }

    public ActionForward displayOriginMap(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclMapForm lclMapForm = (LclMapForm) form;
        List<Object[]> originList = new PortsDAO().getCountryListforOrigin(lclMapForm.getCountryName());
        if (CommonUtils.isNotEmpty(originList)) {
            for (Object[] city : originList) {
                /* if (city[1] != null && city[1] != "") {
                request.setAttribute("countryName", "USA");
                } else {*/
                request.setAttribute("locationName", city[0].toString());
                request.setAttribute("countryName", city[5].toString());
                break;
                //  }
            }
            request.setAttribute("destinationList", originList);
        }
        return mapping.findForward("lclServiceMap");
    }
    public ActionForward displayShipToMap(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclMapForm lclMapForm = (LclMapForm) form;
        request.setAttribute("address", request.getParameter("address"));
        if (!lclMapForm.getCountryName().equals("")) {
            List<Object[]> originList = new PortsDAO().getCountryListforOrigin(lclMapForm.getCountryName());
            if (CommonUtils.isNotEmpty(originList)) {
                for (Object[] city : originList) {
                    /* if (city[1] != null && city[1] != "") {
                    request.setAttribute("countryName", "USA");
                    }else { */
                    request.setAttribute("locationName", city[0].toString());
                    request.setAttribute("countryName", city[5].toString());
                    break;
                    // }
                }
                request.setAttribute("destinationList", originList);
            }
        } else {
            request.setAttribute("locationName", request.getParameter("city"));
            request.setAttribute("countryName", request.getParameter("state")+","+request.getParameter("country"));
            List<String[]> originList = new ArrayList<String[]>();
            String[] obj = new String[6];
            obj[0] = request.getParameter("city");
            obj[1] = "";
            obj[2] = request.getParameter("state");
            originList.add(obj);
            request.setAttribute("destinationList", originList);
        }
        return mapping.findForward("lclServiceMap");
    }
}
