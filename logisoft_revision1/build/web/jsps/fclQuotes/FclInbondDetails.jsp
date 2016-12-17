<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@page import="com.gp.cong.logisoft.bc.fcl.ImportBc"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@include file="../includes/jspVariables.jsp"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
<html> 
    <head>
        <title>JSP for InbondDetailsForm form</title>
        <%@include file="../includes/baseResources.jsp"%>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <script language="javascript" src="${path}/js/common.js"></script>
        <script language="javascript" src="${path}/js/caljs/calendar.js" ></script>
        <script language="javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script language="javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script language="javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/autocomplete.js"></script>
        <%@include file="../includes/resources.jsp" %>
        <script language="javascript">
            start = function(){
                clearFormValues();
            }
            window.onload = start;
        </script>
        <script type="text/javascript" language="javascript">
            var inbondId;
            function deleteInbond(id){
                inbondId=id;
                confirmNew("Are you sure want to Delete this Record","deleteInbond");
            }
            function editInbond(id,inbondNumber,inbondPort,inbondDate,inbondType){
                document.getElementById("inbondDiv").style.display="block";
                document.inbondDetailsForm.inbondNumber.value=inbondNumber;
                document.inbondDetailsForm.inbondDate.value=inbondDate;
                document.inbondDetailsForm.inbondPort.value=inbondPort;
                document.inbondDetailsForm.inbondType.value=inbondType;
                document.inbondDetailsForm.inbondId.value=id;
                document.getElementById("saveInbond").value="Update";
            }
            function confirmMessageFunction(id1,id2){
                if(id1=='deleteInbond' && id2=='ok'){
                    document.inbondDetailsForm.inbondId.value=inbondId;
                    document.inbondDetailsForm.buttonValue.value="delete";
                    document.inbondDetailsForm.submit();
                }
            }
            function validateDate(data) {
                if(data.value!=""){
                    data.value = data.value.getValidDateTime("/","",false);
                    if(data.value==""||data.value.length>10){
                        alertNew("Please enter valid date");
                        data.value="";
                        document.getElementById(data.id).focus();
                    }
                    yearValidation(data);
                }
            }
            function addInbond(obj){
                if( document.inbondDetailsForm.inbondNumber.value ==''){
                    document.inbondDetailsForm.inbondNumber.focus();
                    alertNew("Please Enter Inbond Number");
                    return false;
                }else if( document.inbondDetailsForm.inbondNumber.value.length > 20){
                    document.inbondDetailsForm.inbondType.focus();
                    alertNew("Inbond Number Should not be Greater Than 20 Characters");
                    return false;
                }else if( document.inbondDetailsForm.inbondType.value ==''){
                    document.inbondDetailsForm.inbondType.focus();
                    alertNew("Please Select Inbond Type");
                    return false;
                }
                document.inbondDetailsForm.buttonValue.value=obj.value;
                document.inbondDetailsForm.submit();
                clearFormValues();
            }
            function openInbondDiv(){
                document.inbondDetailsForm.inbondType.value="";
                document.inbondDetailsForm.inbondNumber.value="";
                document.inbondDetailsForm.inbondPort.value=document.getElementById('podValues').value;
                document.inbondDetailsForm.inbondDate.value="";
                document.getElementById("saveInbond").value="Save";
                document.getElementById("inbondDiv").style.display="block";
            }
            function closeIndondDiv(){
                document.getElementById("inbondDiv").style.display="none";
            }
            function closePage(){
                parent.parent.GB_hide();
                parent.parent.makeInbondButtonGreen();
            }
            function clearFormValues(){
                document.inbondDetailsForm.inbondNumber.value="";
                document.inbondDetailsForm.inbondDate.value="";
            }
            function checkNumeric(obj){
                obj.value = obj.value.replace(/\s/g,'');
                if (isNaN(obj.value)){
                    alertNew('Please Enter Valid Number');
                    obj.value='';
                    obj.focus();
                    return false;
                }
            }
        </script>
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
        <html:form action="/fclInbondDetails" name="inbondDetailsForm" type="com.gp.cong.logisoft.struts.form.InbondDetailsForm" scope="request" >
            <table width="100%" border="0" cellpadding="3" cellspacing="0"	id="records" >
                <tr>
                    <td align="left" class="textlabels">
                        <b>File No&nbsp;:</b>
                        <font color="Red" size="2" style="padding-left:5px;">
                            <c:out value="${fileNo}"/></font>
                    </td>
                    <td align="right">
                        <input type="button" value="Add" id="add" class="buttonStyleNew"
                               onclick="openInbondDiv()" />
                        <input type="button" value="Close"  class="buttonStyleNew"
                               onclick="closePage()" />
                    </td>
                </tr>
            </table>
            <div id="inbondDiv" style="display: none">
                <table width="100%" border="0" cellpadding="3" cellspacing="0" id="records" class="tableBorderNew">
                    <tr class="tableHeadingNew"><font style="font-weight: bold">Add/View Inbond Details</font></tr>
                    <tr>
                        <td class="textlabelsBold">INBond #</td>
                        <td align="left">
                            <html:text property="inbondNumber" styleClass="textlabelsBoldForTextBox" maxlength="20"
                                       style="text-transform: uppercase" size="15"/>
                        </td>
                        <td class="textlabelsBold">INBond Type</td>
                        <td>
                            <html:select property="inbondType" styleClass="textlabelsBoldForTextBox" style="width:120px;">
                                <html:optionsCollection name="inbondTypeList"/>
                            </html:select>
                        </td>
                        <td class="textlabelsBold">INBond Port</td>
                        <td>
                            <input Class="textlabelsBoldForTextBox" name="inbondPort" id="inbondPort"  size="22"/>
                            <div id="inbondPort_choices"  style="display: none;width: 4px;" class="newAutoComplete" ></div>
                            <script type="text/javascript">
                                initOPSAutocomplete("inbondPort","inbondPort_choices","","",
                                "${path}/actions/getUnlocationCode.jsp?tabName=QUOTE&from=3&isDojo=false","");
                            </script>
                        </td>
                        <td class="textlabelsBold">INBond Date
                        <img src="${path}/img/CalendarIco.gif" alt="cal" id="inbondCal"
                                 onmousedown="insertDateFromCalendar(this.id,0);"  />
                        </td>
                        <td>
                            <html:text styleClass="textlabelsBoldForTextBox" onchange="validateDate(this);" property="inbondDate"
                                       styleId="txtinbondCal"
                                       size="18" />
                        </td>
                    </tr>
                    <td align="center" colspan="8">
                        <input type="button" value="Save" id="saveInbond" class="buttonStyleNew"
                               onclick="addInbond(this)" />
                        <input type="button" value="cancel"  class="buttonStyleNew"
                               onclick="closeIndondDiv()" />
                    </td>
                </table>
            </div>
            <br>
            <table width="100%" class="tableBorderNew">
                <tr>
                    <td>
                        <div id="inbondDisplayTableId">
                            <table border="0" cellpadding="0" cellspacing="0" id="inbondDetailstable">
                                <c:set var="i" value="0"></c:set>
                                <display:table name="${inbondDetailsList}" id="inbondTable"
                                               class="displaytagstyleNew"   pagesize="50">
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
                                    <display:setProperty name="paging.banner.placement" value="bottom" />
                                    <display:setProperty name="paging.banner.item_name" value="Inbond"/>
                                    <display:setProperty name="paging.banner.items_name" value="Inbonds"/>
                                    <display:column title="Inbond Number" property="inbondNumber"></display:column>
                                    <display:column title="Inbond Type" property="inbondType"></display:column>
                                    <fmt:formatDate pattern="MM/dd/yyyy" var="date" value="${inbondTable.inbondDate}"/>
                                    <display:column title="Inbond Date">
                                        ${date}
                                    </display:column>
                                    <display:column title="Inbond Port" property="inbondPort">
                                    </display:column>
                                    <display:column title="Action" >
                                        <img src="${path}/img/icons/delete.gif" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();"
                                             onclick="deleteInbond('${inbondTable.id}')" id="deleteimg" />
                                        <img src="${path}/img/icons/edit.gif" onmouseover="tooltip.show('<strong>Edit</strong>',null,event);" onmouseout="tooltip.hide();"
                                             onclick="editInbond('${inbondTable.id}','${inbondTable.inbondNumber}','${fn:replace(inbondTable.inbondPort, "'", "\\'")}','${date}','${inbondTable.inbondType}')" id="editimg" />
                                    </display:column>
                                    <c:set var="i" value="${i+1}"></c:set>
                                </display:table>
                            </table>
                        </div>
                    </td>
                </tr>
            </table>
            <html:hidden property="inbondId"/>
            <html:hidden property="bolId" value="${bolId}"/>
            <input type="hidden" name="fileNo" value="${fileNo}"/>
            <input type="hidden" id="podValues" name="podValues" value="${inbondPorts}"/>
            <html:hidden property="buttonValue"/>
        </html:form>
    </body>
</html>

