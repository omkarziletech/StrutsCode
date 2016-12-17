package com.gp.cong.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GenericType {
		/**
		 * @param set
		 * @return this method wil take set object of Map and returnList
		 */
		public static List getKeySet(Set set){
			List returnList = new ArrayList();
				for(Object  object:set){
					if (object instanceof Map.Entry) {
						Map.Entry mapEntry=(Map.Entry)object;
						returnList.add(mapEntry.getValue());
				}
			}
			return returnList;
		}

}
