package com.gp.cong.logisoft.bc.referenceDataManagement;

import java.util.Iterator;
import java.util.List;

import com.gp.cong.logisoft.domain.Warehouse;
import com.gp.cong.logisoft.domain.WarehouseTemp;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;

public class WareHouseTempBC {
	
	public WarehouseTemp getWareHouseDetails(String warehouseCode,String warehouseName,String city,String airCargo) throws Exception {
	WarehouseDAO warehouseDAO=new WarehouseDAO();
	WarehouseTemp warehouseTemp=new WarehouseTemp();
	List warehouseList=warehouseDAO.findForSearchWarehouse(warehouseCode,warehouseName,city,airCargo);
	if(warehouseList!=null && warehouseList.size()>0){
	 warehouseTemp=(WarehouseTemp)warehouseList.get(0);
	}
	return warehouseTemp;
	}
	
	
	public Warehouse getWareHouseAddress(String warehouseName) throws Exception {
	WarehouseDAO warehouseDAO=new WarehouseDAO();
	String wareHouseAddress="";
	Warehouse warehouse=new Warehouse();
	List warehouseList=warehouseDAO.findForWarehousenameAndAddress(
			warehouseName,wareHouseAddress);
	if(warehouseList!=null && warehouseList.size()>0){
		warehouse=(Warehouse)warehouseList.get(0);
	}
	return warehouse;
	}


}
