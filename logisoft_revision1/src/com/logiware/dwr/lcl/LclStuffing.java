/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logiware.dwr.lcl;

import com.gp.cong.logisoft.domain.lcl.LclBooking;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.directwebremoting.WebContextFactory;

/**
 *
 * @author Ram
 */
public class LclStuffing {

    public int stuffingDR(Integer unitID, String unitVolume, String fileNo)throws Exception {
        int containerPrecentage=0;
            HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
            HttpSession session = request.getSession();
            List<LclBooking> stuffedlist = (List<LclBooking>)session.getAttribute("stuffedList");
            if(null == stuffedlist){ stuffedlist=new ArrayList<LclBooking>();}
            List<LclBooking> bookinglist = (List<LclBooking>)session.getAttribute("destuffedList");
            if(null == bookinglist){ stuffedlist=new ArrayList<LclBooking>();}
        return containerPrecentage;
    }

}
