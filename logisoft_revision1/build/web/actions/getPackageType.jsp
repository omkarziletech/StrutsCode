<%@page import="com.gp.cong.logisoft.util.CommonFunctions"%>
<%@ page
	import="org.json.JSONArray,com.gp.cvst.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.GenericCode,com.gp.cvst.logisoft.beans.ChartOfAccountBean"%>
<%@ page import="java.util.*"%>
<jsp:directive.page
	import="com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;" />
<%
	String packType="";
	Integer noOfpkgs=0;
	String functionName = null;
	GenericCode genericCode=new GenericCode();
	JSONArray pakTypeArray = new JSONArray();
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}
	if (functionName.equals("MARKS_NUMBER")) {
		if (request.getParameter("uom") != null
			&& request.getParameter("from") != null
			&& request.getParameter("from").equals("0")) {
			packType = request.getParameter("uom");
                        if(CommonFunctions.isNotNull(request.getParameter("noOfpkgs"))){
                            noOfpkgs = Integer.parseInt(request.getParameter("noOfpkgs"));

                        }
		}
	}
	if(null != packType && !packType.equals("")){
		List packageList = null;
		GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
                if(noOfpkgs>1){
                    packageList = (List)genericCodeDAO.getCodeDescForPackType(packType);
                }else{
                    packageList = (List)genericCodeDAO.getFieldForPackType(packType);
                }
                Iterator iter = packageList.iterator();
                try {
                    while (iter.hasNext()) {
                            genericCode =(GenericCode) iter.next();
                             if(noOfpkgs>1){
                                pakTypeArray.put(genericCode.getCodedesc()+"<-->"+genericCode.getCode());
                             }else{
                                pakTypeArray.put(genericCode.getField1()+"<-->"+genericCode.getCode());
                             }
                    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	 if ("false".equals(request.getParameter("isDojo"))) {
            StringBuilder buffer = new StringBuilder("<UL>");
            for (int i = 0; i < pakTypeArray.length(); i++) {
                buffer.append("<li>");
                buffer.append(pakTypeArray.get(i));
                buffer.append("</li>");
            }
            buffer.append("</UL>");
            out.println(buffer.toString());
        } else {
            out.println(pakTypeArray.toString());
        }
%>