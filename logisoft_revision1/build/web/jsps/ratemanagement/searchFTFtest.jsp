<%@ page language="java"  import="java.text.DateFormat,java.text.SimpleDateFormat,com.gp.cong.logisoft.beans.AirRatesBean,com.gp.cong.logisoft.hibernate.dao.FTFMasterDAO,com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.FTFMaster,com.gp.cong.logisoft.domain.FTFCommodityCharges,com.gp.cong.logisoft.domain.FTFDetails"%>
<jsp:useBean id="listtest" class="com.gp.cong.test.Listtest" scope="request">
</jsp:useBean> 

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>

 
<html> 
	<head>
		<title>JSP for SearchFTFForm form</title>
		
	<body class="whitebackgrnd" >
<html:form action="/searchFTF" scope="request">
          	 	<display:table name="listtest.exlist" defaultsort="5" class="displaytagstyle" id="ftfratestable" sort="list" > 
			 	<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    			<font color="red" size=2>*</font> <--Common Specific accessorial Charges &nbsp;&nbsp;&nbsp;
    				<font color="blue">{0}</font> LCL Foreign to Foreign Rates Displayed,For more Data click on Page Numbers.
    				<br>
    			</span>
  			  	</display:setProperty>
  				<display:setProperty name="paging.banner.one_item_found">
    				<span class="pagebanner">
    				<font color="red" size=2>*</font> <--Common Specific accessorial Charges &nbsp;&nbsp;&nbsp;
						One {0} displayed. Page Number
					</span>
  			 	</display:setProperty>
  			    <display:setProperty name="paging.banner.all_items_found">
    				<span class="pagebanner">
    				<font color="red" size=2>*</font> <--Common Specific accessorial Charges &nbsp;&nbsp;&nbsp;
						{0} {1} Displayed, Page Number
				</span>
  			    </display:setProperty>
    			<display:setProperty name="basic.msg.empty_list">
    			    <span class="pagebanner">
						No Records Found.
					</span>
				</display:setProperty>
				<display:setProperty name="paging.banner.placement" value="bottom" />
				<display:setProperty name="paging.banner.item_name" value="Foreign to Foreign Rate"/>
				<display:setProperty name="paging.banner.items_name" value="Foreign to Foreign Rates"/>
  				<display:column title="Name" property="contract"></display:column>
  			</display:table>		
		</html:form>		
	</body>
</html>

