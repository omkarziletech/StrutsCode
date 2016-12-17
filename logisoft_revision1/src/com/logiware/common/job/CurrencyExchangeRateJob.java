/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.common.job;

import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.currency.xml.CurrencyXml;
import com.gp.cong.logisoft.currency.xml.CurrencyXml.Results.Rate;
import com.gp.cong.logisoft.currency.xml.CurrencyExchangeRateDAO;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import org.hibernate.Transaction;
import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 *
 * @author Mei
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CurrencyExchangeRateJob implements org.quartz.Job {

    private static final Logger log = Logger.getLogger(CurrencyExchangeRateJob.class);

    public void run() throws Exception {
        StringBuilder str = new StringBuilder();
        int currencyCode = new CodetypeDAO().getCodeTypeId("Currency");
        List<String> currencyList = new GenericCodeDAO().getCode(currencyCode);
        if (null != currencyList && !currencyList.isEmpty()) {
            //   http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20%28%20%22USDYUM%22,%20%22USDZMK%22,%20%22USDZWD%22,%20%22USDUYU%22,%20%22USDVUV%22,%20%22USDVEB%22,%20%22USDVND%22,%20%22USDTND%22,%20%22USDTRL%22,%20%22USDPLN%22,%20%22USDMNT%22,%20%22USDMAD%22,%20%22USDMZM%22,%20%22USDMMK%22,%20%22USDRUB%22,%20%22USDTRY%22,%20%22USDAUD%22,%20%22USDBRL%22,%20%22USDCAD%22,%20%22USDCNY%22,%20%22USDHKD%22,%20%22USDIDR%22,%20%22USDILS%22,%20%22USDINR%22,%20%22USDKRW%22,%20%22USDMXN%22,%20%22USDMYR%22,%20%22USDNZD%22,%20%22USDPHP%22,%20%22USDSGD%22,%20%22USDTHB%22,%20%22USDYEN%22,%20%22USDISK%22%29&env=store://datatables.org/alltableswithkeys
            str.append("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20%28");
            for (String s : currencyList) {
                if (!"USD".equalsIgnoreCase(s)) {
                    str.append("%20%22USD").append(s).append("%22,");
                }
            }
            String sub = "";
            if (str.toString().lastIndexOf(",") > -1) {
                sub = str.toString().substring(0, str.toString().lastIndexOf(","));
            } else {
                sub = str.toString();
            }
            sub += "%29&env=store://datatables.org/alltableswithkeys";
            URL url = new URL(sub);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            InputStream in = conn.getInputStream();
            JAXBContext jAXBContext = JAXBContext.newInstance(CurrencyXml.class);
            Unmarshaller unmarshaller = jAXBContext.createUnmarshaller();
            CurrencyXml currencyXml = (CurrencyXml) unmarshaller.unmarshal(in);
            CurrencyExchangeRateDAO currencyExchangeRateDAO = new CurrencyExchangeRateDAO();
            currencyExchangeRateDAO.deleteCurrency();
            if (null != currencyXml.getResults() && null != currencyXml.getResults().getRate()) {
                for (Rate currency : currencyXml.getResults().getRate()) {
                    currencyExchangeRateDAO.insertCurrency(currency);
                }
            }
        }
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDAO jobDAO = new JobDAO();
        Transaction transaction = null;
        try {
            log.info("Currency Exchange Rate Job started on " + new Date());
            transaction = jobDAO.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            Job job = jobDAO.findByClassName(CurrencyExchangeRateJob.class.getCanonicalName());
            job.setStartTime(new Date());
            run();
            job.setEndTime(new Date());
            transaction.commit();
            log.info("Currency Exchange Rate ended on " + new Date());
        } catch (Exception e) {
            log.info("Currency Exchange Rate failed on " + new Date(), e);
            if (null != transaction && transaction.isActive() && jobDAO.getCurrentSession().isConnected() && jobDAO.getCurrentSession().isOpen()) {
                transaction.rollback();
            }
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }
}
