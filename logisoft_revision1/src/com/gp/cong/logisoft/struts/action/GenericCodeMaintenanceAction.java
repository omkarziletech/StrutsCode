/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.ratemanagement.GenericCodeBC;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Genericcodelabels;
import com.gp.cong.logisoft.domain.ProcessInfo;
import com.gp.cong.logisoft.domain.User;

import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericcodelabelsDAO;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.struts.form.GenericCodeMaintenanceForm;
import com.gp.cong.logisoft.util.DBUtil;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;

/**
 * MyEclipse Struts Creation date: 11-08-2004
 * 
 * XDoclet definition:
 * 
 * @struts.action input="/jsps/datareference/GenericCodeMaintenance.jsp"
 *                validate="true"
 * @struts.action-forward name="search"
 *                        path="/jsps/datareference/GenericCodeMaintenance.jsp"
 * @struts.action-forward name="showall"
 *                        path="/jsps/datareference/GenericCodeMaintenance.jsp"
 */
public class GenericCodeMaintenanceAction extends Action {
    /*
     * Generated Methods
     */

    /**
     * Method execute
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        GenericCodeMaintenanceForm genericCodeMaintenanceForm = (GenericCodeMaintenanceForm) form;
        String code = genericCodeMaintenanceForm.getCodeTypeId();
        HttpSession session = request.getSession();
        String action = request.getParameter("buttonValue");
        String codeValue = request.getParameter("codeValue");
        String codeDesc = request.getParameter("codeDesc");
        String forward = "search";
        String message = "";
        List genericCodeFields = new ArrayList();
        genericCodeFields.add("code");
        genericCodeFields.add("codedesc");
        genericCodeFields.add("field1");
        genericCodeFields.add("field2");
        genericCodeFields.add("field3");
        genericCodeFields.add("field4");
        genericCodeFields.add("field5");
        genericCodeFields.add("field6");
        genericCodeFields.add("field7");
        genericCodeFields.add("field8");
        genericCodeFields.add("field9");
        genericCodeFields.add("field10");
        genericCodeFields.add("field11");
        genericCodeFields.add("field12");
        if(CommonConstants.ADD_ACTION.equals(action)) {
           forward = "addnew";
        }else if(CommonUtils.isNotEmpty(request.getParameter("paramId"))) {
            int id = Integer.parseInt(request.getParameter("paramId"));
            GenericCodeDAO gcdao = new GenericCodeDAO();
            GenericCode editDetails = new GenericCode();
            editDetails = gcdao.findById(id);
//            String view = "3";
//            session.setAttribute("view", view);
            session.setAttribute("editDetails", editDetails);
            List editDetailsList = new ArrayList();
            code = String.valueOf(editDetails.getCodetypeid());
            if (code.equals("1")) {
                GenericCode gc = gcdao.findById(id);
                DBUtil dbUtil = new DBUtil();
                GenericCode gc1 = new GenericCode();
                gc1.setCode(gc.getField1());
                List<GenericCode> countryList = gcdao.findByExample(gc1);

                if (countryList != null && countryList.size() > 0) {
                    GenericCode countryId = countryList.get(0);
                    request.setAttribute("cityList", dbUtil.getCityList(countryId));
                }
            }
            editDetailsList.add(editDetails.getCode());
            editDetailsList.add(editDetails.getCodedesc());
            editDetailsList.add(editDetails.getField1());
            editDetailsList.add(editDetails.getField2());
            editDetailsList.add(editDetails.getField3());
            editDetailsList.add(editDetails.getField4());
            editDetailsList.add(editDetails.getField5());
            editDetailsList.add(editDetails.getField6());
            editDetailsList.add(editDetails.getField7());
            editDetailsList.add(editDetails.getField8());
            editDetailsList.add(editDetails.getField9());
            editDetailsList.add(editDetails.getField10());
            editDetailsList.add(editDetails.getField11());
            editDetailsList.add(editDetails.getField12());
            session.setAttribute("editDetailsList", editDetailsList);
            DBUtil db = new DBUtil();
            session.setAttribute("codeDetails", db.getCodesDetails(Integer.parseInt(code)));
            request.setAttribute("edit", "yes");
            request.setAttribute("code", code);
            session.setAttribute("id", id);
            forward = "editcode";
        }else if(CommonUtils.isNotEmpty(request.getParameter("codeId"))){
            UserDAO user1 = new UserDAO();
            int id = Integer.parseInt(request.getParameter("codeId"));
            GenericCodeDAO gcdao = new GenericCodeDAO();
            GenericCode editDetails = new GenericCode();
            editDetails = gcdao.findById(id);

            User userid = null;
            if (session.getAttribute("loginuser") != null) {
                userid = (User) session.getAttribute("loginuser");

            }
            ProcessInfoDAO processinfoDAO = new ProcessInfoDAO();
            ProcessInfo pi = new ProcessInfo();
            String programid = null;
            programid = (String) session.getAttribute("processinfoforcodedetails");
//            String recordid = editDetails.getId().toString();
//            String editstatus = "startedited";
//            String deletestatus = "startdeleted";
//            ProcessInfo processinfoobj = processinfoDAO.findById(Integer.parseInt(programid), recordid, deletestatus, editstatus);
//            if (processinfoobj != null) {
//                String view = "3";
//                User loginuser = user1.findById(processinfoobj.getUserid());
//                loginname = loginuser.getLoginName();
//                msg = "This record is being used by ";
//                message = msg + loginname;
//                request.setAttribute("message", message);
//                session.setAttribute("view", view);
//                forward = "edituser";
//
//            } else {
//                pi.setUserid(userid.getUserId());
//                pi.setProgramid(Integer.parseInt(programid));
//                java.util.Date currdate = new java.util.Date();
//                pi.setProcessinfodate(currdate);
//                pi.setEditstatus(editstatus);
//                pi.setRecordid(recordid);
//                processinfoDAO.save(pi);
//                if (session.getAttribute("view") != null) {
//                    session.removeAttribute("view");
//                }
//            }
            request.setAttribute("user", userid);
            session.setAttribute("editDetails", editDetails);
            List editDetailsList = new ArrayList();
            code = String.valueOf(editDetails.getCodetypeid());
            if (code.equals("1")) {
                GenericCode gc = gcdao.findById(id);
                DBUtil dbUtil = new DBUtil();
                GenericCode gc1 = new GenericCode();
                gc1.setCode(gc.getField1());
                List<GenericCode> countryList = gcdao.findByExample(gc1);

                GenericCode countryId = new GenericCode();
                if (countryList != null && countryList.size() > 0) {
                    countryId = countryList.get(0);
                    request.setAttribute("cityList", dbUtil.getCityList(countryId));
                }

            }
            editDetailsList.add(editDetails.getCode());
            editDetailsList.add(editDetails.getCodedesc());
            editDetailsList.add(editDetails.getField1());
            editDetailsList.add(editDetails.getField2());
            editDetailsList.add(editDetails.getField3());
            editDetailsList.add(editDetails.getField4());
            editDetailsList.add(editDetails.getField5());
            editDetailsList.add(editDetails.getField6());
            editDetailsList.add(editDetails.getField7());
            editDetailsList.add(editDetails.getField8());
            editDetailsList.add(editDetails.getField9());
            editDetailsList.add(editDetails.getField10());
            editDetailsList.add(editDetails.getField11());
            editDetailsList.add(editDetails.getField12());
            session.setAttribute("editDetailsList", editDetailsList);
            DBUtil db = new DBUtil();
            session.setAttribute("codeDetails", db.getCodesDetails(Integer.parseInt(code)));
            session.setAttribute("id", id);
            request.setAttribute("code", code);
            request.setAttribute("view", "3");
            forward = "editcode";
        }else if("search".equals(action)) {
            int codeTypeId = Integer.parseInt(genericCodeMaintenanceForm.getCodeTypeId());
            
            GenericCodeDAO gcdao = new GenericCodeDAO();
            GenericcodelabelsDAO gcldao = new GenericcodelabelsDAO();
            List<Genericcodelabels> codeHeader = null;
            GenericCodeBC genericCodeBC = new GenericCodeBC();
            List codeData = null;
            List gcData = null;
                if("0".equals(code)){
                     codeData = gcdao.findGenericSearchData(codeTypeId, codeValue, codeDesc);
                     //codeData = genericCodeBC.getDetails(gcData);
                }else{
                     codeHeader = gcldao.findForTableHeader(codeTypeId);
                     codeData = gcdao.findGenericCodeDatas(codeTypeId, codeValue, codeDesc);
                     session.setAttribute("genericCodeFields", genericCodeFields);
                     session.setAttribute("codeHeader", codeHeader);
                }
                session.setAttribute("codeData", codeData);
            //---TO GET THE CODETYPE DESCRIPTION and CHECK FOR BLUESCREEN RECORDS-------
            String codeTypeDescription=null,fromBluescreen=null;   
            List<Object[]> descriptionList=new CodetypeDAO().getCodeTypeDescription(Integer.toString(codeTypeId));
            if(descriptionList.size()>0){
            	for(Object[] object :descriptionList){
            		codeTypeDescription=object[0].toString();
            		fromBluescreen=(null!=object[1])?object[1].toString():"";
            		if(fromBluescreen.equalsIgnoreCase("Y")){
            			message="These records are from Blue Screen";
            		}
            	}
            }
            session.setAttribute("codeTypeDescription",codeTypeDescription);
            request.setAttribute("message", message);
            request.setAttribute("code", code);
            forward = "search";
        }else if("delete".equals(action)) {
            if(CommonUtils.isNotEmpty(genericCodeMaintenanceForm.getGenericCodeId())){
                int id = Integer.parseInt(genericCodeMaintenanceForm.getGenericCodeId());
                GenericCode deleteDetails = new GenericCodeDAO().findById(id);
                int codeId  = deleteDetails.getCodetypeid();
                new GenericCodeDAO().delete(deleteDetails);
                session.setAttribute("codeHeader", new GenericcodelabelsDAO().findForTableHeader(codeId));
                session.setAttribute("genericCodeFields", genericCodeFields);
                session.setAttribute("codeData", new GenericCodeDAO().findGenericCode(codeId, "", ""));
                request.setAttribute("code", codeId);
            }
            //get all editable codetype
            forward = "search";
        }else if(null != request.getParameter("param")){
            //get all editable codetype
            forward = "search";
        }
        if(null == action){
            session.removeAttribute("codeData");
        }
        DBUtil dbUtil=new DBUtil();
        session.setAttribute(CommonConstants.CODE_TYPE_LIST,dbUtil.getCodesList());
        request.setAttribute("generic", new GenericCode());
        request.setAttribute("buttonValue", action);
        return mapping.findForward(forward);
    }
}