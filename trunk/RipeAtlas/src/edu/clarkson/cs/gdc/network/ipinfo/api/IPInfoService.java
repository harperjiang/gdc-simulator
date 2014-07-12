package edu.clarkson.cs.gdc.network.ipinfo.api;

import java.nio.charset.Charset;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.eclipse.persistence.queries.ScrollableCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import edu.clarkson.cs.gdc.network.common.Environment;
import edu.clarkson.cs.gdc.network.ipinfo.model.IPInfo;

public class IPInfoService {

	private BloomFilter<CharSequence> filter;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private CapChecker capChecker;

	public IPInfoService() {
		super();

		// Initalize Bloom Filter
		filter = BloomFilter.create(
				Funnels.stringFunnel(Charset.forName("utf8")), 100000, 0.001);
		// Fill in bloom filter
		EntityManager em = Environment.getEnvironment().getEntityManager();
		ScrollableCursor cursor = (ScrollableCursor) em
				.createQuery("select i from IPInfo i")
				.setHint("eclipselink.cursor.scrollable", true)
				.getSingleResult();

		while (cursor.hasMoreElements()) {
			IPInfo info = (IPInfo) cursor.next();
			filter.put(info.getIp());
		}

		capChecker = new CapChecker();
		capChecker.addDailyCap(REQ_DAILY);
		capChecker.addMinuteCap(REQ_PER_MIN);
	}

	public QueryIPInfoRequest queryip(String ip) {
		QueryIPInfoRequest request = new QueryIPInfoRequest();
		request.setIp(ip);
		return request;
	}

	public IPInfo getInfo(String ip) {
		EntityManager em = Environment.getEnvironment().getEntityManager();
		if (filter.mightContain(ip)) {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<IPInfo> cquery = builder.createQuery(IPInfo.class);
			Root<IPInfo> root = cquery.from(IPInfo.class);
			cquery.where(builder.equal(root.get("ip"), ip));
			try {
				IPInfo info = em.createQuery(cquery).getSingleResult();
				return info;
			} catch (NoResultException e) {
				// Eat this exception and go on to request the data
			}
		}

		try {
			// Query remote info, check time constraint
			while (!capChecker.couldRequest()) {
				try {
					Thread.sleep(capChecker.available());
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			IPInfo info = queryip(ip).execute().getResult();
			capChecker.requested();
			if (info != null) {
				filter.put(info.getIp());

				EntityTransaction transaction = em.getTransaction();
				// Insert this item
				transaction.begin();
				em.persist(info);
				transaction.commit();
			}
			return info;
		} catch (Exception e) {
			logger.error("Exception when requesting ip info", e);
			return null;
		}
	}

	private static final int REQ_PER_MIN = 300;

	private static final int REQ_DAILY = 100000;
}
