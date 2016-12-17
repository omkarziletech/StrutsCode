<%-- 
    Document   : dbMonitorInfo
    Created on : Nov 29, 2012, 8:36:36 PM
    Author     : Lakshmi Narayanan
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>      
<c:set var="path" value="${pageContext.request.contextPath}"/>
<div id="info-container${process.id}" class="static-popup" style="display: none;width: 400px;height: 200px;">
    <table class="table" style="margin: 0px;border: none;">
	<tr>
	    <th>
		<div class="float-left">More Info</div>
		<div class="float-right">
		    <a href="javascript: void(0);" onclick="closeInfo('${process.id}')">
			<img alt="Close Info" src="${path}/images/icons/close.png"/>
		    </a>
		</div>
	    </th>
	</tr>
	<tr>
	    <td>
		<textarea class="textbox info" readonly 
			  style="width: 394px;height: 171px;overflow: auto;text-transform: none;">${process.info}</textarea>
	    </td>
	</tr>
    </table>
</div>