package com.gp.cong.logisoft.servlet.AutoCompleter;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gp.cvst.logisoft.domain.Subledger;
import com.gp.cvst.logisoft.hibernate.dao.SubledgerDAO;

public class AutoCompleterForSubLedger {

    @SuppressWarnings("unchecked")
    public void setSubLedgerCodes(HttpServletRequest request, HttpServletResponse response)throws Exception{
            PrintWriter out = response.getWriter();
            String textFieldId = request.getParameter("textFieldId");
            if (textFieldId == null) {
                return;
            }
            String subLedgerCode = request.getParameter(textFieldId);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<ul>");
            if (null != subLedgerCode && !subLedgerCode.trim().equals("")) {
                SubledgerDAO subledgerDAO = new SubledgerDAO();
                List<Subledger> subLedgerList = subledgerDAO.findByProperty("subLedgerCode", subLedgerCode);
                for (Subledger subledger : subLedgerList) {
                    stringBuilder.append("<li id='").append(subledger.getSubLedgerCode()).append("'>");
                    stringBuilder.append("<b><font class='red-90'>").append(subledger.getSubLedgerCode());
                    stringBuilder.append("</font><font class='blue-70'> <--> ").append(subledger.getSubLedgerDesc());
                    stringBuilder.append("</font></b></li>");
                }
            }
            stringBuilder.append("</ul>");
            out.println(stringBuilder.toString());
    }
}
