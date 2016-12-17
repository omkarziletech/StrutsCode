<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="init.jsp" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="/WEB-INF/tlds/date.tld" prefix="date"%>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@include file="/taglib.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
<cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
<cong:javascript src="${path}/jsps/LCL/js/common/remarks.js"/>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript">
        $(document).ready(function () {
            removeColour();
            var ntesymb = $('#ntesymb').val();
            var splNts = $('#splNotes').val();
            if (null !== ntesymb && "" !== ntesymb) {
                disableCheckBox(ntesymb, splNts);
            }
            var moduleId = $('#moduleId').val();
            if (moduleId !== 'Quote') {
                showeventOnly();
            }
            $('#savNotes').click(function () {
                $(".required").each(function () {
                    if ($(this).val().length === 0)
                    {
                        sampleAlert('This field is required');
                        $(this).css("border-color", "red");
                        $(this).focus();
                        return false;
                    } else {
                        if ($('#priorityView').is(":checked")) {
                            submitAjaxForm('refreshPriorityNotes', '#lclRemarksForm', '#refreshPriority');
                        }
                        else {
                            var followupEmail = $('#followupEmail').val();
                            var follow = $('#followupDate').val();
                            var me = $("#userMail").attr('checked');
                            var priorityView = $("#priorityView").attr('checked');
                            if (moduleId === 'Quote') {
                                submitForm('addRemarks');
                            }
                            var eventNotes = $('#eventNotes').val();
                            if (follow === "" && eventNotes === "" && priorityView === false) {
                                submitForm('addRemarks');
                            }
                            if (followupEmail === "" && me === false && follow !== '' && follow !== null) {
                                sampleAlert('Please Enter followUp Email');
                                $('#followupEmail').css("border-color", "red");
                                return false;
                            }
                            else if (followupEmail !== "") {
                                var email = followupEmail.split(",");
                                for (var i = 0; i < email.length; i++) {
                                    if (!validateEmail(email[i], 1, 0)) {
                                        sampleAlert('Please Enter valid EmailId');
                                        return false;
                                    }
                                }
                                submitForm('addRemarks');
                            } else {
                                submitForm('addRemarks');
                            }
                        }
                    }
                });
            });
            $('#updateNotes').click(function () {
                $(".required").each(function () {
                    if ($(this).val().length == 0)
                    {
                        sampleAlert('This field is required');
                        $(this).css("border-color", "red");
                        $(this).focus();
                        return false;
                    } else {
                        var followupEmail = $('#followupEmail').val();
                        var follow = $('#followupDate').val();
                        var me = $("#userMail").attr('checked');
                        if (followupEmail === "" && me === false && follow != '' && follow != null) {
                            sampleAlert('Please Enter followUp Email');
                            $('#followupEmail').css("border-color", "red");
                            return false;
                        }
                        else if (followupEmail !== "" && moduleId !== 'Quote') {
                            var email = followupEmail.split(",");
                            for (var i = 0; i < email.length; i++) {
                                if (!validateEmail(email[i], 1, 0)) {
                                    sampleAlert('Please Enter valid EmailId');
                                    return false;
                                }
                            }
                            submitForm('updateRemarks');
                        } else {
                            submitForm('updateRemarks');
                        }
                    }
                });
            });
            if (parent.$("#lockMessage").val() !== undefined && parent.$("#lockMessage").val() !== '') {
                $('.disabledButton').hide();
            }
        });
    </script>
</head>
<body>
    <cong:div style="width:99%; float:left;">
        <cong:form  id="lclRemarksForm"  name="lclRemarksForm" action="/lclRemarks.do" >
            <cong:hidden id="moduleName" name="moduleName" value="${lclRemarksForm.moduleName}"/>
            <span id="warning" style="color:red"></span>
            <cong:hidden name="fileNumberId" value="${fileNumberId}"/>
            <cong:hidden name="id" id="id" />
            <cong:hidden name="clntAcctNo" id="clntAcctNo" />
            <cong:hidden name="shpAcctNo" id="shpAcctNo" />
            <cong:hidden name="frwdAcctNo" id="frwdAcctNo" />
            <cong:hidden name="consAcctNo" id="consAcctNo" />
            <cong:hidden name="methodName" id="methodName"/>
            <cong:hidden name="noteType" id="noteType"/>
            <cong:hidden name="noteDesc" id="noteDesc"/>
            <cong:hidden name="tpId" id="tpId"/>
            <cong:hidden name="splNotes" id="splNotes" value="${splNotes}"/>
            <input type="hidden" name="ntesymb" id="ntesymb" value="${ntesymb}"/>
            <input type="hidden" name="clntId" id="clntId" value="${clntId}"/>
            <input type="hidden" name="shpId" id="shpId" value="${shpId}"/>
            <input type="hidden" name="fwdId" id="fwdId" value="${fwdId}"/>
            <input type="hidden" name="consId" id="consId" value="${consId}"/>
            <input type="hidden" name="histEdi" id="histEdi" value="${histEdi}"/>
            <input type="hidden" name="removeColor" id="removeColor" value="${greenColor}">
            <cong:hidden name="fileNumber" value="${fileNumber}"/>
            <cong:hidden id="moduleId" name="moduleId" value="${moduleId}"/>
            <cong:hidden name="type"/>
            <c:choose>
                <c:when test="${clntId=='clntNotes' || shpId=='shpNotes' || fwdId=='fwdNotes' || consId=='conNotes' || histEdi=='histEdi'}">
                </c:when>
                <c:otherwise>
                    <cong:table id="noteTable" cellpadding="0" cellspacing="0" width="100%" border="0">
                        <cong:tr styleClass="tableHeadingNew">
                            <cong:td width="90%">Notes for File No: <span style="color: red;">${lclRemarksForm.fileNumber}</span></cong:td>
                            <cong:td>
                                <cong:div styleClass="button-style1 disabledButton" style="float:left" onclick="addNotes()"> Add </cong:div>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td colspan="2">
                                <cong:table id="notesTable" width="40%" style="margin:5px 0; float:left; display:none;" border="0">
                                    <cong:tr>
                                        <cong:td styleClass="td textlabelsBoldforlcl" id="followup" align="left" width="10%">Followup Date</cong:td>
                                        <cong:td  onclick="followDateOnly()">
                                            <cong:calendarNew  name="followupDate" id="followupDate" calType="future" styleClass="textlabelsBoldForTextBox"  onchange="validateDate(this)"/> </cong:td>
                                        <cong:td valign="middle" id="priView" styleClass="textlabelsBoldforlcl">
                                            &nbsp;&nbsp;&nbsp;Priority View
                                            <cong:checkbox name="priorityView" id="priorityView" title="Checked=PriorityView" onclick="checkRemove()" container="null"/>
                                        </cong:td>
                                    </cong:tr>
                                    <c:if test="${lclRemarksForm.moduleId!='Quote'}">
                                        <cong:tr>
                                            <cong:td id="followEvents" styleClass="textlabelsBoldforlcl" valign="bottom">Followup Events</cong:td>
                                            <cong:td align="left" onclick="" valign="bottom">
                                                <%--<cong:selectNew name="eventNotes" collections="eventList" indexProperty="remarks" valueProperty="remarks" container="null"></cong:selectNew>--%>
                                                <c:choose>
                                                    <c:when test="${status!='WV'}">
                                                        <select id="eventNotes" name="eventNotes" class="textlabelsBoldForTextBox" valign="bottom" onchange="showeventOnly()()">
                                                            <option value="">Select</option>
                                                            <option value="Cargo Received-Verified">Cargo Received - Verified</option>
                                                        </select>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <cong:selectNew name="eventNotes" id="eventNotes" container="null" style="color:black">
                                                            <cong:option value="">Select</cong:option>
                                                        </cong:selectNew>
                                                    </c:otherwise>
                                                </c:choose>
                                            </cong:td>
                                            <cong:td></cong:td>
                                            <cong:td></cong:td>
                                            <cong:td></cong:td>
                                            <cong:td id="followEmail" valign="bottom" align="left">Send Followup Event Email To</cong:td>
                                            <cong:td id="loginMail" styleClass="textlabelsBoldforlcl">&nbsp;&nbsp;&nbsp;Me<cong:checkbox name="userMail" id="userMail" container="NULL"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</cong:td>
                                        </cong:tr>
                                        <cong:tr>
                                            <cong:td styleClass="td textlabelsBoldforlcl">Note</cong:td>
                                            <cong:td colspan="3">
                                                <cong:textarea name="remarks" cols="40" rows="3" id="remarks" styleClass="required" style="text-transform:uppercase;color:black;font-size:12px"></cong:textarea>
                                            </cong:td>
                                            <cong:td colspan="3" id="emailForFollow">
                                                <cong:textarea name="followupEmail" cols="40" rows="3" id="followupEmail" style="color:black;font-size:12px"></cong:textarea>
                                            </cong:td>
                                        </cong:tr>
                                    </c:if>
                                    <c:if test="${lclRemarksForm.moduleId=='Quote'}">
                                        <cong:tr>
                                            <cong:td></cong:td>
                                            <cong:td></cong:td>
                                            <cong:td></cong:td>
                                            <cong:td></cong:td>
                                            <cong:td></cong:td>
                                            <cong:td></cong:td>
                                        </cong:tr>
                                        <cong:tr>
                                            <cong:td styleClass="td textlabelsBoldforlcl">Notes</cong:td>
                                            <cong:td colspan="3">
                                                <cong:textarea name="remarks" cols="40" rows="3" id="remarks" styleClass="required" style="text-transform:uppercase;color:black;font-size:12px"></cong:textarea>
                                            </cong:td>
                                        </cong:tr>
                                    </c:if>
                                    <cong:tr>
                                        <cong:td>&nbsp;</cong:td>
                                        <cong:td colspan="3">
                                            <input type="button" id="savNotes" value="Save" class="button-style1" />
                                            <input type="button" id="updateNotes" value="update" class="button-style1" />
                                            <input type="button"  value="Cancel" class="button-style1" onclick="cancelNotes()"/>
                                        </cong:td>
                                    </cong:tr>
                                </cong:table>
                            </cong:td>
                        </cong:tr>
                    </cong:table>
                    <br/>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${clntId=='clntNotes' || shpId=='shpNotes' || fwdId=='fwdNotes' || consId=='conNotes'}">
                    <cong:tr>
                        <%@include file="/jsps/LCL/newLclTpNote.jsp"%>
                        <cong:hidden name="actions" id="actions" />
                    </cong:tr>

                    <table width="100%" id="splNoteTable" cellpadding="0" cellspacing="0" class="tableHeadingNew">
                        <cong:tr>
                            <cong:td>
                                <cong:checkbox name="splNotes" id="acctNotes" onclick="spclNotes('${path}',id,'${clntId}','${shpId}','${fwdId}','${consId}');" container="NULL" title="Check to see Accounting Notes"/><cong:label id="acct" text="Accounting($)"/>
                                <cong:checkbox name="splNotes" id="airNotes" onclick="spclNotes('${path}',id,'${clntId}','${shpId}','${fwdId}','${consId}');" container="NULL"  title="Check to see Air Notes"/><cong:label id="air" text="Air(A)"/>
                                <cong:checkbox name="splNotes" id="autoNotes" onclick="spclNotes('${path}',id,'${clntId}','${shpId}','${fwdId}','${consId}');" container="NULL" title="Check to see Auto Notes"/><cong:label id="auto" text="Auto(*)"/>
                                <cong:checkbox name="splNotes" id="docNotes" onclick="spclNotes('${path}',id,'${clntId}','${shpId}','${fwdId}','${consId}');" container="NULL" title="Check to see Documentation Notes"/><cong:label id="doc" text="Doc(D)"/>
                                <cong:checkbox name="splNotes" id="fclNotes" onclick="spclNotes('${path}',id,'${clntId}','${shpId}','${fwdId}','${consId}');" container="NULL" title="Check to see FCL Notes"/><cong:label id="fcl" text="FCL(F)"/>
                                <cong:checkbox name="splNotes" id="genrlNotes" onclick="spclNotes('${path}',id,'${clntId}','${shpId}','${fwdId}','${consId}');" container="NULL" title="Check to see General/Sales Notes"/><cong:label id="gen" text="General/Sales(G)"/>
                                <cong:checkbox name="splNotes" id="lclNotes" onclick="spclNotes('${path}',id,'${clntId}','${shpId}','${fwdId}','${consId}');" container="NULL" title="Check to see LCL Notes"/><cong:label id="lcl" text="LCL(L)"/>
                                <cong:checkbox name="splNotes" id="whseNotes" onclick="spclNotes('${path}',id,'${clntId}','${shpId}','${fwdId}','${consId}');" container="NULL" title="Check to see Warehouse Notes"/><cong:label id="whse" text="Whse(W)"/>
                                <cong:checkbox name="splNotes" id="voidNotes" onclick="spclNotes('${path}',id,'${clntId}','${shpId}','${fwdId}','${consId}');" container="NULL" title="Check to see Void Notes"/><cong:label id="void" text="Void(V)"/>
                                <cong:checkbox name="splNotes" id="importNotes" onclick="spclNotes('${path}',id,'${clntId}','${shpId}','${fwdId}','${consId}');" container="NULL" title="Check to see Import Notes"/><cong:label id="import" text="Import(I)"/>
                            </cong:td>
                        </cong:tr>
                    </table>
                </c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${histEdi=='histEdi'}">
                        </c:when>
                        <c:otherwise>
                            <table width="100%" cellpadding="0" cellspacing="0" class="tableHeadingNew">
                                <tr><td>List of Notes</td>
                                    <td align="right">Action&nbsp;
                                        <html:select property="actions" styleId="actions"  onchange="searchNotesType('display')">
                                            <html:option value="manualNotes">Manual Notes</html:option>
                                            <html:option value="showAll">Show All</html:option>
                                            <html:option value="showVoid">Show Void</html:option>
                                            <c:if test="${setColor == 'true'}">
                                                <html:option value="voidedNotes" styleClass="red">Voided Notes</html:option>
                                            </c:if>
                                            <c:if test="${setColor != 'true'}">
                                                <html:option value="voidedNotes">Voided Notes</html:option>
                                            </c:if>                                           
                                            <html:option value="followUpDateExist">FollowupDate Exists</html:option>
                                            <html:option value="followUpDatePast">FollowupDate Past</html:option>
                                            <html:option value="autoNotes">Auto Notes</html:option>
                                            <html:option value="events">Events</html:option>
                                            <html:option value="disputedNotes">Disputed Notes</html:option>
                                            <html:option value="trackingNotes">Tracking Notes</html:option>
                                            <html:option value="specialNotes">Special Notes</html:option>
                                            <html:option value="cts">CTS</html:option>
                                            <html:option value="edistg">EDISTG</html:option>
                                            <html:option value="OnHoldNotes">OnHold Notes</html:option>
                                        </html:select>&nbsp;</td>
                                </tr>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
        </cong:form>
    </cong:div>
    <cong:div id="tpNoteDiv" style="width:99%; float:left; height:300px; overflow-y:scroll; border:1px solid #dcdcdc">
        <display:table name="${remarksList}" id="remarks" class="dataTable" requestURI="/lclRemarks.do" style="width:100%">
            <display:column title="" >
                <c:choose>
                    <c:when test="${remarks.noteSymbol=='$'}">
                        <span  title="Accounting Notes">${remarks.noteSymbol}</span>
                    </c:when>
                    <c:when test="${remarks.noteSymbol=='A'}">
                        <span  title="Air Notes">${remarks.noteSymbol}</span>
                    </c:when>
                    <c:when test="${remarks.noteSymbol=='*'}">
                        <span  title="Auto Notes">${remarks.noteSymbol}</span>
                    </c:when>
                    <c:when test="${remarks.noteSymbol=='D'}">
                        <span  title="Documentation Notes">${remarks.noteSymbol}</span>
                    </c:when>
                    <c:when test="${remarks.noteSymbol=='F'}">
                        <span  title="FCL Notes">${remarks.noteSymbol}</span>
                    </c:when>
                    <c:when test="${remarks.noteSymbol=='G'}">
                        <span  title="General Notes">${remarks.noteSymbol}</span>
                    </c:when>
                    <c:when test="${remarks.noteSymbol=='L'}">
                        <span  title="LCL Notes">${remarks.noteSymbol}</span>
                    </c:when>
                    <c:when test="${remarks.noteSymbol=='W'}">
                        <span  title="Warehouse">${remarks.noteSymbol}</span>
                    </c:when>
                    <c:when test="${remarks.noteSymbol=='V'}">
                        <span  title="Void Notes">${remarks.noteSymbol}</span>
                    </c:when>
                    <c:when test="${remarks.noteSymbol=='I'}">
                        <span  title="Import Notes">${remarks.noteSymbol}</span>
                    </c:when>
                    <c:when test="${remarks.type eq 'Outsource'}">
                        <img src="${path}/img/icons/contextelement.gif" alt="Outsource"
                             style="cursor:pointer" title="Outsource Notes"/>
                    </c:when>
                    <c:when test="${remarks.type eq 'Refused'}">
                        <img src="${path}/img/icons/contextelement.gif" 
                             style="cursor:pointer" alt="Refused" title="Refused Notes"/>
                    </c:when>
                    <c:when test="${remarks.type eq 'Manual Note' || remarks.type eq 'QT-ManualNotes' ||
                                    remarks.type eq 'DR-ManualNotes' || remarks.type eq 'BL-ManualNotes'}">
                            <img src="${path}/img/icons/contextelement.gif"
                                 style="cursor:pointer" title="Manual Notes" alt="ManualNotes"/>
                    </c:when>
                    <c:when test="${remarks.type eq 'Priority View'}">
                        <img src="${path}/img/icons/priorityNotes.png" style="cursor:pointer"
                             width="16" height="16" align="middle" alt="Priority" title="Priority View Notes"/>
                    </c:when>
                    <c:when test="${remarks.type == 'Cargo Received-Verified'}">
                        <span title="Followup Event Notes"><img src="${path}/img/icons/notes.GIF" style="cursor:pointer" width="16" height="16" align="middle" alt=""/></span>
                        </c:when>
                        <c:when test="${remarks.status == 'Closed'}">
                        <span title="Voided Notes"><img src="${path}/img/icons/Recycle.png" style="cursor:pointer" width="16" height="16" align="middle" alt=""/></span>
                        </c:when>
                        <c:when test="${remarks.type == 'LclCorrections'}">
                        <span class="hotspot" title="Event Generated  Notes">
                            <img src="${path}/img/icons/event.png"/>
                        </span>
                    </c:when>
                    <c:otherwise>
                        <img alt="auto Notes" title="Auto Generated Notes" style="vertical-align: middle"
                             src="${path}/img/icons/redAsterisk.png"/>
                    </c:otherwise>
                </c:choose>
            </display:column>
            <display:column title="Notes" style="width:300Px;">
                <div style="width:500px; white-space: normal">
                    ${fn:replace(remarks.remarks,'\\N','<br/>')}
                </div>
            </display:column>
            <c:if test="${lclRemarksForm.actions ne 'voidedNotes'}">
                <display:column title="FollowUp">
                    <fmt:formatDate pattern="dd-MMM-yyyy HH:mm:ss" var="followupDate" value="${remarks.followupDate}"/>
                    ${followupDate}
                </display:column>
            </c:if>
            <fmt:formatDate pattern="dd-MMM-yyyy" var="editFollowupDate" value="${remarks.followupDate}"/>
            <display:column title="Created Date" >
                <fmt:formatDate pattern="dd-MMM-yyyy HH:mm:ss" var="enteredDate" value="${remarks.enteredDatetime}"/>
                ${enteredDate}
            </display:column>
            <display:column title="User">
                <c:choose>
                    <c:when test="${lclRemarksForm.actions=='specialNotes'}">
                        ${remarks.userName}
                    </c:when>
                    <c:otherwise>
                        ${remarks.enteredBy.loginName}
                    </c:otherwise>
                </c:choose>
            </display:column>
            <display:column title="Type">
                ${remarks.type}
            </display:column>
            <c:choose>
                <c:when test="${lclRemarksForm.actions == 'voidedNotes'}">
                    <display:column title="DeletedDate">
                        <fmt:formatDate pattern="dd-MMM-yyyy HH:mm:ss" var="deletedDate"  value="${remarks.modifiedDatetime}"/>
                        ${deletedDate}
                    </display:column>
                    <display:column title="User" >
                        ${remarks.modifiedBy.loginName}
                    </display:column>
                </c:when>
                <c:otherwise>
                    <display:column title="Action" >
                        <c:choose>
                            <c:when test="${lclRemarksForm.moduleName eq 'Exports'}">
                                <c:if test="${(remarks.enteredBy.userId eq loginuser.userId) && (remarks.type eq 'Manual Note'
                                              || remarks.type eq 'QT-ManualNotes' || remarks.type eq 'DR-ManualNotes' || remarks.type eq 'BL-ManualNotes')}">
                                      <img src="${path}/img/icons/edit.gif" alt="edit" height="12px" width="12px" title="Edit Note"
                                           onclick="editNote('${remarks.id}', '${remarks.remarks}', '${editFollowupDate}', '${remarks.followupEmail}')" style="cursor:pointer"/>
                                </c:if>
                                <c:if test="${not empty remarks.followupEmail}">
                                    <img src="${path}/img/icons/email.png" alt="email" height="12px" width="12px"
                                         title="${remarks.followupEmail}"/>
                                </c:if>
                                <c:if test="${roleDuty.deleteNotes && (remarks.type eq 'Manual Note' || remarks.type eq 'QT-ManualNotes'
                                              || remarks.type eq 'DR-ManualNotes' || remarks.type eq 'BL-ManualNotes')}">
                                      <img src="${path}/img/icons/trash.jpg" alt="delete" height="12px" width="12px"
                                           title="Delete Note" onclick="deleteNotes('${remarks.id}')" style="cursor:pointer"/>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <c:if test="${roleDuty.deleteManualNotes}">
                                    <c:if test="${(remarks.enteredBy.userId == loginuser.userId) && (remarks.type eq 'Manual Note'
                                                  || remarks.type eq 'QT-ManualNotes' || remarks.type eq 'DR-ManualNotes' || remarks.type eq 'BL-ManualNotes')}">
                                          <img src="${path}/img/icons/edit.gif" alt="edit" height="12px" width="12px"
                                               onclick="editNote('${remarks.id}', '${remarks.remarks}', '${editFollowupDate}', '${remarks.followupEmail}')" style="cursor:pointer"/>
                                    </c:if>
                                    <c:if test="${not empty remarks.followupEmail}">
                                        <img src="${path}/img/icons/email.png" alt="email" height="12px" width="12px"
                                             title="${remarks.followupEmail}"/>
                                    </c:if>
                                    <c:if test="${(remarks.type eq 'Manual Note' || remarks.type eq 'QT-ManualNotes' || remarks.type eq 'DR-ManualNotes' ||
                                                  remarks.type eq 'BL-ManualNotes' || remarks.type=='Priority View' || remarks.type == 'Cargo Received-Verified')}">
                                          <img src="${path}/img/icons/trash.jpg" alt="delete" align="right" height="12px" width="12px"
                                               onclick="deleteNotes('${remarks.id}');" style="cursor:pointer"/>
                                    </c:if>
                                </c:if>
                            </c:otherwise>
                        </c:choose>

                        <c:if test="${(remarks.id ne '' && splNotes ne 'voidNotes' && remarks.noteSymbol ne 'V') && (clntId=='clntNotes' || shpId=='shpNotes' || fwdId=='fwdNotes' || consId=='conNotes')}">
                            <img src="${path}/img/icons/edit.gif" alt="edit" height="12px" width="12px"
                                 onclick="tpEditNote('${remarks.id}', '${remarks.remarks}', '${remarks.noteSymbol}')" style="cursor:pointer"/>
                        </c:if>
                        <c:if test="${(roleDuty.deleteManualNotes && remarks.id ne '' && splNotes ne 'voidNotes' && remarks.noteSymbol ne 'V')  && (clntId=='clntNotes' || shpId=='shpNotes' || fwdId=='fwdNotes' || consId=='conNotes')}">
                            <img src="${path}/img/icons/trash.jpg" alt="delete" height="12px" width="12px" onclick="deleteTpNote('Are you sure you want to delete?', '${remarks.id}', '${remarks.remarks}')" style ="cursor: pointer"/>
                        </c:if>
                    </display:column>
                </c:otherwise>
            </c:choose>
        </display:table>
    </cong:div>
    <script type="text/javascript">
        function submitForm(methodName) {
            parent.$("#notes").addClass('green-background');
            parent.$("#notesBottom").addClass('green-background');
            $("#methodName").val(methodName);
            $("#lclRemarksForm").submit();
        }
        function removeColour() {
            var noteFlag = $('#removeColor').val();
            if (noteFlag != '') {
                if (noteFlag === 'true') {
                    $('.notes').removeClass('button-style1');
                    $('.notes').addClass('green-background');
                } else {
                    $('.notes').removeClass('green-background');
                    $('.notes').addClass('button-style1');
                }
            }
        }

        /* script for alert */
        function sampleAlert(txt) {
            $.prompt(txt);
        }
        function showBlock(tar) {
            $(tar).show(300);
        }
        function cancelNotes() {
            $("#remarks").val('');
            $("#followupDate").val('');
            $("#remarks").css("border-color", "");
            $("#priorityView").attr('checked', false);
            $("#userMail").attr('checked', false);
            $('#followupDate').show();
            $('#followEvents').show();
            $('#eventNotes').show();
            $('#followup').show();
            $('#priorityView').show();
            $('#priView').show();
            $('#followEmail').hide();
            $('#emailForFollow').hide();
            $('#userMail').hide();
            $('#notesTable').hide();
            $('#loginMail').hide();
        }


        function disableCheckBox(ntesymb, splNts) {
            document.getElementById("lclNotes").disabled = true;
            document.getElementById("fclNotes").disabled = true;
            document.getElementById("autoNotes").disabled = true;
            document.getElementById("genrlNotes").disabled = true;
            document.getElementById("whseNotes").disabled = true;
            document.getElementById("docNotes").disabled = true;
            document.getElementById("acctNotes").disabled = true;
            document.getElementById("airNotes").disabled = true;
            document.getElementById("voidNotes").disabled = true;
            document.getElementById("importNotes").disabled = true;
            var symb = ntesymb.split('-');
            if (symb.toString().indexOf("$") !== -1) {
                document.getElementById("acctNotes").disabled = false;
                document.getElementById("acct").className = "green";
            } else {
                document.getElementById("acctNotes").checked = false;
                document.getElementById("acct").className = "black";
            }
            if (symb.toString().indexOf("A") !== -1) {
                document.getElementById("airNotes").disabled = false;
                document.getElementById("air").className = "green";
            } else {
                document.getElementById("airNotes").checked = false;
                document.getElementById("air").className = "black";
            }
            if (symb.toString().indexOf("*") !== -1) {
                document.getElementById("autoNotes").disabled = false;
                document.getElementById("auto").className = "green";
            } else {
                document.getElementById("autoNotes").checked = false;
                document.getElementById("auto").className = "black";
            }
            if (symb.toString().indexOf("D") !== -1) {
                document.getElementById("docNotes").disabled = false;
                document.getElementById("doc").className = "green";
            } else {
                document.getElementById("docNotes").checked = false;
                document.getElementById("doc").className = "black";
            }
            if (symb.toString().indexOf("F") !== -1) {
                document.getElementById("fclNotes").disabled = false;
                document.getElementById("fcl").className = "green";
            } else {
                document.getElementById("fclNotes").checked = false;
                document.getElementById("fcl").className = "black";
            }
            if (symb.toString().indexOf("G") !== -1) {
                document.getElementById("genrlNotes").disabled = false;
                document.getElementById("gen").className = "green";
            } else {
                document.getElementById("genrlNotes").checked = false;
                document.getElementById("gen").className = "black";
            }
            if (symb.toString().indexOf("L") !== -1) {
                document.getElementById("lclNotes").disabled = false;
                document.getElementById("lcl").className = "green";
            } else {
                document.getElementById("lclNotes").checked = false;
                document.getElementById("lcl").className = "black";
            }
            if (symb.toString().indexOf("V") !== -1) {
                document.getElementById("voidNotes").disabled = false;
                document.getElementById("void").className = "green";
            } else {
                document.getElementById("voidNotes").checked = false;
                document.getElementById("void").className = "black";
            }
            if (symb.toString().indexOf("W") !== -1) {
                document.getElementById("whseNotes").disabled = false;
                document.getElementById("whse").className = "green";
            } else {
                document.getElementById("whseNotes").checked = false;
                document.getElementById("whse").className = "black";
            }
            if (symb.toString().indexOf("I") !== -1) {
                document.getElementById("importNotes").disabled = false;
                document.getElementById("import").className = "green";
            } else {
                document.getElementById("importNotes").checked = false;
                document.getElementById("import").className = "black";
            }
            if (splNts == "acctNotes" && symb.toString().indexOf("$") !== -1) {
                document.getElementById("acctNotes").checked = true;
                document.getElementById("acct").className = "blue";
            }
            if (splNts == "airNotes" && symb.toString().indexOf("A") !== -1) {
                document.getElementById("airNotes").checked = true;
                document.getElementById("air").className = "blue";
            }
            if (splNts == "autoNotes" && symb.toString().indexOf("*") !== -1) {
                document.getElementById("autoNotes").checked = true;
                document.getElementById("auto").className = "blue";
            }
            if (splNts == "docNotes" && symb.toString().indexOf("D") !== -1) {
                document.getElementById("docNotes").checked = true;
                document.getElementById("doc").className = "blue";
            }
            if (splNts == "fclNotes" && symb.toString().indexOf("F") !== -1) {
                document.getElementById("fclNotes").checked = true;
                document.getElementById("fcl").className = "blue";
            }
            if (splNts == "genrlNotes" && symb.toString().indexOf("G") !== -1) {
                document.getElementById("genrlNotes").checked = true;
                document.getElementById("gen").className = "blue";
            }
            if (splNts == "lclNotes" && symb.toString().indexOf("L") !== -1) {
                document.getElementById("lclNotes").checked = true;
                document.getElementById("lcl").className = "blue";
            }
            if (splNts == "voidNotes" && symb.toString().indexOf("V") !== -1) {
                document.getElementById("voidNotes").checked = true;
                document.getElementById("void").className = "blue";
            }
            if (splNts == "whseNotes" && symb.toString().indexOf("W") !== -1) {
                document.getElementById("whseNotes").checked = true;
                document.getElementById("whse").className = "blue";
            }
            if (splNts == "importNotes" && symb.toString().indexOf("I") !== -1) {
                document.getElementById("importNotes").checked = true;
                document.getElementById("import").className = "blue";
            }
        }

        function spclNotes(path, id, clntId, shpId, fwdId, consId) {
            var href = "";
            href = path + "/lclRemarks.do?methodName=display&actions=specialNotes&moduleId=Quote&splNotes=" + id + "";
            if (clntId == 'clntNotes') {
                href += "&clntAcctNo=" + $('#clntAcctNo').val() + "&clntId=" + clntId;
            }
            if (shpId == 'shpNotes') {
                href += "&shpAcctNo=" + $('#shpAcctNo').val() + "&shpId=" + shpId;
            }
            if (fwdId == 'fwdNotes') {
                href += "&frwdAcctNo=" + $('#frwdAcctNo').val() + "&fwdId=" + fwdId;
            }
            if (consId == 'conNotes') {
                href += "&consAcctNo=" + $('#consAcctNo').val() + "&consId=" + consId;
            }
            document.location.href = href;
        }
        function checkRemove() {
            var event = $('#eventNotes').val();
            if ($('#priorityView').is(":checked")) {
                $('#followupDate').hide();
                $('#followEvents').hide();
                $('#eventNotes').hide();
                $('#followup').hide();
                $('#followEmail').hide();
                $('#emailForFollow').hide();
                $('#loginMail').hide();
            } else {
                $('#followupDate').show();
                $('#followEvents').show();
                $('#eventNotes').show();
                $('#followup').show();
                $('#priorityView').show();
                $('#priView').show();
                $('#followEmail').hide();
                $('#emailForFollow').hide();
                $('#loginMail').hide();
            }
        }
        function followDateOnly() {
            $('#followEmail').show();
            $('#emailForFollow').show();
            $('#loginMail').show();
            $('#priorityView').hide();
            $('#priView').hide();
            $('#followEvents').hide();
            $('#eventNotes').hide();
        }
        function showeventOnly() {
            var changeList = document.getElementById('eventNotes').value;
            if (changeList != null && changeList != "" && changeList == "Cargo Received-Verified") {
                $('#followupDate').hide();
                $('#followup').hide();
                $('#priorityView').hide();
                $('#priView').hide();
                $('#followEmail').show();
                $('#emailForFollow').show();
                $('#loginMail').show();
            }
            else {
                $('#emailForFollow').hide();
                $('#loginMail').hide();
                $('#followEmail').hide();
            }
        }

        function submitAjaxForm(methodName, formName, selector) {
            showProgressBar();
            $("#methodName").val(methodName);
            var params = $(formName).serialize();
            $.post($(formName).attr("action"), params,
                    function (data) {
                        $(selector).html(data);
                        $(selector, window.parent.document).html(data);
                    });
            parent.$.colorbox.close();
        }
        function validateEmail(field) {
            var regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,5}$/;
            return (regex.test(field)) ? true : false;
        }
    </script>
</body>
