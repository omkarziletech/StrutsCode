<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ page language="java" import="com.gp.cong.logisoft.util.DBUtil,com.gp.cvst.logisoft.domain.FclBl"%>
<%@ page
	import="com.gp.cong.logisoft.bc.notes.NotesConstants,java.text.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*,java.util.*"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

 
<html> 
	<head>
		<title>JSP for FclBlCorrectionPopUpForm form</title>
		<%
			String path = request.getContextPath(); 
		 %>
		<%@include file="../includes/baseResources.jsp" %>
	</head>
	
	<body class="whitebackgrnd">
		<html:form action="/fclBlCorrectionPopUp" scope="request">
			<!--display table for displaying fcl bls notice number -->
			<table cellpadding="0" cellspacing="0" width="100%" class="tableBorderNew">
				<tr>
					<td>
						<table width="100%" cellpadding="0" cellspacing="0" >
						<tr class="tableHeadingNew">Search Results</tr>
						<tr class="textlabels">
          				<td align="left" >
          				<div id="divtablesty1" style="border:thin;overflow:auto;height:80%">
          				
            			<table  border="0" cellpadding="0" cellspacing="0" class="displaytagstyle">
            				<thead>
            					<td>Bol ID</td>
            					<td>BOL DATE</td>
            					<td>SSL Bkg #</td>
            					<td>Quote No</td>
            					
            				</thead>
            			 <%
          					List noticeList = (List)request.getAttribute("noticeNumberList");
          					String className ="odd";
          					SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
          					for(int i=0;i<noticeList.size();i++){
          						FclBl fclbl=(FclBl)noticeList.get(i);
          						String bolId = fclbl.getBolId();
          						String bolDate=sdf1.format(fclbl.getBolDate());
          						String bookingNo=fclbl.getBookNo();
          						String quoteno = fclbl.getQuuoteNo();
          						Integer bolNo = fclbl.getBol();
          						String bol ="";
          						if(bolNo!=null){
          							bol = String.valueOf(bolNo);
          						}
          						if(bolId.contains("==")){			
          						
          				 %>
            				<tbody>
            					<tr  class="<%=className%>">
            						<td><a href="" onclick="backToFcl('<%=bol %>');"><%=bolId %></a> </td>
            						<td><%=bolDate %></td>
            						<td><%=bookingNo %></td>
            						<td><%=quoteno %></td>
            					</tr>
            				</tbody>
						
						<%
							
								if(className!=null && className.equalsIgnoreCase("odd")){
									className="even";
								}else{
									className ="odd";
								}
							}
			                  } 
		                %>
		                </table>
                        </div>
                        </td>
                        </tr>
                        </table>
					</td>
				</tr>
			</table>
			
		</html:form>
		<script type='text/javascript'>
			function backToFcl(ev){
				parent.parent.GB_hide();
				parent.parent.latestBl(ev);
			}
		</script>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

