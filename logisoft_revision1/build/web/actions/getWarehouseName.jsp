<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.*,com.gp.cong.logisoft.domain.*"%>
<%
	String wareHouseCode = "";
	String wareHouseName = "";
	String city = "";
	String airCargo = "";
	String address="";
	String functionName = null;
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}
	if (functionName.equals("WARE_HOUSE")) {
		if (request.getParameter("warehouseCode") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			wareHouseCode = request.getParameter("warehouseCode");
		}

	}else if (functionName.equals("BOOKING")) {
		if (request.getParameter("exportPositioning") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			wareHouseName = request.getParameter("exportPositioning");
		}else if (request.getParameter("loadLocation") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			wareHouseName = request.getParameter("loadLocation");
		}

	}
	if (request.getParameter("warehouseCode") != null) {

	}
	JSONArray portsNoArray = new JSONArray();
	if (wareHouseCode != null && !wareHouseCode.trim().equals("")) {
		WarehouseDAO warehouseDAO = new WarehouseDAO();
		List warehouseList = warehouseDAO.findForSearchWarehouse(
				wareHouseCode, wareHouseName, city, airCargo);
		Iterator iter = warehouseList.iterator();
		while (iter.hasNext()) {
			WarehouseTemp warehouseTemp = (WarehouseTemp) iter.next();
			portsNoArray.put(warehouseTemp.getId() + ":-"
					+ warehouseTemp.getWarehouseName().trim());
		}
	}else 	if (wareHouseName != null && !wareHouseName.trim().equals("")) {
		WarehouseDAO warehouseDAO = new WarehouseDAO();
		List warehouseList = warehouseDAO.findForWarehousenameAndAddress(
				 wareHouseName,address);
		Iterator iter = warehouseList.iterator();
		while (iter.hasNext()) {
			Warehouse warehouse= (Warehouse) iter.next();
			portsNoArray.put( warehouse.getWarehouseName().trim()+":-"+warehouse.getId()+":- "+warehouse.getAddress().trim());
		}
	}
	
	out.println(portsNoArray.toString());
%>
