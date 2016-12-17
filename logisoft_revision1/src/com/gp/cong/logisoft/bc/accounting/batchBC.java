package com.gp.cong.logisoft.bc.accounting;

import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;

public class batchBC {
   EmailschedulerDAO emailschedulerDAO =  new EmailschedulerDAO();
   
   public void save(EmailSchedulerVO emailSchedulerVO) throws Exception {
	   emailschedulerDAO.save(emailSchedulerVO);
   }
   
	
}
