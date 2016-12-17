package com.gp.cong.logisoft.bc.accounting;

import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cvst.logisoft.hibernate.dao.BatchDAO;

public class BatchesBC {
   EmailschedulerDAO emailschedulerDAO =  new EmailschedulerDAO();
   BatchDAO batchDAO = new BatchDAO();
   public void save(EmailSchedulerVO emailSchedulerVO) throws Exception {
	   emailschedulerDAO.save(emailSchedulerVO);
   }
   
   public String getSubLedgerStatus(String sourceLedger) throws Exception {
	   return batchDAO.getSubLedgerStatus(sourceLedger);
   }
}
