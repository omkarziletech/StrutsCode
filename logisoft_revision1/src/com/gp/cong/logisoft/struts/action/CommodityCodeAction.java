/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action;

import com.gp.cong.logisoft.domain.lcl.CommodityType;
import com.gp.cong.logisoft.hibernate.dao.lcl.commodityTypeDAO;
import com.gp.cong.logisoft.struts.action.lcl.LogiwareDispatchAction;
import com.gp.cvst.logisoft.struts.form.lcl.CommodityCodeForm;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Administrator
 */
public class CommodityCodeAction extends LogiwareDispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List commodityList = new commodityTypeDAO().getCommodityList(null);
        request.setAttribute("commodityList", commodityList);
        return mapping.findForward("commodityCode");
    }

    public ActionForward editCommodity(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CommodityCodeForm commodityCodeForm = (CommodityCodeForm) form;
        commodityCodeForm.setId(commodityCodeForm.getId());
        commodityCodeForm.setCode(commodityCodeForm.getCode());
        commodityCodeForm.setActive(commodityCodeForm.getActive());
        commodityCodeForm.setDescEn(commodityCodeForm.getDescEn());
        commodityCodeForm.setHazmat(commodityCodeForm.getHazmat());
        commodityCodeForm.setHighVolumeDiscount(commodityCodeForm.getHighVolumeDiscount());
        commodityCodeForm.setRefrigerationRequired(commodityCodeForm.getRefrigerationRequired());
        commodityCodeForm.setDefaultErt(commodityCodeForm.getDefaultErt());
        commodityCodeForm.setRemarks(commodityCodeForm.getRemarks());
        request.setAttribute("commodityCodeForm", commodityCodeForm);
        return mapping.findForward("editCommodity");
    }

    public ActionForward saveCommodity(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CommodityCodeForm commodityCodeForm = (CommodityCodeForm) form;
        commodityTypeDAO commTypeDAO = new commodityTypeDAO();
        CommodityType commType = new CommodityType();
        commType = commTypeDAO.getByProperty("code", commodityCodeForm.getCode());
        commType.setActive(commodityCodeForm.getActive());
        commType.setHazmat(commodityCodeForm.getHazmat());
        commType.setRefrigerationRequired(commodityCodeForm.getRefrigerationRequired());
        commType.setHighVolumeDiscount(commodityCodeForm.getHighVolumeDiscount());
        commType.setDefaultErt(commodityCodeForm.getDefaultErt());
        commType.setRemarks(commodityCodeForm.getRemarks());
        commTypeDAO.update(commType);
        String message ="Commodity details updated successfully";
        request.setAttribute("message", message);
        return mapping.findForward("editCommodity");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CommodityCodeForm commodityCodeForm = (CommodityCodeForm) form;
        List commodityList = new commodityTypeDAO().getCommodityList(commodityCodeForm.getCode());
        request.setAttribute("code", commodityCodeForm.getCode());
        request.setAttribute("description", commodityCodeForm.getDescEn());
        request.setAttribute("commodityList", commodityList);
        return mapping.findForward("commodityCode");
    }
}
