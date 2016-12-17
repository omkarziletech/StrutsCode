package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.lcl.LclUnitWhse;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitWhseDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclEditWarehouseForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LclEditWarehouseAction extends LogiwareDispatchAction {

    private static String EDIT_WAREHOUSE = "editWarehouse";

    public ActionForward editWarehouse(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclEditWarehouseForm lclEditWarehouseForm = (LclEditWarehouseForm) form;
        LclUnitWhseDAO lclunitwhsedao = new LclUnitWhseDAO();
        LclUnitWhse lclUnitWhse = null;
        if (CommonUtils.isNotEmpty(lclEditWarehouseForm.getUnitWarehouseId())) {
            lclUnitWhse = lclunitwhsedao.findById(lclEditWarehouseForm.getUnitWarehouseId());
            if (lclUnitWhse.getArrivedDatetime() != null) {
                lclEditWarehouseForm.setArrivedDateTime(DateUtils.formatDate(lclUnitWhse.getArrivedDatetime(), "dd-MMM-yyyy hh:mm"));
            }
            if (lclUnitWhse.getDepartedDatetime() != null) {
                lclEditWarehouseForm.setDepartedDateTime(DateUtils.formatDate(lclUnitWhse.getDepartedDatetime(), "dd-MMM-yyyy hh:mm"));
            }
            if (CommonUtils.isNotEmpty(lclUnitWhse.getLocation())) {
                lclEditWarehouseForm.setLocation(lclUnitWhse.getLocation());
            }
            if (lclUnitWhse.getWarehouse() != null) {
                lclEditWarehouseForm.setWarehouseName(lclUnitWhse.getWarehouse().getWarehouseName());
                lclEditWarehouseForm.setWarehouseId(lclUnitWhse.getWarehouse().getId());
                request.setAttribute("warehouseNumber", lclUnitWhse.getWarehouse().getWarehouseNo());
            }
            if (lclUnitWhse.getSealNoOut() != null) {
                lclEditWarehouseForm.setSealNoOut(lclUnitWhse.getSealNoOut());
            }
            request.setAttribute("sealNumber", lclUnitWhse.getSealNoOut());
        }
        request.setAttribute("lclEditWarehouseForm", lclEditWarehouseForm);
        return mapping.findForward(EDIT_WAREHOUSE);
    }

    public ActionForward editImpWarehouse(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclEditWarehouseForm lclEditWarehouseForm = (LclEditWarehouseForm) form;
        String wareHsNo = request.getParameter("wareHsNo");
        request.setAttribute("lclEditWarehouseForm", lclEditWarehouseForm);
        request.setAttribute("wareHsNo", wareHsNo);
        return mapping.findForward("editImpWarehouse");
    }
}
