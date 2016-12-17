/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.servlet.AutoCompleter;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Anshu
 */
public class AutoCompleterForPortName {
    
    public void getPortName(HttpServletRequest request, HttpServletResponse response)throws Exception{
        PrintWriter out = response.getWriter();
            String textFieldId = request.getParameter("textFieldId");
            if (textFieldId == null) {
                return;
            }
            String portname = request.getParameter(textFieldId);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<ul>");
            if (CommonUtils.isNotEmpty(portname)) {
                List result = getPortsName(portname);
                if(CommonUtils.isNotEmpty(result)){
                    for(Object row : result){
                        String po= (String)row;
                        stringBuilder.append("<li id='").append(po).append("'>");                        
                        stringBuilder.append("<font class='red-90'>").append(po).append(" </font>");
                        stringBuilder.append("</li>");
                    }
                }
            }          
            stringBuilder.append("</ul>");
            out.println(stringBuilder.toString());
    }
    
    public List getPortsName(String portName){
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT  portname FROM ports WHERE (govschnum !='NULL' OR eciportcode !='NULL') AND portname LIKE '");
        queryBuilder.append(portName).append("%'ORDER BY portname ASC");
        return new BaseHibernateDAO().getSession().createSQLQuery(queryBuilder.toString()).list();
    }
}
