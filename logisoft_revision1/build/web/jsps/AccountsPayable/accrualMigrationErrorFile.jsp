<%-- 
    Document   : accrualMigrationErrorFile
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
		    <th>type</th>
		    <th>askfld</th>
		    <th>cntrl</th>
		    <th>cstcde</th>
		</tr>
		<tr>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.type" id="type" 
			       class="input-text" maxlength="1" style="width: 10px"  value="${accrualMigrationErrorFile.type}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.askfld" id="askfld" 
			       class="input-text" maxlength="38" style="width: 380px"  value="${accrualMigrationErrorFile.askfld}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.cntrl" id="cntrl" 
			       class="input-text" maxlength="3" style="width: 30px"  value="${accrualMigrationErrorFile.cntrl}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.cstcde" id="cstcde" 
			       class="input-text" maxlength="3" style="width: 30px"  value="${accrualMigrationErrorFile.cstcde}"/>
		    </td>
		</tr>
		<tr class="csv-header">
		    <th>amount</th>
		    <th>paiddt</th>
		    <th>eacode</th>
		    <th>gltrml</th>
		</tr>
		<tr>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.amount" id="amount" 
			       class="input-text" maxlength="11" style="width: 110px"  value="${accrualMigrationErrorFile.amount}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.paiddt" id="paiddt" 
			       class="input-text" maxlength="8" style="width: 80px"  value="${accrualMigrationErrorFile.paiddt}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.eacode" id="eacode" 
			       class="input-text" maxlength="1" style="width: 10px"  value="${accrualMigrationErrorFile.eacode}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.gltrml" id="gltrml" 
			       class="input-text" maxlength="2" style="width: 20px"  value="${accrualMigrationErrorFile.gltrml}"/>
		    </td>
		</tr>
		<tr class="csv-header">
		    <th>vendnm</th>
		    <th>vend</th>
		    <th>invnum</th>
		    <th>invdat</th>
		</tr>
		<tr>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.vendnm" id="vendnm" 
			       class="input-text" maxlength="25" style="width: 250px"  value="${accrualMigrationErrorFile.vendnm}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.vend" id="vend" 
			       class="input-text" maxlength="5" style="width: 50px"  value="${accrualMigrationErrorFile.vend}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.invnum" id="invnum" 
			       class="input-text" maxlength="25" style="width: 250px"  value="${accrualMigrationErrorFile.invnum}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.invdat" id="invdat" 
			       class="input-text" maxlength="8" style="width: 80px"  value="${accrualMigrationErrorFile.invdat}"/>
		    </td>
		</tr>
		<tr class="csv-header">
		    <th>entdat</th>
		    <th>entrby</th>
		    <th>enttim</th>
		    <th>postby</th>
		</tr>
		<tr>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.entdat" id="entdat" 
			       class="input-text" maxlength="8" style="width: 80px"  value="${accrualMigrationErrorFile.entdat}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.entrby" id="entrby" 
			       class="input-text" maxlength="8" style="width: 80px"  value="${accrualMigrationErrorFile.entrby}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.enttim" id="enttim" 
			       class="input-text" maxlength="6" style="width: 60px"  value="${accrualMigrationErrorFile.enttim}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.postby" id="postby" 
			       class="input-text" maxlength="8" style="width: 80px"  value="${accrualMigrationErrorFile.postby}"/>
		    </td>
		</tr>
		<tr class="csv-header">
		    <th>posted</th>
		    <th>posttm</th>
		    <th>chknum</th>
		    <th>venref</th>
		</tr>
		<tr>
		     <td>
			<input type="text" name="accrualMigrationErrorFile.posted" id="posted" 
			       class="input-text" maxlength="1" style="width: 10px"  value="${accrualMigrationErrorFile.posted}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.posttm" id="posttm" 
			       class="input-text" maxlength="6" style="width: 60px"  value="${accrualMigrationErrorFile.posttm}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.chknum" id="chknum" 
			       class="input-text" maxlength="10" style="width: 100px"  value="${accrualMigrationErrorFile.chknum}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.venref" id="venref" 
			       class="input-text" maxlength="25" style="width: 250px"  value="${accrualMigrationErrorFile.venref}"/>
		    </td>
		</tr>
		<tr class="csv-header">
		    <th>expvoy</th>
		    <th>inlvoy</th>
		    <th>unit</th>
		    <th>cstdr</th>
		</tr>
		<tr>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.expvoy" id="expvoy" 
			       class="input-text" maxlength="10" style="width: 100px"  value="${accrualMigrationErrorFile.expvoy}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.inlvoy" id="inlvoy" 
			       class="input-text" maxlength="9" style="width: 90px"  value="${accrualMigrationErrorFile.inlvoy}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.unit" id="unit" 
			       class="input-text" maxlength="28" style="width: 280px"  value="${accrualMigrationErrorFile.unit}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.cstdr" id="cstdr" 
			       class="input-text" maxlength="8" style="width: 80px"  value="${accrualMigrationErrorFile.cstdr}"/>
		    </td>
		</tr>
		<tr class="csv-header">
		    <th>updtby</th>
		    <th>upddat</th>
		    <th>updtim</th>
		    <th>postdt</th>
		</tr>
		<tr>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.updtby" id="updtby" 
			       class="input-text" maxlength="8" style="width: 80px"  value="${accrualMigrationErrorFile.updtby}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.upddat" id="upddat" 
			       class="input-text" maxlength="8" style="width: 80px"  value="${accrualMigrationErrorFile.upddat}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.updtim" id="updtim" 
			       class="input-text" maxlength="6" style="width: 60px"  value="${accrualMigrationErrorFile.updtim}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.postdt" id="postdt" 
			       class="input-text" maxlength="8" style="width: 80px"  value="${accrualMigrationErrorFile.postdt}"/>
		    </td>
		</tr>
		<tr class="csv-header">
		    <th>mstvn</th>
		    <th>uadrs1</th>
		    <th>uadrs2</th>
		    <th>uacity</th>
		</tr>
		<tr>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.mstvn" id="mstvn" 
			       class="input-text" maxlength="5" style="width: 50px"  value="${accrualMigrationErrorFile.mstvn}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.uadrs1" id="uadrs1" 
			       class="input-text" maxlength="25" style="width: 250px"  value="${accrualMigrationErrorFile.uadrs1}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.uadrs2" id="uadrs2" 
			       class="input-text" maxlength="25" style="width: 250px"  value="${accrualMigrationErrorFile.uadrs2}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.uacity" id="uacity" 
			       class="input-text" maxlength="18" style="width: 180px"  value="${accrualMigrationErrorFile.uacity}"/>
		    </td>
		</tr>
		<tr class="csv-header">
		    <th>uastat</th>
		    <th>uazip</th>
		    <th>uaphn</th>
		    <th>uafax</th>
		</tr>
		<tr>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.uastat" id="uastat" 
			       class="input-text" maxlength="2" style="width: 20px"  value="${accrualMigrationErrorFile.uastat}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.uazip" id="uazip" 
			       class="input-text" maxlength="5" style="width: 50px"  value="${accrualMigrationErrorFile.uazip}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.uaphn" id="uaphn" 
			       class="input-text" maxlength="15" style="width: 150px"  value="${accrualMigrationErrorFile.uaphn}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.uafax" id="uafax" 
			       class="input-text" maxlength="15" style="width: 150px"  value="${accrualMigrationErrorFile.uafax}"/>
		    </td>
		</tr>
		<tr class="csv-header">
		    <th>editky</th>
		    <th>appddt</th>
		    <th>appdby</th>
		    <th>appdtm</th>
		</tr>
		<tr>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.editky" id="editky" 
			       class="input-text" maxlength="12" style="width: 120px"  value="${accrualMigrationErrorFile.editky}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.appddt" id="appddt" 
			       class="input-text" maxlength="8" style="width: 80px"  value="${accrualMigrationErrorFile.appddt}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.appdby" id="appdby" 
			       class="input-text" maxlength="8" style="width: 80px"  value="${accrualMigrationErrorFile.appdby}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.appdtm" id="appdtm" 
			       class="input-text" maxlength="6" style="width: 60px"  value="${accrualMigrationErrorFile.appdtm}"/>
		    </td>
		</tr>
		<tr class="csv-header">
		    <th>autocd</th>
		    <th>cmmnts</th>
		    <th>mstck</th>
		    <th>agtvn</th>
		</tr>
		<tr>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.autocd" id="autocd" 
			       class="input-text" maxlength="1" style="width: 10px"  value="${accrualMigrationErrorFile.autocd}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.cmmnts" id="cmmnts" 
			       class="input-text" maxlength="25" style="width: 250px"  value="${accrualMigrationErrorFile.cmmnts}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.mstck" id="mstck" 
			       class="input-text" maxlength="10" style="width: 100px"  value="${accrualMigrationErrorFile.mstck}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.agtvn" id="agtvn" 
			       class="input-text" maxlength="5" style="width: 50px"  value="${accrualMigrationErrorFile.agtvn}"/>
		    </td>
		</tr>
		<tr class="csv-header">
		    <th>key022</th>
		    <th>faecde</th>
		    <th>actcod</th>
		    <th>cutamt</th>
		</tr>
		<tr>
		     <td>
			<input type="text" name="accrualMigrationErrorFile.key022" id="key022" 
			       class="input-text" maxlength="22" style="width: 220px"  value="${accrualMigrationErrorFile.key022}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.faecde" id="faecde" 
			       class="input-text" maxlength="1" style="width: 10px"  value="${accrualMigrationErrorFile.faecde}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.actcod" id="actcod" 
			       class="input-text" maxlength="3" style="width: 30px"  value="${accrualMigrationErrorFile.actcod}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.cutamt" id="cutamt" 
			       class="input-text" maxlength="11" style="width: 110px"  value="${accrualMigrationErrorFile.cutamt}"/>
		    </td>
		</tr>
		<tr class="csv-header">
		    <th>cutdte</th>
		    <th>cuttim</th>
		    <th>voidby</th>
		    <th>voiddt</th>
		</tr>
		<tr>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.cutdte" id="cutdte" 
			       class="input-text" maxlength="80" style="width: 80px"  value="${accrualMigrationErrorFile.cutdte}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.cuttim" id="cuttim" 
			       class="input-text" maxlength="6" style="width: 60px"  value="${accrualMigrationErrorFile.cuttim}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.voidby" id="voidby" 
			       class="input-text" maxlength="8" style="width: 80px"  value="${accrualMigrationErrorFile.voidby}"/>
		    </td>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.voiddt" id="voiddt" 
			       class="input-text" maxlength="6" style="width: 60px"  value="${accrualMigrationErrorFile.voiddt}"/>
		    </td>
		</tr>
		<tr class="csv-header">
		    <th>voidtm</th>
		    <th colspan="3"></th>
		</tr>
		<tr>
		    <td>
			<input type="text" name="accrualMigrationErrorFile.voidtm" id="voidtm" 
			       class="input-text" maxlength="8" style="width: 80px"  value="${accrualMigrationErrorFile.voidtm}"/>
		    </td>
		</tr>
		<tr>
		    <td colspan="4" align="center"><input type="button" value="Save" class="buttonStyleNew" onclick="updateErrorFile(${id})"/></td>
		</tr>
	    </table>
	</c:otherwise>
    </c:choose>
</div>

