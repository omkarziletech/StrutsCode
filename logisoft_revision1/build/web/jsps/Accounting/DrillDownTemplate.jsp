<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<table  border="0" cellpadding="0" cellspacing="0">
		<display:table name="${drillDownList}"   defaultsort="1" defaultorder="descending" 
			class="displaytagstyle" id="drillDownTable" style="width:100%" sort="list"> 
			<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> drillDown Details Displayed,For more drillDown Details click on Page Numbers.
    				<br>
    				</span>
  			  </display:setProperty>
  			  <display:setProperty name="paging.banner.one_item_found">
    				<span class="pagebanner">
						One {0} displayed. Page Number
					</span>
  			  </display:setProperty>
  			   <display:setProperty name="paging.banner.all_items_found">
    				<span class="pagebanner">
						{0} {1} Displayed, Page Number
					</span>
  			  </display:setProperty>
    			<display:setProperty name="basic.msg.empty_list"><span class="pagebanner">
					  No Records Found.
					</span>
				</display:setProperty>
				<display:setProperty name="paging.banner.placement" value="bottom" />
			<display:setProperty name="paging.banner.item_name" value="drillDown"/>
  			<display:setProperty name="paging.banner.items_name" value="drillDown"/>
  			<display:column title="Charge<br/>Code" property="chargeCode" sortable="true" headerClass="sortable">
  			</display:column>
  			<display:column title="Transaction<br/>Amount" property="amount" sortable="true" headerClass="sortable">
  			</display:column>
  		</display:table>
	</table> 		
 		
