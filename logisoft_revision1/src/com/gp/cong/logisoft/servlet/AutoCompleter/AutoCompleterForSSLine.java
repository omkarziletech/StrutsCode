/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gp.cong.logisoft.servlet.AutoCompleter;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.SSLineDAO;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Vinay
 */
public class AutoCompleterForSSLine {

    public void setSSLines(HttpServletRequest request, HttpServletResponse response)throws Exception{
            PrintWriter out = response.getWriter();
            String textFieldId = request.getParameter("textFieldId");
            if (textFieldId == null) {
                return;
            }
            String sslineName = request.getParameter(textFieldId);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<ul>");
            if (CommonUtils.isNotEmpty(sslineName)) {
                List result = new SSLineDAO().getSSLineNames(sslineName);
                if(CommonUtils.isNotEmpty(result)){
                    for(Object row : result){
                        Object[] col = (Object[])row;
                        stringBuilder.append("<li id='").append(col[0]).append("'>");
                        stringBuilder.append("<b><font class='blue-70'>").append(col[1]);
                        stringBuilder.append(" </font><font class='red-90'> <--> ").append(col[0]).append(" </font></b>");
                        stringBuilder.append("</li>");
                    }
                }
            }
            stringBuilder.append("</ul>");
            out.println(stringBuilder.toString());
        }

}
