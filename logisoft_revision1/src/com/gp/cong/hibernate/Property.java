package com.gp.cong.hibernate;

import java.util.Random;

/**
 *
 * @author sunil
 */
public class Property {
    private String name;
    private String proxy;
    private Object value;
    private QueryOperator queryOperator;

    private Property(String name, Object value) {
      this(name, value, QueryOperator.EQUAL);
    }

    private Property(String name, Object value, QueryOperator queryOperator) {
        this.name = name;
        this.value = value;
        this.queryOperator = queryOperator;
        Random random = new Random();        
        this.proxy = "L"+random.nextInt(100000);
    }

    public static Property getInstance(String name, Object value){
        return new Property(name, value);
    }
    public static Property getInstance(String name, Object value, QueryOperator queryOperator){
        return new Property(name, value,queryOperator);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QueryOperator getQueryOperator() {
        return queryOperator;
    }

    public void setQueryOperator(QueryOperator queryOperator) {
        this.queryOperator = queryOperator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Property other = (Property) obj;
        if ((this.proxy == null) ? (other.proxy != null) : !this.proxy.equals(other.proxy)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (this.proxy != null ? this.proxy.hashCode() : 0);
        return hash;
    }

}
