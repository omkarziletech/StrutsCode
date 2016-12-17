package com.gp.cong.logisoft.util;

import java.util.Date;
import java.util.List;
import com.gp.cong.logisoft.domain.FclBuyCostTypeFutureRates;
import com.gp.cong.logisoft.domain.FclBuyCostTypeFutureRatesHisptory;
import com.gp.cong.logisoft.domain.FclBuyCostTypeRates;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.FclBuyDAO;

public class HiberDemo {

    public static FclBuyCostTypeRates getCurretRateObject(
            FclBuyCostTypeFutureRates futurerates,
            FclBuyCostTypeRates getfl_current_records) {

        if (futurerates != null) {

            // getfl_current_records.setFclCostTypeId(futurerates.getFclCostTypeId());
            getfl_current_records.setCostId(futurerates.getCostId());
            getfl_current_records.setActiveAmt(futurerates.getActiveAmt());
            getfl_current_records.setCtcAmt(futurerates.getCtcAmt());
            getfl_current_records.setCurrency(futurerates.getCurrency());
            getfl_current_records.setFtfAmt(futurerates.getFtfAmt());
            getfl_current_records.setMarkup(futurerates.getMinimumAmt());
            getfl_current_records.setMinimumAmt(futurerates.getMinimumAmt());
            getfl_current_records.setRatAmount(futurerates.getRatAmount());
            getfl_current_records.setUnitType(futurerates.getUnitType());
            getfl_current_records.setStandard(futurerates.getStandard());

        }

        return getfl_current_records;
    }

    public FclBuyCostTypeFutureRatesHisptory getHistroyRateObject(
            FclBuyCostTypeRates fclBuyCostTypeRates) {

        FclBuyCostTypeFutureRatesHisptory getfl_history_records = new FclBuyCostTypeFutureRatesHisptory();
        //getfl_history_records.setFclCostId(fclBuyCostTypeRates.getFclCostTypeId());
        getfl_history_records.setActiveAmt(fclBuyCostTypeRates.getActiveAmt());
        getfl_history_records.setCtcAmt(fclBuyCostTypeRates.getCtcAmt());
        getfl_history_records.setCurrency(fclBuyCostTypeRates.getCurrency());
        getfl_history_records.setFtfAmt(fclBuyCostTypeRates.getFtfAmt());
        getfl_history_records.setMarkup(fclBuyCostTypeRates.getMinimumAmt());
        getfl_history_records.setMinimumAmt(fclBuyCostTypeRates.getMinimumAmt());
        getfl_history_records.setRatAmount(fclBuyCostTypeRates.getRatAmount());
        getfl_history_records.setUnitType(fclBuyCostTypeRates.getUnitType());
        getfl_history_records.setStandard(fclBuyCostTypeRates.getStandard());
        getfl_history_records.setFcl_id(fclBuyCostTypeRates.getFclCostId());

        return getfl_history_records;
    }

    public static void main(String[] args)throws Exception {
            /*
             * setting she
             *
             * */
            HiberDemo hiberDemo = new HiberDemo();
            FclBuyDAO fclBuyDAO = new FclBuyDAO();
            //Session sess = HibernateSessionFactory.getSession();
            Date currentDate = new Date();
            List futureRateList = fclBuyDAO.getFutureRates(currentDate);
            // GETTING ALL THE RECORDS FROM FUTURE TABLE BY GIVING CURRENT DATE AS A CONDITION

            for (int i = 0; i < futureRateList.size(); i++) {
                Integer getId = null;
                String untitype = null;
                GenericCode gen = null;
                FclBuyCostTypeFutureRates getFclBuyCostTypeFutureRates = (FclBuyCostTypeFutureRates) futureRateList.get(i);

                if (getFclBuyCostTypeFutureRates != null) {
                    getId = getFclBuyCostTypeFutureRates.getFclCostId();
                    gen = (GenericCode) getFclBuyCostTypeFutureRates.getUnitType();
                    if (gen != null) {
                        untitype = gen.getId().toString();
                    }
                    List currentList = fclBuyDAO.getCurrentRates(getId, untitype);
//					 GETTING ALL THE RECORDS FROM CUURENT RATES TABLE BY GIVING getId, untitype AS A CONDITION	

                    boolean flag = false;
                    for (int k = 0; k < currentList.size(); k++) {
                        FclBuyCostTypeRates fclCurrenteRates = (FclBuyCostTypeRates) currentList.get(k);
                        if (fclCurrenteRates.getActiveAmt() != null && getFclBuyCostTypeFutureRates.getActiveAmt() != null && !fclCurrenteRates.getActiveAmt().equals(
                                getFclBuyCostTypeFutureRates.getActiveAmt())) {
                            flag = true;

                        }
                        if (fclCurrenteRates.getRatAmount() != null && getFclBuyCostTypeFutureRates.getRatAmount() != null && !fclCurrenteRates.getRatAmount().equals(
                                getFclBuyCostTypeFutureRates.getRatAmount())) {
                            flag = true;

                        }
                        if (fclCurrenteRates.getMinimumAmt() != null && getFclBuyCostTypeFutureRates.getMinimumAmt() != null && !fclCurrenteRates.getMinimumAmt().equals(
                                getFclBuyCostTypeFutureRates.getMinimumAmt())) {
                            flag = true;

                        }
                        if (fclCurrenteRates.getCtcAmt() != null && getFclBuyCostTypeFutureRates.getCtcAmt() != null && !fclCurrenteRates.getCtcAmt().equals(
                                getFclBuyCostTypeFutureRates.getCtcAmt())) {
                            flag = true;

                        }
                        if (fclCurrenteRates.getFtfAmt() != null && getFclBuyCostTypeFutureRates.getFtfAmt() != null && !fclCurrenteRates.getFtfAmt().equals(
                                getFclBuyCostTypeFutureRates.getFtfAmt())) {
                            flag = true;

                        }
                        if (fclCurrenteRates.getStandard() != null && getFclBuyCostTypeFutureRates.getStandard() != null && !fclCurrenteRates.getStandard().equals(
                                getFclBuyCostTypeFutureRates.getStandard())) {
                        }
                        if (flag) {
                            FclBuyCostTypeFutureRatesHisptory fcl_history = hiberDemo.getHistroyRateObject(fclCurrenteRates);
                            getCurretRateObject(getFclBuyCostTypeFutureRates, fclCurrenteRates);
                            if (fcl_history != null) {

                                fclBuyDAO.FclHistoryRatessave(fcl_history);
                                fclBuyDAO.FclHistoryRatesDelete(getFclBuyCostTypeFutureRates);
                            }



                        }

                    }

                }

            }
    }
}
