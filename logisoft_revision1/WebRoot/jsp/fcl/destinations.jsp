<%-- 
    Document   : destinations
    Created on : Mar 4, 2013, 7:55:33 PM
    Author     : Lakshmi Narayanan
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>   
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table class="table" style="margin: 1px;width: 998px;">
    <tr>
	<th>
	    <div class="float-left">Destination List</div>
	    <div class="float-right">
		<a href="javascript: closeDestinations()">
		    <img alt="Close Destination List" src="${path}/images/icons/close.png"/>
		</a>
	    </div>
	</th>
    </tr>
    <tr>
	<td class="label table-head-background">
	    <span class="float-left" style="margin: 0 0 0 5px;">
		<input type="checkbox" name="checkAllDestinations" id="checkAllDestinations" onclick="checkAllDestinations(this);">
		<label for="checkAllDestinations">ALL</label>
	    </span>
	    <span class="float-left" style="margin: 1px 0 0 15px;">
		<input type="button" value="Get Rates" class="button" onclick="showRatesForOrigin();"/>
	    </span>
	</td>
    </tr>
    <tr>
	<td>
	    <div style="width: 990px;overflow: auto;height: 290px;">
		<table class="table" style="margin: 0;border: none;width: 100%">
		    <tr>
			<c:forEach var="destinations" items="${destinationsList}">
			    <td>
				<ul class="label list-style-none">
				    <c:forEach var="destination" items="${destinations}">
					<li>
					    <input type="checkbox" name="destinationCheck" 
						   id="destinationCheck${destination.name}" value="${destination.id}">
					    <label for="destinationCheck${destination.name}">${destination.name}</label>
					</li>
				    </c:forEach>
				</ul>
			    </td>
			</c:forEach>
		    </tr>
		</table>
	    </div>
	</td>
    </tr>
</table>