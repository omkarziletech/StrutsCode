package com.logiware.datamigration;

import com.infomata.data.DataRow;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Lakshmi Naryanan
 */
public class DataMapImpl<K, V> extends HashMap<K, V> implements DataMap<K, V> {

    @Override
    public Boolean getBoolean(K key) {
        return (Boolean) get(key);
    }

    @Override
    public String getString(K key) {
        return (String) get(key);
    }

    @Override
    public Integer getInteger(K key) {
        return (Integer) get(key);
    }

    @Override
    public Double getDouble(K key) {
        return (Double) get(key);
    }

    @Override
    public Date getDate(K key) {
        return (Date) get(key);
    }

    @Override
    public List<V> getList(K key) {
        return (List) get(key);
    }

    @Override
    public void putBoolean(K key, Boolean value) {
        put(key, (V) value);
    }

    @Override
    public void putString(K key, String value) {
        put(key, (V) value);
    }

    @Override
    public void putInteger(K key, Integer value) {
        put(key, (V) value);
    }

    @Override
    public void putDouble(K key, Double value) {
        put(key, (V) value);
    }

    @Override
    public void putDate(K key, Date value) {
        put(key, (V) value);
    }

    @Override
    public void putList(K key, List<V> value) {
        put(key, (V) value);
    }

    @Override
    public void putBoolean(K key, ResultSet result, String columnName) throws SQLException {
        Boolean value = result.getBoolean(columnName);
        putBoolean(key, value);
    }

    @Override
    public void putString(K key, ResultSet result, String columnName) throws SQLException {
        String value = result.getString(columnName);
        putString(key, null != value ? value : "");
    }

    @Override
    public void putInteger(K key, ResultSet result, String columnName) throws SQLException {
        Integer value = result.getInt(columnName);
        putInteger(key, value);
    }

    @Override
    public void putDouble(K key, ResultSet result, String columnName) throws SQLException {
        Double value = result.getDouble(columnName);
        putDouble(key, value);
    }

    @Override
    public void putDate(K key, ResultSet result, String columnName) throws SQLException {
        Date value = result.getDate(columnName);
        putDate(key, value);
    }

    @Override
    public void putBoolean(K key, ResultSet result) throws SQLException {
        String columnName = ((String) key).replaceAll("(.)(\\p{Lu})", "$1_$2").toLowerCase();
        putBoolean(key, result, columnName);
    }

    @Override
    public void putString(K key, ResultSet result) throws SQLException {
        String columnName = ((String) key).replaceAll("(.)(\\p{Lu})", "$1_$2").toLowerCase();
        putString(key, result, columnName);
    }

    @Override
    public void putInteger(K key, ResultSet result) throws SQLException {
        String columnName = ((String) key).replaceAll("(.)(\\p{Lu})", "$1_$2").toLowerCase();
        putInteger(key, result, columnName);
    }

    @Override
    public void putDouble(K key, ResultSet result) throws SQLException {
        String columnName = ((String) key).replaceAll("(.)(\\p{Lu})", "$1_$2").toLowerCase();
        putDouble(key, result, columnName);
    }

    @Override
    public void putDate(K key, ResultSet result) throws SQLException {
        String columnName = ((String) key).replaceAll("(.)(\\p{Lu})", "$1_$2").toLowerCase();
        putDate(key, result, columnName);
    }

    @Override
    public void putString(K key, DataRow row, int index) {
        String value = row.getString(index);
        putString(key, null != value ? value : "");
    }
}