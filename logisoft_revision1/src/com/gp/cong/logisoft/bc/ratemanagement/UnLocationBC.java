package com.gp.cong.logisoft.bc.ratemanagement;

import com.gp.cong.common.CommonUtils;
import java.util.List;

import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;

public class UnLocationBC {
	 UnLocationDAO unLocationDAO=new UnLocationDAO();
	 UnLocation unLocation=new UnLocation();
	
	 	public UnLocation getUncodeDesc(String code,String codedesc) throws Exception {
		List terminalNumberList=unLocationDAO.findForManagement(code, codedesc);
		if(terminalNumberList!=null && terminalNumberList.size()>0){
			unLocation=(UnLocation)terminalNumberList.get(0);
		     }
		return unLocation;
	 	}
		public UnLocation getCountry(String city)throws Exception {
			List countryList = unLocationDAO.findCity(city);
			if(countryList!=null && countryList.size()>0){
				unLocation=(UnLocation)countryList.get(0);
			}
			return unLocation;
		}
		public String getCountryCode(String unlocCode) throws Exception {
                    String countryCode = "";
                    if(CommonUtils.isNotEmpty(unlocCode)){
                        return null != new UnLocationDAO().getUnlocation(unlocCode)?new UnLocationDAO().getUnlocation(unlocCode).getCountryId().getCode():"";
                    }
                    return countryCode;
                }
}
