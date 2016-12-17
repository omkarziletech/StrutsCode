<%@ page language="java" import="com.gp.cong.logisoft.util.DBUtil,java.text.DecimalFormat,
java.util.*,com.gp.cong.logisoft.domain.StandardCharges,com.gp.cong.logisoft.beans.AirRatesBean,com.gp.cong.logisoft.domain.AirWeightRangesRates,com.gp.cong.logisoft.domain.AirStandardCharges"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DecimalFormat df = new DecimalFormat("0.00");
String bottomRate="";
String rate="";
StandardCharges scharge=null;
List rList=new ArrayList();
int i=0;
String expressrate="";
String generalrate="";
String lbs="";

	if(session.getAttribute("Grates")!=null){
	generalrate =df.format(session.getAttribute("Grates"));
	}
	if(session.getAttribute("Drates")!=null){
	expressrate=df.format(session.getAttribute("Drates"));
	}
	/*if(session.getAttribute("Stdrates")!=null){
	lbs=df.format(session.getAttribute("Stdrates"));
	}*/
    
 
 if(session.getAttribute("Drates")!=null)
	{
	session.removeAttribute("Drates");
	}
	if(session.getAttribute("Grates")!=null)
	{
	session.removeAttribute("Grates");
	}
	/*if(session.getAttribute("Stdrates")!=null)
	{
	session.removeAttribute("Stdrates");
	}*/

 if(request.getAttribute("return")!=null){%>
<script type="text/javascript">
     self.close();
     opener.location.href="<%=path%>/jsps/ratemanagement/manageAirRates.jsp";
</script>
<% } %>

<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>BottomLine Rate Form</title>
    
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		
		function loadbrate(val)
  	    {
  	   

  	      if(val != "")
  	       {
  	       
  	       document.BottomlineRateForm.blrate.value = val;
  	       document.BottomlineRateForm.submit();
  	       } 
	 
  	    }
  	    function cancelForm()
		{
		alert("hello");
		document.BottomlineRateForm.buttonValue.value="cancel";
		document.BottomlineRateForm.submit();
		}
		</script>

  </head>
  
  <body class="whitebackgrnd" >
    	<table width="100%" border="0" cellpadding="0" cellspacing="0">
				 <tr class="textlabels"><td align="left" class="headerbluelarge">BLR</td>
			     </tr>
				 <tr class="textlabels">
	    			<td align="left" class="headerbluelarge">&nbsp;</td>
	  			 </tr>
	  			 <tr>
	  				<td></td>
	  		     </tr>
	     		 <tr>
	    		    <td height="12"  class="headerbluesmall">&nbsp;&nbsp;Bottome Line Rates</td> 
	  			 </tr>
			</table><br>
  		 <table border="0" >
  		 <tr  class="textlabels">
    	 <td>General Rates</td>
    	 <td></td>
    	 <td></td>
    	 <td>
         <%=generalrate%></td>
          </tr>
          <tr class="textlabels">
         <td>Deferred Rates</td>
    	 <td></td>
    	 <td></td>
    	 <td>
         <%=expressrate %></td>
          </tr>
          <tr class="textlabels">
        
          
 </table>
 
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
 