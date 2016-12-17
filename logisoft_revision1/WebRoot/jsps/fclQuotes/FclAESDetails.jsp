<%@ page language="java" pageEncoding="ISO-8859-1"%>
<jsp:directive.page import="com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.GenericCode,com.gp.cvst.logisoft.domain.FclAESDetails"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@include file="../includes/jspVariables.jsp"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%
String path = request.getContextPath();
String refresh="",index="";
String post=request.getParameter("ready");
if(request.getAttribute("refresh")!=null && request.getAttribute("refresh").equals("refresh")){
  refresh = "true";
}
String view="";
if(session.getAttribute("view")!=null){
  view=(String)session.getAttribute("view");
}
if(request.getAttribute("index")!=null){
 index=(String)request.getAttribute("index");
}
if(request.getAttribute("completed")!=null && request.getAttribute("completed").equals("completed")){
%>    
	<script>
		parent.parent.GB_hide();
	</script>
<%}%>
<html> 
	<head>
		<title>JSP for FclAESDetailsForm form</title>
		<%@include file="../includes/baseResources.jsp"%>
			 <script language="javascript" src="<%=path%>/js/common.js"></script>
                         <script language="javascript" src="<%=path%>/js/caljs/calendar.js" ></script>
        <script language="javascript" src="<%=path%>/js/caljs/lang/calendar-en.js"></script>
        <script language="javascript" src="<%=path%>/js/caljs/calendar-setup.js"></script>
        <script language="javascript" src="<%=path%>/js/caljs/CalendarPopup.js"></script>
         <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="<%=path%>/js/autocomplete.js"></script>
		<script language="javascript" type="text/javascript">
function submit1(){
  document.fclAESDetailsForm.buttonValue.value="save";
  document.fclAESDetailsForm.submit();
}
function aesexcClear(){
        document.fclAESDetailsForm.aesDetails.value="";
        document.fclAESDetailsForm.exception.value="";
}
function submitAdd1(){
	 if(null!=document.fclAESDetailsForm.exception && trim(document.fclAESDetailsForm.aesDetails.value)=='' && trim(document.fclAESDetailsForm.exception.value)==''){	 	              
                alertNew("Please enter either ITN Number or Exception");
                aesexcClear();
	 }else if(null!=document.fclAESDetailsForm.exception && document.fclAESDetailsForm.ITNEXP.value!='' && document.fclAESDetailsForm.ITNEXP.value=='ITN' &&
					 document.fclAESDetailsForm.exception.value!=''){                
		alertNew("Please enter only ITN Number not Exception ");
                aesexcClear();
	 }else if(null!=document.fclAESDetailsForm.ITNEXP &&  document.fclAESDetailsForm.ITNEXP.value!='' && document.fclAESDetailsForm.ITNEXP.value=='EXP' &&
					 document.fclAESDetailsForm.aesDetails.value!=''){
	 	alertNew("Please enter only   Exception   not ITN Number");
                aesexcClear();
	 }else if(null!=document.fclAESDetailsForm.ITNEXP &&  document.fclAESDetailsForm.ITNEXP.value!='' && document.fclAESDetailsForm.ITNEXP.value=='EXP' &&
					 document.fclAESDetailsForm.aesDetails.value!=''){
	 	alertNew("Please enter only   Exception   not ITN Number");
                aesexcClear();
	 }else if(null!=document.fclAESDetailsForm.exception && document.fclAESDetailsForm.aesDetails.value!='' && document.fclAESDetailsForm.exception.value!=''){
	 	alertNew("Please enter either ITN Number or Exception not both");
                aesexcClear();
	 }else{
		document.fclAESDetailsForm.buttonValue.value="add";
		document.fclAESDetailsForm.submit();
                aesexcClear();
	 }
}
var AESId;
function deleteAES(aesdId){
  AESId=aesdId;
  confirmNew("Are you sure want to Delete this Record","deleteAES");
}
function confirmMessageFunction(id1,id2){
	if(id1=='deleteAES' && id2=='ok'){
		document.fclAESDetailsForm.deleteId.value=AESId;
		document.fclAESDetailsForm.buttonValue.value="delete";
		document.fclAESDetailsForm.submit();
	}
}
function load(ev){
  if(ev!= 'undefined' && ev=='true'){
	 document.fclAESDetailsForm.buttonValue.value="refresh";
	 document.fclAESDetailsForm.submit();
  }
}
function makeFormBorderless(form) {
  var element;
  for (var i = 0; i < form.elements.length; i++) {
	 element = form.elements[i];
	 if(element.type == "button"){
		if(element.value=="Add" || element.value=="Save" || element.value=="Delete" ){
			element.style.visibility="hidden";
		}
	  }
	  if (element.type == "text" || element.type == "textarea") {
		element.style.border = 0;
		element.readOnly = true;
                element.tabIndex = -1;
		element.style.backgroundColor = "#CCEBFF";
	  }
	  var imgs = document.getElementsByTagName("img");
		for (var k = 0; k < imgs.length; k++) {
			imgs[k].style.visibility = "hidden";
		}
  }
  return false;
}
function closePage(){
   /*var table=document.getElementById("aesDisplayTableId");
   var tableLength=table.getElementsByTagName("tr");
   var color;
   if(!tableLength.length==0){
      color="red";
      parent.parent.makeButtonRedColorForAes(color,'<%=index%>'); 
   }else{
      parent.parent.makeButtonRedColorForAes(color,'<%=index%>');
   }
   //parent.parent.submit1();*/
   parent.parent.GB_hide();
   parent.parent.makeFclBlCorrectionButtonRed();
}
function getData(ev){
  checkTextAreaLimit(ev,200);
}
function setException(exceptionValues){
    //document.fclAESDetailsForm.exception.value=exceptionValues.toUpperCase();
    var commentVal = document.fclAESDetailsForm.exception.value;
	var totalLength = commentVal.length + exceptionValues.length;
	if(totalLength > 500){
		alertNew('More than 500 characters are not allowed');
		return;
	}
	var oldarray = document.fclAESDetailsForm.exception.value;
	var splittedArray;
	if (oldarray.length == 0) {
		splittedArray = oldarray;
	} else {
		splittedArray = oldarray.split("\n");
	}
	var newarray = exceptionValues.split(">>");
	var resultarray = new Array();
	var flag = false;
	for (var k = 0; k < newarray.length; k++) {
		flag = false;
		for (var l = 0; l < splittedArray.length; l++) {
			if (newarray[k].replace(/^[\s]+/, "").replace(/[\s]+$/, "").replace(/[\s]{2,}/, " ") == splittedArray[l].replace(/^[\s]+/, "").replace(/[\s]+$/, "").replace(/[\s]{2,}/, " ")) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			if (oldarray == "") {
				oldarray = newarray[k];
			} else {
				oldarray += "\n" + newarray[k];
			}
		}
	}
	document.fclAESDetailsForm.exception.value = oldarray.replace(/>/g, "");
}
		</script>
		<%@include file="../includes/resources.jsp" %>
	</head>
	<body class="whitebackgrnd" >
		 <!--DESIGN FOR NEW ALERT BOX ---->
<div id="AlertBox" class="alert">
	<p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Alert</b></p>
	<p id="innerText" class="containerForAlert" style="width: 100%;padding-left: 3px;">

	</p>
	<form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
	<input type="button"  class="buttonStyleForAlert" value="OK"
	onclick="document.getElementById('AlertBox').style.display='none';
	grayOut(false,'');">
	</form>
</div>
	 <!--DESIGN FOR NEW Confirm BOX ---->
<div id="ConfirmBox" class="alert">
	<p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Confirmation</b></p>
	<p id="innerText1" class="containerForAlert" style="width: 100%;padding-left: 3px;"></p>
	
	<form style="text-align:right;padding-right:4px;padding-bottom:4px;padding-top:10px;">
		<input type="button"  class="buttonStyleForAlert" value="OK" onclick="yes()">
		<input type="button"  class="buttonStyleForAlert" value="Cancel" onclick="No()">
	</form>
</div>
		<html:form action="/fclAESDetails" styleId="fclaesdetails" scope="request">
			
			<table width="100%" border="0" cellpadding="3" cellspacing="0"	id="records" >
			  <tr>
				<td align="left" class="textlabels">
					<b>File No&nbsp;:</b>
					     <font color="Red" size="2" style="padding-left:5px;">
							<c:out value="${fclAESDetailsForm.fileNo}"/></font>
				</td>
				<td align="right">                                   
					<input type="button" value="Save" id="add" class="buttonStyleNew" 
								onclick="submitAdd1()" />                                    
				    <input type="button" value="Close"  class="buttonStyleNew"
							onclick="closePage()" />
				</td>
			  </tr>
			</table>
			<table width="100%" border="0" cellpadding="3" cellspacing="0"	id="records" class="tableBorderNew">
                            <c:if test="${importFlag eq false}">
                                <tr class="tableHeadingNew"><font style="font-weight: bold">AES/ITN Add/View Details</font></tr>
                                    <tr class="textlabels">
                                            <td class="textlabelsBold">ITN Number</td>
                                            <td><html:textarea property="aesDetails" styleClass="textlabelsBoldForTextBox"
                                                 cols="45" rows="1" value="" onkeypress="return checkTextAreaLimit(this, 25)"  style="text-transform: uppercase"/></td>
                                                 <td class="textlabelsBold">Exception</td>
                                            <td><html:textarea property="exception" 
                                                           onkeypress="return checkTextAreaLimit(this, 35)" onkeyup="alertTextAreaLimit(this, 35)" styleClass="textlabelsBoldForTextBox"
                                                 cols="90" rows="1" value="" style="text-transform: uppercase;"/>
                                                <img src="<%=path%>/img/icons/display.gif" onclick="goToRemarksLookUp()"/>
                                        </td>
                                    </tr>
                            </c:if>
                                    
			</table>
			<br>                       
			<table width="100%" class="tableBorderNew">
			 <tr>
			   <td>
			    <div id="aesDisplayTableId">
				   <table border="0" cellpadding="0" cellspacing="0" id="aestable">
				  
				   <% int k=0;%>
					  <display:table name="${aesDetailsList}" id="lineitemtable"
						 class="displaytagstyle"   pagesize="<%=pageSize%>">
									<%
									  String exception="";
									  int ind=0;
									  if(null!=request.getAttribute("aesDetailsList")){
									    List aesList=(List)request.getAttribute("aesDetailsList");
									    if(aesList.size()>0){
									      FclAESDetails fclAESDetails=(FclAESDetails)aesList.get(k);
									       if(fclAESDetails.getException()!=null){
										       ind=fclAESDetails.getException().indexOf("-");
											   if(ind!=-1){	
											     exception=fclAESDetails.getException().replace("-","<br>");
											     int brindex=exception.indexOf("<br>");
											     if(brindex==0){
											    	exception=exception.replaceFirst("<br>","");
											     }
											   }else{
											       exception=fclAESDetails.getException();
											   }
									       }
									    }
									  }
									 %>
									
									<display:setProperty name="paging.banner.some_items_found">
										<span class="pagebanner"> <font color="blue">{0}</font>
											LineItem details displayed,For more LineItems click on page
											numbers. <br> </span>
									</display:setProperty>
									<display:setProperty name="paging.banner.one_item_found">
										<span class="pagebanner"> One {0} displayed. Page
											Number </span>
									</display:setProperty>
									<display:setProperty name="paging.banner.all_items_found">
										<span class="pagebanner"> {0} {1} Displayed, Page
											Number </span>
									</display:setProperty>

									<display:setProperty name="basic.msg.empty_list">
										<span class="pagebanner"> No Records Found. </span>
									</display:setProperty>
									<display:setProperty name="paging.banner.placement"
										value="bottom" />
									<display:setProperty name="paging.banner.item_name"
										value="LineItem" />
									<display:setProperty name="paging.banner.items_name"
										value="LineItems" />
										<c:if test="${not empty lineitemtable.aesDetails}">
										<%index="ITN"; %>
										</c:if>
										<c:if test="${not empty lineitemtable.exception}">
										<%index="EXP"; %>
										</c:if>
									<display:column title="ITN Number" property="aesDetails"></display:column>
									<display:column title="Exception"><%=exception%></display:column>
									<display:column title="Action" >
									    <span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();">
											<img src="<%=path%>/img/icons/delete.gif" 
											onclick="deleteAES('${lineitemtable.id}')" id="deleteimg" />
										</span>
									</display:column>
							 <% k++; %>		
							</display:table>
					</table>
					</div>
				   </td>
				  </tr>	
				</table>
				<html:hidden property="bolId" value="${fclAESDetailsForm.bolId}"/><%--
				<html:hidden property="trailorNoId" value="${containerId}"/>
				--%><html:hidden property="buttonValue"/>
				<html:hidden property="deleteId"/>
				<input type="hidden" name="ITNEXP" value="<%=index%>"/>
				<input type="hidden" name="fileNo" value="${fclAESDetailsForm.fileNo}"/>
<%--				<input type="hidden" name="UnitNumber" value="${UnitNo}"/>--%>
<%--				<input type="hidden" name="index" value="<%=index%>"/>--%>
<%--				--%>
		</html:form>
		
		<script type="text/javascript">
		load('<%=refresh%>')
		function goToRemarksLookUp(){
    		  //var remarksCode=document.QuotesForm.commentTemp.value;
		     //if(remarksCode=='%'){
		        //remarksCode = 'percent';
		     //}
                     document.fclAESDetailsForm.exception.value = "";
           var href='<%=path%>/remarksLookUp.do?buttonValue=aesDetails';
		   mywindow=window.open(href,'','width=700,height=400,scrollbars=yes');
		   mywindow.moveTo(200,180);
	       //GB_show('Remarks Info','<%=path%>/remarksLookUp.do?buttonValue=Quotation',
  		  //width="200",height="200");
		}
                function checkTextAreaLimit(ev,length){
                    if(event.keyCode==13){
                        return false;
                    }                    
                    return (ev.value.length < length);
                }
                   function alertTextAreaLimit(field, limit){
                                        if(field.value.length > limit){
                                            alert("Length of this Field cannot be more than 35");
                                            field.value = "";
                                        }
                                    }
		</script>
		<%if(view.equals("3")){ %>
		 <script type="text/javascript">
		   makeFormBorderless(document.getElementById("fclaesdetails"));</script>
       <%}%>
		 
	</body>
</html>

