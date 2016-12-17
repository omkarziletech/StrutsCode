package com.gp.cong.logisoft.bo;

import java.util.List;

import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;

public class CGenericCodeBO {

    public List getGenericCodesInfo() throws Exception {
        GenericCodeDAO gcdao = new GenericCodeDAO();
        return gcdao.findForShowAll();

    }
}
