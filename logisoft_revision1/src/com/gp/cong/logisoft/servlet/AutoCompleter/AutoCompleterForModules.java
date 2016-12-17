package com.gp.cong.logisoft.servlet.AutoCompleter;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;

public class AutoCompleterForModules {

    public void setModuleNames(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        String textFieldId = request.getParameter("textFieldId");
        if (textFieldId == null) {
            return;
        }
        String moduleName = request.getParameter(textFieldId);
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("<ul>");
        if (null != moduleName && !moduleName.trim().equals("")) {
            ItemDAO itemDAO = new ItemDAO();
            List<Item> moduleList = itemDAO.findModuleName(moduleName);
            if (null != moduleList && !moduleList.isEmpty()) {
                for (Item item : moduleList) {
                    stringBuffer.append("<li id='").append(item.getItemDesc()).append("'>");
                    stringBuffer.append("<b><font class='blue-70'>").append(item.getItemDesc()).append("</font></b></li>");
                }
            }
        }
        stringBuffer.append("</ul>");
        out.println(stringBuffer.toString());
    }
}
