<%@include file="../includes/jspVariables.jsp"%>
<%@ page language="java" pageEncoding="ISO-8859-1"
	import="com.gp.cong.logisoft.util.DBUtil,java.util.Iterator,com.gp.cong.logisoft.domain.CarrierOceanEqptRates,com.gp.cong.logisoft.domain.Ports,com.gp.cong.logisoft.domain.CarrierAirlinePortException,java.util.Set,java.util.List,java.util.HashSet,java.util.ArrayList,com.gp.cong.logisoft.domain.CarrierAirline,com.gp.cong.logisoft.domain.CarriersOrLine,com.gp.cong.logisoft.domain.GenericCode"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<jsp:useBean id="carair"
	class="com.gp.cong.logisoft.struts.form.EditManagingCarriersOATForm"></jsp:useBean>
<%
	String path = request.getContextPath();	
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	DBUtil dbUtil=new DBUtil();
	CarriersOrLine carriers=null;
	GenericCode genericobj=null;
	CarrierOceanEqptRates oceanEqptRatesObj=null;
	Set carrierAirLineset=new HashSet<CarrierAirline>();
	Set carrierOceanEqptRatesSet=new HashSet<CarrierOceanEqptRates>();
	CarrierAirline carrierairlineobj=null;
	
	String abbr="";
	String aircod="";
	String alnact="";
	String acomyn="";
	String acomcd="";
	String acompc="";
	String msg="";
	String commision="";
	CarriersOrLine carrierOrline=new CarriersOrLine();
//carrierOrline.setEdiCarrier("I");

	String modify = null;
	modify=(String)session.getAttribute("modifyformanagingcarriers");
	session.setAttribute("airocean",modify);
	List eqptList=new ArrayList();
	Set portExceptionSet=new HashSet<Ports>();
	List portList=new ArrayList();
	String carriertype=null;
	List carriertypeList=new ArrayList();
	if(carriertypeList != null)
	{
		carriertypeList=dbUtil.getGenericCodeList(new Integer(17),"yes","Select Carrier Type");
		request.setAttribute("carriertypeList",carriertypeList);
	}
	if(request.getAttribute("message")!=null)
	{
		msg=(String)request.getAttribute("message");
	
	}
	if(session.getAttribute("view")!=null)
	{
		modify=(String)session.getAttribute("view");
		session.setAttribute("airocean",modify);
	}
	
	if(session.getAttribute("carriers") != null)
	{
		carriers=(CarriersOrLine)session.getAttribute("carriers");
		genericobj=carriers.getCarriertype();
		carriertype=genericobj.getId().toString();
		// changes in source code
		 if(carriers.getEdiCarrier()!=null && carriers.getEdiCarrier().equals("I"))
      {
       carair.setEdiCarrier("on");
       }else
       {
         carair.setEdiCarrier("off");
       }   	
		if(carriers.getEdiCarrier()!=null && carriers.getEdiCarrier()!="")
		{
		carrierOrline.setEdiCarrier(carriers.getEdiCarrier());
		}
		if(carriers.getCarrierAirlineSet()!=null)
		{
		carrierAirLineset=carriers.getCarrierAirlineSet();
		Iterator iter1 = carrierAirLineset.iterator();
		while(iter1.hasNext())
		{
			carrierairlineobj=(CarrierAirline)iter1.next();
			abbr=carrierairlineobj.getAirabbr();
			aircod=carrierairlineobj.getAircod();
			alnact=carrierairlineobj.getAlnact();
			acomyn=carrierairlineobj.getAcomyn();
			acomcd=carrierairlineobj.getAcomcd().toString();
			commision=carrierairlineobj.getAcomyn();
			if(carrierairlineobj.getAcompc()!=null)
			{
			acompc=carrierairlineobj.getAcompc().toString();
			}
			portExceptionSet=carrierairlineobj.getPortcodes();
			
			Iterator iter3=portExceptionSet.iterator();
			while(iter3.hasNext())
			{
				CarrierAirlinePortException airlinePortExceptionobj=(CarrierAirlinePortException)iter3.next();
				portList.add(airlinePortExceptionobj.getShedulenumber());
			}
				
		}
		}
		if(carriers.getCarrierOceanEqptSet()!=null)
		{
		carrierOceanEqptRatesSet=carriers.getCarrierOceanEqptSet();
		Iterator iter2 = carrierOceanEqptRatesSet.iterator();
		while(iter2.hasNext())
		{
			CarrierOceanEqptRates cOcean=new CarrierOceanEqptRates();
			oceanEqptRatesObj=(CarrierOceanEqptRates)iter2.next();
			if(oceanEqptRatesObj!=null)
			{
			cOcean.setEqpttype(oceanEqptRatesObj.getEqpttype());
			cOcean.setSpecialrate(oceanEqptRatesObj.getSpecialrate());
			eqptList.add(cOcean);
			}
		}
		}
		
		session.setAttribute("eqptList",eqptList);
		session.setAttribute("portLst",portList);
	}
	request.setAttribute("carrierOrline",carrierOrline);
CarrierAirline carrier = new CarrierAirline();
// for carriers
 if(acomyn!=null && acomyn.equals("Y"))
    {carair.setAcomyn("on");
    }
    else
    {
    carair.setAcomyn("off");
    }
 
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>Edit Managing Carriers OAT</title>
		<%@include file="../includes/baseResources.jsp" %>
                <%@include file="../fragment/formSerialize.jspf"  %>
		<script language="javascript" src="<%=path%>/js/common.js"></script>
		<script language="javascript" src="<%=path%>/js/isValidEmail.js"></script>
                <script language="javascript">
                    start = function(){
                        serializeForm();
                      }
                    window.onload = start;
                </script>
		<script language="javascript" type="text/javascript">
		function defaultSettings(val){
			if(val=="4" || val=="13"){
				document.getElementById("abbr").style.visibility = 'visible';
			}else{
				document.getElementById("abbr").style.visibility = 'hidden';
			}
			if(val=="4"){
				document.getElementById("divair").style.visibility = 'visible';
			}else{
				document.getElementById("divair").style.visibility = 'hidden';
			}
			if(val=="13"){
				document.getElementById("equiprate").style.visibility = 'visible';
				document.getElementById("fclcontactlabel").style.visibility = 'visible';
				document.getElementById("fclcontacttext").style.visibility = 'visible';
			}else{
				document.getElementById("equiprate").style.visibility = 'hidden';
				document.getElementById("fclcontactlabel").style.visibility = 'hidden';
				document.getElementById("fclcontacttext").style.visibility = 'hidden';
			}
		}
		//fuctions for checkboxes
		function chkall1(){
    		if(document.editManagingCarriersOATForm.ediCarrier.checked){
			    document.editManagingCarriersOATForm.ediCarrier.value="I";
			    document.editManagingCarriersOATForm.ediCarrier.focus();
			    return false;
    			}
        }
        // functions for the check boxes
		function chkall(){
    		if(document.editManagingCarriersOATForm.acomyn.checked){
			    document.editManagingCarriersOATForm.acomyn.value="Y";
			    document.editManagingCarriersOATForm.acomyn.focus();
			    return false;
     			}
        }
        //function to save  Carrier
		function save1(){
		
			if(document.editManagingCarriersOATForm.carrierType.value=="0"){
					alert("please select the carrier type");
					return;
				}
			var carriercode=document.editManagingCarriersOATForm.carriercode;
			if(carriercode.value==""){  
	 		  	  	 alert("Please enter the Carrier Code ");
	  			 	 carriercode.focus();
	  				 return;
  		 		}  
  		 	if(carriercode.value.match(" ")){
            	alert("Space is not allowed for Carrier Code");
            	carriercode.focus();
            	return;
            	}
            var carriername=document.editManagingCarriersOATForm.carriername;
			if(carriername.value==""){  
 		  	  	 alert("Please enter the Carrier Name ");
  			 	 carriername.focus();
  				 return;
  		 		}  
            if(isSpecial(carriercode.value)==false){
    			alert("Special Characters not allowed for Carrier Code.");
  				carriercode.value="";
    			carriercode.focus();
    			return;
	  			} 
  			if(document.editManagingCarriersOATForm.SCAC.value!=""){
	  			var SCAC=document.editManagingCarriersOATForm.SCAC;
	  		 	if(SCAC.value.match(" ")){
	            	alert("Space is not allowed for SCAC");
	            	SCAC.focus();
	            	return;
	            	}
	            if(isSpecial(SCAC.value)==false){
	    			alert("Special Characters not allowed for SCAC");
	  				SCAC.value="";
	    			SCAC.focus();
	    			return;
	  				}
  			}
  			if(document.editManagingCarriersOATForm.carrierType.value=="4"){
	  			if(document.editManagingCarriersOATForm.airabbr.value==""){
		  			alert("Please enter the AirLine ABBR");
		  			document.editManagingCarriersOATForm.airabbr.focus();
		  			return;
	  			}
  			if(document.editManagingCarriersOATForm.aircod.value==""){
	  			alert("Please enter the AirLine code");
	  			document.editManagingCarriersOATForm.aircod.focus();
	  			return;
	  			}
  			if(document.editManagingCarriersOATForm.commisionpercentage.value.length>6){
   				alert("Commission Percentage must not be more than 3 characters");
   				document.editManagingCarriersOATForm.commisionpercentage.value="";
    			document.editManagingCarriersOATForm.commisionpercentage.focus();
    			return;
    			}
  			}
  			document.editManagingCarriersOATForm.buttonValue.value="save";
   			document.editManagingCarriersOATForm.submit();
		}
		function cancelbtn(val){
			if(val==0 || val==3){
					document.editManagingCarriersOATForm.buttonValue.value="cancelview";
					document.editManagingCarriersOATForm.submit();
				}else{
		   			document.editManagingCarriersOATForm.buttonValue.value="cancel";
		   			var result = confirm("Would you like to save the changes?");
		   			if(result){
		   			   save1();
		   			}else{
		   			document.editManagingCarriersOATForm.submit();
		   			}
	 		   }
   		  }
   		  
 		function confirmdelete(){
			document.editManagingCarriersOATForm.buttonValue.value="delete";
    		var result = confirm("Are you sure you want to delete this Carrier");
			if(result){
   				document.editManagingCarriersOATForm.submit();
   			}	
   		}
   		
    	function disabled(val1,val2,val){
			if(val1 == 0 || val1== 3){		
	        	var imgs = document.getElementsByTagName('img');
	   			for(var k=0; k<imgs.length; k++){
	   		 		if(imgs[k].id != "cancel" && imgs[k].id!="portexception" && imgs[k].id!="note" && imgs[k].id!="equiprate"){
	   		    		imgs[k].style.visibility = 'hidden';
	   		 		}
	   			}
	   			var input = document.getElementsByTagName("input");
	   			for(i=0; i<input.length; i++){
		 			if(input[i].id != "buttonValue" && input[i].name!="carriercode" && input[i].name!="aircod"){
		  				input[i].readOnly=true;
						input[i].style.color="blue";
		  			}
	  	 		}
	   			var select = document.getElementsByTagName("select");
	   			for(i=0; i<select.length; i++){
					select[i].disabled=true;
	   			    select[i].style.backgroundColor="blue";
			    }
			    document.getElementById("delete").style.visibility = 'hidden';
	 		   	document.getElementById("save").style.visibility = 'hidden';
	  	 	}
  	 		if(val1 == 1){
  	 			document.getElementById("delete").style.visibility = 'hidden';
  	 			}
  	 		if(val1==3 && val2!=""){
				alert(val2);
				}		
  	 		if(val1==2){
  	 		//defaultSettings(val);
  	 			}
     	}	
     		
		function displayHidden(obj){
			if(document.editManagingCarriersOATForm.carrierType.value=="0"){
				alert("please select the carrier type");
				return;
				}
			document.editManagingCarriersOATForm.buttonValue.value="onchange";
			document.editManagingCarriersOATForm.submit();
			if(obj.value=="4"){
				document.getElementById("divair").style.visibility = 'visible';
			}else{
				document.getElementById("divair").style.visibility = 'hidden';
			}
			if(obj.value=="13"){
				document.getElementById("equiprate").style.visibility = 'visible';
				document.getElementById("abbr").style.visibility = 'visible';
				document.getElementById("fclcontactlabel").style.visibility = 'visible';
				document.getElementById("fclcontacttext").style.visibility = 'visible';
			}else{
				document.getElementById("equiprate").style.visibility = 'hidden';
				document.getElementById("abbr").style.visibility = 'hidden';
				document.getElementById("fclcontactlabel").style.visibility = 'hidden';
				document.getElementById("fclcontacttext").style.visibility = 'hidden';
			}
		}
		
	  function openPortExceptionList(){
        	mywindow=window.open("<%=path%>/jsps/datareference/airPortException.jsp","","width=650,height=450");
           	mywindow.moveTo(200,180);
      }  
        
	  function eqptRatesList(){
        	mywindow=window.open("<%=path%>/jsps/datareference/oceanException.jsp","","width=650,height=450");
           	mywindow.moveTo(200,180);
      }  
         
	  function confirmnote(){
		document.editManagingCarriersOATForm.buttonValue.value="note";
    	document.editManagingCarriersOATForm.submit();
	   	}
	   	
		var newwindow = '';
      function addform() {
           if (!newwindow.closed && newwindow.location){
             	newwindow.location.href = "<%=path%>/jsps/datareference/Claim.jsp";
           }else{
         		newwindow=window.open("<%=path%>/jsps/datareference/Claim.jsp","","width=600,height=300");
         		if (!newwindow.opener) newwindow.opener = self;
           }
           if (window.focus) {newwindow.focus()}
           		document.editManagingCarriersOATForm.buttonValue.value="";
            	document.editManagingCarriersOATForm.submit();
           		return false;
           }
            
       function toUppercase(obj) {
			obj.value = obj.value.toUpperCase();
		}
		</script>
		<%@include file="../includes/resources.jsp"%>
	</head>

	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>','<%=msg%>','<%=carriertype%>')" onkeydown="preventBack()">
		<html:form action="/editManagingCarriersOAT" scope="request">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableBorderNew">
				<tr class="tableHeadingNew"><td><bean:message key="form.managingCarriersOATForm.newcarrier" />
				<td align="right"><input type="button" class="buttonStyleNew" value="Save" name="save"
					onclick="save1()" />
				<input type="button" class="buttonStyleNew" value="Go Back"
					name="cancel" onclick="cancelbtn(<%=modify%>)" />
				<input type="button" class="buttonStyleNew" value="Delete"
					name="delete" onclick="confirmdelete()" />
				<input type="button" class="buttonStyleNew" value="Note" name="note"
					onclick="confirmnote()" disabled="true"/></td>
		       </tr>
				<tr><td colspan="2">
				<table width="100%" border="0" cellspacing="3" cellpadding="0">
				<tr>
					<td width="20%" class="style2"><bean:message key="form.managingCarriersOATForm.carriercode" /></td>
					<td>
						<html:text property="carriercode"
							value="<%=carriers.getCarriercode()%>" maxlength="5" size="3"
							styleClass="varysizeareahighlightgrey" readonly="true" />
					</td>
	                <td class="style2"> <bean:message key="form.managingCarriersOATForm.carriertype" />*</td>
					<td><html:select property="carrierType" styleClass="selectboxstyle" value="<%=carriertype%>" 
							onchange="displayHidden(this)"> <html:optionsCollection name="carriertypeList" /> </html:select>
				
				</tr>
				<tr>
					<td class="style2"> Carrier Name* </td>
					<td> <html:text property="carriername" value="<%=carriers.getCarriername()%>"
							style="text-transform:uppercase" maxlength="25" styleClass="areahighlightyellow1" /></td>
					<td class="style2"> SCAC </td>
					<td> <html:text property="SCAC" value="<%=carriers.getSCAC()%>" maxlength="4" style="text-transform:uppercase"  /></td>
				</tr>
				<%if(carriertype.equals("13")||carriertype.equals("4")){ %>
				<tr id="abbr">
					<td class="style2">
						Abbreviation
					</td>
					<td>
						<html:text property="abbreviation"
							value="<%=carriers.getAbbreviation()%>"
							style="text-transform:uppercase" maxlength="10" />
					</td>
                   <%}if(carriertype.equals("13")){ %>
					<td class="style2" id="fclcontactlabel"> FCL Contract Number</td>
					<td><html:text property="fclContactNumber" value="<%=carriers.getFclContactNumber()%>" maxlength="30" styleId="fclcontacttext" /></td>
				</tr>
				<tr class="style2">
					<td>
						EDI Carrier INTRA or GTNDXUS
					</td>
					<td>
						<table>
							<tr class="style2">
								<html:checkbox property="ediCarrier" name="carair"></html:checkbox>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="center">
						<span class="style2"> <input type="button"
								class="buttonStyleNew" value="Equip Rates" name="equiprate"
								onclick="return GB_show('Carriers', '<%=path%>/jsps/datareference/oceanException.jsp?button='+'code',300,600)" />
				</tr>

				<% } %>
			</table>
			</td>
			</tr>
			</table>

			<%if(carriertype.equals("4")){ 
 %>



			<table width="100%" border="0" class="tableBorderNew">

				<tr class="tableHeadingNew">
					Air Line Specific Details
				</tr>
				<tr class="textfieldstyle">
					<td class="style2" width="20%">
						Air Line Code
					</td>
					<%
      if(aircod!=null && !aircod.equals(""))
      { %>
					<td width="20%">
						<html:text property="aircod" value="<%=aircod%>" maxlength="3"
							onkeypress="return checkIts(event)"
							styleClass="varysizeareahighlightgrey" readonly="true" />
					</td>
					<%}else
      { %>
					<td width="100">
						<html:text property="aircod" value="<%=aircod%>" maxlength="3"
							onkeypress="return checkIts(event)" />
					</td>
					<%} %>

					<td width="100">
						&nbsp;
					</td>
					<td colspan="2" class="style2">
						<input type="button" class="buttonStyleNew" value="Port Exception"
							style="width: 90px" name="portexception"
							onclick="return GB_show('Carriers', '<%=path%>/jsps/datareference/airPortException.jsp?button='+'code',300,600)" />
					<td class="headerbluelarge">
						<input type="button" class="buttonStyleNew" value="AWB" name="awb"
							onclick="return GB_show('Carriers', '<%=path%>/jsps/datareference/Claim.jsp?button='+'code',300,600)" />
				</tr>
				<tr class="textfieldstyle">
					<td class="style2" width="100">
						Air Line ABBR
					</td>
					<td width="100">
						<html:text property="airabbr" value="<%=abbr%>" maxlength="2"
							style="text-transform:uppercase" />
					</td>
				</tr>
				<tr class="textfieldstyle">
					<td class="style2" width="100">
						Air Line Acct
					</td>
					<td width="100">
						<html:text property="alnact" value="<%=alnact%>" maxlength="20"
							style="text-transform:uppercase" />
					</td>
				</tr>
				<tr class="textfieldstyle">
				</tr>
				<tr class="textfieldstyle">
					<td width="15" class="style2">
						Commission Type
					</td>
					<td>
						<table>
							<tr>
								<td align="left">
									<html:checkbox property="acomyn" name="carair"
										onclick="chkall()">
									</html:checkbox>
								</td>
								<td width="75">
									<html:select property="commissiontype" value="<%=acomcd%>"
										styleClass="verysmalldropdownStyle">
										<html:option value="0">0</html:option>
										<html:option value="1">1</html:option>
									</html:select>
								</td>
								<td colspan="2" class="style2">
									(0 on Total,1 on 501 only)
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr class="textfieldstyle">
					<td width="113" class="style2">
						Commission %
					</td>
					<td width="85">
						<html:text property="commisionpercentage" value="<%=acompc%>"
							styleId="commper" maxlength="4"
							onkeypress="getDecimals(this,1,event)" />
					</td>
					<td width="121" class="style2">
						&nbsp;
					</td>
					<td width="89">
						&nbsp;
					</td>
					<td colspan="2" class="style2">
						&nbsp;
					</td>
				</tr>

			</table>
			<% } %>
			<html:hidden property="buttonValue" styleId="buttonValue" />
		</html:form>

	</body>
	 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
