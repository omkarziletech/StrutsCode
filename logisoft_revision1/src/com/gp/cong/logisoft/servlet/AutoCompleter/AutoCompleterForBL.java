package com.gp.cong.logisoft.servlet.AutoCompleter;

import com.gp.cong.common.CommonUtils;
import com.logiware.accounting.dao.AccrualsDAO;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AutoCompleterForBL {

    public void setBlAutoCompleteResults(HttpServletRequest request, HttpServletResponse response)throws Exception {
	    PrintWriter out = response.getWriter();
	    String textFieldId = request.getParameter("textFieldId");
	    if (textFieldId == null) {
		return;
	    }
	    String blNumber = request.getParameter(textFieldId);
	    if (CommonUtils.isNotEmpty(blNumber)) {
		List<Object> results = new AccrualsDAO().getBlAutoCompleteResults(blNumber);
		if (CommonUtils.isNotEmpty(results)) {
		    StringBuilder stringBuilder = new StringBuilder();
		    stringBuilder.append("<ul>");
		    for (Object row : results) {
			Object[] col = (Object[]) row;
			blNumber = (String) col[0];
			String voyageNumber = (String) col[1];
			String dockReceipt = (String) col[2];
			stringBuilder.append("<li id='").append(blNumber).append("@").append(voyageNumber).append("@").append(dockReceipt).append("'>");
			stringBuilder.append("<b><font class='red-90'>").append(blNumber).append("</font> <--> ");
			stringBuilder.append("<font class='green'>").append(voyageNumber).append("</font> <--> ");
			stringBuilder.append("<font class='blue-70'>").append(dockReceipt).append("</font></li>");
		    }
		    stringBuilder.append("</ul>");
		    out.println(stringBuilder.toString());
		}
	    }
    }
}
