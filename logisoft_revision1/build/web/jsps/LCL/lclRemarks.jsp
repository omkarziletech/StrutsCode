<%-- 
    Document   : hsCode
    Created on : Jan 3, 2012, 2:58:19 PM
    Author     : Thamizh
--%>
<%@include file="init.jsp" %>
<script>
     $(document).ready(function(){
       $("#lclRemarksForm").submit(function(){
           currentDate();
       });
     });
       function currentDate(){  
        if($('#followDate').val() == ''){ 
            return; 
        } 
        var d1 = $('#followDate').val(); 
        alert(d1);
        if(d1 == 'NaN' || d1 == 'Invalid Date'){ 
            $('#followDate').val(""); 
            alert("Date must be in dd-MMM-YYYY format"); 
        }else{         
            var d2 = new Date(); 
            alert(d2);
            if((d1.getTime() - d2.getTime()) < 0){ 
                $('#followDate').val(""); 
                alert("followupDate should not be past date."); 
            } 
        } 
}
</script>
<body>
    <form id="lclRemarksForm" method="post" name="lclRemarksForm" action="/logisoft/lclRemarks.do" >
        <cong:hidden name="fileNumberId" value="16"/>
        <cong:hidden name="type" value="Manual Note"/>
        <cong:table cellpadding="0" cellspacing="0" width="100%">
            <cong:tr styleClass="report-title">
                <cong:td> Add Notes</cong:td>
                <cong:td>
                    <cong:div styleClass="button-style1" style="float:right" onclick="showBlock('#notesTable')"> Add </cong:div>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td colspan="2">
                    <cong:table id="notesTable" width="50%" style="margin:5px 0; float:left; display:none;">
                        <cong:tr>
                            <cong:td>Followup Date</cong:td>
                            <cong:td>
                                <cong:calendarNew name="followupDate" id="followDate"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td>&nbsp;</cong:td>
                            <cong:textarea name="remarks" cols="30" rows="3" id="remarks"></cong:textarea>
                        </cong:tr>
                        <cong:tr>
                            <cong:td>&nbsp;</cong:td>
                            <cong:td>
                                <input type="button" value="Save" class="button-style1" onclick="submitForm('addRemarks');"/> 
                                <input type="button" value="Cancel" class="button-style1" onclick="cancelNotes()"/>
                                <cong:hidden name="methodName" id="methodName"/>
                            </cong:td>
                        </cong:tr>

                    </cong:table>
                </cong:td>
            </cong:tr>
        </cong:table>

    </form>

    <table width="100%" cellpadding="0" cellspacing="0" >
        <tr class="report-title"><td>List of Notes<td>
            <td align="right">Action&nbsp;
                <select property="actions">
                    <option>select one</option>    
                    <option>Show All</option>
                    <option>Show void</option>
                    <option>FollowupDate Exists</option>
                    <option>FollowupDate Past</option>
                    <option>Manual Notes</option>
                    <option>Auto Notes</option>
                    <option>Events</option>
                    <option>Disputed Notes</option>
                    <option>Tracking Notes</option>
                </select>&nbsp;</td>

        </tr>
    </table>
    <cong:div style="width:99%; float:left; height:230px; overflow-y:scroll; border:1px solid #dcdcdc">
        <cong:table border="1" width="100%" style="margin:5px 0; float:left">
            <cong:tr styleClass="tableHeading2">
                <cong:td width="2%">&nbsp;</cong:td>
                <cong:td width="2%">&nbsp;</cong:td>
                <cong:td width="25%">Notes</cong:td>
                <cong:td width="15%">FollowUp Date</cong:td>
                <cong:td width="15%">Created Date</cong:td>
                <cong:td width="5%">User</cong:td>
                <cong:td width="5%">Action</cong:td>
            </cong:tr>
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
                        <cong:td></cong:td>
                        <cong:td>${remarks.remarks}</cong:td>
                        <cong:td>${remarks.followupDate}</cong:td>
                        <cong:td>${remarks.enteredDatetime}</cong:td>
                        <cong:td>${remarks.enteredBy.loginName}</cong:td>
                        <cong:td>
                            <cong:img src="${path}/images/error.png" height="12px" width="12px"/>
                        </cong:td>
                    </cong:tr>
                </c:forEach>
        </cong:table>
    </cong:div>
    <script type="text/javascript">
    function submitForm(methodName){
        $("#methodName").val(methodName);
        $("#lclRemarksForm").submit();
    }
        
    /* script for alert */
    function sampleAlert(txt){
        $.prompt(txt);
    }
    function showBlock(tar){
        $(tar).show(300);
           
    }
    function cancelNotes(){
        document.getElementById("notesTable").style.display="none";
    }
    </script>
</body>