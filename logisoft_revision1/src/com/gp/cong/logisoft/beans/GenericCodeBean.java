package com.gp.cong.logisoft.beans;

import com.gp.cong.logisoft.domain.Codetype;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GenericCodeBean implements Serializable {
    /*
     * private String strCode = null;
     * private int nCodeId ;
     * private boolean bFlag;
     * private Object obj,,,,
     * private 
     */

    private String id;
    private String code;
    private String codedesc;
    private String desc;
    private String field1;
    private String field2;
    private String field3;
    private String field4;
    private String field5;
    private String field6;
    private String field7;
    private String field8;
    private String field9;
    private String field10;
    private String field11;
    private String field12;

    public String getField7() {
        return field7;
    }

    public void setField7(String field7) {
        this.field7 = field7;
    }
    private String label;
    private Integer codeTypeId;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCodedesc() {
        return codedesc;
    }

    public void setCodedesc(String codedesc) {
        this.codedesc = codedesc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }

    public String getField5() {
        return field5;
    }

    public void setField5(String field5) {
        this.field5 = field5;
    }

    public String getField6() {
        return field6;
    }

    public void setField6(String field6) {
        this.field6 = field6;
    }

    public String getField8() {
        return field8;
    }

    public void setField8(String field8) {
        this.field8 = field8;
    }

    public List getDetails(List genericCodeList) throws Exception {
        List list = new ArrayList();
        GenericCodeBean gcb;
        Iterator iterator = genericCodeList.iterator();

        while (iterator.hasNext()) {

            gcb = new GenericCodeBean();
            Object[] row = (Object[]) iterator.next();
            String id = String.valueOf(row[0]);
            String code = (String) row[1];
            String desc = (String) row[2];
            String codedesc = (String) row[3];

            String field1 = (String) row[4];
            String field2 = (String) row[5];
            String field3 = (String) row[6];
            String field4 = (String) row[7];
            String field5 = (String) row[8];
            String field6 = (String) row[9];
            gcb.setId(id);
            gcb.setCode(code);
            gcb.setCodedesc(desc);
            gcb.setDesc(codedesc);

            //done by pravin--17.01.08
            if (desc.equals("Airport Codes")) {

                GenericCodeDAO gcdao = new GenericCodeDAO();

                List countries = gcdao.findByCode(field1);
                if (countries != null && countries.size() > 0) {
                    gcb.setField1(((GenericCode) countries.get(0)).getCodedesc());
                }
                UnLocationDAO undao = new UnLocationDAO();
				//List city=undao.getCity(field2);
                //if(city!=null && city.size()>0)
                //{
                //gcb.setField2((String)city.get(0));
                //}
                gcb.setField3(field3);
                gcb.setField4(field4);
                gcb.setField5(field5);
                gcb.setField6(field6);

            } else if (desc.equals("Country Codes")) {
                GenericCodeDAO gcdao = new GenericCodeDAO();

                if (field1 != null) {

                    Iterator regions = gcdao.getAllRegionsForShowall(field1);
                    String region = (String) regions.next();

                    gcb.setField1(region);
                    gcb.setField2(field2);
                    gcb.setField3(field3);
                    gcb.setField4(field4);
                    gcb.setField5(field5);
                    gcb.setField6(field6);

                } else {
                    field1 = "";

                    gcb.setField1(field1);
                    gcb.setField2(field2);
                    gcb.setField3(field3);
                    gcb.setField4(field4);
                    gcb.setField5(field5);
                    gcb.setField6(field6);
                }

            } else {
                gcb.setField1(field1);
                gcb.setField2(field2);
                gcb.setField3(field3);
                gcb.setField4(field4);
                gcb.setField5(field5);
                gcb.setField6(field6);

            }

            list.add(gcb);

        }

        return list;

    }

    public List getGenericDetails(List genericCodeList) throws Exception {
        List list = new ArrayList();
        GenericCodeBean gcb;
        Iterator iterator = genericCodeList.iterator();

        while (iterator.hasNext()) {
            Codetype codetype = new Codetype();
            CodetypeDAO codetyoeDAO = new CodetypeDAO();
            gcb = new GenericCodeBean();
            GenericCode row = (GenericCode) iterator.next();
            String id = row.getId().toString();
            String code = row.getCode();
            Integer codeTypeId = row.getCodetypeid();
            codetype = codetyoeDAO.findById(codeTypeId);
            String desc = codetype.getDescription();

            String codedesc = row.getCodedesc();
            String field1 = row.getField1();
            String field2 = row.getField2();
            String field3 = row.getField3();
            String field4 = row.getField4();
            String field5 = row.getField5();
            String field6 = row.getField6();
            gcb.setId(id);
            gcb.setCode(code);
            gcb.setCodedesc(desc);
            gcb.setDesc(codedesc);

            //done by pravin--17.01.08
            if (desc.equals("Airport Codes")) {

                GenericCodeDAO gcdao = new GenericCodeDAO();

                List countries = gcdao.findByCode(field1);
                if (countries != null && countries.size() > 0) {
                    gcb.setField1(((GenericCode) countries.get(0)).getCodedesc());
                }
                UnLocationDAO undao = new UnLocationDAO();

                List city = undao.getCity(field2);
                if (city != null && city.size() > 0) {
                    gcb.setField2((String) city.get(0));
                }
                gcb.setField3(field3);
                gcb.setField4(field4);
                gcb.setField5(field5);
                gcb.setField6(field6);

            } else if (desc.equals("Country Codes")) {
                GenericCodeDAO gcdao = new GenericCodeDAO();

                if (field1 != null) {

                    Iterator regions = gcdao.getAllRegionsForShowall(field1);
                    String region = (String) regions.next();

                    gcb.setField1(region);
                    gcb.setField2(field2);
                    gcb.setField3(field3);
                    gcb.setField4(field4);
                    gcb.setField5(field5);
                    gcb.setField6(field6);

                } else {
                    field1 = "";

                    gcb.setField1(field1);
                    gcb.setField2(field2);
                    gcb.setField3(field3);
                    gcb.setField4(field4);
                    gcb.setField5(field5);
                    gcb.setField6(field6);
                }

            } else {
                gcb.setField1(field1);
                gcb.setField2(field2);
                gcb.setField3(field3);
                gcb.setField4(field4);
                gcb.setField5(field5);
                gcb.setField6(field6);

            }

            list.add(gcb);

        }

        return list;

    }

    public ArrayList getHeader(List codeHeader) {
        ArrayList list = new ArrayList();
        GenericCodeBean gcb;
        Iterator iterator = codeHeader.iterator();

        while (iterator.hasNext()) {

            gcb = new GenericCodeBean();
            Object[] row = (Object[]) iterator.next();
            String label = (String) row[0];
            gcb.setLabel(label);
            list.add(gcb);

        }

        return list;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    /*public ArrayList getShowAllDetails(List GenericCodeList){
		
     ArrayList list=new ArrayList();
     GenericCodeBean gcb;
     Iterator iter=GenericCodeList.iterator();
     while(iter.hasNext())
     {
     gcb=new GenericCodeBean();
     Object[] row=(Object[]) iter.next();
     String code=(String)row[0];
     String desc=(String)row[1];
     String codedesc=(String)row[2];
		

     gcb.setCode(code);
     gcb.setDesc(desc);
     gcb.setCodedesc(codedesc);
		
		
     list.add(gcb);
     gcb = null;
     }
		
     return list;
		

     }
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCodeTypeId() {
        return codeTypeId;
    }

    public void setCodeTypeId(Integer codeTypeId) {
        this.codeTypeId = codeTypeId;
    }

    public String getField9() {
        return field9;
    }

    public void setField9(String field9) {
        this.field9 = field9;
    }

    public String getField10() {
        return field10;
    }

    public void setField10(String field10) {
        this.field10 = field10;
    }

    public String getField11() {
        return field11;
    }

    public void setField11(String field11) {
        this.field11 = field11;
    }

    public String getField12() {
        return field12;
    }

    public void setField12(String field12) {
        this.field12 = field12;
    }
}
