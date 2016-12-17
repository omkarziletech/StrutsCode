package com.gp.cong.common;

import java.util.List;

import org.hibernate.Transaction;

import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * @author Administrator
 * created by gayatri on 2/3/2010
 * generating unique file Number for quotes...
 */
public class GenerateFileNumber extends Thread {

    private Integer fileNo = null;
    private static final Logger log = Logger.getLogger(GenerateFileNumber.class);

    /**
     * constructor that wil start the current thread.
     */
    public GenerateFileNumber() {
	this.start();// start method wil call run method
    }

    public synchronized void run() {
	fileNo = generateFileNumber();// run method wil call generateFileNumber() at time for one thread
    }

    /**
     * Generating file number taking the highest number from genericcodedup table increamanting one
     * and return, mean while update genericcodedup records with increamated file Number
     * @return File number
     */
    public static synchronized Integer generateFileNumber() {
	Integer fileNo = null;
	try {
	    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
	    Transaction transaction = genericCodeDAO.getSession().beginTransaction();// getting current session..
	    List fileNumberList = genericCodeDAO.findByCodeTypeid(46);

	    if (fileNumberList != null && !fileNumberList.isEmpty()) {
		GenericCode genericCode = (GenericCode) fileNumberList.get(0);
		if (genericCode != null) {
		    int fileNumber = Integer.parseInt(genericCode.getCode());
		    fileNo = fileNumber + 1;
		    genericCode.setCode(String.valueOf(fileNo));
		}
	    }
	    transaction.commit();// commiting throw transaction.
	} catch (Exception e) {
	    log.info("generateFileNumber failed on " + new Date(),e);
	}
	return fileNo;
    }

    /**
     * @return file number
     */
    public Integer getFileNumber() {
	return fileNo;
    }
}
