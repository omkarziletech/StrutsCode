package com.gp.cong.logisoft.util;

import java.util.ArrayList;
import java.util.List;

import com.gp.cong.logisoft.utilities.CommentsForm;
import com.gp.cong.logisoft.utilities.TempDomain;
import com.gp.cvst.logisoft.domain.BookingfclUnits;
import com.gp.cvst.logisoft.domain.Charges;
import com.gp.cvst.logisoft.domain.FclBlCharges;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.hibernate.dao.BookingfclUnitsDAO;
import com.gp.cvst.logisoft.hibernate.dao.ChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO;

public class UtilitiesBC {
	ChargesDAO chargesDAO=new ChargesDAO();
	BookingfclUnitsDAO bookingfclUnitsDAO=new BookingfclUnitsDAO();
	FclBlChargesDAO fclBlChargesDAO =  new FclBlChargesDAO();
	FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
	public List getComments(CommentsForm commentsForm)throws Exception {
		 String fileNo=commentsForm.getFileNo();
		 String action=commentsForm.getAction();
		 List<Object[]> list =new ArrayList();
		 List<Object[]> commentsList =new ArrayList();
		 if(null!=action && action.equalsIgnoreCase("All")){
			 commentsList=setIntoList(chargesDAO.getAllCharges(fileNo),UtilityConstant.QUOTE);
			 if(CommonFunctions.isNotNullOrNotEmpty(commentsList)){
				 list.addAll(commentsList); 
			 }
			 commentsList=setIntoList(bookingfclUnitsDAO.getAllCharges(fileNo),UtilityConstant.BOOKING);
			 if(CommonFunctions.isNotNullOrNotEmpty(commentsList)){
				 list.addAll(commentsList); 
			 }
			 commentsList=setIntoList(fclBlChargesDAO.getAllCharges(fileNo),UtilityConstant.BLCHARGES);
			 if(CommonFunctions.isNotNullOrNotEmpty(commentsList)){
				 list.addAll(commentsList); 
			 }
			 commentsList=setIntoList(fclBlCostCodesDAO.getAllCharges(fileNo),UtilityConstant.BLCOSTCODE);
			 if(CommonFunctions.isNotNullOrNotEmpty(commentsList)){
				 list.addAll(commentsList); 
			 }
		 }else if(null!=action && action.equalsIgnoreCase("Quote")){
			 list =setIntoList(chargesDAO.getAllCharges(fileNo),UtilityConstant.QUOTE);
		 }else if(null!=action && action.equalsIgnoreCase("Booking")){
			 list =setIntoList(bookingfclUnitsDAO.getAllCharges(fileNo),UtilityConstant.BOOKING);
		 }else  if(null!=action && action.equalsIgnoreCase("FclBlCharges")){
			 list =setIntoList(fclBlChargesDAO.getAllCharges(fileNo),UtilityConstant.BLCHARGES);
		 }else  if(null!=action && action.equalsIgnoreCase("FclBlCostCode")){
			 list =setIntoList(fclBlCostCodesDAO.getAllCharges(fileNo),UtilityConstant.BLCOSTCODE);
		 }
		 return list;
	}
	public List setIntoList(List<Object[]> list,String module)throws Exception {
		List<TempDomain> list2 = new ArrayList();
		for(Object[] objects:list){
			  if(CommonFunctions.isNotNull((String)objects[0])){
				   TempDomain tempDomain = new TempDomain();
					tempDomain.setChargesCode((String)objects[2]);
					tempDomain.setChargesId((Integer)objects[1]);
					tempDomain.setComments((String)objects[0]);
					tempDomain.setModule(module);
					list2.add(tempDomain);  
			  }
			}
		return list2;
		}
	public void deleteCharges(CommentsForm commentsForm)throws Exception {
		 Integer chargeId=CommonFunctions.isNotNull(commentsForm.getChargeId())?
				 new Integer(commentsForm.getChargeId()):0;
		 String action=commentsForm.getModule();
		 List<Object[]> list =null;
		 if(null!=action && action.equalsIgnoreCase("Quote")){
			 Charges charges=chargesDAO.findById(chargeId);
			charges.setComment(null);
		 }else if(null!=action && action.equalsIgnoreCase("Booking")){
			BookingfclUnits bookingfclUnits =bookingfclUnitsDAO.findById(chargeId);
			bookingfclUnits.setComment(null);
		 }else  if(null!=action && action.equalsIgnoreCase("FclBlCharges")){
			 FclBlCharges fclBlCharges =fclBlChargesDAO.findById(chargeId);
			 fclBlCharges.setChargesRemarks(null);
		 }else  if(null!=action && action.equalsIgnoreCase("FclBlCostCode")){
			 FclBlCostCodes fclBlCostCodes =fclBlCostCodesDAO.findById(chargeId);
			 fclBlCostCodes.setCostComments(null);
		 }
	}
	
	// constant:---------------
	public static String LIST="commentsList";
	public static String COMMENTSFORM="commentsForm";
	
}
