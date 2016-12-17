<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<cong:javascript  src="${path}/jsps/LCL/js/export/searchScreen.js"/>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="../includes/resources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<body>
<cong:form  id="lclTerminatePopupForm"  name="lclTerminatePopupForm" action="lclTerminatePopup.do" >
    <cong:table >
        <cong:tr>
            <cong:td styleClass="tableHeadingNew" >
                <cong:div styleClass="floatLeft">Batch Terminate</span>
                </cong:div>
            </cong:td>
        </cong:tr>
    </cong:table>
    <cong:table>
        <cong:tr>
            <cong:td >
                <cong:table>
                    <cong:tr>
                        <cong:td ></cong:td>
                        <cong:td>
                        <input type="radio" value="Cancelled by Customer" name="terminateOption"/>Cancelled by Customer
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td></cong:td><cong:td><input type="radio" name="terminateOption" value="Cargo Picked Up" onclick="terminateCargo()"/>Cargo Picked Up</cong:td>
                </cong:tr>
                <cong:tr><cong:td></cong:td><cong:td><input type="radio" name="terminateOption" value="New Booking Issued"/>New Booking Issued</cong:td></cong:tr>
                <cong:tr>
                    <cong:td>
                    </cong:td>
                    <cong:td><input type="radio" name="terminateOption" value="Consolidated"/>Consolidated
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td>
                    </cong:td>
                    <cong:td><input type="radio" name="terminateOption" value="Duplicated"/>Duplicated
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td>
                    </cong:td>
                    <cong:td><input type="radio" name="terminateOption" value="Converted To Air"/>Converted To Air
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td>
                    </cong:td>
                    <cong:td><input type="radio" name="terminateOption" value="Salvage"/>Salvage
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td>
                    </cong:td>
                    <cong:td><input type="radio" name="terminateOption" value="Other"/>Other
                    </cong:td>
                </cong:tr>
                <cong:tr><cong:td >&nbsp;</cong:td></cong:tr><cong:tr><cong:td></cong:td><cong:td>  &nbsp;&nbsp;Do you want to terminate ALL bookings in this consolidation
                    </cong:td> 
                </cong:tr>
                <cong:tr>
                    <cong:td></cong:td>
                    <cong:td> 
                        <input type="radio" id ="consoTerminate" name="consoTerminate"  value="Y"  /> Yes
                        <input type="radio" id ="consoTerminate" name="consoTerminate"  value="N" checked /> No
                    </cong:td>
                </cong:tr> 
            </cong:table>
        </cong:td>
        <cong:td >
            <cong:table > <cong:tr> <cong:td>Comment<span class="red bold" style="font-size: medium">*</span>
                    </cong:td></cong:tr>
                <cong:tr>
                    <cong:td>   <cong:textarea rows="6" cols="30" styleClass="textLCLuppercase" style="width:300px;resize:none;" name="termComment" id="termComment" value=""></cong:textarea></cong:td>
                </cong:tr></cong:table> 
        </cong:td>
        
    </cong:tr>
    <cong:tr >
        <cong:td colspan="2" >
            <cong:table > 
                <c:forEach var="i" begin="1" end="4">
                     <cong:tr>
                        <c:forEach var="j" begin="1" end="10">
                            <cong:td>
                                <input type="text" name="drNumber"  class="textlabelsBoldForTextBox" style="width:70px"/>
                            </cong:td>
                        </c:forEach>
                     </cong:tr>
                </c:forEach>
            </cong:table> 
        </cong:td>
    </cong:tr>

</cong:table>
<cong:hidden id="methodName" name="methodName"/>
<input type="hidden" id="loginUserId" name="loginUserId" value="${loginuser.userId}">
<br>
<cong:table>
    <cong:tr><cong:td>
            <cong:div styleClass="button-style1" onclick="lclBatchTerminationStatus()"> Submit </cong:div>
        </cong:td></cong:tr>
</cong:table>
</cong:form>
            </body>
</html>
