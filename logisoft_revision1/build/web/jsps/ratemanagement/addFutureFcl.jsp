<%@ page language="java"  import="java.text.DateFormat,java.text.DecimalFormat,java.text.ParseException,
java.util.*, java.text.SimpleDateFormat,com.gp.cong.logisoft.domain.FclContainertypeCharges,
com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.FclBuyAirFreightCharges,
com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO,com.gp.cong.logisoft.domain.FclBuy,java.util.ArrayList,
java.util.List,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.FclBuyCostTypeFutureRates,
com.gp.cong.logisoft.domain.FclBuyCost"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<jsp:useBean id="fclrecordform" class="com.gp.cong.logisoft.struts.ratemangement.form.AddFCLRecordsForm" scope="request"/>   
<%
String path = request.getContextPath();
String costcode=""; 
FclBuy fclBuy=new FclBuy();
FclBuyCost fclBuycost=new FclBuyCost();
String terminalNumber="";
String terminalName="";
String destSheduleNumber="";
String destAirportname="";
String comCode="";
String comDesc="";
String message="";
String msg="";
String buy = "";
int ii=0;
String enddate="";
String startDate="";
List unitlist=new ArrayList();
String modify="";
String sslineno="";
String contact="";
String confirm="";
String costdesc="";
String costtype="";
String costname="";
String code="";
String costtypeName="";
DecimalFormat df=new DecimalFormat("0.00");
String process=null;
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
Date d=new Date();
String effdate=dateFormat.format(d);
String sslinename="";
List recordsFrightList=new ArrayList();;
List costcodelist=new ArrayList();;
List costtypelist=new ArrayList();;
List li=new ArrayList();
List unittypelist=new ArrayList();
//List unittypelist=new ArrayList();;
List recordsList=new ArrayList();
DBUtil dbUtil=new DBUtil();
DBUtil dbutil=new DBUtil();
Set editCostType=new HashSet();
String valid="";
String pcctc="";
String pccmi="";
String pcftf="";
String pcretail="";
String amount="";
String markup="";
List unitList=new ArrayList();
List airFreightList=new ArrayList();
List l2=new ArrayList();
List airTypelist=new ArrayList();
%>
<%

String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
List sessionList=(List)session.getAttribute("costFutureCodeList");
	if(session.getAttribute("costFutureCodeList")==null){
	
	valid="Please Select At least One Cost Code! Else select previous";
	}
	if(request.getAttribute("start")!=null){
	startDate=(String)request.getAttribute("start");
	}
	if(request.getAttribute("end")!=null){
	enddate=(String)request.getAttribute("end");
	}
	if(session.getAttribute("editrecords")!=null){
	process=(String)session.getAttribute("editrecords");
	}

	String editPath=path+"/addFCLRecords.do";
	FclBuyCostTypeFutureRates fclBuyCostTypeRates=new FclBuyCostTypeFutureRates();
	fclrecordform.setStandard("off");
	
	if(session.getAttribute("futureaddfclrecords")!=null)
	{
	session.removeAttribute("fclmessage");
	fclBuy=(FclBuy)session.getAttribute("futureaddfclrecords");
	if(fclBuy.getOriginTerminal()!=null)
	{
	terminalNumber=fclBuy.getOriginTerminal().getUnLocationCode();
	terminalName=fclBuy.getOriginTerminal().getUnLocationName();
	}
	if(fclBuy.getDestinationPort()!=null)
	{
	destSheduleNumber=fclBuy.getDestinationPort().getUnLocationCode();
	destAirportname=fclBuy.getDestinationPort().getUnLocationCode();
	}
	if(fclBuy.getComNum()!=null)
	{
	comCode=fclBuy.getComNum().getCode();
	comDesc=fclBuy.getComNum().getCodedesc();
	}
	if(fclBuy.getSslineNo()!=null){
	sslineno=fclBuy.getSslineNo().getAccountNo();
	sslinename=fclBuy.getSslineNo().getAccountName();
	//contact=fclBuy.getSslineNo().getFclContactNumber();
	}
	if(fclBuy.getContract()!=null){
	contact=fclBuy.getContract().toString();
	}
	if(fclBuy.getStartDate()!=null)
	{
	startDate=dateFormat.format(fclBuy.getStartDate());
		
	}
	if(fclBuy.getEndDate()!=null)
	{
	enddate=dateFormat.format(fclBuy.getEndDate());
			
	}
	confirm="records";
	}
	if(session.getAttribute("con")!=null){
	fclBuy=(FclBuy)session.getAttribute("con");
	contact=fclBuy.getContract();
	if(fclBuy.getStartDate()!=null)
	{
	startDate=dateFormat.format(fclBuy.getStartDate());
		
	}
	if(fclBuy.getEndDate()!=null)
	{
	enddate=dateFormat.format(fclBuy.getEndDate());
			
	}
	}

	FclBuyCost fclBuyCost=new FclBuyCost();
	if(session.getAttribute("costFutureCodeList")!=null)
	{
		recordsList=(List)session.getAttribute("costFutureCodeList");
	for(int i=0;i<recordsList.size();i++)
	{
		fclBuyCost=(FclBuyCost)recordsList.get(i);
	
	if(fclBuyCost.getFclBuyFutureTypesSet()!=null)
	{
		Iterator iter=fclBuyCost.getFclBuyFutureTypesSet().iterator();
	
	while(iter.hasNext())
	{
		fclBuyCostTypeRates=(FclBuyCostTypeFutureRates)iter.next();
		unitList.add(fclBuyCostTypeRates);
	}
	}
	if(fclBuyCost.getFclBuyAirFreightSet()!=null)
	{
		Iterator iter=fclBuyCost.getFclBuyAirFreightSet().iterator();
		while(iter.hasNext())
		{
			FclBuyAirFreightCharges fclBuyAirFreightCharges=(FclBuyAirFreightCharges)iter.next();
			recordsFrightList.add(fclBuyAirFreightCharges);
		}
		}
		}
	}
	if(session.getAttribute("fclfrightrecords")!=null)
	{
	airFreightList=(List)session.getAttribute("fclfrightrecords");
	}
	List unitTypeList=new ArrayList();
	if(session.getAttribute("fclrecordsfuture")!=null)
	{
	unitTypeList=(List)session.getAttribute("fclrecordsfuture");
	}
	if(costcodelist != null)
	{
		costcodelist=dbUtil.getGenericCodeCostList(new Integer(36),"no","Select Cost Code");
		request.setAttribute("costFutureCodeList",costcodelist);
		 
	}
	if(costtypelist != null)
	{
		costtypelist=dbUtil.getGenericCodetypeList(new Integer(37),"yes","Select Cost type");
		request.setAttribute("costtypelist",costtypelist);
		 
	}
	String codesc="";
	if(session.getAttribute("costcode")!=null){
		fclBuycost=(FclBuyCost)session.getAttribute("costcode");
		
		if(fclBuycost!=null && fclBuycost.getCostId()!=null){
		costdesc=fclBuycost.getCostId().getCodedesc();
	
		code=fclBuycost.getCostId().getCode().toString();
		}
		if(fclBuycost.getContType()!=null)
		{
		costtype=fclBuycost.getContType().getId().toString();
		codesc=fclBuycost.getContType().getCodedesc();
		}
	 }
		request.setAttribute("defaultcurrency",dbUtil.getGenericFCL(new Integer(32),"yes"));
		if(session.getAttribute("fclrecordsfuture")!=null)
		{
		
		recordsList=(List)session.getAttribute("fclrecordsfuture");
		for(int i=0;i<recordsList.size();i++){
			   
   				FclBuyCostTypeFutureRates	frt=(FclBuyCostTypeFutureRates)recordsList.get(i);
   				if(costdesc!=null && frt!=null && costdesc.equals(frt.getCostCode())){
   				li.add(frt);
   				}
   				}
	 	   }
	    
	if(unittypelist != null)
		{
		unittypelist=dbUtil.getUnitFCLFutureType(new Integer(38),"yes","Select Unit code",li);
		request.setAttribute("unittypelist",unittypelist);
		}
		
		if(session.getAttribute("fclfrightrecords")!=null)
		{
		airTypelist=(List)session.getAttribute("fclfrightrecords");
		
		for(int i=0;i<airTypelist.size();i++){
   				FclBuyAirFreightCharges	frt=(FclBuyAirFreightCharges)airTypelist.get(i);
   				if(costdesc!=null && frt!=null && costdesc.equals(frt.getCostCode())){
				l2.add(frt);
   				}
   				}
		    }
	if(airTypelist != null)
	{
		airTypelist=dbUtil.getWeightFCLList(new Integer(31),"yes","Select Unit code",l2);
		request.setAttribute("wightlist",airTypelist);
	}

	if(session.getAttribute("usermessage")!=null){
		message=(String)session.getAttribute("usermessage");
		}
	if(request.getAttribute("message")!=null){
		msg=(String)request.getAttribute("message");
		}	
	if(session.getAttribute("view")!=null){
		modify=(String)session.getAttribute("view");
		}
	if(request.getAttribute("editflaterate")!=null && !request.getAttribute("editflaterate").equals(""))
		{
			if(request.getAttribute("buy")!=null)
			{
			    buy=(String)request.getAttribute("buy");
			}
			 ii = Integer.parseInt((String)request.getAttribute("editflaterate"));
			
		}	
%>

<html>
	<head>
	<title>JSP for AddLclColoadCommodityForm form</title>
	<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" type="text/javascript">
				
       	function submit1()
		{
			document.addFutureFclForm.buttonValue.value="costcode";
			document.addFutureFclForm.costtype.value="0";
			document.addFutureFclForm.submit();
		}
		function costtypechange()
		{
		
			if(document.addFutureFclForm.costcode.value=="0")
			{
				alert("Please select cost code !");
			}
			else{
				document.addFutureFclForm.buttonValue.value="costType";
				document.addFutureFclForm.submit();
			}
		}
		
		function addForm(val1)
		{
			//alert(val1);
			if(document.addFutureFclForm.costcode.value=="" && document.addFutureFclForm.costcode.value=="0")
			{
				alert("Please select cost code !");
			}
			else if(document.addFutureFclForm.costtype.value=="" && document.addFutureFclForm.costtype.value=="0")
			{
				alert("Please select Cost Type !");
			}
			else if(document.addFutureFclForm.currency.value=="" && document.addFutureFclForm.currency.value=="0")
			{
				 alert("Please select Currency type  !");
				 
			}
			else if(document.addFutureFclForm.efdate.value=="")
			{
				 alert("Please select Effective date  !");
				 
			}
			else if(val1!=null&&val1==11300)
			{
				 if(document.addFutureFclForm.unittype.value!="" && document.addFutureFclForm.unittype.value=="0")
					{
					alert("Please select Unit Type !");
					}
					else{
					document.addFutureFclForm.buttonValue.value="add";
					document.addFutureFclForm.submit();
					}
			}
			/*else if(val1!=null && val1==11306)
			{
					if(document.addFutureFclForm.range.value!=null&&document.addFutureFclForm.range.value=="0")
					{
						alert("Please select Wight Range !");
					}else{
					document.addFutureFclForm.buttonValue.value="add";
					document.addFutureFclForm.submit();
					}
			}*/
			else{
			
				document.addFutureFclForm.buttonValue.value="add";
				document.addFutureFclForm.submit();
			}
		}
		function delete1()
		{
			document.addFutureFclForm.buttonValue.value="delete";
			document.addFutureFclForm.submit();
		}
		function saveForm1(val,val2)
		{
		
			
			document.addFutureFclForm.buttonValue.value="edit";
			document.addFutureFclForm.submit();
			
		}
		function cancelForm(val1,val2)
		{
			
			if(val1!="edit"){
				var1=confirm("Do u want to save this record");
				if(var1){
				saveForm1(val1,val2);
				}
			
				else{
				document.addFutureFclForm.buttonValue.value="cancel";
				document.addFutureFclForm.submit();
				}
			}else{
			document.addFutureFclForm.buttonValue.value="cancel";
			document.addFutureFclForm.submit();
			}
		}
		function noteForm()
		{
			document.addFutureFclForm.buttonValue.value="note";
			document.addFutureFclForm.submit();
		}
		function disabled(val1,val2,val3,val4) {
			if(val4!="" && val4!="edit")
				{
				var imgs = document.getElementsByTagName('img');
	       	     	for(var k=0; k<imgs.length; k++)	{
	   		 			 if(imgs[k].id=="note"||imgs[k].id=="delete" ||imgs[k].id=="fclcopy")	 {
	   		 			     imgs[k].style.visibility = 'hidden';
	   						 }
	   					}
				}
			if(val3!=""){
			var input = document.getElementsByTagName("input");
			   		for(i=0; i<input.length; i++)	{
			  			input[i].readOnly=true;
					 	}
					 	
		  	  	 var imgs = document.getElementsByTagName('img');
	       	     	for(var k=0; k<imgs.length; k++)	{
	   		 			 if(imgs[k].id=="add")	 {
	   		 			     imgs[k].style.visibility = 'hidden';
	   						 }
	   					}
			}
			if(val1!=""&&(val1 == 0 || val1== 3))
			{
				
		  	  	 var imgs = document.getElementsByTagName('img');
	       	     	for(var k=0; k<imgs.length; k++)	{
	   		 			 if(imgs[k].id != "cancel"&&imgs[k].id != "note" || imgs[k].id=="fclcopy")	 {
	   		 			     imgs[k].style.visibility = 'hidden';
	   						 }
	   					}
		   		var input = document.getElementsByTagName("input");
			   		for(i=0; i<input.length; i++)	{
			  			input[i].readOnly=true;
					 	}
		  	 	var textarea = document.getElementsByTagName("textarea");
			  	 	for(i=0; i<textarea.length; i++){
			 			textarea[i].readOnly=true;		 					
					  	}
	   			var select = document.getElementsByTagName("select");
	   		   		for(i=0; i<select.length; i++)	{
				 		select[i].disabled=true;
						select[i].style.backgroundColor="blue";
		  		  	 	}
		  		  	 	document.getElementById("delete").style.visibility = 'hidden';
		  		  	 	document.getElementById("save").style.visibility = 'hidden';
	  	 		}
	  	 		
		  	 if(val1 == 1)	 {
	  	 		 	 document.getElementById("delete").style.visibility = 'hidden';
	  	 		}
	  	 	if(val1==3 && val2!=""){
				
				alert(val2);
				
				}
		 
		 var tables1 = document.getElementById('includedtable');
		
  		 if(tables1!=null)
  		 {
  	 	 displaytagcolor();
   		// initRowHighlighting();
   		// setWarehouseStyle();
   		 }
  		
  		 var tables = document.getElementById('includedcomtable');
  		
  		 if(tables!=null)
  		 {
  		
  	 	 displaytagcolornon();
   		// initRowHighlightingnon();
   		 //setWarehouseStylenon();
   		 }		
    }
    		 function displaytagcolornon()
			{
		 	 	var datatableobj1 = document.getElementById('includedcomtable');
		
				for(i=0; i<datatableobj1.rows.length; i++)
				{
					var tablerowobj = datatableobj1.rows[i];
					if(i%2==0)
					{
						tablerowobj.bgColor='#FFFFFF';
					}
					else
					{
						tablerowobj.bgColor='#E6F2FF';
					}
					if(tablerowobj.cells[0].innerHTML=="com")
					{
//					tablerowobj.bgColor='#FFB7DB';
					
					}else if(tablerowobj.cells[0].innerHTML=="acc")
					{
					tablerowobj.bgColor='#FF3300';
					
					}
		 	 	}
	}
    function displaytagcolor()
	{
		 var datatableobj = document.getElementById('includedtable');
		
		for(i=0; i<datatableobj.rows.length; i++)
				{
					var tablerowobj = datatableobj.rows[i];
					if(i%2==0)
					{
						tablerowobj.bgColor='#FFFFFF';
					}
					else
					{
						tablerowobj.bgColor='#E6F2FF';
					}
					if(tablerowobj.cells[0].innerHTML=="com")
					{
//					tablerowobj.bgColor='#FFB7DB';
					
					}else if(tablerowobj.cells[0].innerHTML=="acc")
					{
					tablerowobj.bgColor='#FF3300';
					
					}
		 	 	}
		 	 	
	}
	function initRowHighlighting()
  	{
			if (!document.getElementById('includedtable')){ return; }
			 var tables = document.getElementById('includedtable');
			attachRowMouseEvents(tables.rows);
		
	}
	function attachRowMouseEvents(rows)
	{
		for(var i =1; i < rows.length; i++)
		{
			var row = rows[i];
			row.onmouseover =	function() 
			{ 
				this.className = 'CCFFFF';
			}
			row.onmouseout =	function() 
			{ 
				this.className = '';
			}
            row.onclick= function() 
			{ 
			}
			}
		
		}
		function initRowHighlightingnon()
  	{
			if (!document.getElementById('includedcomtable')){ return; }
			 var tables = document.getElementById('includedcomtable');
			attachRowMouseEventsnon(tables.rows);
		
	}
	function attachRowMouseEventsnon(rows)
	{
		for(var i =1; i < rows.length; i++)
		{
			var row = rows[i];
			row.onmouseover =	function() 
			{ 
				this.className = 'rowin';
			}
			row.onmouseout =	function() 
			{ 
				this.className = '';
			}
            row.onclick= function() 
			{ 
			}
			}
		
		}
		function setWarehouseStylenon()
	{

		if(document.serachUniversalForm.buttonValue.value=="searchall")
		{
		  	 var x=document.getElementById('includedcomtable').rows[0].cells;
		     x[0].className="sortable sorted order1";
		} 
		if(document.serachUniversalForm.buttonValue.value=="search")
		{
		  	var input = document.getElementsByTagName("input");
		  	
	  	    if(!input[0].value=="")
	  		{
	  		 	var x=document.getElementById('includedcomtable').rows[0].cells;	
	  		   	x[0].className="sortable sorted order1";
	  		}
	  		else if(!input[4].value=="")
	  		{	
	  		 	var x=document.getElementById('includedcomtable').rows[0].cells;	
	  		   	x[2].className="sortable sorted order1";
	  		}
	  		else if(!input[6].value=="")
	  		{	
	  		 	var x=document.getElementById('includedcomtable').rows[0].cells;	
	  		   	x[4].className="sortable sorted order1";
	  		}
	  		
	  	  }
	 }
	function setWarehouseStyle()
	{

		if(document.serachUniversalForm.buttonValue.value=="searchall")
		{
		  	 var x=document.getElementById('includedtable').rows[0].cells;
		     x[0].className="sortable sorted order1";
		} 
		if(document.serachUniversalForm.buttonValue.value=="search")
		{
		  	var input = document.getElementsByTagName("input");
		  	
	  	    if(!input[0].value=="")
	  		{
	  		 	var x=document.getElementById('includedtable').rows[0].cells;	
	  		   	x[0].className="sortable sorted order1";
	  		}
	  		else if(!input[4].value=="")
	  		{	
	  		 	var x=document.getElementById('includedtable').rows[0].cells;	
	  		   	x[2].className="sortable sorted order1";
	  		}
	  		else if(!input[6].value=="")
	  		{	
	  		 	var x=document.getElementById('includedtable').rows[0].cells;	
	  		   	x[4].className="sortable sorted order1";
	  		}
	  		
	  	  }
	 }
		function nonTest(val1){
			
			document.addFutureFclForm.buttonValue.value="index";
			document.addFutureFclForm.index.value=val1;
			document.addFutureFclForm.buy.value="buy";
			document.addFutureFclForm.submit();
			
			mywindow=window.open("<%=path%>/jsps/ratemanagement/futureFclEdit.jsp?flaterate="+val1+"&buy=buy","","width=800,height=300");
        mywindow.moveTo(200,180);		
		}
	
		function comTest(val1){
			
			document.addFutureFclForm.buttonValue.value="index";
			document.addFutureFclForm.index.value=val1;
			document.addFutureFclForm.submit();
			mywindow=window.open("<%=path%>/jsps/ratemanagement/fcladdedit.jsp?flaterate="+val1,"","width=900,height=400");
       		 mywindow.moveTo(200,180);		
		}
		function getHistory()
		{
		     document.addFutureFclForm.buttonValue.value="gethistory";
			 document.addFutureFclForm.submit();
		}
		
		</script>

</head>

<body class="whitebackgrnd"  onLoad="disabled('<%=modify%>','<%=message%>','<%=msg%>','<%=process%>')">
<html:form   action="/addFutureFcl" scope="request">
<font color="blue" size="4"><%=msg%></font>
<div align="center">
  	<input type="button" value="Save" id="save" onclick="saveForm1('<%=process%>','<%=valid%>')" class="buttonStyleNew" />
		   <input type="button" value="Delete" onclick="delete1()" id="delete" class="buttonStyleNew" />
		   	<input type="button" value="Go Back" onclick="cancelForm('<%=process%>','<%=valid%>')"" id="cancel" class="buttonStyleNew" />
		   	<input type="button" value="Note" onclick="noteForm()" id="note" class="buttonStyleNew" />
		
</div>
<table  cellpadding="2" cellspacing="2" width="100%" border="0" class="tableBorderNew">
	<tr class="tableHeadingNew" height="90%"><b>Add FCL CostCodes  Rates</b> </tr>
	
	<tr class="textlabels">
  <td >OriginTerminal</td>
  <td ><html:text property="orgTerminal" styleClass="middumestylegrey" readonly="true" disabled="disabled" value="<%= terminalNumber%>"></html:text>
    </td>
  <td > OriginTerminal Name </td>
  <td ><html:text property="orgName"  styleClass="middumestylegrey" readonly="true"   value="<%=terminalName %>"/></td>
  <td >Dest Port</td>
  <td ><html:text property="destnum" styleClass="middumestylegrey"  readonly="true" disabled="disabled" value="<%= destSheduleNumber%>"/>
</td>
  <td>Dest Port Name </td>
  <td><html:text property="destname" styleClass="middumestylegrey"  readonly="true" value="<%= destAirportname%>"/></td>
  </tr >
   <tr class="textlabels">
  <td >Com Code </td>
  <td><html:text property="comcode" readonly="true" styleClass="middumestylegrey" value="<%= comCode%>"/>
    </td>
  <td >Com Description</td>
  <td><html:text property="comdesc"  readonly="true" styleClass="middumestylegrey"  value="<%=comDesc %>"/></td>
  <td >SS Line Code</td>
  <td ><html:text property="sslineno" readonly="true" styleClass="middumestylegrey" value="<%= sslineno%>"/>
   </td>
   <td >SS Line Name</td>
  <td ><html:text property="sslinename" styleClass="middumestylegrey" readonly="true" value="<%=sslinename %>"/></td>
   
 </tr>
	<tr class="textlabels">
		<td>StartDate</td>
	<td><html:text property="startDate" size="9" styleId="txtitemcreatedon"
					readonly="true" value="<%=startDate%>" />
	<img src="<%=path%>/img/CalendarIco.gif" alt="cal" align="top" id="itemcreatedon" onmousedown="insertDateFromCalendar(this.id,0);" />
	</td>
	
		 <td  >EndDate</td>
		  <td>
						<html:text property="endDate" styleId="txtEitemcreatedon" 
							size="9" readonly="true" value="<%=enddate%>"/>
						<img src="<%=path%>/img/CalendarIco.gif" alt="cal" align="top"
								id="Eitemcreatedon"
								onmousedown="insertDateFromCalendar(this.id,0);" />
						
					</td>
		<td>
		Contratc Name
		</td>
		<td colspan="3"><html:text property="contact"  maxlength="40" size="30"  value="<%=contact%>"/>
			
		</td>
		
	</tr>
	<tr class="textlabels">
		<td  valign="bottom">Cost Code</td>
		 <td ><html:select property="costcode" styleClass="verysmalldropdownStyle" onchange="submit1()">
		   <html:optionsCollection name="costFutureCodeList"/>    
		  </html:select></td>
		  <td  valign="bottom">Cost description</td>
		    <td ><html:text property="costdesc"  styleClass="verysmalldropdownStyle"  value="<%=costdesc %>"/></td>
		<td  >Cost Type </td>
		<td colspan="3"><html:select property="costtype"  onchange="costtypechange()">
		<html:optionsCollection name="costtypelist"/>    
		  </html:select></td>
	</tr>

<tr>
</tr></table>
<br>
<%if(codesc!=null && !codesc.equals("")){ %>
<table  class="tableBorderNew" width="100%">
<tr class="tableHeadingNew" height="90%"><b>Add Rates for <%= codesc%> </tr>
<tr  class="textlabels">
<%

if(codesc!=null && codesc.trim().equalsIgnoreCase("Flat Rate Per Container Size"))
{

%>
	<td >Unit Type</td>
	<td  ><html:select property="unittype" styleClass="shortselectstyle1">
  <html:optionsCollection name="unittypelist"/>  </html:select> </td>
	<td >Amount</td>
	 <td ><html:text property="amount"  onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"   value=""/></td>
	<td >Markup</td>
	  <td ><html:text property="markup"  onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"     value=""/></td>
	<td >Standard</td>
	  <td ><html:checkbox property="standard" name="fclrecordform" ></html:checkbox></td>
<%}
else if(codesc!=null && codesc.trim().equalsIgnoreCase("Per CBM")||codesc.trim().equalsIgnoreCase("per LBS")||codesc.trim().equalsIgnoreCase("Per 1000KG"))
{
%>
  <td  width="70">Retail</td>
  <td><html:text property="pcretail"  onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"    value=""/></td>
  <td >CTC</td>
  <td><html:text property="pcctc"  onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"   value="" /></td>
  <td >FTF</td>
  <td><html:text property="pcftf" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"    value=""/></td>
  <td >Minimun</td>
  <td><html:text property="pcminimun" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"      value=""/></td>
  <%}
   else if(codesc!=null && codesc.trim().equalsIgnoreCase("PERCENT OFR")|| codesc.trim().equalsIgnoreCase("Per 2000 lbs")||codesc.trim().equalsIgnoreCase("Per Dock Receipts")||codesc.trim().equalsIgnoreCase("Per Cubic Foot")||codesc.trim().equalsIgnoreCase("PER BL CHARGES") ||codesc.trim().equalsIgnoreCase("Flat Rate Per Container"))
 {%>
  <td width="70">Retail</td>
    <td><html:text property="pcretail"  onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"   styleClass="verysmalldropdownStyle"  value=""/></td>
  
 <%}%>
<%-- else if(codesc!=null && codesc.trim().equalsIgnoreCase("Air Freight Costs"))--%>
<%--{%>--%>
<%----%>
<%-- <td width="68"><span class="textlabels">Range </span></td>--%>
<%-- <td><html:select property="range" styleClass="verysmalldropdownStyle"  onchange="costtypechange()">--%>
<%--	<html:optionsCollection name="wightlist"/>    --%>
<%--  	</html:select></td>--%>
<%-- <td width="121"><span class="textlabels">Air Freight Amount </span></td>--%>
<%--   <td width="105"><html:text property="afamount"   onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"    value=""/></td>--%>
<%-- --%>
<%--<%} %>--%>
<%--<td>--%>
	Currency
</td>
	
      <td colspan="4">&nbsp;<html:select property="currency" value="USD"> 
      <html:optionsCollection name="defaultcurrency" /> 
      </html:select>  
      </td>
      <td>EffectiveDate</td>
      <td>
			<html:text property="efdate" styleId="txtEFDitemcreatedon" styleClass="verysmalldropdownStyle"
				size="7" readonly="true" value="<%=effdate%>" /></td>
	  <td>
			<div>
			<img src="<%=path%>/img/CalendarIco.gif" alt="cal" align="top"
			id="EFDitemcreatedon"	onmousedown="insertDateFromCalendar(this.id,0);" />
						</div>
		</td>  
	     <td>
	    	 <input type="button" value="Add To List" onclick="addForm('<%=costtype%>')" id="add" class="buttonStyleNew" />
	    </td>
</tr>
</table>
<br>
<%}if(airFreightList!=null && airFreightList.size()>0) {%>
<table  class="tableBorderNew" width="100%">
<tr class="tableHeadingNew" height="90%"><b>FCL Air Freight Rates </tr>
		<tr>
		<td>
		<div id="divtablesty1" class="scrolldisplaytable">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <% 
        int i=0;
    
        %>
        <display:table name="<%=airFreightList%>" pagesize="<%=pageSize%>" class="displaytagstyle"  sort="list" id="includedtable" style="width:100%"> 
			<% String  tempPath1="";
				
				String costType="";
				String wightRange="";
				String currencyAir="";
					
					//String costType="";
					String rateAmount="";
			String iStr=String.valueOf(i);
			tempPath1=path+"/jsps/ratemanagement/AddFCLRecords.jsp";
			if(airFreightList!=null && airFreightList.size()>0)
			{
				
			FclBuyAirFreightCharges fclBuyAirFreight=(FclBuyAirFreightCharges)airFreightList.get(i);
			
			 if(fclBuyAirFreight.getWieghtRange()!=null)
					 {
					 wightRange=fclBuyAirFreight.getWieghtRange().getCodedesc();
					 }
					 if(fclBuyAirFreight.getRatAmount()!=null)
					 {
					     rateAmount	=df.format(fclBuyAirFreight.getRatAmount());			
					 }
					
					 if(fclBuyAirFreight.getCostCode()!=null)
					 {
					 costcode=fclBuyAirFreight.getCostCode();
					 }
					 if(fclBuyAirFreight.getCostType()!=null)
					 {
					 costType=fclBuyAirFreight.getCostType();
					 }
					 if(fclBuyAirFreight.getCostId()!=null)
					 {
					
					 }
			}
			 %>
			<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Item Details Displayed,For more Items click on Page Numbers.
    				<br>
    				</span>
  			  </display:setProperty>
  			  <display:setProperty name="paging.banner.one_item_found">
    				<span class="pagebanner">
						One {0} displayed. Page Number
					</span>
  			  </display:setProperty>
  			  <display:setProperty name="paging.banner.all_items_found">
    				<span class="pagebanner">
						{0} {1} Displayed, Page Number
					</span>
  			  </display:setProperty>
    			<display:setProperty name="basic.msg.empty_list"><span class="pagebanner">
					  No Records Found.
					</span>
				</display:setProperty>
				<display:setProperty name="paging.banner.placement" value="bottom" />
			<display:setProperty name="paging.banner.item_name" value="Item"/>
  			<display:setProperty name="paging.banner.items_name" value="Items"/>
  			<display:column  title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cost Code" >
  			<a href="<%=tempPath1%>" onclick="comTest('<%=iStr%>')">
  			<%=costcode%></a></display:column>
  			
  			<display:column  title="Cost type" ><%=costType%></display:column>
  			<display:column  title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Wieght Range ">
  			
  			<%=wightRange%>
  			</display:column>
  			<display:column title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Air Fright Amount" ><%=rateAmount%></display:column>
  			<display:column title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Currency" ><%=currencyAir%></display:column>
      		
			
  			<%
  			 i++;
  			%> 
  		   	   
  			
		     	    		
       		
		</display:table>
        
        </table></div>  
    	</td> 
  		 </tr>  
		</table>
		<%}if(unitTypeList!=null && unitTypeList.size()>0){ %>
		<br>
		<table  class="tableBorderNew" width="100%">
	<tr class="tableHeadingNew" height="100%"><b>FCL Cost Code Rates </tr>
		<tr>
		<td>
		<div id="divtablesty1" class="scrolldisplaytable">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <% 
     
        int j=0;
        %>
        <display:table name="<%=unitTypeList%>" pagesize="<%=pageSize%>" class="displaytagstyle"  sort="list" id="includedcomtable" style="width:100%"> 
<% String  tempPath1="";
	String unitycode="";
	
	
	String costType="";
	String ratAmount="";
	String ctcAmt="";
	String ftfAmt="";
	String minimumAmt="";
	String activeAmt="";
	String markUp="";
	String standard="";
	String curreny="";
	String efd="";
	Date efdate=null;
	//String costType="";
	String iStr=String.valueOf(j);
				
				tempPath1=path+"/jsps/ratemanagement/addFutureFcl.jsp";

			if(unitTypeList!=null && unitTypeList.size()>0)
			{
				
			FclBuyCostTypeFutureRates fclBuyCostType=(FclBuyCostTypeFutureRates)unitTypeList.get(j);
			if(fclBuyCostType.getUnitType()!=null)
					 {
					 unitycode=fclBuyCostType.getUnitType().getCodedesc();
					 }
					 if(fclBuyCostType.getRatAmount()!=null)
					 {
					 
					 ratAmount=df.format(fclBuyCostType.getRatAmount());
					
					}
					 if(fclBuyCostType.getFtfAmt()!=null)
					 {
						  ftfAmt=df.format(fclBuyCostType.getFtfAmt());
					 }
					
					 if(fclBuyCostType.getCtcAmt()!=null)
					 {
							 ctcAmt=df.format(fclBuyCostType.getCtcAmt());
					 }
					 if(fclBuyCostType.getEffectiveDate()!=null)
					 {
					
					efd=dateFormat.format(fclBuyCostType.getEffectiveDate());
					  
					   }
					 
					 if(fclBuyCostType.getMinimumAmt()!=null)
					 {
							minimumAmt=df.format(fclBuyCostType.getMinimumAmt());
					 }
				
					 if(fclBuyCostType.getActiveAmt()!=null)
					 {
					 
					 activeAmt=df.format(fclBuyCostType.getActiveAmt());
					 
					 }
					
					 if(fclBuyCostType.getMarkup()!=null)
					 {
					
					  markUp=df.format(fclBuyCostType.getMarkup());
					 
					 }
					
					 if(fclBuyCostType.getStandard()!=null)
					 {
					 standard=fclBuyCostType.getStandard();
					 }
					 if(fclBuyCostType.getCostCode()!=null)
					 {
					 costcode=fclBuyCostType.getCostCode();
					 }
					 if(fclBuyCostType.getCostType()!=null)
					 {
					 costType=fclBuyCostType.getCostType();
					 }
					 if(fclBuyCostType.getCurrency()!=null){
					 curreny=fclBuyCostType.getCurrency().getCodedesc().substring(0,3);
					 }

			}
%>
<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Item Details Displayed,For more Items click on Page Numbers.
    				<br>
    				</span>
  			  </display:setProperty>
  			  <display:setProperty name="paging.banner.one_item_found">
    				<span class="pagebanner">
						One {0} displayed. Page Number
					</span>
  			  </display:setProperty>
  			  <display:setProperty name="paging.banner.all_items_found">
    				<span class="pagebanner">
						{0} {1} Displayed, Page Number
					</span>
  			  </display:setProperty>
    			<display:setProperty name="basic.msg.empty_list"><span class="pagebanner">
					  No Records Found.
					</span>
				</display:setProperty>
				<display:setProperty name="paging.banner.placement" value="bottom" />
				<display:setProperty name="paging.banner.item_name" value="Item"/>
	  			<display:setProperty name="paging.banner.items_name" value="Items"/>
	  			<display:column  title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cost Code"><a href="<%=tempPath1%>" onclick="nonTest('<%=iStr%>')"><%=costcode%></a>	</display:column>
  				<display:column  title="Cost type" ><%=costType%></display:column>
  				<display:column title="Retail"><%=ratAmount%></display:column>
  				<display:column title="Unit_Type"><%=unitycode%></display:column>  			
  				<display:column title="Amount"><%=activeAmt%></display:column>
				<display:column title="Markup" ><%=markUp%></display:column>
				<display:column title="FTFAmt" ><%=ftfAmt%></display:column>
				<display:column title="CTCAmt"><%=ctcAmt%></display:column>
				<display:column title="MinimumAmt" ><%=minimumAmt%></display:column>
				<display:column title="Standard" ><%=standard%></display:column>
				<display:column title="EffectiveDate" ><%=efd%></display:column>
				<display:column title="Curreny" ><%=curreny%></display:column>
 <%
  			 j++;
  			
%> 
  		   	   
  			
		     	    		
       		
		</display:table>
        
        </table></div>  
    	</td> 
  		 </tr>  
		</table>
		<%} %>
<html:hidden property="buttonValue" styleId="buttonValue"/>	
<html:hidden property="buy" />
<html:hidden property="index" />	


</html:form>

</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

