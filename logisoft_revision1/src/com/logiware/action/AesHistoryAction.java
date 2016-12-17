package com.logiware.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.AesHistoryDAO;
import com.gp.cong.logisoft.struts.form.AesHistoryForm;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.Action;

public class AesHistoryAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
       AesHistoryForm aesHistoryForm = (AesHistoryForm)form;
            HttpSession session = request.getSession(true);
        if(CommonUtils.isNotEmpty(request.getParameter("fileNumber"))){
            String itn = request.getParameter("fileNumber");
            session.setAttribute("aesHistoryList", new AesHistoryDAO().findByItnNumber(itn));
        }else if("search".equalsIgnoreCase(aesHistoryForm.getButtonValue())){
            session.setAttribute("aesHistoryList", new AesHistoryDAO().searchItnList(aesHistoryForm.getItnNumber(),aesHistoryForm.getFileNumber(),aesHistoryForm.getStatus()));
        }
       return mapping.findForward("success");
    }
}
