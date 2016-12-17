package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.bc.fcl.QuotationConstants;
import com.gp.cong.logisoft.bc.ratemanagement.PortsBC;
import com.gp.cong.logisoft.domain.RefTerminalTemp;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.util.StringFormatter;
import com.gp.cvst.logisoft.beans.SearchQuotationBean;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FileNumberForQuotaionBLBooking;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.reports.dto.QuotationDTO;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class Quotation.
 *
 * @see com.gp.cvst.logisoft.hibernate.dao.Quotation
 * @author MyEclipse - Hibernate Tools
 */
public class QuotationDAO extends BaseHibernateDAO {

    private static final Log log = LogFactory.getLog(QuotationDAO.class);

    public void save(Quotation transientInstance) throws Exception {
	getSession().saveOrUpdate(transientInstance);
	getSession().flush();
    }

    public Iterator getAllQuotesNo() throws Exception {
	Iterator results = null;
	String queryString = "";
	queryString = "select quoteId,quoteNo from Quotation";
	Query queryObject = getSession().createQuery(queryString);
	results = queryObject.list().iterator();

	return results;
    }

    public List findAccountPrefix() throws Exception {
	List list = new ArrayList();
	String queryString = "from Quotation order by quoteId desc";
	Query queryObject = getCurrentSession().createQuery(queryString);

	queryObject.setMaxResults(1);
	list = queryObject.list();
	return list;
    }

    public void delete(Quotation persistentInstance) throws Exception {
	getSession().delete(persistentInstance);
	getSession().flush();
    }

    public Quotation findById(java.lang.Integer id) throws Exception {
	Quotation instance = (Quotation) getSession().get(
		"com.gp.cvst.logisoft.domain.Quotation", id);
	return instance;
    }

    public List findById1(java.lang.String id) throws Exception {
	String queryString = "from Quotation where quoteNo=?0";
	Query queryObject = getSession().createQuery(queryString);
	queryObject.setParameter("0", id);
	return queryObject.list();
    }

    public List findByExample(Quotation instance) throws Exception {
	List results = getSession().createCriteria(
		"com.gp.cvst.logisoft.domain.Quotation").add(
		Example.create(instance)).list();
	log.debug("find by example successful, destination size: "
		+ results.size());
	return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
	String queryString = "from Quotation as model where model."
		+ propertyName + "= ?0";
	Query queryObject = getSession().createQuery(queryString);
	queryObject.setParameter("0", value);
	return queryObject.list();
    }

    public Quotation merge(Quotation detachedInstance) throws Exception {
	Quotation destination = (Quotation) getSession().merge(detachedInstance);
	return destination;
    }

    public void attachDirty(Quotation instance) throws Exception {
	getSession().saveOrUpdate(instance);
    }

    public void attachClean(Quotation instance) throws Exception {
	getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public Integer getQuotesId() throws Exception {
	Integer destination = 0;
	String Query = "select max(Q.quoteId) from Quotation Q ";
	Iterator itr = getCurrentSession().createQuery(Query).list().iterator();
	destination = (Integer) itr.next();
	return destination;
    }

    public List showfordeatils1() throws Exception {
	String queryString = "from Quotation";
	Query queryObject = getCurrentSession().createQuery(queryString);
	return queryObject.list();
    }

    public List showfordeatils() throws Exception {
	Date today = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	SearchQuotationBean objsearchQuotationBean = null;
	List<SearchQuotationBean> lstGenericIfo = new ArrayList<SearchQuotationBean>();
	String queryString1 = "select q.quoteNo,q.quoteDate,q.plor,q.plod,q.origin_terminal.terminalLocation,q.destination_port.portname,q.clienttype from Quotation q order by q.quoteNo";
	List queryObject = getCurrentSession().createQuery(queryString1).list();
	Iterator iter = queryObject.iterator();
	while (iter.hasNext()) {
	    objsearchQuotationBean = new SearchQuotationBean();
	    Object[] row = (Object[]) iter.next();
	    String quoteNo = (String) (row[0]);
	    Date quotestartDate = (Date) row[1];
	    String quoteDate = sdf.format(quotestartDate);
	    String plor = (String) row[2];
	    String plod = (String) row[3];
	    String pol = (String) row[4];
	    String pod = (String) row[5];
	    String clienttype = (String) row[6];
	    objsearchQuotationBean.setQuoteno(quoteNo);
	    objsearchQuotationBean.setStartdate(quoteDate);
	    objsearchQuotationBean.setPlor(plor);
	    objsearchQuotationBean.setPlod(plod);
	    objsearchQuotationBean.setPol(pol);
	    objsearchQuotationBean.setPod(pod);
	    String enddate = sdf.format(today);
	    objsearchQuotationBean.setQuoteendate(enddate);
	    objsearchQuotationBean.setClientype(clienttype);
	    lstGenericIfo.add(objsearchQuotationBean);
	    objsearchQuotationBean = null;
	}
	return lstGenericIfo;
    }

    public List getQuoteNo(String quoteno) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(
		Quotation.class);
	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	if (quoteno != null && !quoteno.equals("")) {
	    criteria.add(Restrictions.like("quoteNo", quoteno + "%"));
	    criteria.addOrder(Order.asc("quoteNo"));
	}
	return criteria.list();
    }

    public List showsearchquot(String quoteno, String plor, String startdate,
	    String endate, String plod, String pol, String pod,
	    String clienttype, String clientName, String carier,
	    String quoteBY, String issuingTerminal) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(
		Quotation.class);
	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	Integer quoteId = 0;

	if (quoteno != null && !quoteno.equals("")) {
	    quoteId = Integer.parseInt(quoteno);
	}
	if (quoteId != null && quoteId != 0) {
	    criteria.add(Restrictions.like("fileNo", quoteId));
	}
	if (plor != null && !plor.equals("")) {
	    criteria.add(Restrictions.like("plor", plor + "%"));
	}
	if (quoteBY != null && !quoteBY.equals("")) {
	    criteria.add(Restrictions.like("quoteBy", quoteBY + "%"));
	}
	if (pol != null && !pol.equals("")) {
	    criteria.add(Restrictions.like("origin_terminal", pol + "%"));
	}
	if (plod != null && !plod.equals("")) {
	    criteria.add(Restrictions.like("finaldestination", plod + "%"));
	}
	if (issuingTerminal != null && !issuingTerminal.equals("")) {
	    criteria.add(Restrictions.like("issuingTerminal",
		    issuingTerminal + "%"));
	}
	if (pod != null && !pod.equals("")) {
	    criteria.add(Restrictions.like("destination_port", pod + "%"));

	}
	if (clienttype != null && !clienttype.equals("")
		&& !clienttype.equals("00")) {
	    criteria.add(Restrictions.like("clienttype", clienttype + "%"));

	}
	if (clientName != null && !clientName.equals("")) {
	    criteria.add(Restrictions.like("clientname", clientName + "%"));

	}
	if (carier != null && !carier.equals("")) {
	    criteria.add(Restrictions.like("sslname", carier + "%"));

	}

	if (startdate != null && !startdate.equals("")) {
	    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	    Date soStartDate = (Date) dateFormat.parse(startdate);
	    criteria.add(Restrictions.ge("quoteDate", soStartDate));
	    criteria.addOrder(Order.asc("quoteDate"));
	}
	if (endate != null && !endate.equals("")) {
	    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	    Date soEndDate = (Date) dateFormat.parse(endate);
	    criteria.add(Restrictions.le("quoteDate", soEndDate));
	    criteria.addOrder(Order.asc("quoteDate"));
	}
	return criteria.list();
    }

    // Nagendra
    public List getQuotationList(String quotno) throws Exception {

	List<SearchQuotationBean> Searchbean = new ArrayList<SearchQuotationBean>();
	String queryString = "select q.quoteNo,q.phone,q.fax,q.contactname,q.clientname,q.email1,q.comment1,q.amount,q.amount1,q.insurancamt,q.markUp,q.ldinclude,q.ldprint,q.idinclude,q.idprint,q.insureinclude,q.insureprint,q.origin_terminal,q.commcode.codedesc,q.destination_port,q.goodsdesc,q.sslname,q.baht,q.bdt,q.cyp,q.eur,q.hkd,q.lkr,q.nt,q.prs,q.rmb,q.won,q.yen,q.printDesc,q.from,q.plor,q.finaldestination,q.commodityPrint,q.carrierPrint,q.transitTime,q.soc,q.hazmat from Quotation q where q.quoteNo='"
		+ quotno + "'";
	List queryObject = getCurrentSession().createQuery(queryString).list();

	Iterator itr = queryObject.iterator();
	NumberFormat number = new DecimalFormat("###,###,##0.00");
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	SearchQuotationBean searchdto = null;
	while (itr.hasNext()) {

	    searchdto = new SearchQuotationBean();
	    Object[] row = (Object[]) itr.next();
	    String quotationNo = (String) row[0];
	    String phoneno = (String) row[1];
	    String fax = (String) row[2];
	    String contactname = (String) row[3];
	    String cutomername = (String) row[4];
	    String email = (String) row[5];
	    String Comment = (String) row[6];
	    Double localdryage = (Double) row[7];
	    Double intermodal = (Double) row[8];
	    Double insuranceamt = (Double) row[9];
	    Double markUp = (Double) row[10];
	    String ldinclude = (String) row[11];

	    if (ldinclude == null) {
		ldinclude = "off";
	    }
	    String ldprint = (String) row[12];

	    if (ldprint == null) {
		ldprint = "off";
	    }
	    String idinclude = (String) row[13];

	    if (idinclude == null) {
		idinclude = "off";
	    }
	    String idprint = (String) row[14];

	    if (idprint == null) {
		idprint = "off";
	    }
	    String insureinclude = (String) row[15];

	    if (insureinclude == null) {
		insureinclude = "off";
	    }
	    String insureprint = (String) row[16];

	    if (insureprint == null) {
		insureprint = "off";
	    }
	    String originport = (String) row[35];
	    String commodity = (String) row[18];
	    String destination = (String) row[36];
	    String goodsdesc = (String) row[20];
	    String sslname = (String) row[21];
	    Double baht = (Double) row[22];
	    Double bdt = (Double) row[23];
	    Double cyp = (Double) row[24];
	    Double eur = (Double) row[25];
	    Double hkd = (Double) row[26];
	    Double lkr = (Double) row[27];
	    Double nt = (Double) row[28];
	    Double prs = (Double) row[29];
	    Double rmb = (Double) row[30];
	    Double won = (Double) row[31];
	    Double yen = (Double) row[32];
	    String printDesc = (String) row[33];
	    String from = (String) row[34];
	    String pol = (String) row[17];
	    String pod = (String) row[19];
	    String commodityPrint = (String) row[37];
	    String carrierPrint = (String) row[38];
	    Integer transitTime = (Integer) row[39];
	    String soc = (String) row[40];
	    String hazmat = (String) row[41];
	    if (soc == null) {
		soc = "N";
	    }
	    searchdto.setSoc(soc);
	    if (transitTime != null) {
		searchdto.setTransitTime(String.valueOf(transitTime));
	    } else {
		searchdto.setTransitTime("");
	    }
	    if (commodityPrint == null) {
		commodityPrint = "off";
	    }
	    if (carrierPrint == null) {
		carrierPrint = "off";
	    }
	    if (baht == null) {
		baht = 0.0;
	    }
	    if (bdt == null) {
		bdt = 0.0;
	    }
	    if (cyp == null) {
		cyp = 0.0;
	    }
	    if (eur == null) {
		eur = 0.0;
	    }
	    if (hkd == null) {
		hkd = 0.0;
	    }
	    if (lkr == null) {
		lkr = 0.0;
	    }
	    if (nt == null) {
		nt = 0.0;
	    }
	    if (prs == null) {
		prs = 0.0;
	    }
	    if (rmb == null) {
		rmb = 0.0;
	    }
	    if (won == null) {
		won = 0.0;
	    }

	    if (yen == null) {
		yen = 0.0;
	    }
	    searchdto.setLdinclude(ldinclude);
	    searchdto.setLdprint(ldprint);
	    searchdto.setIdinclude(idinclude);
	    searchdto.setIdprint(idprint);
	    searchdto.setInsureinclude(insureinclude);
	    searchdto.setInsureprint(insureprint);
	    searchdto.setCompany(cutomername);
	    searchdto.setTo(contactname);
	    searchdto.setFrom(from);
	    searchdto.setCarrierPrint(carrierPrint);
	    searchdto.setCommodityPrint(commodityPrint);
	    searchdto.setPol(pol);
	    searchdto.setPod(pod);
	    searchdto.setFax(fax);
	    searchdto.setPhoneno(phoneno);
	    searchdto.setLocaldryageamt(localdryage.toString());
	    searchdto.setIntermodalamt(intermodal.toString());
	    searchdto.setInsurenceamt(insuranceamt.toString());
	    searchdto.setQuoteno(quotationNo);
	    Double tot = localdryage + intermodal + insuranceamt;
	    searchdto.setTot2(tot.toString());
	    // for contact name
	    searchdto.setEmail(email);
	    searchdto.setComment(Comment);
	    searchdto.setCommodity(commodity);
	    searchdto.setOriginport(originport);
	    searchdto.setDestination(destination);
	    searchdto.setGoodsdesc(goodsdesc);
	    searchdto.setCarrier(sslname);
	    searchdto.setBaht(number.format(baht));
	    searchdto.setBdt(number.format(bdt));
	    searchdto.setCyp(number.format(cyp));
	    searchdto.setEur(number.format(eur));
	    searchdto.setHkd(number.format(hkd));
	    searchdto.setLkr(number.format(lkr));
	    searchdto.setNt(number.format(nt));
	    searchdto.setPrs(number.format(prs));
	    searchdto.setRmb(number.format(rmb));
	    searchdto.setWon(number.format(won));
	    searchdto.setYen(number.format(yen));
	    searchdto.setPrintDesc(printDesc);
	    searchdto.setHazmat(hazmat);
	    Searchbean.add(searchdto);

	    searchdto = null;
	}

	return Searchbean;
    }

    public List getQtFieldsList(String quotno) throws Exception {
	NumberFormat number = new DecimalFormat("###,###,##0.00");
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

	List<QuotationDTO> Quotationlist = new ArrayList<QuotationDTO>();
	String queryStringId = "select qt.quoteId,qt.quoteNo from Quotation qt where qt.quoteNo='"
		+ quotno + "'";
	List queryObject22 = getCurrentSession().createQuery(queryStringId).list();

	Iterator itrid = queryObject22.iterator();
	QuotationDTO QuDto = null;
	while (itrid.hasNext()) {
	    Object[] row22 = (Object[]) itrid.next();
	    Integer Quotationid = (Integer) row22[0];
	    String queryforDistinctUT = "select distinct(c.unitType) from Charges c where c.qouteId='"
		    + Quotationid + "' ";
	    List distUnitTypeList = getCurrentSession().createQuery(
		    queryforDistinctUT).list();
	    int i = 0;
	    while (i < distUnitTypeList.size()) {
		String unitType = (String) distUnitTypeList.get(i);
		List<QuotationDTO> tempList = null;
		String QueryforData = "select c.amount,c.chgCode,c.unitType,qt.localdryage,c.include,c.print,c.markUp,c.number,c.currecny from Quotation qt,Charges c where qt.quoteId='"
			+ Quotationid
			+ "' and c.qouteId='"
			+ Quotationid
			+ "' and c.unitType='" + unitType + "'";
		List dataList = getCurrentSession().createQuery(QueryforData).list();
		Iterator itr2 = dataList.iterator();
		Double includedAmt = 0.0;
		Double usdAmt = 0.0;
		Double BAHTamt = 0.0;
		Double BDTamt = 0.0;
		Double CYPamt = 0.0;
		Double EURamt = 0.0;
		Double HKDamt = 0.0;
		Double LKRamt = 0.0;
		Double NTamt = 0.0;
		Double PRSamt = 0.0;
		Double RMBamt = 0.0;
		Double WONamt = 0.0;
		Double YENamt = 0.0;
		Double INRamt = 0.0;
		Double INDamt = 0.0;
		HashMap<String, Double> currencyMap = new HashMap<String, Double>();
		tempList = new ArrayList<QuotationDTO>();
		int ut = 0;
		while (itr2.hasNext()) {
		    QuDto = new QuotationDTO();

		    Object[] row3 = (Object[]) itr2.next();
		    Double amount = (Double) row3[0];
		    String chgCode = (String) row3[1];
		    String unitTyp = (String) row3[2];
		    String locDryage = (String) row3[3];
		    String include = (String) row3[4];
		    String print = (String) row3[5];
		    Double markup = (Double) row3[6];
		    String Contnumber = (String) row3[7];
		    String currency = (String) row3[8];

		    if (amount == null) {
			amount = 0.0;
		    }
		    if (markup == null) {
			markup = 0.0;
		    }
		    if (include == null) {
			include = "off";
		    }
		    if (print == null) {
			print = "off";
		    }

		    if (!currencyMap.containsKey(currency)) {
			currencyMap.put(currency, amount + markup);
		    } else {
			Double currencyAmount = 0.0;
			currencyAmount = currencyMap.get(currency);
			currencyMap.put(currency, currencyAmount + amount
				+ markup);
		    }
		    unitTyp = getContainerSizeFromUnitType(unitTyp);
		    if (ut == 0) {
			QuDto.setContainer(Contnumber + " X " + unitTyp);
			ut++;
		    } else {
			QuDto.setContainer("");
		    }
		    QuDto.setAmount(number.format(amount + markup));
		    QuDto.setCharge(chgCode);
		    QuDto.setCurrency(currency.substring(0, 3));
		    includedAmt = includedAmt + amount + markup;
		    tempList.add(QuDto);
		    QuDto = null;

		}
		if (tempList != null && tempList.size() > 0) {
		    int t = 0;
		    while (tempList.size() > t) {

			QuotationDTO qdto = new QuotationDTO();
			qdto = (QuotationDTO) tempList.get(t);
			String chargeCodeDesc = qdto.getCharge();
			String chargecode = getChargecodefromGenericcode(chargeCodeDesc);
			if (chargecode != null
				&& chargecode.equalsIgnoreCase("OFR")) {
			    String camt = qdto.getAmount();
			    if (camt.contains(",")) {
				camt = camt.replaceAll(",", "");
			    }

			    Double ofamt = Double.parseDouble(camt)
				    + includedAmt;

			    qdto.setAmount(number.format(ofamt));

			}
			if (t == (tempList.size() - 1)) {
			    StringBuffer stringBuffer = new StringBuffer("");
			    for (Iterator iterator = currencyMap.keySet().iterator(); iterator.hasNext();) {
				String currencyname = (String) iterator.next();
				double d = currencyMap.get(currencyname);
				stringBuffer.append(number.format(d)).append("(").append(currencyname).append(")");
				if (iterator.hasNext()) {
				    stringBuffer.append(",");
				}
			    }
			    qdto.setContotal(stringBuffer.toString());
			    Quotationlist.add(qdto);
			} else {
			    Quotationlist.add(qdto);
			}
			qdto = null;
			t++;
		    }

		}

		i++;
	    }
	}
	if (Quotationlist.isEmpty()) {
	    QuDto = new QuotationDTO();
	    QuDto.setContainer("");
	    QuDto.setAmount("");
	    QuDto.setCharge("");
	    QuDto.setCurrency("");
	    Quotationlist.add(QuDto);

	}
	return Quotationlist;
    }

    public String getContainerSizeFromUnitType(String unittype) throws Exception {

	String destination = "";
	String queryStringcode = "select gc.codedesc from GenericCode gc where gc.id='"
		+ unittype + "'";
	Iterator queryObjectcode = getCurrentSession().createQuery(
		queryStringcode).list().iterator();
	if (queryObjectcode.hasNext()) {
	    destination = (String) queryObjectcode.next();
	}
	return destination;
    }

    public int findquotid(String paramid2) throws Exception {
	int destination = 0;
	String qeury = "select  q.quoteId from Quotation q where q.quoteNo='"
		+ paramid2 + "'";
	Iterator it = getCurrentSession().createQuery(qeury).list().iterator();
	destination = (Integer) it.next();
	return destination;
    }

    public String findQuoteId(String fileNumber) throws Exception {
	String fileNo = fileNumber.indexOf("-") > 1 ? fileNumber.substring(0, fileNumber.indexOf("-")) : fileNumber;
	String qeury = "select q.Quote_ID from Quotation q where q.file_No='" + fileNo + "' limit 1";
	Object quoteId = getCurrentSession().createSQLQuery(qeury).uniqueResult();
	return null!=quoteId && !quoteId.toString().equals("0") ? quoteId.toString()  :"" ;
    }

    public String findFileNo(String quoteID) throws Exception {
	String qeury = "SELECT file_no FROM Quotation WHERE quote_id='" + quoteID + "' limit 1";
	return (String) getCurrentSession().createSQLQuery(qeury.toString()).uniqueResult();
    }

    public List abc(int paramid2) throws Exception {
	SearchQuotationBean objsearchQuotationBean = null;
	List<SearchQuotationBean> lstGenericIfo = new ArrayList<SearchQuotationBean>();
	String queryString = "select q.quoteNo,q.plor,q.plod,q.origin_terminal.terminalLocation,q.destination_port.portname from Quotation q where q.quoteId='"
		+ paramid2 + "'";
	List queryObject = getCurrentSession().createQuery(queryString).list();
	Iterator iter = queryObject.iterator();
	while (iter.hasNext()) {
	    objsearchQuotationBean = new SearchQuotationBean();
	    Object[] row = (Object[]) iter.next();

	    RefTerminalTemp ref = new RefTerminalTemp();
	    RefTerminalTemp ref1 = new RefTerminalTemp();
	    String quoteNo = (String) (row[0]);
	    String plor = (String) (row[1]);
	    String plod = (String) (row[2]);
	    String pol = (String) (row[3]);
	    String pod = (String) (row[4]);
	    String date = "01/07/2008";
	    objsearchQuotationBean.setQuoteno(quoteNo);
	    objsearchQuotationBean.setPlor(plor);
	    objsearchQuotationBean.setPlod(plod);
	    objsearchQuotationBean.setStartdate(date);
	    objsearchQuotationBean.setPol(pol);
	    objsearchQuotationBean.setPod(pod);
	    lstGenericIfo.add(objsearchQuotationBean);
	    objsearchQuotationBean = null;
	}
	return lstGenericIfo;
    }

    public void update(Quotation persistanceInstance) throws Exception {
	getSession().saveOrUpdate(persistanceInstance);
	getSession().flush();
    }

    public void updateFromScheduler(Quotation persistanceInstance) throws Exception {
	Transaction transaction = getCurrentSession().beginTransaction();
	getCurrentSession().saveOrUpdate(persistanceInstance);
	transaction.commit();
    }

    // To get List of Effective Dates to print in the comments Box
    public List getListofEffDatesforQuotaion(String quotno) throws Exception {
	List effDate = new ArrayList();
	NumberFormat number = new DecimalFormat("###,###,##0.00");
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	String queryStringId = "select qt.quoteId,qt.quoteNo from Quotation qt where qt.quoteNo='"
		+ quotno + "'";
	List queryObject22 = getCurrentSession().createQuery(queryStringId).list();
	Iterator itrid = queryObject22.iterator();
	while (itrid.hasNext()) {
	    Object[] row22 = (Object[]) itrid.next();
	    Integer Quotationid = (Integer) row22[0];
	    String queryforDistinctUT = "select distinct(c.unitType) from Charges c where c.qouteId='"
		    + Quotationid + "' and c.include='on' ";
	    List distUnitTypeList = getCurrentSession().createQuery(
		    queryforDistinctUT).list();
	    int i = 0;
	    while (i < distUnitTypeList.size()) {
		String unitType = (String) distUnitTypeList.get(i);
		String QueryforData = "select c.unitType,c.efectiveDate,c.amount,c.futureRate,c.chgCode,c.costType,c.currecny from Charges c where c.qouteId='"
			+ Quotationid + "' and c.unitType='" + unitType + "'";
		List dataList = getCurrentSession().createQuery(QueryforData).list();
		Iterator itr2 = dataList.iterator();
		while (itr2.hasNext()) {
		    Object[] row3 = (Object[]) itr2.next();
		    String unitypeId = (String) row3[0];
		    Date efDate = (Date) row3[1];
		    String edate = "";
		    if (efDate != null) {
			edate = sdf.format(efDate);
			edate = edate + ".";
		    }
		    Double amount = (Double) row3[2];
		    if (amount == null) {
			amount = 0.0;
		    }
		    Double futureRate = (Double) row3[3];
		    if (futureRate == null) {
			futureRate = 0.0;
		    }
		    String chgCode = (String) row3[4];
		    String costType = (String) row3[5];
		    String currency = (String) row3[6];
		    String UnitType = getContainerSizeFromUnitType(unitypeId);
		    String incdec = "";
		    Double incdecAmt = 0.0;
		    if ((futureRate - amount) > 0) {
			incdec = "increase";
			incdecAmt = futureRate - amount;
		    } else if ((futureRate - amount) < 0) {
			incdec = "decrease";
			incdecAmt = (futureRate - amount) * (-1);
		    }
		    String d = chgCode + " will " + incdec + " by "
			    + number.format(incdecAmt) + " " + currency + " ("
			    + costType + ") for " + UnitType + " Effective "
			    + edate;
		    if (!edate.equals("")) {
			effDate.add(d);
		    }
		}
		i++;
	    }
	}// While
	return effDate;
    }

    public String getChargecodefromGenericcode(String desc) throws Exception {
	String QueryString = "Select g.code from GenericCode g where g.codetypeid='36' and g.codedesc='"
		+ desc + "' limit 1";
	String destination = (String)getCurrentSession().createQuery(QueryString).uniqueResult();
	return null!=destination?destination:"";
    }

    public Integer getmaxFile() {
	String QueryString = "select max(fileNo)+1 from Quotation";
	Integer fileId = (Integer)getCurrentSession().createQuery(QueryString).uniqueResult();
	return null!=fileId?fileId:0;
    }

    public List getTotalList(String quotno) throws Exception {
	List totals = null;
	NumberFormat number = new DecimalFormat("###,###,##0.00");
	String queryStringId = "select q.totalCharges,q.baht,q.bdt,q.cyp,q.eur,q.hkd,q.lkr,q.nt,q.prs,q.rmb,q.won,q.yen from Quotation q where q.quoteNo='"
		+ quotno + "'";
	List<QuotationDTO> quotationDTOList = new ArrayList<QuotationDTO>();
	totals = getCurrentSession().createQuery(queryStringId).list();
	for (Iterator iter = totals.iterator(); iter.hasNext();) {
	    Object[] totalObjAry = (Object[]) iter.next();
	    QuotationDTO quotationDTO = new QuotationDTO();
	    if (totalObjAry[0] == null) {
		totalObjAry[0] = 0.0;
	    }
	    if (totalObjAry[1] == null) {
		totalObjAry[1] = 0.0;
	    }
	    if (totalObjAry[2] == null) {
		totalObjAry[2] = 0.0;
	    }
	    if (totalObjAry[3] == null) {
		totalObjAry[3] = 0.0;
	    }
	    if (totalObjAry[4] == null) {
		totalObjAry[4] = 0.0;
	    }
	    if (totalObjAry[5] == null) {
		totalObjAry[5] = 0.0;
	    }
	    if (totalObjAry[6] == null) {
		totalObjAry[6] = 0.0;
	    }
	    if (totalObjAry[7] == null) {
		totalObjAry[7] = 0.0;
	    }
	    if (totalObjAry[8] == null) {
		totalObjAry[8] = 0.0;
	    }
	    if (totalObjAry[9] == null) {
		totalObjAry[9] = 0.0;
	    }
	    if (totalObjAry[10] == null) {
		totalObjAry[11] = 0.0;
	    }
	    quotationDTO.setTotal(number.format(totalObjAry[0]));
	    quotationDTO.setCury1(number.format(totalObjAry[1]));
	    quotationDTO.setCury2(number.format(totalObjAry[2]));
	    quotationDTO.setCury3(number.format(totalObjAry[3]));
	    quotationDTO.setCury4(number.format(totalObjAry[4]));
	    quotationDTO.setCury5(number.format(totalObjAry[5]));
	    quotationDTO.setCury6(number.format(totalObjAry[6]));
	    quotationDTO.setCury7(number.format(totalObjAry[7]));
	    quotationDTO.setCury8(number.format(totalObjAry[8]));
	    quotationDTO.setCury9(number.format(totalObjAry[9]));
	    quotationDTO.setCury10(number.format(totalObjAry[10]));
	    quotationDTO.setCury11(number.format(totalObjAry[11]));

	    quotationDTOList.add(quotationDTO);
	}
	return quotationDTOList;
    }

    public List getSearchListByFileNumber(Map searchMap, String filterBy,
	    String sortByDate, String limit, boolean importFlag, String agentQuery) throws Exception {
	List searchList = null;
	List searchListByFileNumber = new ArrayList();
	String queryStringcode = null;
	int limitFromUser = "All".equalsIgnoreCase(limit) ? -1 : Integer.parseInt(limit);
	FileNumberForQuotaionBLBooking fileNumberForQuotaionBLBooking = null;
	if (importFlag) {
	    queryStringcode = getQueryStringForImport(searchMap, filterBy,
		    sortByDate, agentQuery);
	} else {
	    queryStringcode = getQueryString(searchMap, filterBy,
		    sortByDate, agentQuery);
	}
	if ("All".equalsIgnoreCase(limit)) {
	    searchList = getCurrentSession().createSQLQuery(queryStringcode).list();
	} else {
	    searchList = getCurrentSession().createSQLQuery(queryStringcode).setMaxResults(
		    Integer.parseInt(limit)).list();
	}
	int countLimit = 0;
	for (Object _obj : searchList) {
	    if (countLimit > limitFromUser && limitFromUser != -1) {
		break;
	    } else {
		countLimit++;
	    }
	    Object[] quoteBookingFclId = (Object[]) _obj;
	    fileNumberForQuotaionBLBooking = new FileNumberForQuotaionBLBooking(
		    quoteBookingFclId, importFlag);
	    searchListByFileNumber.add(fileNumberForQuotaionBLBooking);
	}
	return searchListByFileNumber;
    }

    public String appendExportString(Set<Map.Entry> entrySet) throws Exception {
	StringBuilder conditionBuffer = new StringBuilder();
	String inputResult = null;
	conditionBuffer.append("AND (");
	for (Entry entry : entrySet) {
	    String columnName = entry.getKey().toString();
	    if (columnName.equals(FileNumberForQuotaionBLBooking.CLIENT)
		    || entry.getKey().equals(
		    FileNumberForQuotaionBLBooking.SHIPPER)
		    || entry.getKey().equals(
		    FileNumberForQuotaionBLBooking.FORWARD)
		    || entry.getKey().equals(
		    FileNumberForQuotaionBLBooking.CONGINEE)) {
		if (columnName.equals(FileNumberForQuotaionBLBooking.CLIENT)) {
		    conditionBuffer.append("q.clientName like '%").append(((String) entry.getValue()).replace("'", "\\'")).append("%'  OR ");
		} else {
		    if (entry.getKey().equals(
			    FileNumberForQuotaionBLBooking.SHIPPER)) {
			conditionBuffer.append("b.shipper like '%").append(((String) entry.getValue()).replace("'", "\\'")).append("%'  OR ");
		    } else if (entry.getKey().equals(
			    FileNumberForQuotaionBLBooking.FORWARD)) {
			conditionBuffer.append("b.forward like '%").append(((String) entry.getValue()).replace("'", "\\'")).append("%'  OR ");
		    } else if (entry.getKey().equals(
			    FileNumberForQuotaionBLBooking.CONGINEE)) {
			conditionBuffer.append("b.consignee like '%").append(((String) entry.getValue()).replace("'", "\\'")).append("%'  OR ");
		    }
		}
		if (entry.getKey().equals(
			FileNumberForQuotaionBLBooking.SHIPPER)) {
		    conditionBuffer.append("f.house_shipper_name like '%").append(((String) entry.getValue()).replace("'", "\\'")).append("%'  OR ");
		} else if (entry.getKey().equals(
			FileNumberForQuotaionBLBooking.FORWARD)) {
		    conditionBuffer.append("f.forwarding_agent_name like '%").append(((String) entry.getValue()).replace("'", "\\'")).append("%'  OR ");
		} else if (entry.getKey().equals(
			FileNumberForQuotaionBLBooking.CONGINEE)) {
		    conditionBuffer.append("f.house_consignee_name like '%").append(((String) entry.getValue()).replace("'", "\\'")).append("%'  OR ");
		}
	    }
	}
	int i = conditionBuffer.lastIndexOf("OR");
	if (i > -1) {
	    inputResult = conditionBuffer.substring(0, i) + ")";
	}

	return inputResult;
    }

    public String appendString(Set<Map.Entry> entrySet) throws Exception {
	StringBuilder conditionBuffer = new StringBuilder();
	String inputResult = null;
	conditionBuffer.append("AND (");
	for (Entry entry : entrySet) {
	    String columnName = entry.getKey().toString();
	    if (columnName.equals(FileNumberForQuotaionBLBooking.CLIENT)
		    || entry.getKey().equals(
		    FileNumberForQuotaionBLBooking.SHIPPER)
		    || entry.getKey().equals(
		    FileNumberForQuotaionBLBooking.FORWARD)
		    || entry.getKey().equals(
		    FileNumberForQuotaionBLBooking.CONGINEE)) {
		conditionBuffer.append(entry.getKey()).append(" like '%").append(entry.getValue()).append("%'  OR ");
		if (entry.getKey().equals(
			FileNumberForQuotaionBLBooking.SHIPPER)) {
		    conditionBuffer.append(FileNumberForQuotaionBLBooking.BLSHIPPER + " like '%").append(entry.getValue()).append("%'  OR ");
		} else if (entry.getKey().equals(
			FileNumberForQuotaionBLBooking.FORWARD)) {
		    conditionBuffer.append(FileNumberForQuotaionBLBooking.BLFORWARDER + " like '%").append(entry.getValue()).append("%'  OR ");
		} else if (entry.getKey().equals(
			FileNumberForQuotaionBLBooking.CONGINEE)) {
		    conditionBuffer.append(FileNumberForQuotaionBLBooking.BLCONGINEE + " like '%").append(entry.getValue()).append("%'  OR ");
		}
	    }
	}
	int i = conditionBuffer.lastIndexOf("OR");
	if (i > -1) {
	    inputResult = conditionBuffer.substring(0, i) + ")";
	}

	return inputResult;
    }

    public String getQueryStringForImport(Map searchMap, String filterBy,
	    String sortByDate, String agentQuery) throws Exception {
	String condition = " ";
	StringBuilder queryBuffer = new StringBuilder();
	boolean fetchBooking = false;
	boolean fetchBL = false;
	boolean blVoidFlag = false;
	if (sortByDate.equalsIgnoreCase("C")) {
	    condition = " AND b.bookingId IS NOT NULL AND b.PortCutOff >= CURRENT_DATE()";
	    fetchBooking = true;
	    // This is when sort by is "container cutt off" and manditorily we
	    // need to filter out quotation
	    // and we need to display container cut off date which is less than
	    // current date
	} else if (sortByDate.equalsIgnoreCase("D")) {
	    condition = " AND b.bookingId IS NOT NULL AND b.Doc_Cut_Off >= CURRENT_DATE()";
	    fetchBooking = true;
	    // This is when sort by is "DOC cutt off" and manditorily we
	    // need to filter out quotation
	    // and we need to display container cut off date which is less than
	    // current date
	} else if (sortByDate.equalsIgnoreCase("E")) {
	    condition = " AND b.bookingId IS NOT NULL AND b.Etd >= CURRENT_DATE()";
	    fetchBooking = true;
	    // This is when sort by is "container cutt off" and manditorily we
	    // need to filter out quotation
	    // and we need to display container cut off date which is less than
	    // current date
	} else if (sortByDate.equalsIgnoreCase("ETA")) {
	    condition = " AND ((b.bookingId IS NOT NULL AND b.Eta >= CURRENT_DATE()) or (f.Bol IS NOT NULL and f.eta >= CURRENT_DATE()) ) ";
	    fetchBooking = true;
	    fetchBL = true;
	    // This is when sort by is "ETA DATE" and manditorily we
	    // need to filter out quotation
	    // and we need to display ETA date which is less than
	    // current date
	} else if (QuotationConstants.SEARCHBYALL.equals(filterBy)) {
	    fetchBooking = true;
	    fetchBL = true;
	    // TODO : check whether it is required or not.
	} else if (QuotationConstants.SEARCHBYFCL.equals(filterBy)) {
	    fetchBL = true;
	    condition = " AND f.Bol IS NOT NULL ";
	} else if (QuotationConstants.SEARCHBYBOOKING.equals(filterBy)) {
	    condition = " AND f.Bol IS NULL AND b.bookingId IS NOT NULL ";
	    fetchBooking = true;
	} else if (QuotationConstants.SEARCHBYQUOTE.equals(filterBy)) {
	    condition = " AND q.Quote_ID IS NOT NULL AND b.bookingId IS NULL AND f.Bol IS NULL";
	} else if ("SSL".equals(filterBy)) {
	    condition = " AND f.received_master ='No' ";
	    fetchBL = true;
	} else if ("UMF".equals(filterBy)) {
	    condition = " AND (f.manifested_date IS NULL OR f.manifested_date = '')  AND f.file_no IS NOT NULL ";
	    fetchBL = true;
	} else if ("MF".equals(filterBy)) {
	    condition = " AND (f.manifested_date IS NOT NULL AND f.manifested_date != '') AND (f.ready_to_post = 'M') ";
	    fetchBL = true;
	} else if ("MNM".equals(filterBy)) {
	    condition = " AND (f.manifested_date IS NOT NULL AND f.manifested_date != '') AND (f.ready_to_post = 'M') AND f.received_master ='No' ";
	    fetchBL = true;
	} else if ("IR".equals(filterBy)) {
	    condition = " AND f.Bol IS NOT NULL and f.import_Release = 'Y' and f.payment_Release='Y'";
	    fetchBL = true;
	} else if ("NR".equals(filterBy)) {
	    condition = " AND f.Bol IS NOT NULL and (f.import_Release = 'N' or f.import_Release is null) and (f.payment_Release='N' or f.payment_Release is null)";
	    fetchBL = true;
	} else if ("DR".equals(filterBy)) {
	    condition = " AND f.Bol IS NOT NULL and f.import_Release = 'Y' and (f.payment_Release='N' or f.payment_Release is null) ";
	    fetchBL = true;
	} else if ("PR".equals(filterBy)) {
	    condition = " AND f.Bol IS NOT NULL and  (f.import_Release = 'N' or f.import_Release is null) and f.payment_Release='Y'";
	    fetchBL = true;
	} else if ("OP".equals(filterBy)) {
	    condition = " AND f.Bol IS NOT NULL and  f.over_paid_status='1'";
	    fetchBL = true;
	} else if ("Closed".equals(filterBy)) {
	    condition = " AND (f.bl_closed IS NOT NULL AND f.bl_closed != '') AND (f.bl_closed = 'Y')";
	    fetchBL = true;
	} else if ("Audited".equals(filterBy)) {
	    condition = " AND (f.bl_audited IS NOT NULL AND f.bl_audited != '') AND (f.bl_audited = 'Y')";
	    fetchBL = true;
	} else if ("Voided".equals(filterBy)) {
	    blVoidFlag = true;
	    fetchBL = true;
	}
	StringBuilder conditionBuffer = new StringBuilder("");
	StringBuilder bkgBuffer = new StringBuilder("");
	StringBuilder blBuffer = new StringBuilder("");
	conditionBuffer.append(condition);
	bkgBuffer.append(condition);
	blBuffer.append(condition);
	Set<Map.Entry> entrySet = searchMap.entrySet();
	String queryToAppend = appendString(entrySet);
	if (queryToAppend != null) {
	    conditionBuffer.append(queryToAppend);
	    bkgBuffer.append(queryToAppend);
	    blBuffer.append(queryToAppend);
	}
	if (null != searchMap.get(FileNumberForQuotaionBLBooking.QUOTE_DATE)) {
	    if (QuotationConstants.SEARCHBYFCL.equals(filterBy)) { //-- search based on BOL created date
		conditionBuffer.append(" and f.Bol_date between ").append(searchMap.get(FileNumberForQuotaionBLBooking.QUOTE_DATE));
		bkgBuffer.append(" and f.Bol_date between ").append(searchMap.get(FileNumberForQuotaionBLBooking.QUOTE_DATE));
		blBuffer.append(" and f.Bol_date between ").append(searchMap.get(FileNumberForQuotaionBLBooking.QUOTE_DATE));
	    } else if (QuotationConstants.SEARCHBYBOOKING.equals(filterBy)) { //-- search based on Booking created date
		conditionBuffer.append(" and b.BookingDate between ").append(searchMap.get(FileNumberForQuotaionBLBooking.QUOTE_DATE));
		bkgBuffer.append(" and b.BookingDate between ").append(searchMap.get(FileNumberForQuotaionBLBooking.QUOTE_DATE));
		blBuffer.append(" and b.BookingDate between ").append(searchMap.get(FileNumberForQuotaionBLBooking.QUOTE_DATE));
	    } else if (QuotationConstants.SEARCHBYQUOTE.equals(filterBy)) { //-- search based on Quote created date
		conditionBuffer.append(" and q.quote_date between ").append(searchMap.get(FileNumberForQuotaionBLBooking.QUOTE_DATE));
		bkgBuffer.append(" and q.quote_date between ").append(searchMap.get(FileNumberForQuotaionBLBooking.QUOTE_DATE));
		blBuffer.append(" and q.quote_date between ").append(searchMap.get(FileNumberForQuotaionBLBooking.QUOTE_DATE));
	    } else {
		conditionBuffer.append(" AND q.quote_date BETWEEN  ").append(searchMap.get(FileNumberForQuotaionBLBooking.QUOTE_DATE));
		bkgBuffer.append(" AND b.BookingDate BETWEEN  ").append(searchMap.get(FileNumberForQuotaionBLBooking.QUOTE_DATE));
		blBuffer.append(" AND f.bol_date BETWEEN  ").append(searchMap.get(FileNumberForQuotaionBLBooking.QUOTE_DATE));
	    }
	} else {
	    if (null != searchMap.get(FileNumberForQuotaionBLBooking.FILE_NO)) {
		conditionBuffer.append(" and q.file_no like '").append(searchMap.get(FileNumberForQuotaionBLBooking.FILE_NO)).append("%' ");
		bkgBuffer.append(" and b.file_no like '").append(searchMap.get(FileNumberForQuotaionBLBooking.FILE_NO)).append("%' ");
		blBuffer.append(" and f.file_no like '").append(searchMap.get(FileNumberForQuotaionBLBooking.FILE_NO)).append("%' ");
	    }
	    if (null != searchMap.get(FileNumberForQuotaionBLBooking.MASTERBL)) {
		conditionBuffer.append(" and f.new_master_bl like '%").append(searchMap.get(FileNumberForQuotaionBLBooking.MASTERBL)).append("%' ");
		bkgBuffer.append(" and f.new_master_bl like '%").append(searchMap.get(FileNumberForQuotaionBLBooking.MASTERBL)).append("%' ");
		blBuffer.append(" and f.new_master_bl like '%").append(searchMap.get(FileNumberForQuotaionBLBooking.MASTERBL)).append("%' ");
	    }
	    if (null != searchMap.get(FileNumberForQuotaionBLBooking.SSL_BOOKING)) {
		conditionBuffer.append(" and b.SSBookingNo like '%").append(searchMap.get(FileNumberForQuotaionBLBooking.SSL_BOOKING)).append("%' ");
		bkgBuffer.append(" and b.SSBookingNo like '%").append(searchMap.get(FileNumberForQuotaionBLBooking.SSL_BOOKING)).append("%' ");
		blBuffer.append(" and f.BookingNo like '%").append(searchMap.get(FileNumberForQuotaionBLBooking.SSL_BOOKING)).append("%' ");
	    }
	    if (null != searchMap.get("trailer_no")) {
		conditionBuffer.append(" and (SELECT CAST(GROUP_CONCAT(fc.trailer_no SEPARATOR ',') AS CHAR CHARSET utf8) AS trailer_no FROM fcl_bl_container_dtls fc WHERE fc.BolId = f.Bol) like '%").append(searchMap.get("trailer_no")).append("%' ");
		bkgBuffer.append(" and (SELECT CAST(GROUP_CONCAT(fc.trailer_no SEPARATOR ',') AS CHAR CHARSET utf8) AS trailer_no FROM fcl_bl_container_dtls fc WHERE fc.BolId = f.Bol) like '%").append(searchMap.get("trailer_no")).append("%' ");
		blBuffer.append(" and (SELECT CAST(GROUP_CONCAT(fc.trailer_no SEPARATOR ',') AS CHAR CHARSET utf8) AS trailer_no FROM fcl_bl_container_dtls fc WHERE fc.BolId = f.Bol) like '%").append(searchMap.get("trailer_no")).append("%' ");
	    }
	    if (null != searchMap.get("t.inbondNumber")) {
		conditionBuffer.append(" and (SELECT CAST(GROUP_CONCAT(inb.inbond_number SEPARATOR ',') AS CHAR CHARSET utf8) AS inbondNumber FROM fcl_inbond_details inb WHERE inb.Bol_Id = f.Bol) like '%").append(searchMap.get("t.inbondNumber")).append("%' ");
		bkgBuffer.append(" and (SELECT CAST(GROUP_CONCAT(inb.inbond_number SEPARATOR ',') AS CHAR CHARSET utf8) AS inbondNumber FROM fcl_inbond_details inb WHERE inb.Bol_Id = f.Bol) like '%").append(searchMap.get("t.inbondNumber")).append("%' ");
		blBuffer.append(" and (SELECT CAST(GROUP_CONCAT(inb.inbond_number SEPARATOR ',') AS CHAR CHARSET utf8) AS inbondNumber FROM fcl_inbond_details inb WHERE inb.Bol_Id = f.Bol) like '%").append(searchMap.get("t.inbondNumber")).append("%' ");
	    }
	}
	boolean isDestination = false;
	for (Entry entry : entrySet) {
	    if (FileNumberForQuotaionBLBooking.ORIGIN_REGION.equals(entry.getKey())) {
		String value = getSubQueryForRegion("q.Door_Origin",
			entry.getValue().toString(), true);
		String bkg = getSubQueryForRegion("b.door_origin",
			entry.getValue().toString(), true);
		String bl = getSubQueryForRegion("f.door_of_origin",
			entry.getValue().toString(), true);
		if (value != null) {
		    conditionBuffer.append(" AND ");
		    bkgBuffer.append(" AND ");
		    blBuffer.append(" AND ");
		    conditionBuffer.append(value);
		    bkgBuffer.append(bkg);
		    blBuffer.append(bl);
		}
	    } else if (FileNumberForQuotaionBLBooking.DESTINATION_REGION.equals(entry.getKey())) {
		String value = getSubQueryForRegion("q.destination_port",
			entry.getValue().toString(), false);
		String bkg = getSubQueryForRegion("b.destination",
			entry.getValue().toString(), false);
		String bl = getSubQueryForRegion("f.portofDischarge",
			entry.getValue().toString(), false);
		if (value != null) {
		    conditionBuffer.append(" AND ");
		    bkgBuffer.append(" AND ");
		    blBuffer.append(" AND ");
		    conditionBuffer.append(value);
		    bkgBuffer.append(bkg);
		    blBuffer.append(bl);
		}

	    } else if (FileNumberForQuotaionBLBooking.ORIGIN_TERMINAL.equals(entry.getKey())) {
		conditionBuffer.append(" AND q.origin_terminal like '%").append(entry.getValue()).append("%' ");
		bkgBuffer.append(" AND b.OriginTerminal like '%").append(entry.getValue()).append("%' ");
		blBuffer.append(" AND f.terminal like '%").append(entry.getValue()).append("%' ");

	    } else if (FileNumberForQuotaionBLBooking.PLOR.equals(entry.getKey())) {
		conditionBuffer.append(" AND  q.PLOR like '%").append(entry.getValue()).append("%' ");
		bkgBuffer.append(" AND  b.PortofOrgin like '%").append(entry.getValue()).append("%' ");
		blBuffer.append(" AND  f.port_of_loading like '%").append(entry.getValue()).append("%' ");

	    } else if (FileNumberForQuotaionBLBooking.PLOD.equals(entry.getKey())) {
		conditionBuffer.append(" AND  q.finaldestination like '%").append(entry.getValue()).append("%' ");
		bkgBuffer.append(" AND  b.portofDischarge like '%").append(entry.getValue()).append("%' ");
		blBuffer.append(" AND  f.PORT like '%").append(entry.getValue()).append("%' ");

	    } else if (FileNumberForQuotaionBLBooking.DESTINATION.equals(entry.getKey())) {
		isDestination = true;
		conditionBuffer.append(" AND q.destination_port like '%").append(entry.getValue()).append("%' ");
		bkgBuffer.append(" AND b.Destination like '%").append(entry.getValue()).append("%' ");
		blBuffer.append(" AND f.portofDischarge like '%").append(entry.getValue()).append("%' ");

	    } else if (FileNumberForQuotaionBLBooking.ISSUE_TERMINAL.equals(entry.getKey())) {
		conditionBuffer.append(" AND  q.issuing_terminal like '%").append(entry.getValue()).append("%' ");
		bkgBuffer.append(" AND  b.issuing_terminal like '%").append(entry.getValue()).append("%' ");
		blBuffer.append(" AND  f.billing_terminal like '%").append(entry.getValue()).append("%' ");

	    } else if (FileNumberForQuotaionBLBooking.CARRIER.equals(entry.getKey())) {
		conditionBuffer.append(" AND  q.Sslname like '%").append(entry.getValue()).append("%' ");
		bkgBuffer.append(" AND  b.Sslname like '%").append(entry.getValue()).append("%' ");
		blBuffer.append(" AND  q.Sslname like '%").append(entry.getValue()).append("%' ");

	    } else if (FileNumberForQuotaionBLBooking.QUOTE_BY.equals(entry.getKey())) {
		conditionBuffer.append(" AND  q.quote_by like '%").append(entry.getValue()).append("%' ");
		bkgBuffer.append(" AND  q.quote_by like '%").append(entry.getValue()).append("%' ");
		blBuffer.append(" AND  f.bl_by like '%").append(entry.getValue()).append("%' ");

	    } else if (FileNumberForQuotaionBLBooking.BOOKED_BY.equals(entry.getKey())) {
		conditionBuffer.append(" AND  b.booked_by like '%").append(entry.getValue()).append("%' ");
		bkgBuffer.append(" AND  b.booked_by like '%").append(entry.getValue()).append("%' ");
		blBuffer.append(" AND  b.booked_by like '%").append(entry.getValue()).append("%' ");

	    } else if (FileNumberForQuotaionBLBooking.SUBHOUSE.equals(entry.getKey())) {
		conditionBuffer.append(" AND  f.Import_Orgin_BLNo like '").append(entry.getValue()).append("%' ");
		bkgBuffer.append(" AND  f.Import_Orgin_BLNo like '").append(entry.getValue()).append("%' ");
		blBuffer.append(" AND  f.Import_Orgin_BLNo like '").append(entry.getValue()).append("%' ");

	    } else if (FileNumberForQuotaionBLBooking.FCLAMS.equals(entry.getKey())) {
		conditionBuffer.append(" AND  Import_AMS_House_Bl like '").append(entry.getValue()).append("%' ");
		bkgBuffer.append(" AND  Import_AMS_House_Bl like '").append(entry.getValue()).append("%' ");
		blBuffer.append(" AND  Import_AMS_House_Bl like '").append(entry.getValue()).append("%' ");

	    } else if (entry.getKey().equals(FileNumberForQuotaionBLBooking.Bl_VOID)) {
		blVoidFlag = true;
	    }
	}
	if (!isDestination && CommonUtils.isNotEmpty(agentQuery)) {
	    String bookingAgent = agentQuery.replace("q.destination_port", "b.Destination");
	    String blAgent = agentQuery.replace("q.destination_port", "f.portofDischarge");
	    conditionBuffer.append(" AND ").append(agentQuery);
	    bkgBuffer.append(" AND ").append(bookingAgent);
	    blBuffer.append(" AND ").append(blAgent);
	}
	if (!blVoidFlag) {
	    conditionBuffer.append(" AND f.void IS  NULL");
	    bkgBuffer.append(" AND f.void IS  NULL ");
	    blBuffer.append(" AND f.void IS  NULL ");
	} else {
	    conditionBuffer.append(" AND f.void IS not NULL");
	    bkgBuffer.append(" AND f.void IS not NULL ");
	    blBuffer.append(" AND f.void IS not NULL ");
	}
	queryBuffer.append("SELECT  quote_id ,BookingId,Bol AS Bol,t.file_no,t.origin_terminal,t.finaldestination,t.clientname,t.Carrier,");
	queryBuffer.append(" t.Quote_Date,t.destination_port,t.PLOR,t.clienttype,quote_by,t.ramp_city,t.issuing_terminal,");
	queryBuffer.append(" t.clientname AS clientname1,booked_by AS booked_by ,rates_non_rates,Quote_By AS USER,Sslname ,Ssline ,Door_Origin ,");
	queryBuffer.append(" Username ,SSBookingNo ,Documents_Received ,BookingComplete ,Doc_Cut_Off ,PortCutOff ,Etd ,BolId ,");
	queryBuffer.append(" hazmat AS Hazmat_Quote_Count ,");
	queryBuffer.append(" '0' AS Hazmat_Booking_Count,void, ");
	queryBuffer.append(" '0' AS Hazmat_FclBl_Count,inttra, ");
	queryBuffer.append(" ready_to_edi,bl_audited, received_master, bl_closed, ready_to_post,t.container_size,");
	queryBuffer.append(" t.importFlag,importRelease,t.ams,t.subHouse,t.paymentRelease,t.doorDestination,t.inbondNumber,t.quote_date as boldate,t.manifestedDate,t.aes, t.correction_count,t.break_bulk,t.tracking_status,t.aes_status,t.304_success,t.304_failure,t.booking_aes_status");
	queryBuffer.append(",(SELECT COUNT(*) FROM sed_filings WHERE SHPDR = t.file_no and status = 'S') AS aes_count,(SELECT STATUS FROM document_store_log WHERE document_id = t.file_no ORDER BY id DESC LIMIT 1) AS document_status,t.trailer_no,t.997_success,t.eta FROM ( ");
	queryBuffer.append(" SELECT BookingId, Bol,Quote_ID,null as trailer_no_id,IF(ISNULL(f.file_no),q.file_no,f.file_no) AS file_no, ");
	queryBuffer.append(" origin_terminal, finaldestination, clientname,  Carrier, Quote_Date, destination_port, PLOR, clienttype, ");
	queryBuffer.append(" quote_by, q.ramp_city, q.issuing_terminal, clientname AS clientname1, IF(ISNULL(b.rates_non_rates), ");
	queryBuffer.append(" q.rates_non_rates,b.rates_non_rates) AS rates_non_rates, b.booked_by,");
	queryBuffer.append(" q.Sslname,q.Ssline,q.Door_Origin,b.username,b.SSBookingNo, b.Documents_Received ,b.BookingComplete ,");
	queryBuffer.append(" b.Doc_Cut_Off ,b.PortCutOff, b.shipper,b.forward,b.Consignee,");
	queryBuffer.append(" f.house_consignee_name,f.forwarding_agent_name,f.house_shipper_name,b.Etd,f.BolId, ");
        queryBuffer.append( "(SELECT CAST(GROUP_CONCAT(fc.trailer_no SEPARATOR ',') AS CHAR CHARSET utf8) AS trailer_no FROM fcl_bl_container_dtls fc WHERE fc.BolId = f.Bol) as trailer_no,");
	queryBuffer.append(" f.void,q.hazmat,f.inttra, f.ready_to_edi, f.bl_audited, f.received_master, f.bl_closed, f.ready_to_post,");
        queryBuffer.append( " f.bol AS correctionId,q.file_type as importFlag,f.new_master_bl,f.import_release as importRelease,f.Import_AMS_House_Bl as ams ,");
        queryBuffer.append( "f.Import_Orgin_BLNo as subHouse,f.payment_release as paymentRelease,q.door_destination as doorDestination,null as inbondNumber,null as aesItn,f.manifested_date as manifestedDate");
	queryBuffer.append(",f.aes as aes,f.break_bulk,(SELECT logfile_edi.tracking_status FROM logfile_edi WHERE logfile_edi.message_type = '315' AND logfile_edi.file_no = b.file_no AND logfile_edi.status = 'success' ORDER BY logfile_edi.id DESC LIMIT 1) AS tracking_status,");
        queryBuffer.append( "(SELECT aes_history.STATUS FROM aes_history WHERE aes_history.file_no = f.file_no ORDER BY aes_history.id DESC LIMIT 1) AS aes_status,(SELECT logfile_edi.status FROM logfile_edi WHERE logfile_edi.file_no = f.file_no AND logfile_edi.message_type = '304' AND logfile_edi.status = 'success' LIMIT 1 ) AS 304_success,");
        queryBuffer.append( "(SELECT logfile_edi.status FROM logfile_edi WHERE logfile_edi.file_no = f.file_no AND logfile_edi.message_type = '304' AND logfile_edi.status = 'failure' LIMIT 1 ) AS 304_failure,(SELECT COUNT(0) FROM fclblcorrections c WHERE c.file_no = f.file_no AND (c.status <> 'Disable' OR ISNULL(c.status)) GROUP BY c.notice_no LIMIT 1 ) AS correction_count,");
        queryBuffer.append( "b.container_size,(SELECT aes_history.STATUS FROM aes_history WHERE aes_history.file_no = b.file_no ORDER BY aes_history.id DESC LIMIT 1) AS booking_aes_status,");
        queryBuffer.append( "(SELECT edi_997_ack.si_id FROM logfile_edi LEFT JOIN edi_997_ack ON edi_997_ack.si_id =  logfile_edi.id   WHERE  logfile_edi.file_no = f.file_no AND logfile_edi.message_type = '304' AND logfile_edi.status = 'success' ORDER BY logfile_edi.id DESC LIMIT 1) AS 997_success,b.eta as eta");
	queryBuffer.append(" FROM quotation q ");
	queryBuffer.append(" LEFT JOIN booking_fcl b ON b.file_no = q.file_no ");
	queryBuffer.append(" LEFT JOIN fcl_bl f ON f.file_number = q.file_no WHERE q.file_type='I' and q.file_no is not null ");
	queryBuffer.append(conditionBuffer);
	queryBuffer.append(" ");
	queryBuffer.append(" UNION");
	queryBuffer.append(" ");
	queryBuffer.append("SELECT BookingId, Bol,null as Quote_ID,NULL AS trailer_no_id,b.file_no AS file_no,b.OriginTerminal AS origin_terminal, b.portofDischarge AS  finaldestination,");
	queryBuffer.append("null as clientname,  b.sslname AS  Carrier, b.BookingDate AS Quote_Date,b.Destination AS destination_port,b.PortofOrgin AS PLOR, null as clienttype, null as quote_by, null as ramp_city, ");
	queryBuffer.append(" b.issuing_terminal AS issuing_terminal, null AS clientname1, b.rates_non_rates");
	queryBuffer.append(" AS rates_non_rates, b.booked_by,null as Sslname,null as Ssline,b.door_origin,b.username,b.SSBookingNo, b.Documents_Received ");
	queryBuffer.append(" ,b.BookingComplete ,b.Doc_Cut_Off ,b.PortCutOff, b.shipper,b.forward,b.Consignee, ");
	queryBuffer.append(" f.house_consignee_name,f.forwarding_agent_name,f.house_shipper_name,b.Etd,f.BolId, (SELECT CAST(GROUP_CONCAT(fc.trailer_no SEPARATOR ',') AS CHAR CHARSET utf8) AS trailer_no FROM fcl_bl_container_dtls fc WHERE fc.BolId = f.Bol) as trailer_no,f.void,null as hazmat,");
	queryBuffer.append(" f.inttra, f.ready_to_edi, f.bl_audited, f.received_master, f.bl_closed, f.ready_to_post, f.bol AS correctionId");
        queryBuffer.append( " ,b.importFlag,f.new_master_bl,f.import_release as importRelease,f.Import_AMS_House_Bl as ams ,f.Import_Orgin_BLNo as subHouse,");
        queryBuffer.append("f.payment_release as paymentRelease,b.door_destination as doorDestination,null as inbondNumber,null as aesItn,f.manifested_date as manifestedDate");
	queryBuffer.append(",f.aes as aes,f.break_bulk,(SELECT logfile_edi.tracking_status FROM logfile_edi WHERE logfile_edi.message_type = '315' AND logfile_edi.file_no = b.file_no AND logfile_edi.status = 'success' ORDER BY logfile_edi.id DESC LIMIT 1) AS tracking_status,");
        queryBuffer.append( "(SELECT aes_history.STATUS FROM aes_history WHERE aes_history.file_no = f.file_no ORDER BY aes_history.id DESC LIMIT 1) AS aes_status,(SELECT logfile_edi.status FROM logfile_edi WHERE logfile_edi.file_no = f.file_no AND logfile_edi.message_type = '304' AND logfile_edi.status = 'success' LIMIT 1 ) AS 304_success,");
        queryBuffer.append( "(SELECT logfile_edi.status FROM logfile_edi WHERE logfile_edi.file_no = f.file_no AND logfile_edi.message_type = '304' AND logfile_edi.status = 'failure' LIMIT 1 ) AS 304_failure,(SELECT COUNT(0) FROM fclblcorrections c WHERE c.file_no = f.file_no AND (c.status <> 'Disable' OR ISNULL(c.status)) GROUP BY c.notice_no LIMIT 1 ) AS correction_count,");
        queryBuffer.append( "b.container_size,(SELECT aes_history.STATUS FROM aes_history WHERE aes_history.file_no = b.file_no ORDER BY aes_history.id DESC LIMIT 1) AS booking_aes_status,");
        queryBuffer.append( "(SELECT edi_997_ack.si_id FROM logfile_edi LEFT JOIN edi_997_ack ON edi_997_ack.si_id =  logfile_edi.id   WHERE  logfile_edi.file_no = f.file_no AND logfile_edi.message_type = '304' AND logfile_edi.status = 'success' ORDER BY logfile_edi.id DESC LIMIT 1) AS 997_success,b.eta");
	queryBuffer.append(" FROM quotation q right join booking_fcl b ON b.file_no=q.file_no");
	queryBuffer.append(" LEFT JOIN fcl_bl f ON f.file_number = b.file_no WHERE b.importflag='I' AND q.quote_id IS NULL AND b.file_no IS NOT NULL ");
	queryBuffer.append(bkgBuffer);
	queryBuffer.append(" ");
	queryBuffer.append(" UNION");
	queryBuffer.append(" ");
	queryBuffer.append(" SELECT null as BookingId, Bol,null as Quote_ID,null as trailer_no_id,f.file_no AS file_no,f.terminal AS origin_terminal,f.PORT AS finaldestination, ");
	queryBuffer.append(" null as clientname,  null as Carrier, bol_date AS Quote_Date, f.portofDischarge AS destination_port, f.port_of_loading AS  PLOR, null as clienttype, f.bl_by AS  quote_by, null as ramp_city,");
	queryBuffer.append(" billing_terminal as issuing_terminal, null AS clientname1, null as rates_non_rates, ");
	queryBuffer.append(" null as booked_by, ");
	queryBuffer.append(" ssline_name as Sslname,ssline_no as Ssline,f.door_of_origin as Door_Origin,null as username,null as SSBookingNo, null as Documents_Received ,null as BookingComplete ,null as Doc_Cut_Off ");
	queryBuffer.append(" ,null as PortCutOff, null as shipper, null as forward,null as Consignee, ");
	queryBuffer.append(" f.house_consignee_name,f.forwarding_agent_name,f.house_shipper_name,null as Etd,f.BolId, (SELECT CAST(GROUP_CONCAT(fc.trailer_no SEPARATOR ',') AS CHAR CHARSET utf8) AS trailer_no FROM fcl_bl_container_dtls fc WHERE fc.BolId = f.Bol) as trailer_no,f.void, null as hazmat,");
	queryBuffer.append(" f.inttra, f.ready_to_edi, f.bl_audited, f.received_master, f.bl_closed, f.ready_to_post,");
        queryBuffer.append( " f.bol AS correctionId,f.importFlag,f.new_master_bl,f.import_release as importRelease,f.Import_AMS_House_Bl as ams ,");
        queryBuffer.append( "f.Import_Orgin_BLNo as subHouse,f.payment_release as paymentRelease,f.door_of_destination as doorDestination,null as inbondNumber,null as aesItn,f.manifested_date as manifestedDate");
	queryBuffer.append(",f.aes as aes,f.break_bulk,(SELECT logfile_edi.tracking_status FROM logfile_edi WHERE logfile_edi.message_type = '315' AND logfile_edi.file_no = b.file_no AND logfile_edi.status = 'success' ORDER BY logfile_edi.id DESC LIMIT 1) AS tracking_status,");
        queryBuffer.append( "(SELECT aes_history.STATUS FROM aes_history WHERE aes_history.file_no = f.file_no ORDER BY aes_history.id DESC LIMIT 1) AS aes_status,(SELECT logfile_edi.status FROM logfile_edi WHERE logfile_edi.file_no = f.file_no AND logfile_edi.message_type = '304' AND logfile_edi.status = 'success' LIMIT 1 ) AS 304_success,");
        queryBuffer.append( "(SELECT logfile_edi.status FROM logfile_edi WHERE logfile_edi.file_no = f.file_no AND logfile_edi.message_type = '304' AND logfile_edi.status = 'failure' LIMIT 1 ) AS 304_failure,(SELECT COUNT(0) FROM fclblcorrections c WHERE c.file_no = f.file_no AND (c.status <> 'Disable' OR ISNULL(c.status)) GROUP BY c.notice_no LIMIT 1 ) AS correction_count,");
        queryBuffer.append( "b.container_size,(SELECT aes_history.STATUS FROM aes_history WHERE aes_history.file_no = b.file_no ORDER BY aes_history.id DESC LIMIT 1) AS booking_aes_status,");
        queryBuffer.append( "(SELECT edi_997_ack.si_id FROM logfile_edi LEFT JOIN edi_997_ack ON edi_997_ack.si_id =  logfile_edi.id   WHERE  logfile_edi.file_no = f.file_no AND logfile_edi.message_type = '304' AND logfile_edi.status = 'success' ORDER BY logfile_edi.id DESC LIMIT 1) AS 997_success,f.eta");
	queryBuffer.append(" FROM quotation q RIGHT JOIN booking_fcl b ON b.file_no=q.file_no and ");
        queryBuffer.append( "b.importFlag='I' RIGHT JOIN fcl_bl f ON f.file_number = q.file_no and f.importFlag='I'");
	queryBuffer.append(" WHERE f.importflag='I' AND q.quote_id IS NULL AND b.bookingid IS NULL AND f.file_no IS NOT NULL ");
	queryBuffer.append(blBuffer);
	queryBuffer.append(" ) t ");

	queryBuffer.append(" GROUP BY t.file_no");
	if (sortByDate.equalsIgnoreCase("S")) {
	    queryBuffer.append(" ORDER BY t.file_no DESC");
	} else if (sortByDate.equalsIgnoreCase("C")) {
	    queryBuffer.append(" ORDER BY (CASE WHEN PortCutOff  IS NULL THEN 1 ELSE 0 END) DESC,PortCutOff ASC");
	} else if (sortByDate.equalsIgnoreCase("D")) {
	    queryBuffer.append(" ORDER BY (CASE WHEN Doc_Cut_Off  IS NULL THEN 1 ELSE 0 END) DESC,Doc_Cut_Off ASC");
	} else if (sortByDate.equalsIgnoreCase("E")) {
	    queryBuffer.append(" ORDER BY (CASE WHEN Etd  IS NULL THEN 1 ELSE 0 END) DESC,Etd ASC");
	}
	/**
	 * **
	 */
	String queryStringcode = queryBuffer.toString();

	return queryStringcode;
    }

    public String getQueryString(Map searchMap, String filterBy,
	    String sortByDate, String agentQuery) throws Exception {
	String columnName = "t.Quote_Date";
	String condition = " ";
	StringBuilder queryBuffer = new StringBuilder();
	StringBuilder filter = new StringBuilder();
	boolean isfilterBooking = false;
	boolean isFilerBL = false;
	boolean inbound = false;
	boolean unit = false;
	boolean aesItn = false;

	queryBuffer.append("SELECT  quote_id ,BookingId,Bol AS Bol,t.file_no,t.origin_terminal,t.finaldestination,t.clientname,t.Carrier,t.Quote_Date,");
        queryBuffer.append("t.destination_port,t.PLOR,t.clienttype,quote_by,t.ramp_city,t.issuing_terminal,t.clientname AS clientname1,booked_by AS booked_by ,");
        queryBuffer.append( "rates_non_rates,Quote_By AS USER,Sslname ,Ssline ,Door_Origin ,Username ,SSBookingNo ,Documents_Received ,BookingComplete ,Doc_Cut_Off ,");
        queryBuffer.append( "PortCutOff ,t.etd ,BolId ,hazmat AS Hazmat_Quote_Count ,");
        queryBuffer.append( "'0' AS Hazmat_Booking_Count,");
        queryBuffer.append( "void, ");
        queryBuffer.append( "'0' AS Hazmat_FclBl_Count,");
        queryBuffer.append( "inttra, ready_to_edi,bl_audited, received_master, bl_closed, ready_to_post, t.container_size ,t.importFlag,importRelease");
        queryBuffer.append( ",t.ams ,t.subHouse,t.paymentRelease,t.doorDestination,t.inbondNumber,t.bolDate,t.manifestedDate,t.aes,");
	queryBuffer.append("(SELECT COUNT(*) FROM fclblcorrections c WHERE ((c.file_no = t.file_no) AND ((c.status <> 'Disable') OR ISNULL(c.status))) GROUP BY c.notice_no LIMIT 1) AS correction_count,");
	queryBuffer.append("t.break_bulk");
	queryBuffer.append(",(SELECT le.tracking_status AS tracking_status FROM logfile_edi le WHERE ((le.message_type = '315') AND (le.file_no = t.file_no) AND (le.status = 'success')) ORDER BY le.id DESC LIMIT 1) AS tracking_status,");
	queryBuffer.append("(SELECT ah.STATUS AS STATUS FROM aes_history ah WHERE (ah.file_no = t.file_no) ORDER BY ah.id DESC LIMIT 1) AS aes_status,");
	queryBuffer.append("(SELECT le.status AS STATUS FROM logfile_edi le WHERE ((le.file_no = t.file_no) AND (le.message_type = '304') AND (le.status = 'success')) LIMIT 1) AS 304_success,");
	queryBuffer.append("(SELECT le.status AS STATUS FROM logfile_edi le WHERE ((le.file_no = t.file_no) AND (le.message_type = '304') AND (le.status = 'failure')) LIMIT 1) AS 304_failure,");
	queryBuffer.append("(SELECT ah.itn AS STATUS FROM aes_history ah WHERE (ah.file_no = t.file_no) ORDER BY ah.id DESC LIMIT 1) AS booking_aes_status");
	queryBuffer.append(",(SELECT COUNT(*) FROM sed_filings WHERE SHPDR = t.file_no and status = 'S') AS aes_count,(SELECT STATUS FROM document_store_log WHERE document_id = t.file_no ORDER BY id DESC LIMIT 1) AS document_status,t.bolfae,");
        queryBuffer.append( "(SELECT edi_997_ack.si_id FROM logfile_edi LEFT JOIN edi_997_ack ON edi_997_ack.si_id =  logfile_edi.id   WHERE  logfile_edi.file_no = t.file_no AND logfile_edi.message_type = '304' AND logfile_edi.status = 'success' ORDER BY logfile_edi.id DESC LIMIT 1) AS 997_success");
	queryBuffer.append(" FROM ( ");
	queryBuffer.append("select BookingId, Bol,Quote_ID,null as trailer_no_id,IF(ISNULL(f.file_no),q.file_no,f.file_no) AS file_no, origin_terminal, ");
        queryBuffer.append( "finaldestination, clientname,	 Carrier, Quote_Date, destination_port, PLOR, clienttype, quote_by, q.ramp_city, q.issuing_terminal, ");
        queryBuffer.append( "clientname AS clientname1,");
        queryBuffer.append( " IF(ISNULL(b.rates_non_rates), q.rates_non_rates,b.rates_non_rates) AS rates_non_rates, b.booked_by, ");
	queryBuffer.append("q.Sslname,q.Ssline,q.Door_Origin,b.username,b.SSBookingNo, ");
	queryBuffer.append("b.Documents_Received ,b.BookingComplete ,b.Doc_Cut_Off ,b.PortCutOff, ");
	queryBuffer.append("b.shipper,forward,Consignee, ");
	queryBuffer.append("f.house_consignee_name,f.forwarding_agent_name,f.house_shipper_name,IF(ISNULL(f.sail_date),b.Etd,f.sail_date) AS etd ,f.BolId, '',f.void,");
        queryBuffer.append( "q.hazmat, f.inttra, f.ready_to_edi, f.bl_audited, f.received_master, f.bl_closed, f.ready_to_post, f.bol as correctionId,");
        queryBuffer.append( "q.file_type as importFlag,f.new_master_bl,f.import_release as importRelease,f.Import_AMS_House_Bl as ams ,f.Import_Orgin_BLNo as subHouse,");
        queryBuffer.append( "f.payment_release as paymentRelease,q.door_destination as doorDestination,null as inbondNumber,'' as aesItn,f.Bol_date as bolDate,f.manifested_date as manifestedDate,f.aes as aes,f.break_bulk");
	queryBuffer.append(",b.container_size,(SELECT DISTINCT(fbc.bolid) FROM fcl_bl_costcodes fbc WHERE fbc.bolid = f.bol AND fbc.cost_code = 'FAECOMM' AND delete_flag = 'no' ) AS bolfae ");
	queryBuffer.append(" FROM quotation q ");
	boolean found = false;
	filter.append("where (q.file_type!='I' or q.file_type is null) ");
	if (null != searchMap.get(FileNumberForQuotaionBLBooking.SAIL_DATE)) {
	    filter.append(" and f.sail_date between ").append(searchMap.get(FileNumberForQuotaionBLBooking.SAIL_DATE)).append(" ");
	} else if (null != searchMap.get(FileNumberForQuotaionBLBooking.QUOTE_DATE)) {
	    if (QuotationConstants.SEARCHBYFCL.equals(filterBy)) { //-- search based on BOL created date
		filter.append(" and f.Bol_date between ");
	    } else if (QuotationConstants.SEARCHBYBOOKING.equals(filterBy)) { //-- search based on Booking created date
		filter.append(" and b.BookingDate between ");
	    } else { //-- search based on Quote created date
		filter.append(" and q.quote_date between ");
	    }
	    filter.append(searchMap.get(FileNumberForQuotaionBLBooking.QUOTE_DATE)).append(" ");
	} else {
	    if (null != searchMap.get(FileNumberForQuotaionBLBooking.FILE_NO)) {
		filter.append(" and q.file_no like '").append(searchMap.get(FileNumberForQuotaionBLBooking.FILE_NO)).append("%' ");
		found = true;
	    }
	    if (null != searchMap.get(FileNumberForQuotaionBLBooking.MASTERBL)) {
		isFilerBL = true;
		filter.append(" and f.new_master_bl like '%").append(searchMap.get(FileNumberForQuotaionBLBooking.MASTERBL)).append("%' ");
	    }
	    if (null != searchMap.get(FileNumberForQuotaionBLBooking.SSL_BOOKING)) {
		isfilterBooking = true;
		filter.append(" and b.SSBookingNo like '%").append(searchMap.get(FileNumberForQuotaionBLBooking.SSL_BOOKING)).append("%' ");
	    }
	    if (null != searchMap.get("trailer_no")) {
		isFilerBL = true;
		unit = true;
		filter.append(" and tr.trailer_no like '%").append(searchMap.get("trailer_no")).append("%' ");
	    }
	    if (null != searchMap.get("t.inbondNumber")) {
		isFilerBL = true;
		inbound = true;
		filter.append(" and inb.inbond_number like '%").append(searchMap.get("t.inbondNumber")).append("%' ");
	    }
	    if (null != searchMap.get(FileNumberForQuotaionBLBooking.AESDETAILS)) {
		isFilerBL = true;
		aesItn = true;
		filter.append(" and aesItn.aes_details like '%").append(searchMap.get(FileNumberForQuotaionBLBooking.AESDETAILS)).append("%' ");
	    }
	}
	if (sortByDate.equalsIgnoreCase("C")) {
	    isfilterBooking = true;
	    condition = " AND b.PortCutOff >= CURRENT_DATE()";
	    // This is when sort by is "container cutt off" and manditorily we
	    // need to filter out quotation
	    // and we need to display container cut off date which is less than
	    // current date
	} else if (sortByDate.equalsIgnoreCase("D")) {
	    isfilterBooking = true;
	    condition = "  AND b.Doc_Cut_Off >= CURRENT_DATE()";
	    // This is when sort by is "container cutt off" and manditorily we
	    // need to filter out quotation
	    // and we need to display container cut off date which is less than
	    // current date
	} else if (filterBy.equalsIgnoreCase("DNR")) {
	    isfilterBooking = true;
	    condition = " AND (b.Documents_Received='N' or b.Doc_Cut_Off is null) ";
	    // This is when sort by is "DOC cutt off Past" and manditorily we
	    // need to filter out quotation
	    // and we need to display container cut off date which is less thanj
	    // current date
	} else if (sortByDate.equalsIgnoreCase("E")) {
	    isfilterBooking = true;
	    condition = " AND IF(ISNULL(f.sail_date),b.Etd,f.sail_date) >= CURRENT_DATE()";
	    // This is when sort by is "container cutt off" and manditorily we
	    // need to filter out quotation
	    // and we need to display container cut off date which is less than
	    // current date
	} else if (QuotationConstants.SEARCHBYFCL.equals(filterBy)) {
	    isFilerBL = true;
	} else if (QuotationConstants.SEARCHBYBOOKING.equals(filterBy)) {
	    isfilterBooking = true;
	    isFilerBL = false;
	    condition = " AND b.BookingId is not null and f.bol is null ";
	} else if ("UMF".equals(filterBy)) {
	    isFilerBL = true;
	    condition = " AND (f.manifested_date IS NULL OR f.manifested_date = '') ";
	} else if ("MF".equals(filterBy)) {
	    isFilerBL = true;
	    condition = " AND (f.manifested_date IS NOT NULL AND f.manifested_date != '') AND (f.ready_to_post = 'M')";
	} else if (QuotationConstants.SEARCHBYQUOTE.equals(filterBy)) {
	    isfilterBooking = false;
	    isFilerBL = false;
	    condition = " AND b.BookingId is null and f.bol is null ";
	} else if ("SSL".equals(filterBy)) {
	    isFilerBL = true;
	    condition = " AND f.received_master ='No' ";
	} else if ("MNM".equals(filterBy)) {
	    isFilerBL = true;
	    condition = " AND (f.manifested_date IS NOT NULL AND f.manifested_date != '') AND (f.ready_to_post = 'M') AND f.received_master ='No' ";
	} else if ("faeNotApplied".equals(filterBy)) {
	    isFilerBL = true;
	}
	StringBuilder conditionBuffer = new StringBuilder("");
	//conditionBuffer.append(" WHERE (t.importFlag!='I' OR t.importFlag is null)");
	conditionBuffer.append(condition);
	Set<Map.Entry> entrySet = searchMap.entrySet();
	String queryToAppend = appendExportString(entrySet);
	if (queryToAppend != null) {
	    conditionBuffer.append(queryToAppend);
	}
	boolean blVoidFlag = false;
	boolean isDestination = false;
	for (Entry entry : entrySet) {
	    if (FileNumberForQuotaionBLBooking.ORIGIN_REGION.equals(entry.getKey())) {
		String value = getSubQueryForRegion("q.Door_Origin",
			entry.getValue().toString(), true);
		if (value != null) {
		    conditionBuffer.append(" AND ");
		    conditionBuffer.append(value);
		}

	    } else if (FileNumberForQuotaionBLBooking.DESTINATION_REGION.equals(entry.getKey())) {
		String value = getSubQueryForRegion("q.destination_port",
			entry.getValue().toString(), false);
		if (value != null) {
		    conditionBuffer.append(" AND ");
		    conditionBuffer.append(value);
		}

	    } else if (FileNumberForQuotaionBLBooking.ORIGIN_TERMINAL.equals(entry.getKey())) {
		conditionBuffer.append(" AND REPLACE(REPLACE(q.origin_terminal,'\"',''),\"\'\",\"\") like '%").append(entry.getValue().toString().replace("'", "").replace("\"", "")).append("%' ");

	    } else if (FileNumberForQuotaionBLBooking.PLOR.equals(entry.getKey())) {
		conditionBuffer.append(" AND  REPLACE(REPLACE(q.PLOR,'\"',''),\"\'\",\"\") like '%").append(entry.getValue().toString().replace("'", "").replace("\"", "")).append("%' ");

	    } else if (FileNumberForQuotaionBLBooking.PLOD.equals(entry.getKey())) {
		conditionBuffer.append(" AND  REPLACE(REPLACE(q.finaldestination,'\"',''),\"\'\",\"\") like '%").append(entry.getValue().toString().replace("'", "").replace("\"", "")).append("%' ");

	    } else if (FileNumberForQuotaionBLBooking.DESTINATION.equals(entry.getKey())) {
		isDestination = true;
		conditionBuffer.append(" AND REPLACE(REPLACE(q.destination_port,'\"',''),\"\'\",\"\") like '%").append(entry.getValue().toString().replace("'", "").replace("\"", "")).append("%' ");

	    } else if (FileNumberForQuotaionBLBooking.ISSUE_TERMINAL.equals(entry.getKey())) {
		conditionBuffer.append(" AND  q.issuing_terminal like '%").append(entry.getValue()).append("%' ");

	    } else if (FileNumberForQuotaionBLBooking.CARRIER.equals(entry.getKey())) {
		conditionBuffer.append(" AND  q.Sslname like '%").append(entry.getValue()).append("%' ");

	    } else if (FileNumberForQuotaionBLBooking.QUOTE_BY.equals(entry.getKey())) {
		conditionBuffer.append(" AND  q.quote_by like '").append(entry.getValue()).append("%' ");

	    } else if (FileNumberForQuotaionBLBooking.BOOKED_BY.equals(entry.getKey())) {
		isfilterBooking = true;
		conditionBuffer.append(" AND  b.booked_by like '%").append(entry.getValue()).append("%' ");

	    } else if (entry.getKey().equals(FileNumberForQuotaionBLBooking.Bl_VOID)) {
		blVoidFlag = true;
	    }
	}
	if (!isDestination && CommonUtils.isNotEmpty(agentQuery)) {
	    conditionBuffer.append(" AND ").append(agentQuery);
	}
	if (!blVoidFlag) {
	    conditionBuffer.append(" AND f.void IS  NULL ");
	}
	conditionBuffer.append(" AND q.file_no IS NOT NULL ");
	if (!isfilterBooking) {
	    queryBuffer.append("LEFT");
	}
	queryBuffer.append(" JOIN booking_fcl b ON b.file_no = q.file_no ");
	if (!isFilerBL) {
	    queryBuffer.append("LEFT");
	}
	queryBuffer.append(" JOIN fcl_bl f ON f.file_number = q.file_no ");
	if (unit) {
	    queryBuffer.append(" JOIN fcl_bl_container_dtls tr on tr.BolId = f.Bol ");
	}
	if (inbound) {
	    queryBuffer.append(" JOIN fcl_inbond_details inb on inb.bol_id = f.Bol ");
	}
	if (aesItn) {
	    queryBuffer.append(" JOIN fcl_aes_details aesItn on aesItn.file_no = f.file_no ");
	}
	queryBuffer.append(filter);
	queryBuffer.append(conditionBuffer);
	queryBuffer.append(") t ");
	if ("faeNotApplied".equals(filterBy)) {
	    queryBuffer.append(" WHERE t.bolfae IS NULL ");
	}
	queryBuffer.append(" GROUP BY t.file_no");
	if (sortByDate.equalsIgnoreCase("S")) {
	    queryBuffer.append(" ORDER BY t.file_no DESC");
	} else if (sortByDate.equalsIgnoreCase("C")) {
	    queryBuffer.append(" ORDER BY (CASE WHEN PortCutOff  IS NULL THEN 1 ELSE 0 END) DESC,PortCutOff ASC");
	} else if (sortByDate.equalsIgnoreCase("D")) {
	    queryBuffer.append(" ORDER BY (CASE WHEN Doc_Cut_Off  IS NULL THEN 1 ELSE 0 END) DESC,Doc_Cut_Off ASC");
	} else if (sortByDate.equalsIgnoreCase("E")) {
	    queryBuffer.append(" ORDER BY (CASE WHEN Etd  IS NULL THEN 1 ELSE 0 END) DESC,Etd ASC");
	}
	String queryStringcode = queryBuffer.toString();
	return queryStringcode;
    }

    private String getSubQueryForRegion(String column, String regionCode,
	    boolean falg) throws Exception {
	String bracket = "(,)";
	String query = "";
	String region = null;
	PortsBC portsBC = new PortsBC();
	boolean isFirstItem = true;
	List regionList = portsBC.getPortNameAndUnLocCode(Integer.parseInt(regionCode.toString()));
	for (Object obj : regionList) {

	    Object[] ports = (Object[]) obj;
	    String portName = (String) ports[0];
	    portName = (portName != null) ? portName.replace("'", "''")
		    : portName;
	    region = StringFormatter.formatString((String) ports[2],
		    (String) portName, (String) ports[1], (String) ports[3], falg);
	    if (isFirstItem) {
		query += "REPLACE(" + column + ",\"\'\",\"\") like '%" + region.trim().replace("'", "") + "%' ";
		isFirstItem = false;
	    } else {
		query += " OR REPLACE(" + column + ",\"\'\",\"\") like '%" + region.trim().replace("'", "") + "%' ";
	    }
	}
	if (!query.equals("")) {
	    query = bracket.replace(",", query);
	} else {
	    query += column + " like ' %' ";
	}
	return query;
    }

    public String getQueryStringForQotBokFcl(Map searchMap, String table,
	    String firstJoin, String secondJoin) {
	String condition = "";
	int i = 0;
	String queryStringcode = " select quotation.Quote_ID,booking_fcl.BookingId,fcl_bl.Bol from"
		+ "(quotation "
		+ firstJoin
		+ " join  booking_fcl on quotation.file_no=booking_fcl.file_no)"
		+ " "
		+ secondJoin
		+ " join fcl_bl on booking_fcl.file_no=fcl_bl.file_no  ";
	Set MapSet = searchMap.entrySet();
	for (Iterator iter = MapSet.iterator(); iter.hasNext();) {
	    Map.Entry entry = (Map.Entry) iter.next();
	    if (i > 0) {
		if (entry.getKey() != null && entry.getKey().equals("Bol_date")
			|| entry.getKey().equals("Quote_Date")
			|| entry.getKey().equals("BookingDate")) {
		    condition += " and date_format(" + table + "."
			    + entry.getKey() + ", '%Y-%m-%d') between "
			    + entry.getValue();
		} else if (entry.getKey() != null
			&& entry.getKey().equals("quote_by")) {
		    condition += " and quotation." + entry.getKey() + " like'"
			    + entry.getValue() + "%'";
		} else {
		    condition += " and " + table + "." + entry.getKey()
			    + " like'" + entry.getValue() + "%'";
		}

	    } else {
		if (entry.getKey() != null && entry.getKey().equals("Bol_date")
			|| entry.getKey().equals("Quote_Date")
			|| entry.getKey().equals("BookingDate")) {
		    condition += " where date_format(" + table + "."
			    + entry.getKey() + ", '%Y-%m-%d')between "
			    + entry.getValue();
		} else if (entry.getKey() != null
			&& entry.getKey().equals("quote_by")) {
		    condition += " where quotation." + entry.getKey()
			    + " like'" + entry.getValue() + "%'";
		} else {
		    condition += " where " + table + "." + entry.getKey()
			    + " like'" + entry.getValue() + "%'";
		}
	    }
	    i++;
	}

	queryStringcode = queryStringcode + condition;
	return queryStringcode;
    }

    public List getContainerListForBl(String container) throws Exception {
	List searchListByFileNumber = new ArrayList();
	FclBlDAO fclBlDAO = null;
	FileNumberForQuotaionBLBooking fileNumberForQuotaionBLBooking = null;
	fclBlDAO = new FclBlDAO();
	List getFclList = fclBlDAO.getFclListBycontainerNumber(container);
	for (Iterator iterator = getFclList.iterator(); iterator.hasNext();) {
	    FclBl fclBl = (FclBl) iterator.next();
	    // fileNumberForQuotaionBLBooking = new
	    // FileNumberForQuotaionBLBooking(null,null,fclBl);
	    fileNumberForQuotaionBLBooking.setFclBlId(fclBl.getBol());
	    searchListByFileNumber.add(fileNumberForQuotaionBLBooking);
	}
	return searchListByFileNumber;
    }

    public Quotation getFileNoObject(String fileNo) throws Exception {
	Iterator results = null;
	Quotation quotation = null;
	String queryString = "";
	queryString = "from Quotation where fileNo=?0";
	Query queryObject = getCurrentSession().createQuery(queryString);
	queryObject.setParameter("0", fileNo);
	results = queryObject.list().iterator();
	while (results.hasNext()) {
	    quotation = (Quotation) results.next();
	}
	return quotation;
    }

    public Quotation getFileNoObjectFromScheduler(String fileNo) throws Exception {
	Iterator results = null;
	Quotation quotation = null;
	String queryString = "";
	queryString = "from Quotation where fileNo=?0";
	Transaction transaction = getCurrentSession().beginTransaction();
	Query queryObject = getCurrentSession().createQuery(queryString);
	queryObject.setParameter("0", fileNo);
	results = queryObject.list().iterator();
	transaction.commit();
	while (results.hasNext()) {
	    quotation = (Quotation) results.next();
	}
	return quotation;
    }

    public static List getFclOriginDestin(String file_no) throws Exception {
	List resultList = new ArrayList();
	String query = "select Terminal,Port,commodity_code,sslName,ssline_no from fcl_bl f inner join quotation q on f.file_no = q.file_no"
		+ "where f.file_no = ";
	query += file_no;
	List list = new QuotationDAO().getSession().createSQLQuery(query).list();
	for (Object _obj : list) {
	    Object[] objectArray = (Object[]) _obj;
	    resultList.add(objectArray[0]);
	    resultList.add(objectArray[1]);
	    resultList.add(objectArray[2]);
	    resultList.add(objectArray[3]);
	    resultList.add(objectArray[4]);
	}
	return resultList;
    }

    public String getEmailToDisplayINReport(String code,
	    String issuingTerminal, String unlocCode) throws Exception {
	String email = null;
	String query = "select R.email from RETADD R left join ports P on R.destin=P.govschnum where "
		+ "R.code='"
		+ code
		+ "' and R.blterm='"
		+ issuingTerminal
		+ "' and P.unlocationcode='" + unlocCode + "'";
	List list = new QuotationDAO().getSession().createSQLQuery(query).list();
	for (Object object : list) {
	    email = (String) object;
	}
	return email;
    }

    public String getEmailToDisplayForPortZero(String code,
	    String issuingTerminal, String unlocCode) throws Exception {
	String email = null;
	String query = "SELECT R.email FROM RETADD R  WHERE R.code='"
		+ code + "' AND R.blterm='" + issuingTerminal + "'"
		+ " AND R.destin='" + unlocCode + "'";
	List list = new QuotationDAO().getSession().createSQLQuery(query).list();
	for (Object object : list) {
	    email = (String) object;
	}
	return email;
    }

    public Integer getCommentsFr() {
	String QueryString = "select max(fileNo)+1 from Quotation";
	Integer fileId = (Integer)getCurrentSession().createQuery(QueryString).uniqueResult();
	return null!=fileId?fileId:0;
    }

    public String getDestination_port(String Destin) throws Exception {
	String destination = "";
	String unlocCode = "";
	if (null != Destin && !Destin.equals("")) {
	    unlocCode = Destin.substring(Destin.lastIndexOf("(") + 1, Destin.length() - 1);
	    if (null != unlocCode && !unlocCode.equals("")) {
		String queryString = "SELECT portname FROM ports WHERE unlocationcode LIKE '" + unlocCode + "'";
		Query queryObject = getSession().createSQLQuery(queryString);
		destination = null != queryObject.uniqueResult() ? queryObject.uniqueResult().toString() : "";
	    }
	}
	return destination;
    }

    public void updateCopyQuoteAdjustment(Integer quoteId, Integer updateId) throws Exception {
	String queryString = "UPDATE charges c, (SELECT DISTINCT Unit_Type, chargeCodeDesc, account_no,Qoute_ID,adjestment FROM charges WHERE adjestment != 0  AND `Qoute_ID` = " + quoteId + ") as c1 "
		+ "SET c.adjestment = c1.adjestment  WHERE  c.`chargeCodeDesc` = c1.chargeCodeDesc AND c.`Unit_Type`= c1.unit_type "
		+ "AND  c.`Qoute_ID` = " + updateId;
	Query queryObject = getSession().createSQLQuery(queryString);
	queryObject.executeUpdate();
    }

    public Boolean IsSearchScreenReset(String loginName) throws Exception {
	StringBuilder queryBuilder = new StringBuilder();
	Boolean result = false;
	queryBuilder.append("SELECT search_screen_reset FROM user_details WHERE login_name='").append(loginName).append("'");
	result = (Boolean) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
	return result;
    }

    public void deleteInlandOrIntmodRamp(String quoteId, String chargCode) throws Exception {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("delete from Charges  where Qoute_ID='").append(quoteId);
	queryBuilder.append("' and Unit_Type is not null and chargeCodeDesc='");
	queryBuilder.append(chargCode).append("'");
	getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public String getHazmat(String fileNo) throws Exception {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("SELECT hazmat from quotation where file_no='").append(fileNo).append("' limit 1");
	return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }
    public boolean isBlFile(String fileNo) throws Exception {
        String query="select count(*) from fcl_bl where file_no='"+fileNo+"'";
        String isBl=getCurrentSession().createSQLQuery(query).uniqueResult().toString();
        return !isBl.equals("0");
    }
    public boolean isRatedBl(String fileNo) throws Exception {
        String q="select q.rates_non_rates from quotation q join fcl_bl fcl on fcl.file_no=q.file_no where fcl.file_no='"+fileNo+"' order by fcl.Bol desc limit 1";
        Object result=getCurrentSession().createSQLQuery(q).uniqueResult();
        return null!=result && result.toString().equals("R");
    }
    
    public boolean isBulletRate(String sslLine,String originUnLoc,String destUnLoc) throws Exception {
        StringBuilder q=new StringBuilder();
        originUnLoc = StringFormatter.orgDestStringFormatter(originUnLoc);
        destUnLoc = StringFormatter.orgDestStringFormatter(destUnLoc);
        q.append("select count(f.ssline_no) from fcl_buy f join genericcode_dup g on f.com_num = g.id ");
        q.append("where  g.field10 = 'Y' and f.ssline_no='").append(sslLine).append("' and f.polcode='");
        q.append(originUnLoc).append("' and f.podcode='").append(destUnLoc).append("' ");
        return !getCurrentSession().createSQLQuery(q.toString()).uniqueResult().toString().equals("0");
    }
    public boolean isBulletRateByFileNo(String fileNo) throws Exception {
        String q="select count(*) from quotation where file_no='"+fileNo+"' and bullet_rates_check='on'";
        return !getCurrentSession().createSQLQuery(q).uniqueResult().toString().equals("0");
    }
    public Quotation findbyFileNo(String fileNo) {
        Criteria criteria = getCurrentSession().createCriteria(Quotation.class);
        criteria.add(Restrictions.eq("fileNo", fileNo));
        return (Quotation) criteria.setMaxResults(1).uniqueResult();
    }
    
    public void clearSpotCost(String fileNo)throws Exception{
        String q="update charges c join quotation q on(q.Quote_ID=c.Qoute_ID) set c.spotrate_amt=null,c.spotrate_markup=null,standard_chk='off',spotrate_chk='off',q.spot_rate='N' where q.file_no='"+fileNo+"'";
        getCurrentSession().createSQLQuery(q).executeUpdate();
    }
    
    public String getInland(String quoteId)throws Exception{
        String q="select inland from quotation where quote_id ='"+quoteId+"' limit 1";
        Object result=getCurrentSession().createSQLQuery(q).uniqueResult();
        return null==result?"N":result.toString();
    }
    public String brandValue(String qouteno){
      String q="select brand from quotation where quote_id ='"+qouteno+"'"; 
      Object result=getCurrentSession().createSQLQuery(q).uniqueResult();
      return null != result.toString() ? result.toString() : "";
    }
     
}
