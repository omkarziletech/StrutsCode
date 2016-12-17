package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.accounting.CheckRegisterBC;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cvst.logisoft.struts.form.CheckRegisterForm;
import com.gp.cvst.logisoft.util.DBUtil;
import com.oreilly.servlet.ServletUtils;
import org.apache.commons.io.FilenameUtils;

public class CheckRegisterAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CheckRegisterForm checkRegisterForm = (CheckRegisterForm) form;
        String forwardName = "success";
        if (CommonUtils.isEqualIgnoreCase(checkRegisterForm.getButtonValue(), "exportToExcel")) {
            String excelFileName = new CheckRegisterBC().exportToExcel(checkRegisterForm);
            if (CommonUtils.isNotEmpty(excelFileName)) {
                response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(excelFileName));
                response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
                ServletUtils.returnFile(excelFileName, response.getOutputStream());
                return null;
            }
        }
        request.setAttribute("checkType", new DBUtil().checkType());
        request.setAttribute("creditTermList", new DBUtil().getGenericCodeList(29, "yes", "Select Credit Terms"));
        return mapping.findForward(forwardName);
    }
}
