<%-- 
    Document   : hsCode
    Created on : Jan 3, 2012, 2:58:19 PM
    Author     : Thamizh
--%>
<%@include file="init.jsp" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@include file="/jsps/preloader.jsp" %>
<body>
    <cong:form  id="lclUnitSsRemarksForm"  name="lclUnitSsRemarksForm" action="/lclUnitSsRemarks.do" >
        <cong:hidden name="methodName" id="methodName"></cong:hidden>
        <cong:hidden name="headerId" id="headerId" />
        <cong:hidden name="unitId" id="unitId" />
        <cong:hidden name="voyageNo" id="voyageNo"/>
        <cong:table id="noteTable" cellpadding="0" cellspacing="0" width="100%" border="0">
            <cong:tr styleClass="tableHeadingNew">
                <cong:td width="90%">Notes for Voyage No: <span style="color: red;">${lclUnitSsRemarksForm.voyageNo}</span></cong:td>
                <cong:td>
                    <cong:div styleClass="button-style1" style="float:left" onclick="addNotes()"> Add </cong:div>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td colspan="2">
                    <cong:table id="notesTable" width="40%" style="margin:5px 0; float:left; display:none;" border="0">
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
        </cong:table>
        <br/>
        <table width="100%" cellpadding="0" cellspacing="0" class="tableHeadingNew">
            <tr><td>List of Notes</td>
                <td align="right">Action&nbsp;
                    <html:select property="actions" styleId="actions"  onchange="showNotes('displayNotes')">
                        <html:option value="">Show All</html:option>
                        <html:option value="auto">Auto Notes</html:option>
                        <html:option value="manual">Manual Notes</html:option>
                        <c:if test="${setColor == 'true'}">
                            <html:option styleClass="red" value="voidedNotes">Voided Notes</html:option>
                        </c:if>
                        <c:if test="${setColor != 'true'}">
                            <html:option value="voidedNotes">Voided Notes</html:option>
                        </c:if>
                    </html:select>&nbsp;</td>
            </tr>
        </table>
    </cong:form>
    <cong:div style="width:99%; float:left; height:220px; overflow-y:scroll; border:1px solid #dcdcdc">
        <table class="dataTable">
            <thead>
            <th width="1%"></th>
            <th>Notes</th>
            <th>Followup Date</th>
            <th>Created Date</th>
            <th>User</th>
            <c:if test="${lclUnitSsRemarksForm.actions == 'voidedNotes'}">
                <th>Deleted Date</th>
                <th>User</th>
            </c:if>
            <th>Action</th>
        </thead>
        <c:if test="${not empty notesList}">
            <tbody>
                <c:forEach var="unitnotes" items="${notesList}">
                    <c:choose>
                        <c:when test="${rowStyle eq 'oddStyle'}">
                            <c:set var="rowStyle" value="evenStyle"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="rowStyle" value="oddStyle"/>
                        </c:otherwise>
                    </c:choose>
                    <tr class="${rowStyle}">
                        <td width="1%">
                            <c:choose>
                                <c:when test="${unitnotes.type eq 'manual'}">
                                    <img src="${path}/img/icons/contextelement.gif" width="12px" height="12px"
                                         alt="autoIcon" title="Manual Notes"/>
                                </c:when>
                                <c:otherwise>
                                    <span style="color:red;cursor:pointer"  title="Auto Generated Notes">*</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <div style="width:500px; white-space: normal">${fn:replace(unitnotes.remarks,'\\N','<br/>')}</div>
                        </td>
                        <td></td>
                        <td>
                            <fmt:formatDate pattern="dd-MMM-yyyy HH:mm:ss" var="enteredDate" value="${unitnotes.modifiedDatetime}"/>
                            ${enteredDate}
                        </td>
                        <td> ${unitnotes.modifiedby.loginName} </td>
                        <c:if test="${lclUnitSsRemarksForm.actions == 'voidedNotes'}">
                            <td>
                                <fmt:formatDate pattern="dd-MMM-yyyy HH:mm:ss" var="modifiedDate" value="${unitnotes.modifiedDatetime}"/>
                                ${modifiedDate}
                            </td>
                            <td> ${unitnotes.modifiedby.loginName}</td>
                        </c:if>
                        <td>
                            <c:if test="${unitnotes.type eq 'manual' && roleDuty.deleteNotes }">
                                <img src="${path}/img/icons/trash.jpg" alt="delete" title="Delete Manual Notes"
                                     height="12px" width="12px" onclick="deleteNotes('${path}','${unitnotes.id}');" style="cursor:pointer"/>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </c:if>
    </table>
</cong:div>
<script type="text/javascript">
    $(document).ready(function(){
        $('#savNotes').click(function(){
            $(".required").each(function(){
                if($(this).val().length == 0)
                {
                    sampleAlert('This field is required');
                    $(this).css("border-color","red");
                    $(this).focus();
                    return false;
                }else{
                    submitForm('saveNotes');
                }
            });
        });
    });
    function showNotes(methodName){
        showLoading();
        $('#methodName').val(methodName);
        var header=  $('#headerId').val();
        $('#headerId').val(header);
        var unit=  $('#unitId').val();
        $('#unitId').val(unit);
        $('#lclUnitSsRemarksForm').submit();
    }
    function addNotes(){
        showBlock('#notesTable');
    }

    function showBlock(tar){
        $(tar).show(300);
    }

    function cancelNotes(){
        $("#remarks").val('');
        $("#followupDate").val('');
        $("#remarks").css("border-color","");
        $('#notesTable').hide();
    }

    function submitForm(methodName){
        showLoading();
        $("#methodName").val(methodName);
        $("#lclUnitSsRemarksForm").submit();
    }

    function deleteNotes(path,id){
        $.prompt("Are you sure you want to delete?",{
            buttons:{
                Yes:1,
                No:2
            },
            submit:function(v){
                if(v==1){
                    var header=  $('#headerId').val();
                    var unit=  $('#unitId').val();
                    showProgressBar();
                    document.location.href=path+"/lclUnitSsRemarks.do?methodName=deleteNotes&id="+id+"&headerId="+header+"&unitId="+unit;
                    hideProgressBar();
                    $.prompt.close();
                }
                else if(v==2){
                    $.prompt.close();
                }
            }
        });
    }
</script>
</body>