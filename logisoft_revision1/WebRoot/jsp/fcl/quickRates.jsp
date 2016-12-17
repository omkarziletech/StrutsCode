<%-- 
    Document   : quickRates
    Created on : Mar 4, 2013, 8:08:38 PM
    Author     : Lakshmi Narayanan
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table class="table" style="margin: 1px;width: 598px;">
    <tr>
	<th colspan="4">
	<div class="float-left">Rates Quick Review</div>
	<div class="float-right">
	    <a href="javascript: hideQuickRates()">
		<img alt="Close Quick Rates" src="${path}/images/icons/close.png"/>
	    </a>
	</div>
    </th>
    </tr>
    <tr>
	<td class="label">Destination</td>
	<td>
	    <input type="text" class="textbox" name="quickRateDestination" id="quickRateDestination"/>
	    <input type="hidden" name="quickRateDestinationCheck" id="quickRateDestinationCheck"/>
	    <input type="hidden" name="quickRateDestinationId" id="quickRateDestinationId"/>
	    <input type="checkbox" id="quickRateDestinationByCity" checked
		   title="<ul><li>Checked=Look by City Name</li><li>UnChecked=Look by Country</li>" onclick="setDestinationOptions();"/>
	    <input type="checkbox" id="ratesForEntireCountry" title="Show Rates for Entire Country"/>
	</td>
	<td class="label">Commodity</td>
	<td>
	    <input type="text" class="textbox" name="quickRateCommodity" id="quickRateCommodity" value="006100"/>
	    <input type="hidden" name="quickRateCommodityCheck" id="quickRateCommodityCheck" value="006100"/>
            <input type="checkbox" id="quickRateCommodityBulletRate" title="Bullet Rates" onclick="setCommodityOptions();"/>
	</td>
    </tr>
    <tr>
	<td class="label">Origin</td>
	<td>
	    <input type="text" class="textbox" name="quickRateOrigin" id="quickRateOrigin"/>
	    <input type="hidden" name="quickRateOriginCheck" id="quickRateOriginCheck"/>
	    <input type="hidden" name="quickRateOriginId" id="quickRateOriginId"/>
	    <input type="checkbox" id="quickRateOriginByCity" checked
		   title="<ul><li>Checked=Look by City Name</li><li>UnChecked=Look by Country</li>" onclick="setOriginOptions();"/>
	</td>
	<td class="label">Hazmat</td>
	<td class="label">
	    <input type="radio" name="quickRateHazmat" id="quickRateHazmat" value="Y"/>Y
	    <input type="radio" name="quickRateHazmat" id="quickRateHazmat" value="N" checked/>N
	</td>
    </tr>
    <tr>
	<td colspan="4" align="center">
	    <input type="button" id="getRates" value="Rates" class="button" onclick="getQuickRates('${sessionScope.importNavigation}');"/>
	</td>
    </tr>
</table>