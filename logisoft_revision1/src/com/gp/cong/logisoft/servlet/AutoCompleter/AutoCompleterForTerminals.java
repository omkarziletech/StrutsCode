/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.servlet.AutoCompleter;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.RefTerminalTemp;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.TerminalDAO;

/**
 *
 * @author pradeep
 */
public class AutoCompleterForTerminals {

    public void getTerminals(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        String textFieldId = request.getParameter("textFieldId");
        if (textFieldId == null) {
            return;
        }
        String termName = request.getParameter(textFieldId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<ul>");
        if (CommonUtils.isNotEmpty(termName)) {
            List result = getTerminals(termName);
            if (CommonUtils.isNotEmpty(result)) {
                for (Object row : result) {
                    Object[] col = (Object[]) row;
                    stringBuilder.append("<li id='").append(col[0]).append("'>");
                    stringBuilder.append("<b><font class='blue-70'>").append(col[1]);
                    stringBuilder.append("</font><font class='red-90'> <--> ").append(col[0]).append(" </font></b>");
                    stringBuilder.append("</li>");
                }
            }
        }
        stringBuilder.append("</ul>");
        out.println(stringBuilder.toString());
    }

    public List getTerminals(String termName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("SELECT rd.blterm, tr.trmnam");
        queryBuilder.append(" FROM terminal tr, retadd rd");
        queryBuilder.append(" WHERE rd.blterm=tr.trmnum AND tr.trmnam like '").append(termName).append("%'");
        queryBuilder.append(" GROUP BY tr.trmnam");
        return new BaseHibernateDAO().getSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public void setBillingTerminals(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        String textFieldId = request.getParameter("textFieldId");
        if (textFieldId == null) {
            return;
        }
        String terminalNumber = request.getParameter(textFieldId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<ul>");
        if (CommonUtils.isNotEmpty(terminalNumber)) {
            List<RefTerminalTemp> result = new TerminalDAO().getTerminals(terminalNumber);
            for (RefTerminalTemp terminal : result) {
                stringBuilder.append("<li id='").append(terminal.getTrmnum()).append("'>");
                stringBuilder.append("<b><font class='red-90'>").append(terminal.getTrmnum());
                stringBuilder.append("</font><font class='blue-70'> <--> ").append(terminal.getTerminalLocation()).append("</font></b>");
                stringBuilder.append("</li>");
            }
        }
        stringBuilder.append("</ul>");
        out.println(stringBuilder.toString());
    }
}
