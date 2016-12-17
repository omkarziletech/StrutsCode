package com.gp.cong.logisoft.bc.ratemanagement;

import java.util.List;

import com.gp.cong.logisoft.domain.CarriersOrLineTemp;
import com.gp.cong.logisoft.hibernate.dao.CarriersOrLineDAO;

public class CarriersOrLineBC {
	CarriersOrLineDAO carriersOrLineDAO= new CarriersOrLineDAO();
	CarriersOrLineTemp carriersOrLineTemp = null;
	public CarriersOrLineTemp getSslineNumber(String ssLineNo) throws Exception {
		List ssLineList = carriersOrLineDAO.findForSSLine1(ssLineNo);
		if(ssLineList!=null && ssLineList.size()>0){
			carriersOrLineTemp=(CarriersOrLineTemp)ssLineList.get(0);
		}
		return carriersOrLineTemp;
	}
	
	/**
	 * @param ssLineId
	 * @param ssLineName
	 * @return
	 */
	public CarriersOrLineTemp getSslineName( String ssLineId ,String ssLineName) throws Exception {
		List ssLineNameList = carriersOrLineDAO.findForFclSSLine("", ssLineName);
		if(ssLineNameList!=null && ssLineNameList.size()>0) {
			carriersOrLineTemp=(CarriersOrLineTemp)ssLineNameList.get(0);
		}
		return carriersOrLineTemp;
	}

}
