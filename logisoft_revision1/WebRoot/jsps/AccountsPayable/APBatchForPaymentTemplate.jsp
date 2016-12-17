<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
           prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
           prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@include file="../includes/jspVariables.jsp"%>
<un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:set var="i" value="0"></c:set>
<table width="100%"
       style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;" height="15px">
    <tbody>
        <tr>
            <td class="lightBoxHeader">
				Batch Details for Payment
            </td>
            <td>
                <div>
                    <a id="lightBoxClose"  href="javascript:closeBatch('${newApBatchId}');">
                        <img src="${path}/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                    </a>
                </div>
            </td>
        </tr>
    </tbody>
</table>
<br/>
<table class="textlabelsBold" align="center">
    <tr>
        <td>Batch No</td>
        <td><span style="font-size: 14;font-weight: bold;" id="newBatchId"><c:out value="${newApBatchId}"></c:out> </span></td>
    </tr>
    <tr>
        <td>Batch Description</td>
        <td><textarea rows="2" cols="30" id="Description" style="text-transform: uppercase;"></textarea></td>
    </tr>
    <tr>
        <td align="center" colspan="2">
            <input type="button" name='saveButton' id="saveButton" class="buttonStyleNew" value='Save'
                   style='width: 40px' onclick="saveBatchAndMakePayments('${newApBatchId}');"/>
            <input type="button" name='Cancel' class="buttonStyleNew" value='Cancel'
                   style='width: 50px' onclick="closeBatch('${newApBatchId}');" />
        </td>
    </tr>
</table>