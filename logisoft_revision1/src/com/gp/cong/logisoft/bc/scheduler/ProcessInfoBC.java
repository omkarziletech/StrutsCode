package com.gp.cong.logisoft.bc.scheduler;

import java.util.Date;
import java.util.Iterator;
import java.util.List;


import com.gp.cong.common.DateUtils;
import com.gp.cong.common.LockFileNumber;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.domain.ProcessInfo;
import com.gp.cong.logisoft.domain.ProcessInfoHistory;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoHistoryDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.util.CommonFunctions;

public class ProcessInfoBC {

    ProcessInfoDAO processInfoDAO = new ProcessInfoDAO();
    ProcessInfoHistory processInfoHistory = new ProcessInfoHistory();
    UserDAO userDAO = new UserDAO();
    ItemDAO itemDAO = new ItemDAO();
    ProcessInfoHistoryDAO processInfoHistoryDAO = new ProcessInfoHistoryDAO();

    public List getLockedPages() throws Exception {
        List lockedList = processInfoDAO.getLockedPages();
        for (Iterator iter = lockedList.iterator(); iter.hasNext();) {
            ProcessInfo processInfo = (ProcessInfo) iter.next();
            if (processInfo.getUserid() != null) {
                User user = userDAO.findById(processInfo.getUserid());
                if(null != user){
                    processInfo.setUserName(user.getLoginName());
                    processInfo.setEmail(user.getEmail());
                    processInfo.setState(user.getState());
                    processInfo.setCity(user.getCity());
                    processInfo.setCountry(user.getCountry());
                    processInfo.setRole(user.getRole().getRoleDesc());
                    processInfo.setTelephone(user.getTelephone());
                    if (processInfo.getProgramid() != null) {
                        Item item = itemDAO.findById(processInfo.getProgramid());
                        if(null != item) {
                            processInfo.setItemName(item.getItemDesc());
                        }
                     }
                }
            }
        }
        return lockedList;
    }

    public void delete(String index)throws Exception {
        ProcessInfo processInfo = processInfoDAO.findById(Integer.parseInt(index));
        if(null!=processInfo){
        processInfoHistory = new ProcessInfoHistory();
        processInfoHistory.setUserid(processInfo.getUserid());
        processInfoHistory.setProgramid(processInfo.getProgramid());
        processInfoHistory.setRecordid(processInfo.getRecordid());
        processInfoHistory.setProcessinfodate(processInfo.getProcessinfodate());
        processInfoHistory.setId(processInfo.getId());
        processInfoHistory.setAction(processInfo.getAction());
        processInfoHistory.setEditstatus(processInfo.getEditstatus());
        processInfoHistory.setDeletestatus(processInfo.getDeletestatus());
        if (processInfo != null && processInfo.getUserid() != null) {
            processInfoHistoryDAO.save(processInfoHistory);
        }
        java.util.Date currdate = new java.util.Date();
        processInfo.setEditstatus("editcancelled");

        processInfo.setProcessinfodate(currdate);
        if (processInfo != null && processInfo.getId() != null) {
            processInfoDAO.update(processInfo);
        }
        ProcessInfo pi = processInfoDAO.findById(processInfo.getId());
        ProcessInfoHistory pih = new ProcessInfoHistory();
        if (pi != null) {
            pih.setUserid(pi.getUserid());
            pih.setProgramid(pi.getProgramid());
            pih.setRecordid(pi.getRecordid());
            pih.setProcessinfodate(pi.getProcessinfodate());
            pih.setId(pi.getId());
            pih.setAction(processInfo.getAction());
            pih.setEditstatus(pi.getEditstatus());

            if(pih!=null && pih.getId()!=null){
            processInfoHistoryDAO.save(pih);
            }

        }

        // delete in Process_info
        if (pi != null && pi.getId() != null) {
            processInfoDAO.delete(pi);
        }
    }
    }
    //---------------------------------

    public void doLock(String action, String recordId, Integer userId,String module)throws Exception {
        ProcessInfo processInfo = findWhoLockedRecord(null, recordId, null);
        if (processInfo == null) {
            lockRecord(action, recordId, userId,module);
        }
    }

    public void releaseLoack(String module, String recordId, Integer userId) throws Exception {
        if (recordId != null) {
            ProcessInfo processInfo = findWhoLockedRecord(null, recordId, userId);
            int index = recordId.indexOf(FclBlConstants.EQUALDELIMITER);
            if (index > -1 && processInfo == null) {
                recordId = recordId.substring(0, index);
                processInfo = findWhoLockedRecord(module, recordId, userId);
                if (processInfo != null) {
                    processInfoDAO.delete(processInfo);
                }
            } else {
                if (processInfo != null) {
                    processInfoDAO.delete(processInfo);
                }
            }
        }
    }

    /**
     * @param recordId
     * @return
     */
    public String cheackFileNumberForLoack(String recordId, String userId,String moduleId)throws Exception {
        LockFileNumber.recordId = recordId;
        LockFileNumber.userId = (CommonFunctions.isNotNull(userId) ? new Integer(userId) : 0);
        LockFileNumber.moduleId=moduleId;
        LockFileNumber loclFileNumber = new LockFileNumber();
            loclFileNumber.join();// it wil force thread to complete the task before move to next step
        return loclFileNumber.getFileNumber();
    }

    /**
     * locking the records while editing...
     */
    public void lockRecord(String action, String recordId, Integer userId,String moduleId) throws Exception {
        ProcessInfo processInfo = new ProcessInfo();
        processInfo.setAction(action);
        processInfo.setRecordid(recordId);
        processInfo.setUserid(userId);
        moduleId=(null!=moduleId)?moduleId.toUpperCase():moduleId;
        processInfo.setModuleId(moduleId);
        processInfo.setProcessinfodate(new Date());
        processInfoDAO.save(processInfo);
    }

    public ProcessInfo findWhoLockedRecord(String module, String fileNo, Integer userId)throws Exception {
        return processInfoDAO.findWhoLockedRecords(module, fileNo, userId);
    }
    public void deleteAllRecordsWhileChangeTab(Integer userId)throws Exception {
        List<ProcessInfo> list = processInfoDAO.getLockedPages(userId);
        for (ProcessInfo processInfo : list) {
            processInfoDAO.delete(processInfo);
        }
    }
    public String cheackFileINDB(String recordId,String userNumber)throws Exception {
        String returnString = "";
        Integer userId=(null!=userNumber)?new Integer(userNumber):0;
            ProcessInfo processInfo = processInfoDAO.findByFileNo(recordId,userId);
            ProcessInfo sameUserProcessInfo = processInfoDAO.findByFileNoAndUserId(recordId,userId);
            if (processInfo != null && processInfo.getUserid() != null) {
                User user = userDAO.findById(processInfo.getUserid());
                String editDate = DateUtils.formatDate(processInfo.getProcessinfodate(), "MM/dd/yyyy hh:mm a");
                returnString = user.getLoginName() + "\t " + editDate;
            }else if (sameUserProcessInfo != null && sameUserProcessInfo.getUserid() != null) {
                returnString = "sameUser";
            }
        return returnString;
    }
}
