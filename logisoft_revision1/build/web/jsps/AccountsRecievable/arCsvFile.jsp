<%-- 
    Document   : arCsvFile
    Created on : Oct 18, 2011, 3:04:50 PM
    Author     : Lakshmi Narayanan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table width="100%" style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;">
  <tbody>
	<tr>
	  <td class="lightBoxHeader">${fileName}</td>
	  <td>
		<a id="lightBoxClose" href="javascript: closePopUpDiv();">
		  <img alt="" src="/logisoft/js/greybox/w_close.gif" title="Close" style="border: none;">Close
		</a>
	  </td>
	</tr>
  </tbody>
</table>
<div class="scrolldisplaytable" style="height: 200px">
  <c:choose>
	<c:when test="${not empty exception}">
	  <div class="exception-header">Exception occurred:</div>
	  <div class="exception-message">${exception}</div>
	</c:when>
	<c:otherwise>
	  <table width="100%" cellpadding="3" cellspacing="3" class="displaytagstyleNew">
		<tr class="csv-header">
		  <th>trmnum</th>
		  <th>actnum</th>
		  <th>blkey</th>
		  <th>cntnum</th>
		  <th>refnum</th>
		  <th>unus01</th>
		</tr>
		<tr>
		  <td><input type="text" name="trmnum" id="trmnum" class="input-text" maxlength="2" style="width: 20px"  value="${content['trmnum']}"/></td>
		  <td><input type="text" name="actnum" id="actnum" class="input-text" maxlength="5" style="width: 50px" value="${content['actnum']}"/></td>
		  <td><input type="text" name="blkey" id="blkey" class="input-text" maxlength="10" style="width: 100px" value="${content['blkey']}"/></td>
		  <td><input type="text" name="cntnum" id="cntnum" class="input-text" maxlength="3" style="width: 30px" value="${content['cntnum']}"/></td>
		  <td><input type="text" name="refnum" id="refnum" class="input-text" maxlength="9" style="width: 90px" value="${content['refnum']}"/></td>
		  <td><input type="text" name="unus01" id="unus01" class="input-text" maxlength="6" style="width: 60px" value="${content['unus01']}"/></td>
		</tr>
		<tr class="csv-header">
		  <th>totdue</th>
		  <th>type1</th>
		  <th>age</th>
		  <th>shtnam</th>
		  <th>cornum</th>
		  <th>type2</th>
		</tr>
		<tr>
		  <td><input type="text" name="totdue" id="totdue" class="input-text" maxlength="12" style="width: 120px" value="${content['totdue']}"/></td>
		  <td><input type="text" name="type1" id="type1" class="input-text" maxlength="1" style="width: 10px" value="${content['type1']}"/></td>
		  <td><input type="text" name="age" id="age" class="input-text" maxlength="1" style="width: 10px" value="${content['age']}"/></td>
		  <td><input type="text" name="shtnam" id="shtnam" class="input-text" maxlength="7" style="width: 70px" value="${content['shtnam']}"/></td>
		  <td><input type="text" name="cornum" id="cornum" class="input-text" maxlength="9" style="width: 90px" value="${content['cornum']}"/></td>
		  <td><input type="text" name="type2" id="type2" class="input-text" maxlength="1" style="width: 10px" value="${content['type2']}"/></td>
		</tr>
		<tr class="csv-header">
		  <th>hstkey</th>
		  <th>invkey</th>
		  <th>lstupd</th>
		  <th>bldate</th>
		  <th>cmpnum</th>
		  <th>unus02</th>
		</tr>
		<tr>
		  <td><input type="text" name="hstkey" id="hstkey" class="input-text" maxlength="15" style="width: 150px" value="${content['hstkey']}"/></td>
		  <td><input type="text" name="invkey" id="invkey" class="input-text" maxlength="9" style="width: 90px" value="${content['invkey']}"/></td>
		  <td><input type="text" name="lstupd" id="lstupd" class="input-text" maxlength="8" style="width: 80px" value="${content['lstupd']}"/></td>
		  <td><input type="text" name="bldate" id="bldate" class="input-text" maxlength="8" style="width: 80px" value="${content['bldate']}"/></td>
		  <td><input type="text" name="cmpnum" id="cmpnum" class="input-text" maxlength="2" style="width: 20px" value="${content['cmpnum']}"/></td>
		  <td><input type="text" name="unus02" id="unus02" class="input-text" maxlength="4" style="width: 40px" value="${content['unus02']}"/></td>
		</tr>
		<tr class="csv-header">
		  <th>selpmt</th>
		  <th>seladj</th>
		  <th>pdtype</th>
		  <th>postim</th>
		  <th colspan="2">depnum</th>
		</tr>
		<tr>
		  <td><input type="text" name="selpmt" id="selpmt" class="input-text" maxlength="1" style="width: 10px" value="${content['selpmt']}"/></td>
		  <td><input type="text" name="seladj" id="seladj" class="input-text" maxlength="1" style="width: 10px" value="${content['seladj']}"/></td>
		  <td><input type="text" name="pdtype" id="pdtype" class="input-text" maxlength="1" style="width: 10px" value="${content['pdtype']}"/></td>
		  <td><input type="text" name="postim" id="postim" class="input-text" maxlength="6" style="width: 60px" value="${content['postim']}"/></td>
		  <td colspan="2"><input type="text" name="depnum" id="depnum" class="input-text" maxlength="10" style="width: 10px" value="${content['depnum']}"/></td>
		</tr>
		<tr>
		  <td colspan="6" align="center"><input type="button" value="Save" class="buttonStyleNew" onclick="saveCsvFile(${id})"/></td>
		</tr>
	  </table>
	</c:otherwise>
  </c:choose>
</div>

