<%-- 
    Document   : AddValidGLAccountTemplate
    Created on : Dec 18, 2009, 5:35:06 PM
    Author     : lakshminarayanan
--%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<%@include file="../includes/baseResources.jsp"%>
<%@include file="../includes/resources.jsp"%>
<html:html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
		<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}">
        <title>AddValidGLAccountTemplate</title>
        <script type="text/javascript" src="<c:url value="/dwr/engine.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/dwr/util.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/dwr/interface/AccrualsBC.js"/>"></script>
    	<script type="text/javascript" src="<c:url value="/js/common.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/prototype/prototype.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/js/script.aculo.us/effects.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/js/script.aculo.us/controls.js"/>"></script>
        <script type="text/javascript">
			function addMoreParams(element, entry) {
				return entry + "&tabName=SUBLEDGER";
			}
        </script>
    </head>
    <body>
        <html:form action="/accruals" name="accrualsForm"
			type="com.gp.cvst.logisoft.struts.form.AccrualsForm" scope="request">
            <div id="message" style="font:200;color:blue">
                <c:if test="${!empty updated && updated=='Y'}">
                    <c:out value="GL Accounts updated Successfully"/>
                    <script type="text/javascript">
                        parent.parent.GB_hide();
                    </script>
                </c:if>
                <c:if test="${!empty updated && updated=='N'}">
                    <script type="text/javascript">
                        alert("There is no mapping for the charge code and data combination entered, please change values and retry");
                    </script>
                </c:if>
            </div>
            <c:if test="${!empty invalidGLAccountAccruals && fn:length(invalidGLAccountAccruals)>0}">
                <div id="divtablesty1" class="scrolldisplaytable" style="width:100%; overflow: auto;">
                    <input type="button" value="Update" onclick="updateGLAccount()" class="buttonStyleNew">
                    <c:set var="i" value="0"></c:set>
                    <display:table name="${invalidGLAccountAccruals}" class="displaytagstyle"
                        pagesize="100" id="transactionBean" style="width:100%;">
                        <display:setProperty name="paging.banner.placement" value="none"/>
                        <display:column property="customer" title="Vendor<br>Name"/>
                        <display:column property="customerNo" title="Vendor<br>Number"/>
                        <display:column title="Transaction<br>Type">
                               <c:out value="AC"/>
                        </display:column>
                        <display:column title="Charge<br>Code">
                            <input type="text" id="chargeCode${i}" name="chargeCode${i}" value="${transactionBean.chargeCode}" style="width: 100px">
                            <input type="hidden" name="chargeCodeValid${i}" id="chargeCodeValid${i}" value="${transactionBean.chargeCode}">
                            <div  style="display: none;" class="newAutoComplete" id="chargeCodeDiv${i}"></div>
                            <script>
                                initAjaxAutoCompleter("chargeCode${i}", "chargeCodeDiv${i}", "chargeCodeValid${i}", "${path}/servlet/AutoCompleterServlet?action=ChargeCode&textFieldId=chargeCode${i}&codeType=36","");
                            </script>
                        </display:column>
                        <display:column title="Shipment<br>Type">
                            <select name="shipmentType" id="shipmentType" style="width: 90px">
                                <c:if test="${!empty shipmentTypeList}">
                                    <c:forEach var="shipmentType" items="${shipmentTypeList}">
                                        <option value="${shipmentType.value}"><c:out value="${shipmentType.label}"/></option>
                                    </c:forEach>
                                </c:if>
                            </select>
                        </display:column>
                        <display:column title="SuffixValue <br>(Department)">
                            <input type="text" name="suffix" id="suffix${i}" style="width: 50px">
                        </display:column>
                        <display:column style="display:none">
                            <input type="hidden" value="${transactionBean.transactionId}" id="transactionId${i}" name="transactionId">
                        </display:column>
                        <c:set var="i" value="${i+1}"></c:set>
                    </display:table>
                </div>
            </c:if>
            <html:hidden property="buttonValue"/>
            <html:hidden property="accrualIds"/>
            <html:hidden property="shipmentTypes"/>
            <html:hidden property="chargeCodes"/>
            <html:hidden property="suffixValues"/>
        </html:form>
    </body>
    <script type="text/javascript">
        /**
        * Comment
        */
        function updateGLAccount() {
            var transactionId = document.getElementsByName("transactionId");
            var shipmentType = document.getElementsByName("shipmentType");
            var suffix = document.getElementsByName("suffix");
            var accrualIds = "";
            var shipmentTypes = "";
            var chargeCodes = "";
            var suffixValues = "";
            for(var i=0;i<transactionId.length;i++){
                accrualIds=accrualIds+","+transactionId[i].value;
                shipmentTypes=shipmentTypes+","+shipmentType[i].value;
                chargeCodes=chargeCodes+","+document.getElementById("chargeCode"+i).value;
                suffixValues=suffixValues+","+suffix[i].value;
            }
            if(null!=accrualIds && accrualIds!=""){
                document.accrualsForm.buttonValue.value="updateGLAccounts";
                document.accrualsForm.accrualIds.value=accrualIds;
                document.accrualsForm.shipmentTypes.value=shipmentTypes;
                document.accrualsForm.chargeCodes.value=chargeCodes;
                document.accrualsForm.suffixValues.value=suffixValues;
                document.accrualsForm.submit();
            }
        }
    </script>
</html:html>
