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

import com.gp.cvst.logisoft.domain.AcctStructure;
import com.gp.cvst.logisoft.domain.Segment;
import com.gp.cvst.logisoft.hibernate.dao.AcctStructureDAO;
import com.gp.cvst.logisoft.hibernate.dao.SegmentDAO;
import com.gp.cvst.logisoft.struts.form.addSegmentForm;

/** 
 * MyEclipse Struts
 * Creation date: 04-30-2008
 * 
 * XDoclet definition:
 * @struts.action path="/addSegment" name="addSegmentForm" input="/jsps/Accounting/AddSegmentPopUp.jsp" scope="request" validate="true"
 */
public class AddSegmentAction extends Action {
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
        //		 TODO Auto-generated method stub
        addSegmentForm addSegmentForm = (addSegmentForm) form;
        HttpSession session = ((HttpServletRequest) request).getSession();
        SegmentDAO segDAO = new SegmentDAO();
        Segment segmentdomain = new Segment();

        String forwardName = "";
        String buttonValue = addSegmentForm.getButtonValue();
        String addsegcode = addSegmentForm.getAddSegmentcCode();
        String addsegdesc = addSegmentForm.getAddSegmentDesc();
        int addseglength = addSegmentForm.getAddSegmentLength();
        String validateList = addSegmentForm.getValidateList();
        String id = addSegmentForm.getId();
        String asId = addSegmentForm.getAsId();

        request.setAttribute("asId2", asId);
        List addSegmentList = new ArrayList();
        List addseglist = null;



        if (buttonValue.equals("AddSegStructure")) {
            int asId1 = Integer.parseInt(asId);
            request.setAttribute("asId1", asId1);
            if (session.getAttribute("segmentdomain") == null) {
                segmentdomain = new Segment();
            } else {
                segmentdomain = (Segment) session.getAttribute("segmentdomain");
            }
            String acctId = (String) segDAO.countAcctStru(asId1);

            int actid = 0;
            if (acctId != null) {

                actid = Integer.parseInt(acctId);
            }

            if (actid < 5) {

                segmentdomain.setSegmentCode(addsegcode);
                segmentdomain.setSegmentDesc(addsegdesc);
                segmentdomain.setSegment_leng(addseglength);
                AcctStructure as = new AcctStructure();
                AcctStructureDAO asdao = new AcctStructureDAO();
                as = asdao.findById(asId1);
                segmentdomain.setAccount_structure_Id(as);
                if (validateList == null || !validateList.equals("Y")) {
                    validateList = "N";
                }
                segmentdomain.setValidateList(validateList);
                session.setAttribute("acctstuctureid", asId1);
                addSegmentList.add(segmentdomain);
                request.setAttribute("segmentdomainlist", addSegmentList);
                session.setAttribute("segmentdomain", segmentdomain);

                segDAO.save(segmentdomain);

                addseglist = (List) segDAO.findForShow(asId);
                session.setAttribute("addseglist", addseglist);
            } else {
                String Message = "please dont add more than  5 Segments";
                request.setAttribute("msg", Message);
            }
            forwardName = "success";
        }
        if (buttonValue.equals("close")) {
            request.setAttribute("buttonValue", "completed");
            forwardName = "success";
        }
        return mapping.findForward(forwardName);
    }
}
	