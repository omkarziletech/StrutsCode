<%-- 
    Document   : commodity
    Created on : Dec 24, 2013, 4:29:04 PM
    Author     : Rajeshkumar
--%>

<display:table name="${bl}" class="display-table" id="cargo">
    <c:set var="index" value="${cargo_rowNum}"/>
    <c:choose>
        <c:when test="${empty cargo.packageId}">
            <c:set var="error" value="error-indicator"></c:set>
        </c:when>
        <c:otherwise>
            <c:set var="error" value="border-olive"></c:set>
        </c:otherwise>
    </c:choose>
    <display:column title="Package">
        <cong:autocompletor name="packageDesc" id="packageDesc${index}" template="packageType" styleClass="mandatory textbox ${error}"
                            fields="NULL,packageAbbr${index}" width="500" query="PACKAGE_TYPE" value="${cargo.packageDesc}"
                            container="NULL" shouldMatch="true"  callback="getPackageAbbr('${index}');removeErrorBorder('packageDesc${index}');"/>
        <input type="hidden" id="packageAbbr${index}"/>
        <input type="hidden" id="packageId${index}" class="pId" value="${cargo.packageId}"/>
        <html:hidden styleId="packageId" property="packageId" value="${cargo.packageId}"/>
        <input type="hidden" name="ecuPackageDesc" id="ecuPackageDesc"  value="${cargo.packageDesc}"/>
    </display:column>
    <display:column title="PCs">
        <html:text property="packageAmount" styleId="packageAmount" styleClass="textbox border-olive width-30px"
                   value="${cargo.packageAmount}"/>
    </display:column>
    <display:column title="Marks & nos">
        <html:textarea property="marksNo" styleId="marksNo" styleClass="textarea border-olive" value="${cargo.marksNo}" rows="5" cols="50"/>
    </display:column>
    <display:column title="Desc">
        <html:textarea property="goodDesc" styleId="goodDesc" styleClass="textarea border-olive" value="${cargo.goodDesc}" rows="5" cols="50"/>
    </display:column>
    <display:column title="Discharge">
        <html:text property="dischargeInstruction" styleId="dischargeInstruction" styleClass="textbox border-olive" value="${cargo.dischargeInstruction}"/>
    </display:column>
    <display:column title="Wt">
        <html:text property="weightValues" styleId="weightValues" styleClass="textbox width-40px border-olive" value="${cargo.weightValues}"/>
    </display:column>
    <display:column title="Volume">
        <html:text property="volumeValues" styleId="volumeValues" styleClass="textbox width-40px border-olive" value="${cargo.volumeValues}"/>
    </display:column>
    <display:column title="Commercial">
        <html:text property="commercialValue" styleId="commercialValue" styleClass="textbox width-40px border-olive" value="${cargo.commercialValue}"/>
    </display:column>
    <display:column title="Currency">
        <html:text property="currency" styleId="currency" styleClass="textbox width-40px border-olive" value="${cargo.currency}"/>
    </display:column>
</display:table>
