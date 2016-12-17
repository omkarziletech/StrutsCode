package com.gp.cong.logisoft.bc.fcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cvst.logisoft.domain.HazmatMaterial;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HazmatBC {
	public List<String> getHazmatDetails(List hazmatList){
    	List lst = new ArrayList();
    	for (Iterator iter = hazmatList.iterator(); iter.hasNext();) {
    		HazmatMaterial hazmatMaterial = (HazmatMaterial)iter.next();
    		StringBuilder hazmatBuilder = new StringBuilder();
            if("Y".equalsIgnoreCase(hazmatMaterial.getFreeFormat())){
                if(CommonUtils.isNotEmpty(hazmatMaterial.getLine1())){
                    hazmatBuilder.append(hazmatMaterial.getLine1()+" \n");
                }
                if(CommonUtils.isNotEmpty(hazmatMaterial.getLine2())){
                    hazmatBuilder.append(hazmatMaterial.getLine2()+" \n");
                }
                if(CommonUtils.isNotEmpty(hazmatMaterial.getLine3())){
                    hazmatBuilder.append(hazmatMaterial.getLine3()+" \n");
                }
                if(CommonUtils.isNotEmpty(hazmatMaterial.getLine4())){
                    hazmatBuilder.append(hazmatMaterial.getLine4()+" \n");
                }
                if(CommonUtils.isNotEmpty(hazmatMaterial.getLine5())){
                    hazmatBuilder.append(hazmatMaterial.getLine5()+" \n");
                }
                if(CommonUtils.isNotEmpty(hazmatMaterial.getLine6())){
                    hazmatBuilder.append(hazmatMaterial.getLine6()+" \n");
                }
                if(CommonUtils.isNotEmpty(hazmatMaterial.getLine7())){
                    hazmatBuilder.append(hazmatMaterial.getLine7()+" \n");
                }
            }else{
    		if(CommonUtils.isNotEmpty(hazmatMaterial.getReportableQuantity())
    				&& "Y".equalsIgnoreCase(hazmatMaterial.getReportableQuantity())){
    			hazmatBuilder.append("R.Q");
    			hazmatBuilder.append(", ");
    		}
    		if(CommonUtils.isNotEmpty(hazmatMaterial.getUnNumber())){
    			hazmatBuilder.append("UN "+hazmatMaterial.getUnNumber());
    		}
    		if(CommonUtils.isNotEmpty(hazmatMaterial.getPropShipingNumber())){
    			hazmatBuilder.append(", ");
    			hazmatBuilder.append(hazmatMaterial.getPropShipingNumber());
    		}
    		if(CommonUtils.isNotEmpty(hazmatMaterial.getTechnicalName())){
    			hazmatBuilder.append(", ");
    			hazmatBuilder.append("["+hazmatMaterial.getTechnicalName()+"]");
    		}
    		if(CommonUtils.isNotEmpty(hazmatMaterial.getImoClssCode())){
    			hazmatBuilder.append(", ");
    			hazmatBuilder.append("CLASS "+hazmatMaterial.getImoClssCode());
    			if(CommonUtils.isNotEmpty(hazmatMaterial.getImoSubsidiaryClassCode())){
        			hazmatBuilder.append("("+hazmatMaterial.getImoSubsidiaryClassCode()+")");
        		}
    			if(CommonUtils.isNotEmpty(hazmatMaterial.getImoSecondarySubClass())){
        			hazmatBuilder.append("("+hazmatMaterial.getImoSecondarySubClass()+")");
        		}
    		}
    		if(CommonUtils.isNotEmpty(hazmatMaterial.getPackingGroupCode())){
    			hazmatBuilder.append(", ");
    			hazmatBuilder.append("PG "+hazmatMaterial.getPackingGroupCode());
    		}
    		if(CommonUtils.isNotEmpty(hazmatMaterial.getFlashPointUMO())){
    			hazmatBuilder.append(", ");
    			hazmatBuilder.append("FLASHPOINT ("+hazmatMaterial.getFlashPointUMO());
                        hazmatBuilder.append(" DEG C)");
    				
    		}
    		if(CommonUtils.isNotEmpty(hazmatMaterial.getOuterPackingPieces())){
    			hazmatBuilder.append(", ");
    			hazmatBuilder.append(hazmatMaterial.getOuterPackingPieces());
    			if(CommonUtils.isNotEmpty(hazmatMaterial.getOuterPackComposition())){
    				hazmatBuilder.append(" "+hazmatMaterial.getOuterPackComposition());
    			}
    			if(CommonUtils.isNotEmpty(hazmatMaterial.getOuterPackagingType())){
    				hazmatBuilder.append(" "+hazmatMaterial.getOuterPackagingType().toUpperCase());
    			}
    			if(CommonUtils.isNotEmpty(hazmatMaterial.getInnerPackingPieces())){
    				hazmatBuilder.append(" CONTAINING");
    				hazmatBuilder.append(" "+hazmatMaterial.getInnerPackingPieces());
    			}
    			if(CommonUtils.isNotEmpty(hazmatMaterial.getInnerPackComposition())){
    				hazmatBuilder.append(" "+hazmatMaterial.getInnerPackComposition());
    			}
    			if(CommonUtils.isNotEmpty(hazmatMaterial.getInnerPackagingType())){
    				hazmatBuilder.append(" "+hazmatMaterial.getInnerPackagingType().toUpperCase());
    			}
    			if(CommonUtils.isNotEmpty(hazmatMaterial.getNetWeight())
    					&& hazmatMaterial.getNetWeight() != 0d){
    				hazmatBuilder.append(" @");
    				hazmatBuilder.append(" "+hazmatMaterial.getNetWeight());
                                hazmatBuilder.append(hazmatMaterial.getNetWeightUMO()+" EACH");
    			}
    		}
                        if(CommonUtils.isNotEmpty(hazmatMaterial.getGrossWeight())){
                            hazmatBuilder.append(", ");
                            hazmatBuilder.append("TOTAL GROSS WT ");
                            hazmatBuilder.append(hazmatMaterial.getGrossWeight());
                            hazmatBuilder.append(" KGS");
                        }
                        if(CommonUtils.isNotEmpty(hazmatMaterial.getTotalNetWeight())){
                            hazmatBuilder.append(", ");
                            hazmatBuilder.append("TOTAL NET WT ");
                            hazmatBuilder.append(hazmatMaterial.getTotalNetWeight());
                            hazmatBuilder.append(" KGS");
                        }
    			if(CommonUtils.isNotEmpty(hazmatMaterial.getVolume())){
                            hazmatBuilder.append(", ");
                            hazmatBuilder.append("TOTAL VOLUME ");
                            hazmatBuilder.append(hazmatMaterial.getVolume());
                            hazmatBuilder.append(" LITERS ");
    			}
    			if("Y".equals(hazmatMaterial.getExceptedQuantity())){
                            hazmatBuilder.append(", ");
                            hazmatBuilder.append(" EXCEPTED QUANTITY");
    			}
    			if("Y".equals(hazmatMaterial.getResidue())){
                            hazmatBuilder.append(", ");
                            hazmatBuilder.append("RESIDUE");
    			}
    			if("Y".equals(hazmatMaterial.getMarinePollutant())){
    				hazmatBuilder.append(", ");
    				hazmatBuilder.append("MARINE POLLUTANT");
    			}
    			if("Y".equals(hazmatMaterial.getInhalationHazard())){
    				hazmatBuilder.append(", ");
    				hazmatBuilder.append("INHALATION HAZARD");
    			}
    			if("Y".equals(hazmatMaterial.getLimitedQuantity())){
    				hazmatBuilder.append(", ");
    				hazmatBuilder.append("LTD QTY");
    			}
    			if(CommonUtils.isNotEmpty(hazmatMaterial.getEmsCode())){
    				hazmatBuilder.append(", ");
    				hazmatBuilder.append("EMS CODE "+hazmatMaterial.getEmsCode());
    			}
    			if(CommonUtils.isNotEmpty(hazmatMaterial.getContactName())){
    				hazmatBuilder.append(", ");
    				hazmatBuilder.append(hazmatMaterial.getContactName());
    			}
    			if(CommonUtils.isNotEmpty(hazmatMaterial.getEmerreprsNum())){
                                hazmatBuilder.append(", ");
    				hazmatBuilder.append(hazmatMaterial.getEmerreprsNum());
    			}
                }
    			lst.add(hazmatBuilder.toString());
    	}
    	return lst;
    }

        public List removeHazmat(String quoteNo) throws Exception {
            return new HazmatMaterialDAO().findbydoctypeid1("Quote", CommonUtils.isNotEmpty(quoteNo)?Integer.parseInt(quoteNo):0);
        }
}
