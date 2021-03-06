package com.logiware.lcl.dao;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.lcl.model.LclNotificationModel;
import com.logiware.lcl.model.NotificationModel;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author Lucky
 */
public class LclNotificationDAO extends BaseHibernateDAO {

    private static final Logger log = Logger.getLogger(LclNotificationDAO.class);

    public List<LclNotificationModel> getVoyageNotifications(String frequency) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  group_concat(distinct fn.`id`) as fileNumberIds,");
        queryBuilder.append("  group_concat(distinct fn.`file_number`) as fileNumbers,");
        queryBuilder.append("  ssd.`id` as ssDetailId,");
        queryBuilder.append("  uss.`unit_id` as unitId,");
        queryBuilder.append("  lu.unit_no as unitNo,");
        queryBuilder.append("  coalesce(podt.`trmnam`, fdt.`trmnam`) as fromName,");
        queryBuilder.append("  lower(");
        queryBuilder.append("    coalesce(");
        queryBuilder.append("      podt.`imports_contact_email`,");
        queryBuilder.append("      fdt.`imports_contact_email`");
        queryBuilder.append("    )");
        queryBuilder.append("  ) as fromAddress,");
        queryBuilder.append("  vn.`id` as id ");
        queryBuilder.append("from");
        queryBuilder.append("  `lcl_voyage_notification` vn ");
        queryBuilder.append("  join `lcl_unit_ss` uss ");
        queryBuilder.append("    on (vn.`unit_ss_id` = uss.`id`) ");
        queryBuilder.append("  join `lcl_unit` lu ");
        queryBuilder.append("    on (uss.`unit_id` = lu.`id`) ");
        queryBuilder.append("  join `lcl_ss_detail` ssd ");
        queryBuilder.append("    on (uss.`ss_header_id` = ssd.`ss_header_id`) ");
        queryBuilder.append("  join `lcl_booking_piece_unit` bpu ");
        queryBuilder.append("    on (uss.`id` = bpu.`lcl_unit_ss_id`) ");
        queryBuilder.append("  join `lcl_booking_piece` bp ");
        queryBuilder.append("    on (bpu.`booking_piece_id` = bp.`id`) ");
        queryBuilder.append("  join `lcl_file_number` fn ");
        queryBuilder.append("    on (bp.`file_number_id` = fn.`id`) ");
        queryBuilder.append("  join `lcl_booking` bk ");
        queryBuilder.append("    on (");
        queryBuilder.append("      fn.`id` = bk.`file_number_id` ");
        queryBuilder.append("      and bk.`booking_type` <> 'T'");
        queryBuilder.append("    ) ");
        queryBuilder.append("  join `lcl_booking_import` bki ");
        queryBuilder.append("    on (");
        queryBuilder.append("      fn.`id` = bki.`file_number_id` ");
        queryBuilder.append("      and bki.`transshipment` <> true");
        queryBuilder.append("    ) ");
        queryBuilder.append("  left join `un_location` pod ");
        queryBuilder.append("    on (bk.`pod_id` = pod.`id`) ");
        queryBuilder.append("  left join `ports` podp ");
        queryBuilder.append("    on (pod.`un_loc_code` = podp.`unlocationcode`) ");
        queryBuilder.append("  left join `import_port_configuration` podpc ");
        queryBuilder.append("    on (podp.`id` = podpc.`schnum`) ");
        queryBuilder.append("  left join `terminal` podt ");
        queryBuilder.append("    on (podpc.`trm_num` = podt.`trmnum`) ");
        queryBuilder.append("  left join `un_location` fd ");
        queryBuilder.append("    on (bk.`fd_id` = fd.`id`) ");
        queryBuilder.append("  left join `ports` fdp ");
        queryBuilder.append("    on (");
        queryBuilder.append("      fd.`un_loc_code` = fdp.`unlocationcode`");
        queryBuilder.append("    ) ");
        queryBuilder.append("  left join `import_port_configuration` fdpc ");
        queryBuilder.append("    on (fdp.`id` = fdpc.`schnum`) ");
        queryBuilder.append("  left join `terminal` fdt ");
        queryBuilder.append("    on (fdpc.`trm_num` = fdt.`trmnum`) ");
        queryBuilder.append("where vn.").append(frequency).append(" = 'Pending' ");
        queryBuilder.append("  and (podt.`imports_contact_email` <> '' or fdt.`imports_contact_email` <> '')");
        queryBuilder.append("group by vn.`id` ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("fileNumberIds", StringType.INSTANCE);
        query.addScalar("fileNumbers", StringType.INSTANCE);
        query.addScalar("ssDetailId", LongType.INSTANCE);
        query.addScalar("unitId", LongType.INSTANCE);
        query.addScalar("unitNo", StringType.INSTANCE);
        query.addScalar("fromName", StringType.INSTANCE);
        query.addScalar("fromAddress", StringType.INSTANCE);
        query.addScalar("id", LongType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(LclNotificationModel.class));
        return query.list();
    }

    public String getContactEmailAndFax(Long fileNumberId, String codeEmail, String codeFax) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  `LclContactGetEmailFaxByFileIdCodeF`(");
        queryBuilder.append("    :fileNumberId,");
        queryBuilder.append("    :codeEmail,");
        queryBuilder.append("    :codeFax");
        queryBuilder.append("  ) as contacts ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("fileNumberId", fileNumberId);
        query.setString("codeEmail", codeEmail);
        query.setString("codeFax", codeFax);
        query.addScalar("contacts", StringType.INSTANCE);
        return (String) query.uniqueResult();
    }

    public String getDocumentName(Long ssDetailId, Long unitId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  if(");
        queryBuilder.append("    max(disp.ported) = true,");
        queryBuilder.append("    if(");
        queryBuilder.append("      disp.status = 'PORT',");
        queryBuilder.append("      'ARRIVAL NOTICE',");
        queryBuilder.append("      'STATUS UPDATE'");
        queryBuilder.append("    ),");
        queryBuilder.append("    'PRE ADVICE'");
        queryBuilder.append("  ) as documentName ");
        queryBuilder.append("from");
        queryBuilder.append("  (select ");
        queryBuilder.append("    disp.`elite_code` as status,");
        queryBuilder.append("    case");
        queryBuilder.append("      when disp.`elite_code` = 'PORT' ");
        queryBuilder.append("      then true ");
        queryBuilder.append("      else false ");
        queryBuilder.append("    end as ported ");
        queryBuilder.append("  from");
        queryBuilder.append("    `disposition` disp ");
        queryBuilder.append("    join `lcl_unit_ss_dispo` ussd ");
        queryBuilder.append("      on (");
        queryBuilder.append("        disp.`id` = ussd.`disposition_id`");
        queryBuilder.append("      ) ");
        queryBuilder.append("  where ussd.`ss_detail_id` = :ssDetailId ");
        queryBuilder.append("    and ussd.`unit_id` = :unitId ");
        queryBuilder.append("  order by ussd.`id` desc) as disp ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("ssDetailId", ssDetailId);
        query.setLong("unitId", unitId);
        query.addScalar("documentName", StringType.INSTANCE);
        query.setMaxResults(1);
        return (String) query.uniqueResult();
    }

    public void updateVoyageNotification(Long id, String frequency) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update");
        queryBuilder.append("  `lcl_voyage_notification` vn ");
        queryBuilder.append("set");
        queryBuilder.append("  vn.").append(frequency).append(" = 'Closed' ");
        queryBuilder.append("where");
        queryBuilder.append("  vn.`id` = :id");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("id", id);
        query.executeUpdate();
    }

    public void insertDrStatusUpdate(Long fileId, Integer ownerUserId, String toAddress, Integer userId, String subject) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into lcl_fn_notification");
        sb.append("(file_number_id,frequency,status,notify_type,from_user_id,to_address,subject,entered_datetime,entered_by_user_id,modified_datetime) ");
        sb.append(" values(:fileId,'ANY TIME','Pending','DR Notification',:ownerUserId,:toAddress,:subject,SYSDATE(),:userId,SYSDATE())");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setLong("fileId", fileId);
        query.setInteger("ownerUserId", ownerUserId);
        query.setString("toAddress", toAddress);
        query.setString("subject", subject);
        query.setInteger("userId", userId);
        query.executeUpdate();
    }

    public List<NotificationModel> getDrStatusUpdate(String frequency) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("    id as id,");
        queryBuilder.append("    file_number_id as fileNumberId,");
        queryBuilder.append("    from_user_id as fromUserId,");
        queryBuilder.append("    to_address as toAddress,");
        queryBuilder.append("    subject as subject,");
        queryBuilder.append("    entered_by_user_id as enterByUserId ");
        queryBuilder.append("  from ");
        queryBuilder.append("    lcl_fn_notification fn ");
        queryBuilder.append("    where status = 'Pending' ");
        queryBuilder.append("      and notify_type = 'DR Notification'");
        queryBuilder.append("      and frequency = :frequency");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("frequency", frequency);
        query.addScalar("id", LongType.INSTANCE);
        query.addScalar("fileNumberId", LongType.INSTANCE);
        query.addScalar("fromUserId", IntegerType.INSTANCE);
        query.addScalar("toAddress", StringType.INSTANCE);
        query.addScalar("subject", StringType.INSTANCE);
        query.addScalar("enterByUserId", IntegerType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(NotificationModel.class));
        return query.list();
    }

    public void updateDrNotification(Long id) {
        StringBuilder sb = new StringBuilder();
        sb.append("update lcl_fn_notification set status = 'Sent',modified_datetime=SYSDATE() where id=:id");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setLong("id", id);
        query.executeUpdate();
    }

    public String getDocumentNameByFileid(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(");
        queryBuilder.append("    max(voy.`ported`) = true,");
        queryBuilder.append("    if(");
        queryBuilder.append("      voy.`disposition` = 'PORT',");
        queryBuilder.append("      'ARRIVAL NOTICE',");
        queryBuilder.append("      'STATUS UPDATE'");
        queryBuilder.append("    ), ");
        queryBuilder.append("    'PRE ADVICE'");
        queryBuilder.append("  ) as documentName ");
        queryBuilder.append("from");
        queryBuilder.append("  (select");
        queryBuilder.append("     disp.`elite_code` as disposition,");
        queryBuilder.append("     case");
        queryBuilder.append("       when disp.`elite_code` = 'PORT'");
        queryBuilder.append("       then true");
        queryBuilder.append("       else false");
        queryBuilder.append("     end as ported");
        queryBuilder.append("   from");
        queryBuilder.append("     `lcl_booking_piece` bp");
        queryBuilder.append("     join `lcl_booking_piece_unit` bpu");
        queryBuilder.append("       on (bp.`id` = bpu.`booking_piece_id`)");
        queryBuilder.append("     JOIN `lcl_unit_ss` us");
        queryBuilder.append("       ON (bpu.`lcl_unit_ss_id` = us.`id`)");
        queryBuilder.append("     join `lcl_ss_header` sh");
        queryBuilder.append("       on (us.`ss_header_id` = sh.`id`)");
        queryBuilder.append("     join `lcl_ss_detail` sd");
        queryBuilder.append("       on (sh.`id` = sd.`ss_header_id`)");
        queryBuilder.append("     join `lcl_unit` u");
        queryBuilder.append("       on (us.`unit_id` = u.`id`)");
        queryBuilder.append("     join `lcl_unit_ss_dispo` usd");
        queryBuilder.append("       on ( ");
        queryBuilder.append("         sd.`id` = usd.`ss_detail_id`");
        queryBuilder.append("         and u.`id` = usd.`unit_id`");
        queryBuilder.append("       )  ");
        queryBuilder.append("     join `disposition` disp");
        queryBuilder.append("       on (usd.`disposition_id` = disp.`id`)");
        queryBuilder.append("   where");
        queryBuilder.append("     bp.`file_number_id` = :fileId");
        queryBuilder.append("   order by usd.`id` desc");
        queryBuilder.append("  ) as voy");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("fileId", fileId);
        return (String) query.uniqueResult();
    }

    public Integer getNoOfDrsWithoutPickUpDate() throws Exception {
        StringBuilder qb = new StringBuilder();
        qb.append("select  ");
        qb.append("  count(distinct fn.`file_number`) as resultCount ");
        qb.append("from");
        qb.append("  `lcl_file_number` fn");
        qb.append("  join `lcl_booking` bk");
        qb.append("    on (fn.`id` = bk.`file_number_id`)");
        qb.append("  join `lcl_booking_import` bki  ");
        qb.append("    on ( ");
        qb.append("      fn.`id` = bki.`file_number_id`");
        qb.append("      and bki.`picked_up_datetime` is null");
        qb.append("    )  ");
        qb.append("  join `cust_contact` cc");
        qb.append("    on (bk.`cons_acct_no` = cc.`acct_no`)");
        qb.append("  join `genericcode_dup` ge");
        qb.append("    on ( ");
        qb.append("      cc.`code_f` = ge.`id`");
        qb.append("      and ( ");
        qb.append("        (ge.`code` = 'E1' and cc.`email` <> '')");
        qb.append("        or (ge.`code` = 'F1' and cc.`fax` <> '')");
        qb.append("      )");
        qb.append("    )");
        SQLQuery query = getCurrentSession().createSQLQuery(qb.toString());
        query.addScalar("resultCount", IntegerType.INSTANCE);
        return (Integer) query.uniqueResult();
    }

    public List<NotificationModel> getDrsWithNoPickupDate() throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  fn.`id` as id,");
        queryBuilder.append("  fn.`file_number` as fileNumber,");
        queryBuilder.append("  fn.`entered_by_user_id` as enterByUserId,");
        queryBuilder.append("  fn.`owner_user_id` as fromUserId,");
        queryBuilder.append("  fn.`doc_name` as docName, ");
        queryBuilder.append("  fn.unit_no AS unitNo ");
        queryBuilder.append("from");
        queryBuilder.append("  (select ");
        queryBuilder.append("    fn.`id` as id,");
        queryBuilder.append("    fn.`file_number`,");
        queryBuilder.append("    fn.`booking_type`,");
        queryBuilder.append("    fn.`entered_by_user_id`,");
        queryBuilder.append("    fn.`owner_user_id`,");
        queryBuilder.append("    (fn.`booking_type` = 'I' or (fn.`booking_type` = 'T' and max(fn.`available`) = false)) as can_send,");
        queryBuilder.append("    if(");
        queryBuilder.append("      max(fn.`ported`) = true,");
        queryBuilder.append("      if(");
        queryBuilder.append("        fn.`disposition` = 'PORT',");
        queryBuilder.append("        'ARRIVAL NOTICE',");
        queryBuilder.append("        'STATUS UPDATE'");
        queryBuilder.append("      ),");
        queryBuilder.append("      'PRE ADVICE'");
        queryBuilder.append("    ) as doc_name, fn.unit_no AS unit_no, fn.disposition AS disposition  ");
        queryBuilder.append("  from");
        queryBuilder.append("    (select ");
        queryBuilder.append("      fn.`id` as id,");
        queryBuilder.append("      fn.`file_number`,");
        queryBuilder.append("      fn.`booking_type`,");
        queryBuilder.append("      fn.`entered_by_user_id`,");
        queryBuilder.append("      sh.`owner_user_id`,");
        queryBuilder.append("      disp.`elite_code` as disposition,");
        queryBuilder.append("      case");
        queryBuilder.append("        when disp.`elite_code` = 'AVAL' ");
        queryBuilder.append("        then true ");
        queryBuilder.append("        else false ");
        queryBuilder.append("      end as available,");
        queryBuilder.append("      case");
        queryBuilder.append("        when disp.`elite_code` = 'PORT' ");
        queryBuilder.append("        then true ");
        queryBuilder.append("        else false ");
        queryBuilder.append("      end as ported, u.unit_no AS unit_no  ");
        queryBuilder.append("    from");
        queryBuilder.append("      (select ");
        queryBuilder.append("        fn.`id`,");
        queryBuilder.append("        fn.`file_number`,");
        queryBuilder.append("        bk.`booking_type`,");
        queryBuilder.append("        bk.`entered_by_user_id` ");
        queryBuilder.append("      from");
        queryBuilder.append("        `lcl_file_number` fn ");
        queryBuilder.append("        join `lcl_booking` bk ");
        queryBuilder.append("          on (fn.`id` = bk.`file_number_id`) ");
        queryBuilder.append("        join `lcl_booking_import` bki ");
        queryBuilder.append("          on (");
        queryBuilder.append("            fn.`id` = bki.`file_number_id` ");
        queryBuilder.append("            and bki.`picked_up_datetime` is null");
        queryBuilder.append("          ) ");
        queryBuilder.append("      where fn.status <> 'X' ");
        queryBuilder.append("      group by fn.`file_number`) as fn ");
        queryBuilder.append("      join `lcl_booking_piece` bp ");
        queryBuilder.append("        on (fn.`id` = bp.`file_number_id`) ");
        queryBuilder.append("      join `lcl_booking_piece_unit` bpu ");
        queryBuilder.append("        on (bp.`id` = bpu.`booking_piece_id`) ");
        queryBuilder.append("      join `lcl_unit_ss` us ");
        queryBuilder.append("        on (bpu.`lcl_unit_ss_id` = us.`id`) ");
        queryBuilder.append("      join `lcl_ss_header` sh ");
        queryBuilder.append("        on (us.`ss_header_id` = sh.`id` ");
        queryBuilder.append("      and sh.closed_by_user_id IS NULL ");
        queryBuilder.append("      and sh.audited_by_user_id IS NULL ");
        queryBuilder.append("      ) ");
        queryBuilder.append("      join `lcl_ss_detail` sd ");
        queryBuilder.append("        on (sh.`id` = sd.`ss_header_id`) ");
        queryBuilder.append("      join `lcl_unit` u ");
        queryBuilder.append("        on (us.`unit_id` = u.`id`) ");
        queryBuilder.append("      join `lcl_unit_ss_dispo` usd ");
        queryBuilder.append("        on (");
        queryBuilder.append("          sd.`id` = usd.`ss_detail_id` ");
        queryBuilder.append("          and u.`id` = usd.`unit_id`");
        queryBuilder.append("        ) ");
        queryBuilder.append("      join `disposition` disp ");
        queryBuilder.append("        on (");
        queryBuilder.append("          usd.`disposition_id` = disp.`id`");
        queryBuilder.append("          and disp.`elite_code` <> 'DATA'");
        queryBuilder.append("        ) ");
        queryBuilder.append("    order by usd.`id` desc) as fn ");
        queryBuilder.append("  group by fn.`file_number`) as fn ");
        queryBuilder.append("where fn.`can_send` = true and fn.disposition <> 'AVAL' ");
        queryBuilder.append("order by fn.`file_number`");
        log.info(queryBuilder.toString());
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("id", LongType.INSTANCE);
        query.addScalar("fileNumber", StringType.INSTANCE);
        query.addScalar("enterByUserId", IntegerType.INSTANCE);
        query.addScalar("fromUserId", IntegerType.INSTANCE);
        query.addScalar("docName", StringType.INSTANCE);
        query.addScalar("unitNo", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(NotificationModel.class));
        return query.list();
    }

    public String getDisposition(Long fileNumberId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(sh.`closed_by_user_id` is not null, 'CLSD', if(sh.`audited_by_user_id` is not null, 'AUDT', coalesce(ucase(disp.`elite_code`), 'DATA'))) as disposition ");
        queryBuilder.append("from");
        queryBuilder.append("  `lcl_file_number` fn ");
        queryBuilder.append("  left join `lcl_booking_piece` bp ");
        queryBuilder.append("    on (fn.`id` = bp.`file_number_id`) ");
        queryBuilder.append("  left join `lcl_booking_piece_unit` bpu ");
        queryBuilder.append("    on (bp.`id` = bpu.`booking_piece_id`) ");
        queryBuilder.append("  left join `lcl_unit_ss` us ");
        queryBuilder.append("    on (bpu.`lcl_unit_ss_id` = us.`id`) ");
        queryBuilder.append("  left join `lcl_ss_header` sh ");
        queryBuilder.append("    on (us.`ss_header_id` = sh.`id`) ");
        queryBuilder.append("  left join `lcl_ss_detail` sd ");
        queryBuilder.append("    on (sh.`id` = sd.`ss_header_id`) ");
        queryBuilder.append("  left join `lcl_unit` u ");
        queryBuilder.append("    on (us.`unit_id` = u.`id`) ");
        queryBuilder.append("  left join `lcl_unit_ss_dispo` usd ");
        queryBuilder.append("    on (");
        queryBuilder.append("      sd.`id` = usd.`ss_detail_id` ");
        queryBuilder.append("      and u.`id` = usd.`unit_id`");
        queryBuilder.append("    ) ");
        queryBuilder.append("  left join `disposition` disp ");
        queryBuilder.append("    on (");
        queryBuilder.append("      usd.`disposition_id` = disp.`id` ");
        queryBuilder.append("    ) ");
        queryBuilder.append("where");
        queryBuilder.append("  fn.`id` = :fileNumberId ");
        queryBuilder.append("order by usd.`id` desc limit 1");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("fileNumberId", fileNumberId);
        query.addScalar("disposition", StringType.INSTANCE);
        return (String) query.uniqueResult();
    }
       public String getContactEmailAndFaxForStatusUpdateDaily(Long fileNumberId, String codeEmail, String codeFax) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  `LclContactGetEmailFaxForStatusDailyUpdate`(");
        queryBuilder.append("    :fileNumberId,");
        queryBuilder.append("    :codeEmail,");
        queryBuilder.append("    :codeFax");
        queryBuilder.append("  ) as contacts ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("fileNumberId", fileNumberId);
        query.setString("codeEmail", codeEmail);
        query.setString("codeFax", codeFax);
        query.addScalar("contacts", StringType.INSTANCE);
        return (String) query.uniqueResult();
    }

    public List<LclNotificationModel> getCodekContactDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ");
        sb.append("  bkg.`file_number` AS fileNumbers,");
        sb.append("  bkg.`id` AS fileNumberIds,");
        sb.append("  bkg.`entered_by_user_id` AS userId,");
        sb.append("  bkg.`email_fax` AS toEmailFax ");
        sb.append(" FROM");
        sb.append("  (SELECT ");
        sb.append("    bkg.`file_number`,");
        sb.append("    bkg.`id`,");
        sb.append("    bkg.`entered_by_user_id`,");
        sb.append("    IF(");
        sb.append("      `CustomerGetIsNoCredit` (bkg.`acct_no`) = FALSE,");
        sb.append("      `ContactGetEmailFaxByAcctNoCodeK` (bkg.`acct_no`, 'E', 'F'),");
        sb.append("      ''");
        sb.append("    ) AS email_fax ");
        sb.append("  FROM");
        sb.append("    (SELECT ");
        sb.append("      fn.`file_number`,");
        sb.append("      fn.`id`,");
        sb.append("      bkg.`entered_by_user_id`,");
        sb.append("      IF(");
        sb.append("        bkg.`bill_to_party` = 'C',");
        sb.append("        bkg.`cons_acct_no`,");
        sb.append("        IF(");
        sb.append("          bkg.`bill_to_party` = 'T',");
        sb.append("          bkg.`third_party_acct_no`,");
        sb.append("          IF(");
        sb.append("            bkg.`bill_to_party` = 'N',");
        sb.append("            bkg.`noty_acct_no`,''");
        sb.append("          )");
        sb.append("        )");
        sb.append("      ) AS acct_no ");
        sb.append("    FROM");
        sb.append("      (SELECT ");
        sb.append("        bp.`file_number_id` ");
        sb.append("      FROM");
        sb.append("        `lcl_unit_whse` uw ");
        sb.append("        JOIN `lcl_unit_ss` us ");
        sb.append("          ON (us.`ss_header_id` = uw.`ss_header_id`) ");
        sb.append("        JOIN `lcl_booking_piece_unit` bpu ");
        sb.append("          ON (bpu.`lcl_unit_ss_id` = us.`id`) ");
        sb.append("        JOIN `lcl_booking_piece` bp ");
        sb.append("          ON (bp.`id` = bpu.`booking_piece_id`) ");
        sb.append("      WHERE uw.`destuffed_datetime` = DATE_SUB(CURRENT_DATE(), INTERVAL 5 DAY)) AS bp ");
        sb.append("      JOIN `lcl_file_number` fn ");
        sb.append("        ON (fn.`id` = bp.`file_number_id`) ");
        sb.append("      JOIN `lcl_booking` bkg ");
        sb.append("        ON (bkg.`file_number_id` = fn.`id` AND bkg.`booking_type` IN ('I'))");
        sb.append(" JOIN `lcl_booking_import` lbi ");
        sb.append(" ON (lbi.`file_number_id` = fn.`id`)) AS bkg) AS bkg");
        sb.append(" WHERE bkg.`email_fax` <> '';");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.addScalar("fileNumbers", StringType.INSTANCE);
        queryObject.addScalar("fileNumberIds", StringType.INSTANCE);
        queryObject.addScalar("toEmailFax", StringType.INSTANCE);
        queryObject.addScalar("userId", IntegerType.INSTANCE);
        queryObject.setResultTransformer(Transformers.aliasToBean(LclNotificationModel.class));
        return queryObject.list();  
    }
}
