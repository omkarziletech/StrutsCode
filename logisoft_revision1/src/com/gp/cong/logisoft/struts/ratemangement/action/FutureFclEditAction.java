/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.ratemangement.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.FclBuyAirFreightCharges;
import com.gp.cong.logisoft.domain.FclBuyCost;
import com.gp.cong.logisoft.domain.FclBuyCostTypeFutureRates;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.FutureFclEditForm;
import com.gp.cong.logisoft.util.DBUtil;

/** 
 * MyEclipse Struts
 * Creation date: 09-23-2008
 * 
 * XDoclet definition:
 * @struts.action path="/futureFclEdit" name="futureFclEditForm" input="/jsps/ratemanagement/futureFclEdit.jsp" scope="request"
 */
public class FutureFclEditAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
        FutureFclEditForm fCLAddEditForm = (FutureFclEditForm) form;
        String index = fCLAddEditForm.getIndex();
        String buy = fCLAddEditForm.getBuy();
        String buttonValue = fCLAddEditForm.getButtonValue();
        String amount = fCLAddEditForm.getAmount();
        String efd = fCLAddEditForm.getEfdate();
        List recordsList = new ArrayList();
        DBUtil dbUtil = new DBUtil();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String currency = fCLAddEditForm.getCuurency();
        List codeList = new ArrayList();
        String standard = fCLAddEditForm.getStandard();
        String pcctc = fCLAddEditForm.getPcctc();
        String pcftf = fCLAddEditForm.getPcftf();
        String pcretail = fCLAddEditForm.getPcretail();
        String pccmi = fCLAddEditForm.getPcminimun();
        FclBuyAirFreightCharges oldfclBuyAirFreightCharges = null;
        FclBuyCostTypeFutureRates oldFclBuyCostTypeRates = null;
        FclBuyCost fclBuyCost = new FclBuyCost();
        int ind = 0;
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        GenericCode genericCo = new GenericCode();
        String markup = fCLAddEditForm.getMarkup();
        HttpSession session = request.getSession();

        if (buttonValue != null && buttonValue.equals("add")) {
            if (index != null) {
                ind = Integer.parseInt(index);
            }
            if (buy != null && !buy.equals("")) {
                if (session.getAttribute("costFutureCodeList") != null) {



                    codeList = (List) session.getAttribute("costFutureCodeList");
                    for (int i1 = 0; i1 < codeList.size(); i1++) {
                        fclBuyCost = (FclBuyCost) codeList.get(i1);
                        Set fclBuyRates = new HashSet<FclBuyCostTypeFutureRates>();

                        if (fclBuyCost.getFclBuyFutureTypesSet() != null) {
                            fclBuyRates = fclBuyCost.getFclBuyFutureTypesSet();
                            Iterator fclBuyRetrive = fclBuyRates.iterator();
                            while (fclBuyRetrive.hasNext()) {
                                FclBuyCostTypeFutureRates fclBuyCostTypeRates1 = (FclBuyCostTypeFutureRates) fclBuyRetrive.next();
                                recordsList = (List) session.getAttribute("fclrecordsfuture");
                                if (recordsList != null && recordsList.size() > 0) {
                                    oldFclBuyCostTypeRates = (FclBuyCostTypeFutureRates) recordsList.get(ind);
                                }

                                if (oldFclBuyCostTypeRates != null && fclBuyCostTypeRates1 != null && oldFclBuyCostTypeRates.getFclCostId() == fclBuyCostTypeRates1.getFclCostId()) {
                                    if (oldFclBuyCostTypeRates.getUnitType() != null) {
                                        if (oldFclBuyCostTypeRates.getUnitType() != null && fclBuyCostTypeRates1.getUnitType() != null && oldFclBuyCostTypeRates.getUnitType().getId() == fclBuyCostTypeRates1.getUnitType().getId()) {
                                            fclBuyCostTypeRates1.setUnitType(fclBuyCostTypeRates1.getUnitType());
                                            fclBuyCostTypeRates1.setCostType(fclBuyCostTypeRates1.getCostType());
                                            fclBuyCostTypeRates1.setCostCode(fclBuyCostTypeRates1.getCostCode());
                                            //fclBuyCostTypeRates.setFclCostId(fclBuyCostTypeRates1.getFclCostId());
                                            if (currency != null && !currency.equals("0") && !currency.equals("")) {
                                                genericCo = genericCodeDAO.findById(Integer.parseInt(currency));
                                                fclBuyCostTypeRates1.setCurrency(genericCo);
                                            }
                                            if (amount != null && !amount.equals("")) {
                                                double amt = Double.parseDouble(amount);
                                                double amut = amt + 0000.00;
                                                fclBuyCostTypeRates1.setActiveAmt(new Double(amut));


                                            }
                                            if (standard != null && standard.equals("on")) {
                                                fclBuyCostTypeRates1.setStandard("Y");
                                            } else {
                                                fclBuyCostTypeRates1.setStandard("N");
                                            }

                                            if (markup != null && !markup.equals("")) {
                                                double amt = Double.parseDouble(markup);
                                                double mark = amt + 0000.00;
                                                fclBuyCostTypeRates1.setMarkup(mark);
                                            }

                                            if (pcctc != null && !pcctc.equals("")) {
                                                double amt = Double.parseDouble(pcctc);
                                                double pccmut = amt + 0000.00;
                                                fclBuyCostTypeRates1.setCtcAmt(new Double(pccmut));
                                            }
                                            if (pccmi != null && !pccmi.equals("")) {
                                                double amt = Double.parseDouble(pccmi);
                                                double pcamut = amt + 0000.00;
                                                fclBuyCostTypeRates1.setMinimumAmt(new Double(pcamut));
                                            }
                                            if (pcftf != null && !pcftf.equals("")) {
                                                double amt = Double.parseDouble(pcftf);
                                                double pcamut = amt + 0000.00;
                                                fclBuyCostTypeRates1.setFtfAmt(new Double(pcamut));
                                            }
                                            if (pcretail != null && !pcretail.equals("")) {
                                                double amt = Double.parseDouble(pcretail);
                                                double pcramut = amt + 0000.00;
                                                fclBuyCostTypeRates1.setRatAmount(new Double(pcramut));
                                            }
                                            if (efd != null && !efd.equals("")) {
                                                Date start = null;
                                                    start = sdf.parse(efd);
                                                fclBuyCostTypeRates1.setEffectiveDate(start);
                                            }

                                            break;
                                        }
                                    } else {

                                        if (oldFclBuyCostTypeRates.getCostId().equals(fclBuyCostTypeRates1.getCostId())) {
                                            if (oldFclBuyCostTypeRates.getTypeId().equals(fclBuyCostTypeRates1.getTypeId())) {

                                                //fclBuyCostTypeRates1.setUnitType(fclBuyCostTypeRates1.getUnitType());
                                                fclBuyCostTypeRates1.setCostType(fclBuyCostTypeRates1.getCostType());
                                                fclBuyCostTypeRates1.setCostCode(fclBuyCostTypeRates1.getCostCode());
                                                //fclBuyCostTypeRates.setFclCostId(fclBuyCostTypeRates1.getFclCostId());
                                                if (amount != null && !amount.equals("")) {
                                                    double amt = Double.parseDouble(amount);
                                                    double amut = amt + 0000.00;
                                                    fclBuyCostTypeRates1.setActiveAmt(new Double(amut));

                                                }
                                                if (pcctc != null && !pcctc.equals("")) {
                                                    double amt = Double.parseDouble(pcctc);
                                                    double pccmut = amt + 0000.00;
                                                    fclBuyCostTypeRates1.setCtcAmt(new Double(pccmut));
                                                }
                                                if (pccmi != null && !pccmi.equals("")) {
                                                    double amt = Double.parseDouble(pccmi);
                                                    double pcamut = amt + 0000.00;
                                                    fclBuyCostTypeRates1.setMinimumAmt(new Double(pcamut));
                                                }
                                                if (pcftf != null && !pcftf.equals("")) {
                                                    double amt = Double.parseDouble(pcftf);
                                                    double pcamut = amt + 0000.00;
                                                    fclBuyCostTypeRates1.setFtfAmt(new Double(pcamut));
                                                }
                                                if (pcretail != null && !pcretail.equals("")) {
                                                    double amt = Double.parseDouble(pcretail);
                                                    double pcramut = amt + 0000.00;
                                                    fclBuyCostTypeRates1.setRatAmount(new Double(pcramut));
                                                }

                                                if (currency != null && !currency.equals("0") && !currency.equals("")) {
                                                    genericCo = genericCodeDAO.findById(Integer.parseInt(currency));
                                                    fclBuyCostTypeRates1.setCurrency(genericCo);

                                                }
                                                if (efd != null && !efd.equals("")) {
                                                    Date start = null;
                                                        start = sdf.parse(efd);
                                                    fclBuyCostTypeRates1.setEffectiveDate(start);
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                        }

                    }

                }
            } else {
                if (session.getAttribute("costFutureCodeList") != null) {
                    codeList = (List) session.getAttribute("costFutureCodeList");
                    for (int i1 = 0; i1 < codeList.size(); i1++) {
                        fclBuyCost = (FclBuyCost) codeList.get(i1);
                        Set fclBuyAirFright = new HashSet<FclBuyAirFreightCharges>();
                        fclBuyAirFright = fclBuyCost.getFclBuyAirFreightSet();

                        if (fclBuyAirFright != null) {
                            Iterator fclAirFright = fclBuyAirFright.iterator();
                            while (fclAirFright.hasNext()) {
                                recordsList = (List) session.getAttribute("fclfrightrecords");
                                if (recordsList != null && recordsList.size() > 0) {
                                    oldfclBuyAirFreightCharges = (FclBuyAirFreightCharges) recordsList.get(ind);
                                }

                                FclBuyAirFreightCharges fclBuyAirFreightCharges1 = (FclBuyAirFreightCharges) fclAirFright.next();

                                if (oldfclBuyAirFreightCharges != null && fclBuyAirFreightCharges1 != null && oldfclBuyAirFreightCharges.getFclCostTypeId() == fclBuyAirFreightCharges1.getFclCostTypeId()) {

                                    if (oldfclBuyAirFreightCharges.getWieghtRange().getId() == fclBuyAirFreightCharges1.getWieghtRange().getId()) {
                                        fclBuyAirFreightCharges1.setWieghtRange(fclBuyAirFreightCharges1.getWieghtRange());
                                        fclBuyAirFreightCharges1.setCostType(fclBuyAirFreightCharges1.getCostType());
                                        fclBuyAirFreightCharges1.setCostCode(fclBuyAirFreightCharges1.getCostCode());
                                        if (amount != null && !amount.equals("")) {

                                            double amt = Double.parseDouble(amount);
                                            double amut = amt + 0000.00;
                                            fclBuyAirFreightCharges1.setRatAmount(new Double(amut));
                                        }
                                        if (currency != null && !currency.equals("0") && !currency.equals("")) {
                                            genericCo = genericCodeDAO.findById(Integer.parseInt(currency));
                                            fclBuyAirFreightCharges1.setCurrency(genericCo);
                                        }
                                        break;
                                    }
                                }

                            }
                        }

                    }
                }


            }
            request.setAttribute("addfclrecord", "addfclrecor");
        }
        if (buttonValue != null && buttonValue.equals("delete")) {

            if (index != null) {
                ind = Integer.parseInt(index);
            }

            if (session.getAttribute("costFutureCodeList") != null) {
                codeList = (List) session.getAttribute("costFutureCodeList");
                //int bunker=0;
                boolean flag = false;


                for (int i1 = 0; i1 < codeList.size(); i1++) {
                    if (!flag) {
                        fclBuyCost = (FclBuyCost) codeList.get(i1);

                        Set fclBuyRates = new HashSet<FclBuyCostTypeFutureRates>();

                        Set fclBuyAirFright = new HashSet<FclBuyAirFreightCharges>();
                        if (buy != null && !buy.equals("")) {
                            int count1 = 0;

                            if (fclBuyCost.getFclBuyFutureTypesSet() != null) {
                                fclBuyRates = fclBuyCost.getFclBuyFutureTypesSet();
                                Iterator fclBuyRetrive = fclBuyRates.iterator();
                                recordsList = (List) session.getAttribute("fclrecordsfuture");
                                int ocen = 0;
                                List a = new ArrayList();

                                oldFclBuyCostTypeRates = (FclBuyCostTypeFutureRates) recordsList.get(ind);
                                for (int i = 0; i < recordsList.size(); i++) {
                                    FclBuyCostTypeFutureRates frt = (FclBuyCostTypeFutureRates) recordsList.get(i);

                                    if (oldFclBuyCostTypeRates != null && frt != null && oldFclBuyCostTypeRates.getTypeId() != null && oldFclBuyCostTypeRates.getTypeId().equals(frt.getTypeId())) {
                                        count1++;
                                    }
                                }

                                while (fclBuyRetrive.hasNext()) {
                                    FclBuyCostTypeFutureRates fclBuyCostTypeRates1 = (FclBuyCostTypeFutureRates) fclBuyRetrive.next();
                                    if (recordsList != null && recordsList.size() > 0) {
                                        oldFclBuyCostTypeRates = (FclBuyCostTypeFutureRates) recordsList.get(ind);

                                    }
                                    if (oldFclBuyCostTypeRates != null && fclBuyCostTypeRates1 != null && oldFclBuyCostTypeRates.getFclCostId() == fclBuyCostTypeRates1.getFclCostId()) {
                                        if (oldFclBuyCostTypeRates.getUnitType() != null) {

                                            if (oldFclBuyCostTypeRates.getUnitType() != null && fclBuyCostTypeRates1.getUnitType() != null && oldFclBuyCostTypeRates.getUnitType().getId() == fclBuyCostTypeRates1.getUnitType().getId()) {
                                                if (count1 == 1) {
                                                    fclBuyRates.remove(fclBuyCostTypeRates1);
                                                    recordsList.remove(ind);


                                                    codeList.remove(fclBuyCost);
                                                    for (int i = 0; i < codeList.size(); i++) {
                                                        FclBuyCost fcl = (FclBuyCost) codeList.get(i);
                                                    //if(fclBuyCost){


                                                    //}
                                                    }
                                                    session.removeAttribute("costcode");
                                                    session.setAttribute("costFutureCodeList", codeList);

                                                    if (codeList.size() < 1) {
                                                        session.removeAttribute("costFutureCodeList");
                                                    }
                                                    flag = true;
                                                    break;
                                                } else {
                                                    flag = true;
                                                    fclBuyRates.remove(fclBuyCostTypeRates1);
                                                    recordsList.remove(ind);
                                                    break;
                                                }

                                            }
                                        } else {

                                            if (fclBuyCostTypeRates1.getCostType().equals(fclBuyCost.getContType().getCodedesc())) {
                                                if (oldFclBuyCostTypeRates.getTypeId().equals(fclBuyCostTypeRates1.getTypeId())) {
                                                    fclBuyRates.remove(fclBuyCostTypeRates1);
                                                    recordsList.remove(ind);

                                                    codeList.remove(fclBuyCost);
                                                    session.removeAttribute("costcode");

                                                    session.setAttribute("costFutureCodeList", codeList);
                                                    if (codeList.size() < 1) {
                                                        session.removeAttribute("costFutureCodeList");
                                                    }
                                                    flag = true;
                                                    break;
                                                }
                                            }
                                        }
                                        session.setAttribute("fclrecordsfuture", recordsList);
                                    }
                                }//while

                            }//method
                        } //buy
                        else {
                            codeList = (List) session.getAttribute("costFutureCodeList");
                            List airFirghtList = new ArrayList();
                            airFirghtList = (List) session.getAttribute("fclfrightrecords");
                            if (airFirghtList != null && airFirghtList.size() > 0) {
                                oldfclBuyAirFreightCharges = (FclBuyAirFreightCharges) airFirghtList.get(ind);
                            }
                            int count1 = 0;
                            for (int i = 0; i < airFirghtList.size(); i++) {
                                FclBuyAirFreightCharges frt = (FclBuyAirFreightCharges) airFirghtList.get(i);

                                if (oldfclBuyAirFreightCharges != null && frt != null && oldfclBuyAirFreightCharges.getTypeId() != null && oldfclBuyAirFreightCharges.getTypeId().equals(frt.getTypeId())) {
                                    count1++;
                                }
                            }
                            fclBuyAirFright = fclBuyCost.getFclBuyAirFreightSet();
                            if (fclBuyAirFright != null) {
                                Iterator fclAirFright = fclBuyAirFright.iterator();
                                while (fclAirFright.hasNext()) {
                                    if (airFirghtList != null && airFirghtList.size() > 0) {
                                        oldfclBuyAirFreightCharges = (FclBuyAirFreightCharges) airFirghtList.get(ind);
                                    }
                                    FclBuyAirFreightCharges fclBuyAirFreightCharges1 = (FclBuyAirFreightCharges) fclAirFright.next();
                                    if (oldfclBuyAirFreightCharges != null && fclBuyAirFreightCharges1 != null && oldfclBuyAirFreightCharges.getFclCostTypeId() == fclBuyAirFreightCharges1.getFclCostTypeId()) {
                                        if (oldfclBuyAirFreightCharges.getWieghtRange().getId() == fclBuyAirFreightCharges1.getWieghtRange().getId()) {
                                            if (count1 == 1) {
                                                fclBuyAirFright.remove(fclBuyAirFreightCharges1);
                                                airFirghtList.remove(ind);

                                                for (int i = 0; i < codeList.size(); i++) {
                                                    FclBuyCost fcl = (FclBuyCost) codeList.get(i);
                                                //if(fclBuyCost){


                                                //}
                                                }
                                                codeList.remove(fclBuyCost);
                                                session.removeAttribute("costcode");

                                                for (int i = 0; i < codeList.size(); i++) {
                                                    FclBuyCost fclBuy1 = (FclBuyCost) codeList.get(i);


                                                }
                                                session.setAttribute("costFutureCodeList", codeList);
                                                if (codeList.size() < 1) {
                                                    session.removeAttribute("costFutureCodeList");
                                                }
                                                flag = true;
                                                break;
                                            } else {
                                                fclBuyAirFright.remove(fclBuyAirFreightCharges1);
                                                airFirghtList.remove(ind);
                                                flag = true;
                                                break;
                                            }


                                        }
                                    }


                                }
                                session.setAttribute("fclfrightrecords", airFirghtList);


                            }//method


                        }//else
                    }//flag
                }//for
            }//if
            request.setAttribute("addfclrecord", "addfclrecor");
        }//button

        if (buttonValue != null && buttonValue.equals("cancel")) {

            request.setAttribute("addfclrecord", "addfclrecor");
        }
        return mapping.getInputForward();
    }
}