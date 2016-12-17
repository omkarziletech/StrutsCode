package com.gp.cong.logisoft.reports.data;

import java.util.ArrayList;
import java.util.List;

import com.gp.cong.logisoft.reports.dto.MessageDTO;

public class MessageQueryProvider implements QueryProvider {
	
	
	//public static boolean runOnce = false;
	
	public List getObjects(CriteriaSet criteria, int firstResult, int maxResults){
		
		
		
		MessageCriteriaSet msc = (MessageCriteriaSet) criteria;
		
		List returnList = new ArrayList();
		
		int lastResult = 0;
		if(msc.getMessages()!=null && msc.getMessages().size()>0)
		{
			if(firstResult > msc.getMessages().size())
			{
				return null;
			}
	
			if(msc.getMessages().size() > (firstResult + maxResults)){
				lastResult = maxResults;
			}else{
				lastResult = msc.getMessages().size();
			}
		}
		
		
		for(int x = firstResult; x < lastResult; x++){
			returnList.add(msc.getMessages().get(x));
		}
			
		return returnList;
	}

}
