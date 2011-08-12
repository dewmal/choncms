package org.chon.common.dao;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.dbutils.BeanProcessor;
import org.chon.core.common.db.DBXML;
import org.chon.core.common.db.ResultSetCallback;


public class AbstractFacade {
	protected DBXML db;
	
	private BeanProcessor beanProcessor = new BeanProcessor();

	public AbstractFacade(DBXML db) {
		this.db = db;
	}
	
	public static Map<String, Object> beanToMap(Object bean) {
		if(bean == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(bean);
		for (PropertyDescriptor pd : pds) {
			String name = pd.getName();
			if ("class".equals(name)) {
				continue;
			}
			try {
				Object value = BeanUtils.getSimpleProperty(bean, name);
				map.put(name, value);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	protected <T> List<T> _list(String query, Map<String, Object> params, final Class<T> clz) {
		final List<T> ls = new ArrayList<T>();
		db.queryRS(query, params, new ResultSetCallback() {
			public boolean process(ResultSet rs) {
				try {
					T o = beanProcessor.toBean(rs, clz);
					ls.add(o);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return true;
			}
		});
		return ls;
	}
	
	protected <T> T _one(String query, Map<String, Object> params, final Class<T> clz) {
		List<T> ls = _list(query, params, clz);
		return ls.size()>0 ? ls.get(0) : null;
	}

	public DBXML getDb() {
		return db;
	}
	
	public int lastId() {
		final Map<String, Integer> map = new HashMap<String, Integer>(); 
		db.basicQueryRS("SELECT LAST_INSERT_ID()", new ResultSetCallback() {
			@Override
			public boolean process(ResultSet rs) {
				try {
					int lastId = rs.getInt("LAST_INSERT_ID()");
					map.put("id", lastId);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return false;
			}
		});
		
		return map.get("id");
	}
}
