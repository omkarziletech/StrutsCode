package com.gp.cong.logisoft.util;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class CommonFunctions {
	public static boolean isNotNull(Object object) {
        if (object != null && !object.toString().trim().equals("")) {
            return true;
        } else {
            return false;
        }
    }
	public static boolean isNotNullOrNotEmpty(Object object) {
		boolean returnFlag=false;
		if (object instanceof List) {
			List list=(List)object;
			if (list != null && !list.isEmpty()) {
				returnFlag=true;
			}
		}else if (object instanceof Set) {
			Set set=(Set)object;
			if (set != null && !set.isEmpty()) {
				returnFlag= true;
		}
		}
        return returnFlag;
    }
	
	public static Double getPercentOf(Double amount,Integer percent){
		return (amount*percent)/100;
	}
	public String[] splitString(String inputString,String delimeter){
		return null!=inputString?StringUtils.splitByWholeSeparator(inputString,delimeter):null; 
		
	}
}
