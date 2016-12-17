package com.gp.cong.logisoft.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gp.cong.logisoft.domain.CarriersOrLineTemp;
import com.gp.cong.logisoft.domain.CustomerTemp;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.TradingPartnerTemp;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.hibernate.dao.CarriersOrLineDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;

/**
 * @author Vasan
 * This is a helper class to call the dao classes to load objects. 
 *
 */
public class LoadAPLRatesHelper {
	
	private UnLocationDAO refTerminalDAO = null;
	private static Map<String,UnLocation> refTerminalTempMap = null;
	private UnLocationDAO portsDAO=null;
	private static Map<String ,UnLocation> portsTempMap = null;
	private GenericCodeDAO genericCodeDAO = null;
	private static Map<String ,GenericCode> commodityCodeMap = null;
	private CustomerDAO carriersOrLineDAO = null;
	private static Map<String,TradingPartnerTemp> carriersOrLineTempMap=null;
	private static DBUtil dbUtil = new DBUtil();
	private static  Map<String ,GenericCode> genericCodeMap = null;
	
	public LoadAPLRatesHelper(){
		refTerminalDAO = new UnLocationDAO();
		refTerminalTempMap = new HashMap<String, UnLocation>();
		portsDAO=new  UnLocationDAO();
		portsTempMap = new HashMap<String, UnLocation>();
		genericCodeDAO=new GenericCodeDAO();
		commodityCodeMap = new HashMap<String, GenericCode>();
		carriersOrLineDAO = new CustomerDAO();
		carriersOrLineTempMap = new HashMap<String, TradingPartnerTemp>();
		genericCodeMap = new HashMap<String, GenericCode>();
	}
	
	
	public UnLocation getRefTerminalTemp(String originCode)throws Exception {
		if(refTerminalTempMap.get(originCode) == null) {
			List unLocationList = refTerminalDAO.findForManagement(originCode,null);
			if(unLocationList!=null && unLocationList.size()>0)
			{
				UnLocation refTerminal=(UnLocation)unLocationList.get(0);
				refTerminalTempMap.put(originCode, refTerminal);
			}
			
		}
		return refTerminalTempMap.get(originCode);
	}
	
	public UnLocation getPortsTemp(String destinationCode,String controlNo) throws Exception {
		if(portsTempMap.get(destinationCode) == null) {
			List portsList = portsDAO.findForManagement(destinationCode, null);
            if(portsList != null && !portsList.isEmpty()){
            	UnLocation ports = (UnLocation)portsList.get(0);
            	portsTempMap.put(destinationCode, ports);
            }
		}
		return portsTempMap.get(destinationCode);
	}
	public GenericCode getGenericComCode(String commodityCode)throws Exception {
		if(portsTempMap.get(commodityCode) == null) {
			List comList = genericCodeDAO.findForGenericCode(commodityCode);
            if(comList != null && !comList.isEmpty()) {
            	GenericCode genericCode = (GenericCode)comList.get(0);
            	commodityCodeMap.put(commodityCode, genericCode);
            }
		}
		return commodityCodeMap.get(commodityCode);
	}
	public TradingPartnerTemp getStreamShipLine(String streamShipLineNo)throws Exception {
		if(carriersOrLineTempMap.get(streamShipLineNo) == null) {
			 List SSList = carriersOrLineDAO.findAccountNo1(streamShipLineNo);
            if(SSList != null && !SSList.isEmpty()) {
            	TradingPartnerTemp carriersOrLineTemp = (TradingPartnerTemp)SSList.get(0);
            	carriersOrLineTempMap.put(streamShipLineNo, carriersOrLineTemp);
            }
		}
		return carriersOrLineTempMap.get(streamShipLineNo);
	}
	
	public GenericCode getCostID(String costCode)throws Exception {
		if(genericCodeMap.get(costCode+"_36")==null){
			genericCodeMap.put(costCode+"_36", dbUtil.getCostID(costCode.trim(), 36));
		}
		return genericCodeMap.get(costCode+"_36");
	}
	
	public GenericCode getContentTypeID(String contentType)throws Exception {
		if(genericCodeMap.get(contentType+"_38")==null){
			genericCodeMap.put(contentType+"_38", dbUtil.getContentTypeID(contentType.trim(), 38));
		}
		return genericCodeMap.get(contentType+"_38");
	}

	public GenericCode getCurrency(String currency)throws Exception {
		if(genericCodeMap.get(currency+"_32")==null){
			genericCodeMap.put(currency+"_32", dbUtil.getCurrency(currency.trim(), 32));
		}
		return genericCodeMap.get(currency+"_32");
	}
	
	
	public GenericCode getCostTypeID(String costType)throws Exception {
		if(genericCodeMap.get(costType+"_37")==null){
			GenericCode costTypeGenericCode = null;
			if(costType == null || costType.equals("")){
				costTypeGenericCode = genericCodeDAO.findByCodeName("I",37);//11460 PRIMARY FREIGHT //11516 BAL SHIPPING  // SEACORP 11460 Per Container size
			} else if(costType != null && costType.equals("%")){
	        	costTypeGenericCode = genericCodeDAO.findByCodeName("PER",37);//SEACOPR 11516 PER CANTAGE OFR
			} else if(costType != null && costType.equals("*")){ 
	        	costTypeGenericCode = genericCodeDAO.findByCodeName("H",37);//SEACOPR 11516//PER BL
			} else if(costType != null && costType.equals("#")){
	        	costTypeGenericCode = genericCodeDAO.findByCodeName("K",37);
			}else{
	        	costTypeGenericCode = genericCodeDAO.findByCodeName("A",37);
			}
			genericCodeMap.put(costType+"_37", costTypeGenericCode);
		}
		return genericCodeMap.get(costType+"_37");
	}

}
