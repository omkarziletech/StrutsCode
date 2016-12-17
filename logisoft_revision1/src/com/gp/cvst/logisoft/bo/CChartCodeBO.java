package com.gp.cvst.logisoft.bo;
import java.util.*;

import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import java.io.Serializable;

public class CChartCodeBO implements Serializable {

    public List getGenericCodesInfo() throws Exception {
        AccountDetailsDAO Acdao = new AccountDetailsDAO();
        return Acdao.findForShowAll();
    }
}
