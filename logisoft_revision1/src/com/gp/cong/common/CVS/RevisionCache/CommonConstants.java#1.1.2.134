package com.gp.cong.common;

import com.gp.cong.logisoft.bc.ratemanagement.GenericCodeBC;
import com.gp.cong.logisoft.domain.GenericCode;
import org.apache.struts.util.MessageResources;

import com.gp.cong.struts.LoadLogisoftProperties;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import java.util.Date;

import org.apache.log4j.Logger;

public class CommonConstants implements ConstantsInterface{

    private static final Logger log = Logger.getLogger(CommonConstants.class);
    private static MessageResources messageResources = null;
    private static List<String> unitCostTypeListInOrder = null;
    // This Charge Code List is the list To be shown in GetRates Popup.
    public static List<String>  chargeCodeList = new GenericCodeDAO().codesToShownInGetRatesExpand();

    public static int getSearchLimit() throws Exception{
        String limit = LoadLogisoftProperties.getProperty(SEARCH_LIMIT);
        int searchLimit = 0;
        try {
            searchLimit = Integer.parseInt(limit);
            if (searchLimit == 0) {
                searchLimit = DEFAULT_SEARCH_LIMIT;
            }
        } catch (NumberFormatException ex) {
            searchLimit = DEFAULT_SEARCH_LIMIT;            
            log.info("getSearchLimit failed on " + new Date(),ex);
        }
        return searchLimit;
    }

    public static MessageResources loadMessageResources() {
        //if(null == messageResources) {
        messageResources = MessageResources.getMessageResources("com.gp.cong.struts.ApplicationResources");
        //}

        return messageResources;
    }

    public static Map<String,String> getEventMap() throws Exception {
        Map<String,String> eventMap = new HashMap<String, String>();
        GenericCodeBC genericCodeBC = new GenericCodeBC();
        for(GenericCode eventCode : genericCodeBC.getAllEventCode()) {
            eventMap.put(eventCode.getCode(), eventCode.getCodedesc());
        }
        return eventMap;
    }

    public static Map<String,String> getLPropertiesMap() throws Exception {
        Map<String,String> logisoftPropertiesMap = new HashMap<String, String>();
        GenericCodeBC genericCodeBC = new GenericCodeBC();
        for(GenericCode propertiesCode : genericCodeBC.getAllLPropertiesCode()) {
            logisoftPropertiesMap.put(propertiesCode.getCode(), propertiesCode.getCodedesc());
        }
        return logisoftPropertiesMap;
    }
    public static Map<String,String> getPropertiesMap(String field1) throws Exception {
        Map<String,String> logisoftPropertiesMap = new HashMap<String, String>();
        GenericCodeBC genericCodeBC = new GenericCodeBC();
        for(GenericCode propertiesCode : genericCodeBC.getPropertiesCode(field1)) {
            logisoftPropertiesMap.put(propertiesCode.getCode(), propertiesCode.getCodedesc());
        }
        return logisoftPropertiesMap;
    }

    public static String formatAmount(Object object){
		NumberFormat number = new DecimalFormat("###,###,##0.00");
		if(object==null || object.toString().trim().equals("")){
			return "0.00";
		}else{
		return number.format(object);
	    }
	}

    public static List<String> getUnitCostTypeListInOrder() throws Exception {
		if(null == unitCostTypeListInOrder) {
			GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
			setUnitCostTypeListInOrder(genericCodeDAO.getUnitCostTypeListInOrder());
		}
		return unitCostTypeListInOrder;
	}

	public static void setUnitCostTypeListInOrder(
			List<String> unitCostTypeListInOrder) {
		CommonConstants.unitCostTypeListInOrder = unitCostTypeListInOrder;
	}

}
