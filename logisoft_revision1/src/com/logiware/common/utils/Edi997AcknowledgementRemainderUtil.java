/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.common.utils;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.ConstantsInterface;
import static com.gp.cong.common.ConstantsInterface.CONTACT_MODE_EMAIL;
import static com.gp.cong.common.ConstantsInterface.EMAIL_STATUS_PENDING;
import static com.gp.cong.common.ConstantsInterface.USER_SYSTEM;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO;
import com.logiware.common.model.Edi997RemainderModel;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;

/**
 *
 * @author User
 */
public class Edi997AcknowledgementRemainderUtil implements LclCommonConstant, ConstantsInterface {

    private static final Logger log = Logger.getLogger(Edi997AcknowledgementRemainderUtil.class);

    public void sendEdi997AcknowledgementRemainder() throws Exception {
        EmailschedulerDAO emailDAO = new EmailschedulerDAO();
        LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
        Transaction transaction = logFileEdiDAO.getCurrentSession().getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
        List<Edi997RemainderModel> logFileEdiList = logFileEdiDAO.getLogFileEdiList();
        Date today = new Date();
        for (Edi997RemainderModel logFileEdiLists : logFileEdiList) {
            try {
                transaction = logFileEdiDAO.getCurrentSession().getTransaction();
                if (!transaction.isActive()) {
                    transaction.begin();
                }
                StringBuilder textMessage = new StringBuilder();
                textMessage.append("Please check with the below file as a 997 acknowledgement message was not received").append("\n");
                textMessage.append("<HTML><BODY>");
                textMessage.append("<table border='1'><tr bgcolor='#FFFF00'>");

                textMessage.append("<th>");
                textMessage.append("DR NUMBER");
                textMessage.append("</th>");

                textMessage.append("<th>");
                textMessage.append("EDI COMPANY");
                textMessage.append("</th>");

                textMessage.append("<th>");
                textMessage.append("BOOKING NUMBER");
                textMessage.append("</th>");

                textMessage.append("<th>");
                textMessage.append("SENT304 ");
                textMessage.append("</th>");

                textMessage.append("<th>");
                textMessage.append("NUM304");
                textMessage.append("</th>");

                textMessage.append("<th>");
                textMessage.append("NUM997");
                textMessage.append("</th>");

                textMessage.append("</tr>");

                textMessage.append("<tr>");
                textMessage.append("<td>");
                textMessage.append("04-").append(logFileEdiLists.getFileNo());
                textMessage.append("</td>");

                textMessage.append("<td>");
                textMessage.append(logFileEdiLists.getEdiCompany());
                textMessage.append("</td>");

                textMessage.append("<td>");
                textMessage.append(logFileEdiLists.getBookingNumber());
                textMessage.append("</td>");

                textMessage.append("<td>");
                textMessage.append(logFileEdiLists.getSent304());
                textMessage.append("</td>");

                textMessage.append("<td>");
                textMessage.append(logFileEdiLists.getNum304());
                textMessage.append("</td>");

                textMessage.append("<td>");
                textMessage.append(logFileEdiLists.getNum997());
                textMessage.append("</td>");

                textMessage.append("</tr>");

                textMessage.append("</HTML></BODY>");

                EmailSchedulerVO email = new EmailSchedulerVO();
                email.setFromName("");
                email.setFromAddress("mailrobot@ecuworldwide.us");
                email.setToAddress(logFileEdiLists.getToEmail());
                email.setSubject("997 Acknowledgement Message Not Received - 04-" + logFileEdiLists.getFileNo());
                email.setFileLocation("");
                email.setStatus(EMAIL_STATUS_PENDING);
                email.setTextMessage(textMessage.toString());
                email.setHtmlMessage(textMessage.toString());
                email.setName("Edi997Remainder");
                email.setType(CONTACT_MODE_EMAIL);
                email.setModuleName(CommonConstants.SCREEN_NAME_BL);
                email.setModuleId(logFileEdiLists.getFileNo());
                email.setUserName(USER_SYSTEM);
                email.setEmailDate(today);
                emailDAO.save(email);
                transaction = logFileEdiDAO.getCurrentSession().getTransaction();
                transaction.commit();
            } catch (Exception e) {
                log.info("Sending 997 Acknowledgement reminder Update Daily failed for DR(s) " + logFileEdiLists.getFileNo() + " on " + new Date(), e);
            } finally {
                if (transaction.isActive() && logFileEdiDAO.getCurrentSession().isConnected() && logFileEdiDAO.getCurrentSession().isOpen()) {
                    transaction.rollback();
                }
            }
        }
        transaction = logFileEdiDAO.getCurrentSession().getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
    }
}
