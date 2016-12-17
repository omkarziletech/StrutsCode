<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<%@ include file="/jsps/includes/jspVariables.jsp"%>
<%@include file="/jsps/includes/baseResources.jsp"%>
<%@include file="/jsps/includes/resources.jsp"%>
<html>
    <head>
        <meta http-equiv="Content-Type"
              content="text/html; charset=iso-8859-1"/>
        <title>Search Zip Code</title>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/admin/zipCode.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/autocomplete.js"></script>
    </head>
    <body class="whitebackgrnd" onload="clearFormValues()">
        <!--DESIGN FOR NEW Confirm BOX ---->
        <div id="ConfirmBox" class="alert">
            <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Confirmation</b></p>
            <p id="innerText1" class="containerForAlert" style="width: 100%;padding-left: 3px;"></p>
            <form style="text-align:right;padding-right:4px;padding-bottom:4px;padding-top:10px;">
                <input type="button"  class="buttonStyleForAlert" value="OK" onclick="yes()">
                <input type="button"  class="buttonStyleForAlert" value="Cancel" onclick="No()">
            </form>
        </div>
        <html:form action="/zipCode.do" name="zipCodeForm" styleId="zipCodeForm"
                   type="com.logiware.form.ZipCodeForm" scope="request">
             <html:hidden property="buttonValue"/>
            <html:hidden property="id"/>
            <table cellpadding="3" cellspacing="0" class="tableBorderNew" width="100%">
                <tr class="tableHeadingNew">
                    <td colspan="6">Search</td>
                </tr>
                <tr>
                    <td class="textlabelsBold">City</td>
                    <td>
                        <input name="city" id="city" size="20"  class="textlabelsBoldForTextBox" maxlength="50" value<="${zipCodeForm.city}"/>
                        <input type="hidden" id="city_check" value<="${zipCodeForm.city}"/>
			<div id="city_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
			 <script type="text/javascript">
                                initAutocompleteWithFormClear("city","city_choices","state","city_check",
                                "${path}/actions/getZipCode.jsp?tabName=ZIPCODE&from=1&isDojo=false","fillState()","");
			 </script>
                    </td>
                    <td class="textlabelsBold">State</td>
                     <td>
                         <html:text property="state" maxlength="2" style="text-transform: uppercase;background-color: #CCEBFF" readonly="true"
                                    value="${zipCodeForm.state}" styleId="state" size="20"  styleClass="textlabelsBoldForTextBox" />
                     </td>
                     <td class="textlabelsBold">Zip Code</td>
                    <td>
                        <html:text property="zip" value="${zipCodeForm.zip}"
                                   styleId="zip" size="20"  styleClass="textlabelsBoldForTextBox" maxlength="5"/>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="6" style="padding-top: 10px">
                        <input type="button" class="buttonStyleNew" id="buttonName"
                               style="width: 100px;" value='Search' onclick="zipCodeAction(this)"/>
                        <input type="button" class="buttonStyleNew"
                               style="width: 100px;" value='Add New' id="addnew" onclick="addZipCode()"/>
                        <input type="button" class="buttonStyleNew"
                               style="width: 100px;visibility: hidden" value='Cancel' id="cancel" onclick="cancelZipCode()"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="6">
                        <div  id="zipCodeDiv">
                            <c:if test="${!empty zipCodeList}">
                                <display:table name="${zipCodeList}" pagesize="50" id="zipCode"
                                               class="displaytagStyleNew" style="width:100%" requestURI="/zipCode.do?">
                                    <display:setProperty name="paging.banner.some_items_found">
                                        <span class="pagebanner"><font color="blue">{0}</font>
                                            Zip Code displayed,For more Records click on page numbers.</span>
                                        </display:setProperty>
                                        <display:setProperty name="paging.banner.one_item_found">
                                        <span class="pagebanner">One {0} displayed. Page Number</span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.all_items_found">
                                        <span class="pagebanner">{0} {1} displayed, Page Number</span>
                                    </display:setProperty>
                                    <display:setProperty name="basic.msg.empty_list">
                                        <span class="pagebanner">No Records Found.</span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.placement" value="bottom"/>
                                    <display:setProperty name="paging.banner.item_name" value="Zip Code"/>
                                    <display:setProperty name="paging.banner.items_name" value="Zip Code"/>
                                    <display:column title="Zip" property="zip" headerClass="sortable" sortable="true" />
                                    <display:column title="City" property="city" headerClass="sortable" sortable="true"/>
                                    <display:column title="State" property="state" headerClass="sortable" sortable="true"/>
                                    <display:column title="Action">
                                        <img src="${path}/img/icons/delete.gif" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();"
                                                 onclick="deleteZipCode('${zipCode.id}')" id="deleteimg" />
                                            <img src="${path}/img/icons/edit.gif" alt="Edit"  onmouseover="tooltip.show('<storng>Edit</strong>',null,event)" onmouseout="tooltip.hide()"
                                                 onclick="editZipcode('${zipCode.zip}','${zipCode.city}','${zipCode.state}','${zipCode.id}')"/>
                                    </display:column>
                                </display:table>
                            </c:if>
                        </div>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html>


