package edu.clarkson.cs.gdc.network.ipquery.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.clarkson.cs.gdc.network.common.Environment;
import edu.clarkson.cs.gdc.network.common.Response;
import edu.clarkson.cs.gdc.network.common.ResponseError;
import edu.clarkson.cs.gdc.network.ipquery.model.IPInfo;

public class QueryIPInfoResponse extends Response<IPInfo> {

	protected QueryIPInfoResponse(HttpResponse response) throws IOException {
		super(response);
	}

	@Override
	protected IPInfo buildResult(JsonElement json) {
		JsonObject object = json.getAsJsonObject();
		JsonElement geoData = object.get("geolocation_data");
		IPInfo info = Environment.getEnvironment().getParser()
				.fromJson(geoData, IPInfo.class);
		info.setIp(object.get("ip_address").getAsString());
		return info;
	}

	@Override
	protected ResponseError buildError(int errorCode, InputStream content) {
		JsonObject json = Environment.getEnvironment().getReader()
				.parse(new InputStreamReader(content)).getAsJsonObject()
				.get("query_status").getAsJsonObject();
		ResponseError error = new ResponseError();
		error.setCode(errorCode);
		error.setMessage(json.get("query_status_description").getAsString());
		logger.warn("Error code:" + error.getCode());
		logger.warn("Error message:" + error.getMessage());
		return error;

	}

}
