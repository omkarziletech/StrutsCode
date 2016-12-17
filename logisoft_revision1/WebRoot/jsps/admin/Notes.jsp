<%--c.tild and fmt.tld is used for JSTL fmt.tld is using to format date --%>
<%@ page language="java"  import="org.apache.commons.lang3.StringUtils,com.gp.cong.logisoft.util.DBUtil"%>
<jsp:directive.page import="com.gp.cong.logisoft.domain.Notes"/>
<jsp:directive.page import="com.gp.cong.logisoft.struts.form.NotesForm"/>
<jsp:directive.page import="com.gp.cong.logisoft.bc.notes.NotesBC"/>
<%@page import="com.gp.cong.logisoft.bc.notes.NotesConstants"%>
<%@page import="org.apache.struts.util.LabelValueBean"%>
<%@page import="com.gp.cong.logisoft.struts.form.NotesForm"%>
<%@page import="com.gp.cong.common.DateUtils"%>
<jsp:directive.page import="com.gp.cong.logisoft.util.StringFormatter"/>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<bean:define id="fileNumberPrefix" type="String">
    <bean:message key="fileNumberPrefix"/>
</bean:define>
<%    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    NotesBC notesBC = new NotesBC();
    DateUtils dateUtils = new DateUtils();
    List notesList = (List) request.getAttribute("notesList");
    NotesForm notesForm = null;
    String moduleRefId = "";
    String moduleId = "";
    String fileNo = "";
    notesForm = (NotesForm) request.getAttribute("notesForm");
    String itemName = (String) request.getAttribute("itemName");
    if (notesList == null || notesList.isEmpty()) {
        notesList = notesBC.getNotes(notesForm.getModuleId(), notesForm.getModuleRefId(), notesForm.getItemName());
    }
    DBUtil dbUtil = new DBUtil();
    moduleRefId = notesForm.getModuleRefId();
    moduleId = notesForm.getModuleId();
    fileNo = fileNumberPrefix + moduleRefId;
    List actionList = new ArrayList();
    actionList.add(new LabelValueBean("Select One", "0"));
    actionList.add(new LabelValueBean("Show All", "showAll"));
    actionList.add(new LabelValueBean("Show Void", NotesConstants.SHOW_VOID_NOTES));
    actionList.add(new LabelValueBean("FollowupDate Exists", NotesConstants.SHOW_EXISTS_FOLLOWUPDATE_NOTES));
    actionList.add(new LabelValueBean("FollowupDate Past", NotesConstants.SHOW_PAST_FOLLOWUPDATE_NOTES));
    actionList.add(new LabelValueBean("Manual Notes", NotesConstants.SHOW_MANUAL_NOTES));
    actionList.add(new LabelValueBean("Auto Notes", NotesConstants.SHOW_AUTO_NOTES));
    actionList.add(new LabelValueBean("Events", NotesConstants.SHOW_EVENT_NOTES));
    if (!NotesConstants.DISPUTEDBLCODE.equals(itemName)) {
        actionList.add(new LabelValueBean("Disputed Notes", NotesConstants.SHOW_DISPUTED_NOTES));
    }
    actionList.add(new LabelValueBean("Tracking Notes", NotesConstants.SHOW_TRACKING_NOTES));
    request.setAttribute("actionList", actionList);
    request.setAttribute("slashN", "\\N");

%>
<html> 
    <head>
        <%@include file="../includes/resources.jsp" %>
        <title>JSP for NotesForm form</title>
        <%@include file="../includes/baseResources.jsp" %>


        <script language="javascript" src="<%=path%>/js/common.js"></script>	

        <script>
            function saveNotes() {
                var note = document.notesForm.notes;
                if (note.value === "") {
                    alertNew('Please Enter the Note');
                    note.focus();
                    return false;
                }
                document.notesForm.buttonValue.value = "save";
                if (parent.parent.document.getElementById("note")) {
                    parent.parent.document.getElementById("note").className = "buttonColor";
                }
                if (parent.parent.document.getElementById("noteButtonDown")) {
                    parent.parent.document.getElementById("noteButtonDown").className = "buttonColor";
                }
                document.notesForm.submit();
            }

            function updateNotes() {
                var note = document.notesForm.notes;
                if (note.value === "") {
                    alertNew('Please Enter the Note');
                    note.focus();
                    return false;
                }
                document.notesForm.buttonValue.value = "update";
                document.notesForm.submit();
            }

            function setAsVoid(noteId, action) {
                document.notesForm.noteId.value = noteId;
                document.notesForm.buttonValue.value = action;
                document.notesForm.submit();
            }

            function editNotes(noteId) {
                document.notesForm.noteId.value = noteId;
                document.notesForm.buttonValue.value = "EditNotes";
                document.notesForm.submit();
            }

            function showVoid() {
                document.notesForm.buttonValue.value = "";
                document.notesForm.submit();
            }

            function displayNotes() {
                document.getElementById("txtcal2").value = "";
                document.getElementById("notes").value = "";
                if (document.getElementById("printOnReport")) {
                    document.getElementById("printOnReport").checked = false;
                }
                document.getElementById("newNotesDiv").style.display = "block";
            }
            function cancelNotes() {
                document.getElementById("newNotesDiv").style.display = "none";
            }

            function cancelUpdateNotes() {
                document.notesForm.buttonValue.value = null;
                document.notesForm.submit();
            }

            function validateDate(obj) {
                if (Date.parse(obj.value) < Date.parse(Date())) {
                    obj.value = "";
                    alertNew("FollowUp Date should be greater than Today's Date");
                }
            }
        </script>

    </head>
    <body class="whitebackgrnd">
        <!--DESIGN FOR NEW ALERT BOX ---->
        <div id="AlertBox" class="alert">
            <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Alert</b></p>
            <p id="innerText" class="containerForAlert" style="width: 100%;padding-left: 3px;">
            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="document.getElementById('AlertBox').style.display = 'none';
                               grayOut(false, '');">
            </form>
        </div>
        <%if (moduleId.equalsIgnoreCase("FILE")) {%>
        <font color="red" size="2" ><b><%=fileNo%></b></font>
            <%} else if (moduleId.equalsIgnoreCase("TRADING_PARTNER")) {%>
        <font color="red" size="2" ><b><%=moduleRefId%></b></font>
            <%}%>
            <html:form name="notesForm" type="com.gp.cong.logisoft.struts.form.NotesForm" action="/notes" scope="request" method="post">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >

                <c:choose>
                    <c:when test="${(itemName == '100017' && acknowledge != 'acknowledge')
                                    || itemName == '100018' || notesForm.buttonValue=='EditNotes' || notesForm.moduleId == 'CHARGE CODE'}">
                    </c:when>
                    <c:otherwise>
                        <tr class="tableHeadingNew" >
                            <td>Add Notes</td>
                            <td align="right">
                                <input type="button"  value="Add" class="buttonStyleNew" onclick="displayNotes()" align="right"/>
                                <c:if test="${notesForm.moduleId=='DOMESTIC'}">&nbsp;&nbsp;&nbsp;</c:if>
                                </td>
                            </tr>
                    </c:otherwise>
                </c:choose>
            </table>
            <c:choose>
                <c:when test="${notesForm.buttonValue=='EditNotes'}">
                    <c:set var="display" value="block"/>
                </c:when>
                <c:otherwise>
                    <c:set var="display" value="none"/>
                </c:otherwise>
            </c:choose>    
            <div id="newNotesDiv" style="display: ${display}">
                <table width="100%" border="0" cellpadding="2" cellspacing="0" class="tableBorderNew" >
                    <tr  class="textlabelsBold">
                        <td>Followup Date </td>
                        <td>
                            <html:text property="followupDate" styleId="txtcal2" styleClass="textlabelsBoldForTextBox float-left" onchange="validateDate(this)"/>
                            <img width="16" height="16" src="<%=path%>/img/CalendarIco.gif" alt="cal" class="calendar-img" id="cal2"
                                 onmousedown="insertDateFromCalendar(this.id, 2);">
                        </td>
                    </tr>
                    <c:choose>
                        <c:when test="${notesForm.moduleId=='ACCRUALS' || notesForm.moduleId=='AP_INVOICE'}">
                            <tr class="textlabelsBold">
                                <td>Print on Dispute Items Report</td>
                                <td> <html:checkbox property="printOnReport" styleId="printOnReport" value="true"/></td>
                            </tr>
                        </c:when>
                        <c:when test="${notesForm.moduleId=='AR_INVOICE'}">
                            <tr class="textlabelsBold">
                                <td>Print on AR Statement</td>
                                <td> <html:checkbox property="printOnReport" styleId="printOnReport" value="true"/></td>
                            </tr>
                        </c:when>
                    </c:choose>
                    <tr>
                        <td>&nbsp; </td>
                        <td>
                            <html:textarea property="notes" styleId="notes" rows="4" cols="80" 
                                           styleClass="textlabelsBoldForTextBox"  style="text-transform: uppercase"/>
                        </td>
                    </tr>
                    <tr style="padding-top:10px;">
                        <td align="center" colspan="2">
                            <c:choose>
                                <c:when test="${notesForm.buttonValue=='EditNotes' && not empty notesForm.noteId}">
                                    <input type="button" name="update"  value="Update" class="buttonStyleNew" onclick="updateNotes()"/>
                                    <c:if test="${notesForm.moduleId!='DOMESTIC'}">
                                        <input type="button" name="cancel"  value="Cancel" class="buttonStyleNew" onclick="cancelUpdateNotes()"/>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <input type="button" name="save"  value="Save" class="buttonStyleNew" onclick="saveNotes('${acknowledge}')"/>
                                    <input type="button" name="cancel"  value="Cancel" class="buttonStyleNew" onclick="cancelNotes()"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </table>
            </div>

            <br/>
            <table width="100%" border="0" cellpadding="0"  cellspacing="0" class="tableBorderNew">  
                <tr>
                    <td>

                        <table width="100%" cellpadding="0" cellspacing="0" >
                            <tr class="tableHeadingNew"><td>List of Notes</td>
                                <td align="right">Action&nbsp;<html:select property="actions" value="${notesForm.actions}" onchange="showVoid()" styleClass="dropdown_accounting">
                                        <html:optionsCollection name="actionList"/>
                                    </html:select>&nbsp;</td>
                            </tr>
                        </table>


                    </td>

                </tr>
                <tr>
                    <td>
                        <div id="divtablesty1" style="height: 100%; overflow: auto">
                            <table  border="0" cellpadding="0" cellspacing="0">
                                <%
                                    int i = 0;
                                %>
                                <display:table name="${notesList}"   requestURI= ""  class="displaytagstyleNew" id="notetable" style="width:100%" sort="list"> 

                                    <display:setProperty name="paging.banner.some_items_found">
                                        <span class="pagebanner">
                                            <font color="blue">{0}</font> Notes Details Displayed,For more Notes click on Page Numbers.
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
                                    <display:setProperty name="paging.banner.item_name" value="Note"/>
                                    <display:setProperty name="paging.banner.items_name" value="Notes"/>
                                    <%	//String notesDesc="";
                                        String status = "";
                                        if (notesList != null && !notesList.isEmpty()) {
                                            Notes notes = (Notes) notesList.get(i);
                                            //notesDesc =notes.getNoteDesc();
                                            //notesDesc =notesDesc.replace("),",")<br>(");
                                            //notesDesc=StringFormatter.relpaceString(notesDesc,"),",")<br>");//dbUtil.getData(notesDesc,50);
                                            status = notes.getStatus();

                                    %>
                                    <display:column  style="width:1px;visibility:hidden"   property="id" title=""></display:column>
                                    <%                            if (notes.getNoteTpye() != null && notes.getNoteTpye().equalsIgnoreCase("manual")) {%>
                                    <display:column style="width:20px" >
                                        <span  class="hotspot" onmouseover="tooltip.show('<strong>Manually Entered Notes</strong>', null, event);" onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/contextelement.gif"/></span>
                                        </display:column>
                                        <%
                                        } else if (notes.getNoteTpye() != null && (notes.getNoteTpye().equalsIgnoreCase("auto"))) {
                                        %>
                                        <display:column title="">
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Auto Generated  Notes</strong>', null, event);" onmouseout="tooltip.hide();" style="color:red;size: 16px">*</span>
                                    </display:column>
                                    <%
                                    } else if (notes.getNoteTpye() != null && (notes.getNoteTpye().equalsIgnoreCase("event"))) {
                                    %>
                                    <display:column title="">
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Event Generated  Notes</strong>', null, event);" onmouseout="tooltip.hide();" ><img src="<%=path%>/img/icons/event.png"/></span>
                                        </display:column>
                                        <%
                                            }
                                        %>

                                    <display:column title="Notes" style="width:300Px;">
                                        <div style="width:300px; white-space: normal; text-transform: uppercase;">${fn:replace(notetable.noteDesc,slashN,'<br/>')}</div>
                                    </display:column>  			
                                    <fmt:formatDate pattern="dd-MMM-yyyy HH:mm a" var="followupDate" value="${notetable.followupDate}"/>                       
                                    <display:column title="Followup Date"><c:out value="${followupDate}"></c:out></display:column>
                                    <fmt:formatDate pattern="dd-MMM-yyyy HH:mm a" var="updatedDate" value="${notetable.updateDate}"/>
                                    <display:column title="Created Date"><c:out value="${updatedDate}"></c:out></display:column>
                                    <display:column title="user" property="updatedBy"></display:column>
                                    <%if (notes.getVoidNote().equals("N") && (null!=status && !NotesConstants.STATUS_COMPLETED.equals(status)) && notes.getNoteType().equalsIgnoreCase("manual")) {%>
                                    <display:column title="Actions">
                                        <c:if test="${roleDuty.deleteManualNotes}">
                                            <span class="hotspot" onmouseover="tooltip.show('<strong>Void</strong>', null, event);" onmouseout="tooltip.hide();">
                                                <img src="<%=path%>/img/icons/trash.jpg" border="0"
                                                     onclick="setAsVoid('${notetable.id}', '<%=NotesConstants.SET_AS_VOID%>')"/>
                                            </span>
                                        </c:if>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Edit</strong>', null, event);" onmouseout="tooltip.hide();">
                                            <img src="<%=path%>/img/icons/edit.gif" border="0"
                                                 onclick="editNotes('${notetable.id}')"/>
                                        </span>
                                    </display:column>
                                    <%}
                                        }
                                        i++;%>
                                </display:table>
                            </table>
                        </div>
                    </td>
            </table>


            <html:hidden property="buttonValue"/>	
            <html:hidden property="documentName" value="${param.moduleId}"/>
            <html:hidden name="moduleId" property="moduleId" value="${param.moduleId}"/>
            <html:hidden name="moduleRefId" property="moduleRefId" value="${param.moduleRefId}"/>     
            <html:hidden name="itemName" property="itemName" value="${itemName}"/>
            <html:hidden name="acknowledge" property="acknowledge" value="${acknowledge}"/>
            <html:hidden property="noteId" styleId="noteId"/>			
        </html:form>
    </body>

    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

