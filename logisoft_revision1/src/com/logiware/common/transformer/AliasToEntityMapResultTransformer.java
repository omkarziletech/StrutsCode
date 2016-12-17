package com.logiware.common.transformer;

import java.util.LinkedHashMap;
import java.util.List;
import org.hibernate.transform.ResultTransformer;

/**
 *
 * @author Lakshmi Narayanan
 */
public class AliasToEntityMapResultTransformer implements ResultTransformer {

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
	LinkedHashMap result = new LinkedHashMap();
	for (int i = 0; i < tuple.length; i++) {
	    String alias = aliases[i];
	    if (alias != null) {
		result.put(alias, tuple[i]);
	    }
	}
	return result;
    }

    @Override
    public List transformList(List collection) {
	return collection;
    }
}
