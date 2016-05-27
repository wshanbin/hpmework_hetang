package cn.edu.sdu.framework.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;

import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

public abstract class GenericHibernateDao<T> {

	protected static final Object[] EMPTY_OBJECT_ARRAY = new Object[] {};

	protected Log log = LogFactory.getLog(getClass());

	private final Class<T> clazz;
	protected HibernateTemplate hibernateTemplate;

	/**
	 * Inject domain's class type in constructor.
	 * 
	 * @param clazz
	 *            Domain's class.
	 */
	public GenericHibernateDao(Class<T> clazz) {
		this.clazz = clazz;
	}
	/**
	 * @spring.property name="hibernateTemplate" ref="hibernateTemplate"
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@SuppressWarnings("unchecked")
	public T query(Serializable id) {
		T t = (T) hibernateTemplate.get(clazz, id);
		// if(t==null)
		// throw new DataRetrievalFailureException("Object not found.");
		// it is strange that load() method return a lazy-loading proxy object
		// and it may cause LazyInitializationException!
		return t;
	}

	/**
	 * Default implementation of creating new domain object.
	 */
	@Transactional
	public void create(T t) {
		hibernateTemplate.save(t);
	}

	public void flush() {
		hibernateTemplate.flush();
	}

	public void clear() {
		hibernateTemplate.clear();
	}

	/**
	 * Default implementation of deleting new domain object.
	 */
	@Transactional
	public void delete(T t) {
		hibernateTemplate.delete(t);
	}

	/**
	 * Default implementation of updating domain object.
	 */
	@Transactional
	public void update(T t) {
		hibernateTemplate.update(t);
	}


	protected List queryForList(final String select, final Object[] values) {
		// select:
		HibernateCallback selectCallback = new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = session.createQuery(select);
				if (values != null) {
					for (int i = 0; i < values.length; i++)
						query.setParameter(i, values[i]);
				}
				return query.list();
			}
		};
		return (List) hibernateTemplate.execute(selectCallback);
	}

}
