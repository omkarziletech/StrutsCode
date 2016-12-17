<%-- 
    Document   : multiQuoteCharges
    Created on : Mar 8, 2016, 12:02:48 PM
    Author     : NambuRajasekar
--%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"  %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/lable-fields.css" />
<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Charges JSP Page</title>
    </head>

    <table width="90%" border="0" cellpadding="0"  cellspacing="0"  class="tableBorderNew">
        <thead>
            <tr class="tableHeadingNew" >
                <td >Charges</td>
                <td width="88%"> <input  style="visibility:visible" value="Expand"  id="expand"  onclick="getMultiMode('expand')" class="buttonStyleNew"  type="button" >
               <input style="visibility:hidden" value="Collapse" id="collapse"  onclick="getMultiMode('collapse')" class="buttonStyleNew" type="button"></td>
            </tr>
        </thead>
    </table>

    <table width="90%" border="0" cellpadding="0"  cellspacing="0" id="collapseData" class="tableCollapse">
        <c:forEach var="oriDesti" items="${originDestination}">    
            <tr  class="tableHeadingNewGreen" >
                <td >
                    <html:radio  property="multiQuoteRadioId"  styleId="multiQuoteRadioId" name="quotationForm" value="${oriDesti.id}"/>
                </td>
                <td  style="font-size: 12px;text-transform: uppercase;">Origin:
                    <b style="font-size:10px;text-transform: uppercase;">${oriDesti.origin}</b></td>                  

                <td  style="font-size: 12px;text-transform: uppercase;">Destination:
                    <b style="font-size:10px;text-transform: uppercase;">${oriDesti.destination}</b></td>

            </tr>
            <%-- collapse model --%>
            <tr id="collapseRates">
                <td colspan="8" >
                     <div id="divtablesty1" style="border:thin;">
                        <table width="90%" id="collapseRatesTable" border="0"  cellspacing="0" cellpadding="0" class="displaytagstyleNew" >
                            <thead class="displaytagstyleNew">
                            <!--<td><img src="${path}/img/icons/up.gif" border="0" id="collapseIcon" onclick="getExpandMultiRates('collapse','${oriDesti.id}')"/> </td>-->
                            <td>&nbsp;</td>
                            <td width="10%">Unit</td>
                            <td  width="12%">ChargeCode</td>
                            <td>Sell</td>                           
                            <td>&nbsp;&nbsp;Vendor Name</td>
                            <td>&nbsp;&nbsp;Vendor Number</td>
                            </thead>
                            
                            <c:set var="AA" value="0"/> <!--A Unit-->
                            <c:set var="BB" value="0"/> <!--B Unit-->
                            <c:set var="CC" value="0"/> <!--C Unit-->
                            <c:set var="DD" value="0"/> <!--D Unit-->
                            <c:set var="EE" value="0"/> <!--E Unit-->
                            <c:set var="XX" value="0"/> <!--  EXTRA Unit-->
                            <c:forEach var="collapseRates" items="${collapseRates}">
                                <c:if test="${collapseRates.id eq oriDesti.id}">
                                    <tbody>
                                         <c:choose>
                                            <c:when test="${rowStyle eq 'oddStyle'}">
                                                <c:set var="rowStyle" value="evenStyle"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="rowStyle" value="oddStyle"/>
                                            </c:otherwise>
                                        </c:choose>
                                         <tr class="${rowStyle}">
                                            <c:choose>                            
                                                <c:when test="${collapseRates.unitNo eq 'A=20'}">
                                                     <c:set var="AA" value="${AA + 1}"/>
                                                    <c:choose>
                                                        <c:when test="${AA eq 1}"> 
                                                            <td class="whitebackgrnd"><input type="hidden" value="${collapseRates.chargeCode}"></td>
                                                            <td class="whitebackgrnd"> <font style="background-color:#CCEBFF;">${collapseRates.unitNo}</font></td> 
                                                            </c:when>
                                                            <c:otherwise> 
                                                            <td class="whitebackgrnd">&nbsp;</td>  
                                                            <td class="whitebackgrnd">&nbsp;</td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <td> ${collapseRates.chargeCodeDesc}</td>                                    
                                                   
                                                    <td><input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${collapseRates.amount}" size="5">&nbsp; </td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${collapseRates.acctName}"></td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${collapseRates.acctNo}"></td>
                                                </c:when>
                                                    
                                                <c:when test="${collapseRates.unitNo eq 'B=40'}">
                                                     <c:set var="BB" value="${BB + 1}"/>
                                                    <c:choose>
                                                        <c:when test="${BB eq 1}"> 
                                                            <td class="whitebackgrnd"><input type="hidden" value="${collapseRates.chargeCode}"></td>
                                                            <td class="whitebackgrnd"> <font style="background-color:#CCEBFF;">${collapseRates.unitNo}</font></td> 
                                                            </c:when>
                                                            <c:otherwise> 
                                                            <td class="whitebackgrnd">&nbsp;</td>  
                                                            <td class="whitebackgrnd">&nbsp;</td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <td> ${collapseRates.chargeCodeDesc}</td>                                    
                                                    <td><input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${collapseRates.amount}" size="5">&nbsp; </td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${collapseRates.acctName}"></td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${collapseRates.acctNo}"></td>
                                                </c:when>
                                                    
                                                 <c:when test="${collapseRates.unitNo eq 'C=40HC'}">
                                                     <c:set var="CC" value="${CC + 1}"/>
                                                    <c:choose>
                                                        <c:when test="${CC eq 1}"> 
                                                            <td class="whitebackgrnd"><input type="hidden" value="${collapseRates.chargeCode}"></td>
                                                            <td class="whitebackgrnd"> <font style="background-color:#CCEBFF;">${collapseRates.unitNo}</font></td> 
                                                            </c:when>
                                                            <c:otherwise> 
                                                            <td class="whitebackgrnd">&nbsp;</td>  
                                                            <td class="whitebackgrnd">&nbsp;</td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <td> ${collapseRates.chargeCodeDesc}</td>
                                                    <td><input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${collapseRates.amount}" size="5">&nbsp; </td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${collapseRates.acctName}"></td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${collapseRates.acctNo}"></td>
                                                </c:when>
                                                 <c:when test="${collapseRates.unitNo eq 'D=45'}">
                                                     <c:set var="DD" value="${DD + 1}"/>
                                                    <c:choose>
                                                        <c:when test="${DD eq 1}"> 
                                                            <td class="whitebackgrnd"><input type="hidden" value="${collapseRates.chargeCode}"></td>
                                                            <td class="whitebackgrnd"> <font style="background-color:#CCEBFF;">${collapseRates.unitNo}</font></td> 
                                                            </c:when>
                                                            <c:otherwise> 
                                                            <td class="whitebackgrnd">&nbsp;</td>  
                                                            <td class="whitebackgrnd">&nbsp;</td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <td> ${collapseRates.chargeCodeDesc}</td>  
                                                    <td><input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${collapseRates.amount}" size="5">&nbsp; </td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${collapseRates.acctName}"></td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${collapseRates.acctNo}"></td>
                                                </c:when>
                                                 <c:when test="${collapseRates.unitNo eq 'E=48'}">
                                                     <c:set var="EE" value="${EE + 1}"/>
                                                    <c:choose>
                                                        <c:when test="${EE eq 1}"> 
                                                            <td class="whitebackgrnd"><input type="hidden" value="${collapseRates.chargeCode}"></td>
                                                            <td class="whitebackgrnd"> <font style="background-color:#CCEBFF;">${collapseRates.unitNo}</font></td> 
                                                            </c:when>
                                                            <c:otherwise> 
                                                            <td class="whitebackgrnd">&nbsp;</td>  
                                                            <td class="whitebackgrnd">&nbsp;</td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <td> ${collapseRates.chargeCodeDesc}</td> 
                                                    <td><input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${collapseRates.amount}" size="5">&nbsp; </td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${collapseRates.acctName}"></td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${collapseRates.acctNo}"></td>
                                                </c:when>
                                                 <c:otherwise>
                                                     <c:set var="XX" value="${XX + 1}"/>
                                                    <c:choose>
                                                        <c:when test="${XX eq 1}"> 
                                                            <td class="whitebackgrnd"><input type="hidden" value="${collapseRates.chargeCode}"></td>
                                                            <td class="whitebackgrnd"> <font style="background-color:#CCEBFF;">${collapseRates.unitNo}</font></td> 
                                                            </c:when>
                                                            <c:otherwise> 
                                                            <td class="whitebackgrnd">&nbsp;</td>  
                                                            <td class="whitebackgrnd">&nbsp;</td>
                                                        </c:otherwise>
                                                    </c:choose>

                                                    <td> ${charges.chargeCodeDesc}</td> 
                                                    <td><input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${collapseRates.amount}" size="5">&nbsp; </td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${collapseRates.acctName}"></td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${collapseRates.acctNo}"></td>
                                               </c:otherwise>
                                            </c:choose>
                                          </tr>
                                    </tbody>
                                </c:if>
                            </c:forEach>
                         </table>
                    </div>
                </td>
            </tr>
        </c:forEach>
    </table>
    
    <table width="90%" border="0" cellpadding="0"  cellspacing="0" id="expandData" class="tableExpand" style="display:none">
        <c:forEach var="oriDest" items="${originDestination}">    
            <tr  class="tableHeadingNewGreen" >
                <td >
                    <html:radio  property="multiQuoteRadioId"  styleId="multiQuoteRadioId" name="quotationForm" value="${oriDest.id}"/>
                </td>
                <td  style="font-size: 12px;text-transform: uppercase;">Origin:
                    <b style="font-size:10px;text-transform: uppercase;">${oriDest.origin}</b></td>                  

                <td  style="font-size: 12px;text-transform: uppercase;">Destination:
                    <b style="font-size:10px;text-transform: uppercase;">${oriDest.destination}</b></td>

            </tr>
             <%-- Expand model --%>
            <tr  id="expandRates" >
                <td colspan="8" >
                    <div id="divtablesty1" style="border:thin;">
                        <table width="90%" id="expandRatesTable" border="0"  cellspacing="0" cellpadding="0" class="displaytagstyleNew" >
                            <thead class="displaytagstyleNew">
                            <!--<td><img src="${path}/img/icons/up.gif" border="0" id="expandIcon" onclick="getExpandMultiRates('expand','${oriDest.id}')"/> </td>-->
                            <td>&nbsp;</td>
                            <td width="10%">Unit</td>
                            <td  width="15%">ChargeCode</td>
                            <td>Cost</td>
                            <td>MarkUp</td>
                            <td>Sell</td>                           
                            <td>&nbsp;&nbsp;Vendor Name</td>
                            <td>&nbsp;&nbsp;Vendor Number</td>
                            </thead>

                            <c:set var="a" value="0" />
                            <c:set var="b" value="0"/>
                            <c:set var="c" value="0"/>
                            <c:set var="d" value="0"/>
                            <c:set var="e" value="0"/>
                            <c:set var="x" value="0"/>
           
                            <c:forEach var="charges" items="${charges}">
                                <c:if test="${charges.id eq oriDest.id}">                 
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${rowStyle eq 'oddStyle'}">
                                                <c:set var="rowStyle" value="evenStyle"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="rowStyle" value="oddStyle"/>
                                            </c:otherwise>
                                        </c:choose>

                                        <tr class="${rowStyle}">
                                            <c:choose>                            
                                            <c:when test="${charges.unitType eq 'A=20'}">
                                                    <c:set var="a" value="${a + 1}"/>
                                                    
                                                    <c:choose>
                                                        <c:when test="${a eq 1}"> 
                                                            <td class="whitebackgrnd"><input type="hidden" value="${charges.chargeCode}"></td>
                                                            <td class="whitebackgrnd"> <font style="background-color:#CCEBFF;">${charges.unitNo}</font></td> 
                                                            </c:when>
                                                            <c:otherwise> 
                                                            <td class="whitebackgrnd">&nbsp;</td>  
                                                            <td class="whitebackgrnd">&nbsp;</td>
                                                        </c:otherwise>
                                                    </c:choose>

                                                    <td> ${charges.chargeCodeDesc}</td>                                    
                                                    <td> <input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${charges.amount}" size="5">&nbsp;</td>                                      
                                                    <td> <input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${charges.markup}" size="5">&nbsp;</td>
                                                    <td><input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${charges.amount + charges.markup}" size="5">&nbsp; </td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${charges.acctName}"></td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${charges.acctNo}"></td>
                                            </c:when>
                                                
                                            <c:when test="${charges.unitNo eq 'B=40'}">
                                                <c:set var="b" value="${b + 1}"/>
                                                    <c:choose>
                                                        <c:when test="${b eq 1}"> 
                                                            <td class="whitebackgrnd"><input type="hidden" value="${charges.chargeCode}"></td>
                                                            <td class="whitebackgrnd"> <font style="background-color:#CCEBFF;">${charges.unitNo}</font></td> 
                                                        </c:when>
                                                        <c:otherwise> 
                                                            <td class="whitebackgrnd">&nbsp;</td>  
                                                            <td class="whitebackgrnd">&nbsp;</td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <td > ${charges.chargeCodeDesc}</td>                                    
                                                    <td> <input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${charges.amount}" size="5">&nbsp;</td>                                      
                                                    <td> <input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${charges.markup}" size="5">&nbsp;</td>
                                                    <td><input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${charges.amount + charges.markup}" size="5">&nbsp; </td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${charges.acctName}"></td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${charges.acctNo}"></td>
                                            </c:when>
                                            
                                            <c:when test="${charges.unitNo eq 'C=40HC'}">
                                                <c:set var="c" value="${c + 1}"/>
                                                    <c:choose>
                                                        <c:when test="${c eq 1}"> 
                                                            <td class="whitebackgrnd"><input type="hidden" value="${charges.chargeCode}"></td>
                                                            <td class="whitebackgrnd"><font style="background-color:#CCEBFF;">${charges.unitNo}</font> </td>
                                                        </c:when>
                                                        <c:otherwise> 
                                                            <td class="whitebackgrnd">&nbsp;</td>  
                                                            <td class="whitebackgrnd">&nbsp;</td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <td > ${charges.chargeCodeDesc}</td>                                    
                                                    <td> <input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${charges.amount}" size="5">&nbsp;</td>                                      
                                                    <td> <input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${charges.markup}" size="5">&nbsp;</td>
                                                    <td><input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${charges.amount + charges.markup}" size="5">&nbsp; </td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${charges.acctName}"></td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${charges.acctNo}"></td>                          
                                            </c:when>
                                                
                                            <c:when test="${charges.unitNo eq 'D=45'}">
                                                <c:set var="d" value="${d + 1}"/>
                                                    <c:choose>
                                                        <c:when test="${d eq 1}"> 
                                                            <td class="whitebackgrnd"><input type="hidden" value="${charges.chargeCode}"></td>
                                                            <td class="whitebackgrnd"> <font style="background-color:#CCEBFF;">${charges.unitNo}</font>   </td>  
                                                        </c:when>
                                                        <c:otherwise> 
                                                            <td class="whitebackgrnd">&nbsp;</td>  
                                                            <td class="whitebackgrnd">&nbsp;</td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <td > ${charges.chargeCodeDesc}</td>                                    
                                                    <td> <input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${charges.amount}" size="5">&nbsp;</td>                                      
                                                    <td> <input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${charges.markup}" size="5">&nbsp;</td>
                                                    <td><input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${charges.amount + charges.markup}" size="5">&nbsp; </td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${charges.acctName}"></td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${charges.acctNo}"></td>
                                            </c:when>
                                                
                                            <c:when test="${charges.unitNo eq 'E=48'}">
                                                <c:set var="e" value="${e + 1}"/>
                                                    <c:choose>
                                                        <c:when test="${e eq 1}"> 
                                                            <td class="whitebackgrnd"><input type="hidden" value="${charges.chargeCode}"></td>
                                                            <td class="whitebackgrnd"> <font style="background-color:#CCEBFF;">${charges.unitNo}</font>   </td>  
                                                        </c:when>
                                                        <c:otherwise> 
                                                            <td class="whitebackgrnd">&nbsp;</td>  
                                                            <td class="whitebackgrnd">&nbsp;</td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <td > ${charges.chargeCodeDesc}</td>                                    
                                                    <td> <input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${charges.amount}" size="5">&nbsp;</td>                                      
                                                    <td> <input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${charges.markup}" size="5">&nbsp;</td>
                                                    <td><input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${charges.amount + charges.markup}" size="5">&nbsp; </td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${charges.acctName}"></td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${charges.acctNo}"></td>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="x" value="${x + 1}"/>
                                                    <c:choose>
                                                        <c:when test="${x eq 1}"> 
                                                            <td class="whitebackgrnd"><input type="hidden" value="${charges.chargeCode}"></td>
                                                            <td class="whitebackgrnd"> <font style="background-color:#CCEBFF;">${charges.unitNo}</font>   </td>  
                                                        </c:when>
                                                        <c:otherwise> 
                                                            <td class="whitebackgrnd">&nbsp;</td>  
                                                            <td class="whitebackgrnd">&nbsp;</td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <td > ${charges.chargeCodeDesc}</td>                                    
                                                    <td> <input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${charges.amount}" size="5">&nbsp;</td>                                      
                                                    <td> <input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${charges.markup}" size="5">&nbsp;</td>
                                                    <td><input type="text" readonly="readonly" class="BackgrndColorForTextBox" value="${charges.amount + charges.markup}" size="5">&nbsp; </td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${charges.acctName}"></td>
                                                    <td><input type="text" class="textlabelsBoldForTextBox" style="background-color:CCEBFF" readonly="true" value="${charges.acctNo}"></td>
                                            </c:otherwise>
                                        </c:choose>
                                       </tr>
                                    </tbody>
                                </c:if>
                            </c:forEach>               
                        </table>  
                    </div>
                </td>
            </tr>
        </c:forEach>
    </table>
</html>
