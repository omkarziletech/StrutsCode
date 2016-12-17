<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table width="100%"
       style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;">
    <tbody>
        <tr>
            <td class="lightBoxHeader">Trading Partner Range</td>
            <td>
                <a id="lightBoxClose"  href="javascript:closeAssignWindow();">
                    <img src="${path}/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                </a>
            </td>
        </tr>
    </tbody>
</table>
<div style="width:100%;height:70%;overflow: auto">
    <table width="100%" class="displaytagstyleNew">
        <thead>
            <tr>
                <th>Start Range</th>
                <th>End Range</th>
                <th>Action</th>
            </tr>
	</thead>
	<tbody>
            <c:set var="index" value="0"/>
	    <c:set var="showApplyToConsigneeOnlyAccounts" value="true"/>
            <c:if test="${!empty collectorTradingPartners}">
		<c:forEach var="collector" items="${collectorTradingPartners}">
		    <tr class="textlabelsBold">
			<c:choose>
			    <c:when test="${collector.applyToConsigneeOnlyAccounts}">
				<c:set var="showApplyToConsigneeOnlyAccounts" value="false"/>
				<td colspan="2" class="textlabelsBold">
				    Apply to Consignee Only Accounts
				    <input type="checkbox" name="applyToConsigneeOnlyAccounts" id="applyToConsigneeOnlyAccounts" class="textlabelsBoldForTextBox" checked/>
				</td>
				<td>
				    <input type="button" class="buttonStyleNew" value="Re Assign" onclick="applyToConsigneeOnlyAccounts('${collector.id}','${userId}')"/>
				</td>
			    </c:when>
			    <c:otherwise>
				<td>
				    <input type="text" name="startRange" id="startRange" value="${collector.startRange}" size="1" maxlength="1" style="text-transform:uppercase" class="textlabelsBoldForTextBox"/>
				</td>
				<td>
				    <input type="text" name="endRange" id="endRange" value="${collector.endRange}" size="1" maxlength="1" style="text-transform:uppercase" class="textlabelsBoldForTextBox"/>
				</td>
				<td>
				    <input type="button" class="buttonStyleNew" value="Re Assign" onclick="assignCollector('${collector.id}','${userId}','${index}')"/>
				</td>
			    </c:otherwise>
			</c:choose>
			<c:set var="index" value="${index+1}"/>
		    </tr>
		</c:forEach>
	    </c:if>
            <tr class="textlabelsBold">
                <td>
		    <input type="text" name="startRange" id="startRange" value="" size="1" maxlength="1" style="text-transform:uppercase" class="textlabelsBoldForTextBox"/>
		</td>
                <td>
		    <input type="text" name="endRange" id="endRange" value="" size="1" maxlength="1" style="text-transform:uppercase" class="textlabelsBoldForTextBox"/>
		</td>
                <td>
		    <input type="button" class="buttonStyleNew" value="Assign" onclick="assignCollector('','${userId}','${index}')"/>
		</td>
            </tr>
	    <c:if test="${showApplyToConsigneeOnlyAccounts}">
		<tr>
		    <td colspan="2" class="textlabelsBold">
			Apply to Consignee Only Accounts
			<input type="checkbox" name="applyToConsigneeOnlyAccounts" id="applyToConsigneeOnlyAccounts" class="textlabelsBoldForTextBox"/>
		    </td>
		    <td>
			<input type="button" class="buttonStyleNew" value="Assign" onclick="applyToConsigneeOnlyAccounts('${collector.id}','${userId}')"/>
		    </td>
		</tr>
	    </c:if>
        </tbody>
    </table>
</div>