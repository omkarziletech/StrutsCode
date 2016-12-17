<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.domain.*,com.gp.cong.logisoft.bc.referenceDataManagement.*,com.gp.cong.logisoft.domain.*,com.gp.cong.logisoft.domain.UnLocation,com.gp.cong.logisoft.domain.FclBuy,com.gp.cong.logisoft.bc.ratemanagement.UnLocationBC,com.gp.cong.logisoft.bc.ratemanagement.PortsBC,org.json.JSONObject,com.gp.cong.logisoft.domain.CarriersOrLineTemp,com.gp.cong.logisoft.bc.ratemanagement.GenericCodeBC,com.gp.cong.logisoft.bc.ratemanagement.CarriersOrLineBC,com.gp.cong.logisoft.domain.GenericCode"%>
<%@page import="com.gp.cvst.logisoft.domain.Batch"%>

<%
	String requestFor = request.getParameter("requestFor");
	String terminalNumber = request.getParameter("terminalNumber");
	String terminalName = request.getParameter("terminalName");
	String destPortNumber = request.getParameter("destSheduleNumber");
	String destPortName = request.getParameter("destinationPortName");
	String commodityCodeDescription = request
			.getParameter("commodityCodeDescription");
	String commodityCodeForCode = request
			.getParameter("commodityCodeForCode");
	String commodityCodeDescription1 = request
			.getParameter("commodityCodeDescription1");
	String commodityCode = request.getParameter("commodityCode");
	String commodityCode1 = request.getParameter("commodityCode1");
	String sslineNumber = request.getParameter("ssLineNumber");
	String sslineName = request.getParameter("ssLineName");
	String vesselName = request.getParameter("vesselName");
	String vesselNo = request.getParameter("vesselNo");
	String scheduleCode = request.getParameter("scheduleCode");
	String salesCode = request.getParameter("salesCode");
	String wareHouseCode = request.getParameter("wareHouseCode");
	String wareHouseName = request.getParameter("wareHouseName");
	String code = request.getParameter("code");
	String codeDesc = request.getParameter("codeDesc");
	String codeType = request.getParameter("codeType");
	String ev=request.getParameter("ev");
	String index=request.getParameter("index");
	
	JSONObject jsonObj = new JSONObject();
	UnLocationBC unLocationBC = new UnLocationBC();
	GenericCodeBC genericCodeBC = new GenericCodeBC();
	CarriersOrLineBC carriersOrLineBC = new CarriersOrLineBC();
	PortsBC portsBC = new PortsBC();

	if (requestFor == null) {
		return;
	}
	if (requestFor.equals("OrgTerminalName")) {
		UnLocation unLocation = unLocationBC.getUncodeDesc(
				terminalNumber, "");
		jsonObj.put("terminalName", unLocation.getUnLocationName()
				.toString());

	} else if (requestFor.equals("country")) {
		String city = request.getParameter("city");
		UnLocation unLocation = unLocationBC.getCountry(city);
		if(null != unLocation.getCountryId() && null != unLocation.getCountryId().getCodedesc()){
			jsonObj.put("country", unLocation.getCountryId().getCodedesc());
		}
		if (unLocation.getStateId() != null
				&& unLocation.getStateId().getCode() != null) {
			jsonObj.put("state", unLocation.getStateId().getCode());
		}
	} else if (requestFor.equals("OrgTerminalNumber")) {
		UnLocation unLocation = unLocationBC.getUncodeDesc("",
				terminalName);
		jsonObj.put("terminalNumber", unLocation.getUnLocationCode()
				.toString());
	} else if (requestFor.equals("DestinationPortName")) {
		UnLocation unLocation = unLocationBC.getUncodeDesc(
				destPortNumber, "");
		jsonObj.put("destAirportname", unLocation.getUnLocationName()
				.toString());
	} else if (requestFor.equals("DestinationPortNumber")) {
		UnLocation unLocation = unLocationBC.getUncodeDesc("",
				destPortName);
		jsonObj.put("destPortNumber", unLocation.getUnLocationCode()
				.toString());
	} else if (requestFor.equals("CommodityCodeDescription")) {
		GenericCode genericCode = genericCodeBC
				.getCommodityCode(commodityCode);
		jsonObj.put("commodityDescription", ( null !=  genericCode && null != genericCode.getCodedesc())?genericCode.getCodedesc():"");
	}else if (requestFor.equals("ChargeCodeDescription")) {
		String codeTypeId="36";
		GenericCode genericCode = genericCodeBC.getChargeCodeDescription(commodityCode,codeTypeId);
		jsonObj.put("chargeCodeDescription", genericCode.getCodedesc());
		jsonObj.put("index", index);
	}else if (requestFor.equals("ChargeCodeDescriptionForBl")) {
		String codeTypeId="36";
		GenericCode genericCode = genericCodeBC.getChargeCodeDescription(commodityCode,codeTypeId);
		jsonObj.put("ChargeCodeDescriptionForBl", genericCode.getCodedesc());
		jsonObj.put("index", index);
	}else if (requestFor.equals("ChargeCode")) {
		String codeTypeId="36";
		GenericCode genericCode = genericCodeBC.getChargeCode(commodityCodeDescription,codeTypeId);
		jsonObj.put("ChargeCode", genericCode.getCode());
		jsonObj.put("index", index);
	}else if (requestFor.equals("SalesCodeDesc")) {
		GenericCode genericCode = genericCodeBC
				.getSalesCodeDesc(salesCode);
		if (genericCode.getCodedesc() != null) {
			jsonObj.put("salesCodeDesc", genericCode.getCodedesc());
		}
	} else if (requestFor.equals("CommodityCodeDescription1")) {
		try{
		GenericCode genericCode = genericCodeBC
				.getCommodityCode(commodityCode1);
		jsonObj.put("commodityDescription1", ( null !=  genericCode && null != genericCode.getCodedesc())?genericCode.getCodedesc():"");
		}catch(Exception e){
			e.printStackTrace();
		}
	} else if (requestFor.equals("CommodityCode")) {
		GenericCode genericCode = genericCodeBC
				.getCommodityCodeDescription("",
						commodityCodeDescription);
		jsonObj.put("commodityCode", genericCode.getCode());
	} else if (requestFor.equals("CommodityCodeDescriptionForCode")) {
		GenericCode genericCode = genericCodeBC
				.getCommodityCodeForCode(commodityCodeForCode);
		jsonObj.put("commodityCodeDescForCode", genericCode
				.getCodedesc());
	} else if (requestFor.equals("CommodityCode1")) {

		GenericCode genericCode = genericCodeBC
				.getCommodityCodeDescription("",
						commodityCodeDescription1);
		jsonObj.put("commodityCode1", genericCode.getCode());
	} else if (requestFor.equals("SSLineName")) {
		CarriersOrLineTemp carriersOrLineTemp = carriersOrLineBC
				.getSslineNumber(sslineNumber);
		jsonObj.put("ssLineName", carriersOrLineTemp.getCarriername());
		jsonObj.put("ssLineType", carriersOrLineTemp.getCarriertype()
				.getId());
		jsonObj.put("ssLineScac", carriersOrLineTemp.getSCAC());
	} else if (requestFor.equals("SSLineNo")) {
		CarriersOrLineTemp carriersOrLineTemp = carriersOrLineBC
				.getSslineName("", sslineName);
		jsonObj.put("ssLineNo", carriersOrLineTemp.getCarriercode());
		jsonObj.put("ssLineType", carriersOrLineTemp.getCarriertype()
				.getId());
		jsonObj.put("ssLineScac", carriersOrLineTemp.getSCAC());
	} else if (requestFor.equals("OrgTerminalNameInCodes")) {//----for Universal code------
		GenericCode genericCode = new GenericCode();
		RefTerminalTemp refTerminalTemp = portsBC
				.getTerminalName(terminalNumber);
		jsonObj.put("terminalNameInCodes", refTerminalTemp
				.getTerminalLocation());
		jsonObj.put("terminalCity", refTerminalTemp.getCity1());
		genericCode = refTerminalTemp.getGenericCode();
		if (genericCode != null && genericCode.getId() != null) {
			jsonObj.put("terType", refTerminalTemp.getGenericCode()
					.getId());
		}
	} else if (requestFor.equals("OrgTerminalInCodes")) {
		RefTerminalTemp refTerminalTemp = portsBC.getTerminalNumber(
				null, terminalName, null, null);
		jsonObj.put("terminalNumberInCodes", refTerminalTemp
				.getTrmnum());
	} else if (requestFor.equals("DestinationPortNameInCodes")) {
		PortsTemp portsTemp = portsBC.getDestinationPortName(
				destPortNumber, "");
		jsonObj.put("DestinationNameInCodes", portsTemp.getPortname());
	} else if (requestFor.equals("DestinationPortNumberInCodes")) {
		PortsTemp portsTemp = portsBC.getDestinationPortNumber("",
				destPortName);
		jsonObj.put("DestinationNumberInCodes", portsTemp
				.getShedulenumber());
	} else if (requestFor.equals("ScheduleCode")) {
		PortsTemp portsTemp = portsBC.getScheduleCode(scheduleCode);
		jsonObj.put("eciportcode", portsTemp.getEciportcode());
		jsonObj.put("portname", portsTemp.getPortname());
		jsonObj.put("scheduleSuffix", portsTemp.getControlNo());
		jsonObj.put("piercode", portsTemp.getPiercode());
		jsonObj.put("uncode", portsTemp.getUnCode());
		jsonObj.put("countryname", portsTemp.getCountryName());
	} else if (requestFor.equals("VesselNumber")) {
		GenericCode genericCode = genericCodeBC.getCommodityForFclBl(
				"", vesselName);
		jsonObj.put("VesselNo", genericCode.getCode());
	} else if (requestFor.equals("VesselName")) {
		GenericCode genericCode = genericCodeBC.getCommodityForFclBl(
				vesselNo, "");
		jsonObj.put("VesselName", genericCode.getCodedesc());
	} else if (requestFor.equals("VesselName")) {
		GenericCode genericCode = genericCodeBC.getCommodityForFclBl(
				vesselNo, "");
		jsonObj.put("VesselName", genericCode.getCodedesc());
	} else if (requestFor.equals("wareHouseName")) {
		WareHouseTempBC wareHouseTempBC = new WareHouseTempBC();
		WarehouseTemp wareHouseTemp = wareHouseTempBC
				.getWareHouseDetails(wareHouseCode, null, null, null);
		jsonObj.put("wareHouseCity", wareHouseTemp.getCity());
		jsonObj.put("wareHouseName", wareHouseTemp.getWarehouseName());
	} else if (requestFor.equals("CommodityDetails")) {
	    try{
				GenericCode genericCode = genericCodeBC.getCommodityDetails(codeType, code, codeDesc);
				jsonObj.put("commodityCode", genericCode.getCode());
				jsonObj.put("commodityDesc", genericCode.getCodedesc());
				if(ev != null && !ev.equalsIgnoreCase("")){
					jsonObj.put("ev",ev);
				}
			}catch(Exception e){
		     	e.printStackTrace();
			}
	}else if (requestFor.equals("wareHouseAddress")) {
		WareHouseTempBC wareHouseTempBC = new WareHouseTempBC();
		Warehouse wareHouse = wareHouseTempBC
				.getWareHouseAddress(wareHouseName);
		jsonObj.put("WareHouseName", wareHouse.getWarehouseName());
		if(wareHouse.getAddress()!=null && !wareHouse.getAddress().equals("") && !wareHouse.getAddress().equals("null")){
		jsonObj.put("WareHouseAddress", wareHouse.getAddress().trim());
		}else{
		   jsonObj.put("WareHouseAddress", "");
		}
	}
	out.println(jsonObj.toString());
%>