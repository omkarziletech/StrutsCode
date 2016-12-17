package com.logiware.utils;

import com.gp.cvst.logisoft.domain.LineItem;
import com.logiware.bean.LineItemBean;
import java.util.Comparator;

public class LineItemComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof LineItem && o2 instanceof LineItem) {
            LineItem l1 = (LineItem) o1;
            LineItem l2 = (LineItem) o2;
            if (null != l1.getLineItemId() && null != l2.getLineItemId()) {
                Integer id1 = Integer.parseInt(l1.getLineItemId().substring(l1.getLineItemId().lastIndexOf("-") + 1));
                Integer id2 = Integer.parseInt(l2.getLineItemId().substring(l2.getLineItemId().lastIndexOf("-") + 1));
                return id1.compareTo(id2);
            } else {
                return 0;
            }
        } else if (o1 instanceof LineItemBean && o2 instanceof LineItemBean) {
            LineItemBean l1 = (LineItemBean) o1;
            LineItemBean l2 = (LineItemBean) o2;
            if (null != l1.getId() && null != l2.getId()) {
                Integer id1 = Integer.parseInt(l1.getId().substring(l1.getId().lastIndexOf("-") + 1));
                Integer id2 = Integer.parseInt(l2.getId().substring(l2.getId().lastIndexOf("-") + 1));
                return id1.compareTo(id2);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
}
