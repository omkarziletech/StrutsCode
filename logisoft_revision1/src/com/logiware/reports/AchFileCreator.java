package com.logiware.reports;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.BankDetails;
import com.logiware.hibernate.domain.AchSetUp;
import com.logiware.utils.AchSetUpUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Lakshminarayanan
 */
public class AchFileCreator {
    public String createAchFile(List<TransactionBean> achPaymentsList, BankDetails bankDetails) throws Exception {
        BufferedWriter writer = null;
        File file = null;
            AchSetUp achSetUp = bankDetails.getAchSetUp();
            if(null==achSetUp.getBatchSequence()){
                achSetUp.setBatchSequence(1);
            }
            String achFileName = LoadLogisoftProperties.getProperty("reportLocation") + "/Documents/ACH/" + bankDetails.getBankName()
                    + "/" + bankDetails.getBankAcctNo() + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
            file = new File(achFileName);
            if (!file.exists()) {
                file.mkdirs();
            }
            achFileName += "ach_" + StringUtils.leftPad("" + achSetUp.getBatchSequence(), 7, "0") + ".txt";
            file = new File(achFileName);
            writer = new BufferedWriter(new FileWriter(file));
            List<StringBuilder> lines = new ArrayList<StringBuilder>();
            Integer totalRecords = 0;

            //File Header Record
            StringBuilder fileHeader = new StringBuilder();
            fileHeader.append(achSetUp.getFileHeaderRecordTypeCode()); //File Header Record Type
            fileHeader.append(achSetUp.getPriorityCode()); //Priority Code
            fileHeader.append(StringUtils.leftPad(StringUtils.right(bankDetails.getBankRoutingNumber(),9), 10)); //Immediate Destination
            fileHeader.append(StringUtils.leftPad(StringUtils.right(achSetUp.getImmediateOrigin(),10), 10)); //Immediate Origin
            fileHeader.append(DateUtils.formatDate(new Date(), "yyMMdd")); //File Creation Date
            fileHeader.append(DateUtils.formatDate(new Date(), "HHmm")); //File Creation Time
            fileHeader.append(achSetUp.getFileIdModifier()); //File ID Modifier
            fileHeader.append(achSetUp.getRecordSize()); //Record Size
            fileHeader.append(achSetUp.getBlockingFactor()); //Blocking Factor
            fileHeader.append(achSetUp.getFormatCode()); //Format Code
            fileHeader.append(StringUtils.rightPad(StringUtils.upperCase(StringUtils.left(bankDetails.getBankName(), 23)), 23)); //Destination Name
            fileHeader.append(StringUtils.rightPad(StringUtils.left(achSetUp.getCompanyName(), 23), 23)); //Immediate Origin Name
            fileHeader.append(StringUtils.leftPad("", 8)); //Refrence Code
            lines.add(fileHeader);
            totalRecords++;

            Integer batchCount = 0;
            //Batch Header Record
            StringBuilder batchHeader = new StringBuilder();
            batchHeader.append(achSetUp.getBatchHeaderRecordTypeCode()); //Batch Header Record Type
            batchHeader.append(achSetUp.getServiceClassCode()); //Service Class Code
            batchHeader.append(StringUtils.rightPad(StringUtils.left(achSetUp.getCompanyName(), 16), 16)); //Company Name
            batchHeader.append(StringUtils.leftPad("", 20)); //Company Discretionay Data
            batchHeader.append(StringUtils.leftPad(StringUtils.right(achSetUp.getCompanyIdentification(),10),10)); //Company Identification
            batchHeader.append(achSetUp.getStandardEntryClass()); //Standard Entry Class
            batchHeader.append(StringUtils.rightPad(StringUtils.left(achSetUp.getCompanyEntryDescription(),10), 10)); //Company Entry Description
            //batchHeader.append(StringUtils.leftPad("", 6)); //Company Descriptive Date
            batchHeader.append(DateUtils.formatDate(new Date(), "yyMMdd")); //Company Descriptive Date
            batchHeader.append(DateUtils.formatDate(new Date(), "yyMMdd")); //Effective Entry Date
            batchHeader.append(StringUtils.leftPad("", 3)); //Settlement Date
            batchHeader.append(achSetUp.getOriginatorStatusCode()); //Originator Status Code
            batchHeader.append(StringUtils.leftPad(StringUtils.left(bankDetails.getBankRoutingNumber(), 8), 8)); //Originating DFI Identification
            batchHeader.append(StringUtils.leftPad("" + achSetUp.getBatchSequence(), 7, "0")); //Batch Number
            lines.add(batchHeader);
            batchCount++;
            totalRecords++;

            Double totalAmount = 0d;
            Integer detailCount = 0;
            Integer entryHash = 0;
            //Entry Detail Record
            for (TransactionBean achPayments : achPaymentsList) {
                String abaRoutingNo = new TradingPartnerDAO().getAbaRoutingNoByPayAcctNoandPayMethod(achPayments.getCustomerNo(), CommonConstants.PAYMENT_METHOD_ACH);
                String dfiAcctNo = new TradingPartnerDAO().getBankAcctNumberByPayAcctNoandPayMethod(achPayments.getCustomerNo(), CommonConstants.PAYMENT_METHOD_ACH);
                StringBuilder entryDetail = new StringBuilder();
                entryDetail.append(achSetUp.getEntryDetailRecordTypeCode()); //Entry Detail Recod Type
                entryDetail.append(achSetUp.getTransactionCode()); //Transaction Code
                entryDetail.append(StringUtils.leftPad(null != abaRoutingNo ? StringUtils.left(abaRoutingNo, 8) : "", 8)); //Receving DFI Identification
                entryDetail.append(StringUtils.leftPad(null != abaRoutingNo ? StringUtils.substring(abaRoutingNo, 8, 9) : "", 1)); //Check Digit
                entryDetail.append(StringUtils.rightPad(null != dfiAcctNo ? StringUtils.left(dfiAcctNo, 17) : "", 17)); //DFI Account Number
                Double amount = Double.parseDouble(achPayments.getAmount().replaceAll(",", ""));
                totalAmount += amount;
                entryDetail.append(NumberUtils.formatNumber(Double.parseDouble(achPayments.getAmount().replaceAll("[.,]", "")), "0000000000")); //Amount
                entryDetail.append(StringUtils.leftPad("0", 15,"0")); //Identification Number
                entryDetail.append(StringUtils.rightPad(StringUtils.left(achPayments.getCustomer().trim(), 22), 22)); //Receving Company Name
                entryDetail.append(StringUtils.leftPad("", 2)); //Discretionary Data
                entryDetail.append(achSetUp.getAddendaRecordIndicator()); //Addenda Record Indicator
                String traceNumber = StringUtils.leftPad(StringUtils.left(bankDetails.getBankRoutingNumber(), 8), 8, "0");
                traceNumber += StringUtils.leftPad("" + achSetUp.getBatchSequence(), 7, "0");
                entryDetail.append(traceNumber); //Trace Number
                lines.add(entryDetail);
                detailCount++;
                entryHash += null != abaRoutingNo ? Integer.parseInt(StringUtils.substring(abaRoutingNo, 0, 8)) : 0;
                totalRecords++;
            }

            //Batch Control Record
            StringBuilder batchControl = new StringBuilder();
            batchControl.append(AchSetUpUtils.getProperty("batchControlRecordTypeCode")); //Batch Control Record Type
            batchControl.append(achSetUp.getServiceClassCode()); //Service Class Code
            batchControl.append(StringUtils.leftPad("" + detailCount, 6,"0")); //Entry/Addenda Count
            batchControl.append(StringUtils.right(StringUtils.leftPad("" + entryHash, 10,"0"), 10)); //Entry Hash
            batchControl.append(StringUtils.leftPad("0", 12,"0")); //Total Debit Amount
            batchControl.append(StringUtils.leftPad(NumberUtils.formatNumber(totalAmount, "0000000000.00"), 12).replace(".", "")); //Total Credit Amount
            batchControl.append(StringUtils.leftPad(StringUtils.right(achSetUp.getCompanyIdentification(),10),10)); //Company Identification
            batchControl.append(StringUtils.leftPad("", 19)); //Message Authentication Code
            batchControl.append(StringUtils.leftPad("", 6)); //Reserved
            batchControl.append(StringUtils.leftPad(StringUtils.left(bankDetails.getBankRoutingNumber(), 8), 8)); //Originating DFI Identification
            batchControl.append(StringUtils.leftPad("" + achSetUp.getBatchSequence(), 7, "0")); //Batch Number
            lines.add(batchControl);
            totalRecords++;

            //File Control Record
            totalRecords++;
            StringBuilder fileControl = new StringBuilder();
            fileControl.append(AchSetUpUtils.getProperty("fileControlRecordTypeCode")); //File Control Record Type
            fileControl.append(StringUtils.leftPad("" + batchCount, 6,"0")); //Batch Count
            Integer blockCount = (totalRecords) / 10;
            Integer recordsToAdd = 10 - totalRecords % 10;
            if (recordsToAdd != 10) {
                blockCount++;
            }
            fileControl.append(StringUtils.leftPad("" + blockCount, 6,"0")); //Block Count
            fileControl.append(StringUtils.leftPad("" + detailCount, 8,"0")); //Entry/Addenda Count
            fileControl.append(StringUtils.right(StringUtils.leftPad("" + entryHash, 10,"0"), 10)); //Entry Hash
            fileControl.append(StringUtils.leftPad("0", 12,"0")); //Total Debit Amount
            fileControl.append(StringUtils.leftPad(NumberUtils.formatNumber(totalAmount, "0000000000.00"), 12).replace(".", "")); //Total Credit Amount
            fileControl.append(StringUtils.leftPad("", 39)); //Reserved
            lines.add(fileControl);
            if (recordsToAdd != 10) {
                for (int i = 0; i < recordsToAdd; i++) {
                    lines.add(new StringBuilder(StringUtils.repeat("9", 94)));
                }
            }
            IOUtils.writeLines(lines, "\r\n", writer);
            IOUtils.closeQuietly(writer);
            return achFileName;
        } 
    }