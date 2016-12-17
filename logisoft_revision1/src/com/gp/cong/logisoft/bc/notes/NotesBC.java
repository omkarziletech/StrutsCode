package com.gp.cong.logisoft.bc.notes;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import java.util.List;

import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.struts.form.NotesForm;
import com.gp.cvst.logisoft.domain.BookingfclUnits;
import com.gp.cvst.logisoft.domain.Charges;
import com.gp.cvst.logisoft.domain.FclBlCharges;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.logiware.hibernate.domain.PaymentRelease;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * @author user
 * Creates by Gayatri 1/6/2009..
 *
 */
public class NotesBC {

    NotesDAO notesDAO = new NotesDAO();
    NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");

    /**
     * @param notesForm
     * @param loginName
     * @return
     */
    public Notes saveNotes(NotesForm notesForm, String loginName) throws Exception {
        Notes notes = new Notes(notesForm);
        notes.setUpdatedBy(loginName);
        notesDAO.save(notes);
        return notes;
    }
    
    /**
     * @param moduleId
     * @return
     */
    public List getNotes(String moduleId, String moduleRefId, String itemName) throws Exception {
        List notesList = new ArrayList();
        List accrList = new ArrayList();
        notesList = notesDAO.findNotesByModuleId(moduleId, moduleRefId, itemName);
        if(!"100018".equals(itemName)){
            accrList = notesDAO.getAccrualNotes(moduleRefId);
            if(null !=accrList && !accrList.isEmpty()){
                notesList.addAll(accrList);
                Collections.sort(notesList, new Notes());
            }
        }
        return notesList;
    }

    public List getAccrualNotes(String accrualsRefId, String invoiceRefId,String costRefId) throws Exception {
        return notesDAO.getAccrualNotes(accrualsRefId, invoiceRefId,costRefId);
    }
    public List getEventNotes(String moduleId, String moduleRefId, String noteType) throws Exception {
        return notesDAO.findEventNotes(moduleId, moduleRefId, noteType);
    }

    /**
     * @param notesForm
     */
    public void deleteNotes(NotesForm notesForm) throws Exception {
        Notes notes = new Notes(notesForm);
        notes = notesDAO.findById(notes.getId());
        notesDAO.delete(notes);

    }

    /**
     * Set void note
     * @param noteId
     */
    public void setAsVoid(Integer noteId) throws Exception {
        notesDAO.setASVoid(noteId);

    }

    public void setAsClosed(Integer noteId) throws Exception {
        notesDAO.setASClosed(noteId);

    }

    /**
     * @param moduleId
     * @param moduleRefId
     * @return
     */
    public List getVoidNotes(String moduleId, String moduleRefId, String itemName) throws Exception {
        return notesDAO.findVoidNotesByModuleId(moduleId, moduleRefId, itemName);
    }

    public List getByNoteType(String moduleId, String moduleRefId, String noteType, String itemName)  throws Exception {
        return notesDAO.findByNoteType(moduleId, moduleRefId, noteType, itemName);
    }

    public List findPastFollowupDateNotes(String moduleId, String moduleRefId, String itemName) throws Exception {
        return notesDAO.findPastFollowupDateNotes(moduleId, moduleRefId, itemName);
    }

    public List findExistsFollowupDateNotes(String moduleId, String moduleRefId, String itemName) throws Exception {
        return notesDAO.findExistsFollowupDateNotes(moduleId, moduleRefId, itemName);
    }

    public int getcountOfNotes(String moduleId, String moduleRefId) throws Exception {
        return notesDAO.getNotesCount(moduleId, moduleRefId);
    }

    public void saveNotes(Notes notes) throws Exception {
        notesDAO.save(notes);
    }

    public List getDeletedNotesForCorrections(String moduleId, String moduleRefId, String noteDesc) throws Exception {
        return notesDAO.getAllDeletedNotes(moduleId, moduleRefId, noteDesc);
    }
    public void saveNotesWhileAddingCharges(String module,String fileNo,String user,Object chargeCodeObject) throws Exception{
         StringBuilder message=new StringBuilder();
        if(chargeCodeObject instanceof Charges){
        Charges charges=(Charges)chargeCodeObject;
             message.append("INSERTED -> Charge Code - ").append(charges.getChargeCodeDesc()).append(" Cost Type - ").append(charges.getCostType()).append(" Cost - ");
             message.append(numberFormat.format(charges.getAmount())).append(" Sell -").append(numberFormat.format(charges.getMarkUp())).append(" Currency - ").append(charges.getCurrecny()).append(" Vendor Name - ").append(charges.getAccountName()).append(" Vendor Number -").append(charges.getAccountNo()).append(" Comment -").append(charges.getComment());
             if(charges.getUnitType()!=null){
                 message.append("Unit Type -").append(charges.getUnitName());
             }
        }else if(chargeCodeObject instanceof BookingfclUnits){
            BookingfclUnits bookingfclUnits=(BookingfclUnits)chargeCodeObject;
            message.append("INSERTED -> Charge Code - ").append(bookingfclUnits.getChargeCodeDesc()).append(" Cost Type - ").append(bookingfclUnits.getCostType()).append(" Cost - ");
            message.append(numberFormat.format(bookingfclUnits.getAmount()));
            if(null != bookingfclUnits.getMarkUp()){
                message.append(" Sell -").append(numberFormat.format(bookingfclUnits.getMarkUp())).append(" Currency - ");
            }
            message.append(bookingfclUnits.getCurrency()).append(" Vendor Name -").append(bookingfclUnits.getAccountName()).append(" Vendor Number -").append(bookingfclUnits.getAccountNo()).append(" Comment -").append(bookingfclUnits.getComment());
            if(bookingfclUnits.getUnitType()!=null){
                message.append("Unit Type -").append(bookingfclUnits.getUnitType().getCodedesc());
            }
             }
         Notes note = new Notes();
         note.setModuleId(NotesConstants.FILE);
         note.setModuleRefId(fileNo);
         note.setNoteType(NotesConstants.NOTES_TYPE_EVENT);
         note.setUpdateDate(new Date());
         note.setUpdatedBy(user);
         note.setNoteDesc(message.toString());
         saveNotes(note);
    }
    public void saveNotesWhileAddingFclBlCostCodes(FclBlCostCodes fclBlCostCodes,String userName) throws Exception {
        StringBuilder message = new StringBuilder();
        message.append("INSERTED ->Cost Code - ").append(fclBlCostCodes.getCostCodeDesc()).append(" Cost - ");
        message.append(numberFormat.format(fclBlCostCodes.getAmount())).append(" Currency - ").append(fclBlCostCodes.getCurrencyCode());
        message.append(" Vendor Name - ").append(fclBlCostCodes.getAccName());
        message.append(" Vendor Number - ").append(fclBlCostCodes.getAccNo());
        if(CommonUtils.isNotEmpty(fclBlCostCodes.getCostComments())){
            message.append(" Comment -").append(fclBlCostCodes.getCostComments());
        }
        Notes note = new Notes();
        note.setModuleId(NotesConstants.FILE);
        String fileNo = "";
        if(null != fclBlCostCodes.getbookingId()){
            fileNo = new BookingFclDAO().getFileNo(fclBlCostCodes.getbookingId().toString());
        }else if(null != fclBlCostCodes.getBolId() ){
           String bolId = fclBlCostCodes.getBolId().toString();
           fileNo = new FclBlDAO().getFileNo(bolId);
        }
        note.setModuleRefId(fileNo);
        note.setUniqueId(""+fclBlCostCodes.getCodeId());
        note.setUpdateDate(new Date());
        note.setUpdatedBy(userName);
        note.setNoteType(NotesConstants.AUTO);
        note.setNoteDesc(message.toString());
        saveNotes(note);
    }
    public void saveNotesWhileAddingFclBlCharges(FclBlCharges fclBlCharges,String userName) throws Exception {
        StringBuilder message=new StringBuilder();
        message.append("INSERTED ->Charge Code - ").append(fclBlCharges.getChargeCode()).append(" Cost - ");
        message.append(numberFormat.format(fclBlCharges.getAmount())).append(" Currency - ").append(fclBlCharges.getCurrencyCode());
        if(CommonUtils.isNotEmpty(fclBlCharges.getChargesRemarks())){
            message.append(" Comment -").append(fclBlCharges.getChargesRemarks());
        }
        Notes note = new Notes();
        note.setModuleId(NotesConstants.FILE);
        String fileNo = "";
        if(null != fclBlCharges.getBolId()){
           String bolId = fclBlCharges.getBolId().toString();
           fileNo = new FclBlDAO().getFileNo(bolId);
        }
        note.setModuleRefId(fileNo);
        note.setUpdateDate(new Date());
        note.setUpdatedBy(userName);
        note.setNoteType(NotesConstants.AUTO);
        note.setNoteDesc(message.toString());
        saveNotes(note);
    }
    public void saveNotesWhileAddingPaymentCharges(String fileNo,String user,PaymentRelease paymentRelease,String action) throws Exception {
         StringBuilder message=new StringBuilder();
         if(null != paymentRelease){
             if(paymentRelease.getAmount() > 0){
                 message.append("Payment ").append(action).append("-> Amount - ").append(numberFormat.format(paymentRelease.getAmount()));
             }
             if(CommonUtils.isNotEmpty(paymentRelease.getCheckNumber())){
                 message.append(" Check Number - ").append(paymentRelease.getCheckNumber());
             }
             if(CommonUtils.isNotEmpty(paymentRelease.getPaidBy())){
                 message.append(" Paid By - ").append(paymentRelease.getPaidBy());
             }
             if(null != paymentRelease.getPaidDate()){
                 message.append(" Paid Date - ").append(DateUtils.formatDate(paymentRelease.getPaidDate(),"dd-MMM-yyyy"));
             }
             Notes note = new Notes();
             note.setModuleId(NotesConstants.FILE);
             note.setModuleRefId(fileNo);
             note.setNoteType(NotesConstants.NOTES_TYPE_EVENT);
             note.setUpdateDate(new Date());
             note.setUpdatedBy(user);
             note.setNoteDesc(message.toString());
             saveNotes(note);
         }
    }
    public void saveNotesWhileAddingPaymentRelease(String fileNo,String user,String noteDesc) throws Exception {
         Notes note = new Notes();
         note.setModuleId(NotesConstants.FILE);
         note.setModuleRefId(fileNo);
         note.setNoteType(NotesConstants.NOTES_TYPE_AUTO);
         note.setUpdateDate(new Date());
         note.setUpdatedBy(user);
         note.setNoteDesc(noteDesc);
         saveNotes(note);
    }
    public void saveNotes(String fileNo,String user,String noteDesc) throws Exception {
         Notes note = new Notes();
         note.setModuleId(NotesConstants.FILE);
         note.setModuleRefId(fileNo);
         note.setNoteType(NotesConstants.NOTES_TYPE_AUTO);
         note.setUpdateDate(new Date());
         note.setUpdatedBy(user);
         note.setNoteDesc(noteDesc);
         saveNotes(note);
    }
    public void saveNotesWhileTransferCost(String fileNo,String user,String noteDesc) throws Exception {
         Notes note = new Notes();
         note.setModuleId(NotesConstants.FILE);
         note.setModuleRefId(fileNo);
         note.setNoteType(NotesConstants.NOTES_TYPE_AUTO);
         note.setUpdateDate(new Date());
         note.setUpdatedBy(user);
         note.setNoteDesc(noteDesc);
         saveNotes(note);
    }
    public void createEdiStatusNotes(String status,String fileName) throws Exception {
        Date date = new Date();
        Notes notes = new Notes();
        notes.setModuleId(NotesConstants.FILE);
        notes.setModuleRefId(null != fileName && fileName.length()>2?fileName.substring(2,fileName.length()):"");
        notes.setNoteType(NotesConstants.NOTES_TYPE_EVENT);
        notes.setUpdateDate(date);
        notes.setUpdatedBy("System");
        notes.setNoteDesc(status);
        notes.setItemName("100018");
        saveNotes(notes);
    }
    public void ackReceivedNotes(String fileName) throws Exception {
         Date date = new Date();
        Notes notes = new Notes();
        notes.setModuleId(NotesConstants.FILE);
        notes.setModuleRefId(null != fileName ?fileName:"");
        notes.setNoteType(NotesConstants.NOTES_TYPE_EVENT);
        notes.setUpdateDate(date);
        notes.setUpdatedBy("System");
        notes.setNoteDesc("Acknowledgement Received");
        notes.setItemName("");
        saveNotes(notes);
    }
    public void deleteAesDetails(String fileNo,String user,String message) throws Exception {
        Notes note = new Notes();
         note.setModuleId(NotesConstants.FILE);
         note.setModuleRefId(fileNo);
         note.setNoteType(NotesConstants.NOTES_TYPE_EVENT);
         note.setUpdateDate(new Date());
         note.setUpdatedBy(user);
         note.setNoteDesc(message);
         saveNotes(note);
    }
}
