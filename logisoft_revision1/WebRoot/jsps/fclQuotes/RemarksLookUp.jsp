<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.domain.GenericCode"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@include file="../includes/jspVariables.jsp"%>
<html>
    <head>
        <title>JSP for RemarksLookUpForm form</title>
    </head>
    <c:set var="path" value="${pageContext.request.contextPath}"/>
    <%@include file="../includes/baseResources.jsp"%>
    <script language="javascript" src="${path}/js/common.js"></script>
    <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
    <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
    <script src="${path}/js/jquery/jquery-ui-1.8.10.custom.min.js" type="text/javascript"></script>
    <script language="javascript" src="${path}/js/fcl/fclBl.js"></script>
    <script language="javascript" src="${path}/js/fcl/fclBillLanding.js"></script>
    <script language="javascript" src="${path}/js/fcl/quote.js"></script>
    <script language="javascript" src="${path}/js/fcl/editBooking.js.js"></script>
    <script type="text/javascript" >
        function getSearch() {
            var val = document.remarksLookUpForm.remarks.value;
            document.remarksLookUpForm.remarkscode.value = val;
            document.remarksLookUpForm.buttonValue.value = "Search";
            document.remarksLookUpForm.submit();
        }
        function getGo() {
            document.getElementById('submitRemarks').style.display = 'none';
            document.remarksLookUpForm.buttonValue.value = "Go";
            document.remarksLookUpForm.submit();
        }
        function add() {
            document.remarksLookUpForm.buttonValue.value = "addRemarks";
            document.remarksLookUpForm.submit();
        }
        function saveRemarks() {
            document.remarksLookUpForm.buttonValue.value = "saveRemarks";
            document.remarksLookUpForm.submit();
        }
        function save() {
            if (document.remarksLookUpForm.addRemarks.value == '') {
                alert('Please Enter Remarks');
                return false;
            }
            var codedesc = document.remarksLookUpForm.addRemarks.value;
            window.location.href = '/logisoft/remarksLookUp.do?buttonValue=Quotation&Action=save&addRemarks=' + codedesc;
        }
        function openPopup() {
            showPopUp();
            document.getElementById('addPopup').style.display = 'block';
        }
        function closePopup() {
            closePopUp();
            document.getElementById('addPopup').style.display = 'none';
        }
        function checkUnCheckRemrks(id, obj) {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.dwr.DwrUtil",
                    methodName: "checkUnCheckRemrks",
                    param1: id,
                    param2: obj.checked ? "check" : "unCheck",
                    request: true
                }
            });
        }
   

    </script>
    <style type="text/css">
        #addPopup{
            position:fixed;
            _position:absolute;
            border-style: solid solid solid solid;
            background-color: white;
            z-index:99;
            left:20%;
            top:50%;
            bottom:5%;
            right:5%;
            _height:expression(document.body.offset+"px");
        }

    </style>
    <div id="cover" style="width: 906px ;height: 1000px;"></div>
    <body class="whitebackgrnd">
        <html:form action="/remarksLookUp" scope="request">
           
            <html:hidden styleId="importFlag" property="importFlag"/>
            <div id="addPopup" style="display:none">
                <table width="50"><tr><td>Add Remarks</td><td><textarea onkeypress="return checkTextAreaLimit(this, 250)"  style="text-transform: uppercase"  cols="40" rows="4" name="addRemarks" class="textlabelsBoldForTextBox" ></textarea></td></tr>
                    <tr><td align="center" colspan="2"><input type="button" class="buttonStyleNew" value="Close" onclick="closePopup()">

                            <input type="button" class="buttonStyleNew" value="save" onclick="save()"></td>
                    </tr></table>

            </div>

            <c:choose>
                <c:when test="${empty addNewRemarks}">
                    <table width="100%" border="0" cellpadding="1" cellspacing="0" class="tableBorderNew">
                        <tr class="tableHeadingNew">
                            <td>Search</td>
                            <td align="right">
                                <c:if test="${remarksLookUpForm.importFlag eq true && roleDuty.addPredefinedRemarks}">
                                    <input type="button" class="buttonStyleNew" value="Add" onclick="openPopup()">
                                </c:if>
                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td colspan="3" >Remarks
                                <html:text property="remarks"  value="" size="40" maxlength="250" styleClass="textlabelsBoldForTextBox" ></html:text>
                                    <input type="button" class="buttonStyleNew" value="Search" onclick="getSearch()">

                                </td>
                            </tr>
                            <tr style="padding-top:10px;">
                                <td colspan="2">
                                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                        <tr class="tableHeadingNew">
                                            <td >
                                                <input type="button" class="buttonStyleNew" id="submitRemarks" value="Submit" onclick="getGo()">
                                                List of Remarks
                                            </td>
                                        </tr>
                                        <tr><td>
                                                <div id="divtablesty1" style="height:80%;">
                                                <c:set var="index" value="0"/>
                                                <display:table name="${remarksList}"  class="displaytagstyle" requestURI="/remarksLookUp.do"
                                                               id="remarks" sort="list" defaultsort="2" defaultorder="ascending" style="width:100%" pagesize="20">

                                                    <display:setProperty name="paging.banner.some_items_found">
                                                        <span class="pagebanner">
                                                            <font color="blue">{0}</font> Remarks displayed,For more Records click on page numbers.
                                                        </span>
                                                    </display:setProperty>
                                                    <display:setProperty name="paging.banner.one_item_found">
                                                        <span class="pagebanner">
                                                            One {0} displayed. Page Number
                                                        </span>
                                                    </display:setProperty>
                                                    <display:setProperty name="paging.banner.all_items_found">
                                                        <span class="pagebanner">
                                                            {0} {1} Displayed, Page Number

                                                        </span>
                                                    </display:setProperty>
                                                    <display:setProperty name="basic.msg.empty_list"><span class="pagebanner">
                                                            No Records Found.
                                                        </span>
                                                    </display:setProperty>
                                                    <display:setProperty name="paging.banner.placement" value="bottom" />
                                                    <display:setProperty name="paging.banner.item_name" value="Remarks"/>
                                                    <display:setProperty name="paging.banner.items_name" value="Remarks"/>
                                                    <display:column title="" style="width:20px;">
                                                        <c:choose>
                                                            <c:when test="${fn:contains(checkSet,remarks.code)}">
                                                                <input type="checkbox" name="rcheck" id="rcheck" value="${index}" onclick="checkUnCheckRemrks('${remarks.code}', this)" checked/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <input type="checkbox" name="rcheck" id="rcheck" value="${index}" onclick="checkUnCheckRemrks('${remarks.code}', this)"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </display:column>
                                                    <display:column title="Remarks" maxLength="100">${remarks.codedesc}</display:column>
                                                    <display:column title="" paramName="id" property="id" style="width:8px;visibility:hidden"/>
                                                    <c:set var="index" value="${index+1}"/>
                                                </display:table>
                                            </div>
                                        </td></tr>
                                </table>
                            </td></tr>
                    </table>
                </c:when>
                <c:otherwise>
                    <br>
                    <table width="100%" border="0" cellpadding="4" cellspacing="0" class="tableBorderNew">
                        <tr class="tableHeadingNew">Add Remarks</tr>
                        <tr class="textlabelsBold">
                            <td>Remarks
                                <html:text property="remarks" value="" size="40"></html:text>
                                    <input type="button" class="buttonStyleNew" value="Save" onclick="saveRemarks()">
                                </td>
                            </tr>
                        </table>
                </c:otherwise>
            </c:choose>

            <html:hidden property="buttonValue"/>
            <html:hidden property="remarkscode"/>
            <html:hidden property="buttonParameter" value="${buttonValue}"/>
            <input type="hidden" id="preRemarks" value="${fn:escapeXml(checkedRemarksList[0])}"/>
        </html:form>
        <c:choose>
            <c:when test="${buttonValue == 'QuotationForRemarks'}">
                <script type="text/javascript">
                 parent.parent.getAllRemarksFromPopUp(document.getElementById("preRemarks").value);
                 parent.parent.GB_hide();
                 </script>
            </c:when>
            <c:when test="${buttonValue == 'aesDetailsForRemarks'}">
                <script type="text/javascript">
                    window.parent.opener.setException(document.getElementById("preRemarks").value);
                    self.close();
                </script>
            </c:when>
            <c:when test="${buttonValue == 'BlImportsForRemarks'}">
                <script type="text/javascript">
                    parent.parent.getAllRemarksforImportFromPopUp(document.getElementById("preRemarks").value);
                    parent.parent.GB_hide();
                </script>
            </c:when>
            <c:when test="${buttonValue == 'HouseDescForRemarks'}">
                <script type="text/javascript">
                    parent.parent.getAllRemarksforHouseFromPopUp(document.getElementById("preRemarks").value);
                    parent.parent.GB_hide();
                </script>
            </c:when>
            <c:when test="${buttonValue == 'MasterDescForRemarks'}">
                <script type="text/javascript">
                    parent.parent.getAllRemarksforMasterFromPopUp(document.getElementById("preRemarks").value);
                    parent.parent.GB_hide();
                </script>
            </c:when>
            <c:otherwise>

            </c:otherwise>
        </c:choose>
    </body>
</html>

