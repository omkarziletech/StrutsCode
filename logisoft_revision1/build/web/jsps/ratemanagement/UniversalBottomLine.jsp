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
String resKG="";
String resCBM="";
String resLBS="";
String resCFT="";



 //-----------------BLR FOR SEARCHUNIVERSAL---------------
 if(session.getAttribute("SearchUnirateforKg")!=null){
			  
			  resKG=df.format(session.getAttribute("SearchUnirateforKg"));
			  }
			  if(session.getAttribute("SearchUnirateforCbm")!=null){
			  resCBM=df.format(session.getAttribute("SearchUnirateforCbm"));
			  }
			  if(session.getAttribute("SearchUnirateforLbs")!=null){
			  resLBS=df.format(session.getAttribute("SearchUnirateforLbs"));
			  }
			  if(session.getAttribute("SearchUnirateforCft")!=null){
			  resCFT=df.format(session.getAttribute("SearchUnirateforCft"));
			  }
          
 
 if(session.getAttribute("SearchUnirateforKg")!=null){
 session.removeAttribute("SearchUnirateforKg");
 }
 if(session.getAttribute("SearchUnirateforCbm")!=null){
 session.removeAttribute("SearchUnirateforCbm");
 }
 if(session.getAttribute("SearchUnirateforLbs")!=null){
 session.removeAttribute("SearchUnirateforLbs");
 }
 if(session.getAttribute("SearchUnirateforCft")!=null){
 session.removeAttribute("SearchUnirateforCft");
 }
 
 
 
String defaultRate="";

if(session.getAttribute("universaldefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("universaldefaultRate");
}

 if(request.getAttribute("return")!=null){%>
<script type="text/javascript">
     self.close();
     opener.location.href="<%=path%>/jsps/ratemanagement/searchUniversal.jsp";
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
		

		document.BottomlineRateForm.buttonValue.value="cancel";
		document.BottomlineRateForm.submit();
		}
		</script>

  </head>
  
  <body class="whitebackgrnd" >
    	<table width="100%" border="0" cellpadding="0" cellspacing="0">
				 <tr class="textlabels"><td align="left" class="headerbluelarge">BLR</td>
			     </tr>
				 
	  			 <tr>
	  				<td></td>
	  		     </tr>
	     		
			</table><br>
<table class="tableBorderNew" width="100%" border="0">
  <tr class="tableHeadingNew">Bottome Line Rates</tr>
  <tr>
    <td>
  		
 <table border="0" >
  		 
  		 <tr  class="textlabels">
    	 <%if(defaultRate!=null && defaultRate.equals("M")){ %>
    	 <td>Rates/1000KG</td><td></td><td></td>
    	 <td>
         <%=resKG %></td>
          </tr>
          <tr  class="textlabels">
    	 <td>Rates/Cbm</td><td></td><td></td>
    	 <td>
         <%=resCBM %></td>
          </tr>
        <%}
         else if(defaultRate!=null && defaultRate.equals("E"))
         {
   %> 
          <tr  class="textlabels">
    	 <td>Rates/lbs</td><td></td><td></td>
    	 <td>
         <%=resLBS %></td>
          </tr>
          <tr  class="textlabels">
    	 <td>Rates/cft</td><td></td><td></td>
    	 <td>
         <%=resCFT %></td>
          </tr>
        <%} %>
    </table>
  </td>
  </tr>
</table>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
 