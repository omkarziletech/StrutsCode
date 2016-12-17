<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.CarriersOrLineDAO,com.gp.cong.logisoft.domain.CarriersOrLine"%>


<%	
	String functionName= null;
	String carrier="";
	if (request.getParameter("tabName") != null) {
			functionName = request.getParameter("tabName");
		}
		if (functionName == null) {
			return;
		}
	if(functionName.equals("CARRIER")){
		if(null != request.getParameter("carrierCode")){
			carrier= request.getParameter("carrierCode");
		}
	}
	StringBuffer stringBuffer = new StringBuffer();
	stringBuffer.append("<ul>");
	if (carrier != null && !carrier.trim().equals("")) {
		CarriersOrLineDAO carriersOrLineDAO = new CarriersOrLineDAO();
		List<CarriersOrLine> carrierList = carriersOrLineDAO.findByCarrierCode(carrier);
		if(null!=carrierList){
				for(CarriersOrLine carriersOrLine : carrierList) {
				if(null != carriersOrLine.getSCAC() && !carriersOrLine.getSCAC().trim().equals("")){
					stringBuffer.append("<li id='"+ carriersOrLine.getCarriercode().toString() + "'>"
						+ "<b>"+carriersOrLine.getCarriercode().toString()
						+ " <--> "+carriersOrLine.getCarriername().toString()
						+ " <--> " + carriersOrLine.getSCAC()
						+ "</b></li>");
				}else{
					stringBuffer.append("<li id='"+ carriersOrLine.getCarriercode().toString() + "'>"
						+ "<b>"+carriersOrLine.getCarriercode().toString()
						+ " <--> " + carriersOrLine.getCarriername()
						+ "</b></li>");
				}
			}
		}
	}
	stringBuffer.append("</ul>");	
	out.println(stringBuffer.toString());
%>