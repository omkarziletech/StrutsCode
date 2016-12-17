
<jsp:directive.page import="com.gp.cong.logisoft.domain.RetailWeightRangesRates"/>
<jsp:directive.page import="com.gp.cong.logisoft.hibernate.dao.StandardChargesDAO"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.RetailCommodityCharges"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.RetailStandardCharges"/>
<jsp:directive.page import="java.util.Iterator"/>
<jsp:directive.page import="java.util.HashMap"/>
<jsp:directive.page import="java.util.Map"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.AirWeightRangesRates"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.StandardCharges"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.AirCommodityCharges"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.AirStandardCharges"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.LCLColoadMaster"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.LCLColoadDetails"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.LCLColoadCommodityCharges"/>
<jsp:directive.page import="com.gp.cong.logisoft.hibernate.dao.LCLColoadMasterDAO"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.FTFMaster"/>
<jsp:directive.page import="com.gp.cong.logisoft.hibernate.dao.FTFMasterDAO"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.FTFDetails"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.FTFCommodityCharges"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.UniversalMaster"/>
<jsp:directive.page import="com.gp.cong.logisoft.hibernate.dao.UniversalMasterDAO"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.UniverseCommodityChrg"/>
<%!
Map map = new HashMap();

    public Map getBottomLineRates(int id) throws Exception {
        StandardChargesDAO sChargesDAO = new StandardChargesDAO();
        RetailStandardCharges scharge = sChargesDAO.findById1(id);
        RetailWeightRangesRates retailweightobj = new RetailWeightRangesRates();
        RetailCommodityCharges retailCommodity = new RetailCommodityCharges();
        double rateKGS = 0.0, rcbm = 0.0, cft = 0.0, lbs = 0.0, scft = 0.0, slbs = 0.0, amtkg = 0.0, cbm = 0.0;

        if (scharge.getRetailWeightRangeSet() != null) {
            Iterator iter = (Iterator) scharge.getRetailWeightRangeSet().iterator();
				while(iter.hasNext()){
					retailweightobj=(RetailWeightRangesRates)iter.next();
				if(retailweightobj.getGeneralRate()!=null ){
					rateKGS+=retailweightobj.getGeneralRate();//1000kg	
				}
				if(retailweightobj.getGeneralMinAmt()!=null){
					rcbm+=retailweightobj.getGeneralMinAmt();//cbm 
				} 
				if(retailweightobj.getDeferredRate()!=null){
					cft+=retailweightobj.getDeferredRate();	
				} 
				if(retailweightobj.getExpressMinAmt()!=null){
					lbs+=retailweightobj.getExpressMinAmt();
				 }
			}
		   }
           if(scharge.getRetailCommoditySet()!=null){
        	  
				Iterator iter=(Iterator)scharge.getRetailCommoditySet().iterator();
				while(iter.hasNext()){
					retailCommodity=(RetailCommodityCharges)iter.next();
			if(retailCommodity.getStandard()!=null && !retailCommodity.getStandard().equals("") && retailCommodity.getStandard().equals("Y")) {	
				if( retailCommodity.getAmtPer1000kg()!=null ){
					amtkg+=retailCommodity.getAmtPer1000kg();	
				}
				if(retailCommodity.getAmtPerCbm()!=null){
					cbm+=retailCommodity.getAmtPerCbm();	
				} 
				if(retailCommodity.getAmtPer100lbs()!=null){
					slbs+=retailCommodity.getAmtPer100lbs();
				} 
				if(retailCommodity.getAmtPerCft()!=null){
					scft+=retailCommodity.getAmtPerCft();
				}
			  }
		   }
	       }
	       map.put("kgs",String.valueOf(rateKGS+amtkg));
           map.put("cbm",String.valueOf(rcbm+cbm));
	       map.put("lbs",String.valueOf(lbs+slbs));
	       map.put("cft",String.valueOf(scft+cft));
           
         return  map;

}
	public Map getAirBottomLineRate(int id) throws Exception {
				StandardChargesDAO sChargesDAO = new StandardChargesDAO();
				StandardCharges scharge = sChargesDAO.findById(id);
				AirWeightRangesRates aiirweightObj = new AirWeightRangesRates();
				AirCommodityCharges airCommodity=new AirCommodityCharges();
				double generalrate=0.0,expressrate=0.0,lbs=0.0;
				if(scharge.getAirWeightRangeSet()!=null)	{
					Iterator iter=(Iterator)scharge.getAirWeightRangeSet().iterator();
					while(iter.hasNext()){
						aiirweightObj=(AirWeightRangesRates)iter.next();
					
					if(aiirweightObj.getGeneralRate()!=null && aiirweightObj. getDeferredRate()!=null){
						generalrate+=aiirweightObj.getGeneralRate();
						expressrate+=aiirweightObj.getDeferredRate();
						}
					}
				}
	           if(scharge.getAirCommoditySet()!=null){
					Iterator iter=(Iterator)scharge.getAirCommoditySet().iterator();
					while(iter.hasNext()){
						airCommodity=(AirCommodityCharges)iter.next();
						if(airCommodity.getStandard()!=null && !airCommodity.getStandard().equals("") && airCommodity.getStandard().equals("Y")) {	
							if( airCommodity.getAmtPer100lbs()!=null)	{
							 lbs+=airCommodity.getAmtPer100lbs();
							}
					 	}
				    }
		       }//if
	           if(scharge.getAirStandardCharges()!=null){
					Iterator iter=(Iterator)scharge.getAirStandardCharges().iterator();
					while(iter.hasNext()){
						AirStandardCharges airsss=(AirStandardCharges)iter.next();
						if(airsss.getStandard()!=null && !airsss.getStandard().equals("") && airsss.getStandard().equals("Y")) {	
							if( airsss.getAmtPer100lbs()!=null)	{
								lbs+=airsss.getAmtPer100lbs();
							}
				 		}
				    }
		       }		
			   generalrate=generalrate+lbs;
	           expressrate=expressrate+lbs;
			   map.put("generalrate",String.valueOf(generalrate));
			   map.put("expressrate",String.valueOf(expressrate));
			return map;
	}
	public Map getLclBottmLineRate(int id) throws Exception {
			LCLColoadMasterDAO lCLColoadMasterDAO=new LCLColoadMasterDAO();
			LCLColoadMaster lCLColoadMaster = lCLColoadMasterDAO.findById(id);
			LCLColoadDetails lclColoadDetails=new LCLColoadDetails();
			LCLColoadCommodityCharges lCLColoadCommodityCharges=new LCLColoadCommodityCharges();
			double rate=0.0,rcbm=0.0,cft=0.0,lbs=0.0,scft=0.0,slbs=0.0,amtkg=0.0,cbm=0.0;
			if(lCLColoadMaster.getLclColoadDetailsSet()!=null){
				Iterator iter=(Iterator)lCLColoadMaster.getLclColoadDetailsSet().iterator();
				while(iter.hasNext()){
					lclColoadDetails=(LCLColoadDetails)iter.next();
					if(lclColoadDetails.getMetric1000kg()!=null ){
						rate+=lclColoadDetails.getMetric1000kg();//1000kg	
					}
					if(lclColoadDetails.getMetricCbm()!=null){
						rcbm+=lclColoadDetails.getMetricCbm();//cbm 
					} 
					if(lclColoadDetails.getEnglishCft()!=null){
						cft+=lclColoadDetails.getEnglishCft();	
					} 
					if(lclColoadDetails.getEnglish100lb()!=null){
						lbs+=lclColoadDetails.getEnglish100lb();
					}
				}
			}
           if(lCLColoadMaster.getLclColoadCommChgSet()!=null)	{
        	  Iterator iter=(Iterator)lCLColoadMaster.getLclColoadCommChgSet().iterator();
				while(iter.hasNext()){
					lCLColoadCommodityCharges=(LCLColoadCommodityCharges)iter.next();
					if(lCLColoadCommodityCharges.getStandard()!=null && !lCLColoadCommodityCharges.getStandard().equals("") && lCLColoadCommodityCharges.getStandard().equals("Y"))	 {	
				if( lCLColoadCommodityCharges.getAmtPer1000kg()!=null ){
					amtkg+=lCLColoadCommodityCharges.getAmtPer1000kg();	
				}
				if(lCLColoadCommodityCharges.getAmtPerCbm()!=null){
					cbm+=lCLColoadCommodityCharges.getAmtPerCbm();	
					} 
				if(lCLColoadCommodityCharges.getAmtPer100lbs()!=null){
					slbs+=lCLColoadCommodityCharges.getAmtPer100lbs();
				} 
				if(lCLColoadCommodityCharges.getAmtPerCft()!=null){
					scft+=lCLColoadCommodityCharges.getAmtPerCft();
				}
			  } 
		    }
	       }
           map.put("kgs",String.valueOf(rate+amtkg));
           map.put("cbm",String.valueOf(rcbm+cbm));
	       map.put("lbs",String.valueOf(lbs+slbs));
	       map.put("cft",String.valueOf(scft+cft));
           return map;
	}
	public Map getFtfBottmLineRate(int id) throws Exception {
	FTFMasterDAO ftfMasterDAO=new FTFMasterDAO();
	FTFMaster ftfMaster = ftfMasterDAO.findById(id);
			FTFDetails ftfDetails=new FTFDetails();
			FTFCommodityCharges ftfCommodityCharges=new FTFCommodityCharges();
			double rate=0.0, rcbm=0.0,cft=0.0,lbs=0.0,scft=0.0,slbs=0.0,amtkg=0.0,cbm=0.0,res1=0.0,res2=0.0;
			double res3=0.0,res4=0.0;
			if(ftfMaster.getFtfDetailsSet()!=null)
			{
				Iterator iter=(Iterator)ftfMaster.getFtfDetailsSet().iterator();
				while(iter.hasNext()){
					ftfDetails=(FTFDetails)iter.next();
					if(ftfDetails.getMetric1000kg()!=null ){
						rate+=ftfDetails.getMetric1000kg();//1000kg	
					}
					if(ftfDetails.getMetricCbm()!=null){
						rcbm+=ftfDetails.getMetricCbm();//cbm 
					} 
					if(ftfDetails.getEnglishCft()!=null){
						cft+=ftfDetails.getEnglishCft();	
					} 
					if(ftfDetails.getEnglish100lb()!=null){
						lbs+=ftfDetails.getEnglish100lb();
					}
					}
			}
           if(ftfMaster.getFtfCommChgSet()!=null){
        	  
				Iterator iter=(Iterator)ftfMaster.getFtfCommChgSet().iterator();
				while(iter.hasNext())
				{
					ftfCommodityCharges=(FTFCommodityCharges)iter.next();
					
				
			if(ftfCommodityCharges.getStandard()!=null && !ftfCommodityCharges.getStandard().equals("") && ftfCommodityCharges.getStandard().equals("Y"))
			 {	
				if( ftfCommodityCharges.getAmtPer1000kg()!=null ){
					amtkg+=ftfCommodityCharges.getAmtPer1000kg();	
				}
				if(ftfCommodityCharges.getAmtPerCbm()!=null){
					
					cbm+=ftfCommodityCharges.getAmtPerCbm();	
					
				} 
				if(ftfCommodityCharges.getAmtPer100lbs()!=null){
					slbs+=ftfCommodityCharges.getAmtPer100lbs();
				} 
				if(ftfCommodityCharges.getAmtPerCft()!=null)
				{
					scft+=ftfCommodityCharges.getAmtPerCft();
				}
			 }
		 	
		    }
	       }
	       map.put("kgs",String.valueOf(rate+amtkg));
           map.put("cbm",String.valueOf(rcbm+cbm));
	       map.put("lbs",String.valueOf(lbs+slbs));
	       map.put("cft",String.valueOf(scft+cft));
	       return map;
          
          
	}
	public Map getUniversalBottmLineRate(int id) throws Exception {
			UniversalMasterDAO universalMasterDAO = new UniversalMasterDAO();
			UniversalMaster universalMaster = universalMasterDAO.findById(id);
			UniverseCommodityChrg universalComm=new UniverseCommodityChrg();
			double rate=0.0,rcbm=0.0,cft=0.0,lbs=0.0,scft=0.0,slbs=0.0,amtkg=0.0,cbm=0.0;
			if(universalMaster.getUniversalCommodity()!=null){
        	  Iterator iter=(Iterator)universalMaster.getUniversalCommodity().iterator();
				while(iter.hasNext()){
					universalComm=(UniverseCommodityChrg)iter.next();
				if(universalComm.getStandard()!=null && !universalComm.getStandard().equals("") && universalComm.getStandard().equals("Y")) {	
				if( universalComm.getAmtPer1000kg()!=null ){
					amtkg+=universalComm.getAmtPer1000kg();	
				}
				if(universalComm.getAmtPerCbm()!=null){
					cbm+=universalComm.getAmtPerCbm();	
				} 
				if(universalComm.getAmtPer100lbs()!=null){
					slbs+=universalComm.getAmtPer100lbs();
				} 
				if(universalComm.getAmtPerCft()!=null){
					scft+=universalComm.getAmtPerCft();
				}
			 }
		   }
	      }
	       map.put("kgs",String.valueOf(amtkg));
           map.put("cbm",String.valueOf(cbm));
	       map.put("lbs",String.valueOf(slbs));
	       map.put("cft",String.valueOf(scft));
	       return map;
	}
%>