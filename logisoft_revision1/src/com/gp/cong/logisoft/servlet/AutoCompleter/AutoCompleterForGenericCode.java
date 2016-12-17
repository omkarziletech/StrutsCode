package com.gp.cong.logisoft.servlet.AutoCompleter;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AutoCompleterForGenericCode {

    public void setCountry(HttpServletRequest request, HttpServletResponse response)throws Exception{
            PrintWriter out = response.getWriter();
            String textFieldId = request.getParameter("textFieldId");
            if (textFieldId == null) {
                return;
            }
            String codeDesc = request.getParameter(textFieldId);
            String codeTypeId = request.getParameter("codeTypeId");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<ul>");
            if (CommonUtils.isNotEmpty(codeDesc)) {
                List<String> properties = new ArrayList<String>();
                List<String> operators = new ArrayList<String>();
                List<Object> values = new ArrayList<Object>();
                properties.add("codedesc");
                operators.add("like");
                values.add(codeDesc);
                properties.add("codetypeid");
                operators.add("=");
                values.add(Integer.parseInt(codeTypeId));
                List<GenericCode> genericCodes = new GenericCodeDAO().findByProperties(properties, operators, values);
                if (CommonUtils.isNotEmpty(genericCodes)) {
                    for (GenericCode genericCode : genericCodes) {
                        stringBuilder.append("<li id='").append(genericCode.getId()).append("'>");
                        stringBuilder.append("<b><font class='blue-70'>");
                        stringBuilder.append(genericCode.getCodedesc());
                        stringBuilder.append("</font></b>");
                        stringBuilder.append("</li>");
                    }
                }
            }
            stringBuilder.append("</ul>");
            out.println(stringBuilder.toString());
    }
    public void setState(HttpServletRequest request, HttpServletResponse response)throws Exception{
            PrintWriter out = response.getWriter();
            String textFieldId = request.getParameter("textFieldId");
            if (textFieldId == null) {
                return;
            }
            String codeDesc = request.getParameter(textFieldId);
            String codeTypeId = request.getParameter("codeTypeId");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<ul>");
            if (CommonUtils.isNotEmpty(codeDesc)) {
                List<String> properties = new ArrayList<String>();
                List<String> operators = new ArrayList<String>();
                List<Object> values = new ArrayList<Object>();
                properties.add("codedesc");
                operators.add("like");
                values.add(codeDesc);
                properties.add("codetypeid");
                operators.add("=");
                values.add(Integer.parseInt(codeTypeId));
                List<GenericCode> genericCodes = new GenericCodeDAO().findByProperties(properties, operators, values);
                if (CommonUtils.isNotEmpty(genericCodes)) {
                    for (GenericCode genericCode : genericCodes) {
                        stringBuilder.append("<li id='").append(genericCode.getId()).append("'>");
                        stringBuilder.append("<b><font class='red-90'>");
                        stringBuilder.append(genericCode.getCode()).append("</font><font class='blue-70'> - ").append(genericCode.getCodedesc());
                        stringBuilder.append("</font></b>");
                        stringBuilder.append("</li>");
                    }
                }
            }
            stringBuilder.append("</ul>");
            out.println(stringBuilder.toString());
    }
}
