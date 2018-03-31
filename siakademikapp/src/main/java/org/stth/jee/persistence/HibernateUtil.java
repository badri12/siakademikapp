package org.stth.jee.persistence;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil
{
	private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
	private static SessionFactory sessionFactory;

	private static Session session;

	static void configureSessionFactory()
	{
		try {
			logger.debug("Initializing HibernateUtil");
			StandardServiceRegistry standardRegistry = 
					new StandardServiceRegistryBuilder().configure().build();
			Metadata metaData = 
					new MetadataSources(standardRegistry).getMetadataBuilder().build();
			sessionFactory = metaData.getSessionFactoryBuilder().build();

		}
		catch (Throwable e)
		{
			System.err.println("Initial SessionFactory creation failed" + e);
			logger.error(e.toString());
			throw new ExceptionInInitializerError(e);
		}
	}

	public static SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	public static Session getSession() {
		if (sessionFactory == null) {
			configureSessionFactory();
			logger.debug(" " + sessionFactory);
		}
		if (session == null) {
			session = sessionFactory.getCurrentSession();
		}
		if (!session.isOpen()) {
			session = sessionFactory.openSession();
		}
		return session;
	}

	public static void closeSession() throws HibernateException {
		try {
			sessionFactory.getCurrentSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
