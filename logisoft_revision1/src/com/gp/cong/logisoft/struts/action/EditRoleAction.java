package com.gp.cong.logisoft.struts.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.domain.Role;
import com.gp.cong.logisoft.domain.RoleItemAssociation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.hibernate.dao.RoleDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.struts.form.EditRoleForm;
import com.gp.cong.logisoft.util.DBUtil;
import com.logiware.bean.ItemBean;

public class EditRoleAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        EditRoleForm editRoleForm = (EditRoleForm) form;// TODO Auto-generated
        // method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        Role role = (com.gp.cong.logisoft.domain.Role) session.getAttribute("role");
        String buttonValue = editRoleForm.getButtonValue();
        DBUtil dbUtil = new DBUtil();
        String msg = "";
        String message = null;

        if (buttonValue != null && buttonValue.equals("save")) {
            Set roleItemSet = new HashSet<RoleItemAssociation>();
            for (ItemBean item : editRoleForm.getRoleItemBeans()) {
                if (CommonUtils.isEqualIgnoreCase(item.getChecked(), CommonConstants.ON)) {
                    roleItemSet.add(new RoleItemAssociation(item, role.getRoleId()));
                }
            }
            role.getRoleItem().clear();
            role.setRoleItem(roleItemSet);
            role.setUpdatedDate(new Date());
            User userid = null;
            if (session.getAttribute("loginuser") != null) {
                userid = (User) session.getAttribute("loginuser");
            }
            role.setUpdatedBy(userid.getLoginName());
            new RoleDAO().update(role, userid.getLoginName());
            request.setAttribute("save", "save");
            request.setAttribute("role", role);
            if (session.getAttribute("roleList") != null) {
                List roleList = (List) session.getAttribute("roleList");
                for (Iterator iterator = roleList.iterator(); iterator.hasNext();) {
                    Role tempRole = (Role) iterator.next();
                    if (role.getRoleId().toString().equals(tempRole.getRoleId().toString())) {
                        tempRole.setUpdatedBy(role.getUpdatedBy());
                        tempRole.setUpdatedDate(role.getUpdatedDate());
                    }
                }
            }
            message = "Role name updated successfully";
                response.sendRedirect(request.getContextPath()
                        + "/jsps/admin/SearchRole.jsp?message=" + message);
        } else if (buttonValue != null && buttonValue.equals("cancelview")) {
            if (session.getAttribute("view") != null) {
                session.removeAttribute("view");
            }
                response.sendRedirect(request.getContextPath()
                        + "/jsps/admin/SearchRole.jsp");
        } else if (null!=buttonValue && buttonValue.equals("note")) {
            Notes notes = new Notes();
            ItemDAO itemDAO = new ItemDAO();
            Item item = new Item();
            String moduleId = "";
            if (session.getAttribute("processinfoforrole") != null) {
                String itemId = (String) session.getAttribute("processinfoforrole");
                item = itemDAO.findById(Integer.parseInt(itemId));
                moduleId = item.getItemDesc();
            }
            //request.setAttribute("notesTypelist", dbUtil.getNotesList(28, "no","Select Note type"));
            request.setAttribute("notes", moduleId);
            request.setAttribute("notePopup", "notePopup");
            return mapping.findForward("editrole");
        } else if (buttonValue != null && buttonValue.equals("delete")) {
            RoleDAO roleDAO = new RoleDAO();
            new UserDAO().updateProperty("role", role, null);
            roleDAO.delete(role);
            session.setAttribute("roleList",dbUtil.getAllRole());
            message = "Role details deleted successfully";
                response.sendRedirect(request.getContextPath()
                        + "/jsps/admin/SearchRole.jsp?message=" + message);
        } else {
            String programid = null;
            programid = (String) session.getAttribute("processinfoforrole");
            String recordid = role.getRoleId().toString();
            dbUtil.getProcessInfo(programid, recordid, "editcancelled", null);
            if (session.getAttribute("role") != null) {
                session.removeAttribute("role");
            }
                response.sendRedirect(request.getContextPath()
                        + "/jsps/admin/SearchRole.jsp");
        }
        return null;
    }
}
