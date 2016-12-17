package com.gp.cong.logisoft.struts.action;

import com.gp.cong.logisoft.beans.CodeBean;
import com.gp.cong.logisoft.beans.GenericCodeBean;
import com.gp.cong.logisoft.beans.NoteBean;
import com.gp.cong.logisoft.bo.CGenericCodeBO;
import com.gp.cong.logisoft.domain.AuditLogRecord;
import com.gp.cong.logisoft.domain.AuditLogRecordCodedetails;
import com.gp.cong.logisoft.domain.Codetype;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericcodelabelsDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.struts.form.CodeDetailsForm;
import com.gp.cong.logisoft.util.DBUtil;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CodeDetailsAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        CodeDetailsForm codeDetailsForm = (CodeDetailsForm) form;
        String forwardName = "";
        String buttonValue = codeDetailsForm.getButtonValue();
        HttpSession session = request.getSession(true);
        String codevalue = codeDetailsForm.getCodevalue();
        String codeTypeId = codeDetailsForm.getCodeTypeId();
        String column1 = codeDetailsForm.getColumn1();
        String column2 = codeDetailsForm.getColumn2();
        String column3 = codeDetailsForm.getColumn3();
        String column4 = codeDetailsForm.getColumn4();
        String column5 = codeDetailsForm.getColumn5();
        String column6 = codeDetailsForm.getColumn6();
        String column7 = codeDetailsForm.getColumn7();
        String column8 = codeDetailsForm.getColumn8();
        String column9 = codeDetailsForm.getColumn9();
        String column10 = null != codeDetailsForm.getColumn10()?codeDetailsForm.getColumn10():"";
        String column11 = null != codeDetailsForm.getColumn11()?codeDetailsForm.getColumn11():"";
        String column12 = null != codeDetailsForm.getColumn12()?codeDetailsForm.getColumn12():"";
        String column13 = null != codeDetailsForm.getColumn13()?codeDetailsForm.getColumn13():"";
        String city = codeDetailsForm.getCity();
        DBUtil dbUtil = new DBUtil();
        String msg = "";
        List genericCodeList;
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
        GenericCodeDAO gcdao = new GenericCodeDAO();
         if (buttonValue.equals("save")) {
            GenericCode gc = new GenericCode();
            gcdao = new GenericCodeDAO();
            gc.setCode(codevalue);
            if("39".equals(codeTypeId)){
                gc.setCodedesc(column1);
            }else{
                gc.setCodedesc(column1.toUpperCase());
            }
            gc.setCodetypeid(Integer.parseInt(codeTypeId));
            gc.setField1(column2);
            gc.setField2(column3);
            gc.setField3(column4);
            gc.setField4(column5);
            gc.setField5(column6);
            gc.setField6(column7);
            gc.setField7(column8);
            gc.setField8(column9);
            gc.setField9(column10);
            gc.setField10(column11);
            gc.setField11(column12);
            gc.setField12(column13);
            User userId = null;
            if (session.getAttribute("loginuser") != null) {
                userId = (User) session.getAttribute("loginuser");
            }
            //newly added on 2-1-2008
            gcdao.save(gc, userId.getLoginName());
            if (session.getAttribute("genericCodeList") != null) {
                session.removeAttribute("genericCodeList");
            }
            if (session.getAttribute("gcList") != null) {
                session.removeAttribute("gcList");
            }
            session.setAttribute("codeHeader", new GenericcodelabelsDAO().findForTableHeader(Integer.parseInt(codeTypeId)));
            session.setAttribute("genericCodeFields", genericCodeFields);
            List genericCodeLst = new ArrayList();
            genericCodeLst.add(gc);
            session.setAttribute("gcData", genericCodeLst);
            session.setAttribute("codeData", gcdao.findGenericCodeDatas(null != codeTypeId?Integer.parseInt(codeTypeId):0, "", ""));
            msg = "Code Details Saved Successfully";
            request.setAttribute("message", msg);
            forwardName = "savesuccess";
        }
        GenericCode gc1 = new GenericCode();
        int id = 0;
        if (buttonValue.equals("update") || buttonValue.equals("cancel") || buttonValue.equals("note")) {
            String s = (String.valueOf(session.getAttribute("id")));
            id = (new Integer(s)).intValue();
            gc1 = gcdao.findById(id);
            session.setAttribute("codeHeader", new GenericcodelabelsDAO().findForTableHeader(Integer.parseInt(codeTypeId)));
            session.setAttribute("genericCodeFields", genericCodeFields);
            session.setAttribute("codeData", gcdao.findGenericCodeDatas(null != codeTypeId?Integer.parseInt(codeTypeId):0, "", ""));
        }
        if (buttonValue.equals("update")) {
            List gcList = new ArrayList();
            List<GenericCodeBean> gcData = new ArrayList<GenericCodeBean>();
            GenericCodeBean gc2 = null;
            int j = 0;
            if (session.getAttribute("genericCodeList") != null) {
                gcList = (List) session.getAttribute("genericCodeList");
            } else if (session.getAttribute("gcdt") != null) {
                gcList = (List) session.getAttribute("gcdt");
            } else if (session.getAttribute("gcList") != null) {
                gcList = (List) session.getAttribute("gcList");
            }
            User userid = null;
            if (session.getAttribute("loginuser") != null) {
                userid = (User) session.getAttribute("loginuser");
            }
            gc1.setId(id);
            gc1.setCode(codevalue);
            List list = gcdao.findByExample(gc1);
            gc1.setCodetypeid(gc1.getCodetypeid());
            if("39".equals(codeTypeId)){
                gc1.setCodedesc(column1);
            }else{
                gc1.setCodedesc(column1.toUpperCase());
            }
            gc1.setField1(column2);
            gc1.setField2(column3);
            gc1.setField3(column4);
            gc1.setField4(column5);
            gc1.setField5(column6);
            gc1.setField6(column7);
            gc1.setField7(column8);
            gc1.setField8(column9);
            gc1.setField9(column10);
            gc1.setField10(column11);
            gc1.setField11(column12);
            gc1.setField12(column13);
//            if (gc1.getId() != null) {
//                gcdao.auditupdate(gc1, userid.getLoginName());
//            }
            for (j = 0; j < gcList.size(); j++) {
                gc2 = (GenericCodeBean) gcList.get(j);
                if (gc2.getId().equals(gc1.getId())) {
                    gc2.setCode(gc1.getCode());
                    gc2.setCodedesc(gc1.getCodedesc());
                    gc2.setCodeTypeId(gc1.getCodetypeid());
                    gc2.setField1(gc1.getField1());
                    gc2.setField2(gc1.getField2());
                    gc2.setField3(gc1.getField3());
                    gc2.setField4(gc1.getField4());
                    gc2.setField5(gc1.getField5());
                    gc2.setField6(gc1.getField6());
                    gc2.setField7(gc1.getField7());
                    gc2.setField8(gc1.getField8());
                    gc2.setField9(gc1.getField9());
                    gc2.setField10(gc1.getField10());
                    gc2.setField11(gc1.getField11());
                    gc2.setField12(gc1.getField12());
                    gcdao.update(gc2);
                }
            }
            String programid = null;
            programid = (String) session.getAttribute("processinfoforcodedetails");
            String recordid = gc1.getId().toString();
            dbUtil.getProcessInfo(programid, recordid, "edited", "deleted");
            msg = "Code Details Updated Successfully";
            request.setAttribute("message", msg);
            request.setAttribute("code", gc1.getCodetypeid());
            request.setAttribute("buttonValue", buttonValue);
            gcData.add(gc2);
            if (session.getAttribute("genericCodeList") != null) {
                session.removeAttribute("genericCodeList");
            }
            if (session.getAttribute("gcList") != null) {
                session.removeAttribute("gcList");
            }
            session.setAttribute("gcData", gcData);
            session.setAttribute("codeData", gcdao.findGenericCodeDatas(gc1.getCodetypeid(), "", ""));
            forwardName = "updatesuccess";
        }

        if (buttonValue.equals("delete")) {

            GenericCode gc = new GenericCode();
            String s = (String.valueOf(session.getAttribute("id")));
            int id1 = (new Integer(s)).intValue();
            GenericCodeDAO gcdao1 = new GenericCodeDAO();
            gc = gcdao1.findById(id1);
            User userId = null;
            if (session.getAttribute("loginuser") != null) {
                userId = (User) session.getAttribute("loginuser");
            }
            gcdao.delete(gc, userId.getLoginName());
            msg = "Code Details Deleted Successfully";
            request.setAttribute("message", msg);
            forwardName = "deletesuccess";
        }
        if (buttonValue.equals("codeselected")) {

            int i = Integer.parseInt(request.getParameter("code"));
            if (i == 1) {
                session.setAttribute("column2", "US");
            }
            DBUtil db = new DBUtil();
            session.setAttribute("codeDetails", db.getCodesDetails(i));
            String str = new Integer(i).toString();
            session.setAttribute("code", str);
            forwardName = "success";


        }
        if (buttonValue.equals("cancel")) {
            String programid = null;
            programid = (String) session.getAttribute("processinfoforcodedetails");
            String recordid = gc1.getId().toString();
            dbUtil.getProcessInfo(programid, recordid, "editcancelled", null);
            forwardName = "updatesuccess";

        }
        if (buttonValue.equals("newcancel")) {
            forwardName = "savesuccess";

        }
        if (buttonValue.equals("note")) {
            ItemDAO itemDAO = new ItemDAO();
            Item item = new Item();
            String itemName = "";
            if (session.getAttribute("processinfoforcodedetails") != null) {
                String itemId = (String) session.getAttribute("processinfoforcodedetails");
                item = itemDAO.findById(Integer.parseInt(itemId));
                CodetypeDAO cdao = new CodetypeDAO();
                Codetype ct = cdao.findById(gc1.getCodetypeid());
                itemName = ct.getDescription();
            }
            AuditLogRecord auditLogRecord = new AuditLogRecordCodedetails();
            NoteBean noteBean = new NoteBean();
            noteBean.setItemName(itemName);
            noteBean.setAuditLogRecord(auditLogRecord);
            noteBean.setButtonValue(buttonValue);
            //noteBean.setUser(user);
            noteBean.setPageName("cancelcode");
            String noteId = "";
            if (gc1.getCode() != null && !gc1.getCode().equals("")) {
                noteId = gc1.getId().toString();
                noteBean.setNoteId(noteId);
                noteBean.setReferenceId(gc1.getCode());
            }
            List auditList = null;
            auditList = dbUtil.getNoteInformation(noteId, auditLogRecord);
            noteBean.setAuditList(auditList);
            noteBean.setVoidednote("");
            request.setAttribute("noteBean", noteBean);
            forwardName = "note";

        }
       
        if (buttonValue.equals("countryselected")) {

            Integer i = 0;
            if (session.getAttribute("code") != null) {
                i = Integer.parseInt((String) session.getAttribute("code"));
            }

            String str = i.toString();
            session.setAttribute("code", str);
            DBUtil db = new DBUtil();
            session.setAttribute("codeDetails", db.getCodesDetails(i));
            GenericCode gc = new GenericCode();
            gc.setCode(column2);
            List<GenericCode> countryList = gcdao.findByExample(gc);

            GenericCode countryId = countryList.get(0);
            request.setAttribute("cityList", dbUtil.getCityList(countryId));
            request.setAttribute("column1", column1);
            session.setAttribute("column2", column2);
            request.setAttribute("column3", column3);
            request.setAttribute("column4", column4);
            request.setAttribute("column5", column5);
            request.setAttribute("column6", column6);
            request.setAttribute("column7", column7);
            request.setAttribute("column8", column8);
            request.setAttribute("column9", column9);
            request.setAttribute("column10", column10);
            request.setAttribute("column11", column11);
            request.setAttribute("column12", column12);
            request.setAttribute("column13", column13);
            request.setAttribute("codevalue", codevalue);
            forwardName = "success";
        }
        if (buttonValue.equals("cityselected")) {

            Integer i = 0;
            if (session.getAttribute("code") != null) {
                i = Integer.parseInt((String) session.getAttribute("code"));
            }
            String str = i.toString();
            session.setAttribute("code", str);
            DBUtil db = new DBUtil();
            session.setAttribute("codeDetails", db.getCodesDetails(i));
            request.setAttribute("state", dbUtil.getStateByCity(column3));
            request.setAttribute("column1", column1);
            session.setAttribute("column2", column2);
            request.setAttribute("column3", column3);
            request.setAttribute("column4", column4);
            request.setAttribute("column5", column5);
            request.setAttribute("column6", column6);
            request.setAttribute("column7", column7);
            request.setAttribute("column8", column8);
            request.setAttribute("column9", column9);
            request.setAttribute("column10", column10);
            request.setAttribute("column11", column11);
            request.setAttribute("column12", column12);
            request.setAttribute("column13", column13);
            request.setAttribute("codevalue", codevalue);
            forwardName = "success";
        }
        if (buttonValue.equals("editcityselected")) {

            String s = (String.valueOf(session.getAttribute("id")));
            id = (new Integer(s)).intValue();

            DBUtil db = new DBUtil();
            session.setAttribute("codeDetails", db.getCodesDetails(gcdao.findById(id).getCodetypeid()));
            List<String> editDetailsList = (List) session.getAttribute("editDetailsList");
            GenericCode gc = new GenericCode();
            gc.setCode(column2);
            List<GenericCode> countryList = gcdao.findByExample(gc);
            GenericCode countryId = countryList.get(0);
            request.setAttribute("cityList", dbUtil.getCityList(countryId));
            column4 = dbUtil.getStateByCity(column3);
            editDetailsList.add(0, codevalue);
            editDetailsList.add(1, column1);
            editDetailsList.add(2, column2);
            editDetailsList.add(3, column3);
            editDetailsList.add(4, column4);
            editDetailsList.add(5, column5);
            editDetailsList.add(6, column6);
            editDetailsList.add(7, column7);
            editDetailsList.add(8, column8);
            editDetailsList.add(9, column9);
            editDetailsList.add(10, column10);
            editDetailsList.add(11, column11);
            editDetailsList.add(12, column12);
            editDetailsList.add(13, column13);
            session.setAttribute("editDetailsList", editDetailsList);
            forwardName = "success";

        }
        if (buttonValue.equals("editcountry")) {
            String s = (String.valueOf(session.getAttribute("id")));
            id = (new Integer(s)).intValue();
            String str = new Integer(id).toString();
            session.setAttribute("code", str);
            DBUtil db = new DBUtil();
            session.setAttribute("codeDetails", db.getCodesDetails(id));
            GenericCode gc = new GenericCode();
            gc.setCode(column2);
            List<GenericCode> countryList = gcdao.findByExample(gc);

            GenericCode countryId = countryList.get(0);
            request.setAttribute("cityList", dbUtil.getCityList(countryId));

            List<String> editDetailsList = (List) session.getAttribute("editDetailsList");
            editDetailsList.add(0, codevalue);
            editDetailsList.add(1, column1);
            editDetailsList.add(2, column2);
            editDetailsList.add(3, "0");
            editDetailsList.add(4, "");
            editDetailsList.add(5, column5);
            editDetailsList.add(6, column6);
            editDetailsList.add(7, column7);
            editDetailsList.add(8, column8);
            editDetailsList.add(9, column9);
            editDetailsList.add(10, column10);
            editDetailsList.add(11, column11);
            editDetailsList.add(12, column12);
            editDetailsList.add(13, column13);
            session.setAttribute("editDetailsList", editDetailsList);
            forwardName = "success";
        }
        if (session.getAttribute("genericCodeList") != null) {
            CGenericCodeBO objGenericBO = new CGenericCodeBO();
            genericCodeList = objGenericBO.getGenericCodesInfo();
            session.setAttribute("genericCodeList", genericCodeList);
        }
        if (session.getAttribute("codeBean") != null) {
            CodeBean cBean = (CodeBean) session.getAttribute("codeBean");
            List codeData = gcdao.findforTableData(Integer.parseInt(cBean.getCodeTypeId()), cBean.getCodeValue(), cBean.getCodeDesc());
            GenericCodeBean gcbean = new GenericCodeBean();
            List gcData = gcbean.getDetails(codeData);

            session.setAttribute("gcData", gcData);
        }
        request.setAttribute("code", codeTypeId);
        return mapping.findForward(forwardName);
    }
}