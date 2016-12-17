<%@include file="init.jsp" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
<script>
    $(document).ready(function() {
        $('#savNotes').click(function() {
            $(".required").each(function() {
                if ($(this).val().length == 0) {
                    sampleAlert('This field is required');
                    $(this).css("border-color", "red");
                    $(this).focus();
                    return false;
                } else {
                    submitForm('addRemarks');
                }
            });
        });

    });
</script>
<body>
    <cong:form  id="lclSSHeaderRemarksForm"  name="lclSSHeaderRemarksForm" action="/lclSSHeaderRemarks.do" >
        <span id="warning" style="color:red"></span>
        <cong:hidden name="headerId" id="headerId" value="${lclSSHeaderRemarksForm.headerId}"/>
        <cong:hidden name="id" id="id" />
        <cong:hidden name="voyageNumber" id="voyageNumber" value="${lclSSHeaderRemarksForm.voyageNumber}"/>
        <cong:div style="width:99%; float:left;">
            <cong:table id="noteTable" cellpadding="0" cellspacing="0" width="100%" border="0">
                <cong:tr styleClass="tableHeadingNew">
                    <cong:td width="90%">Notes for Voyage No: <span style="color: red;">${lclSSHeaderRemarksForm.voyageNumber}</span></cong:td>
                    <cong:td>
                        <c:if test="${lclSSHeaderRemarksForm.moduleName ne 'LCL_IMP_VOYAGE'}">
                            <cong:div styleClass="button-style1" style="float:left" onclick="addNotes()"> Add </cong:div>
                        </c:if>
                    </cong:td>
                </cong:tr>
                <c:if test="${lclSSHeaderRemarksForm.moduleName ne 'LCL_IMP_VOYAGE'}">
                    <cong:tr>
                        <cong:td colspan="2">
                            <cong:table id="notesTable" width="40%" style="margin:5px 0; float:left; display:none;" border="0">
                                <cong:tr>
                                    <cong:td styleClass="td textlabelsBoldforlcl" id="followup" align="left" width="10%">Followup Date</cong:td>
                                    <cong:td  onclick="followDateOnly()">
                                        <cong:calendarNew  name="followupDate" id="followupDate" calType="future" styleClass="textlabelsBoldForTextBox"  onchange="validateDate(this)"/> </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td id="followEvents" styleClass="textlabelsBoldforlcl" valign="bottom">Followup Events</cong:td>
                                    <cong:td align="left" onclick="" valign="bottom">
                                        <select id="eventNotes" name="eventNotes" class="textlabelsBoldForTextBox" valign="bottom" >
                                            <option value="">Select</option>
                                        </select>
                                    </cong:td>
                                    <cong:td></cong:td>
                                    <cong:td></cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="td textlabelsBoldforlcl">Note</cong:td>
                                    <cong:td colspan="3">
                                        <cong:textarea name="remarks" cols="40" rows="3" id="remarks" styleClass="required" style="text-transform:uppercase;color:black;font-size:12px"></cong:textarea>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td>&nbsp;</cong:td>
                                    <cong:td colspan="3">
                                        <input type="button" id="savNotes" value="Save" class="button-style1" />
                                        <input type="button"  value="Cancel" class="button-style1" onclick="cancelNotes()"/>
                                    </cong:td>
                                </cong:tr>
                                <cong:hidden name="methodName" id="methodName"/>
                            </cong:table>
                        </cong:td>
                    </cong:tr>
                </c:if>
            </cong:table>
            <c:if test="${lclSSHeaderRemarksForm.moduleName ne 'LCL_IMP_VOYAGE'}">
                <br/>
                <table width="100%" cellpadding="0" cellspacing="0" class="tableHeadingNew">
                    <tr><td>List of Notes</td>
                        <td align="right">Action&nbsp;
                            <html:select property="actions" styleId="actions"  onchange="submitForm('displayNotes')">
                                <html:option value="show All">Show All</html:option>
                                <html:option value="1">Show Void</html:option>
                                <html:option value="2">FollowupDate Exists</html:option>
                                <html:option value="3">FollowupDate Past</html:option>
                                <html:option value="4">Manual Notes</html:option>
                                <html:option value="5">Auto Notes</html:option>
                                <html:option value="6">Events</html:option>
                                <html:option value="7">Disputed Notes</html:option>
                                <html:option value="8">Tracking Notes</html:option>
                                <html:option value="9">Special Notes</html:option>
                                <html:option value="10">CTS</html:option>

                                <c:if test="${setColor == 'true'}">
                                    <html:option styleClass="red" value="11">VoidedNotes</html:option>
                                </c:if>
                                <c:if test="${setColor != 'true'}">
                                    <html:option value="11">VoidedNotes</html:option>
                                </c:if>
                            </html:select>&nbsp;</td>
                    </tr>
                </table>
            </c:if>
        </cong:div>
        <cong:div style="width:99%; float:left; height:240px; overflow-y:scroll; border:1px solid #dcdcdc">
            <display:table name="${remarksList}" id="remarks" class="dataTable" requestURI="/lclRemarks.do" style="width:100%">
                <display:column style="width:7px">
                    <c:choose>
                        <c:when test="${remarks.type eq  'Manual Note'}">
                            <span title="Manual Notes">
                                <img src="${path}/img/icons/contextelement.gif" style="cursor:pointer" alt=""/>
                            </span>
                        </c:when>
                        <c:otherwise>
                            <img alt="auto Notes" title="Auto Generated Notes" style="vertical-align: middle"
                                 src="${path}/img/icons/redAsterisk.png"/>
                        </c:otherwise>
                    </c:choose>
                </display:column>
                <display:column title="Notes" style="width:600Px;">
                    <div style="width:600px; white-space: normal">${fn:replace(remarks.remarks,'\\N','<br/>')}</div>
                </display:column>
                <c:if test="${lclSSHeaderRemarksForm.moduleName ne 'LCL_IMP_VOYAGE'}">
                    <display:column title="FollowUp">
                        <fmt:formatDate pattern="dd-MMM-yyyy HH:mm:ss" var="followupDate" value="${remarks.followupDatetime}"/>
                        ${followupDate}</display:column></c:if>
                <display:column title="Created Date" style="width:100Px;">
                    <fmt:formatDate pattern="dd-MMM-yyyy HH:mm:ss" var="enteredDate" value="${remarks.enteredDatetime}"/>
                    ${enteredDate}
                </display:column>
                <display:column title="User" style="width:70Px;">
                    <c:choose>
                        <c:when test="${lclRemarksForm.actions==9}">
                            ${fn:toUpperCase(remarks.userName)}
                        </c:when>
                        <c:otherwise>
                            ${fn:toUpperCase(remarks.modifiedBy.loginName)}
                        </c:otherwise>
                    </c:choose>
                </display:column>
                <c:choose>
                    <c:when test="${lclSSHeaderRemarksForm.actions=='11'}">
                        <display:column title="Deleted Date" style="width:100Px;">
                            <fmt:formatDate pattern="dd-MMM-yyyy HH:mm:ss" var="deletedDate" value="${remarks.modifiedDatetime}"/>
                            ${deletedDate}
                        </display:column>
                        <display:column title="User" style="width:70Px;">

                            ${fn:toUpperCase(remarks.modifiedBy.loginName)}
                        </display:column>
                    </c:when>
                </c:choose>
                <c:choose>
                    <c:when test="${remarks.type == 'Manual Note' && roleDuty.deleteNotes}">
                        <display:column title="Action" >
                            <img src="${path}/img/icons/trash.jpg" alt="delete" height="12px" width="12px" onclick="deleteNotes('Are you sure you want to delete?', '${remarks.id}');" style="cursor:pointer"/>
                        </display:column>             
                    </c:when>
                    <c:otherwise>
                        <c:out value=""/>
                    </c:otherwise>
                </c:choose>
                <c:forEach items="${remarksList}" var="remarks">
                    <c:choose>
                        <c:when test="${zebra=='odd'}">
                            <c:set var="zebra" value="even"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="zebra" value="odd"/>
                        </c:otherwise>
                    </c:choose>
                    <cong:tr styleClass="${zebra}">
                        <cong:td></cong:td>
                    </cong:tr>
                </c:forEach>
            </display:table>
        </cong:div>
    </cong:form>
    <script type="text/javascript">

        function addNotes() {
            showBlock('#notesTable');
        }

        function showBlock(tar) {
            $(tar).show(300);
        }

        function cancelNotes() {
            $("#remarks").val('');
            $("#followupDate").val('');
            $("#remarks").css("border-color", "");
            //$("#priorityView").attr('checked', false);
            //$("#userMail").attr('checked', false);
            $('#followupDate').show();
            $('#followEvents').show();
            $('#eventNotes').show();
            $('#followup').show();
            //$('#priorityView').show();
            $('#priView').show();
            //$('#followEmail').hide();
            //$('#emailForFollow').hide();
            //$('#userMail').hide();
            $('#notesTable').hide();
            //$('#loginMail').hide();
        }

        function followDateOnly() {
            //$('#priorityView').hide();
            //$('#priView').hide();
            $('#followEvents').hide();
            $('#eventNotes').hide();
            //$('#followEmail').hide();
            //$('#emailForFollow').hide();
            //$('#loginMail').hide();
        }

        function submitForm(methodName) {
            $("#methodName").val(methodName);
            $("#lclSSHeaderRemarksForm").submit();
        }

        function deleteNotes(txt, id)
        {
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function(v) {
                    if (v == 1) {
                        showProgressBar();
                        $("#id").val(id);
                        submitForm('deleteNotes');
                        hideProgressBar();
                        $.prompt.close();
                    }
                    else if (v == 2) {
                        $.prompt.close();
                    }
                }
            });
        }
    </script>
</body>
