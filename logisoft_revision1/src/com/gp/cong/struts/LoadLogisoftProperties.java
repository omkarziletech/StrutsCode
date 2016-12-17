package com.gp.cong.struts;

import com.logiware.common.dao.PropertyDAO;

public class LoadLogisoftProperties {

    public static String getProperty(String property) throws Exception{
	return new PropertyDAO().getProperty(property);
    }

}
