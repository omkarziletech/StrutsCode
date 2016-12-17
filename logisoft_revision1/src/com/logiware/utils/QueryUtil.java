/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.utils;

import com.logiware.properties.QueryProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sunil
 */
public class QueryUtil {

    public static String getQuery(String query, String param, Map map)throws Exception {    

        List<String> list = new ArrayList<String>();
        list.add(param.trim());
        for (Object k : map.keySet()) {
            if (k.toString().contains("param")) {
                list.add(((String[])map.get(k))[0].toString());
            }
        }
        String q = QueryProperties.getQuery(query, list.toArray(new String[]{}));
        //changed by Mahalakshmi
            return q ;//end 
    }
}
    