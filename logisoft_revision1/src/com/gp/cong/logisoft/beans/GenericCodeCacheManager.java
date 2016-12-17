package com.gp.cong.logisoft.beans;

import java.util.HashMap;
import java.util.Map;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import java.io.Serializable;

public class GenericCodeCacheManager implements Serializable {

    private static Map<Integer, Object> cacheMap = new HashMap<Integer, Object>();
    private static Map<Integer, Object> codeMap = new HashMap<Integer, Object>();

    public static Object getCodeDesc(Integer key) throws Exception {
        if (cacheMap.get(key) == null) {
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
            if (key != null) {
                GenericCode genericCode = genericCodeDAO.findById(key);
                cacheMap.put(key, genericCode.getCodedesc());
            }
        }
        return cacheMap.get(key);
    }

    public static Object getCode(Integer key) throws Exception {
        if (codeMap.get(key) == null) {
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
            GenericCode genericCode = genericCodeDAO.findById(key);
            codeMap.put(key, genericCode.getCode());
        }
        return codeMap.get(key);
    }
}
