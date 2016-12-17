/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.datamigration;

import com.infomata.data.DataRow;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lakshmi Naryanan
 */
public interface DataMap<K, V> extends Map<K, V> {

    public Boolean getBoolean(K key);

    public String getString(K key);

    public Integer getInteger(K key);

    public Double getDouble(K key);

    public Date getDate(K key);

    public List<V> getList(K key);

    public void putBoolean(K key, Boolean value);

    public void putString(K key, String value);

    public void putInteger(K key, Integer value);

    public void putDouble(K key, Double value);

    public void putDate(K key, Date value);

    public void putList(K key, List<V> value);

    public void putBoolean(K key, ResultSet result, String columnName) throws SQLException;

    public void putString(K key, ResultSet result, String columnName) throws SQLException;

    public void putInteger(K key, ResultSet result, String columnName) throws SQLException;

    public void putDouble(K key, ResultSet result, String columnName) throws SQLException;

    public void putDate(K key, ResultSet result, String columnName) throws SQLException;

    public void putBoolean(K key, ResultSet result) throws SQLException;

    public void putString(K key, ResultSet result) throws SQLException;

    public void putInteger(K key, ResultSet result) throws SQLException;

    public void putDouble(K key, ResultSet result) throws SQLException;

    public void putDate(K key, ResultSet result) throws SQLException;

    public void putString(K key, DataRow row, int index);
}
