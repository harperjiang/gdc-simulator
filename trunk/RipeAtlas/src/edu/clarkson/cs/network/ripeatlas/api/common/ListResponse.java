package edu.clarkson.cs.network.ripeatlas.api.common;

import java.io.IOException;

import org.apache.http.HttpResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class ListResponse<T> extends RipeAtlasResponse<T> {

	private int count;

	private String prevPageUrl;

	private String nextPageUrl;

	private int limit;

	private int offset;

	protected ListResponse(HttpResponse response) throws IOException {
		super(response);
	}

	protected void metainfo(JsonElement json) {
		JsonObject jsonObject = (JsonObject) json;
		JsonObject meta = jsonObject.get("meta").getAsJsonObject();
		this.count = meta.get("total_count").getAsInt();
		if (!isNull(meta.get("previous"))) {
			this.prevPageUrl = meta.get("previous").getAsString();
		}
		if (!isNull(meta.get("next"))) {
			this.nextPageUrl = meta.get("next").getAsString();
		}
		this.limit = meta.get("limit").getAsInt();
		this.offset = meta.get("offset").getAsInt();
	}

	public int getCount() {
		return count;
	}

	public String getPrevPageUrl() {
		return prevPageUrl;
	}

	public String getNextPageUrl() {
		return nextPageUrl;
	}

	public int getLimit() {
		return limit;
	}

	public int getOffset() {
		return offset;
	}

}
