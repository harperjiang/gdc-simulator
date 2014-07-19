package edu.clarkson.cs.network.ipinfo.api;

import java.nio.charset.Charset;

import org.eclipse.persistence.queries.ScrollableCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import edu.clarkson.cs.network.ipinfo.model.IPInfo;

public class IPInfoService {

	private BloomFilter<CharSequence> filter;

	private IPInfoDao ipInfoDao;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private CapChecker capChecker;

	public IPInfoService() {
		super();

		ipInfoDao = new IPInfoDao();

		// Initalize Bloom Filter
		filter = BloomFilter.create(
				Funnels.stringFunnel(Charset.forName("utf8")), 100000, 0.001);
		// Fill in bloom filter
		ScrollableCursor cursor = ipInfoDao.all();

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
		if (filter.mightContain(ip)) {
			IPInfo info = ipInfoDao.find(ip);
			if (info != null)
				return info;
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
				ipInfoDao.save(info);
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
