/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.ProcessInfo;
import com.gp.cong.logisoft.domain.Role;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.RoleDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.struts.form.SearchRoleForm;
import com.gp.cong.logisoft.beans.SearchUserBean;

/** 
 * MyEclipse Struts
 * Creation date: 11-06-2007
 * 
 * XDoclet definition:
 * @struts.action path="/searchRole" name="searchRoleForm" input="/jsps/admin/SearchRole.jsp" scope="request" validate="true"
 */
public class SearchRoleAction extends Action {
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
        SearchRoleForm searchRoleForm = (SearchRoleForm) form;// TODO Auto-generated method stub
        String roleName = searchRoleForm.getRoleName();
        HttpSession session = ((HttpServletRequest) request).getSession();
        String roleCreatedDate = searchRoleForm.getTxtCal();
        String match = searchRoleForm.getMatch();
        String message = "";
        String buttonValue = searchRoleForm.getButtonValue();
        SearchUserBean suBean = new SearchUserBean();
        suBean.setRoleName(roleName);
        suBean.setTxtCal(roleCreatedDate);
        suBean.setMatch(match);
        String loginname = null;
        String msg = "";
        String forwardName = "";
        String name = searchRoleForm.getName();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        if (request.getParameter("paramid") != null) {
            RoleDAO role1 = new RoleDAO();

            Role role = role1.findById(Integer.parseInt(request.getParameter("paramid")));
            User userid = null;
            UserDAO user1 = new UserDAO();
            if (session.getAttribute("loginuser") != null) {
                userid = (User) session.getAttribute("loginuser");

            }
            ProcessInfoDAO processinfoDAO = new ProcessInfoDAO();
            ProcessInfo pi = new ProcessInfo();
            String programid = null;
            programid = (String) session.getAttribute("processinfoforrole");
            String recordid = "";
            if (role.getRoleId() != null) {
                recordid = role.getRoleId().toString();
            }

            String editstatus = "startedited";
            String deletestatus = "startdeleted";
            ProcessInfo processinfoobj = processinfoDAO.findById(Integer.parseInt(programid), recordid, deletestatus, editstatus);
            if (processinfoobj != null && !processinfoobj.getUserid().equals(userid.getUserId())) {
                String view = "3";
                User loginuser = user1.findById(processinfoobj.getUserid());
                loginname = loginuser.getLoginName();
                msg = "This record is being used by ";
                message = msg + loginname;
                session.setAttribute("role", role);
                    response.sendRedirect(request.getContextPath() + "/jsps/admin/EditRole.jsp?message=" + message + "&viewOnly=" + true);
            } else {
                pi.setUserid(userid.getUserId());
                pi.setProgramid(Integer.parseInt(programid));
                java.util.Date currdate = new java.util.Date();
                pi.setProcessinfodate(currdate);
                pi.setEditstatus(editstatus);
                pi.setRecordid(recordid);
                processinfoDAO.save(pi);
                session.setAttribute("role", role);
                    response.sendRedirect(request.getContextPath() + "/jsps/admin/EditRole.jsp?viewOnly=" + false);
            }
        } else if (request.getParameter("param") != null) {
            RoleDAO roleDao = new RoleDAO();
            Role role = roleDao.findById(Integer.parseInt(request.getParameter("param")));
            session.setAttribute("role", role);
                response.sendRedirect(request.getContextPath() + "/jsps/admin/EditRole.jsp?viewOnly=" + true);
        } else {
            if (buttonValue != null && buttonValue.equals("add")) {
                //forwardName="addrole";
                    response.sendRedirect(request.getContextPath() + "/jsps/admin/NewRole.jsp");
            } else if (buttonValue != null && buttonValue.equals("search")) {
                request.setAttribute("suBean", suBean);
                java.util.Date javaDate = null;
                if (roleCreatedDate != null && !roleCreatedDate.equals("")) {
                        javaDate = sdf.parse(roleCreatedDate);
                }
                RoleDAO roleDAO = new RoleDAO();


                if (match.equals("match")) {
                    List roleList = roleDAO.findForManagement(roleName, javaDate);
                    session.setAttribute("roleList", roleList);
                    if (roleName != null && !roleName.equals("")) {
                        session.setAttribute("roleListCaption", "RoleName {Match Only}");
                    }
                    if (javaDate != null && !javaDate.equals("")) {
                        session.setAttribute("roleListCaption", "Role Created on{Match Only}");
                    }
                } else if (match.equals("starts")) {
                    List roleList = roleDAO.findForSearchRoleAction(roleName, javaDate, match);
                    session.setAttribute("roleList", roleList);
                    if (roleName != null && !roleName.equals("")) {
                        session.setAttribute("roleListCaption", "RoleName {Start At List}");
                    }
                    if (javaDate != null && !javaDate.equals("")) {
                        session.setAttribute("roleListCaption", "Role Created on{Start At List}");
                    }
                }
                forwardName = "searchrole";
            } else if (buttonValue != null && buttonValue.equals("searchall")) {
                forwardName = "searchrole";
            }
        }
        if (session.getAttribute("popup") != null) {

            session.removeAttribute("popup");
        }
        request.setAttribute("buttonValue", buttonValue);
        return mapping.findForward(forwardName);
    }
}