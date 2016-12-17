<%--
    Document   : hsCode
    Created on : Jan 3, 2012, 2:58:19 PM
    Author     : Mei
--%>
<%@include file="init.jsp" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="/WEB-INF/tlds/date.tld" prefix="date"%>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
<body>
    <cong:form  id="lclUnitSsRemarksForm"  name="lclUnitSsRemarksForm" action="/lclImportUnitNotes.do" >
        <cong:hidden name="methodName" id="methodName"></cong:hidden>
        <cong:hidden name="headerId" id="headerId" value='${lclUnitSsRemarksForm.headerId}'></cong:hidden>
        <cong:hidden name="unitId" id="unitId" value='${lclUnitSsRemarksForm.unitId}'></cong:hidden>
        <cong:hidden name="unitNo" id="unitNo" value='${lclUnitSsRemarksForm.unitNo}'></cong:hidden>
        <cong:hidden name="id" id="id"/>
        <table cellpadding="0" cellspacing="0" width="97%" class="tableHeadingNew">
            <tr>
                <td> Unit Notes <span style="color: red;">${lclUnitSsRemarksForm.unitNo}</span></td>
                <td>
                    <c:if test="${not empty lclUnitSsRemarksForm.headerId}">
                        <cong:div styleClass="button-style1 disabledButton" style="float:right" onclick="showNotes()"> Add </cong:div>
                    </c:if>
                </td>
            </tr>
        </table>
        <table id="notesTable" width="60%"  style="margin:5px 0; float:left; display:none;">
            <tr>

                <td class="td textlabelsBoldforlcl" id="followup" align="left" width="10%">Followup Date</td>
                <td><cong:calendarNew  name="followUpDate" id="followUpDate" calType="future"  styleClass="textlabelsBoldForTextBox" onchange="validateDate(this)"/> 
                </td>
                <td valign="middle">
                    &nbsp;&nbsp;&nbsp;<p id="priView" class="textlabelsBoldforlcl">Priority View
                        <input type="checkbox" name="priorityView" id="priorityView" title="Checked=PriorityView" onclick="priorityCheck()"/>
                    </p>
                </td>
                <td class="textBoldforlcl"  id="followUpHead">Send Followup Event Email To</td>
                <td align="right"  id="followUpHeadEmail">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Me<cong:checkbox name="userEmail" id="userEmail" container="NULL"/></td>
            </tr>
            <tr>
                <td valign="top"class="textlabelsBoldforlcl" >Note</td>
                <td><textarea name="remarks" cols="30" rows="3" id="remarks" style="text-transform:uppercase;color:black;font-size:12px"></textarea></td>
                <td width="10%"></td>
                <td valign="top" colspan="2" id="followUpText"><textarea name="followUpEmail" cols="30" rows="3" id="followUpEmail" style="color:black;font-size:12px"></textarea></td>
            </tr>
            <tr><td colspan="5" align="center">
                    <input type="button" id="savNotes" value="Save" class="button-style1" onclick="submitNotes('addUnitNotes')"/>
		    <input type="button" id="updateNotes" value="update" class="button-style1" onclick="submitNotes('updateUnitNotes')"/>
                    <input type="button"  value="Cancel" class="button-style1" onclick="cancelNotes()"/>
                </td></tr>
        </table>

        <br/>
        <table width="98%" cellpadding="0" cellspacing="0" class="tableHeadingNew">
            <tr><td>List of Notes</td>
                <td align="right">Action&nbsp;
                    <html:select property="actions" styleId="actions"  onchange="showAllNotes('displayNotes')">
                        <html:option value="4">Manual Notes</html:option>
                        <html:option value="">Show All</html:option>
                        <html:option value="1">Show Void</html:option>
                        <html:option value="2">FollowupDate Exists</html:option>
                        <html:option value="3">FollowupDate Past</html:option>
                        <html:option value="5">Auto Notes</html:option>
                        <html:option value="6">Events</html:option>
                        <html:option value="7">Disputed Notes</html:option>
                        <html:option value="8">Tracking Notes</html:option>
                        <html:option value="9">Special Notes</html:option>
                        <html:option value="10">CTS</html:option>
                        <html:option value="11">EDISTG</html:option>
                    </html:select>&nbsp;</td>
            </tr>
        </table>
    </cong:form>
    <c:if test="${not empty notesList}">
        <cong:div style="width:99%; float:left; height:270px; overflow-y:scroll; border:1px solid #dcdcdc">
            <display:table name="${notesList}" id="unitnotes" class="dataTable" requestURI="/lclImportUnitNotes.do" style="width:100%">
                <display:column style="width:20Px">
                    <c:if test="${unitnotes.type eq 'manual'}">
                        <img src="${path}/img/icons/contextelement.gif" style="cursor:pointer" alt="" title="Manual Notes"/>
                    </c:if>
                    <c:if test="${unitnotes.type eq 'auto'}">
                        <img alt="auto Notes" title="Auto Generated Notes" style="vertical-align: middle"
                             src="${path}/img/icons/redAsterisk.png"/>
                    </c:if>
                </display:column>
                <display:column title="Notes" style="width:350Px;">
                    <div style="width:500px; white-space: normal"> ${fn:replace(unitnotes.remarks,'\\N','<br/>')}</div>
                </display:column>
                <display:column title="Followup Date" style="width:90Px;">
                    <fmt:formatDate pattern="dd-MMM-yyyy" var="followUpDate" value="${unitnotes.followupDateTime}"/>
                    ${fn:toUpperCase(followUpDate)}
                </display:column>
                <display:column title="Created Date" style="width:90Px;">
                    <fmt:formatDate pattern="dd-MMM-yyyy HH:mm:ss" var="enteredDate" value="${unitnotes.modifiedDatetime}"/>
                    ${enteredDate}
                </display:column>
                <display:column title="User" style="width:70Px;">
                    ${fn:toUpperCase(unitnotes.modifiedby.loginName)}
                </display:column>
                <display:column title="Action" style="width:30Px;">
                    <c:if test="${roleDuty.deleteManualNotes && unitnotes.type ne 'auto' && unitnotes.type ne 'Void'}">
			 <c:if test="${(unitnotes.enteredBy.userId == loginuser.userId) && (unitnotes.type=='manual')}">
                         <img src="${path}/img/icons/edit.gif" alt="edit" height="12px" width="12px"
			      onclick="editNote('${unitnotes.id}','${unitnotes.remarks}','${followUpDate}','${unitnotes.followupEmail}')" style="cursor:pointer"/>
			 </c:if>
                         <c:if test="${(followUpDate ne '' && followUpDate ne null)}">
                        <img src="${path}/img/icons/email.png" alt="email" height="12px" width="12px" title="${unitnotes.followupEmail}"/>
                        </c:if>
                        <img src="${path}/img/icons/trash.jpg" alt="delete" height="12px" width="12px" onclick="deleteNotes('Are you sure you want to delete?', '${unitnotes.id}');" style="cursor:pointer"/>
                    </c:if>
                </display:column>
            </display:table>
        </cong:div>
    </c:if>
    <script type="text/javascript">
        $(document).ready(function(){
            $("#followUpDate").change(function(){
                if($(this).val()!==''){
                    $("#followUpHead").show();
                    $("#followUpHeadEmail").show();
                    $("#followUpText").show();
                    $("#priView").hide();
                }else{
                    $("#followUpHead").hide();
                    $("#followUpHeadEmail").hide();
                    $("#followUpText").hide();
                    $("#priView").show();
                }
            });
            if(parent.$("#lockMessage").val()!==undefined && parent.$("#lockMessage").val()!==''){
                $('.disabledButton').hide();
            }
        });
        function showAllNotes(methodName) {
            var header = $('#headerId').val();
            $('#headerId').val(header);
            var unit = $('#unitId').val();
            $('#unitId').val(unit);
            $('#methodName').val(methodName);
            $('#lclUnitSsRemarksForm').submit();
        }
        function showNotes() {
            clearValues();
            $("#notesTable").show();
            $("#followUpHead").hide();
            $("#followUpHeadEmail").hide();
            $("#followUpText").hide();
	    $("#savNotes").css("display", "block");
	    $("#updateNotes").css("display", "none");
        }
	function editNote(id,remarks,followUpdate,followupEmail){	  
	    $("#id").val(id)
	    $("#priorityView").attr('checked', false);
            $("#userEmail").attr('checked', false);
	    $("#notesTable").show();  
	    $("#followUpHead").show();
            $("#followUpHeadEmail").show();
            $("#followUpText").show();
            $("#priView").hide();
	    $("#followUpDate").val(followUpdate);
	    $("#remarks").val(remarks);
	    $("#followUpEmail").val(followupEmail);
	    $("#savNotes").css("display", "none");
	    $("#updateNotes").css("display", "block");
	}
        function cancelNotes() {
            $("#notesTable").hide();
            clearValues();
        }
        function clearValues() {
	    $('#id').val('');
            $('#remarks').val('');
            $('#followUpDate').val('');
            $('#userEmail').attr("checked", false);
            $('#followUpEmail').val('');
            $('#priView').show();
        }
        function submitForm(methodName)
        {
            $("#methodName").val(methodName);
            $("#lclUnitSsRemarksForm").submit();
        }
        function submitNotes(methodName) {
            var followupEmail=$('#followUpEmail').val();
            var checked=$('#userEmail').attr("checked")?true:false;
            if ($('#remarks').val() === "" || $('#remarks').val() === null) {
                sampleAlert('This field is required');
                $('#remarks').css("border-color", "red");
                $('#remarks').focus();
            } else if ($('#followUpDate').val() !== "" && checked===false
                && ($('#followUpEmail').val() === '' || $('#followUpEmail').val() === null)) {
                sampleAlert('Please Enter FollowUp Email');
                $('#followUpEmail').css("border-color", "red");
                $('#followUpEmail').focus();
            }else if (followupEmail != "") {
                var email = followupEmail.split(",");
                for (var i = 0; i < email.length; i++) {
                    if (!validateEmail(email[i], 1, 0)) {
                        $('#followUpEmail').css("border-color", "red");
                        $('#followUpEmail').focus();
                        sampleAlert('Please Enter valid EmailId');
                        return false;
                    }
                }
                showProgressBar();
                submitForm(methodName);
                hideProgressBar();
            }  else {
                showProgressBar();
                submitForm(methodName);
                hideProgressBar();
            }
        }
        function priorityCheck() {
            if ($('#priorityView').is(":checked")) {
                $("#followup").hide();
                $("#followUpDate").hide();
                $("followUpHeadEmail").hide();
                $("#userEmail").hide();
            } else {
                $("#followup").show();
                $("#followUpDate").show();
                $("followUpHeadEmail").show();
                $("#userEmail").show();
            }
        }
        function deleteNotes(txt, notesId) {
            $('#id').val(notesId);
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function(v) {
                    if (v === 1) {
                        showProgressBar();
                        $("#methodName").val("voidNotes");
                        $("#lclUnitSsRemarksForm").submit();
                        hideProgressBar();
                        $.prompt.close();
                    }
                    else if (v === 2) {
                        $.prompt.close();
                    }
                }
            });
        }
        function validateEmail(field) {
            var regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,5}$/;
            return (regex.test(field)) ? true : false;
        }
    </script>
</body>