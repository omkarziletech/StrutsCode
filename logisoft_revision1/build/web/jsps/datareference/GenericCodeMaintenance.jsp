<jsp:directive.page import="org.apache.commons.lang3.StringUtils"/><%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.User,com.gp.cong.logisoft.beans.CodeBean,com.gp.cong.logisoft.bo.CGenericCodeBO,com.gp.cong.logisoft.domain.GenericCode"%>
<%@ page import="com.gp.cong.logisoft.beans.*"%>
<%@ page import="com.gp.cong.logisoft.domain.*"%>
<%@ page import="org.apache.struts.util.LabelValueBean"%>
<%@ page import="com.gp.cong.logisoft.util.*"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@taglib uri="http://cong.logiwareinc.com/string" prefix="str"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%
    List generigCodeFields = (List)session.getAttribute("genericCodeFields");
%>
<html>
    <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
<head>
<title>GenericCodeMaintenance</title>
<%@include file="../includes/baseResources.jsp" %>
<script language="javascript" src="${path}/js/isValidEmail.js" ></script>
<link rel="stylesheet" type="text/css" media="all" href="${path}/css/cal(2)/skins/aqua/theme.css" title="Aqua" />
<link rel="alternate stylesheet" type="text/css" media="all" href="${path}/css/cal(2)/calendar-win2k-cold-2.css" title="win2k-cold-2" />
<script language="javascript" src="${path}/js/dojo/dojo.js"></script>
<script type="text/javascript">
dojo.hostenv.setModulePrefix('utils', 'utils');
dojo.widget.manager.registerWidgetPackage('utils');
dojo.require("utils.AutoComplete");
dojo.require("dojo.io.*");
dojo.require("dojo.event.*");
dojo.require("dojo.html.*");
</script>
<script language="javascript" type="text/javascript">

function showall1(){
    document.GenericCodeMaintenanceForm.buttonValue.value="showall";
    document.GenericCodeMaintenanceForm.submit();
}
function editDetails(id){
    document.GenericCodeMaintenanceForm.codeTypeID.value=id;
    document.GenericCodeMaintenanceForm.buttonValue.value="edit";
    document.GenericCodeMaintenanceForm.submit();
}
function search1(){
    if(document.GenericCodeMaintenanceForm.codeTypeId.value=="0"){
        alert("Please Select Code Type");
        return;
    }
    document.GenericCodeMaintenanceForm.buttonValue.value="search";
    document.GenericCodeMaintenanceForm.submit();
}
function displaytagcolor(){
    var datatableobj = document.getElementById('codetable');
    for(i=0; i<datatableobj.rows.length; i++){
        var tablerowobj = datatableobj.rows[i];
        if(i%2==0){
            tablerowobj.bgColor='#FFFFFF';
        }else{
            tablerowobj.bgColor='#E6F2FF';
        }
    }
}
function initRowHighlighting(){
    if (!document.getElementById('codetable')){ return; }
    var tables = document.getElementById('codetable');
    attachRowMouseEvents(tables.rows);
}
function attachRowMouseEvents(rows){
    for(var i =1; i < rows.length; i++){
        var row = rows[i];
        row.onmouseover =function(){
            this.className = 'rowin';
        }
        row.onmouseout =function(){
            this.className = '';
        }
        row.onclick= function(){
        }
    }
}
function setWarehouseStyle(){
    if(document.GenericCodeMaintenanceForm.buttonValue.value=="showall"){
        var x=document.getElementById('codetable').rows[0].cells;
        x[1].className="sortable sorted order1";
    }
    if(document.GenericCodeMaintenanceForm.buttonValue.value=="search"){
        var x=document.getElementById('codetable').rows[0].cells;
        x[0].className="sortable sorted order1";
    }
}
function disabled(val){
    if(val == 0){
        var imgs = document.getElementsByTagName('img');
        for(var k=0; k<imgs.length; k++){
            if(imgs[k].id != "showall" && imgs[k].id!="search"){
                imgs[k].style.visibility = 'hidden';
            }
        }
    }
    var datatableobj = document.getElementById('codetable');
    if(datatableobj!=null){
        setWarehouseStyle();
    }
}
var newwindow = '';
function addnew1() {
    if (!newwindow.closed && newwindow.location){
        newwindow.location.href = "${path}/jsps/datareference/GenericCode.jsp";
    }
    else{
        newwindow=window.open("${path}/jsps/datareference/GenericCode.jsp","","width=500,height=100");
        if (!newwindow.opener) newwindow.opener = self;
    }
    if (window.focus) {newwindow.focus()}
    return false;
}
function popup1(mylink, windowname){
    if (!window.focus)return true;
    var href;
    if (typeof(mylink) == 'string')
        href=mylink;
    else
        href=mylink.href;
    mywindow=window.open(href, windowname, 'width=600,height=400,scrollbars=yes');
    mywindow.moveTo(200,180);
    return false;
}
function getGenericPage(codeType){
    window.location.href="${path}/jsps/datareference/CodeDetails.jsp?codeType="+codeType;
}
function getComCodeDesc(ev){
    if(event.keyCode==9 || event.keyCode==13){
        document.getElementById("codeDesc").value="";
        var params = new Array();
        params['requestFor'] = "CommodityCodeDescriptionForCode";
        params['commodityCodeForCode'] = document.GenericCodeMaintenanceForm.codeValue.value;
        var bindArgs = {
            url: "${path}/actions/getChargeCodeDesc1.jsp",
            error: function(type, data, evt){alert("error");},
            mimetype: "text/json",
            content: params
        };
        var req = dojo.io.bind(bindArgs);
        dojo.event.connect(req, "load", this, "populateComCodeDescDojo");
    }
}
function populateComCodeDescDojo(type, data, evt) {
    if(data){
        document.getElementById("codeDesc").value=data.commodityCodeDescForCode;
    }
}
function addCodeType(){
    var codeType = document.getElementById('codeType').value;
    GB_show('Code', '${path}/jsps/datareference/GenericCode.jsp?codeType='+codeType,150,500);
}
function deleteCode(id,codeTypeDescription){
    if(confirm('Are You Sure. Do You Want To Delete The '+codeTypeDescription)){
         document.GenericCodeMaintenanceForm.buttonValue.value="delete";
         document.GenericCodeMaintenanceForm.genericCodeId.value=id;
         document.GenericCodeMaintenanceForm.submit();
    }
}
</script>
<%@include file="../includes/resources.jsp" %>
    </head>
    <body  class="whitebackgrnd">
        <html:form action="/genericcodemaintenance" name="GenericCodeMaintenanceForm" type="com.gp.cong.logisoft.struts.form.GenericCodeMaintenanceForm" scope="request">
            <html:errors/>
            <font color="blue" size="4">${message}</font>
            <input type="hidden" name="buttonValue" id="buttonValue" value="${buttonValue}"/>
            <input type="hidden" name="genericCodeId" id="genericCodeId"/>
            <table width="100%"  border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                            <tr class="tableHeadingNew"><td> Search Criteria</td>
                                <td align="right"><input type="button" class="buttonStyleNew"  value="Add New" id="addnew"
                                                          onclick="addCodeType()"/></td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <table width="100%"  border="0" cellpadding="3" cellspacing="3">
                                        <tr class="style2">
                                            <td class="textlabels">Code Type </td>
                                            <td><html:select property="codeTypeId" value="${requestScope.code}" 
                                                   styleId="codeType"  styleClass="textlabelsBoldForTextBox" >
                                                    <html:optionsCollection name="codeTypeList"/>
                                                </html:select></td>
                                            <td class="textlabels">Code Description</td>
                                            <td><input name="codeDesc" type="text" class="textlabelsBoldForTextBox uppercase"  value="" style="width:215px"/></td>
                                            <td class="textlabels">Code</td>
                                            <td><input name="codeValue" id="codeValue" maxlength="10" class="textlabelsBoldForTextBox uppercase"  value=""
                                                  onkeydown="getComCodeDesc(this.value)" size="20"/>
                                                <dojo:autoComplete formId="GenericCodeMaintenanceForm" textboxId="codeValue"
                                                 action="${path}/actions/getChargeCode.jsp?tabName=GENERIC_CODE_MAINTENANCE"/></td>
                                    </tr>
                                    <tr>
                                        <td colspan="8" align="center">
                                            <input type="button" class="buttonStyleNew" value="Search" id="search" onclick="search1()"/>
                                    </tr >
                        </table>
                    </td>
                </tr>
            </table>
            <br>
        </td>
    </tr>
<tr>
    <td width="100%">
          <table width="100%" class="tableBorderNew" cellpadding="0" cellspacing="0">
            <tr class="tableHeadingNew"><td><c:out value="${codeTypeDescription}"/></td></tr>
            <tr><td>
                   <c:set var="rowIndex" value="0"/>
                    <div id="divtablesty1">
                         <c:choose>
                            <c:when test="${!empty codeData}">
                        <display:table name="${codeData}" class="displaytagstyle" pagesize="15" style="width:100%" sort="list" id="codetable">
                            <display:setProperty name="paging.banner.some_items_found">
                                <span class="pagebanner">
                                    <font color="blue">{0}</font> Code details displayed,For more code click on page numbers.
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
                            <display:setProperty name="paging.banner.item_name" value="GenericCode"/>
                            <display:setProperty name="paging.banner.items_name" value="GenericCodes"/>
                            <c:choose>
                                <c:when test="${codeTypeId == '0'}">
                                    <display:column property="code" title="Code" sortable="true" paramId="paramId" paramProperty="id" href="${path}/genericcodemaintenance.do"/>
                                    <display:column property="codedesc" title="Code Type" sortable="true"></display:column>
                                    <display:column property="desc" title="Description"   sortable="true"></display:column>
                                </c:when>
                                <c:otherwise>
                                 <c:forEach var="headerLabel" items="${codeHeader}" varStatus="status">
                                    <display:column title="${headerLabel.label}" sortable="true">
                                        <c:choose>
                                            <c:when test="${headerLabel.label == 'Code'}">
                                                <a href="${path}/genericcodemaintenance.do?paramId=${codetable.id}">${codetable.code}</a>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="property" value="${genericCodeFields[status.index]}"/>
                                                ${str:splitter(codetable[property],90,'<br>')}
                                            </c:otherwise>
                                        </c:choose>
                                    </display:column>
                                 </c:forEach>
                                </c:otherwise>
                             </c:choose>
                            <display:column title="Actions">
                                    <img src="${path}/img/icons/pubserv.gif" onclick="window.location.href='${path}/genericcodemaintenance.do?codeId=${codetable.id}'" 
                                         onmouseover="tooltip.show('<strong>MoreInfo</strong>',null,event);" onmouseout="tooltip.hide();"/>
                                    <c:if test="${codeTypeDescription == 'Print Comments' || codeTypeDescription == 'Pre-Defined Remarks FCLE'
                                                  || codeTypeDescription == 'Pre-Defined Remarks FCLI' || codeTypeDescription == 'AES Commodity Code'
                                                  || codeTypeDescription == 'Pre-Defined Remarks LCLE'}">
                                        <img src="${path}/img/icons/delete.gif" onclick="deleteCode('${codetable.id}','${codeTypeDescription}')"
                                             onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();"/>
                                    </c:if>
                            </display:column>
                        </display:table>
                         <c:set var="rowIndex" value="${rowIndex+1}"/>
                          </c:when>
                            <c:otherwise>
                                <div>No Records Found</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </td>
            </tr>
        </table>
    </td>
</tr>
</table>
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
