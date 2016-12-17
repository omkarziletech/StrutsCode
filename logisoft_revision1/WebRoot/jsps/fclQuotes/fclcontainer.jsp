<jsp:directive.page import="com.gp.cvst.logisoft.domain.FclBlContainer"/>
<jsp:directive.page import="java.util.*"/>
<jsp:directive.page import="java.text.SimpleDateFormat"/>
<jsp:directive.page import="com.gp.cvst.logisoft.domain.FclBl"/>
<jsp:directive.page import="com.gp.cong.logisoft.util.CommonFunctions"/>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<bean:define id="fileNumberPrefix" type="String">
       <bean:message key="fileNumberPrefix"/>
 </bean:define>
<%
 String path = request.getContextPath(); 
 com.gp.cvst.logisoft.util.DBUtil dbUtil = new com.gp.cvst.logisoft.util.DBUtil();
 com.gp.cong.logisoft.util.DBUtil unitDB=new  com.gp.cong.logisoft.util.DBUtil();
 List contList= null;
 List addcontainerlist=null;
 List unittypelist=new ArrayList();
	request.setAttribute("sizeLegend",dbUtil.getSizeLegend());
	request.setAttribute("totalTrls",dbUtil.getTotalTrls());
String view="";
if(session.getAttribute("view")!=null){
	view=(String)session.getAttribute("view");
}
List hazmatList=new ArrayList();
if(session.getAttribute("bookinghazmat")!=null){
	hazmatList=(List)session.getAttribute("bookinghazmat");
}
String bol="";
String fileNo="",completeFileNo="",newFileNo="";
if (request.getAttribute("fclBl") != null) {
FclBl fclbl = (FclBl) request.getAttribute("fclBl");
		if(fclbl.getBol()!=null){
			bol=fclbl.getBol().toString();
			completeFileNo=fclbl.getBolId().toString();
		}
		if(fclbl.getFileNo()!=null){
			fileNo=fileNumberPrefix+fclbl.getFileNo().toString();
			newFileNo=fclbl.getFileNo().toString();
		}
}
if(request.getAttribute("fclBlContainerList")!=null){
	addcontainerlist=(List)request.getAttribute("fclBlContainerList");
}
SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
 
if(unittypelist != null){
  unittypelist=unitDB.getUnitFCLUnitypeTest(new Integer(38),"yes","Select Unit code",null,bol.toString());
  request.setAttribute("unittypelist",unittypelist);
}
%>
<html>
<head>

<script language="javascript" type="text/javascript">

//-- OTHER SCRIPT FUNCTIONS WRITTEN IN fclBlContainer.js----------

function gotoMarksNumbers(index,id,bol,fileNo,form){
  var unitnumber;
  if(document.fclBillLaddingform.trailerNo[index]==undefined){
   unitnumber=document.fclBillLaddingform.trailerNo.value;
  }else{
   unitnumber=document.fclBillLaddingform.trailerNo[index].value;
  }
  GB_show('Description of Packages and Goods','<%=path%>/fclmarksnumber.do?button='+'NewFCLBL&index1='+index+
	 '&containerId='+id+'&bolid='+bol+'&fileNo='+fileNo+'&unitNo='+unitnumber,400,900);
}   
/*function gotoAES(index,id,bol,fileNo,form){
 var unitnumber;
 if(document.fclBillLaddingform.trailerNo[index]==undefined){
   unitnumber=document.fclBillLaddingform.trailerNo.value;
 }else{
   unitnumber=document.fclBillLaddingform.trailerNo[index].value;
 }
  GB_show('AES Details','<%=path%>/fclAESDetails.do?buttonValue='+'fclbl&index='+index+
	 '&containerId='+id+'&bolid='+bol+'&fileNo='+fileNo+'&unitNo='+unitnumber,600,850);
}*/    
function gotoHazmat(index,id,bol,fileNo,form){
	var trailerNo;
	if(undefined!=document.fclBillLaddingform.trailerNo && document.fclBillLaddingform.trailerNo.length!=undefined){
		trailerNo=document.fclBillLaddingform.trailerNo[index].value;
	}else{
		trailerNo=document.fclBillLaddingform.trailerNo.value;
	}

 // return GB_show('HazMat', '<%=path%>/fCLHazMat.do?buttonValue=fclbl&index='+index+'&containerId='+id+'&bolid='+bol+'&name=FclBl'+'&fileNo='+fileNo+'&unitNo='+trailerNo,390,860);
}  

function editContainerDetails(index,id,bol){
  GB_show('Container','<%=path%>/fclBillLadding.do?button=editContainer&bol='+bol+'&id='+id+'&index='+index,300,800);
}
function getUpdatedContainerDetails(bol){
  document.fclBillLaddingform.bol.value=bol;
  document.fclBillLaddingform.buttonValue.value="getUpdatedContainerDetails";
  document.fclBillLaddingform.submit();
}
function pausecomp(millis){ 
	var date = new Date(); 
	var curDate = null; do {
	curDate = new Date();
	} while(curDate-date < millis); 
} 
function getUpdatedDetails(){
	
  document.fclBillLaddingform.buttonValue.value="getUpdatedContainerDetails";
  document.fclBillLaddingform.submit();
}
/*function makeButtonRedColorForAes(color,index){
  if(color=='red'){
    document.getElementById("aesbutton"+index).style.backgroundColor="#FE7A87";
  }else{
    document.getElementById("aesbutton"+index).style.backgroundColor="#8DB7D6";
  }
}*/
function makeButtonRedColorForPkgs(fromMarks,index,action){
  alert('parent'+action);
  if(action=='PKG'){
	   if(fromMarks=='red'){
	    document.getElementById("marksbutton"+index).style.backgroundColor="#FE7A87";
	  }else{
	    document.getElementById("marksbutton"+index).style.backgroundColor="#8DB7D6";
	  }
  }else{
  alert('else'+fromMarks);
  	if(fromMarks=='red'){
	    document.getElementById("hazmatbutton"+index).style.backgroundColor="#FE7A87";
	  }else{
	    document.getElementById("hazmatbutton"+index).style.backgroundColor="#8DB7D6";
	  }
  }
}


function addButtons(val){
if(undefined != document.fclBillLaddingform.trailerNo && document.fclBillLaddingform.trailerNo.length!=undefined){
  for(var i=0;i<document.fclBillLaddingform.trailerNo.length;i++){
    if(document.fclBillLaddingform.trailerNo[i].value!=""){
    if(null!= document.getElementById("marksbutton"+i)){
    	// document.getElementById("marksbutton"+i).style.visibility="visible";
    }
 	if(null!=document.getElementById("aesbutton"+i)){
 		//document.getElementById("aesbutton"+i).style.visibility="visible";
 	}  
    if(val=='Y'){
         if(null!=document.getElementById("hazmatbutton"+i)){
         	//document.getElementById("hazmatbutton"+i).style.visibility="visible";
         }
 	   }
    }
  }
}else{
  if(document.fclBillLaddingform.trailerNo.value!=""){
 	// document.getElementById("marksbutton"+0).style.visibility="visible";
    // document.getElementById("aesbutton"+0).style.visibility="visible";
     if(val=='Y'){
 		//document.getElementById("hazmatbutton"+0).style.visibility="visible";
 	 }
   }
}
}
function addContainerDetails(bol,breakBulk){
    if(undefined != breakBulk && null != breakBulk && breakBulk == 'Y'){
        GB_show('Container','<%=path%>/fclBillLadding.do?button=addContainerList&bol='+bol+'&breakBulk='+breakBulk,300,800);
    }else{
       GB_show('Container','<%=path%>/fclBillLadding.do?button=addContainerList&bol='+bol,300,800);
    }
}
var indexValue;
function openCommentPopUpToDisable(index,sizeLegend,bol,id,fileNo){
	showPopUp();
	 indexValue=index;
	//---div design in fclbillladding.jsp--
    document.getElementById('containerCommentBox').style.display='block';
    document.fclBillLaddingform.containerComments.value="";
    document.getElementById("editImage"+index).style.visibility="hidden";
 	/*document.getElementById("marksbutton"+index).style.visibility="hidden";
 	document.getElementById("aesbutton"+index).style.visibility="hidden";
 	if(document.getElementById("hazmatbutton"+index)!=null){
  	  document.getElementById("hazmatbutton"+index).style.visibility="hidden";
	}*/
    document.getElementById("idOfContainer").value=id;
	document.getElementById("sizeOfContainer").value=sizeLegend;
	document.getElementById("currentFileNo").value=fileNo;
	document.getElementById("bolId").value=bol;
}
function disableThisContainer(){
  document.fclBillLaddingform.buttonValue.value="disableThisContainer";
  document.fclBillLaddingform.submit();
}
function closeCommentPopUp(){
  document.getElementById("editImage"+indexValue).style.visibility="visible";
 /* if(document.getElementById("unitNo"+indexValue).value!=''){
 	document.getElementById("marksbutton"+indexValue).style.visibility="visible";
 	document.getElementById("aesbutton"+indexValue).style.visibility="visible";
 	if(document.getElementById("hazmatbutton"+indexValue)!=null){
  	  document.getElementById("hazmatbutton"+indexValue).style.visibility="visible";
	}
  }*/
  closePopUp();
  document.getElementById('containerCommentBox').style.display='none';
}
function openCommentPopUpToEnable(index,sizeLegend,bol,id,fileNo){
   var comment;
   var j;
   showPopUp();
   //---div design in fclbillladding.jsp--
   document.getElementById('editContainerCommentBox').style.display='block';
  
  //--to get the comments----
  FclBlBC.getCommentsOfEachContainer(id,function(data){
       if(data!=null){
          j=data.indexOf("-");
          if(j!=-1){
            comment=data.substring(0,j);
          }else{
            comment=data;
          }
          document.fclBillLaddingform.tempContainerComments.value=comment;
       }
     }
  );
 
  document.getElementById("idOfContainer").value=id;
  document.getElementById("sizeOfContainer").value=sizeLegend;
  document.getElementById("currentFileNo").value=fileNo;
  document.getElementById("bolId").value=bol;
}
function enableThisContainer(){
  document.fclBillLaddingform.buttonValue.value="enableThisContainer";
  document.fclBillLaddingform.submit();
}
function closeCommentPopUpForEnable(){
  closePopUp();
  document.getElementById('editContainerCommentBox').style.display='none';
}

</script>
  
 </head>   
<body class="whitebackgrnd" />   
 		
	<table width="100%" border="0" cellpadding="0"  class="tableBorderNew" cellspacing="0"
				id="records">
					<tr class="tableHeadingNew" colspan="2">
						<td><font style="font-weight: bold">Container Details</font></td>
						<td align="right">
							<%--<input type="button" value="Save" id="save1" class="buttonStyleNew"
								onclick="submit1()" />
							--%>
<%--							<input type="button" value="Add" id="add1" class="buttonStyleNew"--%>
<%--								onclick="addContainerDetails('<%=bol%>')" /> --%>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<table border="0" cellpadding="0" cellspacing="0" id="containerTable">
								<%int i = 0;%>
					<display:table name="<%=addcontainerlist%>" id="lineitemtable"
						class="displaytagstyle" pagesize="30" >
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
							<%
								String trailerNo = "";
								String sealNo = "";
								String lastUpdate = "";
								String sizeLegend = "";
								String markNo="";
								String id="",manuallyAdded="";
								String userName="",containerComment="",disabledFlag="";
								
								if (addcontainerlist != null && addcontainerlist.size() > 0) {
									FclBlContainer fclContainer = (FclBlContainer) addcontainerlist.get(i);
										if(fclContainer.getTrailerNoId()!=null){
										  id=fclContainer.getTrailerNoId().toString();
										}
										if (fclContainer.getTrailerNo() != null) {
											trailerNo = fclContainer.getTrailerNo();
										}
										if (fclContainer.getSealNo() != null) {
											sealNo = fclContainer.getSealNo();
										}
										if(fclContainer.getMarks()!=null){
											markNo=fclContainer.getMarks();
										}
										if (fclContainer.getLastUpdate() != null) {
											lastUpdate = sdf.format(fclContainer.getLastUpdate());
										}
										if (fclContainer.getSizeLegend() != null) {
											sizeLegend = fclContainer.getSizeLegend().getId().toString();
										}
										if (fclContainer.getUserName() != null) {
											userName = fclContainer.getUserName();
										}
										if(fclContainer.getContainerComments()!=null){
										  containerComment=fclContainer.getContainerComments();
										}
										if(fclContainer.getDisabledFlag()!=null){
										  disabledFlag=fclContainer.getDisabledFlag();
										}
										if(fclContainer.getManuallyAddedFlag()!=null){
										  manuallyAdded=fclContainer.getManuallyAddedFlag();
										}
							   }
				%>
				    
				<%if(!manuallyAdded.equals("") && manuallyAdded.equalsIgnoreCase("M")){ %>
				    <display:column style="color:red;font-weight:bold;font-size:14pt;">*</display:column>
				<%}else{ %>
				    <display:column style="visibility:hidden;"></display:column>
				<%}%>
					<display:column title="Unit No">
						<%--<html:checkbox property="trialCheckbox" onclick="cleardata()"></html:checkbox>--%>
						<input type="text" name="trailerNo" Class="BackgrndColorForTextBox"  size="15" 
						    id="unitNo<%=i%>" onkeypress="allowFreeFormat(this)" 
							value="<%=trailerNo%>" onkeydown="enable(this)"
							maxlength="13" readonly="readonly" />
					</display:column>
					<display:column title="Seal #">
						<html:text property="sealNo" size="15" styleClass="BackgrndColorForTextBox" styleId="<%=String.valueOf(i)%>"
							readonly="true"  value="<%=sealNo%>" onkeydown="getDate(this)" ></html:text>
					</display:column>
					<display:column title="Last Upd">
						<input type="text" readonly="true"  name="lastUpdate" class="BackgrndColorForTextBox"
							id="txtItemcreatedon1<%=i%>" size="10" value="<%=lastUpdate%>" />
					</display:column>
					<display:column title="User">
						<input type="text" readonly="true"  Class="BackgrndColorForTextBox" name="user" 
						   id="txtItemcreatedon1<%=i%>" size="15"  value="<%= userName%>"/>
					</display:column>
					<display:column title="Size Legend">
					<%--<html:text property="sizeLegend" value="<%=sizeLegend%>" styleClass="BackgrndColorForTextBox" styleId="<%=String.valueOf(i)%>" style="width:60%" 
					readonly="true" onfocus="this.blur()"/>
						--%><html:select styleClass="BackgrndColorForTextBox"
							property="sizeLegend" value="<%=sizeLegend%>" onchange="getDate(this)" disabled="true"
							styleId="<%=String.valueOf(i)%>" style="width:60%" >
							<html:optionsCollection name="unittypelist" />
						</html:select>
					</display:column>
					<display:column title="Marks and Numbers">
						<html:textarea styleClass="BackgrndColorForTextBox" readonly="true"  property="marksNo"  styleId="<%=String.valueOf(i)%>"
							 value="<%=markNo%>"  cols="18" />
					</display:column>
					<display:column>
					  <%if(disabledFlag.equalsIgnoreCase("D")){%>
					    <span>&nbsp;</span>
					  <%}else if(CommonFunctions.isNotNull(trailerNo)){%>
						   <input id="marksbutton<%=i%>"  type="button" value="PKGS" class="buttonStyleNew" style="width:33px;"
							 onclick="gotoMarksNumbers('<%=i%>','<%=id%>','<%=bol%>','<%=completeFileNo%>',this.form)" onmouseover="tooltip.show('<strong>Packages </strong>',null,event);" onmouseout="tooltip.hide();"/>
<%--						<span onmouseover="tooltip.show('<strong>View/Add AES Details </strong>');" onmouseout="tooltip.hide();">--%>
<%--						  <input id="aesbutton<%=i%>" type="button" value="AES" class="buttonStyleNew"  --%>
<%--							onclick="gotoAES('<%=i%>','<%=id%>','<%=bol%>','<%=completeFileNo%>',this.form)"--%>
<%--							  style="width:25px;" />--%>
<%--					    </span>--%>
					    <c:if test="${hazmatidentity=='hazmatidentity'}">
						     <input id="hazmatbutton<%=i%>"  type="button" value="HAZ" class="buttonStyleNew" onmouseover="tooltip.show('<strong>Hazmat</strong>',null,event);" onmouseout="tooltip.hide();"
							   onclick="gotoHazmat('<%=i%>','<%=id%>','<%=bol%>','<%=completeFileNo%>',this.form)"
 					             style="width:25px;"/>
					    </c:if>
					 <%}%>
					</display:column>
					<display:column title="Actions">
					  <%if(disabledFlag.equalsIgnoreCase("D")){%>
					        <img src="<%=path%>/img/icons/key.ico" id="enableButton<%=i%>" name="containerEnableIcon" onmouseover="tooltip.show('Enable',null,event);" onmouseout="tooltip.hide();"
					  			onclick="openCommentPopUpToEnable('<%=i%>','<%=sizeLegend%>','<%=bol%>','<%=id%>','<%=newFileNo%>')"/>
					  <%}else{%>
					     <span class="hotspot" onmouseover="tooltip.show('Edit',null,event);" onmouseout="tooltip.hide();">
		 	  			    <img src="<%=path%>/img/icons/edit.gif" border="0" id="editImage<%=i%>" name="containerEditIcon"
		 	  			  		onclick="editContainerDetails('<%=i%>','<%=id%>','<%=bol%>')"/>
					     </span>
					     <span class="hotspot" onmouseover="tooltip.show('Disable',null,event);" onmouseout="tooltip.hide();">
					  	 	<img src="<%=path%>/img/icons/lockon.ico" id="disableButton<%=i%>" name="containerDisableIcon"
					  		onclick="openCommentPopUpToDisable('<%=i%>','<%=sizeLegend%>','<%=bol%>','<%=id%>','<%=newFileNo%>')"/>
					  	</span>
					  <%}%>
					  <%if(null!=containerComment && !containerComment.trim().equals("")){ %>
					     <span class="hotspot" onmouseover="tooltip.showComments('<strong><%=containerComment%></strong>',null,event);"	
					    	onmouseout="tooltip.hideComments();" style="color:black;">
						    <img id="viewgif4"  src="<%=path%>/img/icons/view.gif"/></span>
					  <%}%>
					</display:column>
					<display:column>
						<input type="hidden" name="id<%=i%>" value="<%=id%>" id="containerId<%=i%>" />
					</display:column>
					
					<%i++;%>
				</display:table>
			</table>
		</td>
<%--		Marks/Description  Add Booking Hazmat Hazmat   font-weight:bold;--%>
	</tr>
</table>
     <input type="hidden" name="idOfContainer" id="idOfContainer"/>
 	 <input type="hidden" name="sizeOfContainer" id="sizeOfContainer"/>
 	 <input type="hidden" name="currentFileNo" id="currentFileNo"/>
 	 <input type="hidden" name="bolId" id="bolId" />
 	
</body>
<script>//addButtons('${fclBl.hazmat}')</script>
   
</html>

