package com.gp.cong.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import com.gp.cong.logisoft.beans.NoteBean;
import com.gp.cong.logisoft.beans.SearchUserBean;
import com.gp.cong.logisoft.domain.AuditLogRecord;
import com.gp.cong.logisoft.domain.AuditLogRecordUser;
import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.domain.Role;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.UserPrinterAssociation;
import com.gp.cong.logisoft.hibernate.dao.CustomerDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.hibernate.dao.RoleDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSearchTemplateDAO;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cong.logisoft.struts.form.EditUserForm;

public class EditUserAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        EditUserForm editUserForm = (EditUserForm) form;// TODO Auto-generated method stub
        String firstName = editUserForm.getFirstName();
        String lastName = editUserForm.getLastName();
        String packageType = editUserForm.getCtsPackageType();
        String palletType = editUserForm.getCtsPalletType();
        String telephone = editUserForm.getTelephone();
        String address1 = editUserForm.getAddress1();
        String address2 = editUserForm.getAddress2();
        String city = editUserForm.getCity();
        String zipcode = editUserForm.getZipCode();
        String loginName = editUserForm.getLoginName();
        String roleId = editUserForm.getRole();

        String extension = editUserForm.getExtension();
        String fax = editUserForm.getFax();
        String terminal = editUserForm.getTerminal();
        String impterminal = editUserForm.getImportTerminal();
        String billTerminal = editUserForm.getBillingTerminal();
        String officeCityLocation = editUserForm.getOfficeCityLocation();
        Date userCreatedDate = new Date(System.currentTimeMillis());
        String password = editUserForm.getPassword();
        String status = editUserForm.getStatus();
        String email = editUserForm.getEmail();
        String outsourceEmail = editUserForm.getOutsourceEmail();
        String diffLclBookedDimsActual = editUserForm.getDifflclBookedDimsActual();
        String templateId = editUserForm.getTemplateId();
        String message = "";
        String buttonValue = editUserForm.getButtonValue();
        User user = (User) session.getAttribute("user");
        User loginUser = (User) session.getAttribute("loginuser");
        String forwardName = "edituser";
        DBUtil dbUtil = new DBUtil();
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        UserDAO userDAO = new UserDAO();
        if (buttonValue != null && !buttonValue.equals("cancel") && !buttonValue.equals("cancelview")
                && !buttonValue.equals("note") && !buttonValue.equals("searchcity") && !buttonValue.equals("print")) {
            if (editUserForm.getSignatureImageOutput().getFileData() != null
                    && editUserForm.getSignatureImageOutput().getFileData().length > 0) {
                user.setSignatureImage(editUserForm.getSignatureImageOutput().getFileData());
            }
            RefTerminalDAO terminalDAO = new RefTerminalDAO();
            RefTerminal terminalobj = terminalDAO.findById(terminal);
            RefTerminal importTerminalobj = terminalDAO.impfindById(impterminal);
            RefTerminal billingTerminalobj = terminalDAO.impfindById(billTerminal);
            
            user.setFirstName(firstName);
            user.setLastName(lastName);

            user.setAddress1(address1);
            user.setAddress2(address2);
            user.setCtsPackageType(packageType);
            user.setCtsPalletType(palletType);
            user.setFlatFee(editUserForm.getFlatFee());
            user.setFuelMarkUp(editUserForm.getFuelMarkUp());
            user.setLineMarkUp(editUserForm.getLineMarkUp());
            user.setMinAmount(editUserForm.getMinAmount());
            if (null != editUserForm.getCtsAccountNo()) {
                user.setCtsAccount(new CustomerDAO().getCustTemp(editUserForm.getCtsAccountNo()));
            }
            if (null != terminalobj) {
                if (terminalobj.getPhnnum1() != null && terminalobj.getPhnnum1() != "") {
                    user.setTelephone(terminalobj.getPhnnum1());
                }
                user.setFax(terminalobj.getFaxnum1());

                user.setTerminal(terminalobj);
                if (null != loginUser && null != user && loginUser.getUserId().equals(user.getUserId())) {
                    loginUser.setTerminal(terminalobj);
                    request.setAttribute("loginuser", loginUser);
                }
                user.setOfficeCityLOcation(terminalobj.getGovSchCode());
            }
            if (null != importTerminalobj) {
                user.setImportTerminal(importTerminalobj);
                if (null != loginUser && null != user && loginUser.getUserId().equals(user.getUserId())) {
                    loginUser.setImportTerminal(importTerminalobj);
                }
            }
            if (null != billingTerminalobj) {
                user.setBillingTerminal(billingTerminalobj);
                if (null != loginUser && null != user && loginUser.getUserId().equals(user.getUserId())) {
                    loginUser.setBillingTerminal(billingTerminalobj);
                }
            }
            if(CommonUtils.isNotEmpty(templateId)){
                user.setUserTemplate(new LclSearchTemplateDAO().findById(Integer.parseInt(templateId)));
            }
            user.setZipCode(zipcode);
            user.setWarehouse(editUserForm.getWarehouse());
            if (CommonUtils.isNotEmpty(editUserForm.getWarehouseNo())) {
                user.setWarehouseNo(editUserForm.getWarehouseNo());
            } else {
                user.setWarehouseNo(null);
            }
            user.setLoginName(loginName);
            user.setPassword(password);
            user.setStatus(status);
            user.setEmail(email);
            user.setAchApprover(editUserForm.isAchApprover());
            user.setSearchScreenReset(editUserForm.isSearchScreenReset());
            user.setExtension(extension);
            user.setUserCreatedDate(userCreatedDate);
            user.setGenericCode(user.getGenericCode());
            user.setUnLocation(user.getUnLocation());
            user.setState(user.getState());
            user.setCity(city);
            user.setOutsourceEmail(outsourceEmail);
            user.setDifflclBookedDimsActual(diffLclBookedDimsActual);
            RoleDAO roleDAO = new RoleDAO();
            if (roleId != null) {
                Role role = roleDAO.findById(Integer.parseInt(roleId));
                user.setRole(role);
            }

            if (buttonValue != null && buttonValue.equals("save")) {
                String phone1 = dbUtil.stringtokenizer(telephone);
                String fax1 = dbUtil.stringtokenizer(fax);

                String programid = null;
                programid = (String) session.getAttribute("processinfoforuser");
                String recordid = user.getUserId().toString();
                dbUtil.getProcessInfo(programid, recordid, "edited", "deleted");
                user.setOfficeCityLOcation(officeCityLocation);
                user.setFax(fax);
                user.setTelephone(telephone);

                user.setTelephone(phone1);
                user.setFax(fax1);
                          UserPrinterAssociation userPrinter = null;
                            if (null != user.getAssocitedPrinter()) {
                                for (Object printer : user.getAssocitedPrinter()) {
                                    userPrinter = (UserPrinterAssociation) printer;
                                    userPrinter.setUserId(user.getUserId());
                                }
                            }
                userDAO.update(user, user.getLoginName());
//                List userList = (List) session.getAttribute("userList");
//                if (null != userList && !userList.equals("")) {
//                    for (int i = 0; i < userList.size(); i++) {
//                        User tempUser = (User) userList.get(i);
//
//                        if (tempUser.getUserId().equals(user.getUserId())) {
//
//                            Role role1 = user.getRole();
//                            RefTerminal terminal1 = user.getTerminal();
//                            RefTerminal importTerminal1 = user.getImportTerminal();
//                            tempUser.setFirstName(user.getFirstName());
//                            tempUser.setLastName(user.getLastName());
//                            tempUser.setTelephone(user.getTelephone());
//                            tempUser.setLoginName(user.getLoginName());
//                            tempUser.setAddress1(user.getAddress1());
//                            tempUser.setAddress2(user.getAddress2());
//
//                            tempUser.setExtension(user.getExtension());
//                            tempUser.setFax(user.getFax());
//                            tempUser.setEmail(user.getEmail());
//
//                            tempUser.setOfficeCityLOcation(user.getOfficeCityLOcation());
//                            tempUser.setZipCode(user.getZipCode());
//                            //tempUser.setRoleId(roleId.getRoleId());
//                            tempUser.setStatus(user.getStatus());
//                            tempUser.setTerminal(terminal1);
//                            tempUser.setImportTerminal(importTerminal1);
//                            //tempUser.setTerminalId(terminal1.getEcitrm());
//                            tempUser.setUserId(user.getUserId());
//                            tempUser.setPassword(user.getPassword());
//                            tempUser.setAchApprover(user.isAchApprover());
//                            tempUser.setSearchScreenReset(user.isSearchScreenReset());
//                            tempUser.setRole(role1);
//                            tempUser.setAssocitedPrinter(user.getAssocitedPrinter());
//                            tempUser.setGenericCode(user.getGenericCode());
//                            tempUser.setUnLocation(user.getUnLocation());
//                            tempUser.setState(user.getState());
//                            tempUser.setCity(user.getCity());
//                            tempUser.setOutsourceEmail(user.getOutsourceEmail());
//                            tempUser.setDifflclBookedDimsActual(user.getDifflclBookedDimsActual());
//                            User userid = null;
//                            if (session.getAttribute("loginuser") != null) {
//                                userid = (User) session.getAttribute("loginuser");
//                            }
//                            UserPrinterAssociation userPrinter = null;
//                            if (null != user.getAssocitedPrinter()) {
//                                for (Object printer : user.getAssocitedPrinter()) {
//                                    userPrinter = (UserPrinterAssociation) printer;
//                                    userPrinter.setUserId(user.getUserId());
//                                }
//                            }
//                          
//                            userDAO.update(user, userid.getLoginName());
//                            break;
//                        }
//                    }
//                }

                if (session.getAttribute("user") != null) {
                    session.removeAttribute("user");
                }
                if (session.getAttribute("printerAddList") != null) {
                    session.removeAttribute("printerAddList");
                }
                if (session.getAttribute("view") != null) {
                    session.removeAttribute("view");
                }
                message = "User details updated successfully";
                request.setAttribute("message", message);
                forwardName = "cancel";
                setRequestValues(session, request, dbUtil);
            }

        }
        if (buttonValue != null && buttonValue.equals("cancel")) {
            String programid = null;
            programid = (String) session.getAttribute("processinfoforuser");
            String recordid = null != user && null != user.getUserId() ? user.getUserId().toString() : "";
            dbUtil.getProcessInfo(programid, recordid, "editcancelled", null);
            if (session.getAttribute("user") != null) {
                session.removeAttribute("user");
            }
            if (session.getAttribute("printerAddList") != null) {
                session.removeAttribute("printerAddList");
            }
            if (session.getAttribute("view") != null) {
                session.removeAttribute("view");
            }
            setRequestValues(session, request, dbUtil);
            forwardName = "cancel";
        }
        if (buttonValue != null && buttonValue.equals("cancelview")) {
            forwardName = "cancel";
            setRequestValues(session, request, dbUtil);
        }


        if (buttonValue != null && buttonValue.equals("delete")) {
            List userList = (List) session.getAttribute("userList");
            User userid = null;
            if (session.getAttribute("loginuser") != null) {
                userid = (User) session.getAttribute("loginuser");
            }
            String programid = null;
            programid = (String) session.getAttribute("processinfoforuser");
            String recordid = user.getUserId().toString();
            if (null != userList && !userList.equals("")) {
                for (int i = 0; i < userList.size(); i++) {
                    User tempUser = (User) userList.get(i);

                    if (tempUser.getUserId().equals(user.getUserId())) {
                        userList.remove(tempUser);
                        //session.setAttribute("userList",userList);
                        User userId = null;
                        if (session.getAttribute("loginuser") != null) {
                            userId = (User) session.getAttribute("loginuser");
                        }
                        userDAO.delete(tempUser, userId.getLoginName());
                        dbUtil.getProcessInfo(programid, recordid, "deleted", "edited");
                        message = "User details deleted successfully";
                        break;
                    }

                }
            }
            if (session.getAttribute("view") != null) {
                session.removeAttribute("view");
            }
            request.setAttribute("message", message);
            setRequestValues(session, request, dbUtil);
            forwardName = "cancel";

        }

        if (buttonValue != null && buttonValue.equals("note")) {
            ItemDAO itemDAO = new ItemDAO();
            Item item = new Item();
            String itemName = "";
            if (session.getAttribute("processinfoforuser") != null) {
                String itemId = (String) session.getAttribute("processinfoforuser");
                item = itemDAO.findById(Integer.parseInt(itemId));
                itemName = item.getItemDesc();
            }

            forwardName = "note";
            AuditLogRecord auditLogRecord = new AuditLogRecordUser();
            NoteBean noteBean = new NoteBean();
            noteBean.setItemName(itemName);
            noteBean.setAuditLogRecord(auditLogRecord);
            noteBean.setButtonValue(buttonValue);
            noteBean.setUser(user);
            noteBean.setPageName("cancel");
            String noteId = user.getUserId().toString();
            noteBean.setNoteId(noteId);
            noteBean.setReferenceId(user.getLoginName());
            noteBean.setVoidednote("");
            List auditList = null;
            auditList = dbUtil.getNoteInformation(noteId, auditLogRecord);
            noteBean.setAuditList(auditList);
            request.setAttribute("noteBean", noteBean);
            String documentName = "User";
        }


        if (buttonValue.equals("searchcity")) {
            List searchcity = unLocationDAO.findbyCity(city);
            if (session.getAttribute("user") != null) {
                user = (User) session.getAttribute("user");
            } else {
                user = new User();

            }
            if (searchcity != null && searchcity.size() > 0) {
                UnLocation unl = (UnLocation) searchcity.get(0);
                user.setUnLocation(unl);
                user.setCity(city);
            } else {

                user.setUnLocation(new UnLocation());
                user.setCity(city);
            }
            session.setAttribute("user", user);
            return mapping.findForward("edituser");

        }
        request.setAttribute("palletList", userDAO.getPalletList());
        request.setAttribute("packageList", userDAO.getPackageList());
        return mapping.findForward(forwardName);
    }

    public List<LabelValueBean> getPackageList() {
        List<LabelValueBean> packages = new ArrayList<LabelValueBean>();
        packages.add(new LabelValueBean("Select", ""));
        packages.add(new LabelValueBean("Bag", "BAG"));
        packages.add(new LabelValueBean("Bundle", "BDL"));
        packages.add(new LabelValueBean("Box", "BOX"));
        packages.add(new LabelValueBean("Barrel", "BRL"));
        packages.add(new LabelValueBean("Crate", "CRT"));
        packages.add(new LabelValueBean("Carton", "CTN"));
        packages.add(new LabelValueBean("Drum", "DRM"));
        packages.add(new LabelValueBean("Pail", "PAL"));
        packages.add(new LabelValueBean("Pieces", "PCS"));
        packages.add(new LabelValueBean("REEL", "REL"));
        packages.add(new LabelValueBean("Other", "OTH"));
        return packages;
    }

    public List<LabelValueBean> getPalletList() {
        List<LabelValueBean> pallets = new ArrayList<LabelValueBean>();
        pallets.add(new LabelValueBean("Select", ""));
        pallets.add(new LabelValueBean("Pallet", "PAD"));
        pallets.add(new LabelValueBean("Skid", "SKD"));
        pallets.add(new LabelValueBean("Loose", "LSE"));
        pallets.add(new LabelValueBean("Other", "OTH"));
        return pallets;
    }

    public void setRequestValues(HttpSession session, HttpServletRequest request, DBUtil dbUtil) throws Exception {
        SearchUserBean suBean = (SearchUserBean) session.getAttribute("suBean");
        request.setAttribute("userList", new UserDAO().getUserList(suBean));
        request.setAttribute("statuslist", dbUtil.getStatusList());
        request.setAttribute("rolelist", dbUtil.getRoleList());
    }
}
