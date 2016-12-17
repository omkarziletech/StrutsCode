package com.logiware.edi.tracking;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.util.EdiUtil;
import com.gp.cong.struts.LoadLogisoftProperties;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EdiTrackingSystemBC {
    EdiTrackingSystemDAO logFileEdiDAO = new EdiTrackingSystemDAO();
	 public String getEdiList(String drNumber)throws Exception{
             String status =logFileEdiDAO.findDrNumberStatus(drNumber);
             return status;
	 }
	 public void setEdiLog(String filename,String processedDate,String status,String description,String ediCompany,String messageType,String drNumber)throws Exception{
            EdiTrackingSystem logFileEdi = new EdiTrackingSystem();
            logFileEdi.setFilename(null != filename?filename:"");
            logFileEdi.setProcessedDate(null != processedDate?processedDate:"");
            logFileEdi.setStatus(null != status?status:"");
            logFileEdi.setDescription(null != description?description:"");
            logFileEdi.setEdiCompany(null != ediCompany?ediCompany:"");
            logFileEdi.setMessageType(null != messageType?messageType:"");
            logFileEdi.setDrnumber(null != drNumber?drNumber:"");
            logFileEdiDAO.saveLogFileEdi(logFileEdi);
        }
	 public void setShipmentStatusLog(String filename,String processedDate,String status,String description,String ediCompany,String messageType,String drNumber,String trackingStatus)throws Exception{
            EdiTrackingSystem logFileEdi = new EdiTrackingSystem();
            logFileEdi.setFilename(null != filename?filename:"");
            logFileEdi.setProcessedDate(null != processedDate?processedDate:"");
            logFileEdi.setStatus(null != status?status:"");
            logFileEdi.setDescription(null != description?description:"");
            logFileEdi.setEdiCompany(null != ediCompany?ediCompany:"");
            logFileEdi.setMessageType(null != messageType?messageType:"");
            logFileEdi.setDrnumber(null != drNumber?drNumber:"");
            logFileEdi.setDrnumber(null != drNumber?drNumber:"");
            logFileEdi.setTrackingStatus(null != trackingStatus?trackingStatus:"");
            logFileEdiDAO.saveLogFileEdi(logFileEdi);
        }
         public String exportEdiTracking(EdiTrackingSystemForm ediTrackingForm) throws Exception {
             List ediFileList = new ArrayList();
             DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
             String fromDate = ediTrackingForm.getFromDate();
                String toDate = ediTrackingForm.getToDate();
                String fromDateStr="";
                String toDateStr="";
                if(null != fromDate && null != toDate){
                        fromDateStr = DateUtils.formatDate(dateFormat.parse(fromDate), "yyyyMMdd");
                        toDateStr = DateUtils.formatDate(dateFormat.parse(toDate), "yyyyMMdd");
                }
    		List<EdiSystemBean> ediAckList = new ArrayList<EdiSystemBean>();
		ediFileList=logFileEdiDAO.findAllEdi(ediTrackingForm.getDrNumber(), ediTrackingForm.getMessageType(),
                    ediTrackingForm.getEdiCompany(),ediTrackingForm.getPlaceOfReceipt(),ediTrackingForm.getPlaceOfDelivery(),
                    ediTrackingForm.getPortOfLoad(),ediTrackingForm.getPortOfDischarge(),ediTrackingForm.getBookingNo(),
                            fromDateStr,toDateStr);
                    ediAckList = new EdiUtil().getEdiTrackingBeanList(ediFileList);
		String fileName = "EdiTracking.xls";
		String folderPath = LoadLogisoftProperties.getProperty("reportLocation");
		File file = new File(folderPath);
		if (!file.exists()) {
			file.mkdir();
		}
		String excelFilePath = LoadLogisoftProperties.getProperty("reportLocation") + "/" + fileName;
		new ExportEdiTrackingToExcel().exportToExcel(excelFilePath, ediAckList);
		return excelFilePath;
	}
}
