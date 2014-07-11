package edu.clarkson.cs.gdc.network.ipquery.api;

import java.nio.charset.Charset;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import edu.clarkson.cs.gdc.network.ipquery.model.IPInfo;

public class IPInfoService {

	private BloomFilter<CharSequence> filter;

	private EntityManager entityManager;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public IPInfoService() {
		super();

		// Initalize Entity Manager
		entityManager = Persistence.createEntityManagerFactory("network")
				.createEntityManager();

		// Initalize Bloom Filter
		filter = BloomFilter.create(
				Funnels.stringFunnel(Charset.forName("utf8")), 100000, 0.001);
		// Fill in bloom filter
		
	}

	public QueryIPInfoRequest queryip(String ip) {
		QueryIPInfoRequest request = new QueryIPInfoRequest();
		request.setIp(ip);
		return request;
	}

	public IPInfo getInfo(String ip) {
		if (filter.mightContain(ip)) {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<IPInfo> cquery = builder.createQuery(IPInfo.class);
			Root<IPInfo> root = cquery.from(IPInfo.class);
			cquery.where(builder.equal(root.get("ip"), ip));
			try {
				IPInfo info = entityManager.createQuery(cquery)
						.getSingleResult();
				return info;
			} catch (NoResultException e) {
				// Eat this exception and go on to request the data
			}
		}

		try {
			IPInfo info = queryip(ip).execute().getResult();
			if (info != null) {
				filter.put(info.getIp());

				EntityTransaction transaction = entityManager.getTransaction();
				// Insert this item
				transaction.begin();
				entityManager.persist(info);
				transaction.commit();
			}
			return info;
		} catch (Exception e) {
			logger.error("Exception when requesting ip info", e);
			return null;
		}

	}
}
