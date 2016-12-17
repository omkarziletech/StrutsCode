<%@include file="/jsps/LCL/init.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<%@include file="/jsps/includes/resources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
    <body>
        <html:form action="/lclUnitType.do" styleId="lclUnitTypeForm" name="lclUnitTypeForm" type="com.gp.cong.logisoft.struts.form.lclDataReference.LclUnitTypeForm" scope="request">
            <input type="hidden" name="methodName" id="methodName" value="${lclUnitTypeForm.methodName}"/>
            <%--    <input type="hidden" name="description" id="description" value="${description}"/>--%>
            <br/>
            <table width="99.5%" align="center" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew"><td>Unit Type Search</td></tr>
                <tr><td>
                        <table width="50%">
                            <tr>
                                <td class="textlabelsBoldforlcl">Description</td>
                                <td>
                                    <cong:autocompletor id="description" name="description" template="one" query="UNIT_TYPE" container="NULL" value="${description}"
                                                        styleClass="textlabelsBoldForTextBox" shouldMatch="true" width="300" scrollHeight="200px" />
                                </td>
                                <td>
                                    <input type="button" class="button-style1"  value='Search' onclick="unitTypeSearch('${path}')"/>
                                    <input type="button" class="button-style1" value='Add New' id="addnew" onclick="addUnitType()"/>
                                </td>
                            </tr>
                        </table>
                <tr class="tableHeadingNew"><td>List of Unit Type</td></tr>
                <tr><td>
                        <display:table name="${unitTypeList}" id="unitType" class="dataTable"  pagesize="25" requestURI="/lclUnitType.do" style="width:100%">
                            <display:setProperty name="paging.banner.placement" value="bottom"/>
                            <display:column  title="Description" style="width:300Px;" sortable="true">
                                <a style="cursor: pointer;" onclick="editUnitType('${path}','${unitType.id}','${unitType.description}','${unitType.eliteType}','${unitType.shortDesc}','${unitType.interiorLengthImperial}','${unitType.interiorLengthMetric}','${unitType.interiorWidthImperial}','${unitType.interiorWidthMetric}','${unitType.interiorHeightImperial}','${unitType.interiorHeightMetric}','${unitType.doorHeightImperial}','${unitType.doorHeightMetric}','${unitType.doorWidthImperial}','${unitType.doorWidthMetric}','${unitType.grossWeightImperial}','${unitType.grossWeightMetric}','${unitType.tareWeightImperial}','${unitType.tareWeightMetric}','${unitType.volumeImperial}','${unitType.volumeMetric}','${unitType.targetVolumeImperial}','${unitType.targetVolumeMetric}','${unitType.enabledLclAir}','${unitType.remarks}','${unitType.enabledLclAir}','${unitType.enabledLclExports}','${unitType.enabledLclImports}')">
                                    <font color="blue">
                                        <span onmouseover="tooltip.showSmall('<strong>${unitType.description}</strong>')" onmouseout="tooltip.hide();">
                                            ${fn:substring(unitType.description,0,20)}
                                        </span>
                                    </font>
                                </a>
                            </display:column>
                            <display:column title="LCL Air" style="width:50Px;">
                                <c:choose>
                                    <c:when test="${unitType.enabledLclAir==true}">
                                        <c:set var="lclAir" value="Y"/>
                                        <div align="center">${lclAir}</div>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="lclAir" value="N"/>
                                        <div align="center">${lclAir}</div>
                                    </c:otherwise>
                                </c:choose>
                            </display:column>
                            <display:column title="LCL Exports" style="width:50Px;">
                                <c:choose>
                                    <c:when test="${unitType.enabledLclExports==true}">
                                        <c:set var="lclExports" value="Y"/>
                                        <div align="center">${lclExports}</div>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="lclExports" value="N"/>
                                        <div align="center">${lclExports}</div>
                                    </c:otherwise>
                                </c:choose>
                            </display:column>
                            <display:column title="LCL Imports" style="width:50Px;">
                                <c:choose>
                                    <c:when test="${unitType.enabledLclImports==true}">
                                        <c:set var="lclImports" value="Y"/>
                                        <div align="center">${lclImports}</div>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="lclImports" value="N"/>
                                        <div align="center">${lclImports}</div>
                                    </c:otherwise>
                                </c:choose>
                            </display:column>
                            <display:column title="Ref" style="width:50Px;">
                                <c:choose>
                                    <c:when test="${unitType.refrigerated==true}">
                                        <c:set var="refrigerated" value="Y"/>
                                        <div align="center">${refrigerated}</div>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="refrigerated" value="N"/>
                                        <div align="center">${refrigerated}</div>
                                    </c:otherwise>
                                </c:choose>
                            </display:column>
                            <display:column title="Elite Type" style="width:50px;" property="eliteType"/>
                            <display:column title="Short Desc" style="width:50px;" property="shortDesc"/>
                            <display:column title="Interior Length Imperial" style="width:50Px;" property="interiorLengthImperial"/>
                            <display:column title="Interior Length Metric" style="width:50Px;" property="interiorLengthMetric"/>
                            <display:column title="Interior Width Imperial" style="width:50Px;" property="interiorWidthImperial"/>
                            <display:column title="Interior Width Metric" style="width:50Px;" property="interiorWidthMetric"/>
                            <display:column title="Interior Height Imperial" style="width:50Px;" property="interiorHeightImperial"/>
                            <display:column title="Interior Height Metric" style="width:50Px;" property="interiorHeightMetric"/>
                            <display:column title="Door Height Imperial" style="width:50Px;" property="doorHeightImperial"/>
                            <display:column title="Door Height Metric" style="width:50Px;" property="doorHeightMetric"/>
                            <display:column title="Door Width Imperial" style="width:50Px;" property="doorWidthImperial"/>
                            <display:column title="Door Width Metric" style="width:50Px;" property="doorWidthMetric"/>
                            <display:column title="Gross Weight Imperial" style="width:50Px;" property="grossWeightImperial"/>
                            <display:column title="Gross Weight Metric" style="width:50Px;" property="grossWeightMetric"/>
                            <display:column title="Tare Weight Imperial" style="width:50Px;" property="tareWeightImperial"/>
                            <display:column title="Tare Weight Metric" style="width:50Px;" property="tareWeightMetric"/>
                            <display:column title="Volume Imperial" style="width:50Px;" property="volumeImperial"/>
                            <display:column title="Volume Metric" style="width:50Px;" property="volumeMetric"/>
                            <display:column title="Target Volume Imperial" style="width:50Px;" property="targetVolumeImperial"/>
                            <display:column title="Target Volume Metric" style="width:50Px;" property="targetVolumeMetric"/>
                        </display:table>
                    </td></tr>
            </table>
        </html:form>
    </body>
</html>
<script type="text/javascript" >
    function editUnitType(path,id,descrip,elite,shortDesc,intLenImp,intLenMet,intWidImp,intWidMet,intHeightImp,intHeightMet,doorHeightImp,doorHeightMet,doorWidImp,doorWidMet,grossWeightImp,grossWeightMet,tareWeightImp,tareWeightMet,volImp,volMet,tarVolImp,tarVolMet,ref,remarks,enabLclAir,enabLclExports,enabLclImports){
        var href=path+"/lclUnitType.do?methodName=editUnitType&id="+id+"&description="+descrip+"&eliteType="+elite+"&shortDesc="+shortDesc+"&interiorLengthImperial="+intLenImp+"&interiorLengthMetric="+intLenMet+"&interiorWidthImperial="+intWidImp+"&interiorWidthMetric="+intWidMet+"&interiorHeightImperial="+intHeightImp+"&interiorHeightMetric="+intHeightMet+"&doorHeightImperial="+doorHeightImp+"&doorHeightMetric="+doorHeightMet+"&doorWidthImperial="+doorWidImp+"&doorWidthMetric="+doorWidMet+"&grossWeightImperial="+grossWeightImp+"&grossWeightMetric="+grossWeightMet+"&tareWeightImperial="+tareWeightImp+"&tareWeightMetric="+tareWeightMet+"&volumeImperial="+volImp+"&volumeMetric="+volMet+"&targetVolumeImperial="+tarVolImp+"&targetVolumeMetric="+tarVolMet+"&refrigerated="+ref+"&remarks="+remarks+"&enabledLclAir="+enabLclAir+"&enabledLclExports="+enabLclExports+"&enabledLclImports="+enabLclImports;
        document.location.href=href;
    }
    function addUnitType(){
        $('#methodName').val('addUnitType');
        $('#lclUnitTypeForm').submit();
    }
    function unitTypeSearch(path){
        var descrip=$('#description').val();
        var href=path+"/lclUnitType.do?methodName=search&description="+descrip;
        document.location.href=href;
    }
</script>
