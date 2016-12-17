<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<table width="100%"
	style="background-image: url(${pageContext.request.contextPath}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;" height="15px">
	<tbody>
		<tr>
			<td class="lightBoxHeader">
				File Viewer
			</td>
			<td>
				<div style="vertical-align: top">
					<a id="lightBoxClose" href="javascript: closeIFrameDiv();">
						<img src="/logisoft/js/greybox/w_close.gif" title="Close" style="border: none;">Close
					</a>
				</div>
			</td>
		</tr>
	</tbody>
</table>
<div id="iFrameContentDiv">
	<iframe id="iframe" src="${pageContext.request.contextPath}/servlet/PdfServlet?fileName=${fileName}" frameborder="1" scrolling="auto"></iframe>
</div>
		

