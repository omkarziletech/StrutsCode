<%@page import="com.gp.cong.common.CommonUtils"%>
<%@page import="com.logiware.constants.ItemConstants"%>
<%@page import="com.gp.cong.struts.LoadLogisoftProperties"%>
<%@page import="com.gp.cong.logisoft.hibernate.dao.ItemDAO"%>
<%@page import="com.gp.cong.logisoft.domain.Item"%>
<%@page import="com.gp.cong.logisoft.domain.ItemTree"%>
<%@page import="com.gp.cong.logisoft.hibernate.dao.ItemTreeDAO"%>
<%@page import="com.gp.cong.logisoft.bc.fcl.ImportBc"%>
<%@page import="com.gp.cong.logisoft.bc.fcl.ItemMasterBC"%><%@include file="includes/jspVariables.jsp" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<un:useConstants className="com.logiware.constants.ItemConstants" var="itemConstants"/>
<script type="text/javascript">
    <c:choose>
	<c:when test="${screenName=='fileSearch'}">
	    window.parent.gotoFclSearchScreen();
	</c:when>
	<c:when test="${screenName=='Quotes' || screenName=='copyQuote' || screenName=='Bookings' || screenName=='BL'}">
	    window.parent.gotoFclOpsScreens("${selectedFileNumber}");
	</c:when>
	<c:when test="${trade=='ports'}">
            window.parent.location.href = "${path}/jsps/datareference/managePorts.jsp";
	</c:when>
        <c:when test="${trade=='addairrates'}">
	    window.parent.changeChildsByDesc("${itemConstants.TAB_NAME}=${itemConstants.AIR}");
        </c:when>
        <c:when test="${trade=='addretailrates'}">
	    window.parent.changeChildsByDesc("${itemConstants.TAB_NAME}=${itemConstants.RETAIL}");
        </c:when>
        <c:when test="${trade=='lclcoload'}">
	    window.parent.changeChildsByDesc("${itemConstants.TAB_NAME}=${itemConstants.COLOAD}");
        </c:when>
        <c:when test="${trade=='journalentry'}">
	    window.parent.changeChilds("${itemConstants.TAB_NAME}=${itemConstants.JOURNAL_ENTRY}");
        </c:when>
        <c:when test="${trade=='Batch'}">
	    window.parent.changeChilds("${itemConstants.TAB_NAME}=${itemConstants.BATCHES}");
        </c:when>
        <c:when test="${trade=='TrasactionHistory'}">
	    window.parent.changeChilds("${itemConstants.TAB_NAME}=${itemConstants.CHART_OF_ACCOUNTS}");
        </c:when>
        <c:when test="${trade=='ftf'}">
	    window.parent.changeChildsByDesc("${itemConstants.TAB_NAME}=${itemConstants.FTF}");
        </c:when>
        <c:when test="${trade=='universal'}">
	    window.parent.changeChildsByDesc("${itemConstants.TAB_NAME}=${itemConstants.UNIVERSAL_IMPORT}");
        </c:when>
        <c:when test="${trade=='inlandvoyage'}">
	    window.parent.changeChildsByDesc("${itemConstants.TAB_NAME}=${itemConstants.EXPORRT_VOYAGES}");
        </c:when>
        <c:when test="${trade=='fclbl'}">
	    window.parent.changeChildsByDesc("${itemConstants.TAB_NAME}=${itemConstants.FCL_BL_EXPORT}&fileno=${FileNo}");
        </c:when>
        <c:when test="${trade=='fileSearch'
                        || trade=='booking'}">
	    window.parent.changeChildsByDesc("${itemConstants.TAB_NAME}=${itemConstants.FCL_SEARCH_EXPORT}&fileno=${FileNo}");
        </c:when>
        <c:when test="${trade=='bookingForImport'}">
	    window.parent.changeChildsByDesc("${itemConstants.TAB_NAME}=${itemConstants.FILE_SEARCH_IMPORT}&fileno=${FileNo}");
        </c:when>
        <c:when test="${trade=='copyQuote'}">
	    window.parent.changeChildsByDesc("${itemConstants.TAB_NAME}=${FCL_SEARCH}&fileno=${FileNo}");
        </c:when>
        <c:otherwise>
	    window.parent.showHome();
        </c:otherwise>
    </c:choose>
</script>