package com.gp.cong.logisoft.bc.fcl;

import com.gp.cong.logisoft.beans.CostBean;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.reports.QuotesReportPdfCreator;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.ChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.reports.dto.QuotationDTO;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.util.MessageResources;

/**
 * @author user
 *
 */
public class QuotationReportBC {

    List<QuotationDTO> QtFieldsList = null;
    List<CostBean> otherChargesLIst = null;
    Quotation quotation = null;
    QuotationDAO QtDAO = new QuotationDAO();
    String Comments = "";
    String rateChangeAlert = "";
    String disclaimer = "";
    String grandTotal = "";
    EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();

    public List<QuotationDTO> getChargesList(String quoteNo) throws Exception {

        List QtFieldsList = null;
        QtFieldsList = (List) QtDAO.getQtFieldsList(quoteNo);
        return QtFieldsList;

    }

    public List<CostBean> getOtherChargesList(String quoteNo,
            MessageResources messageResources) throws Exception {
        ChargesDAO chDao = new ChargesDAO();
        List otherChargesFinalList = new ArrayList();
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        List otherChargesLIst = (List) chDao.getChargesforQuotation2(Integer.parseInt(quoteNo), messageResources);
        quotation = getQuotation(quoteNo);
        if (quotation != null) {
            if (quotation.getLdprint() == null) {
                quotation.setLdprint("off");
            }
            if (quotation.getLdprint().equals("on")) {
                if (quotation.getAmount() == null) {
                    quotation.setAmount(0.0);
                }
                CostBean costb = new CostBean();
                costb.setOtherprint("on");
                costb.setChargecode("Local Dryage");
                costb.setCurrency("USD");
                costb.setRetail(number.format(quotation.getAmount()));
                if (quotation.getAmount() != 0.0) {
                    otherChargesFinalList.add(costb);
                }
                costb = null;
            }

            if (quotation.getIdprint() != null
                    && quotation.getIdprint().equals("on")) {
                if (quotation.getAmount1() == null) {
                    quotation.setAmount1(0.0);
                }
                CostBean costb = new CostBean();
                costb.setOtherprint("on");
                costb.setChargecode("Intermodal");
                costb.setCurrency("USD");
                costb.setRetail(number.format(quotation.getAmount1()));
                if (quotation.getAmount1() != 0.0) {
                    otherChargesFinalList.add(costb);
                }
                costb = null;
            }
            if (quotation.getInsureprint() != null
                    && quotation.getInsureprint().equals("on")) {
                if (quotation.getInsurancamt() == null) {
                    quotation.setInsurancamt(0.0);
                }
                CostBean costb = new CostBean();
                costb.setOtherprint("on");
                costb.setChargecode("Intermodal");
                costb.setCurrency("USD");
                costb.setRetail(number.format(quotation.getInsurancamt()));
                if (quotation.getInsurancamt() != 0.0) {
                    otherChargesFinalList.add(costb);
                }
                costb = null;
            }
        }
        otherChargesFinalList.addAll(otherChargesLIst);
        return otherChargesFinalList;

    }

    public Quotation getQuotation(String quoteNo) throws Exception {

        QuotationDAO quotationDAO = new QuotationDAO();
        Quotation quote = quotationDAO.findById(Integer.parseInt(quoteNo));
        return quote;
    }

    public void save(EmailSchedulerVO emailSchedulerVO) throws Exception {
        emailschedulerDAO.save(emailSchedulerVO);
    }

    public void createQuotationPDF(String quoteNo, String fileName, String contextPath,
            MessageResources messageResources, User user, String printRemarks, String regionRemarks,
            String fromEmailAddress, String fromName, HttpServletRequest request)
            throws Exception {
        QuotesReportPdfCreator quotesReportPdfCreator = new QuotesReportPdfCreator(request);
        //QtFieldsList = quotationReportBC.getChargesList(quoteNo);
        otherChargesLIst = getOtherChargesList(quoteNo, messageResources);
        Comments = getPerKGorLSBChargesFromOtherCharges(quoteNo, messageResources);

        rateChangeAlert = getRateChangeAlert(quoteNo, messageResources);
        disclaimer = getDisclaimerForReport();
        quotation = getQuotation(quoteNo);
        String from = user.getFirstName().concat(" " + user.getLastName());
        quotation.setUserPhone(user.getTelephone() != null ? user.getTelephone() : "");
        quotation.setUserFax(user.getFax() != null ? user.getFax() : "");
        quotation.setUserEmail(user.getEmail() != null ? user.getEmail() : "");
        quotation.setFrom(from);

        quotation.setPrintRemarks(printRemarks);
        quotation.setRegionRemarks(regionRemarks);
        quotation.setMessageResourceProperty(messageResources.getMessage("OceanFreightPopUp"));
        if (quotation.getNoOfDays() != null && !quotation.getNoOfDays().equalsIgnoreCase("") && !"n/a".equalsIgnoreCase(quotation.getNoOfDays())) {
            quotation.setTransitTime(Integer.parseInt(quotation.getNoOfDays().trim()));
        }

        if (quotation.getComment1() != null) {
            Comments = quotation.getComment1() + "\n" + Comments;
        }

        //grandTotal = quotationReportBC.getGrandTotalForQuoation(quoteNo);
        quotesReportPdfCreator.createReport(QtFieldsList, otherChargesLIst, Comments, rateChangeAlert, disclaimer,
                quotation, grandTotal, fileName, contextPath, messageResources, fromEmailAddress, fromName, request);

    }

    /**
     * To get RateChange Alert If a Charge Contain Effective date. By Pradeep
     *
     * @param quoteNo
     * @param messageResources
     * @return
     */
    public String getRateChangeAlert(String quoteNo,
            MessageResources messageResources) throws Exception {
        // For ContainerList
        List listofEffectiveDates = QtDAO.getListofEffDatesforQuotaion(quoteNo);
        StringBuffer rateAlertString = new StringBuffer();
        if (listofEffectiveDates != null && !listofEffectiveDates.isEmpty()) {
            int ed = 0;
            while (listofEffectiveDates.size() > ed) {
                rateAlertString = rateAlertString.append((String) listofEffectiveDates.get(ed));
                rateAlertString.append("\n");
                ed++;
            }
        }
        // For OtherCharges List
        if (getOtherChargesList(quoteNo, messageResources) != null) {
            List otherChargesList = getOtherChargesList(quoteNo,
                    messageResources);
            if (otherChargesList != null && !otherChargesList.isEmpty()) {
                NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");
                for (Iterator iterator = otherChargesList.iterator(); iterator.hasNext();) {
                    CostBean costBean = (CostBean) iterator.next();
                    String chargecode = costBean.getChargecode();
                    String retailamt = costBean.getRetail();
                    String oinclude = costBean.getOtherinclude();
                    String oprint = costBean.getOtherprint();
                    String chargecurrency = costBean.getCurrency();
                    String othereffectivedate = costBean.getOtherEffectiveDate();
                    String newrate = costBean.getRetailFuture();
                    if (newrate == null) {
                        newrate = "0.0";
                    }
                    String costtype = costBean.getCostType();
                    if (oinclude == null) {
                        oinclude = "off";
                    }
                    if (oprint == null) {
                        oprint = "off";
                    }
                    if (othereffectivedate != null) {
                        String incdec = "";
                        Double incdecAmt = 0.0;
                        Double futureRate = 0.0;
                        Double retailamount = 0.0;
                        if (retailamt != null && retailamt.contains(",")) {
                            retailamt = retailamt.replaceAll(",", "");
                        }
                        retailamount = Double.valueOf(retailamt);
                        if (newrate != null && newrate.contains(",")) {
                            newrate = newrate.replaceAll(",", "");
                        }
                        futureRate = Double.valueOf(newrate);
                        if ((futureRate - retailamount) > 0) {
                            incdec = "increase";
                            incdecAmt = futureRate - retailamount;
                        } else if ((futureRate - retailamount) < 0) {
                            incdec = "decrease";
                            incdecAmt = (futureRate - retailamount) * (-1);
                        }
                        String d = chargecode + " will " + incdec + " by "
                                + numberFormat.format(incdecAmt) + " "
                                + chargecurrency + " (" + costtype
                                + ") Effective " + othereffectivedate;
                        rateAlertString.append(d);
                        rateAlertString.append("\n");
                    }
                }// for
            }// if

        }// if

        return rateAlertString.toString();
    }

    /**
     * To get PerKG or LSB Charges from OtherCharges List of a Quotation. By
     * Pradeep
     *
     * @param quoteNo
     * @param messageResources
     * @return
     */
    public String getPerKGorLSBChargesFromOtherCharges(String quoteNo,
            MessageResources messageResources) throws Exception {
        List otherChargesList = getOtherChargesList(quoteNo, messageResources);
        StringBuffer perkgLSBString = new StringBuffer();
        for (Iterator iterator = otherChargesList.iterator(); iterator.hasNext();) {
            CostBean costBean = (CostBean) iterator.next();
            if (costBean != null && costBean.getCostType() != null) {
                if (costBean.getCostType().trim().equalsIgnoreCase("Per 1000KG")
                        || costBean.getCostType().trim().equalsIgnoreCase(
                        "PER 2000 LBS")) {
                    String perkg1 = "An additional " + costBean.getChargecode()
                            + " will apply as follows: " + costBean.getRetail()
                            + " " + costBean.getCostType()
                            + " with a minimum amount of "
                            + costBean.getMinimum() + "("
                            + costBean.getCurrency() + ")";
                    perkgLSBString.append(perkg1);
                    if (iterator.hasNext()) {
                        perkgLSBString.append("\n");
                    }
                }
            }

        }
        return perkgLSBString.toString();
    }

    /**
     * To get Disclaimer for the Quotation Report. By pradeep
     *
     * @return String
     */
    public String getDisclaimerForReport() throws Exception {
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        List disclimerlist = genericCodeDAO.getDisclaimerForQuoationReport();
        StringBuffer stringBuffer = new StringBuffer();
        if (disclimerlist != null && !disclimerlist.isEmpty()) {
            for (Iterator iterator = disclimerlist.iterator(); iterator.hasNext();) {
                String codeDesc = (String) iterator.next();
                stringBuffer.append(codeDesc);
                if (iterator.hasNext()) {
                    stringBuffer.append("\n");
                }
            }// for
        }
        return stringBuffer.toString();
    }

    /**
     * To Get GrandTotal for Quotation Report
     *
     * @param quoteNo
     * @return
     */
    public String getGrandTotalForQuoation(String quoteNo) throws Exception {
        List totalList = (List) QtDAO.getTotalList(quoteNo);
        String grandtotal = "";
        if (totalList != null && !totalList.isEmpty()) {
            QuotationDTO abc = (QuotationDTO) totalList.get(0);

            int a = 0;
            if (!abc.getTotal().equals("0.00")) {
                grandtotal = grandtotal + abc.getTotal() + "(USD)";
                a = 1;
            }
            if (!abc.getCury1().equals("0.00")) {
                if (a == 1) {
                    grandtotal = grandtotal + ",";
                }
                a = 1;
                grandtotal = grandtotal + abc.getCury1() + "(BAHT)";
            }
            if (!abc.getCury2().equals("0.00")) {
                if (a == 1) {
                    grandtotal = grandtotal + ",";
                }
                a = 1;
                grandtotal = grandtotal + abc.getCury2() + "(BDT)";
            }
            if (!abc.getCury3().equals("0.00")) {
                if (a == 1) {
                    grandtotal = grandtotal + ",";
                }
                a = 1;
                grandtotal = grandtotal + abc.getCury3() + "(CYP)";
            }
            if (!abc.getCury4().equals("0.00")) {
                if (a == 1) {
                    grandtotal = grandtotal + ",";
                }
                a = 1;
                grandtotal = grandtotal + abc.getCury4() + "(EUR)";
            }
            if (!abc.getCury5().equals("0.00")) {
                if (a == 1) {
                    grandtotal = grandtotal + ",";
                }
                a = 1;
                grandtotal = grandtotal + abc.getCury5() + "(HDK)";
            }
            if (!abc.getCury6().equals("0.00")) {
                if (a == 1) {
                    grandtotal = grandtotal + ",";
                }
                a = 1;
                grandtotal = grandtotal + abc.getCury6() + "(LKR)";
            }
            if (!abc.getCury7().equals("0.00")) {
                if (a == 1) {
                    grandtotal = grandtotal + ",";
                }
                a = 1;
                grandtotal = grandtotal + abc.getCury7() + "(NT)";
            }
            if (!abc.getCury8().equals("0.00")) {
                if (a == 1) {
                    grandtotal = grandtotal + ",";
                }
                a = 1;
                grandtotal = grandtotal + abc.getCury8() + "(PRS)";
            }
            if (!abc.getCury9().equals("0.00")) {
                if (a == 1) {
                    grandtotal = grandtotal + ",";
                }
                a = 1;
                grandtotal = grandtotal + abc.getCury9() + "(RMB)";
            }
            if (!abc.getCury10().equals("0.00")) {
                if (a == 1) {
                    grandtotal = grandtotal + ",";
                }
                a = 1;
                grandtotal = grandtotal + abc.getCury10() + "(WON)";
            }
            if (!abc.getCury11().equals("0.00")) {
                if (a == 1) {
                    grandtotal = grandtotal + ",";
                }
                a = 1;
                grandtotal = grandtotal + abc.getCury11() + "(YEN)";
            }

        }
        return grandtotal;
    }
}
