package com.gp.cong.logisoft.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
///import com.moneydance.util.StringUtils;


import org.apache.commons.lang3.StringUtils;

import com.gp.cong.logisoft.domain.FclBuy;
import com.gp.cong.logisoft.domain.FclBuyCost;
import com.gp.cong.logisoft.domain.FclBuyCostTypeRates;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.TradingPartnerTemp;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.hibernate.dao.FclBuyDAO;

/**
 * @author Vasan
 * This class was created to load the aplrates.
 * The excel reader will create the object and this 
 * class will insert into the database.  
 * 
 * */
public class LoadAPLRates {

    LoadAPLRatesHelper loadAPLRatesHelper = null;
    FclBuy fclBuy = null;
    DBUtil dbUtil = null;
    FclBuyDAO fclBuyDAO = null;
    //ArrayList<String> denialRecordList = null;
    Set<String> denialRecordList = null;
    Set<FclBuyCost> fclBuyCostSet = null;
    Map<String, FclBuyCost> fclBuyCostMap = null;

    public LoadAPLRates() {
        loadAPLRatesHelper = new LoadAPLRatesHelper();
        dbUtil = new DBUtil();
        fclBuyDAO = new FclBuyDAO();
        //denialRecordList = new ArrayList<String>();
        denialRecordList = new HashSet<String>();
        fclBuyCostMap = new HashMap<String, FclBuyCost>();
    }

    public Set<String> validate(List<APLRatesUSCargo> aplRatesUSCargoList) throws Exception {
        for (Iterator iter = aplRatesUSCargoList.iterator(); iter.hasNext();) {
            APLRatesUSCargo aplRatesUSCargo = (APLRatesUSCargo) iter.next();

            if (isNotNull(aplRatesUSCargo.getOriginCode().trim())) {
                UnLocation refTerminalTemp = loadAPLRatesHelper.getRefTerminalTemp(aplRatesUSCargo.getOriginCode());
                if (refTerminalTemp == null) {
                    denialRecordList.add("The Terminal '" + aplRatesUSCargo.getOriginCode() + "' is not in our Database.");
                }
            } else {
                denialRecordList.add("The Terminal fileds are Empty.");
            }
            if (isNotNull(aplRatesUSCargo.getDestinationCode().trim())) {
                String d[] = StringUtils.splitPreserveAllTokens(aplRatesUSCargo.getDestinationCode(), '-');
                String port, control = null;
                if (d != null && d.length > 1) {
                    port = d[0];
                    control = d[1];
                } else {
                    port = aplRatesUSCargo.getDestinationCode();
                }
                UnLocation portsTemp = loadAPLRatesHelper.getPortsTemp(port, null);
                if (portsTemp == null) {
                    denialRecordList.add("The Destination '" + aplRatesUSCargo.getDestinationCode() + "' is not in our Database");
                }

            } else {
                denialRecordList.add("The Destination fileds are Empty.");
            }
            if (isNotNull(aplRatesUSCargo.getCommodityCode().trim())) {
                GenericCode genericCode = loadAPLRatesHelper.getGenericComCode(aplRatesUSCargo.getCommodityCode());
                if (genericCode == null) {
                    denialRecordList.add("The Commodity Code '" + aplRatesUSCargo.getCommodityCode() + "' is not in our Database");
                }
            } else {
                denialRecordList.add("The Commodity fileds are Empty.");
            }


            if (isNotNull(aplRatesUSCargo.getSteamshipLine().trim())) {
                TradingPartnerTemp carriersOrLineTemp = loadAPLRatesHelper.getStreamShipLine(aplRatesUSCargo.getSteamshipLine());
                if (carriersOrLineTemp == null) {
                    denialRecordList.add("The Stream ShipLine '" + aplRatesUSCargo.getSteamshipLine() + "' is not in our Database");
                }

            } else {
                denialRecordList.add("The SSLine fileds are Empty.");
            }

            for (int costCodeCount = 0; costCodeCount < aplRatesUSCargo.getCostCodes().size(); costCodeCount++) {
                String costCode = aplRatesUSCargo.getCostCodes().get(costCodeCount);
                String costType = null, contentType = null, amount = null, currency = null, mintype = null, minamt = null;
                if (isNotNull(costCode.trim())) {
                    String costCodeSplitValues[] = StringUtils.splitPreserveAllTokens(costCode, '/');
                    if (costCodeSplitValues.length < 3) {
                        denialRecordList.add("The '" + costCode + "' format is incorrect.");
                    }
                    if (costCodeSplitValues.length > 2 && isNotNull(costCodeSplitValues[2])) {
                        costType = costCodeSplitValues[0];
                        contentType = costCodeSplitValues[1];
                        amount = costCodeSplitValues[2];

                        if (isNotNull(amount) && !amount.trim().equals("")) {
                            double amt = Double.parseDouble(amount);
                        }
                        if (costCodeSplitValues.length == 4) {
                            currency = costCodeSplitValues[3];

                        } else if (costCodeSplitValues.length == 5) {
                            mintype = costCodeSplitValues[3];
                            minamt = costCodeSplitValues[4];
                        } else if (costCodeSplitValues.length == 6) {
                            mintype = costCodeSplitValues[3];
                            minamt = costCodeSplitValues[4];
                            currency = costCodeSplitValues[5];
                        }
                        if (isNotNull(currency)) {
                            GenericCode genericCodeForCurrency = loadAPLRatesHelper.getCurrency(currency);
                            if (genericCodeForCurrency == null) {
                                denialRecordList.add("The currency  '" + currency + "' is not in our Database");
                            }
                        }
                        GenericCode genericCostCode = loadAPLRatesHelper.getCostID(costType);
                        if (genericCostCode == null) {
                            denialRecordList.add("The Cost Code '" + costCode + "' is not in our Database");
                        }
                        // Validation is for Unit Type(Container type Ex:-A,B,C)
                        if (contentType != null && !contentType.equals("") && !contentType.equals("*") && !contentType.equals("%") && !contentType.equals("#")) {
                            GenericCode contentTypeGeneriCode = loadAPLRatesHelper.getContentTypeID(contentType);
                            if (contentTypeGeneriCode == null) {
                                denialRecordList.add("The Unit Type '" + contentType + "' is not in our Database");
                            }
                        }

                    }//if
                }// isNotNull
            }
        }
        return denialRecordList;
    }

    public void execute(List<APLRatesUSCargo> aplRatesUSCargoList) throws Exception {
        for (Iterator iter = aplRatesUSCargoList.iterator(); iter.hasNext();) {
            APLRatesUSCargo aplRatesUSCargo = (APLRatesUSCargo) iter.next();
            fclBuy = new FclBuy();
            fclBuyCostSet = new HashSet<FclBuyCost>();

            //Origin Code -- Cell1 --
            if (isNotNull(aplRatesUSCargo.getOriginCode().trim())) {
                UnLocation refTerminalTemp = loadAPLRatesHelper.getRefTerminalTemp(aplRatesUSCargo.getOriginCode());

                if (refTerminalTemp != null) {
                    fclBuy.setOriginTerminal(refTerminalTemp);
                }
            }

            //Destination --Cell3 --
            if (isNotNull(aplRatesUSCargo.getDestinationCode().trim())) {
                String d[] = StringUtils.splitPreserveAllTokens(aplRatesUSCargo.getDestinationCode(), '-');
                String port, control = null;
                if (d != null && d.length > 1) {
                    port = d[0];
                    control = d[1];
                } else {
                    port = aplRatesUSCargo.getDestinationCode();
                }
                fclBuy.setDestinationPort(loadAPLRatesHelper.getPortsTemp(port, control));
            }


            //Commodity Code --Cell5--
            if (isNotNull(aplRatesUSCargo.getCommodityCode().trim())) {
                fclBuy.setComNum(loadAPLRatesHelper.getGenericComCode(aplRatesUSCargo.getCommodityCode()));
            }

            //StreamShip Line# --Cell6--
            if (isNotNull(aplRatesUSCargo.getSteamshipLine().trim())) {
                fclBuy.setSslineNo(loadAPLRatesHelper.getStreamShipLine(aplRatesUSCargo.getSteamshipLine()));
            }

            //Contract --Cell7--
            if (isNotNull(aplRatesUSCargo.getContractNo().trim())) {
                fclBuy.setContract(aplRatesUSCargo.getContractNo());
            }

            //Start Date --Cell8-
            fclBuy.setStartDate(aplRatesUSCargo.getStartDate());

            //End Date --Cell9-
            fclBuy.setEndDate(aplRatesUSCargo.getEndDate());

            for (int costCodeCount = 0; costCodeCount < aplRatesUSCargo.getCostCodes().size(); costCodeCount++) {
                String costCode = aplRatesUSCargo.getCostCodes().get(costCodeCount);
                String costType = null, contentType = null, amount = null, currency = null, mintype = null, minamt = null;
                if (isNotNull(costCode.trim())) {
                    String costCodeSplitValues[] = StringUtils.splitPreserveAllTokens(costCode, '/');
                    if (costCodeSplitValues.length > 2 && isNotNull(costCodeSplitValues[2])) {
                        costType = costCodeSplitValues[0];
                        contentType = costCodeSplitValues[1];
                        amount = costCodeSplitValues[2];

                        if (costCodeSplitValues.length == 4) {
                            currency = costCodeSplitValues[3];
                        } else if (costCodeSplitValues.length == 5) {
                            mintype = costCodeSplitValues[3];
                            minamt = costCodeSplitValues[4];

                        } else if (costCodeSplitValues.length == 6) {
                            mintype = costCodeSplitValues[3];
                            minamt = costCodeSplitValues[4];
                            currency = costCodeSplitValues[5];
                        }

                        GenericCode genericCostCode = loadAPLRatesHelper.getCostID(costType);

                        if (fclBuyCostMap.get(costType) == null) {
                            FclBuyCost fclBuyCost = new FclBuyCost();//-->OFR
                            fclBuyCost.setCostId(genericCostCode);
                            fclBuyCost.setContType(loadAPLRatesHelper.getCostTypeID(contentType));
                            fclBuyCostMap.put(costType, fclBuyCost);
                            fclBuyCostSet.add(fclBuyCost);
                        }


                        FclBuyCostTypeRates fclBuyCostTypeRates = new FclBuyCostTypeRates();
                        GenericCode contentTypeGeneriCode = loadAPLRatesHelper.getContentTypeID(contentType);
                        fclBuyCostTypeRates.setUnitType(contentTypeGeneriCode);

                        if (contentTypeGeneriCode != null) {
                            if (isNotNull(amount.trim())) {
                                fclBuyCostTypeRates.setActiveAmt(Double.valueOf(Double.parseDouble(amount)));
                            } else {
                                fclBuyCostTypeRates.setActiveAmt(Double.valueOf(0.0D));
                            }
                        } else if (amount != null && !amount.trim().equals("")) {
                            fclBuyCostTypeRates.setRatAmount(Double.valueOf(Double.parseDouble(amount)));
                        } else {
                            fclBuyCostTypeRates.setRatAmount(Double.valueOf(0.0D));
                        }

                        if (isNotNull(currency)) {
                            fclBuyCostTypeRates.setCurrency(loadAPLRatesHelper.getCurrency(currency));
                        } else {
                            fclBuyCostTypeRates.setCurrency(loadAPLRatesHelper.getCurrency("USD"));
                        }
                        if (minamt != null && !minamt.trim().equals("")) {
                            fclBuyCostTypeRates.setMinimumAmt(Double.valueOf(Double.parseDouble(minamt)));
                        }
                        fclBuyCostTypeRates.setStandard("Y");

                        if (amount != null && !amount.trim().equals("")) {
                            FclBuyCost fclBuyCost = fclBuyCostMap.get(costType);
                            fclBuyCostTypeRates.setFclCostId(fclBuyCost.getFclCostId());
                            fclBuyCost.getFclBuyUnitTypesSet().add(fclBuyCostTypeRates);
                        }
                    }
                }
            }

            fclBuy.setFclBuyCostsSet(fclBuyCostSet);

            List fclDetailsList = dbUtil.getFCLDetails(fclBuy.getOriginTerminal(), fclBuy.getDestinationPort(), fclBuy.getComNum(), fclBuy.getSslineNo(), fclBuy.getOriginalRegion(), fclBuy.getDestinationRegion());
            if (fclBuy.getOriginTerminal() != null && fclBuy.getDestinationPort() != null && fclBuy.getComNum() != null && fclBuy.getSslineNo() != null) {
                if (!fclDetailsList.isEmpty()) {
                    //this is to remove the child records.
                    FclBuy fclBuyObjFromDB = (FclBuy) fclDetailsList.get(0);
                    fclBuyObjFromDB.setOriginTerminal(fclBuy.getOriginTerminal());
                    fclBuyObjFromDB.setDestinationPort(fclBuy.getDestinationPort());
                    fclBuyObjFromDB.setComNum(fclBuy.getComNum());
                    fclBuyObjFromDB.setSslineNo(fclBuy.getSslineNo());
                    fclBuyObjFromDB.setContract(fclBuy.getContract());
                    fclBuyObjFromDB.setStartDate(fclBuy.getStartDate());
                    fclBuyObjFromDB.setEndDate(fclBuy.getEndDate());
                    fclBuyObjFromDB.getFclBuyCostsSet().clear();
                    fclBuyObjFromDB.getFclBuyCostsSet().addAll(fclBuy.getFclBuyCostsSet());
                    fclBuyDAO.update(fclBuyObjFromDB);
                } else {
                    fclBuyDAO.save(fclBuy);
                }
            }
            //for every row we need to add new records. so we are clearing the map.
            fclBuyCostMap.clear();
        }
    }

    private boolean isNotNull(Object object) {
        if (object == null || object.equals("")) {
            return false;
        }

        return true;



    }
}
