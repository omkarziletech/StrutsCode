<%@ page language="java"
	import="java.util.*,com.gp.cong.logisoft.hibernate.dao.*,com.gp.cong.logisoft.domain.*"%>
<%   
	String wareHouseName = "";
	String functionName = null;
	String address = "";
        String wareHouseNo = "";
        String wareHouseType = "";
        boolean fcl = false;
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}
	if (functionName.equals("BOOKING")) {
		if (request.getParameter("exportPositioning") != null
				&& request.getParameter("from") != null) {
			wareHouseName = request.getParameter("exportPositioning");
                        wareHouseType = request.getParameter("from");
                        fcl = true;
		}else if (request.getParameter("equipmentReturnName") != null
				&& request.getParameter("from") != null) {
			wareHouseName = request.getParameter("equipmentReturnName");
                        wareHouseType = request.getParameter("from");
                        fcl = true;
		}
	}else if (functionName.equals("IMPORT_AGENT_WAREHOUSE")) {
		if (request.getParameter("importAgentWarehouse") != null) {
			wareHouseNo = request.getParameter("importAgentWarehouse");
		}
	}else if("FCLBL".equals(functionName)){
            if (request.getParameter("importWareHouseName") != null) {
                    wareHouseName = request.getParameter("importWareHouseName");
                    wareHouseType = "FCLI";
                    fcl = true;
		}
        }

	StringBuffer portsNoArray = new StringBuffer();
	portsNoArray.append("<ul>");

	if (wareHouseName != null && !wareHouseName.trim().equals("")) {
		WarehouseDAO warehouseDAO = new WarehouseDAO();
		List warehouseList =null;
                if(fcl){
                     warehouseList = warehouseDAO.findForWarehousenameByType(
                                     wareHouseName,wareHouseType);
                }else{
                     warehouseList = warehouseDAO.findForWarehousenameAndAddress(
                                     wareHouseName,address);
                }
		Iterator iter = warehouseList.iterator();
		while (iter.hasNext()) {
			Warehouse warehouse = (Warehouse) iter.next();
			if (warehouse != null) {
				String wareHousName = warehouse.getWarehouseName() != null ? warehouse.getWarehouseName().trim(): "";
				String wareHouseCode = warehouse.getWarehouseNo() != null ? warehouse.getWarehouseNo().trim(): "";
                                int warehouseId =warehouse.getId();
				String wareHouseAddress = warehouse.getAddress() != null ? warehouse.getAddress().trim(): "";
				if (wareHouseAddress.equalsIgnoreCase("null")) {
					wareHouseAddress = "";
				}
				portsNoArray.append("<li id='" + warehouseId
						+ "'><font class='blue-70'>" + wareHousName + "</font> <-->"
						+ "<br/><font class=grey>" + wareHouseAddress
						+ "</font>,"
						+ "<b><font class='red-90'>" + wareHouseCode
						+ "</font></b>" + "</b></li>");
			}
		}
            }else if(wareHouseNo != null && !wareHouseNo.trim().equals("")){
                WarehouseDAO warehouseDAO = new WarehouseDAO();
                List warehouseList = warehouseDAO
				.findForWarehouseNo(wareHouseNo);
               Iterator iter = warehouseList.iterator();
               while (iter.hasNext()) {
			Warehouse warehouse = (Warehouse) iter.next();
                        if (warehouse != null) {
				String wareHousName = warehouse.getWarehouseName() != null ? warehouse
						.getWarehouseName().trim()
						: "";
				String wareHouseCode = warehouse.getWarehouseNo() != null ? warehouse
						.getWarehouseNo().trim()
						: "";                                
				portsNoArray.append("<li id='" + wareHouseCode
						+ "'><font class='blue-70'>" + wareHouseCode + " <--> </font><font class='green'>"
                                                + wareHousName
						+ "</font></li>");
			}
                        }
            }
	portsNoArray.append("</ul>");       
	out.println(portsNoArray.toString());
%>
