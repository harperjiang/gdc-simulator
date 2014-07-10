package edu.clarkson.cs.gdc.network.common;

import java.text.MessageFormat;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

public abstract class ListRequest<T extends Response<?>> extends Request<T> {

	public ListRequest(String url) {
		super(url);
	}

	private int offset = 0;

	private int limit = 20;

	private Object[] query;

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Object[] getQuery() {
		return query;
	}

	public void setQuery(Object[] query) {
		this.query = query;
	}

	@Override
	protected HttpUriRequest buildRequest() {
		String url = MessageFormat.format("{0}?{1}", getUrl(), QueryUtils
				.queryString("offset", getOffset(), "limit", getLimit()));
		if (query != null && query.length != 0) {
			String queryStr = QueryUtils.queryString(query);
			url = MessageFormat.format("{0}&{1}", url, queryStr);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Querying url:" + url);
		}
		HttpGet get = new HttpGet(url);
		return get;
	}
}
