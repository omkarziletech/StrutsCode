package com.gp.cong.common;

import org.hibernate.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gp.cong.logisoft.bc.scheduler.ProcessInfoBC;
import com.gp.cong.logisoft.domain.ProcessInfo;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;

/**
 * @author Administrator
 * created by gayatri on 8/3/2010
 * generating unique file Number for quotes...
 */
public class LockFileNumber extends Thread {
	String result= null;
	public static String  recordId=null;
	public static Integer  userId=null;
	public static String  moduleId=null;
        private static final Log log = LogFactory.getLog(LockFileNumber.class);

	/**
	 * constructor that wil start the current thread.
	 */
	public LockFileNumber() {		
		this.start();// start method wil call run method
	}
	public synchronized void run() {

        try {
            result = checkFileNumberForLock(); // run method wil call generateFileNumber() at time for one thread
        } catch (Exception ex) {
            log.info("checkFileNumberForLock :",ex);
        }
	}

	/** Generating file number taking the highest number from genericcodedup table increamanting one
	 * and return, mean while update genericcodedup records with increamated file Number
	 * @return File number 
	 */
	public static synchronized String checkFileNumberForLock()throws Exception{
		ProcessInfoBC processInfoBC = new ProcessInfoBC();
		String returnString="";
		UserDAO userDAO = new UserDAO();
		ProcessInfoDAO processInfoDAO = new ProcessInfoDAO();
		Transaction transaction = processInfoDAO.getSession().beginTransaction();// getting current session..		
		ProcessInfo processInfo= processInfoDAO.findByFileNo(recordId,userId);		
		if(processInfo!=null && processInfo.getUserid()!=null){
			User user=userDAO.findById(processInfo.getUserid());
			String editDate=DateUtils.formatDate(processInfo.getProcessinfodate(), "MM/dd/yyyy hh:mm a");
			returnString=user.getLoginName()+"\t "+editDate;			
		}else{
			processInfoBC.doLock("FILENO", recordId, userId,moduleId);
		}
		transaction.commit();		
		return returnString;
	}
	

	/**
	 * @return file number 
	 */
	public String getFileNumber(){
		return result;
	}
}
