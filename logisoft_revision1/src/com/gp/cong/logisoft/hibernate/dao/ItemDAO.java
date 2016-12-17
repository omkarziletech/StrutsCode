package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.domain.Country;
import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.logiware.bean.ItemsForRoleBean;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.apache.log4j.Logger;

/**
 * @author Gho
 *
 */
public class ItemDAO extends BaseHibernateDAO {

    //private static final Log log = LogFactory.getLog(StateDAO.class);
    private static final Logger log = Logger.getLogger(ItemDAO.class);
    private static final String StateID = "id";
    private static final String State = "notify_party";
    private static final String CUSTOMER = "customer";

    /**
     * get all The State Instances
     *
     * @return
     */
    public void save(Item transientInstance, String userName) throws Exception {
        getCurrentSession().save(transientInstance);
        /*AuditLogInterceptor audit=new AuditLogInterceptor();
         audit.setSessionFactory(HibernateSessionFactory.getSessionFactory());
	 audit.setUserName(userName);
	 AuditLogRecord aud=new AuditLogRecordItem();
	 audit.setAuditLogReord(aud);
	 audit.onFlushDiy(transientInstance, transientInstance.getItemId(), null, null, null, null);
	 Iterator iter=null;
	 audit.postFlush(iter);*/
        // tx.commit();
    }

    public List getAllNotifyParties() throws Exception {
        String queryString = "from State";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list();
    }

    public Iterator getAllStatesForDisplay() throws Exception {
        Iterator results = null;
        results = getCurrentSession().createQuery(
                "select State.id,State.stateName from State State").list().iterator();
        return results;
    }

    public Iterator getStatesByCountry(Country country) throws Exception {
        if (country != null) {
            //System.out.println("country........name id"+country.getCountrycode()+"country..."+country.getCountryname());
        }
        String queryString = "select model.id, model.stateName from State as model where model.country=?0";
        Query query = getCurrentSession().createQuery(queryString);
        query.setParameter("0", country);

        return query.list().iterator();
    }

    public Item findById(Integer id) throws Exception {
        Item instance = (Item) getCurrentSession().get("com.gp.cong.logisoft.domain.Item", id);
        return instance;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        //     log.debug("finding Notify Party instance with property: " + propertyName
        //       + ", value: " + value);
        String queryString = "select model.id,model.StateName "
                + "from State as model where model." + propertyName + "= ?0";

        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public List findByCustomerid(Object customerid) throws Exception {
        return findByProperty(CUSTOMER, customerid);
    }

    public Iterator getPredecessorForDisplay(String edit) throws Exception {
        Iterator results = null;

        //if(edit==null)
        //{
        results = getCurrentSession().createQuery(
                "select item.itemId,item.itemDesc from Item item where programName is null order by itemDesc").list().iterator();
        //}
        /*else
	 {
	 String queryString = "select item.itemId,item.itemDesc from Item item where programName is null and item.itemDesc!=?";
	 Query queryObject = getCurrentSession().createQuery(queryString);
	 queryObject.setParameter(0, edit);
	 results = queryObject.list().iterator();

	 }*/
        return results;
    }

    public List getChild(Integer parentId) throws Exception {
        Session session = null;
        session = HibernateSessionFactory.getSession();
        String queryString = "select item from Item item, "
                + "ItemTree tree where item.itemId=tree.itemId and tree.parentId='" + parentId + "'"
                + " order by item.itemId";
        List list = session.createQuery(queryString).list();
        return list;
    }

    public List<Item> getMenu() throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select item from Item item,ItemTree tree");
        queryBuilder.append(" where item.itemId=tree.itemId and tree.parentId=1");
        queryBuilder.append(" order by item.itemDesc");
        return getCurrentSession().createQuery(queryBuilder.toString()).list();
    }

    public List<Item> getSubMenu(Integer parentId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select item from Item item,ItemTree tree");
        queryBuilder.append(" where item.itemId=tree.itemId and tree.parentId=").append(parentId);
        queryBuilder.append(" order by item.itemDesc");
        return getCurrentSession().createQuery(queryBuilder.toString()).list();
    }

    public List<Item> getSubMenuWitoutTP(Integer parentId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select item from Item item,ItemTree tree");
        queryBuilder.append(" where item.itemId=tree.itemId and tree.parentId=").append(parentId);
        queryBuilder.append(" and item.itemDesc != 'Trading Partner Maintenance' and item.itemDesc != 'EDI'  order by item.itemDesc");
        return getCurrentSession().createQuery(queryBuilder.toString()).list();
    }

    public List<ItemsForRoleBean> getItemsForRole(Integer parentId, Integer roleId) throws Exception {
        List<ItemsForRoleBean> itemsForRole = new ArrayList<ItemsForRoleBean>();
        StringBuilder queryBuilder = new StringBuilder("select item.itemId,item.itemDesc,item.programName,role.modify from Item item,ItemTree tree,RoleItemAssociation role");
        queryBuilder.append(" where item.itemId=tree.itemId and tree.parentId=").append(parentId);
        queryBuilder.append(" and role.itemId.itemId=tree.itemId and tree.parentId=").append(parentId);
        queryBuilder.append(" and item.programName is not null and role.roleId=").append(roleId);
        queryBuilder.append(" order by item.itemId");
        List result = getCurrentSession().createQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            ItemsForRoleBean itemsForRoleBean = new ItemsForRoleBean();
            itemsForRoleBean.setItemId((Integer) col[0]);
            itemsForRoleBean.setItemDesc((String) col[1]);
            itemsForRoleBean.setProgram((String) col[2]);
            itemsForRoleBean.setModify((String) col[3]);
            itemsForRole.add(itemsForRoleBean);
        }
        return itemsForRole;
    }

    public List<ItemsForRoleBean> getScreensForMenu(Integer parentId, Integer roleId, String screenNames) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  screen.item_id as itemId,");
        queryBuilder.append("  screen.item_desc as itemDesc,");
        queryBuilder.append("  screen.program as program,");
        queryBuilder.append("    concat(");
//	queryBuilder.append("      '").append(contextPath).append("'");
        queryBuilder.append("      screen.program,");
        queryBuilder.append("      if(");
        queryBuilder.append("        role.modify != '',");
        queryBuilder.append("        '&modify=1&accessMode=1',");
        queryBuilder.append("        '&modify=0&accessMode=0'");
        queryBuilder.append("      ),");
        queryBuilder.append("      '&programid=',");
        queryBuilder.append("      screen.item_id,");
        queryBuilder.append("      '&date=',");
        queryBuilder.append("      now()");
        queryBuilder.append("    ) as src");
        queryBuilder.append("  ");
        queryBuilder.append("from");
        queryBuilder.append("  item_treestructure menu");
        queryBuilder.append("  join item_master screen");
        queryBuilder.append("    on (");
        queryBuilder.append("      menu.item_id = screen.item_id");
        if (CommonUtils.isNotEmpty(screenNames)) {
            queryBuilder.append("      and screen.item_desc in (").append(screenNames).append(")");
        }
        queryBuilder.append("    )");
        queryBuilder.append("  join role_item_assoc role");
        queryBuilder.append("    on (");
        queryBuilder.append("      menu.item_id = role.item_id");
        queryBuilder.append("      and role.role_id = ").append(roleId);
        queryBuilder.append("    )");
        queryBuilder.append("  ");
        queryBuilder.append("where menu.parent_id = ").append(parentId);
        queryBuilder.append("  ");
        queryBuilder.append("order by screen.item_id");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("itemId", IntegerType.INSTANCE);
        query.addScalar("itemDesc", StringType.INSTANCE);
        query.addScalar("program", StringType.INSTANCE);
        query.addScalar("src", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ItemsForRoleBean.class));
        return query.list();
    }

    public List<ItemsForRoleBean> getFclOpsScreens(Integer parentId, Integer roleId, String fileNumber, boolean isImportFile, String contextPath) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        String multiQuote = isImportFile ? "'IMPORT MultiQuote'" : "'MultiQuote'";
        String quote = isImportFile ? "'IMPORT QUOTE'" : "'Quotes'";
        String booking = isImportFile ? "'IMPORT BOOKING'" : "'Bookings'";
        String bl = isImportFile ? "'IMPORT FCL BL'" : "'FCL BL'";
        queryBuilder.append("select");
        queryBuilder.append("  screen.`item_id` as itemId,");
        queryBuilder.append("  screen.`item_desc` as itemDesc,");
        queryBuilder.append("  screen.`program` as program,");
        queryBuilder.append("    concat(");
        queryBuilder.append("    '").append(contextPath).append("',");
        queryBuilder.append("    if(");
        queryBuilder.append("      screen.`item_desc` = ").append(multiQuote).append(",");
        queryBuilder.append("      concat('/multiQuote.do?methodName=viewFile&paramid1=', screen.`multi_quote_id`),");
        queryBuilder.append("      if(");
        queryBuilder.append("        screen.`item_desc` = ").append(quote).append(",");
        queryBuilder.append("        concat('/searchquotation.do?paramid1=', screen.`quote_id`),");
        queryBuilder.append("        if(");
        queryBuilder.append("          screen.`item_desc` = ").append(booking).append(",");
        queryBuilder.append("          concat('/searchBookings.do?paramid=', screen.`booking_id`),");
        if (isImportFile) {
            queryBuilder.append("          concat('/fclBL.do?paramid=', screen.`bl_id`)");
        } else {
            queryBuilder.append("          concat('/fclBlNew.do?methodName=goToHome&blId=', screen.`bl_id`)");
        }
        queryBuilder.append("        )");
        queryBuilder.append("      )");
        queryBuilder.append("    ),");
        queryBuilder.append("    if(screen.`modify` <> '', '&modify=1&accessMode=1', '&modify=0&accessMode=0'),");
        queryBuilder.append("    '&programid=', screen.`item_id`,");
        queryBuilder.append("    '&date=', now()");
        queryBuilder.append("  ) as src ");
        queryBuilder.append("from");
        queryBuilder.append("  (select ");
        queryBuilder.append("    screen.`item_id`,");
        queryBuilder.append("    screen.`item_desc`,");
        queryBuilder.append("    screen.`program`,");
        queryBuilder.append("    role.`modify`,");
        queryBuilder.append("    if(screen.`item_desc` = ").append(multiQuote).append(", (select q.`quote_id` from `quotation` q where q.`file_no` = '").append(fileNumber).append("' and q.`multi_quote_flag` in ('M', 'C') limit 1), null) as multi_quote_id,");
        queryBuilder.append("    if(screen.`item_desc` = ").append(quote).append(", (select q.`quote_id` from `quotation` q where q.`file_no` = '").append(fileNumber).append("' limit 1), null) as quote_id,");
        queryBuilder.append("    if(screen.`item_desc` = ").append(booking).append(", (select b.`bookingid` from `booking_fcl` b where b.`file_no` = '").append(fileNumber).append("' limit 1), null) as booking_id,");
        queryBuilder.append("    if(screen.`item_desc` = ").append(bl).append(", (select b.`bol` from `fcl_bl` b where b.`file_no` = '").append(fileNumber).append("' limit 1), null) as bl_id ");
        queryBuilder.append("  from");
        queryBuilder.append("    `item_treestructure` menu");
        queryBuilder.append("    join item_master screen");
        queryBuilder.append("      on (");
        queryBuilder.append("        screen.`item_id` = menu.`item_id`");
        queryBuilder.append("        and screen.`item_desc` in (");
        queryBuilder.append("          ").append(multiQuote).append(",");
        queryBuilder.append("          ").append(quote).append(",");
        queryBuilder.append("          ").append(booking).append(",");
        queryBuilder.append("          ").append(bl);
        queryBuilder.append("        )");
        queryBuilder.append("      )");
        queryBuilder.append("    join `role_item_assoc` role");
        queryBuilder.append("      on (");
        queryBuilder.append("        role.`item_id` = menu.`item_id`");
        queryBuilder.append("        and role.`role_id` = ").append(roleId);
        queryBuilder.append("      )");
        queryBuilder.append("  where menu.`parent_id` = ").append(parentId).append(") as screen ");
        queryBuilder.append("where screen.`multi_quote_id` <> 0");
        queryBuilder.append("  or screen.`quote_id` <> 0");
        queryBuilder.append("  or screen.`booking_id` <> 0");
        queryBuilder.append("  or screen.`bl_id` <> 0 ");
        queryBuilder.append("order by screen.`item_id`");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("itemId", IntegerType.INSTANCE);
        query.addScalar("itemDesc", StringType.INSTANCE);
        query.addScalar("program", StringType.INSTANCE);
        query.addScalar("src", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ItemsForRoleBean.class));
        return query.list();
    }

    public List<ItemsForRoleBean> getLclOpsScreens(Integer parentId, Integer roleId, String fileNumber, boolean isImportFile, String contextPath) throws Exception {
        Long fileNumberId = new LclFileNumberDAO().getFileId(fileNumber);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  screen.`item_id` as itemId,");
        queryBuilder.append("  screen.`item_desc` as itemDesc,");
        queryBuilder.append("  screen.`program` as program,");
        queryBuilder.append("    concat(");
        queryBuilder.append("    '").append(contextPath).append("',");
        queryBuilder.append("    if(screen.`item_desc` = 'Quotes',");
        queryBuilder.append("      '/lclQuote.do?methodName=editQuote',");
        if (isImportFile) {
            queryBuilder.append("      '/lclBooking.do?methodName=editBooking'");
        } else {
            queryBuilder.append("      if(screen.`item_desc` = 'Bookings',");
            queryBuilder.append("        '/lclBooking.do?methodName=editBooking',");
            queryBuilder.append("        '/lclBl.do?methodName=editBooking'");
            queryBuilder.append("      )");
        }
        queryBuilder.append("    ),");
        queryBuilder.append("    '&fileNumberId=").append(fileNumberId).append("',");
        queryBuilder.append("    '&fileNumber=").append(fileNumber).append("',");
        queryBuilder.append("    '&moduleName=").append(isImportFile ? "Imports" : "Exports").append("',");
        queryBuilder.append("    '&moduleId=").append(fileNumber).append("',");
        queryBuilder.append("    '&screenName=LCL FILE',");
        queryBuilder.append("    '&operationType=Scan or Attach',");
        queryBuilder.append("    if(screen.`item_desc` = 'Quotes',");
        queryBuilder.append("      '&homeScreenQtFileFlag=true',");
        queryBuilder.append("      '&homeScreenDrFileFlag=true'");
        queryBuilder.append("    ),");
        queryBuilder.append("    if(screen.`modify` <> '', '&modify=1&accessMode=1', '&modify=0&accessMode=0'),");
        queryBuilder.append("    '&programid=', screen.`item_id`,");
        queryBuilder.append("    '&date=', now()");
        queryBuilder.append("  ) as src ");
        queryBuilder.append("from");
        queryBuilder.append("  (select ");
        queryBuilder.append("    screen.`item_id`,");
        queryBuilder.append("    screen.`item_desc`,");
        queryBuilder.append("    screen.`program`,");
        queryBuilder.append("    role.`modify`,");
        queryBuilder.append("    if(screen.`item_desc` = 'Quotes', (select q.`file_number_id` from `lcl_quote` q where q.`file_number_id` = ").append(fileNumberId).append(" limit 1), null) as quote_id,");
        if (isImportFile) {
            queryBuilder.append("    if(screen.`item_desc` = 'Bookings', (select b.`file_number_id` from `lcl_booking` b where b.`file_number_id` = ").append(fileNumberId).append(" limit 1), null) as booking_id");
        } else {
            queryBuilder.append("    if(screen.`item_desc` = 'Bookings', (select b.`file_number_id` from `lcl_booking` b where b.`file_number_id` = ").append(fileNumberId).append(" limit 1), null) as booking_id,");
            queryBuilder.append("    if(screen.`item_desc` = 'LCL BL', (select b.`file_number_id` from `lcl_bl` b where b.`file_number_id` = ").append(fileNumberId).append(" limit 1), null) as bl_id");
        }
        queryBuilder.append("  from");
        queryBuilder.append("    `item_treestructure` menu");
        queryBuilder.append("    join item_master screen");
        queryBuilder.append("      on (");
        queryBuilder.append("        screen.`item_id` = menu.`item_id` ");
        queryBuilder.append("        and screen.`item_desc` in (");
        queryBuilder.append("          'Quotes',");
        if (isImportFile) {
            queryBuilder.append("          'Bookings'");
        } else {
            queryBuilder.append("          'Bookings',");
            queryBuilder.append("          'LCL BL'");
        }
        queryBuilder.append("        )");
        queryBuilder.append("      )");
        queryBuilder.append("    join `role_item_assoc` role");
        queryBuilder.append("      on (");
        queryBuilder.append("        role.`item_id` = menu.`item_id`");
        queryBuilder.append("        and role.`role_id` = ").append(roleId);
        queryBuilder.append("      ) ");
        queryBuilder.append("  where menu.`parent_id` = ").append(parentId).append(") as screen ");
        queryBuilder.append("where screen.`quote_id` <> 0");
        queryBuilder.append("  or screen.`booking_id` <> 0 ");
        if (!isImportFile) {
            queryBuilder.append("  or screen.`bl_id` <> 0 ");
        }
        queryBuilder.append("order by screen.`item_id`");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("itemId", IntegerType.INSTANCE);
        query.addScalar("itemDesc", StringType.INSTANCE);
        query.addScalar("program", StringType.INSTANCE);
        query.addScalar("src", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ItemsForRoleBean.class));
        return query.list();
    }

    public List<ItemsForRoleBean> getLclVoyageScreens(Integer parentId, Integer roleId, String voyageNo, boolean isImportVoyage, String contextPath) throws Exception {
        Long headerId = new LclSsHeaderDAO().getHeaderId(voyageNo);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  screen.`item_id` as itemId,");
        queryBuilder.append("  screen.`item_desc` as itemDesc,");
        queryBuilder.append("  screen.`program` as program,");
        queryBuilder.append("  concat(");
        queryBuilder.append("  '").append(contextPath).append("',");
        if (isImportVoyage) {
            queryBuilder.append("  '/lclImportAddVoyage.do?methodName=editVoyage',");
            queryBuilder.append("  '&headerId=").append(headerId).append("',");
        } else {
            queryBuilder.append("  if(screen.`item_desc` = 'Units/Voyages',");
            queryBuilder.append("    concat(");
            queryBuilder.append("      '/lclAddVoyage.do?methodName=editVoyage',");
            queryBuilder.append("      '&headerId=").append(headerId).append("',");
            queryBuilder.append("      '&filterByChanges='");
            queryBuilder.append("    ),");
            queryBuilder.append("    screen.`program`");
            queryBuilder.append("  ),");
        }
        queryBuilder.append("    '&moduleName=").append(isImportVoyage ? "Imports" : "Exports").append("',");
        queryBuilder.append("    '&homeScreenVoyageFileFlag=true',");
        queryBuilder.append("    if(role.`modify` <> '', '&modify=1&accessMode=1', '&modify=0&accessMode=0'),");
        queryBuilder.append("    '&programid=', screen.`item_id`,");
        queryBuilder.append("    '&date=', now()");
        queryBuilder.append("  ) as src ");
        queryBuilder.append("from");
        queryBuilder.append("  `item_treestructure` menu");
        queryBuilder.append("  join item_master screen");
        queryBuilder.append("    on (");
        queryBuilder.append("      screen.`item_id` = menu.`item_id`");
        queryBuilder.append("      and screen.`item_desc` in (");
        if (isImportVoyage) {
            queryBuilder.append("        'Voyages/Units Search'");
        } else {
            queryBuilder.append("        'Units/Voyages',");
            queryBuilder.append("        'VoyageNotification'");
        }
        queryBuilder.append("      )");
        queryBuilder.append("    )");
        queryBuilder.append("  join `role_item_assoc` role");
        queryBuilder.append("    on (");
        queryBuilder.append("      role.`item_id` = menu.`item_id`");
        queryBuilder.append("      and role.`role_id` = ").append(roleId);
        queryBuilder.append("    ) ");
        queryBuilder.append("where menu.`parent_id` = ").append(parentId);
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("itemId", IntegerType.INSTANCE);
        query.addScalar("itemDesc", StringType.INSTANCE);
        query.addScalar("program", StringType.INSTANCE);
        query.addScalar("src", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ItemsForRoleBean.class));
        return query.list();
    }

    public List<ItemsForRoleBean> getNewFclScreen(Integer parentId, Integer roleId, String screenName, String contextPath) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        boolean importFlag;
        queryBuilder.append("select");
        queryBuilder.append("  screen.item_id as itemId,");
        queryBuilder.append("  screen.item_desc as itemDesc,");
        queryBuilder.append("  screen.program as program,");
        queryBuilder.append("  concat('").append(contextPath).append("',");
        if (CommonUtils.in(screenName, "Quotes", "IMPORT QUOTE")) {
            importFlag = screenName.equals("IMPORT QUOTE");
            queryBuilder.append("   '/searchquotation.do?addQuote=addQuote&importFlag=").append(importFlag).append("',");
        } else if (CommonUtils.in(screenName, "multiQuote", "IMPORT MultiQuote")) {
            importFlag = screenName.equals("IMPORT MultiQuote");
            queryBuilder.append("   '/multiQuote.do?methodName=display&importFlag=").append(importFlag).append("',");
        } else if (CommonUtils.in(screenName, "Bookings", "IMPORT BOOKING")) {
            importFlag = screenName.equals("IMPORT BOOKING");
            queryBuilder.append("   '/searchBookings.do?newbooking=newbooking&importFlag=").append(importFlag).append("',");
        } else if (CommonUtils.isEqualIgnoreCase(screenName, "FCL BL")) {
            queryBuilder.append("   '/jsps/FCL/FclBl.jsp',");
        } else if (CommonUtils.isEqualIgnoreCase(screenName, "IMPORT FCL BL")) {
            queryBuilder.append("   '/fclBL.do?newFclBL=newFclBL&importFlag=true',");
        }
        queryBuilder.append("      if(");
        queryBuilder.append("        role.modify != '',");
        queryBuilder.append("        '&modify=1&accessMode=1',");
        queryBuilder.append("        '&modify=0&accessMode=0'");
        queryBuilder.append("      ),");
        queryBuilder.append("      '&programid=',");
        queryBuilder.append("      screen.item_id,");
        queryBuilder.append("      '&date=',");
        queryBuilder.append("      now()");
        queryBuilder.append("	) as src");
        queryBuilder.append("  from");
        queryBuilder.append("    item_treestructure menu");
        queryBuilder.append("    join item_master screen");
        queryBuilder.append("      on (");
        queryBuilder.append("        menu.item_id = screen.item_id");
        queryBuilder.append("        and screen.item_desc = '").append(screenName).append("'");
        queryBuilder.append("      )");
        queryBuilder.append("    join role_item_assoc role");
        queryBuilder.append("      on (");
        queryBuilder.append("        menu.item_id = role.item_id");
        queryBuilder.append("        and role.role_id = ").append(roleId);
        queryBuilder.append("      )");
        queryBuilder.append("  where menu.parent_id = ").append(parentId);
        queryBuilder.append("  order by screen.item_id");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("itemId", IntegerType.INSTANCE);
        query.addScalar("itemDesc", StringType.INSTANCE);
        query.addScalar("program", StringType.INSTANCE);
        query.addScalar("src", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ItemsForRoleBean.class));
        return query.list();
    }

    public List<ItemsForRoleBean> getItemsForRoleByDesc(Integer parentId, Integer roleId, String itemDesc) throws Exception {
        List<ItemsForRoleBean> itemsForRole = new ArrayList<ItemsForRoleBean>();
        StringBuilder queryBuilder = new StringBuilder("select item.itemId,item.itemDesc,item.programName,role.modify from Item item,ItemTree tree,RoleItemAssociation role");
        queryBuilder.append(" where item.itemId=tree.itemId and tree.parentId=").append(parentId);
        queryBuilder.append(" and role.itemId.itemId=tree.itemId and tree.parentId=").append(parentId);
        queryBuilder.append(" and item.programName is not null and role.roleId=").append(roleId);
        queryBuilder.append(" and item.itemDesc in (").append(itemDesc).append(")");
        queryBuilder.append(" order by item.itemId");
        List result = getCurrentSession().createQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            ItemsForRoleBean itemsForRoleBean = new ItemsForRoleBean();
            itemsForRoleBean.setItemId((Integer) col[0]);
            itemsForRoleBean.setItemDesc((String) col[1]);
            itemsForRoleBean.setProgram((String) col[2]);
            itemsForRoleBean.setModify((String) col[3]);
            itemsForRole.add(itemsForRoleBean);
        }
        return itemsForRole;
    }

    public List findAllItems() throws Exception {
        String queryString = "from Item order by itemDesc";
        Query queryObject = getCurrentSession().createQuery(queryString);

        return queryObject.list();
    }

    public void update(Item persistanceInstance, String userName) throws Exception {
        getSession().update(persistanceInstance);
    }

    public void delete(Item persistanceInstance, String userName) throws Exception {
        getSession().delete(persistanceInstance);
    }

    public List<Item> findItemName(String itemName) throws Exception {
        List<Item> list = new ArrayList<Item>();
        String queryString = "from Item where itemDesc=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", itemName);
        list = queryObject.list();
        return list;
    }

    public Item findItemNameUC(String itemName, String uniqueCode) throws Exception {
        String queryString = "from Item where itemDesc=?0 and uniqueCode=?1";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", itemName);
        queryObject.setParameter("1", uniqueCode);
        return (Item) queryObject.uniqueResult();
    }

    public List<Item> findModuleName(String itemName) throws Exception {
        List<Item> list = new ArrayList<Item>();
        String queryString = "from Item where itemDesc like ?0";
        Query query = getCurrentSession().createQuery(queryString);
        query.setParameter("0", itemName + "%");
        list = query.list();
        return list;
    }

    public List findItemNameforEdit(String itemName, String existingItemName) throws Exception {
        List list = new ArrayList();
        String queryString = "from Item where itemDesc=?0 and itemDesc != ?1";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", itemName);
        queryObject.setParameter("1", existingItemName);
        list = queryObject.list();
        return list;
    }

    public List findForSearchMenuAction(String menu, String action, Date itemcreatedon) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Item.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (menu != null && !menu.equals("")) {
            criteria.add(Restrictions.like("itemDesc", menu + "%"));
        }
        if (action != null && !action.equals("")) {
            criteria.add(Restrictions.like("programName", action + "%"));
        }
        if (itemcreatedon != null && !itemcreatedon.equals("")) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String strSODate = dateFormat.format(itemcreatedon);
            //criteria.add(Restrictions.eq("logisticOrderDate", strSODate));
            Date soStartDate = (Date) dateFormat.parse(strSODate);
            Date soEndDate = new Date(soStartDate.getYear(), soStartDate.getMonth(), soStartDate.getDate() + 1);
            criteria.add(Restrictions.between("itemcreatedon", soStartDate, soEndDate));
        }
        criteria.addOrder(Order.asc("itemDesc"));
        return criteria.list();
    }

    public List getItemCode(String itemCode) throws Exception {
        List list = new ArrayList();
        String queryString = "from Item where uniqueCode like ?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", itemCode + "%");
        list = queryObject.list();
        return list;
    }

    public List getItemCodenotLike(String itemCode) throws Exception {
        List list = new ArrayList();
        String queryString = "from Item where uniqueCode = ?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", itemCode);
        list = queryObject.list();
        return list;
    }

    public boolean getItemCodeNotEqual(String itemCode) throws Exception {
        List list = new ArrayList();
        String queryString = "from Item where uniqueCode = ?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", itemCode);
        list = queryObject.list();
        return CommonUtils.isNotEmpty(list);
    }

    public List findForSearchMenuStartsAction(String menu, String action, Date itemcreatedon) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Item.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (menu != null && !menu.equals("")) {
            criteria.add(Restrictions.ge("itemDesc", menu));
            criteria.addOrder(Order.asc("itemDesc"));
        }
        if (action != null && !action.equals("")) {
            criteria.add(Restrictions.ge("programName", action));
            criteria.addOrder(Order.asc("programName"));
        }
        if (itemcreatedon != null && !itemcreatedon.equals("")) {
            try {
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String strSODate = dateFormat.format(itemcreatedon);
                //criteria.add(Restrictions.eq("logisticOrderDate", strSODate));
                Date soStartDate = (Date) dateFormat.parse(strSODate);
                Date soEndDate = new Date(soStartDate.getYear(), soStartDate.getMonth(), soStartDate.getDate() + 1);

                criteria.add(Restrictions.ge("itemcreatedon", soStartDate));
                criteria.addOrder(Order.asc("itemcreatedon"));
            } catch (Exception e) {
                log.info("Error while parsing date on findForSearchMenuStartsAction method ", e);
            }
        }
        criteria.addOrder(Order.asc("itemDesc"));
        return criteria.list();
    }

    public List findItemName(Item parentId) throws Exception {
        List list = new ArrayList();
        String queryString = "from ItemTree where parentId=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", parentId);
        list = queryObject.list();
        return list;
    }

    public Object[] getBookingAndBlIds(String quoteId) throws Exception {
        Object[] result = null;
        String queryString = "SELECT b.bookingid, bl.bol FROM quotation q LEFT OUTER JOIN booking_fcl b ON q.file_no=b.file_no LEFT OUTER JOIN "
                + "fcl_bl bl ON q.file_no=bl.file_no WHERE quote_id = '" + quoteId + "' ORDER BY bl.bol ASC LIMIT 1";
        Query queryObject = getCurrentSession().createSQLQuery(queryString).addScalar("b.bookingid", StringType.INSTANCE).addScalar("bl.bol", StringType.INSTANCE);
        result = (Object[]) queryObject.uniqueResult();
        return result;
    }

    public List findRoleAssociation(Item item2) throws Exception {
        List list = new ArrayList();
        String queryString = "from RoleItemAssociation where itemId=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", item2);
        list = queryObject.list();
        return list;
    }

    public String getItemId(String itemDesc) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select cast(item_id as char character set latin1) as id");
        queryBuilder.append(" from item_master");
        queryBuilder.append(" where item_desc = '").append(itemDesc).append("'");
        queryBuilder.append(" limit 1");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public String getItemId(String itemDesc, String uniqueCode) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select cast(item_id as char character set latin1) as id");
        queryBuilder.append(" from item_master");
        queryBuilder.append(" where item_desc = '").append(itemDesc).append("'");
        if (CommonUtils.isNotEmpty(uniqueCode)) {
            queryBuilder.append(" and unique_code = '").append(uniqueCode).append("'");
        }
        queryBuilder.append(" limit 1");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public Item getItem(String property, Object value) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(Item.class);
        criteria.add(Restrictions.eq(property, value));
        return (Item) criteria.uniqueResult();
    }

    public Item getTradingPartnerMenu() throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select item from Item item,ItemTree tree");
        queryBuilder.append(" where item.itemId=tree.itemId and item.itemDesc = 'Trading Partner Maintenance' order by item.itemDesc");
        Object itemObj = getCurrentSession().createQuery(queryBuilder.toString()).setMaxResults(1).uniqueResult();
        return null != itemObj ? (Item) itemObj : null;
    }

    public Item getInttraInboundSIMenu() throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select item from Item item,ItemTree tree");
        queryBuilder.append(" where item.itemId=tree.itemId and item.itemDesc = 'EDI' order by item.itemDesc");
        Object itemObj = getCurrentSession().createQuery(queryBuilder.toString()).setMaxResults(1).uniqueResult();
        return null != itemObj ? (Item) itemObj : null;
    }

    public Integer getParentId(Integer itemId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  parent_id ");
        sb.append("FROM");
        sb.append("  `item_treestructure` ");
        sb.append("WHERE item_id =").append(itemId);
        return (Integer) getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
    }
}
