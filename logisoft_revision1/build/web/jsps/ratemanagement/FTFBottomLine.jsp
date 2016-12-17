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



	

 
 //-----BLR FOR FOREIGN RATES---------
 
  
              if(session.getAttribute("FTFrateforKg")!=null){
			  
			  resKG=df.format(session.getAttribute("FTFrateforKg"));
			  }
			  if(session.getAttribute("FTFrateforCbm")!=null){
			  resCBM=df.format(session.getAttribute("FTFrateforCbm"));
			  }
			  if(session.getAttribute("FTFrateforLbs")!=null){
			  resLBS=df.format(session.getAttribute("FTFrateforLbs"));
			  }
			  if(session.getAttribute("FTFrateforCft")!=null){
			  resCFT=df.format(session.getAttribute("FTFrateforCft"));
			  }
          
 
 if(session.getAttribute("FTFrateforKg")!=null){
 session.removeAttribute("FTFrateforKg");
 }
 if(session.getAttribute("FTFrateforCbm")!=null){
 session.removeAttribute("FTFrateforCbm");
 }
 if(session.getAttribute("FTFrateforLbs")!=null){
 session.removeAttribute("FTFrateforLbs");
 }
 if(session.getAttribute("FTFrateforCft")!=null){
 session.removeAttribute("FTFrateforCft");
 }
 
 
String defaultRate="";

if(session.getAttribute("serachftfdefaultRate")!=null)
{
defaultRate=(String)session.getAttribute("serachftfdefaultRate");
}

 if(request.getAttribute("return")!=null){%>
<script type="text/javascript">
     self.close();
     opener.location.href="<%=path%>/jsps/ratemanagement/searchFTF.jsp";
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
 
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
 