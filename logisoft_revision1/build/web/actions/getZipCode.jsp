<%-- 
    Document   : getZipCode.jsp
    Created on : Oct 26, 2010, 6:05:41 PM
    Author     : Pradeep
--%>

<%@ page language="java"
	import="java.util.*,com.gp.cvst.logisoft.domain.Zipcode"%>


<%
String zipCodeOrCity = "";
if(request.getParameter("zip")!=null){
        if("1".equals(request.getParameter("from"))){
             zipCodeOrCity = request.getParameter("zip");
        }else{
             zipCodeOrCity = request.getParameter("zip");
        }
 }else if(request.getParameter("city")!=null){
     if("1".equals(request.getParameter("from"))){
             zipCodeOrCity = request.getParameter("city");
        }
     }
   StringBuffer stringBuffer = new StringBuffer("");

   List<Zipcode> zipCodeList = new ArrayList<Zipcode> ();
    if(com.gp.cong.common.CommonUtils.isNotEmpty(zipCodeOrCity)){
      com.gp.cvst.logisoft.hibernate.dao.ZipCodeDAO zipCodeDAO = new com.gp.cvst.logisoft.hibernate.dao.ZipCodeDAO();
     zipCodeList = zipCodeDAO.findZipCode(zipCodeOrCity.replace("'", "''").replace("\"", ""), "zipOrCity");
     }
    stringBuffer.append("<ul>");
    if(request.getParameter("city")!=null){
        for(Zipcode zipcode:zipCodeList){
           stringBuffer.append("<li id='"+ zipcode.getCountry_code()+ "'>");
           stringBuffer.append(zipcode.getCity()+"/"+zipcode.getState() );
           stringBuffer.append("</li>");
       }
    }else{
        for(Zipcode zipcode:zipCodeList){
           stringBuffer.append("<li id='"+ zipcode.getCountry_code()+ "'>");
           stringBuffer.append(zipcode.getZip()+"<-->"+zipcode.getCity()+", "+zipcode.getState() );
           stringBuffer.append("</li>");
        }
    }
    
    stringBuffer.append("</ul>");
   out.println(stringBuffer.toString());
//zipCodeDAO.
%>