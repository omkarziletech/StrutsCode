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
        <title>Show Online Users</title>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/admin/unLocation.js"></script>
    </head>
    <body class="whitebackgrnd" >
        <html:form action="/unlocation.do" name="searchUnLocationForm" styleId="searchUnLocationForm"
                   type="com.logiware.form.SearchUnLocationForm" scope="request">
            <table cellpadding="3" cellspacing="0" class="tableBorderNew" width="100%">
                <tr class="tableHeadingNew">
                <td colspan="8">Search</td>
            </tr>

            <tr>
            <td class="textlabelsBold">
					UnLocationCode
            </td>
            <td>
                <html:text property="unLocationCode" value="${searchUnLocationForm.unLocationCode}"
                           styleId="unLocationCode" size="20"  styleClass="textlabelsBoldForTextBox" maxlength="50"/>
            </td>
            <td class="textlabelsBold">
					UNLocation Name
            </td>
            <td>
                <html:text property="unLocationName" value="${searchUnLocationForm.unLocationName}"
                           styleId="unLocationName" size="20"  styleClass="textlabelsBoldForTextBox" maxlength="50"/>
            </td>
            <td class="textlabelsBold">
					Country Name
            </td>
            <td>
                <html:text property="countryName"
                           value="${searchUnLocationForm.countryName}" styleId="countryName" size="20"  styleClass="textlabelsBoldForTextBox" maxlength="50"/>
            </td>
            <td class="textlabelsBold">
					State Name
            </td>
            <td>
                <html:text property="stateName"
                           value="${searchUnLocationForm.stateName}" styleId="stateName" size="20"  styleClass="textlabelsBoldForTextBox" maxlength="50"/>
            </td>
        </tr>
        <tr>
        <td align="center" colspan="8" style="padding-top: 10px">
            <input type="button" class="buttonStyleNew"
                   style="width: 100px;" value='Search' onclick="searchUnLocations()"/>
            <input type="button" class="buttonStyleNew"
                   style="width: 100px;" value='Add New' id="addnew" onclick="addUnLocations()"/>
            <input type="button" class="buttonStyleNew"
                   style="width: 100px;" value='Restart' id="restart" onclick="restartUnLocations()"/>
        </td>
    </tr>
    <tr>
    <td colspan="8">
        <div>
            <c:if test="${!empty UNLocationList}">
                <display:table name="${UNLocationList}" pagesize="100" id="UNLocation"
                               class="displaytagStyleNew" style="width:100%" requestURI="/unlocation.do?">
                    <display:setProperty name="paging.banner.some_items_found">
                        <span class="pagebanner"><font color="blue">{0}</font>
                            Un Location displayed,For more Records click on page numbers.</span>
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
                    <display:setProperty name="paging.banner.item_name" value="Un Location"/>
                    <display:setProperty name="paging.banner.items_name" value="Un Locations"/>
                    <display:column title="Code" property="unLocationCode" headerClass="sortable" sortable="true" />
                    <display:column title="Name" property="unLocationName" headerClass="sortable" sortable="true"/>
                    <display:column title="Country" property="countryId.codedesc" headerClass="sortable" sortable="true"/>
                    <display:column title="State" property="stateId.codedesc" headerClass="sortable" sortable="true"/>
                    <display:column title="Synonymous City" property="addSynonymousCity" style="text-align:center" headerClass="sortable" sortable="true"/>
                    <display:column title="Synonymous Group" property="addSynonymousGroup" style="text-align:center" headerClass="sortable" sortable="true"/>
                    <display:column title="CY Yard" property="cyYard" style="text-align:center" headerClass="sortable" sortable="true"/>
                    <display:column title="Alternate Port Name" property="alternatePortName" headerClass="sortable" 
                                    sortable="true" style="text-transform: uppercase"/>
                    <display:column title="Action">
                        <span onmouseover="tooltip.show('<storng>Edit</strong>',null,event)" onmouseout="tooltip.hide()">
                            <img src="${path}/img/icons/edit.gif" alt="Edit" onclick="editUnLocation('${UNLocation.unLocationCode}','${fn:replace(UNLocation.unLocationName,'\'','\\&#39;')}','${UNLocation.countryId.codedesc}','${UNLocation.id}','${UNLocation.stateId.codedesc}','${UNLocation.stateId.id}','${UNLocation.countryId.id}','${UNLocation.stateId.codedesc}','${UNLocation.addSynonymousCity}','${UNLocation.addSynonymousGroup}','${UNLocation.cyYard}','${fn:replace(UNLocation.alternatePortName,'\'','\\&#39;')}')"/>
                        </span>
                    </display:column>  
                </display:table>
            </c:if>
        </div>
    </td>
</tr>
</table>
<html:hidden property="buttonValue"/>
<html:hidden property="userId"/>
<html:hidden property="codeDesc"/>            
<html:hidden property="stateCodeDesc"/>
<html:hidden property="stateId"/>
<html:hidden property="countryId"/>
<html:hidden property="addSynonymousGroup"/>
<html:hidden property="addSynonymousCity"/>
<html:hidden property="cyYard"/>
<html:hidden property="alternatePortName"/>
</html:form>
</body>
<script type="text/javascript">        
    function addUnLocations(){
        document.searchUnLocationForm.buttonValue.value="addUnLocations";
        document.searchUnLocationForm.submit();
    }
    function restartUnLocations(){
        document.searchUnLocationForm.buttonValue.value="restartUnLocations";
        document.searchUnLocationForm.submit();
    }
    function editUnLocation(code,name,desc,id,state,stateId,countryId,codedesc,city,group,cyYard,alternatePortName){
        document.searchUnLocationForm.buttonValue.value="editUnLocations";
        document.searchUnLocationForm.userId.value=id;
        document.searchUnLocationForm.codeDesc.value=desc;
        document.searchUnLocationForm.unLocationCode.value=code;
        document.searchUnLocationForm.unLocationName.value=name;
        document.searchUnLocationForm.stateCodeDesc.value=state;
        document.searchUnLocationForm.stateId.value=stateId;
        document.searchUnLocationForm.countryId.value=countryId;
        document.searchUnLocationForm.addSynonymousCity.value=city;
        document.searchUnLocationForm.addSynonymousGroup.value=group;
        document.searchUnLocationForm.cyYard.value=cyYard;
        document.searchUnLocationForm.alternatePortName.value=alternatePortName!=""?alternatePortName.toString().toUpperCase():"";
        document.searchUnLocationForm.submit();
    }
</script>
</html>


