package com.gp.cvst.logisoft.reports.data;

import java.util.ArrayList;
import java.util.List;

public class ReportDataSourceImpl implements ReportDataSource {

    private CriteriaSet criteriaSet;
    private QueryProvider queryProvider;
    private List resultPage;
    private int pageStart = Integer.MAX_VALUE;
    private int pageEnd = Integer.MIN_VALUE;
    private static final int PAGE_SIZE = 50;

    //
    // Getters and setters for criteriaSet and queryProvider
    //
    
        
    public void setCriteriaSet(CriteriaSet criteria){
    	this.criteriaSet=criteria;
    }
    
    public CriteriaSet getCriteriaSet(){
    	return this.criteriaSet;
    }
    
    public void setQueryProvider(QueryProvider qp){
    	this.queryProvider=qp;
    }
    
    public QueryProvider getQueryProvider(){
    	return this.queryProvider;
    }
    
    
    public List getObjects(int firstResult,
                           int maxResults) {

         List queryResults = getQueryProvider()
                                .getObjects(getCriteriaSet(),
                                            firstResult,
                                            maxResults);
         if(queryResults != null){
	         if (resultPage == null) {
	             resultPage = new ArrayList(queryResults.size());
	         }
	         resultPage.clear();
	         for(int i = 0; i < queryResults.size(); i++) {
	             resultPage.add(queryResults.get(i));
	         }
	         pageStart = firstResult;
	         pageEnd = firstResult + queryResults.size() - 1;
	         return resultPage;
         }
         return null;
    }

    public final Object getObject(int index) {
        Object result = null;
  	
	        if ((resultPage == null)
	            || (index < pageStart)
	            || (index > pageEnd)) {
	            resultPage = getObjects(index, PAGE_SIZE);
	        }

	        int pos = index - pageStart;
	        if ((resultPage != null)
	             && (resultPage.size() > pos)) {
	            result = resultPage.get(pos);
	        }

        return result;
    	
    }
}