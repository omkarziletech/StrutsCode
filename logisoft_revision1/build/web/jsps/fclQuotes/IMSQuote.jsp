<%@ page language="java"
         import="java.util.*"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
           prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
           prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
           prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%> 
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles"
           prefix="tiles"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-template"
           prefix="template"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-nested"
           prefix="nested"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../includes/jspVariables.jsp"  %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="../includes/resources.jsp" %>
<%@include file="../fragment/formSerialize.jspf"  %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script language="text/javascript" src="${path}/js/common.js"></script>
<c:if test="${ImsAdded == 'quote'}">
    <script>
	parent.parent.GB_hide();
	parent.parent.refreshPage();
    </script>
</c:if>
<div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
    <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
    <form style="text-align:center;padding-right:4px;padding-bottom: 4px;">
	<input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
    </form>
</div>
<body>
    <html:form action="/rateGrid" styleId="rateId" scope="request">
        <div id="mainDiv">
            <c:choose>
                <c:when test="${importFlag}">
                    <c:set var="emptyPickUpOrReturn" value="Empty Retrun"></c:set>
                    <c:set var="cargoPickUpOrReturn" value="Cargo Retrun"></c:set>
                </c:when>
                <c:otherwise>
                    <c:set var="emptyPickUpOrReturn" value="Empty PickUp"></c:set>
                    <c:set var="cargoPickUpOrReturn" value="Cargo PickUp"></c:set>
                </c:otherwise>
            </c:choose>
            <c:choose>
               <c:when test="${empty emptyLocationList && not empty ErrosMessageIMS}">
                    <font size="4" color="red">${ErrosMessageIMS}</font>
                </c:when> 
                <c:when test="${not empty LocErrosMessage}">
                    <font size="4" color="red">${LocErrosMessage}</font>
                </c:when>
                <c:when test="${not empty QuoteErrosMessage}">
                    <font size="4" color="red">${QuoteErrosMessage}</font>
                    <c:if test="${not empty rateGridForm.emptyLocation}">
                        <div id="routeDetails">
                            <table>
                                <tr class="textlabelsBold">
                                    <td colspan="7">Select ${emptyPickUpOrReturn}&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;
                                        <c:choose>
                                            <c:when test="${emptyLocationListStyle=='readOnly'}">
                                                <html:text property="emptyLocation" readonly="true" styleId="emptyLocation" styleClass="textbox readonly"
                                                           size="22" />
                                            </c:when>
                                            <c:otherwise>
                                                <html:select property="emptyLocation"  styleId="emptyLocation" onchange="getImsQuote(this)"
                                                             style="width:200px;" styleClass="dropdown_accounting">
                                                    <html:optionsCollection name="emptyLocationList" /></html:select>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <div id="routeDetails">
                        <table width="100%" class="tableBorderNew">
                            <tr class="textlabelsBold">
                                <c:if test="${not empty emptyLocationList}">
                                    <td colspan="7">Select ${emptyPickUpOrReturn}&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;
                                        <c:choose>
                                            <c:when test="${emptyLocationListStyle=='readOnly'}">
                                                <html:text property="emptyLocation" readonly="true" styleId="emptyLocation" styleClass="textbox readonly"
                                                           size="22" />
					    </c:when>
					    <c:otherwise>
						<html:select property="emptyLocation"  styleId="emptyLocation" onchange="getImsQuote(this)"
							     style="width:200px;" styleClass="dropdown_accounting">
						    <html:optionsCollection name="emptyLocationList" /></html:select>
					    </c:otherwise>
					</c:choose>
				    </td>
                                </c:if>
                            </tr>
                            <c:if test="${not empty imsQuoteList}">
				<tr class="textlabelsBold">
				    <td>
					<input type="button" class="buttonStyleNew" onclick="saveImsQuote()" value="Submit"/>
				    </td>
                                    <td>Move Type : INTERNATIONAL</td>
                                    <td>Service Type : DOOR</td>
                                    <td>Delivery Type : LIVE</td>
                                    <td>Hazardous : ${hazardous}</td>
                                    <td>Over Weight : NO</td>
                                    <td>Reefer : NO</td>
				</tr>
				<tr>
				    <td colspan="7">
                                        <table width="100%">
                                            <display:table name="${imsQuoteList}" id="imsQuote" class="displaytagstyleNew"   pagesize="50">
                                                <display:setProperty name="paging.banner.some_items_found">
						    <span class="pagebanner"> <font color="blue">{0}</font>
							ImsQuote details displayed,For more LineItems click on page
							numbers. <br> </span>
						    </display:setProperty>
						    <display:setProperty name="paging.banner.one_item_found">
						    <span class="pagebanner"> One {0} displayed. Page
							Number </span>
						    </display:setProperty>
						    <display:setProperty name="paging.banner.all_items_found">
						    <span class="pagebanner"> {0} {1} Displayed, Page
							Number </span>
						    </display:setProperty>

						<display:setProperty name="basic.msg.empty_list">
						    <span class="pagebanner"> No Records Found. </span>
						</display:setProperty>
                                                <c:choose>
                                                    <c:when test="${companyCode == '02'}">
                                                        <c:set var="truckerAccount" value="IMSLLC0001"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="truckerAccount" value="IMSLLC0002"/>
                                                    </c:otherwise>
                                                </c:choose>   
						<c:choose>
						    <c:when test="${not empty imsQuote.trucker}">
							<c:set var="trucker" value="${imsQuote.trucker}"/>
						    </c:when>
						    <c:otherwise>
							<c:set var="trucker" value="${truckerAccount}"/>
						    </c:otherwise>
						</c:choose>
                                                <display:column title="">
                                                    <input type="radio" name="selectIms" id="selectIms" value="${imsQuote.allInRate},${imsQuote.allIn2Rate},${imsQuote.quoteNumber},${trucker}" />
                                                </display:column>
                                                <display:column title="${emptyPickUpOrReturn}" property="emptyName"></display:column>
                                                <display:column title="${cargoPickUpOrReturn}" property="destinationName"></display:column>
                                                <display:column title="Port/Ramp/CY">
                                                    ${originTerminal}
                                                </display:column>
                                                <display:column title="Trucker">
						    <c:choose>
							<c:when test="${not empty imsQuote.trucker}">
							    <span onmouseover="tooltip.showSmall('<strong><a style=\'color: green\'>${imsQuote.truckerName}</a></strong>',null,event);" onmouseout="tooltip.hide();" style="cursor: pointer">
                                                                ${imsQuote.trucker}
                                                            </span>
							</c:when>
							<c:otherwise>
							    <span onmouseover="tooltip.showSmall('<strong><a style=\'color: green\'>IMS LLC</a></strong>',null,event);" onmouseout="tooltip.hide();" style="cursor: pointer">
                                                                ${truckerAccount}
                                                            </span>
							</c:otherwise>
						    </c:choose>
						</display:column>
                                                <display:column title="Mode" property="mode"></display:column>
                                                <display:column title="Via">
                                                    <c:if test="${imsQuote.viaDesc != '0'}">
                                                        ${imsQuote.viaDesc}
                                                    </c:if>
                                                </display:column>
                                                <display:column title="Size" property="containerSizeDesc"></display:column>
                                                <display:column title="Type" property="containerTypeDesc"></display:column>
                                                <display:column title="Buy" >
                                                    <c:choose>
                                                        <c:when test="${hazardous == 'YES'}">
                                                            <span onmouseover="tooltip.showSmall('<strong><a style=\'color: green\'>Break Down</a><br>-----------------------------<br><a style=\'color: green\'>Base Quote</a> : <a style=\'color: blue\'>$${imsQuote.quoteAmount}</a><br><a style=\'color: green\'>CurrentFuel(28%):</a>  <a style=\'color: blue\'>$${imsQuote.fuelFees}</a><br><a style=\'color: green\'>Hazardous :</a>  <a style=\'color: blue\'>$${imsQuote.hazardousFees}</a><br>-----------------------------<br>   <a style=\'color: green\'>Total :</a> <a style=\'color: blue\'>$${imsQuote.allInRate}</a></strong>',null,event);" onmouseout="tooltip.hide();" style="color: blue;cursor: pointer">
                                                                $${imsQuote.allInRate}
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span onmouseover="tooltip.showSmall('<strong><a style=\'color: green\'>Break Down</a><br>-----------------------------<br><a style=\'color: green\'>Base Quote</a> : <a style=\'color: blue\'>$${imsQuote.quoteAmount}</a><br><a style=\'color: green\'>CurrentFuel(28%):</a>  <a style=\'color: blue\'>$${imsQuote.fuelFees}</a><br>-----------------------------<br>   <a style=\'color: green\'>Total :</a> <a style=\'color: blue\'>$${imsQuote.allInRate}</a></strong>',null,event);" onmouseout="tooltip.hide();" style="color: blue;cursor: pointer">
                                                                $${imsQuote.allInRate}
                                                            </span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </display:column>
                                                <display:column title="Sell">
                                                    <c:choose>
                                                        <c:when test="${hazardous == 'YES'}">
                                                            <span onmouseover="tooltip.showSmall('<strong><a style=\'color: green\'>Break Down</a><br>-----------------------------<br><a style=\'color: green\'>Base Quote</a> : <a style=\'color: blue\'>$${imsQuote.quote2Amt}</a><br><a style=\'color: green\'>CurrentFuel(28%):</a>  <a style=\'color: blue\'>$${imsQuote.fuel2Fees}</a><br><a style=\'color: green\'>Hazardous :</a>  <a style=\'color: blue\'>$${imsQuote.hazardousFees}</a><br>-----------------------------<br>   <a style=\'color: green\'>Total :</a> <a style=\'color: blue\'>$${imsQuote.allIn2Rate}</a></strong>',null,event);" onmouseout="tooltip.hide();" style="color: blue;cursor: pointer">
                                                                $${imsQuote.allIn2Rate}
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span onmouseover="tooltip.showSmall('<strong><a style=\'color: green\'>Break Down</a><br>-----------------------------<br><a style=\'color: green\'>Base Quote</a> : <a style=\'color: blue\'>$${imsQuote.quote2Amt}</a><br><a style=\'color: green\'>CurrentFuel(28%):</a>  <a style=\'color: blue\'>$${imsQuote.fuel2Fees}</a><br>-----------------------------<br>   <a style=\'color: green\'>Total :</a> <a style=\'color: blue\'>$${imsQuote.allIn2Rate}</a></strong>',null,event);" onmouseout="tooltip.hide();" style="color: blue;cursor: pointer">
                                                                $${imsQuote.allIn2Rate}
                                                            </span>
                                                        </c:otherwise>
                                                    </c:choose>

                                                </display:column>
                                                <display:column title="Quote Number" property="quoteNumber"></display:column>
                                            </display:table>
                                        </table>
                                    </td>
                                </tr>
                            </c:if>
                        </table>
                    </div>

                </c:otherwise>
            </c:choose>

        </div>
        <html:hidden property="origin"/>
        <html:hidden property="originZip"/>
        <html:hidden property="action"/>
        <html:hidden property="buy"/>
        <html:hidden property="sell"/>
        <html:hidden property="comment"/>
        <html:hidden property="chargeId"/>
        <html:hidden property="screenName"/>
        <html:hidden property="truckerNumber"/>
        <html:hidden property="hazardous"/>
        <html:hidden property="fileNo"/>
    </html:form>
    <script type="text/javascript" language="javascript">
        function getImsQuote(obj){
            if(obj.value != ''){
                document.getElementById("newProgressBar").style.display = "block";
                document.rateGridForm.action.value = "getImsQuote";
                document.rateGridForm.submit();
            }
        }
        function saveImsQuote(){
	    if (jQuery('input:radio[name=selectIms]:checked').is(':checked')) {
		document.getElementById("newProgressBar").style.display = "block";
		var quoteValue = jQuery('input:radio[name=selectIms]:checked').val();
		var pickUp = document.getElementById("emptyLocation").value;
		var quote=quoteValue.split(',');
		var date = new Date();
		var currentDate = date.getMonth() + "/" + date.getDate() + "/" + date.getYear();
		var userName = '${loginuser.loginName}';
		document.rateGridForm.buy.value = quote[0];
		document.rateGridForm.sell.value = quote[1];
		var comment = pickUp;
		if(jQuery.trim(quote[2])!=""){
		    comment+="--"+quote[2];
		}
		comment+="("+userName+"-"+currentDate+").";
		document.rateGridForm.comment.value = comment;
		document.rateGridForm.truckerNumber.value = quote[3];
		document.rateGridForm.action.value = "saveImsQuote";
		document.rateGridForm.submit();
	    }else{
		alert("Please Select Rate");
	    }
	}
    </script>
</body>