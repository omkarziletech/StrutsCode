/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cvst.logisoft.domain.SegmentValues;
import com.gp.cvst.logisoft.hibernate.dao.SegmentDAO;
import com.gp.cvst.logisoft.hibernate.dao.SegmentValuesDAO;
import com.gp.cvst.logisoft.struts.form.addForm;

/** 
 * MyEclipse Struts
 * Creation date: 05-08-2008
 * 
 * XDoclet definition:
 * @struts.action path="/add" name="AddForm" input="/jsps/Accounting/AddValuePopup.jsp" scope="request"
 */
public class addAction extends Action {
    /*
     * Generated Methods
     */

    /**
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        addForm addSegmentForm = (addForm) form;
        HttpSession session = ((HttpServletRequest) request).getSession();
        SegmentValuesDAO segvalDAO = new SegmentValuesDAO();
        SegmentDAO segmentdao = new SegmentDAO();
        SegmentValues segmentdomain = new SegmentValues();
        List segList = new ArrayList();
        String forwardName = "";
        String buttonValue = addSegmentForm.getButtonValue();
        String addsegcode = addSegmentForm.getAddcode();
        String addsegdesc = addSegmentForm.getAdddesc();
        String segcodeid = addSegmentForm.getSegcodeId();
        String id = addSegmentForm.getId();
        String segcode = addSegmentForm.getSegcode();
        int segvalId = Integer.parseInt(id);
        String segid = "";
        if (buttonValue.equals("segmentValues")) {
            segid = segmentdao.selectAccountCodeid(segvalId, segcode);
            segmentdomain.setSegmentValueDesc(addsegdesc);
            segmentdomain.setSegmentValue(addsegcode);
            segmentdomain.setSegmentCodeId(segcodeid);
            segmentdomain.setSegmentCodeId(segid);
            segList.add(segmentdomain);
            List segcodes = (List) segvalDAO.comparevalue(segid);
            if (!segcodes.contains(addsegcode)) {
                segvalDAO.saveSegmentValues(segmentdomain);
            } else {

                String msg = "Please enter different Segment Value, This Segment Value already exists";
                request.setAttribute("message", msg);
            }
            forwardName = "success";
        }
        session.setAttribute("segList", segList);
        return mapping.findForward(forwardName);
    }
}
	