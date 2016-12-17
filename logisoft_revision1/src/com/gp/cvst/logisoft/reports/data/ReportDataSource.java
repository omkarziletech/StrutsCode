package com.gp.cvst.logisoft.reports.data;

import java.io.Serializable;


  public interface ReportDataSource extends Serializable {
	       Object getObject(int index);
	       void setQueryProvider(QueryProvider qp);
	       QueryProvider getQueryProvider();
	       void setCriteriaSet(CriteriaSet cr);
	       CriteriaSet getCriteriaSet();
   }
