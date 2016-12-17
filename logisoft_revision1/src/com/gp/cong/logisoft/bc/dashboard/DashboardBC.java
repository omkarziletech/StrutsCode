package com.gp.cong.logisoft.bc.dashboard;

import java.util.List;

import com.gp.cong.logisoft.domain.OperationDataWarehouseCount;
import com.gp.cong.logisoft.hibernate.dao.OperationDataWarehouseCountDAO;

public class DashboardBC {
	OperationDataWarehouseCountDAO operationDataWarehouseCountDAO = new OperationDataWarehouseCountDAO();
	public String getAllOperationDataWarehouseCountForLatestMonthXML(Integer limit) throws Exception {
		List<OperationDataWarehouseCount> operationDataWarehouseCountList = operationDataWarehouseCountDAO.getAllOperationDataWarehouseCountForLatestMonth(limit);
		StringBuffer dwcXML = new StringBuffer();
		dwcXML.append("<data style='display : none'>");
		for(OperationDataWarehouseCount dataWarehouseCount : operationDataWarehouseCountList) {
			dwcXML.append("<datum>");
			dwcXML.append("<text>"+dataWarehouseCount.getMonth().toUpperCase()+"-"+dataWarehouseCount.getYear()+"</text>");
			Integer dwCount = dataWarehouseCount.getQuoteCount()+dataWarehouseCount.getBookingCount()+dataWarehouseCount.getBlCount();
			dwcXML.append("<number>"+dwCount+"</number>");
			dwcXML.append("</datum>");
		}
		dwcXML.append("</data>");
		return dwcXML.toString();
	}

}
