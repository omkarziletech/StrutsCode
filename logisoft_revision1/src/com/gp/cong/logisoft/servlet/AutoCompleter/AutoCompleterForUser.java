package com.gp.cong.logisoft.servlet.AutoCompleter;

import com.gp.cong.common.CommonUtils;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;

public class AutoCompleterForUser {

    public void setUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        String textFieldId = request.getParameter("textFieldId");
        if (textFieldId == null) {
            return;
        }
        String userName = request.getParameter(textFieldId);
        if (null == userName) {
            return;
        }
        String role = request.getParameter("role");
        StringBuilder stringBuilder = new StringBuilder();
        String get = request.getParameter("get");
        String requestFrom = request.getParameter("requestFrom");
        stringBuilder.append("<ul>");
        if (CommonUtils.isEqualIgnoreCase(get, "id")) {
            List<User> users = new UserDAO().findByUserName(userName);
            for (User user : users) {
                stringBuilder.append("<li id='").append(user.getUserId()).append("'>");
                stringBuilder.append("<b><font class='blue-70'>").append(user.getLoginName()).append("</font>");
                if (null != user.getRole() && CommonUtils.isNotEmpty(user.getRole().getRoleDesc())) {
                    stringBuilder.append("<font class='red-90'> <--> ");
                    stringBuilder.append(user.getRole().getRoleDesc());
                    stringBuilder.append("</font>");
                }
                stringBuilder.append("</b>");
                stringBuilder.append("</li>");
            }
        } else if (CommonUtils.isEqualIgnoreCase(get, "emailAddress")) {
            List<User> users = new UserDAO().findByUserName(userName);
            for (User user : users) {
                if (CommonUtils.isNotEmpty(user.getEmail())) {
                    stringBuilder.append("<li id='").append(user.getEmail()).append("'>");
                    stringBuilder.append("<b><font class='blue-70'>").append(user.getLoginName());
                    stringBuilder.append("</font><font class='red-90'> <--> ");
                    stringBuilder.append(user.getEmail());
                    stringBuilder.append("</font>");
                    stringBuilder.append("</b></li>");
                }
            }
        } else if (CommonUtils.isNotEmpty(role)) {
            List<User> users = new UserDAO().findUserByNameAndRole(userName, role);
            for (User user : users) {
                stringBuilder.append("<li id='").append(user.getUserId()).append("'>");
                stringBuilder.append("<b><font class='blue-70'>").append(user.getLoginName()).append("</font>");
                if (null != user.getRole() && CommonUtils.isNotEmpty(user.getRole().getRoleDesc())) {
                    stringBuilder.append("<font class='red-90'> <--> ");
                    stringBuilder.append(user.getRole().getRoleDesc());
                    stringBuilder.append("</font>");
                }
                stringBuilder.append("</b></li>");
            }
        } else if (CommonUtils.isEqualIgnoreCase(requestFrom, "emailScheduler")) {
            List<User> users = new UserDAO().findByUserName(userName);
            for (User user : users) {
                stringBuilder.append("<li id='").append(user.getLoginName()).append("'>");
                stringBuilder.append("<b><font class='blue-70'>").append(user.getLoginName());
                stringBuilder.append("</font></b></li>");
            }
        } else {
            List<User> users = new UserDAO().findByUserName(userName);
            for (User user : users) {
                stringBuilder.append("<li id='").append(user.getLoginName()).append("'>");
                stringBuilder.append("<b><font class='blue-70'>").append(user.getLoginName()).append("</font>");
                if (null != user.getRole() && CommonUtils.isNotEmpty(user.getRole().getRoleDesc())) {
                    stringBuilder.append("<font class='red-90'> <--> ");
                    stringBuilder.append(user.getRole().getRoleDesc());
                    stringBuilder.append("</font>");
                }
                stringBuilder.append("</b></li>");
            }
        }
        stringBuilder.append("</ul>");
        out.println(stringBuilder.toString());
    }

    public void setUserByName(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        String textFieldId = request.getParameter("textFieldId");
        if (textFieldId == null) {
            return;
        }
        String usrName = request.getParameter(textFieldId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<ul>");
        if (CommonUtils.isNotEmpty(usrName)) {
            List<String> result = findUserNameList(usrName);
            if (CommonUtils.isNotEmpty(result)) {
                for (String str : result) {
                    stringBuilder.append("<li id='").append(str).append("'>");
                    stringBuilder.append("<b><font class='blue-70'>").append(str).append("</font></b>");
                    stringBuilder.append("</li>");
                }
            }
        }
        stringBuilder.append("</ul>");
        out.println(stringBuilder.toString());
    }

    public void setCollectors(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        String textFieldId = request.getParameter("textFieldId");
        String loginName = request.getParameter(textFieldId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<ul>");
        List<String> collectors = new UserDAO().getCollectors(loginName);
        if (CommonUtils.isNotEmpty(collectors)) {
            for (String collector : collectors) {
                stringBuilder.append("<li id='").append(collector).append("'>");
                stringBuilder.append("<b><font class='blue-70'>").append(collector).append("</font></b>");
                stringBuilder.append("</li>");
            }
        }
        stringBuilder.append("</ul>");
        out.println(stringBuilder.toString());
    }

    public List<String> findUserNameList(String userName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("SELECT usrnam");
        queryBuilder.append(" FROM  retadd");
        queryBuilder.append(" WHERE usrnam<> '' and usrnam LIKE '").append(userName).append("%'");
        queryBuilder.append(" GROUP BY usrnam");
        return (List<String>) new BaseHibernateDAO().getSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public void setEmailAddress(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        String textFieldId = request.getParameter("textFieldId");
        String eMailAddress = request.getParameter(textFieldId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<ul>");
        if (CommonUtils.isNotEmpty(eMailAddress)) {
            EmailschedulerDAO emailDAO = new EmailschedulerDAO();
            List<String> list = emailDAO.getByEmail(eMailAddress);
            if (CommonUtils.isNotEmpty(list)) {
                for (String emailId : list) {
                    stringBuilder.append("<li id='").append(emailId).append("'>");
                    stringBuilder.append("<b><font class='blue-70'>").append(emailId);
                    stringBuilder.append("</font></b></li>");
                }
            }
        }
        stringBuilder.append("</ul>");
        out.println(stringBuilder.toString());
    }
}
