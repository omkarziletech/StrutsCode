package com.gp.cong.logisoft.edi.dbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.gp.cong.logisoft.edi.inttra.SQLStatements;
import com.gp.cong.logisoft.domain.EdiAck;

public class SQLFunctions {
	
	public void logFileData(String filename,String systemdate,String status,String description,String company,String messageType,String drNumber)throws Exception{
		PreparedStatement pstmt=null;
		Connection conn=new DBUtil().getLogisoftConnection();
		      pstmt = conn.prepareStatement(SQLStatements.UPDATE_LOG); // create a statement
		      pstmt.setString(1, filename); // set input parameter 1
		      pstmt.setString(2, systemdate); // set input parameter 2
		      pstmt.setString(3, status); // set input parameter 3
		      pstmt.setString(4, description); // set input parameter 4
		      pstmt.setString(5, company); // set input parameter 5
		      pstmt.setString(6, messageType); // set input parameter 6
		      pstmt.setString(7, drNumber);
		      pstmt.executeUpdate();
			}
	
	public void databaseWrite(EdiAck ed)throws Exception{
			Connection conn=new DBUtil().getLogisoftConnection();
			PreparedStatement pst = conn.prepareStatement(SQLStatements.ACK_ID);
			pst.setString(1, ed.getFileName());
			ResultSet rst = pst.executeQuery();
			Integer si_id=0;
			if(rst.next()){
					si_id = Integer.parseInt(rst.getString(1));
			}
			  PreparedStatement pstmt = conn.prepareStatement(SQLStatements.ACK_LOG);
		      pstmt.setString(1, ed.getQuoteTerm()); 
		      pstmt.setString(2, ed.getQuoteDr()); 
		      pstmt.setString(3, ed.getFileName()); 
		      pstmt.setString(4, ed.getControlSequenceNumber()); 
		      pstmt.setString(5, ed.getEdiCompanyIOrG()); 
		      pstmt.setString(6, ed.getAckReceivedDate()); 
		      pstmt.setString(7, ed.getAckCreatedTimeStamp()); 
		      pstmt.setString(8, ed.getShipmentId());
		      pstmt.setString(9, ed.getShippingInstructionAuditNumber()); 
		      pstmt.setString(10, ed.getSeverity()); 
		      pstmt.setString(11, ed.getDetailCommentsInAckMessage()); 
		      pstmt.setString(12, ed.getBookingNumber());
		      pstmt.setString(13, ed.getBillOfLadingNumber()); 
		      pstmt.setString(14, ed.getCarrierScac()); 
		      pstmt.setString(15, ed.getDivisionCode()); 
		      pstmt.setString(16, ed.getForwarderReferenceNumber());
		      pstmt.setString(17, ed.getConsigneeOrderNumber()); 
		      pstmt.setString(18, ed.getPurchaseOrderNumber()); 
		      pstmt.setString(19, ed.getContractNumber());
		      pstmt.setString(20, ed.getExportReferenceNumber()); 
		      pstmt.setString(21, ed.getBrokerReferenceNumber()); 
		      pstmt.setString(22, ed.getCustomerOrderNumber()); 
		      pstmt.setString(23, ed.getFederalMaritimeComNumber());
		      pstmt.setString(24, ed.getInvoiceNumber()); 
		      pstmt.setString(25, ed.getTransactionReferenceNumber()); 
		      pstmt.setString(26, ed.getDetailAuditId());
		      pstmt.setInt(27, si_id);
		      pstmt.executeUpdate();
			}
}
