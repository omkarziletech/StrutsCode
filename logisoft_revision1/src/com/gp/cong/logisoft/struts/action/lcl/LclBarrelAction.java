/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.logisoft.domain.lcl.CommodityType;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.PackageType;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclBarrelForm;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author lakshh
 */
public class LclBarrelAction extends LogiwareDispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(SUCCESS);
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBarrelForm lclBarrelForm=(LclBarrelForm)form;
        
        LclBookingPiece lclBookingPiece=lclBarrelForm.getLclBookingPiece();
        
        lclBookingPiece.setCommodityType(new CommodityType(new Long(16483)));
        lclBookingPiece.setPackageType(new PackageType(new Long(131)));
        lclBookingPiece.setEnteredBy(getCurrentUser(request));
        lclBookingPiece.setModifiedBy(getCurrentUser(request));
        lclBookingPiece.setEnteredDatetime(new Date());
        lclBookingPiece.setModifiedDatetime(new Date());
        lclBookingPiece.setPersonalEffects("N");
        lclBookingPiece.setLclFileNumber(new LclFileNumber(lclBarrelForm.getFileNumberId()));
        new LclBookingPieceDAO().saveOrUpdate(lclBookingPiece);
        return mapping.findForward(SUCCESS);
    }
}
