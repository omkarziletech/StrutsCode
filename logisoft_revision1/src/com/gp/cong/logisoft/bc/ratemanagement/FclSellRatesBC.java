/**
 * 
 */
package com.gp.cong.logisoft.bc.ratemanagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import com.gp.cong.logisoft.domain.CarriersOrLine;
import com.gp.cong.logisoft.domain.CarriersOrLineTemp;
import com.gp.cong.logisoft.domain.CustomerTemp;
import com.gp.cong.logisoft.domain.FclBuy;
import com.gp.cong.logisoft.domain.FclBuyCost;
import com.gp.cong.logisoft.domain.FclBuyCostTypeRates;
import com.gp.cong.logisoft.domain.FclConsolidator;
import com.gp.cong.logisoft.domain.FclOrgDestMiscData;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.TradingPartnerTemp;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.hibernate.dao.CarriersOrLineDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerDAO;
import com.gp.cong.logisoft.hibernate.dao.FclBuyCostDAO;
import com.gp.cong.logisoft.hibernate.dao.FclBuyCostTypeRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.FclBuyDAO;
import com.gp.cong.logisoft.hibernate.dao.FclOrgDestMiscDataDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.SearchFCLForm;
import com.gp.cvst.logisoft.domain.ArInvoice;
import com.gp.cvst.logisoft.domain.ArinvoiceCharges;
import com.gp.cvst.logisoft.struts.form.ARInvoiceForm;

/**
 * @author Administrator
 *
 */
public class FclSellRatesBC {

	UnLocationDAO unLocationDAO=new UnLocationDAO();
	UnLocation unLocation=new UnLocation();
	GenericCodeDAO genericCodeDAO=new GenericCodeDAO();
	GenericCode genericCode = null;
	CustomerDAO carriersOrLineDAO= new CustomerDAO();
	TradingPartnerTemp carriersOrLineTemp = null;
	FclBuyDAO fclBuyDAO=new FclBuyDAO();
	FclBuyCostDAO fclBuyCostDAO=new FclBuyCostDAO();
	CustomerDAO customerDAO=new CustomerDAO();
	FclBuyCostTypeRatesDAO fclBuyCostTypeRatesDAO=new FclBuyCostTypeRatesDAO();
	FclOrgDestMiscDataDAO fclOrgDestMiscDataDAO = new FclOrgDestMiscDataDAO();
	Set set = new HashSet();
	List fclBuyList=new ArrayList();
	
	/**
	 * @param code
	 * @param codedesc
	 * @return
	 */
	public UnLocation getUncodeDesc(String code,String codedesc) throws Exception {
		
		List terminalNumberList=unLocationDAO.findForManagement(code, codedesc);
		if(terminalNumberList!=null && terminalNumberList.size()>0){
			unLocation=(UnLocation)terminalNumberList.get(0);
		}
		return unLocation;
	}
	
	public TradingPartnerTemp getSSLine(String accountNo) throws Exception {
		List tradingPartnerTempList=customerDAO.findAccountNo1(accountNo);
		return (TradingPartnerTemp)tradingPartnerTempList.get(0);
	}
	/**
	 * @param comCode
	 * @return
	 */
	public GenericCode getCommodityCode(String comCode) throws Exception {
		List commodityCodeList=genericCodeDAO.findForGenericCode(comCode);
		if(commodityCodeList!=null && commodityCodeList.size()>0){
			genericCode=(GenericCode)commodityCodeList.get(0);
		}
		return genericCode;
	}
	
	/**
	 * @param codeValue
	 * @param comCodedesc
	 * @return
	 */
	public GenericCode getCommodityCodeDescription(String codeValue ,String comCodedesc) throws Exception {
		List commodityCodeDescriptionList=genericCodeDAO.findForAirRates(null,comCodedesc);
		if(commodityCodeDescriptionList!=null && commodityCodeDescriptionList.size()>0) {
			genericCode=(GenericCode)commodityCodeDescriptionList.get(0);
		}
		return genericCode;
	}
	/**
	 * @param ssLineNo
	 * @return
	 */
	public TradingPartnerTemp getSslineNumber(String ssLineNo) throws Exception {
		List ssLineList = carriersOrLineDAO.findAccountNo1(ssLineNo);
		if(ssLineList!=null && ssLineList.size()>0){
			carriersOrLineTemp=(TradingPartnerTemp)ssLineList.get(0);
		}
		return carriersOrLineTemp;
	}
	
	/**
	 * @param ssLineId
	 * @param ssLineName
	 * @return
	 */
	public TradingPartnerTemp getSslineName( String ssLineId ,String ssLineName) throws Exception {
		List ssLineNameList = carriersOrLineDAO.findAccountName1(ssLineName);
		if(ssLineNameList!=null && ssLineNameList.size()>0) {
			carriersOrLineTemp=(TradingPartnerTemp)ssLineNameList.get(0);
		}
		return carriersOrLineTemp;
	}
	
	/**
	 * @return
	 */
	public List getUnitTypes() throws Exception {
		List<LabelValueBean> genericCodeList=new ArrayList<LabelValueBean>();
		Iterator iter=genericCodeDAO.getAllGenericCodesForDisplay(38,"yes");
		genericCodeList.add(new LabelValueBean("All","0"));
		while(iter.hasNext()) {
	    	Object[] row = (Object[]) iter.next();
			Integer cid = (Integer) row[0];
			String cname = (String) row[1];
			genericCodeList.add(new LabelValueBean(cname,cid.toString()));
	    }
		return genericCodeList;
	}
	
	public List getUnitTypesList() throws Exception {
		List<GenericCode> unitTypeList=new ArrayList<GenericCode>();
		unitTypeList=genericCodeDAO.getUnitTypesList();
		return unitTypeList;
	}
	
	public GenericCode getUnitType(String unitType) throws Exception {
		GenericCode genericCode=genericCodeDAO.findById(Integer.parseInt(unitType));
		return genericCode;
	}
	
	public List getConsolidatorList(MessageResources messageResources) {
		List<FclConsolidator> consolidatorList=new ArrayList<FclConsolidator>();
		String OFR=messageResources.getMessage("OceanFreight");
		String charges[]=OFR.split(",");
		for(int i=0;i<charges.length;i++){
			FclConsolidator fclConsolidator=new FclConsolidator();
			fclConsolidator.setCharge(charges[i]);
			fclConsolidator.setRollToCharge(charges[i]);
			fclConsolidator.setDisplay("Y");
			fclConsolidator.setExcludeFromTotal("N");
			consolidatorList.add(fclConsolidator);
		}
		
		return consolidatorList;
	}
	public List displayRecords(String originRegion,String destRegion,String  originTerminal,String destPort,String commodity,String ssline,String[] unitType) throws Exception {
		List searchList=fclBuyDAO.findForSearchFclBuyRatesMatch(originTerminal,destPort, commodity, ssline,originRegion,destRegion);
		
		
		for (int i = 0; i < searchList.size(); i++) {
			FclBuy fclBuy = (FclBuy) searchList.get(i);
			List newList = fclBuyCostDAO.findAllUsers(fclBuy.getFclStdId());
			for (int j = 0;j < newList.size(); j++) {
				FclBuyCost fclBuyCost = (FclBuyCost) newList.get(j);
				fclBuyCost.setOrgTerminalName(fclBuy.getOriginTerminal());
				fclBuyCost.setDestinationPortName(fclBuy.getDestinationPort());
				fclBuyCost.setCommodityCode(fclBuy.getComNum());
				fclBuyCost.setCommodityCodeDesc(fclBuy.getComNum().getCodedesc());
				fclBuyCost.setSsLineName(fclBuy.getSslineNo());
				if (fclBuyCost.getCostId() != null
						&& fclBuyCost.getCostId().getCode().equalsIgnoreCase("OFR")) {
					set = fclBuyCost.getFclBuyUnitTypesSet();
					Iterator it3 = set.iterator();
					while (it3.hasNext()) {
						fclBuyList.add(fclBuyCost);
						break;
					}
	
				}
	
			}
			for (int k = 0;k < newList.size(); k++) {
				FclBuyCost fclBuyCost = (FclBuyCost) newList.get(k);
				fclBuyCost.setOrgTerminalName(fclBuy.getOriginTerminal());
				fclBuyCost.setDestinationPortName(fclBuy.getDestinationPort());
				fclBuyCost.setCommodityCode(fclBuy.getComNum());
				fclBuyCost.setCommodityCodeDesc(fclBuy.getComNum().getCodedesc());
				fclBuyCost.setSsLineName(fclBuy.getSslineNo());
				if (fclBuyCost.getCostId() != null && fclBuyCost.getCostId().getCode().equalsIgnoreCase("OFR")) {
	
				} else {
					set = fclBuyCost.getFclBuyUnitTypesSet();
					Iterator it3 = set.iterator();
					while (it3.hasNext()) {
						fclBuyList.add(fclBuyCost);
						break;
					}
				}
			}
	
		}	
		List unitTypeList=new ArrayList();
		List fclUnitTypeList=new ArrayList();
		HashMap hashMap=new HashMap();
		String addedUnitType=",";
		for(int l=0;l<fclBuyList.size();l++)
		{
			FclBuyCost fclBuyCost = (FclBuyCost) fclBuyList.get(l);	
			fclBuyCost.setCostCode(fclBuyCost.getCostId().getCode());
			fclBuyCost.setCostType(fclBuyCost.getContType().getCode());
			List fclBuyCostTypeRatesList=fclBuyCostTypeRatesDAO.getFclBuyCostTypeRates(fclBuyCost.getFclCostId());
			if(fclBuyCostTypeRatesList!=null){
				Iterator iter=fclBuyCostTypeRatesList.iterator();
				unitTypeList=new ArrayList();
				while(iter.hasNext()){
					FclBuyCostTypeRates fclBuyCostTypeRates=(FclBuyCostTypeRates)iter.next();
					if(fclBuyCostTypeRates.getUnitType().getId()!=null ){
						fclBuyCost.setCurrency("");
						fclBuyCost.setRetail(null);
						
					if(fclBuyCostTypeRates.getActiveAmt()!=null && fclBuyCostTypeRates.getMarkup()!=null){
						fclBuyCostTypeRates.setUnitAmount(fclBuyCostTypeRates.getActiveAmt()+fclBuyCostTypeRates.getMarkup());
						fclBuyCostTypeRates.setUnitname(fclBuyCostTypeRates.getUnitType().getCodedesc());
					}
					
				
					boolean flag=false;
					for(int i=0;i<unitType.length;i++){
						String uType=unitType[i];
						if(uType.equals("0") || uType.equals(fclBuyCostTypeRates.getUnitType().getId().toString()))
						{
							if(addedUnitType.indexOf(","+fclBuyCostTypeRates.getUnitType().getCodedesc()+",")==-1){
								addedUnitType+=fclBuyCostTypeRates.getUnitType().getCodedesc()+",";
							}
							flag=true;
							break;
						}
					}
					if(flag){
						unitTypeList.add(fclBuyCostTypeRates);
					}
					
					}
					else if(fclBuyCostTypeRates.getCurrency()!=null && fclBuyCostTypeRates.getRatAmount()!=null)
					{
						fclBuyCost.setCurrency(fclBuyCostTypeRates.getCurrency().getCode());
						fclBuyCost.setRetail(fclBuyCostTypeRates.getRatAmount());
					}
				}
				
			}
			fclBuyCost.setUnitTypeList(unitTypeList);
	}
	for (Iterator iterator = fclBuyList.iterator(); iterator.hasNext();) {
		FclBuyCost fclBuyCost = (FclBuyCost) iterator.next();
		
		addEmptyUnitTypes(fclBuyCost,addedUnitType);
	}
	return 	fclBuyList;
	}
	
	
	
	public List displayRecordsForConsolidator(String originRegion,String destRegion,String  originTerminal,String destPort,String commodity,String ssline,String[] unitType,List fclconsolidatorList) throws Exception {
		
		Map<String,FclConsolidator> fclConsolidatorMap =  new HashMap<String,FclConsolidator>();
		if(fclconsolidatorList !=null && !fclconsolidatorList.isEmpty()){
			for (Iterator iterator = fclconsolidatorList.iterator(); iterator.hasNext();) {
				FclConsolidator fclConsolidator = (FclConsolidator) iterator.next();
				fclConsolidatorMap.put(fclConsolidator.getCharge(), fclConsolidator);
			}
		}
		Map<String,FclBuyCost> fclBuyCostMap =  new HashMap<String,FclBuyCost>();
		List<FclBuy> fclBuyList=fclBuyDAO.findForSearchFclBuyRatesMatch(originTerminal,destPort, commodity, ssline,originRegion,destRegion);
		
		List unitTypeList=new ArrayList();
		String addedUnitType=",";
		for (int i = 0; i < fclBuyList.size(); i++) {
			FclBuy fclBuy = (FclBuy) fclBuyList.get(i);
			List<FclBuyCost> fclBuyCostList = fclBuyCostDAO.findAllUsers(fclBuy.getFclStdId());
			for (int j = 0;j < fclBuyCostList.size(); j++) {
				FclBuyCost fclBuyCost = (FclBuyCost) fclBuyCostList.get(j);
				unitTypeList=new ArrayList();
				fclBuyCost.setOrgTerminalName(fclBuy.getOriginTerminal());
				fclBuyCost.setDestinationPortName(fclBuy.getDestinationPort());
				fclBuyCost.setCommodityCode(fclBuy.getComNum());
				fclBuyCost.setCommodityCodeDesc(fclBuy.getComNum().getCodedesc());
				fclBuyCost.setSsLineName(fclBuy.getSslineNo());
				fclBuyCost.setCostCode(fclBuyCost.getCostId().getCode());
				fclBuyCost.setCostType(fclBuyCost.getContType().getCode());
				fclBuyCost.setCurrency("");
				fclBuyCost.setRetail(null);
				fclBuyCost.setUnitTypeList(unitTypeList);
				
				FclConsolidator fclConsolidator = fclConsolidatorMap.get(fclBuyCost.getCostId().getCode());
				if(fclBuyCost.getCostType().trim().equalsIgnoreCase("A")){
				if(fclConsolidator!=null){
					if(fclConsolidator.getDisplay().equalsIgnoreCase("Y")){
					String key = fclBuy.getOriginTerminal().getId()+"_"+fclBuy.getDestinationPort().getId()
						+"_"+fclBuy.getComNum().getId()+"_"+fclBuy.getSslineNo().getAccountno()+"_"+fclConsolidator.getRollToCharge();
					
					if(fclBuyCostMap.get(key)!=null){
						FclBuyCost existingFclBuyCost = fclBuyCostMap.get(key);
						List existingFclBuyUnitTypeRates=fclBuyCostTypeRatesDAO.getFclBuyCostTypeRates(existingFclBuyCost.getFclCostId());
						List fclBuyUnitTypeRates=fclBuyCostTypeRatesDAO.getFclBuyCostTypeRates(fclBuyCost.getFclCostId());
						List<FclBuyCostTypeRates> mergeUnitTypesList = null; 
						
						if(fclBuyUnitTypeRates.size()>=existingFclBuyUnitTypeRates.size()){
							mergeUnitTypesList = mergeUnitTypesAmount(existingFclBuyCost.getFclBuyUnitTypesSet(),fclBuyCost.getFclBuyUnitTypesSet(),"N",unitType);
						}else{
							mergeUnitTypesList = mergeUnitTypesAmount(fclBuyCost.getFclBuyUnitTypesSet(),existingFclBuyCost.getFclBuyUnitTypesSet(),"N",unitType);
						}
						existingFclBuyCost.setUnitTypeList(mergeUnitTypesList);
						
					}else{
						List fclBuyUnitTypeRates=fclBuyCostTypeRatesDAO.getFclBuyCostTypeRates(fclBuyCost.getFclCostId());
						if(fclBuyUnitTypeRates!=null){
							unitTypeList=new ArrayList();
							
							for (Iterator iterator = fclBuyUnitTypeRates.iterator(); iterator.hasNext();) {
								if(fclConsolidator.getExcludeFromTotal().equalsIgnoreCase("N")){
									FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
									if(fclBuyCostTypeRates.getUnitType()!=null){
										if(fclBuyCostTypeRates.getActiveAmt()==null){
											fclBuyCostTypeRates.setActiveAmt(0.00);
										}
										if(fclBuyCostTypeRates.getMarkup()==null){
											fclBuyCostTypeRates.setMarkup(0.00);
										}
									fclBuyCostTypeRates.setUnitAmount(fclBuyCostTypeRates.getActiveAmt()+fclBuyCostTypeRates.getMarkup());
									
									fclBuyCostTypeRates.setUnitname(fclBuyCostTypeRates.getUnitType().getCodedesc());

									
									boolean flag=false;
									for(int utype=0;utype<unitType.length;utype++){
										String uType=unitType[utype];
										if(uType.equals("0") || uType.equals(fclBuyCostTypeRates.getUnitType().getId().toString())){
											flag=true;
											if(addedUnitType.indexOf(","+fclBuyCostTypeRates.getUnitType().getCodedesc()+",")==-1){
												addedUnitType+=fclBuyCostTypeRates.getUnitType().getCodedesc()+",";
											}
											break;
										}
									}
									if(flag){
							
										unitTypeList.add(fclBuyCostTypeRates);
										
									}
									}else{
										fclBuyCost.setCurrency(fclBuyCostTypeRates.getCurrency().getCode());
										if(fclBuyCostTypeRates.getMinimumAmt()!=null && !fclBuyCostTypeRates.getRatAmount().equals("0.00")){
											fclBuyCost.setRetail(fclBuyCostTypeRates.getMinimumAmt());
										}else{
										fclBuyCost.setRetail(fclBuyCostTypeRates.getRatAmount());
										}
									}
								}else{

									FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
									if(fclBuyCostTypeRates.getUnitType()!=null){
									fclBuyCostTypeRates.setUnitAmount(0.00);
									fclBuyCostTypeRates.setUnitname(fclBuyCostTypeRates.getUnitType().getCodedesc());

									
									boolean flag=false;
									for(int utype=0;utype<unitType.length;utype++){
										String uType=unitType[utype];
										if(uType.equals("0") || uType.equals(fclBuyCostTypeRates.getUnitType().getId().toString())){
											flag=true;
											if(addedUnitType.indexOf(","+fclBuyCostTypeRates.getUnitType().getCodedesc()+",")==-1){
												addedUnitType+=fclBuyCostTypeRates.getUnitType().getCodedesc()+",";
											}
											break;
										}
									}
									if(flag){
										unitTypeList.add(fclBuyCostTypeRates);
									}
									}else{
										fclBuyCost.setCurrency(fclBuyCostTypeRates.getCurrency().getCode());
										if(fclBuyCostTypeRates.getMinimumAmt()!=null && !fclBuyCostTypeRates.getRatAmount().equals("0.00")){
											fclBuyCost.setRetail(fclBuyCostTypeRates.getMinimumAmt());
										}else{
										fclBuyCost.setRetail(fclBuyCostTypeRates.getRatAmount());
										}
									}
								}
							}
						}
						fclBuyCost.setUnitTypeList(unitTypeList);
						fclBuyCostMap.put(key,fclBuyCost);
					}
					}
				}else{
					String key = fclBuy.getOriginTerminal().getId()+"_"+fclBuy.getDestinationPort().getId()
					+"_"+fclBuy.getComNum().getId()+"_"+fclBuy.getSslineNo().getAccountno()+"_"+"001";
				if(fclBuyCostMap.get(key)!=null){
					FclBuyCost existingFclBuyCost = fclBuyCostMap.get(key);
					List existingFclBuyUnitTypeRates=fclBuyCostTypeRatesDAO.getFclBuyCostTypeRates(existingFclBuyCost.getFclCostId());
					List fclBuyUnitTypeRates=fclBuyCostTypeRatesDAO.getFclBuyCostTypeRates(fclBuyCost.getFclCostId());
					List<FclBuyCostTypeRates> mergeUnitTypesList = null; 	
					if(fclBuyUnitTypeRates.size()>=existingFclBuyUnitTypeRates.size()){
						mergeUnitTypesList = mergeUnitTypesAmount(existingFclBuyCost.getFclBuyUnitTypesSet(),fclBuyCost.getFclBuyUnitTypesSet(),"N",unitType);
					}else{
						mergeUnitTypesList = mergeUnitTypesAmount(fclBuyCost.getFclBuyUnitTypesSet(),existingFclBuyCost.getFclBuyUnitTypesSet(),"N",unitType);
					}
					
					existingFclBuyCost.setUnitTypeList(mergeUnitTypesList);
					
				}else{
					
					List fclBuyUnitTypeRates=fclBuyCostTypeRatesDAO.getFclBuyCostTypeRates(fclBuyCost.getFclCostId());
					if(fclBuyUnitTypeRates!=null){
						unitTypeList=new ArrayList();
						
						for (Iterator iterator = fclBuyUnitTypeRates.iterator(); iterator.hasNext();) {
							
								FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
								if(fclBuyCostTypeRates.getUnitType()!=null){
									if(fclBuyCostTypeRates.getActiveAmt()==null){
										fclBuyCostTypeRates.setActiveAmt(0.00);
									}
									if(fclBuyCostTypeRates.getMarkup()==null){
										fclBuyCostTypeRates.setMarkup(0.00);
									}
								fclBuyCostTypeRates.setUnitAmount(fclBuyCostTypeRates.getActiveAmt()+fclBuyCostTypeRates.getMarkup());
								fclBuyCostTypeRates.setUnitname(fclBuyCostTypeRates.getUnitType().getCodedesc());
								
								
								boolean flag=false;
								for(int utype=0;utype<unitType.length;utype++){
									String uType=unitType[utype];
									if(uType.equals("0") || uType.equals(fclBuyCostTypeRates.getUnitType().getId().toString())){
										flag=true;
										if(addedUnitType.indexOf(","+fclBuyCostTypeRates.getUnitType().getCodedesc()+",")==-1){
											addedUnitType+=fclBuyCostTypeRates.getUnitType().getCodedesc()+",";
										}
										break;
									}
								}
								if(flag){
									unitTypeList.add(fclBuyCostTypeRates);
								}
								}else{
									fclBuyCost.setCurrency(fclBuyCostTypeRates.getCurrency().getCode());
									if(fclBuyCostTypeRates.getMinimumAmt()!=null && !fclBuyCostTypeRates.getRatAmount().equals("0.00")){
										fclBuyCost.setRetail(fclBuyCostTypeRates.getMinimumAmt());
									}else{
									fclBuyCost.setRetail(fclBuyCostTypeRates.getRatAmount());
									}
								}
						}
					}
					fclBuyCost.setUnitTypeList(unitTypeList);
					fclBuyCostMap.put(key,fclBuyCost);
				}
			
					
				}
			}else{
				
				String key = fclBuy.getOriginTerminal().getId()+"_"+fclBuy.getDestinationPort().getId()
				+"_"+fclBuy.getComNum().getId()+"_"+fclBuy.getSslineNo().getAccountno()+"_"+fclBuyCost.getCostCode();
				if(fclBuyCost.getFclBuyUnitTypesSet()!=null){
					Iterator iter=fclBuyCost.getFclBuyUnitTypesSet().iterator();
					while(iter.hasNext()){
						FclBuyCostTypeRates fclBuyCostTypeRates=(FclBuyCostTypeRates)iter.next();
						fclBuyCost.setCurrency(fclBuyCostTypeRates.getCurrency().getCode());
						
						if(fclBuyCostTypeRates.getMinimumAmt()!=null && !fclBuyCostTypeRates.getMinimumAmt().equals(0.0)){
							fclBuyCost.setRetail(fclBuyCostTypeRates.getMinimumAmt());
						}else{
						fclBuyCost.setRetail(fclBuyCostTypeRates.getRatAmount());
						}
					}
					fclBuyCostMap.put(key,fclBuyCost);
				}
				
			}
			}
		}
		
		 
		
		
		List<FclBuyCost> fclBuyCostList = new ArrayList<FclBuyCost>();
		for (Iterator iterator = fclBuyCostMap.keySet().iterator(); iterator.hasNext();) {
			String name = (String) iterator.next();
			FclBuyCost fclBuyCost = fclBuyCostMap.get(name); 
			
			addEmptyUnitTypes(fclBuyCost,addedUnitType);
			fclBuyCostList.add(fclBuyCost);
		}
		return fclBuyCostList;
		
	}
	
	
	/**
	 * @param fclBuyCost
	 * @param unitTypes
	 */
	
	private void addEmptyUnitTypes(FclBuyCost fclBuyCost,String unitTypes){
		String modifiedUnitTypes = new String(unitTypes);
		if(modifiedUnitTypes.startsWith(",")){
			modifiedUnitTypes = modifiedUnitTypes.substring(1); 
		}
		
		if(modifiedUnitTypes.endsWith(",")){
			modifiedUnitTypes = modifiedUnitTypes.substring(0,modifiedUnitTypes.length()-1); 
		}
		String[] unitTypeAry = modifiedUnitTypes.split(",");
		for (int i = 0; i < unitTypeAry.length; i++) {
			FclBuyCostTypeRates blankFclBuyCostTypeRates = new FclBuyCostTypeRates();
			blankFclBuyCostTypeRates.setUnitAmount(0d);
			blankFclBuyCostTypeRates.setCurrency(new GenericCode());
			blankFclBuyCostTypeRates.setUnitname(unitTypeAry[i]);
			boolean flag=false;
			for (Iterator iterator = fclBuyCost.getUnitTypeList().iterator(); iterator.hasNext();) {
				FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
				if(fclBuyCostTypeRates.getUnitname().equals(unitTypeAry[i])){
					flag=true;
					break;
				}
			}
			if(!flag){
			fclBuyCost.getUnitTypeList().add(blankFclBuyCostTypeRates);
			}
		}
		HashMap hashMap=new HashMap();
		List tempList=new ArrayList();
		List tempUnitTypeList=new ArrayList();
		List unitTypeList=fclBuyCost.getUnitTypeList();
		fclBuyCost.setUnitTypeList(new ArrayList());
		for (Iterator iterator = unitTypeList.iterator(); iterator.hasNext();) {
			FclBuyCostTypeRates tempFclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
			hashMap.put(tempFclBuyCostTypeRates.getUnitname(), tempFclBuyCostTypeRates);
			tempList.add(tempFclBuyCostTypeRates.getUnitname());
		}
		Collections.sort(tempList);
		for (int i=0;i<tempList.size();i++) {
			FclBuyCostTypeRates tempFclBuyCostTypeRates = (FclBuyCostTypeRates)hashMap.get(tempList.get(i));
			tempUnitTypeList.add(tempFclBuyCostTypeRates);
		}
		fclBuyCost.setUnitTypeList(tempUnitTypeList);
	}
	
	
	/**
	 * @param fclBuyUnitTypesSet1
	 * @param fclBuyUnitTypesSet12
	 * @param excludeFromTotal
	 * @return
	 */
	private List<FclBuyCostTypeRates> mergeUnitTypesAmount(Set fclBuyUnitTypesSet1, Set fclBuyUnitTypesSet12,String excludeFromTotal,String[] unitType){
		List <FclBuyCostTypeRates>mergeUnitTypeAmountList=new ArrayList<FclBuyCostTypeRates>();
		Map<String,FclBuyCostTypeRates> fclConsolidatorMap =  new HashMap<String,FclBuyCostTypeRates>();
		
		for (Iterator iterator = fclBuyUnitTypesSet1.iterator(); iterator.hasNext();) {
			FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
			if(fclBuyCostTypeRates.getUnitType()!=null){
				fclConsolidatorMap.put(fclBuyCostTypeRates.getUnitType().getId().toString(), fclBuyCostTypeRates);
			}
			
		}
		for (Iterator iterator = fclBuyUnitTypesSet12.iterator(); iterator.hasNext();) {
			FclBuyCostTypeRates fclBuyCostTypeRates = (FclBuyCostTypeRates) iterator.next();
			String key=fclBuyCostTypeRates.getUnitType().getId().toString();
			if(fclConsolidatorMap.get(key)!=null){
				FclBuyCostTypeRates mergefclBuyCostTypeRates=fclConsolidatorMap.get(key);
				if(excludeFromTotal.equalsIgnoreCase("N")){
					if(mergefclBuyCostTypeRates.getActiveAmt()==null){
						mergefclBuyCostTypeRates.setActiveAmt(0.00);
					}
					if(mergefclBuyCostTypeRates.getMarkup()==null){
						mergefclBuyCostTypeRates.setMarkup(0.00);
					}
					if(fclBuyCostTypeRates.getActiveAmt()==null){
						fclBuyCostTypeRates.setActiveAmt(0.00);
					}
					if(fclBuyCostTypeRates.getMarkup()==null){
						fclBuyCostTypeRates.setMarkup(0.00);
					}
					if(mergefclBuyCostTypeRates.getUnitAmount()==null || mergefclBuyCostTypeRates.getUnitAmount().equals(0.0)){
						mergefclBuyCostTypeRates.setUnitAmount(mergefclBuyCostTypeRates.getActiveAmt()+mergefclBuyCostTypeRates.getMarkup());
					}
				
					mergefclBuyCostTypeRates.setUnitAmount(mergefclBuyCostTypeRates.getUnitAmount()+fclBuyCostTypeRates.getActiveAmt()+fclBuyCostTypeRates.getMarkup());
				}
				mergefclBuyCostTypeRates.setUnitname(mergefclBuyCostTypeRates.getUnitType().getCodedesc());
				boolean flag=false;
				for(int utype=0;utype<unitType.length;utype++){
					String uType=unitType[utype];
					if(uType.equals("0") || uType.equals(fclBuyCostTypeRates.getUnitType().getId().toString())){
						flag=true;
						
						break;
					}
				}
				if(flag){
					mergeUnitTypeAmountList.add(mergefclBuyCostTypeRates);
				}
				fclConsolidatorMap.remove(mergefclBuyCostTypeRates);
			}else{
				if(excludeFromTotal.equalsIgnoreCase("N")){
					if(fclBuyCostTypeRates.getActiveAmt()==null){
						fclBuyCostTypeRates.setActiveAmt(0.00);
					}
					if(fclBuyCostTypeRates.getMarkup()==null){
						fclBuyCostTypeRates.setMarkup(0.00);
					}
					fclBuyCostTypeRates.setUnitAmount(fclBuyCostTypeRates.getActiveAmt()+fclBuyCostTypeRates.getMarkup());
				}else{
					fclBuyCostTypeRates.setUnitAmount(0.00);
				}
				
					fclBuyCostTypeRates.setUnitname(fclBuyCostTypeRates.getUnitType().getCodedesc());
					boolean flag=false;
					for(int utype=0;utype<unitType.length;utype++){
						String uType=unitType[utype];
						if(uType.equals("0") || uType.equals(fclBuyCostTypeRates.getUnitType().getId().toString())){
							flag=true;
							break;
						}
					}
					if(flag){
						mergeUnitTypeAmountList.add(fclBuyCostTypeRates);
						
					}
			}
		}
		return mergeUnitTypeAmountList;
		
	}
	
	
	
	public void save(FclOrgDestMiscData fclOrgDestMiscData)
	 throws Exception {
		fclOrgDestMiscDataDAO.save(fclOrgDestMiscData);
	}
	public List getorgdestmiscdate(UnLocation orgTerminal,UnLocation dest,TradingPartnerTemp tradingPartnerTemp)
	 throws Exception {
		List orgdestmiscdatelist=fclOrgDestMiscDataDAO.getorgdestmiscdate(orgTerminal, dest,tradingPartnerTemp);
		return orgdestmiscdatelist;
	}
}
