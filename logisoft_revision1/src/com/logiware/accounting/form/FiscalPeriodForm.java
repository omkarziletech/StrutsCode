package com.logiware.accounting.form;

import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.FiscalYear;
import com.logiware.accounting.dao.FiscalPeriodDAO;
import java.util.List;

/**
 *
 * @author Lakshmi Narayanan
 */
public class FiscalPeriodForm extends BaseForm {

    private FiscalYear fiscalYear;
    private List<FiscalPeriod> fiscalPeriods;
    private Integer budgetSet;

    public FiscalYear getFiscalYear() {
        if (null == fiscalYear) {
            fiscalYear = new FiscalYear();
        }
        return fiscalYear;
    }

    public void setFiscalYear(FiscalYear fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public List<FiscalYear> getFiscalYears() {
        return new FiscalPeriodDAO().getAllFiscalYears();
    }

    public List<FiscalPeriod> getFiscalPeriods() {
        return fiscalPeriods;
    }

    public void setFiscalPeriods(List<FiscalPeriod> fiscalPeriods) {
        this.fiscalPeriods = fiscalPeriods;
    }

    public Integer getBudgetSet() {
        return budgetSet;
    }

    public void setBudgetSet(Integer budgetSet) {
        this.budgetSet = budgetSet;
    }
}
