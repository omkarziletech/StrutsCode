package com.logiware.accounting.form;

import com.gp.cvst.logisoft.hibernate.dao.AccountGroupDAO;
import com.logiware.accounting.dao.EcuAccountMappingDAO;
import com.logiware.accounting.domain.EcuAccountMapping;
import java.util.List;

/**
 *
 * @author Lakshmi Narayanan
 */
public class EcuAccountMappingForm extends BaseForm {

    private EcuAccountMapping ecuAccountMapping;
    private List<String> accountTypes = null;

    public EcuAccountMapping getEcuAccountMapping() {
        if (null == ecuAccountMapping) {
            ecuAccountMapping = new EcuAccountMapping();
        }
        return ecuAccountMapping;
    }

    public void setEcuAccountMapping(EcuAccountMapping ecuAccountMapping) {
        this.ecuAccountMapping = ecuAccountMapping;
    }

    public List<EcuAccountMapping> getEcuAccountMappings() throws Exception {
        return new EcuAccountMappingDAO().findAll();
    }

    public List<String> getAccountTypes() {
        if (null == accountTypes) {
            accountTypes = new AccountGroupDAO().getAccountTypes();
        }
        return accountTypes;
    }
}
