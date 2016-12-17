<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@include file="../includes/jspVariables.jsp"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
    <% pageContext.setAttribute("newLineChar", "\r\n"); %>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
    </head>
</html>
<body class="whitebackgrnd"/>
    <table width="100%" border="0" cellpadding="0"  class="tableBorderNew" cellspacing="0"
           id="records">
        <tr class="tableHeadingNew">
            <td><font style="font-weight: bold">Container Details</font></td>
            <td align="right">
                <c:if test="${addContainer == 'Yes' || fclBlForm.fclBl.breakBulk == 'Y'}">
                    <input type="button" value="Add" id="add1" class="buttonStyleNew"
                           onclick="GB_show('Container','${path}/fclBlContainerDetls.do?methodName=addContainer&bol=${fclBlForm.fclBl.bol}&breakBulk=${fclBlForm.fclBl.breakBulk}',300,830);"/>
                </c:if>

            </td>
        </tr>
        <tr>
            <td>
               
                 
                <table border="0" cellpadding="0" cellspacing="0" id="containerTable" >
                    <div style=" max-height: 245px; overflow: auto;">
                <c:set var="index" value="1"/>
                    <display:table name="${fclBlContainerDtlsList}" id="container"
                                   class="displaytagstyle">

                       
                        <display:column style="color:red;font-weight:bold;font-size:14pt;">*</display:column>
                        <display:column title="Unit No">
                            <%--<html:checkbox property="trialCheckbox" onclick="cleardata()"></html:checkbox>--%>
                            <input type="text" name="trailerNo" Class="BackgrndColorForTextBox"  size="15"
                                   id="unitNo${index}" onkeypress="allowFreeFormat(this)"
                                   value="${container.trailerNo}" onkeydown="enable(this)"
                                   maxlength="13" readonly="readonly" tabindex="-1" />
                        </display:column>
                        <display:column title="Seal #">
                            <html:text property="sealNo" size="15" styleClass="BackgrndColorForTextBox" 
                                       readonly="true"  value="${container.sealNo}" onkeydown="getDate(this)" tabindex="-1" ></html:text>
                        </display:column>
                        <display:column title="Last Update">
                            <fmt:formatDate pattern="dd-MMM-yyyy" var="containerDate" value="${container.lastUpdate}"/>
                            <input type="text" readonly="true"  name="lastUpdate" class="BackgrndColorForTextBox"
                                   id="txtItemcreatedon1${index}" size="8" value="${containerDate}" tabindex="-1" />
                        </display:column>
                        <display:column title="User">
                            <input type="text" readonly="true"  Class="BackgrndColorForTextBox" name="user"
                                   id="txtItemcreatedon1${index}" size="15"  value="${container.userName}" tabindex="-1" />
                        </display:column>
                        <display:column title="Size Legend">
                            <input type="text" readonly="true"  Class="BackgrndColorForTextBox" name="user"
                                   id="txtItemcreatedon1${index}" size="15"  value="${container.sizeLegend.codedesc}" tabindex="-1" />
                            <c:if test="${! empty container.specialEquipment}">
                                (${container.specialEquipment})
                            </c:if>
                        </display:column>
                        <display:column title="Marks and Numbers">
                            <html:textarea styleClass="BackgrndColorForTextBox" readonly="true"  property="marksNo"  
                                           value="${container.marks}"  cols="18" tabindex="-1" />
                        </display:column>
                        <display:column>
                            <c:choose>
                                <c:when test="${container.disabledFlag == 'D'}">
                                    <span>&nbsp;</span>
                                </c:when>
                                <c:when test="${not empty container.trailerNo}">
                                    <span>
                                        <input id="marksbutton${index}"  type="button" value="PKGS" 
                                               <c:choose>  
                                                   <c:when test="${not empty container.fclBlMarks}">
                                                       class="buttonColor"
                                                   </c:when>
                                                   <c:otherwise>
                                                       class="buttonStyleNew"
                                                   </c:otherwise>
                                               </c:choose>
                                               onclick="gotoMarksNumbers('${container.trailerNo}','${container.trailerNoId}','${fclBlForm.fclBl.bol}','${fclBlForm.fclBl.fileNo}','${fclBlForm.fclBl.readyToPost}')"  onmouseover="tooltip.show('<strong>Packages </strong>',null,event);" onmouseout="tooltip.hide();"/>
                                    </span>
                                    <c:if test="${fclBlForm.fclBl.hazmat=='Y'}">
                                        <input id="hazmatbutton${index}"  type="button" value="HAZ" 
                                              <c:choose>  
                                                   <c:when test="${container.hazmat}">
                                                       class="buttonColor"
                                                   </c:when>
                                                   <c:otherwise>
                                                       class="buttonStyleNew"
                                                   </c:otherwise>
                                               </c:choose>

                                               onclick="return GB_show('HazMat', '${path}/fCLHazMat.do?buttonValue=fclbl&indexValue=${index}&containerId=${container.trailerNoId}&bolid=${fclBlForm.fclBl.bol}&name=FclBl&fileNo=${fclBlForm.fclBl.fileNo}&unitNo=${container.trailerNo}&manifest=${fclBlForm.fclBl.readyToPost}',500,1100);"
                                               onmouseover="tooltip.show('<strong>Hazmat</strong>',null,event);" onmouseout="tooltip.hide();"/>
                                    </c:if>
                                </c:when>
                            </c:choose>
                        </display:column>
                        <display:column title="Actions">
                            <c:choose>
                                <c:when test="${container.disabledFlag == 'D'}">
                                    <img src="${path}/img/icons/key.png" id="enableButton${index}" name="containerEnableIcon" onmouseover="tooltip.show('Enable',null,event);" onmouseout="tooltip.hide();"
                                          <c:choose>
                                            <c:when test="${fclBlForm.fclBl.confirmOnBoard == 'Y'}">
                                                style="visibility: hidden"
                                            </c:when>
                                            <c:otherwise>
                                                style="visibility: visible"
                                            </c:otherwise>
                                          </c:choose>
                                         onclick="openCommentPopUp('${container.sizeLegend.id}','${container.trailerNoId}','enableContainer','${roleDuty.disabledContainerwithAPcosts}')"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="hotspot" onmouseover="tooltip.show('Edit',null,event);" onmouseout="tooltip.hide();">
                                        <c:set var="containerMarks" value="${fn:replace(container.marks,newLineChar,';')}"/>
                                        <img src="${path}/img/icons/edit.gif" border="0" id="editImage${index}" name="containerEditIcon"
                                             onclick="GB_show('Container','${path}/fclBlContainerDetls.do?methodName=editContainer&bol=${fclBlForm.fclBl.bol}&id=${container.trailerNoId}&readyToPost=${fclBlForm.fclBl.readyToPost}&breakBulk=${fclBlForm.fclBl.breakBulk}',300,830);"/>
                                    </span>
                                    <span>
                                        <img src="${path}/img/icons/disable_lock.png" id="disableButton${index}" name="containerDisableIcon" onmouseover="tooltip.show('Disable',null,event);" onmouseout="tooltip.hide();"
                                         <c:choose>
                                             <c:when test="${fclBlForm.fclBl.confirmOnBoard == 'Y'}">
                                                style="visibility: hidden"
                                            </c:when>
                                            <c:otherwise>
                                                style="visibility: visible"
                                            </c:otherwise>
                                          </c:choose>
                                             onclick="openCommentPopUp('${container.sizeLegend.id}','${container.trailerNoId}','disableContainer','${roleDuty.disabledContainerwithAPcosts}')"/>
                                    </span>
                                </c:otherwise>
                            </c:choose>
                                        <c:if test="${not empty container.containerComments}">
                               <c:set var="containerComments" value="${fn:replace(container.containerComments,newLineChar,' ')}"/>  
                                <span class="hotspot" onmouseover="tooltip.showComments('<strong>${containerComments}</strong>',150,event);"  
                                      onmouseout="tooltip.hideComments();" style="color:black;">  
                                    <img id="viewgif4"  src="${path}/img/icons/view.gif"/></span>
                                </c:if>
                            </display:column>
                            <display:column>
                            <input type="hidden" name="id${index}" value="${container.trailerNoId}" id="containerId${index}" />
                        </display:column>
                        <c:set var="index" value="${index+1}"/>
                    </display:table>
                     </div>
                </table>
                
            </td>
        </tr>
    </table>
    <input type="hidden" name="idOfContainer" id="idOfContainer"/>
    <input type="hidden" name="sizeOfContainer" id="sizeOfContainer"/>
    <input type="hidden" name="currentFileNo" id="currentFileNo"/>
    <input type="hidden" name="bolId" id="bolId" />
    <input type="hidden" name="containerIndex" id="containerIndex" />
</body>
