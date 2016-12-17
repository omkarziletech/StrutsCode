/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.TemplateOrder;
import com.gp.cong.logisoft.domain.lcl.LclSearchTemplate;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author lakshh
 */
public class LclSearchTemplateDAO extends BaseHibernateDAO<LclSearchTemplate> {

    public LclSearchTemplateDAO() {
        super(LclSearchTemplate.class);
    }

    public List<LclSearchTemplate> getAllTemplate()throws Exception {
        List<LclSearchTemplate> templateList = new ArrayList<LclSearchTemplate>();
        String queryString = "from LclSearchTemplate";
        Query queryObject = getSession().createQuery(queryString);
        templateList = queryObject.list();
        return templateList;
    }
    
    public LclSearchTemplate getTemplateByName(String templateName)throws Exception {
        LclSearchTemplate lclSearchTemplate = null;
        String queryString = "FROM LclSearchTemplate WHERE templateName='" + templateName + "'";
        Query queryObject = getSession().createQuery(queryString);
        lclSearchTemplate = (LclSearchTemplate) queryObject.uniqueResult();
        return lclSearchTemplate;
    }

    public List<TemplateOrder> getTemplateOrderedList(int templateId) throws Exception {
        Query query = getSession().createQuery("from TemplateOrder where lclSearchTemplate.id=:templateId ");
        query.setParameter("templateId", templateId);
        return (List<TemplateOrder>) query.list();
    }

    public void deleteTemplateOrderedList(int templateId) throws Exception {
        Query query = getSession().createQuery("delete from TemplateOrder  where lclSearchTemplate.id=:templateId ");
        query.setParameter("templateId", templateId);
        query.executeUpdate();
    }
}
