<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/taglib.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/lclInbond.js"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<body style="background:#ffffff">
    <cong:div style="width:99%; float:left; overflow-x: hidden;">
        <cong:form name="lclInbondForm" id="lclInbondForm" action="/lclInbonds.do">
            <cong:hidden name="fileNumberId" value="${fileNumberId}"/>
            <cong:hidden name="fileNumber" value="${fileNumber}"/>
            <cong:hidden name="status" id="status"/>
            <cong:hidden name="unitId" id="unitId"/>
            <cong:hidden name="headerId" id="headerId"/>
            <cong:hidden name="fileState" id="fileState"/>
            <input type="hidden" id="insertInbondFlag" value="${not empty insertInbondFlag ? insertInbondFlag : 'false'}"/>
            <cong:table width="100%">
                <cong:tr styleClass="tableHeadingNew" >
                    <cong:td>
                        Inbond list for File No:
                        <span class="fileNo">${lclInbondForm.fileNumber}</span>
                    </cong:td>
                    <cong:td>
                        <cong:div styleClass="button-style1" style="float:left" onclick="addInbond();">Add Inbond</cong:div>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="2">
                        <cong:table id="inbondTable" width="109.5%"  style="display: none; border: 1px solid #dcdcdc; border-top:0 ">
                            <cong:tr>
                                <cong:td width="8%" styleClass="td">Inbond #</cong:td>
                                <cong:td><cong:text styleClass="text mandatory textuppercaseLetter" name="inbondNo" id="inbondNo" value="${lclInbond.inbondNo}" onkeyup="checkForNumberOnly(this)" maxlength="9"/>
                                    <input type="checkbox" name="allowfreetext" id="allowfreetext" onclick="checkBoxMaxLength()" style="align:floatLeft" title="Checked=Allow any Format<br> UnChecked=Allow only Number Format"/>
                                </cong:td>
                                <cong:td width="8%" styleClass="td">INBond Type</cong:td>
                                <cong:td>
                                    <html:select styleClass="textlabelsBoldForTextBox smallDropDown" style="width: 130px" property="inbondType" styleId="inbondType" value="${lclInbond.inbondType}">
                                        <html:option value="" styleClass="tdLeft">Select Inbond Type</html:option>
                                        <html:option value="IT" styleClass="textlabelsBoldForTextBox">IT</html:option>
                                        <html:option value="IE" styleClass="textlabelsBoldForTextBox">IE</html:option>
                                        <html:option value="TE" styleClass="textlabelsBoldForTextBox">TE</html:option>
                                        <html:option value="WDT" styleClass="textlabelsBoldForTextBox">WDT</html:option>
                                        <html:option value="WDIE" styleClass="textlabelsBoldForTextBox">WDIE</html:option>
                                        <html:option value="WDTE" styleClass="textlabelsBoldForTextBox">WDTE</html:option>
                                        <html:option value="IT/QP" styleClass="textlabelsBoldForTextBox">IT/QP</html:option>
                                        <html:option value="IE/QP" styleClass="textlabelsBoldForTextBox">IE/QP</html:option>
                                        <html:option value="TE/QP" styleClass="textlabelsBoldForTextBox">TE/QP</html:option>
                                    </html:select>
                                    <%--<cong:text styleClass="text mandatory" name="inbondType" id="inbondType" value="${lclInbond.inbondType}" maxlength="100"/>--%> </cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td width="8%" styleClass="td">INBond Port</cong:td>
                                <cong:td>
                                    <cong:autocompletor name="inbondPort" id="inbondPort" template="one" query="CONCAT_PORT_NAME" fields="NULL,NULL,NULL,inbondPortId"
                                                        container="NULL" styleClass="text" width="250" shouldMatch="true"/>
                                    <cong:hidden id="inbondPortId" name="inbondPortId"/>
                                </cong:td>
                                <cong:td  width="8%" styleClass="td">INBond Date</cong:td>
                                <cong:hidden name="id" id="id"/>
                                <cong:td><cong:calendarNew styleClass="text cal-text" name="inbondDatetime" id="inbondDatetime" value="${lclInbond.inbondDatetime}"  calType="future"/> </cong:td>
                            </cong:tr>

                            <c:if test="${lclInbondForm.moduleName eq 'Exports'}">
                                <cong:tr>
                                    <cong:td width="8%" styleClass="td">Inbond Open/Closed</cong:td>                                   
                                    <cong:td>
                                        <input type="radio" name="inbondOpenClose" id="openInbond" value="Y" checked="checked"/>Open
                                        <input type="radio" name="inbondOpenClose" id="closedInbond" value="N" />Closed
                                    </cong:td>                                        
                                    <cong:td width="8%" styleClass="td">ECI's Bond</cong:td>                                    
                                    <cong:td>
                                        <input type="radio" name="eciBond" id="eciBondYes" value="Y"/>Yes
                                        <input type="radio" name="eciBond" id="eciBondNo" value="N" />No
                                    </cong:td>                                    
                                </cong:tr>
                            </c:if>                                        
                            <cong:tr>
                                <cong:td></cong:td>
                                <cong:td>
                                    <input type="button" class="button-style1" value="Save" id="saveInbnd" />
                                    <input type="button" class="button-style1" value="Cancel" onclick="hideInbond();"/>
                                </cong:td>
                                <cong:td  colspan="2"></cong:td>
                            </cong:tr>
                        </cong:table>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="2">
                        <table width="100%" border="0"
                               style="border-collapse: collapse; border: 1px solid #dcdcdc" class="dataTable"  id="inbondtable">
                            <thead>
                                <tr>
                                    <th>Inbond #</th>
                                    <th>Type</th>
                                    <th>Date</th>
                                    <th>Port</th>                                   
                                        <c:if test="${lclInbondForm.moduleName eq 'Exports'}">
                                        <th>Inbond O/C</th>                                    
                                        <th>ECI's Bond</th>
                                        </c:if>                                                                     
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <c:forEach items="${inbondList}" var="lclInbond">
                                <c:choose>
                                    <c:when test="${zebra=='odd'}">
                                        <c:set var="zebra" value="even"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="zebra" value="odd"/>
                                    </c:otherwise>
                                </c:choose>
                                <tr class="${zebra}" style="text-transform:uppercase;">
                                    <td>${lclInbond.inbondNo}</td>
                                    <td>${lclInbond.inbondType}</td>
                                    <fmt:formatDate pattern="dd-MMM-yyyy" var="inbondDate" value="${lclInbond.inbondDatetime}"/>
                                    <td>
                                        <c:out value="${inbondDate}"/>
                                    </td>                                 
                                    <td>${lclInbond.inbondPort.unLocationName} </td>
                                    <c:if test="${lclInbondForm.moduleName eq 'Exports'}">
                                        <td>
                                            <c:choose>
                                                <c:when test="${lclInbond.inbondOpenClose eq 'true'}">
                                                    Open
                                                </c:when>
                                                <c:otherwise>
                                                    Closed
                                                </c:otherwise>

                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${lclInbond.eciBond eq 'true'}">
                                                    Yes
                                                </c:when>
                                                <c:otherwise>
                                                    No
                                                </c:otherwise>

                                            </c:choose>
                                        </td>                                 
                                    </c:if>                            

                                    <td>
                                        <c:choose>
                                            <c:when test="${lclInbondForm.moduleName eq 'Exports'}">
                                                <img src="${path}/images/edit.png" alt="edit" style="cursor: pointer" onclick="editInbondExport('${lclInbond.inbondId}', '${lclInbond.inbondNo}', '${lclInbond.inbondType}', '${inbondDate}', '${fn:replace(lclInbond.inbondPort.unLocationName,'\'','\\&#39;')}', '${lclInbond.inbondPort.id}', '${lclInbond.inbondOpenClose}', '${lclInbond.eciBond}')"/>
                                                <img src="${path}/img/icons/trash.jpg" alt="delete" height="12px" width="12px" onclick="deleteInbond('Are you sure you want to delete?');
                                                        closeInbond('${lclInbond.inbondId}', '${lclInbond.inbondNo}')" style="cursor: pointer"/>
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${path}/images/edit.png" alt="edit" style="cursor: pointer" onclick="editInbond('${lclInbond.inbondId}', '${lclInbond.inbondNo}', '${lclInbond.inbondType}', '${inbondDate}', '${fn:replace(lclInbond.inbondPort.unLocationName,'\'','\\&#39;')}', '${lclInbond.inbondPort.id}')"/>
                                                <img src="${path}/img/icons/trash.jpg" alt="delete" height="12px" width="12px" onclick="deleteInbond('Are you sure you want to delete?');
                                                        closeInbond('${lclInbond.inbondId}', '${lclInbond.inbondNo}')" style="cursor: pointer"/>                                           
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <%-- inbond common info starts --%>
            <%-- header --%>
            <br/>
            <c:if test="${lclInbondForm.moduleName eq 'Imports'}">
                <cong:table id="main-info-table" width="100%" styleClass="table">
                    <cong:tr>
                        <cong:td>
                            <div id="voy-detail" onclick="toggle('#inner-info-table');" class="head-tag">
                                <img alt="down" id="img-down" src="${path}/img/icons/down.gif"/>
                                <img alt="down" id="img-right" src="${path}/img/icons/right_arrow.gif" style="display: none"/>
                                <span id="click-msg" class="green">
                                    Click this bar to hide
                                </span>
                            </div>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <%-- table --%>
                        <cong:td>
                            <cong:table id="inner-info-table" width="100%" styleClass="" border="">
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Inbond via</cong:td>
                                    <cong:td colspan="3">
                                        <cong:text name="inbondVia" id="inbondVia" styleClass="textbox width-445px" container="NULL" maxlength="100"
                                                   value="${importInbond.inbondVia}"/>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Value estimated</cong:td>
                                    <cong:td styleClass="textBoldforlcl">
                                        <c:choose>
                                            <c:when test="${importInbond.declaredValueEstimated}">
                                                <input type="radio" value="Y" name="valueEstimated" checked="yes"/> Yes
                                                <input type="radio" value="N" name="valueEstimated"/> No
                                            </c:when>
                                            <c:otherwise>
                                                <input type="radio" value="Y" name="valueEstimated"/> Yes
                                                <input type="radio" value="N" name="valueEstimated" checked="yes"/> No
                                            </c:otherwise>
                                        </c:choose>
                                    </cong:td>
                                    <cong:td styleClass="textlabelsBoldforlcl">Weight estimated</cong:td>
                                    <cong:td styleClass="textBoldforlcl">
                                        <c:choose>
                                            <c:when test="${importInbond.declaredWeightEstimated eq true}">
                                                <input type="radio" value="Y" name="weightEstimated" checked="yes"/> Yes
                                                <input type="radio" value="N" name="weightEstimated"/> No
                                            </c:when>
                                            <c:otherwise>
                                                <input type="radio" value="Y" name="weightEstimated"/> Yes
                                                <input type="radio" value="N" name="weightEstimated" checked="yes"/> No
                                            </c:otherwise>
                                        </c:choose>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Class of entry</cong:td>
                                    <cong:td>
                                        <cong:text name="entryClass" id="entryClass" styleClass="textbox" container="NULL"  maxlength="50"
                                                   value="${importInbond.entryClass}"/>
                                    </cong:td>
                                    <cong:td styleClass="textlabelsBoldforlcl">Value</cong:td>
                                    <cong:td>
                                        <cong:text name="customReleaseValue" id="customReleaseValue" styleClass="text" container="NULL"
                                                   value="${importInbond.customsReleaseValue}" onkeyup="isNumeric(this);"/>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">IT Class</cong:td>
                                    <cong:td>
                                        <cong:text name="itClass" id="itClass" value="${importInbond.itClass}" styleClass="textbox"  maxlength="50"/>
                                    </cong:td>
                                    <c:if test="${importInbond.lclFileNumber.state ne 'Q'}">
                                        <cong:td styleClass="textlabelsBoldforlcl">7512 Print Entry#</cong:td>
                                        <cong:td styleClass="textBoldforlcl">
                                            <c:choose>
                                                <c:when test="${importInbond.printEntry7512 eq 'L'}">
                                                    <input type="radio" value="L" name="printEntry7512" checked="yes"/> Left
                                                    <input type="radio" value="R" name="printEntry7512"/> Right
                                                    <input type="radio" value="B" name="printEntry7512"/> Both
                                                </c:when>
                                                <c:when test="${importInbond.printEntry7512 eq 'R'}">
                                                    <input type="radio" value="L" name="printEntry7512"/> Left
                                                    <input type="radio" value="R" name="printEntry7512" checked="yes"/> Right
                                                    <input type="radio" value="B" name="printEntry7512"/> Both
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="radio" value="L" name="printEntry7512"/> Left
                                                    <input type="radio" value="R" name="printEntry7512"/> Right
                                                    <input type="radio" value="B" name="printEntry7512" checked="yes"/> Both
                                                </c:otherwise>
                                            </c:choose>
                                        </cong:td>   
                                    </c:if>
                                </cong:tr>
                                <cong:tr>
                                    <cong:td colspan="4" align="center">
                                        <input type="button" class="button" value="Save" onclick="submitForm('saveInbond');"/>
                                    </cong:td>
                                </cong:tr>
                            </cong:table>
                        </cong:td>  
                    </cong:tr>
                </cong:table>
            </c:if>
            <%-- inbond common info ends --%>
            <cong:hidden name="methodName" id="methodName"/>
            <cong:hidden name="moduleName" id="moduleName"/>
        </cong:form>
    </cong:div>
</body>
<script type="text/javascript">
    $(document).ready(function () {
        parent.$("#insertInbondFlag").val($("#insertInbondFlag").val());
        $('#inbondPort').keyup(function () {
            $('#inbondPortId').val('');
        });
        var inbondNumberObj = document.getElementById("inbondNo");
        setCheckBoxOnload(inbondNumberObj)
        checkBoxMaxLength();
        var table = document.getElementById("inbondtable");
        if (table.rows.length > 1) {
            var row = table.rows[1];
            var cell0 = row.cells[0];
            var cell1 = row.cells[1];
            var cell2 = row.cells[2];
            var cell3 = row.cells[3];
            parent.$("#inbonds").addClass('green-background');
            parent.$("#inbond").addClass('green-background');
            parent.$("#inbondimp").addClass('green-background');
            parent.document.getElementById("inbondNumber").innerHTML = cell1.innerHTML+cell0.innerHTML+cell3.innerHTML +cell2.innerHTML;
        }else{
            parent.$("#inbonds").removeClass('green-background');
            parent.$("#inbonds").addClass('button-style1');
            parent.$("#inbond").removeClass('green-background');
            parent.$("#inbond").addClass('button-style1');
            parent.$("#inbondimp").removeClass('green-background');
            parent.$("#inbondimp").addClass('button-style1');
            parent.document.getElementById("inbondNumber").innerHTML = "";
        }
    });
    function checkBoxMaxLength(){
        var inbondChecked= document.getElementById("allowfreetext").checked;
        if(inbondChecked){
            $("#inbondType").removeClass("mandatory");
            $("#inbondNo").attr('maxlength','25');
        }else{
            $("#inbondNo").attr('maxlength','9');
            $("#inbondType").addClass("mandatory");
        }
        $("#inbondNo").val('');
    }
    function setCheckBoxOnload(obj){
        var inbondNumber=obj.value;
        if(inbondNumber.length < 9 || inbondNumber.length > 9 || isNotAlphabetic(inbondNumber)){
            document.getElementById("allowfreetext").checked= false;
        }
    }
    function checkForNumberOnly(obj){
        var inbondChecked= document.getElementById("allowfreetext").checked;
        if(!inbondChecked){
            if(!/^\d*(\.\d{0,6})?$/.test(obj.value)){
                obj.value="";
                sampleAlert("This field should be Numeric");
            }
        }
    }

</script>