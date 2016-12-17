package com.gp.cong.logisoft.struts.ratemangement.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.util.LabelValueBean;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.gp.cong.logisoft.util.DBUtil;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.logisoft.beans.NoteBean;
import com.gp.cong.logisoft.domain.FclBuy;
import com.gp.cong.logisoft.hibernate.dao.CustomerDAO;
import com.gp.cong.logisoft.hibernate.dao.FclBuyDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.FclBuyAirFreightCharges;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.domain.AuditLogRecord;
import com.gp.cong.logisoft.domain.FclBuyCostTypeRates;
import com.gp.cong.logisoft.domain.FclBuyCost;
import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.domain.AuditLogRecordFcl;
import com.gp.cong.logisoft.domain.TradingPartnerTemp;
import com.gp.cong.logisoft.domain.UnLocation;

import java.util.*;
import com.gp.cong.logisoft.struts.ratemangement.form.AddFCLRecordsForm;

import com.logiware.accounting.utils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

public class AddFCLRecordsAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception{
        AddFCLRecordsForm addFCLRecordsForm = (AddFCLRecordsForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        GenericCode genObj = new GenericCode();
        FclBuyCostTypeRates fclBuyCostTypeRates = new FclBuyCostTypeRates();
        FclBuyCost fclBuyCost = new FclBuyCost();
        FclBuyAirFreightCharges fclBuyAirFreightCharges = new FclBuyAirFreightCharges();
        FclBuy fclBuy = new FclBuy();
        List unittypelist = new ArrayList();
        FclBuyDAO fclBuyDAO = new FclBuyDAO();
        String buttonValue = addFCLRecordsForm.getButtonValue();
        String costcode = addFCLRecordsForm.getCostcode();
        String costtype = addFCLRecordsForm.getCosttype();
        String afamount = addFCLRecordsForm.getAfamount();
        String amount = addFCLRecordsForm.getAmount();
        String pcctc = addFCLRecordsForm.getPcctc();
        String contract = addFCLRecordsForm.getContact();
        String pcftf = addFCLRecordsForm.getPcftf();
        int count = 0;
        String index = addFCLRecordsForm.getIndex();
        String programid = null;
        String recordid = "";
        String buy = addFCLRecordsForm.getBuy();
        String pcretail = addFCLRecordsForm.getPcretail();
        String pccmi = addFCLRecordsForm.getPcminimun();
        DBUtil dbUtil = new DBUtil();
        String range = addFCLRecordsForm.getRange();
        ;
        String FORWARD = "";
        String trmNum = addFCLRecordsForm.getOrgTerminal();
        String portNum = addFCLRecordsForm.getDestnum();
        String comCode = addFCLRecordsForm.getComcode();
        String sslineNO = addFCLRecordsForm.getSslineno();
        String ssName = addFCLRecordsForm.getSslinename();
        String startDate = addFCLRecordsForm.getStartDate();
        String endDate = addFCLRecordsForm.getEndDate();
        String standard = addFCLRecordsForm.getStandard();
        String unittype = addFCLRecordsForm.getUnittype();
        GenericCode genericCo = null;
        String markup = addFCLRecordsForm.getMarkup();
        List recordsList = new ArrayList();
        Set fclset = new HashSet<FclBuyCostTypeRates>();
        List costCodeList = new ArrayList();
        String currency = addFCLRecordsForm.getCurrency();
        String effectiveDate = addFCLRecordsForm.getEffectiveDate();
        String descCode = "";
        CustomerDAO carriersOrLineDAO = new CustomerDAO();
        TradingPartnerTemp carriersOrLineTemp = new TradingPartnerTemp();
        UnLocationDAO refTerminalDAO = new UnLocationDAO();
        if (costtype != null && !costtype.equals("") && !costtype.equals("0")) {
            GenericCode gen = genericCodeDAO.findById(Integer.parseInt(costtype));
            if (gen != null) {
                descCode = gen.getCodedesc();
            }
        }
        if (session.getAttribute("addfclrecords") != null) {
            fclBuy = (FclBuy) session.getAttribute("addfclrecords");
        }

        fclBuy.setContract(contract);
        if (amount != null && !amount.equals("")) {
            fclBuyCostTypeRates.setActiveAmt(Double.parseDouble(amount));
        } else {

            fclBuyCostTypeRates.setActiveAmt(0.00);
        }
        if (standard != null && standard.equals("on")) {
            fclBuyCostTypeRates.setStandard("Y");
        } else {
            fclBuyCostTypeRates.setStandard("N");
        }
        if (markup != null && !markup.equals("")) {

            fclBuyCostTypeRates.setMarkup(Double.parseDouble(markup));
        } else {
            fclBuyCostTypeRates.setMarkup(0.00);
        }

        if (pcctc != null && !pcctc.equals("")) {
            fclBuyCostTypeRates.setCtcAmt(Double.parseDouble(pcctc));
        } else {
            fclBuyCostTypeRates.setCtcAmt(0.00);
        }
        if (pccmi != null && !pccmi.equals("")) {
            fclBuyCostTypeRates.setMinimumAmt(Double.parseDouble(pccmi));
        } else {
            fclBuyCostTypeRates.setMinimumAmt(0.00);
        }
        if (pcftf != null && !pcftf.equals("")) {
            fclBuyCostTypeRates.setFtfAmt(Double.parseDouble(pcftf));
        } else {
            fclBuyCostTypeRates.setFtfAmt(0.00);
        }
        if (pcretail != null && !pcretail.equals("")) {
            fclBuyCostTypeRates.setRatAmount(Double.parseDouble(pcretail));
        } else {
            fclBuyCostTypeRates.setRatAmount(0.00);
        }
        if (currency != null && !currency.equals("") && !currency.equals("0")) {
            GenericCode gen = genericCodeDAO.findById(Integer.parseInt(currency));
            if (gen != null) {
                fclBuyCostTypeRates.setCurrency(gen);
            }
        }
        if (effectiveDate != null && effectiveDate != "") {
            Date start = null;
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            start = sdf.parse(effectiveDate);
            fclBuyCostTypeRates.setEffectiveDate(start);
        }

        if (startDate != null && startDate != "") {
            Date start = null;
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            start = sdf.parse(startDate);
            fclBuy.setStartDate(start);
        }
        if (endDate != null && endDate != "") {
            Date end = null;
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            end = sdf.parse(endDate);
            fclBuy.setEndDate(end);
        }
        fclBuy.setContract(contract);
        session.setAttribute("con", fclBuy);

        //BUTTON FOR COST CODE YOU CAN SELECT COST CODE
        if (buttonValue.equals("costcode")) {
            if (costcode != null && !costcode.equals("0") && !costcode.equals("")) {
                if (startDate != null && startDate != "") {
                    Date start = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    start = sdf.parse(startDate);
                    fclBuy.setStartDate(start);
                }
                if (endDate != null && endDate != "") {
                    Date end = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    end = sdf.parse(endDate);
                    fclBuy.setEndDate(end);
                }

                GenericCode gen = null;
                genObj = genericCodeDAO.findById(Integer.parseInt(costcode));
                costCodeList = new ArrayList();
                boolean flag = false;
                if (session.getAttribute("costCodeList") != null) {
                    costCodeList = (List) session.getAttribute("costCodeList");
                    for (int i = 0; i < costCodeList.size(); i++) {
                        FclBuyCost fclBuy1 = (FclBuyCost) costCodeList.get(i);
                        if (fclBuy1.getCostId().getId().equals(genObj.getId())) {
                            session.setAttribute("costcode", fclBuy1);
                            flag = true;
                            break;
                        }
                    }
                }
                if (!flag) {
                    fclBuyCost.setCostId(genObj);
                    fclBuyCost.setContType(gen);
                    session.setAttribute("costcode", fclBuyCost);
                }

                request.setAttribute("start", startDate);
                request.setAttribute("end", endDate);
                FORWARD = "addfclrecords";

            }
        } //		BUTTON FOR COST TYPE YOU CAN SELECT COST TYPE
        else if (buttonValue != null && buttonValue.equals("costType")) {
            if (startDate != null && startDate != "") {
                Date start = null;
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                start = sdf.parse(startDate);
                fclBuy.setStartDate(start);
            }
            if (endDate != null && endDate != "") {
                Date end = null;
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                end = sdf.parse(endDate);
                fclBuy.setEndDate(end);
            }
            request.setAttribute("start", startDate);
            request.setAttribute("end", endDate);
            FORWARD = "addfclrecords";
            if (session.getAttribute("costCodeList") != null) {
                costCodeList = (List) session.getAttribute("costCodeList");
                for (int i = 0; i < costCodeList.size(); i++) {
                    fclBuyCost = (FclBuyCost) costCodeList.get(i);
                    if (fclBuyCost != null && fclBuyCost.getCostId() != null) {
                        if (fclBuyCost.getCostId().toString() != null && fclBuyCost.getContType() != null) {
                            if (fclBuyCost.getCostId().getId().toString().equals(costcode) && !fclBuyCost.getContType().getId().toString().equals(costtype)) {
                                count = 1;
                                request.setAttribute("message", "Already your selected different cost type for this cost code");
                                break;
                            }
                        }
                    }
                }
            }

            //THIS IF STATEMENT IS FOR CHECK COSTTYPE TO REDIRECTING TO ADDRECORDS.JSP
            if (request.getAttribute("message") == null) {
                if (costtype != null && !costtype.equals("") && !costtype.equals("0")) {

                    if (session.getAttribute("costcode") != null) {
                        fclBuyCost = (FclBuyCost) session.getAttribute("costcode");

                    } else {
                        fclBuyCost = new FclBuyCost();
                    }
                    GenericCode gen = genericCodeDAO.findById(Integer.parseInt(costtype));
                    fclBuyCost.setContType(gen);
                    session.setAttribute("costcode", fclBuyCost);
                }
            }

            request.setAttribute("start", startDate);
            request.setAttribute("end", endDate);

        } //		THIS IF STATEMENT IS FOR CHECK COSTTYPE AND CREATING SESSION
        else if (buttonValue != null && buttonValue.equals("add") && !buttonValue.equals("")) {

            if (session.getAttribute("costCodeList") != null) {
                costCodeList = (List) session.getAttribute("costCodeList");
            } else {
                costCodeList = new ArrayList();
            }
            if (descCode != null && descCode.trim().equalsIgnoreCase("PER CONTAINER SIZE")) {
                if (session.getAttribute("fclrecords") != null) {
                    recordsList = (List) session.getAttribute("fclrecords");
                } else {
                    recordsList = new ArrayList();
                }
                if (session.getAttribute("costcode") != null) {
                    fclBuyCost = (FclBuyCost) session.getAttribute("costcode");
                } else {
                    fclBuyCost = new FclBuyCost();
                }
                fclBuyCostTypeRates = new FclBuyCostTypeRates();
                if (unittype != null && !unittype.equals("0") && !unittype.equals("")) {
                    genericCo = genericCodeDAO.findById(Integer.parseInt(unittype));
                    fclBuyCostTypeRates.setUnitType(genericCo);
                }
                if (currency != null && !currency.equals("") && !currency.equals("0")) {
                    GenericCode gen = genericCodeDAO.findById(Integer.parseInt(currency));
                    if (gen != null) {
                        fclBuyCostTypeRates.setCurrency(gen);
                    }
                }
                if (effectiveDate != null && effectiveDate != "") {
                    Date start = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    start = sdf.parse(effectiveDate);
                    fclBuyCostTypeRates.setEffectiveDate(start);
                }

                if (startDate != null && startDate != "") {
                    Date start = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    start = sdf.parse(startDate);
                    fclBuy.setStartDate(start);
                }
                if (endDate != null && endDate != "") {
                    Date end = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    end = sdf.parse(endDate);
                    fclBuy.setEndDate(end);
                }
                if (amount != null && !amount.equals("")) {
                    fclBuyCostTypeRates.setActiveAmt(Double.parseDouble(amount));
                }
                if (standard != null && standard.equals("on")) {
                    fclBuyCostTypeRates.setStandard("Y");
                } else {
                    fclBuyCostTypeRates.setStandard("N");
                }
                if (markup != null && !markup.equals("")) {
                    fclBuyCostTypeRates.setMarkup(Double.parseDouble(markup));
                } else {
                    fclBuyCostTypeRates.setMarkup(0.00);
                }
                fclBuyCostTypeRates.setCostCode(fclBuyCost.getCostId().getCodedesc());
                fclBuyCostTypeRates.setCostType(fclBuyCost.getContType().getCodedesc());
                fclBuyCostTypeRates.setCostId(fclBuyCost.getContType().getId().toString());
                fclBuyCostTypeRates.setTypeId(fclBuyCost.getCostId().getId().toString());
                fclBuyCostTypeRates.setFclCostId(fclBuyCost.getFclStdId());
                recordsList.add(fclBuyCostTypeRates);

                session.setAttribute("fclrecords", recordsList);

                Set unitTypeSet = new HashSet<FclBuyCostTypeRates>();

                for (int i = 0; i < recordsList.size(); i++) {
                    FclBuyCostTypeRates fclBuyCostType = (FclBuyCostTypeRates) recordsList.get(i);
                    if (fclBuyCostType.getCostCode().equals(fclBuyCost.getCostId().getCodedesc()) && fclBuyCostType.getCostType().equals(fclBuyCost.getContType().getCodedesc())) {
                        unitTypeSet.add(fclBuyCostType);
                    }
                }

                fclBuyCost.setFclBuyUnitTypesSet(unitTypeSet);

            }//IF END
            else if (descCode != null && descCode.trim().equalsIgnoreCase("Air Freight Costs")) {
                if (session.getAttribute("costcode") != null) {
                    fclBuyCost = (FclBuyCost) session.getAttribute("costcode");
                } else {
                    fclBuyCost = new FclBuyCost();
                }

                if (currency != null && !currency.equals("") && !currency.equals("0")) {
                    GenericCode gen = genericCodeDAO.findById(Integer.parseInt(currency));
                    if (gen != null) {
                        fclBuyAirFreightCharges.setCurrency(gen);
                    }
                }
                if (effectiveDate != null && effectiveDate != "") {
                    Date start = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    start = sdf.parse(effectiveDate);
                }

                if (range != null && !range.equals("0") && !range.equals("")) {
                    genericCo = genericCodeDAO.findById(Integer.parseInt(range));
                    fclBuyAirFreightCharges.setWieghtRange(genericCo);
                }
                if (afamount != null) {
                    fclBuyAirFreightCharges.setRatAmount(Double.parseDouble(afamount));
                }
                fclBuyAirFreightCharges.setCostCode(fclBuyCost.getCostId().getCodedesc());
                fclBuyAirFreightCharges.setCostType(fclBuyCost.getContType().getCodedesc());
                fclBuyAirFreightCharges.setCostId(fclBuyCost.getContType().getId().toString());
                fclBuyAirFreightCharges.setTypeId(fclBuyCost.getCostId().getId().toString());
                if (session.getAttribute("fclfrightrecords") != null) {
                    recordsList = (List) session.getAttribute("fclfrightrecords");
                } else {
                    recordsList = new ArrayList();
                }
                recordsList.add(fclBuyAirFreightCharges);
                session.setAttribute("fclfrightrecords", recordsList);
                Set airFreightSet = new HashSet<FclBuyAirFreightCharges>();
                for (int i = 0; i < recordsList.size(); i++) {
                    FclBuyAirFreightCharges fclBuyAirFreight = (FclBuyAirFreightCharges) recordsList.get(i);
                    if (fclBuyAirFreight.getCostCode().equals(fclBuyCost.getCostId().getCodedesc()) &&
                            fclBuyAirFreight.getCostType().equals(fclBuyCost.getContType().getCodedesc())) {
                        airFreightSet.add(fclBuyAirFreight);
                    }
                }

                fclBuyCost.setFclBuyAirFreightSet(airFreightSet);
            }//END ELSE IF
            else {
                if (session.getAttribute("costcode") != null) {
                    fclBuyCost = (FclBuyCost) session.getAttribute("costcode");
                } else {
                    fclBuyCost = new FclBuyCost();
                }

                if (session.getAttribute("costCodeList") != null) {
                    costCodeList = (List) session.getAttribute("costCodeList");
                    FclBuyCost fclBuyCost1 = new FclBuyCost();
                    for (int i = 0; i < costCodeList.size(); i++) {
                        fclBuyCost1 = (FclBuyCost) costCodeList.get(i);
                        if (fclBuyCost1 != null && fclBuyCost1.getCostId() != null) {

                            if (fclBuyCost1.getCostId().toString() != null && fclBuyCost1.getContType() != null) {
                                if (fclBuyCost1.getCostId().getId().toString().equals(costcode) && fclBuyCost1.getContType().getId().toString().equals(costtype)) {
                                    request.setAttribute("message", "Already your selected different cost type for this cost code");
                                    count = 1;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (session.getAttribute("fclrecords") != null) {
                    recordsList = (List) session.getAttribute("fclrecords");
                } else {
                    recordsList = new ArrayList();
                }

                if (count != 1) {

                    if (currency != null && !currency.equals("") && !currency.equals("0")) {
                        GenericCode gen = genericCodeDAO.findById(Integer.parseInt(currency));
                        if (gen != null) {
                            fclBuyCostTypeRates.setCurrency(gen);
                        }
                    }
                    if (effectiveDate != null && effectiveDate != "") {
                        Date start = null;
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                        start = sdf.parse(effectiveDate);
                        fclBuyCostTypeRates.setEffectiveDate(start);
                    }
                    if (pcctc != null && !pcctc.equals("")) {
                        fclBuyCostTypeRates.setCtcAmt(Double.parseDouble(pcctc));
                    }
                    if (pccmi != null && !pccmi.equals("")) {
                        fclBuyCostTypeRates.setMinimumAmt(Double.parseDouble(pccmi));
                    }
                    if (pcftf != null && !pcftf.equals("")) {
                        fclBuyCostTypeRates.setFtfAmt(Double.parseDouble(pcftf));
                    }
                    if (pcretail != null && !pcretail.equals("")) {
                        fclBuyCostTypeRates.setRatAmount(Double.parseDouble(pcretail));
                    }
                    fclBuyCostTypeRates.setCostCode(fclBuyCost.getCostId().getCodedesc());
                    fclBuyCostTypeRates.setCostType(fclBuyCost.getContType().getCodedesc());
                    fclBuyCostTypeRates.setCostId(fclBuyCost.getContType().getId().toString());
                    fclBuyCostTypeRates.setTypeId(fclBuyCost.getCostId().getId().toString());
                    //fclBuyCostTypeRates.setFclCostId(fclBuyCost.getFclStdId());
                    if (count != 1) {
                        recordsList.add(fclBuyCostTypeRates);
                    }
                    session.setAttribute("fclrecords", recordsList);

                    for (int i = 0; i < recordsList.size(); i++) {
                        if (fclBuyCost.getCostId().getCodedesc().equals(fclBuyCostTypeRates.getCostCode())
                                && fclBuyCost.getContType().getCodedesc().equals(fclBuyCostTypeRates.getCostType())) {
                            fclset.add(fclBuyCostTypeRates);
                        }
                    }

                    fclBuyCost.setFclBuyUnitTypesSet(fclset);
                }
            }

            //THIS ELSE IF FOR CACULATION  Per Cubic Foot DATA
            costCodeList = new ArrayList();
            boolean b = false;
            if (session.getAttribute("costCodeList") != null) {
                costCodeList = (List) session.getAttribute("costCodeList");
                if (costcode != null && !costcode.equals("0") && !costcode.equals("")) {
                    GenericCode gen = null;
                    genObj = genericCodeDAO.findById(Integer.parseInt(costcode));
                }

                for (int k1 = 0; k1 < costCodeList.size(); k1++) {
                    FclBuyCost f1 = (FclBuyCost) costCodeList.get(k1);

                    if (genObj != null && f1 != null && genObj.getId() != null && genObj.getId().equals(f1.getCostId().getId())) {
                        b = true;
                    }
                }
                if (costCodeList.size() > 0) {
                    for (int k = 0; k < costCodeList.size(); k++) {
                        FclBuyCost f1 = (FclBuyCost) costCodeList.get(k);
                        if (!b) {
                            if (f1.getCostId().getId() != null && fclBuyCost.getCostId() != null) {
                                if (f1.getCostId().getId().toString().equals(fclBuyCost.getCostId().getId().toString())
                                        && f1.getContType().getId().toString().equals(fclBuyCost.getContType().getId().toString())) {
                                    costCodeList.remove(f1);
                                    costCodeList.add(fclBuyCost);
                                    break;
                                } else {
                                    costCodeList.add(fclBuyCost);
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    costCodeList.add(fclBuyCost);
                }
            } else {
                costCodeList.add(fclBuyCost);
            }
            session.setAttribute("costCodeList", costCodeList);

            request.setAttribute("start", startDate);
            request.setAttribute("end", endDate);
            FORWARD = "addfclrecords";
        } else if (buttonValue != null && !buttonValue.equals("") && buttonValue.equals("save")) {
            if (session.getAttribute("costcode") != null) {
                fclBuyCost = (FclBuyCost) session.getAttribute("costcode");
            } else {
                fclBuyCost = new FclBuyCost();
            }
            if (session.getAttribute("addfclrecords") != null) {

                fclBuy = (FclBuy) session.getAttribute("addfclrecords");
                fclBuy.setComNum(fclBuy.getComNum());
                fclBuy.setDestinationPort(fclBuy.getDestinationPort());
                fclBuy.setOriginTerminal(fclBuy.getOriginTerminal());
                fclBuy.setSslineNo(fclBuy.getSslineNo());
                fclBuy.setContract(contract);

                if (startDate != null && startDate != "") {
                    Date start = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    start = sdf.parse(startDate);
                    fclBuy.setStartDate(start);
                }
                if (endDate != null && endDate != "") {
                    Date end = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    end = sdf.parse(endDate);
                    fclBuy.setEndDate(end);
                }
            }
            if (fclBuy.getFclStdId() != null) {
                recordid = fclBuy.getFclStdId().toString();
            }
            programid = (String) session.getAttribute("processinfoforfl");

            dbUtil.getProcessInfo(programid, recordid, "deleted", "edited");

            if (session.getAttribute("costCodeList") != null) {
                costCodeList = (List) session.getAttribute("costCodeList");
                for (int i = 0; i < costCodeList.size(); i++) {
                    fclBuyCost = (FclBuyCost) costCodeList.get(i);
                    //fclBuyCost.setFclStdId(fclBuy.getFclStdId());
                    if (fclBuyCost.getContType() != null) {
                        costtype = fclBuyCost.getContType().getId().toString();
                    }
                    fclset.add(fclBuyCost);
                }
                fclBuy.setFclBuyCostsSet(fclset);

                fclBuyDAO.save(fclBuy);
                Map fclBuyMap = new HashMap<Integer, FclBuy>();
                if (!fclBuyMap.containsKey(fclBuy.getFclStdId())) {
                    fclBuyMap.put(fclBuy.getFclStdId(), fclBuy);
                }
                session.setAttribute("fclBuyMap", fclBuyMap);
                List CodeList = new ArrayList();
                List comList = new ArrayList();
                Set retrive = new HashSet<FclBuyCost>();
                if (session.getAttribute("searchFclcodelist") != null) {
                    CodeList = (List) session.getAttribute("searchFclcodelist");
                }
                session.setAttribute("searchFclcodelist", CodeList);
                session.setAttribute("fclcommonList", comList);

                List value = new ArrayList();
                unittypelist = dbUtil.getUnitListForFCLTest(new Integer(38), "yes", "Select Unit code");
                for (int i = 0; i < unittypelist.size(); i++) {
                    LabelValueBean removegenCo = (LabelValueBean) unittypelist.get(i);
                    if (!removegenCo.getValue().equals("0")) {
                        value.add(removegenCo.getLabel());
                    }
                }
                session.setAttribute("searchunittypelist", value);
                FclBuy fl = new FclBuy();
                fl.setComNum(fclBuy.getComNum());
                fl.setDestinationPort(fclBuy.getDestinationPort());
                fl.setOriginTerminal(fclBuy.getOriginTerminal());
                fl.setSslineNo(fclBuy.getSslineNo());

                session.setAttribute("searchfclrecords", fl);
                session.setAttribute("fclmessage", "Record has Added successfully");
            }

            if (session.getAttribute("con") != null) {
                session.removeAttribute("con");
            }
            if (session.getAttribute("editrecords") != null) {
                session.removeAttribute("editrecords");
            }
            if (session.getAttribute("fclfrightrecords") != null) {
                session.removeAttribute("fclfrightrecords");
            }
            if (session.getAttribute("addfclrecords") != null) {
                session.removeAttribute("addfclrecords");
            }
            if (session.getAttribute("fclfrightrecords") != null) {
                session.removeAttribute("fclfrightrecords");
            }
            if (session.getAttribute("fclrecords") != null) {
                session.removeAttribute("fclrecords");
            }

            FORWARD = "fclsearch";
        } else if (buttonValue != null && !buttonValue.equals("") && buttonValue.equals("edit")) {
            FclBuy newFclBuy = null;
            FclBuy oldFclBuy = null;
            if (session.getAttribute("addfclrecords") != null) {
                newFclBuy = (FclBuy) session.getAttribute("addfclrecords");
                oldFclBuy = fclBuyDAO.findById(newFclBuy.getFclStdId());
                oldFclBuy.setComNum(newFclBuy.getComNum());
                oldFclBuy.setDestinationPort(newFclBuy.getDestinationPort());
                oldFclBuy.setOriginTerminal(newFclBuy.getOriginTerminal());
                oldFclBuy.setSslineNo(newFclBuy.getSslineNo());
                oldFclBuy.setContract(contract);
                if (CommonUtils.isNotEmpty(startDate)) {
                    oldFclBuy.setStartDate(DateUtils.parseDate(startDate, "MM/dd/yyyy"));
                }
                if (CommonUtils.isNotEmpty(endDate)) {
                    oldFclBuy.setEndDate(DateUtils.parseDate(endDate, "MM/dd/yyyy"));
                }
                String fbcInclude ="costCode,costType,costId,costCodeDesc,currency,retail,futureRetail,effectiveDate";
                String fbctrInclude = "activeAmt,ctcAmt,markup,effectiveDate,currency,standard,ftfAmt,minimumAmt,ratAmount,oldAmount,oldMarkUp";
               
                for (Iterator<FclBuyCost> nfbcIt = newFclBuy.getFclBuyCostsSet().iterator(); nfbcIt.hasNext();) {
                    FclBuyCost newFclBuyCost = nfbcIt.next();
                    newFclBuyCost.setFclStdId(oldFclBuy.getFclStdId());
                    if (!oldFclBuy.getFclBuyCostsSet().contains(newFclBuyCost)) {
                        oldFclBuy.getFclBuyCostsSet().add(newFclBuyCost);
                    } else {
                        for (Iterator<FclBuyCost> ofbcIt = oldFclBuy.getFclBuyCostsSet().iterator(); ofbcIt.hasNext();) {
                            FclBuyCost oldFclBuyCost = ofbcIt.next();
                            if (CommonUtils.isEqual(oldFclBuyCost.getFclCostId(), newFclBuyCost.getFclCostId())) {
                                BeanUtils.copyProperties(newFclBuyCost, oldFclBuyCost, Arrays.asList(StringUtils.split(fbcInclude, ",")));
                                for (Iterator<FclBuyCostTypeRates> nfbctrIt = newFclBuyCost.getFclBuyUnitTypesSet().iterator(); nfbctrIt.hasNext();) {
                                    FclBuyCostTypeRates newFclBuyCostTypeRate = nfbctrIt.next();
                                    newFclBuyCostTypeRate.setFclCostId(oldFclBuyCost.getFclCostId());
                                    if (!oldFclBuyCost.getFclBuyUnitTypesSet().contains(newFclBuyCostTypeRate)) {
                                        oldFclBuyCost.getFclBuyUnitTypesSet().add(newFclBuyCostTypeRate);
                                    } else {
                                        for (Iterator<FclBuyCostTypeRates> ofbctrIt = oldFclBuyCost.getFclBuyUnitTypesSet().iterator(); ofbctrIt.hasNext();) {
                                            FclBuyCostTypeRates oldFclBuyCostTypeRate = ofbctrIt.next();
                                            if (CommonUtils.isEqual(oldFclBuyCostTypeRate.getFclCostTypeId(), newFclBuyCostTypeRate.getFclCostTypeId())) {
                                                BeanUtils.copyProperties(newFclBuyCostTypeRate, oldFclBuyCostTypeRate,Arrays.asList(StringUtils.split(fbctrInclude, ",")));
                                                System.out.println(oldFclBuyCostTypeRate.getActiveAmt() + "new");
                                            }
                                        }
                                    }
                                }
                                for (Iterator<FclBuyCostTypeRates> ofbctrIt = oldFclBuyCost.getFclBuyUnitTypesSet().iterator(); ofbctrIt.hasNext();) {
                                    FclBuyCostTypeRates oldFclBuyCostTypeRate = ofbctrIt.next();
                                    if (!newFclBuyCost.getFclBuyUnitTypesSet().contains(oldFclBuyCostTypeRate)) {
                                        ofbctrIt.remove();
                                    }
                                }
                            }
                        }
                        for (Iterator<FclBuyCost> ofbcIt = oldFclBuy.getFclBuyCostsSet().iterator(); ofbcIt.hasNext();) {
                            FclBuyCost oldFclBuyCost = ofbcIt.next();
                            if (!newFclBuy.getFclBuyCostsSet().contains(oldFclBuyCost)) {
                                ofbcIt.remove();
                            }
                        }
                    }
                }
                 new FclBuyDAO().update(oldFclBuy);
            }
           
            //updatefclBuy.setFclBuyCostsSet(null);
            Set fclBuycost1 = new HashSet<FclBuyCost>();
            if (session.getAttribute("costCodeList") != null) {
                if (newFclBuy != null && newFclBuy.getFclStdId() != null) {
                    recordid = newFclBuy.getFclStdId().toString();
                }
                programid = (String) session.getAttribute("processinfoforfl");
                dbUtil.getProcessInfo(programid, recordid, "deleted", "edited");
                List CodeList = new ArrayList();
                if (session.getAttribute("searchFclcodelist") != null) {
                    List newCodeList = (List) session.getAttribute("searchFclcodelist");
                    CodeList.addAll(newCodeList);
                    for (Iterator iter = newCodeList.iterator(); iter.hasNext();) {
                        FclBuyCost fclBuyCostId = (FclBuyCost) iter.next();
                        if (fclBuyCostId != null && newFclBuy != null && fclBuyCostId.getFclStdId() != null && newFclBuy.getFclStdId() != null && newFclBuy.getFclStdId().equals(fclBuyCostId.getFclStdId())) {
                            CodeList.remove(fclBuyCostId);
                        }
                    }
                }

                //t.commit();
                Set retrive = new HashSet<FclBuyCost>();
                FclBuy addrecords = fclBuyDAO.findById(newFclBuy.getFclStdId());
                if (addrecords.getFclBuyCostsSet() != null) {
                    retrive = addrecords.getFclBuyCostsSet();
                    Iterator it = retrive.iterator();
                    while (it.hasNext()) {
                        fclBuyCost = (FclBuyCost) it.next();
                        fclBuyCost.setFclStdId(addrecords.getFclStdId());
                        Iterator setIt = fclBuyCost.getFclBuyUnitTypesSet().iterator();
                        while (setIt.hasNext()) {
                            FclBuyCostTypeRates furate = (FclBuyCostTypeRates) setIt.next();
                            if (furate != null && fclBuyCost != null && furate.getFclCostId() != null && fclBuyCost.getFclCostId() != null && furate.getFclCostId().equals(fclBuyCost.getFclCostId())) {
                                CodeList.add(fclBuyCost);
                                break;
                            }
                        }
                    }
                }
                FclBuy fl = new FclBuy();
                fl.setComNum(newFclBuy.getComNum());
                fl.setDestinationPort(newFclBuy.getDestinationPort());
                fl.setOriginTerminal(newFclBuy.getOriginTerminal());

                fl.setSslineNo(newFclBuy.getSslineNo());
                session.setAttribute("searchFclcodelist", CodeList);
                List value = new ArrayList();
                unittypelist = dbUtil.getUnitListForFCLTest(new Integer(38), "yes", "Select Unit code");
                for (int i = 0; i < unittypelist.size(); i++) {
                    LabelValueBean removegenCo = (LabelValueBean) unittypelist.get(i);
                    if (!removegenCo.getValue().equals("0")) {
                        value.add(removegenCo.getLabel());
                    }
                }
                session.setAttribute("searchunittypelist", value);
                session.setAttribute("fclmessage", "Record has Edited successfully");
                if (session.getAttribute("con") != null) {
                    session.removeAttribute("con");
                }
                if (session.getAttribute("editrecords") != null) {
                    session.removeAttribute("editrecords");
                }
                if (session.getAttribute("fclfrightrecords") != null) {
                    session.removeAttribute("fclfrightrecords");
                }
                if (session.getAttribute("addfclrecords") != null) {
                    session.removeAttribute("addfclrecords");
                }
                //if(session.getAttribute("costCodeList")!=null)session.removeAttribute("costCodeList");
                if (session.getAttribute("fclfrightrecords") != null) {
                    session.removeAttribute("fclfrightrecords");
                }
                if (session.getAttribute("fclrecords") != null) {
                    session.removeAttribute("fclrecords");
                }
                if (session.getAttribute("fclrecords") != null) {
                    session.removeAttribute("fclrecords");
                }

            }//if for session

            FORWARD = "fclsearch";
        } else if (buttonValue != null && buttonValue.equals("delete")) {
            if (session.getAttribute("addfclrecords") != null) {

                fclBuy = (FclBuy) session.getAttribute("addfclrecords");
            }

            if (fclBuy.getFclStdId() != null) {
                recordid = fclBuy.getFclStdId().toString();
            }
            programid = (String) session.getAttribute("processinfoforfl");
            dbUtil.getProcessInfo(programid, recordid, "deleted", "edited");
            if (fclBuy.getComNum() != null && fclBuy.getComNum().getCode().equals("000000")) {
                if (session.getAttribute("fclcommonList") != null) {
                    session.removeAttribute("fclcommonList");
                }
            } else {
                if (session.getAttribute("searchFclcodelist") != null) {
                    session.removeAttribute("searchFclcodelist");
                }
            }
            fclBuyDAO.delete(fclBuy);
            session.setAttribute("fclmessage", "Record has deleted successfully");
            if (session.getAttribute("editrecords") != null) {
                session.removeAttribute("editrecords");
            }
            if (session.getAttribute("fclfrightrecords") != null) {
                session.removeAttribute("fclfrightrecords");
            }
            if (session.getAttribute("costcode") != null) {
                session.removeAttribute("costcode");
            }
            if (session.getAttribute("costCodeList") != null) {
                session.removeAttribute("costCodeList");
            }
            if (session.getAttribute("fclfrightrecords") != null) {
                session.removeAttribute("fclfrightrecords");
            }
            if (session.getAttribute("unittypelist") != null) {
                session.removeAttribute("unittypelist");
            }
            if (session.getAttribute("fclrecords") != null) {
                session.removeAttribute("fclrecords");
            }

            if (session.getAttribute("con") != null) {
                session.removeAttribute("con");
            }
            if (session.getAttribute("addfclrecords") != null) {
                session.removeAttribute("addfclrecords");
            }
            if (session.getAttribute("searchFclcodelist") != null) {
                session.removeAttribute("searchFclcodelist");
            }
            if (session.getAttribute("searchunittypelist") != null) {
                session.removeAttribute("searchunittypelist");
            }
            FORWARD = "fclsearch";
        }

        if (buttonValue != null && buttonValue.equals("index")) {
            if (buy != null && !buy.equals("")) {

                request.setAttribute("buy", "buy");
            }
            FORWARD = "addfclrecords";
        }
        if (buttonValue != null && buttonValue.equals("index")) {
            if (buy != null && !buy.equals("")) {

                request.setAttribute("buy", "buy");
            }
            FORWARD = "addfclrecords";
        } else if (buttonValue != null && buttonValue.equals("cancel")) {
            if (session.getAttribute("addfclrecords") != null) {
                fclBuy = (FclBuy) session.getAttribute("addfclrecords");
            }
            if (fclBuy.getFclStdId() != null) {
                recordid = fclBuy.getFclStdId().toString();
            }
            programid = (String) session.getAttribute("processinfoforfl");

            dbUtil.getProcessInfo(programid, recordid, "deleted", "edited");
            if (session.getAttribute("usermessage") != null) {
                session.removeAttribute("usermessage");
            }
            if (session.getAttribute("con") != null) {
                session.removeAttribute("con");
            }
            if (session.getAttribute("costCodeList") != null) {
                session.removeAttribute("costCodeList");
            }
            if (session.getAttribute("fclrecords") != null) {
                session.removeAttribute("fclrecords");
            }
            if (request.getAttribute("usermessage") != null) {
                session.removeAttribute("usermessage");
            }
            if (request.getAttribute("view") != null) {
                session.removeAttribute("view");
            }
            if (session.getAttribute("fclfrightrecords") != null) {
                session.removeAttribute("fclfrightrecords");
            }
            if (session.getAttribute("costcode") != null) {
                session.removeAttribute("costcode");
            }
            if (session.getAttribute("addfclrecords") != null) {
                session.removeAttribute("addfclrecords");
            }
            if (session.getAttribute("fclrecords") != null) {
                session.removeAttribute("fclrecords");
            }

            FORWARD = "fclsearch";

        } else if (buttonValue != null && buttonValue.equals("note")) {
            if (session.getAttribute("addfclrecords") != null) {
                fclBuy = (FclBuy) session.getAttribute("addfclrecords");
            }

            ItemDAO itemDAO = new ItemDAO();
            Item item = new Item();
            String itemName = "";
            if (session.getAttribute("processinfoforfl") != null) {
                String itemId = (String) session.getAttribute("processinfoforfl");
                item = itemDAO.findById(Integer.parseInt(itemId));
                itemName = item.getItemDesc();

            }

            AuditLogRecord auditLogRecord = new AuditLogRecordFcl();
            NoteBean noteBean = new NoteBean();
            noteBean.setItemName(itemName);
            noteBean.setAuditLogRecord(auditLogRecord);
            noteBean.setButtonValue(buttonValue);
            //noteBean.setUser(user);
            noteBean.setPageName("cancelFcldetails");
            String noteId = "";

            if (fclBuy.getFclStdId() != null) {
                noteId = fclBuy.getFclStdId().toString();

                noteBean.setNoteId(noteId);
                noteBean.setReferenceId(noteId);
            }

            List auditList = null;

            auditList = dbUtil.getNoteInformation(noteId, auditLogRecord);
            noteBean.setAuditList(auditList);
            noteBean.setVoidednote("");
            request.setAttribute("noteBean", noteBean);
            request.setAttribute("buttonValue", buttonValue);

            if (fclBuy.getFclStdId() != null) {
                recordid = fclBuy.getFclStdId().toString();
            }
            programid = (String) session.getAttribute("processinfoforfl");
            dbUtil.getProcessInfo(programid, recordid, "deleted", "edited");

            FORWARD = "note";

        } else if (buttonValue != null && buttonValue.equals("copy")) {
            FclBuy fcll = new FclBuy();
            fcll = (FclBuy) session.getAttribute("addfclrecords");
            fcll.getFclStdId();
            session.setAttribute("getBackAddFcl", fcll.getFclStdId());
            FORWARD = "copy";

        } else if (buttonValue != null && !buttonValue.equals("") && buttonValue.equals("copysave")) {
            boolean flag1 = false;
            boolean send = false;
            List list = new ArrayList();
            DBUtil dbutil = new DBUtil();
            if (startDate != null && startDate != "") {
                Date start = null;
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                start = sdf.parse(startDate);
                fclBuy.setStartDate(start);
            }
            if (endDate != null && endDate != "") {
                Date end = null;
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                end = sdf.parse(endDate);
                fclBuy.setEndDate(end);
            }
            fclBuy.setContract(contract);
            if (session.getAttribute("costcode") != null) {
                fclBuyCost = (FclBuyCost) session.getAttribute("costcode");
            } else {
                fclBuyCost = new FclBuyCost();
            }

            if (trmNum != null && !trmNum.equals("")) {

                List unitTypeList = refTerminalDAO.findForManagement(trmNum, null);
                if (unitTypeList != null && unitTypeList.size() > 0) {
                    UnLocation refTerminal = (UnLocation) unitTypeList.get(0);
                    fclBuy.setOriginTerminal(refTerminal);
                    session.setAttribute("addfclrecords", fclBuy);
                } else {
                    send = true;
                }
            } else {
                //RefTerminalTemp r1=null;
                //fclBuy.setOriginTerminal(r1);
                //session.setAttribute("addfclrecords", fclBuy);
            }
            if (portNum != null && !portNum.equals("")) {
                List portsList = refTerminalDAO.findForManagement(portNum, null);
                if (portsList != null && portsList.size() > 0) {
                    UnLocation ports = (UnLocation) portsList.get(0);
                    fclBuy.setDestinationPort(ports);
                    session.setAttribute("addfclrecords", fclBuy);
                } else {
                    send = true;
                }
            } else {
                //PortsTemp p1=null;
                //fclBuy.setDestinationPort(p1);
                //session.setAttribute("addfclrecords", fclBuy);
            }

            if (comCode != null && !comCode.equals("")) {
                List comList = genericCodeDAO.findForGenericCode(comCode);
                if (comList != null && comList.size() > 0) {
                    GenericCode gen = (GenericCode) comList.get(0);
                    fclBuy.setComNum(gen);
                    session.setAttribute("addfclrecords", fclBuy);
                } else {
                    //GenericCode g1=null;
                    //fclBuy.setComNum(g1);
                    //session.setAttribute("addfclrecords", fclBuy);
                    send = true;
                    //request.setAttribute("alertmsg", "Enter Proper Commodity ");
                }
            } else {
                //GenericCode g1=null;
                //fclBuy.setComNum(g1);
                //session.setAttribute("addfclrecords", fclBuy);
            }
            if (sslineNO != null && !sslineNO.equals("") || (ssName != null && !ssName.equals(""))) {

                List SSNo = carriersOrLineDAO.findAccountNo1(sslineNO);
                if (SSNo != null && SSNo.size() > 0) {
                    carriersOrLineTemp = (TradingPartnerTemp) SSNo.get(0);
                    fclBuy.setSslineNo(carriersOrLineTemp);
                    session.setAttribute("addfclrecords", fclBuy);
                } else {
                    send = true;
                }
            } else {
                //CarriersOrLineTemp c1=null;
                //fclBuy.setSslineNo(c1);
                //session.setAttribute("addfclrecords", fclBuy);
            }
            if (session.getAttribute("addfclrecords") != null) {

                fclBuy = (FclBuy) session.getAttribute("addfclrecords");
                fclBuy.setComNum(fclBuy.getComNum());
                fclBuy.setDestinationPort(fclBuy.getDestinationPort());
                fclBuy.setOriginTerminal(fclBuy.getOriginTerminal());
                fclBuy.setSslineNo(fclBuy.getSslineNo());
                fclBuy.setContract(contract);

                if (startDate != null && startDate != "") {
                    Date start = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    start = sdf.parse(startDate);
                    fclBuy.setStartDate(start);
                }
                if (endDate != null && endDate != "") {
                    Date end = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    end = sdf.parse(endDate);
                    fclBuy.setEndDate(end);
                }
            }
            if (!flag1 && !send && index != null && index.equals("getInster")) {

                if (fclBuy != null) {
                    list = dbutil.getFCLDetails(fclBuy.getOriginTerminal(), fclBuy.getDestinationPort(), fclBuy.getComNum(), fclBuy.getSslineNo(), fclBuy.getOriginalRegion(), fclBuy.getDestinationRegion());
                    if (list != null && list.size() > 0) {
                        flag1 = true;
                        request.setAttribute("message", "This records is exists Please Change Org Trm or Dest Port or Commodity Code");
                    }
                }
                if (fclBuy.getFclStdId() != null) {
                    recordid = fclBuy.getFclStdId().toString();
                }
                programid = (String) session.getAttribute("processinfoforfl");

                dbUtil.getProcessInfo(programid, recordid, "deleted", "edited");

                if (session.getAttribute("fclcommonList") != null) {

                    session.removeAttribute("fclcommonList");
                }
                if (session.getAttribute("costCodeList") != null) {
                    costCodeList = (List) session.getAttribute("costCodeList");
                    for (int i = 0; i < costCodeList.size(); i++) {
                        fclBuyCost = (FclBuyCost) costCodeList.get(i);
                        //fclBuyCost.setFclStdId(fclBuy.getFclStdId());
                        if (fclBuyCost.getContType() != null) {
                            costtype = fclBuyCost.getContType().getId().toString();
                        }
                        fclset.add(fclBuyCost);
                    }
                    fclBuy.setFclBuyCostsSet(fclset);

                    fclBuyDAO.save(fclBuy);

                    List CodeList = new ArrayList();
                    List comList = new ArrayList();
                    Set retrive = new HashSet<FclBuyCost>();

                    if (fclBuy.getComNum() != null && fclBuy.getComNum().getCode().equals("000000")) {
                        genObj = genericCodeDAO.findById(11292);
                        List lii = fclBuyDAO.findForSearchFclBuyRatesMatch(fclBuy.getOriginTerminal(), fclBuy.getDestinationPort(), genObj, null);
                        for (int i = 0; i < lii.size(); i++) {
                            FclBuy fby = (FclBuy) lii.get(i);
                            if (fby.getFclBuyCostsSet() != null) {
                                retrive = fby.getFclBuyCostsSet();
                                Iterator it = retrive.iterator();
                                while (it.hasNext()) {
                                    fclBuyCost = (FclBuyCost) it.next();
                                    fclBuyCost.setFclStdId(fby.getFclStdId());
                                    comList.add(fclBuyCost);

                                }
                            }

                        }

                    } else {
                        genObj = genericCodeDAO.findById(11292);
                        List lii = fclBuyDAO.findForSearchFclBuyRatesMatch(fclBuy.getOriginTerminal(), fclBuy.getDestinationPort(), genObj, null);
                        for (int i = 0; i < lii.size(); i++) {
                            FclBuy fby = (FclBuy) lii.get(i);
                            if (fby.getFclBuyCostsSet() != null) {
                                retrive = fby.getFclBuyCostsSet();
                                Iterator it = retrive.iterator();
                                while (it.hasNext()) {
                                    fclBuyCost = (FclBuyCost) it.next();
                                    fclBuyCost.setFclStdId(fby.getFclStdId());
                                    comList.add(fclBuyCost);

                                }
                            }

                        }
                        if (fclBuy.getFclBuyCostsSet() != null) {
                            retrive = fclBuy.getFclBuyCostsSet();
                            Iterator it = retrive.iterator();
                            while (it.hasNext()) {
                                fclBuyCost = (FclBuyCost) it.next();
                                fclBuyCost.setFclStdId(fclBuy.getFclStdId());
                                CodeList.add(fclBuyCost);

                            }
                        }
                    }
                    session.setAttribute("searchFclcodelist", CodeList);
                    session.setAttribute("fclcommonList", comList);

                    List value = new ArrayList();
                    unittypelist = dbUtil.getUnitListForFCLTest(new Integer(38), "yes", "Select Unit code");
                    for (int i = 0; i < unittypelist.size(); i++) {
                        LabelValueBean removegenCo = (LabelValueBean) unittypelist.get(i);
                        if (!removegenCo.getValue().equals("0")) {
                            value.add(removegenCo.getLabel());
                        }
                    }
                    session.setAttribute("searchunittypelist", value);
                    FclBuy fl = new FclBuy();
                    fl.setComNum(fclBuy.getComNum());
                    fl.setDestinationPort(fclBuy.getDestinationPort());
                    fl.setOriginTerminal(fclBuy.getOriginTerminal());
                    fl.setSslineNo(fclBuy.getSslineNo());

                    session.setAttribute("searchfclrecords", fl);
                    session.setAttribute("fclmessage", "Record has Added successfully");

                }

                if (session.getAttribute("con") != null) {
                    session.removeAttribute("con");
                }
                if (session.getAttribute("editrecords") != null) {
                    session.removeAttribute("editrecords");
                }
                if (session.getAttribute("fclfrightrecords") != null) {
                    session.removeAttribute("fclfrightrecords");
                }
                if (session.getAttribute("addfclrecords") != null) {
                    session.removeAttribute("addfclrecords");
                }
                if (session.getAttribute("fclfrightrecords") != null) {
                    session.removeAttribute("fclfrightrecords");
                }
                if (session.getAttribute("fclrecords") != null) {
                    session.removeAttribute("fclrecords");
                }
                if (session.getAttribute("getBackAddFcl") != null) {
                    session.removeAttribute("getBackAddFcl");
                }
                FORWARD = "fclsearch";
            } else {

                FORWARD = "existrecords";

            }
        } else if (buttonValue != null && buttonValue.equals("copycancel")) {

            if (session.getAttribute("addfclrecords") != null) {
                session.removeAttribute("addfclrecords");

            }
            if (session.getAttribute("con") != null) {
                session.removeAttribute("con");
            }
            if (session.getAttribute("getBackAddFcl") != null) {
                FclBuy fclb = new FclBuy();
                Integer i = (Integer) session.getAttribute("getBackAddFcl");
                FclBuyDAO fcl = new FclBuyDAO();
                fclb = fcl.findById(i.valueOf(i));
                session.setAttribute("addfclrecords", fclb);
                session.setAttribute("con", fclb);
            }

            FORWARD = "copycancel";

        }
        if (request.getAttribute("view") != null) {
            session.removeAttribute("view");
        }

        return mapping.findForward(FORWARD);
    }
}
