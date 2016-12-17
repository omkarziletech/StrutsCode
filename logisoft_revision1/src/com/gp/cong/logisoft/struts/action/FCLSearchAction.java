/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action;

import com.gp.cong.logisoft.hibernate.dao.FCLSearchDAO;
import com.gp.cong.logisoft.struts.form.FCLSearchForm;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Vinay
 */
public class FCLSearchAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	FCLSearchForm fsf = (FCLSearchForm) form;
	FCLSearchDAO fsd = new FCLSearchDAO(fsf);
	String forward = "fclDataSearch";
	try {
	    if (fsf.getAction().equals("update")) {
		request.setAttribute("searchResults", fsd.search());
	    } else if (fsf.getAction().equals("get")) {
		forward = "editFCLData";
		request.setAttribute("currentFCLData", fsd.getFCLData(fsf.getId()));
	    } else if (fsf.getAction().equals("edit")) {
		fsd.update();
		request.setAttribute("searchResults", fsd.search());
	    }
	} catch (Exception e) {
	    throw e;
	}
	return mapping.findForward(forward);
    }
}
